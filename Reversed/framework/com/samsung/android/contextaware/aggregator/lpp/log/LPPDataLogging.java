package com.samsung.android.contextaware.aggregator.lpp.log;

import android.os.Binder;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class LPPDataLogging extends DataLogging {
    String FileType = "log";
    String TAG = "LPPDataLogging";

    LPPDataLogging(String str) {
        this.strLogFileName = str;
    }

    public void LogInit(String str) {
        super.LogInit();
        Log.m29d(this.TAG, "LogInit : " + this.saveFilePath + str);
        Calendar instance = Calendar.getInstance();
        long j = (long) instance.get(1);
        long j2 = (long) (instance.get(2) + 1);
        long j3 = (long) instance.get(5);
        long j4 = (long) instance.get(11);
        long j5 = (long) instance.get(12);
        long j6 = (long) instance.get(13);
        this.saveFilePath += String.format(str + "_%04d%02d%02d_%02d%02d%02d/", new Object[]{Long.valueOf(j), Long.valueOf(j2), Long.valueOf(j3), Long.valueOf(j4), Long.valueOf(j5), Long.valueOf(j6)});
        try {
            File file = new File(this.saveFilePath);
            if (!file.exists() && !file.mkdirs()) {
                Log.m31e(this.TAG, "Unable to create dir:" + this.saveFilePath);
            }
        } catch (Throwable e) {
            Log.m31e(this.TAG, "error in dir" + e.toString());
        }
    }

    public void addLogStream(String str) {
        this.m_LoggingData.add(str);
    }

    public void createFileToLog() {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            Log.m29d(this.TAG, "[DataLogging] strLogFileName:" + this.strLogFileName);
            this.m_fileOutStream = new FileOutputStream(new File(this.saveFilePath + this.strLogFileName + "." + this.FileType));
        } catch (Throwable e) {
            Log.m31e(this.TAG, "error in createFileToLog:" + e.toString());
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    public void setFileType(String str) {
        this.FileType = str;
    }
}
