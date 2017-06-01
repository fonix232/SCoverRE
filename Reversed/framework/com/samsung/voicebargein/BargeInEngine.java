package com.samsung.voicebargein;

import android.util.Log;

public class BargeInEngine {
    private static final String TAG = BargeInEngine.class.getSimpleName();

    public static int init() {
        try {
            Log.i(TAG, "Trying to load libBargeInEngine.so");
            System.loadLibrary("BargeInEngine");
            Log.i(TAG, "Loading libBargeInEngine.so");
            return 0;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "WARNING: Could not load libBargeInEngine.so");
            return -1;
        } catch (Exception e2) {
            Log.e(TAG, "WARNING: Could not load libBargeInEngine.so");
            return -1;
        }
    }

    public void asyncPrint(String str) {
    }

    public native void phrasespotClose(long j);

    public native long phrasespotInit(String str, String str2);

    public native String phrasespotPipe(long j, short[] sArr, long j2, long j3, float[] fArr);
}
