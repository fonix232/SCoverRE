package com.samsung.android.app.ledcover.common;

import android.content.Context;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.db.LCoverDbHelper;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverDotInfo;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.noti.LCoverNotiUtils;
import com.samsung.android.app.ledcover.noti.applist.LCoverLocalAppManager;
import java.util.ArrayList;

public class LCoverSingleton {
    public static final String TAG = "[LED_COVER]LCoverSingleton";
    private static LCoverSingleton singleton;
    private ArrayList<LCoverIconInfo> CustomLEDList;
    private LCoverLocalAppManager lManager;
    private LCoverDbAccessor mDbAccessor;
    private LCoverDbHelper mDbHelper;
    private LCoverNotiUtils mLCoverNotiUtils;

    public static synchronized LCoverSingleton getInstance() {
        LCoverSingleton lCoverSingleton;
        synchronized (LCoverSingleton.class) {
            if (singleton == null) {
                SLog.m12v(TAG, "<<Start Singleton>>");
                singleton = new LCoverSingleton();
            }
            lCoverSingleton = singleton;
        }
        return lCoverSingleton;
    }

    public void LoadingCustomIconDataBase(LCoverIconInfo iconInfo) {
        SLog.m12v(TAG, "[Loading DB] Dot getIconName : " + iconInfo.getIconName());
        LCoverDotInfo[] dotInfo = iconInfo.getDotDataClass();
        for (int i = 0; i < iconInfo.getIconArray().length(); i++) {
            if (iconInfo.getIconArray().charAt(i) == Defines.DOT_ENABLE) {
                dotInfo[i].setDotEnable(true);
            } else if (iconInfo.getIconArray().charAt(i) == Defines.DOT_DISABLE) {
                dotInfo[i].setDotEnable(false);
            }
        }
        iconInfo.setDotDataClass(dotInfo);
        iconInfo.setListIndex(this.CustomLEDList.size());
        if (searchCustomUserLED(iconInfo)) {
            this.CustomLEDList.add(iconInfo);
            SLog.m12v(TAG, "[setCustomUserLED] Set User List");
            return;
        }
        SLog.m12v(TAG, "[setCustomUserLED] add not User List");
    }

    public boolean searchCustomUserLED(LCoverIconInfo iconInfo) {
        boolean addListItem = true;
        if (this.CustomLEDList != null) {
            for (int i = 0; i < this.CustomLEDList.size(); i++) {
                if (iconInfo.getId() == ((LCoverIconInfo) this.CustomLEDList.get(i)).getId()) {
                    SLog.m12v(TAG, "[searchCustomUserLED] modify Data");
                    addListItem = false;
                    this.CustomLEDList.set(i, iconInfo);
                }
            }
        }
        return addListItem;
    }

    public LCoverIconInfo getCustomLEDList(int arrayID) {
        if (this.CustomLEDList != null) {
            for (int i = 0; i < this.CustomLEDList.size(); i++) {
                if (((LCoverIconInfo) this.CustomLEDList.get(i)).getId() == arrayID) {
                    return (LCoverIconInfo) this.CustomLEDList.get(i);
                }
            }
        } else {
            SLog.m12v(TAG, "[getCustomLEDList] Custom List instance null pointer ");
        }
        return null;
    }

    public void checkCustomLEDList() {
        for (int i = 0; i < this.CustomLEDList.size(); i++) {
            SLog.m12v(TAG, "[Check] Save user name: " + ((LCoverIconInfo) this.CustomLEDList.get(i)).getIconName());
        }
    }

    public ArrayList<LCoverIconInfo> getCustomLEDList() {
        return this.CustomLEDList;
    }

    public void setCustomLEDList(ArrayList<LCoverIconInfo> customLEDList) {
        SLog.m12v(TAG, "[setCustomLEDList] Set List ");
        this.CustomLEDList = customLEDList;
    }

    public LCoverNotiUtils getmLCoverNotiUtils(Context mContext) {
        if (this.mLCoverNotiUtils == null) {
            this.mLCoverNotiUtils = new LCoverNotiUtils(mContext);
        } else {
            SLog.m12v(TAG, "[getmLCoverNotiUtils] mLCoverNotiUtils  instance is not null value");
        }
        return this.mLCoverNotiUtils;
    }

    public LCoverLocalAppManager getlManager(Context mContext) {
        if (this.lManager == null) {
            this.lManager = new LCoverLocalAppManager(mContext);
        } else {
            SLog.m12v(TAG, "[getlManager] lManager  instance is not null value");
        }
        return this.lManager;
    }

    public LCoverDbHelper getmDbHelper(Context mContext) {
        if (this.mDbHelper == null) {
            this.mDbHelper = new LCoverDbHelper(mContext);
        } else {
            SLog.m12v(TAG, "[getmDbHelper] mDbHelper instance is not null value");
        }
        return this.mDbHelper;
    }

    public LCoverDbAccessor getDbAccessor(Context mContext) {
        if (this.mDbAccessor == null) {
            this.mDbAccessor = new LCoverDbAccessor(mContext);
        } else {
            SLog.m12v(TAG, "[getDbAccessor] mDbAccessor instance is not null value");
        }
        return this.mDbAccessor;
    }
}
