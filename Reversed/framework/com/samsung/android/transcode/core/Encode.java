package com.samsung.android.transcode.core;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaMuxer;
import android.util.Log;
import com.samsung.android.transcode.util.Constants;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public abstract class Encode {
    protected static final int ENC_FULL_TRANSCODE = 0;
    protected static final int ENC_REWRITE = 1;
    protected static final int ENC_TRANS_REWRITE = 2;
    protected static final int ENC_UNABLE_TO_COMPLY = -1;
    private static final long ONE_BILLION = 1000000000;
    protected static final int ORIENTATION_0 = 0;
    protected static final int ORIENTATION_180 = 180;
    protected static final int ORIENTATION_270 = 270;
    protected static final int ORIENTATION_90 = 90;
    private static final String VERSION = "1.44";
    protected boolean m2ndTimeEncoding = false;
    protected int mAudioTrackIndex = -1;
    protected EncodeEventListener mEncodeEventListener;
    protected int mFramesSkipInterval;
    protected MediaCodec mInputAudioDecoder;
    protected MediaCodec mInputVideoDecoder;
    protected Logo mLogo;
    protected boolean mLogoPresent;
    protected boolean mMMSMode = false;
    protected MediaMuxer mMuxer;
    protected boolean mMuxerStarted;
    protected int mOriginalAudioChannelCount = 0;
    protected int mOutputAudioAACProfile = 2;
    protected int mOutputAudioBitRate = 128000;
    protected int mOutputAudioChannelCount = 2;
    protected MediaCodec mOutputAudioEncoder;
    protected String mOutputAudioMimeType = CodecsMime.AUDIO_CODEC_AAC;
    protected int mOutputAudioSampleRateHZ = 44100;
    protected String mOutputFilePath;
    protected int mOutputFormat = 0;
    protected int mOutputHeight;
    protected long mOutputMaxSizeKB = -1;
    protected int mOutputVideoBitRate;
    protected MediaCodec mOutputVideoEncoder;
    protected int mOutputVideoFrameRate = 30;
    protected int mOutputVideoIFrameInterval = 1;
    protected String mOutputVideoMimeType = CodecsMime.VIDEO_CODEC_H264;
    protected int mOutputVideoProfile = -1;
    protected int mOutputWidth;
    protected float mSizeFraction = 0.8f;
    protected boolean mSkipFrames;
    protected int mTransRewritable = 0;
    protected volatile boolean mUserStop = false;
    protected int mVideoTrackIndex = -1;

    public interface EncodeEventListener {
        void onCompleted();

        void onStarted();
    }

    public static final class BitRate {
        public static final int AUDIO_AAC_BITRATE = 128;
        public static final int MID_AUDIO_AAC_BITRATE = 64;
        public static final int MIN_AUDIO_AAC_BITRATE = 32;
        public static final int MIN_AUDIO_AMR_BITRATE = 8;
        public static final int MIN_VIDEO_D1_BITRATE = 500;
        public static final int MIN_VIDEO_FHD_BITRATE = 1000;
        public static final int MIN_VIDEO_HD_BITRATE = 550;
        public static final int MIN_VIDEO_QCIF_BITRATE = 64;
        public static final int MIN_VIDEO_QVGA_BITRATE = 150;
        public static final int MIN_VIDEO_VGA_BITRATE = 350;
        public static final int VIDEO_D1_BITRATE = 5000;
        public static final int VIDEO_DEFAULT_BITRATE = 2000;
        public static final int VIDEO_FHD_BITRATE = 10000;
        public static final int VIDEO_HD_BITRATE = 8000;
        public static final int VIDEO_QCIF_BITRATE = 280;
        public static final int VIDEO_QVGA_BITRATE = 512;
        public static final int VIDEO_VGA_BITRATE = 5000;
    }

    public static final class CodecType {
        public static final int AUDIO_CODEC_AAC = 2;
        public static final int AUDIO_CODEC_AMR = 1;
        public static final int VIDEO_CODEC_H263 = 3;
        public static final int VIDEO_CODEC_H264 = 4;
    }

    public static final class CodecsMime {
        public static final String AUDIO_CODEC_AAC = "audio/mp4a-latm";
        public static final String AUDIO_CODEC_AMR = "audio/3gpp";
        public static final String VIDEO_CODEC_H263 = "video/3gpp";
        public static final String VIDEO_CODEC_H264 = "video/avc";
    }

    public static final class ContentType {
        public static final String VIDEO_3G2 = "video/3gpp2";
        public static final String VIDEO_3GP = "video/3gp";
        public static final String VIDEO_3GPP = "video/3gpp";
        public static final String VIDEO_ASF = "video/x-ms-asf";
        public static final String VIDEO_AVI = "video/avi";
        public static final String VIDEO_DIVX = "video/divx";
        public static final String VIDEO_FLV = "video/flv";
        public static final String VIDEO_MKV = "video/x-matroska";
        public static final String VIDEO_MP4 = "video/mp4";
        public static final String VIDEO_MP4V_ES = "video/mp4v-es";
        public static final String VIDEO_MPEG = "video/mpeg";
        public static final String VIDEO_UNSPECIFIED = "video/*";
        public static final String VIDEO_WMV = "video/x-ms-wmv";
        public static final ArrayList<String> sSupportedVideoTypes = new ArrayList();

        static {
            sSupportedVideoTypes.add("video/3gpp");
            sSupportedVideoTypes.add(VIDEO_3G2);
            sSupportedVideoTypes.add(VIDEO_MP4);
            sSupportedVideoTypes.add(VIDEO_MP4V_ES);
            sSupportedVideoTypes.add(VIDEO_3GP);
            sSupportedVideoTypes.add(VIDEO_AVI);
            sSupportedVideoTypes.add(VIDEO_WMV);
            sSupportedVideoTypes.add(VIDEO_ASF);
            sSupportedVideoTypes.add(VIDEO_DIVX);
            sSupportedVideoTypes.add(VIDEO_MPEG);
            sSupportedVideoTypes.add(VIDEO_MKV);
            sSupportedVideoTypes.add(VIDEO_FLV);
        }
    }

    public static final class EncodeResolution {
        public static final int D1 = 4;
        public static final int FULL_HD = 6;
        public static final int HD = 5;
        public static final int QCIF = 1;
        public static final int QVGA = 2;
        public static final int VGA = 3;

        public static boolean isSupportedResolution(int i) {
            return i >= 1 && i <= 6;
        }
    }

    protected static class Logo {
        public static final int LEFT_MARGIN = 20;
        public static final int TOP_MARGIN = 20;
        public Bitmap mLogoBitmap;
        public int mLogoDrawHeight;
        public int mLogoDrawWidth;
        public int mTopX;
        public int mTopY;
    }

    private void printVersionInfo() {
        Log.d(Constants.TAG, "Transcode Framework v.1.44");
    }

    private void setLogoData(Bitmap bitmap, int i, int i2) {
        this.mLogo = new Logo();
        this.mLogo.mLogoBitmap = bitmap;
        this.mLogo.mLogoDrawWidth = i;
        this.mLogo.mLogoDrawHeight = i2;
        this.mLogo.mTopX = 20;
        this.mLogo.mTopY = 20;
        this.mLogoPresent = true;
    }

    protected long computePresentationTimeNsec(int i) {
        return (((long) i) * ONE_BILLION) / ((long) this.mOutputVideoFrameRate);
    }

    public void encode() throws IOException {
        printVersionInfo();
        try {
            Log.d(Constants.TAG, "starting encoder prepartion");
            prepare();
            Log.d(Constants.TAG, "encoder preparation done.");
            this.mMuxer = new MediaMuxer(this.mOutputFilePath, this.mOutputFormat);
            this.mMuxerStarted = false;
            this.mVideoTrackIndex = -1;
            this.mAudioTrackIndex = -1;
            Log.d(Constants.TAG, "starting to encode");
            if (this.mEncodeEventListener != null) {
                this.mEncodeEventListener.onStarted();
            }
            startEncoding();
            Log.d(Constants.TAG, "encoding finished.");
            File file = new File(this.mOutputFilePath);
            long length = file.length();
            Log.d(Constants.TAG, "generated output file size after muxer close " + length);
            if (!this.mUserStop && this.mOutputMaxSizeKB != -1 && ((double) length) / 1024.0d > ((double) this.mOutputMaxSizeKB) && (this instanceof EncodeVideo)) {
                Log.d(Constants.TAG, "file size exceeded the max size limit " + this.mOutputMaxSizeKB);
                if (!file.delete()) {
                    Log.d(Constants.TAG, "Could not clean up file: " + file.getAbsolutePath());
                }
                this.mSizeFraction = 0.7f;
                this.mSkipFrames = true;
                if (this.mFramesSkipInterval >= 2) {
                    this.mFramesSkipInterval *= 2;
                } else {
                    this.mFramesSkipInterval = 2;
                }
                if (this.mOutputWidth == 176) {
                    this.mOutputWidth = 128;
                    this.mOutputHeight = 96;
                }
                try {
                    Log.d(Constants.TAG, "2nd time starting encoder preparation");
                    this.m2ndTimeEncoding = true;
                    prepare();
                    Log.d(Constants.TAG, "2nd time encoder preparation done.");
                    this.mMuxer = new MediaMuxer(this.mOutputFilePath, this.mOutputFormat);
                    this.mMuxerStarted = false;
                    this.mVideoTrackIndex = -1;
                    this.mAudioTrackIndex = -1;
                    Log.d(Constants.TAG, "2nd time starting to encode");
                    startEncoding();
                    Log.d(Constants.TAG, "2nd time encoding finished.");
                    Log.d(Constants.TAG, "2nd time generated output file size after muxer close " + new File(this.mOutputFilePath).length());
                    this.m2ndTimeEncoding = false;
                } finally {
                    release();
                }
            }
            if (this.mEncodeEventListener != null) {
                if (this.mUserStop) {
                    Log.d(Constants.TAG, "user stopped. Not calling onCompleted");
                } else {
                    Log.d(Constants.TAG, "calling onCompleted");
                    this.mEncodeEventListener.onCompleted();
                }
                this.mEncodeEventListener = null;
            }
        } finally {
            release();
        }
    }

    protected abstract void prepare() throws IOException;

    protected abstract void prepare_for_transrewrite() throws IOException;

    protected abstract void release();

    public void setLogo(AssetManager assetManager, String str) throws IOException {
        InputStream open = assetManager.open(str);
        Bitmap decodeStream = BitmapFactory.decodeStream(open);
        open.close();
        if (decodeStream != null) {
            int i = (int) (((float) this.mOutputWidth) * 0.075f);
            setLogoData(decodeStream, i, i);
        }
    }

    public void setLogo(Bitmap bitmap) {
        int i = (int) (((float) this.mOutputWidth) * 0.075f);
        setLogoData(bitmap, i, i);
    }

    public void setLogo(String str) {
        Bitmap decodeFile = BitmapFactory.decodeFile(str);
        if (decodeFile != null) {
            int i = (int) (((float) this.mOutputWidth) * 0.075f);
            setLogoData(decodeFile, i, i);
        }
    }

    public void setProgressUpdateListener(EncodeEventListener encodeEventListener) {
        this.mEncodeEventListener = encodeEventListener;
    }

    protected abstract void startEncoding() throws IOException;

    public abstract void startTransRewriting() throws IOException;

    public abstract void stop();

    public int suggestBitRate(int i, int i2) {
        return i < 1920 ? i < GL10.GL_INVALID_ENUM ? (i >= 720 || i >= 640) ? 5000 : i < 320 ? BitRate.VIDEO_QCIF_BITRATE : 512 : BitRate.VIDEO_HD_BITRATE : 10000;
    }

    public void transRewrite() throws IOException {
        printVersionInfo();
        try {
            Log.d(Constants.TAG, "starting transRewrite prepartion");
            prepare_for_transrewrite();
            Log.d(Constants.TAG, "transRewrite preparation done.");
            this.mMuxer = new MediaMuxer(this.mOutputFilePath, this.mOutputFormat);
            this.mMuxerStarted = false;
            this.mVideoTrackIndex = -1;
            this.mAudioTrackIndex = -1;
            Log.d(Constants.TAG, "starting transRewrite");
            if (this.mEncodeEventListener != null) {
                this.mEncodeEventListener.onStarted();
            }
            if (this.mTransRewritable != 0) {
                startTransRewriting();
            } else {
                startEncoding();
            }
            Log.d(Constants.TAG, "transRewrite finished.");
            if (this.mEncodeEventListener != null) {
                if (this.mUserStop) {
                    Log.d(Constants.TAG, "user stopped. Not calling onCompleted");
                } else {
                    Log.d(Constants.TAG, "calling onCompleted");
                    this.mEncodeEventListener.onCompleted();
                }
                this.mEncodeEventListener = null;
            }
        } finally {
            release();
        }
    }
}
