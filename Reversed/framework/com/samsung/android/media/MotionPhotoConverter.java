package com.samsung.android.media;

import com.samsung.android.media.SemExtendedFormat.KeyName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MotionPhotoConverter {
    private static final String TAG = "MotionPhotoConverter";
    private static MotionPhotoConverter sInstance;

    private MotionPhotoConverter() {
    }

    public static synchronized MotionPhotoConverter getInstance() {
        MotionPhotoConverter motionPhotoConverter;
        synchronized (MotionPhotoConverter.class) {
            if (sInstance == null) {
                sInstance = new MotionPhotoConverter();
            }
            motionPhotoConverter = sInstance;
        }
        return motionPhotoConverter;
    }

    public synchronized void convertToMp4(String str, String str2) {
        Throwable e;
        Throwable th;
        Throwable th2 = null;
        synchronized (this) {
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(str2);
                try {
                    fileOutputStream2.write(SemExtendedFormat.getData(new File(str), KeyName.MOTION_PHOTO_DATA));
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (Throwable th3) {
                            th2 = th3;
                        }
                    }
                    if (th2 != null) {
                        try {
                            throw th2;
                        } catch (IOException e2) {
                            e = e2;
                            fileOutputStream = fileOutputStream2;
                        } catch (Throwable th4) {
                            th = th4;
                            fileOutputStream = fileOutputStream2;
                            throw th;
                        }
                    }
                    fileOutputStream = fileOutputStream2;
                } catch (Throwable th5) {
                    th = th5;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th6) {
                            if (th2 == null) {
                                th2 = th6;
                            } else if (th2 != th6) {
                                th2.addSuppressed(th6);
                            }
                        }
                    }
                    if (th2 == null) {
                        try {
                            throw th2;
                        } catch (IOException e3) {
                            e = e3;
                        }
                    } else {
                        throw th;
                    }
                }
            } catch (Throwable th7) {
                th = th7;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (th2 == null) {
                    throw th;
                } else {
                    throw th2;
                }
            }
        }
        try {
            e.printStackTrace();
        } catch (Throwable th8) {
            th = th8;
            throw th;
        }
    }
}
