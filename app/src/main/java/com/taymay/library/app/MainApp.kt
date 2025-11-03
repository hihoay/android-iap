package com.taymay.library.app

import android.app.Application
import taymay.firebase.utils.setupFirebase

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupFirebase(this, true) {

        }
    }
}