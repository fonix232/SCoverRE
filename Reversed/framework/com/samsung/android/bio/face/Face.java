package com.samsung.android.bio.face;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Face implements Parcelable {
    public static final Creator<Face> CREATOR = new C09971();
    private long mDeviceId;
    private int mFaceId;
    private int mGroupId;
    private CharSequence mName;

    static class C09971 implements Creator<Face> {
        C09971() {
        }

        public Face createFromParcel(Parcel parcel) {
            return new Face(parcel);
        }

        public Face[] newArray(int i) {
            return new Face[i];
        }
    }

    private Face(Parcel parcel) {
        this.mName = parcel.readString();
        this.mGroupId = parcel.readInt();
        this.mFaceId = parcel.readInt();
        this.mDeviceId = parcel.readLong();
    }

    public Face(CharSequence charSequence, int i, int i2, long j) {
        this.mName = charSequence;
        this.mGroupId = i;
        this.mFaceId = i2;
        this.mDeviceId = j;
    }

    public int describeContents() {
        return 0;
    }

    public long getDeviceId() {
        return this.mDeviceId;
    }

    public int getFaceId() {
        return this.mFaceId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName.toString());
        parcel.writeInt(this.mGroupId);
        parcel.writeInt(this.mFaceId);
        parcel.writeLong(this.mDeviceId);
    }
}
