package romao.matheus.dataglove.connection

/*
 * Created by romao on 22/04/18
 */

interface WebSocketInterface {
    fun onNewMessage(message: String)
    fun onStatusChange(status: WebSocketStatus)
}