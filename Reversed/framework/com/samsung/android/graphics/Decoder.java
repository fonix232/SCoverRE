package com.samsung.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Decoder {
    private static final String TAG = "Decoder";

    public static class Options {
        public Bitmap inBitmap;
        public boolean inJustDecodeBounds;
        public boolean inMutable;
        public int inPageNum;
        public boolean inPreferQualityOverSpeed;
        public Config inPreferredConfig = Config.ARGB_8888;
        public int inSampleSize;
        public boolean isPreview = false;
        public int outHeight;
        public int outPageNum;
        public int outWidth;
    }

    static {
        try {
            System.loadLibrary("MMCodec");
        } catch (Throwable e) {
            Log.e(TAG, "Load library fail : " + e.toString());
        }
    }

    public static Bitmap decodeByteArray(byte[] bArr, int i, int i2) {
        if (bArr == null || i2 <= 0 || i >= i2) {
            return null;
        }
        byte[] bArr2 = bArr;
        int i3 = i;
        long nativedecinstance = nativedecinstance("nofile", bArr2, i3, bArr.length, 0, new int[2]);
        if (nativedecinstance == 0) {
            return BitmapFactory.decodeByteArray(bArr, i, i2);
        }
        return nativeDecodeByteArray(bArr, i, bArr.length, nativedecinstance, 1, null);
    }

    public static Bitmap decodeByteArray(byte[] bArr, int i, int i2, Options options) {
        if (bArr == null || i2 <= 0 || i >= i2) {
            return null;
        }
        long nativeCreateFds = nativeCreateFds();
        int[] iArr = new int[2];
        nativecopybytebuffer(nativeCreateFds, bArr, i, bArr.length);
        nativegetImageinfo(nativeCreateFds, iArr);
        Bitmap doDecode = doDecode(nativeCreateFds, iArr, options);
        nativefreeFds(nativeCreateFds);
        if (doDecode == null) {
            android.graphics.BitmapFactory.Options bitmapFactoryOptions = getBitmapFactoryOptions(options);
            doDecode = BitmapFactory.decodeByteArray(bArr, i, i2, bitmapFactoryOptions);
            options.outWidth = bitmapFactoryOptions.outWidth;
            options.outHeight = bitmapFactoryOptions.outHeight;
        }
        return doDecode;
    }

    public static Bitmap decodeFile(String str) {
        if (str == null) {
            return null;
        }
        long nativedecinstance = nativedecinstance(str, null, 0, 0, 1, new int[2]);
        return nativedecinstance != 0 ? nativeDecodeFile(str, nativedecinstance, 1, null) : BitmapFactory.decodeFile(str);
    }

    public static Bitmap decodeFile(String str, Options options) {
        if (str == null) {
            return null;
        }
        long nativeCreateFds = nativeCreateFds();
        int[] iArr = new int[2];
        nativecopyfilename(nativeCreateFds, str);
        nativegetImageinfo(nativeCreateFds, iArr);
        Bitmap doDecode = doDecode(nativeCreateFds, iArr, options);
        nativefreeFds(nativeCreateFds);
        if (doDecode == null) {
            android.graphics.BitmapFactory.Options bitmapFactoryOptions = getBitmapFactoryOptions(options);
            doDecode = BitmapFactory.decodeFile(str, bitmapFactoryOptions);
            options.outWidth = bitmapFactoryOptions.outWidth;
            options.outHeight = bitmapFactoryOptions.outHeight;
        }
        return doDecode;
    }

    private static Bitmap doDecode(long j, int[] iArr, Options options) {
        if (j == 0) {
            return null;
        }
        int i = iArr[0];
        int i2 = iArr[1];
        if (i == -1 || i2 == -1) {
            return null;
        }
        if (options.inJustDecodeBounds) {
            options.outWidth = i;
            options.outHeight = i2;
            return options.inBitmap;
        }
        Bitmap nativeCreateBitmap;
        int i3 = options.inSampleSize;
        if (i3 == 0) {
            i3 = 1;
        }
        i = ((i + i3) - 1) / i3;
        i2 = ((i2 + i3) - 1) / i3;
        if (options.inBitmap == null) {
            nativeCreateBitmap = nativeCreateBitmap(i, i2);
        } else if (options.inBitmap.getWidth() == i && options.inBitmap.getHeight() == i2) {
            nativeCreateBitmap = options.inBitmap;
        } else {
            Log.v(TAG, "inBitmap Erraneous\n");
            nativeCreateBitmap = null;
        }
        if (nativeCreateBitmap != null) {
            nativelockBitmap(j, nativeCreateBitmap);
            nativeDecode(j, i3);
            nativeunlockBitmap(nativeCreateBitmap);
        }
        return nativeCreateBitmap;
    }

    protected static android.graphics.BitmapFactory.Options getBitmapFactoryOptions(Options options) {
        android.graphics.BitmapFactory.Options options2 = new android.graphics.BitmapFactory.Options();
        if (options != null) {
            options2.inBitmap = options.inBitmap;
            options2.inMutable = options.inMutable;
            options2.inJustDecodeBounds = options.inJustDecodeBounds;
            options2.inSampleSize = options.inSampleSize;
            options2.inPreferredConfig = options.inPreferredConfig;
            options2.inPreferQualityOverSpeed = options.inPreferQualityOverSpeed;
        }
        return options2;
    }

    private static native Bitmap nativeCreateBitmap(int i, int i2);

    private static native long nativeCreateFds();

    private static native int nativeDecode(long j, int i);

    private static native Bitmap nativeDecodeByteArray(byte[] bArr, int i, int i2, long j, int i3, Bitmap bitmap);

    private static native Bitmap nativeDecodeFile(String str, long j, int i, Bitmap bitmap);

    private static native int nativecopybytebuffer(long j, byte[] bArr, int i, int i2);

    private static native int nativecopyfilename(long j, String str);

    private static native long nativedecinstance(String str, byte[] bArr, int i, int i2, int i3, int[] iArr);

    private static native int nativefreeFds(long j);

    private static native int nativegetImageinfo(long j, int[] iArr);

    private static native int nativelockBitmap(long j, Bitmap bitmap);

    private static native int nativeunlockBitmap(Bitmap bitmap);
}
