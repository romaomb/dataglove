package romao.matheus.dataglove.core

import android.app.Application
import com.facebook.stetho.Stetho

/*
 * Created by mathe on 17/03/2018.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}