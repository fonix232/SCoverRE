package com.samsung.android.speech;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.samsung.android.speech.SemSpeechRecognizer.ResultListener;
import com.samsung.voicebargein.BargeInEngine;
import com.samsung.voicebargein.BargeInEngineWrapper;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.LinkedBlockingQueue;

class PDTAudioTask extends AudioTask implements Runnable {
    static final int DEFAULT_BLOCK_SIZE = 320;
    private int AUDIO_START = 0;
    public float CMscore = 0.0f;
    private final int RECOGNITION_WAIT_TIME = 100;
    private String TAG = PDTAudioTask.class.getSimpleName();
    public double THscore = -1.5d;
    private BargeInEngine aPDTBargeInEngine = null;
    private String acousticModelPathname = (Config.GetPDTAM(0, 2) + Config.PDT_MAIN_SUFFIX);
    public int block_size = 0;
    public byte[] buf;
    public long consoleInitReturn = -1;
    public boolean done = false;
    private int dualThresholdFlag = 0;
    public File f22f = null;
    private Handler handler = new C02561();
    public boolean isCameraBargeIn = false;
    public boolean isCancelBargeIn = false;
    private boolean isMakePCM = false;
    public boolean isPDTBargeInEnable = false;
    public boolean isSensoryResult = false;
    public String loadPath = null;
    public int mCommandType = 0;
    public DataOutputStream mDataOutputStream = null;
    public int mLanguage = 1;
    public Handler mStopHandler = null;
    private ResultListener m_listener = null;
    public int numRecogResult = 0;
    public LinkedBlockingQueue<short[]> f23q = null;
    private int readNshorts = -1;
    private int recogAfterReadCount = 0;
    private String searchGrammarPathname = (Config.GetPDTGRAMMAR(0, 2) + Config.PDT_MAIN_SUFFIX);
    public short[] speech = null;
    private int totalReadCount = 0;

    class C02561 extends Handler {
        C02561() {
        }

        public void handleMessage(Message message) {
            String[] stringArray = message.getData().getStringArray("recognition_result");
            if (PDTAudioTask.this.m_listener != null) {
                PDTAudioTask.this.m_listener.onResults(stringArray);
            }
        }
    }

