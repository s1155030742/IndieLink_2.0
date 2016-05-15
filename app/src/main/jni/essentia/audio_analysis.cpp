

// Streaming extractor designed for analysis of music collections

#include <jni.h>
#include <cstring>
#include <essentia/streaming/algorithms/poolstorage.h>
#include <essentia/essentiautil.h>
#include "MusicExtractor.h"

using namespace std;
using namespace essentia;
using namespace essentia::streaming;
using namespace essentia::scheduler;

JNIEXPORT jint JNICALL Java_com_indielink_indielink_Audio_Audio_audio_1analysis(JNIEnv* env, jobject, jstring audioFilename,jstring outputFilename,jstring profileFilename) {


    // Returns: 1 on essentia error
    //          2 if there are no tags in the file
    int result;

    char *audioFilenameChar;
    audioFilenameChar = std::strcpy(audioFilenameChar,env->GetStringUTFChars(audioFilename, JNI_FALSE));

    char *outputFilenameChar;
    outputFilenameChar = std::strcpy(outputFilenameChar,env->GetStringUTFChars(outputFilename, JNI_FALSE));

    char *profileFilenameChar;
    profileFilenameChar = std::strcpy(profileFilenameChar,env->GetStringUTFChars(profileFilename, JNI_FALSE));

    const char *_framesChar = env->GetStringUTFChars((jstring)"_frames", JNI_FALSE);

    outputFilenameChar = std::strcat(outputFilenameChar,_framesChar);

    string audioFilenameString = std::string(audioFilenameChar);
    string outputFilenameString = std::string(outputFilenameChar);
    string profileFilenameString = std::string(profileFilenameChar);

    try {
        essentia::init();

        cout.precision(10); // TODO ????

        MusicExtractor *extractor = new MusicExtractor();

        extractor->setExtractorOptions(profileFilenameString);
        extractor->mergeValues(extractor->results);

        result = extractor->compute(audioFilenameString);

        if (result > 0) {
            cerr << "Quitting early." << endl;
        } else {
            extractor->outputToFile(extractor->stats, outputFilenameString);
            if (extractor->options.value<Real>("outputFrames")) {
                extractor->outputToFile(extractor->results, outputFilenameString);
            }
        }
        essentia::shutdown();
    }
    catch (EssentiaException& e) {
        cerr << e.what() << endl;
        return 1;
    }
    return result;
}
