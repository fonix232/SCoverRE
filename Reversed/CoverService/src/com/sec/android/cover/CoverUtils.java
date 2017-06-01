package com.sec.android.cover;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.PowerManager.WakeLock;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.sec.android.cover.ledcover.reflection.location.RefCountry;
import com.sec.android.cover.ledcover.reflection.location.RefCountryDetector;
import java.util.Locale;

public class CoverUtils {
    private static final String PACKAGE_NAME_MOBILE_CARE = "com.samsung.mobilecare";
    private static final String TAG = "CoverUtils";
    private static final long WAKELOCK_MAX_TIME = 60000;

    public static boolean isTPhoneEnabled(Context context) {
        int isTwoPhoneRegistered = Global.getInt(context.getContentResolver(), Constants.SETTINGS_TWOPHONE_REGISTERED, 0);
        int isTPhoneEnabled = System.semGetIntForUser(context.getContentResolver(), Constants.SETTINGS_T_PHONE_ENABLED, 0, -2);
        if (isTwoPhoneRegistered == 1 || isTPhoneEnabled == 1) {
            return true;
        }
        return false;
    }

    public static boolean isTphoneRelaxMode(Context context) {
        if (isTPhoneEnabled(context) && System.getInt(context.getContentResolver(), Constants.SETTINGS_T_PHONE_RELAX_MODE, 0) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isPackageExist(Context context, String targetPackage) {
        for (ApplicationInfo packageInfo : context.getPackageManager().getInstalledApplications(0)) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPackageInstalled(Context context, String pkgName) {
        try {
            if (context.getPackageManager().getApplicationInfo(pkgName, 128).enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean requestDumpUpload(Context context) {
        if (isPackageInstalled(context, PACKAGE_NAME_MOBILE_CARE)) {
            Log.d(TAG, "requestDumpUpload : Requesting dump upload to MC...");
            context.sendBroadcast(new Intent("com.sec.android.cover.intent.action.MOBILE_CARE_UPLOAD"));
            return true;
        }
        Log.e(TAG, "requestDumpUpload : MC is not installed");
        return false;
    }

    public static boolean isSetupWizardRunning(Context context) {
        if (Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 0) {
            return true;
        }
        return false;
    }

    public static byte[] getBytesFromHexString(String s) {
        if (TextUtils.isEmpty(s) || s.length() % 2 != 0) {
            throw new NumberFormatException("Invalid HEX string \"" + s + "\"");
        }
        byte[] data = new byte[(s.length() / 2)];
        for (int i = 0; i < s.length() / 2; i++) {
            int a = Character.digit(s.charAt(i * 2), 16);
            int b = Character.digit(s.charAt((i * 2) + 1), 16);
            if (a < 0 || b < 0) {
                throw new NumberFormatException("Invalid HEX string \"" + s + "\"");
            }
            data[i] = (byte) ((a << 4) + b);
        }
        return data;
    }

    public static String formatNumber(String number, TelephonyManager telephonyManager, Object countryDetector) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        String formattedNumber = null;
        Object country = RefCountryDetector.get().detectCountry(countryDetector);
        if (country != null) {
            formattedNumber = PhoneNumberUtils.formatNumber(number, RefCountry.get().getCountryIso(country));
        }
        if (TextUtils.isEmpty(formattedNumber)) {
            String iso = telephonyManager.getNetworkCountryIso();
            if (!TextUtils.isEmpty(iso)) {
                formattedNumber = PhoneNumberUtils.formatNumber(number, iso.toUpperCase());
            }
        }
        if (TextUtils.isEmpty(formattedNumber)) {
            return PhoneNumberUtils.formatNumber(number, Locale.getDefault().getCountry());
        }
        return formattedNumber;
    }

    public static void acquireWakeLockSafely(WakeLock wakelock) {
        if (wakelock == null) {
            Log.e(TAG, "Cannot acquire null wakelock!");
            return;
        }
        try {
            wakelock.acquire(WAKELOCK_MAX_TIME);
        } catch (Exception e) {
            Log.e(TAG, "Failed to acquire wakelock:" + wakelock.toString(), e);
        }
    }

    public static void releaseWakeLockSafely(WakeLock wakelock) {
        if (wakelock == null) {
            Log.e(TAG, "Cannot release null wakelock!");
            return;
        }
        try {
            wakelock.release();
        } catch (Exception e) {
            Log.e(TAG, "Failed to release wakelock:" + wakelock.toString(), e);
        }
    }
}
