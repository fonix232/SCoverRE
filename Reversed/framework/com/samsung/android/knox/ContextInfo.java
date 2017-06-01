package com.samsung.android.knox;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.UserHandle;

public class ContextInfo implements Parcelable {
    public static final Creator<ContextInfo> CREATOR = new C02011();
    private static final int MIN_PERSONA_ID = 100;
    public final int mCallerUid;
    public final int mContainerId;

    static class C02011 implements Creator<ContextInfo> {
        C02011() {
        }

        public ContextInfo createFromParcel(Parcel parcel) {
            return new ContextInfo(parcel);
        }

        public ContextInfo[] newArray(int i) {
            return new ContextInfo[i];
        }
    }

    public ContextInfo() {
        this.mCallerUid = Process.myUid();
        int userId = UserHandle.getUserId(this.mCallerUid);
        if (userId < 100) {
            this.mContainerId = 0;
        } else {
            this.mContainerId = userId;
        }
    }

    public ContextInfo(int i) {
        this.mCallerUid = i;
        int userId = UserHandle.getUserId(this.mCallerUid);
        if (userId < 100) {
            this.mContainerId = 0;
        } else {
            this.mContainerId = userId;
        }
    }

    public ContextInfo(int i, int i2) {
        this.mCallerUid = i;
        this.mContainerId = i2;
    }

    private ContextInfo(Parcel parcel) {
        this.mCallerUid = parcel.readInt();
        this.mContainerId = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "Caller uid: " + this.mCallerUid + " ,Container id: " + this.mContainerId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mCallerUid);
        parcel.writeInt(this.mContainerId);
    }
}
