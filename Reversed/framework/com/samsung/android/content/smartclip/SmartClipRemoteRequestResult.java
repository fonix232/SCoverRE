package com.samsung.android.content.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipRemoteRequestResult implements Parcelable {
    public static final Creator<SmartClipRemoteRequestResult> CREATOR = new C10371();
    public int mRequestId = 0;
    public int mRequestType = 0;
    public Parcelable mResultData = null;

    static class C10371 implements Creator<SmartClipRemoteRequestResult> {
        C10371() {
        }

        public SmartClipRemoteRequestResult createFromParcel(Parcel parcel) {
            SmartClipRemoteRequestResult smartClipRemoteRequestResult = new SmartClipRemoteRequestResult(0, 0, null);
            smartClipRemoteRequestResult.readFromParcel(parcel);
            return smartClipRemoteRequestResult;
        }

        public SmartClipRemoteRequestResult[] newArray(int i) {
            return new SmartClipRemoteRequestResult[i];
        }
    }

    public SmartClipRemoteRequestResult(int i, int i2, Parcelable parcelable) {
        this.mRequestId = i;
        this.mResultData = parcelable;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.mRequestId = parcel.readInt();
        this.mRequestType = parcel.readInt();
        this.mResultData = parcel.readParcelable(null);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mRequestId);
        parcel.writeInt(this.mRequestType);
        parcel.writeParcelable(this.mResultData, i);
    }
}
