package com.samsung.android.sdk.look;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.sdk.SsdkInterface;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonFrequentContactAdapter;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;

public final class Slook implements SsdkInterface {
    public static final int AIRBUTTON = 1;
    public static final int COCKTAIL_BAR = 6;
    public static final int COCKTAIL_PANEL = 7;
    public static final int SMARTCLIP = 2;
    public static final int SPEN_BEZEL_INTERACTION = 5;
    public static final int SPEN_HOVER_ICON = 4;
    private static final String TAG = "Slook";
    private static final int VERSION_CODE = 6;
    private static final String VERSION_NAME = "1.3.1";
    public static final int WRITINGBUDDY = 3;

    public static class VERSION_CODES {
        public static final int L1 = 1;
        public static final int L2 = 2;
    }

    public int getVersionCode() {
        return 6;
    }

    public String getVersionName() {
        return VERSION_NAME;
    }

    public void initialize(Context arg0) throws SsdkUnsupportedException {
        if (!SsdkVendorCheck.isSamsungDevice()) {
            throw new SsdkUnsupportedException("This device is not samsung product.", 0);
        } else if (isSupportedDevice()) {
            try {
                insertLog(arg0);
            } catch (SecurityException e) {
                throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
            }
        } else {
            throw new SsdkUnsupportedException("This device is not supported.", 1);
        }
    }

    private boolean isSupportedDevice() {
        int type = 1;
        while (type <= 7) {
            switch (type) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    if (VERSION.SDK_INT >= 17 && SlookImpl.isFeatureEnabled(type)) {
                        return true;
                    }
                case 6:
                    if (VERSION.SDK_INT >= 19 && SlookImpl.isFeatureEnabled(type)) {
                        return true;
                    }
                case COCKTAIL_PANEL /*7*/:
                    if (VERSION.SDK_INT >= 21) {
                        if (!SlookImpl.isFeatureEnabled(type)) {
                            break;
                        }
                        return true;
                    } else if (VERSION.SDK_INT >= 19 && SlookImpl.isFeatureEnabled(6)) {
                        return true;
                    }
                default:
                    break;
            }
            type++;
        }
        return false;
    }

    public boolean isFeatureEnabled(int type) {
        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (VERSION.SDK_INT < 17) {
                    return false;
                }
                return SlookImpl.isFeatureEnabled(type);
            case 6:
                if (VERSION.SDK_INT >= 19) {
                    return SlookImpl.isFeatureEnabled(type);
                }
                return false;
            case COCKTAIL_PANEL /*7*/:
                if (VERSION.SDK_INT < 19) {
                    return false;
                }
                if (VERSION.SDK_INT < 21) {
                    return SlookImpl.isFeatureEnabled(6);
                }
                return SlookImpl.isFeatureEnabled(type);
            default:
                throw new IllegalArgumentException("The type(" + type + ") is not supported.");
        }
    }

    private void insertLog(Context context) {
        try {
            if (FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE")) {
                int version = -1;
                try {
                    version = context.getPackageManager().getPackageInfo("com.samsung.android.providers.context", SlookCocktailManager.COCKTAIL_DISPLAY_POLICY_NOT_PROVISION).versionCode;
                } catch (NameNotFoundException e) {
                    Log.d("SM_SDK", "Could not find ContextProvider");
                }
                Log.d("SM_SDK", "versionCode: " + version);
                if (version <= 1) {
                    Log.d("SM_SDK", "Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
                    return;
                } else if (context.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
                    throw new SecurityException();
                } else {
                    ContentValues cv = new ContentValues();
                    String appId = getClass().getPackage().getName();
                    String feature = context.getPackageName() + "#" + getVersionCode();
                    cv.put("app_id", appId);
                    cv.put("feature", feature);
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
                    broadcastIntent.putExtra(SlookAirButtonFrequentContactAdapter.DATA, cv);
                    broadcastIntent.setPackage("com.samsung.android.providers.context");
                    context.sendBroadcast(broadcastIntent);
                    return;
                }
            }
            Log.d("SM_SDK", "CONTEXTSERVICE_ENABLE_SURVEY_MODE is disable");
        } catch (NoClassDefFoundError e2) {
            Log.e("SM_SDK", "NoClassDefFoundError : " + e2);
        } catch (NoSuchMethodError e3) {
            Log.e("SM_SDK", "NoSuchMethodError : " + e3);
        }
    }
}
