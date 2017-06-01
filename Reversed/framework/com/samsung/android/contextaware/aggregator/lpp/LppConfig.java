package com.samsung.android.contextaware.aggregator.lpp;

import android.content.Context;
import android.os.Looper;
import java.io.Serializable;

public class LppConfig implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean GPSAlways = true;
    public int GPSKeepOn_Timer = 15;
    public int GPSRequest_APDR = 100;
    private int GPSRequest_By = 0;
    public int GPSRequest_Timer = 45;
    private String LogConfig = "[LPPTest Configuration]\r\n";
    private boolean[] LogFlags = new boolean[]{false, false, false, false};
    private String LogFolderName = "LPPTest";
    private boolean flag_log = false;
    public transient Looper looper;
    private Context mcontext;

    public LppConfig(Context context, int i, int i2, int i3) {
        this.GPSRequest_APDR = i;
        this.GPSRequest_Timer = i2;
        this.GPSKeepOn_Timer = i3;
        this.mcontext = context;
    }

    public LppConfig(LppConfig lppConfig) {
        boolean[] logFlags = lppConfig.getLogFlags();
        for (int i = 0; i < 4; i++) {
            this.LogFlags[i] = logFlags[i];
        }
        this.GPSRequest_APDR = lppConfig.GPSRequest_APDR;
        this.GPSRequest_Timer = lppConfig.GPSRequest_Timer;
        this.GPSKeepOn_Timer = lppConfig.GPSKeepOn_Timer;
        this.LogConfig = lppConfig.getConfigStr();
        this.LogFolderName = lppConfig.getLogFolderNameStr();
        this.mcontext = lppConfig.getContext();
    }

    public LppConfig(boolean z, boolean z2, boolean z3, boolean z4, int i, int i2, int i3, int i4, String str, String str2) {
        this.LogFlags[0] = z;
        this.LogFlags[1] = z2;
        this.LogFlags[2] = z3;
        this.LogFlags[3] = z4;
        this.GPSRequest_By = i;
        this.GPSRequest_APDR = i2;
        this.GPSRequest_Timer = i3;
        this.GPSKeepOn_Timer = i4;
        this.LogConfig = str;
        this.LogFolderName = str2;
    }

    public LppConfig(boolean z, boolean z2, boolean z3, boolean z4, int i, boolean z5, String str) {
        this.LogFlags[0] = z;
        this.LogFlags[1] = z2;
        this.LogFlags[2] = z3;
        this.LogFlags[3] = z4;
        this.GPSAlways = z5;
        this.LogFolderName = str;
    }

    public String getConfigStr() {
        return this.LogConfig;
    }

    public Context getContext() {
        return this.mcontext;
    }

    public boolean getLogCommand() {
        return this.flag_log;
    }

    public boolean[] getLogFlags() {
        return this.LogFlags;
    }

    public String getLogFolderNameStr() {
        return this.LogFolderName;
    }

    public void setContext(Context context) {
        this.mcontext = context;
    }

    public void setLogParameter(boolean z, boolean z2, boolean z3, boolean z4, String str, String str2) {
        this.flag_log = true;
        this.LogFlags[0] = z;
        this.LogFlags[1] = z2;
        this.LogFlags[2] = z3;
        this.LogFlags[3] = z4;
        this.LogConfig = str;
        this.LogFolderName = str2;
    }
}
