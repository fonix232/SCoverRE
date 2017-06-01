package com.samsung.android.share;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

public class SShareLogging {
    private static final boolean DEBUG = false;
    private static final String TAG = "SShareLogging";
    private Context mContext;
    private Intent mIntent;

    public SShareLogging(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
    }

    private boolean checkSurveyCondition(Intent intent) {
        String action = intent.getAction();
        return "android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action);
    }

    public void insertLog(String str, String str2) {
        if (!checkSurveyCondition(this.mIntent)) {
            return;
        }
        if (this.mContext.checkCallingOrSelfPermission(SShareConstants.SURVERY_PERMISSION) == 0) {
            Parcelable contentValues = new ContentValues();
            contentValues.put(SShareConstants.SURVEY_CONTENT_APPID, SShareConstants.SURVEY_APP_NAME);
            contentValues.put(SShareConstants.SURVEY_CONTENT_FEATURE, str);
            if (str2 != null) {
                contentValues.put(SShareConstants.SURVEY_CONTENT_EXTRA, str2);
            }
            Intent intent = new Intent();
            intent.setAction(SShareConstants.SURVERY_ACTION);
            intent.putExtra("data", contentValues);
            intent.setPackage(SShareConstants.SURVEY_TARGET_PACKAGE);
            this.mContext.sendBroadcast(intent);
            return;
        }
        Log.w(TAG, "insertLog : no permission of survey");
    }
}
