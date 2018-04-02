package romao.matheus.dataglove.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_simulation.view.*
import org.rajawali3d.view.ISurface
import retrofit2.Call
import retrofit2.Callback
import romao.matheus.dataglove.R
import romao.matheus.dataglove.connection.RetrofitConfig
import romao.matheus.dataglove.model.Sensor
import romao.matheus.dataglove.utilities.RajawaliRenderer

class SimulationFragment : Fragment() {

    companion object {
        var transmission = true
        var sensor = ArrayList<Sensor.SensorData>()
    }

    private var baseURL = "http://10.1.1.1:5000"

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

    private fun getSensorData() {
        val api = RetrofitConfig().getRetrofitInstance(baseURL)
        val call = api.requestDataSensor()
        call.enqueue(object : Callback<Sensor> {
            override fun onFailure(call: Call<Sensor>?, t: Throwable?) {
                showLog("Erro 1")
            }

            override fun onResponse(call: Call<Sensor>?, response: retrofit2.Response<Sensor>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        sensor = response.body()!!.data
                        TableFragment.tableList.forEachIndexed { index, it ->
                            it.text = sensor[index].angle.toString()
                        }
                        if (transmission) getSensorData()
                    } else {
                        showLog("Erro 2")
                    }
                } else {
                    showLog("Erro 3")
                }
            }
        })
    }

    private fun showLog(errorMessage: String) {
        Log.i("DATAGLOVE", errorMessage)
    }
}
