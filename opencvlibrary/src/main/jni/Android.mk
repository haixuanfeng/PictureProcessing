LOCAL_PATH:=$(call my-dir)
include C:/Users/DYT/AndroidStudioProjects/PictureProcessingGraduationDesign/OpenCV-android-sdk/sdk/native/jni/include
include $(CLEAR_VARS)
LOCAL_MODULE:=native-lib
LOCAL_SRC_FILES:=native-lib.cpp
include $(BUILD_SHARED_LIBRARY)
