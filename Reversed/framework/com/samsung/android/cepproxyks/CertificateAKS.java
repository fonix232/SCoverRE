package com.samsung.android.cepproxyks;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.cert.Certificate;

public class CertificateAKS implements Parcelable {
    public static final Creator<CertificateAKS> CREATOR = new C10081();
    public Certificate[] mCertificate;

    static class C10081 implements Creator<CertificateAKS> {
        C10081() {
        }

        public CertificateAKS createFromParcel(Parcel parcel) {
            return new CertificateAKS(parcel);
        }

        public CertificateAKS[] newArray(int i) {
            return null;
        }
    }

    public CertificateAKS(Parcel parcel) {
        this.mCertificate = (Certificate[]) parcel.readSerializable();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(this.mCertificate);
    }
}
