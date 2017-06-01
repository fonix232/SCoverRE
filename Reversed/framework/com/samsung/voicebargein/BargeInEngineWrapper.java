package com.samsung.voicebargein;

import android.util.Log;

public class BargeInEngineWrapper {
    private static final String TAG = BargeInEngineWrapper.class.getSimpleName();
    private static BargeInEngine uniqueInstance;

    private BargeInEngineWrapper() {
    }

    public static synchronized BargeInEngine getInstance() {
        synchronized (BargeInEngineWrapper.class) {
            if (uniqueInstance == null) {
                Log.i(TAG, "getInstance() : make new libBargeInEngine");
                if (BargeInEngine.init() == 0) {
                    uniqueInstance = new BargeInEngine();
                } else {
                    Log.e(TAG, "cannot load libBargeInEngine.so");
                    return null;
                }
            }
            Log.i(TAG, "getInstance() : get existed libBargeInEngine");
            BargeInEngine bargeInEngine = uniqueInstance;
            return bargeInEngine;
        }
    }
}
