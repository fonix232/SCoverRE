package com.samsung.android.hardware;

import com.samsung.android.smartface.SmartFaceManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SemHardwareInterface {
    private static String EPEN_SAVINGMODE_PATH = "/sys/class/sec/sec_epen/epen_saving_mode";
    private static final String TAG = "SemHardwareInterface";
    public static final int TORCH_BRIGHTNESS_MAX = 3;
    public static final int TORCH_BRIGHTNESS_MIN = 1;
    public static final int TORCH_BRIGHTNESS_OFF = 0;
    public static final int TORCH_BRIGHTNESS_STANDARD = 2;
    private static final int[] mLevelTable = new int[]{0, 1, 10, 14};

    private SemHardwareInterface() {
    }

    public static boolean setEPenSavingmode(int i) {
        return sysfsWrite(EPEN_SAVINGMODE_PATH, i);
    }

    public static void setTorchLight(int i) {
        Throwable e;
        Throwable e2;
        Throwable th;
        Writer writer;
        OutputStreamWriter outputStreamWriter = null;
        i = mLevelTable[i];
        String str = "/sys/class/camera/flash/rear_flash";
        try {
            if (!new File(str).exists()) {
                str = "/sys/class/camera/rear/rear_flash";
            }
            Writer fileWriter = new FileWriter(str);
            if (i <= 0 || i >= 15) {
                fileWriter.write(SmartFaceManager.PAGE_MIDDLE);
            } else {
                try {
                    fileWriter.write(Integer.toString(i));
                } catch (FileNotFoundException e3) {
                    e = e3;
                    outputStreamWriter = fileWriter;
                    try {
                        e.printStackTrace();
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (Throwable e22) {
                                e22.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (Throwable e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    writer = fileWriter;
                    e222.printStackTrace();
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (Throwable e2222) {
                            e2222.printStackTrace();
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    writer = fileWriter;
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                    throw th;
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Throwable e22222) {
                    e22222.printStackTrace();
                }
            }
            writer = fileWriter;
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        } catch (IOException e6) {
            e22222 = e6;
            e22222.printStackTrace();
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
    }

    public static void setTorchLight(boolean z) {
        if (z) {
            setTorchLight(1);
        } else {
            setTorchLight(0);
        }
    }

    private static boolean sysfsWrite(String str, int i) {
        Throwable e;
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str));
            try {
                fileOutputStream2.write(Integer.toString(i).getBytes());
                fileOutputStream2.close();
                return true;
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = fileOutputStream2;
                e.printStackTrace();
                try {
                    fileOutputStream.close();
                } catch (Throwable e3) {
                    e3.printStackTrace();
                }
                return false;
            }
        } catch (Throwable e4) {
            try {
                e4.printStackTrace();
                return false;
            } catch (IOException e5) {
                e = e5;
                e.printStackTrace();
                fileOutputStream.close();
                return false;
            }
        }
    }
}
