cmake_minimum_required(VERSION 3.18.1)
project(gkcore)

# xHook upstream usa Android.mk; aqui expomos a biblioteca via CMake.
add_library(
    xhook
    SHARED
    xhook/libxhook/jni/xhook.c
    xhook/libxhook/jni/xh_core.c
    xhook/libxhook/jni/xh_elf.c
    xhook/libxhook/jni/xh_jni.c
    xhook/libxhook/jni/xh_log.c
    xhook/libxhook/jni/xh_util.c
    xhook/libxhook/jni/xh_version.c
)

add_library(gkcore SHARED gkcore.cpp tramp.S)

find_library(log-lib log)

target_include_directories(
    xhook
    PUBLIC
    ${CMAKE_CURRENT_SOURCE_DIR}/xhook/libxhook/jni
)

target_compile_options(
    xhook
    PRIVATE
    -Wall
    -Wextra
    -Werror
    -fvisibility=hidden
    -std=c11
)

# Linka o xhook e a biblioteca de log do Android com o gkcore
target_link_libraries(xhook ${log-lib})
target_link_libraries(gkcore xhook ${log-lib})
