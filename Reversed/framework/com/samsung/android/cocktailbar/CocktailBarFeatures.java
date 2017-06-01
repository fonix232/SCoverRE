package com.samsung.android.cocktailbar;

import android.content.Context;
import android.content.pm.PackageManager;
import java.io.File;
import java.util.ArrayList;

public class CocktailBarFeatures {
    public static final String CATEGORY_NORMAL = "normal";
    public static final boolean COCKTAIL_ENABLED = true;
    private static final int FEATURE_COCKTAIL_BAR = 1;
    private static final int FEATURE_COCKTAIL_PANEL = 2;
    private static final int FEATURE_NONE = 0;
    private static ArrayList<String> mCategoryFilter;
    private static int sCocktailFeature = 0;
    private static boolean sQueriedTypeCocktail = false;

    private static void ensureCocktailFeature(Context context) {
        if (!sQueriedTypeCocktail) {
            sQueriedTypeCocktail = true;
            PackageManager packageManager = null;
            if (context != null) {
                packageManager = context.getPackageManager();
            }
            try {
                sCocktailFeature = verifyCocktailFeature(packageManager, 1, "com.sec.feature.cocktailbar");
                if (sCocktailFeature == 0) {
                    sCocktailFeature = verifyCocktailFeature(packageManager, 2, "com.sec.feature.cocktailpanel");
                }
            } catch (Exception e) {
            }
        }
    }

    public static synchronized ArrayList<String> getCategroyFilters(Context context) {
        ArrayList<String> arrayList;
        synchronized (CocktailBarFeatures.class) {
            if (mCategoryFilter == null) {
                mCategoryFilter = new ArrayList();
            }
            arrayList = mCategoryFilter;
        }
        return arrayList;
    }

    public static boolean isSupportCategory(Context context, String str) {
        if (mCategoryFilter == null) {
            getCategroyFilters(context);
        }
        return mCategoryFilter.size() <= 0 || mCategoryFilter.contains(str);
    }

    public static boolean isSupportCocktailBar(Context context) {
        ensureCocktailFeature(context);
        return sCocktailFeature == 1;
    }

    public static boolean isSupportCocktailPanel(Context context) {
        ensureCocktailFeature(context);
        return sCocktailFeature == 1 || sCocktailFeature == 2;
    }

    @Deprecated
    public static boolean isSystemBarType(Context context) {
        return isSupportCocktailBar(context);
    }

    private static int verifyCocktailFeature(PackageManager packageManager, int i, String str) {
        return packageManager != null ? packageManager.hasSystemFeature(str) ? i : 0 : new File(new StringBuilder().append("system/etc/permissions/").append(str).append(".xml").toString()).exists() ? i : 0;
    }
}
