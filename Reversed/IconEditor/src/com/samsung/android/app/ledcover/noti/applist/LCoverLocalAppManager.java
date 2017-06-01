package com.samsung.android.app.ledcover.noti.applist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LCoverLocalAppManager implements ILCoverManager {
    private static final String TAG = "[LED_COVER]LCoverLocalAppManager";
    private List<ResolveInfo> mAppList;
    private Context mContext;

    public LCoverLocalAppManager(Context mContext2) {
        this.mContext = mContext2;
    }

    public void updateAppList() {
        SLog.m12v(TAG, "updateAppList()");
        Intent appListIntent = new Intent("android.intent.action.MAIN", null);
        appListIntent.addCategory("android.intent.category.LAUNCHER");
        this.mAppList = this.mContext.getPackageManager().queryIntentActivities(appListIntent, 0);
    }

    public ArrayList<String> updateMsgAppList() {
        SLog.m12v(TAG, "updateMsgAppList()");
        ArrayList<String> msgAppListArray = new ArrayList();
        Intent appListIntent = new Intent("android.intent.action.MAIN", null);
        appListIntent.addCategory("android.intent.category.APP_MESSAGING");
        for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentActivities(appListIntent, 0)) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            SLog.m12v(TAG, "Message App : " + pkgName);
            msgAppListArray.add(pkgName);
        }
        return msgAppListArray;
    }

    public ArrayList<LCoverAppInfo> getInstalledAppList() {
        SLog.m12v(TAG, "getInstalledAppList()");
        updateAppList();
        ArrayList<String> msgAppListArray = updateMsgAppList();
        ArrayList<LCoverAppInfo> mAppInfoList = new ArrayList();
        ArrayList<String> excludeAppList = getExcludeAppList();
        for (ResolveInfo resolveInfo : this.mAppList) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (!(excludeAppList.contains(pkgName) || msgAppListArray.contains(pkgName))) {
                boolean isAdded = false;
                for (int i = 0; i < mAppInfoList.size(); i++) {
                    if (((LCoverAppInfo) mAppInfoList.get(i)).getPackageName().equals(pkgName)) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    mAppInfoList.add(new LCoverAppInfo(0, pkgName, resolveInfo.activityInfo.applicationInfo.loadIcon(this.mContext.getPackageManager()), resolveInfo.activityInfo.applicationInfo.loadLabel(this.mContext.getPackageManager()).toString(), false, 0, null));
                }
            }
        }
        return mAppInfoList;
    }

    private ArrayList<String> getExcludeAppList() {
        ArrayList<String> excludeAppList = new ArrayList();
        XmlPullParser xpp = this.mContext.getResources().getXml(C0198R.xml.vn_exclude_apps);
        if (xpp != null) {
            try {
                for (int eventType = xpp.getEventType(); eventType != 1; eventType = xpp.next()) {
                    switch (eventType) {
                        case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                            excludeAppList.add(xpp.getText());
                            break;
                        default:
                            break;
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return excludeAppList;
    }

    private LCoverAppInfo getMissedCallAppInfo() {
        return new LCoverAppInfo(0, Defines.PKG_MISSED_CALL, this.mContext.getResources().getDrawable(C0198R.drawable.icon_noti_missed_call), this.mContext.getResources().getString(C0198R.string.led_notifications_missed_call), false, 0, null);
    }
}
