LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := essentia
LOCAL_SRC_FILES := $(LOCAL_PATH)/$(TARGET_ARCH_ABI)/libessentia.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include\
 $(LOCAL_PATH)/essentia/include/extractor_music
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := MusicExtractor
LOCAL_SRC_FILES := $(LOCAL_PATH)/essentia/include/extractor_music/MusicExtractor.cpp\
$(LOCAL_PATH)/essentia/include/extractor_music/MusicLowlevelDescriptors.cpp\
$(LOCAL_PATH)/essentia/include/extractor_music/MusicRhythmDescriptors.cpp\
$(LOCAL_PATH)/essentia/include/extractor_music/MusicTonalDescriptors.cpp
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include\
 $(LOCAL_PATH)/essentia/include/extractor_music
LOCAL_SHARED_LIBRARIES := essentia
LOCAL_LDLIBS    := -llog
LOCAL_CPPFLAGS  := -frtti
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := audio_analysis
LOCAL_SRC_FILES := $(LOCAL_PATH)/essentia/audio_analysis.cpp
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/essentia/include\
 $(LOCAL_PATH)/essentia/include/extractor_music
LOCAL_SHARED_LIBRARIES := essentia \
MusicExtractor
LOCAL_LDLIBS    := -llog
LOCAL_CPPFLAGS  := -frtti
include $(BUILD_SHARED_LIBRARY)