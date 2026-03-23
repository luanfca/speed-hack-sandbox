package io.github.seuuser.speedhksb

import android.app.Application

class GKApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Removido o startService daqui para evitar IllegalStateException no background.
    }
}
