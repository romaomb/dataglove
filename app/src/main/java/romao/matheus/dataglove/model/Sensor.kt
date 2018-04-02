package romao.matheus.dataglove.model

import com.google.gson.annotations.SerializedName

/*
 * Created by romao on 16/03/18.
 */

class Sensor {

    @SerializedName("result")
    var data: ArrayList<SensorData> = ArrayList()

    inner class SensorData {
        @SerializedName("gpio")
        val gpio: Int = 0

        @SerializedName("angle")
        val angle: Double = 0.0
    }
}