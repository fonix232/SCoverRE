package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class CocktailInfo implements Parcelable {
    public static final Creator<CocktailInfo> CREATOR = new C10131();
    private int mCategory = 1;
    private ComponentName mClassInfo = null;
    private Bundle mContentInfo = null;
    private RemoteViews mContentView = null;
    private int mDisplayPolicy = 1;
    private RemoteViews mHelpView = null;
    private int mOrientation = 1;
    private int mUserId = 0;

    static class C10131 implements Creator<CocktailInfo> {
        C10131() {
        }

        public CocktailInfo createFromParcel(Parcel parcel) {
            CocktailInfo cocktailInfo = new CocktailInfo();
            cocktailInfo.readFromParcel(parcel);
            return cocktailInfo;
        }

        public CocktailInfo[] newArray(int i) {
            return new CocktailInfo[i];
        }
    }

    public static class Builder {
        private int mCategory = 1;
        private ComponentName mClassInfo = null;
        private Bundle mContentInfo = null;
        private RemoteViews mContentView = null;
        private Context mContext = null;
        private int mDisplayPolicy = 1;
        private RemoteViews mHelpView = null;
        private int mOrientation = 1;

        public Builder(Context context) {
            this.mContext = context;
        }

        public CocktailInfo build() {
            CocktailInfo cocktailInfo = new CocktailInfo();
            cocktailInfo.mOrientation = this.mOrientation;
            cocktailInfo.mDisplayPolicy = this.mDisplayPolicy;
            cocktailInfo.mCategory = this.mCategory;
            cocktailInfo.mContentView = this.mContentView;
            cocktailInfo.mHelpView = this.mHelpView;
            cocktailInfo.mUserId = this.mContext.getUserId();
            cocktailInfo.mContentInfo = this.mContentInfo;
            cocktailInfo.mClassInfo = this.mClassInfo;
            return cocktailInfo;
        }

        public Builder setCategory(int i) {
            this.mCategory = i;
            return this;
        }

        public Builder setClassloader(ComponentName componentName) {
            this.mClassInfo = componentName;
            return this;
        }

        public Builder setContentInfo(Bundle bundle) {
            this.mContentInfo = bundle;
            return this;
        }

        public Builder setContentView(RemoteViews remoteViews) {
            this.mContentView = remoteViews;
            return this;
        }

        public Builder setDiplayPolicy(int i) {
            this.mDisplayPolicy = i;
            return this;
        }

        public Builder setHelpView(RemoteViews remoteViews) {
            this.mHelpView = remoteViews;
            return this;
        }

        public Builder setOrientation(int i) {
            this.mOrientation = i;
            return this;
        }
    }

    public int describeContents() {
        return 0;
    }

    public String dump() {
        String str = "U:" + this.mUserId + " ORI:" + this.mOrientation + " DP:" + this.mDisplayPolicy + " CAT:" + this.mCategory;
        if (this.mContentView != null) {
            str = str + " has RemoteViews";
        }
        if (this.mContentInfo != null) {
            str = str + " has ContentInfo";
        }
        if (this.mClassInfo != null) {
            str = str + " ClassInfo : " + this.mClassInfo.flattenToShortString();
        }
        return this.mHelpView != null ? str + " has HelpView" : str;
    }

    public int getCategory() {
        return this.mCategory;
    }

    public ComponentName getClassInfo() {
        return this.mClassInfo;
    }

    public Bundle getContentInfo() {
        return this.mContentInfo;
    }

    public RemoteViews getContentView() {
        return this.mContentView;
    }

    public int getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    public RemoteViews getHelpView() {
        return this.mHelpView;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void mergeInfo(CocktailInfo cocktailInfo) {
        this.mOrientation = cocktailInfo.mOrientation;
        this.mCategory = cocktailInfo.mCategory;
        this.mDisplayPolicy = cocktailInfo.mDisplayPolicy;
        if (cocktailInfo.mContentInfo != null) {
            this.mContentInfo = cocktailInfo.mContentInfo;
            this.mContentView = null;
        }
        if (cocktailInfo.mContentView != null) {
            this.mContentView = cocktailInfo.mContentView;
            this.mContentInfo = null;
        }
        if (cocktailInfo.mHelpView != null) {
            this.mHelpView = cocktailInfo.mHelpView;
        }
        if (cocktailInfo.mClassInfo != null) {
            this.mClassInfo = cocktailInfo.mClassInfo;
        }
    }

    public void readFromParcel(Parcel parcel) {
        this.mContentView = (RemoteViews) parcel.readParcelable(RemoteViews.class.getClassLoader());
        this.mHelpView = (RemoteViews) parcel.readParcelable(RemoteViews.class.getClassLoader());
        this.mUserId = parcel.readInt();
        this.mOrientation = parcel.readInt();
        this.mDisplayPolicy = parcel.readInt();
        this.mCategory = parcel.readInt();
        this.mContentInfo = parcel.readBundle();
        this.mClassInfo = parcel.readInt() != 0 ? new ComponentName(parcel) : null;
    }

    public void setCategory(int i) {
        this.mCategory = i;
    }

    public void updateContentView(RemoteViews remoteViews, boolean z) {
        if (!z || this.mContentView == null) {
            this.mContentView = remoteViews;
        } else {
            this.mContentView.mergeRemoteViews(remoteViews);
        }
    }

    public void updateHelpView(RemoteViews remoteViews, boolean z) {
        if (!z || this.mHelpView == null) {
            this.mHelpView = remoteViews;
        } else {
            this.mHelpView.mergeRemoteViews(remoteViews);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mContentView, i);
        parcel.writeParcelable(this.mHelpView, i);
        parcel.writeInt(this.mUserId);
        parcel.writeInt(this.mOrientation);
        parcel.writeInt(this.mDisplayPolicy);
        parcel.writeInt(this.mCategory);
        parcel.writeBundle(this.mContentInfo);
        if (this.mClassInfo != null) {
            parcel.writeInt(1);
            this.mClassInfo.writeToParcel(parcel, i);
            return;
        }
        parcel.writeInt(0);
    }
}
