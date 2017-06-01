package com.samsung.android.content.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipRemoteRequestInfo implements Parcelable {
    public static final Creator<SmartClipRemoteRequestInfo> CREATOR = new C10361();
    public static final int REQUEST_TYPE_AIR_BUTTON_HIT_TEST = 2;
    public static final int REQUEST_TYPE_INJECT_INPUT_EVENT = 3;
    public static final int REQUEST_TYPE_INVALID = 0;
    public static final int REQUEST_TYPE_SCROLLABLE_AREA_INFO = 4;
    public static final int REQUEST_TYPE_SCROLLABLE_VIEW_INFO = 5;
    public static final int REQUEST_TYPE_SMART_CLIP_META_EXTRACTION = 1;
    public int mCallerPid = 0;
    public int mCallerUid = 0;
    public Parcelable mRequestData;
    public int mRequestId = 0;
    public int mRequestType = 0;
    public int mTargetWindowLayer = -1;

    static class C10361 implements Creator<SmartClipRemoteRequestInfo> {
        C10361() {
        }

        public SmartClipRemoteRequestInfo createFromParcel(Parcel parcel) {
            SmartClipRemoteRequestInfo smartClipRemoteRequestInfo = new SmartClipRemoteRequestInfo();
            smartClipRemoteRequestInfo.readFromParcel(parcel);
            return smartClipRemoteRequestInfo;
        }

        public SmartClipRemoteRequestInfo[] newArray(int i) {
            return new SmartClipRemoteRequestInfo[i];
        }
    }

    public SmartClipRemoteRequestInfo(int i, int i2, Parcelable parcelable) {
        this.mRequestId = i;
        this.mRequestType = i2;
        this.mRequestData = parcelable;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.mCallerPid = parcel.readInt();
        this.mCallerUid = parcel.readInt();
        this.mRequestId = parcel.readInt();
        this.mRequestType = parcel.readInt();
        this.mRequestData = parcel.readParcelable(null);
        this.mTargetWindowLayer = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mCallerPid);
        parcel.writeInt(this.mCallerUid);
        parcel.writeInt(this.mRequestId);
        parcel.writeInt(this.mRequestType);
        parcel.writeParcelable(this.mRequestData, i);
        parcel.writeInt(this.mTargetWindowLayer);
    }
}
