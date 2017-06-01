package com.samsung.android.app.interactivepanoramaviewer.util;

import android.util.Log;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.nio.ByteBuffer;

public class JniUtil {
    public static final int NV12 = 0;
    public static final int YUV420PlANAR = 1;

    static {
        try {
            System.loadLibrary("InteractivePanoramaUtil");
            Log.m35v(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, "InteractivePanoramaUtil loaded");
        } catch (Throwable e) {
            Log.m31e(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET, "Unable to Load " + e.getLocalizedMessage());
        }
    }

    public static native void postProcessBufferHW(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public static native int swABGR8888ToNV12(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2);

    public static native int swABGR8888ToRGB565(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2);

    public static native void swCrop(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, int i3, int i4, int i5);

    public static native int swPPResize(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i, int i2, int i3, int i4);
}
