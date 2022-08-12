package com.oliverbotello.hms.peopledex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oliverbotello.hms.peopledex.utils.RequestPermissions
import android.Manifest
import android.view.Window
import android.widget.Toast
import com.oliverbotello.hms.peopledex.ui.list.ListFragment

class MainActivity : AppCompatActivity() {
    private val managerPermissions: RequestPermissions =
        RequestPermissions(this, arrayOf(Manifest.permission.CAMERA))

    /*
    * AppCompatActivity
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        managerPermissions.verifyPermissions()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frmlyt_frame, ListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestPermissions.REQUEST_PERMISSION_CODE) {
            if (!managerPermissions.verifyResult(grantResults)) {
                Toast.makeText(applicationContext, "Is necessary camera access", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }
    }
}