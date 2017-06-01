package com.samsung.android.speech;

import android.content.Context;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

public class SemSpeechRecognizer {
    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    private static final String TAG = SemSpeechRecognizer.class.getSimpleName();
    private final String SVOICE_LANGUAGE_FILE;
    private AudioTask audio;
    private Thread audio_thread;
    private Handler handler;
    private int intBargeInResult;
    public boolean isEnableBargeIn;
    private boolean isEnableChineseBargeIn;
    private boolean isEnableExtraRussian;
    private boolean isEnableExtraSpanish;
    private Context mContext;
    private ResultListener mListener;
    private int mState;
    private Handler mStopHandler;
    private boolean samsungOOVResult;
    private int uselanguage;

    public interface ResultListener {
        void onResults(String[] strArr);
    }

    public SemSpeechRecognizer() {
        this.audio = null;
        this.audio_thread = null;
        this.mListener = null;
        this.mState = 0;
        this.isEnableBargeIn = false;
        this.isEnableChineseBargeIn = false;
        this.isEnableExtraSpanish = false;
        this.isEnableExtraRussian = false;
        this.samsungOOVResult = false;
        this.intBargeInResult = -1;
        this.uselanguage = 1;
        this.handler = null;
        this.mStopHandler = null;
        this.mContext = null;
        this.SVOICE_LANGUAGE_FILE = "/data/data/com.vlingo.midas/files/language.bin";
        this.mContext = null;
        init();
    }

    public SemSpeechRecognizer(Context context) {
        this.audio = null;
        this.audio_thread = null;
        this.mListener = null;
        this.mState = 0;
        this.isEnableBargeIn = false;
        this.isEnableChineseBargeIn = false;
        this.isEnableExtraSpanish = false;
        this.isEnableExtraRussian = false;
        this.samsungOOVResult = false;
        this.intBargeInResult = -1;
        this.uselanguage = 1;
        this.handler = null;
        this.mStopHandler = null;
        this.mContext = null;
        this.SVOICE_LANGUAGE_FILE = "/data/data/com.vlingo.midas/files/language.bin";
        this.mContext = context;
        Log.i(TAG, "BargeInRecognizer get Context " + this.mContext);
        init();
    }

    private void SendHandlerMessage(int i) {
        if (this.handler != null) {
            Message obtainMessage = this.handler.obtainMessage();
            BaseBundle bundle = new Bundle();
            bundle.putInt("commandType", i);
            obtainMessage.setData(bundle);
            if (i == 2) {
                Log.d(TAG, "sendMessageDelayed : 1500");
                this.handler.sendMessageDelayed(obtainMessage, 1500);
                return;
            }
            Log.d(TAG, "sendMessageDelayed : 700");
            this.handler.sendMessageDelayed(obtainMessage, 700);
        }
    }

    private void delayedStartBargeIn(int i, Handler handler) {
        Log.i(TAG, "delayedStartBargeIn");
        synchronized (this) {
            if (this.audio != null) {
                Log.w(TAG, "BargeIn is running. So Do nothing");
                this.audio.BargeinAct[0] = (short) -1;
            } else {
                if (isPDTModel()) {
                    Log.d(TAG, "Load PDTAudioTask");
                    this.audio = new PDTAudioTask(this.mListener, Config.DEFAULT_PATH, i, this.uselanguage, this.samsungOOVResult);
                } else {
                    Log.d(TAG, "Load SensoryAudioTask");
                    this.audio = new SensoryAudioTask(this.mListener, Config.DEFAULT_PATH, i, this.uselanguage, this.samsungOOVResult);
                    Log.d(TAG, "Complete Loading SensoryAudioTask");
                }
                if (this.audio == null || this.audio.rec == null) {
                    Log.e(TAG, "fail to running Bargein");
                    if (this.audio != null) {
                        this.audio.stop();
                    }
                    if (this.audio_thread != null) {
                        Log.e(TAG, "why running empty audio_thread");
                    }
                    this.audio = null;
                } else {
                    this.audio.setHandler(handler);
                    this.audio_thread = new Thread(this.audio);
                    this.audio_thread.start();
                    this.mState = 1;
                    Log.d(TAG, "mState change to : " + this.mState);
                }
            }
        }
    }

