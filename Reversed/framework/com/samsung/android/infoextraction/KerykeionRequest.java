package com.samsung.android.infoextraction;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class KerykeionRequest implements Parcelable {
    public static final Creator<KerykeionRequest> CREATOR = new C01981();
    private HermesObject mHermesObject;
    private List<Object> mPrimitive;
    private int nPatternType;
    private int nType;

    static class C01981 implements Creator<KerykeionRequest> {
        C01981() {
        }

        public KerykeionRequest createFromParcel(Parcel parcel) {
            KerykeionRequest kerykeionRequest = new KerykeionRequest();
            kerykeionRequest.readFromParcel(parcel);
            return kerykeionRequest;
        }

        public KerykeionRequest[] newArray(int i) {
            return new KerykeionRequest[i];
        }
    }

    public KerykeionRequest() {
        this.mPrimitive = null;
        this.nPatternType = 0;
        this.mHermesObject = null;
        this.mPrimitive = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public HermesObject getHermesObject() {
        return this.mHermesObject;
    }

    public int getPatternType() {
        return this.nPatternType;
    }

    public List<Object> getSourceData() {
        return this.mPrimitive;
    }

    public int getType() {
        return this.nType;
    }

    public void readFromParcel(Parcel parcel) {
        this.nType = parcel.readInt();
        this.mPrimitive = parcel.readArrayList(Object.class.getClassLoader());
        this.nPatternType = parcel.readInt();
        this.mHermesObject = (HermesObject) parcel.readParcelable(HermesObject.class.getClassLoader());
    }

    public void setRequestData(int i, List<Object> list, int i2) {
        this.nType = i;
        this.nPatternType = i2;
        for (Object next : list) {
            if (((next instanceof String) | (next instanceof Uri)) != 0) {
                this.mPrimitive.add(next);
            }
        }
    }

    public void setRequestData(int i, List<Object> list, int i2, HermesObject hermesObject) {
        this.nType = i;
        this.nPatternType = i2;
        for (Object next : list) {
            if (((next instanceof String) | (next instanceof Uri)) != 0) {
                this.mPrimitive.add(next);
            }
        }
        if (hermesObject != null) {
            this.mHermesObject = hermesObject;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.nType);
        parcel.writeList(this.mPrimitive);
        parcel.writeInt(this.nPatternType);
        parcel.writeParcelable(this.mHermesObject, i);
    }
}
