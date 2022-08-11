package com.oliverbotello.hms.peopledex.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class RequestPermissions(private val activity: Activity, private val permissions: Array<String>) {
    companion object {
        const val REQUEST_PERMISSION_CODE: Int = 101;
    }

    private val missedPermissions: ArrayList<String> = arrayListOf()

    fun verifyPermissions() {
        for (permission in permissions) {
            if (
                ActivityCompat.checkSelfPermission(this.activity.applicationContext, permission)
                != PackageManager.PERMISSION_GRANTED
            )
                missedPermissions.add(permission)
        }

        if (missedPermissions.size > 0)
            requestPermission(missedPermissions.toTypedArray())
    }

    private fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE)
    }

    fun verifyResult(grantResults: IntArray): Boolean {
        return grantResults.size == missedPermissions.size &&
                grantResults.none { result -> result != PackageManager.PERMISSION_GRANTED }
    }
}