package com.samsung.android.media;

import android.media.AudioSystem;
import android.util.Log;

public class SemHiddenSoundManager {
    public static final int ERROR = -1;
    public static final int PACKAGE_ALL = 0;
    private static String TAG = "SemHiddenSoundManager";
    public static final int VOLUME_DEVICE = -3;
    public static final int VOLUME_FULL = -2;

    private SemHiddenSoundManager() {
    }

    public static int getPlaybackRecorderPackage() {
        try {
            return Integer.parseInt(AudioSystem.getParameters("audioParam;hiddensound_pid"));
        } catch (Throwable e) {
            Log.e(TAG, "Invalid PID", e);
            return -1;
        }
    }

    public static int getPlaybackRecorderVersion() {
        String parameters = AudioSystem.getParameters("audioParam;hiddensound_version");
        if ("".equals(parameters)) {
            Log.i(TAG, "Dont support");
            return -1;
        }
        int parseInt;
        try {
            parseInt = Integer.parseInt(parameters);
        } catch (Throwable e) {
            Log.e(TAG, "Invalid Version", e);
            parseInt = -1;
        }
        return parseInt;
    }

    public static int getPlaybackRecorderVolume() {
        try {
            return Integer.parseInt(AudioSystem.getParameters("audioParam;hiddensound_volume"));
        } catch (Throwable e) {
            Log.e(TAG, "Invalid volume", e);
            return -1;
        }
    }

    public static void setPlaybackRecorderPackage(int i) {
        AudioSystem.setParameters("audioParam;hiddensound_pid=" + i);
    }

    public static void setPlaybackRecorderVolume(int i) {
        AudioSystem.setParameters("audioParam;hiddensound_volume=" + i);
    }
}
