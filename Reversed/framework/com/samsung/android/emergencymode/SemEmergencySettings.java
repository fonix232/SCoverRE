package com.samsung.android.emergencymode;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;

public class SemEmergencySettings {
    private static final String TAG = "SemEmergencySettings";

    private SemEmergencySettings() {
    }

    private static String get(ContentResolver contentResolver, String str) {
        if (contentResolver == null || str == null) {
            return null;
        }
        String str2 = null;
        Cursor cursor = null;
        try {
            String str3 = "pref='" + str + "'";
            cursor = contentResolver.query(SemEmergencyConstants.URI_PREFSETTINGS, null, str3, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                str2 = cursor.getString(cursor.getColumnIndex(SemEmergencyConstants.VALUE));
                cursor.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Elog.m3d(TAG, "Exception " + e);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return str2;
    }

    public static boolean getBoolean(ContentResolver contentResolver, String str, boolean z) {
        String str2 = get(contentResolver, str);
        return str2 == null ? z : Boolean.parseBoolean(str2);
    }

    public static double getDouble(ContentResolver contentResolver, String str, double d) {
        try {
            return Double.parseDouble(get(contentResolver, str));
        } catch (Exception e) {
            Elog.m3d(TAG, "Exception " + e);
            return d;
        }
    }

    @Deprecated
    public static String getEmergencyNumber(ContentResolver contentResolver, String str) {
        return getEmergencyNumber(ActivityThread.currentApplication().getApplicationContext(), contentResolver, str);
    }

    public static String getEmergencyNumber(Context context, ContentResolver contentResolver, String str) {
        if (contentResolver == null || str == null || context == null) {
            return null;
        }
        String str2 = null;
        Cursor cursor = null;
        boolean z = false;
        int i = 1;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String str3 = null;
        int i2 = 0;
        while (i2 < telephonyManager.getSimCount()) {
            try {
                if (!(str3 == null || str3 == "")) {
                    if (i == 1) {
                    }
                    i = telephonyManager.getSimState(i2);
                    if (i == 5) {
                        z = true;
                    }
                    i2++;
                }
                str3 = telephonyManager.getNetworkOperator(getSubId(context, i2));
                i = telephonyManager.getSimState(i2);
                if (i == 5) {
                    z = true;
                }
                i2++;
            } catch (Exception e) {
                Elog.m3d(TAG, "Exception " + e);
                return str2;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                Elog.m3d(TAG, "getEmergencyNumber not found emergency number!");
                str2 = "China".equalsIgnoreCase(SystemProperties.get("ro.csc.country_code")) ? "119" : "911";
            }
        }
        String substring = str3.substring(0, 3);
        Elog.m3d(TAG, "getEmergencyNumber requested Country : " + substring + " sim ready = " + z);
        cursor = contentResolver.query(SemEmergencyConstants.URI_ECCLIST, null, "mcc='" + substring + "'", null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            str2 = makeEmergencyNumber(cursor.getString(cursor.getColumnIndex(str)), z);
        }
        if (cursor != null) {
            cursor.close();
        }
        if (str2 == null) {
            Elog.m3d(TAG, "getEmergencyNumber not found emergency number!");
            str2 = "China".equalsIgnoreCase(SystemProperties.get("ro.csc.country_code")) ? "119" : "911";
        }
        return str2;
    }

    public static int getInt(ContentResolver contentResolver, String str, int i) {
        try {
            return Integer.parseInt(get(contentResolver, str));
        } catch (Exception e) {
            Elog.m3d(TAG, "Exception " + e);
            return i;
        }
    }

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        try {
            return Long.parseLong(get(contentResolver, str));
        } catch (Exception e) {
            Elog.m3d(TAG, "Exception " + e);
            return j;
        }
    }

    public static String getString(ContentResolver contentResolver, String str, String str2) {
        String str3 = get(contentResolver, str);
        return str3 == null ? str2 : str3;
    }

    private static int getSubId(Context context, int i) {
        SubscriptionManager from = SubscriptionManager.from(context);
        if (from != null) {
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = from.getActiveSubscriptionInfoForSimSlotIndex(i);
            if (activeSubscriptionInfoForSimSlotIndex != null) {
                return activeSubscriptionInfoForSimSlotIndex.getSubscriptionId();
            }
        }
        return 0;
    }

    private static boolean isPossibleNormalCall() {
        boolean z = false;
        try {
            ITelephony asInterface = Stub.asInterface(ServiceManager.checkService("phone"));
            if (asInterface != null) {
                int serviceState = asInterface.getServiceState();
                Elog.m3d(TAG, "serviceState : " + serviceState);
                if (serviceState == 0) {
                    z = true;
                }
            }
            return z;
        } catch (RemoteException e) {
            Elog.m3d(TAG, "Failed to clear missed calls notification due to remote exception");
            return false;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    private static String makeEmergencyNumber(String str, boolean z) {
        if (PhoneNumberUtils.isEmergencyNumber(str)) {
            Elog.m3d(TAG, "This is Emergency number");
            return str;
        } else if (z && isPossibleNormalCall()) {
            Elog.m3d(TAG, "SIM Ready, not emergency number.");
            return str;
        } else {
            Elog.m3d(TAG, "SIM Ready = " + z + ", default emergency number.");
            return null;
        }
    }

    public static void put(ContentResolver contentResolver, String str, Object obj) {
        if (contentResolver != null) {
            contentResolver.delete(SemEmergencyConstants.URI_PREFSETTINGS, "pref='" + str + "'", null);
            ContentValues contentValues = new ContentValues();
            contentValues.put(SemEmergencyConstants.PREF, str);
            contentValues.put(SemEmergencyConstants.VALUE, String.valueOf(obj));
            Uri insert = contentResolver.insert(SemEmergencyConstants.URI_PREFSETTINGS, contentValues);
        }
    }
}
