package com.samsung.android.knox.sdp.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.samsung.android.fingerprint.FingerprintEvent;

public class SdpEngineInfo implements Parcelable {
    public static final Creator<SdpEngineInfo> CREATOR = new C02091();
    private static String PERSONA_PWD_RESET_TOKEN = "PersonaPwdResetToken";
    private static String PWD_RESET_TOKEN = "PwdResetToken";
    private String mAlias;
    private int mFlags;
    private int mId;
    private boolean mIsMigrating;
    private String mPackageName;
    private int mState;
    private int mType;
    private int mUserId;
    private int mVersion;

    static class C02091 implements Creator<SdpEngineInfo> {
        C02091() {
        }

        public SdpEngineInfo createFromParcel(Parcel parcel) {
            return new SdpEngineInfo(parcel);
        }

        public SdpEngineInfo[] newArray(int i) {
            return new SdpEngineInfo[i];
        }
    }

    public SdpEngineInfo() {
        this.mIsMigrating = false;
        this.mPackageName = "";
        this.mAlias = null;
        this.mPackageName = "";
        this.mId = -1;
        this.mUserId = -1;
        this.mState = -1;
        this.mFlags = -1;
        this.mVersion = -1;
        this.mType = -1;
        this.mIsMigrating = false;
    }

    private SdpEngineInfo(Parcel parcel) {
        boolean z = false;
        this.mIsMigrating = false;
        this.mPackageName = "";
        this.mAlias = parcel.readString();
        this.mPackageName = parcel.readString();
        this.mId = parcel.readInt();
        this.mUserId = parcel.readInt();
        this.mState = parcel.readInt();
        this.mFlags = parcel.readInt();
        this.mVersion = parcel.readInt();
        this.mType = parcel.readInt();
        if (parcel.readInt() != 0) {
            z = true;
        }
        this.mIsMigrating = z;
    }

    public SdpEngineInfo(String str, int i, int i2, int i3, int i4, int i5, boolean z) {
        this.mIsMigrating = false;
        this.mPackageName = "";
        if (str == null) {
            String str2 = (i < 0 || i > FingerprintEvent.EVENT_FACTORY_APP) ? "" : "android_" + i;
            this.mAlias = str2;
        } else {
            this.mAlias = str;
        }
        this.mId = i;
        this.mUserId = i2;
        this.mState = i3;
        this.mFlags = i4;
        this.mVersion = i5;
        this.mPackageName = "";
        if (this.mAlias == null || this.mAlias.isEmpty()) {
            this.mType = -1;
        } else {
            this.mType = this.mAlias.equals(new StringBuilder().append("android_").append(i).toString()) ? 1 : 2;
        }
        this.mIsMigrating = z;
    }

    public int describeContents() {
        return 0;
    }

    public String getAlias() {
        return this.mAlias;
    }

    public int getFlag() {
        return this.mFlags;
    }

    public int getId() {
        return this.mId;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getResetTokenTimaAlias() {
        return this.mType == 1 ? PERSONA_PWD_RESET_TOKEN + this.mId : this.mType == 2 ? PWD_RESET_TOKEN + this.mId : null;
    }

    public int getState() {
        return this.mState;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public boolean isAndroidDefaultEngine() {
        return this.mType == 1;
    }

    public boolean isCustomEngine() {
        return this.mType == 2;
    }

    public boolean isMdfpp() {
        return !isMinor();
    }

    public boolean isMigrating() {
        return this.mIsMigrating;
    }

    public boolean isMinor() {
        return (this.mFlags & 1) == 1;
    }

    public void setFlag(int i) {
        this.mFlags = i;
    }

    public void setIsMigrating(boolean z) {
        this.mIsMigrating = z;
    }

    public void setPackageName(String str) {
        if (str != null) {
            this.mPackageName = str;
        }
    }

    public void setState(int i) {
        this.mState = i;
    }

    public void setVersion(int i) {
        this.mVersion = i;
    }

    public String toString() {
        return new String("SdpEngineInfo { alias:" + this.mAlias + " pkg: " + this.mPackageName + " id:" + this.mId + " userid:" + this.mUserId + " state:" + this.mState + " flags:" + this.mFlags + " version:" + this.mVersion + " type:" + this.mType + "}");
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mAlias);
        parcel.writeString(this.mPackageName);
        parcel.writeInt(this.mId);
        parcel.writeInt(this.mUserId);
        parcel.writeInt(this.mState);
        parcel.writeInt(this.mFlags);
        parcel.writeInt(this.mVersion);
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mIsMigrating ? 1 : 0);
    }
}
