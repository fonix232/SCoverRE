package com.samsung.android.media;

import android.util.Log;
import com.samsung.android.media.SemExtendedFormat.AudioJPEGData;
import com.samsung.android.media.SemExtendedFormat.QdioJPEGData;
import java.io.FileInputStream;
import java.io.IOException;

public class QdioJNI {
    private static final String TAG = "QdioJNI";

    static {
        System.loadLibrary("SEF");
    }

    private static native int AddSoundInQdioFile(String str, byte[] bArr, int i, String str2, int i2);

    public static native int DeleteQdioFromFile(String str);

    private static native int[] ParseQdioFile(String str);

    private static native long[] ParseQdioFile64(String str);

    public static QdioJPEGData checkAudioInJPEG(String str) {
        QdioJPEGData qdioJPEGData = null;
        if (checkFileString(str)) {
            int[] ParseQdioFile = ParseQdioFile(str);
            if (ParseQdioFile == null) {
                return null;
            }
            if (ParseQdioFile.length % 2 != 0) {
                Log.e(TAG, "Some Sound Data is broken");
                return null;
            }
            QdioJPEGData qdioJPEGData2 = new QdioJPEGData();
            int i = 0;
            while (i < ParseQdioFile.length / 2) {
                if (ParseQdioFile[i] <= 0 || ParseQdioFile[i + 1] <= 0) {
                    Log.e(TAG, "Some Sound Data stream is broken");
                    return null;
                }
                qdioJPEGData2.startOffset.add(Integer.valueOf(ParseQdioFile[i]));
                qdioJPEGData2.endOffset.add(Integer.valueOf(ParseQdioFile[i + 1]));
                qdioJPEGData2.audio_count++;
                qdioJPEGData2.filename = str;
                i++;
            }
            return qdioJPEGData2;
        }
        Log.e(TAG, "checkAudioInJPEG input parameter is null : filename = " + str);
        return null;
    }

    public static boolean checkFileString(String str) {
        return str != null && str.length() > 0;
    }

    private static native int copyAdioData(String str, String str2);

    public static int copyAdioInJPEGtoPNG(String str, String str2) {
        return (checkFileString(str) && checkFileString(str2)) ? copyAdioData(str, str2) : 0;
    }

    public static AudioJPEGData getAudioDataInJPEG(String str) {
        AudioJPEGData audioJPEGData = null;
        if (checkFileString(str)) {
            int[] ParseQdioFile = ParseQdioFile(str);
            if (ParseQdioFile == null) {
                return null;
            }
            if (ParseQdioFile.length % 2 != 0) {
                Log.e(TAG, "Some Sound Data is broken");
                return null;
            }
            AudioJPEGData audioJPEGData2 = new AudioJPEGData();
            int i = 0;
            while (i < ParseQdioFile.length / 2) {
                if (ParseQdioFile[i] <= 0 || ParseQdioFile[i + 1] <= 0) {
                    Log.e(TAG, "Some Sound Data stream is broken");
                    return null;
                }
                audioJPEGData2.startOffset.add(Integer.valueOf(ParseQdioFile[i]));
                audioJPEGData2.endOffset.add(Integer.valueOf(ParseQdioFile[i + 1]));
                audioJPEGData2.audio_count++;
                audioJPEGData2.filename = str;
                i++;
            }
            return audioJPEGData2;
        }
        Log.e(TAG, "getAudioDataInJPEG input parameter is null : filename = " + str);
        return null;
    }

    public static long[] getAudioDataPositionArray(String str) {
        long[] jArr = null;
        if (checkFileString(str)) {
            int[] ParseQdioFile = ParseQdioFile(str);
            if (ParseQdioFile == null) {
                return null;
            }
            if (ParseQdioFile.length % 2 != 0) {
                Log.e(TAG, "Some Sound Data is broken");
                return null;
            }
            return new long[]{(long) ParseQdioFile[0], ((long) ParseQdioFile[1]) - r1[0]};
        }
        Log.e(TAG, "getAudioPositionArray input parameter is null : filename = " + str);
        return null;
    }

    public static byte[] getAudioStreamBuffer(AudioJPEGData audioJPEGData, int i) throws IOException {
        byte[] bArr = null;
        if (audioJPEGData == null) {
            Log.e(TAG, "qdioJpegData is null");
            return null;
        } else if (audioJPEGData.audio_count <= i) {
            Log.e(TAG, "invalid index. file : " + audioJPEGData.getFileName() + " has " + audioJPEGData.audio_count + " audio streams but index = " + i);
            return null;
        } else {
            FileInputStream fileInputStream = new FileInputStream(audioJPEGData.getFileName());
            int startOffset = audioJPEGData.getStartOffset(i);
            int length = startOffset + audioJPEGData.getLength(i);
            if (fileInputStream.available() < length) {
                Log.e(TAG, "fis.available is smaller then audio stream end : fileLen = " + fileInputStream.available() + ", audio strema end on " + length);
                fileInputStream.close();
                return null;
            }
            try {
                Log.i(TAG, "fis.avaliable = " + fileInputStream.available());
                Log.i(TAG, "s = " + startOffset + ", " + length);
                bArr = new byte[(length - startOffset)];
                if (startOffset < 0) {
                    return null;
                }
                if (fileInputStream.skip((long) startOffset) == 0) {
                    fileInputStream.close();
                    return null;
                } else if (fileInputStream.read(bArr) == 0) {
                    fileInputStream.close();
                    return null;
                } else {
                    fileInputStream.close();
                    return bArr;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                fileInputStream.close();
            }
        }
    }

    public static byte[] getAudioStreamBuffer(QdioJPEGData qdioJPEGData, int i) throws IOException {
        byte[] bArr = null;
        if (qdioJPEGData == null) {
            Log.e(TAG, "qdioJpegData is null");
            return null;
        } else if (qdioJPEGData.audio_count <= i) {
            Log.e(TAG, "invalid index. file : " + qdioJPEGData.getFileName() + " has " + qdioJPEGData.audio_count + " audio streams but index = " + i);
            return null;
        } else {
            FileInputStream fileInputStream = new FileInputStream(qdioJPEGData.getFileName());
            int startOffset = qdioJPEGData.getStartOffset(i);
            int length = startOffset + qdioJPEGData.getLength(i);
            if (fileInputStream.available() < length) {
                Log.e(TAG, "fis.available is smaller then audio stream end : fileLen = " + fileInputStream.available() + ", audio strema end on " + length);
                fileInputStream.close();
                return null;
            }
            try {
                Log.i(TAG, "fis.avaliable = " + fileInputStream.available());
                Log.i(TAG, "s = " + startOffset + ", " + length);
                bArr = new byte[(length - startOffset)];
                if (startOffset < 0) {
                    return null;
                }
                if (fileInputStream.skip((long) startOffset) == 0) {
                    fileInputStream.close();
                    return null;
                } else if (fileInputStream.read(bArr) == 0) {
                    fileInputStream.close();
                    return null;
                } else {
                    fileInputStream.close();
                    return bArr;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                fileInputStream.close();
            }
        }
    }

    private static native int getNativeVersion();

    public static String getVersion() {
        return "1.02_" + getNativeVersion();
    }

    public static int isJPEG(String str) {
        int i = -1;
        if (checkFileString(str)) {
            if (isQdioFile(str) != 0) {
                i = 1;
            }
            return i;
        }
        Log.e(TAG, "isJPEG input parameter is null : filename = " + str);
        return -1;
    }

    private static native int isQdioFile(String str);
}
