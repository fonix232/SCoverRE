package com.samsung.android.app.ledcover.common;

import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadUtils {
    public static final String STR_DEFAULT_URL = "https://www.samsungimaging.com/common/support/firmware/downloadUrlList.do?";
    public static final String STR_URL_PARAM_LOC = "loc=";
    public static final String STR_URL_PARAM_PRD_NAME = "prd_mdl_name=";
    private static final String TAG = "DownloadUtils";

    public static String makeURL(String prodId, String location, String type) {
        return "https://www.samsungimaging.com/common/support/firmware/downloadUrlList.do?prd_mdl_name=" + prodId + type + "&" + STR_URL_PARAM_LOC + location;
    }

    public static Bundle makeErrorInfo(String from, int code) {
        Bundle errorInfo = new Bundle();
        errorInfo.putString("error_from", from);
        errorInfo.putInt("error_code", code);
        return errorInfo;
    }

    public static String convertStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
                sb.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String readFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        byte[] bytes = new byte[((int) file.length())];
        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
            String contents = new String(bytes);
            SLog.m12v(TAG, "Contents of certification file:" + contents);
            return contents;
        } finally {
            in.close();
        }
    }

    public static void set_permission(String name, int mode) {
        try {
            Class.forName("android.os.FileUtils").getMethod("setPermissions", new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE}).invoke(null, new Object[]{name, Integer.valueOf(mode), Integer.valueOf(-1), Integer.valueOf(-1)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean verifyFileSha256(java.io.File r13, java.lang.String r14) {
        /*
        r5 = 0;
        r2 = 0;
        r10 = "SHA-256";
        r5 = java.security.MessageDigest.getInstance(r10);	 Catch:{ NoSuchAlgorithmException -> 0x002c }
        r3 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0036 }
        r3.<init>(r13);	 Catch:{ FileNotFoundException -> 0x0036 }
        r10 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r10];
        r7 = 0;
    L_0x0012:
        r7 = r3.read(r0);	 Catch:{ IOException -> 0x001e }
        r10 = -1;
        if (r7 == r10) goto L_0x0051;
    L_0x0019:
        r10 = 0;
        r5.update(r0, r10, r7);	 Catch:{ IOException -> 0x001e }
        goto L_0x0012;
    L_0x001e:
        r1 = move-exception;
        r10 = "DownloadUtils";
        r11 = "IO Exception while md.update";
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);	 Catch:{ all -> 0x008a }
        r10 = 0;
        r3.close();	 Catch:{ IOException -> 0x0081 }
    L_0x002a:
        r2 = r3;
    L_0x002b:
        return r10;
    L_0x002c:
        r1 = move-exception;
        r10 = "DownloadUtils";
        r11 = "SHA-256 MessageDigest Failed";
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = 0;
        goto L_0x002b;
    L_0x0036:
        r1 = move-exception;
        r10 = "DownloadUtils";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "FileNotFound : ";
        r11 = r11.append(r12);
        r11 = r11.append(r13);
        r11 = r11.toString();
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = 0;
        goto L_0x002b;
    L_0x0051:
        r3.close();	 Catch:{ IOException -> 0x0078 }
    L_0x0054:
        r6 = r5.digest();
        r8 = new java.lang.StringBuffer;
        r8.<init>();
        r4 = 0;
    L_0x005e:
        r10 = r6.length;
        if (r4 >= r10) goto L_0x0098;
    L_0x0061:
        r10 = r6[r4];
        r10 = r10 & 255;
        r10 = r10 + 256;
        r11 = 16;
        r10 = java.lang.Integer.toString(r10, r11);
        r11 = 1;
        r10 = r10.substring(r11);
        r8.append(r10);
        r4 = r4 + 1;
        goto L_0x005e;
    L_0x0078:
        r1 = move-exception;
        r10 = "DownloadUtils";
        r11 = "IO Exception while close FileInputStream";
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        goto L_0x0054;
    L_0x0081:
        r1 = move-exception;
        r11 = "DownloadUtils";
        r12 = "IO Exception while close FileInputStream";
        com.samsung.android.app.ledcover.common.SLog.m12v(r11, r12);
        goto L_0x002a;
    L_0x008a:
        r10 = move-exception;
        r3.close();	 Catch:{ IOException -> 0x008f }
    L_0x008e:
        throw r10;
    L_0x008f:
        r1 = move-exception;
        r11 = "DownloadUtils";
        r12 = "IO Exception while close FileInputStream";
        com.samsung.android.app.ledcover.common.SLog.m12v(r11, r12);
        goto L_0x008e;
    L_0x0098:
        r10 = r8.toString();
        r9 = r10.toUpperCase();
        r10 = "DownloadUtils";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Local ZIP File Hash (SHA256) : ";
        r11 = r11.append(r12);
        r11 = r11.append(r9);
        r11 = r11.toString();
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = "DownloadUtils";
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Server ZIP File Hash (SHA256) : ";
        r11 = r11.append(r12);
        r11 = r11.append(r14);
        r11 = r11.toString();
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = r14.toUpperCase();
        r10 = r10.equals(r9);
        if (r10 == 0) goto L_0x00e5;
    L_0x00da:
        r10 = "DownloadUtils";
        r11 = "ZIP File Hash Verify Success!";
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = 1;
        r2 = r3;
        goto L_0x002b;
    L_0x00e5:
        r10 = "DownloadUtils";
        r11 = "ZIP File Hash Verify Failed!";
        com.samsung.android.app.ledcover.common.SLog.m12v(r10, r11);
        r10 = 0;
        r2 = r3;
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.app.ledcover.common.DownloadUtils.verifyFileSha256(java.io.File, java.lang.String):boolean");
    }
}
