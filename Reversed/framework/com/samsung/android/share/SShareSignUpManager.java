package com.samsung.android.share;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager;

public class SShareSignUpManager {
    private static final Uri BASE_CONTENT_URI_PUBLIC = Uri.parse("content://com.samsung.android.coreapps.easysignup.public");
    private static final int SERVICE_OFF = 0;
    private static final int SERVICE_ON = 1;
    private static final String TAG = "SShareSignUpManager";

    private static int[] convertToIntArray(String str) {
        int[] iArr = null;
        if (!(TextUtils.isEmpty(str) || str.equals("[]"))) {
            String[] split = str.replaceAll("\\[", "").replaceAll("\\]", "").split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            iArr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                try {
                    iArr[i] = Integer.parseInt(split[i]);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return iArr;
    }

    public static int getServiceStatus(Context context, int i) {
        int i2 = 0;
        String str = "";
        if (!isSupprotSettings(context)) {
            Cursor query = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("sids").build(), new String[]{"sids"}, null, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    str = query.getString(query.getColumnIndex("sids"));
                }
                query.close();
            }
        } else if (isAuth(context)) {
            str = Global.getString(context.getContentResolver(), "coreapps_on_sids");
        }
        Log.d(TAG, "getServiceStatus - sids : " + str);
        int[] convertToIntArray = convertToIntArray(str);
        if (convertToIntArray != null) {
            for (int i3 : convertToIntArray) {
                if (i == i3) {
                    i2 = 1;
                    break;
                }
            }
        }
        if (i2 == 1) {
            Log.d(TAG, "getServiceStatus : serviceId (" + i + ") is ON");
        } else {
            Log.d(TAG, "getServiceStatus : serviceId (" + i + ") is OFF");
        }
        return i2;
    }

    public static int getSupportedFeatures(Context context, int i) {
        int i2 = -1;
        if (isOwner(context)) {
            if (isSupprotSettings(context)) {
                i2 = Global.getInt(context.getContentResolver(), "coreapps_features_sid_" + i, -1);
            } else {
                Uri withAppendedPath = Uri.withAppendedPath(Uri.withAppendedPath(BASE_CONTENT_URI_PUBLIC, "features"), String.valueOf(i));
                Cursor query = context.getContentResolver().query(withAppendedPath, new String[]{"features"}, null, null, null);
                if (query != null) {
                    if (query.moveToFirst()) {
                        i2 = query.getInt(query.getColumnIndex("features"));
                    }
                    query.close();
                }
            }
        }
        Log.d(TAG, "serviceId : " + i + ", features : " + i2);
        return i2;
    }

    public static boolean isAuth(Context context) {
        boolean z = false;
        if (!isSupprotSettings(context)) {
            Cursor query = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("is_auth").build(), null, null, null, null);
            if (query != null) {
                if (query.getCount() > 0) {
                    z = true;
                }
                query.close();
            }
        } else if (Global.getInt(context.getContentResolver(), "coreapps", 0) == 1) {
            z = true;
        }
        Log.d(TAG, "isAuth is " + z);
        return z;
    }

    public static boolean isJoined(Context context, int i) {
        String str = "";
        boolean z = false;
        if (!isSupprotSettings(context)) {
            Cursor query = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("join_sids").build(), new String[]{"join_sids"}, null, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    str = query.getString(query.getColumnIndex("join_sids"));
                }
                query.close();
            }
        } else if (isAuth(context)) {
            str = Global.getString(context.getContentResolver(), "coreapps_join_sids");
        }
        Log.d(TAG, "isJoined - join sids : " + str);
        int[] convertToIntArray = convertToIntArray(str);
        if (convertToIntArray != null) {
            for (int i2 : convertToIntArray) {
                if (i == i2) {
                    z = true;
                    break;
                }
            }
        }
        Log.d(TAG, "isJoined : serviceId (" + i + ") is joined " + z);
        return z;
    }

    private static boolean isOwner(Context context) {
        boolean z = false;
        UserHandle myUserHandle = Process.myUserHandle();
        UserManager userManager = (UserManager) context.getSystemService("user");
        if (userManager == null) {
            return false;
        }
        long serialNumberForUser = userManager.getSerialNumberForUser(myUserHandle);
        Log.d(TAG, "userSerialNumber = " + serialNumberForUser);
        if (0 == serialNumberForUser) {
            z = true;
        }
        return z;
    }

    private static boolean isSupprotSettings(Context context) {
        return VERSION.SDK_INT < 23;
    }
}
