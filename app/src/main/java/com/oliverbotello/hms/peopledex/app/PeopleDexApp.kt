package com.oliverbotello.hms.peopledex.app

import android.app.Application
import android.util.Log
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication
import com.oliverbotello.hms.peopledex.PicturesHelper
import com.oliverbotello.hms.peopledex.R
import com.oliverbotello.hms.peopledex.model.PeopleDexConnection

class PeopleDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PeopleDexConnection.initConnection(applicationContext)
        PicturesHelper.initialize(applicationContext)
        MLApplication.initialize(applicationContext)

        MLApplication.getInstance().apiKey = applicationContext.getString(R.string.api_key)
    }
}