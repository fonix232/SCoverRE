package com.samsung.android.aod;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.aod.IAODManager.Stub;
import java.util.List;

public class AODManager {
    private static final String TAG = "AODManager";
    Context mContext;
    private IAODManager mService;

    public interface AODChangeListener {
        void readyToScreenTurningOn();
    }

    public AODManager(Context context, IAODManager iAODManager) {
        this.mContext = context;
        this.mService = iAODManager;
    }

    public static AODManager getInstance(Context context) {
        return (AODManager) context.getSystemService("AODManagerService");
    }

    private IAODManager getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("AODManagerService"));
        }
        if (this.mService == null) {
            Log.wtf(TAG, "getService fail!");
        }
        return this.mService;
    }

    public boolean isAODState() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.isAODState();
        } catch (Throwable e) {
            throw new RuntimeException("AODManagerService dead?", e);
        }
    }

    public void readyToScreenTurningOn() {
        if (getService() != null) {
            try {
                this.mService.readyToScreenTurningOn();
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void requestCalendarData() {
        if (getService() != null) {
            try {
                this.mService.requestCalendarData();
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void requestNotificationKeys() {
        if (getService() != null) {
            try {
                this.mService.requestNotificationKeys();
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public int setLiveClockInfo(int i, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8) {
        if (getService() == null) {
            return -1;
        }
        try {
            return this.mService.setLiveClockInfo(i, j, j2, j3, j4, j5, j6, j7, j8);
        } catch (Throwable e) {
            throw new RuntimeException("AODManagerService dead?", e);
        }
    }

    public void setLiveClockNeedle(byte[] bArr) {
        if (getService() != null) {
            try {
                this.mService.setLiveClockNeedle(bArr);
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void updateAODTspRect(int i, int i2, int i3, int i4) {
        if (getService() != null) {
            try {
                this.mService.updateAODTspRect(i, i2, i3, i4);
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void updateCalendarData(List<String> list, List<String> list2) {
        if (getService() != null) {
            try {
                this.mService.updateCalendarData(list, list2);
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void updateNotificationKeys(int i, List<String> list) {
        if (getService() != null) {
            try {
                this.mService.updateNotificationKeys(i, list);
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }

    public void writeAODCommand(String str, String str2, String str3, String str4, String str5) {
        if (getService() != null) {
            try {
                this.mService.writeAODCommand(str, str2, str3, str4, str5);
            } catch (Throwable e) {
                throw new RuntimeException("AODManagerService dead?", e);
            }
        }
    }
}
