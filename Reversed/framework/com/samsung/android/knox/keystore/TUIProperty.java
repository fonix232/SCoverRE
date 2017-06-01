package com.samsung.android.knox.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.samsung.android.fingerprint.FingerprintEvent;

public class TUIProperty implements Parcelable {
    public static final Creator<TUIProperty> CREATOR = new C02071();
    public int loginExpirationPeriod;
    public int loginRetry;
    public byte[] pin;
    public byte[] secretImage;

    static class C02071 implements Creator<TUIProperty> {
        C02071() {
        }

        public TUIProperty createFromParcel(Parcel parcel) {
            return new TUIProperty(parcel);
        }

        public TUIProperty[] newArray(int i) {
            return new TUIProperty[i];
        }
    }

    public TUIProperty() {
        this.loginRetry = 2;
        this.loginExpirationPeriod = FingerprintEvent.STATUS_ENROLL_FAILURE_SERVICE_FAILURE;
        this.pin = null;
        this.secretImage = null;
    }

    private TUIProperty(Parcel parcel) {
        this.loginRetry = 2;
        this.loginExpirationPeriod = FingerprintEvent.STATUS_ENROLL_FAILURE_SERVICE_FAILURE;
        this.pin = null;
        this.secretImage = null;
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.loginRetry = parcel.readInt();
        this.loginExpirationPeriod = parcel.readInt();
        this.pin = parcel.createByteArray();
        this.secretImage = parcel.createByteArray();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.loginRetry);
        parcel.writeInt(this.loginExpirationPeriod);
        parcel.writeByteArray(this.pin);
        parcel.writeByteArray(this.secretImage);
    }
}
