package com.samsung.android.speech;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.samsung.android.speech.SemSpeechRecognizer.ResultListener;
import java.io.DataOutputStream;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

class AudioTask implements Runnable {
    static final int DEFAULT_BLOCK_SIZE = 320;
    protected int AUDIO_RECORD_FOR_BARGE_IN = MediaRecorder.semGetInputSource(3);
    protected int AUDIO_RECORD_FOR_BARGE_IN_SENSORY = MediaRecorder.semGetInputSource(7);
    protected int AUDIO_RECORD_FOR_VOICE_RECOGNITION = 6;
    private int AUDIO_START = 0;
    public short[] BargeinAct = new short[]{(short) -1};
    private float CMscore = 0.0f;
    private final int RECOGNITION_WAIT_TIME = 100;
    private double THscore = -1.5d;
    private MMUIRecognizer aMMUIRecognizer = null;
    private String acousticModelPathname = (Config.GetSensoryAM(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String acousticModelPathname_sub = (Config.GetSensoryAM(0, 2) + Config.SENSORY_SUB_SUFFIX);
    private int block_size = 0;
    public float[] cmResult = new float[]{0.0f};
    private long consoleInitReturn = -1;
    private String defaultloadNameList = Config.GetSamsungNameList(0);
    private boolean done = false;
    private int dualThresholdFlag = 0;
    private File f20f = null;
    private Handler handler = new C02551();
    private boolean isEnableSamsungOOVResult = false;
    private boolean isMakePCM = false;
    private boolean isSensoryCameraBargeIn = false;
    private boolean isSensoryResult = false;
    private String loadNameList = Config.GetSamsungNameList(0);
    private String loadPath = null;
    private int mCommandType = 0;
    private DataOutputStream mDataOutputStream = null;
    private int mEmbeddedEngineLanguage = 1;
    private int mLanguage = 1;
    private Handler mStopHandler = null;
    private String mTAG = AudioTask.class.getSimpleName();
    private ResultListener m_listener = null;
    private String modelPath = (Config.GetSamsungPath(1) + "param");
    private int numRecogResult = 0;
    private LinkedBlockingQueue<short[]> f21q = null;
    private int readNshorts = -1;
    public AudioRecord rec = null;
    private int recogAfterReadCount = 0;
    private String searchGrammarPathname = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String searchGrammarPathname_sub = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_SUB_SUFFIX);
    public String[] strResult = new String[3];
    private int totalReadCount = 0;
    public String[] utfResult = new String[1];
    private String wordListPath = Config.GetSamsungPath(1);

    class C02551 extends Handler {
        C02551() {
        }

        public void handleMessage(Message message) {
            String[] stringArray = message.getData().getStringArray("recognition_result");
            if (AudioTask.this.m_listener != null) {
                AudioTask.this.m_listener.onResults(stringArray);
            }
        }
    }

    AudioTask(ResultListener resultListener, String str, int i, int i2, boolean z) {
    }

    private void SendHandlerMessage(String[] strArr) {
        Message obtainMessage = this.handler.obtainMessage();
        BaseBundle bundle = new Bundle();
        bundle.putStringArray("recognition_result", strArr);
        obtainMessage.setData(bundle);
        try {
            this.handler.sendMessage(obtainMessage);
        } catch (Throwable e) {
            Log.e(this.mTAG, "IllegalStateException " + e.getMessage());
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
                Log.e(this.mTAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section1");
                return -1;
            } else if (this.aMMUIRecognizer != null) {
                Log.d(this.mTAG, "Barge-in : Too long input so Reset");
                this.aMMUIRecognizer.ResetFx();
                this.aMMUIRecognizer.SASRReset();
            }
        }
        if (this.done) {
            Log.e(this.mTAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section2");
            return -1;
        }
        if (i2 == 2 && this.aMMUIRecognizer != null) {
            if (this.done) {
                Log.e(this.mTAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section3");
                return -1;
            }
            this.aMMUIRecognizer.ResetFx();
            this.numRecogResult = this.aMMUIRecognizer.SASRDoRecognition(this.cmResult, this.strResult, "/system/voicebargeindata/sasr/input.txt", this.BargeinAct, this.utfResult);
            this.strResult[0] = this.strResult[0].replace('_', ' ');
            if (this.mEmbeddedEngineLanguage == 0 || this.mEmbeddedEngineLanguage == 2) {
                this.utfResult[0] = this.utfResult[0].replace('_', ' ');
                this.strResult[0] = this.utfResult[0];
            }
            Log.i(this.mTAG, "numResult[0] : " + this.cmResult[0]);
            Log.i(this.mTAG, "strResult[0] : " + this.strResult[0]);
            Log.i(this.mTAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            if (this.mCommandType == 3 && this.BargeinAct[0] == (short) 2) {
                this.THscore = -1.8d;
            } else if (this.mCommandType == 7) {
                this.THscore = -1.0d;
            } else {
                this.THscore = -1.5d;
            }
            Log.i(this.mTAG, "THscore : " + this.THscore);
            if (this.done) {
                Log.e(this.mTAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section4");
                return -1;
            }
            if (this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) {
                if (this.isSensoryResult) {
                    Log.i(this.mTAG, "isSensoryCameraBargeIn is true and isSensoryResult is true");
                    Log.d(this.mTAG, "EmbeddedEngine Recognizer : " + this.BargeinAct[0]);
                    this.isSensoryResult = false;
                    Log.i(this.mTAG, "Set isSensoryResult = false. So isSensoryResult : " + this.isSensoryResult);
                } else {
                    Log.i(this.mTAG, "isSensoryCameraBargeIn is true and keyword is not detected by sensory and keyword or non-keyword is detected by embeddedEngine.");
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
                Log.e(this.mTAG, "readByteBlock return -1 : Section13");
                return -1;
            }
            this.aMMUIRecognizer.SASRReset();
        }
        return i;
    }

    public static short swap(short s) {
        return (short) (((s & 255) << 8) | (((s >> 8) & 255) << 0));
    }

    public static void swap(short[] sArr) {
        for (int i = 0; i < sArr.length; i++) {
            sArr[i] = swap(sArr[i]);
        }
    }

    public static short twoBytesToShort(byte b, byte b2) {
        return (short) ((b & 255) | (b2 << 8));
    }

    protected AudioRecord getAudioRecord(int i) {
        Throwable th;
        Log.i(this.mTAG, "getAudioRecord modified by jy");
        AudioRecord audioRecord;
        try {
            audioRecord = new AudioRecord(i, 16000, 16, 2, 8192);
            try {
                if (audioRecord.getState() != 1) {
                    Log.d(this.mTAG, "getAudioRecord for " + i + "=false, got !initialized");
                    if (audioRecord != null) {
                        audioRecord.release();
                    }
                    return null;
                }
                Log.d(this.mTAG, "got AudioRecord using source=" + i + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
                Log.i(this.mTAG, "getAudioRecord for " + i + "=true");
                return audioRecord;
            } catch (IllegalArgumentException e) {
                try {
                    Log.e(this.mTAG, "getAudioRecord for " + i + "=false, IllegalArgumentException");
                    Log.e(this.mTAG, "got IllegalArgumentException using source=" + i + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
                    if (audioRecord != null) {
                        audioRecord.release();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (IllegalArgumentException e2) {
            audioRecord = null;
            Log.e(this.mTAG, "getAudioRecord for " + i + "=false, IllegalArgumentException");
            Log.e(this.mTAG, "got IllegalArgumentException using source=" + i + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
            if (audioRecord != null) {
                audioRecord.release();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            Object obj = null;
            throw th;
        }
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public LinkedBlockingQueue<short[]> getQueue() {
        return this.f21q;
    }

    void init(LinkedBlockingQueue<short[]> linkedBlockingQueue, int i, ResultListener resultListener, String str, int i2, int i3, boolean z) {
        this.mTAG = AudioTask.class.getSimpleName();
        Log.i(this.mTAG, "AudioTask init()");
        Log.i(this.mTAG, "command : " + i2);
        Log.i(this.mTAG, "Language : " + i3);
        this.done = false;
        this.f21q = linkedBlockingQueue;
        this.block_size = i;
        this.mCommandType = i2;
        this.m_listener = resultListener;
        this.loadPath = str;
        this.mLanguage = i3;
        this.BargeinAct[0] = (short) -1;
    }

    protected boolean isBargeInFile(String str) {
        return new File(str).exists();
    }

    public void run() {
        Log.i(this.mTAG, "run start");
    }

    public void setBlockSize(int i) {
        this.block_size = i;
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
        Log.i(this.mTAG, "mEmbeddedEngineLanguage : " + this.mEmbeddedEngineLanguage);
    }

    public void setHandler(Handler handler) {
        this.mStopHandler = handler;
    }

    public void setSamsungFilePath(int i, int i2) {
        this.wordListPath = Config.GetSamsungPath(i);
        this.modelPath = this.wordListPath + "param";
        this.loadNameList = Config.GetSamsungNameList(i2);
    }

    public void stop() {
        Log.i(this.mTAG, "AudioTask : stop start");
        this.mStopHandler = null;
        this.done = true;
        this.readNshorts = -1;
        Log.i(this.mTAG, "AudioTask : stop end");
    }

    public void stopBargeInAudioRecord() {
        Log.i(this.mTAG, "stopBargeInAudioRecord start");
        if (this.rec != null) {
            Log.d(this.mTAG, "Call rec.stop start");
            this.rec.stop();
            Log.d(this.mTAG, "Call rec.stop end");
            Log.d(this.mTAG, "Call rec.release start");
            this.rec.release();
            Log.d(this.mTAG, "Call rec.release end");
            this.rec = null;
            Log.d(this.mTAG, "rec = null");
        }
        Log.i(this.mTAG, "stopBargeInAudioRecord end");
    }
}
