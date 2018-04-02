package romao.matheus.dataglove.interfaces

import retrofit2.Call
import retrofit2.http.GET
import romao.matheus.dataglove.model.Sensor

/*
 * Created by romao on 16/03/18.
 */

interface RetrofitService {
    @GET(" ")
    fun requestDataSensor(): Call<Sensor>
}