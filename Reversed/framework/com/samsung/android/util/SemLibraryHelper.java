package com.samsung.android.util;

import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class SemLibraryHelper {
    private static boolean isInitialized = false;
    private static HashMap<String, String> jarFileMap;
    private static HashMap<String, String> jarLibraryMap;
    private static HashMap<String, String> soLibraryMap;
    private static HashMap<String, String> soLibraryVersionMap;

    private SemLibraryHelper() {
    }

    private static int addSupportedLibraries(String[] strArr, int i, Iterator<String> it) {
        int i2 = 0;
        while (it.hasNext()) {
            String str = (String) it.next();
            if (isSupportedLibrary(str)) {
                strArr[i + i2] = str;
                i2++;
            }
        }
        return i2;
    }

    private static boolean checkLibrary(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lib");
        stringBuilder.append((String) soLibraryMap.get(str));
        stringBuilder.append(".so");
        return fileFile("/system/lib/", stringBuilder.toString()) || fileFile("/system/lib64/", stringBuilder.toString()) || fileFile("/system/vendor/lib/", stringBuilder.toString()) || fileFile("/system/vendor/lib64/", stringBuilder.toString());
    }

    private static boolean fileFile(String str, String str2) {
        File[] listFiles = new File(str).listFiles();
        int i = 0;
        while (i < listFiles.length) {
            try {
                File file = listFiles[i];
                if (file.isFile()) {
                    if (file.getName().matches(str2)) {
                        return true;
                    }
                } else if (fileFile(file.getCanonicalPath().toString(), str2)) {
                    return true;
                }
                i++;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    private static String getJarLibraryVersion(String str) {
        Object newInstance;
        String str2 = (String) jarFileMap.get(str);
        if (str2 == null) {
            try {
                newInstance = Class.forName((String) jarLibraryMap.get(str)).newInstance();
            } catch (Throwable th) {
                return null;
            }
        }
        try {
            newInstance = Class.forName((String) jarLibraryMap.get(str), true, new DexClassLoader(new File(str2).getPath(), File.createTempFile("opt", "dex").getAbsolutePath(), null, SemLibraryHelper.class.getClassLoader())).newInstance();
        } catch (Exception e) {
            return null;
        }
        return ((LibraryVersionQuery) newInstance).getLibraryVersion();
    }

    public static String getLibraryVersion(String str) {
        if (!isInitialized) {
            initializeMapData();
        }
        String soLibraryVersion = getSoLibraryVersion(str);
        return soLibraryVersion != null ? soLibraryVersion : getJarLibraryVersion(str);
    }

    private static String getSoLibraryVersion(String str) {
        return !checkLibrary(str) ? null : (String) soLibraryVersionMap.get(str);
    }

    public static String[] getSupportedLibraryList() {
        if (!isInitialized) {
            initializeMapData();
        }
        String[] strArr = new String[(jarLibraryMap.size() + soLibraryMap.size())];
        int addSupportedLibraries = addSupportedLibraries(strArr, 0, jarLibraryMap.keySet().iterator()) + 0;
        addSupportedLibraries += addSupportedLibraries(strArr, addSupportedLibraries, soLibraryMap.keySet().iterator());
        String[] strArr2 = new String[addSupportedLibraries];
        for (int i = 0; i < addSupportedLibraries; i++) {
            strArr2[i] = strArr[i];
        }
        return strArr2;
    }

    private static void initializeMapData() {
        jarLibraryMap = new HashMap();
        jarLibraryMap.put("SmartCropping", "");
        jarLibraryMap.put("SmpsManager", "com.samsung.audio.SmpsManager");
        jarLibraryMap.put("VeSDK", "com.samsung.app.video.editor.external.LibraryVersionQuery");
        jarLibraryMap.put("SPay", "android.spay.LibraryVersionQuery");
        jarLibraryMap.put("TmoWfcUtils", "");
        jarLibraryMap.put("SamsungAndroidDRK", "");
        jarFileMap = new HashMap();
        jarFileMap.put("SmartCropping", null);
        jarFileMap.put("SmpsManager", "/system/framework/SmpsManager.jar");
        jarFileMap.put("VeSDK", "/system/framework/videoeditor_sdk.jar");
        jarFileMap.put("SPay", null);
        jarFileMap.put("TmoWfcUtils", null);
        jarFileMap.put("SamsungAndroidDRK", null);
        soLibraryMap = new HashMap();
        soLibraryMap.put("NativeSecureStorage", "secure_storage");
        soLibraryMap.put("JNISecureStorage", "secure_storage_jni");
        soLibraryMap.put("NativeSaivBarcode", "saiv_barcode");
        soLibraryMap.put("NativeSmartCropping", "smart_cropping");
        soLibraryMap.put("NativeParallelCV", "OpenCv");
        soLibraryMap.put("NativeSOMP", "somp");
        soLibraryMap.put("JNISaivHprFaceCMHSupport", "saiv_HprFace_cmh_support_jni");
        soLibraryMap.put("NativeSaivBeautySolution", "saiv_BeautySolution");
        soLibraryVersionMap = new HashMap();
        soLibraryVersionMap.put("NativeSecureStorage", "1.0.0");
        soLibraryVersionMap.put("JNISecureStorage", "1.0.0");
        soLibraryVersionMap.put("NativeSaivBarcode", "1.0.0");
        soLibraryVersionMap.put("NativeSmartCropping", "2.0.9");
        soLibraryVersionMap.put("NativeParallelCV", "3.0.0");
        soLibraryVersionMap.put("NativeSOMP", "3.1.0");
        soLibraryVersionMap.put("JNISaivHprFaceCMHSupport", "1.0.0");
        soLibraryVersionMap.put("NativeSaivBeautySolution", "1.0.0");
    }

    public static boolean isSupportedLibrary(String str) {
        if (!isInitialized) {
            initializeMapData();
        }
        return getLibraryVersion(str) != null;
    }
}
