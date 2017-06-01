package com.samsung.android.transcode;

import android.graphics.Rect;
import com.samsung.android.edge.EdgeLightingPolicyInfo;
import com.samsung.android.media.SemExtendedFormat;
import com.samsung.android.transcode.core.Encode.EncodeResolution;
import com.samsung.android.transcode.core.EncodeImages;
import com.samsung.android.transcode.util.CodecsHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class EncodeSoundNShot extends EncodeImages {
    private static final int SOUND_PCM_WAV = 256;
    private static final int SOUND_SHOT_INFO = 2048;
    private String mSoundNShotFilePath;

    public static class SEFDataPosition {
        public int length;
        public int offset;
    }

    public EncodeSoundNShot(String str, int i, int i2, String str2) {
        if (i <= 0) {
            throw new IllegalArgumentException("width cannot be equal to or less than 0");
        } else if (i2 <= 0) {
            throw new IllegalArgumentException("height cannot be equal to or less than 0");
        } else if (str == null) {
            throw new NullPointerException("File path cannot be null");
        } else if (str2 == null) {
            throw new NullPointerException("soundNshot path cannot be null");
        } else if (isSoundNShot(str2)) {
            this.mSoundNShotFilePath = str2;
            initialize(str, i, i2, -1, new String[]{str2});
        } else {
            throw new IllegalArgumentException("File is not Sound and Shot.");
        }
    }

    public EncodeSoundNShot(String str, int i, String str2) {
        if (!EncodeResolution.isSupportedResolution(i)) {
            throw new IllegalArgumentException("Invalid resolution value");
        } else if (str == null) {
            throw new NullPointerException("File path cannot be null");
        } else if (str2 == null) {
            throw new NullPointerException("soundNshot path cannot be null");
        } else if (isSoundNShot(str2)) {
            Rect rect = new Rect();
            CodecsHelper.fillResolutionRect(i, rect);
            this.mSoundNShotFilePath = str2;
            initialize(str, rect.width(), rect.height(), -1, new String[]{str2});
        } else {
            throw new IllegalArgumentException("File is not Sound and Shot.");
        }
    }

    private static int convertSDRDH(int i) {
        return ((i & 255) << 8) | ((EdgeLightingPolicyInfo.RANGE_PRIVATE_MASK & i) >> 8);
    }

    private static SEFDataPosition getSEFDataPosition(File file) throws IOException {
        Throwable th;
        int i = 0;
        SEFDataPosition sEFDataPosition = new SEFDataPosition();
        String str = "SEFT";
        String str2 = "IOBS";
        RandomAccessFile randomAccessFile;
        try {
            byte[] bArr = new byte[4];
            randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
            try {
                randomAccessFile.seek(file.length() - 4);
                if (randomAccessFile.read(bArr) > 0) {
                    int little2big;
                    String str3 = new String(bArr);
                    if (str3.equals(str)) {
                        randomAccessFile.seek(file.length() - ((long) 8));
                        little2big = little2big(randomAccessFile.readInt()) + 8;
                    } else if (str3.equals(str2)) {
                        randomAccessFile.seek((file.length() - ((long) 8)) - 30);
                        little2big = (little2big(randomAccessFile.readInt()) + 8) + 30;
                    } else {
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                        return null;
                    }
                    byte[] bArr2 = new byte[4];
                    randomAccessFile.read(bArr2);
                    randomAccessFile.seek(0);
                    randomAccessFile.seek(file.length() - ((long) little2big));
                    randomAccessFile.read(bArr2);
                    little2big(randomAccessFile.readInt());
                    int little2big2 = little2big(randomAccessFile.readInt());
                    int[] iArr = new int[little2big2];
                    int[] iArr2 = new int[little2big2];
                    int[] iArr3 = new int[little2big2];
                    for (int i2 = 0; i2 < little2big2; i2++) {
                        iArr[i2] = convertSDRDH(randomAccessFile.readInt());
                        iArr2[i2] = little2big(randomAccessFile.readInt()) - 21;
                        iArr3[i2] = little2big(randomAccessFile.readInt()) - 21;
                    }
                    while (i < little2big2) {
                        if (iArr[i] != 256) {
                            i++;
                        } else {
                            sEFDataPosition.offset = (int) ((file.length() - ((long) little2big)) - ((long) iArr2[i]));
                            sEFDataPosition.length = iArr3[i];
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                            return sEFDataPosition;
                        }
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    return null;
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile = null;
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
            throw th;
        }
    }

    private boolean isSoundNShot(String str) {
        File file = new File(str);
        try {
            return SemExtendedFormat.isValidFile(file) && SemExtendedFormat.hasData(file, 2048);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int little2big(int i) {
        return ((((i & 255) << 24) | ((EdgeLightingPolicyInfo.RANGE_PRIVATE_MASK & i) << 8)) | ((16711680 & i) >> 8)) | ((i >> 24) & 255);
    }

    protected void prepare() throws IOException {
        Throwable th;
        File file = null;
        FileInputStream fileInputStream;
        try {
            File file2 = new File(this.mSoundNShotFilePath);
            try {
                fileInputStream = new FileInputStream(file2);
                try {
                    SEFDataPosition sEFDataPosition = getSEFDataPosition(file2);
                    setAudioData(fileInputStream.getFD(), sEFDataPosition.offset, sEFDataPosition.length);
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    setMaxOutputSize((int) (file2.length() / 1024));
                    super.prepare();
                } catch (Throwable th2) {
                    th = th2;
                    file = file2;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    setMaxOutputSize((int) (file.length() / 1024));
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = null;
                file = file2;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                setMaxOutputSize((int) (file.length() / 1024));
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = null;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            setMaxOutputSize((int) (file.length() / 1024));
            throw th;
        }
    }
}
