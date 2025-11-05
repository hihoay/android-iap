package com.taymay.library.app

import android.app.Application
import taymay.iap.frameworks.utils.setupIAP

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupIAP(this, true) {

        }
    }
}