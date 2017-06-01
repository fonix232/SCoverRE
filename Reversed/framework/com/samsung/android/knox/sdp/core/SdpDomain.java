package com.samsung.android.knox.sdp.core;

import java.io.Serializable;

public class SdpDomain implements Serializable {
    private final String mAlias;
    private final String mPackageName;

    public SdpDomain(String str, String str2) {
        if (str == null) {
            str = "";
        }
        this.mAlias = str;
        if (str2 == null) {
            str2 = "";
        }
        this.mPackageName = str2;
    }

    public String getAlias() {
        return this.mAlias;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String toString() {
        return new String("SdpDomain { alias : " + this.mAlias + " / pkgName : " + this.mPackageName + " }");
    }
}
