package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.GeneralUtil;
import android.util.Log;
import com.samsung.android.desktopmode.SemDesktopModeManager;
import com.samsung.android.emergencymode.SemEmergencyManager;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.mateservice.action.ActionBase;
import com.sec.android.app.CscFeatureTagFramework;
import java.util.ArrayList;
import java.util.List;

public class SShareCommon {
    private static final boolean DEBUG = false;
    private static final String TAG = "SShareCommon";
    private static boolean mIsSupportBixby = false;
    private static boolean mIsSupportButtons = false;
    private static boolean mIsSupportEnhancedMoreActions = false;
    private static boolean mIsSupportGridResolver = false;
    private static boolean mIsSupportLogging = false;
    private static boolean mIsSupportMoreActions = false;
    private static boolean mIsSupportNearby = false;
    private static boolean mIsSupportPageMode = false;
    private static boolean mIsSupportRecentSort = false;
    private static boolean mIsSupportResolverGuide = false;
    private static boolean mIsSupportShareLink = false;
    private static boolean mIsSupportShareLinkLayout = false;
    private static boolean mIsSupportShowButtonShapes = false;
    private static boolean mIsSupportSimpleSharing = false;
    private Context mContext;
    private boolean mDeviceDefault;
    private boolean mEasySignUpAlreadyChecked;
    private boolean mEasySignUpEnabled;
    private List<Intent> mExtraIntentList;
    private int mIconChangePlayer;
    private int mIconPrint;
    private int mIconQuickConnect;
    private int mIconScreenMirroring;
    private int mIconScreenSharing;
    private int mItemCount;
    private int mLaunchedFromUid;
    private Intent mResolverGuideIntent;
    private boolean mSendBixbyResult;
    private String mShareLinkReflectionTitle;

    public SShareCommon(Context context, Intent intent, boolean z, int i, List<Intent> list, int i2) {
        this(context, intent, z, false, false, i, list, i2);
    }

    public SShareCommon(Context context, Intent intent, boolean z, boolean z2, boolean z3, int i, List<Intent> list, int i2) {
        this.mItemCount = 0;
        this.mEasySignUpAlreadyChecked = false;
        this.mEasySignUpEnabled = false;
        this.mIconChangePlayer = 0;
        this.mIconScreenMirroring = 0;
        this.mIconScreenSharing = 0;
        this.mIconQuickConnect = 0;
        this.mIconPrint = 0;
        this.mContext = context;
        this.mItemCount = i2;
        this.mLaunchedFromUid = i;
        this.mExtraIntentList = list;
        this.mDeviceDefault = z;
        if (!(!z || z2 || z3 || i < 0 || UserHandle.isIsolated(i))) {
            if (getShareLinkSupportState()) {
                setShareLinkFeature(intent);
                setShareLinkLayoutFeature();
            } else {
                setSimpleSharingFeature(intent);
            }
            setQuickConnectFeature();
            setMoreActionsFeature(intent);
            setRecentSortFeature();
            setLoggingFeature();
            setBixbyFeature();
        }
        if (z2 || z3) {
            setGridResolverFeature();
            setButtonsFeature();
            setResolverGuideFeature(intent);
        }
        setPageModeFeature();
    }

    private boolean checkSimpleShareSupport() {
        if (this.mEasySignUpAlreadyChecked) {
            return this.mEasySignUpEnabled;
        }
        try {
            int supportedFeatures = SShareSignUpManager.getSupportedFeatures(this.mContext, 2);
            Log.d(TAG, "checkSimpleShareSupport = " + supportedFeatures);
            if (supportedFeatures == -1) {
                this.mEasySignUpEnabled = false;
            } else {
                this.mEasySignUpEnabled = true;
            }
            this.mEasySignUpAlreadyChecked = true;
        } catch (Throwable e) {
            this.mEasySignUpEnabled = false;
            Log.e(TAG, "SShareSignUpManager returns exception !!!", e);
        }
        return this.mEasySignUpEnabled;
    }

