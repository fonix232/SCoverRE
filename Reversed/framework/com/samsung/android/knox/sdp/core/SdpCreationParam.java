package com.samsung.android.knox.sdp.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class SdpCreationParam implements Parcelable {
    public static final Creator<SdpCreationParam> CREATOR = new C02081();
    private String mAlias;
    private int mFlags;
    private ArrayList<SdpDomain> mPrivilegedApps;

    static class C02081 implements Creator<SdpCreationParam> {
        C02081() {
        }

        public SdpCreationParam createFromParcel(Parcel parcel) {
            return new SdpCreationParam(parcel);
        }

        public SdpCreationParam[] newArray(int i) {
            return new SdpCreationParam[i];
        }
    }

    private SdpCreationParam(Parcel parcel) {
        this.mFlags = 0;
        this.mAlias = parcel.readString();
        this.mFlags = parcel.readInt();
        this.mPrivilegedApps = (ArrayList) parcel.readSerializable();
    }

    public SdpCreationParam(String str, int i, ArrayList<SdpDomain> arrayList) {
        this.mFlags = 0;
        if (str == null) {
            str = "";
        }
        this.mAlias = str;
        this.mFlags = validateFlags(i);
        this.mPrivilegedApps = validatePrivilegedApps(arrayList);
    }

    private int validateFlags(int i) {
        return (i < 0 || i > 1) ? 0 : i;
    }

    private ArrayList<SdpDomain> validatePrivilegedApps(ArrayList<SdpDomain> arrayList) {
        ArrayList<SdpDomain> arrayList2 = new ArrayList();
        if (arrayList != null) {
            for (SdpDomain sdpDomain : arrayList) {
                if (!(sdpDomain.getPackageName() == null || sdpDomain.getPackageName().trim().isEmpty())) {
                    arrayList2.add(sdpDomain);
                }
            }
        }
        return arrayList2;
    }

    public int describeContents() {
        return 0;
    }

    public String getAlias() {
        return this.mAlias;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public ArrayList<SdpDomain> getPrivilegedApps() {
        return this.mPrivilegedApps;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nSdpCreationParam { ");
        stringBuilder.append("\n");
        stringBuilder.append("alias:").append(this.mAlias);
        stringBuilder.append("\n");
        for (SdpDomain sdpDomain : this.mPrivilegedApps) {
            stringBuilder.append(sdpDomain.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mAlias);
        parcel.writeInt(this.mFlags);
        parcel.writeSerializable(this.mPrivilegedApps);
    }
}
