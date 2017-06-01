package com.samsung.android.service.DeviceRootKeyService;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Set;

public final class Tlv implements Parcelable {
    public static final Creator<Tlv> CREATOR = new C02381();
    public static final int TLV_ATTRS = 17;
    public static final int TLV_TAG_CERT_SD = 10;
    public static final int TLV_TAG_CERT_SM = 9;
    public static final int TLV_TAG_DN_QUALIFIER = 6;
    public static final int TLV_TAG_EXPONENT = 1;
    public static final int TLV_TAG_EXTEND_PCR_DATA = 13;
    public static final int TLV_TAG_EXT_KEYUSAGE = 7;
    public static final int TLV_TAG_HASH_ALGO = 3;
    public static final int TLV_TAG_ISSUER = 2;
    public static final int TLV_TAG_KEYUSAGE = 5;
    private static final int TLV_TAG_MAX = 18;
    public static final int TLV_TAG_SIGN_DATA_BLOB = 8;
    public static final int TLV_TAG_SUBJECT = 4;
    public static final int TLV_TAG_TID = 14;
    public static final int TLV_TAG_TIMESTAMP = 11;
    public static final int TLV_TAG_TLV_KEY_INFO = 16;
    public static final int TLV_TAG_WRAPPED_KEY = 15;
    public static final int TLV_TAG_WRAPPED_PCR = 12;
    private HashMap<Integer, byte[]> mTlvList = new HashMap();

    static class C02381 implements Creator<Tlv> {
        C02381() {
        }

        public Tlv createFromParcel(Parcel parcel) {
            Tlv tlv = new Tlv();
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                int readInt2 = parcel.readInt();
                byte[] bArr = new byte[parcel.readInt()];
                parcel.readByteArray(bArr);
                tlv.setTlv(readInt2, bArr);
            }
            return tlv;
        }

        public Tlv[] newArray(int i) {
            return new Tlv[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getTlvValue(int i) {
        return (i < 1 || i >= 18) ? null : (byte[]) this.mTlvList.get(Integer.valueOf(i));
    }

    public int getTotalSize() {
        return this.mTlvList.size();
    }

    public Set<Integer> getValidKeyList() {
        return this.mTlvList.keySet();
    }

    public boolean setTlv(int i, byte[] bArr) {
        if (i < 1 || i >= 18) {
            return false;
        }
        this.mTlvList.put(Integer.valueOf(i), bArr);
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mTlvList.size());
        for (Integer intValue : this.mTlvList.keySet()) {
            int intValue2 = intValue.intValue();
            parcel.writeInt(intValue2);
            parcel.writeInt(((byte[]) this.mTlvList.get(Integer.valueOf(intValue2))).length);
            parcel.writeByteArray((byte[]) this.mTlvList.get(Integer.valueOf(intValue2)));
        }
    }
}
