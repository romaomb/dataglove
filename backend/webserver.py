#!/usr/bin/python
import json
import numpy as np
import threading
from dataglove import MPU6050
from autobahn.twisted.websocket import WebSocketServerProtocol, WebSocketServerFactory


gpio_array = [15, 13, 11, 33, 31, 29, 8, 37, 35, 22, 18, 16, 38, 36, 32, 12]
mpu = MPU6050()

class MyServerProtocol(WebSocketServerProtocol):

    def onConnect(self, request):
        print("Client connecting: {0}".format(request.peer))

    def onOpen(self):
        print("WebSocket connection open.")

    def onMessage(self, payload, isBinary):
        angles = mpu.get_angles()
        data = {"sensors":[{'gpio':gpio_array[i],'angle':np.round(angles[i])} for i in range(0, len(gpio_array))]} 
        payload = json.dumps(data, separators=(',', ':'), ensure_ascii = False).encode('utf8')
        self.sendMessage(payload, isBinary = False)
        mpu.set_complementary_angles()

    def onClose(self, wasClean, code, reason):
        print("WebSocket connection closed: {0}".format(reason))


if __name__ == '__main__':
    import sys
    from twisted.python import log
    from twisted.internet import reactor
    
    mpu.setup_gpio()
    mpu.calibration()
    mpu.set_complementary_angles()

    log.startLogging(sys.stdout)
    factory = WebSocketServerFactory(u"ws://10.1.1.1:5000")
    factory.protocol = MyServerProtocol
    reactor.listenTCP(5000, factory)
    reactor.run()