package com.samsung.android.share.executor.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class Parameter implements Parcelable {
    public static final Creator<Parameter> CREATOR = new C02491();
    private String CHObjectType;
    private List<CHObject> CHObjects = new ArrayList();
    private Boolean isMandatory;
    private String parameterName;
    private String parameterType;
    private String slotName;
    private String slotType;
    private String slotValue;
    private String slotValueType;

    static class C02491 implements Creator<Parameter> {
        C02491() {
        }

        public Parameter createFromParcel(Parcel parcel) {
            return new Parameter(parcel);
        }

        public Parameter[] newArray(int i) {
            return new Parameter[i];
        }
    }

    public Parameter(Parcel parcel) {
        Boolean bool;
        boolean z = true;
        this.slotType = parcel.readString();
        this.slotName = parcel.readString();
        this.slotValue = parcel.readString();
        this.slotValueType = parcel.readString();
        this.CHObjectType = parcel.readString();
        if (parcel.readByte() == (byte) 1) {
            this.CHObjects = new ArrayList();
            parcel.readList(this.CHObjects, CHObject.class.getClassLoader());
        } else {
            this.CHObjects = null;
        }
        this.parameterName = parcel.readString();
        this.parameterType = parcel.readString();
        byte readByte = parcel.readByte();
        if (readByte == (byte) 2) {
            bool = null;
        } else {
            if (readByte == (byte) 0) {
                z = false;
            }
            bool = Boolean.valueOf(z);
        }
        this.isMandatory = bool;
    }

    public Parameter(String str, String str2, String str3, String str4, String str5, List<CHObject> list, String str6, String str7, Boolean bool) {
        this.slotType = str;
        this.slotName = str2;
        this.slotValue = str3;
        this.slotValueType = str4;
        this.CHObjectType = str5;
        this.CHObjects = list;
        this.parameterName = str6;
        this.parameterType = str7;
        this.isMandatory = bool;
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

    public Boolean getIsMandatory() {
        return this.isMandatory;
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

    public String getSlotValueType() {
        return this.slotValueType;
    }

    public void setCHObjectType(String str) {
        this.CHObjectType = str;
    }

    public void setCHObjects(List<CHObject> list) {
        this.CHObjects = list;
    }

    public void setIsMandatory(Boolean bool) {
        this.isMandatory = bool;
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

    public void setSlotValueType(String str) {
        this.slotValueType = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        parcel.writeString(this.slotType);
        parcel.writeString(this.slotName);
        parcel.writeString(this.slotValue);
        parcel.writeString(this.slotValueType);
        parcel.writeString(this.CHObjectType);
        if (this.CHObjects == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeList(this.CHObjects);
        }
        parcel.writeString(this.parameterName);
        parcel.writeString(this.parameterType);
        if (this.isMandatory == null) {
            parcel.writeByte((byte) 2);
            return;
        }
        if (!this.isMandatory.booleanValue()) {
            i2 = 0;
        }
        parcel.writeByte((byte) i2);
    }
}
