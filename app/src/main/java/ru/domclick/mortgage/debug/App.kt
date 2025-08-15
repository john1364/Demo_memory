package ru.domclick.mortgage.debug

import android.app.Application
import ru.dgis.sdk.DGis

class App : Application() {

    var gisContext: ru.dgis.sdk.Context? = null

    override fun onCreate() {
        super.onCreate()

        gisContext = DGis.initialize(
            appContext = this,
            keySource = dGisKeySource
        )
    }
}