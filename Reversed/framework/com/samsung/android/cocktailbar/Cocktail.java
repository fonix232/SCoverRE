package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;

public class Cocktail {
    public static final Creator<Cocktail> CREATOR = new C10091();
    public static final int STATE_DISABLE = 2;
    public static final int STATE_ENABLE = 1;
    public static final int STATE_NONE = 0;
    private PendingIntent mBroadcast;
    private int mCocktailId;
    private CocktailInfo mCocktailInfo;
    private boolean mEnable;
    private boolean mIsPackageUpdated;
    private CocktailProviderInfo mProviderInfo;
    private int mState;
    private int mUid;
    private int mVersion;

    static class C10091 implements Creator<Cocktail> {
        C10091() {
        }

        public Cocktail createFromParcel(Parcel parcel) {
            Cocktail cocktail = new Cocktail();
            cocktail.readFromParcel(parcel);
            return cocktail;
        }

        public Cocktail[] newArray(int i) {
            return new Cocktail[i];
        }
    }

    public Cocktail() {
        this.mUid = 0;
        this.mCocktailInfo = new CocktailInfo();
        this.mVersion = 1;
        this.mState = 0;
        this.mEnable = true;
        this.mIsPackageUpdated = false;
    }

    public Cocktail(int i) {
        this.mUid = 0;
        this.mCocktailInfo = new CocktailInfo();
        this.mVersion = 1;
        this.mState = 0;
        this.mEnable = true;
        this.mIsPackageUpdated = false;
        this.mCocktailId = i;
    }

    public Cocktail(int i, CocktailInfo cocktailInfo) {
        this(i);
        this.mCocktailInfo = cocktailInfo;
    }

    public static String getUpdateIntentName(int i) {
        switch (i) {
            case 1:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE;
            case 2:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE_V2;
            default:
                return CocktailBarManager.ACTION_COCKTAIL_UPDATE;
        }
    }

    @Deprecated
    public void addCocktailInfo(CocktailInfo cocktailInfo) {
        this.mCocktailInfo = cocktailInfo;
    }

    public int describeContents() {
        return 0;
    }

    public String dump() {
        String str = "[CocktailId:" + this.mCocktailId + " uid:" + this.mUid + " version:" + this.mVersion + " state:" + this.mState;
        if (this.mBroadcast != null) {
            str = str + " has broadcast";
        }
        if (this.mCocktailInfo != null) {
            str = str + " " + this.mCocktailInfo.dump();
        }
        return str + " ]";
    }

    public PendingIntent getBroadcast() {
        return this.mBroadcast;
    }

    public int getCocktailId() {
        return this.mCocktailId;
    }

    public CocktailInfo getCocktailInfo() {
        return this.mCocktailInfo;
    }

    public ComponentName getProvider() {
        return this.mProviderInfo != null ? this.mProviderInfo.provider : null;
    }

    public CocktailProviderInfo getProviderInfo() {
        return this.mProviderInfo;
    }

    public int getState() {
        return this.mState;
    }

    public int getUid() {
        return this.mUid;
    }

    public String getUpdateIntentName() {
        return getUpdateIntentName(this.mVersion);
    }

    public int getVersion() {
        return this.mVersion;
    }

    public boolean isPackageUpdated() {
        return this.mIsPackageUpdated;
    }

    public void readFromParcel(Parcel parcel) {
        this.mCocktailId = parcel.readInt();
        this.mUid = parcel.readInt();
        this.mVersion = parcel.readInt();
        this.mState = parcel.readInt();
        this.mBroadcast = (PendingIntent) parcel.readParcelable(PendingIntent.class.getClassLoader());
        this.mProviderInfo = (CocktailProviderInfo) parcel.readParcelable(ComponentName.class.getClassLoader());
        this.mCocktailInfo = (CocktailInfo) parcel.readParcelable(CocktailInfo.class.getClassLoader());
        this.mIsPackageUpdated = parcel.readByte() == (byte) 1;
    }

    public void setBroadcast(PendingIntent pendingIntent) {
        this.mBroadcast = pendingIntent;
    }

    public void setPackageUpdated(boolean z) {
        this.mIsPackageUpdated = z;
    }

    public void setProviderInfo(CocktailProviderInfo cocktailProviderInfo) {
        this.mProviderInfo = cocktailProviderInfo;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public void setUid(int i) {
        this.mUid = i;
    }

    public void setVersion(int i) {
        this.mVersion = i;
    }

    public void updateCocktailContentView(RemoteViews remoteViews, boolean z) {
        if (this.mCocktailInfo != null) {
            this.mCocktailInfo.updateContentView(remoteViews, z);
        }
    }

    public void updateCocktailHelpView(RemoteViews remoteViews, boolean z) {
        if (this.mCocktailInfo != null) {
            this.mCocktailInfo.updateHelpView(remoteViews, z);
        }
    }

    public void updateCocktailInfo(CocktailInfo cocktailInfo) {
        if (this.mCocktailInfo == null || cocktailInfo == null) {
            this.mCocktailInfo = cocktailInfo;
        } else {
            this.mCocktailInfo.mergeInfo(cocktailInfo);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mCocktailId);
        parcel.writeInt(this.mUid);
        parcel.writeInt(this.mVersion);
        parcel.writeInt(this.mState);
        parcel.writeParcelable(this.mBroadcast, i);
        parcel.writeParcelable(this.mProviderInfo, i);
        parcel.writeParcelable(this.mCocktailInfo, i);
        if (this.mIsPackageUpdated) {
            parcel.writeByte((byte) 1);
        } else {
            parcel.writeByte((byte) 0);
        }
    }
}
