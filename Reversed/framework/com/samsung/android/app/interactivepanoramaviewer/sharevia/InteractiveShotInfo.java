package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.os.Environment;
import android.util.Log;
import com.samsung.android.media.SemExtendedFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class InteractiveShotInfo implements AppController {
    static InteractiveShotInfo mInstance;
    final String KEY_INFO = "Interactive_Panorama_Info";
    final String KEY_VIDEO = "Interactive_Panorama_000";
    final String TAG = "InteractiveShotInfo";
    private boolean bInit = false;
    private boolean isSEFDataFlagNull = false;
    public int mAlgoType = -1;
    public int mAlgoVersion = -1;
    public int mCameraType = -1;
    public int mCaptureDirection = -1;
    public int mCropHeight = -1;
    public int mCropWidth = -1;
    public int mCropX = -1;
    public int mCropY = -1;
    private String mFilePath;
    public int mFirstFrame = -1;
    public int mHeight = -1;
    public int mLastFrame = -1;
    public int mMatrixHeight = -1;
    public int mMatrixWidth = -1;
    public int mOrientation = -1;
    public float[][] mSRCMatrix;
    public int mTotalFrames = -1;
    public byte[] mVideo;
    public int mVideoLength = -1;
    public String mVideoPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(this.tempPath).toString();
    public int mVideoStart = -1;
    public int mWidth = -1;
    private File sefFile;
    private String tempPath = "/.interactivePano.mp4";

    private InteractiveShotInfo() {
    }

    public static synchronized void freeInstance() {
        synchronized (InteractiveShotInfo.class) {
            mInstance = null;
        }
    }

    private void getInfo(String str) {
        FileOutputStream fileOutputStream;
        Throwable th;
        try {
            byte[] data = SemExtendedFormat.getData(this.sefFile, "Interactive_Panorama_Info");
            if (data != null) {
                this.mWidth = getInt(data, 0);
                this.mHeight = getInt(data, 1);
                this.mTotalFrames = getInt(data, 2);
                this.mFirstFrame = getInt(data, 3);
                this.mLastFrame = getInt(data, 4);
                if (this.mTotalFrames != 0 && (this.mLastFrame - this.mFirstFrame) + 1 > 0) {
                    this.mAlgoType = getInt(data, 5);
                    this.mAlgoVersion = getInt(data, 6);
                    this.mOrientation = getInt(data, 7);
                    this.mCaptureDirection = getInt(data, 8);
                    this.mCameraType = getInt(data, 9);
                    this.mCropX = getInt(data, 10);
                    this.mCropY = getInt(data, 11);
                    this.mCropWidth = getInt(data, 12);
                    this.mCropHeight = getInt(data, 13);
                    this.mMatrixWidth = getInt(data, 14);
                    this.mMatrixHeight = getInt(data, 15);
                    int i = this.mTotalFrames;
                    this.mSRCMatrix = (float[][]) Array.newInstance(Float.TYPE, new int[]{i, 9});
                    for (i = 0; i < this.mSRCMatrix.length; i++) {
                        for (int i2 = 0; i2 < this.mSRCMatrix[i].length; i2++) {
                            this.mSRCMatrix[i][i2] = (float) (((double) getInt(data, ((this.mSRCMatrix[i].length * i) + i2) + 16)) / 1000000.0d);
                        }
                    }
                    byte[] data2 = SemExtendedFormat.getData(this.sefFile, "Interactive_Panorama_000");
                    if (data2 != null) {
                        String parentDirPath = getParentDirPath(str);
                        if (parentDirPath != null) {
                            this.mVideoPath = new StringBuilder(String.valueOf(parentDirPath)).append(this.tempPath).toString();
                        }
                        Log.m35v("InteractiveShotInfo", "input video file path=  " + this.mVideoPath);
                        try {
                            fileOutputStream = new FileOutputStream(this.mVideoPath);
                            try {
                                fileOutputStream.write(data2);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                return;
                            } catch (Throwable th2) {
                                th = th2;
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = null;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            throw th;
                        }
                    } else {
                        this.isSEFDataFlagNull = true;
                        Log.m33i("InteractiveShotInfo", "SEF Data Null");
                        return;
                    }
                }
                Log.m33i("InteractiveShotInfo", "Total Frames is less than or equal to Zero ");
                return;
            }
            Log.m33i("InteractiveShotInfo", "SEF:getSEFData KEY_INFO returns null");
        } catch (Throwable th4) {
            th4.printStackTrace();
        } catch (Throwable th42) {
            th42.printStackTrace();
        }
    }

    public static synchronized InteractiveShotInfo getInstance() {
        InteractiveShotInfo interactiveShotInfo;
        synchronized (InteractiveShotInfo.class) {
            if (mInstance == null) {
                mInstance = new InteractiveShotInfo();
            }
            interactiveShotInfo = mInstance;
        }
        return interactiveShotInfo;
    }

    private int getInt(byte[] bArr, int i) throws IOException {
        int i2 = i * 4;
        return ((bArr[i2 + 3] & 255) << 24) | (((bArr[i2] & 255) | ((bArr[i2 + 1] & 255) << 8)) | ((bArr[i2 + 2] & 255) << 16));
    }

    public static String getParentDirPath(String str) {
        try {
            return str.substring(0, str.lastIndexOf(File.separatorChar, str.length() - 1));
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getResize() {
        return false;
    }

    public void getResizedValue(int i, int i2) {
        if (i != i2) {
            float f = ((float) i) / ((float) i2);
            for (int i3 = 0; i3 < this.mSRCMatrix.length; i3++) {
                this.mSRCMatrix[i3][6] = this.mSRCMatrix[i3][6] / f;
                this.mSRCMatrix[i3][7] = this.mSRCMatrix[i3][7] / f;
            }
            this.mWidth = 1280;
            this.mHeight = 720;
            this.mCropX = (int) (((float) this.mCropX) / f);
            this.mCropY = (int) (((float) this.mCropY) / f);
            this.mCropWidth = (int) (((float) this.mCropWidth) / f);
            this.mCropHeight = (int) (((float) this.mCropHeight) / f);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean init(java.lang.String r6, java.lang.String r7) {
        /*
        r5 = this;
        r4 = 1;
        r3 = 0;
        monitor-enter(r5);
        r0 = "InteractiveShotInfo";
        r1 = "frozen moment sharevia version 1.0.8";
        android.util.Log.m35v(r0, r1);	 Catch:{ all -> 0x0058 }
        r0 = "InteractiveShotInfo";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0058 }
        r2 = "bInit ";
        r1.<init>(r2);	 Catch:{ all -> 0x0058 }
        r2 = r5.bInit;	 Catch:{ all -> 0x0058 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0058 }
        r1 = r1.toString();	 Catch:{ all -> 0x0058 }
        android.util.Log.m35v(r0, r1);	 Catch:{ all -> 0x0058 }
        r0 = r5.bInit;	 Catch:{ all -> 0x0058 }
        if (r0 == 0) goto L_0x002a;
    L_0x0028:
        monitor-exit(r5);
        return r4;
    L_0x002a:
        r5.mFilePath = r6;	 Catch:{ all -> 0x0058 }
        r0 = new java.io.File;	 Catch:{ all -> 0x0058 }
        r1 = r5.mFilePath;	 Catch:{ all -> 0x0058 }
        r0.<init>(r1);	 Catch:{ all -> 0x0058 }
        r5.sefFile = r0;	 Catch:{ all -> 0x0058 }
        r0 = r5.sefFile;	 Catch:{ all -> 0x0058 }
        r0 = r0.exists();	 Catch:{ all -> 0x0058 }
        if (r0 == 0) goto L_0x0049;
    L_0x003d:
        r5.getInfo(r7);	 Catch:{ all -> 0x0058 }
        r0 = r5.isSEFDataFlagNull;	 Catch:{ all -> 0x0058 }
        if (r0 == 0) goto L_0x0054;
    L_0x0044:
        r0 = 0;
        r5.bInit = r0;	 Catch:{ all -> 0x0058 }
        monitor-exit(r5);
        return r3;
    L_0x0049:
        r0 = "InteractiveShotInfo";
        r1 = "SEF File not exist";
        android.util.Log.m29d(r0, r1);	 Catch:{ all -> 0x0058 }
        monitor-exit(r5);
        return r3;
    L_0x0054:
        r0 = 1;
        r5.bInit = r0;	 Catch:{ all -> 0x0058 }
        goto L_0x0028;
    L_0x0058:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.app.interactivepanoramaviewer.sharevia.InteractiveShotInfo.init(java.lang.String, java.lang.String):boolean");
    }

    public void printInfo() {
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mWidth);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mHeight);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mTotalFrames);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mFirstFrame);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mLastFrame);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mAlgoType);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mAlgoVersion);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mOrientation);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCaptureDirection);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCameraType);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCropX);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCropY);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCropWidth);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mCropHeight);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mMatrixWidth);
        Log.m29d("InteractiveShotInfo", "IP:: values: " + this.mMatrixHeight);
        for (float[] arrays : this.mSRCMatrix) {
            Log.m29d("InteractiveShotInfo", "IP:: values: " + Arrays.toString(arrays));
        }
    }

    public int transformRotation() {
        switch (this.mOrientation) {
            case 90:
                return 1;
            case 180:
                return 2;
            case 270:
                return 3;
            default:
                return 0;
        }
    }
}
