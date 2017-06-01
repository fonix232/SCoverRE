package com.samsung.android.security;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class SemSdCardEncryptionPolicy implements Parcelable, Cloneable, Comparable<SemSdCardEncryptionPolicy> {
    public static final Creator<SemSdCardEncryptionPolicy> CREATOR = new C02361();
    public String mCurrentUUID;
    private int mEnc;
    public int mEncryptState;
    private int mExcludeMedia;
    private int mFullEnc;
    public int mIsPolicy;

    static class C02361 implements Creator<SemSdCardEncryptionPolicy> {
        C02361() {
        }

        public SemSdCardEncryptionPolicy createFromParcel(Parcel parcel) {
            return new SemSdCardEncryptionPolicy(parcel);
        }

        public SemSdCardEncryptionPolicy[] newArray(int i) {
            return new SemSdCardEncryptionPolicy[i];
        }
    }

    public SemSdCardEncryptionPolicy() {
        init();
    }

    public SemSdCardEncryptionPolicy(int i, int i2, String str) {
        this.mIsPolicy = i;
        this.mEncryptState = i2;
        this.mCurrentUUID = str;
    }

    public SemSdCardEncryptionPolicy(Parcel parcel) {
        this.mIsPolicy = parcel.readInt();
        this.mEncryptState = parcel.readInt();
        this.mCurrentUUID = parcel.readString();
    }

    public static SemSdCardEncryptionPolicy readFromParcel(Parcel parcel) {
        return new SemSdCardEncryptionPolicy(parcel);
    }

    public static SemSdCardEncryptionPolicy unflattenFromString(int i, String str) {
        String str2 = null;
        int i2 = 3;
        String[] split = str.split(" ");
        try {
            str2 = split[0];
            i2 = Integer.parseInt(split[1]);
        } catch (Exception e) {
        }
        return new SemSdCardEncryptionPolicy(i, i2, str2);
    }

    public static void writeToParcel(SemSdCardEncryptionPolicy semSdCardEncryptionPolicy, Parcel parcel) {
        if (semSdCardEncryptionPolicy != null) {
            semSdCardEncryptionPolicy.writeToParcel(parcel, 0);
        } else {
            parcel.writeString(null);
        }
    }

    public SemSdCardEncryptionPolicy clone() {
        return new SemSdCardEncryptionPolicy(this.mIsPolicy, this.mEncryptState, this.mCurrentUUID);
    }

    public int compareTo(SemSdCardEncryptionPolicy semSdCardEncryptionPolicy) {
        return equals(semSdCardEncryptionPolicy) ? 0 : 1;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                SemSdCardEncryptionPolicy semSdCardEncryptionPolicy = (SemSdCardEncryptionPolicy) obj;
                if (this.mIsPolicy == semSdCardEncryptionPolicy.mIsPolicy && this.mEncryptState == semSdCardEncryptionPolicy.mEncryptState) {
                    z = true;
                }
                return z;
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    public String getCurrentUUID() {
        return this.mCurrentUUID;
    }

    public int getEncryptState() {
        return this.mEncryptState;
    }

    public int getEncryptionState() {
        return this.mEnc;
    }

    public int hashCode() {
        return 0;
    }

    public void init() {
        this.mIsPolicy = 0;
        this.mEncryptState = 3;
        this.mCurrentUUID = null;
    }

    public boolean isAdminPolicyEnabled() {
        return this.mIsPolicy == 1;
    }

    public void setIsPolicy(int i) {
        this.mIsPolicy = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mIsPolicy);
        parcel.writeInt(this.mEncryptState);
        parcel.writeString(this.mCurrentUUID);
    }
}
