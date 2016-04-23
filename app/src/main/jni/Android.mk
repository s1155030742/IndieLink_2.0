LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := essentia
LOCAL_SRC_FILES := $(LOCAL_PATH)/$(TARGET_ARCH_ABI)/libessentia.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := audio_analysis
LOCAL_SRC_FILES := $(LOCAL_PATH)/essentia/audio_analysis.cpp
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include
FILE_LIST := $(foreach dir, $(LOCAL_EXPORT_C_INCLUDES), $(wildcard $(dir).cpp))
LOCAL_SHARED_LIBRARIES := essentia

LOCAL_LDLIBS     = -lz -lm
LOCAL_LDLIBS    := -llog
LOCAL_CPPFLAGS  := -std=c++0x -frtti

include $(BUILD_SHARED_LIBRARY)