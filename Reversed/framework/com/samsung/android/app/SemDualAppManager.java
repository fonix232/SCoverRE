package com.samsung.android.app;

import android.content.Context;
import android.os.Bundle;
import android.util.secutil.Log;
import java.util.List;

public class SemDualAppManager {
    private static final String BLACKBERRYMESSENGER_PACKAGE_NAME = "com.bbm";
    static final String[] DUAL_APP_WHITELIST_PACKAGES = new String[]{FACEBOOK_PACKAGE_NAME, WHATSAPP_PACKAGE_NAME, FACEBOOKMESSENGER_PACKAGE_NAME, QQMOBILECHINA_PACKAGE_NAME, QQMOBILEINTERNATIONAL_PACKAGE_NAME, WECHAT_PACKAGE_NAME, SKYPE_PACKAGE_NAME, VIBER_PACKAGE_NAME, LINE_PACKAGE_NAME, BLACKBERRYMESSENGER_PACKAGE_NAME, TELEGRAM_PACKAGE_NAME, KAKAOTALK_PACKAGE_NAME, HIKE_PACKAGE_NAME, ICQ_PACKAGE_NAME, YAHOOMESSENGER_PACKAGE_NAME, ZALO_PACKAGE_NAME};
    public static final String DUAL_ORI_SHORTCUT_COMPONENT = "dual_shortcut_component";
    private static final String FACEBOOKMESSENGER_PACKAGE_NAME = "com.facebook.orca";
    private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    private static final String HIKE_PACKAGE_NAME = "com.bsb.hike";
    private static final String ICQ_PACKAGE_NAME = "com.icq.mobile.client";
    private static final String KAKAOTALK_PACKAGE_NAME = "com.kakao.talk";
    private static final String LINE_PACKAGE_NAME = "jp.naver.line.android";
    public static final int MAX_DUALAPP_ID = 99;
    public static final int MIN_DUALAPP_ID = 95;
    private static final String QQMOBILECHINA_PACKAGE_NAME = "com.tencent.mobileqq";
    private static final String QQMOBILEINTERNATIONAL_PACKAGE_NAME = "com.tencent.mobileqqi";
    private static final String SKYPE_PACKAGE_NAME = "com.skype.raider";
    private static final String TAG = "SemDualAppManager";
    private static final String TELEGRAM_PACKAGE_NAME = "org.telegram.messenger";
    private static final String VIBER_PACKAGE_NAME = "com.viber.voip";
    private static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String YAHOOMESSENGER_PACKAGE_NAME = "com.yahoo.mobile.client.android.im";
    private static final String ZALO_PACKAGE_NAME = "com.zing.zalo";
    private static ISemDualAppManager mService = null;
    private static SemDualAppManager sDAInstance = null;
    private Context mContext;

    private SemDualAppManager() {
    }

    public static List<String> getAllInstalledWhitelistedPackages() {
        Log.m51d(TAG, "This device doesn't support DA. getAllInstalledWhitelistedPackages returns null");
        return null;
    }

    public static String[] getAllWhitelistedPackages() {
        Log.m51d(TAG, "This device doesn't support DA. getAllWhitelistedPackages Returns null");
        return null;
    }

    public static int getDualAppProfileId() {
        Log.m51d(TAG, "This device doesn't support DA. getDualAppProfileId Returns UserHandle.USER_NULL");
        return -10000;
    }

    private static ISemDualAppManager getDualAppService() {
        Log.m51d(TAG, "This device doesn't support DA. getDualAppService Returns null");
        return null;
    }

    public static SemDualAppManager getInstance(Context context) {
        if (sDAInstance == null) {
            synchronized (SemDualAppManager.class) {
                if (sDAInstance == null) {
                    sDAInstance = new SemDualAppManager();
                    sDAInstance.mContext = context;
                }
            }
        }
        return sDAInstance;
    }

    public static boolean isDualAppId(int i) {
        return false;
    }

    private static boolean isDualAppIdInternal(int i) {
        return i >= 95 && i <= 99;
    }

    public static boolean isInstalledWhitelistedPackage(String str) {
        Log.m51d(TAG, "isInstalledWhitelistedPackage. package Name = [" + str + "]");
        Log.m51d(TAG, "This device doesn't support DA. isInstalledWhitelistedPackage returns false");
        return false;
    }

    public static Bundle updateDualAppData(Context context, int i, Bundle bundle) {
        Log.m51d(TAG, "This device doesn't support DA. updateDualAppData returns null");
        return null;
    }

    public boolean isSupported() {
        Log.m51d(TAG, "This device doesn't support DA. Return false");
        return false;
    }

    public boolean isWhitelistedPackage(String str) {
        Log.m51d(TAG, "This device doesn't support DA. isWhitelistedPackage returns false");
        return false;
    }
}
