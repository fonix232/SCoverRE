package com.samsung.android.contextaware.utilbundle.logger;

import android.os.Environment;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CaFileLogger {
    private static final int LOGTYPE_GPSPOS = 1;
    private static final String LOG_FILE_DIR_NAME = ("log" + File.separator + "cae");
    private static volatile CaFileLogger instance;
    private final Map<String, DataOutputStream> mDataOutputStream = new HashMap();
    private final Map<String, File> mFile = new HashMap();

    public static CaFileLogger getInstance() {
        if (instance == null) {
            synchronized (CaFileLogger.class) {
                if (instance == null) {
                    instance = new CaFileLogger();
                }
            }
        }
        return instance;
    }

    private File getLogFile(String str, File file) throws IOException {
        Calendar instance = Calendar.getInstance();
        long j = (long) instance.get(1);
        long j2 = (long) (instance.get(2) + 1);
        long j3 = (long) instance.get(5);
        long j4 = (long) instance.get(11);
        long j5 = (long) instance.get(12);
        long j6 = (long) instance.get(13);
        String format = String.format("%04dY%02dM%02dD%02dH%02dM%02dS_" + str + ".log", new Object[]{Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5), Long.valueOf(j6)});
        CaLogger.info("logDir = " + file.toString());
        CaLogger.info("fileName = " + format);
        File file2 = new File(file, format);
        if (file2.createNewFile()) {
            return file2;
        }
        CaLogger.error("createNewFile() error");
        return null;
    }

    private File getLogStorageDir() {
        File file = new File(Environment.getDataDirectory().getAbsolutePath() + File.separator + LOG_FILE_DIR_NAME + File.separator);
        if (file == null) {
            try {
                CaLogger.error("External storage directory is null");
                return null;
            } catch (Throwable e) {
                CaLogger.error(e.toString());
            }
        } else if (!file.exists() || file.isDirectory()) {
            if (!(file.exists() || file.mkdir())) {
                CaLogger.error("Unable to create directory: " + file.getAbsolutePath());
                return null;
            }
            return file;
        } else {
            CaLogger.error(file.getAbsolutePath() + " already exists and is not a directory");
            return null;
        }
    }

    public final void logging(String str, String str2) {
        if (this.mFile.containsKey(str) && this.mDataOutputStream.containsKey(str)) {
            try {
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeBytes(str2 + System.getProperty("line.separator"));
            } catch (Throwable e) {
                CaLogger.exception(e);
            }
            return;
        }
        CaLogger.error("This file dose not exist.");
    }

    public final void loggingForKML(String str, long j, double[] dArr, float[] fArr, long j2) {
        if (!this.mFile.containsKey(str) || !this.mDataOutputStream.containsKey(str)) {
            CaLogger.error("This file dose not exist.");
        } else if (dArr != null && dArr.length > 3 && fArr != null && fArr.length > 3) {
            try {
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeInt(1);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeLong(j);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeFloat(fArr[0]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeDouble(dArr[0]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeDouble(dArr[1]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeDouble(dArr[2]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeFloat(fArr[1]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeFloat(fArr[2]);
                ((DataOutputStream) this.mDataOutputStream.get(str)).writeLong(j2);
            } catch (Throwable e) {
                CaLogger.exception(e);
            }
        }
    }

    public final boolean startLogging(String str) {
        if (this.mFile.containsKey(str) || this.mDataOutputStream.containsKey(str)) {
            CaLogger.error("This file is created already.");
            return false;
        }
        try {
            File logStorageDir = getLogStorageDir();
            if (logStorageDir == null) {
                CaLogger.error("Log directory is null");
                return false;
            }
            File logFile = getLogFile(str, logStorageDir);
            if (logFile == null) {
                CaLogger.error("Log file is null");
                return false;
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(logFile));
            this.mFile.put(str, logFile);
            this.mDataOutputStream.put(str, dataOutputStream);
            return true;
        } catch (Throwable e) {
            CaLogger.exception(e);
            return false;
        } catch (Throwable e2) {
            CaLogger.exception(e2);
            return false;
        } catch (Throwable e3) {
            CaLogger.exception(e3);
            return false;
        }
    }

    public final boolean stopLogging(String str) {
        if (!this.mFile.containsKey(str) || !this.mDataOutputStream.containsKey(str)) {
            return false;
        }
        try {
            ((DataOutputStream) this.mDataOutputStream.get(str)).close();
            this.mFile.remove(str);
            this.mDataOutputStream.remove(str);
            return true;
        } catch (Throwable e) {
            CaLogger.exception(e);
            return false;
        }
    }
}
