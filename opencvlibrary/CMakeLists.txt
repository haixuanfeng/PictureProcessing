cmake_minimum_required(VERSION 3.4.1)
include_directories(${CMAKE_SOURCE_DIR}/../OpenCV-android-sdk/sdk/native/jni/include)
set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/../OpenCV-android-sdk/sdk/native/jni)
find_package(OpenCV 3.4 REQUIRED java)
add_library(native-lib
             SHARED
             src/main/jni/native-lib.cpp
             src/main/jni/test.cpp)
find_library( log-lib
              log )
target_link_libraries(native-lib

                       ${log-lib}
                       ${OpenCV_LIBS})
