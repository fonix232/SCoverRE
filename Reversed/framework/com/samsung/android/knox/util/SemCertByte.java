package com.samsung.android.knox.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SemCertByte implements Parcelable {
    public static final Creator<SemCertByte> CREATOR = new C02111();
    public byte[] caCertBytes;
    public int caSize;
    public byte[] certBytes;
    public int certsize;

    static class C02111 implements Creator<SemCertByte> {
        C02111() {
        }

        public SemCertByte createFromParcel(Parcel parcel) {
            return new SemCertByte(parcel);
        }

        public SemCertByte[] newArray(int i) {
            return null;
        }
    }

    public SemCertByte(Parcel parcel) {
        this.certsize = parcel.readInt();
        this.certBytes = new byte[this.certsize];
        parcel.readByteArray(this.certBytes);
        this.caSize = parcel.readInt();
        this.caCertBytes = new byte[this.caSize];
        parcel.readByteArray(this.caCertBytes);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.certsize);
        parcel.writeByteArray(this.certBytes);
        parcel.writeInt(this.caSize);
        parcel.writeByteArray(this.caCertBytes);
    }
}
