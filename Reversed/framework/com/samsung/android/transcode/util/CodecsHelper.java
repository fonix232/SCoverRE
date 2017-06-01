package com.samsung.android.transcode.util;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.Surface;
import com.samsung.android.transcode.core.Encode.BitRate;
import com.samsung.android.transcode.core.Encode.CodecsMime;
import com.samsung.android.transcode.core.Encode.ContentType;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.microedition.khronos.opengles.GL10;

public class CodecsHelper {
    public static int GetCodecResource(int i, int i2) {
        int i3;
        Throwable th;
        int i4 = -1;
        Log.d(Constants.TAG, "GetCodecResource");
        try {
            MediaCodec createEncoderByType = MediaCodec.createEncoderByType(CodecsMime.VIDEO_CODEC_H264);
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(CodecsMime.VIDEO_CODEC_H264, i, i2);
            createVideoFormat.setInteger("bitrate", 120000);
            createVideoFormat.setInteger("frame-rate", 30);
            createVideoFormat.setInteger("i-frame-interval", 1);
            createVideoFormat.setInteger("color-format", 21);
            if (createEncoderByType == null) {
                i3 = -1;
            } else {
                try {
                    createEncoderByType.configure(createVideoFormat, null, null, 1);
                    int integer = createEncoderByType.getOutputFormat().getInteger("max_capacity");
                    i3 = createEncoderByType.getOutputFormat().getInteger("remained_resource");
                    try {
                        Log.d(Constants.TAG, "max_capacity = " + integer + ", remained_resource = " + i3);
                        createEncoderByType.release();
                    } catch (Throwable e) {
                        Throwable th2 = e;
                        i4 = i3;
                        th = th2;
                        Log.d(Constants.TAG, "can't get resource - remained_resource = " + i4);
                        th.printStackTrace();
                        return i4;
                    }
                } catch (Exception e2) {
                    th = e2;
                    Log.d(Constants.TAG, "can't get resource - remained_resource = " + i4);
                    th.printStackTrace();
                    return i4;
                }
            }
            i4 = i3;
            return i4;
        } catch (Throwable th3) {
            th3.printStackTrace();
            return -1;
        }
    }

    public static MediaCodec createAudioDecoder(MediaCodecInfo mediaCodecInfo, MediaFormat mediaFormat) throws IOException {
        MediaCodec createByCodecName = MediaCodec.createByCodecName(mediaCodecInfo.getName());
        createByCodecName.configure(mediaFormat, null, null, 0);
        createByCodecName.start();
        return createByCodecName;
    }

    public static MediaCodec createAudioDecoder(MediaFormat mediaFormat) throws IOException {
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType(getMimeTypeFor(mediaFormat));
        createDecoderByType.configure(mediaFormat, null, null, 0);
        createDecoderByType.start();
        return createDecoderByType;
    }

    public static MediaCodec createAudioEncoder(MediaCodecInfo mediaCodecInfo, MediaFormat mediaFormat) throws IOException {
        MediaCodec createByCodecName = MediaCodec.createByCodecName(mediaCodecInfo.getName());
        createByCodecName.configure(mediaFormat, null, null, 1);
        createByCodecName.start();
        return createByCodecName;
    }

