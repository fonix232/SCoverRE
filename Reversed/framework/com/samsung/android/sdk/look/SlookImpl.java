package com.samsung.android.sdk.look;

import android.app.ActivityThread;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.PackageItemInfo;
import android.content.pm.ResolveInfo;
import android.os.BaseBundle;
import android.os.Build.VERSION;
import android.os.SystemProperties;
import android.os.UserHandle;
import java.util.List;

public class SlookImpl {
    private static final int AIRBUTTON = 1;
    private static final int COCKTAIL_BAR = 6;
    private static final int COCKTAIL_PANEL = 7;
    public static final boolean DEBUG = true;
    private static final int SDK_INT = SystemProperties.getInt("ro.slook.ver", 0);
    private static final int SMARTCLIP = 2;
    private static final int SPEN_HOVER_ICON = 4;
    private static final int WRITINGBUDDY = 3;
    private static int sCocktailLevel = -1;
    private static int sHasMetaEdgeSingle = -1;
    private static int sUspLevel = -1;

    public static class VERSION_CODES {
        public static final int L1 = 1;
        public static final int L2 = 2;
    }

    private static void checkCocktailLevel() {
        int i = 0;
        if (sCocktailLevel == -1) {
            IPackageManager packageManager = ActivityThread.getPackageManager();
            if (packageManager != null) {
                try {
                    sCocktailLevel = packageManager.hasSystemFeature("com.sec.feature.cocktailbar", 0) ? 6 : 0;
                    if (sCocktailLevel == 0) {
                        if (packageManager.hasSystemFeature("com.sec.feature.cocktailpanel", 0)) {
                            i = 7;
                        }
                        sCocktailLevel = i;
                    }
                } catch (Throwable e) {
                    throw new RuntimeException("Package manager has died", e);
                }
            }
        }
    }

    private static void checkValidCocktailMetaData() {
        if (sHasMetaEdgeSingle == -1) {
            sHasMetaEdgeSingle = 0;
            IPackageManager packageManager = ActivityThread.getPackageManager();
            String currentOpPackageName = ActivityThread.currentOpPackageName();
            if (packageManager != null && currentOpPackageName != null) {
                try {
                    PackageItemInfo applicationInfo = packageManager.getApplicationInfo(currentOpPackageName, 128, UserHandle.myUserId());
                    if (applicationInfo != null) {
                        String string;
                        BaseBundle baseBundle = applicationInfo.metaData;
                        if (baseBundle != null) {
                            string = baseBundle.getString("com.samsung.android.cocktail.mode", "");
                            if (string != null && string.equals("edge_single")) {
                                sHasMetaEdgeSingle = 1;
                            }
                        }
                        if (sHasMetaEdgeSingle == 0) {
                            Intent intent = new Intent("com.samsung.android.cocktail.action.COCKTAIL_UPDATE");
                            intent.setPackage(currentOpPackageName);
                            intent.resolveTypeIfNeeded(ActivityThread.currentApplication().getContentResolver());
                            List list = packageManager.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(ActivityThread.currentApplication().getContentResolver()), 128, UserHandle.myUserId()).getList();
                            int size = list == null ? 0 : list.size();
                            for (int i = 0; i < size; i++) {
                                PackageItemInfo packageItemInfo = ((ResolveInfo) list.get(i)).activityInfo;
                                if ((packageItemInfo.applicationInfo.flags & 262144) == 0 && currentOpPackageName.equals(packageItemInfo.packageName)) {
                                    baseBundle = packageItemInfo.metaData;
                                    if (baseBundle != null) {
                                        string = baseBundle.getString("com.samsung.android.cocktail.mode", "");
                                        if (string != null && string.equals("edge_single")) {
                                            sHasMetaEdgeSingle = 1;
                                            break;
                                        }
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getVersionCode() {
        return SDK_INT;
    }

    public static boolean isFeatureEnabled(int i) {
        boolean z = true;
        boolean z2 = false;
        switch (i) {
            case 1:
            case 3:
                break;
            case 2:
            case 4:
                if (VERSION.SDK_INT >= 24) {
                    return false;
                }
                break;
            case 6:
                checkCocktailLevel();
                if (sCocktailLevel > 0 && sCocktailLevel <= i) {
                    return true;
                }
                if (sCocktailLevel <= 0) {
                    return false;
                }
                checkValidCocktailMetaData();
                if (sHasMetaEdgeSingle != 1) {
                    z = false;
                }
                return z;
            case 7:
                checkCocktailLevel();
                if (sCocktailLevel > 0 && sCocktailLevel <= i) {
                    z2 = true;
                }
                return z2;
            default:
                return false;
        }
        if (sUspLevel == -1 && ActivityThread.getPackageManager() == null) {
            if (i == 1) {
                z = false;
                return z;
            } else if (i != 3) {
                if (sUspLevel < 2) {
                    z = false;
                }
                return z;
            } else {
                z2 = true;
                return z2;
            }
        } else if (i == 1) {
            if (sUspLevel < 2 || sUspLevel > 3) {
                z = false;
            }
            return z;
        } else if (i != 3) {
            if (sUspLevel >= 2 && sUspLevel <= 9) {
                z2 = true;
            }
            return z2;
        } else {
            if (sUspLevel < 2) {
                z = false;
            }
            return z;
        }
    }
}
