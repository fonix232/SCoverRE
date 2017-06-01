package com.samsung.android.app.ledcover.common;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.SemSystemProperties;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.app.LCoverNoti_CustomDotView;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.EventBuilder;
import com.samsung.context.sdk.samsunganalytics.LogBuilders.ScreenViewBuilder;
import com.samsung.context.sdk.samsunganalytics.SamsungAnalytics;
import java.util.Locale;

public class Utils {
    public static final String TAG = "[LED_COVER]LedCoverMainUtills";

    public static boolean isSamsungMobile() {
        String brand = Build.BRAND.toLowerCase();
        String manufacture = Build.MANUFACTURER.toLowerCase();
        SLog.m12v(TAG, "isSamsungMobile() : " + brand + " , " + manufacture);
        if (brand.equals("samsung")) {
            return true;
        }
        if (brand.equals("google") || !manufacture.equals("samsung")) {
            return false;
        }
        return true;
    }

    public static void getAppInfo(Context mContext) {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            int localVersionCode = packageInfo.versionCode;
            String localVersionName = packageInfo.versionName;
            SLog.m12v(TAG, "[App Info] VersionCode : " + localVersionCode);
            SLog.m12v(TAG, "[App Info] VersionName : " + localVersionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceName(Context mContext) {
        String deviceName = System.getString(mContext.getContentResolver(), "device_name");
        if (deviceName == null) {
            return Global.getString(mContext.getContentResolver(), "device_name");
        }
        return deviceName;
    }

    public static void recursiveRecycle(View root) {
        if (root != null) {
            root.setBackground(null);
            if (root instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) root;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    recursiveRecycle(group.getChildAt(i));
                }
                if (root instanceof AdapterView) {
                    group.removeAllViewsInLayout();
                } else {
                    group.removeAllViews();
                }
            }
            if (root instanceof LCoverNoti_CustomDotView) {
                ((LCoverNoti_CustomDotView) root).destroyDrawingCache();
            }
            if (root instanceof ImageView) {
                ((ImageView) root).setImageDrawable(null);
            }
        }
    }

    public static Dialog loadingProgressBar(Context context) {
        Dialog mProgress = new Dialog(context, C0198R.style.MainLoadingProgress);
        mProgress.setCancelable(false);
        mProgress.addContentView(new ProgressBar(context), new LayoutParams(-2, -2));
        return mProgress;
    }

    public static String readPDAVersion() {
        String version = SemSystemProperties.get(Defines.VERSION_PDA, C0316a.f163d);
        SLog.m12v(TAG, "PDA Version : " + version);
        return version;
    }

    public static boolean isRtl() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public static void initSALog(Application application) {
        SamsungAnalytics.setConfiguration(application, new Configuration().setTrackingId(Defines.SA_TRACKING_ID).setVersion(Defines.SA_UI_VERSION).enableAutoDeviceId());
    }

    public static void sendScreenViewSALog(String id) {
        SamsungAnalytics.getInstance().sendLog(((ScreenViewBuilder) new ScreenViewBuilder().setScreenView(id)).build());
    }

    public static void sendEventSALog(String screenId, String eventId, String eventDetail) {
        SamsungAnalytics.getInstance().sendLog(((EventBuilder) new EventBuilder().setEventName(eventId).setScreenView(screenId)).build());
    }

    public static void sendEventSALog(String screenId, String eventId, String eventDetail, long eventValue) {
        SamsungAnalytics.getInstance().sendLog(((EventBuilder) new EventBuilder().setEventName(eventId).setEventDetail(String.valueOf(eventValue)).setScreenView(screenId)).build());
    }

    private static void setTextSize(Context context, TextView view, float textSize, float maxSizeScale) {
        if (view != null) {
            float fontScale = context.getResources().getConfiguration().fontScale;
            float defaultTextSize = textSize / fontScale;
            if (fontScale > maxSizeScale) {
                view.setTextSize(0, defaultTextSize * maxSizeScale);
            }
        }
    }

    public static void setLargeTextSize(Context context, TextView view, float textSize) {
        setTextSize(context, view, textSize, 1.2f);
    }
}
