package com.samsung.android.edge;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class EdgeLightingPolicyInfo implements Parcelable {
    public static final int CATEGORY_BLACK = 2;
    public static final int CATEGORY_WHITE = 1;
    public static final Creator<EdgeLightingPolicyInfo> CREATOR = new C00651();
    private static final int RANGE_MASK = 65535;
    public static final int RANGE_NOTIFICATION = 1;
    public static final int RANGE_PRIVATE_MASK = 65280;
    public static final int RANGE_PRIVATE_NOTIFICATION_AFTER_WAKEUP = 1024;
    public static final int RANGE_PRIVATE_NOT_HUN_BUT_NOTIFICATION = 256;
    public static final int RANGE_PRIVATE_TOAST = 512;
    public static final int RANGE_PUBLIC_ALL = 7;
    public static final int RANGE_PUBLIC_MASK = 255;
    public static final int RANGE_RESERVED_FLAG = 65536;
    public static final int RANGE_WAKE_LOCK = 4;
    public static final int RANGE_WAKE_UP = 2;
    public final int category;
    public final String packageName;
    public final int range;

    static class C00651 implements Creator<EdgeLightingPolicyInfo> {
        C00651() {
        }

        public EdgeLightingPolicyInfo createFromParcel(Parcel parcel) {
            return new EdgeLightingPolicyInfo(parcel);
        }

        public EdgeLightingPolicyInfo[] newArray(int i) {
            return new EdgeLightingPolicyInfo[i];
        }
    }

    public EdgeLightingPolicyInfo(Parcel parcel) {
        this.packageName = parcel.readString();
        this.category = parcel.readInt();
        this.range = parcel.readInt();
    }

    public EdgeLightingPolicyInfo(String str, int i, int i2) {
        this.packageName = str;
        this.category = i;
        this.range = RANGE_MASK & i2;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return ("EdgeLightingPolicyInfo{packageName= " + this.packageName) + ", category= " + this.category + ", range= " + this.range + "}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.packageName);
        parcel.writeInt(this.category);
        parcel.writeInt(this.range);
    }
}
