package com.samsung.android.infoextraction;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class HermesObject implements Parcelable {
    public static final Creator<HermesObject> CREATOR = new C01971();
    private Object obj = null;

    static class C01971 implements Creator<HermesObject> {
        C01971() {
        }

        public HermesObject createFromParcel(Parcel parcel) {
            HermesObject hermesObject = new HermesObject();
            hermesObject.readFromParcel(parcel);
            return hermesObject;
        }

        public HermesObject[] newArray(int i) {
            return new HermesObject[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public Object getObject() {
        return this.obj;
    }

    public void readFromParcel(Parcel parcel) {
        this.obj = parcel.readParcelable(HermesObject.class.getClassLoader());
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable((HermesObject) this.obj, i);
    }
}
