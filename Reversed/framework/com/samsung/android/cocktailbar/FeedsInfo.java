package com.samsung.android.cocktailbar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class FeedsInfo implements Parcelable {
    public static final Creator<FeedsInfo> CREATOR = new C10151();
    public Bundle extras;
    public CharSequence feedsText;
    public int icon;
    public Bitmap largeIcon;
    public String packageName;

    static class C10151 implements Creator<FeedsInfo> {
        C10151() {
        }

        public FeedsInfo createFromParcel(Parcel parcel) {
            return new FeedsInfo(parcel);
        }

        public FeedsInfo[] newArray(int i) {
            return new FeedsInfo[i];
        }
    }

    public static final class Builder {
        private Bundle mExtras;
        private CharSequence mFeedsText;
        private int mIcon;
        private Bitmap mLargeIcon;
        private String mPackageName;

        public Builder(CharSequence charSequence, String str) {
            this.mFeedsText = charSequence;
            this.mPackageName = str;
        }

        public FeedsInfo build() {
            FeedsInfo feedsInfo = new FeedsInfo(this.mFeedsText, this.mPackageName);
            feedsInfo.icon = this.mIcon;
            feedsInfo.largeIcon = this.mLargeIcon;
            feedsInfo.extras = this.mExtras != null ? new Bundle(this.mExtras) : new Bundle();
            return feedsInfo;
        }

        public Builder setExtras(Bundle bundle) {
            this.mExtras = bundle;
            return this;
        }

        public Builder setIcon(int i) {
            this.mIcon = i;
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            this.mLargeIcon = bitmap;
            return this;
        }
    }

    public FeedsInfo(Parcel parcel) {
        this.extras = new Bundle();
        this.feedsText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.icon = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.largeIcon = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
        }
        this.packageName = parcel.readString();
        this.extras = parcel.readBundle();
    }

    private FeedsInfo(CharSequence charSequence, String str) {
        this.extras = new Bundle();
        this.feedsText = charSequence;
        this.packageName = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.feedsText, parcel, i);
        parcel.writeInt(this.icon);
        if (this.largeIcon != null) {
            parcel.writeInt(1);
            this.largeIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(this.packageName);
        this.extras = parcel.readBundle();
    }
}
