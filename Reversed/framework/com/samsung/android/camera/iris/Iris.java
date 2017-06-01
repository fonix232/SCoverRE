package com.samsung.android.camera.iris;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Iris implements Parcelable {
    public static final Creator<Iris> CREATOR = new C10011();
    private long mDeviceId;
    private int mGroupId;
    private int mIrisId;
    private CharSequence mName;

    static class C10011 implements Creator<Iris> {
        C10011() {
        }

        public Iris createFromParcel(Parcel parcel) {
            return new Iris(parcel);
        }

        public Iris[] newArray(int i) {
            return new Iris[i];
        }
    }

    private Iris(Parcel parcel) {
        this.mName = parcel.readString();
        this.mGroupId = parcel.readInt();
        this.mIrisId = parcel.readInt();
        this.mDeviceId = parcel.readLong();
    }

    public Iris(CharSequence charSequence, int i, int i2, long j) {
        this.mName = charSequence;
        this.mGroupId = i;
        this.mIrisId = i2;
        this.mDeviceId = j;
    }

    public int describeContents() {
        return 0;
    }

    public long getDeviceId() {
        return this.mDeviceId;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getIrisId() {
        return this.mIrisId;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName.toString());
        parcel.writeInt(this.mGroupId);
        parcel.writeInt(this.mIrisId);
        parcel.writeLong(this.mDeviceId);
    }
}
