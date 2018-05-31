package romao.matheus.dataglove.connection

import android.os.Handler
import android.os.Message
import okhttp3.*
import java.util.concurrent.TimeUnit

/*
 * Created by romao on 22/04/18
 */

class WebSocketClient(private val mServerUrl: String) {

    private var webSocket: WebSocket? = null
    private var messageHandler: Handler? = null
    private var statusHandler: Handler? = null
    private var listener: WebSocketInterface? = null
    private val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    fun connect(listener: WebSocketInterface) {
        val request = Request.Builder()
                .url(mServerUrl)
                .build()
        webSocket = client.newWebSocket(request, SocketListener())
        this.listener = listener
        messageHandler = Handler { msg ->
            this.listener!!.onNewMessage(msg.obj as String)
            true
        }
        statusHandler = Handler { msg ->
            this.listener!!.onStatusChange(msg.obj as WebSocketStatus)
            true
        }
    }

    fun disconnect() {
        val message = statusHandler!!.obtainMessage(0, WebSocketStatus.DISCONNECTED)
        statusHandler!!.sendMessage(message)
        webSocket!!.cancel()
    }

    fun resetHandlers() {
        listener = null
        messageHandler!!.removeCallbacksAndMessages(null)
        statusHandler!!.removeCallbacksAndMessages(null)
    }

    fun sendMessage(message: String) {
        webSocket!!.send(message)
    }

    private inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            val message = statusHandler!!.obtainMessage(0, WebSocketStatus.CONNECTED)
            statusHandler!!.sendMessage(message)
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            val message = messageHandler!!.obtainMessage(0, text)
            messageHandler!!.sendMessage(message)
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
            val message = statusHandler!!.obtainMessage(0, WebSocketStatus.DISCONNECTED)
            statusHandler!!.sendMessage(message)
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            disconnect()
        }
    }
}
