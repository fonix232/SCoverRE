package com.samsung.android.knox.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CSRProfile implements Parcelable {
    public static final Creator<CSRProfile> CREATOR = new C02051();
    public String commonName;
    public String country;
    public CSRFormat csrFormat;
    public String domainComponent;
    public String emailAddress;
    public KeyAlgorithm keyAlgType;
    public int keyLength;
    public String locality;
    public String organization;
    public ProfileType profileType;
    public String state;
    public String templateName;

    static class C02051 implements Creator<CSRProfile> {
        C02051() {
        }

        public CSRProfile createFromParcel(Parcel parcel) {
            return new CSRProfile(parcel);
        }

        public CSRProfile[] newArray(int i) {
            return new CSRProfile[i];
        }
    }

    public enum CSRFormat {
        PKCS10,
        CRMF,
        PROPRIETARY
    }

    public enum KeyAlgorithm {
        RSA,
        ECC
    }

    public enum ProfileType {
        SCEP,
        CMP,
        CMC,
        PROPRIETARY
    }

    public CSRProfile() {
        this.profileType = ProfileType.SCEP;
        this.csrFormat = CSRFormat.PKCS10;
        this.keyAlgType = KeyAlgorithm.RSA;
        this.templateName = null;
        this.keyLength = 1024;
        this.commonName = null;
        this.organization = null;
        this.domainComponent = null;
        this.emailAddress = null;
        this.country = null;
        this.state = null;
        this.locality = null;
        this.profileType = ProfileType.SCEP;
        this.csrFormat = CSRFormat.PKCS10;
        this.keyAlgType = KeyAlgorithm.RSA;
    }

    private CSRProfile(Parcel parcel) {
        this.profileType = ProfileType.SCEP;
        this.csrFormat = CSRFormat.PKCS10;
        this.keyAlgType = KeyAlgorithm.RSA;
        this.templateName = null;
        this.keyLength = 1024;
        this.commonName = null;
        this.organization = null;
        this.domainComponent = null;
        this.emailAddress = null;
        this.country = null;
        this.state = null;
        this.locality = null;
        try {
            this.profileType = ProfileType.valueOf(parcel.readString());
        } catch (Throwable e) {
            this.profileType = null;
            e.printStackTrace();
        }
        if (this.profileType == null) {
            this.profileType = ProfileType.SCEP;
        }
        try {
            this.csrFormat = CSRFormat.valueOf(parcel.readString());
        } catch (Throwable e2) {
            this.csrFormat = null;
            e2.printStackTrace();
        }
        if (this.csrFormat == null) {
            this.csrFormat = CSRFormat.PKCS10;
        }
        try {
            this.keyAlgType = KeyAlgorithm.valueOf(parcel.readString());
        } catch (Throwable e22) {
            this.keyAlgType = null;
            e22.printStackTrace();
        }
        if (this.keyAlgType == null) {
            this.keyAlgType = KeyAlgorithm.RSA;
        }
        this.templateName = parcel.readString();
        this.keyLength = parcel.readInt();
        this.commonName = parcel.readString();
        this.organization = parcel.readString();
        this.domainComponent = parcel.readString();
        this.emailAddress = parcel.readString();
        this.country = parcel.readString();
        this.state = parcel.readString();
        this.locality = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (this.profileType == null) {
            parcel.writeString(ProfileType.SCEP.name());
        } else {
            parcel.writeString(this.profileType.name());
        }
        if (this.csrFormat == null) {
            parcel.writeString(CSRFormat.PKCS10.name());
        } else {
            parcel.writeString(this.csrFormat.name());
        }
        if (this.keyAlgType == null) {
            parcel.writeString(KeyAlgorithm.RSA.name());
        } else {
            parcel.writeString(this.keyAlgType.name());
        }
        parcel.writeString(this.templateName);
        parcel.writeInt(this.keyLength);
        parcel.writeString(this.commonName);
        parcel.writeString(this.organization);
        parcel.writeString(this.domainComponent);
        parcel.writeString(this.emailAddress);
        parcel.writeString(this.country);
        parcel.writeString(this.state);
        parcel.writeString(this.locality);
    }
}
