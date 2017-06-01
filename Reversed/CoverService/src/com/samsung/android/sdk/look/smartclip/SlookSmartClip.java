package com.samsung.android.sdk.look.smartclip;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import com.samsung.android.sdk.look.Slook;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonFrequentContactAdapter;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.smartclip.SmartClipDataExtractionListener;
import com.samsung.android.smartclip.SmartClipMetaUtils;

public final class SlookSmartClip {
    private static final String PERMISSION_API_USAGE_LOG = "com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY";
    private static final String TAG = "SmartClip";
    private static boolean mApiUsageLogSent = false;
    private DataExtractionListener mDataExtractionListener = null;
    private Slook mSlook = new Slook();
    private View mView = null;

    class C00021 implements SmartClipDataExtractionListener {
        C00021() {
        }

        public int onExtractSmartClipData(View view, SlookSmartClipDataElement resultElement, SlookSmartClipCroppedArea croppedArea) {
            return SlookSmartClip.this.mDataExtractionListener.onExtractSmartClipData(view, resultElement, croppedArea);
        }
    }

    public interface DataExtractionListener {
        public static final int EXTRACTION_DEFAULT = 1;
        public static final int EXTRACTION_DISCARD = 0;

        int onExtractSmartClipData(View view, SlookSmartClipDataElement slookSmartClipDataElement, SlookSmartClipCroppedArea slookSmartClipCroppedArea);
    }

    public SlookSmartClip(View view) {
        this.mView = view;
        if (view != null && !mApiUsageLogSent) {
            sendApiUsageLog(view.getContext(), "SlookSmartClip");
            mApiUsageLogSent = true;
        }
    }

    public void addMetaTag(SlookSmartClipMetaTag metaTag) {
        if (isSupport(1)) {
            SmartClipMetaUtils.addMetaTag(this.mView, metaTag);
        }
    }

    public void removeMetaTag(String tagType) {
        if (isSupport(1)) {
            SmartClipMetaUtils.removeMetaTag(this.mView, tagType);
        }
    }

    public void clearAllMetaTag() {
        if (isSupport(1)) {
            SmartClipMetaUtils.clearAllMetaTag(this.mView);
        }
    }

    public void setDataExtractionListener(DataExtractionListener listener) {
        if (isSupport(1)) {
            this.mDataExtractionListener = listener;
            SmartClipMetaUtils.setDataExtractionListener(this.mView, new C00021());
        }
    }

    public int extractDefaultSmartClipData(SlookSmartClipDataElement resultElement, SlookSmartClipCroppedArea croppedArea) {
        if (!isSupport(1)) {
            return 0;
        }
        if (this.mView == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The view is null!");
            return 0;
        } else if (resultElement == null) {
            Log.e(TAG, "extractDefaultSmartClipData : The result element is null!");
            return 0;
        } else if (croppedArea != null) {
            return SmartClipMetaUtils.extractDefaultSmartClipData(this.mView, resultElement, croppedArea);
        } else {
            Log.e(TAG, "extractDefaultSmartClipData : The cropped area is null!");
            return 0;
        }
    }

    private boolean isSupport(int ver) {
        if (this.mSlook.isFeatureEnabled(2)) {
            return true;
        }
        return false;
    }

    private void sendApiUsageLog(Context context, String apiName) {
        String appId = this.mSlook.getClass().getPackage().getName();
        String feature = context.getPackageName() + "#" + this.mSlook.getVersionCode();
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo("com.samsung.android.providers.context", SlookCocktailManager.COCKTAIL_DISPLAY_POLICY_NOT_PROVISION).versionCode;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "sendApiUsageLog : Could not find ContextProvider");
        }
        Log.d(TAG, "sendApiUsageLog : Context framework's versionCode = " + version);
        if (version <= 1) {
            Log.d(TAG, "sendApiUsageLog : Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
        } else if (context.checkCallingOrSelfPermission(PERMISSION_API_USAGE_LOG) != 0) {
            throw new SecurityException("Requires com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
        } else {
            ContentValues cv = new ContentValues();
            cv.put("app_id", appId);
            cv.put("feature", feature);
            cv.put("extra", apiName);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
            broadcastIntent.putExtra(SlookAirButtonFrequentContactAdapter.DATA, cv);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcast(broadcastIntent);
        }
    }
}
