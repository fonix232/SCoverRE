package com.samsung.android.mateservice.common;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Process;
import android.os.UserHandle;
import com.samsung.android.mateservice.util.UtilLog;
import com.samsung.android.smartface.SmartFaceManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FwDependency {
    private static final boolean PRODUCT_DEV = internalIsProductDev();
    private static final String SYSTEM_PROPERTIES_CLASS_NAME = "android.os.SystemProperties";
    private static final String TAG = "Fw";

    public static boolean bindServiceAsUser(Context context, Intent intent, ServiceConnection serviceConnection, int i, UserHandle userHandle) {
        Method method = null;
        try {
            method = Context.class.getMethod("bindServiceAsUser", new Class[]{Intent.class, ServiceConnection.class, Integer.TYPE, UserHandle.class});
            if (method != null) {
                return ((Boolean) method.invoke(context, new Object[]{intent, serviceConnection, Integer.valueOf(i), userHandle})).booleanValue();
            }
        } catch (Throwable e) {
            if (UtilLog.isDebugLogLevel()) {
                e.printStackTrace();
            }
        }
        if (method == null) {
            UtilLog.m10w(TAG, "bindServiceAsUser fail to invoke method", new Object[0]);
        }
        return false;
    }

    public static int getProcessSystemUid() {
        int i = -1;
        try {
            Field field = Process.class.getField("SYSTEM_UID");
            if (field != null) {
                i = field.getInt(null);
            }
        } catch (Throwable th) {
            if (UtilLog.isDebugLogLevel()) {
                th.printStackTrace();
            }
        }
        return i;
    }

    static String getSystemProperty(String str) {
        String str2 = "";
        try {
            return (String) Class.forName(SYSTEM_PROPERTIES_CLASS_NAME).getDeclaredMethod("get", new Class[]{String.class}).invoke(null, new Object[]{str});
        } catch (Throwable th) {
            if (!UtilLog.isDebugLogLevel()) {
                return str2;
            }
            th.printStackTrace();
            return str2;
        }
    }

    public static String getSystemProperty(String str, String str2) {
        String str3 = str2;
        try {
            return (String) Class.forName(SYSTEM_PROPERTIES_CLASS_NAME).getDeclaredMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{str, str2});
        } catch (Throwable th) {
            if (!UtilLog.isDebugLogLevel()) {
                return str3;
            }
            th.printStackTrace();
            return str3;
        }
    }

    public static UserHandle getUserHandle(String str) {
        UserHandle userHandle = null;
        try {
            Field field = UserHandle.class.getField(str);
            if (field != null) {
                userHandle = (UserHandle) field.get(null);
            }
        } catch (Throwable th) {
            if (UtilLog.isDebugLogLevel()) {
                th.printStackTrace();
            }
        }
        if (userHandle == null) {
            UtilLog.m10w(TAG, "getUserHandle return null", new Object[0]);
        }
        return userHandle;
    }

    private static boolean internalIsProductDev() {
        boolean z = false;
        Class cls = Debug.class;
        try {
            Method method;
            if (VERSION.SDK_INT >= 24) {
                method = cls.getMethod("semIsProductDev", new Class[0]);
                if (method != null) {
                    return ((Boolean) method.invoke(null, new Object[0])).booleanValue();
                }
            } else if (VERSION.SDK_INT <= 23) {
                method = cls.getMethod("isProductShip", new Class[0]);
                if (method != null) {
                    return ((Integer) method.invoke(null, new Object[0])).intValue() != 1;
                }
            }
        } catch (Throwable e) {
            if (UtilLog.isDebugLogLevel()) {
                e.printStackTrace();
            }
        }
        if (!SmartFaceManager.TRUE.equals(getSystemProperty("ro.product_ship", SmartFaceManager.FALSE))) {
            z = true;
        }
        return z;
    }

    public static boolean isProductDev() {
        return PRODUCT_DEV;
    }
}
