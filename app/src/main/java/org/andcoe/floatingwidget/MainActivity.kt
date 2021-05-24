package org.andcoe.floatingwidget

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DRAW_OVERLAYS_PERMISSION_REQUEST_CODE = 666
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isDrawOverlaysAllowed()) {
            requestForDrawingOverAppsPermission()
        }
        finish()
    }

    private fun isDrawOverlaysAllowed(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)

    private fun requestForDrawingOverAppsPermission() {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, DRAW_OVERLAYS_PERMISSION_REQUEST_CODE)
    }

//    private fun startFloatingWidgetMaybe() {
//        if (isDrawOverlaysAllowed()) {
//            startService(Intent(this@MainActivity, FloatingWidgetService::class.java))
//            return
//        }
//    }



}