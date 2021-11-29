package ro.kazy.tcpclient

import android.app.Application
import android.content.Context

/**
 * Created by catalin on 29/11/2021
 */
class ApplicationProvider : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        var context: Context? = null
            private set
    }
}