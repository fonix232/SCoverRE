package com.samsung.android.app.interactivepanoramaviewer.sharevia;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class EncoderInterface {
    private static final String MIME_TYPE = "video/avc";
    private static final String TAG = "EncoderInterface";
    private static final boolean VERBOSE = true;
    int EncBitRate = 6000000;
    int FRAME_RATE;
    int IFRAME_INTERVAL = 5;
    int TIMEOUT_USEC = 0;
    int frameEncoded;
    private BufferInfo mBufferInfo;
    private MediaCodec mEncoder;
    private MediaMuxer mMuxer;
    private boolean mMuxerStarted;
    private int mOrientation;
    private int mTrackIndex;

    private void drainEncoder(boolean z, byte[] bArr) {
        Log.m29d(TAG, "drainEncoder(" + z + ")");
        ByteBuffer[] inputBuffers = this.mEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers = this.mEncoder.getOutputBuffers();
        boolean z2 = z;
        while (true) {
            int dequeueInputBuffer;
            boolean z3;
            if (z) {
                dequeueInputBuffer = this.mEncoder.dequeueInputBuffer(0);
                if (dequeueInputBuffer < 0) {
                    z3 = z2;
                } else {
                    this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                    Log.m29d(TAG, "sending EOS to encoder");
                    z3 = z2;
                }
            } else if (z2) {
                z3 = z2;
            } else {
                dequeueInputBuffer = this.mEncoder.dequeueInputBuffer(0);
                if (dequeueInputBuffer < 0) {
                    z3 = false;
                    if (dequeueInputBuffer == -1) {
                        Log.m29d(TAG, "no input available, spinning to encode buffer");
                    }
                } else {
                    Buffer buffer = inputBuffers[dequeueInputBuffer];
                    buffer.position(0);
                    buffer.put(bArr);
                    buffer.position(0);
                    int i = this.frameEncoded;
                    this.frameEncoded = i + 1;
                    long presentationTimeUs = getPresentationTimeUs(i);
                    Log.m35v(TAG, "ts of frame " + (this.frameEncoded - 1) + " is " + presentationTimeUs);
                    this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, bArr.length, presentationTimeUs, 0);
                    z3 = true;
                }
            }
            dequeueInputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, (long) this.TIMEOUT_USEC);
            if (dequeueInputBuffer != -1) {
                if (dequeueInputBuffer == -3) {
                    outputBuffers = this.mEncoder.getOutputBuffers();
                    z2 = z3;
                } else if (dequeueInputBuffer != -2) {
                    if (dequeueInputBuffer >= 0) {
                        Buffer buffer2 = outputBuffers[dequeueInputBuffer];
                        if (buffer2 != null) {
                            if ((this.mBufferInfo.flags & 2) != 0) {
                                Log.m29d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                                this.mBufferInfo.size = 0;
                            }
                            if (this.mBufferInfo.size != 0) {
                                if (this.mMuxerStarted) {
                                    buffer2.position(this.mBufferInfo.offset);
                                    buffer2.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                                    this.mMuxer.writeSampleData(this.mTrackIndex, buffer2, this.mBufferInfo);
                                    Log.m29d(TAG, "sent " + this.mBufferInfo.size + " bytes to muxer");
                                } else {
                                    throw new RuntimeException("muxer hasn't started");
                                }
                            }
                            this.mEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                            if ((this.mBufferInfo.flags & 4) != 0) {
                                break;
                            }
                            z2 = z3;
                        } else {
                            throw new RuntimeException("encoderOutputBuffer " + dequeueInputBuffer + " was null");
                        }
                    }
                    Log.m37w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + dequeueInputBuffer);
                    z2 = z3;
                } else if (this.mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                } else {
                    MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                    Log.m29d(TAG, "encoder output format changed: " + outputFormat);
                    this.mTrackIndex = this.mMuxer.addTrack(outputFormat);
                    this.mMuxer.start();
                    this.mMuxerStarted = true;
                    z2 = z3;
                }
            } else if (z || !z3) {
                Log.m29d(TAG, "no output available, spinning to await EOS");
                z2 = z3;
            } else {
                return;
            }
        }
        if (z) {
            Log.m29d(TAG, "end of stream reached");
        } else {
            Log.m37w(TAG, "reached end of stream unexpectedly");
        }
    }

    private int prepareEncoder(int i, int i2, int i3, int i4, int i5, int i6, String str) {
        this.mBufferInfo = new BufferInfo();
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MIME_TYPE, i, i2);
        createVideoFormat.setInteger("color-format", 21);
        createVideoFormat.setInteger("bitrate", i3);
        createVideoFormat.setInteger("frame-rate", i4);
        createVideoFormat.setInteger("i-frame-interval", i5);
        Log.m29d(TAG, "format: " + createVideoFormat);
        try {
            this.mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
            this.mEncoder.configure(createVideoFormat, null, null, 1);
            this.mEncoder.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (this.mEncoder != null) {
            Log.m33i(TAG, "Output file is " + str);
            try {
                this.mMuxer = new MediaMuxer(str, 0);
                this.mTrackIndex = -1;
                this.mMuxerStarted = false;
                return 0;
            } catch (IOException e2) {
                Log.m37w(TAG, "MediaMuxer creation failed");
                return -1;
            }
        }
        Log.m31e(TAG, "mEncoder is null returning");
        return -1;
    }

    private void releaseEncoder() {
        Log.m29d(TAG, "releasing encoder objects");
        if (this.mEncoder != null) {
            this.mEncoder.stop();
            this.mEncoder.release();
            this.mEncoder = null;
        }
        if (this.mMuxer != null) {
            this.mMuxer.stop();
            this.mMuxer.release();
            this.mMuxer = null;
        }
    }

    public void deinit() {
        drainEncoder(true, null);
        releaseEncoder();
    }

    public void encode(byte[] bArr) {
        drainEncoder(false, bArr);
    }

    long getPresentationTimeUs(int i) {
        return ((long) i) * ((long) (1000000 / this.FRAME_RATE));
    }

    public int init(int i, int i2, int i3, int i4, int i5, int i6, String str, int i7) {
        if (str == null || str.isEmpty()) {
            Log.m29d(TAG, "wrong arguments to init");
            return -1;
        } else if (i6 == 21) {
            this.FRAME_RATE = i4;
            this.EncBitRate = i3;
            this.mOrientation = i7;
            Log.m29d(TAG, "video/avc output " + i + "x" + i2 + " @" + i3);
            return prepareEncoder(i, i2, i3, i4, i5, i6, str);
        } else {
            Log.m37w(TAG, "color format not supported as of now");
            return -1;
        }
    }

    public void setTimeOutUs(int i) {
        this.TIMEOUT_USEC = i;
    }
}
