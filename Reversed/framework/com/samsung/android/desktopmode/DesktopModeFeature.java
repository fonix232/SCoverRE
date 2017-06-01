package com.samsung.android.desktopmode;

import android.os.Debug;
import android.util.Log;

public class DesktopModeFeature {
    public static final boolean CHECK_KCC = false;
    public static final boolean CLEAR_HOME_STACK = true;
    public static final boolean DEBUG = (!Debug.semIsProductDev() ? Log.isLoggable("DESKTOPMODE_DEBUG", 3) : true);
    public static final boolean ENABLED = true;
    public static final boolean FEATURE_TOUCHPAD = false;
    public static final boolean LAUNCH_APP_FREEFORM = true;
    public static final boolean REMOVE_ALL_TASKS = false;
    public static final boolean RESTORE_TOP_TASK = false;
    public static final boolean SHOW_ONGOING_NOTIFICATION = true;
    public static final boolean SWITCH_TASK_STACKS = true;
}