    PDTAudioTask(ResultListener resultListener, String str, int i, int i2, boolean z) {
        super(resultListener, str, i, i2, z);
        init(new LinkedBlockingQueue(), DEFAULT_BLOCK_SIZE, resultListener, str, i, i2, z);
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

    private int getPDTBargeInAct(int i, String str) {
        switch (i) {
            case 0:
            case 1:
            case 2:
                if (str.startsWith("Answer")) {
                    return 1;
                }
                if (str.startsWith("Reject")) {
                    return 2;
                }
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                if (str.startsWith("Smile")) {
                    return 1;
                }
                if (str.startsWith("Cheese")) {
                    return 2;
                }
                if (str.startsWith("Capture")) {
                    return 3;
                }
                if (str.startsWith("Shoot")) {
                    return 4;
                }
                if (str.startsWith("Record Video") || str.startsWith("Record_Video") || str.startsWith("RecordVideo")) {
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
        }
        return -1;
    }

    private boolean getPDTRecognitionResult(long j, short[] sArr) {
        float[] fArr = new float[3];
        String phrasespotPipe = this.aPDTBargeInEngine.phrasespotPipe(j, sArr, 320, 16000, fArr);
        if (phrasespotPipe != null) {
            this.BargeinAct[0] = (short) getPDTBargeInAct(this.mCommandType, phrasespotPipe);
            this.strResult[0] = phrasespotPipe;
            float f = fArr[0];
            Log.i(this.TAG, "consoleResult : " + phrasespotPipe);
            Log.d(this.TAG, "strResult[0] : " + this.strResult[0]);
            Log.d(this.TAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            Log.i(this.TAG, "CMscore : " + f);
            if (!this.isCameraBargeIn) {
                SendHandlerMessage(this.strResult);
                return true;
            } else if (this.recogAfterReadCount == 0) {
                this.recogAfterReadCount = 1;
                SendHandlerMessage(this.strResult);
                return true;
            }
        }
        return false;
    }

    private void setPDTFilePath(int i, int i2) {
        String GetPDTAM = Config.GetPDTAM(i, i2);
        String GetPDTGRAMMAR = Config.GetPDTGRAMMAR(i, i2);
        GetPDTAM = GetPDTAM + Config.PDT_MAIN_SUFFIX;
        GetPDTGRAMMAR = GetPDTGRAMMAR + Config.PDT_MAIN_SUFFIX;
        if ((isBargeInFile(Config.PDT_SO_FILE_PATH) || isBargeInFile(Config.PDT_SO_FILE_PATH_64)) && isBargeInFile(GetPDTAM) && isBargeInFile(GetPDTGRAMMAR)) {
            this.isPDTBargeInEnable = true;
            this.acousticModelPathname = GetPDTAM;
            this.searchGrammarPathname = GetPDTGRAMMAR;
        }
        if (this.mCommandType == 7) {
            this.isCameraBargeIn = true;
            if (this.isPDTBargeInEnable) {
                this.isPDTBargeInEnable = true;
            }
        } else if (this.mCommandType == 9) {
            this.isCancelBargeIn = true;
        }
    }

    void init(LinkedBlockingQueue<short[]> linkedBlockingQueue, int i, ResultListener resultListener, String str, int i2, int i3, boolean z) {
        this.TAG = PDTAudioTask.class.getSimpleName();
        Log.i(this.TAG, "PDTAudioTask init()");
        Log.i(this.TAG, "command : " + i2);
        Log.i(this.TAG, "Language : " + i3);
        this.done = false;
        this.f23q = linkedBlockingQueue;
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
        setPDTFilePath(i3, i2);
        this.speech = new short[DEFAULT_BLOCK_SIZE];
        Log.i(this.TAG, "isPDTBargeInEnable : " + this.isPDTBargeInEnable);
        this.totalReadCount = 0;
        this.recogAfterReadCount = 0;
        if (this.isMakePCM) {
            this.f22f = new File("/sdcard/", "testPCM.pcm");
            try {
                this.mDataOutputStream = new DataOutputStream(new FileOutputStream(this.f22f, true));
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
        } else if (this.isPDTBargeInEnable) {
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
        if (this.isPDTBargeInEnable) {
            this.aPDTBargeInEngine = BargeInEngineWrapper.getInstance();
            if (this.aPDTBargeInEngine != null) {
                this.consoleInitReturn = this.aPDTBargeInEngine.phrasespotInit(this.acousticModelPathname, this.searchGrammarPathname);
            } else {
                Log.e(this.TAG, "BargeInEngineWrapper.getInstance() is null");
            }
        }
    }

    public boolean isPDTBargeinEnabled() {
        return this.isPDTBargeInEnable;
    }

    int readShortBlock() {
        if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : Section1");
            this.readNshorts = -1;
            return -1;
        }
        if (!(this.rec == null || this.done)) {
            this.readNshorts = this.rec.read(this.speech, 0, this.speech.length);
        }
        if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : Section2");
            this.readNshorts = -1;
            return -1;
        }
        if (this.readNshorts < DEFAULT_BLOCK_SIZE) {
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
            if (this.isPDTBargeInEnable) {
                if (this.done) {
                    Log.e(this.TAG, "readByteBlock return -1 : Section5");
                    this.readNshorts = -1;
                    return -1;
                } else if (this.aPDTBargeInEngine != null && this.totalReadCount > this.AUDIO_START) {
                    boolean pDTRecognitionResult = getPDTRecognitionResult(this.consoleInitReturn, this.speech);
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

    public void run() {
        Log.d(this.TAG, "PDTAudioTask run()");
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
        if (this.aPDTBargeInEngine != null) {
            Log.i(this.TAG, "PDT phrasespotClose start");
            if (this.consoleInitReturn != -1) {
                this.aPDTBargeInEngine.phrasespotClose(this.consoleInitReturn);
            }
            Log.i(this.TAG, "PDT phrasespotClose end");
        }
        this.aPDTBargeInEngine = null;
        this.m_listener = null;
        Log.d(this.TAG, "aPDTBargeInEngine = null");
        Log.d(this.TAG, "m_listener = null");
        Log.i(this.TAG, "run end");
        if (!this.done && this.mStopHandler != null) {
            this.mStopHandler.sendEmptyMessage(0);
        }
    }

    public void stop() {
        Log.i(this.TAG, "PDTAudioTask : stop start");
        this.mStopHandler = null;
        this.done = true;
        this.readNshorts = -1;
        Log.i(this.TAG, "PDTAudioTask : stop end");
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
