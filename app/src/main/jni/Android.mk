LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := essentia
LOCAL_SRC_FILES := $(LOCAL_PATH)/essentia/lib/libessentia.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include/essentia
include $(PREBUILT_SHARED_LIBRARY)
