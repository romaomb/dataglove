package romao.matheus.dataglove.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_simulation.view.*
import org.rajawali3d.view.ISurface
import romao.matheus.dataglove.R
import romao.matheus.dataglove.connection.WebSocketClient
import romao.matheus.dataglove.utilities.RajawaliRenderer
import romao.matheus.dataglove.connection.WebSocketInterface
import romao.matheus.dataglove.connection.WebSocketStatus
import romao.matheus.dataglove.model.SensorList


class SimulationFragment : Fragment(), WebSocketInterface {

    companion object {
        var keepTransmission = false
        var sensorList = SensorList(ArrayList())
        var client = WebSocketClient("http://10.1.1.1:5000")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mFrameLayout = inflater.inflate(R.layout.fragment_simulation, container, false) as FrameLayout
        mFrameLayout.rajawaliSV.apply {
            setFrameRate(60.0)
            renderMode = ISurface.RENDERMODE_WHEN_DIRTY
            val renderer = RajawaliRenderer(context, this)
            setSurfaceRenderer(renderer)
        }
        return mFrameLayout
    }

    override fun onResume() {
        super.onResume()
        client.connect(this)
    }

    override fun onPause() {
        super.onPause()
        client.disconnect()
    }

    private fun showLog(errorMessage: String) {
        Log.i("DATAGLOVE", errorMessage)
    }

    private fun requestNewData() {
        if (keepTransmission) client.sendMessage("")
    }

    override fun onNewMessage(message: String) {
        sensorList = GsonBuilder().create().fromJson(message, SensorList::class.java)
        TableFragment.tableList.forEachIndexed { index, it ->
            it.text = sensorList.sensors[index].angle.toString()
        }
        requestNewData()
    }

    override fun onStatusChange(status: WebSocketStatus) {
        if (status === WebSocketStatus.CONNECTED) {
            showLog("Conectado")
            keepTransmission = true
            requestNewData()
        }
        else showLog("Desconectado")
    }
}