    private void init() {
        Log.i(TAG, "make new SemSpeechRecognizer VER 16.11.30");
        this.isEnableBargeIn = isUseModel();
        this.isEnableChineseBargeIn = isChineseMode();
        if (isPDTModel()) {
            this.isEnableExtraSpanish = true;
            this.isEnableExtraRussian = true;
        } else {
            this.isEnableExtraSpanish = isBargeInFile("/system/voicebargeindata/include/bargein_language_extra_es");
            this.isEnableExtraRussian = isBargeInFile("/system/voicebargeindata/include/bargein_language_extra_ru");
        }
        setLanguage();
        this.mState = 0;
        Log.i(TAG, "isEnableBargeIn : " + this.isEnableBargeIn);
        Log.i(TAG, "uselanguage : " + this.uselanguage);
        Log.i(TAG, "isEnableChineseBargeIn : " + this.isEnableChineseBargeIn);
        Log.i(TAG, "isEnableExtraSpanish : " + this.isEnableExtraSpanish);
        Log.i(TAG, "isEnableExtraRussian : " + this.isEnableExtraRussian);
    }

    private static boolean isBargeInFile(String str) {
        return new File(str).exists();
    }

    private static boolean isPDTModel() {
        return isBargeInFile(Config.PDT_SO_FILE_PATH) || isBargeInFile(Config.PDT_SO_FILE_PATH_64);
    }

    private static boolean isSamsungModel() {
        return isBargeInFile(Config.SAMSUNG_SO_FILE_PATH) && isBargeInFile(Config.GetSamsungModels(1)) && isBargeInFile(Config.GetSamsungModels(0));
    }

    private static boolean isSensoryModel() {
        return isBargeInFile(Config.SENSORY_SO_FILE_PATH) || isBargeInFile(Config.SENSORY_SO_FILE_PATH_64);
    }

    private boolean isUseModel() {
        if (isPDTModel()) {
            Log.i(TAG, "use libBargeInEngine.so");
            return true;
        } else if (isSamsungModel()) {
            this.samsungOOVResult = true;
            return true;
        } else if (isSensoryModel()) {
            this.samsungOOVResult = false;
            Log.i(TAG, "Could not find libsasr-jni.so use only libSensoryBargeInEngine.so");
            return true;
        } else {
            Log.e(TAG, "Error : Could not find libsasr-jni.so && libSensoryBargeInEngine.so");
            return false;
        }
    }

