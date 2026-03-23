cmake_minimum_required(VERSION 3.18.1)
project(gkcore)

# Define o subdiretorio do xhook (que voce vai baixar via git submodule)
add_subdirectory(xhook)

add_library(gkcore SHARED gkcore.cpp tramp.S)

find_library(log-lib log)

# Linka o xhook e a biblioteca de log do Android com o gkcore
target_link_libraries(gkcore xhook ${log-lib})
