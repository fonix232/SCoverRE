package com.sec.vpn.knox;

import android.content.Context;
import android.content.ContextWrapper;

public class GenericVpnContext extends ContextWrapper {
    public static final boolean CONNECTING = true;
    private static final String CONTAINER_VPN_PERMISSION = "com.sec.enterprise.knox.KNOX_CONTAINER_VPN";
    public static final boolean DISCONNECTING = false;
    public static final boolean META_DISABLED = false;
    public static final boolean META_ENABLED = true;
    public static final boolean PER_APP_VPN = false;
    public static final boolean SYSTEM_VPN = true;
    private static String TAG = "GenericVpnContext";
    private boolean mEnableMetaHeader = false;
    private boolean mIsConnecting = false;
    private String mProfile = "";

    public GenericVpnContext(Context context) {
        super(context);
    }

    public void enableMetaData(boolean z) {
        this.mEnableMetaHeader = z;
    }

    public String getVPNProfile() {
        return this.mProfile;
    }

    public boolean getVPNState() {
        return this.mIsConnecting;
    }

    public boolean isMetaEnabled() {
        return this.mEnableMetaHeader;
    }

    public void setGenericVpnParams(String str, boolean z) {
        this.mProfile = str;
        this.mIsConnecting = z;
    }
}
