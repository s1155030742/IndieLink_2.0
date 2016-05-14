#ifndef MUSIC_EXTRACTOR_H
#define MUSIC_EXTRACTOR_H

#include "essentia/pool.h"
#include "essentia/algorithm.h"
#include "essentia/types.h"
#include "essentia/scheduler/network.h"
#include "essentia/streaming/sourcebase.h"
#include "essentia/streaming/streamingalgorithm.h"
#include "essentia/algorithmfactory.h"
#include "essentia/streaming/algorithms/poolstorage.h"
#include "essentia/streaming/algorithms/vectorinput.h"

#include "MusicLowlevelDescriptors.h"
#include "MusicRhythmDescriptors.h"
#include "MusicTonalDescriptors.h"


#define EXTRACTOR_VERSION "music 1.0"
#define EXTRACTOR_HL_VERSION "music_highlevel 1.0"

using namespace std;
using namespace essentia;
using namespace streaming;

 class MusicExtractor{

 protected:

  Pool computeAggregation(Pool& pool);

  Real analysisSampleRate;
  Real startTime;
  Real endTime;
  bool requireMbid;
  Real indent;

  Real replayGain;
  string downmix;
  vector<standard::Algorithm*> svms;

 public:

  virtual ~MusicExtractor() {
    for (int i = 0; i < (int)svms.size(); i++) {
      if (svms[i]) {
        delete svms[i];
      }
    }
  }
  Pool results;
  Pool stats;
  Pool options;

  int compute(const string& audioFilename);
  void setExtractorOptions(const std::string& filename);
  void setExtractorDefaultOptions();
  void mergeValues(Pool &pool);
  void readMetadata(const string& audioFilename);
  void computeMetadata(const string& audioFilename);
  void computeReplayGain(const string& audioFilename);
  void computeSVMDescriptors(Pool& pool);
  void loadSVMModels();
  void outputToFile(Pool& pool, const string& outputFilename);

 };

 #endif
