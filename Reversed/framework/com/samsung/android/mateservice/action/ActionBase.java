package com.samsung.android.mateservice.action;

public final class ActionBase {
    public static final int ATTR_ACCESS_GRANTED_PKG = 524288;
    public static final int ATTR_ACCESS_PLATFORM_PKG = 262144;
    public static final int ATTR_ACCESS_PRIVILEGED_PKG = 131072;
    public static final int ATTR_ACCESS_SYSTEM_UID = 65536;
    public static final int ATTR_TYPE_APP_SVC = 2097152;
    public static final int ATTR_TYPE_CLIENT = 4194304;
    public static final int ATTR_TYPE_SDK = 8388608;
    public static final int ATTR_TYPE_SYS_SVC = 1048576;
    static final int BASE_AGENT_SYSTEM_UID = 2162688;
    static final int BASE_SYS_SVC_PLATFORM_PKG = 1310720;
    static final int BASE_SYS_SVC_PRIVILEGED_PKG = 1179648;
    static final int BASE_SYS_SVC_SYSTEM_UID = 1114112;
    public static final int MASK_ATTR_ACCESS = 983040;
    public static final int MASK_ATTR_ACCESS_EXTRA = 61440;
    public static final int MASK_ATTR_NUMBERING = 4095;
    public static final int MASK_ATTR_RESERVED = -16777216;
    public static final int MASK_ATTR_TYPE = -268435456;

    private ActionBase() {
    }
}
