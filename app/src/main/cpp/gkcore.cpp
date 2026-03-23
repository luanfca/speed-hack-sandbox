#include <jni.h>
#include <time.h>
#include <sys/time.h>
#include <android/log.h>
#include "xhook.h"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "GK", __VA_ARGS__)

static float gFactor = 1.0f;

/* ---------- stubs redirecionados ---------- */
int fake_clock_gettime(clockid_t clk, struct timespec *ts) {
    int rc = clock_gettime(clk, ts);
    if (rc == 0 && clk == CLOCK_MONOTONIC) {
        ts->tv_sec = (time_t)(ts->tv_sec * gFactor);
        ts->tv_nsec = (long)(ts->tv_nsec * gFactor);
    }
    return rc;
}

int fake_gettimeofday(struct timeval *tv, struct timezone *tz) {
    int rc = gettimeofday(tv, tz);
    if (rc == 0) {
        tv->tv_sec = (time_t)(tv->tv_sec * gFactor);
        tv->tv_usec = (suseconds_t)(tv->tv_usec * gFactor);
    }
    return rc;
}

/* ---------- JNI ---------- */
extern "C" {
JNIEXPORT void JNICALL Java_io_github_seuuser_speedhksb_core_HookInstaller_setFactor(JNIEnv*, jclass, jfloat f) {
    gFactor = f;
    LOGI("Fator de velocidade alterado para = %.2f", f);
}

JNIEXPORT jboolean JNICALL Java_io_github_seuuser_speedhksb_core_HookInstaller_isHookReady(JNIEnv*, jclass) {
    return gFactor > 0 ? JNI_TRUE : JNI_FALSE;
}
}

/* ---------- Inicializacao Unica ---------- */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    xhook_register(".*\\.so$", "clock_gettime", (void*)fake_clock_gettime, nullptr);
    xhook_register(".*\\.so$", "gettimeofday", (void*)fake_gettimeofday, nullptr);
    xhook_refresh(0);

    LOGI("xhook registrado com sucesso na inicializacao");
    return JNI_VERSION_1_6;
}
