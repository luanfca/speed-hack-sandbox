package io.github.seuuser.speedhksb.core

object HookInstaller {
    init {
        System.loadLibrary("gkcore")
    }

    @JvmStatic
    external fun setFactor(factor: Float)

    @JvmStatic
    external fun isHookReady(): Boolean
}
