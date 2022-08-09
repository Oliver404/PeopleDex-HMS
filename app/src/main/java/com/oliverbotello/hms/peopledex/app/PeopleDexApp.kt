package com.oliverbotello.hms.peopledex.app

import android.app.Application
import com.oliverbotello.hms.peopledex.R

class PeopleDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MLApplication.initialize(applicationContext)
        MLApplication.getInstance().setApiKey(applicationContext.getString(R.string.api_key))
    }
}