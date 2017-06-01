package com.samsung.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.util.Log;
import com.samsung.android.graphics.Decoder.Options;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class RegionDecoder {
    private static final String TAG = "RegionDecoder";
    private static final boolean USE_MULTICORE = true;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private final Object mNativeLock;
    private final Object mNativeLock_decode;
    private RegionDecoder mNativeSisoRegionDecoder;
    private boolean mRecycled;
    private long secmmrd;

    static {
        try {
            System.loadLibrary("MMCodec");
        } catch (Throwable e) {
            Log.e(TAG, "Load library fail : " + e.toString());
        }
    }

    private RegionDecoder(String str, byte[] bArr, int i, int i2, boolean z, boolean z2) {
        this.secmmrd = 0;
        this.mRecycled = true;
        this.mNativeLock = new Object();
        this.mNativeLock_decode = new Object();
        this.secmmrd = 0;
        this.secmmrd = nativerdinstance(str, bArr, i, i2, z, z2);
        configMultiCore(this.secmmrd);
        this.mRecycled = false;
    }

    private static native int RequestCancelDecode(long j);

    private void checkRecycled(String str) {
        if (this.mRecycled) {
            throw new IllegalStateException(str);
        }
    }

    private static native int configLTN(long j);

    private static native int configMultiCore(long j);

    private static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int read = bufferedInputStream.read(); read != -1; read = bufferedInputStream.read()) {
            byteArrayOutputStream.write((byte) read);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static native Bitmap nativeDecodeRegion(long j, int i, int i2, int i3, int i4, int i5, Bitmap bitmap, int i6, int i7);

    private static native ByteBuffer nativeDecodeRegionBB(long j, int i, int i2, int i3, int i4, int i5, ByteBuffer byteBuffer, int i6, int i7);

    private static native int nativeGetHeight(long j);

    private static native int nativeGetWidth(long j);

    private static native long nativerdinstance(String str, byte[] bArr, int i, int i2, boolean z, boolean z2);

    private static native int nativerecycle(long j);

    public static RegionDecoder newInstance(InputStream inputStream, boolean z) {
        if (inputStream == null) {
            return null;
        }
        try {
            RegionDecoder regionDecoder = new RegionDecoder(null, convertInputStreamToByteArray(inputStream), 0, 0, z, false);
            if (regionDecoder.secmmrd != 0) {
                return regionDecoder;
            }
            regionDecoder.mNativeSisoRegionDecoder = null;
            try {
                regionDecoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, z);
            } catch (Throwable e) {
                Log.w(TAG, e.toString());
            }
            return regionDecoder.mBitmapRegionDecoder != null ? regionDecoder : null;
        } catch (Throwable e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public static RegionDecoder newInstance(String str, boolean z) {
        try {
            RegionDecoder regionDecoder = new RegionDecoder(str, null, 0, 0, z, false);
            if (regionDecoder.secmmrd != 0) {
                return regionDecoder;
            }
            regionDecoder.mNativeSisoRegionDecoder = null;
            try {
                regionDecoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(str, z);
            } catch (Throwable e) {
                Log.w(TAG, e.toString());
            }
            return regionDecoder.mBitmapRegionDecoder != null ? regionDecoder : null;
        } catch (Throwable e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public static RegionDecoder newInstance(byte[] bArr, int i, int i2, boolean z) {
        if ((i | i2) < 0 || bArr.length < i + i2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        try {
            RegionDecoder regionDecoder = new RegionDecoder(null, bArr, i, i2, z, false);
            if (regionDecoder.secmmrd != 0) {
                return regionDecoder;
            }
            regionDecoder.mNativeSisoRegionDecoder = null;
            try {
                regionDecoder.mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(bArr, i, i2, z);
            } catch (Throwable e) {
                Log.w(TAG, e.toString());
            }
            return regionDecoder.mBitmapRegionDecoder != null ? regionDecoder : null;
        } catch (Throwable e2) {
            Log.w(TAG, e2.toString());
            return null;
        }
    }

    public Bitmap decodeRegion(Rect rect, Options options) {
        try {
            if (this.mNativeSisoRegionDecoder == null && this.mBitmapRegionDecoder != null) {
                return this.mBitmapRegionDecoder.decodeRegion(rect, Decoder.getBitmapFactoryOptions(options));
            }
            int width = rect.width();
            int height = rect.height();
            int i = options.inSampleSize;
            if (i == 0) {
                i = 1;
            }
            width = ((width + i) - 1) / i;
            height = ((height + i) - 1) / i;
            if (options.inBitmap == null || (options.inBitmap.getWidth() == width && options.inBitmap.getHeight() == height)) {
                Bitmap nativeDecodeRegion;
                synchronized (this.mNativeLock_decode) {
                    nativeDecodeRegion = nativeDecodeRegion(this.secmmrd, rect.left, rect.right, rect.top, rect.bottom, i, options.inBitmap, width, height);
                }
                return nativeDecodeRegion;
            }
            Log.v(TAG, "inBitmap Erraneous\n");
            return options.inBitmap;
        } catch (Throwable e) {
            Log.w(TAG, e.toString());
            return options.inBitmap;
        }
    }

    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    public int getHeight() {
        synchronized (this.mNativeLock) {
            checkRecycled("getHeight called on recycled region decoder");
            if (this.mNativeSisoRegionDecoder != null || this.mBitmapRegionDecoder == null) {
                int nativeGetHeight = nativeGetHeight(this.secmmrd);
                return nativeGetHeight;
            }
            nativeGetHeight = this.mBitmapRegionDecoder.getHeight();
            return nativeGetHeight;
        }
    }

    public int getWidth() {
        synchronized (this.mNativeLock) {
            checkRecycled("getWidth called on recycled region decoder");
            if (this.mNativeSisoRegionDecoder != null || this.mBitmapRegionDecoder == null) {
                int nativeGetWidth = nativeGetWidth(this.secmmrd);
                return nativeGetWidth;
            }
            nativeGetWidth = this.mBitmapRegionDecoder.getWidth();
            return nativeGetWidth;
        }
    }

    public final boolean isRecycled() {
        return this.mRecycled;
    }

    public void recycle() {
        synchronized (this.mNativeLock) {
            synchronized (this.mNativeLock_decode) {
                if (!this.mRecycled) {
                    nativerecycle(this.secmmrd);
                    this.secmmrd = 0;
                    this.mRecycled = true;
                }
            }
        }
    }

    public void requestCancelDecode() {
        synchronized (this.mNativeLock) {
            if (!this.mRecycled) {
                RequestCancelDecode(this.secmmrd);
            }
            recycle();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int useRowDecode() {
        /*
        r6 = this;
        r1 = r6.mNativeLock;
        monitor-enter(r1);
        r0 = r6.mRecycled;	 Catch:{ all -> 0x001a }
        if (r0 != 0) goto L_0x0017;
    L_0x0007:
        r2 = r6.secmmrd;	 Catch:{ all -> 0x001a }
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x0017;
    L_0x000f:
        r2 = r6.secmmrd;	 Catch:{ all -> 0x001a }
        r0 = configLTN(r2);	 Catch:{ all -> 0x001a }
        monitor-exit(r1);
        return r0;
    L_0x0017:
        monitor-exit(r1);
        r0 = 0;
        return r0;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.graphics.RegionDecoder.useRowDecode():int");
    }
}
