package com.samsung.android.media.codec;

import com.samsung.android.transcode.core.Encode.EncodeEventListener;
import com.samsung.android.transcode.core.EncodeVideo;
import java.io.IOException;

public class SemVideoTranscoder {
    private EncodeVideo mEncodeVideo = new EncodeVideo();
    private ProgressEventListener mProgressEventListener;

    class C02231 implements EncodeEventListener {
        C02231() {
        }

        public void onCompleted() {
            SemVideoTranscoder.this.mProgressEventListener.onCompleted();
        }

        public void onStarted() {
            SemVideoTranscoder.this.mProgressEventListener.onStarted();
        }
    }

    public static final class CodecType {
        public static final int AUDIO_CODEC_AAC = 2;
        public static final int AUDIO_CODEC_AMR = 1;
        public static final int VIDEO_CODEC_H263 = 3;
        public static final int VIDEO_CODEC_H264 = 4;

        private CodecType() {
        }
    }

    public interface ProgressEventListener {
        void onCompleted();

        void onStarted();
    }

    public static int getMaxEncodingDuration(int i, int i2, int i3, int i4) {
        return EncodeVideo.getMaxEncodingDuration(i, i2, i3, i4);
    }

    public void encode() throws IOException {
        this.mEncodeVideo.encode();
    }

    public int getOutputFileSize() {
        return this.mEncodeVideo.getOutputFileSize();
    }

    public void initialize(String str, int i, int i2, String str2) throws IOException {
        this.mEncodeVideo.initialize(str, i, i2, str2);
    }

    public void setEncodingCodecs(int i, int i2) {
        this.mEncodeVideo.setEncodingCodecs(i, i2);
    }

    public void setMaxOutputSize(int i) {
        this.mEncodeVideo.setMaxOutputSize(i);
    }

    public void setProgressEventListener(ProgressEventListener progressEventListener) {
        this.mProgressEventListener = progressEventListener;
        this.mEncodeVideo.setProgressUpdateListener(new C02231());
    }

    public void setTrimTime(long j, long j2) {
        this.mEncodeVideo.setTrimTime(j, j2);
    }

    public void stop() {
        this.mEncodeVideo.stop();
    }
}
