package com.samsung.android.config;

import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import java.io.PrintWriter;

public class SamsungCoreConfig {
    public static final boolean DSS_ENABLED = true;
    public static final boolean FEATURE_AOD;
    public static final boolean FEATURE_AUDIO_SPEAKER_LR_SWITCHING = (Integer.valueOf("1").intValue() == 4);
    public static final boolean FEATURE_CAMERA_ROTATION = true;
    public static final boolean FEATURE_CAR_MODE = true;
    public static final boolean FEATURE_COCKTAIL = true;
    public static final boolean FEATURE_CONVENTIONAL_MODE = true;
    public static final boolean FEATURE_COVER = true;
    public static final boolean FEATURE_HORIZONTAL_MODE = false;
    public static final boolean FEATURE_KEYBOARD_COVER = true;
    public static final boolean FEATURE_KNOX_DESKTOP = true;
    public static final boolean FEATURE_PACKAGE_CONFIGURATIONS_ENABLED = true;
    public static final boolean FEATURE_REDUCE_SCREEN = true;
    public static final boolean FEATURE_SAMSUNG_SERVICES = true;
    public static final boolean FEATURE_SAMSUNG_STARTING_WINDOW = true;
    public static final boolean FEATURE_SAMSUNG_STARTING_WINDOW_AUTO_CAPTURE = false;
    public static final boolean FEATURE_SF_EFFECTS = true;
    private static final String TAG = "SamsungCoreConfig";
    public static final int VERSION_AOD = Integer.valueOf("3").intValue();

    static {
        boolean z = true;
        if (VERSION_AOD <= 0) {
            z = false;
        }
        FEATURE_AOD = z;
    }

    public static void dump(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("VERSION_AOD=");
        printWriter.print(VERSION_AOD);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        printWriter.print(str);
        printWriter.print("FEATURE_COCKTAIL=");
        printWriter.print(true);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        printWriter.print(str);
        printWriter.print("FEATURE_REDUCE_SCREEN=");
        printWriter.print(true);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        printWriter.print(str);
        printWriter.print("FEATURE_CAMERA_ROTATION=");
        printWriter.print(true);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        printWriter.print(str);
        printWriter.print("FEATURE_HORIZONTAL_MODE=");
        printWriter.print(false);
        printWriter.println(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
    }
}
