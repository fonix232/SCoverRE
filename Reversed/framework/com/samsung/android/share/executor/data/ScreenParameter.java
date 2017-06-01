package com.samsung.android.share.executor.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class ScreenParameter implements Parcelable {
    public static final Creator<ScreenParameter> CREATOR = new C02501();
    private String CHObjectType;
    private List<CHObject> CHObjects = new ArrayList();
    private String parameterName;
    private String parameterType;
    private String slotName;
    private String slotType;
    private String slotValue;

    static class C02501 implements Creator<ScreenParameter> {
        C02501() {
        }

        public ScreenParameter createFromParcel(Parcel parcel) {
            return new ScreenParameter(parcel);
        }

        public ScreenParameter[] newArray(int i) {
            return new ScreenParameter[i];
        }
    }

    protected ScreenParameter(Parcel parcel) {
        this.slotType = parcel.readString();
        this.slotName = parcel.readString();
        this.slotValue = parcel.readString();
        this.CHObjectType = parcel.readString();
        this.CHObjects = parcel.createTypedArrayList(CHObject.CREATOR);
        this.parameterName = parcel.readString();
        this.parameterType = parcel.readString();
    }

    public ScreenParameter(String str, String str2, String str3, String str4, List<CHObject> list, String str5, String str6) {
        this.slotType = str;
        this.slotName = str2;
        this.slotValue = str3;
        this.CHObjectType = str4;
        this.CHObjects = list;
        this.parameterName = str5;
        this.parameterType = str6;
    }

    public static Creator<ScreenParameter> getCREATOR() {
        return CREATOR;
    }

    public int describeContents() {
        return 0;
    }

    public String getCHObjectType() {
        return this.CHObjectType;
    }

    public List<CHObject> getCHObjects() {
        return this.CHObjects;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public String getParameterType() {
        return this.parameterType;
    }

    public String getSlotName() {
        return this.slotName;
    }

    public String getSlotType() {
        return this.slotType;
    }

    public String getSlotValue() {
        return this.slotValue;
    }

    public void setCHObjectType(String str) {
        this.CHObjectType = str;
    }

    public void setCHObjects(List<CHObject> list) {
        this.CHObjects = list;
    }

    public void setParameterName(String str) {
        this.parameterName = str;
    }

    public void setParameterType(String str) {
        this.parameterType = str;
    }

    public void setSlotName(String str) {
        this.slotName = str;
    }

    public void setSlotType(String str) {
        this.slotType = str;
    }

    public void setSlotValue(String str) {
        this.slotValue = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.slotType);
        parcel.writeString(this.slotName);
        parcel.writeString(this.slotValue);
        parcel.writeString(this.CHObjectType);
        parcel.writeTypedList(this.CHObjects);
        parcel.writeString(this.parameterName);
        parcel.writeString(this.parameterType);
    }
}
