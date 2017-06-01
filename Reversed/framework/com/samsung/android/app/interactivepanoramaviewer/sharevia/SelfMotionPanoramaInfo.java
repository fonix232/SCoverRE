package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.os.Environment;
import android.util.Log;
import com.samsung.android.media.SemExtendedFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class SelfMotionPanoramaInfo implements AppController {
    private static final String SEF_KEYNAME_SOUND_SHOT_WAVE = "Motion_Panorama_AAC_000";
    private static final int SMP_HEIGHT = 1440;
    private static final int SMP_WIDTH = 1920;
    static SelfMotionPanoramaInfo mInstance;
    final String KEY_INFO = "Wide_Selfie_Motion_Info";
    final String KEY_VIDEO = "Wide_Selfie_Motion_MP4_000";
    final String TAG = "InteractivePano_SelfMotionPanoramaInfo";
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
    public float[][] mExtraFramesEndMatrix;
    public float[][] mExtraFramesStartMatrix;
    private String mFilePath;
    public int mFirstFrame = -1;
    public int[] mFrameRendererFlag;
    public int mHeight = -1;
    public boolean mIsFilpRequired;
    public int mLastFrame = -1;
    public int mMatrixHeight = -1;
    public int mMatrixWidth = -1;
    public int mMaxInputFrameCount;
    public int mNumExtraFramesEnd;
    public int mNumExtraFramesStart;
    public int mOrientation = -1;
    public float[][] mSRCMatrix;
    public long[] mTimeStamp;
    public int mTotalFrames = -1;
    public byte[] mVideo;
    public int mVideoLength = -1;
    public String mVideoPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(this.tempPath).toString();
    public int mVideoStart = -1;
    public int mWidth = -1;
    public int maxFramesForViewAngleFitting;
    private File sefFile;
    private String tempPath = "/.interactivePano.mp4";

    private SelfMotionPanoramaInfo() {
    }

    public static synchronized void freeInstance() {
        synchronized (SelfMotionPanoramaInfo.class) {
            mInstance = null;
        }
    }

    private boolean getInfo(String str) {
        FileOutputStream fileOutputStream;
        Throwable th;
        try {
            byte[] data = SemExtendedFormat.getData(this.sefFile, "Wide_Selfie_Motion_Info");
            if (data != null) {
                this.mWidth = getInt(data, 0);
                this.mHeight = getInt(data, 1);
                this.mTotalFrames = getInt(data, 2);
                this.mFirstFrame = getInt(data, 3);
                this.mLastFrame = getInt(data, 4);
                if (this.mWidth == SMP_WIDTH && this.mHeight == SMP_HEIGHT && this.mTotalFrames > 0 && this.mFirstFrame >= 0 && this.mLastFrame >= 0 && (this.mLastFrame - this.mFirstFrame) + 1 > 0 && this.mLastFrame <= this.mTotalFrames) {
                    int i;
                    this.mAlgoType = getInt(data, 5);
                    this.mAlgoVersion = getInt(data, 6);
                    this.mOrientation = getInt(data, 7);
                    this.mCaptureDirection = getInt(data, 8);
                    this.mCameraType = getInt(data, 9);
                    this.mIsFilpRequired = getInt(data, 10) == 1;
                    this.mMaxInputFrameCount = getInt(data, 11);
                    this.mCropX = getInt(data, 12);
                    this.mCropY = getInt(data, 13);
                    this.mCropWidth = getInt(data, 14);
                    this.mCropHeight = getInt(data, 15);
                    this.mMatrixWidth = getInt(data, 16);
                    this.mMatrixHeight = getInt(data, 17);
                    int i2 = this.mTotalFrames;
                    this.mSRCMatrix = (float[][]) Array.newInstance(Float.TYPE, new int[]{i2, 9});
                    for (i2 = 0; i2 < this.mSRCMatrix.length; i2++) {
                        for (i = 0; i < this.mSRCMatrix[i2].length; i++) {
                            this.mSRCMatrix[i2][i] = (float) (((double) getInt(data, ((this.mSRCMatrix[i2].length * i2) + i) + 18)) / 1000000.0d);
                        }
                    }
                    this.mTimeStamp = new long[this.mTotalFrames];
                    i = 36072;
                    for (i2 = 0; i2 < this.mTotalFrames; i2++) {
                        this.mTimeStamp[i2] = getLong(data, i);
                        i += 8;
                    }
                    this.mFrameRendererFlag = new int[this.mTotalFrames];
                    i = 12018;
                    i2 = 0;
                    while (i2 < this.mTotalFrames) {
                        int i3 = i + 1;
                        this.mFrameRendererFlag[i2] = getInt(data, i);
                        i2++;
                        i = i3;
                    }
                    Log.m35v("InteractivePano_SelfMotionPanoramaInfo", "offset 2 =" + 13018);
                    updateExtraFrameData(data, 13018);
                    byte[] data2 = SemExtendedFormat.getData(this.sefFile, "Wide_Selfie_Motion_MP4_000");
                    if (data2 != null) {
                        String parentDirPath = getParentDirPath(str);
                        if (parentDirPath != null) {
                            this.mVideoPath = new StringBuilder(String.valueOf(parentDirPath)).append(this.tempPath).toString();
                        }
                        Log.m35v("InteractivePano_SelfMotionPanoramaInfo", "input video file path=  " + this.mVideoPath);
                        try {
                            fileOutputStream = new FileOutputStream(this.mVideoPath);
                            try {
                                fileOutputStream.write(data2);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                                return true;
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
                        Log.m33i("InteractivePano_SelfMotionPanoramaInfo", "SEF Data Null");
                        return false;
                    }
                }
                Log.m33i("InteractivePano_SelfMotionPanoramaInfo", "Total Frames is less than or equal to Zero ");
                return false;
            }
            Log.m33i("InteractivePano_SelfMotionPanoramaInfo", "SEF:getSEFData KEY_INFO returns null");
            return false;
        } catch (Throwable th4) {
            th4.printStackTrace();
            return false;
        } catch (Throwable th42) {
            th42.printStackTrace();
            return false;
        } catch (Throwable th422) {
            th422.printStackTrace();
            return false;
        }
    }

    public static synchronized SelfMotionPanoramaInfo getInstance() {
        SelfMotionPanoramaInfo selfMotionPanoramaInfo;
        synchronized (SelfMotionPanoramaInfo.class) {
            if (mInstance == null) {
                mInstance = new SelfMotionPanoramaInfo();
            }
            selfMotionPanoramaInfo = mInstance;
        }
        return selfMotionPanoramaInfo;
    }

    private int getInt(byte[] bArr, int i) throws IOException {
        int i2 = i * 4;
        return ((bArr[i2 + 3] & 255) << 24) | (((bArr[i2] & 255) | ((bArr[i2 + 1] & 255) << 8)) | ((bArr[i2 + 2] & 255) << 16));
    }

    private long getLong(byte[] bArr, int i) throws IOException {
        return (((((((((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8)) | ((((long) bArr[i + 2]) & 255) << 16)) | ((((long) bArr[i + 3]) & 255) << 24)) | ((((long) bArr[i + 4]) & 255) << 32)) | ((((long) bArr[i + 5]) & 255) << 40)) | ((((long) bArr[i + 6]) & 255) << 48)) | ((((long) bArr[i + 7]) & 255) << 56);
    }

    public static String getParentDirPath(String str) {
        try {
            return str.substring(0, str.lastIndexOf(File.separatorChar, str.length() - 1));
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateExtraFrameData(byte[] bArr, int i) {
        int i2 = i + 1;
        try {
            int i3;
            int i4;
            this.maxFramesForViewAngleFitting = getInt(bArr, i);
            int i5 = i2 + 1;
            this.mNumExtraFramesStart = getInt(bArr, i2);
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "INFO: maxFramesForViewAngleFitting = " + this.maxFramesForViewAngleFitting + " mNumExtraFramesStart=" + this.mNumExtraFramesStart);
            int i6 = i5 + (this.maxFramesForViewAngleFitting * 9);
            i2 = this.maxFramesForViewAngleFitting;
            this.mExtraFramesStartMatrix = (float[][]) Array.newInstance(Float.TYPE, new int[]{i2, 9});
            for (i2 = 0; i2 < this.mExtraFramesStartMatrix.length; i2++) {
                i3 = 0;
                while (i3 < this.mExtraFramesStartMatrix[i2].length) {
                    i4 = i5 + 1;
                    this.mExtraFramesStartMatrix[i2][i3] = (float) (((double) getInt(bArr, i5)) / 1000000.0d);
                    i3++;
                    i5 = i4;
                }
            }
            i5 = i6 + 1;
            this.mNumExtraFramesEnd = getInt(bArr, i6);
            i2 = this.maxFramesForViewAngleFitting;
            this.mExtraFramesEndMatrix = (float[][]) Array.newInstance(Float.TYPE, new int[]{i2, 9});
            for (i2 = 0; i2 < this.mExtraFramesEndMatrix.length; i2++) {
                i3 = 0;
                while (i3 < this.mExtraFramesEndMatrix[i2].length) {
                    i4 = i5 + 1;
                    this.mExtraFramesEndMatrix[i2][i3] = (float) (((double) getInt(bArr, i5)) / 1000000.0d);
                    i3++;
                    i5 = i4;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "INFO: updateExtraFrameData" + e.getMessage());
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
        r0 = "InteractivePano_SelfMotionPanoramaInfo";
        r1 = "self motion pano sharevia version 1.0.0";
        android.util.Log.m35v(r0, r1);	 Catch:{ all -> 0x0066 }
        r0 = "InteractivePano_SelfMotionPanoramaInfo";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0066 }
        r2 = "bInit ";
        r1.<init>(r2);	 Catch:{ all -> 0x0066 }
        r2 = r5.bInit;	 Catch:{ all -> 0x0066 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0066 }
        r1 = r1.toString();	 Catch:{ all -> 0x0066 }
        android.util.Log.m35v(r0, r1);	 Catch:{ all -> 0x0066 }
        r0 = r5.bInit;	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x002a;
    L_0x0028:
        monitor-exit(r5);
        return r4;
    L_0x002a:
        r5.mFilePath = r6;	 Catch:{ all -> 0x0066 }
        r0 = new java.io.File;	 Catch:{ all -> 0x0066 }
        r1 = r5.mFilePath;	 Catch:{ all -> 0x0066 }
        r0.<init>(r1);	 Catch:{ all -> 0x0066 }
        r5.sefFile = r0;	 Catch:{ all -> 0x0066 }
        r0 = r5.sefFile;	 Catch:{ all -> 0x0066 }
        r0 = r0.exists();	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x004c;
    L_0x003d:
        r0 = r5.getInfo(r7);	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0057;
    L_0x0043:
        r0 = r5.isSEFDataFlagNull;	 Catch:{ all -> 0x0066 }
        if (r0 == 0) goto L_0x0062;
    L_0x0047:
        r0 = 0;
        r5.bInit = r0;	 Catch:{ all -> 0x0066 }
        monitor-exit(r5);
        return r3;
    L_0x004c:
        r0 = "InteractivePano_SelfMotionPanoramaInfo";
        r1 = "SEF File not exist";
        android.util.Log.m29d(r0, r1);	 Catch:{ all -> 0x0066 }
        monitor-exit(r5);
        return r3;
    L_0x0057:
        r0 = "InteractivePano_SelfMotionPanoramaInfo";
        r1 = "SEF File INFO is incorrect";
        android.util.Log.m29d(r0, r1);	 Catch:{ all -> 0x0066 }
        monitor-exit(r5);
        return r3;
    L_0x0062:
        r0 = 1;
        r5.bInit = r0;	 Catch:{ all -> 0x0066 }
        goto L_0x0028;
    L_0x0066:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.app.interactivepanoramaviewer.sharevia.SelfMotionPanoramaInfo.init(java.lang.String, java.lang.String):boolean");
    }

    public void printInfo() {
        int i = 0;
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mWidth: " + this.mWidth);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mHeight: " + this.mHeight);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mTotalFrames: " + this.mTotalFrames);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mFirstFrame: " + this.mFirstFrame);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mLastFrame: " + this.mLastFrame);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mAlgoType: " + this.mAlgoType);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mAlgoVersion: " + this.mAlgoVersion);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mOrientation: " + this.mOrientation);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCaptureDirection: " + this.mCaptureDirection);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCameraType: " + this.mCameraType);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCropX: " + this.mCropX);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCropY: " + this.mCropY);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCropWidth: " + this.mCropWidth);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mCropHeight: " + this.mCropHeight);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mMatrixWidth: " + this.mMatrixWidth);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mMatrixHeight: " + this.mMatrixHeight);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "INFO: mIsFilpRequired = " + this.mIsFilpRequired);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "INFO: mMaxInputFrameCount = " + this.mMaxInputFrameCount);
        int i2 = 0;
        while (this.mSRCMatrix != null && i2 < this.mSRCMatrix.length) {
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP:: mSRCMatrix[" + i2 + "]: " + Arrays.toString(this.mSRCMatrix[i2]));
            i2++;
        }
        if (this.mFrameRendererFlag != null) {
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP :: mFrameRenderingFlags = " + Arrays.toString(this.mFrameRendererFlag));
        }
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP: maxFramesForViewAngleFitting = " + this.maxFramesForViewAngleFitting);
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP: mNumExtraFramesStart = " + this.mNumExtraFramesStart);
        i2 = 0;
        while (this.mExtraFramesStartMatrix != null && i2 < this.mExtraFramesStartMatrix.length) {
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP: mExtraFramesStartMatrix[" + i2 + "]=" + Arrays.toString(this.mExtraFramesStartMatrix[i2]));
            i2++;
        }
        Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP: mNumExtraFramesEnd = " + this.mNumExtraFramesEnd);
        while (this.mExtraFramesEndMatrix != null && i < this.mExtraFramesEndMatrix.length) {
            Log.m29d("InteractivePano_SelfMotionPanoramaInfo", "IP: mExtraFramesEndMatrix[" + i + "]=" + Arrays.toString(this.mExtraFramesEndMatrix[i]));
            i++;
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
