package com.samsung.android.cepproxyks;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CertByte implements Parcelable {
    public static final Creator<CertByte> CREATOR = new C10071();
    public byte[] caCertBytes;
    public int caSize;
    public byte[] certBytes;
    public int certsize;

    static class C10071 implements Creator<CertByte> {
        C10071() {
        }

        public CertByte createFromParcel(Parcel parcel) {
            return new CertByte(parcel);
        }

        public CertByte[] newArray(int i) {
            return null;
        }
    }

    public CertByte(Parcel parcel) {
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
