package romao.matheus.dataglove.scenes.simulation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_simulation.view.*
import org.rajawali3d.view.ISurface
import romao.matheus.dataglove.R
import romao.matheus.dataglove.connection.WebSocketClient
import romao.matheus.dataglove.connection.WebSocketInterface
import romao.matheus.dataglove.connection.WebSocketStatus
import romao.matheus.dataglove.model.SensorList
import romao.matheus.dataglove.scenes.table.TableFragment
import romao.matheus.dataglove.utilities.RajawaliRenderer


class SimulationFragment : Fragment(), WebSocketInterface {

    companion object {
        var sensorList = SensorList(ArrayList())
    }

    private var hasCreatedMenu = false
    private var isFirstClick = false
    private var fromConnectOptions = false
    private var fromDisconnectOptions = false
    private var clientStatus: WebSocketStatus? = null
    private var client = WebSocketClient("http://10.1.1.1:5000")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val mFrameLayout = inflater.inflate(R.layout.fragment_simulation, container, false) as FrameLayout
        mFrameLayout.rajawaliSV.apply {
            setFrameRate(60.0)
            renderMode = ISurface.RENDERMODE_WHEN_DIRTY
            val renderer = RajawaliRenderer(context, this)
            setSurfaceRenderer(renderer)
        }
        return mFrameLayout
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (hasCreatedMenu) {
            if (isFirstClick) {
                isFirstClick = false
                if (clientStatus == WebSocketStatus.CONNECTED) {
                    menu!!.findItem(R.id.ativado).isChecked = true
                } else {
                    menu!!.findItem(R.id.pausado).isChecked = true
                }
            } else if (clientStatus == WebSocketStatus.CONNECTED) {
                menu!!.findItem(R.id.ativado).isChecked = true
            } else {
                menu!!.findItem(R.id.pausado).isChecked = true
            }
        } else {
            hasCreatedMenu = true
            isFirstClick = true
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ativado -> {
                if (!item.isChecked) {
                    fromConnectOptions = true
                    client.connect(this)
                }
                return true
            }
            R.id.pausado -> {
                if (!item.isChecked) {
                    fromDisconnectOptions = true
                    client.disconnect()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        client.connect(this)
    }

    override fun onPause() {
        super.onPause()
        client.disconnect()
        client.resetHandlers()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun requestNewData() {
        client.sendMessage("")
    }

    override fun onNewMessage(message: String) {
        sensorList = GsonBuilder().create().fromJson(message, SensorList::class.java)
        TableFragment.tableList.forEachIndexed { index, it ->
            it.text = sensorList.sensors[index].angle.toString()
        }
        requestNewData()
    }

    override fun onStatusChange(status: WebSocketStatus) {
        clientStatus = status
        if (status == WebSocketStatus.CONNECTED) {
            if (fromConnectOptions) fromConnectOptions = false
            showToast("Conectado com sucesso!")
            requestNewData()
        } else if (status == WebSocketStatus.DISCONNECTED) {
            when {
                isFirstClick -> showToast("DataGlove não encontrada...")
                fromConnectOptions -> {
                    fromConnectOptions = false
                    showToast("DataGlove não encontrada...")
                }
                fromDisconnectOptions -> {
                    fromDisconnectOptions = false
                    showToast("Pausado com sucesso!")
                    client.resetHandlers()
                }
                else -> showToast("A conexão foi perdida...")
            }
        }
    }
}
