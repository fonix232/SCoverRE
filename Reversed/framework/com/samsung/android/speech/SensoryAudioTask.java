package com.samsung.android.speech;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.samsung.android.speech.SemSpeechRecognizer.ResultListener;
import com.sensoryinc.fluentsoftsdk.SensoryBargeInEngine;
import com.sensoryinc.fluentsoftsdk.SensoryBargeInEngineWrapper;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

class SensoryAudioTask extends AudioTask implements Runnable {
    static final int DEFAULT_BLOCK_SIZE = 160;
    private int AUDIO_START = 0;
    private final int RECOGNITION_WAIT_TIME = 100;
    private String TAG = SensoryAudioTask.class.getSimpleName();
    public double THscore = -1.5d;
    private MMUIRecognizer aMMUIRecognizer = null;
    private SensoryBargeInEngine aSensoryBargeInEngine = null;
    private String acousticModelPathname = (Config.GetSensoryAM(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String acousticModelPathname_sub = (Config.GetSensoryAM(0, 2) + Config.SENSORY_SUB_SUFFIX);
    public int block_size = 0;
    public byte[] buf;
    public long consoleInitReturn = -1;
    public long consoleInitReturn_sub = -1;
    public String defaultloadNameList = Config.GetSamsungNameList(0);
    public boolean done = false;
    private int dualThresholdFlag = 0;
    public File f24f = null;
    private Handler handler = new C02591();
    public boolean isCameraBargeIn = false;
    public boolean isCancelBargeIn = false;
    public boolean isEnableSamsungOOVResult = false;
    private boolean isMakePCM = false;
    public boolean isSASRInitProblem = false;
    public boolean isSensoryBargeInEnable = false;
    public boolean isSensoryCameraBargeIn = false;
    public boolean isSensoryResult = false;
    public boolean isSubModelBargeInEnable = false;
    public String loadNameList = Config.GetSamsungNameList(0);
    public String loadPath = null;
    public int mCommandType = 0;
    public DataOutputStream mDataOutputStream = null;
    public int mEmbeddedEngineLanguage = 1;
    public int mLanguage = 1;
    public Handler mStopHandler = null;
    private ResultListener m_listener = null;
    public String modelPath = (Config.GetSamsungPath(1) + "param");
    public int numRecogResult = 0;
    public LinkedBlockingQueue<short[]> f25q = null;
    private int readNshorts = -1;
    private int recogAfterReadCount = 0;
    public int resultSASRInit = 0;
    public int resultSASRLoadModel = 0;
    private String searchGrammarPathname = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String searchGrammarPathname_sub = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_SUB_SUFFIX);
    public float sensoryCMscore = 0.0f;
    public float sensoryChineseCaptureCMTH = 800.0f;
    public float sensoryChineseStopCMTH = 130.0f;
    public float sensoryJapaneseShootCMTH = 400.0f;
    public float sensoryKoreanCancelCMTH = 150.0f;
    public float sensoryKoreanRejectCMTH = 100.0f;
    public float sensoryKoreanShootCMTH = 400.0f;
    public float sensoryKoreanStopCMTH = 150.0f;
    public float sensoryRussianCheeseCMTH = 300.0f;
    public float sensoryUKEnglishStopCMTH = 400.0f;
    public float sensoryUSEnglishCaptureCMTH = 450.0f;
    public float sensoryUSEnglishCheeseCMTH = 400.0f;
    public float sensoryUSEnglishRecordVideoCMTH = 250.0f;
    public float sensoryUSEnglishShootCMTH = 150.0f;
    public float sensoryUSEnglishSnoozeCMTH = 100.0f;
    public float sensoryUSEnglishStopCMTH = 400.0f;
    public short[] speech = null;
    private int totalReadCount = 0;
    public String wordListPath = Config.GetSamsungPath(1);

    class C02591 extends Handler {
        C02591() {
        }

        public void handleMessage(Message message) {
            String[] stringArray = message.getData().getStringArray("recognition_result");
            if (SensoryAudioTask.this.m_listener != null) {
                SensoryAudioTask.this.m_listener.onResults(stringArray);
            }
        }
    }

    SensoryAudioTask(ResultListener resultListener, String str, int i, int i2, boolean z) {
        super(resultListener, str, i, i2, z);
        Log.i(this.TAG, "super()");
        init(new LinkedBlockingQueue(), 160, resultListener, str, i, i2, z);
    }

    private void SendHandlerMessage(String[] strArr) {
        Message obtainMessage = this.handler.obtainMessage();
        BaseBundle bundle = new Bundle();
        bundle.putStringArray("recognition_result", strArr);
        obtainMessage.setData(bundle);
        try {
            this.handler.sendMessage(obtainMessage);
        } catch (Throwable e) {
            Log.e(this.TAG, "IllegalStateException " + e.getMessage());
            stop();
        }
    }

    private int getMMUIRecognitionResult(short[] sArr, int i) {
        int i2 = 0;
        if (this.aMMUIRecognizer != null) {
            i2 = this.aMMUIRecognizer.RECThread(sArr);
        }
        if (i2 == -2) {
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section1");
                return -1;
            } else if (this.aMMUIRecognizer != null) {
                Log.d(this.TAG, "Barge-in : Too long input so Reset");
                this.aMMUIRecognizer.ResetFx();
                this.aMMUIRecognizer.SASRReset();
            }
        }
        if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section2");
            return -1;
        }
        if (i2 == 2 && this.aMMUIRecognizer != null) {
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section3");
                return -1;
            }
            this.aMMUIRecognizer.ResetFx();
            this.numRecogResult = this.aMMUIRecognizer.SASRDoRecognition(this.cmResult, this.strResult, "/system/voicebargeindata/sasr/input.txt", this.BargeinAct, this.utfResult);
            this.strResult[0] = this.strResult[0].replace('_', ' ');
            if (this.mEmbeddedEngineLanguage == 0 || this.mEmbeddedEngineLanguage == 2) {
                this.utfResult[0] = this.utfResult[0].replace('_', ' ');
                this.strResult[0] = this.utfResult[0];
            }
            Log.i(this.TAG, "numResult[0] : " + this.cmResult[0]);
            Log.i(this.TAG, "strResult[0] : " + this.strResult[0]);
            Log.i(this.TAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            if (this.mCommandType == 3 && this.BargeinAct[0] == (short) 2) {
                this.THscore = -1.8d;
            } else if (this.mCommandType == 7) {
                this.THscore = -1.0d;
            } else {
                this.THscore = -1.5d;
            }
            Log.i(this.TAG, "THscore : " + this.THscore);
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section4");
                return -1;
            }
            if (this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) {
                if (this.isSensoryResult) {
                    Log.i(this.TAG, "isSensoryCameraBargeIn is true and isSensoryResult is true");
                    Log.d(this.TAG, "EmbeddedEngine Recognizer : " + this.BargeinAct[0]);
                    this.isSensoryResult = false;
                    Log.i(this.TAG, "Set isSensoryResult = false. So isSensoryResult : " + this.isSensoryResult);
                } else {
                    Log.i(this.TAG, "isSensoryCameraBargeIn is true and keyword is not detected by sensory and keyword or non-keyword is detected by embeddedEngine.");
                    this.strResult[0] = "TH-Reject";
                    this.BargeinAct[0] = (short) -1;
                    SendHandlerMessage(this.strResult);
                }
            } else if (((double) this.cmResult[0]) > this.THscore) {
                SendHandlerMessage(this.strResult);
            } else {
                this.strResult[0] = "TH-Reject";
                this.BargeinAct[0] = (short) -1;
                SendHandlerMessage(this.strResult);
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section13");
                return -1;
            }
            this.aMMUIRecognizer.SASRReset();
        }
        return i;
    }

    private boolean getSensoryRecognitionResult(long j, short[] sArr) {
        float[] fArr = new float[3];
        if (this.dualThresholdFlag == -1) {
            fArr[1] = -1.0f;
        } else {
            fArr[1] = (float) this.dualThresholdFlag;
        }
        String phrasespotPipe = this.aSensoryBargeInEngine.phrasespotPipe(j, sArr, 160, 16000, fArr);
        this.dualThresholdFlag = (int) fArr[1];
        if (phrasespotPipe != null) {
            this.BargeinAct[0] = (short) getSensoryBargeInAct(this.mCommandType, phrasespotPipe);
            this.strResult[0] = phrasespotPipe;
            float f = fArr[0];
            Log.i(this.TAG, "consoleResult : " + phrasespotPipe);
            Log.d(this.TAG, "strResult[0] : " + this.strResult[0]);
            Log.d(this.TAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            Log.i(this.TAG, "sensoryCMscore : " + f);
            Log.i(this.TAG, "dualThresholdFlag = " + this.dualThresholdFlag);
            if (!resultSensoryOOV(j, this.BargeinAct[0], f)) {
                if (!this.isSensoryCameraBargeIn) {
                    SendHandlerMessage(this.strResult);
                    return true;
                } else if (this.recogAfterReadCount == 0) {
                    this.recogAfterReadCount = 1;
                    SendHandlerMessage(this.strResult);
                    if (this.isEnableSamsungOOVResult) {
                        this.isSensoryResult = true;
                        Log.i(this.TAG, "Set isSensoryResult = true. So isSensoryResult : " + this.isSensoryResult);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean resultSensoryOOV(long j, int i, float f) {
        if (this.mCommandType != 2) {
            if (this.mCommandType != 7) {
                if (this.mCommandType != 3) {
                    if (this.mCommandType == 9) {
                        switch (this.mLanguage) {
                            case 0:
                                if (i == 1 && f < this.sensoryKoreanCancelCMTH) {
                                    Log.i(this.TAG, "Korean cancel score is low. So rejected");
                                    return true;
                                }
                            default:
                                break;
                        }
                    }
                }
                switch (this.mLanguage) {
                    case 0:
                        if (i == 1 && f < this.sensoryKoreanStopCMTH) {
                            Log.i(this.TAG, "Korean stop score is low. So rejected");
                            return true;
                        }
                    case 1:
                        if (i == 1 && f < this.sensoryUSEnglishStopCMTH) {
                            Log.i(this.TAG, "US English stop score is low. So rejected");
                            return true;
                        } else if (i == 2 && f < this.sensoryUSEnglishSnoozeCMTH) {
                            Log.i(this.TAG, "US English snooze score is low. So rejected");
                            return true;
                        }
                        break;
                    case 2:
                        if (i == 1 && f < this.sensoryChineseStopCMTH) {
                            Log.i(this.TAG, "Chinese stop score is low. So rejected");
                            return true;
                        }
                    case 10:
                        if (i == 1 && f < this.sensoryUKEnglishStopCMTH) {
                            Log.i(this.TAG, "UK English stop score is low. So rejected");
                            return true;
                        }
                    default:
                        break;
                }
            } else if (this.dualThresholdFlag != 1) {
                switch (this.mLanguage) {
                    case 1:
                    case 10:
                        if (this.isSubModelBargeInEnable && j == this.consoleInitReturn_sub) {
                            if (i == 2 && f < this.sensoryUSEnglishCheeseCMTH) {
                                Log.i(this.TAG, "Sub English cheese score is low. So rejected");
                                return true;
                            } else if (i == 3 && f < this.sensoryUSEnglishCaptureCMTH) {
                                Log.i(this.TAG, "Sub English capture score is low. So rejected");
                                return true;
                            } else if (i == 4 && f < this.sensoryUSEnglishShootCMTH) {
                                Log.i(this.TAG, "Sub English shoot score is low. So rejected");
                                return true;
                            } else if (i == 5 && f < this.sensoryUSEnglishRecordVideoCMTH) {
                                Log.i(this.TAG, "Sub English record video score is low. So rejected");
                                return true;
                            }
                        }
                        break;
                    case 2:
                        if (i == 3 && f < this.sensoryChineseCaptureCMTH) {
                            Log.i(this.TAG, "Chinese capture score is low. So rejected");
                            return true;
                        }
                    case 7:
                        if (i == 4 && f < this.sensoryJapaneseShootCMTH) {
                            Log.i(this.TAG, "Japanese shoot score is low. So rejected");
                            return true;
                        }
                    case 8:
                        if (i == 2 && f < this.sensoryRussianCheeseCMTH) {
                            Log.e(this.TAG, "Russian cheese score is low. So rejected");
                            return true;
                        }
                    default:
                        break;
                }
            } else {
                switch (this.mLanguage) {
                    case 0:
                        if (i == 3 && f < 300.0f) {
                            Log.i(this.TAG, "Korean capture score is low. So rejected");
                            return true;
                        } else if (i == 2 && f < 1200.0f) {
                            Log.i(this.TAG, "Korean cheese score is low. So rejected");
                            return true;
                        } else if (i == 4 && f < 400.0f) {
                            Log.i(this.TAG, "Korean shoot score is low. So rejected");
                            return true;
                        } else if (i == 5 && f < 800.0f) {
                            Log.i(this.TAG, "Korean record video score is low. So rejected");
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        switch (this.mLanguage) {
            case 0:
                if (i == 2 && f < this.sensoryKoreanRejectCMTH) {
                    Log.i(this.TAG, "Korean reject score is low. So rejected");
                    return true;
                }
        }
        return false;
    }

    public static short twoBytesToShort(byte b, byte b2) {
        return (short) ((b & 255) | (b2 << 8));
    }

    public int getSensoryBargeInAct(int i, String str) {
        switch (i) {
            case 0:
                if (str.startsWith("stop")) {
                    return 1;
                }
                break;
            case 1:
                if (str.startsWith("next")) {
                    return 1;
                }
                if (str.startsWith("previous")) {
                    return 2;
                }
                break;
            case 2:
                if (str.startsWith("answer")) {
                    return 1;
                }
                if (str.startsWith("reject")) {
                    return 2;
                }
                break;
            case 3:
                if (str.startsWith("stop")) {
                    return 1;
                }
                if (str.startsWith("snooze")) {
                    return 2;
                }
                break;
            case 4:
            case 5:
            case 6:
                if (str.startsWith("next")) {
                    return 1;
                }
                if (str.startsWith("previous")) {
                    return 2;
                }
                if (str.startsWith("pause")) {
                    return 3;
                }
                if (str.startsWith("play")) {
                    return 4;
                }
                if (str.startsWith("volume up") || str.startsWith("volume_up") || str.startsWith("volumeup")) {
                    return 5;
                }
                if (str.startsWith("volume down") || str.startsWith("volume_down") || str.startsWith("volumedown")) {
                    return 6;
                }
                break;
            case 7:
                if (str.startsWith("smile")) {
                    return 1;
                }
                if (str.startsWith("cheese")) {
                    return 2;
                }
                if (str.startsWith("capture")) {
                    return 3;
                }
                if (str.startsWith("shoot")) {
                    return 4;
                }
                if (str.startsWith("record video") || str.startsWith("record_video") || str.startsWith("recordvideo")) {
                    return 5;
                }
                if (str.startsWith("auto settings") || str.startsWith("auto_settings") || str.startsWith("autosettings")) {
                    return 6;
                }
                if (str.startsWith("beauty face") || str.startsWith("beauty_face") || str.startsWith("beautyface")) {
                    return 7;
                }
                if (str.startsWith("timer")) {
                    return 8;
                }
                if (str.startsWith("zoom in") || str.startsWith("zoom_in") || str.startsWith("zoomin")) {
                    return 9;
                }
                if (str.startsWith("zoom out") || str.startsWith("zoom_out") || str.startsWith("zoomout")) {
                    return 10;
                }
                if (str.startsWith("flash on") || str.startsWith("flash_on") || str.startsWith("flashon")) {
                    return 11;
                }
                if (str.startsWith("flash off") || str.startsWith("flash_off") || str.startsWith("flashoff")) {
                    return 12;
                }
                if (str.startsWith("upload pics") || str.startsWith("upload_pics") || str.startsWith("uploadpics")) {
                    return 13;
                }
                if (str.startsWith("gallery")) {
                    return 14;
                }
                break;
            case 8:
                if (str.startsWith("buddy photo share") || str.startsWith("buddy_photo_share") || str.startsWith("buddyphotoshare")) {
                    return 1;
                }
                if (str.startsWith("next")) {
                    return 2;
                }
                if (str.startsWith("previous")) {
                    return 3;
                }
                if (str.startsWith("play")) {
                    return 4;
                }
                if (str.startsWith("slideshow")) {
                    return 5;
                }
                if (str.startsWith("stop")) {
                    return 6;
                }
                if (str.startsWith("camera")) {
                    return 7;
                }
                break;
            case 9:
                if (str.startsWith("cancel")) {
                    return 1;
                }
                break;
            case 10:
                if (str.startsWith("yes")) {
                    return 1;
                }
                if (str.startsWith("no")) {
                    return 2;
                }
                break;
        }
        return -1;
    }

    void init(LinkedBlockingQueue<short[]> linkedBlockingQueue, int i, ResultListener resultListener, String str, int i2, int i3, boolean z) {
        this.TAG = SensoryAudioTask.class.getSimpleName();
        Log.i(this.TAG, "SensoryAudioTask init()");
        Log.i(this.TAG, "command : " + i2);
        Log.i(this.TAG, "Language : " + i3);
        this.done = false;
        this.f25q = linkedBlockingQueue;
        this.block_size = i;
        this.mCommandType = i2;
        this.rec = null;
        this.m_listener = resultListener;
        this.loadPath = str;
        this.mLanguage = i3;
        this.BargeinAct[0] = (short) -1;
        if (i2 == 7 && i3 == 0) {
            this.dualThresholdFlag = -1;
        }
        setSensoryFilePath(i3, i2);
        this.isEnableSamsungOOVResult = z;
        this.speech = new short[160];
        Log.i(this.TAG, "isSensoryBargeInEnable : " + this.isSensoryBargeInEnable);
        Log.i(this.TAG, "isEnableSamsungOOVResult : " + this.isEnableSamsungOOVResult);
        this.totalReadCount = 0;
        this.recogAfterReadCount = 0;
        if (this.isMakePCM) {
            this.f24f = new File("/sdcard/", "testPCM.pcm");
            try {
                this.mDataOutputStream = new DataOutputStream(new FileOutputStream(this.f24f, true));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (this.isCameraBargeIn || this.isCancelBargeIn) {
            this.AUDIO_START = 0;
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_VOICE_RECOGNITION);
            if (this.rec != null) {
                Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_VOICE_RECOGNITION);
            }
        } else if (this.isSensoryBargeInEnable) {
            this.AUDIO_START = 50;
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_BARGE_IN_SENSORY);
            if (this.rec != null) {
                Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_BARGE_IN_SENSORY);
            }
        }
        if (this.rec == null) {
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_BARGE_IN);
            Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_BARGE_IN);
        }
        if (this.isSensoryBargeInEnable) {
            this.aSensoryBargeInEngine = SensoryBargeInEngineWrapper.getInstance();
            if (this.aSensoryBargeInEngine != null) {
                this.consoleInitReturn = this.aSensoryBargeInEngine.phrasespotInit(this.acousticModelPathname, this.searchGrammarPathname);
                if (this.isSubModelBargeInEnable) {
                    this.consoleInitReturn_sub = this.aSensoryBargeInEngine.phrasespotInit(this.acousticModelPathname_sub, this.searchGrammarPathname_sub);
                }
            } else {
                Log.e(this.TAG, "SensoryBargeInEngineWrapper.getInstance() is null");
            }
        }
        if ((this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) || !this.isSensoryBargeInEnable) {
            this.aMMUIRecognizer = IWSpeechRecognizerWrapper.getInstance();
            if (this.aMMUIRecognizer != null) {
                this.aMMUIRecognizer.SetSRLanguage(this.mEmbeddedEngineLanguage);
            }
            setSamsungFilePath(this.mEmbeddedEngineLanguage, i2);
            Log.d(this.TAG, "Load Model");
            if (this.aMMUIRecognizer != null) {
                this.resultSASRLoadModel = this.aMMUIRecognizer.SASRLoadModel(this.modelPath);
                if (this.resultSASRLoadModel == 0) {
                    this.isSASRInitProblem = true;
                }
            }
            Log.d(this.TAG, "Load Model result : " + this.resultSASRLoadModel);
            if (isBargeInFile(this.wordListPath + this.loadNameList)) {
                Log.d(this.TAG, "Wordlist is " + this.loadNameList);
            } else {
                Log.d(this.TAG, "Wordlist is not exist. So set default wordlist");
                this.loadNameList = this.defaultloadNameList;
            }
            Log.d(this.TAG, "Load Wordlist");
            if (this.aMMUIRecognizer != null) {
                if (!this.isSASRInitProblem) {
                    this.resultSASRInit = this.aMMUIRecognizer.SASRInit(this.wordListPath + this.loadNameList);
                }
                if (this.resultSASRInit == 0) {
                    this.isSASRInitProblem = true;
                }
            }
            Log.d(this.TAG, "Load Wordlist result : " + this.resultSASRInit);
            if (!(this.aMMUIRecognizer == null || this.isSASRInitProblem)) {
                this.aMMUIRecognizer.SASRReset();
            }
        }
        Log.d(this.TAG, "resultSASRLoadModel : " + this.resultSASRLoadModel);
        Log.d(this.TAG, "resultSASRInit : " + this.resultSASRInit);
        Log.d(this.TAG, "isSASRInitProblem : " + this.isSASRInitProblem);
    }

    public boolean isSensoryBargeinEnabled() {
        return this.isSensoryBargeInEnable;
    }

    int readShortBlock() {
        if (this.isSASRInitProblem) {
            Log.e(this.TAG, "readByteBlock return -1 : isSASRInitProblem");
            this.readNshorts = -1;
            return -1;
        } else if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : Section1");
            this.readNshorts = -1;
            return -1;
        } else {
            if (!(this.rec == null || this.done)) {
                this.readNshorts = this.rec.read(this.speech, 0, this.speech.length);
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section2");
                this.readNshorts = -1;
                return -1;
            }
            if (this.readNshorts < 160) {
                Log.e(this.TAG, "AudioRecord Read problem : nshorts = " + this.readNshorts + " command = " + this.mCommandType + " language : " + this.mLanguage);
            }
            if (this.totalReadCount % 20 == 0) {
                Log.d(this.TAG, "nshorts = " + (this.readNshorts * 10) + " command = " + this.mCommandType + " language : " + this.mLanguage + " dualThr : " + this.dualThresholdFlag);
            }
            this.totalReadCount++;
            if (this.recogAfterReadCount != 0) {
                this.recogAfterReadCount = (this.recogAfterReadCount + 1) % 100;
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section3");
                this.readNshorts = -1;
                return -1;
            }
            if (this.readNshorts <= 0) {
                Log.i(this.TAG, "readNshorts is " + this.readNshorts + " So do nothing");
            } else if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section4");
                this.readNshorts = -1;
                return -1;
            } else {
                if (this.isSensoryBargeInEnable) {
                    if (this.done) {
                        Log.e(this.TAG, "readByteBlock return -1 : Section5");
                        this.readNshorts = -1;
                        return -1;
                    } else if (this.aSensoryBargeInEngine != null && this.totalReadCount > this.AUDIO_START) {
                        boolean sensoryRecognitionResult = getSensoryRecognitionResult(this.consoleInitReturn, this.speech);
                        if (this.isSubModelBargeInEnable && getSensoryRecognitionResult(this.consoleInitReturn_sub, this.speech)) {
                            Log.i(this.TAG, "It is Recognized by sub Model");
                        }
                    }
                }
                if (this.totalReadCount > 50 && ((this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) || !this.isSensoryBargeInEnable)) {
                    if (this.done) {
                        Log.e(this.TAG, "readByteBlock return -1 : Section6");
                        this.readNshorts = -1;
                        return -1;
                    } else if (this.aMMUIRecognizer != null) {
                        this.readNshorts = getMMUIRecognitionResult(this.speech, this.readNshorts);
                        if (this.readNshorts == -1) {
                            return -1;
                        }
                    }
                }
                if (this.isMakePCM) {
                    AudioTask.swap(this.speech);
                    int i = 0;
                    while (i < this.speech.length) {
                        try {
                            this.mDataOutputStream.writeShort(this.speech[i]);
                            i++;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return this.readNshorts;
        }
    }

    public void run() {
        Log.d(this.TAG, "SensoryAudioTask run()");
        if (this.rec != null) {
            Log.d(this.TAG, "Call rec.startRecording start");
            this.rec.startRecording();
            Log.d(this.TAG, "Call startRecording end");
            while (!this.done) {
                readShortBlock();
                if (!this.done) {
                    if (this.readNshorts <= 0) {
                        break;
                    }
                }
                break;
            }
        }
        Log.e(this.TAG, "Bargein fail to start");
        stopBargeInAudioRecord();
        if (this.aMMUIRecognizer != null) {
            Log.i(this.TAG, "SASRClose start");
            if (!this.isSASRInitProblem) {
                Log.d(this.TAG, "SASRCloseReturn : " + this.aMMUIRecognizer.SASRClose());
            }
            Log.i(this.TAG, "SASRClose end");
        }
        if (this.aSensoryBargeInEngine != null) {
            Log.i(this.TAG, "phrasespotClose start");
            if (this.consoleInitReturn != -1) {
                this.aSensoryBargeInEngine.phrasespotClose(this.consoleInitReturn);
            }
            if (this.isSubModelBargeInEnable && this.consoleInitReturn_sub != -1) {
                this.aSensoryBargeInEngine.phrasespotClose(this.consoleInitReturn_sub);
            }
            Log.i(this.TAG, "phrasespotClose end");
        }
        this.aMMUIRecognizer = null;
        this.aSensoryBargeInEngine = null;
        this.m_listener = null;
        Log.d(this.TAG, "aMMUIRecognizer = null");
        Log.d(this.TAG, "aSensoryBargeInEngine = null");
        Log.d(this.TAG, "m_listener = null");
        Log.i(this.TAG, "SensoryAudioTask run end");
        if (!this.done && this.mStopHandler != null) {
            this.mStopHandler.sendEmptyMessage(0);
        }
    }

    public void setEmbeddedEngineLanguage() {
        this.mEmbeddedEngineLanguage = this.mLanguage;
        if (this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) {
            this.mEmbeddedEngineLanguage = 0;
        } else if (this.mEmbeddedEngineLanguage == 10) {
            this.mEmbeddedEngineLanguage = 1;
        } else if (this.mEmbeddedEngineLanguage == 11) {
            this.mEmbeddedEngineLanguage = 3;
        } else if (this.mEmbeddedEngineLanguage == 9) {
            this.mEmbeddedEngineLanguage = 1;
        } else if (this.mEmbeddedEngineLanguage == 13) {
            this.mEmbeddedEngineLanguage = 2;
        } else if (this.mEmbeddedEngineLanguage == 12) {
            this.mEmbeddedEngineLanguage = 2;
        } else if (this.mEmbeddedEngineLanguage == 14) {
            this.mEmbeddedEngineLanguage = 2;
        }
        Log.i(this.TAG, "mEmbeddedEngineLanguage : " + this.mEmbeddedEngineLanguage);
    }

    public void setHandler(Handler handler) {
        this.mStopHandler = handler;
    }

    public void setSamsungFilePath(int i, int i2) {
        this.wordListPath = Config.GetSamsungPath(i);
        this.modelPath = this.wordListPath + "param";
        this.loadNameList = Config.GetSamsungNameList(i2);
    }

    public void setSensoryFilePath(int i, int i2) {
        String GetSensoryAM = Config.GetSensoryAM(i, i2);
        String GetSensoryGRAMMAR = Config.GetSensoryGRAMMAR(i, i2);
        String str = GetSensoryAM;
        String str2 = GetSensoryGRAMMAR;
        GetSensoryAM = GetSensoryAM + Config.SENSORY_MAIN_SUFFIX;
        GetSensoryGRAMMAR = GetSensoryGRAMMAR + Config.SENSORY_MAIN_SUFFIX;
        str = str + Config.SENSORY_SUB_SUFFIX;
        str2 = str2 + Config.SENSORY_SUB_SUFFIX;
        if (isBargeInFile(Config.SENSORY_SO_FILE_PATH) || isBargeInFile(Config.SENSORY_SO_FILE_PATH_64)) {
            if (isBargeInFile(GetSensoryAM) && isBargeInFile(GetSensoryGRAMMAR)) {
                this.isSensoryBargeInEnable = true;
                this.acousticModelPathname = GetSensoryAM;
                this.searchGrammarPathname = GetSensoryGRAMMAR;
            }
            if (isBargeInFile(str) && isBargeInFile(str2)) {
                this.isSubModelBargeInEnable = true;
                this.acousticModelPathname_sub = str;
                this.searchGrammarPathname_sub = str2;
                Log.i(this.TAG, "SUB model is loaded ");
            }
        }
        if (this.mCommandType == 7) {
            this.isCameraBargeIn = true;
            if (this.isSensoryBargeInEnable) {
                this.isSensoryCameraBargeIn = true;
            }
        } else if (this.mCommandType == 9) {
            this.isCancelBargeIn = true;
        }
    }

    public void stop() {
        Log.i(this.TAG, "SensoryAudioTask : stop start");
        this.mStopHandler = null;
        this.done = true;
        this.readNshorts = -1;
        Log.i(this.TAG, "SensoryAudioTask : stop end");
    }

    public void stopBargeInAudioRecord() {
        Log.i(this.TAG, "stopBargeInAudioRecord start");
        if (this.rec != null) {
            if (this.isMakePCM) {
                try {
                    this.mDataOutputStream.flush();
                    this.mDataOutputStream.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            Log.d(this.TAG, "Call rec.stop start");
            this.rec.stop();
            Log.d(this.TAG, "Call rec.stop end");
            Log.d(this.TAG, "Call rec.release start");
            this.rec.release();
            Log.d(this.TAG, "Call rec.release end");
            this.rec = null;
            Log.d(this.TAG, "rec = null");
        }
        Log.i(this.TAG, "stopBargeInAudioRecord end");
    }
}
