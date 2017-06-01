package com.sec.android.cover.ledcover.reflection.media;

import com.sec.android.cover.ledcover.reflection.AbstractBaseReflection;

public class RefAudioManager extends AbstractBaseReflection {
    private static RefAudioManager sInstance;

    public static synchronized RefAudioManager get() {
        RefAudioManager refAudioManager;
        synchronized (RefAudioManager.class) {
            if (sInstance == null) {
                sInstance = new RefAudioManager();
            }
            refAudioManager = sInstance;
        }
        return refAudioManager;
    }

    public int getStreamMinVolume(Object instance, int streamType) {
        return checkInt(invokeNormalMethod(instance, "getStreamMinVolume", new Class[]{Integer.TYPE}, Integer.valueOf(streamType)));
    }

    protected String getBaseClassName() {
        return "android.media.AudioManager";
    }
}
