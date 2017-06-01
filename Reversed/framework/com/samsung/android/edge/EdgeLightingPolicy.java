package com.samsung.android.edge;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public final class EdgeLightingPolicy implements Parcelable {
    public static final Creator<EdgeLightingPolicy> CREATOR = new C00641();
    public static final int TYPE_EXCLUDE_BLACK_LIST = 4;
    public static final int TYPE_EXCLUDE_SYSTEM_APP = 2;
    public static final int TYPE_INCLUDE_ALL_APP = 1;
    private ArrayList<EdgeLightingPolicyInfo> mPolicyInfoList = new ArrayList();
    private int mType = 0;
    private long mVersion = 0;

    static class C00641 implements Creator<EdgeLightingPolicy> {
        C00641() {
        }

        public EdgeLightingPolicy createFromParcel(Parcel parcel) {
            return new EdgeLightingPolicy(parcel);
        }

        public EdgeLightingPolicy[] newArray(int i) {
            return new EdgeLightingPolicy[i];
        }
    }

    public EdgeLightingPolicy(Parcel parcel) {
        this.mType = parcel.readInt();
        this.mVersion = parcel.readLong();
        parcel.readTypedList(this.mPolicyInfoList, EdgeLightingPolicyInfo.CREATOR);
    }

    public void addEdgeLightingPolicyInfo(EdgeLightingPolicyInfo edgeLightingPolicyInfo) {
        this.mPolicyInfoList.add(edgeLightingPolicyInfo);
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<EdgeLightingPolicyInfo> getEdgeLightingPolicyInfoList() {
        return this.mPolicyInfoList;
    }

    public int getPolicyType() {
        return this.mType;
    }

    public long getPolicyVersion() {
        return this.mVersion;
    }

    public void setPolicyType(int i) {
        this.mType = i;
    }

    public void setPolicyVersion(long j) {
        this.mVersion = j;
    }

    public String toString() {
        String str = "EdgeLightingPolicy{Type = " + this.mType + ", version = " + this.mVersion + "}";
        for (EdgeLightingPolicyInfo edgeLightingPolicyInfo : this.mPolicyInfoList) {
            str = str + " " + edgeLightingPolicyInfo.toString();
        }
        return str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeLong(this.mVersion);
        parcel.writeTypedList(this.mPolicyInfoList);
    }
}
