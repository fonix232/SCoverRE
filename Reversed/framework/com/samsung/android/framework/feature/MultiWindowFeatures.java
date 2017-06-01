package com.samsung.android.framework.feature;

import android.os.Bundle;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.ArrayList;

public class MultiWindowFeatures {
    public static boolean ENSURE_DOCKED_VIEW_SUPPORT = false;
    public static final String FEATURE_ENSURE_DOCKED_VIEW = "com.sec.feature.multiwindow.ensure_docked_view";
    public static final String FEATURE_LONG_DEVICE_SNAP_MODE = "com.sec.feature.multiwindow.long_device_snap_mode";
    public static final String FEATURE_SNAP_WINDOW = "com.sec.feature.multiwindow.snap_view";
    public static boolean FREEFORM_ADJUST_STACK_ORDER = false;
    public static boolean FREEFORM_DENSITY_CHANGE = false;
    public static boolean FREEFORM_FOCUSED_FRAME = false;
    public static boolean FREEFORM_GESTURE = false;
    public static boolean FREEFORM_GHOST_MODE = false;
    public static boolean FREEFORM_GUIDE_RESIZE = false;
    public static boolean FREEFORM_SLIDE_MODE = false;
    public static boolean FREEFORM_SUPPORT = false;
    public static boolean LONG_DEVICE_SNAP_MODE_SUPPORT = false;
    public static boolean MINIMIZED_DOCK_DYNAMIC_ENABLED = true;
    public static boolean MULTIINSTANCE_SUPPORT = false;
    public static boolean MULTIWINDOW_DYNAMIC_ENABLED = false;
    public static boolean SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED = false;
    public static final boolean SAMSUNG_MULTIWINDOW_HIDE_STATUSBAR_IN_DOCKED = true;
    public static final boolean SAMSUNG_MULTIWINDOW_SUPPORT = true;
    public static final String SET_FREEFORM_GESTURE = "db_popup_view_shortcut";
    public static final String SET_MULTIWINDOW_DYNAMIC_ENALBED = "multi_window_enabled";
    public static final String SET_MULTIWINDOW_FEATURE_LIST = "set_multiwindow_feature_list";
    public static boolean SNAP_WINDOW_SUPPORT = false;
    public static final String TAG = "MultiWindowFeatures";
    public static final int UPDATE_DECOR_CAPTION_FEATURES = 89;
    public static final int UPDATE_ENSURE_DOCKED_VIEW_SUPPORT = 262144;
    public static final int UPDATE_FREEFORM_DENSITY_CHANGE = 64;
    public static final int UPDATE_FREEFORM_FOCUSED_FRAME = 16;
    public static final int UPDATE_FREEFORM_GESTURE = 4;
    public static final int UPDATE_FREEFORM_GHOST_MODE = 8;
    public static final int UPDATE_FREEFORM_GUIDE_RESIZE = 128;
    public static final int UPDATE_FREEFORM_SLIDE_MODE = 32;
    public static final int UPDATE_FREEFORM_SUPPORT = 2;
    public static final int UPDATE_LONG_DEVICE_SNAP_MODE_SUPPORT = 1048576;
    public static final int UPDATE_MULTIINSTANCE_SUPPORT = 65536;
    public static final int UPDATE_MULTIWINDOW_DYNAMIC_ENABLED = 131072;
    public static final int UPDATE_SAMSUNG_MULTIWINDOW_ENABLED = 1;
    public static final int UPDATE_SNAP_WINDOW_SUPPORT = 524288;
    public static final String VAL_FREEFORM_DENSITY_CHANGE = "freeform_density_change";
    public static final String VAL_FREEFORM_FOCUSED_FRAME = "freeform_focused_frame";
    public static final String VAL_FREEFORM_GHOST_MODE = "freeform_ghost_mode";
    public static final String VAL_FREEFORM_GUIDE_RESIZE = "freeform_guide_resize";
    public static final String VAL_FREEFORM_SLIDE_MODE = "freeform_slide_mode";
    public static final String VAL_FREEFORM_SUPPORT = "freeform_support";
    public static final String VAL_MULTIINSTANCE_SUPPORT = "multiinstance_support";
    public static final String VAL_MULTIWINDOW_DEFAULT_SETTINGS = "multiwindow_enabled,freeform_density_change,freeform_focused_frame,freeform_guide_resize";
    public static final String VAL_MULTIWINDOW_ENABLED = "multiwindow_enabled";

