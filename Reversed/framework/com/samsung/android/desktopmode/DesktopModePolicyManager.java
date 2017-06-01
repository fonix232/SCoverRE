package com.samsung.android.desktopmode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteClosable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import com.samsung.android.game.IGameManagerService;
import com.samsung.android.game.IGameManagerService.Stub;
import com.samsung.android.share.SShareConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class DesktopModePolicyManager {
    public static final String ACTION_UPDATE_DATABASE = "com.samsung.android.desktopmode.action.UPDATE_DATABASE";
    private static final boolean DEBUG = DesktopModeFeature.DEBUG;
    private static final String DEX_SUPPORTED = "com.samsung.android.dex.launchpolicy.supported";
    private static final String KEEPALIVE_DENSITY = "com.samsung.android.keepalive.density";
    public static final int LAUNCH_POLICY_COMPATIBILITY = 2;
    public static final int LAUNCH_POLICY_FREEFORM = 16;
    public static final int LAUNCH_POLICY_GAME = 32768;
    public static final int LAUNCH_POLICY_NOT_SUPPORTED_DECLARED_CATEGORY_HOME = 128;
    public static final int LAUNCH_POLICY_NOT_SUPPORTED_DECLARED_TOUCHSCREEN = 64;
    public static final int LAUNCH_POLICY_NOT_SUPPORTED_LISTED = 32;
    public static final int LAUNCH_POLICY_UNDEFINED = 1;
    public static final int RESIZE_MODE_CROP_WINDOWS = 1;
    public static final int RESIZE_MODE_FORCE_RESIZEABLE = 4;
    public static final int RESIZE_MODE_RESIZEABLE = 2;
    public static final int RESIZE_MODE_RESIZEABLE_AND_PIPABLE = 3;
    public static final int RESIZE_MODE_UNRESIZEABLE = 0;
    public static final String TAG = DesktopModePolicyManager.class.getSimpleName();
    private static HashSet<String> hKeepPolicyPackages = new HashSet(Arrays.asList(new String[]{"com.google.android.apps.tachyon", "com.google.android.music", "com.google.android.videos", "com.google.android.apps.photos", "com.google.android.apps.docs", "com.google.android.apps.docs.editors.docs", "com.google.android.apps.docs.editors.sheets", "com.google.android.apps.docs.editors.slides"}));
    private static HashSet<String> hKillPolicyPackages = new HashSet(Arrays.asList(new String[]{"com.sec.android.app.sbrowser", "com.sec.android.inputmethod", "com.sec.android.inputmethod.iwnnime.japan", "com.samsung.android.app.spage", "com.microsoft.office.word", "com.microsoft.office.excel", "com.microsoft.office.powerpoint", "com.facebook.katana"}));
    private static HashMap<String, Integer> mDeXLaunchPolicy = null;
    private static String[] sCategoryHomeExceptPackages = new String[]{SShareConstants.SCREEN_MIRRORING_PKG, "com.sec.android.app.desktoplauncher", "com.google.android.googlequicksearchbox", "com.asurion.android.mobilerecovery.att"};
    private static String[] sNotSupportedListPackages = new String[0];
    private static String[] sTouchScreenExceptPackages = new String[]{"com.microsoft.office.powerpoint", "com.google.android.music", "com.google.android.videos"};

    static class DeXPolicyDatabaseHelper extends SQLiteOpenHelper {
        private static final String POLICY_UPDATE_DB = "/data/user/0/com.sec.android.app.desktoplauncher/databases/DexPkgList.db";
        private static final int POLICY_UPDATE_DB_VERSION = 1;
        private static DeXPolicyDatabaseHelper mDeXPolicyDatabaseHelper = null;
        private final String MODE_COLUMN = "mode";
        protected final int MODE_NONE = 0;
        private final String PKG_COLUMN = "packagename";
        private final String POLICY_LAUNCH_PACKAGE_TABLE = "LaunchPackageList";

        private DeXPolicyDatabaseHelper(Context context) {
            super(context, POLICY_UPDATE_DB, null, 1);
        }

        private static boolean checkDataBase() {
            if (new File(POLICY_UPDATE_DB).exists()) {
                return true;
            }
            Log.d(DesktopModePolicyManager.TAG, "checkDataBase. There's no DB exist. or can not access.");
            return false;
        }

        public static DeXPolicyDatabaseHelper getInstance(Context context) {
            if (mDeXPolicyDatabaseHelper == null) {
                mDeXPolicyDatabaseHelper = new DeXPolicyDatabaseHelper(context);
            }
            return mDeXPolicyDatabaseHelper;
        }

        protected HashMap<String, Integer> getAllLaunchPolicy() {
            HashMap<String, Integer> hashMap = new HashMap();
            if (!checkDataBase()) {
                return hashMap;
            }
            Cursor cursor = null;
            SQLiteClosable sQLiteClosable = null;
            try {
                sQLiteClosable = getReadableDatabase();
                if (sQLiteClosable != null) {
                    cursor = sQLiteClosable.query("LaunchPackageList", new String[]{"packagename", "mode"}, null, null, null, null, null);
                    if (!(cursor == null || cursor.getCount() == 0)) {
                        cursor.moveToFirst();
                        for (int i = 0; i < cursor.getCount(); i++) {
                            hashMap.put(cursor.getString(0), Integer.valueOf(cursor.getInt(1)));
                            cursor.moveToNext();
                        }
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteClosable != null) {
                    sQLiteClosable.close();
                }
            } catch (Throwable e) {
                if (DesktopModePolicyManager.DEBUG) {
                    e.printStackTrace();
                }
                if (hashMap != null) {
                    hashMap.clear();
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteClosable != null) {
                    sQLiteClosable.close();
                }
            } catch (Throwable e2) {
                if (DesktopModePolicyManager.DEBUG) {
                    e2.printStackTrace();
                }
                if (hashMap != null) {
                    hashMap.clear();
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteClosable != null) {
                    sQLiteClosable.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                if (sQLiteClosable != null) {
                    sQLiteClosable.close();
                }
            }
            return hashMap;
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            super.onOpen(sQLiteDatabase);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    public static void closeLaunchPolicyDB() {
        if (mDeXLaunchPolicy != null) {
            mDeXLaunchPolicy.clear();
        }
        mDeXLaunchPolicy = null;
    }

    public static int createLaunchModePolicyCacheFromDB(Context context) {
        if (mDeXLaunchPolicy == null) {
            DeXPolicyDatabaseHelper instance = DeXPolicyDatabaseHelper.getInstance(context);
            if (instance == null) {
                return 0;
            }
            mDeXLaunchPolicy = instance.getAllLaunchPolicy();
            Log.d(TAG, "createLaunchModePolicyCacheFromDB ( " + mDeXLaunchPolicy.size() + ")");
            return mDeXLaunchPolicy.size();
        }
        Log.d(TAG, "createLaunchModePolicyCacheFromDB ( " + mDeXLaunchPolicy.size() + ") already cache is exist");
        return mDeXLaunchPolicy.size();
    }

    public static Bundle getDesktopModeKillPolicy() {
        BaseBundle bundle = new Bundle();
        bundle.putString("name", "DeX");
        bundle.putSerializable("kill_packages", hKillPolicyPackages);
        bundle.putSerializable("keep_packages", hKeepPolicyPackages);
        bundle.putBoolean("skip_sdk_version_check", true);
        if (DEBUG) {
            Log.d(TAG, "getDesktopModeKillPolicy.., args [" + bundle + "]");
        }
        return bundle;
    }

    public static int getLaunchModePolicyFromCache(Context context, String str) {
        int i = 2;
        if (mDeXLaunchPolicy == null) {
            DeXPolicyDatabaseHelper instance = DeXPolicyDatabaseHelper.getInstance(context);
            if (instance != null) {
                mDeXLaunchPolicy = instance.getAllLaunchPolicy();
                if (DEBUG) {
                    Slog.d(TAG, "getLaunchModePolicyFromCache ( " + mDeXLaunchPolicy.size() + ")");
                }
            }
        }
        if (mDeXLaunchPolicy != null) {
            Integer num = (Integer) mDeXLaunchPolicy.get(str);
            if (num != null) {
                if (num.intValue() == 1) {
                    i = 16;
                } else if (num.intValue() == 2) {
                    i = 32;
                }
            }
            if (DEBUG) {
                Slog.d(TAG, "getLaunchModePolicyFromCache ( " + i + ")");
            }
        }
        return i;
    }

    public static Bundle getLaunchModePolicyList(Context context) {
        BaseBundle bundle = new Bundle();
        DeXPolicyDatabaseHelper instance = DeXPolicyDatabaseHelper.getInstance(context);
        if (instance != null) {
            HashMap allLaunchPolicy = instance.getAllLaunchPolicy();
            if (allLaunchPolicy != null) {
                if (DEBUG) {
                    Slog.d(TAG, "getLaunchModePolicy ( " + allLaunchPolicy.size() + ")");
                }
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (String str : allLaunchPolicy.keySet()) {
                    if (((Integer) allLaunchPolicy.get(str)).intValue() == 1) {
                        arrayList.add(str);
                    } else {
                        arrayList2.add(str);
                    }
                }
                String[] strArr = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
                bundle.putStringArray("white_list", (String[]) arrayList.toArray(new String[arrayList.size()]));
                bundle.putStringArray("black_list", strArr);
                arrayList.clear();
                arrayList2.clear();
                allLaunchPolicy.clear();
            }
        }
        return bundle;
    }

    public static int getResizeableMode(ApplicationInfo applicationInfo, ActivityInfo activityInfo) {
        if (DEBUG) {
            Slog.d(TAG, "getResizeableMode().start");
        }
        if (DEBUG) {
            Slog.d(TAG, "getResizeableMode(). App SDK Ver :" + applicationInfo.targetSdkVersion + " appInfo.privateFlags : " + applicationInfo.privateFlags);
        }
        int i;
        if (applicationInfo.targetSdkVersion >= 24) {
            if (activityInfo != null) {
                i = activityInfo.resizeMode;
                if (DEBUG) {
                    Slog.d(TAG, "getResizeableMode(). N & over, resizeMode :" + i);
                }
                if ((i == 2 || i == 3 || i == 4) && (isResizeableExplicitlyActivity(activityInfo) || isResizeableExplicitlyApplication(applicationInfo))) {
                    return 16;
                }
                if (i == 0) {
                    return 2;
                }
            } else if ((applicationInfo.privateFlags & 2048) == 0) {
                return 2;
            } else {
                if (isResizeableExplicitlyApplication(applicationInfo)) {
                    return 16;
                }
            }
        } else if (activityInfo != null) {
            i = activityInfo.resizeMode;
            if (DEBUG) {
                Slog.d(TAG, "getResizeableMode(). under N, resizeMode :" + i);
            }
            if (i == 0) {
                return 2;
            }
        }
        return -1;
    }

    public static boolean isCategoryHomeDeclared(Context context, String str, int i) {
        boolean z = false;
        if (isExceptList(context, sCategoryHomeExceptPackages, str)) {
            if (DEBUG) {
                Slog.d(TAG, "isCategoryHomeDeclared ( " + false + " , " + str + ")");
            }
            return false;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.setPackage(str);
        if (context.getPackageManager().resolveActivityAsUser(intent, 65536, i) != null) {
            z = true;
        }
        if (DEBUG) {
            Slog.d(TAG, "isCategoryHomeDeclared ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isDexSupported(Context context, String str, ApplicationInfo applicationInfo) {
        boolean z = false;
        BaseBundle baseBundle = null;
        if (applicationInfo != null) {
            baseBundle = applicationInfo.metaData;
        } else if (DEBUG) {
            Slog.d(TAG, "isDexSupported, ApplicationInfo is null.");
        }
        if (baseBundle == null && str != null) {
            try {
                PackageItemInfo applicationInfo2 = context.getPackageManager().getApplicationInfo(str, 128);
                if (applicationInfo2 != null) {
                    baseBundle = applicationInfo2.metaData;
                    if (DEBUG) {
                        Slog.d(TAG, "isDexSupported, tmpAppInfo.metaData=" + applicationInfo2.metaData);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (baseBundle != null) {
            z = baseBundle.getBoolean(DEX_SUPPORTED);
        } else if (DEBUG) {
            Slog.d(TAG, "isDexSupported, ApplicationInfo.metaData is null.");
        }
        if (DEBUG) {
            Slog.d(TAG, "isDexSupported ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isExceptList(Context context, String[] strArr, String str) {
        boolean z = false;
        for (String equalsIgnoreCase : strArr) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                z = true;
                break;
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "isExceptList ( " + str + ", " + z + " )");
        }
        return z;
    }

    public static boolean isGame(String str) {
        boolean z = true;
        IGameManagerService iGameManagerService = null;
        try {
            IBinder service = ServiceManager.getService("gamemanager");
            if (service != null) {
                iGameManagerService = Stub.asInterface(service);
            }
            if (iGameManagerService != null) {
                int identifyGamePackage = iGameManagerService.identifyGamePackage(str);
                if (DEBUG) {
                    Slog.d(TAG, "isGame ( " + identifyGamePackage + " , " + str + ")");
                }
                if (identifyGamePackage != 1) {
                    z = false;
                }
                return z;
            }
        } catch (Throwable e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "isGame ( false , " + str + ")");
        }
        return false;
    }

    public static boolean isKeepAlive(Context context, String str) {
        boolean z = false;
        BaseBundle baseBundle = null;
        try {
            PackageItemInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            if (applicationInfo != null) {
                baseBundle = applicationInfo.metaData;
            } else if (DEBUG) {
                Slog.d(TAG, "isKeepAlive, ApplicationInfo is null.");
            }
            if (baseBundle != null) {
                z = baseBundle.getBoolean(KEEPALIVE_DENSITY);
            } else if (DEBUG) {
                Slog.d(TAG, "isKeepAlive, ApplicationInfo.metaData is null.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (DEBUG) {
            Slog.d(TAG, "isKeepAlive ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isKeepWhiteList(Context context, String str) {
        boolean z = false;
        if (hKeepPolicyPackages.contains(str)) {
            z = true;
        }
        if (DEBUG) {
            Slog.d(TAG, "isKeepWhiteList ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isLaunchModePolicyAvailable() {
        return DeXPolicyDatabaseHelper.checkDataBase();
    }

    public static boolean isNotSupportedListed(Context context, String str) {
        boolean z = false;
        if (sNotSupportedListPackages == null || sNotSupportedListPackages.length == 0) {
            if (DEBUG) {
                Slog.d(TAG, "no sNotSupportedListPackages");
            }
            return false;
        }
        for (String equalsIgnoreCase : sNotSupportedListPackages) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                z = true;
                break;
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "isNotSupportedListed ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isPreloadedAppResizeable(Context context, String str, ApplicationInfo applicationInfo) {
        boolean z = false;
        if (str.startsWith("com.samsung") || str.startsWith("com.sec") || str.startsWith("com.android") || str.startsWith("com.google") || str.startsWith(SShareConstants.RESOLVER_GUIDE_ACTIVITY_PKG)) {
            if (!(applicationInfo == null || (applicationInfo.flags & 1) == 0)) {
                if (DEBUG) {
                    Slog.d(TAG, "isPreloadedAppResizeable, preload app.");
                }
                if (applicationInfo.targetSdkVersion >= 24) {
                    z = true;
                }
            }
        } else if (DEBUG) {
            Slog.d(TAG, "isPreloadedAppResizeable, NOT samsung app " + str);
        }
        if (DEBUG) {
            Slog.d(TAG, "isPreloadedAppResizeable ( " + z + " , " + str + ")");
        }
        return z;
    }

    public static boolean isResizeableExplicitlyActivity(ActivityInfo activityInfo) {
        boolean z = false;
        try {
            BaseBundle baseBundle = activityInfo.metaData;
            if (baseBundle != null && baseBundle.getBoolean("resizeableSetExplicitly")) {
                z = true;
            }
        } catch (Throwable e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "isResizeableExplicitlyActivity ( " + z + " )");
        }
        return z;
    }

    public static boolean isResizeableExplicitlyApplication(ApplicationInfo applicationInfo) {
        boolean z = false;
        try {
            BaseBundle baseBundle = applicationInfo.metaData;
            if (baseBundle != null && baseBundle.getBoolean("resizeableSetExplicitly")) {
                z = true;
            }
        } catch (Throwable e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "isResizeableExplicitlyApplication ( " + z + ")");
        }
        return z;
    }

    public static boolean isTouchScreenDeclared(Context context, String str, int i) {
        boolean z = false;
        Object obj = null;
        Object obj2 = null;
        if (isExceptList(context, sTouchScreenExceptPackages, str)) {
            if (DEBUG) {
                Slog.d(TAG, "isTouchScreenDeclared ( " + false + " , " + str + ")");
            }
            return false;
        }
        try {
            PackageInfo packageInfoAsUser = context.getPackageManager().getPackageInfoAsUser(str, 16384, i);
            if (packageInfoAsUser != null) {
                FeatureInfo[] featureInfoArr = packageInfoAsUser.reqFeatures;
                if (featureInfoArr != null && featureInfoArr.length > 0) {
                    for (FeatureInfo featureInfo : featureInfoArr) {
                        if (!(featureInfo == null || featureInfo.name == null || !featureInfo.name.equalsIgnoreCase("android.hardware.touchscreen"))) {
                            if (featureInfo.flags == 1) {
                                obj = 1;
                                if (DEBUG) {
                                    Slog.d(TAG, "isTouchScreenDeclared, FEATURE_TOUCHSCREEN is true.");
                                }
                            } else {
                                obj = null;
                                if (DEBUG) {
                                    Slog.d(TAG, "isTouchScreenDeclared, FEATURE_TOUCHSCREEN is false.");
                                }
                            }
                        }
                        if (!(featureInfo == null || featureInfo.name == null || !featureInfo.name.equalsIgnoreCase("android.hardware.touchscreen.multitouch"))) {
                            if (featureInfo.flags == 1) {
                                obj2 = 1;
                                if (DEBUG) {
                                    Slog.d(TAG, "isTouchScreenDeclared, FEATURE_TOUCHSCREEN_MULTITOUCH is true.");
                                }
                            } else {
                                obj2 = null;
                                if (DEBUG) {
                                    Slog.d(TAG, "isTouchScreenDeclared, FEATURE_TOUCHSCREEN_MULTITOUCH is false.");
                                }
                            }
                        }
                    }
                    if (!(obj == null && r1 == null)) {
                        z = true;
                        if (DEBUG) {
                            Slog.d(TAG, "isTouchScreenDeclared, ( bTouchScreen || bTouchScreenMultitouch ) = true. ");
                        }
                    }
                } else if (DEBUG) {
                    Slog.d(TAG, "isTouchScreenDeclared, FeatureInfo is null.");
                }
            } else if (DEBUG) {
                Slog.d(TAG, "isTouchScreenDeclared, PackageInfo is null.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (DEBUG) {
            Slog.d(TAG, "isTouchScreenDeclared ( " + z + " , " + str + ")");
        }
        return z;
    }
}
