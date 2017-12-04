#!/usr/bin/python
import smbus
import math
import RPi.GPIO as GPIO
import time
import os 


class MPU6050:
    def __init__(self):
        power_mgmt_1 = 0x6b
        self.low_address = 0x68
        self.acc_scale = 16384.0
        self.acc_g = 16384
        self.data_address = 0x3b
        self.bytes = 14
        self.gyro_scale = 131.0
        self.gpio_leds = [19, 21]
        self.gpio_array = [11, 13, 15, 29, 31, 33, 35, 37, 8, 16, 18, 22, 32, 36, 38, 12]
        self.gpio_array_qtd = len(self.gpio_array)
        self.gyro_offset = [[0, 0, 0]] * self.gpio_array_qtd
        self.acc_offset = [[0, 0, 0]] * self.gpio_array_qtd
        self.filtered_angles = [0] * self.gpio_array_qtd
        self.alpha = 0.98
        self.alpha_compliment = 1 - self.alpha
        self.bus = smbus.SMBus(1)
        self.bus.write_byte_data(self.low_address, power_mgmt_1, 0)
        self.cont = 0
        self.begin = 0
        self.end = 0

    def read_everything(self):
        raw_data = self.bus.read_i2c_block_data(self.low_address, self.data_address, self.bytes)
        acc_data = [self.twos_compliment((raw_data[0] << 8) + raw_data[1]),
                    self.twos_compliment((raw_data[2] << 8) + raw_data[3]),
                    self.twos_compliment((raw_data[4] << 8) + raw_data[5])]
        gyro_data = [self.twos_compliment((raw_data[8] << 8) + raw_data[9]),
                     self.twos_compliment((raw_data[10] << 8) + raw_data[11]),
                     self.twos_compliment((raw_data[12] << 8) + raw_data[13])]
        return (acc_data, gyro_data)
       
    def twos_compliment(self, val):
        if (val >= 0x8000):
            return -((65535 - val) + 1)
        else:
            return val

    def distance(self, a,b):
        return math.sqrt((a*a)+(b*b))

    def get_y_rotation(self, val):
        radians = math.atan2(val[0], self.distance(val[1],val[2]))
        return -math.degrees(radians)

    def get_data(self, index):
        (acc_raw, gyro_raw) = self.read_everything() 
        acc_data = [(acc_raw[0] - self.acc_offset[index][0])/self.acc_scale, 
                    (acc_raw[1] - self.acc_offset[index][1])/self.acc_scale, 
                    (acc_raw[2] - self.acc_offset[index][2])/self.acc_scale]
        gyro_data = [(gyro_raw[0] - self.gyro_offset[index][0])/self.gyro_scale, 
                     (gyro_raw[1] - self.gyro_offset[index][1])/self.gyro_scale, 
                     (gyro_raw[2] - self.gyro_offset[index][2])/self.gyro_scale]
        return (acc_data, gyro_data)

    def select_mpu(self, gpio_pin):
        GPIO.output(self.gpio_array, GPIO.HIGH)
        GPIO.output(gpio_pin, GPIO.LOW)

    def calibration(self):
        GPIO.output(self.gpio_leds[0], GPIO.HIGH)
        #cont = 0
        self.begin=time.time()
        while (time.time() - self.begin) < 3:
            index = 0
            while index < self.gpio_array_qtd:
                self.select_mpu(self.gpio_array[index])
                (acc_raw, gyro_raw) = self.read_everything() 
                self.acc_offset[index] = [acc_raw[0] + self.acc_offset[index][0], 
                                          acc_raw[1] + self.acc_offset[index][1], 
                                          acc_raw[2] + self.acc_offset[index][2]]
                self.gyro_offset[index] = [gyro_raw[0] + self.gyro_offset[index][0], 
                                           gyro_raw[1] + self.gyro_offset[index][1], 
                                           gyro_raw[2] + self.gyro_offset[index][2]]
                index+=1
            self.cont += 1
        self.end = time.time()
        index=0
        while index < self.gpio_array_qtd:
            self.gyro_offset[index] = [self.gyro_offset[index][0] / self.cont, 
                                       self.gyro_offset[index][1] / self.cont, 
                                       self.gyro_offset[index][2] / self.cont]
            self.acc_offset[index] = [self.acc_offset[index][0] / self.cont, 
                                      self.acc_offset[index][1] / self.cont, 
                                      self.acc_offset[index][2] / self.cont]
            self.acc_offset[index][2] -= self.acc_g
            index+=1
        GPIO.output(self.gpio_leds[1], GPIO.HIGH) #green

    def set_complementary_angles(self):
        index = 0
        while index < self.gpio_array_qtd:
            self.select_mpu(self.gpio_array[index])
            begin = time.time()
            (acc_data, gyro_data) = self.get_data(index) 
            dt = time.time() - begin
            acc_y_angle = self.get_y_rotation(acc_data)
            gyro_delta_y = gyro_data[1] * dt
            self.filtered_angles[index] = (self.alpha *(acc_y_angle + gyro_delta_y)) + (self.alpha_compliment * acc_y_angle)
            index += 1

    def get_angles(self):
        self.set_complementary_angles()
        return self.filtered_angles

    def setup_gpio(self):
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(self.gpio_leds, GPIO.OUT)
        GPIO.setup(self.gpio_array, GPIO.OUT)

    def get_cont(self):
        return self.cont

    def start(self):
        self.setup_gpio()
        self.calibration()

    def get_time_calibration(self):
        return (self.end - self.begin)

    def sensor_test(self, sensor_number):
        self.select_mpu(self.gpio_array[sensor_number])
        begin = time.time()
        (acc_data, gyro_data) = self.get_data(sensor_number)
        dt = time.time() - begin
        acc_y_angle = self.get_y_rotation(acc_data)
        gyro_delta_y = gyro_data[1] * dt
        angle = (self.alpha *(acc_y_angle + gyro_delta_y)) + (self.alpha_compliment * acc_y_angle)
        return angle
