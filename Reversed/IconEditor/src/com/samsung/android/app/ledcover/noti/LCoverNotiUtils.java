package com.samsung.android.app.ledcover.noti;

import android.content.Context;
import android.provider.Settings.Secure;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.noti.applist.LCoverLocalAppManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LCoverNotiUtils {
    public static final String TAG = "[LED_COVER]LCoverNotiUtils";
    private Context mContext;
    private ArrayList<LCoverAppInfo> mInstalledAppList;
    LCoverLocalAppManager mLCoverManager;

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiUtils.1 */
    class C02571 implements Comparator<LCoverAppInfo> {
        C02571() {
        }

        public int compare(LCoverAppInfo o1, LCoverAppInfo o2) {
            return o1.getAppName().compareToIgnoreCase(o2.getAppName());
        }
    }

    public LCoverNotiUtils(Context context) {
        this.mLCoverManager = null;
        this.mInstalledAppList = null;
        this.mContext = context;
        this.mLCoverManager = LCoverSingleton.getInstance().getlManager(this.mContext);
    }

    private void updateAllAppList() {
        SLog.m12v(TAG, "updateAllAppList()");
        this.mInstalledAppList = this.mLCoverManager.getInstalledAppList();
        Collections.sort(this.mInstalledAppList, new C02571());
        SLog.m12v(TAG, "updateAllAppList() size " + this.mInstalledAppList.size());
    }

    public ArrayList<LCoverAppInfo> getInstalledAppList() {
        updateAllAppList();
        return this.mInstalledAppList;
    }

    public static boolean isAccessibilityON(Context context) {
        String enabledNotificationListeners = Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        String packageName = context.getPackageName();
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName)) {
            return false;
        }
        return true;
    }
}