    public static boolean addSettingDB(String str, ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.contains(str)) {
            return false;
        }
        arrayList.add(str);
        return true;
    }

    public static String makeSettingDB(ArrayList<String> arrayList) {
        String str = "";
        if (arrayList != null) {
            int size = arrayList.size();
            for (String str2 : arrayList) {
                str = str + str2;
                if (arrayList.indexOf(str2) != size - 1) {
                    str = str + FingerprintManager.FINGER_PERMISSION_DELIMITER;
                }
            }
        }
        return str;
    }

    public static int onMultiWindowSettingsChange(Bundle bundle) {
        if (bundle == null) {
            return 0;
        }
        String string = bundle.getString(SET_MULTIWINDOW_FEATURE_LIST);
        ArrayList arrayList = new ArrayList();
        arrayList = parseSettingDB(string);
        boolean z = SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED;
        boolean z2 = FREEFORM_SUPPORT;
        boolean z3 = FREEFORM_GESTURE;
        boolean z4 = FREEFORM_GHOST_MODE;
        boolean z5 = FREEFORM_FOCUSED_FRAME;
        boolean z6 = FREEFORM_SLIDE_MODE;
        boolean z7 = FREEFORM_DENSITY_CHANGE;
        boolean z8 = FREEFORM_GUIDE_RESIZE;
        boolean z9 = MULTIINSTANCE_SUPPORT;
        boolean z10 = MULTIWINDOW_DYNAMIC_ENABLED;
        boolean z11 = ENSURE_DOCKED_VIEW_SUPPORT;
        boolean z12 = SNAP_WINDOW_SUPPORT;
        boolean z13 = LONG_DEVICE_SNAP_MODE_SUPPORT;
        SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED = arrayList.contains(VAL_MULTIWINDOW_ENABLED);
        FREEFORM_SUPPORT = arrayList.contains(VAL_FREEFORM_SUPPORT);
        FREEFORM_GHOST_MODE = arrayList.contains(VAL_FREEFORM_GHOST_MODE);
        FREEFORM_FOCUSED_FRAME = arrayList.contains(VAL_FREEFORM_FOCUSED_FRAME);
        FREEFORM_SLIDE_MODE = arrayList.contains(VAL_FREEFORM_SLIDE_MODE);
        FREEFORM_DENSITY_CHANGE = arrayList.contains(VAL_FREEFORM_DENSITY_CHANGE);
        FREEFORM_GUIDE_RESIZE = arrayList.contains(VAL_FREEFORM_GUIDE_RESIZE);
        MULTIINSTANCE_SUPPORT = arrayList.contains(VAL_MULTIINSTANCE_SUPPORT);
        MULTIWINDOW_DYNAMIC_ENABLED = bundle.getInt(SET_MULTIWINDOW_DYNAMIC_ENALBED, 1) == 1;
        FREEFORM_GESTURE = bundle.getInt(SET_FREEFORM_GESTURE, -1) == 1;
        ENSURE_DOCKED_VIEW_SUPPORT = bundle.getBoolean(FEATURE_ENSURE_DOCKED_VIEW);
        SNAP_WINDOW_SUPPORT = bundle.getBoolean(FEATURE_SNAP_WINDOW);
        LONG_DEVICE_SNAP_MODE_SUPPORT = bundle.getBoolean(FEATURE_LONG_DEVICE_SNAP_MODE);
        int i = 0;
        if (SAMSUNG_MULTIWINDOW_DYNAMIC_ENABLED != z) {
            i = 1;
        }
        if (FREEFORM_SUPPORT != z2) {
            i |= 2;
        }
        if (FREEFORM_GESTURE != z3) {
            i |= 4;
        }
        if (FREEFORM_GHOST_MODE != z4) {
            i |= 8;
        }
        if (FREEFORM_FOCUSED_FRAME != z5) {
            i |= 16;
        }
        if (FREEFORM_SLIDE_MODE != z6) {
            i |= 32;
        }
        if (FREEFORM_DENSITY_CHANGE != z7) {
            i |= 64;
        }
        if (FREEFORM_GUIDE_RESIZE != z8) {
            i |= 128;
        }
        if (MULTIINSTANCE_SUPPORT != z9) {
            i |= 65536;
        }
        if (MULTIWINDOW_DYNAMIC_ENABLED != z10) {
            i |= 131072;
        }
        if (ENSURE_DOCKED_VIEW_SUPPORT != z11) {
            i |= 262144;
        }
        if (SNAP_WINDOW_SUPPORT != z12) {
            i |= 524288;
        }
        if (LONG_DEVICE_SNAP_MODE_SUPPORT != z13) {
            i |= 1048576;
        }
        return i;
    }

    public static ArrayList<String> parseSettingDB(String str) {
        ArrayList<String> arrayList = new ArrayList();
        if (str != null) {
            String[] split = str.trim().split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("")) {
                    arrayList.add(split[i]);
                }
            }
        }
        return arrayList;
    }

    public static boolean removeSettingDB(String str, ArrayList<String> arrayList) {
        if (arrayList == null || !arrayList.contains(str)) {
            return false;
        }
        arrayList.remove(str);
        return true;
    }

    public static boolean updateDecorCaption(int i) {
        return (i & 89) != 0;
    }

    public static boolean updateMultiWindowDynamicEnabled(int i) {
        return (131072 & i) != 0;
    }
}
