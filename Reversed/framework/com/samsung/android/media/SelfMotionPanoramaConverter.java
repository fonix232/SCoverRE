package com.samsung.android.media;

import android.util.Log;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD.BufferData;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.DecoderInterfaceFHD.ContentMetaData;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.EncoderInterface;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.ImageRenderer3d;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.OffscreenRenderer;
import com.samsung.android.app.interactivepanoramaviewer.sharevia.SelfMotionPanoramaInfo;
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

public class SelfMotionPanoramaConverter {
    private static final /* synthetic */ int[] f17xe31e4572 = null;
    static final int QUEUE_CAPAPCITY = 2;
    private static final String TAG = "SelfMotionPanoramaConverter";
    private static final int TARGET_BIT_RATE = 16000000;
    private static final int TARGET_COLOUR_FORMAT = VmVideoColorFormat.VM_COLOR_FORMAT_RGB565.getValue();
    private static int TARGET_ENCODE_HEIGHT = 720;
    private static int TARGET_ENCODE_WIDTH = GL10.GL_INVALID_ENUM;
    private static final int TARGET_FRAME_RATE = 30;
    private static final int TARGET_QUALITY = VmVideoQuality.VM_QUALITY_HIGH.getValue();
    private static SelfMotionPanoramaConverter mInstance;
    boolean TWICE_ENCODING = false;
    volatile boolean bStopRequested;
    Object lock = new Object();
    long mDecodeTime;
    BlockingQueue<DecodedBuffer> mDecoderGLQueue = new ArrayBlockingQueue(2);
    long mEncodeTime;
    private ENCODER mEncoder = ENCODER.ANDROID_ENCODER;
    Thread mEncoderThread;
    BlockingQueue<byte[]> mGLEncoderQueue = new ArrayBlockingQueue(2);
    Thread mGLThread;
    long mGlTime;
    volatile boolean mIsEncoderInit = false;
    String mOutSharePath;
    long mSEFTime;
    private int mTotalFrame;
    long mTotalTime;

    private static class DecodedBuffer {
        private byte[] buffer;
        private int frameNo;
        private float[] matrix;

        public DecodedBuffer(byte[] bArr, int i, float[] fArr) {
            this.buffer = bArr;
            this.frameNo = i;
            this.matrix = fArr;
        }
    }

    private enum ENCODER {
        NATIVE,
        MP4_CONVERTER,
        ANDROID_ENCODER
    }