    private boolean getButtonShapeSupportState() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        return contentResolver != null && System.getInt(contentResolver, "show_button_background", 0) == 1;
    }

    private boolean getButtonsSupportState() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        return contentResolver != null && System.getInt(contentResolver, "default_app_selection_option", 0) == 1;
    }

    private boolean getEnhancedMoreActionsSupportState() {
        return !"2016B".equals(SShareConstants.SECUX_VERSION) ? "2017A".equals(SShareConstants.SECUX_VERSION) : true;
    }

    private boolean getMoreActionsSupportState(Intent intent) {
        boolean z = true;
        if (isEmergencyOrUPSModeEnabled()) {
            return false;
        }
        try {
            this.mIconScreenSharing = intent.getIntExtra(SShareConstants.MORE_ACTIONS_SCREEN_SHARING, 0);
            this.mIconScreenMirroring = intent.getIntExtra(SShareConstants.MORE_ACTIONS_SCREEN_MIRRORING, 0);
            this.mIconChangePlayer = intent.getIntExtra(SShareConstants.MORE_ACTIONS_CHANGE_PLAYER, 0);
            this.mIconQuickConnect = getSupportNearby() ? intent.getIntExtra(SShareConstants.MORE_ACTIONS_QUICK_CONNECT, 0) : 0;
            this.mIconPrint = UserHandle.myUserId() >= 100 ? 0 : intent.getIntExtra(SShareConstants.MORE_ACTIONS_PRINT, 0);
        } catch (Throwable e) {
            Log.e(TAG, "Exception !!! during getting more actions", e);
        } catch (OutOfMemoryError e2) {
            Log.d(TAG, "OutOfMemoryError !!! during getting more actions");
        }
        if (this.mIconChangePlayer == 1) {
            intent.putExtra(SShareConstants.MORE_ACTIONS_CHANGE_PLAYER, 0);
        }
        if (this.mIconScreenMirroring == 1) {
            intent.putExtra(SShareConstants.MORE_ACTIONS_SCREEN_MIRRORING, 0);
        }
        if (this.mIconScreenSharing == 1 || this.mIconScreenSharing == 2) {
            intent.putExtra(SShareConstants.MORE_ACTIONS_SCREEN_SHARING, 0);
        }
        if (this.mIconQuickConnect == 1) {
            intent.putExtra(SShareConstants.MORE_ACTIONS_QUICK_CONNECT, 0);
        }
        if (this.mIconPrint == 1) {
            intent.putExtra(SShareConstants.MORE_ACTIONS_PRINT, 0);
        }
        if (!(this.mIconChangePlayer == 1 || this.mIconScreenMirroring == 1 || this.mIconScreenSharing == 1 || this.mIconScreenSharing == 2 || this.mIconQuickConnect == 1 || this.mIconPrint == 1)) {
            z = false;
        }
        return z;
    }

    private boolean getPageModeSupportState() {
        return (!GeneralUtil.isPhone() || mIsSupportButtons || isDesktopModeEnabled()) ? false : true;
    }

    private boolean getQuickConnectSupportState() {
        try {
            this.mContext.getPackageManager().getApplicationInfo(SShareConstants.QUICK_CONNECT_PKG, 128);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getResolverGuideSupportState(Intent intent) {
        return isUSA();
    }

    private boolean getShareLinkLayoutSupportState() {
        return !"2016B".equals(SShareConstants.SECUX_VERSION) ? "2017A".equals(SShareConstants.SECUX_VERSION) : true;
    }

    private boolean getShareLinkSupportState() {
        return !"2016B".equals(SShareConstants.SECUX_VERSION) ? "2017A".equals(SShareConstants.SECUX_VERSION) : true;
    }

    private boolean getShareLinkSupportState(Intent intent) {
        boolean checkSimpleShareSupport = checkSimpleShareSupport();
        boolean isIntentTypeSupportRemoteShare = isIntentTypeSupportRemoteShare(intent);
        boolean isKnoxModeEnabled = isKnoxModeEnabled();
        boolean isEmergencyOrUPSModeEnabled = isEmergencyOrUPSModeEnabled();
        boolean isForceSimpleSharingDisable = isForceSimpleSharingDisable(intent);
        if (checkSimpleShareSupport && isIntentTypeSupportRemoteShare && !isKnoxModeEnabled && !isEmergencyOrUPSModeEnabled && !isForceSimpleSharingDisable) {
            return true;
        }
        Log.d(TAG, "featureEnable = " + checkSimpleShareSupport + " intentSupport = " + isIntentTypeSupportRemoteShare + " knoxMode = " + isKnoxModeEnabled + " emergencyMode = " + isEmergencyOrUPSModeEnabled + " forceDisable = " + isForceSimpleSharingDisable);
        return false;
    }

    private boolean getSimpleSharingSupportState(Intent intent) {
        boolean checkSimpleShareSupport = checkSimpleShareSupport();
        boolean isIntentTypeSupportRemoteShare = isIntentTypeSupportRemoteShare(intent);
        boolean isKnoxModeEnabled = isKnoxModeEnabled();
        boolean isEmergencyOrUPSModeEnabled = isEmergencyOrUPSModeEnabled();
        boolean isForceSimpleSharingDisable = isForceSimpleSharingDisable(intent);
        if (checkSimpleShareSupport && isIntentTypeSupportRemoteShare && !isKnoxModeEnabled && !isEmergencyOrUPSModeEnabled && !isForceSimpleSharingDisable) {
            return true;
        }
        Log.d(TAG, "featureEnable = " + checkSimpleShareSupport + " intentSupport = " + isIntentTypeSupportRemoteShare + " knoxMode = " + isKnoxModeEnabled + " emergencyMode = " + isEmergencyOrUPSModeEnabled + " forceDisable = " + isForceSimpleSharingDisable);
        return false;
    }

    private boolean hasExtraIntentUriInfo() {
        if (this.mExtraIntentList != null) {
            for (int i = 0; i < this.mExtraIntentList.size(); i++) {
                Bundle extras = ((Intent) this.mExtraIntentList.get(i)).getExtras();
                if (extras != null && ((Uri) extras.getParcelable("android.intent.extra.STREAM")) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDesktopModeEnabled() {
        return ((SemDesktopModeManager) this.mContext.getSystemService("desktopmode")) != null && SemDesktopModeManager.isDesktopMode();
    }

    private boolean isEmergencyOrUPSModeEnabled() {
        if (this.mContext == null) {
            return false;
        }
        SemEmergencyManager instance = SemEmergencyManager.getInstance(this.mContext);
        Object obj = null;
        boolean z = false;
        if (instance != null) {
            obj = (!instance.isEmergencyMode() || instance.checkModeType(512)) ? null : 1;
            z = instance.isEmergencyMode() ? instance.checkModeType(512) : false;
            boolean isEmergencyMode = instance.isEmergencyMode();
        }
        if (obj != null) {
            z = true;
        }
        return z;
    }

    private boolean isForceSimpleSharingDisable(Intent intent) {
        return intent.getIntExtra(SShareConstants.SIMPLE_SHARING_FORCE_DISABLE, 0) == 1;
    }

    private boolean isIntentTypeSupportRemoteShare(Intent intent) {
        String action = intent.getAction();
        return ("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) && isIntentUriDataIValidCheck(intent);
    }

    private boolean isIntentUriDataIValidCheck(Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.SEND".equals(action)) {
            Uri uri = null;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                uri = (Uri) extras.getParcelable("android.intent.extra.STREAM");
            }
            if (uri == null) {
                return hasExtraIntentUriInfo();
            } else {
                if ("com.android.contacts".equals(uri.getEncodedAuthority())) {
                    return true;
                }
            }
        } else if ("android.intent.action.SEND_MULTIPLE".equals(action)) {
            ArrayList arrayList = new ArrayList();
            arrayList = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            if (arrayList == null) {
                return false;
            }
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                if (arrayList.get(i) != null) {
                    return "com.android.contacts".equals(((Uri) arrayList.get(i)).getEncodedAuthority()) ? true : true;
                } else {
                    i++;
                }
            }
            return false;
        }
        return true;
    }

    private boolean isUSA() {
        return SemCscFeature.getInstance().getBoolean(CscFeatureTagFramework.TAG_CSCFEATURE_FRAMEWORK_SUPPORTRESOLVERACTIVITYGUIDE);
    }

    private void setBixbyFeature() {
        mIsSupportBixby = SShareConstants.ENABLE_BIXBY;
    }

    private void setButtonsFeature() {
        mIsSupportButtons = getButtonsSupportState();
        if (mIsSupportButtons) {
            mIsSupportShowButtonShapes = getButtonShapeSupportState();
        }
    }

    private void setGridResolverFeature() {
        mIsSupportGridResolver = true;
    }

    private void setLoggingFeature() {
        mIsSupportLogging = SShareConstants.ENABLE_SURVEY_MODE;
    }

    private void setMoreActionsFeature(Intent intent) {
        mIsSupportMoreActions = getMoreActionsSupportState(intent);
        mIsSupportEnhancedMoreActions = getEnhancedMoreActionsSupportState();
    }

    private void setPageModeFeature() {
        mIsSupportPageMode = getPageModeSupportState();
    }

    private void setQuickConnectFeature() {
        mIsSupportNearby = getQuickConnectSupportState();
    }

    private void setRecentSortFeature() {
        mIsSupportRecentSort = mIsSupportPageMode;
    }

    private void setResolverGuideFeature(Intent intent) {
        mIsSupportResolverGuide = getResolverGuideSupportState(intent);
    }

    private void setShareLinkFeature(Intent intent) {
        mIsSupportShareLink = getShareLinkSupportState(intent);
    }

    private void setShareLinkLayoutFeature() {
        mIsSupportShareLinkLayout = getShareLinkLayoutSupportState();
    }

    private void setSimpleSharingFeature(Intent intent) {
        mIsSupportSimpleSharing = getSimpleSharingSupportState(intent);
    }

    public int getChangePlayerEnable() {
        return this.mIconChangePlayer;
    }

    public String getMenuName(int i) {
        if (i == 0) {
            return this.mShareLinkReflectionTitle != null ? this.mShareLinkReflectionTitle : this.mContext.getResources().getString(17041151);
        } else {
            if (i == 1) {
                return this.mContext.getResources().getString(17041146);
            }
            if (i == 2) {
                return this.mContext.getResources().getString(17041147);
            }
            Log.w(TAG, "wrong app type!! name is null");
            return "";
        }
    }

    public int getPrintEnable() {
        return this.mIconPrint;
    }

    public int getQuickConnectEnable() {
        return this.mIconQuickConnect;
    }

    public Intent getResolverGuideIntent() {
        return this.mResolverGuideIntent;
    }

    public int getScreenMirroringEnable() {
        return this.mIconScreenMirroring;
    }

    public int getScreenSharingEnable() {
        return this.mIconScreenSharing;
    }

    public boolean getSupportBixby() {
        return mIsSupportBixby;
    }

    public boolean getSupportButtons() {
        return mIsSupportButtons;
    }

    public boolean getSupportEnhancedMoreActions() {
        return mIsSupportEnhancedMoreActions;
    }

    public boolean getSupportGridResolver() {
        return mIsSupportGridResolver;
    }

    public boolean getSupportLogging() {
        return mIsSupportLogging;
    }

    public boolean getSupportMoreActions() {
        return mIsSupportMoreActions;
    }

    public boolean getSupportNearby() {
        return mIsSupportNearby;
    }

    public boolean getSupportPageMode() {
        return mIsSupportPageMode;
    }

    public boolean getSupportRecentSort() {
        return mIsSupportRecentSort;
    }

    public boolean getSupportResolverGuide() {
        return mIsSupportResolverGuide;
    }

    public boolean getSupportShareLink() {
        return mIsSupportShareLink;
    }

    public boolean getSupportShareLinkLayout() {
        return mIsSupportShareLinkLayout;
    }

    public boolean getSupportShowButtonShapes() {
        return mIsSupportShowButtonShapes;
    }

    public boolean getSupportSimpleSharing() {
        return mIsSupportSimpleSharing;
    }

    public boolean isDeviceDefaultTheme() {
        return this.mDeviceDefault;
    }

    public boolean isIntentFileUriScheme(Intent intent) {
        String action = intent.getAction();
        Uri uri;
        if ("android.intent.action.SEND".equals(action)) {
            uri = null;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                uri = (Uri) extras.getParcelable("android.intent.extra.STREAM");
            }
            return uri != null && "file".equals(uri.getScheme());
        } else if ("android.intent.action.SEND_MULTIPLE".equals(action)) {
            ArrayList arrayList = new ArrayList();
            arrayList = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
            if (arrayList == null) {
                return false;
            }
            int i = 0;
            while (i < arrayList.size()) {
                if (arrayList.get(i) != null && "file".equals(((Uri) arrayList.get(i)).getScheme())) {
                    return true;
                }
                i++;
            }
        } else {
            uri = intent.getData();
            if (uri != null && "file".equals(uri.getScheme())) {
                return true;
            }
        }
    }

    public boolean isKnoxModeEnabled() {
        return UserHandle.getUserId(this.mLaunchedFromUid) >= 100;
    }

    public void setResolverGuideIntent(Activity activity, Intent intent, boolean z) {
        if (mIsSupportResolverGuide) {
            try {
                Intent intent2 = new Intent(intent);
                intent2.setComponent(new ComponentName(SShareConstants.RESOLVER_GUIDE_ACTIVITY_PKG, SShareConstants.RESOLVER_GUIDE_ACTIVITY_CLASS));
                intent2.putExtra("android.intent.extra.INTENT", intent);
                intent2.putExtra(SShareConstants.EXTRA_SAFE_FORWARD, z);
                intent2.addFlags(ActionBase.ATTR_TYPE_SDK);
                this.mResolverGuideIntent = intent2;
            } catch (ActivityNotFoundException e) {
                Log.w(TAG, "Activity Not Found");
            } catch (Exception e2) {
                Log.w(TAG, "Class Not Found");
            }
        }
    }

    public void setShareLinkReflectionTitle(String str) {
        this.mShareLinkReflectionTitle = str;
    }
}
