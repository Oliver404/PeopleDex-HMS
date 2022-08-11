package com.oliverbotello.hms.peopledex.app

import android.app.Application
import com.huawei.hms.mlsdk.common.MLApplication
import com.oliverbotello.hms.peopledex.R

class PeopleDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MLApplication.initialize(applicationContext)

        MLApplication.getInstance().apiKey = applicationContext.getString(R.string.api_key)
    }
}