# DataGlove

This project was made in three months, so I could get my bachelor degree on Computer Engineering. Now I'm looking forward to improve it. There's no stable version yet, to see the current development status go to [develop branch](https://github.com/matheusromao/dataglove-android/tree/develop).

## Abstract

The follow up of rehabilitation sessions for the range of movement of the hand is performed through a [goniometer](http://www.berktree.com/assets/images/default/stainless-steel-finger-goniometers-short-finger-goniometer-measures-3--9cm-long-model-926611.jpg). However, the use of this instrument for angular measurement of finger joints is time-consuming and subject to many errors. Therefore, this work presents a dataglove that dynamically provides the extension and flexion angles of the [metacarpophalangeal, interphalangeal proximal and distal fingers joints](http://www.assh.org/portals/1/Images/anatomy_images/Joints-Thumb-Inter.jpg?ver=2014-02-03-164205-753). Using a Raspberry Pi 3 B, 16 inertial sensors and a battery, this dataglove provides its readings via Wi-Fi, which can be viewed in a 3D hand model or in a table format through an Android application.

## Hardware

* Raspberry Pi 3 model B
* 16 MPU6050 (on GY-521 breadboard)
* RPi PowerPack V2.0 (5 V, 1.8 A)
* I2C bus board
* "Traffic Lights" board

## Glove Materials

* Lycra
* Velcro
* 3D printed case

## Backend (Python)

* [Autobahn|Python](https://github.com/crossbario/autobahn-python) to setup a WebSocket and send data from the sensors.
* [Numpy](https://github.com/numpy/numpy) to handle calculations. 
* SMBus to get MPU6050 data (credits to [Andrew Birkett](http://blog.bitify.co.uk/2013/11/reading-data-from-mpu-6050-on-raspberry.html)).
* hostapd, dnsmasq and iptables to [set up the Raspberry as an Access Point](https://github.com/raspberrypi/documentation/blob/master/configuration/wireless/access-point.md).

## Mobile (Kotlin - Android)

* [Rajawali](https://github.com/Rajawali/Rajawali) to make a 3D hand skeleton model.
* [okHttp3](https://github.com/square/okhttp) to handle WebSocket connection.
* [Gson](https://github.com/google/gson) to deserialize JSON into a data class model.
* MVP Architecture.

## TODO

- [x] Convert JAVA to Kotlin
- [x] Use ViewPager to setup fragments
- [x] Replace Volley with Retrofit (did not work really well)
- [x] Replace web.py with Flask (it was ok, but did not improved speed as expected)
- [x] Replace Flask with Autobahn|Python on Twisted
- [x] Android MVP Architecture
- [x] Replace Retrofit with okHttp3
- [x] Update Table Layout
- [ ] Fix joints rotation bug
- [ ] Fix camera rotation bug
- [ ] Make a 3D texture for the hand model

![DataGlove on the table](https://image.ibb.co/cK4sjn/Data_Glove.jpg)
