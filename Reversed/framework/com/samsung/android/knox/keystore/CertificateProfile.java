package com.samsung.android.knox.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class CertificateProfile implements Parcelable {
    public static final Creator<CertificateProfile> CREATOR = new C02061();
    public String alias;
    public boolean allowAllPackages;
    public boolean allowRawSigning;
    public boolean allowWiFi;
    public boolean isCSRResponse;
    public List<String> packageList;

    static class C02061 implements Creator<CertificateProfile> {
        C02061() {
        }

        public CertificateProfile createFromParcel(Parcel parcel) {
            return new CertificateProfile(parcel);
        }

        public CertificateProfile[] newArray(int i) {
            return new CertificateProfile[i];
        }
    }

    public CertificateProfile() {
        this.isCSRResponse = false;
        this.alias = null;
        this.packageList = new ArrayList();
        this.allowWiFi = false;
        this.allowAllPackages = false;
        this.allowRawSigning = false;
    }

    private CertificateProfile(Parcel parcel) {
        boolean z = false;
        this.isCSRResponse = false;
        this.alias = null;
        this.packageList = new ArrayList();
        this.allowWiFi = false;
        this.allowAllPackages = false;
        this.allowRawSigning = false;
        this.isCSRResponse = parcel.readInt() != 0;
        this.alias = parcel.readString();
        parcel.readStringList(this.packageList);
        this.allowWiFi = parcel.readInt() != 0;
        this.allowAllPackages = parcel.readInt() != 0;
        if (parcel.readInt() != 0) {
            z = true;
        }
        this.allowRawSigning = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeInt(this.isCSRResponse ? 1 : 0);
        parcel.writeString(this.alias);
        parcel.writeStringList(this.packageList);
        parcel.writeInt(this.allowWiFi ? 1 : 0);
        parcel.writeInt(this.allowAllPackages ? 1 : 0);
        if (!this.allowRawSigning) {
            i2 = 0;
        }
        parcel.writeInt(i2);
    }
}
