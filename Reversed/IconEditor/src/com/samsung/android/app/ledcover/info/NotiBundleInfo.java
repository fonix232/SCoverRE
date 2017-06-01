package com.samsung.android.app.ledcover.info;

public class NotiBundleInfo {
    public int mCount;
    public String mIconData;
    public int mIconId;
    public boolean mIsDefaultMsg;
    public boolean mIsMissedCall;
    public String mPkgName;

    public NotiBundleInfo(int count, String pkgName, int id, String data, boolean missedCall, boolean defaultMsg) {
        this.mCount = count;
        this.mPkgName = pkgName;
        this.mIconId = id;
        this.mIconData = data;
        this.mIsMissedCall = missedCall;
        this.mIsDefaultMsg = defaultMsg;
    }

    public NotiBundleInfo(int count, String pkgName, int id, String data) {
        this.mCount = count;
        this.mPkgName = pkgName;
        this.mIconId = id;
        this.mIconData = data;
    }

    public void setCnt(int _cnt) {
        this.mCount = _cnt;
    }

    public void setPkgName(String _name) {
        this.mPkgName = _name;
    }

    public void setIconId(int _id) {
        this.mIconId = _id;
    }

    public void setIconData(String _data) {
        this.mIconData = _data;
    }

    public void setMissedCall(boolean _isCall) {
        this.mIsMissedCall = _isCall;
    }

    public void setMissedMsg(boolean _isMsg) {
        this.mIsMissedCall = _isMsg;
    }
}
