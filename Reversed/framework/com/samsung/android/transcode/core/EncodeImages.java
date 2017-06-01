package com.samsung.android.transcode.core;

import android.media.ExifInterface;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.util.Log;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.transcode.renderer.RenderTexture_GL_2d;
import com.samsung.android.transcode.surfaces.InputSurface;
import com.samsung.android.transcode.util.CodecsHelper;
import com.samsung.android.transcode.util.Constants;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class EncodeImages extends Encode {
    private static final long TIMEOUT_USEC = 10000;
    private MediaExtractor mAudioExtractor;
    private boolean mCopyAudio;
    private long mEncodedOutputDurationMs = -1;
    private volatile boolean mEncoding = false;
    private int mFramesPerImage = 1;
    private int mFramesToWrite = 0;
    private ArrayList<String> mInputFilePaths;
    private int mInputOrientationDegrees = 0;
    private InputSurface mInputSurface;
    private RenderTexture_GL_2d mLogoRenderer;
    private RenderTexture_GL_2d mRenderer;
    private Object mStopLock = new Object();

    public void initialize(String str, int i, int i2, long j, String[] strArr) {
        this.mOutputFilePath = str;
        this.mOutputWidth = i;
        this.mOutputHeight = i2;
        this.mInputFilePaths = new ArrayList(Arrays.asList(strArr));
        this.mEncodedOutputDurationMs = j;
    }

    public void initialize(String str, int i, int i2, long j, String[] strArr, FileDescriptor fileDescriptor, int i3, int i4) throws IOException {
        setAudioData(fileDescriptor, i3, i4);
        initialize(str, i, i2, j, strArr);
    }

    public void initialize(String str, int i, int i2, long j, String[] strArr, String str2) throws IOException {
        setAudioData(str2);
        initialize(str, i, i2, j, strArr);
    }

    protected void prepare() throws IOException {
        this.mEncoding = true;
        if (this.mCopyAudio) {
            prepareAudioCodecs();
        }
        if (this.mEncodedOutputDurationMs == -1) {
            this.mFramesToWrite = this.mInputFilePaths.size();
            this.mEncodedOutputDurationMs = ((long) (this.mFramesToWrite / this.mOutputVideoFrameRate)) * 1000;
        } else {
            this.mFramesToWrite = (int) (((double) (((long) this.mOutputVideoFrameRate) * this.mEncodedOutputDurationMs)) / 1000.0d);
        }
        prepareVideoCodecs();
        this.mFramesPerImage = this.mFramesToWrite / this.mInputFilePaths.size();
        int attributeInt = new ExifInterface((String) this.mInputFilePaths.get(0)).getAttributeInt("Orientation", 1);
        if (attributeInt == 6) {
            this.mInputOrientationDegrees = 90;
        } else if (attributeInt == 3) {
            this.mInputOrientationDegrees = 180;
        } else if (attributeInt == 8) {
            this.mInputOrientationDegrees = 270;
        }
        Log.d(Constants.TAG, "Total frames to be written " + this.mFramesToWrite + ". Frames per image " + this.mFramesPerImage);
    }

    protected void prepareAudioCodecs() throws IOException {
        int i = 0;
        MediaFormat trackFormat = this.mAudioExtractor.getTrackFormat(CodecsHelper.getAndSelectAudioTrackIndex(this.mAudioExtractor));
        Log.d(Constants.TAG, "Audio input format " + trackFormat);
        this.mOutputAudioSampleRateHZ = trackFormat.getInteger("sample-rate");
        this.mOutputAudioChannelCount = trackFormat.getInteger("channel-count");
        try {
            i = trackFormat.getInteger("max-input-size");
        } catch (NullPointerException e) {
            Log.d(Constants.TAG, "Audio max input size not defined");
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(this.mOutputAudioMimeType, this.mOutputAudioSampleRateHZ, this.mOutputAudioChannelCount);
        if (i != 0) {
            createAudioFormat.setInteger("max-input-size", i);
        }
        createAudioFormat.setInteger("bitrate", this.mOutputAudioBitRate);
        createAudioFormat.setInteger("aac-profile", this.mOutputAudioAACProfile);
        Log.d(Constants.TAG, "Audio output format " + createAudioFormat);
        this.mOutputAudioEncoder = CodecsHelper.createAudioEncoder(CodecsHelper.getEncoderCodec(this.mOutputAudioMimeType), createAudioFormat);
        this.mInputAudioDecoder = CodecsHelper.createAudioDecoder(trackFormat);
        if (this.mOutputAudioEncoder == null || this.mInputAudioDecoder == null) {
            throw new IOException("Codec initialization error, unable to create Codecs!");
        } else if (this.mEncodedOutputDurationMs == -1) {
            this.mEncodedOutputDurationMs = trackFormat.getLong("durationUs") / 1000;
        }
    }

    protected void prepareVideoCodecs() throws IOException {
        if (this.mOutputMaxSizeKB != -1) {
            this.mOutputVideoBitRate = CodecsHelper.getVideoEncodingBitRate(this.mSizeFraction, this.mOutputMaxSizeKB, this.mEncodedOutputDurationMs, this.mOutputAudioBitRate / 1000, this.mOutputWidth, this.mOutputHeight) * 1000;
        } else {
            this.mOutputVideoBitRate = suggestBitRate(this.mOutputWidth, this.mOutputHeight) * 1000;
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mOutputVideoMimeType, this.mOutputWidth, this.mOutputHeight);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", this.mOutputVideoBitRate);
        createVideoFormat.setInteger("frame-rate", this.mOutputVideoFrameRate);
        createVideoFormat.setInteger("i-frame-interval", this.mOutputVideoIFrameInterval);
        this.mOutputVideoEncoder = MediaCodec.createEncoderByType(this.mOutputVideoMimeType);
        this.mOutputVideoEncoder.configure(createVideoFormat, null, null, 1);
        this.mInputSurface = new InputSurface(this.mOutputVideoEncoder.createInputSurface());
        this.mOutputVideoEncoder.start();
        this.mInputSurface.makeCurrent();
        this.mRenderer = new RenderTexture_GL_2d();
        this.mRenderer.prepare();
        if (this.mLogoPresent) {
            this.mLogoRenderer = new RenderTexture_GL_2d();
            this.mLogoRenderer.prepare();
        }
    }

    protected void prepare_for_transrewrite() throws IOException {
    }

    protected void release() {
        try {
            Log.d(Constants.TAG, "releasing encoder objects");
            if (this.mOutputVideoEncoder != null) {
                this.mOutputVideoEncoder.stop();
                this.mOutputVideoEncoder.release();
                this.mOutputVideoEncoder = null;
            }
        } catch (Throwable e) {
            Log.d(Constants.TAG, "Exception in releasing output video encoder.");
            e.printStackTrace();
        } catch (Throwable th) {
            synchronized (this.mStopLock) {
                this.mStopLock.notifyAll();
                this.mEncoding = false;
            }
        }
        if (this.mRenderer != null) {
            try {
                this.mRenderer.release();
                this.mRenderer = null;
            } catch (Throwable e2) {
                Log.d(Constants.TAG, "Exception in releasing renderer.");
                e2.printStackTrace();
            }
        }
        if (this.mLogoRenderer != null) {
            try {
                this.mLogoRenderer.release();
                this.mLogoRenderer = null;
            } catch (Throwable e22) {
                Log.d(Constants.TAG, "Exception in releasing logo renderer.");
                e22.printStackTrace();
            }
        }
        if (!(this.mLogo == null || this.mLogo.mLogoBitmap == null || this.mLogo.mLogoBitmap.isRecycled())) {
            this.mLogo.mLogoBitmap.recycle();
            this.mLogo.mLogoBitmap = null;
            this.mLogo = null;
            this.mLogoPresent = false;
        }
        if (this.mInputSurface != null) {
            try {
                this.mInputSurface.release();
                this.mInputSurface = null;
            } catch (Throwable e222) {
                Log.d(Constants.TAG, "Exception in releasing input surface.");
                e222.printStackTrace();
            }
        }
        if (this.mOutputAudioEncoder != null) {
            try {
                this.mOutputAudioEncoder.stop();
                this.mOutputAudioEncoder.release();
                this.mOutputAudioEncoder = null;
            } catch (Throwable e2222) {
                Log.d(Constants.TAG, "Exception in releasing output audio encoder.");
                e2222.printStackTrace();
            }
        }
        if (this.mInputAudioDecoder != null) {
            try {
                this.mInputAudioDecoder.stop();
                this.mInputAudioDecoder.release();
                this.mInputAudioDecoder = null;
            } catch (Throwable e22222) {
                Log.d(Constants.TAG, "Exception in releasing input audio decoder.");
                e22222.printStackTrace();
            }
        }
        if (this.mAudioExtractor != null) {
            try {
                this.mAudioExtractor.release();
                this.mAudioExtractor = null;
            } catch (Throwable e222222) {
                Log.d(Constants.TAG, "Exception in releasing audio extractor.");
                e222222.printStackTrace();
            }
        }
        if (this.mInputFilePaths != null) {
            this.mInputFilePaths.clear();
            this.mInputFilePaths = null;
        }
        if (this.mMuxer != null) {
            try {
                if (this.mMuxerStarted) {
                    this.mMuxer.stop();
                }
                this.mMuxer.release();
                this.mMuxer = null;
            } catch (Throwable e2222222) {
                Log.d(Constants.TAG, "Exception in releasing muxer.");
                e2222222.printStackTrace();
            }
        }
        synchronized (this.mStopLock) {
            this.mStopLock.notifyAll();
            this.mEncoding = false;
        }
    }

    public void setAudioData(FileDescriptor fileDescriptor, int i, int i2) throws IOException {
        this.mAudioExtractor = CodecsHelper.createExtractor(fileDescriptor, (long) i, (long) i2);
        this.mCopyAudio = true;
    }

    public void setAudioData(String str) throws IOException {
        this.mAudioExtractor = CodecsHelper.createExtractor(str);
        this.mCopyAudio = true;
    }

    public void setMaxOutputSize(int i) {
        this.mOutputMaxSizeKB = (long) i;
    }

    public void setOutputDuration(long j) {
        this.mEncodedOutputDurationMs = j;
    }

    protected void startEncoding() throws IOException {
        if (this.mUserStop) {
            Log.d(Constants.TAG, "Not starting encoding because it is stopped by user.");
            return;
        }
        ByteBuffer[] outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
        ByteBuffer[] outputBuffers2 = !this.mCopyAudio ? null : this.mOutputAudioEncoder.getOutputBuffers();
        Buffer[] inputBuffers = !this.mCopyAudio ? null : this.mOutputAudioEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers3 = !this.mCopyAudio ? null : this.mInputAudioDecoder.getOutputBuffers();
        ByteBuffer[] inputBuffers2 = !this.mCopyAudio ? null : this.mInputAudioDecoder.getInputBuffers();
        BufferInfo bufferInfo = new BufferInfo();
        BufferInfo bufferInfo2 = new BufferInfo();
        BufferInfo bufferInfo3 = new BufferInfo();
        MediaFormat mediaFormat = null;
        Object obj = !this.mCopyAudio ? 1 : null;
        Object obj2 = !this.mCopyAudio ? 1 : null;
        Object obj3 = !this.mCopyAudio ? 1 : null;
        int i = -1;
        int size = this.mInputFilePaths.size();
        int i2 = this.mOutputVideoFrameRate << 1;
        if (this.mLogoPresent) {
            this.mLogoRenderer.loadTexture(this.mLogo.mLogoBitmap, this.mLogo.mLogoDrawWidth, this.mLogo.mLogoDrawHeight);
            this.mLogo.mLogoBitmap.recycle();
            this.mLogo.mLogoBitmap = null;
            float f = (-(((float) (this.mOutputWidth >> 1)) - (((float) (this.mLogo.mTopX + (this.mLogo.mLogoDrawWidth >> 1))) * 1.0f))) / ((float) (this.mOutputWidth >> 1));
            float f2 = (((float) (this.mOutputHeight >> 1)) - (((float) (this.mLogo.mTopY + (this.mLogo.mLogoDrawHeight >> 1))) * 1.0f)) / ((float) (this.mOutputHeight >> 1));
            switch (this.mInputOrientationDegrees) {
                case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1_LAND /*90*/:
                    f2 *= -1.0f;
                    break;
                case 180:
                    f *= -1.0f;
                    f2 *= -1.0f;
                    break;
                case 270:
                    f *= -1.0f;
                    break;
            }
            this.mLogoRenderer.setProjectionMatrixTranslate(f, f2);
            this.mLogoRenderer.setProjectionMatrixScale((((float) this.mLogo.mLogoDrawWidth) * 1.0f) / ((float) this.mOutputWidth), (((float) this.mLogo.mLogoDrawHeight) * 1.0f) / ((float) this.mOutputHeight));
            this.mLogoRenderer.setProjectionMatrixRotate((float) this.mInputOrientationDegrees, 0.0f, 0.0f, 1.0f);
        }
        this.mRenderer.loadTexture((String) this.mInputFilePaths.get(0), this.mOutputWidth, this.mOutputHeight);
        Object obj4 = null;
        Object obj5 = obj3;
        Object obj6 = obj2;
        Object obj7 = obj;
        ByteBuffer[] byteBufferArr = outputBuffers3;
        ByteBuffer[] byteBufferArr2 = outputBuffers2;
        int i3 = 0;
        MediaFormat mediaFormat2 = null;
        int i4 = 0;
        int i5 = 0;
        outputBuffers3 = outputBuffers;
        while (true) {
            if (obj4 == null || obj5 == null) {
                Object obj8;
                MediaFormat mediaFormat3;
                int i6;
                int i7;
                int i8;
                int i9;
                int i10;
                int dequeueInputBuffer;
                long sampleTime;
                Object obj9;
                Buffer buffer;
                Buffer duplicate;
                ByteBuffer byteBuffer;
                Object obj10;
                while (!this.mUserStop && obj4 == null) {
                    if (mediaFormat2 == null || this.mMuxerStarted) {
                        int dequeueOutputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "Video encoder output try again later ");
                            obj8 = obj4;
                            mediaFormat3 = mediaFormat2;
                            outputBuffers = outputBuffers3;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "Video encoder output buffer changed");
                            obj8 = obj4;
                            mediaFormat3 = mediaFormat2;
                            outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                        } else if (dequeueOutputBuffer == -2) {
                            Log.d(Constants.TAG, "Video encoder output buffer changed");
                            if (this.mVideoTrackIndex < 0) {
                                obj8 = obj4;
                                mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                                outputBuffers = outputBuffers3;
                            } else {
                                throw new RuntimeException("Video encoder output format changed after muxer has started");
                            }
                        } else if (dequeueOutputBuffer >= 0) {
                            Buffer buffer2 = outputBuffers3[dequeueOutputBuffer];
                            if (buffer2 != null) {
                                if ((bufferInfo.flags & 2) != 0) {
                                    Log.d(Constants.TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    bufferInfo.size = 0;
                                }
                                if (bufferInfo.size != 0) {
                                    if (this.mMuxerStarted) {
                                        buffer2.position(bufferInfo.offset);
                                        buffer2.limit(bufferInfo.offset + bufferInfo.size);
                                        this.mMuxer.writeSampleData(this.mVideoTrackIndex, buffer2, bufferInfo);
                                        Log.d(Constants.TAG, "sent " + bufferInfo.size + " bytes to muxer");
                                    } else {
                                        throw new RuntimeException("muxer hasn't started. We cannot write video encoder output.");
                                    }
                                }
                                this.mOutputVideoEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                if ((bufferInfo.flags & 4) != 0) {
                                    if (i5 <= this.mFramesToWrite) {
                                        Log.d(Constants.TAG, "reached video encoder end of stream unexpectedly");
                                    } else {
                                        Log.d(Constants.TAG, "video endcoder end of stream reached");
                                    }
                                    dequeueOutputBuffer = 1;
                                    mediaFormat3 = mediaFormat2;
                                    outputBuffers = outputBuffers3;
                                }
                            } else {
                                throw new RuntimeException("video encoder OutputBuffer " + dequeueOutputBuffer + " is null");
                            }
                        } else {
                            Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                            obj8 = obj4;
                            mediaFormat3 = mediaFormat2;
                            outputBuffers = outputBuffers3;
                        }
                        if (!this.mUserStop && i5 < this.mFramesToWrite) {
                            if (mediaFormat3 == null || this.mMuxerStarted) {
                                GLES20.glClear(16384);
                                this.mRenderer.draw();
                                if (this.mLogoPresent && i5 % i2 < this.mOutputVideoFrameRate) {
                                    this.mLogoRenderer.draw();
                                }
                                this.mInputSurface.setPresentationTime(computePresentationTimeNsec(i5));
                                Log.d(Constants.TAG, "sending frame " + i5 + " to video encoder");
                                this.mInputSurface.swapBuffers();
                                i5++;
                                i6 = i3 + 1;
                                if (i6 != this.mFramesPerImage) {
                                    i7 = i6;
                                    i8 = i4;
                                    i9 = i5;
                                } else if (i4 + 1 != size) {
                                    i7 = 0;
                                    i8 = i4;
                                    i9 = i5;
                                } else {
                                    i10 = i4 + 1;
                                    this.mRenderer.loadTexture((String) this.mInputFilePaths.get(i10), this.mOutputWidth, this.mOutputHeight);
                                    i7 = 0;
                                    i8 = i10;
                                    i9 = i5;
                                }
                                if (this.mCopyAudio) {
                                    if (!this.mUserStop && obj7 == null) {
                                        if (mediaFormat == null || this.mMuxerStarted) {
                                            dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                            if (dequeueInputBuffer != -1) {
                                                i3 = this.mAudioExtractor.readSampleData(inputBuffers2[dequeueInputBuffer], 0);
                                                sampleTime = this.mAudioExtractor.getSampleTime();
                                                if ((sampleTime > this.mEncodedOutputDurationMs * 1000 ? 1 : null) == null) {
                                                    if (i3 > 0) {
                                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, this.mAudioExtractor.getSampleFlags());
                                                    }
                                                    obj9 = this.mAudioExtractor.advance() ? null : 1;
                                                } else {
                                                    obj9 = 1;
                                                }
                                                if (obj9 != null) {
                                                    Log.d(Constants.TAG, "audio decoder sending EOS");
                                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                                }
                                            } else {
                                                Log.d(Constants.TAG, "audio decoder input try again later");
                                                obj9 = obj7;
                                            }
                                            if (!this.mUserStop && obj6 == null && r20 == -1) {
                                                if (mediaFormat == null || this.mMuxerStarted) {
                                                    i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                                                    if (i6 != -1) {
                                                        Log.d(Constants.TAG, "audio decoder output buffer try again later");
                                                    } else if (i6 != -3) {
                                                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                                    } else if (i6 != -2) {
                                                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                                    } else if (i6 >= 0) {
                                                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                                    } else if ((bufferInfo2.flags & 2) != 0) {
                                                        i = i6;
                                                    } else {
                                                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                                        this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                                                    }
                                                }
                                            }
                                            if (this.mUserStop || i == -1) {
                                                obj4 = obj6;
                                            } else {
                                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                if (dequeueInputBuffer != -1) {
                                                    buffer = inputBuffers[dequeueInputBuffer];
                                                    i3 = bufferInfo2.size;
                                                    sampleTime = bufferInfo2.presentationTimeUs;
                                                    if (i3 >= 0) {
                                                        duplicate = byteBufferArr[i].duplicate();
                                                        duplicate.position(bufferInfo2.offset);
                                                        duplicate.limit(bufferInfo2.offset + i3);
                                                        buffer.position(0);
                                                        buffer.put(duplicate);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                                    }
                                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                    i = -1;
                                                    if ((bufferInfo2.flags & 4) == 0) {
                                                        obj4 = obj6;
                                                    } else {
                                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                                        obj4 = 1;
                                                    }
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                    obj4 = obj6;
                                                }
                                            }
                                            if (!this.mUserStop && obj5 == null) {
                                                if (mediaFormat == null || this.mMuxerStarted) {
                                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                    if (i10 == -1) {
                                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                        mediaFormat2 = mediaFormat;
                                                        outputBuffers3 = byteBufferArr2;
                                                    } else if (i10 == -3) {
                                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                        mediaFormat2 = mediaFormat;
                                                    } else if (i10 != -2) {
                                                        if (i10 >= 0) {
                                                            byteBuffer = byteBufferArr2[i10];
                                                            if ((bufferInfo3.flags & 2) == 0) {
                                                                if (bufferInfo3.size != 0) {
                                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                                }
                                                                if ((bufferInfo3.flags & 4) == 0) {
                                                                    obj10 = obj5;
                                                                } else {
                                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                                    obj10 = 1;
                                                                }
                                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                                obj5 = obj10;
                                                                outputBuffers3 = byteBufferArr2;
                                                                mediaFormat2 = mediaFormat;
                                                            } else {
                                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                                mediaFormat2 = mediaFormat;
                                                                outputBuffers3 = byteBufferArr2;
                                                            }
                                                        } else {
                                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                            mediaFormat2 = mediaFormat;
                                                            outputBuffers3 = byteBufferArr2;
                                                        }
                                                    } else if (this.mAudioTrackIndex < 0) {
                                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                                        outputBuffers3 = byteBufferArr2;
                                                    } else {
                                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                    }
                                                }
                                            }
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    }
                                    obj9 = obj7;
                                    if (mediaFormat == null) {
                                        if (this.mUserStop) {
                                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                            if (dequeueInputBuffer != -1) {
                                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                obj4 = obj6;
                                            } else {
                                                buffer = inputBuffers[dequeueInputBuffer];
                                                i3 = bufferInfo2.size;
                                                sampleTime = bufferInfo2.presentationTimeUs;
                                                if (i3 >= 0) {
                                                    duplicate = byteBufferArr[i].duplicate();
                                                    duplicate.position(bufferInfo2.offset);
                                                    duplicate.limit(bufferInfo2.offset + i3);
                                                    buffer.position(0);
                                                    buffer.put(duplicate);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                                }
                                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                i = -1;
                                                if ((bufferInfo2.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                                    obj4 = 1;
                                                } else {
                                                    obj4 = obj6;
                                                }
                                            }
                                            if (mediaFormat == null) {
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            }
                                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                            if (i10 == -1) {
                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            } else if (i10 == -3) {
                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                mediaFormat2 = mediaFormat;
                                            } else if (i10 != -2) {
                                                if (this.mAudioTrackIndex < 0) {
                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                }
                                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                                outputBuffers3 = byteBufferArr2;
                                            } else if (i10 >= 0) {
                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            } else {
                                                byteBuffer = byteBufferArr2[i10];
                                                if ((bufferInfo3.flags & 2) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                    mediaFormat2 = mediaFormat;
                                                    outputBuffers3 = byteBufferArr2;
                                                } else {
                                                    if (bufferInfo3.size != 0) {
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                    }
                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                        obj10 = 1;
                                                    } else {
                                                        obj10 = obj5;
                                                    }
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                    obj5 = obj10;
                                                    outputBuffers3 = byteBufferArr2;
                                                    mediaFormat2 = mediaFormat;
                                                }
                                            }
                                        }
                                        obj4 = obj6;
                                        if (mediaFormat == null) {
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (i10 == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat2 = mediaFormat;
                                        } else if (i10 != -2) {
                                            if (i10 >= 0) {
                                                byteBuffer = byteBufferArr2[i10];
                                                if ((bufferInfo3.flags & 2) == 0) {
                                                    if (bufferInfo3.size != 0) {
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                    }
                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                        obj10 = obj5;
                                                    } else {
                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                        obj10 = 1;
                                                    }
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                    obj5 = obj10;
                                                    outputBuffers3 = byteBufferArr2;
                                                    mediaFormat2 = mediaFormat;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                    mediaFormat2 = mediaFormat;
                                                    outputBuffers3 = byteBufferArr2;
                                                }
                                            } else {
                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            }
                                        } else if (this.mAudioTrackIndex < 0) {
                                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                    }
                                    i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                                    if (i6 != -1) {
                                        Log.d(Constants.TAG, "audio decoder output buffer try again later");
                                    } else if (i6 != -3) {
                                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                    } else if (i6 != -2) {
                                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                    } else if (i6 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                    } else if ((bufferInfo2.flags & 2) != 0) {
                                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                        this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                                    } else {
                                        i = i6;
                                    }
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            buffer = inputBuffers[dequeueInputBuffer];
                                            i3 = bufferInfo2.size;
                                            sampleTime = bufferInfo2.presentationTimeUs;
                                            if (i3 >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo2.offset);
                                                duplicate.limit(bufferInfo2.offset + i3);
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo2.flags & 4) == 0) {
                                                obj4 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj4 = 1;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj4 = obj6;
                                        }
                                        if (mediaFormat == null) {
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (i10 == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat2 = mediaFormat;
                                        } else if (i10 != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                } else {
                                                    obj10 = obj5;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            }
                                        }
                                    }
                                    obj4 = obj6;
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (i10 >= 0) {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj10 = obj5;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                obj4 = obj6;
                                obj9 = obj7;
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                                if (!(this.mUserStop || this.mMuxerStarted || mediaFormat3 == null)) {
                                    if (!this.mCopyAudio || mediaFormat2 != null) {
                                        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                                        if (this.mCopyAudio) {
                                            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat2);
                                        }
                                        this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                                        this.mMuxer.start();
                                        this.mMuxerStarted = true;
                                    }
                                }
                                if (this.mUserStop) {
                                    Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                } else {
                                    i3 = i7;
                                    i4 = i8;
                                    i5 = i9;
                                    obj6 = obj4;
                                    obj7 = obj9;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr2 = outputBuffers3;
                                    obj4 = obj8;
                                    mediaFormat2 = mediaFormat3;
                                    outputBuffers3 = outputBuffers;
                                }
                            }
                        }
                        if (!this.mUserStop && i5 == this.mFramesToWrite) {
                            Log.d(Constants.TAG, "sending EOS to video encoder");
                            this.mOutputVideoEncoder.signalEndOfInputStream();
                            i7 = i3;
                            i8 = i4;
                            i9 = i5 + 1;
                        } else {
                            i7 = i3;
                            i8 = i4;
                            i9 = i5;
                        }
                        if (this.mCopyAudio) {
                            if (mediaFormat == null) {
                                obj9 = obj7;
                                if (mediaFormat == null) {
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj4 = obj6;
                                        } else {
                                            buffer = inputBuffers[dequeueInputBuffer];
                                            i3 = bufferInfo2.size;
                                            sampleTime = bufferInfo2.presentationTimeUs;
                                            if (i3 >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo2.offset);
                                                duplicate.limit(bufferInfo2.offset + i3);
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo2.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj4 = 1;
                                            } else {
                                                obj4 = obj6;
                                            }
                                        }
                                        if (mediaFormat == null) {
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (i10 == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat2 = mediaFormat;
                                        } else if (i10 != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                } else {
                                                    obj10 = obj5;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            }
                                        }
                                    }
                                    obj4 = obj6;
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (i10 >= 0) {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj10 = obj5;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                                if (i6 != -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later");
                                } else if (i6 != -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                    byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                } else if (i6 != -2) {
                                    Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                } else if (i6 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo2.flags & 2) != 0) {
                                    i = i6;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                                }
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        buffer = inputBuffers[dequeueInputBuffer];
                                        i3 = bufferInfo2.size;
                                        sampleTime = bufferInfo2.presentationTimeUs;
                                        if (i3 >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo2.offset);
                                            duplicate.limit(bufferInfo2.offset + i3);
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo2.flags & 4) == 0) {
                                            obj4 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj4 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj4 = obj6;
                                    }
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            } else {
                                                obj10 = obj5;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        }
                                    }
                                }
                                obj4 = obj6;
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (i10 >= 0) {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj10 = obj5;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                Log.d(Constants.TAG, "audio decoder input try again later");
                                obj9 = obj7;
                            } else {
                                i3 = this.mAudioExtractor.readSampleData(inputBuffers2[dequeueInputBuffer], 0);
                                sampleTime = this.mAudioExtractor.getSampleTime();
                                if (sampleTime > this.mEncodedOutputDurationMs * 1000) {
                                }
                                if ((sampleTime > this.mEncodedOutputDurationMs * 1000 ? 1 : null) == null) {
                                    obj9 = 1;
                                } else {
                                    if (i3 > 0) {
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, this.mAudioExtractor.getSampleFlags());
                                    }
                                    if (this.mAudioExtractor.advance()) {
                                    }
                                    obj9 = this.mAudioExtractor.advance() ? null : 1;
                                }
                                if (obj9 != null) {
                                    Log.d(Constants.TAG, "audio decoder sending EOS");
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                }
                            }
                            if (mediaFormat == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj4 = obj6;
                                    } else {
                                        buffer = inputBuffers[dequeueInputBuffer];
                                        i3 = bufferInfo2.size;
                                        sampleTime = bufferInfo2.presentationTimeUs;
                                        if (i3 >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo2.offset);
                                            duplicate.limit(bufferInfo2.offset + i3);
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo2.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj4 = 1;
                                        } else {
                                            obj4 = obj6;
                                        }
                                    }
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            } else {
                                                obj10 = obj5;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        }
                                    }
                                }
                                obj4 = obj6;
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (i10 >= 0) {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj10 = obj5;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                            if (i6 != -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later");
                            } else if (i6 != -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i6 != -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i6 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo2.flags & 2) != 0) {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                            } else {
                                i = i6;
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers[dequeueInputBuffer];
                                    i3 = bufferInfo2.size;
                                    sampleTime = bufferInfo2.presentationTimeUs;
                                    if (i3 >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo2.offset);
                                        duplicate.limit(bufferInfo2.offset + i3);
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo2.flags & 4) == 0) {
                                        obj4 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj4 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj4 = obj6;
                                }
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        } else {
                                            obj10 = obj5;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    }
                                }
                            }
                            obj4 = obj6;
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (i10 >= 0) {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj10 = obj5;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        obj4 = obj6;
                        obj9 = obj7;
                        mediaFormat2 = mediaFormat;
                        outputBuffers3 = byteBufferArr2;
                        if (!this.mCopyAudio) {
                            if (this.mUserStop) {
                                Log.d(Constants.TAG, "Encoding abruptly stopped.");
                            } else {
                                i3 = i7;
                                i4 = i8;
                                i5 = i9;
                                obj6 = obj4;
                                obj7 = obj9;
                                mediaFormat = mediaFormat2;
                                byteBufferArr2 = outputBuffers3;
                                obj4 = obj8;
                                mediaFormat2 = mediaFormat3;
                                outputBuffers3 = outputBuffers;
                            }
                        }
                        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                        if (this.mCopyAudio) {
                            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat2);
                        }
                        this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                        this.mMuxer.start();
                        this.mMuxerStarted = true;
                        if (this.mUserStop) {
                            i3 = i7;
                            i4 = i8;
                            i5 = i9;
                            obj6 = obj4;
                            obj7 = obj9;
                            mediaFormat = mediaFormat2;
                            byteBufferArr2 = outputBuffers3;
                            obj4 = obj8;
                            mediaFormat2 = mediaFormat3;
                            outputBuffers3 = outputBuffers;
                        } else {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        }
                    }
                }
                obj8 = obj4;
                mediaFormat3 = mediaFormat2;
                outputBuffers = outputBuffers3;
                if (mediaFormat3 == null) {
                    if (this.mUserStop) {
                        Log.d(Constants.TAG, "sending EOS to video encoder");
                        this.mOutputVideoEncoder.signalEndOfInputStream();
                        i7 = i3;
                        i8 = i4;
                        i9 = i5 + 1;
                        if (this.mCopyAudio) {
                            obj4 = obj6;
                            obj9 = obj7;
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else {
                            if (mediaFormat == null) {
                                obj9 = obj7;
                                if (mediaFormat == null) {
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj4 = obj6;
                                        } else {
                                            buffer = inputBuffers[dequeueInputBuffer];
                                            i3 = bufferInfo2.size;
                                            sampleTime = bufferInfo2.presentationTimeUs;
                                            if (i3 >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo2.offset);
                                                duplicate.limit(bufferInfo2.offset + i3);
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo2.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj4 = 1;
                                            } else {
                                                obj4 = obj6;
                                            }
                                        }
                                        if (mediaFormat == null) {
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (i10 == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat2 = mediaFormat;
                                        } else if (i10 != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                            outputBuffers3 = byteBufferArr2;
                                        } else if (i10 >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                } else {
                                                    obj10 = obj5;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            }
                                        }
                                    }
                                    obj4 = obj6;
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (i10 >= 0) {
                                            byteBuffer = byteBufferArr2[i10];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size != 0) {
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj10 = obj5;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj10 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                obj5 = obj10;
                                                outputBuffers3 = byteBufferArr2;
                                                mediaFormat2 = mediaFormat;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                                mediaFormat2 = mediaFormat;
                                                outputBuffers3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                                if (i6 != -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later");
                                } else if (i6 != -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                    byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                } else if (i6 != -2) {
                                    Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                } else if (i6 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo2.flags & 2) != 0) {
                                    i = i6;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                                }
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        buffer = inputBuffers[dequeueInputBuffer];
                                        i3 = bufferInfo2.size;
                                        sampleTime = bufferInfo2.presentationTimeUs;
                                        if (i3 >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo2.offset);
                                            duplicate.limit(bufferInfo2.offset + i3);
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo2.flags & 4) == 0) {
                                            obj4 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj4 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj4 = obj6;
                                    }
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            } else {
                                                obj10 = obj5;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        }
                                    }
                                }
                                obj4 = obj6;
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (i10 >= 0) {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj10 = obj5;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                i3 = this.mAudioExtractor.readSampleData(inputBuffers2[dequeueInputBuffer], 0);
                                sampleTime = this.mAudioExtractor.getSampleTime();
                                if (sampleTime > this.mEncodedOutputDurationMs * 1000) {
                                }
                                if ((sampleTime > this.mEncodedOutputDurationMs * 1000 ? 1 : null) == null) {
                                    if (i3 > 0) {
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, this.mAudioExtractor.getSampleFlags());
                                    }
                                    if (this.mAudioExtractor.advance()) {
                                    }
                                    obj9 = this.mAudioExtractor.advance() ? null : 1;
                                } else {
                                    obj9 = 1;
                                }
                                if (obj9 != null) {
                                    Log.d(Constants.TAG, "audio decoder sending EOS");
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                }
                            } else {
                                Log.d(Constants.TAG, "audio decoder input try again later");
                                obj9 = obj7;
                            }
                            if (mediaFormat == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj4 = obj6;
                                    } else {
                                        buffer = inputBuffers[dequeueInputBuffer];
                                        i3 = bufferInfo2.size;
                                        sampleTime = bufferInfo2.presentationTimeUs;
                                        if (i3 >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo2.offset);
                                            duplicate.limit(bufferInfo2.offset + i3);
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo2.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj4 = 1;
                                        } else {
                                            obj4 = obj6;
                                        }
                                    }
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            } else {
                                                obj10 = obj5;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        }
                                    }
                                }
                                obj4 = obj6;
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (i10 >= 0) {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj10 = obj5;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                            if (i6 != -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later");
                            } else if (i6 != -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i6 != -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i6 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo2.flags & 2) != 0) {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                            } else {
                                i = i6;
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers[dequeueInputBuffer];
                                    i3 = bufferInfo2.size;
                                    sampleTime = bufferInfo2.presentationTimeUs;
                                    if (i3 >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo2.offset);
                                        duplicate.limit(bufferInfo2.offset + i3);
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo2.flags & 4) == 0) {
                                        obj4 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj4 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj4 = obj6;
                                }
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        } else {
                                            obj10 = obj5;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    }
                                }
                            }
                            obj4 = obj6;
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (i10 >= 0) {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj10 = obj5;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        if (!this.mCopyAudio) {
                            if (this.mUserStop) {
                                Log.d(Constants.TAG, "Encoding abruptly stopped.");
                            } else {
                                i3 = i7;
                                i4 = i8;
                                i5 = i9;
                                obj6 = obj4;
                                obj7 = obj9;
                                mediaFormat = mediaFormat2;
                                byteBufferArr2 = outputBuffers3;
                                obj4 = obj8;
                                mediaFormat2 = mediaFormat3;
                                outputBuffers3 = outputBuffers;
                            }
                        }
                        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                        if (this.mCopyAudio) {
                            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat2);
                        }
                        this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                        this.mMuxer.start();
                        this.mMuxerStarted = true;
                        if (this.mUserStop) {
                            i3 = i7;
                            i4 = i8;
                            i5 = i9;
                            obj6 = obj4;
                            obj7 = obj9;
                            mediaFormat = mediaFormat2;
                            byteBufferArr2 = outputBuffers3;
                            obj4 = obj8;
                            mediaFormat2 = mediaFormat3;
                            outputBuffers3 = outputBuffers;
                        } else {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        }
                    }
                    i7 = i3;
                    i8 = i4;
                    i9 = i5;
                    if (this.mCopyAudio) {
                        if (mediaFormat == null) {
                            obj9 = obj7;
                            if (mediaFormat == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj4 = obj6;
                                    } else {
                                        buffer = inputBuffers[dequeueInputBuffer];
                                        i3 = bufferInfo2.size;
                                        sampleTime = bufferInfo2.presentationTimeUs;
                                        if (i3 >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo2.offset);
                                            duplicate.limit(bufferInfo2.offset + i3);
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo2.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj4 = 1;
                                        } else {
                                            obj4 = obj6;
                                        }
                                    }
                                    if (mediaFormat == null) {
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (i10 == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat2 = mediaFormat;
                                    } else if (i10 != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                        outputBuffers3 = byteBufferArr2;
                                    } else if (i10 >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            } else {
                                                obj10 = obj5;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        }
                                    }
                                }
                                obj4 = obj6;
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (i10 >= 0) {
                                        byteBuffer = byteBufferArr2[i10];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size != 0) {
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj10 = obj5;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj10 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            obj5 = obj10;
                                            outputBuffers3 = byteBufferArr2;
                                            mediaFormat2 = mediaFormat;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                            mediaFormat2 = mediaFormat;
                                            outputBuffers3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                            if (i6 != -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later");
                            } else if (i6 != -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i6 != -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i6 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo2.flags & 2) != 0) {
                                i = i6;
                            } else {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers[dequeueInputBuffer];
                                    i3 = bufferInfo2.size;
                                    sampleTime = bufferInfo2.presentationTimeUs;
                                    if (i3 >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo2.offset);
                                        duplicate.limit(bufferInfo2.offset + i3);
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo2.flags & 4) == 0) {
                                        obj4 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj4 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj4 = obj6;
                                }
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        } else {
                                            obj10 = obj5;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    }
                                }
                            }
                            obj4 = obj6;
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (i10 >= 0) {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj10 = obj5;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            Log.d(Constants.TAG, "audio decoder input try again later");
                            obj9 = obj7;
                        } else {
                            i3 = this.mAudioExtractor.readSampleData(inputBuffers2[dequeueInputBuffer], 0);
                            sampleTime = this.mAudioExtractor.getSampleTime();
                            if (sampleTime > this.mEncodedOutputDurationMs * 1000) {
                            }
                            if ((sampleTime > this.mEncodedOutputDurationMs * 1000 ? 1 : null) == null) {
                                obj9 = 1;
                            } else {
                                if (i3 > 0) {
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, this.mAudioExtractor.getSampleFlags());
                                }
                                if (this.mAudioExtractor.advance()) {
                                }
                                obj9 = this.mAudioExtractor.advance() ? null : 1;
                            }
                            if (obj9 != null) {
                                Log.d(Constants.TAG, "audio decoder sending EOS");
                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            }
                        }
                        if (mediaFormat == null) {
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj4 = obj6;
                                } else {
                                    buffer = inputBuffers[dequeueInputBuffer];
                                    i3 = bufferInfo2.size;
                                    sampleTime = bufferInfo2.presentationTimeUs;
                                    if (i3 >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo2.offset);
                                        duplicate.limit(bufferInfo2.offset + i3);
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo2.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj4 = 1;
                                    } else {
                                        obj4 = obj6;
                                    }
                                }
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        } else {
                                            obj10 = obj5;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    }
                                }
                            }
                            obj4 = obj6;
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (i10 >= 0) {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj10 = obj5;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                        if (i6 != -1) {
                            Log.d(Constants.TAG, "audio decoder output buffer try again later");
                        } else if (i6 != -3) {
                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                        } else if (i6 != -2) {
                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                        } else if (i6 >= 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                        } else if ((bufferInfo2.flags & 2) != 0) {
                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                            this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                        } else {
                            i = i6;
                        }
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                buffer = inputBuffers[dequeueInputBuffer];
                                i3 = bufferInfo2.size;
                                sampleTime = bufferInfo2.presentationTimeUs;
                                if (i3 >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo2.offset);
                                    duplicate.limit(bufferInfo2.offset + i3);
                                    buffer.position(0);
                                    buffer.put(duplicate);
                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo2.flags & 4) == 0) {
                                    obj4 = obj6;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj4 = 1;
                                }
                            } else {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj4 = obj6;
                            }
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    } else {
                                        obj10 = obj5;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                }
                            }
                        }
                        obj4 = obj6;
                        if (mediaFormat == null) {
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        }
                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (i10 == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else if (i10 == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat2 = mediaFormat;
                        } else if (i10 != -2) {
                            if (i10 >= 0) {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj10 = obj5;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                            outputBuffers3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    obj4 = obj6;
                    obj9 = obj7;
                    mediaFormat2 = mediaFormat;
                    outputBuffers3 = byteBufferArr2;
                    if (!this.mCopyAudio) {
                        if (this.mUserStop) {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        } else {
                            i3 = i7;
                            i4 = i8;
                            i5 = i9;
                            obj6 = obj4;
                            obj7 = obj9;
                            mediaFormat = mediaFormat2;
                            byteBufferArr2 = outputBuffers3;
                            obj4 = obj8;
                            mediaFormat2 = mediaFormat3;
                            outputBuffers3 = outputBuffers;
                        }
                    }
                    this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                    if (this.mCopyAudio) {
                        this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat2);
                    }
                    this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                    this.mMuxer.start();
                    this.mMuxerStarted = true;
                    if (this.mUserStop) {
                        i3 = i7;
                        i4 = i8;
                        i5 = i9;
                        obj6 = obj4;
                        obj7 = obj9;
                        mediaFormat = mediaFormat2;
                        byteBufferArr2 = outputBuffers3;
                        obj4 = obj8;
                        mediaFormat2 = mediaFormat3;
                        outputBuffers3 = outputBuffers;
                    } else {
                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                    }
                }
                GLES20.glClear(16384);
                this.mRenderer.draw();
                this.mLogoRenderer.draw();
                this.mInputSurface.setPresentationTime(computePresentationTimeNsec(i5));
                Log.d(Constants.TAG, "sending frame " + i5 + " to video encoder");
                this.mInputSurface.swapBuffers();
                i5++;
                i6 = i3 + 1;
                if (i6 != this.mFramesPerImage) {
                    i7 = i6;
                    i8 = i4;
                    i9 = i5;
                } else if (i4 + 1 != size) {
                    i10 = i4 + 1;
                    this.mRenderer.loadTexture((String) this.mInputFilePaths.get(i10), this.mOutputWidth, this.mOutputHeight);
                    i7 = 0;
                    i8 = i10;
                    i9 = i5;
                } else {
                    i7 = 0;
                    i8 = i4;
                    i9 = i5;
                }
                if (this.mCopyAudio) {
                    obj4 = obj6;
                    obj9 = obj7;
                    mediaFormat2 = mediaFormat;
                    outputBuffers3 = byteBufferArr2;
                } else {
                    if (mediaFormat == null) {
                        obj9 = obj7;
                        if (mediaFormat == null) {
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj4 = obj6;
                                } else {
                                    buffer = inputBuffers[dequeueInputBuffer];
                                    i3 = bufferInfo2.size;
                                    sampleTime = bufferInfo2.presentationTimeUs;
                                    if (i3 >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo2.offset);
                                        duplicate.limit(bufferInfo2.offset + i3);
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo2.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj4 = 1;
                                    } else {
                                        obj4 = obj6;
                                    }
                                }
                                if (mediaFormat == null) {
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                                i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (i10 == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat2 = mediaFormat;
                                } else if (i10 != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                    outputBuffers3 = byteBufferArr2;
                                } else if (i10 >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        } else {
                                            obj10 = obj5;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    }
                                }
                            }
                            obj4 = obj6;
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (i10 >= 0) {
                                    byteBuffer = byteBufferArr2[i10];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size != 0) {
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj10 = obj5;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj10 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        obj5 = obj10;
                                        outputBuffers3 = byteBufferArr2;
                                        mediaFormat2 = mediaFormat;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                        mediaFormat2 = mediaFormat;
                                        outputBuffers3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                        if (i6 != -1) {
                            Log.d(Constants.TAG, "audio decoder output buffer try again later");
                        } else if (i6 != -3) {
                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                        } else if (i6 != -2) {
                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                        } else if (i6 >= 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                        } else if ((bufferInfo2.flags & 2) != 0) {
                            i = i6;
                        } else {
                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                            this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                        }
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                buffer = inputBuffers[dequeueInputBuffer];
                                i3 = bufferInfo2.size;
                                sampleTime = bufferInfo2.presentationTimeUs;
                                if (i3 >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo2.offset);
                                    duplicate.limit(bufferInfo2.offset + i3);
                                    buffer.position(0);
                                    buffer.put(duplicate);
                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo2.flags & 4) == 0) {
                                    obj4 = obj6;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj4 = 1;
                                }
                            } else {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj4 = obj6;
                            }
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    } else {
                                        obj10 = obj5;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                }
                            }
                        }
                        obj4 = obj6;
                        if (mediaFormat == null) {
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        }
                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (i10 == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else if (i10 == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat2 = mediaFormat;
                        } else if (i10 != -2) {
                            if (i10 >= 0) {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj10 = obj5;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                            outputBuffers3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                    if (dequeueInputBuffer != -1) {
                        i3 = this.mAudioExtractor.readSampleData(inputBuffers2[dequeueInputBuffer], 0);
                        sampleTime = this.mAudioExtractor.getSampleTime();
                        if (sampleTime > this.mEncodedOutputDurationMs * 1000) {
                        }
                        if ((sampleTime > this.mEncodedOutputDurationMs * 1000 ? 1 : null) == null) {
                            if (i3 > 0) {
                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, this.mAudioExtractor.getSampleFlags());
                            }
                            if (this.mAudioExtractor.advance()) {
                            }
                            obj9 = this.mAudioExtractor.advance() ? null : 1;
                        } else {
                            obj9 = 1;
                        }
                        if (obj9 != null) {
                            Log.d(Constants.TAG, "audio decoder sending EOS");
                            this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                        }
                    } else {
                        Log.d(Constants.TAG, "audio decoder input try again later");
                        obj9 = obj7;
                    }
                    if (mediaFormat == null) {
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj4 = obj6;
                            } else {
                                buffer = inputBuffers[dequeueInputBuffer];
                                i3 = bufferInfo2.size;
                                sampleTime = bufferInfo2.presentationTimeUs;
                                if (i3 >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo2.offset);
                                    duplicate.limit(bufferInfo2.offset + i3);
                                    buffer.position(0);
                                    buffer.put(duplicate);
                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo2.flags & 4) == 0) {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj4 = 1;
                                } else {
                                    obj4 = obj6;
                                }
                            }
                            if (mediaFormat == null) {
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                            i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (i10 == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat2 = mediaFormat;
                            } else if (i10 != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                                outputBuffers3 = byteBufferArr2;
                            } else if (i10 >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    } else {
                                        obj10 = obj5;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                }
                            }
                        }
                        obj4 = obj6;
                        if (mediaFormat == null) {
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        }
                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (i10 == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else if (i10 == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat2 = mediaFormat;
                        } else if (i10 != -2) {
                            if (i10 >= 0) {
                                byteBuffer = byteBufferArr2[i10];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size != 0) {
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj10 = obj5;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj10 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    obj5 = obj10;
                                    outputBuffers3 = byteBufferArr2;
                                    mediaFormat2 = mediaFormat;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                    mediaFormat2 = mediaFormat;
                                    outputBuffers3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                            outputBuffers3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    i6 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                    if (i6 != -1) {
                        Log.d(Constants.TAG, "audio decoder output buffer try again later");
                    } else if (i6 != -3) {
                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                    } else if (i6 != -2) {
                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                    } else if (i6 >= 0) {
                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                    } else if ((bufferInfo2.flags & 2) != 0) {
                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                        this.mInputAudioDecoder.releaseOutputBuffer(i6, false);
                    } else {
                        i = i6;
                    }
                    if (this.mUserStop) {
                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            buffer = inputBuffers[dequeueInputBuffer];
                            i3 = bufferInfo2.size;
                            sampleTime = bufferInfo2.presentationTimeUs;
                            if (i3 >= 0) {
                                duplicate = byteBufferArr[i].duplicate();
                                duplicate.position(bufferInfo2.offset);
                                duplicate.limit(bufferInfo2.offset + i3);
                                buffer.position(0);
                                buffer.put(duplicate);
                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, i3, sampleTime, bufferInfo2.flags);
                            }
                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                            i = -1;
                            if ((bufferInfo2.flags & 4) == 0) {
                                obj4 = obj6;
                            } else {
                                Log.d(Constants.TAG, "audio decoder: EOS");
                                obj4 = 1;
                            }
                        } else {
                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                            obj4 = obj6;
                        }
                        if (mediaFormat == null) {
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        }
                        i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (i10 == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else if (i10 == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat2 = mediaFormat;
                        } else if (i10 != -2) {
                            if (this.mAudioTrackIndex < 0) {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                            mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                            outputBuffers3 = byteBufferArr2;
                        } else if (i10 >= 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        } else {
                            byteBuffer = byteBufferArr2[i10];
                            if ((bufferInfo3.flags & 2) == 0) {
                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            } else {
                                if (bufferInfo3.size != 0) {
                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                }
                                if ((bufferInfo3.flags & 4) == 0) {
                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                    obj10 = 1;
                                } else {
                                    obj10 = obj5;
                                }
                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                obj5 = obj10;
                                outputBuffers3 = byteBufferArr2;
                                mediaFormat2 = mediaFormat;
                            }
                        }
                    }
                    obj4 = obj6;
                    if (mediaFormat == null) {
                        mediaFormat2 = mediaFormat;
                        outputBuffers3 = byteBufferArr2;
                    }
                    i10 = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                    if (i10 == -1) {
                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                        mediaFormat2 = mediaFormat;
                        outputBuffers3 = byteBufferArr2;
                    } else if (i10 == -3) {
                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                        outputBuffers3 = this.mOutputAudioEncoder.getOutputBuffers();
                        mediaFormat2 = mediaFormat;
                    } else if (i10 != -2) {
                        if (i10 >= 0) {
                            byteBuffer = byteBufferArr2[i10];
                            if ((bufferInfo3.flags & 2) == 0) {
                                if (bufferInfo3.size != 0) {
                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer, bufferInfo3);
                                }
                                if ((bufferInfo3.flags & 4) == 0) {
                                    obj10 = obj5;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                    obj10 = 1;
                                }
                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                obj5 = obj10;
                                outputBuffers3 = byteBufferArr2;
                                mediaFormat2 = mediaFormat;
                            } else {
                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                this.mOutputAudioEncoder.releaseOutputBuffer(i10, false);
                                mediaFormat2 = mediaFormat;
                                outputBuffers3 = byteBufferArr2;
                            }
                        } else {
                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                            mediaFormat2 = mediaFormat;
                            outputBuffers3 = byteBufferArr2;
                        }
                    } else if (this.mAudioTrackIndex < 0) {
                        mediaFormat2 = this.mOutputAudioEncoder.getOutputFormat();
                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat2);
                        outputBuffers3 = byteBufferArr2;
                    } else {
                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                    }
                }
                if (!this.mCopyAudio) {
                    if (this.mUserStop) {
                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                    } else {
                        i3 = i7;
                        i4 = i8;
                        i5 = i9;
                        obj6 = obj4;
                        obj7 = obj9;
                        mediaFormat = mediaFormat2;
                        byteBufferArr2 = outputBuffers3;
                        obj4 = obj8;
                        mediaFormat2 = mediaFormat3;
                        outputBuffers3 = outputBuffers;
                    }
                }
                this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                if (this.mCopyAudio) {
                    this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat2);
                }
                this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                this.mMuxer.start();
                this.mMuxerStarted = true;
                if (this.mUserStop) {
                    i3 = i7;
                    i4 = i8;
                    i5 = i9;
                    obj6 = obj4;
                    obj7 = obj9;
                    mediaFormat = mediaFormat2;
                    byteBufferArr2 = outputBuffers3;
                    obj4 = obj8;
                    mediaFormat2 = mediaFormat3;
                    outputBuffers3 = outputBuffers;
                } else {
                    Log.d(Constants.TAG, "Encoding abruptly stopped.");
                }
            }
            return;
        }
    }

    public void startTransRewriting() throws IOException {
    }

    public void stop() {
        Log.d(Constants.TAG, "Stop method called");
        synchronized (this.mStopLock) {
            if (this.mEncoding) {
                this.mUserStop = true;
                try {
                    Log.d(Constants.TAG, "Calling wait on stop lock.");
                    this.mStopLock.wait();
                } catch (Throwable e) {
                    Log.d(Constants.TAG, "Stop lock interrupted.");
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
