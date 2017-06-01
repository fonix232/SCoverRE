package com.samsung.android.app.ledcover.info;

import android.content.ContentValues;
import android.os.Parcel;

public class LCoverIconInfo {
    private LCoverDotInfo[] dotDataClass;
    private boolean enableDraw;
    private int listIndex;
    public int mCallerIDCount;
    public boolean mCheckBox;
    public int mCnt;
    public String mIcon;
    public int mIconInt;
    public int mId;
    public String mName;

    public LCoverIconInfo(int id, String icon, String name, int cnt) {
        this.mId = id;
        this.mIcon = icon;
        this.mName = name;
        this.mCnt = cnt;
        this.mCallerIDCount = 0;
    }

    public LCoverIconInfo(LCoverIconInfo userData) {
        int i;
        int ledMatrixTotal = Defines.getLedMatrixTotal();
        LCoverDotInfo[] dotInfo = new LCoverDotInfo[ledMatrixTotal];
        for (i = 0; i < ledMatrixTotal; i++) {
            dotInfo[i] = new LCoverDotInfo();
        }
        for (i = 0; i < userData.getIconArray().length(); i++) {
            if (userData.getIconArray().charAt(i) == Defines.DOT_ENABLE) {
                dotInfo[i].setDotEnable(true);
            } else if (userData.getIconArray().charAt(i) == Defines.DOT_DISABLE) {
                dotInfo[i].setDotEnable(false);
            }
        }
        setDotDataClass(dotInfo);
        this.enableDraw = userData.enableDraw;
        this.listIndex = userData.listIndex;
        this.mId = userData.mId;
        this.mName = userData.mName;
        this.mCnt = userData.mCnt;
        this.mCheckBox = userData.mCheckBox;
    }

    public LCoverIconInfo(int id, String name, int cnt) {
        int ledMatrixTotal = Defines.getLedMatrixTotal();
        this.mId = id;
        this.mName = name;
        this.mCnt = cnt;
        this.enableDraw = false;
        this.dotDataClass = new LCoverDotInfo[ledMatrixTotal];
        for (int i = 0; i < ledMatrixTotal; i++) {
            this.dotDataClass[i] = new LCoverDotInfo();
        }
    }

    public LCoverIconInfo(int id, int icon, String name, int cnt) {
        this.mId = id;
        this.mIconInt = icon;
        this.mName = name;
        this.mCnt = cnt;
    }

    protected LCoverIconInfo(Parcel in) {
        this.mId = in.readInt();
        this.mIcon = in.readString();
        this.mName = in.readString();
        this.mCnt = in.readInt();
    }

    public void increaseCallerIDCount() {
        this.mCallerIDCount++;
    }

    public void resetCallerIDCount() {
        this.mCallerIDCount = 0;
    }

    public void setId(int _id) {
        this.mId = _id;
    }

    public void setIconArray(String _icon) {
        this.mIcon = _icon;
    }

    public void setIconName(String _name) {
        this.mName = _name;
    }

    public void setIconArray(int _icon) {
        this.mIconInt = _icon;
    }

    public void setCount(int _cnt) {
        this.mCnt = _cnt;
    }

    public int getId() {
        return this.mId;
    }

    public String getIconArray() {
        return this.mIcon;
    }

    public String getIconName() {
        return this.mName;
    }

    public int getIconArrayInt() {
        return this.mIconInt;
    }

    public int getCount() {
        return this.mCnt;
    }

    public Boolean getIsChecked() {
        return Boolean.valueOf(this.mCheckBox);
    }

    public int getListIndex() {
        return this.listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public LCoverDotInfo[] getDotDataClass() {
        return this.dotDataClass;
    }

    public void setDotDataClass(LCoverDotInfo[] dotDataClass) {
        this.dotDataClass = dotDataClass;
    }

    public boolean isEnableDraw() {
        return this.enableDraw;
    }

    public void setEnableDraw(boolean enableDraw) {
        this.enableDraw = enableDraw;
    }

    public void setIsChecked(Boolean mCheckBox) {
        this.mCheckBox = mCheckBox.booleanValue();
    }

    public ContentValues contentValues() {
        ContentValues values = new ContentValues();
        if (this.mIcon.equals(Defines.PRESET_ICON_ARRAY)) {
            values.put(Defines.ICON_COL_ID, Integer.valueOf(this.mId));
        }
        values.put(Defines.ICON_COL_ICON_ARRAY, this.mIcon);
        values.put(Defines.ICON_COL_ICON_NAME, this.mName);
        values.put(Defines.ICON_COL_COUNT, Integer.valueOf(this.mCnt));
        return values;
    }

    public LCoverDotInfo getDotInstance(int position, boolean selected) {
        return null;
    }
}
