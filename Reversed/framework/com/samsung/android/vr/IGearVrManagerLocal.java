package com.samsung.android.vr;

public interface IGearVrManagerLocal {
    public static final int TYPE_GET_THREAD_CONTAINS = 5;
    public static final int TYPE_GET_THREAD_ENDS_WITH = 4;
    public static final int TYPE_GET_THREAD_EQUALS = 1;
    public static final int TYPE_GET_THREAD_EQUALS_IGNORE_CASE = 2;
    public static final int TYPE_GET_THREAD_STARTS_WITH = 3;
    public static final int VR_LOCAL_API_VERSION_CODE = 2;

    int[] getThreadId(int i, String str, int i2);

    boolean isVrMode();

    String readSysNode(String str);

    boolean removeSysNode(String str);

    void setHomeKeyBlocked(boolean z);

    void setOverlayRestriction(boolean z, String[] strArr, int i);

    int setPermissions(String str, int i, int i2, int i3);

    void setReadyForVrMode(boolean z);

    void setSystemMouseControlType(int i);

    void setSystemMouseShowMouseEnabled(boolean z);

    int setThreadAffinity(int i, int[] iArr);

    boolean setThreadGroup(int i, int i2);

    boolean setThreadScheduler(int i, int i2, int i3);

    void setVrMode(boolean z);

    boolean writeSysNode(String str, String str2, boolean z);
}
