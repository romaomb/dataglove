package romao.matheus.dataglove.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

/*
 * Created by mathe on 17/03/2018.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        Stetho.initializeWithDefaults(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")

        @JvmStatic
        lateinit var context: Context
            private set
    }
}