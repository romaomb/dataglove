package romao.matheus.dataglove.connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import romao.matheus.dataglove.interfaces.RetrofitService

/*
 * Created by romao on 16/03/18.
 */

class RetrofitConfig {
    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun getRetrofitInstance(baseUrl: String): RetrofitService {
        return getRetrofit(baseUrl).create(RetrofitService::class.java)
    }
}