package io.github.seuuser.speedhksb

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val overlayReqCode = 1001
    private val notifReqCode = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissionsAndStart()
    }

    private fun checkPermissionsAndStart() {
        // 1. Checa permissao de notificacao (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), notifReqCode)
                return
            }
        }

        // 2. Checa permissao de sobreposicao (Android 6+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, overlayReqCode)
            return
        }

        startGKService()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notifReqCode) {
            checkPermissionsAndStart()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == overlayReqCode) {
            checkPermissionsAndStart()
        }
    }

    private fun startGKService() {
        val intent = Intent(this, FloatService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        finish()
    }
}
