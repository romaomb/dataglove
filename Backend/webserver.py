#!/usr/bin/python
import web
import json
from dataglove import MPU6050


urls = ('/', 'index')
gpio_array = [11, 13, 15, 29, 31, 33, 35, 37, 8, 16, 18, 22, 32, 36, 38, 12]
mpu = MPU6050()

class index:    
    def create_json(self, angles):
        data = []
        index = 0
        while index < len(gpio_array):
            data.append(str(gpio_array[index])+":"+str(angles[index])) 
            index += 1
        return json.dumps(data, sort_keys=True)

    def GET(self):
        angles = mpu.get_angles()
        return self.create_json(angles)

if __name__ == "__main__":
    mpu.setup_gpio()
    mpu.calibration()
    try:
        app = web.application(urls, globals())
        app.run()
    finally:
        mpu.safe_exit()