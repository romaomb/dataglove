package romao.matheus.dataglove.connection

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import romao.matheus.dataglove.interfaces.RetrofitService

/*
 * Created by romao on 16/03/18.
 */

class RetrofitConfig {
    private fun getRetrofit(baseUrl: String): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    fun getRetrofitInstance(baseUrl: String): RetrofitService {
        return getRetrofit(baseUrl).create(RetrofitService::class.java)
    }
}