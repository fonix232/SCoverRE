package com.samsung.android.multiwindow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.os.UserHandle;
import com.samsung.android.feature.SemFloatingFeature;

public class MultiWindowLogger {
    static final String APP_ID = "app_id";
    static final boolean DEBUG = false;
    public static final String DOCKED_BY = "DOBY";
    public static final String DOCKED_BY_FREEFORM_DRAGGING = "FreeformDragging";
    public static final String DOCKED_BY_RECENTS_DRAGGING = "RecentsDragging";
    public static final String DOCKED_BY_RECENTS_LONG = "RecentsLong";
    public static final String DOCKED_BY_RECENTS_MWBUTTON = "RecentsMWButton";
    public static final String DOCKED_OFF = "DOOF";
    public static final String DOCKED_OFF_BY_DIVIDER_CLOSE_BUTTON = "DividerCloseButton";
    public static final String DOCKED_OFF_BY_DIVIDER_DRAGGING = "DividerDragging";
    public static final String DOCKED_OFF_BY_MINIMIZED_NOTIFICAION = "MinimizedNotification";
    public static final String DOCKED_OFF_BY_MW_NOTSUPPORTED_APP = "MWNotSupportedApp";
    public static final String DOCKED_OFF_BY_RECENTS_LONG = "RecentsLong";
    public static final String DOCKED_PACKAGE = "DOPK";
    static final String EXTRA = "extra";
    static final String FEATURE = "feature";
    public static final String FREEFORM_ACTIONS = "FFAC";
    public static final String FREEFORM_ACTIONS_CHANGE_TO_SPLIT = "ChangeToSplit";
    public static final String FREEFORM_ACTIONS_CLOSE = "Close";
    public static final String FREEFORM_ACTIONS_MAXIMIZE = "Maximize";
    public static final String FREEFORM_ACTIONS_MINIMIZE = "Minimize";
    public static final String FREEFORM_ACTIONS_TRASH = "Trash";
    public static final String FREEFORM_BY = "FFBY";
    public static final String FREEFORM_BY_DIVIDER = "DividerButton";
    public static final String FREEFORM_BY_GESTURE = "Gesture";
    public static final String FREEFORM_BY_RECENTS_DRAGGING = "RecentsDragging";
    public static final String FREEFORM_ENTER_PACKAGE = "FFEP";
    public static final String FREEFORM_PACKAGE_LIST = "FFPA";
    public static final String FREEFORM_TASK_COUNT = "FFCO";
    static final String GSIM_DATA = "data";
    static final String GSIM_INTENT = "com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY";
    static final String GSIM_INTENT_MULTI = "com.samsung.android.providers.context.log.action.USE_MULTI_APP_FEATURE_SURVEY";
    static final String GSIM_PACKAGE = "com.samsung.android.providers.context";
    static final String MULTIWINDOW_ID = "com.samsung.android.multiwindow";
    public static final String SNAP_BY = "SNBY";
    public static final String SNAP_BY_FULLAPP = "fromFullApp";
    public static final String SNAP_BY_RECENT = "fromRecent";
    public static final String SNAP_BY_SPLIT = "fromSplit";
    public static final String SNAP_OFF = "SNOF";
    public static final String SNAP_OFF_DIVIDER_DRAGGING = "DividerDragging";
    public static final String SNAP_OFF_DIVIDER_FINISH_BUTTON = "DividerFinishButton";
    public static final String SNAP_OFF_ORIANTATION_CHANGED = "OrientationChanged";
    public static final String SNAP_PACKAGE = "SNPK";
    public static final String SPLIT_ACTIONS = "SPAC";
    public static final String SPLIT_ACTIONS_CLOSE = "Close";
    public static final String SPLIT_ACTIONS_SWITCH = "Switch";
    public static final String SPLIT_BY = "SPBY";
    public static final String SPLIT_BY_FREEFORM_DRAGGING = "FreeformDragging";
    public static final String SPLIT_BY_HOME = "Home";
    public static final String SPLIT_BY_RECENTS = "Recents";
    public static final String SPLIT_BY_RECENTS_APPLIST = "RecentsAppList";
    public static final String SPLIT_PAIR = "SPPA";
    private static final boolean SURVEY_LOG = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE");
    static final String TAG = "MultiWindowLogger";

    public static void logGSIM(Context context, String str, String str2) {
        if (SURVEY_LOG) {
            Parcelable contentValues = new ContentValues();
            contentValues.put("app_id", MULTIWINDOW_ID);
            contentValues.put("feature", str);
            if (str2 != null) {
                contentValues.put("extra", str2);
            }
            Intent intent = new Intent();
            intent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
            intent.putExtra("data", contentValues);
            intent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcastAsUser(intent, UserHandle.CURRENT_OR_SELF);
        }
    }

    public static void logGSIM(Context context, String[] strArr, String[] strArr2) {
        if (SURVEY_LOG) {
            int i = 0;
            while (i < strArr.length && strArr[i] != null) {
                i++;
            }
            Parcelable[] parcelableArr = new ContentValues[i];
            for (int i2 = 0; i2 < i; i2++) {
                parcelableArr[i2] = new ContentValues();
                parcelableArr[i2].put("app_id", MULTIWINDOW_ID);
                parcelableArr[i2].put("feature", strArr[i2]);
                if (strArr2[i2] != null) {
                    parcelableArr[i2].put("extra", strArr2[i2]);
                }
            }
            Intent intent = new Intent();
            intent.setAction(GSIM_INTENT_MULTI);
            intent.putExtra("data", parcelableArr);
            intent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcastAsUser(intent, UserHandle.CURRENT_OR_SELF);
        }
    }
}
