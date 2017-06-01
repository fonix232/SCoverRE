package com.sec.knox.container.util;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PathTranslator {
    private static final String PACKAGE_DATA_PATH_PREFIX = "/data/user";
    private static final String PACKAGE_DE_DATA_PATH_PREFIX = "/data/user_de";
    private static final String PATH_MNT_EXTSD = "/mnt/extSdCard";
    private static final String PATH_MNT_KNOX = "/mnt/knox/default/emulated";
    private static final String PATH_MNT_RUNTIME = "/mnt/runtime/default/emulated";
    private static final String PATH_MNT_SD = "/mnt/sdcard";
    private static final String PATH_STORAGE_EMULATED = "/storage/emulated";
    private static final String PATH_STORAGE_EMULATED_EXP = "^/storage/emulated/([0-9]+)";
    private static final String PATH_STORAGE_EMULATED_LEGACY = "/storage/emulated/legacy";
    private static final String PATH_STORAGE_EXTSD = "/storage/extSdCard";
    private static final String PATH_STORAGE_SELF_PRIMARY = "/storage/self/primary";
    private static final Map<String, String> mFilePathMap = new HashMap();

    static {
        mFilePathMap.put("^/data/data", "/data/user/?");
        mFilePathMap.put("^/storage/enc_emulated/legacy", "/mnt/shell/enc_emulated/?");
        mFilePathMap.put("^/storage/enc_emulated/([0-9]+)", "/mnt/shell/enc_emulated/?");
        mFilePathMap.put("^/data/clipboard", "/data/clipboard");
        mFilePathMap.put("^/data/user", PACKAGE_DATA_PATH_PREFIX);
        mFilePathMap.put("^/data/user_de", PACKAGE_DE_DATA_PATH_PREFIX);
        mFilePathMap.put("^/data/system/container/", "/data/system/container/");
    }

    public static String getAppLevelPathForMediaScan(String str) {
        String str2 = "";
        Log.d("epmf", "real path=" + str);
        if (str == null || str.length() < 1) {
            return null;
        }
        if (str.startsWith(PATH_MNT_EXTSD) || str.startsWith(PATH_STORAGE_EXTSD)) {
            return str;
        }
        if (str.startsWith(PATH_MNT_KNOX)) {
            str2 = str.replaceFirst(PATH_MNT_KNOX, PATH_STORAGE_EMULATED);
            Log.d("epmf", "appLevelPath=" + str2);
            return str2;
        }
        Log.d("epmf", "pathout=" + str);
        return str;
    }

    public static String getRealPath(String str, int i) {
        String str2 = "";
        Log.d("epmf", "path=" + str + " cid=" + i);
        if (str == null || str.length() < 1) {
            return null;
        }
        if (str.startsWith(PATH_MNT_EXTSD) || str.startsWith(PATH_STORAGE_EXTSD)) {
            return str;
        }
        if (!str.startsWith("/mnt/sdcard") && !str.startsWith(PATH_STORAGE_SELF_PRIMARY) && !str.startsWith(PATH_STORAGE_EMULATED_LEGACY) && !str.startsWith(PATH_STORAGE_EMULATED)) {
            for (Entry entry : mFilePathMap.entrySet()) {
                if (str.matches(((String) entry.getKey()) + ".*")) {
                    str2 = ((String) entry.getValue()).replace("?", String.valueOf(i));
                    str = str.replaceFirst((String) entry.getKey(), str2);
                    Log.d("epmf", "match key=" + ((String) entry.getKey()) + " val=" + ((String) entry.getValue()) + " real=" + str2);
                    break;
                }
            }
        }
        String str3 = (i >= 100 ? PATH_MNT_KNOX : PATH_MNT_RUNTIME) + "/" + String.valueOf(i);
        String str4 = str.startsWith("/mnt/sdcard") ? "/mnt/sdcard" : str.startsWith(PATH_STORAGE_SELF_PRIMARY) ? PATH_STORAGE_SELF_PRIMARY : str.startsWith(PATH_STORAGE_EMULATED_LEGACY) ? PATH_STORAGE_EMULATED_LEGACY : str.matches("^/storage/emulated/([0-9]+).*") ? PATH_STORAGE_EMULATED_EXP : PATH_STORAGE_EMULATED;
        str = str.replaceFirst(str4, str3);
        Log.d("epmf", "match key=" + str4 + " real=" + str3);
        Log.d("epmf", "pathout=" + str);
        return str;
    }

    public static boolean isPackageDataRelatedPath(String str, int i) {
        String realPath = getRealPath(str, i);
        if (realPath == null) {
            return false;
        }
        boolean startsWith = realPath.startsWith(PACKAGE_DATA_PATH_PREFIX);
        boolean startsWith2 = realPath.startsWith(PACKAGE_DE_DATA_PATH_PREFIX);
        if (startsWith || startsWith2) {
            Log.d("epmf", "package path detected: " + realPath);
        } else {
            Log.d("epmf", "not a package path: " + realPath);
        }
        if (startsWith) {
            startsWith2 = true;
        }
        return startsWith2;
    }
}
