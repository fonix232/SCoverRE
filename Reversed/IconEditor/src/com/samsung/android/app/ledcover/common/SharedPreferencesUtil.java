package com.samsung.android.app.ledcover.common;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.samsung.context.sdk.samsunganalytics.C0316a;

public class SharedPreferencesUtil {
    private static final String FILE_NAME_SETTINGS = "settings";
    private static final String KEY_IS_SHORTCUT_ADDED = "is_shortcut_added";
    private static final String KEY_MANAGER_INITIAL_INSTALLED = "key_manager_initial_installed";
    private static final String KEY_PDA_VERSION = "key_pda_version";
    private static final String KEY_REBOOTING_FLAG = "key_rebooting_flag";
    private static final String KEY_SHORTCUT_ENABLED = "isShortcutEnable";

    public static void saveManagerInitialInstalled(Context context, boolean isOn) {
        putBoolean(context, FILE_NAME_SETTINGS, KEY_MANAGER_INITIAL_INSTALLED, isOn);
    }

    public static boolean loadManagerInitialInstalled(Context context) {
        return getBooleanDefaultAsTrue(context, FILE_NAME_SETTINGS, KEY_MANAGER_INITIAL_INSTALLED);
    }

    private static void putBoolean(Context context, String fileName, String keyName, boolean b) {
        Editor editor = context.getSharedPreferences(fileName, 0).edit();
        editor.putBoolean(keyName, b);
        editor.apply();
    }

    private static boolean getBooleanDefaultAsTrue(Context context, String fileName, String keyName) {
        return context.getSharedPreferences(fileName, 0).getBoolean(keyName, true);
    }

    private static boolean getBooleanDefaultAsFalse(Context context, String fileName, String keyName) {
        return context.getSharedPreferences(fileName, 0).getBoolean(keyName, false);
    }

    private static void putString(Context context, String fileName, String keyName, String str) {
        Editor editor = context.getSharedPreferences(fileName, 0).edit();
        editor.putString(keyName, str);
        editor.apply();
    }

    private static String getString(Context context, String fileName, String keyName) {
        return context.getSharedPreferences(fileName, 0).getString(keyName, C0316a.f163d);
    }

    public static void saveShortcutEnable(Context context, boolean isOn) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(KEY_SHORTCUT_ENABLED, isOn);
        editor.apply();
    }

    public static boolean loadShortcutEnable(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_SHORTCUT_ENABLED, true);
    }

    public static boolean isShortcutAdded(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_IS_SHORTCUT_ADDED, false);
    }

    public static void setShortcutAdded(Context context, boolean isInstalled) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(KEY_IS_SHORTCUT_ADDED, isInstalled);
        editor.apply();
    }

    public static void setKeyRebootingFlag(Context context, boolean isTrue) {
        putBoolean(context, FILE_NAME_SETTINGS, KEY_REBOOTING_FLAG, isTrue);
    }

    public static boolean getKeyRebootingFlag(Context context) {
        return getBooleanDefaultAsFalse(context, FILE_NAME_SETTINGS, KEY_REBOOTING_FLAG);
    }

    public static void setKeyPDAVersion(Context context, String version) {
        putString(context, FILE_NAME_SETTINGS, KEY_PDA_VERSION, version);
    }

    public static String getKeyPDAVersion(Context context) {
        return getString(context, FILE_NAME_SETTINGS, KEY_PDA_VERSION);
    }
}
