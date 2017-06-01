package com.samsung.android.share.executor.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CHObject implements Parcelable {
    public static final Creator<CHObject> CREATOR = new C02471();
    private String CH_Type;
    private String CH_Value;
    private String CH_ValueType;

    static class C02471 implements Creator<CHObject> {
        C02471() {
        }

        public CHObject createFromParcel(Parcel parcel) {
            return new CHObject(parcel);
        }

        public CHObject[] newArray(int i) {
            return new CHObject[i];
        }
    }

    public CHObject(Parcel parcel) {
        this.CH_Type = parcel.readString();
        this.CH_Value = parcel.readString();
        this.CH_ValueType = parcel.readString();
    }

    public CHObject(String str, String str2, String str3) {
        this.CH_Type = str;
        this.CH_Value = str2;
        this.CH_ValueType = str3;
    }

    public int describeContents() {
        return 0;
    }

    public String getCHType() {
        return this.CH_Type;
    }

    public String getCHValue() {
        return this.CH_Value;
    }

    public String getCHValueType() {
        return this.CH_ValueType;
    }

    public void setCHType(String str) {
        this.CH_Type = str;
    }

    public void setCHValue(String str) {
        this.CH_Value = str;
    }

    public void setCHValueType(String str) {
        this.CH_ValueType = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.CH_Type);
        parcel.writeString(this.CH_Value);
        parcel.writeString(this.CH_ValueType);
    }
}
