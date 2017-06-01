package com.samsung.android.codecsolution;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.Log;
import com.samsung.android.feature.SemFloatingFeature;

public class Logging {
    private static final String ACTION_GENERAL_MULTI_LOGGING = "com.samsung.android.providers.context.log.action.USE_MULTI_APP_FEATURE_SURVEY";
    private static final String ACTION_GENERAL_SINGLE_LOGGING = "com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY";
    private static final String ACTION_STATUS_MULTI_LOGGING = "com.samsung.android.providers.context.log.action.REPORT_MULTI_APP_STATUS_SURVEY";
    private static final String ACTION_STATUS_SINGLE_LOGGING = "com.samsung.android.providers.context.log.action.REPORT_APP_STATUS_SURVEY";
    private static final String APP_ID = "com.samsung.android.codecsolution";
    private static final String TAG = "Logging";
    static Boolean sEnableSurveyFeature = Boolean.valueOf(SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE"));

    public static void insertLog(Context context, String str, String str2) {
        insertLog(context, APP_ID, str, str2, -1);
    }

    public static void insertLog(Context context, String str, String str2, long j) {
        insertLog(context, APP_ID, str, str2, j);
    }

    public static void insertLog(Context context, String str, String str2, String str3, long j) {
        Log.m29d(TAG, "appId: " + str + ", feature: " + str2 + ", extra: " + str3 + ", value: " + j);
        if (sEnableSurveyFeature.booleanValue()) {
            Parcelable contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("feature", str2);
            if (str3 != null) {
                contentValues.put("extra", str3);
            }
            if (j > -1) {
                contentValues.put("value", Long.valueOf(j));
            }
            Intent intent = new Intent();
            intent.setAction(ACTION_GENERAL_SINGLE_LOGGING);
            intent.putExtra("data", contentValues);
            intent.setPackage("com.samsung.android.providers.context");
            try {
                context.sendBroadcastAsUser(intent, UserHandle.CURRENT_OR_SELF);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
