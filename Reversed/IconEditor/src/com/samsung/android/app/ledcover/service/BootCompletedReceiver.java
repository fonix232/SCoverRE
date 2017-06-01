package com.samsung.android.app.ledcover.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.app.ledcover.BuildConfig;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.SharedPreferencesUtil;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.info.Defines;

public class BootCompletedReceiver extends BroadcastReceiver {
    public static final String TAG = "[LED_COVER]BootCompletedReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SLog.m12v(TAG, "BootCompletedReceiver onReceive() ACTION_BOOT_COMPLETED");
            String savedVersion = SharedPreferencesUtil.getKeyPDAVersion(context);
            String currentVersion = Utils.readPDAVersion();
            SharedPreferencesUtil.setKeyRebootingFlag(context, false);
            if (savedVersion.equals(currentVersion)) {
                Intent iService = new Intent();
                iService.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, Defines.ICON_SERVICE_NAME));
                context.startService(iService);
                SLog.m12v(TAG, "BootCompletedReceiver onReceive() ACTION_BOOT_COMPLETED");
                return;
            }
            SLog.m12v(TAG, "FOTA Updating : " + savedVersion + " -> " + currentVersion);
            SharedPreferencesUtil.setKeyPDAVersion(context, currentVersion);
        }
    }
}
