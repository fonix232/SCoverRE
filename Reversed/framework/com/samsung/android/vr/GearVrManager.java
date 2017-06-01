package com.samsung.android.vr;

import android.app.IVRManager;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;

public final class GearVrManager implements IVRManager, IGearVrManagerLocal, SemGearVrManager {
    public static final String FEATURE_HMT = "com.samsung.feature.hmt";
    public static final int HMT_EVENT_ABNORMAL = 4;
    public static final int HMT_EVENT_DOCK = 1;
    public static final int HMT_EVENT_MOUNT = 16;
    public static final int HMT_EVENT_SENSOR_BOOTING_WITHOUT_TA = 256;
    public static final int HMT_EVENT_SENSOR_BOOTING_WITH_TA = 512;
    public static final int HMT_EVENT_SENSOR_CONNECTED_TA = 1024;
    public static final int HMT_EVENT_UNDOCK = 2;
    public static final int HMT_EVENT_UNMOUNT = 32;
    public static final String PACKAGENAME_VRSERVICE = "com.samsung.android.hmt.vrsvc";
    public static final String PACKAGENAME_VRSETUPWIZARD = "com.samsung.android.app.vrsetupwizard";
    public static final String PACKAGENAME_VRSETUPWIZARD_STUB = "com.samsung.android.app.vrsetupwizardstub";
    public static final String PACKAGENAME_VRSYSTEM = "com.samsung.android.hmt.vrsystem";
    private Context mContext;
    private IGearVrManagerService mService;
    private final IBinder mToken = new Binder();

    public GearVrManager(Context context, IGearVrManagerService iGearVrManagerService) {
        this.mContext = context;
        this.mService = iGearVrManagerService;
    }

    @Deprecated
    public int GetPowerLevelState() {
        return getPowerLevelState();
    }

    @Deprecated
    public int[] SetVrClocks(String str, int i, int i2) {
        return acquireVrClocks(str, i, i2);
    }

    public int[] acquireVrClocks(String str, int i, int i2) {
        try {
            int[] acquireVrClocks;
            synchronized (this.mToken) {
                acquireVrClocks = this.mService.acquireVrClocks(this.mToken, str, i, i2);
            }
            return acquireVrClocks;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void enforceCallingPermission(int i, int i2, String str) throws SecurityException {
        try {
            this.mService.enforceCallingPermission(i, i2, str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void enforceCallingSelfPermission(String str) throws SecurityException {
        try {
            this.mService.enforceCallingSelfPermission(str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public String getOption(String str) {
        throw new RuntimeException("Not support");
    }

    public int getPowerLevelState() {
        try {
            return this.mService.getPowerLevelState();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public String getSystemOption(String str) {
        try {
            return this.mService.getSystemOption(str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getThreadId(int i, String str, int i2) {
        try {
            return this.mService.getThreadId(i, str, i2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public int getVRBright() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public int getVRColorTemperature() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public boolean isConnected() {
        throw new RuntimeException("Not support");
    }

    public boolean isDock() {
        try {
            return this.mService.isDock();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isMount() {
        try {
            return this.mService.isMount();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public boolean isVRComfortableViewEnabled() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public boolean isVRDarkAdaptationEnabled() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public boolean isVRLowPersistenceEnabled() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public boolean isVRMode() {
        return isVrMode();
    }

    public boolean isVrMode() {
        try {
            return this.mService.isVrMode();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public String readSysNode(String str) {
        try {
            return this.mService.readSysNode(str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public boolean relFreq(String str) {
        return releaseVrClocks(str);
    }

    @Deprecated
    public void releaseCPUMhz(String str) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void releaseGPUMhz(String str) {
        throw new RuntimeException("Not support");
    }

    public boolean releaseVrClocks(String str) {
        try {
            boolean releaseVrClocks;
            synchronized (this.mToken) {
                releaseVrClocks = this.mService.releaseVrClocks(this.mToken, str);
            }
            return releaseVrClocks;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeSysNode(String str) {
        try {
            return this.mService.removeSysNode(str);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int[] retrieveEnableFrequencyLevels() {
        try {
            return this.mService.retrieveEnableFrequencyLevels();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int[] return2EnableFreqLev() {
        return retrieveEnableFrequencyLevels();
    }

    @Deprecated
    public int setAffinity(int i, int[] iArr) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public int[] setCPUClockMhz(String str, int[] iArr, int i) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public int setGPUClockMhz(String str, int i) {
        throw new RuntimeException("Not support");
    }

    public void setHomeKeyBlocked(boolean z) {
        try {
            this.mService.setHomeKeyBlocked(z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void setOption(String str, String str2) {
        throw new RuntimeException("Not support");
    }

    public void setOverlayRestriction(boolean z, String[] strArr, int i) {
        try {
            this.mService.setOverlayRestriction(z, strArr, i);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int setPermissions(String str, int i, int i2, int i3) {
        try {
            return this.mService.setPermissions(str, i, i2, i3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setReadyForVrMode(boolean z) {
        try {
            this.mService.setReadyForVrMode(z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystemMouseControlType(int i) {
        try {
            this.mService.setSystemMouseControlType(i);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystemMouseShowMouseEnabled(boolean z) {
        try {
            this.mService.setSystemMouseShowMouseEnabled(z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setSystemOption(String str, String str2) {
        try {
            this.mService.setSystemOption(str, str2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public int setThreadAffinity(int i, int[] iArr) {
        try {
            return this.mService.setThreadAffinity(i, iArr);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setThreadGroup(int i, int i2) {
        try {
            return this.mService.setThreadGroup(i, i2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setThreadSchedFifo(String str, int i, int i2, int i3) {
        try {
            return this.mService.setThreadSchedFifo(str, i, i2, i3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setThreadScheduler(int i, int i2, int i3) {
        try {
            return this.mService.setThreadScheduler(i, i2, i3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public void setVRBright(int i) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void setVRColorTemperature(int i) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void setVRComfortableView(boolean z) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void setVRDarkAdaptation(boolean z) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void setVRLowPersistence(boolean z) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public void setVRMode(boolean z) {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public boolean setVideoMode(String str, float f, boolean z) {
        throw new RuntimeException("Not support");
    }

    public void setVrMode(boolean z) {
        try {
            this.mService.setVrMode(z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public String vrManagerVersion() {
        throw new RuntimeException("Not support");
    }

    @Deprecated
    public String vrOVRVersion() {
        throw new RuntimeException("Not support");
    }

    public boolean writeSysNode(String str, String str2, boolean z) {
        try {
            return this.mService.writeSysNode(str, str2, z);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