    private String readString(String str) {
        Throwable e;
        File file = new File(str);
        FileInputStream fileInputStream = null;
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                byte[] bArr = new byte[fileInputStream2.available()];
                fileInputStream2.read(bArr);
                fileInputStream2.close();
                return new String(bArr);
            } catch (IOException e2) {
                e = e2;
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e3) {
                        e3.printStackTrace();
                    }
                }
                e.printStackTrace();
                return null;
            }
        } catch (IOException e4) {
            e = e4;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            e.printStackTrace();
            return null;
        }
    }

    private void setLanguage() {
        String str;
        String str2;
        String str3;
        Locale locale = Locale.getDefault();
        if (locale == null) {
            str = "en_US";
            str2 = "en";
            str3 = "US";
        } else {
            str = locale.toString();
            str2 = locale.getLanguage();
            str3 = locale.getCountry();
        }
        Log.i(TAG, "stringLanguage : " + str2);
        Log.i(TAG, "stringCountry : " + str3);
        Log.i(TAG, "sVoiceLanguage : " + null);
        if (str2 == null) {
            return;
        }
        if (str2.equals(Locale.KOREA.getLanguage())) {
            this.uselanguage = 0;
        } else if (str2.equals(Locale.US.getLanguage())) {
            if (str3.equals("GB")) {
                this.uselanguage = 10;
            } else {
                this.uselanguage = 1;
            }
        } else if (str2.equals(Locale.CHINA.getLanguage()) && this.isEnableChineseBargeIn) {
            if (str3.equals("CN")) {
                this.uselanguage = 2;
            } else if (str3.equals("TW")) {
                this.uselanguage = 12;
            } else if (str3.equals("HK")) {
                this.uselanguage = 13;
            } else if (str3.equals("SG")) {
                this.uselanguage = 14;
            } else {
                this.uselanguage = 1;
            }
        } else if (str3.equals("ES")) {
            this.uselanguage = 3;
            if (this.isEnableExtraSpanish || str2.equals("es")) {
                Log.i(TAG, "Extra Sapnish is enabled : " + str);
            } else {
                this.uselanguage = 1;
            }
        } else if (str2.equals("es")) {
            this.uselanguage = 11;
        } else if (str2.equals(Locale.FRANCE.getLanguage())) {
            this.uselanguage = 4;
        } else if (str2.equals(Locale.GERMAN.getLanguage())) {
            this.uselanguage = 5;
        } else if (str2.equals(Locale.ITALY.getLanguage())) {
            this.uselanguage = 6;
        } else if (str2.equals(Locale.JAPAN.getLanguage())) {
            this.uselanguage = 7;
        } else if (str2.equals("ru")) {
            this.uselanguage = 8;
        } else if (str2.equals("pt")) {
            if (str3.equals("BR")) {
                this.uselanguage = 9;
            } else {
                this.uselanguage = 1;
            }
        } else if (!this.isEnableExtraRussian) {
            this.uselanguage = 1;
        } else if (str.contains("az_AZ") || str.contains("kk_KZ") || str.contains("uz_UZ") || str.equals("ky_KZ") || str.equals("tg_TJ") || str.equals("tk_TM") || str.equals("be_BY")) {
            this.uselanguage = 8;
            Log.i(TAG, "Extra Russian is enabled : " + str);
        } else {
            this.uselanguage = 1;
        }
    }

    private void start(int i) {
        Log.i(TAG, "start");
        if (isEnabled(i)) {
            this.mState = 1;
            Log.d(TAG, "mState change to : " + this.mState);
            if (this.mStopHandler == null) {
                this.mStopHandler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message message) {
                        Log.e(SemSpeechRecognizer.TAG, "audio is halt without stopRecognition()");
                        SemSpeechRecognizer.this.stopRecognition();
                    }
                };
                Log.d(TAG, "StopHandler create");
            }
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message message) {
                        SemSpeechRecognizer.this.delayedStartBargeIn(message.getData().getInt("commandType"), SemSpeechRecognizer.this.mStopHandler);
                    }
                };
                Log.d(TAG, "handler create");
            }
            SendHandlerMessage(i);
        }
    }

    public String getBargeInCmdLanguage() {
        switch (this.uselanguage) {
            case 0:
                return "ko-KR";
            case 1:
                return "en-US";
            case 2:
                return "zh-CN";
            case 3:
                return "es-ES";
            case 4:
                return "fr-FR";
            case 5:
                return "de-DE";
            case 6:
                return "it-IT";
            case 7:
                return "ja-JP";
            case 8:
                return "ru-RU";
            case 9:
                return "pt-BR";
            case 10:
                return "en-GB";
            case 11:
                return "v-es-LA";
            case 12:
                return "zh-TW";
            case 13:
                return "zh-HK";
            default:
                return "en-US";
        }
    }

    public int getCommandLanguage() {
        Log.i(TAG, "getCommandLanguage : " + this.uselanguage);
        return this.uselanguage;
    }

    public String[] getCommandStringArray(int i) {
        return getCommandStringArray(i, this.uselanguage);
    }

    public String[] getCommandStringArray(int i, int i2) {
        Log.i(TAG, "getCommandStringArray : CommandType ( " + i + " ) Language ( " + i2 + " )");
        if (i2 >= 15) {
            i2 = 1;
        }
        if (!isEnabled(i, i2)) {
            i2 = 1;
            Log.i(TAG, "getCommandStringArray : possible language is ( " + 1 + " )");
        }
        switch (i) {
            case 2:
                return isPDTModel() ? CommandLanguage.CALL_PDT[i2] : CommandLanguage.CALL[i2];
            case 3:
                return CommandLanguage.ALARM[i2];
            case 4:
                return CommandLanguage.MUSIC[i2];
            case 7:
                return CommandLanguage.CAMERA[i2];
            case 9:
                return CommandLanguage.CANCEL[i2];
            default:
                return null;
        }
    }

    public int getRecognitionResult() {
        synchronized (this) {
            if (this.audio != null) {
                short s = this.audio.BargeinAct[0];
                return s;
            }
            int i = this.intBargeInResult;
            return i;
        }
    }

    public int getState() {
        Log.i(TAG, "getState mState : " + this.mState);
        return this.mState;
    }

    public boolean isChineseMode() {
        return isPDTModel() || isBargeInFile(Config.GetSamsungModels(2));
    }

    public boolean isEnabled() {
        return this.isEnableBargeIn;
    }

    public boolean isEnabled(int i) {
        int i2 = this.uselanguage;
        if (isEnabled(i, i2)) {
            return true;
        }
        if (i2 == 1) {
            return false;
        }
        this.uselanguage = 1;
        return isEnabled(i, this.uselanguage);
    }

    public boolean isEnabled(int i, int i2) {
        if (isPDTModel()) {
            String GetPDTAM = Config.GetPDTAM(i2, i);
            String GetPDTGRAMMAR = Config.GetPDTGRAMMAR(i2, i);
            GetPDTAM = GetPDTAM + Config.PDT_MAIN_SUFFIX;
            GetPDTGRAMMAR = GetPDTGRAMMAR + Config.PDT_MAIN_SUFFIX;
            if (!isBargeInFile(GetPDTAM) || !isBargeInFile(GetPDTGRAMMAR)) {
                return false;
            }
            Log.i(TAG, "isEnabled: PDTBargeIn is available in commandType (" + i + ") uselanguage(" + i2 + ")");
            return true;
        }
        if (isSensoryModel()) {
            String GetSensoryAM = Config.GetSensoryAM(i2, i);
            String GetSensoryGRAMMAR = Config.GetSensoryGRAMMAR(i2, i);
            GetSensoryAM = GetSensoryAM + Config.SENSORY_MAIN_SUFFIX;
            GetSensoryGRAMMAR = GetSensoryGRAMMAR + Config.SENSORY_MAIN_SUFFIX;
            if (isBargeInFile(GetSensoryAM) && isBargeInFile(GetSensoryGRAMMAR)) {
                Log.i(TAG, "isEnabled: SensoryBargeIn is available in commandType (" + i + ") uselanguage(" + i2 + ")");
                return true;
            }
        }
        if (isSamsungModel()) {
            String GetSamsungModels = Config.GetSamsungModels(i2);
            String str = Config.GetSamsungPath(i2) + Config.GetSamsungNameList(i);
            if (isBargeInFile(GetSamsungModels) && isBargeInFile(str)) {
                Log.i(TAG, "isEnabled: SamsungBargeIn is available in commandType (" + i + ") uselanguage(" + i2 + ")");
                return true;
            }
        }
        Log.w(TAG, "isEnabled: BargeIn is not available in commandType (" + i + ") uselanguage(" + i2 + ")");
        return false;
    }

    public void setContext(Context context) {
        Log.i(TAG, "setContext");
        this.mContext = context;
    }

    public void setListener(ResultListener resultListener) {
        this.mListener = resultListener;
        this.mState = 0;
    }

    public void startRecognition(int i) {
        Log.i(TAG, "startRecognition");
        Log.i(TAG, "commandType : " + i);
        this.intBargeInResult = -1;
        setLanguage();
        start(i);
    }

    public void startRecognition(int i, int i2) {
        Log.i(TAG, "startRecognition Type2");
        Log.i(TAG, "commandType : " + i);
        Log.i(TAG, "setLanguage : " + i2);
        this.intBargeInResult = -1;
        this.uselanguage = i2;
        start(i);
    }

    public void stopRecognition() {
        Log.i(TAG, "stopRecognition");
        synchronized (this) {
            if (this.isEnableBargeIn) {
                if (this.handler != null) {
                    this.handler.removeMessages(0);
                    this.handler = null;
                    Log.d(TAG, "handler = null");
                }
                if (this.mStopHandler != null) {
                    this.mStopHandler.removeMessages(0);
                    this.mStopHandler = null;
                    Log.d(TAG, "Stop Handler = null");
                }
                if (this.audio != null) {
                    this.intBargeInResult = this.audio.BargeinAct[0];
                    this.audio.stop();
                    if (this.audio_thread != null) {
                        try {
                            Log.d(TAG, "wait for audio to stop: begin");
                            this.audio_thread.join(700);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "audio_thread was not working");
                    }
                    Log.d(TAG, "wait for audio to stop: end");
                    this.audio = null;
                    Log.d(TAG, "audio = null");
                }
                this.audio_thread = null;
                Log.d(TAG, "audio_thread = null");
                this.mState = 0;
                Log.d(TAG, "mState change to : " + this.mState);
            }
        }
    }
}
