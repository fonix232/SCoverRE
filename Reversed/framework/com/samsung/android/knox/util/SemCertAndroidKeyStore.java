package com.samsung.android.knox.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.cert.Certificate;

public class SemCertAndroidKeyStore implements Parcelable {
    public static final Creator<SemCertAndroidKeyStore> CREATOR = new C02101();
    public Certificate[] certs;

    static class C02101 implements Creator<SemCertAndroidKeyStore> {
        C02101() {
        }

        public SemCertAndroidKeyStore createFromParcel(Parcel parcel) {
            return new SemCertAndroidKeyStore(parcel);
        }

        public SemCertAndroidKeyStore[] newArray(int i) {
            return null;
        }
    }

    public SemCertAndroidKeyStore(Parcel parcel) {
        this.certs = (Certificate[]) parcel.readSerializable();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(this.certs);
    }
}
