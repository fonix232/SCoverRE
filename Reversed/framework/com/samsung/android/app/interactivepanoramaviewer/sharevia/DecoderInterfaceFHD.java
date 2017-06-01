package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import com.samsung.android.app.interactivepanoramaviewer.util.JniUtil;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DecoderInterfaceFHD implements AppController {
    private static final long DECODE_WAIT_TIME = 10000;
    public static final int GOP_FACTOR = 4;
    int IFRAME_INTERVAL = 10;
    private final String TAG = "DecoderInterface";
    Object lockObj = new Object();
    int mBufferSize;
    private int mColorFormat = 0;
    ByteBuffer mCroppedBuffer = null;
    protected volatile int mCurrentState = 1000;
    int mExif = 0;
    File mFile;
    MediaFormat mFormat;
    int mFrameHeight = 720;
    int mFrameWidth = 1280;
    ArrayList<BufferData> mFrames;
    ArrayList<BufferData> mKeyFrames;
    MediaCodec mMediaDecoder;
    MediaExtractor mMediaExtractor;
    long mNextGOPTs = 0;
    int mPPhandler1 = -1;
    String mPath;
    ByteBuffer mResizeBuffer;
    MediaMetadataRetriever mRetriever;
    ArrayList<Long> mSyncTs;
    long mTotalDuration;
    int nFramesInLastGop;
    int nTotalFrame = 0;
    int nTotalSyncFrame = 0;
    DirectionFHD prevGOPRequestDir;

    public class BufferData {
        public boolean bDirty;
        public float[] mAffineData;
        public byte[] mBuffer;
        public int mRenderedFrameIdx;
    }

    public class ContentMetaData {
        public int mHeight;
        public int mWidth;
    }

    public enum DirectionFHD {
        POSITIVE(1),
        NEGATIVE(-1);
        
        private int mDirection;

        private DirectionFHD(int i) {
            this.mDirection = i;
        }

        public int getDirection() {
            return this.mDirection;
        }
    }

    public DecoderInterfaceFHD(String str) {
        this.mPath = str;
    }

    private int calculateGOPSize(int i) {
        if (this.mMediaExtractor == null) {
            return 0;
        }
        this.mMediaExtractor.seekTo(0, 2);
        long j = 0;
        long j2 = 0;
        int i2 = 0;
        while (j != -1) {
            i2++;
            this.mSyncTs.add(Long.valueOf(j));
            this.mMediaExtractor.advance();
            j2 = this.mMediaExtractor.getSampleTime();
            Log.m35v("DecoderInterface", "calculateGOPSize 1 cur = " + j2);
            this.mMediaExtractor.seekTo(j2, 1);
            j = this.mMediaExtractor.getSampleTime();
            Log.m35v("DecoderInterface", "calculateGOPSize 2 cur = " + j);
        }
        this.nTotalSyncFrame = i2;
        this.mMediaExtractor.seekTo(j2, 2);
        Log.m35v("DecoderInterface", "calculateGOPSize I frame done: prevTs" + j2 + " time: " + this.mMediaExtractor.getSampleTime());
        this.nFramesInLastGop = 0;
        if (j2 == -1) {
            this.nFramesInLastGop = 1;
        }
        while (this.mMediaExtractor.advance()) {
            this.nFramesInLastGop++;
        }
        Log.m35v("DecoderInterface", "calculateGOPSize nFramesInLastGop = " + this.nFramesInLastGop);
        if (this.nTotalSyncFrame != 1) {
            i = (i - this.nFramesInLastGop) / (this.nTotalSyncFrame - 1);
        }
        this.mMediaExtractor.seekTo(0, 2);
        Log.m35v("DecoderInterface", "GOP size" + i + "total sync frames" + this.nTotalSyncFrame);
        return i;
    }

    private int calculateTotalFrames() {
        int i = 0;
        if (this.mMediaExtractor == null) {
            return 0;
        }
        this.mMediaExtractor.seekTo(0, 2);
        while (this.mMediaExtractor.advance()) {
            i++;
        }
        this.mMediaExtractor.seekTo(0, 2);
        return i;
    }

    private int calculateTotalIFrames() {
        if (this.mMediaExtractor == null) {
            return 0;
        }
        this.mMediaExtractor.seekTo(0, 2);
        int i = 0;
        long j = 0;
        while (j != -1) {
            i++;
            this.mMediaExtractor.advance();
            j = this.mMediaExtractor.getSampleTime();
            Log.m35v("DecoderInterface", "calculateTotalIFrames 1 cur = " + j);
            this.mMediaExtractor.seekTo(j, 1);
            j = this.mMediaExtractor.getSampleTime();
            Log.m35v("DecoderInterface", "calculateTotalIFrames 2 cur = " + j);
        }
        this.mMediaExtractor.seekTo(0, 2);
        Log.m35v("DecoderInterface", "no. of I frames" + i);
        return i;
    }

    public void deInit() {
        synchronized (this.lockObj) {
            Log.m35v("DecoderInterface", "deinit entry");
            this.mCurrentState = 1006;
            if (this.mMediaDecoder != null) {
                this.mMediaDecoder.stop();
                this.mMediaDecoder.release();
            }
            if (this.mMediaExtractor != null) {
                this.mMediaExtractor.release();
            }
            Log.m35v("DecoderInterface", "deinit exit");
        }
    }

    public ContentMetaData getContentMetaData() {
        ContentMetaData contentMetaData = new ContentMetaData();
        contentMetaData.mWidth = this.mFrameWidth;
        contentMetaData.mHeight = this.mFrameHeight;
        return contentMetaData;
    }

    public int getGOPInterval() {
        return this.IFRAME_INTERVAL;
    }

    public int getTotalFrameCount() {
        return this.nTotalFrame;
    }

    public void init(boolean z) {
        Log.m35v("DecoderInterface", "init entry " + this.mPath);
        this.mCurrentState = 1001;
        this.mRetriever = new MediaMetadataRetriever();
        this.mRetriever.setDataSource(this.mPath);
        String extractMetadata = this.mRetriever.extractMetadata(18);
        if (extractMetadata != null) {
            this.mFrameWidth = Integer.parseInt(extractMetadata);
        }
        extractMetadata = this.mRetriever.extractMetadata(19);
        if (extractMetadata != null) {
            this.mFrameHeight = Integer.parseInt(extractMetadata);
        }
        extractMetadata = this.mRetriever.extractMetadata(24);
        this.mExif = 0;
        if (extractMetadata != null) {
            this.mExif = Integer.parseInt(extractMetadata);
        }
        this.mBufferSize = ((this.mFrameWidth * this.mFrameHeight) * 3) / 2;
        this.mMediaExtractor = new MediaExtractor();
        try {
            this.mMediaExtractor.setDataSource(this.mPath);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        for (int i = 0; i < this.mMediaExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = this.mMediaExtractor.getTrackFormat(i);
            extractMetadata = trackFormat.getString("mime");
            if (extractMetadata.startsWith("video/")) {
                this.mMediaExtractor.selectTrack(i);
                try {
                    this.mMediaDecoder = MediaCodec.createDecoderByType(extractMetadata);
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
                this.mMediaDecoder.configure(trackFormat, null, null, 0);
                this.mFormat = trackFormat;
            }
        }
        if (this.mMediaDecoder != null) {
            this.mMediaDecoder.start();
            this.nTotalFrame = calculateTotalFrames();
            Log.m35v("DecoderInterface", "init TotalFrameCount" + this.nTotalFrame);
            this.mSyncTs = new ArrayList();
            this.IFRAME_INTERVAL = calculateGOPSize(this.nTotalFrame);
            Log.m35v("DecoderInterface", "IFRAME_INTERVAL = " + this.IFRAME_INTERVAL);
            Log.m35v("DecoderInterface", "init Done width, height, exif " + this.mFrameWidth + " " + this.mFrameHeight + " " + this.mExif);
            return;
        }
        Log.m31e("DecoderInterface", "decoder is null");
    }

    public boolean nextframe(BufferData bufferData) {
        if (bufferData == null) {
            Log.m35v("DecoderInterface", "data is null");
            return false;
        } else if (this.mMediaDecoder == null || this.mMediaExtractor == null) {
            Log.m35v("DecoderInterface", "decoder not initialized");
            return false;
        } else {
            int i = this.mFrameWidth;
            int i2 = this.mFrameHeight;
            if (this.mCroppedBuffer == null) {
                this.mCroppedBuffer = ByteBuffer.allocateDirect(this.mBufferSize);
            }
            try {
                ByteBuffer[] inputBuffers = this.mMediaDecoder.getInputBuffers();
                ByteBuffer[] outputBuffers = this.mMediaDecoder.getOutputBuffers();
                BufferInfo bufferInfo = new BufferInfo();
                try {
                    int readSampleData;
                    int dequeueInputBuffer = this.mMediaDecoder.dequeueInputBuffer(DECODE_WAIT_TIME);
                    if (dequeueInputBuffer >= 0) {
                        readSampleData = this.mMediaExtractor.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                        if (readSampleData >= 0) {
                            try {
                                this.mMediaDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, this.mMediaExtractor.getSampleTime(), 0);
                                this.mMediaExtractor.advance();
                            } catch (Exception e) {
                                return true;
                            }
                        }
                        try {
                            this.mMediaDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            Log.m29d("DecoderInterface", "THis is the End of the Stream");
                        } catch (Exception e2) {
                            return true;
                        }
                    }
                    try {
                        int dequeueOutputBuffer = this.mMediaDecoder.dequeueOutputBuffer(bufferInfo, DECODE_WAIT_TIME);
                        switch (dequeueOutputBuffer) {
                            case -3:
                                Log.m29d("DecoderInterface", "INFO_OUTPUT_BUFFERS_CHANGED");
                                this.mMediaDecoder.getOutputBuffers();
                                break;
                            case -2:
                                this.mFormat = this.mMediaDecoder.getOutputFormat();
                                if (!this.mFormat.containsKey("color-format")) {
                                    Log.m35v("DecoderInterface", "cf not present");
                                    break;
                                }
                                Log.m29d("DecoderInterface", "New format " + this.mFormat);
                                i = this.mFormat.getInteger("color-format");
                                if (i == 21) {
                                    this.mColorFormat = 0;
                                } else if (i == 19) {
                                    this.mColorFormat = 1;
                                }
                                Log.m29d("DecoderInterface", "mColorFormat " + this.mColorFormat);
                                break;
                            case -1:
                                Log.m29d("DecoderInterface", "dequeueOutputBuffer timed out! with wait period 10000");
                                break;
                            default:
                                if (bufferInfo.size != 0) {
                                    if (bufferInfo.size == ((this.mFrameWidth * this.mFrameHeight) * 3) / 2) {
                                        readSampleData = i2;
                                    } else {
                                        i = ((this.mFrameWidth + 128) - 1) & -128;
                                        readSampleData = ((this.mFrameHeight + 32) - 1) & -32;
                                    }
                                    Buffer buffer = outputBuffers[dequeueOutputBuffer];
                                    buffer.position(0);
                                    Image outputImage = this.mMediaDecoder.getOutputImage(dequeueOutputBuffer);
                                    if (outputImage == null) {
                                        JniUtil.swCrop(buffer, this.mCroppedBuffer, this.mFrameWidth, readSampleData, this.mFrameWidth, this.mFrameHeight, this.mColorFormat);
                                    } else {
                                        JniUtil.swCrop(buffer, this.mCroppedBuffer, outputImage.getWidth(), outputImage.getHeight(), this.mFrameWidth, this.mFrameHeight, this.mColorFormat);
                                    }
                                    this.mCroppedBuffer.position(0);
                                    this.mCroppedBuffer.get(bufferData.mBuffer);
                                    bufferData.bDirty = true;
                                    this.mMediaDecoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    break;
                                }
                                Log.m35v("DecodeActivity", "end of stream------------------");
                                this.mCurrentState = 1012;
                                return false;
                        }
                        if ((bufferInfo.flags & 4) == 0) {
                            return true;
                        }
                        Log.m29d("DecoderInterface", "OutputBuffer BUFFER_FLAG_END_OF_STREAM here");
                        this.mCurrentState = 1012;
                        return false;
                    } catch (Exception e3) {
                        return true;
                    }
                } catch (Throwable e4) {
                    e4.printStackTrace();
                    return true;
                }
            } catch (IllegalStateException e5) {
                return false;
            }
        }
    }

    public boolean seekToRequiredGOP(int i) {
        int i2 = 0;
        if (this.mMediaDecoder == null || this.mMediaExtractor == null) {
            Log.m35v("DecoderInterface", "decoder not initialized");
            return false;
        }
        int gOPInterval = i / getGOPInterval();
        Log.m35v("DecoderInterface", "req frame No =" + i + " reqIndex=" + gOPInterval);
        Log.m35v("DecoderInterface", "tss " + this.mSyncTs.toString());
        if (gOPInterval >= 0) {
            if (gOPInterval < this.mSyncTs.size()) {
                Log.m35v("DecoderInterface", "reqIndex computation failed");
                i2 = gOPInterval;
            } else {
                i2 = this.mSyncTs.size() - 1;
            }
        }
        long longValue = ((Long) this.mSyncTs.get(i2)).longValue();
        this.mMediaExtractor.seekTo(longValue, 2);
        Log.m35v("DecoderInterface", "frame ts " + longValue + " extarctor ts " + this.mMediaExtractor.getSampleTime() + "frameCounter= " + (getGOPInterval() * i2));
        return true;
    }
}
