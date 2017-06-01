package com.sec.tima.keystore.util;

import android.os.SystemProperties;

public class Utility {
    public static final String CHIPNAME = SystemProperties.get("ro.chipname");
    private static final String[] ECC_SUPPORT_CHIPSETS = new String[]{"MSM8996", "exynos8890", "msm8998", "exynos8895"};
    private static Utility INSTANCE = null;
    public static final String PRODUCT_NAME = SystemProperties.get("ro.product.name");
    public static final String SDK_VERSION = SystemProperties.get("ro.build.version.sdk");
    private final String[] FIPS_SUPPORT_CHIPSETS = new String[]{"MSM8998", "MSM8996", "exynos8890", "exynos8895"};
    private final String[] SDK_21_MODELS = new String[]{"ZERO"};
    private final String[] SDK_22_MODELS = new String[]{"ZERO", "NOBLE", "ZEN"};
    private final String[] SDK_23_MODELS = new String[]{"ZERO", "NOBLE", "ZEN", "HERO", "SC-02H", "SCV33"};
    private final String[] SDK_24_MODELS = new String[]{"ZERO", "NOBLE", "ZEN", "HERO", "SC-02H", "SCV33"};
    private boolean mIsEnabled = false;

    Utility() {
        if (!(PRODUCT_NAME == null || SDK_VERSION == null)) {
            if (SDK_VERSION.equals("21")) {
                checkModels(this.SDK_21_MODELS);
            } else if (SDK_VERSION.equals("22")) {
                checkModels(this.SDK_22_MODELS);
            } else if (SDK_VERSION.equals("23")) {
                checkModels(this.SDK_23_MODELS);
            } else if (SDK_VERSION.equals("24")) {
                checkModels(this.SDK_24_MODELS);
            }
        }
        if (CHIPNAME != null) {
            checkModels(CHIPNAME, this.FIPS_SUPPORT_CHIPSETS);
        }
    }

    private void checkModels(String str, String[] strArr) {
        for (String toLowerCase : strArr) {
            if (str.toLowerCase().startsWith(toLowerCase.toLowerCase())) {
                this.mIsEnabled = true;
                return;
            }
        }
    }

    private void checkModels(String[] strArr) {
        for (String toLowerCase : strArr) {
            if (PRODUCT_NAME.toLowerCase().startsWith(toLowerCase.toLowerCase())) {
                this.mIsEnabled = true;
                return;
            }
        }
    }

    public static boolean isECCSupported() {
        if (CHIPNAME != null) {
            for (String toLowerCase : ECC_SUPPORT_CHIPSETS) {
                if (CHIPNAME.toLowerCase().startsWith(toLowerCase.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isFipsTimaEnabled() {
        if (INSTANCE == null) {
            INSTANCE = new Utility();
            if (INSTANCE == null) {
                return false;
            }
        }
        return INSTANCE.mIsEnabled;
    }
}
