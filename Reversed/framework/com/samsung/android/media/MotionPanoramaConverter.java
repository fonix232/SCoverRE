package com.samsung.android.media;

import android.util.Log;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD.BufferData;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD.ContentMetaData;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.EncoderInterface;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.MotionPanoramaInfo;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.OffscreenRenderer;
import com.samsung.android.app.interactivepanoramaviewer.util.JniUtil;
import com.samsung.android.transcode.core.Encode.BitRate;
import com.sec.android.app.interactiveshot.jni.MP4Writer;
import com.sec.android.app.interactiveshot.jni.MP4Writer.VmVideoColorFormat;
import com.sec.android.app.interactiveshot.jni.MP4Writer.VmVideoQuality;
import java.io.File;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.microedition.khronos.opengles.GL10;

public class MotionPanoramaConverter {
    private static final /* synthetic */ int[] f16xc77b37de = null;
    static final int QUEUE_CAPAPCITY = 2;
    private static final String TAG = "MotionPanoramaConverter";
    private static final int TARGET_BIT_RATE = 16000000;
    private static final int TARGET_COLOUR_FORMAT = VmVideoColorFormat.VM_COLOR_FORMAT_RGB565.getValue();
    private static int TARGET_ENCODE_HEIGHT = 720;
    private static int TARGET_ENCODE_WIDTH = GL10.GL_INVALID_ENUM;
    private static final int TARGET_FRAME_RATE = 30;
    private static final int TARGET_QUALITY = VmVideoQuality.VM_QUALITY_HIGH.getValue();
    private static MotionPanoramaConverter mInstance;
    boolean TWICE_ENCODING = false;
    volatile boolean bStopRequested;
    Object lock = new Object();
    long mDecodeTime;
    BlockingQueue<byte[]> mDecoderGLQueue = new ArrayBlockingQueue(2);
    long mEncodeTime;
    private ENCODER mEncoder = ENCODER.ANDROID_ENCODER;
    Thread mEncoderThread;
    BlockingQueue<byte[]> mGLEncoderQueue = new ArrayBlockingQueue(2);
    Thread mGLThread;
    long mGlTime;
    volatile boolean mIsEncoderInit = false;
    String mOutSharePath;
    long mSEFTime;
    long mTotalTime;

    private enum ENCODER {
        NATIVE,
        MP4_CONVERTER,
        ANDROID_ENCODER
    }