    private static /* synthetic */ int[] m13xdabb6016() {
        if (f17xe31e4572 != null) {
            return f17xe31e4572;
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
        f17xe31e4572 = iArr;
        return iArr;
    }

    private SelfMotionPanoramaConverter() {
    }

    private void decodeMp4(SelfMotionPanoramaInfo selfMotionPanoramaInfo) {
        byte[][] bArr;
        long currentTimeMillis;
        Throwable e;
        Log.v(TAG, "decodeMp4 entry");
        this.mDecodeTime = System.currentTimeMillis();
        DecoderInterfaceFHD decoderInterfaceFHD = new DecoderInterfaceFHD(selfMotionPanoramaInfo.mVideoPath);
        decoderInterfaceFHD.init(true);
        decoderInterfaceFHD.seekToRequiredGOP(selfMotionPanoramaInfo.mFirstFrame);
        ContentMetaData contentMetaData = decoderInterfaceFHD.getContentMetaData();
        this.mDecodeTime = System.currentTimeMillis() - this.mDecodeTime;
        Log.v(TAG, "share decoder  mp4 width : " + contentMetaData.mWidth + " mp4 height: " + contentMetaData.mHeight + "decode init time: " + this.mDecodeTime);
        if (selfMotionPanoramaInfo.getResize()) {
            bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, 1382400});
        } else {
            bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, ((contentMetaData.mWidth * contentMetaData.mHeight) * 3) / 2});
        }
        int i = 0;
        int gOPInterval = selfMotionPanoramaInfo.mFirstFrame - (selfMotionPanoramaInfo.mFirstFrame % decoderInterfaceFHD.getGOPInterval());
        decoderInterfaceFHD.getClass();
        BufferData bufferData = new BufferData(decoderInterfaceFHD);
        byte[] bArr2 = new byte[1];
        int i2 = selfMotionPanoramaInfo.mLastFrame;
        int i3 = 0;
        while (true) {
            int i4;
            try {
                boolean z;
                currentTimeMillis = System.currentTimeMillis();
                bufferData.bDirty = false;
                i4 = i3 + 1;
                bufferData.mBuffer = bArr[i3];
                if (i4 == 4) {
                    i4 = 0;
                }
                if (gOPInterval > i2) {
                    z = false;
                } else {
                    try {
                        z = decoderInterfaceFHD.nextframe(bufferData);
                    } catch (InterruptedException e2) {
                        e = e2;
                    }
                }
                this.mDecodeTime += System.currentTimeMillis() - currentTimeMillis;
                Log.v(TAG, "share decode isDecode= " + z);
                if (z) {
                    i++;
                    Log.v(TAG, "share decode nextframe done " + i);
                    if (bufferData.bDirty) {
                        if (gOPInterval >= selfMotionPanoramaInfo.mFirstFrame && selfMotionPanoramaInfo.mFrameRendererFlag[gOPInterval] == 1) {
                            int i5;
                            if (gOPInterval == selfMotionPanoramaInfo.mFirstFrame) {
                                for (i5 = 0; i5 < selfMotionPanoramaInfo.mNumExtraFramesStart; i5++) {
                                    Log.v(TAG, "share decode framesDecoded sent to encode = " + gOPInterval);
                                    this.mDecoderGLQueue.put(new DecodedBuffer(bufferData.mBuffer, gOPInterval, selfMotionPanoramaInfo.mExtraFramesStartMatrix[i5]));
                                    Thread.sleep(25);
                                }
                            }
                            this.mDecoderGLQueue.put(new DecodedBuffer(bufferData.mBuffer, gOPInterval, selfMotionPanoramaInfo.mSRCMatrix[gOPInterval]));
                            if (gOPInterval == selfMotionPanoramaInfo.mLastFrame) {
                                for (i5 = 0; i5 < selfMotionPanoramaInfo.mNumExtraFramesEnd; i5++) {
                                    Log.v(TAG, "share decode framesDecoded sent to encode = " + gOPInterval);
                                    this.mDecoderGLQueue.put(new DecodedBuffer(bufferData.mBuffer, gOPInterval, selfMotionPanoramaInfo.mExtraFramesEndMatrix[i5]));
                                    Thread.sleep(25);
                                }
                            }
                            Log.v(TAG, "share decode framesDecoded sent to encode = " + gOPInterval);
                        }
                        gOPInterval++;
                        Log.v(TAG, "share decode framesDecoded = " + gOPInterval);
                        bufferData.bDirty = false;
                    }
                } else {
                    this.mDecoderGLQueue.put(new DecodedBuffer(bArr2, -1, ImageRenderer3d.IDENTITY_MATRIX));
                }
                if (!z || this.bStopRequested) {
                    break;
                }
                i3 = i4;
            } catch (InterruptedException e3) {
                e = e3;
                i4 = i3;
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

    private void encodeMp4(String str, SelfMotionPanoramaInfo selfMotionPanoramaInfo) {
        EncoderInterface encoderInterface;
        long currentTimeMillis;
        Log.v(TAG, "encodeMp4 entry");
        this.mEncodeTime = System.currentTimeMillis();
        MP4Writer mP4Writer = null;
        File file = new File(str);
        int i = 0;
        switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
            case 1:
                encoderInterface = new EncoderInterface();
                i = encoderInterface.init(TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT, TARGET_BIT_RATE, 30, 1, 21, file.getAbsolutePath(), selfMotionPanoramaInfo.mOrientation);
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
                        switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
                            case 1:
                                encoderInterface.setTimeOutUs(BitRate.MIN_VIDEO_D1_BITRATE);
                                break;
                        }
                        while (!this.bStopRequested && arrayList.size() > 0) {
                            byte[] bArr3 = (byte[]) arrayList.remove(arrayList.size() - 1);
                            switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
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
                    switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
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
                switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
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
        switch (m13xdabb6016()[this.mEncoder.ordinal()]) {
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

    public static synchronized SelfMotionPanoramaConverter getInstance() {
        SelfMotionPanoramaConverter selfMotionPanoramaConverter;
        synchronized (SelfMotionPanoramaConverter.class) {
            if (mInstance == null) {
                mInstance = new SelfMotionPanoramaConverter();
            }
            selfMotionPanoramaConverter = mInstance;
        }
        return selfMotionPanoramaConverter;
    }

    private void processFrame(SelfMotionPanoramaInfo selfMotionPanoramaInfo) {
        long currentTimeMillis;
        int i;
        Throwable e;
        Log.v(TAG, "processFrame entry");
        this.mGlTime = System.currentTimeMillis();
        OffscreenRenderer.offscreenInitialize(null, selfMotionPanoramaInfo.mWidth, selfMotionPanoramaInfo.mHeight, selfMotionPanoramaInfo.mCropX, selfMotionPanoramaInfo.mCropY, selfMotionPanoramaInfo.mCropWidth, selfMotionPanoramaInfo.mCropHeight, TARGET_ENCODE_WIDTH, TARGET_ENCODE_HEIGHT, 0, selfMotionPanoramaInfo.transformRotation(), selfMotionPanoramaInfo.mIsFilpRequired);
        this.mGlTime = System.currentTimeMillis() - this.mGlTime;
        Log.v(TAG, "gl init time: " + this.mGlTime);
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{4, (TARGET_ENCODE_WIDTH * TARGET_ENCODE_HEIGHT) * 4});
        Object obj = new byte[1];
        int i2 = 0;
        while (!this.bStopRequested) {
            try {
                DecodedBuffer decodedBuffer = (DecodedBuffer) this.mDecoderGLQueue.take();
                byte[] -get0 = decodedBuffer.buffer;
                if (-get0.length == 1) {
                    this.mGLEncoderQueue.put(obj);
                    break;
                }
                currentTimeMillis = System.currentTimeMillis();
                i = i2 + 1;
                try {
                    Object obj2 = bArr[i2];
                    if (i == 4) {
                        i = 0;
                    }
                    OffscreenRenderer.offscreenTransformFrame(-get0, obj2, decodedBuffer.matrix);
                    Log.v(TAG, "encoded actual frame no = " + decodedBuffer.frameNo);
                    this.mGLEncoderQueue.put(obj2);
                    this.mGlTime += System.currentTimeMillis() - currentTimeMillis;
                    i2 = i;
                } catch (InterruptedException e2) {
                    e = e2;
                }
            } catch (InterruptedException e3) {
                e = e3;
                i = i2;
            }
        }
        i = i2;
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
            final SelfMotionPanoramaInfo instance = SelfMotionPanoramaInfo.getInstance();
            if (instance.init(str, str2)) {
                int i;
                instance.printInfo();
                Log.v(TAG, "share offscreenInitialize width: " + instance.mWidth + " height: " + instance.mHeight + " cropx: " + instance.mCropX + " cropy: " + instance.mCropY + " cropWidth: " + instance.mCropWidth + " cropHeight: " + instance.mCropHeight + " orientation: " + instance.mOrientation + " captureMode: " + instance.transformRotation() + " mFirstFrame: " + instance.mFirstFrame + " mLastFrame: " + instance.mLastFrame);
                this.mTotalFrame = instance.mLastFrame - instance.mFirstFrame;
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
                        SelfMotionPanoramaConverter.this.encodeMp4(str3, instance);
                    }
                });
                this.mEncoderThread.start();
                this.mGLThread = new Thread(new Runnable() {
                    public void run() {
                        SelfMotionPanoramaConverter.this.processFrame(instance);
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
                SelfMotionPanoramaInfo.freeInstance();
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
