package io.github.seuuser.speedhksb

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.core.app.NotificationCompat
import io.github.seuuser.speedhksb.core.HookInstaller

class FloatService : Service() {
    private lateinit var wm: WindowManager
    private var windowView: View? = null
    private val channelId = "gk_ch"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1,
                buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(1, buildNotification())
        }

        createFloatView()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "GK Overlay",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun createFloatView() {
        windowView = LayoutInflater.from(this).inflate(R.layout.float_panel, null)
        val seek = windowView!!.findViewById<SeekBar>(R.id.seekSpeed)
        seek.progress = 10

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar, p: Int, f: Boolean) {
                HookInstaller.setFactor((p + 10) / 10f)
            }

            override fun onStartTrackingTouch(s: SeekBar) {}

            override fun onStopTrackingTouch(s: SeekBar) {}
        })

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 300
        wm.addView(windowView, params)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("SpeedHack SB")
            .setContentText("Acelerador ativo")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowView?.let { wm.removeView(it) }
    }
}