    private static /* synthetic */ int[] m12x2af74482() {
        if (f16xc77b37de != null) {
            return f16xc77b37de;
        }
        int[] iArr = new int[ENCODER.values().length];
        try {
            iArr[ENCODER.ANDROID_ENCODER.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[ENCODER.MP4_CONVERTER.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[ENCODER.NATIVE.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        f16xc77b37de = iArr;
        return iArr;
    }

    private MotionPanoramaConverter() {
    }

    private void decodeMp4(MotionPanoramaInfo motionPanoramaInfo) {
        long currentTimeMillis;
        Throwable e;
        Log.v(TAG, "decodeMp4 entry");
        this.mDecodeTime = System.currentTimeMillis();
        DecoderInterfaceFHD decoderInterfaceFHD = new DecoderInterfaceFHD(motionPanoramaInfo.mVideoPath);
        decoderInterfaceFHD.init(true);
        ContentMetaData contentMetaData = decoderInterfaceFHD.getContentMetaData();
        this.mDecodeTime = System.currentTimeMillis() - this.mDecodeTime;
        Log.v(TAG, "share decoder  mp4 width : " + contentMetaData.mWidth + " mp4 height: " + contentMetaData.mHeight + "decode init time: " + this.mDecodeTime);
        byte[][] bArr = motionPanoramaInfo.getResize() ? (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, 1382400}) : (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, ((contentMetaData.mWidth * contentMetaData.mHeight) * 3) / 2});
        int i = 0;
        int i2 = 0;
        decoderInterfaceFHD.getClass();
        BufferData bufferData = new BufferData(decoderInterfaceFHD);
        Object obj = new byte[1];
        int i3 = motionPanoramaInfo.mLastFrame;
        int i4 = 0;
        while (true) {
            int i5;
            try {
                currentTimeMillis = System.currentTimeMillis();
                bufferData.bDirty = false;
                i5 = i4 + 1;
                try {
                    bufferData.mBuffer = bArr[i4];
                    if (i5 == 4) {
                        i5 = 0;
                    }
                    boolean nextframe = i2 == i3 ? false : decoderInterfaceFHD.nextframe(bufferData);
                    this.mDecodeTime += System.currentTimeMillis() - currentTimeMillis;
                    Log.v(TAG, "share decode isDecode= " + nextframe);
                    if (nextframe) {
                        i++;
                        Log.v(TAG, "share decode nextframe done " + i);
                        if (bufferData.bDirty) {
                            this.mDecoderGLQueue.put(bufferData.mBuffer);
                            i2++;
                            bufferData.bDirty = false;
                        }
                    } else {
                        this.mDecoderGLQueue.put(obj);
                    }
                    if (!nextframe || this.bStopRequested) {
                        break;
                    }
                    i4 = i5;
                } catch (InterruptedException e2) {
                    e = e2;
                }
            } catch (InterruptedException e3) {
                e = e3;
                i5 = i4;
            }
        }
        currentTimeMillis = System.currentTimeMillis();
        decoderInterfaceFHD.deInit();
        this.mDecodeTime += System.currentTimeMillis() - currentTimeMillis;
        Log.v(TAG, "decodeMp4 completed");
        e.printStackTrace();
        currentTimeMillis = System.currentTimeMillis();
        decoderInterfaceFHD.deInit();
        this.mDecodeTime += System.currentTimeMillis() - currentTimeMillis;
        Log.v(TAG, "decodeMp4 completed");
    }

    private void encodeMp4(String str, MotionPanoramaInfo motionPanoramaInfo) {
        EncoderInterface encoderInterface;
        long currentTimeMillis;
        Log.v(TAG, "encodeMp4 entry");
        this.mEncodeTime = System.currentTimeMillis();
        MP4Writer mP4Writer = null;
        File file = new File(str);
        int i = 0;
        switch (m12x2af74482()[this.mEncoder.ordinal()]) {
            case 1:
                encoderInterface = new EncoderInterface();
                i = encoderInterface.init(TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT, TARGET_BIT_RATE, 30, 1, 21, file.getAbsolutePath(), motionPanoramaInfo.mOrientation);
                break;
            case 2:
                encoderInterface = null;
                break;
            case 3:
                mP4Writer = MP4Writer.getInstance();
                i = mP4Writer.InitMp4EngineJava(TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT, file.getAbsolutePath(), 0, TARGET_QUALITY, 30, TARGET_COLOUR_FORMAT);
                encoderInterface = null;
                break;
            default:
                encoderInterface = null;
                break;
        }
        this.mEncodeTime = System.currentTimeMillis() - this.mEncodeTime;
        Log.v(TAG, "share init done with retVal" + i + " encode init time" + this.mEncodeTime);
        this.mIsEncoderInit = true;
        int i2 = 0;
        ArrayList arrayList = new ArrayList();
        Buffer allocateDirect = ByteBuffer.allocateDirect((TARGET_ENCODE_WIDTH * TARGET_ENCODE_HEIGHT) * 4);
        Buffer allocateDirect2 = ByteBuffer.allocateDirect(((TARGET_ENCODE_WIDTH * TARGET_ENCODE_HEIGHT) * 3) / 2);
        byte[] bArr = new byte[(((TARGET_ENCODE_WIDTH * TARGET_ENCODE_HEIGHT) * 3) / 2)];
        while (!this.bStopRequested) {
            try {
                byte[] bArr2 = (byte[]) this.mGLEncoderQueue.take();
                if (bArr2.length == 1) {
                    if (this.TWICE_ENCODING && arrayList.size() > 0) {
                        arrayList.remove(arrayList.size() - 1);
                        currentTimeMillis = System.currentTimeMillis();
                        switch (m12x2af74482()[this.mEncoder.ordinal()]) {
                            case 1:
                                encoderInterface.setTimeOutUs(BitRate.MIN_VIDEO_D1_BITRATE);
                                break;
                        }
                        while (!this.bStopRequested && arrayList.size() > 0) {
                            byte[] bArr3 = (byte[]) arrayList.remove(arrayList.size() - 1);
                            switch (m12x2af74482()[this.mEncoder.ordinal()]) {
                                case 1:
                                    encoderInterface.encode(bArr3);
                                    break;
                                case 3:
                                    mP4Writer.EncodeFrame(bArr3);
                                    break;
                            }
                            i2++;
                            Log.v(TAG, "share EncodeFrame done : " + i2 + "ret value =" + i);
                        }
                        this.mEncodeTime += System.currentTimeMillis() - currentTimeMillis;
                    }
                    currentTimeMillis = System.currentTimeMillis();
                    switch (m12x2af74482()[this.mEncoder.ordinal()]) {
                        case 1:
                            encoderInterface.deinit();
                            break;
                        case 3:
                            mP4Writer.DeInitMP4Engine();
                            break;
                    }
                    this.mEncodeTime += System.currentTimeMillis() - currentTimeMillis;
                    Log.v(TAG, "encodeMp4 completed ret value" + i);
                }
                currentTimeMillis = System.currentTimeMillis();
                allocateDirect.put(bArr2);
                allocateDirect.position(0);
                allocateDirect2.position(0);
                JniUtil.swABGR8888ToNV12(allocateDirect, allocateDirect2, TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT);
                allocateDirect.position(0);
                allocateDirect2.position(0);
                allocateDirect2.get(bArr);
                switch (m12x2af74482()[this.mEncoder.ordinal()]) {
                    case 1:
                        encoderInterface.encode(bArr);
                        break;
                    case 3:
                        mP4Writer.EncodeFrame(bArr);
                        break;
                }
                if (this.TWICE_ENCODING) {
                    Object obj = new byte[bArr.length];
                    System.arraycopy(bArr, 0, obj, 0, bArr.length);
                    arrayList.add(obj);
                }
                i2++;
                Log.v(TAG, "share EncodeFrame done : " + i2 + "ret value =" + i);
                this.mEncodeTime += System.currentTimeMillis() - currentTimeMillis;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        currentTimeMillis = System.currentTimeMillis();
        switch (m12x2af74482()[this.mEncoder.ordinal()]) {
            case 1:
                encoderInterface.deinit();
                break;
            case 3:
                mP4Writer.DeInitMP4Engine();
                break;
        }
        this.mEncodeTime += System.currentTimeMillis() - currentTimeMillis;
        Log.v(TAG, "encodeMp4 completed ret value" + i);
    }

    public static synchronized MotionPanoramaConverter getInstance() {
        MotionPanoramaConverter motionPanoramaConverter;
        synchronized (MotionPanoramaConverter.class) {
            if (mInstance == null) {
                mInstance = new MotionPanoramaConverter();
            }
            motionPanoramaConverter = mInstance;
        }
        return motionPanoramaConverter;
    }

    private void processFrame(MotionPanoramaInfo motionPanoramaInfo) {
        long currentTimeMillis;
        int i;
        int i2;
        Throwable e;
        Log.v(TAG, "processFrame entry");
        this.mGlTime = System.currentTimeMillis();
        OffscreenRenderer.offscreenInitialize(null, motionPanoramaInfo.mWidth, motionPanoramaInfo.mHeight, motionPanoramaInfo.mCropX, motionPanoramaInfo.mCropY, motionPanoramaInfo.mCropWidth, motionPanoramaInfo.mCropHeight, TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT, 0, motionPanoramaInfo.transformRotation(), false);
        this.mGlTime = System.currentTimeMillis() - this.mGlTime;
        Log.v(TAG, "gl init time: " + this.mGlTime);
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, (TARGET_ENCODE_WIDTH * TARGET_ENCODE_HEIGHT) * 4});
        Object obj = new byte[1];
        int i3 = 0;
        int i4 = 0;
        while (!this.bStopRequested) {
            try {
                byte[] bArr2 = (byte[]) this.mDecoderGLQueue.take();
                if (bArr2.length == 1) {
                    this.mGLEncoderQueue.put(obj);
                    break;
                }
                currentTimeMillis = System.currentTimeMillis();
                i = i4 + 1;
                try {
                    Object obj2 = bArr[i4];
                    if (i == 4) {
                        i = 0;
                    }
                    i2 = i3 + 1;
                    try {
                        OffscreenRenderer.offscreenTransformFrame(bArr2, obj2, motionPanoramaInfo.mSRCMatrix[i3]);
                        this.mGLEncoderQueue.put(obj2);
                        this.mGlTime += System.currentTimeMillis() - currentTimeMillis;
                        i3 = i2;
                        i4 = i;
                    } catch (InterruptedException e2) {
                        e = e2;
                    }
                } catch (InterruptedException e3) {
                    e = e3;
                    i2 = i3;
                }
            } catch (InterruptedException e4) {
                e = e4;
                i2 = i3;
                i = i4;
            }
        }
        i2 = i3;
        i = i4;
        currentTimeMillis = System.currentTimeMillis();
        OffscreenRenderer.offscreenFinalize();
        this.mGlTime += System.currentTimeMillis() - currentTimeMillis;
        Log.v(TAG, "processFrame completed");
        e.printStackTrace();
        currentTimeMillis = System.currentTimeMillis();
        OffscreenRenderer.offscreenFinalize();
        this.mGlTime += System.currentTimeMillis() - currentTimeMillis;
        Log.v(TAG, "processFrame completed");
    }

    private void stopShare() {
        Log.v(TAG, "stopShare entry");
        this.bStopRequested = true;
        if (this.mGLThread != null) {
            this.mGLThread.interrupt();
        }
        if (this.mEncoderThread != null) {
            this.mEncoderThread.interrupt();
        }
        synchronized (this.lock) {
            if (this.mOutSharePath != null) {
                new File(this.mOutSharePath).delete();
            }
        }
        Log.v(TAG, "stopShare exit");
    }

    public synchronized void convertToMP4(String str, String str2) {
        this.mOutSharePath = str2;
        synchronized (this.lock) {
            if (this.bStopRequested) {
                return;
            }
            this.mTotalTime = System.currentTimeMillis();
            Log.v(TAG, "Internal version = 1.5");
            Log.v(TAG, "share entry file to be shared: " + str + " output file: " + str2);
            long currentTimeMillis = System.currentTimeMillis();
            final MotionPanoramaInfo instance = MotionPanoramaInfo.getInstance();
            if (instance.init(str, str2)) {
                int i;
                instance.printInfo();
                Log.v(TAG, "share offscreenInitialize width: " + instance.mWidth + " height: " + instance.mHeight + " cropx: " + instance.mCropX + " cropy: " + instance.mCropY + " cropWidth: " + instance.mCropWidth + " cropHeight: " + instance.mCropHeight + " orientation: " + instance.mOrientation + " captureMode: " + instance.transformRotation());
                TARGET_ENCODE_WIDTH = instance.mWidth;
                TARGET_ENCODE_HEIGHT = instance.mHeight;
                if (instance.mOrientation == 90 || instance.mOrientation == 270) {
                    i = TARGET_ENCODE_WIDTH;
                    TARGET_ENCODE_WIDTH = TARGET_ENCODE_HEIGHT;
                    TARGET_ENCODE_HEIGHT = i;
                }
                this.mSEFTime = System.currentTimeMillis() - currentTimeMillis;
                instance.printInfo();
                Log.v(TAG, "share offscreenInitialize width: " + instance.mWidth + " height: " + instance.mHeight + " cropx: " + instance.mCropX + " cropy: " + instance.mCropY + " cropWidth: " + instance.mCropWidth + " cropHeight: " + instance.mCropHeight + " orientation: " + instance.mOrientation + " captureMode: " + instance.transformRotation());
                final String str3 = str2;
                this.mEncoderThread = new Thread(new Runnable() {
                    public void run() {
                        MotionPanoramaConverter.this.encodeMp4(str3, instance);
                    }
                });
                this.mEncoderThread.start();
                this.mGLThread = new Thread(new Runnable() {
                    public void run() {
                        MotionPanoramaConverter.this.processFrame(instance);
                    }
                });
                this.mGLThread.start();
                decodeMp4(instance);
                try {
                    if (this.mEncoderThread.isAlive()) {
                        this.mEncoderThread.join();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                this.mIsEncoderInit = false;
                if (instance.mVideoPath != null) {
                    new File(instance.mVideoPath).delete();
                }
                MotionPanoramaInfo.freeInstance();
                if (instance.mOrientation == 90 || instance.mOrientation == 270) {
                    i = TARGET_ENCODE_WIDTH;
                    TARGET_ENCODE_WIDTH = TARGET_ENCODE_HEIGHT;
                    TARGET_ENCODE_HEIGHT = i;
                }
                Log.v(TAG, "share exit Total Time: mTotalTime " + (System.currentTimeMillis() - this.mTotalTime) + " encode: " + this.mEncodeTime + " decode: " + this.mDecodeTime + " opengl: " + this.mGlTime + " sef: " + this.mSEFTime);
                return;
            }
            Log.d(TAG, "SEF init fails");
        }
    }
}
