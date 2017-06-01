package com.samsung.android.content.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class SemSmartClipExtendedMetaTag extends SemSmartClipMetaTag implements Parcelable {
    public static final Creator<SemSmartClipExtendedMetaTag> CREATOR = new C10251();
    public static final String TAG = "SemSmartClipExtendedMetaTag";
    protected byte[] mExtraData = null;
    protected Parcelable mParcelableData = null;

    static class C10251 implements Creator<SemSmartClipExtendedMetaTag> {
        C10251() {
        }

        public SemSmartClipExtendedMetaTag createFromParcel(Parcel parcel) {
            Log.m29d(SemSmartClipExtendedMetaTag.TAG, "SemSmartClipExtendedMetaTag.createFromParcel called");
            SemSmartClipExtendedMetaTag semSmartClipExtendedMetaTag = new SemSmartClipExtendedMetaTag(null, null);
            semSmartClipExtendedMetaTag.readFromParcel(parcel);
            return semSmartClipExtendedMetaTag;
        }

        public SemSmartClipExtendedMetaTag[] newArray(int i) {
            return new SemSmartClipExtendedMetaTag[i];
        }
    }

    public SemSmartClipExtendedMetaTag(String str, String str2) {
        super(str, str2);
    }

    public SemSmartClipExtendedMetaTag(String str, String str2, Parcelable parcelable) {
        super(str, str2);
        this.mParcelableData = parcelable;
    }

    public SemSmartClipExtendedMetaTag(String str, String str2, byte[] bArr) {
        super(str, str2);
        this.mExtraData = bArr;
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getExtraData() {
        return this.mExtraData;
    }

    public Parcelable getParcelableData() {
        return this.mParcelableData;
    }

    public void readFromParcel(Parcel parcel) {
        Object obj = null;
        String readString = parcel.readString();
        String readString2 = parcel.readString();
        setType(readString);
        setValue(readString2);
        int readInt = parcel.readInt();
        if (readInt > 0) {
            this.mExtraData = new byte[readInt];
            parcel.readByteArray(this.mExtraData);
        } else {
            this.mExtraData = null;
        }
        if (parcel.readInt() != 0) {
            obj = 1;
        }
        if (obj != null) {
            this.mParcelableData = parcel.readParcelable(null);
        } else {
            this.mParcelableData = null;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getType());
        parcel.writeString(getValue());
        if (this.mExtraData != null) {
            parcel.writeInt(this.mExtraData.length);
            parcel.writeByteArray(this.mExtraData);
        } else {
            parcel.writeInt(0);
        }
        if (this.mParcelableData != null) {
            parcel.writeInt(1);
            parcel.writeParcelable(this.mParcelableData, 0);
            return;
        }
        parcel.writeInt(0);
    }
}
