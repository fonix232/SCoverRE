package com.samsung.android.speech;

import android.util.Log;

public class IWSpeechRecognizerWrapper {
    private static final String TAG = IWSpeechRecognizerWrapper.class.getSimpleName();
    private static MMUIRecognizer uniqueInstance;

    private IWSpeechRecognizerWrapper() {
    }

    public static synchronized MMUIRecognizer getInstance() {
        synchronized (IWSpeechRecognizerWrapper.class) {
            if (uniqueInstance == null) {
                Log.i(TAG, "getInstance() : make new MMUIRecognizer");
                if (MMUIRecognizer.init() == 0) {
                    uniqueInstance = new MMUIRecognizer();
                } else {
                    Log.e(TAG, "cannot load libsasr-jni.so");
                    return null;
                }
            }
            Log.i(TAG, "getInstance() : get existed MMUIRecognizer");
            MMUIRecognizer mMUIRecognizer = uniqueInstance;
            return mMUIRecognizer;
        }
    }
}
