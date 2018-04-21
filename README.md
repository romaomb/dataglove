# DataGlove - Android
This is where the fun begins!

I made this really quick to get my bachelor degree (and had to present on 04/12/2017), but now I am really looking to improve my little child.

(I'm going to write a better description here, asap)

There she is: 

![DataGlove on the table](https://image.ibb.co/cK4sjn/Data_Glove.jpg)

## Hardware

* Raspberry Pi 3 model B
* 16 MPU6050 (on GY-521 breadboard)
* RPi PowerPack V2.0 (5 V, 1.8 A)
* Litte board that I created for I2C bus
* 3D printed case
* Really dumb "Traffic Lights" board
* (Obviously not a hardware) Homemade Lycra glove

## Backend: Python

* SMBus to get sensor data
* hostapd, dnsmasq and IPTABLES to set up the Raspberry Pi as an Access Point
* Flask to run the "server-side stuff" 

## Mobile: Kotlin

* Retrofit 2 to get data
* Rajawali to make a 3D hand skeleton model
* A bunch of TextViews to make a table

Romão 
