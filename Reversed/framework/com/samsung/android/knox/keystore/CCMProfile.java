package com.samsung.android.knox.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class CCMProfile implements Parcelable {
    public static final Creator<CCMProfile> CREATOR = new C02041();
    public AccessControlMethod accessControlMethod;
    public String accessControlPassword;
    public List<String> packageList;
    public int tuiPinLength;
    public TUIProperty tuiProperty;
    public boolean whiteListAllPackages;

    static class C02041 implements Creator<CCMProfile> {
        C02041() {
        }

        public CCMProfile createFromParcel(Parcel parcel) {
            return new CCMProfile(parcel);
        }

        public CCMProfile[] newArray(int i) {
            return new CCMProfile[i];
        }
    }

    public enum AccessControlMethod {
        LOCK_STATE(0),
        PASSWORD(1),
        TRUSTED_UI(2),
        TRUSTED_PINPAD(3),
        AFW(15);
        
        private int value;

        private AccessControlMethod(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public CCMProfile() {
        this.accessControlMethod = AccessControlMethod.LOCK_STATE;
        this.packageList = new ArrayList();
        this.whiteListAllPackages = false;
        this.accessControlPassword = null;
        this.tuiPinLength = 6;
        this.tuiProperty = null;
        this.accessControlMethod = AccessControlMethod.LOCK_STATE;
    }

    private CCMProfile(Parcel parcel) {
        this.accessControlMethod = AccessControlMethod.LOCK_STATE;
        this.packageList = new ArrayList();
        this.whiteListAllPackages = false;
        this.accessControlPassword = null;
        this.tuiPinLength = 6;
        this.tuiProperty = null;
        readFromParcel(parcel);
    }

    public CCMProfile(AccessControlMethod accessControlMethod) {
        this.accessControlMethod = AccessControlMethod.LOCK_STATE;
        this.packageList = new ArrayList();
        this.whiteListAllPackages = false;
        this.accessControlPassword = null;
        this.tuiPinLength = 6;
        this.tuiProperty = null;
        this.accessControlMethod = accessControlMethod;
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        boolean z = false;
        try {
            this.accessControlMethod = AccessControlMethod.valueOf(parcel.readString());
            if (this.accessControlMethod == null) {
                this.accessControlMethod = AccessControlMethod.LOCK_STATE;
            }
            this.accessControlPassword = parcel.readString();
            if (parcel.readInt() != 0) {
                z = true;
            }
            this.whiteListAllPackages = z;
            parcel.readStringList(this.packageList);
            this.tuiProperty = (TUIProperty) parcel.readParcelable(getClass().getClassLoader());
        } catch (Throwable e) {
            this.accessControlMethod = null;
            e.printStackTrace();
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.accessControlMethod == null) {
            AccessControlMethod accessControlMethod = this.accessControlMethod;
            parcel.writeString(AccessControlMethod.LOCK_STATE.name());
        } else {
            parcel.writeString(this.accessControlMethod.name());
        }
        parcel.writeString(this.accessControlPassword);
        parcel.writeInt(this.whiteListAllPackages ? 1 : 0);
        parcel.writeStringList(this.packageList);
        parcel.writeParcelable(this.tuiProperty, i);
    }
}
