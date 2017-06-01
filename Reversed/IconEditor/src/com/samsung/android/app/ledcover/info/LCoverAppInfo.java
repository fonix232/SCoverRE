package com.samsung.android.app.ledcover.info;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LCoverAppInfo implements Parcelable {
    public static final Creator<LCoverAppInfo> CREATOR;
    private Drawable mAppIcon;
    private String mAppName;
    private String mIconArray;
    private int mIconId;
    private int mId;
    private boolean mIsChecked;
    private String mPackageName;

    /* renamed from: com.samsung.android.app.ledcover.info.LCoverAppInfo.1 */
    static class C02471 implements Creator<LCoverAppInfo> {
        C02471() {
        }

        public LCoverAppInfo createFromParcel(Parcel in) {
            return new LCoverAppInfo(in);
        }

        public LCoverAppInfo[] newArray(int size) {
            return new LCoverAppInfo[size];
        }
    }

    public LCoverAppInfo(int id, String pkgName, Drawable icon, String appName, boolean isChecked, int iconId, String iconArray) {
        this.mId = id;
        this.mPackageName = pkgName;
        this.mAppIcon = icon;
        this.mIconArray = iconArray;
        this.mAppName = appName;
        this.mIsChecked = isChecked;
        this.mIconId = iconId;
    }

    protected LCoverAppInfo(Parcel in) {
        this.mId = in.readInt();
        this.mAppName = in.readString();
        this.mPackageName = in.readString();
        this.mIconId = in.readInt();
        this.mIconArray = in.readString();
    }

    static {
        CREATOR = new C02471();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.mIsChecked ? 1 : 0));
        dest.writeInt(this.mId);
        dest.writeString(this.mAppName);
        dest.writeString(this.mPackageName);
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int _id) {
        this.mId = _id;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public void setIconId(int _id) {
        this.mIconId = _id;
    }

    public String getIconArray() {
        return this.mIconArray;
    }

    public void setIconArray(String _icon_array) {
        this.mIconArray = _icon_array;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String mPackgeName) {
        this.mPackageName = mPackgeName;
    }

    public Drawable getAppIcon() {
        return this.mAppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.mAppIcon = appIcon;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public Boolean getIsChecked() {
        return Boolean.valueOf(this.mIsChecked);
    }

    public void setIsChecked(Boolean isChecked) {
        this.mIsChecked = isChecked.booleanValue();
    }

    public ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(Defines.PKG_COL_PACKAGE_NAME, this.mPackageName);
        values.put(Defines.PKG_COL_ICON_INDEX, Integer.valueOf(this.mIconId));
        return values;
    }
}