    public static MediaExtractor createExtractor(AssetManager assetManager, String str) throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        AssetFileDescriptor openFd = assetManager.openFd(str);
        mediaExtractor.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
        openFd.close();
        return mediaExtractor;
    }

    public static MediaExtractor createExtractor(FileDescriptor fileDescriptor, long j, long j2) throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.semSetRunningMode(1);
        mediaExtractor.setDataSource(fileDescriptor, j, j2);
        return mediaExtractor;
    }

    public static MediaExtractor createExtractor(String str) throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        mediaExtractor.semSetRunningMode(1);
        mediaExtractor.setDataSource(str);
        return mediaExtractor;
    }

    public static MediaMetadataRetriever createMediaMetadataRetriever(String str) throws IOException {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return mediaMetadataRetriever;
    }

    public static MediaCodec createVideoDecoder(MediaFormat mediaFormat, Surface surface) throws IOException {
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType(getMimeTypeFor(mediaFormat));
        Log.d(Constants.TAG, "createVideoDecoder");
        try {
            createDecoderByType.configure(mediaFormat, surface, null, 0);
            createDecoderByType.start();
            Log.d(Constants.TAG, "createVideoDecoder - start");
            return createDecoderByType;
        } catch (Throwable e) {
            e.printStackTrace();
            if (createDecoderByType != null) {
                createDecoderByType.release();
            }
            throw new IOException("createVideoDecode configure error");
        }
    }

    public static void fillResolutionRect(int i, Rect rect) {
        switch (i) {
            case 1:
                rect.set(0, 0, 176, 144);
                return;
            case 2:
                rect.set(0, 0, 320, 240);
                return;
            case 3:
                rect.set(0, 0, 640, 480);
                return;
            case 4:
                rect.set(0, 0, 720, 480);
                return;
            case 5:
                rect.set(0, 0, GL10.GL_INVALID_ENUM, 720);
                return;
            case 6:
                rect.set(0, 0, 1920, 1080);
                return;
            default:
                return;
        }
    }

    public static int getAndSelectAudioTrackIndex(MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
            if (isAudioFormat(mediaExtractor.getTrackFormat(i))) {
                mediaExtractor.selectTrack(i);
                return i;
            }
        }
        return -1;
    }

    public static int getAndSelectVideoTrackIndex(MediaExtractor mediaExtractor) {
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
            if (isVideoFormat(mediaExtractor.getTrackFormat(i))) {
                mediaExtractor.selectTrack(i);
                return i;
            }
        }
        return -1;
    }

    public static MediaCodecInfo getDecoderCodec(String str) {
        MediaCodecInfo isSecCodecAvailable = isSecCodecAvailable(str, false);
        if (isSecCodecAvailable == null) {
            int codecCount = MediaCodecList.getCodecCount();
            int i = 0;
            while (i < codecCount) {
                MediaCodecInfo mediaCodecInfo;
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
                if (codecInfoAt.isEncoder()) {
                    mediaCodecInfo = isSecCodecAvailable;
                } else {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                            mediaCodecInfo = codecInfoAt;
                            break;
                        }
                    }
                    mediaCodecInfo = isSecCodecAvailable;
                }
                i++;
                isSecCodecAvailable = mediaCodecInfo;
            }
        }
        return isSecCodecAvailable;
    }

    public static MediaCodecInfo getEncoderCodec(String str) {
        MediaCodecInfo isSecCodecAvailable = isSecCodecAvailable(str, true);
        if (isSecCodecAvailable == null) {
            int codecCount = MediaCodecList.getCodecCount();
            int i = 0;
            while (i < codecCount) {
                MediaCodecInfo mediaCodecInfo;
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
                if (codecInfoAt.isEncoder()) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                            mediaCodecInfo = codecInfoAt;
                            break;
                        }
                    }
                    mediaCodecInfo = isSecCodecAvailable;
                } else {
                    mediaCodecInfo = isSecCodecAvailable;
                }
                i++;
                isSecCodecAvailable = mediaCodecInfo;
            }
        }
        return isSecCodecAvailable;
    }

    private static String getMimeTypeFor(MediaFormat mediaFormat) {
        return mediaFormat.getString("mime");
    }

    public static int getVideoEncodingBitRate(float f, long j, long j2, int i, int i2, int i3) {
        int i4 = 0;
        int i5 = ((int) ((((((float) j) * f) * 8.0f) * 1000.0f) / ((float) j2))) - (i + 2);
        int i6 = (i2 * i3) / 256;
        if (i6 < 100) {
            i6 = 64;
            i4 = 2000;
        } else if (i6 > 100 && i6 < 1000) {
            i6 = 150;
            i4 = 5000;
        } else if (i6 <= 1000) {
            i6 = 0;
        } else {
            i6 = BitRate.MIN_VIDEO_VGA_BITRATE;
            i4 = 15000;
        }
        return i5 >= i6 ? i5 <= i4 ? i5 : i4 : i6;
    }

    private static boolean isAudioFormat(MediaFormat mediaFormat) {
        return getMimeTypeFor(mediaFormat).startsWith("audio/");
    }

    private static MediaCodecInfo isSecCodecAvailable(String str, boolean z) {
        MediaCodecInfo mediaCodecInfo = null;
        for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            String name = codecInfoAt.getName();
            if (codecInfoAt.isEncoder() == z) {
                if (name.equals("OMX.SEC.naac.enc") || name.equals("OMX.SEC.aac.enc")) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    MediaCodecInfo mediaCodecInfo2 = mediaCodecInfo;
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                            mediaCodecInfo2 = codecInfoAt;
                        }
                    }
                    mediaCodecInfo = mediaCodecInfo2;
                }
            }
        }
        return mediaCodecInfo;
    }

    public static boolean isSupportedCodec(MediaFormat mediaFormat) {
        return ContentType.sSupportedVideoTypes.contains(mediaFormat.getString("mime"));
    }

    public static boolean isSupportedFormat(MediaMetadataRetriever mediaMetadataRetriever) {
        return ContentType.sSupportedVideoTypes.contains(mediaMetadataRetriever.extractMetadata(12));
    }

    public static boolean isSupportedFormat(String str) {
        boolean z;
        Throwable e;
        String str2 = "";
        if (str == null) {
            return false;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                mediaMetadataRetriever.setDataSource(fileInputStream.getFD());
                z = mediaMetadataRetriever.extractMetadata(12).contains(ContentType.VIDEO_MP4);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e2) {
                    }
                }
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e4) {
                            z = false;
                        }
                    }
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                    z = false;
                    return z;
                } catch (Throwable th) {
                    e = th;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e5) {
                            throw e;
                        }
                    }
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                    throw e;
                }
            }
        } catch (Exception e6) {
            e = e6;
            fileInputStream = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            z = false;
            return z;
        } catch (Throwable th2) {
            e = th2;
            fileInputStream = null;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            throw e;
        }
        return z;
    }

    private static boolean isVideoFormat(MediaFormat mediaFormat) {
        return getMimeTypeFor(mediaFormat).startsWith("video/");
    }

    public static void scheduleAfter(int i, Runnable runnable) throws InterruptedException, ExecutionException {
        ((ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2)).schedule(runnable, (long) i, TimeUnit.SECONDS);
    }
}
