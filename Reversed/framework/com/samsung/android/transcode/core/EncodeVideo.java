package com.samsung.android.transcode.core;

import android.graphics.Rect;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.opengl.GLES20;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.transcode.core.Encode.BitRate;
import com.samsung.android.transcode.core.Encode.CodecsMime;
import com.samsung.android.transcode.core.Encode.ContentType;
import com.samsung.android.transcode.core.Encode.EncodeResolution;
import com.samsung.android.transcode.renderer.RenderTexture_GL_2d;
import com.samsung.android.transcode.surfaces.InputSurface;
import com.samsung.android.transcode.surfaces.OutputSurface;
import com.samsung.android.transcode.util.CodecsHelper;
import com.samsung.android.transcode.util.Constants;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class EncodeVideo extends Encode {
    private static final int IMAGE_WAIT_TIMEOUT_MS = 1000;
    private static final String KEY_ERROR_TYPE = "error-type";
    private static final int REWRITE_AUDIO_BUFFER_SIZE = 131072;
    private static final long TIMEOUT_USEC = 10000;
    private boolean formatupdated = false;
    private MediaExtractor mAudioExtractor;
    private boolean mCopyAudio;
    private volatile boolean mEncoding = false;
    private String mInputFilePath;
    private int mInputOrientationDegrees = 0;
    private InputSurface mInputSurface;
    private RenderTexture_GL_2d mLogoRenderer;
    private OutputSurface mOutputSurface;
    private Object mStopLock = new Object();
    private long mTrimAudioEndUs;
    private long mTrimAudioStartUs;
    private long mTrimVideoEndUs;
    private long mTrimVideoStartUs;
    private MediaExtractor mVideoExtractor;

    class C02601 implements Runnable {
        C02601() {
        }

        public void run() {
            EncodeVideo.this.formatupdated = true;
        }
    }

    private static class Debugger {
        private Debugger() {
        }

        public static boolean isEnabled() {
            return false;
        }

        public static void log(Object obj) {
            if (isEnabled()) {
                Log.d(Constants.TAG, obj.toString());
            }
        }
    }

    private static class SpsPps {
        byte[] _pps;
        byte[] _sps;

        private SpsPps() {
        }
    }

    private boolean CheckVideoCodec(int i, int i2, String str, boolean z) {
        int andSelectVideoTrackIndex;
        int integer;
        Throwable e;
        int i3;
        MediaExtractor mediaExtractor = null;
        int GetCodecResource = z ? 0 : CodecsHelper.GetCodecResource(i, i2);
        try {
            mediaExtractor = CodecsHelper.createExtractor(str);
            if (mediaExtractor != null) {
                andSelectVideoTrackIndex = CodecsHelper.getAndSelectVideoTrackIndex(mediaExtractor);
                if (andSelectVideoTrackIndex >= 0) {
                    MediaFormat trackFormat = mediaExtractor.getTrackFormat(andSelectVideoTrackIndex);
                    integer = trackFormat.getInteger("width");
                    try {
                        andSelectVideoTrackIndex = trackFormat.getInteger("height");
                    } catch (IOException e2) {
                        e = e2;
                        andSelectVideoTrackIndex = 0;
                        i3 = integer;
                        integer = 0;
                        try {
                            e.printStackTrace();
                            if (mediaExtractor != null) {
                                mediaExtractor.release();
                            }
                            if (andSelectVideoTrackIndex != 0) {
                                Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                                return false;
                            }
                            if (!z) {
                                Log.d(Constants.TAG, "Codec resource is not enough");
                                return false;
                            }
                            if (i3 > 0) {
                                return true;
                            }
                            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                            return false;
                        } catch (Throwable th) {
                            if (mediaExtractor != null) {
                                mediaExtractor.release();
                            }
                        }
                    } catch (NullPointerException e3) {
                        e = e3;
                        andSelectVideoTrackIndex = 0;
                        i3 = integer;
                        integer = 0;
                        e.printStackTrace();
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        if (andSelectVideoTrackIndex != 0) {
                            if (z) {
                                Log.d(Constants.TAG, "Codec resource is not enough");
                                return false;
                            }
                            if (i3 > 0) {
                                return true;
                            }
                            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                            return false;
                        }
                        Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                        return false;
                    } catch (IllegalArgumentException e4) {
                        e = e4;
                        andSelectVideoTrackIndex = 0;
                        i3 = integer;
                        integer = 0;
                        e.printStackTrace();
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        if (andSelectVideoTrackIndex != 0) {
                            Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                            return false;
                        }
                        if (z) {
                            Log.d(Constants.TAG, "Codec resource is not enough");
                            return false;
                        }
                        if (i3 > 0) {
                            return true;
                        }
                        Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                        return false;
                    }
                    try {
                        if (trackFormat.containsKey(KEY_ERROR_TYPE)) {
                            i3 = integer;
                            integer = andSelectVideoTrackIndex;
                            andSelectVideoTrackIndex = trackFormat.getInteger(KEY_ERROR_TYPE);
                        } else {
                            i3 = integer;
                            integer = andSelectVideoTrackIndex;
                            andSelectVideoTrackIndex = 0;
                        }
                    } catch (IOException e5) {
                        e = e5;
                        i3 = integer;
                        integer = andSelectVideoTrackIndex;
                        andSelectVideoTrackIndex = 0;
                        e.printStackTrace();
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        if (andSelectVideoTrackIndex != 0) {
                            if (z) {
                                Log.d(Constants.TAG, "Codec resource is not enough");
                                return false;
                            }
                            if (i3 > 0) {
                                return true;
                            }
                            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                            return false;
                        }
                        Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                        return false;
                    } catch (NullPointerException e6) {
                        e = e6;
                        i3 = integer;
                        integer = andSelectVideoTrackIndex;
                        andSelectVideoTrackIndex = 0;
                        e.printStackTrace();
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        if (andSelectVideoTrackIndex != 0) {
                            Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                            return false;
                        }
                        if (z) {
                            Log.d(Constants.TAG, "Codec resource is not enough");
                            return false;
                        }
                        if (i3 > 0) {
                            return true;
                        }
                        Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                        return false;
                    } catch (IllegalArgumentException e7) {
                        e = e7;
                        i3 = integer;
                        integer = andSelectVideoTrackIndex;
                        andSelectVideoTrackIndex = 0;
                        e.printStackTrace();
                        if (mediaExtractor != null) {
                            mediaExtractor.release();
                        }
                        if (andSelectVideoTrackIndex != 0) {
                            if (z) {
                                Log.d(Constants.TAG, "Codec resource is not enough");
                                return false;
                            }
                            if (i3 > 0) {
                                return true;
                            }
                            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                            return false;
                        }
                        Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                        return false;
                    }
                }
                try {
                    Log.d(Constants.TAG, "Can't get VideoTrack");
                    integer = 0;
                    i3 = 0;
                } catch (IOException e8) {
                    e = e8;
                    integer = 0;
                    i3 = 0;
                    e.printStackTrace();
                    if (mediaExtractor != null) {
                        mediaExtractor.release();
                    }
                    if (andSelectVideoTrackIndex != 0) {
                        if (z) {
                            Log.d(Constants.TAG, "Codec resource is not enough");
                            return false;
                        }
                        if (i3 > 0) {
                            return true;
                        }
                        Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                        return false;
                    }
                    Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                    return false;
                } catch (NullPointerException e9) {
                    e = e9;
                    integer = 0;
                    i3 = 0;
                    e.printStackTrace();
                    if (mediaExtractor != null) {
                        mediaExtractor.release();
                    }
                    if (andSelectVideoTrackIndex != 0) {
                        Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                        return false;
                    }
                    if (z) {
                        Log.d(Constants.TAG, "Codec resource is not enough");
                        return false;
                    }
                    if (i3 > 0) {
                        return true;
                    }
                    Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                    return false;
                } catch (IllegalArgumentException e10) {
                    e = e10;
                    integer = 0;
                    i3 = 0;
                    e.printStackTrace();
                    if (mediaExtractor != null) {
                        mediaExtractor.release();
                    }
                    if (andSelectVideoTrackIndex != 0) {
                        if (z) {
                            Log.d(Constants.TAG, "Codec resource is not enough");
                            return false;
                        }
                        if (i3 > 0) {
                            return true;
                        }
                        Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                        return false;
                    }
                    Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                    return false;
                }
                if (mediaExtractor != null) {
                    mediaExtractor.release();
                }
                if (andSelectVideoTrackIndex != 0) {
                    Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                    return false;
                } else if (z && GetCodecResource >= 0 && GetCodecResource < i3 * integer) {
                    Log.d(Constants.TAG, "Codec resource is not enough");
                    return false;
                } else if (i3 > 0 && integer > 0) {
                    return true;
                } else {
                    Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                    return false;
                }
            }
            Log.d(Constants.TAG, "Can't create Extractor");
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            return false;
        } catch (IOException e11) {
            e = e11;
            andSelectVideoTrackIndex = 0;
            integer = 0;
            i3 = 0;
            e.printStackTrace();
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            if (andSelectVideoTrackIndex != 0) {
                Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                return false;
            }
            if (z) {
                Log.d(Constants.TAG, "Codec resource is not enough");
                return false;
            }
            if (i3 > 0) {
                return true;
            }
            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
            return false;
        } catch (NullPointerException e12) {
            e = e12;
            andSelectVideoTrackIndex = 0;
            integer = 0;
            i3 = 0;
            e.printStackTrace();
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            if (andSelectVideoTrackIndex != 0) {
                if (z) {
                    Log.d(Constants.TAG, "Codec resource is not enough");
                    return false;
                }
                if (i3 > 0) {
                    return true;
                }
                Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
                return false;
            }
            Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
            return false;
        } catch (IllegalArgumentException e13) {
            e = e13;
            andSelectVideoTrackIndex = 0;
            integer = 0;
            i3 = 0;
            e.printStackTrace();
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            if (andSelectVideoTrackIndex != 0) {
                Log.d(Constants.TAG, "Extactor Error appear : " + andSelectVideoTrackIndex);
                return false;
            }
            if (z) {
                Log.d(Constants.TAG, "Codec resource is not enough");
                return false;
            }
            if (i3 > 0) {
                return true;
            }
            Log.d(Constants.TAG, "Resolution Error appear : width = " + i3 + ", height= " + integer);
            return false;
        }
    }

    private boolean CheckVideoFormat(String str) {
        MediaExtractor createExtractor;
        Throwable e;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        boolean z = true;
        try {
            createExtractor = CodecsHelper.createExtractor(str);
            try {
                mediaMetadataRetriever = CodecsHelper.createMediaMetadataRetriever(str);
                if (CodecsHelper.getAndSelectVideoTrackIndex(createExtractor) == -1 || !CodecsHelper.isSupportedFormat(mediaMetadataRetriever)) {
                    Log.d(Constants.TAG, "Video Format is not supported");
                    z = false;
                }
                if (createExtractor != null) {
                    createExtractor.release();
                }
                if (mediaMetadataRetriever == null) {
                    return z;
                }
                mediaMetadataRetriever.release();
                return z;
            } catch (IOException e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    if (createExtractor != null) {
                        createExtractor.release();
                    }
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                    return false;
                } catch (Throwable th) {
                    e = th;
                    if (createExtractor != null) {
                        createExtractor.release();
                    }
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                    throw e;
                }
            }
        } catch (IOException e3) {
            e = e3;
            createExtractor = mediaMetadataRetriever;
            e.printStackTrace();
            if (createExtractor != null) {
                createExtractor.release();
            }
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            return false;
        } catch (Throwable th2) {
            e = th2;
            createExtractor = mediaMetadataRetriever;
            if (createExtractor != null) {
                createExtractor.release();
            }
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            throw e;
        }
    }

    public static int checkRewritable(String str) {
        MediaExtractor mediaExtractor = null;
        if (str == null) {
            Log.e(Constants.TAG, "Invalid file path: " + str);
            return -1;
        } else if (!CodecsHelper.isSupportedFormat(str)) {
            return 0;
        } else {
            int i;
            int i2;
            try {
                mediaExtractor = CodecsHelper.createExtractor(str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Debugger.log("Number of tracks: " + mediaExtractor.getTrackCount());
            int andSelectVideoTrackIndex = CodecsHelper.getAndSelectVideoTrackIndex(mediaExtractor);
            if (andSelectVideoTrackIndex != -1) {
                String string = mediaExtractor.getTrackFormat(andSelectVideoTrackIndex).getString("mime");
                if (string.contains(CodecsMime.VIDEO_CODEC_H264) || string.contains(ContentType.VIDEO_MP4V_ES) || string.contains("video/3gpp") || string.contains("video/hevc")) {
                    andSelectVideoTrackIndex = 1;
                    i = 1;
                } else {
                    Log.d(Constants.TAG, "Unsupported mime type: video");
                    andSelectVideoTrackIndex = 1;
                    i = 0;
                }
            } else {
                Log.d(Constants.TAG, "Valid video track absent");
                andSelectVideoTrackIndex = 0;
                i = 0;
            }
            int andSelectAudioTrackIndex = CodecsHelper.getAndSelectAudioTrackIndex(mediaExtractor);
            if (andSelectAudioTrackIndex != -1) {
                String string2 = mediaExtractor.getTrackFormat(andSelectAudioTrackIndex).getString("mime");
                if (string2.contains(CodecsMime.AUDIO_CODEC_AAC) || string2.contains(CodecsMime.AUDIO_CODEC_AMR) || string2.contains("audio/amr-wb")) {
                    i2 = 1;
                    andSelectAudioTrackIndex = 1;
                } else {
                    Log.d(Constants.TAG, "Unsuppported mime type: audio");
                    i2 = 1;
                    andSelectAudioTrackIndex = 0;
                }
            } else {
                Log.d(Constants.TAG, "Valid audio track absent");
                i2 = 0;
                andSelectAudioTrackIndex = 0;
            }
            return andSelectVideoTrackIndex == 0 ? -1 : i != 0 ? i2 == 0 ? 1 : andSelectAudioTrackIndex != 0 ? 1 : 0 : 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean findAtom(java.lang.String r14, java.lang.String r15) throws java.io.IOException {
        /*
        r3 = 0;
        r0 = new java.io.File;
        r0.<init>(r14);
        r1 = 4;
        r5 = new byte[r1];
        r1 = 4;
        r6 = new byte[r1];
        r8 = r0.length();
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "file size: ";
        r1 = r1.append(r2);
        r1 = r1.append(r8);
        r1 = r1.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r1);
        r1 = 5;
        r7 = new java.lang.String[r1];
        r1 = 0;
        r2 = "mdia";
        r7[r1] = r2;
        r1 = 1;
        r2 = "minf";
        r7[r1] = r2;
        r1 = 2;
        r2 = "moov";
        r7[r1] = r2;
        r1 = 3;
        r2 = "stbl";
        r7[r1] = r2;
        r1 = 4;
        r2 = "trak";
        r7[r1] = r2;
        r1 = 0;
        r4 = new java.io.RandomAccessFile;	 Catch:{ all -> 0x0180 }
        r2 = "r";
        r4.<init>(r0, r2);	 Catch:{ all -> 0x0180 }
        r0 = 0;
    L_0x0053:
        r2 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r2 < 0) goto L_0x00ff;
    L_0x0057:
        r2 = 1;
    L_0x0058:
        if (r2 != 0) goto L_0x0102;
    L_0x005a:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0107 }
        r2.<init>();	 Catch:{ IOException -> 0x0107 }
        r10 = "filePointer: ";
        r2 = r2.append(r10);	 Catch:{ IOException -> 0x0107 }
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0107 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0107 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ IOException -> 0x0107 }
        r4.seek(r0);	 Catch:{ IOException -> 0x0107 }
    L_0x0074:
        r2 = 0;
        r10 = r5.length;	 Catch:{ all -> 0x00f9 }
        r4.read(r5, r2, r10);	 Catch:{ all -> 0x00f9 }
        r10 = unsignedIntToLong(r5);	 Catch:{ all -> 0x00f9 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r2.<init>();	 Catch:{ all -> 0x00f9 }
        r12 = "Atom Size: ";
        r2 = r2.append(r12);	 Catch:{ all -> 0x00f9 }
        r2 = r2.append(r10);	 Catch:{ all -> 0x00f9 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ all -> 0x00f9 }
        r2 = 0;
        r12 = r6.length;	 Catch:{ all -> 0x00f9 }
        r4.read(r6, r2, r12);	 Catch:{ all -> 0x00f9 }
        r2 = new java.lang.String;	 Catch:{ all -> 0x00f9 }
        r2.<init>(r6);	 Catch:{ all -> 0x00f9 }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r12.<init>();	 Catch:{ all -> 0x00f9 }
        r13 = "Atom Box: ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x00f9 }
        r12 = r12.append(r2);	 Catch:{ all -> 0x00f9 }
        r12 = r12.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r12);	 Catch:{ all -> 0x00f9 }
        r12 = java.util.Arrays.binarySearch(r7, r2);	 Catch:{ all -> 0x00f9 }
        if (r12 >= 0) goto L_0x010d;
    L_0x00bb:
        r2 = r2.equals(r15);	 Catch:{ all -> 0x00f9 }
        if (r2 != 0) goto L_0x0134;
    L_0x00c1:
        r12 = 1;
        r2 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r2 != 0) goto L_0x014d;
    L_0x00c7:
        r10 = 8;
        r10 = r10 + r0;
        r4.seek(r10);	 Catch:{ all -> 0x00f9 }
        r2 = 8;
        r2 = new byte[r2];	 Catch:{ all -> 0x00f9 }
        r10 = 0;
        r11 = r2.length;	 Catch:{ all -> 0x00f9 }
        r4.read(r2, r10, r11);	 Catch:{ all -> 0x00f9 }
        r10 = new java.math.BigInteger;	 Catch:{ all -> 0x00f9 }
        r10.<init>(r2);	 Catch:{ all -> 0x00f9 }
        r10 = r10.longValue();	 Catch:{ all -> 0x00f9 }
        r0 = r0 + r10;
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r2.<init>();	 Catch:{ all -> 0x00f9 }
        r12 = "64bit: ";
        r2 = r2.append(r12);	 Catch:{ all -> 0x00f9 }
        r2 = r2.append(r10);	 Catch:{ all -> 0x00f9 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ all -> 0x00f9 }
        goto L_0x0053;
    L_0x00f9:
        r0 = move-exception;
        r1 = r4;
    L_0x00fb:
        r1.close();
        throw r0;
    L_0x00ff:
        r2 = 0;
        goto L_0x0058;
    L_0x0102:
        r0 = r3;
    L_0x0103:
        r4.close();
        return r0;
    L_0x0107:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00f9 }
        goto L_0x0074;
    L_0x010d:
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r10.<init>();	 Catch:{ all -> 0x00f9 }
        r11 = "Found parent: ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x00f9 }
        r2 = r10.append(r2);	 Catch:{ all -> 0x00f9 }
        r10 = " move to position: ";
        r2 = r2.append(r10);	 Catch:{ all -> 0x00f9 }
        r2 = r2.append(r12);	 Catch:{ all -> 0x00f9 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ all -> 0x00f9 }
        r10 = 8;
        r0 = r0 + r10;
        goto L_0x0053;
    L_0x0134:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r0.<init>();	 Catch:{ all -> 0x00f9 }
        r1 = "Found: ";
        r0 = r0.append(r1);	 Catch:{ all -> 0x00f9 }
        r0 = r0.append(r15);	 Catch:{ all -> 0x00f9 }
        r0 = r0.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r0);	 Catch:{ all -> 0x00f9 }
        r0 = 1;
        goto L_0x0103;
    L_0x014d:
        r12 = 0;
        r2 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r2 != 0) goto L_0x015b;
    L_0x0153:
        r0 = "filePointer does not go forward. Exit.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r0);	 Catch:{ all -> 0x00f9 }
        r0 = 0;
        goto L_0x0103;
    L_0x015b:
        r0 = r0 + r10;
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f9 }
        r2.<init>();	 Catch:{ all -> 0x00f9 }
        r12 = "move: ";
        r2 = r2.append(r12);	 Catch:{ all -> 0x00f9 }
        r2 = r2.append(r0);	 Catch:{ all -> 0x00f9 }
        r12 = " atomsize ";
        r2 = r2.append(r12);	 Catch:{ all -> 0x00f9 }
        r2 = r2.append(r10);	 Catch:{ all -> 0x00f9 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f9 }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ all -> 0x00f9 }
        goto L_0x0053;
    L_0x0180:
        r0 = move-exception;
        goto L_0x00fb;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.transcode.core.EncodeVideo.findAtom(java.lang.String, java.lang.String):boolean");
    }

    public static int getMaxEncodingDuration(int i, int i2, int i3, int i4) {
        long j = (long) ((int) (((float) i) * 0.7f));
        int i5 = (i2 * i3) / 256;
        i5 = i5 >= 100 ? (i5 > 100 && i5 < 1000) ? 150 : i5 <= 1000 ? 0 : BitRate.MIN_VIDEO_VGA_BITRATE : 64;
        long j2 = i4 != 1 ? ((j * 8) * 1000) / ((long) (i5 + 64)) : ((j * 8) * 1000) / ((long) (i5 + 8));
        Log.d(Constants.TAG, "Size " + i + " width " + i2 + " height " + i3 + " audiocodec " + i4 + " maxdur " + j2);
        i5 = (int) j2;
        return i5 >= 1000 ? i5 : 0;
    }

    private int getVideoSampleSize(MediaFormat mediaFormat) {
        return !mediaFormat.getString("mime").startsWith("video/") ? 0 : mediaFormat.getInteger("width") * mediaFormat.getInteger("height");
    }

    private static boolean isRecognizedFormat(int i) {
        switch (i) {
            case 19:
            case 20:
            case 21:
            case 39:
            case 2130706688:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSupportedFormat(String str) {
        return CodecsHelper.isSupportedFormat(str);
    }

    private static int parseAvcProfile(byte[] bArr) {
        switch (bArr[5] & 255) {
            case SemMotionRecognitionEvent.BLOW /*66*/:
                return 1;
            case SemMotionRecognitionEvent.CALLPOSE_R /*77*/:
                return 2;
            case SemMotionRecognitionEvent.TILT_UP_LEVEL_2_LAND /*88*/:
                return 4;
            case 100:
                return 8;
            case 110:
                return 16;
            case FingerprintEvent.STATUS_IDENTIFY_FAILURE_DATABASE_FAILURE /*122*/:
                return 32;
            case 244:
                return 64;
            default:
                return -1;
        }
    }

    private static int selectColorFormat(MediaCodecInfo mediaCodecInfo, String str) {
        CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(str);
        for (int i : capabilitiesForType.colorFormats) {
            if (isRecognizedFormat(i)) {
                Debugger.log("colorFormat: " + i);
                return i;
            }
        }
        Log.e(Constants.TAG, "couldn't find a good color format for " + mediaCodecInfo.getName() + " / " + str);
        return 0;
    }

    private static final long unsignedIntToLong(byte[] bArr) {
        return ((((((((long) (bArr[0] & 255)) | 0) << 8) | ((long) (bArr[1] & 255))) << 8) | ((long) (bArr[2] & 255))) << 8) | ((long) (bArr[3] & 255));
    }

    public int checkTransRewritable(MediaFormat mediaFormat, int i, String str) {
        SpsPps spsPps = null;
        int i2 = 0;
        int integer = mediaFormat.getInteger("width");
        int integer2 = mediaFormat.getInteger("height");
        int GetCodecResource = CodecsHelper.GetCodecResource(integer, integer2);
        if (GetCodecResource >= 0 && GetCodecResource < integer * integer2) {
            Debugger.log("Overshoot capability");
            return 1;
        }
        try {
            if (findAtom(str, "ctts")) {
                Debugger.log("ctts detected");
                return 0;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (CodecsMime.VIDEO_CODEC_H264.equals(mediaFormat.getString("mime"))) {
            if (mediaFormat.containsKey("csd-0")) {
                Buffer byteBuffer = mediaFormat.getByteBuffer("csd-0");
                byte[] bArr = new byte[byteBuffer.capacity()];
                byte[] bArr2 = new byte[(byteBuffer.capacity() - 4)];
                byteBuffer.get(bArr);
                byteBuffer.position(4);
                byteBuffer.get(bArr2, 0, bArr2.length);
                byteBuffer.position(0);
                this.mOutputVideoProfile = parseAvcProfile(bArr);
                Debugger.log("Profile: " + this.mOutputVideoProfile);
                try {
                    spsPps = getLocalEncHeader(i);
                    if (spsPps == null) {
                        Debugger.log("localSPS is null");
                        return 0;
                    }
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
                String str2 = "";
                for (byte toHexString : bArr2) {
                    str2 = str2 + "0x" + Integer.toHexString(toHexString) + " ";
                }
                Debugger.log("TargetSPS: " + str2);
                integer = 0;
                while (integer < bArr2.length) {
                    try {
                        Debugger.log("i: " + integer + " " + bArr2[integer] + " " + spsPps._sps[integer]);
                        if (bArr2[integer] != spsPps._sps[integer]) {
                            Debugger.log("SPS does not match");
                            integer = 1;
                            break;
                        }
                        integer++;
                    } catch (Throwable e22) {
                        e22.printStackTrace();
                        return 0;
                    }
                }
                integer = 2;
                if (mediaFormat.containsKey("csd-1")) {
                    Buffer byteBuffer2 = mediaFormat.getByteBuffer("csd-1");
                    bArr2 = new byte[(byteBuffer2.capacity() - 4)];
                    byteBuffer2.position(4);
                    byteBuffer2.get(bArr2, 0, bArr2.length);
                    byteBuffer2.position(0);
                    str2 = "";
                    for (byte toHexString2 : bArr2) {
                        str2 = str2 + "0x" + Integer.toHexString(toHexString2) + " ";
                    }
                    Debugger.log("TargetPPS: " + str2);
                    int i3 = 0;
                    while (i3 < bArr2.length) {
                        try {
                            Debugger.log("i: " + i3 + " " + bArr2[i3] + " " + spsPps._pps[i3]);
                            if (bArr2[i3] != spsPps._pps[i3]) {
                                Debugger.log("PPS does not match");
                                integer = 1;
                                break;
                            }
                            i3++;
                        } catch (Throwable e222) {
                            e222.printStackTrace();
                            return 0;
                        }
                    }
                }
                i2 = integer;
            } else {
                Debugger.log("Unable to detect csd-0 or csd-1");
            }
        } else if (ContentType.VIDEO_MP4V_ES.equals(mediaFormat.getString("mime"))) {
            Debugger.log("Mpeg4: Rewrite");
            i2 = 1;
        } else if ("video/3gpp".equals(mediaFormat.getString("mime"))) {
            Debugger.log("3gp: Rewrite");
            i2 = 1;
        } else {
            Log.e(Constants.TAG, "Unable to detect csd-0: " + mediaFormat.getString("mime"));
        }
        return i2;
    }

    public SpsPps getLocalEncHeader(int i) throws Exception {
        MediaCodec createEncoderByType;
        Buffer allocate = ByteBuffer.allocate(((this.mOutputWidth * this.mOutputHeight) * 3) / 2);
        try {
            createEncoderByType = MediaCodec.createEncoderByType(this.mOutputVideoMimeType);
        } catch (Throwable e) {
            Log.e(Constants.TAG, "codec cannot be created");
            e.printStackTrace();
            createEncoderByType = null;
        } catch (Throwable e2) {
            Log.e(Constants.TAG, "type is not a valid mime type: " + this.mOutputVideoMimeType);
            e2.printStackTrace();
            createEncoderByType = null;
        } catch (Throwable e22) {
            Log.e(Constants.TAG, "type is null");
            e22.printStackTrace();
            createEncoderByType = null;
        }
        MediaCodec createEncoderByType2 = MediaCodec.createEncoderByType(this.mOutputVideoMimeType);
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mOutputVideoMimeType, this.mOutputWidth, this.mOutputHeight);
        createVideoFormat.setInteger("bitrate", this.mOutputVideoBitRate);
        createVideoFormat.setInteger("frame-rate", i);
        createVideoFormat.setInteger("color-format", selectColorFormat(createEncoderByType2.getCodecInfo(), this.mOutputVideoMimeType));
        createVideoFormat.setInteger("i-frame-interval", this.mOutputVideoIFrameInterval);
        createVideoFormat.setInteger("profile", this.mOutputVideoProfile);
        Debugger.log("Local video format " + createVideoFormat);
        createEncoderByType.configure(createVideoFormat, null, null, 1);
        createEncoderByType.start();
        try {
            ByteBuffer[] inputBuffers = createEncoderByType.getInputBuffers();
            ByteBuffer[] outputBuffers = createEncoderByType.getOutputBuffers();
            int dequeueInputBuffer = createEncoderByType.dequeueInputBuffer(-1);
            Debugger.log("inputBufferIndex A: " + dequeueInputBuffer);
            if (dequeueInputBuffer >= 0) {
                Buffer buffer = inputBuffers[dequeueInputBuffer];
                buffer.clear();
                buffer.put(allocate);
                createEncoderByType.queueInputBuffer(dequeueInputBuffer, 0, allocate.capacity(), 0, 0);
                Debugger.log("inputBufferIndex G4: " + dequeueInputBuffer);
                dequeueInputBuffer = createEncoderByType.dequeueInputBuffer(-1);
                inputBuffers[dequeueInputBuffer].put(allocate);
                createEncoderByType.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                Debugger.log("inputBufferIndex G5: " + dequeueInputBuffer);
            }
            Thread.sleep(100);
            BufferInfo bufferInfo = new BufferInfo();
            dequeueInputBuffer = createEncoderByType.dequeueOutputBuffer(bufferInfo, 0);
            Debugger.log("outputBufferIndex: " + dequeueInputBuffer);
            int dequeueOutputBuffer = dequeueInputBuffer != -2 ? dequeueInputBuffer : createEncoderByType.dequeueOutputBuffer(bufferInfo, 0);
            Debugger.log("outputBufferIndex 4: " + dequeueOutputBuffer);
            if (dequeueOutputBuffer < 0) {
                return null;
            }
            ByteBuffer byteBuffer = outputBuffers[dequeueOutputBuffer];
            Object obj = new byte[bufferInfo.size];
            Debugger.log("bufferInfo.size: " + bufferInfo.size);
            byteBuffer.get(obj);
            Buffer wrap = ByteBuffer.wrap(obj);
            if (wrap.getInt() != 1) {
                Debugger.log("Invalid start pattern");
            } else {
                Debugger.log("start pattern match");
            }
            while (true) {
                if (wrap.get() == (byte) 0 && wrap.get() == (byte) 0 && wrap.get() == (byte) 0 && wrap.get() == (byte) 1) {
                    break;
                }
            }
            dequeueInputBuffer = wrap.position();
            Debugger.log("ppsIndex: " + dequeueInputBuffer);
            Object obj2 = new byte[(dequeueInputBuffer - 8)];
            System.arraycopy(obj, 4, obj2, 0, obj2.length);
            Object obj3 = new byte[(obj.length - dequeueInputBuffer)];
            System.arraycopy(obj, dequeueInputBuffer, obj3, 0, obj3.length);
            SpsPps spsPps = new SpsPps();
            spsPps._sps = Arrays.copyOf(obj2, obj2.length);
            spsPps._pps = Arrays.copyOf(obj3, obj3.length);
            String str = "";
            for (byte toHexString : obj2) {
                str = str + "0x" + Integer.toHexString(toHexString) + " ";
            }
            Debugger.log("Local SPS: " + str);
            str = "";
            for (byte toHexString2 : obj3) {
                str = str + "0x" + Integer.toHexString(toHexString2) + " ";
            }
            Debugger.log("Local PPS: " + str);
            createEncoderByType.releaseOutputBuffer(dequeueOutputBuffer, false);
            createEncoderByType.dequeueOutputBuffer(bufferInfo, 0);
            createEncoderByType.stop();
            createEncoderByType.release();
            return spsPps;
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            createEncoderByType.stop();
            createEncoderByType.release();
        }
    }

    public int getOutputFileSize() {
        try {
            int videoEncodingBitRate;
            MediaExtractor createExtractor = CodecsHelper.createExtractor(this.mInputFilePath);
            MediaFormat trackFormat = createExtractor.getTrackFormat(CodecsHelper.getAndSelectVideoTrackIndex(createExtractor));
            long j = this.mTrimVideoEndUs;
            long j2 = j == 0 ? trackFormat.getLong("durationUs") : j;
            createExtractor.release();
            if ((this.mOutputMaxSizeKB < 0 ? 1 : 0) == 0) {
                if (this.mOutputVideoMimeType == CodecsMime.VIDEO_CODEC_H264) {
                    this.mSizeFraction = 0.9f;
                }
                videoEncodingBitRate = CodecsHelper.getVideoEncodingBitRate(this.mSizeFraction, this.mOutputMaxSizeKB, (j2 - this.mTrimVideoStartUs) / 1000, this.mOutputAudioBitRate / 1000, this.mOutputWidth, this.mOutputHeight) * 1000;
            } else {
                videoEncodingBitRate = suggestBitRate(this.mOutputWidth, this.mOutputHeight) * 1000;
            }
            videoEncodingBitRate = (int) ((((double) (videoEncodingBitRate + this.mOutputAudioBitRate)) / 1000.0d) * (((double) (j2 - this.mTrimVideoStartUs)) / 8000000.0d));
            return this.mOutputMaxSizeKB == 0 ? (int) (((double) videoEncodingBitRate) * 0.9d) : videoEncodingBitRate;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        } catch (NullPointerException e2) {
            Log.d(Constants.TAG, "NullPointerException appear");
            return -1;
        }
    }

    public void initialize(String str, int i, int i2, String str2) throws IOException {
        if (i <= 0) {
            throw new IllegalArgumentException("width cannot be equal to or less than 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("height cannot be equal to or less than 0");
        } else if (str == null) {
            throw new IllegalArgumentException("output file path cannot be null");
        } else if (str2 == null) {
            throw new IllegalArgumentException("input file path cannot be null");
        } else if (!CheckVideoFormat(str2)) {
            throw new IOException("Not a valid video format.");
        } else if (CheckVideoCodec(i, i2, str2, false)) {
            this.mOutputFilePath = str;
            this.mOutputWidth = i;
            this.mOutputHeight = i2;
            this.mInputFilePath = str2;
        } else {
            throw new IOException("Not a valid video codec.");
        }
    }

    public void initialize(String str, int i, int i2, String str2, boolean z) throws IOException {
        if (i <= 0) {
            throw new IllegalArgumentException("width cannot be equal to or less than 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("height cannot be equal to or less than 0");
        } else if (str == null) {
            throw new IllegalArgumentException("output file path cannot be null");
        } else if (str2 == null) {
            throw new IllegalArgumentException("input file path cannot be null");
        } else if (!CheckVideoFormat(str2)) {
            throw new IOException("Not a valid video format.");
        } else if (CheckVideoCodec(i, i2, str2, z)) {
            this.mOutputFilePath = str;
            this.mOutputWidth = i;
            this.mOutputHeight = i2;
            this.mInputFilePath = str2;
        } else {
            throw new IOException("Not a valid video codec.");
        }
    }

    public void initialize(String str, int i, String str2) throws IOException {
        if (!EncodeResolution.isSupportedResolution(i)) {
            throw new IllegalArgumentException("Invalid resolution value.");
        } else if (str == null) {
            throw new IllegalArgumentException("Output file path cannot be null");
        } else if (str2 != null) {
            Rect rect = new Rect();
            CodecsHelper.fillResolutionRect(i, rect);
            initialize(str, rect.width(), rect.height(), str2);
        } else {
            throw new IllegalArgumentException("Input file path cannot be null");
        }
    }

    protected void prepare() throws IOException {
        this.mEncoding = true;
        prepareVideoCodec();
        prepareAudioCodec();
    }

    protected void prepareAudioCodec() throws IOException {
        if (this.mInputFilePath != null) {
            this.mAudioExtractor = CodecsHelper.createExtractor(this.mInputFilePath);
            int andSelectAudioTrackIndex = CodecsHelper.getAndSelectAudioTrackIndex(this.mAudioExtractor);
            if (andSelectAudioTrackIndex != -1) {
                MediaFormat trackFormat = this.mAudioExtractor.getTrackFormat(andSelectAudioTrackIndex);
                if ("audio/unknown".equals(trackFormat.getString("mime"))) {
                    Log.d(Constants.TAG, "Audio mime type is unknown. Ignore audio track.");
                    this.mCopyAudio = false;
                    return;
                }
                if (trackFormat.containsKey(KEY_ERROR_TYPE)) {
                    andSelectAudioTrackIndex = trackFormat.getInteger(KEY_ERROR_TYPE);
                    if (andSelectAudioTrackIndex != 0) {
                        Log.d(Constants.TAG, "Audio codec error appear : " + andSelectAudioTrackIndex);
                        this.mCopyAudio = false;
                        return;
                    }
                }
                this.mCopyAudio = true;
                if (this.mTrimAudioEndUs == 0) {
                    this.mTrimAudioEndUs = trackFormat.getLong("durationUs");
                    Log.d(Constants.TAG, "mTrimAudioEndUs was 0 but updated");
                }
                Log.d(Constants.TAG, "Audio input format " + trackFormat);
                this.mOutputAudioSampleRateHZ = trackFormat.getInteger("sample-rate");
                this.mOutputAudioChannelCount = trackFormat.getInteger("channel-count");
                String string = trackFormat.getString("mime");
                if (CodecsMime.AUDIO_CODEC_AAC.equals(string)) {
                    try {
                        this.mInputAudioDecoder = CodecsHelper.createAudioDecoder(CodecsHelper.getDecoderCodec(string), trackFormat);
                        if (this.mCopyAudio) {
                            this.mInputAudioDecoder.getOutputBuffers();
                        }
                        ByteBuffer[] inputBuffers = !this.mCopyAudio ? null : this.mInputAudioDecoder.getInputBuffers();
                        BufferInfo bufferInfo = new BufferInfo();
                        Runnable c02601 = new C02601();
                        int i = -1;
                        while (!this.formatupdated) {
                            if (!this.formatupdated) {
                                int dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    int readSampleData = this.mAudioExtractor.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                                    long sampleTime = this.mAudioExtractor.getSampleTime();
                                    if (readSampleData > 0) {
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                    } else if (readSampleData == -1) {
                                        this.mCopyAudio = false;
                                        this.formatupdated = true;
                                        Log.d(Constants.TAG, "Audio buffer is empty, size :" + readSampleData);
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio decoder input try again later while preparing audio codec");
                                }
                            }
                            CodecsHelper.scheduleAfter(3, c02601);
                            if (!this.formatupdated && r8 == -1) {
                                andSelectAudioTrackIndex = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                                if (andSelectAudioTrackIndex == -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later while preparing audio codec");
                                } else if (andSelectAudioTrackIndex == -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed ");
                                    this.mInputAudioDecoder.getOutputBuffers();
                                } else if (andSelectAudioTrackIndex == -2) {
                                    this.mOutputAudioSampleRateHZ = this.mInputAudioDecoder.getOutputFormat().getInteger("sample-rate");
                                    this.mOutputAudioChannelCount = this.mInputAudioDecoder.getOutputFormat().getInteger("channel-count");
                                    Log.d(Constants.TAG, "audio decoder: output format changed: SampleRate" + this.mOutputAudioSampleRateHZ + ",ChannelCount" + this.mOutputAudioChannelCount);
                                    this.formatupdated = true;
                                } else if (andSelectAudioTrackIndex < 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo.flags & 2) == 0) {
                                    i = andSelectAudioTrackIndex;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(andSelectAudioTrackIndex, false);
                                }
                            }
                        }
                        if (this.mInputAudioDecoder != null) {
                            try {
                                this.mInputAudioDecoder.stop();
                                this.mInputAudioDecoder.release();
                                this.mInputAudioDecoder = null;
                            } catch (Throwable e) {
                                Log.d(Constants.TAG, "Exception in releasing input audio decoder.");
                                e.printStackTrace();
                            }
                        }
                        if (this.mCopyAudio) {
                            this.mAudioExtractor.seekTo(0, 0);
                        }
                    } catch (Throwable e2) {
                        e2.printStackTrace();
                    } catch (Throwable e22) {
                        e22.printStackTrace();
                    }
                }
                andSelectAudioTrackIndex = 0;
                try {
                    andSelectAudioTrackIndex = trackFormat.getInteger("max-input-size");
                } catch (NullPointerException e3) {
                    Log.d(Constants.TAG, "Audio max input size not defined");
                }
                if (this.mMMSMode && this.mOutputAudioChannelCount >= 2) {
                    this.mOriginalAudioChannelCount = this.mOutputAudioChannelCount;
                    this.mOutputAudioChannelCount = 1;
                }
                MediaFormat createAudioFormat = MediaFormat.createAudioFormat(this.mOutputAudioMimeType, this.mOutputAudioSampleRateHZ, this.mOutputAudioChannelCount);
                if (andSelectAudioTrackIndex != 0) {
                    createAudioFormat.setInteger("max-input-size", andSelectAudioTrackIndex);
                }
                createAudioFormat.setInteger("bitrate", this.mOutputAudioBitRate);
                createAudioFormat.setInteger("aac-profile", this.mOutputAudioAACProfile);
                Log.d(Constants.TAG, "Audio output format " + createAudioFormat);
                this.mOutputAudioEncoder = CodecsHelper.createAudioEncoder(CodecsHelper.getEncoderCodec(this.mOutputAudioMimeType), createAudioFormat);
                this.mInputAudioDecoder = CodecsHelper.createAudioDecoder(CodecsHelper.getDecoderCodec(string), trackFormat);
                return;
            }
            this.mCopyAudio = false;
            return;
        }
        throw new IOException("mInputFilePath is NULL");
    }

    protected void prepareVideoCodec() throws IOException {
        if (this.mInputFilePath != null) {
            int parseInt;
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(this.mInputFilePath);
                String extractMetadata = mediaMetadataRetriever.extractMetadata(24);
                if (extractMetadata != null) {
                    try {
                        parseInt = Integer.parseInt(extractMetadata);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        parseInt = 0;
                    }
                    switch (parseInt) {
                        case 0:
                            this.mInputOrientationDegrees = 0;
                            break;
                        case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1_LAND /*90*/:
                            this.mInputOrientationDegrees = 90;
                            break;
                        case 180:
                            this.mInputOrientationDegrees = 180;
                            break;
                        case 270:
                            this.mInputOrientationDegrees = 270;
                            break;
                        default:
                            break;
                    }
                }
                this.mInputOrientationDegrees = 0;
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            } catch (Throwable e2) {
                e2.printStackTrace();
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            } catch (Throwable th) {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            }
            this.mVideoExtractor = CodecsHelper.createExtractor(this.mInputFilePath);
            MediaFormat trackFormat = this.mVideoExtractor.getTrackFormat(CodecsHelper.getAndSelectVideoTrackIndex(this.mVideoExtractor));
            Log.d(Constants.TAG, "input video format: " + trackFormat);
            if (this.mTrimVideoEndUs == 0) {
                this.mTrimVideoEndUs = trackFormat.getLong("durationUs");
                Log.d(Constants.TAG, "mTrimVideoEndUs was 0 but updated");
            }
            if (this.mOutputMaxSizeKB < 0) {
                this.mOutputVideoBitRate = suggestBitRate(this.mOutputWidth, this.mOutputHeight) * 1000;
            } else {
                if (!this.m2ndTimeEncoding && this.mOutputVideoMimeType == CodecsMime.VIDEO_CODEC_H264) {
                    this.mSizeFraction = 0.9f;
                }
                if (this.mMMSMode) {
                    this.mOutputAudioBitRate = 32000;
                }
                this.mOutputVideoBitRate = CodecsHelper.getVideoEncodingBitRate(this.mSizeFraction, this.mOutputMaxSizeKB, (this.mTrimVideoEndUs - this.mTrimVideoStartUs) / 1000, this.mOutputAudioBitRate / 1000, this.mOutputWidth, this.mOutputHeight) * 1000;
            }
            try {
                parseInt = trackFormat.getInteger("frame-rate");
            } catch (Exception e3) {
                parseInt = 0;
            }
            if (this.mOutputVideoMimeType == CodecsMime.VIDEO_CODEC_H264 && parseInt > 0) {
                this.mOutputVideoFrameRate = parseInt;
            }
            if (this.mMMSMode) {
                this.mOutputVideoFrameRate /= 2;
            }
            if (parseInt > this.mOutputVideoFrameRate) {
                this.mSkipFrames = true;
                this.mFramesSkipInterval = (int) Math.ceil((double) (((float) parseInt) / ((float) this.mOutputVideoFrameRate)));
            }
            Log.d(Constants.TAG, "mOutputVideoFrameRate: " + this.mOutputVideoFrameRate);
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mOutputVideoMimeType, this.mOutputWidth, this.mOutputHeight);
            createVideoFormat.setInteger("color-format", 2130708361);
            createVideoFormat.setInteger("bitrate", this.mOutputVideoBitRate);
            createVideoFormat.setInteger("frame-rate", this.mOutputVideoFrameRate);
            createVideoFormat.setInteger("i-frame-interval", this.mOutputVideoIFrameInterval);
            Log.d(Constants.TAG, "output video format " + createVideoFormat);
            this.mOutputVideoEncoder = MediaCodec.createEncoderByType(this.mOutputVideoMimeType);
            this.mOutputVideoEncoder.configure(createVideoFormat, null, null, 1);
            this.mInputSurface = new InputSurface(this.mOutputVideoEncoder.createInputSurface());
            this.mOutputVideoEncoder.start();
            this.mInputSurface.makeCurrent();
            this.mOutputSurface = new OutputSurface(this.mInputOrientationDegrees);
            if (this.mLogoPresent) {
                this.mLogoRenderer = new RenderTexture_GL_2d();
                this.mLogoRenderer.prepare();
            }
            this.mInputVideoDecoder = CodecsHelper.createVideoDecoder(trackFormat, this.mOutputSurface.getSurface());
            if (this.mInputVideoDecoder == null) {
                throw new IOException("can't set VideoDecoder");
            }
            return;
        }
        throw new IOException("mInputFilePath is NULL");
    }

    protected void prepareVideoCodecNeo() throws IOException {
        int parseInt;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(this.mInputFilePath);
            String extractMetadata = mediaMetadataRetriever.extractMetadata(24);
            this.mOutputVideoBitRate = Integer.parseInt(mediaMetadataRetriever.extractMetadata(20));
            if (extractMetadata != null) {
                try {
                    parseInt = Integer.parseInt(extractMetadata);
                } catch (Throwable e) {
                    e.printStackTrace();
                    parseInt = 0;
                }
                switch (parseInt) {
                    case 0:
                        this.mInputOrientationDegrees = 0;
                        break;
                    case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1_LAND /*90*/:
                        this.mInputOrientationDegrees = 90;
                        break;
                    case 180:
                        this.mInputOrientationDegrees = 180;
                        break;
                    case 270:
                        this.mInputOrientationDegrees = 270;
                        break;
                    default:
                        break;
                }
            }
            this.mInputOrientationDegrees = 0;
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        } catch (Throwable e2) {
            e2.printStackTrace();
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        } catch (Throwable th) {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        this.mVideoExtractor = CodecsHelper.createExtractor(this.mInputFilePath);
        MediaFormat trackFormat = this.mVideoExtractor.getTrackFormat(CodecsHelper.getAndSelectVideoTrackIndex(this.mVideoExtractor));
        Debugger.log("input video format: " + trackFormat);
        if (this.mTrimVideoEndUs == 0) {
            this.mTrimVideoEndUs = trackFormat.getLong("durationUs");
            Debugger.log("mTrimVideoEndUs was 0 but updated");
        }
        try {
            parseInt = trackFormat.getInteger("frame-rate");
        } catch (Exception e3) {
            parseInt = 0;
        }
        if (this.mOutputVideoMimeType == CodecsMime.VIDEO_CODEC_H264 && parseInt > 0) {
            this.mOutputVideoFrameRate = parseInt;
        }
        if (parseInt > this.mOutputVideoFrameRate) {
            this.mSkipFrames = true;
            this.mFramesSkipInterval = (int) Math.ceil((double) (((float) parseInt) / ((float) this.mOutputVideoFrameRate)));
        }
        Debugger.log("mOutputVideoFrameRate: " + this.mOutputVideoFrameRate);
        this.mTransRewritable = checkRewritable(this.mInputFilePath);
        Debugger.log("askRewritable: " + this.mTransRewritable);
        if (this.mTransRewritable == 1 || this.mTransRewritable == 2) {
            this.mTransRewritable = checkTransRewritable(trackFormat, parseInt, this.mInputFilePath);
            Debugger.log("mTransRewritable: " + this.mTransRewritable);
        }
        if (this.mTransRewritable != -1) {
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.mOutputVideoMimeType, this.mOutputWidth, this.mOutputHeight);
            createVideoFormat.setInteger("color-format", 2130708361);
            createVideoFormat.setInteger("bitrate", this.mOutputVideoBitRate);
            createVideoFormat.setInteger("frame-rate", this.mOutputVideoFrameRate);
            createVideoFormat.setInteger("i-frame-interval", this.mOutputVideoIFrameInterval);
            if (this.mOutputVideoProfile == -1) {
                Debugger.log("Skip video profile setup");
            } else {
                createVideoFormat.setInteger("profile", this.mOutputVideoProfile);
            }
            Debugger.log("output video format " + createVideoFormat);
            if (this.mTransRewritable != 1) {
                this.mOutputVideoEncoder = MediaCodec.createEncoderByType(this.mOutputVideoMimeType);
                this.mOutputVideoEncoder.configure(createVideoFormat, null, null, 1);
                this.mInputSurface = new InputSurface(this.mOutputVideoEncoder.createInputSurface());
                this.mOutputVideoEncoder.start();
                this.mInputSurface.makeCurrent();
                this.mOutputSurface = new OutputSurface(this.mInputOrientationDegrees);
                this.mInputVideoDecoder = CodecsHelper.createVideoDecoder(trackFormat, this.mOutputSurface.getSurface());
                if (this.mInputVideoDecoder == null) {
                    throw new IOException("can't set VideoDecoder");
                }
                return;
            }
            return;
        }
        throw new IOException("Unable to handle input file");
    }

    protected void prepare_for_transrewrite() throws IOException {
        this.mEncoding = true;
        this.mTransRewritable = -1;
        prepareVideoCodecNeo();
        prepareAudioCodec();
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
                this.mEncoding = false;
                this.mStopLock.notifyAll();
            }
        }
        if (this.mInputVideoDecoder != null) {
            try {
                this.mInputVideoDecoder.stop();
                this.mInputVideoDecoder.release();
                this.mInputVideoDecoder = null;
            } catch (Throwable e2) {
                Log.d(Constants.TAG, "Exception in releasing input video decoder.");
                e2.printStackTrace();
            }
        }
        if (this.mVideoExtractor != null) {
            try {
                this.mVideoExtractor.release();
                this.mVideoExtractor = null;
            } catch (Throwable e22) {
                Log.d(Constants.TAG, "Exception in releasing video extractor.");
                e22.printStackTrace();
            }
        }
        if (this.mOutputSurface != null) {
            try {
                this.mOutputSurface.release();
                this.mOutputSurface = null;
            } catch (Throwable e222) {
                Log.d(Constants.TAG, "Exception in releasing outputSurface.");
                e222.printStackTrace();
            }
        }
        if (this.mLogoRenderer != null) {
            try {
                this.mLogoRenderer.release();
                this.mLogoRenderer = null;
            } catch (Throwable e2222) {
                Log.d(Constants.TAG, "Exception in releasing logo renderer.");
                e2222.printStackTrace();
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
            } catch (Throwable e22222) {
                Log.d(Constants.TAG, "Exception in releasing input surface.");
                e22222.printStackTrace();
            }
        }
        if (this.mOutputAudioEncoder != null) {
            try {
                this.mOutputAudioEncoder.stop();
                this.mOutputAudioEncoder.release();
                this.mOutputAudioEncoder = null;
            } catch (Throwable e222222) {
                Log.d(Constants.TAG, "Exception in releasing output audio encoder.");
                e222222.printStackTrace();
            }
        }
        if (this.mInputAudioDecoder != null) {
            try {
                this.mInputAudioDecoder.stop();
                this.mInputAudioDecoder.release();
                this.mInputAudioDecoder = null;
            } catch (Throwable e2222222) {
                Log.d(Constants.TAG, "Exception in releasing input audio decoder.");
                e2222222.printStackTrace();
            }
        }
        if (this.mAudioExtractor != null) {
            try {
                this.mAudioExtractor.release();
                this.mAudioExtractor = null;
            } catch (Throwable e22222222) {
                Log.d(Constants.TAG, "Exception in releasing audio extractor.");
                e22222222.printStackTrace();
            }
        }
        if (this.mMuxer != null) {
            try {
                if (this.mMuxerStarted) {
                    this.mMuxer.stop();
                }
                this.mMuxer.release();
                this.mMuxer = null;
            } catch (Throwable e222222222) {
                Log.d(Constants.TAG, "Exception in releasing muxer.");
                e222222222.printStackTrace();
            }
        }
        synchronized (this.mStopLock) {
            this.mEncoding = false;
            this.mStopLock.notifyAll();
        }
    }

    public void setEncodingCodecs(int i, int i2) {
        switch (i) {
            case 3:
                this.mOutputVideoMimeType = "video/3gpp";
                break;
            case 4:
                this.mOutputVideoMimeType = CodecsMime.VIDEO_CODEC_H264;
                break;
            default:
                Log.e(Constants.TAG, "videoCodecType is: " + i);
                throw new IllegalArgumentException("Invalid video codec");
        }
        switch (i2) {
            case 1:
                this.mOutputAudioMimeType = CodecsMime.AUDIO_CODEC_AMR;
                return;
            case 2:
                this.mOutputAudioMimeType = CodecsMime.AUDIO_CODEC_AAC;
                return;
            default:
                throw new IllegalArgumentException("Invalid audio codec");
        }
    }

    public void setMaxOutputSize(int i) {
        boolean z = false;
        if (i > 0) {
            Log.d(Constants.TAG, "max output size is " + i);
            this.mOutputMaxSizeKB = (long) i;
            if (this.mOutputMaxSizeKB < 1000) {
                z = true;
            }
            if (z || (this.mOutputWidth < 200 && this.mOutputHeight < 200)) {
                this.mMMSMode = true;
            }
            Log.d(Constants.TAG, "mMMSMode is " + this.mMMSMode);
            return;
        }
        throw new IllegalArgumentException("size cannot be 0 or lesser");
    }

    public void setTrimTime(long j, long j2) {
        Object obj = 1;
        if ((j >= 0 ? 1 : null) == null) {
            throw new IllegalArgumentException("start time cannot be negative");
        }
        if ((j2 >= 0 ? 1 : null) == null) {
            throw new IllegalArgumentException("end time cannot be negative");
        }
        if (j > j2) {
            obj = null;
        }
        if (obj == null) {
            throw new IllegalArgumentException("start time cannot be more than end time");
        } else if (j == j2) {
            throw new IllegalArgumentException("endUs cannot be equal to startUs");
        } else {
            long j3 = j * 1000;
            this.mTrimAudioStartUs = j3;
            this.mTrimVideoStartUs = j3;
            j3 = j2 * 1000;
            this.mTrimAudioEndUs = j3;
            this.mTrimVideoEndUs = j3;
            Debugger.log("Trim startUS: " + this.mTrimVideoStartUs + ", endUS: " + this.mTrimVideoEndUs);
        }
    }

    protected void startEncoding() throws IOException {
        Throwable e;
        MediaFormat mediaFormat;
        Buffer allocateDirect;
        int dequeueOutputBuffer;
        if (this.mUserStop) {
            Log.d(Constants.TAG, "Not starting encoding because it is stopped by user.");
            return;
        }
        ByteBuffer[] outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
        ByteBuffer[] inputBuffers = this.mInputVideoDecoder.getInputBuffers();
        ByteBuffer[] outputBuffers2 = this.mInputVideoDecoder.getOutputBuffers();
        ByteBuffer[] outputBuffers3 = !this.mCopyAudio ? null : this.mOutputAudioEncoder.getOutputBuffers();
        Buffer[] inputBuffers2 = !this.mCopyAudio ? null : this.mOutputAudioEncoder.getInputBuffers();
        ByteBuffer[] outputBuffers4 = !this.mCopyAudio ? null : this.mInputAudioDecoder.getOutputBuffers();
        ByteBuffer[] inputBuffers3 = !this.mCopyAudio ? null : this.mInputAudioDecoder.getInputBuffers();
        BufferInfo bufferInfo = new BufferInfo();
        BufferInfo bufferInfo2 = new BufferInfo();
        BufferInfo bufferInfo3 = new BufferInfo();
        BufferInfo bufferInfo4 = new BufferInfo();
        MediaFormat mediaFormat2 = null;
        MediaFormat mediaFormat3 = null;
        Object obj = !this.mCopyAudio ? 1 : null;
        Object obj2 = !this.mCopyAudio ? 1 : null;
        Object obj3 = !this.mCopyAudio ? 1 : null;
        Object obj4 = null;
        Object obj5 = null;
        int i = -1;
        int i2 = 0;
        int i3 = 0;
        int i4 = this.mOutputVideoFrameRate << 1;
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
        if (this.mTrimVideoStartUs != 0) {
            this.mVideoExtractor.seekTo(this.mTrimVideoStartUs, 0);
        }
        if (this.mCopyAudio && this.mTrimAudioStartUs != 0) {
            this.mAudioExtractor.seekTo(this.mTrimAudioStartUs, 0);
            while (true) {
                if ((this.mAudioExtractor.getSampleTime() >= this.mTrimAudioStartUs ? 1 : null) == null) {
                    if (this.mAudioExtractor.getSampleTime() == -1) {
                        throw new RuntimeException("Invalid File!");
                    }
                    this.mAudioExtractor.advance();
                }
            }
        }
        long j = -1;
        Object obj6 = obj3;
        Object obj7 = obj2;
        Object obj8 = obj;
        ByteBuffer[] byteBufferArr = outputBuffers4;
        ByteBuffer[] byteBufferArr2 = outputBuffers3;
        Object obj9 = null;
        while (true) {
            if (obj4 == null || obj6 == null) {
                int dequeueInputBuffer;
                int readSampleData;
                long sampleTime;
                Object obj10;
                ByteBuffer byteBuffer;
                boolean z;
                int i5;
                int i6;
                ByteBuffer[] byteBufferArr3;
                Buffer buffer;
                Buffer duplicate;
                ByteBuffer byteBuffer2;
                long j2;
                Object obj11;
                if (!this.mUserStop && obj9 == null) {
                    if (mediaFormat3 == null || this.mMuxerStarted) {
                        dequeueInputBuffer = this.mInputVideoDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            readSampleData = this.mVideoExtractor.readSampleData(inputBuffers[dequeueInputBuffer], 0);
                            sampleTime = this.mVideoExtractor.getSampleTime();
                            if ((sampleTime > this.mTrimVideoEndUs ? 1 : null) == null) {
                                if (readSampleData >= 0) {
                                    this.mInputVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mVideoExtractor.getSampleFlags());
                                }
                                obj10 = this.mVideoExtractor.advance() ? null : 1;
                            } else {
                                obj10 = 1;
                            }
                            if (obj10 != null) {
                                Log.d(Constants.TAG, "video extractor: EOS");
                                this.mInputVideoDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            }
                        } else {
                            Log.d(Constants.TAG, "no video decoder input buffer");
                            obj10 = obj9;
                        }
                        while (!this.mUserStop && r16 == null) {
                            if (mediaFormat3 == null || this.mMuxerStarted) {
                                dequeueInputBuffer = this.mInputVideoDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "no video decoder output buffer");
                                } else if (dequeueInputBuffer != -3) {
                                    Log.d(Constants.TAG, "video decoder: output buffers changed");
                                    outputBuffers2 = this.mInputVideoDecoder.getOutputBuffers();
                                } else if (dequeueInputBuffer == -2) {
                                    byteBuffer = outputBuffers2[dequeueInputBuffer];
                                    if ((bufferInfo2.flags & 2) != 0) {
                                        Log.d(Constants.TAG, "video decoder: returned buffer for time " + bufferInfo2.presentationTimeUs);
                                        z = bufferInfo2.size == 0;
                                        this.mInputVideoDecoder.releaseOutputBuffer(dequeueInputBuffer, z);
                                        if (!z) {
                                            Log.d(Constants.TAG, "output surface: await new image");
                                            if (this.mOutputSurface.checkForNewImage(1000)) {
                                                try {
                                                    Log.d(Constants.TAG, "video decoder: checkForNewImage return false!!  mUserStop : " + this.mUserStop);
                                                } catch (RuntimeException e2) {
                                                    e = e2;
                                                }
                                            } else {
                                                Log.d(Constants.TAG, "output surface: draw image");
                                                GLES20.glClear(16384);
                                                this.mOutputSurface.drawImage();
                                                if (this.mLogoPresent) {
                                                    if (i2 % i4 < this.mOutputVideoFrameRate) {
                                                        this.mLogoRenderer.draw();
                                                    }
                                                }
                                                if ((bufferInfo2.presentationTimeUs >= this.mTrimVideoStartUs ? 1 : null) != null) {
                                                    if (this.mSkipFrames && i3 % this.mFramesSkipInterval != 0) {
                                                        i5 = i3;
                                                        dequeueInputBuffer = i2;
                                                    } else {
                                                        i5 = 0;
                                                        try {
                                                            this.mInputSurface.setPresentationTime(bufferInfo2.presentationTimeUs * 1000);
                                                            Log.d(Constants.TAG, "input surface: swap buffers");
                                                            this.mInputSurface.swapBuffers();
                                                            Log.d(Constants.TAG, "video encoder: notified of new frame");
                                                            dequeueInputBuffer = i2 + 1;
                                                        } catch (Throwable e3) {
                                                            i3 = 0;
                                                            e = e3;
                                                            String message = e.getMessage();
                                                            if (this.mUserStop && message != null) {
                                                                if (!message.equals(OutputSurface.EXCEPTION_FRAME_NOT_AVAILABLE)) {
                                                                }
                                                                if ((bufferInfo2.flags & 4) != 0) {
                                                                    Log.d(Constants.TAG, "video decoder: EOS");
                                                                    this.mOutputVideoEncoder.signalEndOfInputStream();
                                                                    i6 = 1;
                                                                }
                                                                if (mediaFormat3 == null) {
                                                                    if (this.mCopyAudio) {
                                                                        obj9 = obj7;
                                                                        mediaFormat = mediaFormat2;
                                                                        byteBufferArr3 = byteBufferArr2;
                                                                    } else {
                                                                        if (mediaFormat2 == null) {
                                                                            if (mediaFormat2 == null) {
                                                                                if (!this.mUserStop) {
                                                                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                                    if (dequeueInputBuffer != -1) {
                                                                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                        obj9 = obj7;
                                                                                    } else {
                                                                                        buffer = inputBuffers2[dequeueInputBuffer];
                                                                                        readSampleData = bufferInfo4.size;
                                                                                        sampleTime = bufferInfo4.presentationTimeUs;
                                                                                        if (readSampleData >= 0) {
                                                                                            duplicate = byteBufferArr[i].duplicate();
                                                                                            duplicate.position(bufferInfo4.offset);
                                                                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                                if (allocateDirect == null) {
                                                                                                    allocateDirect.position(0);
                                                                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                                    }
                                                                                                    buffer.position(0);
                                                                                                    buffer.put(allocateDirect);
                                                                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                                    allocateDirect.clear();
                                                                                                } else {
                                                                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                                                                }
                                                                                            } else {
                                                                                                buffer.position(0);
                                                                                                buffer.put(duplicate);
                                                                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                            }
                                                                                        }
                                                                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                        i = -1;
                                                                                        if ((bufferInfo4.flags & 4) == 0) {
                                                                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                            obj9 = 1;
                                                                                        } else {
                                                                                            obj9 = obj7;
                                                                                        }
                                                                                    }
                                                                                    if (mediaFormat2 == null) {
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                                    if (dequeueOutputBuffer == -1) {
                                                                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else if (dequeueOutputBuffer == -3) {
                                                                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                        mediaFormat = mediaFormat2;
                                                                                    } else if (dequeueOutputBuffer != -2) {
                                                                                        if (this.mAudioTrackIndex < 0) {
                                                                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                        }
                                                                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else if (dequeueOutputBuffer >= 0) {
                                                                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                        if ((bufferInfo3.flags & 2) == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                            mediaFormat = mediaFormat2;
                                                                                            byteBufferArr3 = byteBufferArr2;
                                                                                        } else {
                                                                                            if (bufferInfo3.size == 0) {
                                                                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                    j2 = bufferInfo3.presentationTimeUs;
                                                                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                                } else {
                                                                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                                                                }
                                                                                            }
                                                                                            j2 = j;
                                                                                            if ((bufferInfo3.flags & 4) == 0) {
                                                                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                                obj11 = 1;
                                                                                            } else {
                                                                                                obj11 = obj6;
                                                                                            }
                                                                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                            j = j2;
                                                                                            obj6 = obj11;
                                                                                            mediaFormat = mediaFormat2;
                                                                                            byteBufferArr3 = byteBufferArr2;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                obj9 = obj7;
                                                                                if (mediaFormat2 == null) {
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer == -3) {
                                                                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                    mediaFormat = mediaFormat2;
                                                                                } else if (dequeueOutputBuffer != -2) {
                                                                                    if (dequeueOutputBuffer >= 0) {
                                                                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                        if ((bufferInfo3.flags & 2) == 0) {
                                                                                            if (bufferInfo3.size == 0) {
                                                                                                j2 = j;
                                                                                            } else {
                                                                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                                                                }
                                                                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                                                                }
                                                                                                j2 = bufferInfo3.presentationTimeUs;
                                                                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                            }
                                                                                            if ((bufferInfo3.flags & 4) == 0) {
                                                                                                obj11 = obj6;
                                                                                            } else {
                                                                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                                obj11 = 1;
                                                                                            }
                                                                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                            j = j2;
                                                                                            obj6 = obj11;
                                                                                            mediaFormat = mediaFormat2;
                                                                                            byteBufferArr3 = byteBufferArr2;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                            mediaFormat = mediaFormat2;
                                                                                            byteBufferArr3 = byteBufferArr2;
                                                                                        }
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                } else if (this.mAudioTrackIndex < 0) {
                                                                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                }
                                                                            }
                                                                            i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                                                            if (i5 == -1) {
                                                                                Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                                                            } else if (i5 == -3) {
                                                                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                                                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                                                            } else if (i5 == -2) {
                                                                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                                                            } else if (i5 < 0) {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                                                            } else if ((bufferInfo4.flags & 2) == 0) {
                                                                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                                                                this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                                                            } else {
                                                                                i = i5;
                                                                            }
                                                                            if (this.mUserStop) {
                                                                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                                if (dequeueInputBuffer != -1) {
                                                                                    buffer = inputBuffers2[dequeueInputBuffer];
                                                                                    readSampleData = bufferInfo4.size;
                                                                                    sampleTime = bufferInfo4.presentationTimeUs;
                                                                                    if (readSampleData >= 0) {
                                                                                        duplicate = byteBufferArr[i].duplicate();
                                                                                        duplicate.position(bufferInfo4.offset);
                                                                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                        if (this.mOriginalAudioChannelCount <= 0) {
                                                                                            buffer.position(0);
                                                                                            buffer.put(duplicate);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                        } else {
                                                                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                            if (allocateDirect == null) {
                                                                                                Log.e(Constants.TAG, "TempAudio is null!");
                                                                                            } else {
                                                                                                allocateDirect.position(0);
                                                                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                                }
                                                                                                buffer.position(0);
                                                                                                buffer.put(allocateDirect);
                                                                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                                allocateDirect.clear();
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                    i = -1;
                                                                                    if ((bufferInfo4.flags & 4) == 0) {
                                                                                        obj9 = obj7;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                        obj9 = 1;
                                                                                    }
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                    obj9 = obj7;
                                                                                }
                                                                                if (mediaFormat2 == null) {
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer == -3) {
                                                                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                    mediaFormat = mediaFormat2;
                                                                                } else if (dequeueOutputBuffer != -2) {
                                                                                    if (this.mAudioTrackIndex < 0) {
                                                                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                    }
                                                                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer >= 0) {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                j2 = bufferInfo3.presentationTimeUs;
                                                                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                            } else {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                        }
                                                                                        j2 = j;
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        } else {
                                                                                            obj11 = obj6;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                }
                                                                            }
                                                                            obj9 = obj7;
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (dequeueOutputBuffer >= 0) {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            j2 = j;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        }
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            obj11 = obj6;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else if (this.mAudioTrackIndex < 0) {
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                            }
                                                                        }
                                                                        dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                        if (dequeueInputBuffer != -1) {
                                                                            Log.d(Constants.TAG, "audio decoder input try again later");
                                                                        } else {
                                                                            readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                                                            sampleTime = this.mAudioExtractor.getSampleTime();
                                                                            if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                                                                obj8 = 1;
                                                                            } else {
                                                                                if (readSampleData > 0) {
                                                                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                                                                }
                                                                                obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                                                            }
                                                                            if (obj8 != null) {
                                                                                Log.d(Constants.TAG, "audio decoder sending EOS");
                                                                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                                                            }
                                                                        }
                                                                        if (mediaFormat2 == null) {
                                                                            if (this.mUserStop) {
                                                                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                                if (dequeueInputBuffer != -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                    obj9 = obj7;
                                                                                } else {
                                                                                    buffer = inputBuffers2[dequeueInputBuffer];
                                                                                    readSampleData = bufferInfo4.size;
                                                                                    sampleTime = bufferInfo4.presentationTimeUs;
                                                                                    if (readSampleData >= 0) {
                                                                                        duplicate = byteBufferArr[i].duplicate();
                                                                                        duplicate.position(bufferInfo4.offset);
                                                                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                        if (this.mOriginalAudioChannelCount <= 0) {
                                                                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                            if (allocateDirect == null) {
                                                                                                allocateDirect.position(0);
                                                                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                                }
                                                                                                buffer.position(0);
                                                                                                buffer.put(allocateDirect);
                                                                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                                allocateDirect.clear();
                                                                                            } else {
                                                                                                Log.e(Constants.TAG, "TempAudio is null!");
                                                                                            }
                                                                                        } else {
                                                                                            buffer.position(0);
                                                                                            buffer.put(duplicate);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                        }
                                                                                    }
                                                                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                    i = -1;
                                                                                    if ((bufferInfo4.flags & 4) == 0) {
                                                                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                        obj9 = 1;
                                                                                    } else {
                                                                                        obj9 = obj7;
                                                                                    }
                                                                                }
                                                                                if (mediaFormat2 == null) {
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer == -3) {
                                                                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                    mediaFormat = mediaFormat2;
                                                                                } else if (dequeueOutputBuffer != -2) {
                                                                                    if (this.mAudioTrackIndex < 0) {
                                                                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                    }
                                                                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer >= 0) {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                j2 = bufferInfo3.presentationTimeUs;
                                                                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                            } else {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                        }
                                                                                        j2 = j;
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        } else {
                                                                                            obj11 = obj6;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                }
                                                                            }
                                                                            obj9 = obj7;
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (dequeueOutputBuffer >= 0) {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            j2 = j;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        }
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            obj11 = obj6;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else if (this.mAudioTrackIndex < 0) {
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                            }
                                                                        }
                                                                        i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                                                        if (i5 == -1) {
                                                                            Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                                                        } else if (i5 == -3) {
                                                                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                                                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                                                        } else if (i5 == -2) {
                                                                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                                                        } else if (i5 < 0) {
                                                                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                                                        } else if ((bufferInfo4.flags & 2) == 0) {
                                                                            i = i5;
                                                                        } else {
                                                                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                                                            this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                                                        }
                                                                        if (this.mUserStop) {
                                                                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                            if (dequeueInputBuffer != -1) {
                                                                                buffer = inputBuffers2[dequeueInputBuffer];
                                                                                readSampleData = bufferInfo4.size;
                                                                                sampleTime = bufferInfo4.presentationTimeUs;
                                                                                if (readSampleData >= 0) {
                                                                                    duplicate = byteBufferArr[i].duplicate();
                                                                                    duplicate.position(bufferInfo4.offset);
                                                                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                    if (this.mOriginalAudioChannelCount <= 0) {
                                                                                        buffer.position(0);
                                                                                        buffer.put(duplicate);
                                                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                    } else {
                                                                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                        if (allocateDirect == null) {
                                                                                            Log.e(Constants.TAG, "TempAudio is null!");
                                                                                        } else {
                                                                                            allocateDirect.position(0);
                                                                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                            }
                                                                                            buffer.position(0);
                                                                                            buffer.put(allocateDirect);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                            allocateDirect.clear();
                                                                                        }
                                                                                    }
                                                                                }
                                                                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                i = -1;
                                                                                if ((bufferInfo4.flags & 4) == 0) {
                                                                                    obj9 = obj7;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                    obj9 = 1;
                                                                                }
                                                                            } else {
                                                                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                obj9 = obj7;
                                                                            }
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (this.mAudioTrackIndex < 0) {
                                                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                }
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer >= 0) {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        } else {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                    }
                                                                                    j2 = j;
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    } else {
                                                                                        obj11 = obj6;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            }
                                                                        }
                                                                        obj9 = obj7;
                                                                        if (mediaFormat2 == null) {
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        }
                                                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                        if (dequeueOutputBuffer == -1) {
                                                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else if (dequeueOutputBuffer == -3) {
                                                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                            mediaFormat = mediaFormat2;
                                                                        } else if (dequeueOutputBuffer != -2) {
                                                                            if (dequeueOutputBuffer >= 0) {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        j2 = j;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                        j2 = bufferInfo3.presentationTimeUs;
                                                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                    }
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        obj11 = obj6;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                        } else if (this.mAudioTrackIndex < 0) {
                                                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else {
                                                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                        }
                                                                    }
                                                                    if (!this.mCopyAudio) {
                                                                        if (this.mUserStop) {
                                                                            obj7 = obj9;
                                                                            mediaFormat2 = mediaFormat;
                                                                            byteBufferArr2 = byteBufferArr3;
                                                                            obj9 = obj10;
                                                                        } else {
                                                                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                                                            return;
                                                                        }
                                                                    }
                                                                    this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                                                                    if (this.mCopyAudio) {
                                                                        this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                                                                    }
                                                                    this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                                                                    this.mMuxer.start();
                                                                    this.mMuxerStarted = true;
                                                                    if (this.mUserStop) {
                                                                        obj7 = obj9;
                                                                        mediaFormat2 = mediaFormat;
                                                                        byteBufferArr2 = byteBufferArr3;
                                                                        obj9 = obj10;
                                                                    } else {
                                                                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                                                        return;
                                                                    }
                                                                }
                                                                dequeueInputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                                                                if (dequeueInputBuffer == -1) {
                                                                    Log.d(Constants.TAG, "no video encoder output buffer");
                                                                } else if (dequeueInputBuffer == -3) {
                                                                    Log.d(Constants.TAG, "video encoder: output buffers changed");
                                                                    outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                                                                } else if (dequeueInputBuffer == -2) {
                                                                    Log.d(Constants.TAG, "video encoder: output format changed " + this.mOutputVideoEncoder.getOutputFormat());
                                                                    if (this.mVideoTrackIndex < 0) {
                                                                        mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                                                                    } else {
                                                                        throw new RuntimeException("Video encoder output format changed after muxer has started");
                                                                    }
                                                                } else if (dequeueInputBuffer >= 0) {
                                                                    byteBuffer = outputBuffers[dequeueInputBuffer];
                                                                    if ((bufferInfo.flags & 2) == 0) {
                                                                        if (bufferInfo.size != 0) {
                                                                            Log.d(Constants.TAG, "video encoder: writing sample data timestamp " + bufferInfo.presentationTimeUs);
                                                                            this.mMuxer.writeSampleData(this.mVideoTrackIndex, byteBuffer, bufferInfo);
                                                                        }
                                                                        if ((bufferInfo.flags & 4) == 0) {
                                                                            obj9 = obj4;
                                                                        } else {
                                                                            Log.d(Constants.TAG, "video encoder: EOS");
                                                                            obj9 = 1;
                                                                        }
                                                                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                                                        obj4 = obj9;
                                                                    } else {
                                                                        Log.d(Constants.TAG, "video encoder: codec config buffer");
                                                                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                                                    }
                                                                } else {
                                                                    Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                                                                }
                                                                if (this.mCopyAudio) {
                                                                    obj9 = obj7;
                                                                    mediaFormat = mediaFormat2;
                                                                    byteBufferArr3 = byteBufferArr2;
                                                                } else {
                                                                    if (mediaFormat2 == null) {
                                                                        if (mediaFormat2 == null) {
                                                                            if (this.mUserStop) {
                                                                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                                if (dequeueInputBuffer != -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                    obj9 = obj7;
                                                                                } else {
                                                                                    buffer = inputBuffers2[dequeueInputBuffer];
                                                                                    readSampleData = bufferInfo4.size;
                                                                                    sampleTime = bufferInfo4.presentationTimeUs;
                                                                                    if (readSampleData >= 0) {
                                                                                        duplicate = byteBufferArr[i].duplicate();
                                                                                        duplicate.position(bufferInfo4.offset);
                                                                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                        if (this.mOriginalAudioChannelCount <= 0) {
                                                                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                            if (allocateDirect == null) {
                                                                                                allocateDirect.position(0);
                                                                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                                }
                                                                                                buffer.position(0);
                                                                                                buffer.put(allocateDirect);
                                                                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                                allocateDirect.clear();
                                                                                            } else {
                                                                                                Log.e(Constants.TAG, "TempAudio is null!");
                                                                                            }
                                                                                        } else {
                                                                                            buffer.position(0);
                                                                                            buffer.put(duplicate);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                        }
                                                                                    }
                                                                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                    i = -1;
                                                                                    if ((bufferInfo4.flags & 4) == 0) {
                                                                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                        obj9 = 1;
                                                                                    } else {
                                                                                        obj9 = obj7;
                                                                                    }
                                                                                }
                                                                                if (mediaFormat2 == null) {
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                                if (dequeueOutputBuffer == -1) {
                                                                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer == -3) {
                                                                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                    mediaFormat = mediaFormat2;
                                                                                } else if (dequeueOutputBuffer != -2) {
                                                                                    if (this.mAudioTrackIndex < 0) {
                                                                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                    }
                                                                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else if (dequeueOutputBuffer >= 0) {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                j2 = bufferInfo3.presentationTimeUs;
                                                                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                            } else {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                        }
                                                                                        j2 = j;
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        } else {
                                                                                            obj11 = obj6;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                }
                                                                            }
                                                                            obj9 = obj7;
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (dequeueOutputBuffer >= 0) {
                                                                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                    if ((bufferInfo3.flags & 2) == 0) {
                                                                                        if (bufferInfo3.size == 0) {
                                                                                            j2 = j;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                            if (j <= bufferInfo3.presentationTimeUs) {
                                                                                            }
                                                                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                                throw new IOException("Audio time stamps are not in increasing order.");
                                                                                            }
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        }
                                                                                        if ((bufferInfo3.flags & 4) == 0) {
                                                                                            obj11 = obj6;
                                                                                        } else {
                                                                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                            obj11 = 1;
                                                                                        }
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        j = j2;
                                                                                        obj6 = obj11;
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                        mediaFormat = mediaFormat2;
                                                                                        byteBufferArr3 = byteBufferArr2;
                                                                                    }
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else if (this.mAudioTrackIndex < 0) {
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                            }
                                                                        }
                                                                        i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                                                        if (i5 == -1) {
                                                                            Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                                                        } else if (i5 == -3) {
                                                                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                                                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                                                        } else if (i5 == -2) {
                                                                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                                                        } else if (i5 < 0) {
                                                                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                                                        } else if ((bufferInfo4.flags & 2) == 0) {
                                                                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                                                            this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                                                        } else {
                                                                            i = i5;
                                                                        }
                                                                        if (this.mUserStop) {
                                                                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                            if (dequeueInputBuffer != -1) {
                                                                                buffer = inputBuffers2[dequeueInputBuffer];
                                                                                readSampleData = bufferInfo4.size;
                                                                                sampleTime = bufferInfo4.presentationTimeUs;
                                                                                if (readSampleData >= 0) {
                                                                                    duplicate = byteBufferArr[i].duplicate();
                                                                                    duplicate.position(bufferInfo4.offset);
                                                                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                    if (this.mOriginalAudioChannelCount <= 0) {
                                                                                        buffer.position(0);
                                                                                        buffer.put(duplicate);
                                                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                    } else {
                                                                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                        if (allocateDirect == null) {
                                                                                            Log.e(Constants.TAG, "TempAudio is null!");
                                                                                        } else {
                                                                                            allocateDirect.position(0);
                                                                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                            }
                                                                                            buffer.position(0);
                                                                                            buffer.put(allocateDirect);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                            allocateDirect.clear();
                                                                                        }
                                                                                    }
                                                                                }
                                                                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                i = -1;
                                                                                if ((bufferInfo4.flags & 4) == 0) {
                                                                                    obj9 = obj7;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                    obj9 = 1;
                                                                                }
                                                                            } else {
                                                                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                obj9 = obj7;
                                                                            }
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (this.mAudioTrackIndex < 0) {
                                                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                }
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer >= 0) {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        } else {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                    }
                                                                                    j2 = j;
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    } else {
                                                                                        obj11 = obj6;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            }
                                                                        }
                                                                        obj9 = obj7;
                                                                        if (mediaFormat2 == null) {
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        }
                                                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                        if (dequeueOutputBuffer == -1) {
                                                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else if (dequeueOutputBuffer == -3) {
                                                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                            mediaFormat = mediaFormat2;
                                                                        } else if (dequeueOutputBuffer != -2) {
                                                                            if (dequeueOutputBuffer >= 0) {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        j2 = j;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                        j2 = bufferInfo3.presentationTimeUs;
                                                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                    }
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        obj11 = obj6;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                        } else if (this.mAudioTrackIndex < 0) {
                                                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else {
                                                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                        }
                                                                    }
                                                                    dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                    if (dequeueInputBuffer != -1) {
                                                                        readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                                                        sampleTime = this.mAudioExtractor.getSampleTime();
                                                                        if (sampleTime > this.mTrimAudioEndUs) {
                                                                        }
                                                                        if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                                                            if (readSampleData > 0) {
                                                                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                                                            }
                                                                            if (this.mAudioExtractor.advance()) {
                                                                            }
                                                                            obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                                                        } else {
                                                                            obj8 = 1;
                                                                        }
                                                                        if (obj8 != null) {
                                                                            Log.d(Constants.TAG, "audio decoder sending EOS");
                                                                            this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                                                        }
                                                                    } else {
                                                                        Log.d(Constants.TAG, "audio decoder input try again later");
                                                                    }
                                                                    if (mediaFormat2 == null) {
                                                                        if (this.mUserStop) {
                                                                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                            if (dequeueInputBuffer != -1) {
                                                                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                                obj9 = obj7;
                                                                            } else {
                                                                                buffer = inputBuffers2[dequeueInputBuffer];
                                                                                readSampleData = bufferInfo4.size;
                                                                                sampleTime = bufferInfo4.presentationTimeUs;
                                                                                if (readSampleData >= 0) {
                                                                                    duplicate = byteBufferArr[i].duplicate();
                                                                                    duplicate.position(bufferInfo4.offset);
                                                                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                    if (this.mOriginalAudioChannelCount <= 0) {
                                                                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                        if (allocateDirect == null) {
                                                                                            allocateDirect.position(0);
                                                                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                            }
                                                                                            buffer.position(0);
                                                                                            buffer.put(allocateDirect);
                                                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                            allocateDirect.clear();
                                                                                        } else {
                                                                                            Log.e(Constants.TAG, "TempAudio is null!");
                                                                                        }
                                                                                    } else {
                                                                                        buffer.position(0);
                                                                                        buffer.put(duplicate);
                                                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                    }
                                                                                }
                                                                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                                i = -1;
                                                                                if ((bufferInfo4.flags & 4) == 0) {
                                                                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                    obj9 = 1;
                                                                                } else {
                                                                                    obj9 = obj7;
                                                                                }
                                                                            }
                                                                            if (mediaFormat2 == null) {
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                            if (dequeueOutputBuffer == -1) {
                                                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer == -3) {
                                                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                                mediaFormat = mediaFormat2;
                                                                            } else if (dequeueOutputBuffer != -2) {
                                                                                if (this.mAudioTrackIndex < 0) {
                                                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                                }
                                                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else if (dequeueOutputBuffer >= 0) {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            j2 = bufferInfo3.presentationTimeUs;
                                                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                        } else {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                    }
                                                                                    j2 = j;
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    } else {
                                                                                        obj11 = obj6;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            }
                                                                        }
                                                                        obj9 = obj7;
                                                                        if (mediaFormat2 == null) {
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        }
                                                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                        if (dequeueOutputBuffer == -1) {
                                                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else if (dequeueOutputBuffer == -3) {
                                                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                            mediaFormat = mediaFormat2;
                                                                        } else if (dequeueOutputBuffer != -2) {
                                                                            if (dequeueOutputBuffer >= 0) {
                                                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                                if ((bufferInfo3.flags & 2) == 0) {
                                                                                    if (bufferInfo3.size == 0) {
                                                                                        j2 = j;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                                                        }
                                                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                                                        }
                                                                                        j2 = bufferInfo3.presentationTimeUs;
                                                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                    }
                                                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                                                        obj11 = obj6;
                                                                                    } else {
                                                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                        obj11 = 1;
                                                                                    }
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    j = j2;
                                                                                    obj6 = obj11;
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                    mediaFormat = mediaFormat2;
                                                                                    byteBufferArr3 = byteBufferArr2;
                                                                                }
                                                                            } else {
                                                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                        } else if (this.mAudioTrackIndex < 0) {
                                                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else {
                                                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                        }
                                                                    }
                                                                    i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                                                    if (i5 == -1) {
                                                                        Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                                                    } else if (i5 == -3) {
                                                                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                                                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                                                    } else if (i5 == -2) {
                                                                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                                                    } else if (i5 < 0) {
                                                                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                                                    } else if ((bufferInfo4.flags & 2) == 0) {
                                                                        i = i5;
                                                                    } else {
                                                                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                                                        this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                                                    }
                                                                    if (this.mUserStop) {
                                                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                                                        if (dequeueInputBuffer != -1) {
                                                                            buffer = inputBuffers2[dequeueInputBuffer];
                                                                            readSampleData = bufferInfo4.size;
                                                                            sampleTime = bufferInfo4.presentationTimeUs;
                                                                            if (readSampleData >= 0) {
                                                                                duplicate = byteBufferArr[i].duplicate();
                                                                                duplicate.position(bufferInfo4.offset);
                                                                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                                                                if (this.mOriginalAudioChannelCount <= 0) {
                                                                                    buffer.position(0);
                                                                                    buffer.put(duplicate);
                                                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                                                } else {
                                                                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                                                    if (allocateDirect == null) {
                                                                                        Log.e(Constants.TAG, "TempAudio is null!");
                                                                                    } else {
                                                                                        allocateDirect.position(0);
                                                                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                                                        }
                                                                                        buffer.position(0);
                                                                                        buffer.put(allocateDirect);
                                                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                                                        allocateDirect.clear();
                                                                                    }
                                                                                }
                                                                            }
                                                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                                            i = -1;
                                                                            if ((bufferInfo4.flags & 4) == 0) {
                                                                                obj9 = obj7;
                                                                            } else {
                                                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                                                obj9 = 1;
                                                                            }
                                                                        } else {
                                                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                                            obj9 = obj7;
                                                                        }
                                                                        if (mediaFormat2 == null) {
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        }
                                                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                        if (dequeueOutputBuffer == -1) {
                                                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else if (dequeueOutputBuffer == -3) {
                                                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                            mediaFormat = mediaFormat2;
                                                                        } else if (dequeueOutputBuffer != -2) {
                                                                            if (this.mAudioTrackIndex < 0) {
                                                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                            }
                                                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else if (dequeueOutputBuffer >= 0) {
                                                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        } else {
                                                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                            if ((bufferInfo3.flags & 2) == 0) {
                                                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                if (bufferInfo3.size == 0) {
                                                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                                                    }
                                                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                        j2 = bufferInfo3.presentationTimeUs;
                                                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                    } else {
                                                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                                                    }
                                                                                }
                                                                                j2 = j;
                                                                                if ((bufferInfo3.flags & 4) == 0) {
                                                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                    obj11 = 1;
                                                                                } else {
                                                                                    obj11 = obj6;
                                                                                }
                                                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                j = j2;
                                                                                obj6 = obj11;
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                        }
                                                                    }
                                                                    obj9 = obj7;
                                                                    if (mediaFormat2 == null) {
                                                                        mediaFormat = mediaFormat2;
                                                                        byteBufferArr3 = byteBufferArr2;
                                                                    }
                                                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                                                    if (dequeueOutputBuffer == -1) {
                                                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                                        mediaFormat = mediaFormat2;
                                                                        byteBufferArr3 = byteBufferArr2;
                                                                    } else if (dequeueOutputBuffer == -3) {
                                                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                                        mediaFormat = mediaFormat2;
                                                                    } else if (dequeueOutputBuffer != -2) {
                                                                        if (dequeueOutputBuffer >= 0) {
                                                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                                            if ((bufferInfo3.flags & 2) == 0) {
                                                                                if (bufferInfo3.size == 0) {
                                                                                    j2 = j;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                                                    }
                                                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                                                    }
                                                                                    j2 = bufferInfo3.presentationTimeUs;
                                                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                                                }
                                                                                if ((bufferInfo3.flags & 4) == 0) {
                                                                                    obj11 = obj6;
                                                                                } else {
                                                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                                                    obj11 = 1;
                                                                                }
                                                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                j = j2;
                                                                                obj6 = obj11;
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            } else {
                                                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                                                mediaFormat = mediaFormat2;
                                                                                byteBufferArr3 = byteBufferArr2;
                                                                            }
                                                                        } else {
                                                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                                            mediaFormat = mediaFormat2;
                                                                            byteBufferArr3 = byteBufferArr2;
                                                                        }
                                                                    } else if (this.mAudioTrackIndex < 0) {
                                                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                                        byteBufferArr3 = byteBufferArr2;
                                                                    } else {
                                                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                                    }
                                                                }
                                                                if (!this.mCopyAudio) {
                                                                    if (this.mUserStop) {
                                                                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                                                        return;
                                                                    }
                                                                    obj7 = obj9;
                                                                    mediaFormat2 = mediaFormat;
                                                                    byteBufferArr2 = byteBufferArr3;
                                                                    obj9 = obj10;
                                                                }
                                                                this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                                                                if (this.mCopyAudio) {
                                                                    this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                                                                }
                                                                this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                                                                this.mMuxer.start();
                                                                this.mMuxerStarted = true;
                                                                if (this.mUserStop) {
                                                                    obj7 = obj9;
                                                                    mediaFormat2 = mediaFormat;
                                                                    byteBufferArr2 = byteBufferArr3;
                                                                    obj9 = obj10;
                                                                } else {
                                                                    Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                                                    return;
                                                                }
                                                            }
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                    i3 = i5 + 1;
                                                } else {
                                                    dequeueInputBuffer = i2;
                                                }
                                                i2 = dequeueInputBuffer;
                                            }
                                        }
                                        if ((bufferInfo2.flags & 4) != 0) {
                                            Log.d(Constants.TAG, "video decoder: EOS");
                                            this.mOutputVideoEncoder.signalEndOfInputStream();
                                            i6 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "video decoder: codec config buffer");
                                        this.mInputVideoDecoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                    }
                                } else {
                                    Log.d(Constants.TAG, "video decoder: codec info format changed" + this.mInputVideoDecoder.getOutputFormat());
                                }
                            }
                        }
                        if (!this.mUserStop && obj4 == null) {
                            if (mediaFormat3 == null || this.mMuxerStarted) {
                                dequeueInputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                                if (dequeueInputBuffer == -1) {
                                    Log.d(Constants.TAG, "no video encoder output buffer");
                                } else if (dequeueInputBuffer == -3) {
                                    Log.d(Constants.TAG, "video encoder: output buffers changed");
                                    outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                                } else if (dequeueInputBuffer == -2) {
                                    Log.d(Constants.TAG, "video encoder: output format changed " + this.mOutputVideoEncoder.getOutputFormat());
                                    if (this.mVideoTrackIndex < 0) {
                                        mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                                    } else {
                                        throw new RuntimeException("Video encoder output format changed after muxer has started");
                                    }
                                } else if (dequeueInputBuffer >= 0) {
                                    byteBuffer = outputBuffers[dequeueInputBuffer];
                                    if ((bufferInfo.flags & 2) == 0) {
                                        if (bufferInfo.size != 0) {
                                            Log.d(Constants.TAG, "video encoder: writing sample data timestamp " + bufferInfo.presentationTimeUs);
                                            this.mMuxer.writeSampleData(this.mVideoTrackIndex, byteBuffer, bufferInfo);
                                        }
                                        if ((bufferInfo.flags & 4) == 0) {
                                            obj9 = obj4;
                                        } else {
                                            Log.d(Constants.TAG, "video encoder: EOS");
                                            obj9 = 1;
                                        }
                                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                        obj4 = obj9;
                                    } else {
                                        Log.d(Constants.TAG, "video encoder: codec config buffer");
                                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                                }
                            }
                        }
                        if (this.mCopyAudio) {
                            obj9 = obj7;
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            if (!this.mUserStop && r21 == null) {
                                if (mediaFormat2 == null || this.mMuxerStarted) {
                                    dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                        sampleTime = this.mAudioExtractor.getSampleTime();
                                        if (sampleTime > this.mTrimAudioEndUs) {
                                        }
                                        if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                            if (readSampleData > 0) {
                                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                            }
                                            if (this.mAudioExtractor.advance()) {
                                            }
                                            obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                        } else {
                                            obj8 = 1;
                                        }
                                        if (obj8 != null) {
                                            Log.d(Constants.TAG, "audio decoder sending EOS");
                                            this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder input try again later");
                                    }
                                }
                            }
                            if (!this.mUserStop && obj7 == null && r22 == -1) {
                                if (mediaFormat2 == null || this.mMuxerStarted) {
                                    i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                    if (i5 == -1) {
                                        Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                    } else if (i5 == -3) {
                                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                    } else if (i5 == -2) {
                                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                    } else if (i5 < 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                    } else if ((bufferInfo4.flags & 2) == 0) {
                                        i = i5;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                        this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                    }
                                }
                            }
                            if (this.mUserStop && i != -1) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        } else {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            } else {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            }
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        obj9 = obj7;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                }
                            } else {
                                obj9 = obj7;
                            }
                            if (!this.mUserStop && obj6 == null) {
                                if (mediaFormat2 == null || this.mMuxerStarted) {
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (dequeueOutputBuffer >= 0) {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size == 0) {
                                                    j2 = j;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj11 = obj6;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                            }
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        if (!(this.mUserStop || this.mMuxerStarted || mediaFormat3 == null)) {
                            if (!this.mCopyAudio || mediaFormat != null) {
                                this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                                if (this.mCopyAudio) {
                                    this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                                }
                                this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                                this.mMuxer.start();
                                this.mMuxerStarted = true;
                            }
                        }
                        if (this.mUserStop) {
                            obj7 = obj9;
                            mediaFormat2 = mediaFormat;
                            byteBufferArr2 = byteBufferArr3;
                            obj9 = obj10;
                        } else {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        }
                    }
                }
                obj10 = obj9;
                while (!this.mUserStop) {
                    if (mediaFormat3 == null) {
                        if (mediaFormat3 == null) {
                            if (this.mCopyAudio) {
                                if (mediaFormat2 == null) {
                                    if (mediaFormat2 == null) {
                                        if (this.mUserStop) {
                                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                            if (dequeueInputBuffer != -1) {
                                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                                obj9 = obj7;
                                            } else {
                                                buffer = inputBuffers2[dequeueInputBuffer];
                                                readSampleData = bufferInfo4.size;
                                                sampleTime = bufferInfo4.presentationTimeUs;
                                                if (readSampleData >= 0) {
                                                    duplicate = byteBufferArr[i].duplicate();
                                                    duplicate.position(bufferInfo4.offset);
                                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                                    if (this.mOriginalAudioChannelCount <= 0) {
                                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                        if (allocateDirect == null) {
                                                            allocateDirect.position(0);
                                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                            }
                                                            buffer.position(0);
                                                            buffer.put(allocateDirect);
                                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                            allocateDirect.clear();
                                                        } else {
                                                            Log.e(Constants.TAG, "TempAudio is null!");
                                                        }
                                                    } else {
                                                        buffer.position(0);
                                                        buffer.put(duplicate);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                    }
                                                }
                                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                                i = -1;
                                                if ((bufferInfo4.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                                    obj9 = 1;
                                                } else {
                                                    obj9 = obj7;
                                                }
                                            }
                                            if (mediaFormat2 == null) {
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                            if (dequeueOutputBuffer == -1) {
                                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else if (dequeueOutputBuffer == -3) {
                                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                                mediaFormat = mediaFormat2;
                                            } else if (dequeueOutputBuffer != -2) {
                                                if (this.mAudioTrackIndex < 0) {
                                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                                }
                                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                                byteBufferArr3 = byteBufferArr2;
                                            } else if (dequeueOutputBuffer >= 0) {
                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                if ((bufferInfo3.flags & 2) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                    mediaFormat = mediaFormat2;
                                                    byteBufferArr3 = byteBufferArr2;
                                                } else {
                                                    if (bufferInfo3.size == 0) {
                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                        }
                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                            j2 = bufferInfo3.presentationTimeUs;
                                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                        } else {
                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                        }
                                                    }
                                                    j2 = j;
                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                        obj11 = 1;
                                                    } else {
                                                        obj11 = obj6;
                                                    }
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                    j = j2;
                                                    obj6 = obj11;
                                                    mediaFormat = mediaFormat2;
                                                    byteBufferArr3 = byteBufferArr2;
                                                }
                                            }
                                        }
                                        obj9 = obj7;
                                        if (mediaFormat2 == null) {
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (dequeueOutputBuffer == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat = mediaFormat2;
                                        } else if (dequeueOutputBuffer != -2) {
                                            if (dequeueOutputBuffer >= 0) {
                                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                                if ((bufferInfo3.flags & 2) == 0) {
                                                    if (bufferInfo3.size == 0) {
                                                        j2 = j;
                                                    } else {
                                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                        if (j <= bufferInfo3.presentationTimeUs) {
                                                        }
                                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                            throw new IOException("Audio time stamps are not in increasing order.");
                                                        }
                                                        j2 = bufferInfo3.presentationTimeUs;
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                    }
                                                    if ((bufferInfo3.flags & 4) == 0) {
                                                        obj11 = obj6;
                                                    } else {
                                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                                        obj11 = 1;
                                                    }
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                    j = j2;
                                                    obj6 = obj11;
                                                    mediaFormat = mediaFormat2;
                                                    byteBufferArr3 = byteBufferArr2;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                    mediaFormat = mediaFormat2;
                                                    byteBufferArr3 = byteBufferArr2;
                                                }
                                            } else {
                                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else if (this.mAudioTrackIndex < 0) {
                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                    }
                                    i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                    if (i5 == -1) {
                                        Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                    } else if (i5 == -3) {
                                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                    } else if (i5 == -2) {
                                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                    } else if (i5 < 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                    } else if ((bufferInfo4.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                        this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                    } else {
                                        i = i5;
                                    }
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            buffer = inputBuffers2[dequeueInputBuffer];
                                            readSampleData = bufferInfo4.size;
                                            sampleTime = bufferInfo4.presentationTimeUs;
                                            if (readSampleData >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo4.offset);
                                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                                if (this.mOriginalAudioChannelCount <= 0) {
                                                    buffer.position(0);
                                                    buffer.put(duplicate);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                } else {
                                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                    if (allocateDirect == null) {
                                                        Log.e(Constants.TAG, "TempAudio is null!");
                                                    } else {
                                                        allocateDirect.position(0);
                                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                        }
                                                        buffer.position(0);
                                                        buffer.put(allocateDirect);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                        allocateDirect.clear();
                                                    }
                                                }
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo4.flags & 4) == 0) {
                                                obj9 = obj7;
                                            } else {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj9 = 1;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj9 = obj7;
                                        }
                                        if (mediaFormat2 == null) {
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (dequeueOutputBuffer == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat = mediaFormat2;
                                        } else if (dequeueOutputBuffer != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size == 0) {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        j2 = bufferInfo3.presentationTimeUs;
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                    } else {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                }
                                                j2 = j;
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                } else {
                                                    obj11 = obj6;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        }
                                    }
                                    obj9 = obj7;
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (dequeueOutputBuffer >= 0) {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size == 0) {
                                                    j2 = j;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj11 = obj6;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio decoder input try again later");
                                } else {
                                    readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                    sampleTime = this.mAudioExtractor.getSampleTime();
                                    if (sampleTime > this.mTrimAudioEndUs) {
                                    }
                                    if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                        obj8 = 1;
                                    } else {
                                        if (readSampleData > 0) {
                                            this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                        }
                                        if (this.mAudioExtractor.advance()) {
                                        }
                                        obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                    }
                                    if (obj8 != null) {
                                        Log.d(Constants.TAG, "audio decoder sending EOS");
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                    }
                                }
                                if (mediaFormat2 == null) {
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj9 = obj7;
                                        } else {
                                            buffer = inputBuffers2[dequeueInputBuffer];
                                            readSampleData = bufferInfo4.size;
                                            sampleTime = bufferInfo4.presentationTimeUs;
                                            if (readSampleData >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo4.offset);
                                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                                if (this.mOriginalAudioChannelCount <= 0) {
                                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                    if (allocateDirect == null) {
                                                        allocateDirect.position(0);
                                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                        }
                                                        buffer.position(0);
                                                        buffer.put(allocateDirect);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                        allocateDirect.clear();
                                                    } else {
                                                        Log.e(Constants.TAG, "TempAudio is null!");
                                                    }
                                                } else {
                                                    buffer.position(0);
                                                    buffer.put(duplicate);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                }
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo4.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj9 = 1;
                                            } else {
                                                obj9 = obj7;
                                            }
                                        }
                                        if (mediaFormat2 == null) {
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (dequeueOutputBuffer == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat = mediaFormat2;
                                        } else if (dequeueOutputBuffer != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size == 0) {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        j2 = bufferInfo3.presentationTimeUs;
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                    } else {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                }
                                                j2 = j;
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                } else {
                                                    obj11 = obj6;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        }
                                    }
                                    obj9 = obj7;
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (dequeueOutputBuffer >= 0) {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size == 0) {
                                                    j2 = j;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj11 = obj6;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                if (i5 == -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                } else if (i5 == -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                    byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                } else if (i5 == -2) {
                                    Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                } else if (i5 < 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo4.flags & 2) == 0) {
                                    i = i5;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                }
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            } else {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                } else {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                }
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            obj9 = obj7;
                                        } else {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            obj9 = obj7;
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                            if (!this.mCopyAudio) {
                                if (this.mUserStop) {
                                    Log.d(Constants.TAG, "Encoding abruptly stopped.");
                                } else {
                                    obj7 = obj9;
                                    mediaFormat2 = mediaFormat;
                                    byteBufferArr2 = byteBufferArr3;
                                    obj9 = obj10;
                                }
                            }
                            this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                            if (this.mCopyAudio) {
                                this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                            }
                            this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                            this.mMuxer.start();
                            this.mMuxerStarted = true;
                            if (this.mUserStop) {
                                obj7 = obj9;
                                mediaFormat2 = mediaFormat;
                                byteBufferArr2 = byteBufferArr3;
                                obj9 = obj10;
                            } else {
                                Log.d(Constants.TAG, "Encoding abruptly stopped.");
                            }
                        }
                        dequeueInputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                        if (dequeueInputBuffer == -1) {
                            Log.d(Constants.TAG, "no video encoder output buffer");
                        } else if (dequeueInputBuffer == -3) {
                            Log.d(Constants.TAG, "video encoder: output buffers changed");
                            outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                        } else if (dequeueInputBuffer == -2) {
                            Log.d(Constants.TAG, "video encoder: output format changed " + this.mOutputVideoEncoder.getOutputFormat());
                            if (this.mVideoTrackIndex < 0) {
                                throw new RuntimeException("Video encoder output format changed after muxer has started");
                            }
                            mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                        } else if (dequeueInputBuffer >= 0) {
                            Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                        } else {
                            byteBuffer = outputBuffers[dequeueInputBuffer];
                            if ((bufferInfo.flags & 2) == 0) {
                                Log.d(Constants.TAG, "video encoder: codec config buffer");
                                this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                            } else {
                                if (bufferInfo.size != 0) {
                                    Log.d(Constants.TAG, "video encoder: writing sample data timestamp " + bufferInfo.presentationTimeUs);
                                    this.mMuxer.writeSampleData(this.mVideoTrackIndex, byteBuffer, bufferInfo);
                                }
                                if ((bufferInfo.flags & 4) == 0) {
                                    Log.d(Constants.TAG, "video encoder: EOS");
                                    obj9 = 1;
                                } else {
                                    obj9 = obj4;
                                }
                                this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                                obj4 = obj9;
                            }
                        }
                        if (this.mCopyAudio) {
                            obj9 = obj7;
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            if (mediaFormat2 == null) {
                                if (mediaFormat2 == null) {
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj9 = obj7;
                                        } else {
                                            buffer = inputBuffers2[dequeueInputBuffer];
                                            readSampleData = bufferInfo4.size;
                                            sampleTime = bufferInfo4.presentationTimeUs;
                                            if (readSampleData >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo4.offset);
                                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                                if (this.mOriginalAudioChannelCount <= 0) {
                                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                    if (allocateDirect == null) {
                                                        allocateDirect.position(0);
                                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                        }
                                                        buffer.position(0);
                                                        buffer.put(allocateDirect);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                        allocateDirect.clear();
                                                    } else {
                                                        Log.e(Constants.TAG, "TempAudio is null!");
                                                    }
                                                } else {
                                                    buffer.position(0);
                                                    buffer.put(duplicate);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                }
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo4.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj9 = 1;
                                            } else {
                                                obj9 = obj7;
                                            }
                                        }
                                        if (mediaFormat2 == null) {
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (dequeueOutputBuffer == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat = mediaFormat2;
                                        } else if (dequeueOutputBuffer != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size == 0) {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        j2 = bufferInfo3.presentationTimeUs;
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                    } else {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                }
                                                j2 = j;
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                } else {
                                                    obj11 = obj6;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        }
                                    }
                                    obj9 = obj7;
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (dequeueOutputBuffer >= 0) {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size == 0) {
                                                    j2 = j;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj11 = obj6;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                if (i5 == -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                } else if (i5 == -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                    byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                } else if (i5 == -2) {
                                    Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                } else if (i5 < 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo4.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                } else {
                                    i = i5;
                                }
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            } else {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                } else {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                }
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            obj9 = obj7;
                                        } else {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                sampleTime = this.mAudioExtractor.getSampleTime();
                                if (sampleTime > this.mTrimAudioEndUs) {
                                }
                                if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                    if (readSampleData > 0) {
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                    }
                                    if (this.mAudioExtractor.advance()) {
                                    }
                                    obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                } else {
                                    obj8 = 1;
                                }
                                if (obj8 != null) {
                                    Log.d(Constants.TAG, "audio decoder sending EOS");
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                }
                            } else {
                                Log.d(Constants.TAG, "audio decoder input try again later");
                            }
                            if (mediaFormat2 == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    } else {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                } else {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                }
                                            } else {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        } else {
                                            obj9 = obj7;
                                        }
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                            if (i5 == -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                            } else if (i5 == -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i5 == -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i5 < 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo4.flags & 2) == 0) {
                                i = i5;
                            } else {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        } else {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            } else {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            }
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        obj9 = obj7;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        if (!this.mCopyAudio) {
                            if (this.mUserStop) {
                                Log.d(Constants.TAG, "Encoding abruptly stopped.");
                            } else {
                                obj7 = obj9;
                                mediaFormat2 = mediaFormat;
                                byteBufferArr2 = byteBufferArr3;
                                obj9 = obj10;
                            }
                        }
                        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                        if (this.mCopyAudio) {
                            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                        }
                        this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                        this.mMuxer.start();
                        this.mMuxerStarted = true;
                        if (this.mUserStop) {
                            obj7 = obj9;
                            mediaFormat2 = mediaFormat;
                            byteBufferArr2 = byteBufferArr3;
                            obj9 = obj10;
                        } else {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        }
                    }
                    dequeueInputBuffer = this.mInputVideoDecoder.dequeueOutputBuffer(bufferInfo2, TIMEOUT_USEC);
                    if (dequeueInputBuffer != -1) {
                        Log.d(Constants.TAG, "no video decoder output buffer");
                    } else if (dequeueInputBuffer != -3) {
                        Log.d(Constants.TAG, "video decoder: output buffers changed");
                        outputBuffers2 = this.mInputVideoDecoder.getOutputBuffers();
                    } else if (dequeueInputBuffer == -2) {
                        Log.d(Constants.TAG, "video decoder: codec info format changed" + this.mInputVideoDecoder.getOutputFormat());
                    } else {
                        byteBuffer = outputBuffers2[dequeueInputBuffer];
                        if ((bufferInfo2.flags & 2) != 0) {
                            Log.d(Constants.TAG, "video decoder: codec config buffer");
                            this.mInputVideoDecoder.releaseOutputBuffer(dequeueInputBuffer, false);
                        } else {
                            Log.d(Constants.TAG, "video decoder: returned buffer for time " + bufferInfo2.presentationTimeUs);
                            if (bufferInfo2.size == 0) {
                            }
                            this.mInputVideoDecoder.releaseOutputBuffer(dequeueInputBuffer, z);
                            if (!z) {
                                Log.d(Constants.TAG, "output surface: await new image");
                                if (this.mOutputSurface.checkForNewImage(1000)) {
                                    Log.d(Constants.TAG, "video decoder: checkForNewImage return false!!  mUserStop : " + this.mUserStop);
                                } else {
                                    Log.d(Constants.TAG, "output surface: draw image");
                                    GLES20.glClear(16384);
                                    this.mOutputSurface.drawImage();
                                    if (this.mLogoPresent) {
                                        if (i2 % i4 < this.mOutputVideoFrameRate) {
                                            this.mLogoRenderer.draw();
                                        }
                                    }
                                    if (bufferInfo2.presentationTimeUs >= this.mTrimVideoStartUs) {
                                    }
                                    if ((bufferInfo2.presentationTimeUs >= this.mTrimVideoStartUs ? 1 : null) != null) {
                                        dequeueInputBuffer = i2;
                                    } else {
                                        if (this.mSkipFrames) {
                                            i5 = i3;
                                            dequeueInputBuffer = i2;
                                            i3 = i5 + 1;
                                        }
                                        i5 = 0;
                                        this.mInputSurface.setPresentationTime(bufferInfo2.presentationTimeUs * 1000);
                                        Log.d(Constants.TAG, "input surface: swap buffers");
                                        this.mInputSurface.swapBuffers();
                                        Log.d(Constants.TAG, "video encoder: notified of new frame");
                                        dequeueInputBuffer = i2 + 1;
                                        i3 = i5 + 1;
                                    }
                                    i2 = dequeueInputBuffer;
                                }
                            }
                            if ((bufferInfo2.flags & 4) != 0) {
                                Log.d(Constants.TAG, "video decoder: EOS");
                                this.mOutputVideoEncoder.signalEndOfInputStream();
                                i6 = 1;
                            }
                        }
                    }
                    if (mediaFormat3 == null) {
                        if (this.mCopyAudio) {
                            if (mediaFormat2 == null) {
                                if (mediaFormat2 == null) {
                                    if (this.mUserStop) {
                                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                        if (dequeueInputBuffer != -1) {
                                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                            obj9 = obj7;
                                        } else {
                                            buffer = inputBuffers2[dequeueInputBuffer];
                                            readSampleData = bufferInfo4.size;
                                            sampleTime = bufferInfo4.presentationTimeUs;
                                            if (readSampleData >= 0) {
                                                duplicate = byteBufferArr[i].duplicate();
                                                duplicate.position(bufferInfo4.offset);
                                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                                if (this.mOriginalAudioChannelCount <= 0) {
                                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                    if (allocateDirect == null) {
                                                        allocateDirect.position(0);
                                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                        }
                                                        buffer.position(0);
                                                        buffer.put(allocateDirect);
                                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                        allocateDirect.clear();
                                                    } else {
                                                        Log.e(Constants.TAG, "TempAudio is null!");
                                                    }
                                                } else {
                                                    buffer.position(0);
                                                    buffer.put(duplicate);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                                }
                                            }
                                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                            i = -1;
                                            if ((bufferInfo4.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio decoder: EOS");
                                                obj9 = 1;
                                            } else {
                                                obj9 = obj7;
                                            }
                                        }
                                        if (mediaFormat2 == null) {
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                        if (dequeueOutputBuffer == -1) {
                                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer == -3) {
                                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                            mediaFormat = mediaFormat2;
                                        } else if (dequeueOutputBuffer != -2) {
                                            if (this.mAudioTrackIndex < 0) {
                                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                            }
                                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                            byteBufferArr3 = byteBufferArr2;
                                        } else if (dequeueOutputBuffer >= 0) {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                if (bufferInfo3.size == 0) {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        j2 = bufferInfo3.presentationTimeUs;
                                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                    } else {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                }
                                                j2 = j;
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                } else {
                                                    obj11 = obj6;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        }
                                    }
                                    obj9 = obj7;
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (dequeueOutputBuffer >= 0) {
                                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                            if ((bufferInfo3.flags & 2) == 0) {
                                                if (bufferInfo3.size == 0) {
                                                    j2 = j;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                    if (j <= bufferInfo3.presentationTimeUs) {
                                                    }
                                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                        throw new IOException("Audio time stamps are not in increasing order.");
                                                    }
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                }
                                                if ((bufferInfo3.flags & 4) == 0) {
                                                    obj11 = obj6;
                                                } else {
                                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                                    obj11 = 1;
                                                }
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                j = j2;
                                                obj6 = obj11;
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                                mediaFormat = mediaFormat2;
                                                byteBufferArr3 = byteBufferArr2;
                                            }
                                        } else {
                                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else if (this.mAudioTrackIndex < 0) {
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                }
                                i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                                if (i5 == -1) {
                                    Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                                } else if (i5 == -3) {
                                    Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                    byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                                } else if (i5 == -2) {
                                    Log.d(Constants.TAG, "audio decoder: output format changed: ");
                                } else if (i5 < 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                                } else if ((bufferInfo4.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                    this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                                } else {
                                    i = i5;
                                }
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            } else {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                } else {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                }
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            obj9 = obj7;
                                        } else {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                Log.d(Constants.TAG, "audio decoder input try again later");
                            } else {
                                readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                                sampleTime = this.mAudioExtractor.getSampleTime();
                                if (sampleTime > this.mTrimAudioEndUs) {
                                }
                                if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                    obj8 = 1;
                                } else {
                                    if (readSampleData > 0) {
                                        this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                    }
                                    if (this.mAudioExtractor.advance()) {
                                    }
                                    obj8 = this.mAudioExtractor.advance() ? 1 : null;
                                }
                                if (obj8 != null) {
                                    Log.d(Constants.TAG, "audio decoder sending EOS");
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                                }
                            }
                            if (mediaFormat2 == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    } else {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                } else {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                }
                                            } else {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        } else {
                                            obj9 = obj7;
                                        }
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                            if (i5 == -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                            } else if (i5 == -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i5 == -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i5 < 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo4.flags & 2) == 0) {
                                i = i5;
                            } else {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        } else {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            } else {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            }
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        obj9 = obj7;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        obj9 = obj7;
                        mediaFormat = mediaFormat2;
                        byteBufferArr3 = byteBufferArr2;
                        if (!this.mCopyAudio) {
                            if (this.mUserStop) {
                                Log.d(Constants.TAG, "Encoding abruptly stopped.");
                            } else {
                                obj7 = obj9;
                                mediaFormat2 = mediaFormat;
                                byteBufferArr2 = byteBufferArr3;
                                obj9 = obj10;
                            }
                        }
                        this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                        if (this.mCopyAudio) {
                            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                        }
                        this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                        this.mMuxer.start();
                        this.mMuxerStarted = true;
                        if (this.mUserStop) {
                            obj7 = obj9;
                            mediaFormat2 = mediaFormat;
                            byteBufferArr2 = byteBufferArr3;
                            obj9 = obj10;
                        } else {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        }
                    }
                    dequeueInputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                    if (dequeueInputBuffer == -1) {
                        Log.d(Constants.TAG, "no video encoder output buffer");
                    } else if (dequeueInputBuffer == -3) {
                        Log.d(Constants.TAG, "video encoder: output buffers changed");
                        outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                    } else if (dequeueInputBuffer == -2) {
                        Log.d(Constants.TAG, "video encoder: output format changed " + this.mOutputVideoEncoder.getOutputFormat());
                        if (this.mVideoTrackIndex < 0) {
                            mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                        } else {
                            throw new RuntimeException("Video encoder output format changed after muxer has started");
                        }
                    } else if (dequeueInputBuffer >= 0) {
                        byteBuffer = outputBuffers[dequeueInputBuffer];
                        if ((bufferInfo.flags & 2) == 0) {
                            if (bufferInfo.size != 0) {
                                Log.d(Constants.TAG, "video encoder: writing sample data timestamp " + bufferInfo.presentationTimeUs);
                                this.mMuxer.writeSampleData(this.mVideoTrackIndex, byteBuffer, bufferInfo);
                            }
                            if ((bufferInfo.flags & 4) == 0) {
                                obj9 = obj4;
                            } else {
                                Log.d(Constants.TAG, "video encoder: EOS");
                                obj9 = 1;
                            }
                            this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                            obj4 = obj9;
                        } else {
                            Log.d(Constants.TAG, "video encoder: codec config buffer");
                            this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                        }
                    } else {
                        Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                    }
                    if (this.mCopyAudio) {
                        obj9 = obj7;
                        mediaFormat = mediaFormat2;
                        byteBufferArr3 = byteBufferArr2;
                    } else {
                        if (mediaFormat2 == null) {
                            if (mediaFormat2 == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    } else {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                } else {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                }
                                            } else {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        } else {
                                            obj9 = obj7;
                                        }
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                            if (i5 == -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                            } else if (i5 == -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i5 == -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i5 < 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo4.flags & 2) == 0) {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                            } else {
                                i = i5;
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        } else {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            } else {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            }
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        obj9 = obj7;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                            sampleTime = this.mAudioExtractor.getSampleTime();
                            if (sampleTime > this.mTrimAudioEndUs) {
                            }
                            if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                if (readSampleData > 0) {
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                }
                                if (this.mAudioExtractor.advance()) {
                                }
                                obj8 = this.mAudioExtractor.advance() ? 1 : null;
                            } else {
                                obj8 = 1;
                            }
                            if (obj8 != null) {
                                Log.d(Constants.TAG, "audio decoder sending EOS");
                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            }
                        } else {
                            Log.d(Constants.TAG, "audio decoder input try again later");
                        }
                        if (mediaFormat2 == null) {
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                } else {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            } else {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            }
                                        } else {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    } else {
                                        obj9 = obj7;
                                    }
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                        if (i5 == -1) {
                            Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                        } else if (i5 == -3) {
                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                        } else if (i5 == -2) {
                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                        } else if (i5 < 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                        } else if ((bufferInfo4.flags & 2) == 0) {
                            i = i5;
                        } else {
                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                            this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                        }
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                buffer = inputBuffers2[dequeueInputBuffer];
                                readSampleData = bufferInfo4.size;
                                sampleTime = bufferInfo4.presentationTimeUs;
                                if (readSampleData >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo4.offset);
                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                    if (this.mOriginalAudioChannelCount <= 0) {
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                    } else {
                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                        if (allocateDirect == null) {
                                            Log.e(Constants.TAG, "TempAudio is null!");
                                        } else {
                                            allocateDirect.position(0);
                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                            }
                                            buffer.position(0);
                                            buffer.put(allocateDirect);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                            allocateDirect.clear();
                                        }
                                    }
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo4.flags & 4) == 0) {
                                    obj9 = obj7;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj9 = 1;
                                }
                            } else {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj9 = obj7;
                            }
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size == 0) {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        } else {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                    }
                                    j2 = j;
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    } else {
                                        obj11 = obj6;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            }
                        }
                        obj9 = obj7;
                        if (mediaFormat2 == null) {
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat = mediaFormat2;
                        } else if (dequeueOutputBuffer != -2) {
                            if (dequeueOutputBuffer >= 0) {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size == 0) {
                                        j2 = j;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                        j2 = bufferInfo3.presentationTimeUs;
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj11 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    if (!this.mCopyAudio) {
                        if (this.mUserStop) {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        } else {
                            obj7 = obj9;
                            mediaFormat2 = mediaFormat;
                            byteBufferArr2 = byteBufferArr3;
                            obj9 = obj10;
                        }
                    }
                    this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                    if (this.mCopyAudio) {
                        this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                    }
                    this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                    this.mMuxer.start();
                    this.mMuxerStarted = true;
                    if (this.mUserStop) {
                        obj7 = obj9;
                        mediaFormat2 = mediaFormat;
                        byteBufferArr2 = byteBufferArr3;
                        obj9 = obj10;
                    } else {
                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                    }
                }
                if (mediaFormat3 == null) {
                    if (this.mCopyAudio) {
                        if (mediaFormat2 == null) {
                            if (mediaFormat2 == null) {
                                if (this.mUserStop) {
                                    dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                    if (dequeueInputBuffer != -1) {
                                        Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                        obj9 = obj7;
                                    } else {
                                        buffer = inputBuffers2[dequeueInputBuffer];
                                        readSampleData = bufferInfo4.size;
                                        sampleTime = bufferInfo4.presentationTimeUs;
                                        if (readSampleData >= 0) {
                                            duplicate = byteBufferArr[i].duplicate();
                                            duplicate.position(bufferInfo4.offset);
                                            duplicate.limit(bufferInfo4.offset + readSampleData);
                                            if (this.mOriginalAudioChannelCount <= 0) {
                                                allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                                if (allocateDirect == null) {
                                                    allocateDirect.position(0);
                                                    allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                    for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                        allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                        allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                    }
                                                    buffer.position(0);
                                                    buffer.put(allocateDirect);
                                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                    allocateDirect.clear();
                                                } else {
                                                    Log.e(Constants.TAG, "TempAudio is null!");
                                                }
                                            } else {
                                                buffer.position(0);
                                                buffer.put(duplicate);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                            }
                                        }
                                        this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                        i = -1;
                                        if ((bufferInfo4.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio decoder: EOS");
                                            obj9 = 1;
                                        } else {
                                            obj9 = obj7;
                                        }
                                    }
                                    if (mediaFormat2 == null) {
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                    if (dequeueOutputBuffer == -1) {
                                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer == -3) {
                                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                        mediaFormat = mediaFormat2;
                                    } else if (dequeueOutputBuffer != -2) {
                                        if (this.mAudioTrackIndex < 0) {
                                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                        }
                                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                        byteBufferArr3 = byteBufferArr2;
                                    } else if (dequeueOutputBuffer >= 0) {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            if (bufferInfo3.size == 0) {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    j2 = bufferInfo3.presentationTimeUs;
                                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                                } else {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                            }
                                            j2 = j;
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            } else {
                                                obj11 = obj6;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    }
                                }
                                obj9 = obj7;
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (dequeueOutputBuffer >= 0) {
                                        byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                        if ((bufferInfo3.flags & 2) == 0) {
                                            if (bufferInfo3.size == 0) {
                                                j2 = j;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                                if (j <= bufferInfo3.presentationTimeUs) {
                                                }
                                                if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                    throw new IOException("Audio time stamps are not in increasing order.");
                                                }
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            }
                                            if ((bufferInfo3.flags & 4) == 0) {
                                                obj11 = obj6;
                                            } else {
                                                Log.d(Constants.TAG, "audio encoder: EOS");
                                                obj11 = 1;
                                            }
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            j = j2;
                                            obj6 = obj11;
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                            this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                            mediaFormat = mediaFormat2;
                                            byteBufferArr3 = byteBufferArr2;
                                        }
                                    } else {
                                        Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else if (this.mAudioTrackIndex < 0) {
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                            }
                            i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                            if (i5 == -1) {
                                Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                            } else if (i5 == -3) {
                                Log.d(Constants.TAG, "audio decoder: output buffers changed");
                                byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                            } else if (i5 == -2) {
                                Log.d(Constants.TAG, "audio decoder: output format changed: ");
                            } else if (i5 < 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                            } else if ((bufferInfo4.flags & 2) == 0) {
                                Log.d(Constants.TAG, "audio decoder: codec config buffer");
                                this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                            } else {
                                i = i5;
                            }
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        } else {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            } else {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            }
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        obj9 = obj7;
                                    } else {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            Log.d(Constants.TAG, "audio decoder input try again later");
                        } else {
                            readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                            sampleTime = this.mAudioExtractor.getSampleTime();
                            if (sampleTime > this.mTrimAudioEndUs) {
                            }
                            if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                                obj8 = 1;
                            } else {
                                if (readSampleData > 0) {
                                    this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                                }
                                if (this.mAudioExtractor.advance()) {
                                }
                                obj8 = this.mAudioExtractor.advance() ? 1 : null;
                            }
                            if (obj8 != null) {
                                Log.d(Constants.TAG, "audio decoder sending EOS");
                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                            }
                        }
                        if (mediaFormat2 == null) {
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                } else {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            } else {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            }
                                        } else {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    } else {
                                        obj9 = obj7;
                                    }
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                        if (i5 == -1) {
                            Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                        } else if (i5 == -3) {
                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                        } else if (i5 == -2) {
                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                        } else if (i5 < 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                        } else if ((bufferInfo4.flags & 2) == 0) {
                            i = i5;
                        } else {
                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                            this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                        }
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                buffer = inputBuffers2[dequeueInputBuffer];
                                readSampleData = bufferInfo4.size;
                                sampleTime = bufferInfo4.presentationTimeUs;
                                if (readSampleData >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo4.offset);
                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                    if (this.mOriginalAudioChannelCount <= 0) {
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                    } else {
                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                        if (allocateDirect == null) {
                                            Log.e(Constants.TAG, "TempAudio is null!");
                                        } else {
                                            allocateDirect.position(0);
                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                            }
                                            buffer.position(0);
                                            buffer.put(allocateDirect);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                            allocateDirect.clear();
                                        }
                                    }
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo4.flags & 4) == 0) {
                                    obj9 = obj7;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj9 = 1;
                                }
                            } else {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj9 = obj7;
                            }
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size == 0) {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        } else {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                    }
                                    j2 = j;
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    } else {
                                        obj11 = obj6;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            }
                        }
                        obj9 = obj7;
                        if (mediaFormat2 == null) {
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat = mediaFormat2;
                        } else if (dequeueOutputBuffer != -2) {
                            if (dequeueOutputBuffer >= 0) {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size == 0) {
                                        j2 = j;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                        j2 = bufferInfo3.presentationTimeUs;
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj11 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    obj9 = obj7;
                    mediaFormat = mediaFormat2;
                    byteBufferArr3 = byteBufferArr2;
                    if (!this.mCopyAudio) {
                        if (this.mUserStop) {
                            Log.d(Constants.TAG, "Encoding abruptly stopped.");
                        } else {
                            obj7 = obj9;
                            mediaFormat2 = mediaFormat;
                            byteBufferArr2 = byteBufferArr3;
                            obj9 = obj10;
                        }
                    }
                    this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                    if (this.mCopyAudio) {
                        this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                    }
                    this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                    this.mMuxer.start();
                    this.mMuxerStarted = true;
                    if (this.mUserStop) {
                        obj7 = obj9;
                        mediaFormat2 = mediaFormat;
                        byteBufferArr2 = byteBufferArr3;
                        obj9 = obj10;
                    } else {
                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                    }
                }
                dequeueInputBuffer = this.mOutputVideoEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                if (dequeueInputBuffer == -1) {
                    Log.d(Constants.TAG, "no video encoder output buffer");
                } else if (dequeueInputBuffer == -3) {
                    Log.d(Constants.TAG, "video encoder: output buffers changed");
                    outputBuffers = this.mOutputVideoEncoder.getOutputBuffers();
                } else if (dequeueInputBuffer == -2) {
                    Log.d(Constants.TAG, "video encoder: output format changed " + this.mOutputVideoEncoder.getOutputFormat());
                    if (this.mVideoTrackIndex < 0) {
                        throw new RuntimeException("Video encoder output format changed after muxer has started");
                    }
                    mediaFormat3 = this.mOutputVideoEncoder.getOutputFormat();
                } else if (dequeueInputBuffer >= 0) {
                    Log.d(Constants.TAG, "Unexpected result from video encoder dequeue output format.");
                } else {
                    byteBuffer = outputBuffers[dequeueInputBuffer];
                    if ((bufferInfo.flags & 2) == 0) {
                        Log.d(Constants.TAG, "video encoder: codec config buffer");
                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                    } else {
                        if (bufferInfo.size != 0) {
                            Log.d(Constants.TAG, "video encoder: writing sample data timestamp " + bufferInfo.presentationTimeUs);
                            this.mMuxer.writeSampleData(this.mVideoTrackIndex, byteBuffer, bufferInfo);
                        }
                        if ((bufferInfo.flags & 4) == 0) {
                            Log.d(Constants.TAG, "video encoder: EOS");
                            obj9 = 1;
                        } else {
                            obj9 = obj4;
                        }
                        this.mOutputVideoEncoder.releaseOutputBuffer(dequeueInputBuffer, false);
                        obj4 = obj9;
                    }
                }
                if (this.mCopyAudio) {
                    obj9 = obj7;
                    mediaFormat = mediaFormat2;
                    byteBufferArr3 = byteBufferArr2;
                } else {
                    if (mediaFormat2 == null) {
                        if (mediaFormat2 == null) {
                            if (this.mUserStop) {
                                dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                                if (dequeueInputBuffer != -1) {
                                    Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                    obj9 = obj7;
                                } else {
                                    buffer = inputBuffers2[dequeueInputBuffer];
                                    readSampleData = bufferInfo4.size;
                                    sampleTime = bufferInfo4.presentationTimeUs;
                                    if (readSampleData >= 0) {
                                        duplicate = byteBufferArr[i].duplicate();
                                        duplicate.position(bufferInfo4.offset);
                                        duplicate.limit(bufferInfo4.offset + readSampleData);
                                        if (this.mOriginalAudioChannelCount <= 0) {
                                            allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                            if (allocateDirect == null) {
                                                allocateDirect.position(0);
                                                allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                                for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                    allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                    allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                                }
                                                buffer.position(0);
                                                buffer.put(allocateDirect);
                                                this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                                allocateDirect.clear();
                                            } else {
                                                Log.e(Constants.TAG, "TempAudio is null!");
                                            }
                                        } else {
                                            buffer.position(0);
                                            buffer.put(duplicate);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                        }
                                    }
                                    this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                    i = -1;
                                    if ((bufferInfo4.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio decoder: EOS");
                                        obj9 = 1;
                                    } else {
                                        obj9 = obj7;
                                    }
                                }
                                if (mediaFormat2 == null) {
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                                dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                                if (dequeueOutputBuffer == -1) {
                                    Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer == -3) {
                                    Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                    byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                    mediaFormat = mediaFormat2;
                                } else if (dequeueOutputBuffer != -2) {
                                    if (this.mAudioTrackIndex < 0) {
                                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                    }
                                    mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                    Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                    byteBufferArr3 = byteBufferArr2;
                                } else if (dequeueOutputBuffer >= 0) {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        if (bufferInfo3.size == 0) {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                j2 = bufferInfo3.presentationTimeUs;
                                                this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                            } else {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                        }
                                        j2 = j;
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        } else {
                                            obj11 = obj6;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                }
                            }
                            obj9 = obj7;
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                    if ((bufferInfo3.flags & 2) == 0) {
                                        if (bufferInfo3.size == 0) {
                                            j2 = j;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                            if (j <= bufferInfo3.presentationTimeUs) {
                                            }
                                            if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                                throw new IOException("Audio time stamps are not in increasing order.");
                                            }
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        }
                                        if ((bufferInfo3.flags & 4) == 0) {
                                            obj11 = obj6;
                                        } else {
                                            Log.d(Constants.TAG, "audio encoder: EOS");
                                            obj11 = 1;
                                        }
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        j = j2;
                                        obj6 = obj11;
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                        this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                        mediaFormat = mediaFormat2;
                                        byteBufferArr3 = byteBufferArr2;
                                    }
                                } else {
                                    Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else if (this.mAudioTrackIndex < 0) {
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                        }
                        i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                        if (i5 == -1) {
                            Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                        } else if (i5 == -3) {
                            Log.d(Constants.TAG, "audio decoder: output buffers changed");
                            byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                        } else if (i5 == -2) {
                            Log.d(Constants.TAG, "audio decoder: output format changed: ");
                        } else if (i5 < 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                        } else if ((bufferInfo4.flags & 2) == 0) {
                            Log.d(Constants.TAG, "audio decoder: codec config buffer");
                            this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                        } else {
                            i = i5;
                        }
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                buffer = inputBuffers2[dequeueInputBuffer];
                                readSampleData = bufferInfo4.size;
                                sampleTime = bufferInfo4.presentationTimeUs;
                                if (readSampleData >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo4.offset);
                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                    if (this.mOriginalAudioChannelCount <= 0) {
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                    } else {
                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                        if (allocateDirect == null) {
                                            Log.e(Constants.TAG, "TempAudio is null!");
                                        } else {
                                            allocateDirect.position(0);
                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                            }
                                            buffer.position(0);
                                            buffer.put(allocateDirect);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                            allocateDirect.clear();
                                        }
                                    }
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo4.flags & 4) == 0) {
                                    obj9 = obj7;
                                } else {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj9 = 1;
                                }
                            } else {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj9 = obj7;
                            }
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size == 0) {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        } else {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                    }
                                    j2 = j;
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    } else {
                                        obj11 = obj6;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            }
                        }
                        obj9 = obj7;
                        if (mediaFormat2 == null) {
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat = mediaFormat2;
                        } else if (dequeueOutputBuffer != -2) {
                            if (dequeueOutputBuffer >= 0) {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size == 0) {
                                        j2 = j;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                        j2 = bufferInfo3.presentationTimeUs;
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj11 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    dequeueInputBuffer = this.mInputAudioDecoder.dequeueInputBuffer(TIMEOUT_USEC);
                    if (dequeueInputBuffer != -1) {
                        readSampleData = this.mAudioExtractor.readSampleData(inputBuffers3[dequeueInputBuffer], 0);
                        sampleTime = this.mAudioExtractor.getSampleTime();
                        if (sampleTime > this.mTrimAudioEndUs) {
                        }
                        if ((sampleTime > this.mTrimAudioEndUs ? null : 1) == null) {
                            if (readSampleData > 0) {
                                this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, this.mAudioExtractor.getSampleFlags());
                            }
                            if (this.mAudioExtractor.advance()) {
                            }
                            obj8 = this.mAudioExtractor.advance() ? 1 : null;
                        } else {
                            obj8 = 1;
                        }
                        if (obj8 != null) {
                            Log.d(Constants.TAG, "audio decoder sending EOS");
                            this.mInputAudioDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
                        }
                    } else {
                        Log.d(Constants.TAG, "audio decoder input try again later");
                    }
                    if (mediaFormat2 == null) {
                        if (this.mUserStop) {
                            dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                            if (dequeueInputBuffer != -1) {
                                Log.d(Constants.TAG, "audio encoder input buffer try again later");
                                obj9 = obj7;
                            } else {
                                buffer = inputBuffers2[dequeueInputBuffer];
                                readSampleData = bufferInfo4.size;
                                sampleTime = bufferInfo4.presentationTimeUs;
                                if (readSampleData >= 0) {
                                    duplicate = byteBufferArr[i].duplicate();
                                    duplicate.position(bufferInfo4.offset);
                                    duplicate.limit(bufferInfo4.offset + readSampleData);
                                    if (this.mOriginalAudioChannelCount <= 0) {
                                        allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                        if (allocateDirect == null) {
                                            allocateDirect.position(0);
                                            allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                            for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                                allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                                allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                            }
                                            buffer.position(0);
                                            buffer.put(allocateDirect);
                                            this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                            allocateDirect.clear();
                                        } else {
                                            Log.e(Constants.TAG, "TempAudio is null!");
                                        }
                                    } else {
                                        buffer.position(0);
                                        buffer.put(duplicate);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                    }
                                }
                                this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                                i = -1;
                                if ((bufferInfo4.flags & 4) == 0) {
                                    Log.d(Constants.TAG, "audio decoder: EOS");
                                    obj9 = 1;
                                } else {
                                    obj9 = obj7;
                                }
                            }
                            if (mediaFormat2 == null) {
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                            dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                            if (dequeueOutputBuffer == -1) {
                                Log.d(Constants.TAG, "audio encoder output buffer try again later");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer == -3) {
                                Log.d(Constants.TAG, "audio encoder: output buffers changed");
                                byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                                mediaFormat = mediaFormat2;
                            } else if (dequeueOutputBuffer != -2) {
                                if (this.mAudioTrackIndex < 0) {
                                    throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                                }
                                mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                                Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                                byteBufferArr3 = byteBufferArr2;
                            } else if (dequeueOutputBuffer >= 0) {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    if (bufferInfo3.size == 0) {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            j2 = bufferInfo3.presentationTimeUs;
                                            this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                        } else {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                    }
                                    j2 = j;
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    } else {
                                        obj11 = obj6;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            }
                        }
                        obj9 = obj7;
                        if (mediaFormat2 == null) {
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat = mediaFormat2;
                        } else if (dequeueOutputBuffer != -2) {
                            if (dequeueOutputBuffer >= 0) {
                                byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                                if ((bufferInfo3.flags & 2) == 0) {
                                    if (bufferInfo3.size == 0) {
                                        j2 = j;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                        if (j <= bufferInfo3.presentationTimeUs) {
                                        }
                                        if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                            throw new IOException("Audio time stamps are not in increasing order.");
                                        }
                                        j2 = bufferInfo3.presentationTimeUs;
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                    }
                                    if ((bufferInfo3.flags & 4) == 0) {
                                        obj11 = obj6;
                                    } else {
                                        Log.d(Constants.TAG, "audio encoder: EOS");
                                        obj11 = 1;
                                    }
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    j = j2;
                                    obj6 = obj11;
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                    this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    mediaFormat = mediaFormat2;
                                    byteBufferArr3 = byteBufferArr2;
                                }
                            } else {
                                Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        } else if (this.mAudioTrackIndex < 0) {
                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                        }
                    }
                    i5 = this.mInputAudioDecoder.dequeueOutputBuffer(bufferInfo4, TIMEOUT_USEC);
                    if (i5 == -1) {
                        Log.d(Constants.TAG, "audio decoder output buffer try again later while decoding");
                    } else if (i5 == -3) {
                        Log.d(Constants.TAG, "audio decoder: output buffers changed");
                        byteBufferArr = this.mInputAudioDecoder.getOutputBuffers();
                    } else if (i5 == -2) {
                        Log.d(Constants.TAG, "audio decoder: output format changed: ");
                    } else if (i5 < 0) {
                        Log.d(Constants.TAG, "Unexpected result from audio decoder dequeue output format.");
                    } else if ((bufferInfo4.flags & 2) == 0) {
                        i = i5;
                    } else {
                        Log.d(Constants.TAG, "audio decoder: codec config buffer");
                        this.mInputAudioDecoder.releaseOutputBuffer(i5, false);
                    }
                    if (this.mUserStop) {
                        dequeueInputBuffer = this.mOutputAudioEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                        if (dequeueInputBuffer != -1) {
                            buffer = inputBuffers2[dequeueInputBuffer];
                            readSampleData = bufferInfo4.size;
                            sampleTime = bufferInfo4.presentationTimeUs;
                            if (readSampleData >= 0) {
                                duplicate = byteBufferArr[i].duplicate();
                                duplicate.position(bufferInfo4.offset);
                                duplicate.limit(bufferInfo4.offset + readSampleData);
                                if (this.mOriginalAudioChannelCount <= 0) {
                                    buffer.position(0);
                                    buffer.put(duplicate);
                                    this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, sampleTime, bufferInfo4.flags);
                                } else {
                                    allocateDirect = ByteBuffer.allocateDirect(duplicate.capacity());
                                    if (allocateDirect == null) {
                                        Log.e(Constants.TAG, "TempAudio is null!");
                                    } else {
                                        allocateDirect.position(0);
                                        allocateDirect.limit(readSampleData / this.mOriginalAudioChannelCount);
                                        for (i5 = 0; i5 < readSampleData / (this.mOriginalAudioChannelCount * 2); i5++) {
                                            allocateDirect.put(i5 * 2, duplicate.get((this.mOriginalAudioChannelCount * i5) * 2));
                                            allocateDirect.put((i5 * 2) + 1, duplicate.get(((this.mOriginalAudioChannelCount * i5) * 2) + 1));
                                        }
                                        buffer.position(0);
                                        buffer.put(allocateDirect);
                                        this.mOutputAudioEncoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData / this.mOriginalAudioChannelCount, sampleTime, bufferInfo4.flags);
                                        allocateDirect.clear();
                                    }
                                }
                            }
                            this.mInputAudioDecoder.releaseOutputBuffer(i, false);
                            i = -1;
                            if ((bufferInfo4.flags & 4) == 0) {
                                obj9 = obj7;
                            } else {
                                Log.d(Constants.TAG, "audio decoder: EOS");
                                obj9 = 1;
                            }
                        } else {
                            Log.d(Constants.TAG, "audio encoder input buffer try again later");
                            obj9 = obj7;
                        }
                        if (mediaFormat2 == null) {
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                        dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                        if (dequeueOutputBuffer == -1) {
                            Log.d(Constants.TAG, "audio encoder output buffer try again later");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer == -3) {
                            Log.d(Constants.TAG, "audio encoder: output buffers changed");
                            byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                            mediaFormat = mediaFormat2;
                        } else if (dequeueOutputBuffer != -2) {
                            if (this.mAudioTrackIndex < 0) {
                                throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                            }
                            mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                            Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                            byteBufferArr3 = byteBufferArr2;
                        } else if (dequeueOutputBuffer >= 0) {
                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        } else {
                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                            if ((bufferInfo3.flags & 2) == 0) {
                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                if (bufferInfo3.size == 0) {
                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                    if (j <= bufferInfo3.presentationTimeUs) {
                                    }
                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                        j2 = bufferInfo3.presentationTimeUs;
                                        this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                    } else {
                                        throw new IOException("Audio time stamps are not in increasing order.");
                                    }
                                }
                                j2 = j;
                                if ((bufferInfo3.flags & 4) == 0) {
                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                    obj11 = 1;
                                } else {
                                    obj11 = obj6;
                                }
                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                j = j2;
                                obj6 = obj11;
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        }
                    }
                    obj9 = obj7;
                    if (mediaFormat2 == null) {
                        mediaFormat = mediaFormat2;
                        byteBufferArr3 = byteBufferArr2;
                    }
                    dequeueOutputBuffer = this.mOutputAudioEncoder.dequeueOutputBuffer(bufferInfo3, TIMEOUT_USEC);
                    if (dequeueOutputBuffer == -1) {
                        Log.d(Constants.TAG, "audio encoder output buffer try again later");
                        mediaFormat = mediaFormat2;
                        byteBufferArr3 = byteBufferArr2;
                    } else if (dequeueOutputBuffer == -3) {
                        Log.d(Constants.TAG, "audio encoder: output buffers changed");
                        byteBufferArr3 = this.mOutputAudioEncoder.getOutputBuffers();
                        mediaFormat = mediaFormat2;
                    } else if (dequeueOutputBuffer != -2) {
                        if (dequeueOutputBuffer >= 0) {
                            byteBuffer2 = byteBufferArr2[dequeueOutputBuffer];
                            if ((bufferInfo3.flags & 2) == 0) {
                                if (bufferInfo3.size == 0) {
                                    j2 = j;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder writing sample data to muxer " + bufferInfo3.presentationTimeUs);
                                    if (j <= bufferInfo3.presentationTimeUs) {
                                    }
                                    if ((j <= bufferInfo3.presentationTimeUs ? null : 1) == null) {
                                        throw new IOException("Audio time stamps are not in increasing order.");
                                    }
                                    j2 = bufferInfo3.presentationTimeUs;
                                    this.mMuxer.writeSampleData(this.mAudioTrackIndex, byteBuffer2, bufferInfo3);
                                }
                                if ((bufferInfo3.flags & 4) == 0) {
                                    obj11 = obj6;
                                } else {
                                    Log.d(Constants.TAG, "audio encoder: EOS");
                                    obj11 = 1;
                                }
                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                j = j2;
                                obj6 = obj11;
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            } else {
                                Log.d(Constants.TAG, "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG");
                                this.mOutputAudioEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                mediaFormat = mediaFormat2;
                                byteBufferArr3 = byteBufferArr2;
                            }
                        } else {
                            Log.d(Constants.TAG, "Unexpected result from audio encoder dequeue output format.");
                            mediaFormat = mediaFormat2;
                            byteBufferArr3 = byteBufferArr2;
                        }
                    } else if (this.mAudioTrackIndex < 0) {
                        mediaFormat = this.mOutputAudioEncoder.getOutputFormat();
                        Log.d(Constants.TAG, "audio encoder: output format changed " + mediaFormat);
                        byteBufferArr3 = byteBufferArr2;
                    } else {
                        throw new RuntimeException("Audio encoder output format changed after muxer is started.");
                    }
                }
                if (!this.mCopyAudio) {
                    if (this.mUserStop) {
                        Log.d(Constants.TAG, "Encoding abruptly stopped.");
                    } else {
                        obj7 = obj9;
                        mediaFormat2 = mediaFormat;
                        byteBufferArr2 = byteBufferArr3;
                        obj9 = obj10;
                    }
                }
                this.mVideoTrackIndex = this.mMuxer.addTrack(mediaFormat3);
                if (this.mCopyAudio) {
                    this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
                }
                this.mMuxer.setOrientationHint(this.mInputOrientationDegrees);
                this.mMuxer.start();
                this.mMuxerStarted = true;
                if (this.mUserStop) {
                    obj7 = obj9;
                    mediaFormat2 = mediaFormat;
                    byteBufferArr2 = byteBufferArr3;
                    obj9 = obj10;
                } else {
                    Log.d(Constants.TAG, "Encoding abruptly stopped.");
                }
            }
            return;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startTransRewriting() throws java.io.IOException {
        /*
        r39 = this;
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 != 0) goto L_0x0103;
    L_0x0006:
        r27 = 0;
        r18 = 0;
        r0 = r39;
        r2 = r0.mCopyAudio;
        if (r2 != 0) goto L_0x010a;
    L_0x0010:
        r2 = 1;
    L_0x0011:
        r0 = r39;
        r3 = r0.mCopyAudio;
        if (r3 != 0) goto L_0x010d;
    L_0x0017:
        r3 = 1;
    L_0x0018:
        r0 = r39;
        r4 = r0.mCopyAudio;
        if (r4 != 0) goto L_0x0110;
    L_0x001e:
        r4 = 1;
    L_0x001f:
        r11 = 0;
        r16 = 0;
        r15 = 0;
        r21 = -1;
        r13 = 0;
        r12 = 0;
        r0 = r39;
        r5 = r0.mOutputVideoFrameRate;
        r5 = r5 << 1;
        r6 = 0;
        r0 = r39;
        r0 = r0.mTrimVideoEndUs;
        r30 = r0;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r22 = 0;
        r5 = (r8 > r22 ? 1 : (r8 == r22 ? 0 : -1));
        if (r5 == 0) goto L_0x0323;
    L_0x003f:
        r0 = r39;
        r5 = r0.mTransRewritable;
        r8 = 2;
        if (r5 == r8) goto L_0x0113;
    L_0x0046:
        r0 = r39;
        r5 = r0.mTransRewritable;
        r8 = 1;
        if (r5 == r8) goto L_0x02e4;
    L_0x004d:
        r28 = r6;
        r23 = r4;
    L_0x0051:
        if (r16 != 0) goto L_0x0334;
    L_0x0053:
        r0 = r39;
        r4 = r0.mCopyAudio;
        if (r4 != 0) goto L_0x0338;
    L_0x0059:
        r0 = r39;
        r4 = r0.mTransRewritable;
        if (r4 != 0) goto L_0x03a1;
    L_0x005f:
        r4 = "Transcoding start";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r0 = r39;
        r4 = r0.mOutputVideoEncoder;
        r17 = r4.getOutputBuffers();
        r0 = r39;
        r4 = r0.mInputVideoDecoder;
        r32 = r4.getInputBuffers();
        r0 = r39;
        r4 = r0.mInputVideoDecoder;
        r14 = r4.getOutputBuffers();
        r0 = r39;
        r4 = r0.mCopyAudio;
        if (r4 != 0) goto L_0x03cd;
    L_0x0083:
        r4 = 0;
    L_0x0084:
        r0 = r39;
        r5 = r0.mCopyAudio;
        if (r5 != 0) goto L_0x03d7;
    L_0x008a:
        r5 = 0;
        r9 = r5;
    L_0x008c:
        r0 = r39;
        r5 = r0.mCopyAudio;
        if (r5 != 0) goto L_0x03e2;
    L_0x0092:
        r5 = 0;
    L_0x0093:
        r0 = r39;
        r6 = r0.mCopyAudio;
        if (r6 != 0) goto L_0x03ec;
    L_0x0099:
        r6 = 0;
        r10 = r6;
    L_0x009b:
        r33 = new android.media.MediaCodec$BufferInfo;
        r33.<init>();
        r34 = new android.media.MediaCodec$BufferInfo;
        r34.<init>();
        r35 = new android.media.MediaCodec$BufferInfo;
        r35.<init>();
        r36 = new android.media.MediaCodec$BufferInfo;
        r36.<init>();
        r24 = -1;
        r20 = r5;
        r26 = r4;
        r22 = r3;
        r19 = r2;
        r2 = r11;
    L_0x00ba:
        if (r16 != 0) goto L_0x03f7;
    L_0x00bc:
        r0 = r39;
        r3 = r0.mUserStop;
        if (r3 == 0) goto L_0x0400;
    L_0x00c2:
        r11 = r2;
    L_0x00c3:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x047d;
    L_0x00c9:
        r37 = r14;
        r14 = r13;
        r13 = r12;
        r12 = r37;
    L_0x00cf:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x0610;
    L_0x00d5:
        r37 = r17;
        r17 = r16;
        r16 = r37;
    L_0x00db:
        r0 = r39;
        r2 = r0.mCopyAudio;
        if (r2 != 0) goto L_0x06fb;
    L_0x00e1:
        r2 = r26;
        r3 = r27;
    L_0x00e5:
        r0 = r39;
        r4 = r0.mUserStop;
        if (r4 == 0) goto L_0x099f;
    L_0x00eb:
        r0 = r39;
        r4 = r0.mUserStop;
        if (r4 != 0) goto L_0x09eb;
    L_0x00f1:
        r26 = r2;
        r27 = r3;
        r2 = r11;
        r37 = r13;
        r13 = r14;
        r14 = r12;
        r12 = r37;
        r38 = r16;
        r16 = r17;
        r17 = r38;
        goto L_0x00ba;
    L_0x0103:
        r2 = "Not starting encoding because it is stopped by user.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        return;
    L_0x010a:
        r2 = 0;
        goto L_0x0011;
    L_0x010d:
        r3 = 0;
        goto L_0x0018;
    L_0x0110:
        r4 = 0;
        goto L_0x001f;
    L_0x0113:
        r0 = r39;
        r6 = r0.mTrimVideoEndUs;
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r10 = 0;
        r5.seekTo(r8, r10);
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Input time: ";
        r5 = r5.append(r8);
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = r5.append(r8);
        r8 = " After seekto previous I: ";
        r5 = r5.append(r8);
        r0 = r39;
        r8 = r0.mVideoExtractor;
        r8 = r8.getSampleTime();
        r5 = r5.append(r8);
        r5 = r5.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
    L_0x0151:
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r8 = r5.getSampleTime();
        r5 = (r8 > r30 ? 1 : (r8 == r30 ? 0 : -1));
        if (r5 < 0) goto L_0x0192;
    L_0x015d:
        r5 = 1;
    L_0x015e:
        if (r5 != 0) goto L_0x01b3;
    L_0x0160:
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r5.advance();
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r5 = r5.getSampleFlags();
        r8 = 1;
        if (r5 == r8) goto L_0x0194;
    L_0x0172:
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r6 = r5.getSampleTime();
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Proceed to find I: Sampletime: ";
        r5 = r5.append(r8);
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        goto L_0x0151;
    L_0x0192:
        r5 = 0;
        goto L_0x015e;
    L_0x0194:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Found I: ";
        r5 = r5.append(r8);
        r0 = r39;
        r8 = r0.mVideoExtractor;
        r8 = r8.getSampleTime();
        r5 = r5.append(r8);
        r5 = r5.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
    L_0x01b3:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Transcode section Start: ";
        r5 = r5.append(r8);
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = r5.append(r8);
        r8 = ", End: ";
        r5 = r5.append(r8);
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x025e;
    L_0x01e1:
        r5 = 1;
    L_0x01e2:
        if (r5 != 0) goto L_0x0280;
    L_0x01e4:
        r4 = "Reversed. Recalculating...";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r0 = r39;
        r6 = r0.mTrimVideoStartUs;
        r5 = 2;
        r4.seekTo(r6, r5);
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r4 = r4.getSampleTime();
        r0 = r39;
        r0.mTrimVideoStartUs = r4;
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r0 = r39;
        r6 = r0.mTrimVideoStartUs;
        r5 = 2;
        r4.seekTo(r6, r5);
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r4 = r4.getSampleTime();
        r0 = r39;
        r0.mTrimAudioStartUs = r4;
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r4 = r4.getSampleFlags();
        r5 = 1;
        if (r4 == r5) goto L_0x0260;
    L_0x0225:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Now...Input time: ";
        r4 = r4.append(r5);
        r0 = r39;
        r6 = r0.mTrimVideoStartUs;
        r4 = r4.append(r6);
        r5 = " After seekto: ";
        r4 = r4.append(r5);
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r6 = r5.getSampleTime();
        r4 = r4.append(r6);
        r4 = r4.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r6 = -1;
        r16 = 1;
        r4 = 1;
        r28 = r6;
        r23 = r4;
        goto L_0x0051;
    L_0x025e:
        r5 = 0;
        goto L_0x01e2;
    L_0x0260:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Starting on I: ";
        r4 = r4.append(r5);
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r6 = r5.getSampleTime();
        r4 = r4.append(r6);
        r4 = r4.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        goto L_0x0225;
    L_0x0280:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r8 = "Set transcode mode: Start: ";
        r5 = r5.append(r8);
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = r5.append(r8);
        r8 = ", End: ";
        r5 = r5.append(r8);
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        r0 = r39;
        r0.mTrimVideoEndUs = r6;
        r0 = r39;
        r0.mTrimAudioEndUs = r6;
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r10 = 0;
        r5.seekTo(r8, r10);
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r8 = r5.getSampleTime();
        r0 = r39;
        r0.mTrimVideoStartUs = r8;
        r0 = r39;
        r5 = r0.mAudioExtractor;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r10 = 0;
        r5.seekTo(r8, r10);
        r0 = r39;
        r5 = r0.mAudioExtractor;
        r8 = r5.getSampleTime();
        r0 = r39;
        r0.mTrimAudioStartUs = r8;
        r28 = r6;
        r23 = r4;
        goto L_0x0051;
    L_0x02e4:
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = 0;
        r4.seekTo(r8, r5);
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r4 = r4.getSampleTime();
        r0 = r39;
        r0.mTrimVideoStartUs = r4;
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r0 = r39;
        r8 = r0.mTrimVideoStartUs;
        r5 = 0;
        r4.seekTo(r8, r5);
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r4 = r4.getSampleTime();
        r0 = r39;
        r0.mTrimAudioStartUs = r4;
        r16 = 1;
        r4 = 1;
        r5 = "Abandon TransRewrite. Switch to Rewrite mode.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        r28 = r6;
        r23 = r4;
        goto L_0x0051;
    L_0x0323:
        r6 = -1;
        r16 = 1;
        r4 = 1;
        r5 = "Start point has not been updated!";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        r28 = r6;
        r23 = r4;
        goto L_0x0051;
    L_0x0334:
        if (r23 == 0) goto L_0x0053;
    L_0x0336:
        goto L_0x0059;
    L_0x0338:
        r0 = r39;
        r4 = r0.mTrimAudioStartUs;
        r6 = 0;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x0059;
    L_0x0342:
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r0 = r39;
        r6 = r0.mTrimAudioStartUs;
        r5 = 0;
        r4.seekTo(r6, r5);
    L_0x034e:
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r4 = r4.getSampleTime();
        r0 = r39;
        r6 = r0.mTrimAudioStartUs;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 < 0) goto L_0x036f;
    L_0x035e:
        r4 = 1;
    L_0x035f:
        if (r4 != 0) goto L_0x0371;
    L_0x0361:
        r4 = "Advance audio...";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r4.advance();
        goto L_0x034e;
    L_0x036f:
        r4 = 0;
        goto L_0x035f;
    L_0x0371:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Audio Transcode section: Current position: ";
        r4 = r4.append(r5);
        r0 = r39;
        r5 = r0.mAudioExtractor;
        r6 = r5.getSampleTime();
        r4 = r4.append(r6);
        r5 = " mTrimAudioStartUs: ";
        r4 = r4.append(r5);
        r0 = r39;
        r6 = r0.mTrimAudioStartUs;
        r4 = r4.append(r6);
        r4 = r4.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        goto L_0x0059;
    L_0x03a1:
        r0 = r39;
        r4 = r0.mTransRewritable;
        r5 = 2;
        if (r4 == r5) goto L_0x005f;
    L_0x03a8:
        r2 = "Rewriting starts";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r5 = 0;
        r4 = 0;
        r0 = r39;
        r2 = r0.mVideoExtractor;
        r6 = com.samsung.android.transcode.util.CodecsHelper.getAndSelectVideoTrackIndex(r2);
        r0 = r39;
        r2 = r0.mAudioExtractor;
        r3 = com.samsung.android.transcode.util.CodecsHelper.getAndSelectAudioTrackIndex(r2);
        r2 = 0;
        r7 = -1;
        if (r6 != r7) goto L_0x09f3;
    L_0x03c4:
        r2 = new java.io.IOException;
        r3 = "Absent valid video track";
        r2.<init>(r3);
        throw r2;
    L_0x03cd:
        r0 = r39;
        r4 = r0.mOutputAudioEncoder;
        r4 = r4.getOutputBuffers();
        goto L_0x0084;
    L_0x03d7:
        r0 = r39;
        r5 = r0.mOutputAudioEncoder;
        r5 = r5.getInputBuffers();
        r9 = r5;
        goto L_0x008c;
    L_0x03e2:
        r0 = r39;
        r5 = r0.mInputAudioDecoder;
        r5 = r5.getOutputBuffers();
        goto L_0x0093;
    L_0x03ec:
        r0 = r39;
        r6 = r0.mInputAudioDecoder;
        r6 = r6.getInputBuffers();
        r10 = r6;
        goto L_0x009b;
    L_0x03f7:
        if (r23 == 0) goto L_0x00bc;
    L_0x03f9:
        r2 = "Transcoding Done";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x03a8;
    L_0x0400:
        if (r2 != 0) goto L_0x00c2;
    L_0x0402:
        if (r18 != 0) goto L_0x0455;
    L_0x0404:
        r0 = r39;
        r3 = r0.mInputVideoDecoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3 = r3.dequeueInputBuffer(r4);
        r4 = -1;
        if (r3 == r4) goto L_0x045d;
    L_0x0411:
        r2 = r32[r3];
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r5 = 0;
        r5 = r4.readSampleData(r2, r5);
        r0 = r39;
        r2 = r0.mVideoExtractor;
        r6 = r2.getSampleTime();
        r2 = (r6 > r28 ? 1 : (r6 == r28 ? 0 : -1));
        if (r2 <= 0) goto L_0x0466;
    L_0x0428:
        r2 = 1;
    L_0x0429:
        if (r2 != 0) goto L_0x047b;
    L_0x042b:
        if (r5 >= 0) goto L_0x0468;
    L_0x042d:
        r0 = r39;
        r2 = r0.mVideoExtractor;
        r2 = r2.advance();
        if (r2 == 0) goto L_0x0479;
    L_0x0437:
        r2 = 0;
    L_0x0438:
        r4 = "Move forward to locate: Video";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r11 = r2;
    L_0x043f:
        if (r11 == 0) goto L_0x00c3;
    L_0x0441:
        r2 = "video extractor: EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mInputVideoDecoder;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 4;
        r2.queueInputBuffer(r3, r4, r5, r6, r8);
        goto L_0x00c3;
    L_0x0455:
        r0 = r39;
        r3 = r0.mMuxerStarted;
        if (r3 != 0) goto L_0x0404;
    L_0x045b:
        goto L_0x00c2;
    L_0x045d:
        r3 = "no video decoder input buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r11 = r2;
        goto L_0x00c3;
    L_0x0466:
        r2 = 0;
        goto L_0x0429;
    L_0x0468:
        r0 = r39;
        r2 = r0.mInputVideoDecoder;
        r4 = 0;
        r0 = r39;
        r8 = r0.mVideoExtractor;
        r8 = r8.getSampleFlags();
        r2.queueInputBuffer(r3, r4, r5, r6, r8);
        goto L_0x042d;
    L_0x0479:
        r2 = 1;
        goto L_0x0438;
    L_0x047b:
        r11 = 1;
        goto L_0x043f;
    L_0x047d:
        if (r15 != 0) goto L_0x00c9;
    L_0x047f:
        if (r18 != 0) goto L_0x04db;
    L_0x0481:
        r0 = r39;
        r2 = r0.mInputVideoDecoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r34;
        r3 = r2.dequeueOutputBuffer(r0, r4);
        r2 = -1;
        if (r3 == r2) goto L_0x04e3;
    L_0x0490:
        r2 = -3;
        if (r3 == r2) goto L_0x04f1;
    L_0x0493:
        r2 = -2;
        if (r3 == r2) goto L_0x0504;
    L_0x0496:
        r2 = r14[r3];
        r0 = r34;
        r2 = r0.flags;
        r2 = r2 & 2;
        if (r2 != 0) goto L_0x050c;
    L_0x04a0:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "video decoder: returned buffer for time ";
        r2 = r2.append(r4);
        r0 = r34;
        r4 = r0.presentationTimeUs;
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r34;
        r2 = r0.size;
        if (r2 != 0) goto L_0x0522;
    L_0x04c1:
        r2 = 0;
    L_0x04c2:
        r0 = r39;
        r4 = r0.mInputVideoDecoder;
        r4.releaseOutputBuffer(r3, r2);
        if (r2 != 0) goto L_0x0524;
    L_0x04cb:
        r0 = r34;
        r2 = r0.flags;
        r2 = r2 & 4;
        if (r2 != 0) goto L_0x05f9;
    L_0x04d3:
        r37 = r14;
        r14 = r13;
        r13 = r12;
        r12 = r37;
        goto L_0x00cf;
    L_0x04db:
        r0 = r39;
        r2 = r0.mMuxerStarted;
        if (r2 != 0) goto L_0x0481;
    L_0x04e1:
        goto L_0x00c9;
    L_0x04e3:
        r2 = "no video decoder output buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r37 = r14;
        r14 = r13;
        r13 = r12;
        r12 = r37;
        goto L_0x00cf;
    L_0x04f1:
        r2 = "video decoder: output buffers changed";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mInputVideoDecoder;
        r2 = r2.getOutputBuffers();
        r14 = r13;
        r13 = r12;
        r12 = r2;
        goto L_0x00cf;
    L_0x0504:
        r37 = r14;
        r14 = r13;
        r13 = r12;
        r12 = r37;
        goto L_0x00cf;
    L_0x050c:
        r2 = "video decoder: codec config buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mInputVideoDecoder;
        r4 = 0;
        r2.releaseOutputBuffer(r3, r4);
        r37 = r14;
        r14 = r13;
        r13 = r12;
        r12 = r37;
        goto L_0x00cf;
    L_0x0522:
        r2 = 1;
        goto L_0x04c2;
    L_0x0524:
        r2 = "output surface: await new image";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mOutputSurface;	 Catch:{ RuntimeException -> 0x05cd }
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = r2.checkForNewImage(r3);	 Catch:{ RuntimeException -> 0x05cd }
        if (r2 == 0) goto L_0x05b0;
    L_0x0536:
        r2 = "output surface: draw image";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ RuntimeException -> 0x05cd }
        r2 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.opengl.GLES20.glClear(r2);	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r39;
        r2 = r0.mOutputSurface;	 Catch:{ RuntimeException -> 0x05cd }
        r2.drawImage();	 Catch:{ RuntimeException -> 0x05cd }
        r2 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x05cd }
        r2.<init>();	 Catch:{ RuntimeException -> 0x05cd }
        r3 = "presentationTimeUs: ";
        r2 = r2.append(r3);	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r34;
        r4 = r0.presentationTimeUs;	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r2.append(r4);	 Catch:{ RuntimeException -> 0x05cd }
        r3 = "StartPnt: ";
        r2 = r2.append(r3);	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r39;
        r4 = r0.mTrimVideoStartUs;	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r2.append(r4);	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r2.toString();	 Catch:{ RuntimeException -> 0x05cd }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r34;
        r2 = r0.presentationTimeUs;	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r39;
        r4 = r0.mTrimVideoStartUs;	 Catch:{ RuntimeException -> 0x05cd }
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 >= 0) goto L_0x05de;
    L_0x057e:
        r2 = 1;
    L_0x057f:
        if (r2 != 0) goto L_0x05e0;
    L_0x0581:
        r0 = r39;
        r2 = r0.mSkipFrames;	 Catch:{ RuntimeException -> 0x05cd }
        if (r2 != 0) goto L_0x05e2;
    L_0x0587:
        r2 = 0;
        r0 = r39;
        r3 = r0.mInputSurface;	 Catch:{ RuntimeException -> 0x0b69 }
        r0 = r34;
        r4 = r0.presentationTimeUs;	 Catch:{ RuntimeException -> 0x0b69 }
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = r4 * r6;
        r3.setPresentationTime(r4);	 Catch:{ RuntimeException -> 0x0b69 }
        r3 = "input surface: swap buffers";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);	 Catch:{ RuntimeException -> 0x0b69 }
        r0 = r39;
        r3 = r0.mInputSurface;	 Catch:{ RuntimeException -> 0x0b69 }
        r3.swapBuffers();	 Catch:{ RuntimeException -> 0x0b69 }
        r3 = "video encoder: notified of new frame";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);	 Catch:{ RuntimeException -> 0x0b69 }
        r3 = r13 + 1;
    L_0x05ab:
        r12 = r2 + 1;
    L_0x05ad:
        r13 = r3;
        goto L_0x04cb;
    L_0x05b0:
        r2 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x05cd }
        r2.<init>();	 Catch:{ RuntimeException -> 0x05cd }
        r3 = "video decoder: checkForNewImage return false!!  mUserStop : ";
        r2 = r2.append(r3);	 Catch:{ RuntimeException -> 0x05cd }
        r0 = r39;
        r3 = r0.mUserStop;	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r2.append(r3);	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r2.toString();	 Catch:{ RuntimeException -> 0x05cd }
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);	 Catch:{ RuntimeException -> 0x05cd }
        goto L_0x00c3;
    L_0x05cd:
        r2 = move-exception;
    L_0x05ce:
        r3 = r2.getMessage();
        r0 = r39;
        r4 = r0.mUserStop;
        if (r4 != 0) goto L_0x05ed;
    L_0x05d8:
        r3 = new java.lang.RuntimeException;
        r3.<init>(r2);
        throw r3;
    L_0x05de:
        r2 = 0;
        goto L_0x057f;
    L_0x05e0:
        r3 = r13;
        goto L_0x05ad;
    L_0x05e2:
        r0 = r39;
        r2 = r0.mFramesSkipInterval;	 Catch:{ RuntimeException -> 0x05cd }
        r2 = r12 % r2;
        if (r2 == 0) goto L_0x0587;
    L_0x05ea:
        r2 = r12;
        r3 = r13;
        goto L_0x05ab;
    L_0x05ed:
        if (r3 == 0) goto L_0x05d8;
    L_0x05ef:
        r4 = "Surface frame wait timed out";
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x04cb;
    L_0x05f8:
        goto L_0x05d8;
    L_0x05f9:
        r2 = "video decoder: EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = 1;
        r0 = r39;
        r3 = r0.mOutputVideoEncoder;
        r3.signalEndOfInputStream();
        r15 = r2;
        r37 = r12;
        r12 = r14;
        r14 = r13;
        r13 = r37;
        goto L_0x00cf;
    L_0x0610:
        if (r16 != 0) goto L_0x00d5;
    L_0x0612:
        if (r18 != 0) goto L_0x0653;
    L_0x0614:
        r0 = r39;
        r2 = r0.mOutputVideoEncoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r33;
        r3 = r2.dequeueOutputBuffer(r0, r4);
        r2 = -1;
        if (r3 == r2) goto L_0x065b;
    L_0x0623:
        r2 = -3;
        if (r3 == r2) goto L_0x0669;
    L_0x0626:
        r2 = -2;
        if (r3 == r2) goto L_0x067d;
    L_0x0629:
        if (r3 < 0) goto L_0x06a4;
    L_0x062b:
        r2 = r17[r3];
        r0 = r33;
        r4 = r0.flags;
        r4 = r4 & 2;
        if (r4 != 0) goto L_0x06b2;
    L_0x0635:
        r0 = r33;
        r4 = r0.size;
        if (r4 != 0) goto L_0x06c8;
    L_0x063b:
        r0 = r33;
        r2 = r0.flags;
        r2 = r2 & 4;
        if (r2 != 0) goto L_0x06f2;
    L_0x0643:
        r2 = r16;
    L_0x0645:
        r0 = r39;
        r4 = r0.mOutputVideoEncoder;
        r5 = 0;
        r4.releaseOutputBuffer(r3, r5);
        r16 = r17;
        r17 = r2;
        goto L_0x00db;
    L_0x0653:
        r0 = r39;
        r2 = r0.mMuxerStarted;
        if (r2 != 0) goto L_0x0614;
    L_0x0659:
        goto L_0x00d5;
    L_0x065b:
        r2 = "no video encoder output buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r37 = r17;
        r17 = r16;
        r16 = r37;
        goto L_0x00db;
    L_0x0669:
        r2 = "video encoder: output buffers changed";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mOutputVideoEncoder;
        r2 = r2.getOutputBuffers();
        r17 = r16;
        r16 = r2;
        goto L_0x00db;
    L_0x067d:
        r2 = "video encoder: output format changed";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mVideoTrackIndex;
        if (r2 >= 0) goto L_0x069b;
    L_0x0689:
        r0 = r39;
        r2 = r0.mOutputVideoEncoder;
        r2 = r2.getOutputFormat();
        r18 = r2;
        r37 = r16;
        r16 = r17;
        r17 = r37;
        goto L_0x00db;
    L_0x069b:
        r2 = new java.lang.RuntimeException;
        r3 = "Video encoder output format changed after muxer has started";
        r2.<init>(r3);
        throw r2;
    L_0x06a4:
        r2 = "Unexpected result from video encoder dequeue output format.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r37 = r17;
        r17 = r16;
        r16 = r37;
        goto L_0x00db;
    L_0x06b2:
        r2 = "video encoder: codec config buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mOutputVideoEncoder;
        r4 = 0;
        r2.releaseOutputBuffer(r3, r4);
        r37 = r17;
        r17 = r16;
        r16 = r37;
        goto L_0x00db;
    L_0x06c8:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "video encoder: writing sample data timestamp ";
        r4 = r4.append(r5);
        r0 = r33;
        r6 = r0.presentationTimeUs;
        r4 = r4.append(r6);
        r4 = r4.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r0 = r39;
        r4 = r0.mMuxer;
        r0 = r39;
        r5 = r0.mVideoTrackIndex;
        r0 = r33;
        r4.writeSampleData(r5, r2, r0);
        goto L_0x063b;
    L_0x06f2:
        r2 = "video encoder: EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = 1;
        goto L_0x0645;
    L_0x06fb:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x071d;
    L_0x0701:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x07bf;
    L_0x0707:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x082d;
    L_0x070d:
        r2 = r22;
    L_0x070f:
        r0 = r39;
        r3 = r0.mUserStop;
        if (r3 == 0) goto L_0x089f;
    L_0x0715:
        r22 = r2;
        r3 = r27;
        r2 = r26;
        goto L_0x00e5;
    L_0x071d:
        if (r19 != 0) goto L_0x0701;
    L_0x071f:
        if (r27 != 0) goto L_0x0797;
    L_0x0721:
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3 = r2.dequeueInputBuffer(r4);
        r2 = -1;
        if (r3 == r2) goto L_0x079f;
    L_0x072e:
        r2 = r10[r3];
        r0 = r39;
        r4 = r0.mAudioExtractor;
        r5 = 0;
        r5 = r4.readSampleData(r2, r5);
        r0 = r39;
        r2 = r0.mAudioExtractor;
        r6 = r2.getSampleTime();
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Audio psntTimeUs: ";
        r2 = r2.append(r4);
        r2 = r2.append(r6);
        r4 = ", TrimTCEndTime ";
        r2 = r2.append(r4);
        r0 = r28;
        r2 = r2.append(r0);
        r2 = r2.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = (r6 > r28 ? 1 : (r6 == r28 ? 0 : -1));
        if (r2 <= 0) goto L_0x07a7;
    L_0x0769:
        r2 = 1;
    L_0x076a:
        if (r2 != 0) goto L_0x07bc;
    L_0x076c:
        if (r5 > 0) goto L_0x07a9;
    L_0x076e:
        r0 = r39;
        r2 = r0.mAudioExtractor;
        r2 = r2.advance();
        if (r2 == 0) goto L_0x07ba;
    L_0x0778:
        r2 = 0;
    L_0x0779:
        r4 = "Move forward to locate: Audio";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r19 = r2;
    L_0x0781:
        if (r19 == 0) goto L_0x0701;
    L_0x0783:
        r2 = "audio decoder sending EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 4;
        r2.queueInputBuffer(r3, r4, r5, r6, r8);
        goto L_0x0701;
    L_0x0797:
        r0 = r39;
        r2 = r0.mMuxerStarted;
        if (r2 != 0) goto L_0x0721;
    L_0x079d:
        goto L_0x0701;
    L_0x079f:
        r2 = "audio decoder input try again later";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x0701;
    L_0x07a7:
        r2 = 0;
        goto L_0x076a;
    L_0x07a9:
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r4 = 0;
        r0 = r39;
        r8 = r0.mAudioExtractor;
        r8 = r8.getSampleFlags();
        r2.queueInputBuffer(r3, r4, r5, r6, r8);
        goto L_0x076e;
    L_0x07ba:
        r2 = 1;
        goto L_0x0779;
    L_0x07bc:
        r19 = 1;
        goto L_0x0781;
    L_0x07bf:
        if (r22 != 0) goto L_0x0707;
    L_0x07c1:
        r2 = -1;
        r0 = r21;
        if (r0 != r2) goto L_0x0707;
    L_0x07c6:
        if (r27 != 0) goto L_0x07eb;
    L_0x07c8:
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r36;
        r2 = r2.dequeueOutputBuffer(r0, r4);
        r3 = -1;
        if (r2 == r3) goto L_0x07f3;
    L_0x07d7:
        r3 = -3;
        if (r2 == r3) goto L_0x07fb;
    L_0x07da:
        r3 = -2;
        if (r2 == r3) goto L_0x080d;
    L_0x07dd:
        if (r2 < 0) goto L_0x0815;
    L_0x07df:
        r0 = r36;
        r3 = r0.flags;
        r3 = r3 & 2;
        if (r3 != 0) goto L_0x081d;
    L_0x07e7:
        r21 = r2;
        goto L_0x0707;
    L_0x07eb:
        r0 = r39;
        r2 = r0.mMuxerStarted;
        if (r2 != 0) goto L_0x07c8;
    L_0x07f1:
        goto L_0x0707;
    L_0x07f3:
        r2 = "audio decoder output buffer try again later";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x0707;
    L_0x07fb:
        r2 = "audio decoder: output buffers changed";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r2 = r2.getOutputBuffers();
        r20 = r2;
        goto L_0x0707;
    L_0x080d:
        r2 = "audio decoder: output format changed: ";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x0707;
    L_0x0815:
        r2 = "Unexpected result from audio decoder dequeue output format.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x0707;
    L_0x081d:
        r3 = "audio decoder: codec config buffer";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r0 = r39;
        r3 = r0.mInputAudioDecoder;
        r4 = 0;
        r3.releaseOutputBuffer(r2, r4);
        goto L_0x0707;
    L_0x082d:
        r2 = -1;
        r0 = r21;
        if (r0 == r2) goto L_0x070d;
    L_0x0832:
        r0 = r39;
        r2 = r0.mOutputAudioEncoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3 = r2.dequeueInputBuffer(r4);
        r2 = -1;
        if (r3 == r2) goto L_0x0863;
    L_0x083f:
        r2 = r9[r3];
        r0 = r36;
        r5 = r0.size;
        r0 = r36;
        r6 = r0.presentationTimeUs;
        if (r5 >= 0) goto L_0x086d;
    L_0x084b:
        r0 = r39;
        r2 = r0.mInputAudioDecoder;
        r3 = 0;
        r0 = r21;
        r2.releaseOutputBuffer(r0, r3);
        r21 = -1;
        r0 = r36;
        r2 = r0.flags;
        r2 = r2 & 4;
        if (r2 != 0) goto L_0x0896;
    L_0x085f:
        r2 = r22;
        goto L_0x070f;
    L_0x0863:
        r2 = "audio encoder input buffer try again later";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = r22;
        goto L_0x070f;
    L_0x086d:
        r4 = r20[r21];
        r4 = r4.duplicate();
        r0 = r36;
        r8 = r0.offset;
        r4.position(r8);
        r0 = r36;
        r8 = r0.offset;
        r8 = r8 + r5;
        r4.limit(r8);
        r8 = 0;
        r2.position(r8);
        r2.put(r4);
        r0 = r39;
        r2 = r0.mOutputAudioEncoder;
        r4 = 0;
        r0 = r36;
        r8 = r0.flags;
        r2.queueInputBuffer(r3, r4, r5, r6, r8);
        goto L_0x084b;
    L_0x0896:
        r2 = "audio decoder: EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = 1;
        goto L_0x070f;
    L_0x089f:
        if (r23 != 0) goto L_0x0715;
    L_0x08a1:
        if (r27 != 0) goto L_0x08ea;
    L_0x08a3:
        r0 = r39;
        r3 = r0.mOutputAudioEncoder;
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r35;
        r6 = r3.dequeueOutputBuffer(r0, r4);
        r3 = -1;
        if (r6 == r3) goto L_0x08f2;
    L_0x08b2:
        r3 = -3;
        if (r6 == r3) goto L_0x0900;
    L_0x08b5:
        r3 = -2;
        if (r6 == r3) goto L_0x0915;
    L_0x08b8:
        if (r6 < 0) goto L_0x0949;
    L_0x08ba:
        r7 = r26[r6];
        r0 = r35;
        r3 = r0.flags;
        r3 = r3 & 2;
        if (r3 != 0) goto L_0x0957;
    L_0x08c4:
        r0 = r35;
        r3 = r0.size;
        if (r3 != 0) goto L_0x096d;
    L_0x08ca:
        r4 = r24;
    L_0x08cc:
        r0 = r35;
        r3 = r0.flags;
        r3 = r3 & 4;
        if (r3 != 0) goto L_0x0996;
    L_0x08d4:
        r3 = r23;
    L_0x08d6:
        r0 = r39;
        r7 = r0.mOutputAudioEncoder;
        r8 = 0;
        r7.releaseOutputBuffer(r6, r8);
        r24 = r4;
        r23 = r3;
        r22 = r2;
        r2 = r26;
        r3 = r27;
        goto L_0x00e5;
    L_0x08ea:
        r0 = r39;
        r3 = r0.mMuxerStarted;
        if (r3 != 0) goto L_0x08a3;
    L_0x08f0:
        goto L_0x0715;
    L_0x08f2:
        r3 = "audio encoder output buffer try again later";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r22 = r2;
        r3 = r27;
        r2 = r26;
        goto L_0x00e5;
    L_0x0900:
        r3 = "audio encoder: output buffers changed";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r0 = r39;
        r3 = r0.mOutputAudioEncoder;
        r3 = r3.getOutputBuffers();
        r22 = r2;
        r2 = r3;
        r3 = r27;
        goto L_0x00e5;
    L_0x0915:
        r0 = r39;
        r3 = r0.mAudioTrackIndex;
        if (r3 >= 0) goto L_0x0940;
    L_0x091b:
        r0 = r39;
        r3 = r0.mOutputAudioEncoder;
        r3 = r3.getOutputFormat();
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "audio encoder: output format changed ";
        r4 = r4.append(r5);
        r4 = r4.append(r3);
        r4 = r4.toString();
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r22 = r2;
        r2 = r26;
        goto L_0x00e5;
    L_0x0940:
        r2 = new java.lang.RuntimeException;
        r3 = "Audio encoder output format changed after muxer is started.";
        r2.<init>(r3);
        throw r2;
    L_0x0949:
        r3 = "Unexpected result from audio encoder dequeue output format.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r22 = r2;
        r3 = r27;
        r2 = r26;
        goto L_0x00e5;
    L_0x0957:
        r3 = "audio encoder ignoring BUFFER_FLAG_CODEC_CONFIG";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r0 = r39;
        r3 = r0.mOutputAudioEncoder;
        r4 = 0;
        r3.releaseOutputBuffer(r6, r4);
        r22 = r2;
        r3 = r27;
        r2 = r26;
        goto L_0x00e5;
    L_0x096d:
        r0 = r35;
        r4 = r0.presentationTimeUs;
        r3 = (r24 > r4 ? 1 : (r24 == r4 ? 0 : -1));
        if (r3 > 0) goto L_0x0981;
    L_0x0975:
        r3 = 1;
    L_0x0976:
        if (r3 != 0) goto L_0x0983;
    L_0x0978:
        r2 = new java.io.IOException;
        r3 = "Audio time stamps are not in increasing order.";
        r2.<init>(r3);
        throw r2;
    L_0x0981:
        r3 = 0;
        goto L_0x0976;
    L_0x0983:
        r0 = r35;
        r4 = r0.presentationTimeUs;
        r0 = r39;
        r3 = r0.mMuxer;
        r0 = r39;
        r8 = r0.mAudioTrackIndex;
        r0 = r35;
        r3.writeSampleData(r8, r7, r0);
        goto L_0x08cc;
    L_0x0996:
        r3 = "audio encoder: EOS";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        r3 = 1;
        goto L_0x08d6;
    L_0x099f:
        r0 = r39;
        r4 = r0.mMuxerStarted;
        if (r4 != 0) goto L_0x00eb;
    L_0x09a5:
        if (r18 == 0) goto L_0x00eb;
    L_0x09a7:
        r0 = r39;
        r4 = r0.mCopyAudio;
        if (r4 != 0) goto L_0x09da;
    L_0x09ad:
        r0 = r39;
        r4 = r0.mMuxer;
        r0 = r18;
        r4 = r4.addTrack(r0);
        r0 = r39;
        r0.mVideoTrackIndex = r4;
        r0 = r39;
        r4 = r0.mCopyAudio;
        if (r4 != 0) goto L_0x09de;
    L_0x09c1:
        r0 = r39;
        r4 = r0.mMuxer;
        r0 = r39;
        r5 = r0.mInputOrientationDegrees;
        r4.setOrientationHint(r5);
        r0 = r39;
        r4 = r0.mMuxer;
        r4.start();
        r4 = 1;
        r0 = r39;
        r0.mMuxerStarted = r4;
        goto L_0x00eb;
    L_0x09da:
        if (r3 != 0) goto L_0x09ad;
    L_0x09dc:
        goto L_0x00eb;
    L_0x09de:
        r0 = r39;
        r4 = r0.mMuxer;
        r4 = r4.addTrack(r3);
        r0 = r39;
        r0.mAudioTrackIndex = r4;
        goto L_0x09c1;
    L_0x09eb:
        r2 = "Encoding abruptly stopped.";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x03f9;
    L_0x09f3:
        r0 = r39;
        r7 = r0.mVideoExtractor;
        r6 = r7.getTrackFormat(r6);
        r7 = -1;
        if (r3 != r7) goto L_0x0a33;
    L_0x09fe:
        r0 = r39;
        r7 = r0.mMuxerStarted;
        if (r7 == 0) goto L_0x0a3c;
    L_0x0a04:
        r0 = r39;
        r6 = r0.getVideoSampleSize(r6);
        r2 = -1;
        if (r3 == r2) goto L_0x0a82;
    L_0x0a0d:
        r2 = r4;
    L_0x0a0e:
        r6 = java.nio.ByteBuffer.allocate(r6);
        r7 = new android.media.MediaCodec$BufferInfo;
        r7.<init>();
        r0 = r39;
        r4 = r0.mVideoExtractor;
        r8 = 0;
        r4 = r4.readSampleData(r6, r8);
        r7.size = r4;
        r4 = r5;
    L_0x0a23:
        r0 = r39;
        r5 = r0.mUserStop;
        if (r5 == 0) goto L_0x0a84;
    L_0x0a29:
        r4 = -1;
        if (r3 != r4) goto L_0x0ae6;
    L_0x0a2c:
        r0 = r39;
        r2 = r0.mUserStop;
        if (r2 == 0) goto L_0x0b61;
    L_0x0a32:
        return;
    L_0x0a33:
        r0 = r39;
        r2 = r0.mAudioExtractor;
        r2 = r2.getTrackFormat(r3);
        goto L_0x09fe;
    L_0x0a3c:
        r0 = r39;
        r7 = r0.mMuxer;
        r7 = r7.addTrack(r6);
        r0 = r39;
        r0.mVideoTrackIndex = r7;
        if (r2 != 0) goto L_0x0a64;
    L_0x0a4a:
        r2 = -1;
    L_0x0a4b:
        r0 = r39;
        r3 = r0.mMuxer;
        r0 = r39;
        r7 = r0.mInputOrientationDegrees;
        r3.setOrientationHint(r7);
        r0 = r39;
        r3 = r0.mMuxer;
        r3.start();
        r3 = 1;
        r0 = r39;
        r0.mMuxerStarted = r3;
        r3 = r2;
        goto L_0x0a04;
    L_0x0a64:
        r7 = "audio/unknown";
        r8 = "mime";
        r8 = r2.getString(r8);
        r7 = r7.equals(r8);
        if (r7 != 0) goto L_0x0a4a;
    L_0x0a74:
        r0 = r39;
        r7 = r0.mMuxer;
        r2 = r7.addTrack(r2);
        r0 = r39;
        r0.mAudioTrackIndex = r2;
        r2 = r3;
        goto L_0x0a4b;
    L_0x0a82:
        r2 = 1;
        goto L_0x0a0e;
    L_0x0a84:
        if (r4 != 0) goto L_0x0a29;
    L_0x0a86:
        r5 = 0;
        r7.offset = r5;
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r8 = 0;
        r5 = r5.readSampleData(r6, r8);
        r7.size = r5;
        r5 = r7.size;
        if (r5 < 0) goto L_0x0aba;
    L_0x0a98:
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r8 = r5.getSampleTime();
        r7.presentationTimeUs = r8;
        r8 = -1;
        r5 = (r30 > r8 ? 1 : (r30 == r8 ? 0 : -1));
        if (r5 == 0) goto L_0x0ac8;
    L_0x0aa8:
        r8 = r7.presentationTimeUs;
        r5 = (r8 > r30 ? 1 : (r8 == r30 ? 0 : -1));
        if (r5 > 0) goto L_0x0ac6;
    L_0x0aae:
        r5 = 1;
    L_0x0aaf:
        if (r5 != 0) goto L_0x0ac8;
    L_0x0ab1:
        r4 = 1;
        r5 = "sawEOS: true: V";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r5);
        goto L_0x0a23;
    L_0x0aba:
        r4 = "saw input EOS: Video";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r4);
        r4 = 1;
        r5 = 0;
        r7.size = r5;
        goto L_0x0a23;
    L_0x0ac6:
        r5 = 0;
        goto L_0x0aaf;
    L_0x0ac8:
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r5 = r5.getSampleFlags();
        r7.flags = r5;
        r0 = r39;
        r5 = r0.mMuxer;
        r0 = r39;
        r8 = r0.mVideoTrackIndex;
        r5.writeSampleData(r8, r6, r7);
        r0 = r39;
        r5 = r0.mVideoExtractor;
        r5.advance();
        goto L_0x0a23;
    L_0x0ae6:
        r3 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r4 = java.nio.ByteBuffer.allocate(r3);
        r5 = new android.media.MediaCodec$BufferInfo;
        r5.<init>();
        r0 = r39;
        r3 = r0.mAudioExtractor;
        r6 = 0;
        r3 = r3.readSampleData(r4, r6);
        r5.size = r3;
    L_0x0afc:
        r0 = r39;
        r3 = r0.mUserStop;
        if (r3 != 0) goto L_0x0a2c;
    L_0x0b02:
        if (r2 != 0) goto L_0x0a2c;
    L_0x0b04:
        r3 = 0;
        r5.offset = r3;
        r0 = r39;
        r3 = r0.mAudioExtractor;
        r6 = 0;
        r3 = r3.readSampleData(r4, r6);
        r5.size = r3;
        r3 = r5.size;
        if (r3 < 0) goto L_0x0b37;
    L_0x0b16:
        r0 = r39;
        r3 = r0.mAudioExtractor;
        r6 = r3.getSampleTime();
        r5.presentationTimeUs = r6;
        r6 = -1;
        r3 = (r30 > r6 ? 1 : (r30 == r6 ? 0 : -1));
        if (r3 == 0) goto L_0x0b44;
    L_0x0b26:
        r6 = r5.presentationTimeUs;
        r3 = (r6 > r30 ? 1 : (r6 == r30 ? 0 : -1));
        if (r3 > 0) goto L_0x0b42;
    L_0x0b2c:
        r3 = 1;
    L_0x0b2d:
        if (r3 != 0) goto L_0x0b44;
    L_0x0b2f:
        r2 = 1;
        r3 = "sawEOS: true: A";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r3);
        goto L_0x0afc;
    L_0x0b37:
        r2 = "saw input EOS: Audio";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        r2 = 1;
        r3 = 0;
        r5.size = r3;
        goto L_0x0afc;
    L_0x0b42:
        r3 = 0;
        goto L_0x0b2d;
    L_0x0b44:
        r0 = r39;
        r3 = r0.mAudioExtractor;
        r3 = r3.getSampleFlags();
        r5.flags = r3;
        r0 = r39;
        r3 = r0.mMuxer;
        r0 = r39;
        r6 = r0.mAudioTrackIndex;
        r3.writeSampleData(r6, r4, r5);
        r0 = r39;
        r3 = r0.mAudioExtractor;
        r3.advance();
        goto L_0x0afc;
    L_0x0b61:
        r2 = "Rewriting finished";
        com.samsung.android.transcode.core.EncodeVideo.Debugger.log(r2);
        goto L_0x0a32;
    L_0x0b69:
        r3 = move-exception;
        r12 = r2;
        r2 = r3;
        goto L_0x05ce;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.transcode.core.EncodeVideo.startTransRewriting():void");
    }

    public void stop() {
        Log.d(Constants.TAG, "Stop method called ");
        synchronized (this.mStopLock) {
            if (this.mOutputSurface != null) {
                this.mOutputSurface.notifyFrameSyncObject();
            }
            this.mUserStop = true;
            if (this.mEncoding) {
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
