package com.samsung.context.sdk.samsunganalytics.p000a.p005e;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0315d;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.e.e */
public class C0294e {
    public static final int f83a = 0;
    public static final int f84b = -1001;
    public static String f85c;
    private static HashMap<String, Integer> f86d;

    static {
        f85c = "RSSAV1wsc2s314SAamk";
    }

    private C0294e() {
    }

    public static int m83a(String str, String str2) {
        if (f86d == null) {
            C0294e.m88b();
        }
        Integer num = (Integer) f86d.get(str);
        if (num == null || num.intValue() >= str2.length()) {
            return f83a;
        }
        C0311a.m149e("Invalid length : " + str2);
        C0311a.m149e("MAX length : " + num);
        return f84b;
    }

    public static String m84a(String str) {
        Exception e;
        if (str == null) {
            return null;
        }
        String format;
        try {
            MessageDigest.getInstance("SHA-256").update(str.getBytes("UTF-8"));
            format = String.format(Locale.US, "%064x", new Object[]{new BigInteger(1, r0.digest())});
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            C0311a.m142a(C0294e.class, e);
            format = null;
            return format;
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            C0311a.m142a(C0294e.class, e);
            format = null;
            return format;
        }
        return format;
    }

    public static boolean m85a() {
        String str;
        String str2;
        if (VERSION.SDK_INT > 23) {
            str = "com.samsung.android.feature.SemFloatingFeature";
            str2 = "getBoolean";
        } else {
            str = "com.samsung.android.feature.FloatingFeature";
            str2 = "getEnableStatus";
        }
        try {
            Class cls = Class.forName(str);
            Object invoke = cls.getMethod("getInstance", null).invoke(null, new Object[f83a]);
            boolean booleanValue = ((Boolean) cls.getMethod(str2, new Class[]{String.class}).invoke(invoke, new Object[]{"SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE"})).booleanValue();
            if (booleanValue) {
                C0311a.m148d("cf feature is supported");
                return booleanValue;
            }
            C0311a.m148d("feature is not supported");
            return booleanValue;
        } catch (Exception e) {
            C0311a.m148d("Floating feature is not supported (non-samsung device)");
            C0311a.m142a(C0294e.class, e);
            return false;
        }
    }

    public static boolean m86a(Context context, Configuration configuration) {
        if (context == null) {
            C0315d.m156a("context cannot be null");
            return false;
        } else if (configuration == null) {
            C0315d.m156a("Configuration cannot be null");
            return false;
        } else if (!TextUtils.isEmpty(configuration.getDeviceId()) || configuration.isEnableAutoDeviceId()) {
            if (configuration.isEnableUseInAppLogging()) {
                if (configuration.getUserAgreement() == null) {
                    C0315d.m156a("If you want to use In App Logging, you should implement UserAgreement interface");
                    return false;
                }
            } else if (!C0294e.m87a(context, "com.sec.spp.permission.TOKEN", false)) {
                C0315d.m156a("If you want to use DLC Logger, define 'com.sec.spp.permission.TOKEN_XXXX' permission in AndroidManifest");
                return false;
            } else if (!TextUtils.isEmpty(configuration.getDeviceId())) {
                C0315d.m156a("This mode is not allowed to set device Id");
                return false;
            } else if (!TextUtils.isEmpty(configuration.getUserId())) {
                C0315d.m156a("This mode is not allowed to set user Id");
                return false;
            }
            if (configuration.getVersion() != null) {
                return true;
            }
            C0315d.m156a("you should set the version");
            return false;
        } else {
            C0315d.m156a("Device Id is empty, set Device Id or enable auto device id");
            return false;
        }
    }

    public static boolean m87a(Context context, String str, boolean z) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), Constants.DOWNLOAD_BUFFER_SIZE);
            if (packageInfo.requestedPermissions != null) {
                String[] strArr = packageInfo.requestedPermissions;
                int length = strArr.length;
                for (int i = f83a; i < length; i++) {
                    String str2 = strArr[i];
                    if (z) {
                        if (str2.equalsIgnoreCase(str)) {
                            return true;
                        }
                    } else if (str2.startsWith(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            C0311a.m142a(C0294e.class, e);
        }
        return false;
    }

    private static void m88b() {
        f86d = new HashMap();
        f86d.put("pn", Integer.valueOf(100));
        f86d.put("pnd", Integer.valueOf(Constants.HTTP_ERROR_BAD_REQUEST));
        f86d.put("en", Integer.valueOf(100));
        f86d.put("ed", Integer.valueOf(Constants.HTTP_ERROR_BAD_REQUEST));
        f86d.put("exm", Integer.valueOf(Constants.HTTP_ERROR_BAD_REQUEST));
        f86d.put("exd", Integer.valueOf(CredentialsApi.ACTIVITY_RESULT_ADD_ACCOUNT));
        f86d.put("sti", Integer.valueOf(CredentialsApi.ACTIVITY_RESULT_ADD_ACCOUNT));
        f86d.put("cd", Integer.valueOf(CredentialsApi.ACTIVITY_RESULT_ADD_ACCOUNT));
        f86d.put("cm", Integer.valueOf(CredentialsApi.ACTIVITY_RESULT_ADD_ACCOUNT));
    }
}
