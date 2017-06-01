package com.samsung.android.app.ledcover.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.update.ApplicationManager.OnInstalledPackaged;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0314c;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class StubUtil {
    public static File APK_DOWNLOAD_PATH = null;
    public static String APK_FILE_PREFIX = null;
    public static String APK_FILE_SUFFIX = null;
    public static final int APK_INSTALL_REQUEST_TO_GALAXYAPPS = 1;
    public static final int APK_INSTALL_REQUEST_TO_PLATFORM = 0;
    public static final String GET_CHINA_URL = "http://cn-ms.samsungapps.com/getCNVasURL.as";
    public static final String GET_DOWNLOAD_URL_URL = "https://vas.samsungapps.com/stub/stubDownload.as";
    public static final String MCC_OF_CHINA = "460";
    public static final String TAG = "[LED_COVER]StubUtil";
    protected static final int TYPE_GET_DOWNLOAD_URL = 2;
    protected static final int TYPE_UPDATE_CHECK = 1;
    public static final String UPDATE_CHECK_URL = "http://vas.samsungapps.com/stub/stubUpdateCheck.as";
    public static final boolean USE_GALAXYAPPS_FOR_UPDATE = false;
    private static Context applicationContext;
    static Dialog detailsDialog;

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.2 */
    static class C02652 implements FilenameFilter {
        C02652() {
        }

        public boolean accept(File dir, String filename) {
            if (filename.startsWith(StubUtil.APK_FILE_PREFIX)) {
                return true;
            }
            return false;
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.3 */
    static class C02663 implements OnClickListener {
        final /* synthetic */ Dialog val$mDialog;

        C02663(Dialog dialog) {
            this.val$mDialog = dialog;
        }

        public void onClick(View v) {
            if (this.val$mDialog != null && this.val$mDialog.isShowing()) {
                this.val$mDialog.dismiss();
            }
            if (this.val$mDialog != null) {
                this.val$mDialog.dismiss();
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.4 */
    static class C02674 implements DialogInterface.OnClickListener {
        C02674() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Utils.sendEventSALog(Defines.SA_SCREEN_UPDATE_SOFTWARE, Defines.SA_UPDATE_SOFTWARE_EVENT_CANCEL, "Cancel");
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_ROOT_ACTIVITY);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.5 */
    static class C02685 implements DialogInterface.OnClickListener {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ StubData val$stubData;

        C02685(StubData stubData, Activity activity) {
            this.val$stubData = stubData;
            this.val$activity = activity;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Utils.sendEventSALog(Defines.SA_SCREEN_UPDATE_SOFTWARE, Defines.SA_UPDATE_SOFTWARE_EVENT_UPDATE, "Update");
            StubUtil.downloadAPK(this.val$stubData.getDownloadURI(), this.val$stubData.getSignature(), (StubListener) this.val$activity);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.6 */
    static class C02696 extends Handler {
        final /* synthetic */ Activity val$activity;

        C02696(Activity activity) {
            this.val$activity = activity;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StubUtil.TYPE_UPDATE_CHECK /*1*/:
                    StubUtil.detailsDialog = StubUtil.showPopupForInstalling(this.val$activity);
                case StubUtil.TYPE_GET_DOWNLOAD_URL /*2*/:
                    if (StubUtil.detailsDialog != null) {
                        StubUtil.detailsDialog.dismiss();
                    }
                default:
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.StubUtil.1 */
    static class C04221 implements OnInstalledPackaged {
        final /* synthetic */ Activity val$activity;

        C04221(Activity activity) {
            this.val$activity = activity;
        }

        public void packageInstalled(String packageName, int returnCode) {
            CharSequence tempMsg;
            SLog.m12v(StubUtil.TAG, "returnCode" + Integer.toString(returnCode));
            Context tempContext = this.val$activity.getApplicationContext();
            if (returnCode == StubUtil.TYPE_UPDATE_CHECK) {
                SLog.m12v(StubUtil.TAG, "Install succeeded");
                String string = tempContext.getString(C0198R.string.update_success);
                Object[] objArr = new Object[StubUtil.TYPE_UPDATE_CHECK];
                objArr[StubUtil.APK_INSTALL_REQUEST_TO_PLATFORM] = tempContext.getString(C0270R.string.app_name);
                tempMsg = String.format(string, objArr);
            } else {
                SLog.m12v(StubUtil.TAG, "Install failed: " + returnCode);
                tempMsg = tempContext.getString(C0198R.string.update_fail);
            }
            Toast.makeText(this.val$activity, tempMsg, StubUtil.TYPE_UPDATE_CHECK).show();
        }
    }

    public static void init(Context context) {
        applicationContext = context;
        APK_DOWNLOAD_PATH = applicationContext.getFilesDir();
        APK_FILE_PREFIX = applicationContext.getPackageName();
        APK_FILE_SUFFIX = ".apk";
    }

    public static void checkUpdate(StubListener listener) {
        Builder builder = Uri.parse(UPDATE_CHECK_URL).buildUpon();
        builder.appendQueryParameter("appId", getPackageName()).appendQueryParameter("versionCode", getVersionCode()).appendQueryParameter(C0314c.f156c, getDeviceId()).appendQueryParameter("mcc", getMcc()).appendQueryParameter("mnc", getMnc()).appendQueryParameter("csc", getCsc()).appendQueryParameter("sdkVer", getSdkVer()).appendQueryParameter("pd", getPd());
        StubRequest request = new StubRequest();
        request.setType(TYPE_UPDATE_CHECK);
        request.setUrl(builder.toString());
        request.setListener(listener);
        request.setIsChina(isChina());
        request.run();
    }

    public static void getDownloadUrl(Context context, StubListener listener) {
        Builder builder = Uri.parse(GET_DOWNLOAD_URL_URL).buildUpon();
        builder.appendQueryParameter("appId", getPackageName()).appendQueryParameter(C0314c.f156c, getDeviceId()).appendQueryParameter("mcc", getMcc()).appendQueryParameter("mnc", getMnc()).appendQueryParameter("csc", getCsc()).appendQueryParameter("sdkVer", getSdkVer()).appendQueryParameter("encImei", getEncImei()).appendQueryParameter("pd", getPd());
        SLog.m12v(TAG, "<<Update>> getDownloadUrl ==> " + builder.toString());
        StubRequest request = new StubRequest();
        request.setType(TYPE_GET_DOWNLOAD_URL);
        request.setUrl(builder.toString());
        request.setIsChina(isChina());
        request.setListener(listener);
        request.run();
    }

    public static void downloadAPK(String url, String signature, StubListener listener) {
        ApkDownload apkDownload = new ApkDownload();
        apkDownload.setUrl(url);
        apkDownload.setSignature(signature);
        apkDownload.setListener(listener);
        apkDownload.run();
    }

    public static String getPackageName() {
        return applicationContext.getPackageName();
    }

    public static String getVersionCode() {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), APK_INSTALL_REQUEST_TO_PLATFORM).versionCode);
        } catch (Throwable ex) {
            log(ex);
            return C0316a.f163d;
        }
    }

    public static String getVersionName() {
        try {
            return String.valueOf(applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), APK_INSTALL_REQUEST_TO_PLATFORM).versionName);
        } catch (Throwable ex) {
            log(ex);
            return C0316a.f163d;
        }
    }

    public static String getDeviceId() {
        return Build.MODEL.replaceFirst("SAMSUNG-", C0316a.f163d);
    }

    public static String getMcc() {
        String operator = ((TelephonyManager) applicationContext.getSystemService("phone")).getSimOperator();
        if (operator == null || operator.length() <= 3) {
            return C0316a.f163d;
        }
        return operator.substring(APK_INSTALL_REQUEST_TO_PLATFORM, 3);
    }

    public static boolean isChina() {
        return MCC_OF_CHINA.equals(getMcc());
    }

    public static String getMnc() {
        String operator = ((TelephonyManager) applicationContext.getSystemService("phone")).getSimOperator();
        if (operator == null || operator.length() <= 3) {
            return C0316a.f163d;
        }
        return operator.substring(3);
    }

    public static String getCsc() {
        Throwable ex;
        Throwable th;
        File file = new File("/system/csc/sales_code.dat");
        InputStream in = null;
        if (!file.exists()) {
            return "WIFI";
        }
        String str = "FAIL";
        try {
            byte[] buffer = new byte[20];
            InputStream in2 = new FileInputStream(file);
            try {
                if (in2.read(buffer) > 0) {
                    str = new String(buffer, APK_INSTALL_REQUEST_TO_PLATFORM, 3);
                }
                close(in2);
                in = in2;
                return str;
            } catch (Exception e) {
                ex = e;
                in = in2;
                try {
                    log(ex);
                    close(in);
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    close(in);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                close(in);
                throw th;
            }
        } catch (Exception e2) {
            ex = e2;
            log(ex);
            close(in);
            return str;
        }
    }

    public static String getSdkVer() {
        return String.valueOf(VERSION.SDK_INT);
    }

    public static String getEncImei() {
        String uniqueId = ((TelephonyManager) applicationContext.getSystemService("phone")).getDeviceId();
        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = Build.MODEL + Build.SERIAL;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(uniqueId.getBytes("iso-8859-1"), APK_INSTALL_REQUEST_TO_PLATFORM, uniqueId.length());
            return Base64.encodeToString(md.digest(), APK_INSTALL_REQUEST_TO_PLATFORM);
        } catch (Throwable ex) {
            log(ex);
            return C0316a.f163d;
        }
    }

    public static String getPd() {
        String PD_TEST_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/go_to_andromeda.test";
        SLog.m12v(TAG, PD_TEST_FILE_PATH);
        if (new File(PD_TEST_FILE_PATH).exists()) {
            return StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY;
        }
        return StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION;
    }

    public static void callGalaxyApps(Activity activity) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("samsungapps://ProductDetail/" + getPackageName()));
        intent.putExtra("type", "cover");
        intent.addFlags(335544352);
        activity.startActivityForResult(intent, TYPE_UPDATE_CHECK);
    }

    public static boolean isNoMatchingApplication(StubData data) {
        return StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION.equals(data.getResultCode());
    }

    public static boolean isUpdateNotNecessary(StubData data) {
        return StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY.equals(data.getResultCode());
    }

    public static boolean isUpdateAvailable(StubData data) {
        return StubCodes.UPDATE_CHECK_UPDATE_AVAILABLE.equals(data.getResultCode());
    }

    public static boolean isDownloadNotAvailable(StubData data) {
        return StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION.equals(data.getResultCode());
    }

    public static boolean isDownloadAvailable(StubData data) {
        return StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY.equals(data.getResultCode());
    }

    public static boolean isError(StubData data) {
        String resultCode = data.getResultCode();
        if (StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION.equals(resultCode) || StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY.equals(resultCode) || StubCodes.UPDATE_CHECK_UPDATE_AVAILABLE.equals(resultCode) || StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION.equals(resultCode) || StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY.equals(resultCode)) {
            return false;
        }
        return true;
    }

    public static void log(String s) {
        Log.d("StubGuideSample", "irin" + s);
    }

    public static void log(Throwable t) {
        Log.e("StubGuideSample", "StubGuideSample got exception", t);
    }

    public static String formatSize(String contentSize) {
        try {
            long size = Long.parseLong(contentSize);
            if (size >= 1048576) {
                return (size / 1048576) + "MB";
            }
            if (size >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                return (size / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "KB";
            }
            return size + "Bytes";
        } catch (NumberFormatException e) {
            return "Unknown size";
        }
    }

    public static boolean validateApkSignature(String apkFilePath, String signatureFromServer) {
        try {
            PackageInfo pi = applicationContext.getPackageManager().getPackageArchiveInfo(apkFilePath, 64);
            if (pi == null) {
                return false;
            }
            X509Certificate signInfo = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(pi.signatures[APK_INSTALL_REQUEST_TO_PLATFORM].toByteArray()));
            if (signInfo == null) {
                return false;
            }
            StringBuffer sigBuffer = new StringBuffer();
            byte[] signature = signInfo.getSignature();
            int length = signature.length;
            for (int i = APK_INSTALL_REQUEST_TO_PLATFORM; i < length; i += TYPE_UPDATE_CHECK) {
                sigBuffer.append(signature[i]);
            }
            if (signatureFromServer.equals(sigBuffer.toString())) {
                return true;
            }
            return false;
        } catch (Throwable ex) {
            log(ex);
            return false;
        }
    }

    public static void onSilenceInstall(String apkFilePath, Activity activity) {
        SLog.m12v(TAG, "onSilenceInstall");
        try {
            ApplicationManager manager = new ApplicationManager(applicationContext);
            manager.installPackage(getPackageName(), apkFilePath);
            manager.setOnInstalledPackaged(new C04221(activity));
        } catch (Exception e) {
            SLog.m12v(TAG, "InstallCommand::silenceInstall::" + e.getMessage());
            Toast.makeText(activity, activity.getApplicationContext().getString(C0198R.string.update_fail), TYPE_UPDATE_CHECK).show();
        }
    }

    public static void removeDownloadedApks() {
        File[] files = APK_DOWNLOAD_PATH.listFiles(new C02652());
        int length = files.length;
        for (int i = APK_INSTALL_REQUEST_TO_PLATFORM; i < length; i += TYPE_UPDATE_CHECK) {
            File file = files[i];
            if (file.delete()) {
                log("remove apk success: " + file.getAbsolutePath());
            } else {
                log("remove apk fail: " + file.getAbsolutePath());
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getChinaURL() {
        /*
        r24 = applicationContext;
        r25 = "StubUtil";
        r26 = 0;
        r24 = r24.getSharedPreferences(r25, r26);
        r25 = "cnVasURL";
        r26 = "";
        r4 = r24.getString(r25, r26);
        r24 = applicationContext;
        r25 = "StubUtil";
        r26 = 0;
        r24 = r24.getSharedPreferences(r25, r26);
        r25 = "cnVasTime";
        r26 = 0;
        r12 = r24.getLong(r25, r26);
        r18 = 0;
        r24 = "";
        r0 = r24;
        r24 = r4.equals(r0);
        if (r24 != 0) goto L_0x0041;
    L_0x0030:
        r24 = java.lang.System.currentTimeMillis();
        r24 = r24 - r12;
        r26 = 86400000; // 0x5265c00 float:7.82218E-36 double:4.2687272E-316;
        r24 = (r24 > r26 ? 1 : (r24 == r26 ? 0 : -1));
        if (r24 > 0) goto L_0x0041;
    L_0x003d:
        log(r4);
    L_0x0040:
        return r4;
    L_0x0041:
        r5 = 0;
        r4 = "";
        r22 = new java.net.URL;	 Catch:{ Exception -> 0x00b5 }
        r24 = "http://cn-ms.samsungapps.com/getCNVasURL.as";
        r0 = r22;
        r1 = r24;
        r0.<init>(r1);	 Catch:{ Exception -> 0x00b5 }
        r24 = "http://cn-ms.samsungapps.com/getCNVasURL.as";
        log(r24);	 Catch:{ Exception -> 0x00b5 }
        r24 = r22.openConnection();	 Catch:{ Exception -> 0x00b5 }
        r0 = r24;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00b5 }
        r5 = r0;
        r24 = 1;
        r0 = r24;
        r5.setInstanceFollowRedirects(r0);	 Catch:{ Exception -> 0x00b5 }
        r17 = r5.getRequestProperties();	 Catch:{ Exception -> 0x00b5 }
        r24 = r17.keySet();	 Catch:{ Exception -> 0x00b5 }
        r25 = r24.iterator();	 Catch:{ Exception -> 0x00b5 }
    L_0x0070:
        r24 = r25.hasNext();	 Catch:{ Exception -> 0x00b5 }
        if (r24 == 0) goto L_0x00eb;
    L_0x0076:
        r11 = r25.next();	 Catch:{ Exception -> 0x00b5 }
        r11 = (java.lang.String) r11;	 Catch:{ Exception -> 0x00b5 }
        r24 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r24.<init>();	 Catch:{ Exception -> 0x00b5 }
        r0 = r24;
        r24 = r0.append(r11);	 Catch:{ Exception -> 0x00b5 }
        r26 = ": ";
        r0 = r24;
        r1 = r26;
        r26 = r0.append(r1);	 Catch:{ Exception -> 0x00b5 }
        r0 = r17;
        r24 = r0.get(r11);	 Catch:{ Exception -> 0x00b5 }
        r24 = (java.util.List) r24;	 Catch:{ Exception -> 0x00b5 }
        r27 = 0;
        r0 = r24;
        r1 = r27;
        r24 = r0.get(r1);	 Catch:{ Exception -> 0x00b5 }
        r24 = (java.lang.String) r24;	 Catch:{ Exception -> 0x00b5 }
        r0 = r26;
        r1 = r24;
        r24 = r0.append(r1);	 Catch:{ Exception -> 0x00b5 }
        r24 = r24.toString();	 Catch:{ Exception -> 0x00b5 }
        log(r24);	 Catch:{ Exception -> 0x00b5 }
        goto L_0x0070;
    L_0x00b5:
        r8 = move-exception;
    L_0x00b6:
        log(r8);	 Catch:{ all -> 0x0120 }
        r24 = applicationContext;
        r25 = "StubUtil";
        r26 = 0;
        r16 = r24.getSharedPreferences(r25, r26);
        r6 = r16.edit();
        r24 = "cnVasURL";
        r0 = r24;
        r6.putString(r0, r4);
        r24 = "cnVasTime";
        r26 = java.lang.System.currentTimeMillis();
        r0 = r24;
        r1 = r26;
        r6.putLong(r0, r1);
        r6.apply();
        log(r4);
        if (r5 == 0) goto L_0x00e6;
    L_0x00e3:
        r5.disconnect();
    L_0x00e6:
        close(r18);
        goto L_0x0040;
    L_0x00eb:
        r24 = "\n";
        log(r24);	 Catch:{ Exception -> 0x00b5 }
        r10 = r5.getHeaderFields();	 Catch:{ Exception -> 0x00b5 }
        r24 = r10.keySet();	 Catch:{ Exception -> 0x00b5 }
        r25 = r24.iterator();	 Catch:{ Exception -> 0x00b5 }
    L_0x00fc:
        r24 = r25.hasNext();	 Catch:{ Exception -> 0x00b5 }
        if (r24 == 0) goto L_0x0189;
    L_0x0102:
        r11 = r25.next();	 Catch:{ Exception -> 0x00b5 }
        r11 = (java.lang.String) r11;	 Catch:{ Exception -> 0x00b5 }
        if (r11 != 0) goto L_0x0152;
    L_0x010a:
        r24 = r10.get(r11);	 Catch:{ Exception -> 0x00b5 }
        r24 = (java.util.List) r24;	 Catch:{ Exception -> 0x00b5 }
        r26 = 0;
        r0 = r24;
        r1 = r26;
        r24 = r0.get(r1);	 Catch:{ Exception -> 0x00b5 }
        r24 = (java.lang.String) r24;	 Catch:{ Exception -> 0x00b5 }
        log(r24);	 Catch:{ Exception -> 0x00b5 }
        goto L_0x00fc;
    L_0x0120:
        r24 = move-exception;
    L_0x0121:
        r25 = applicationContext;
        r26 = "StubUtil";
        r27 = 0;
        r16 = r25.getSharedPreferences(r26, r27);
        r6 = r16.edit();
        r25 = "cnVasURL";
        r0 = r25;
        r6.putString(r0, r4);
        r25 = "cnVasTime";
        r26 = java.lang.System.currentTimeMillis();
        r0 = r25;
        r1 = r26;
        r6.putLong(r0, r1);
        r6.apply();
        log(r4);
        if (r5 == 0) goto L_0x014e;
    L_0x014b:
        r5.disconnect();
    L_0x014e:
        close(r18);
        throw r24;
    L_0x0152:
        r24 = r10.get(r11);	 Catch:{ Exception -> 0x00b5 }
        r24 = (java.util.List) r24;	 Catch:{ Exception -> 0x00b5 }
        r24 = r24.iterator();	 Catch:{ Exception -> 0x00b5 }
    L_0x015c:
        r26 = r24.hasNext();	 Catch:{ Exception -> 0x00b5 }
        if (r26 == 0) goto L_0x00fc;
    L_0x0162:
        r23 = r24.next();	 Catch:{ Exception -> 0x00b5 }
        r23 = (java.lang.String) r23;	 Catch:{ Exception -> 0x00b5 }
        r26 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r26.<init>();	 Catch:{ Exception -> 0x00b5 }
        r0 = r26;
        r26 = r0.append(r11);	 Catch:{ Exception -> 0x00b5 }
        r27 = ": ";
        r26 = r26.append(r27);	 Catch:{ Exception -> 0x00b5 }
        r0 = r26;
        r1 = r23;
        r26 = r0.append(r1);	 Catch:{ Exception -> 0x00b5 }
        r26 = r26.toString();	 Catch:{ Exception -> 0x00b5 }
        log(r26);	 Catch:{ Exception -> 0x00b5 }
        goto L_0x015c;
    L_0x0189:
        r24 = "\n";
        log(r24);	 Catch:{ Exception -> 0x00b5 }
        r24 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r25 = r5.getResponseCode();	 Catch:{ Exception -> 0x00b5 }
        r0 = r24;
        r1 = r25;
        if (r0 == r1) goto L_0x01c3;
    L_0x019a:
        r24 = new java.io.IOException;	 Catch:{ Exception -> 0x00b5 }
        r25 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b5 }
        r25.<init>();	 Catch:{ Exception -> 0x00b5 }
        r26 = "status code ";
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x00b5 }
        r26 = r5.getResponseCode();	 Catch:{ Exception -> 0x00b5 }
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x00b5 }
        r26 = " != ";
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x00b5 }
        r26 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x00b5 }
        r25 = r25.toString();	 Catch:{ Exception -> 0x00b5 }
        r24.<init>(r25);	 Catch:{ Exception -> 0x00b5 }
        throw r24;	 Catch:{ Exception -> 0x00b5 }
    L_0x01c3:
        r20 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x00b5 }
        r20.<init>();	 Catch:{ Exception -> 0x00b5 }
        r19 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x00b5 }
        r24 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x00b5 }
        r25 = r5.getInputStream();	 Catch:{ Exception -> 0x00b5 }
        r24.<init>(r25);	 Catch:{ Exception -> 0x00b5 }
        r0 = r19;
        r1 = r24;
        r0.<init>(r1);	 Catch:{ Exception -> 0x00b5 }
    L_0x01da:
        r14 = r19.readLine();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        if (r14 == 0) goto L_0x01ee;
    L_0x01e0:
        log(r14);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r0 = r20;
        r0.append(r14);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        goto L_0x01da;
    L_0x01e9:
        r8 = move-exception;
        r18 = r19;
        goto L_0x00b6;
    L_0x01ee:
        r24 = "\n";
        log(r24);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r9 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r15 = r9.newPullParser();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r24 = new java.io.StringReader;	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r25 = r20.toString();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r24.<init>(r25);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r0 = r24;
        r15.setInput(r0);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r7 = r15.getEventType();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
    L_0x020d:
        r24 = 1;
        r0 = r24;
        if (r7 == r0) goto L_0x0230;
    L_0x0213:
        switch(r7) {
            case 2: goto L_0x021b;
            default: goto L_0x0216;
        };	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
    L_0x0216:
        r7 = r15.next();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        goto L_0x020d;
    L_0x021b:
        r21 = r15.getName();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        r24 = "serverURL";
        r0 = r21;
        r1 = r24;
        r24 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        if (r24 == 0) goto L_0x0216;
    L_0x022b:
        r4 = r15.nextText();	 Catch:{ Exception -> 0x01e9, all -> 0x0264 }
        goto L_0x0216;
    L_0x0230:
        r24 = applicationContext;
        r25 = "StubUtil";
        r26 = 0;
        r16 = r24.getSharedPreferences(r25, r26);
        r6 = r16.edit();
        r24 = "cnVasURL";
        r0 = r24;
        r6.putString(r0, r4);
        r24 = "cnVasTime";
        r26 = java.lang.System.currentTimeMillis();
        r0 = r24;
        r1 = r26;
        r6.putLong(r0, r1);
        r6.apply();
        log(r4);
        if (r5 == 0) goto L_0x025d;
    L_0x025a:
        r5.disconnect();
    L_0x025d:
        close(r19);
        r18 = r19;
        goto L_0x0040;
    L_0x0264:
        r24 = move-exception;
        r18 = r19;
        goto L_0x0121;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.app.ledcover.update.StubUtil.getChinaURL():java.lang.String");
    }

    public static boolean isNetworkAvailable(Activity activity) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) activity.getSystemService("connectivity")).getActiveNetworkInfo();
        boolean checkNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        SLog.m12v(TAG, "<update> isNetworkAvailable() =>" + checkNetwork);
        return checkNetwork;
    }

    public static void showPopupForNoNetwork(Activity activity) {
        SLog.m12v(TAG, "<update> showPopupForNoNetwork()");
        String titleString = activity.getApplicationContext().getString(C0198R.string.no_network_connection);
        String msgSrting = activity.getApplicationContext().getString(C0198R.string.wifi_not_available_msg);
        try {
            if (detailsDialog != null) {
                detailsDialog.dismiss();
            }
            if (!activity.isFinishing()) {
                Dialog mDialog = new Dialog(activity);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.getWindow().requestFeature(TYPE_UPDATE_CHECK);
                mDialog.setContentView(C0198R.layout.update_custom_dialog_no_network);
                ((TextView) mDialog.findViewById(C0198R.id.dialog_latest_title)).setText(titleString);
                ((TextView) mDialog.findViewById(C0198R.id.dialog_latest_message)).setText(msgSrting);
                TextView okButton = (TextView) mDialog.findViewById(C0198R.id.dialog_latest_okBtn);
                TextView cancelButton = (TextView) mDialog.findViewById(C0198R.id.dialog_latest_cancelBtn);
                okButton.setVisibility(APK_INSTALL_REQUEST_TO_PLATFORM);
                LayoutParams params = okButton.getLayoutParams();
                okButton.setGravity(17);
                params.width = 280;
                params.height = 144;
                okButton.setLayoutParams(params);
                if (cancelButton != null) {
                    cancelButton.setVisibility(8);
                }
                okButton.setOnClickListener(new C02663(mDialog));
                mDialog.show();
                detailsDialog = mDialog;
            }
        } catch (Exception e) {
            Log.d("Popup", e.toString());
            e.printStackTrace();
        }
    }

    public static Dialog showPopupForDownloading(Activity activity, StubData stubData) {
        try {
            if (detailsDialog != null) {
                detailsDialog.dismiss();
            }
            if (!activity.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false).setTitle(activity.getResources().getString(C0198R.string.update_title)).setMessage(activity.getResources().getString(C0198R.string.update_pop_msg)).setPositiveButton(activity.getResources().getString(C0198R.string.update), new C02685(stubData, activity)).setNegativeButton(activity.getResources().getString(C0198R.string.cancel), new C02674());
                AlertDialog dialog = builder.create();
                dialog.show();
                detailsDialog = dialog;
            }
        } catch (Exception e) {
            SLog.m12v(TAG, "initiateProgressPopupWindow()- error - " + e.toString());
            e.printStackTrace();
        }
        return detailsDialog;
    }

    private static Dialog showPopupForInstalling(Activity activity) {
        SLog.m12v(TAG, "<update> showPopupForDownload()");
        try {
            if (detailsDialog != null) {
                detailsDialog.dismiss();
            }
            if (!activity.isFinishing()) {
                Dialog detailsDialogtemp = new Dialog(activity);
                String title = activity.getApplicationContext().getString(C0198R.string.update_title);
                String message = activity.getApplicationContext().getString(C0198R.string.update_msg) + "...";
                detailsDialogtemp.setCanceledOnTouchOutside(false);
                detailsDialogtemp.setCancelable(false);
                detailsDialogtemp.getWindow().requestFeature(TYPE_UPDATE_CHECK);
                detailsDialogtemp.setContentView(C0198R.layout.update_custom_dialog_progress);
                ((TextView) detailsDialogtemp.findViewById(C0198R.id.title_apk)).setText(title);
                ((TextView) detailsDialogtemp.findViewById(C0198R.id.message_apk)).setText(message);
                detailsDialogtemp.show();
                detailsDialog = detailsDialogtemp;
                SLog.m12v(TAG, "so initiateProgressPopupWindow() is ok");
            }
        } catch (Exception e) {
            SLog.m12v(TAG, "initiateProgressPopupWindow()- error - " + e.toString());
            e.printStackTrace();
        }
        return detailsDialog;
    }

    public static void showPopupForConnectServer(Activity activity) {
        SLog.m12v(TAG, "<update> showPopupForGearConnect()");
        try {
            if (detailsDialog != null) {
                detailsDialog.dismiss();
            }
            if (!activity.isFinishing()) {
                Dialog mDialog = new Dialog(activity);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.getWindow().requestFeature(TYPE_UPDATE_CHECK);
                mDialog.setContentView(C0198R.layout.update_custom_popup_connect);
                ((TextView) mDialog.findViewById(C0198R.id.message)).setText(activity.getString(C0198R.string.setting_apk_download_prepare));
                ((TextView) mDialog.findViewById(C0270R.id.title)).setText(activity.getString(C0198R.string.update_title));
                mDialog.show();
                detailsDialog = mDialog;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Handler createHandler(Activity activity) {
        return new C02696(activity);
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
