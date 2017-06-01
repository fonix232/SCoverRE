package com.samsung.android.app.ledcover.update;

import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageInstallObserver.Stub;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.update.AppsPackageInstaller.IMultiUserInstallerCallback;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApplicationManager {
    public static final int INSTALL_FAILED_ABORTED = -115;
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
    public static final int INSTALL_FAILED_DEXOPT = -11;
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
    public static final int INSTALL_FAILED_DUPLICATE_PERMISSION = -112;
    public static final int INSTALL_FAILED_EAS_POLICY_REJECTED_PERMISSION = -116;
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
    public static final int INSTALL_FAILED_INVALID_APK = -2;
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
    public static final int INSTALL_FAILED_INVALID_URI = -3;
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
    public static final int INSTALL_FAILED_NEWER_SDK = -14;
    public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
    public static final int INSTALL_FAILED_OLDER_SDK = -12;
    public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
    public static final int INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE = -26;
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
    public static final int INSTALL_FAILED_TEST_ONLY = -15;
    public static final int INSTALL_FAILED_UID_CHANGED = -24;
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
    public static final int INSTALL_FAILED_USER_RESTRICTED = -111;
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
    public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;
    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;
    public static final int INSTALL_SUCCEEDED = 1;
    public static final int NO_NATIVE_LIBRARIES = -114;
    public static final String TAG = "[LED_COVER]ApplicationManager";
    public final int INSTALL_REPLACE_EXISTING;
    private Handler handler;
    private Context mContext;
    private Method method;
    private PackageInstallObserver observer;
    private OnInstalledPackaged onInstalledPackaged;
    String pkgname;
    private PackageManager pm;
    int returncode;

    /* renamed from: com.samsung.android.app.ledcover.update.ApplicationManager.1 */
    class C02611 extends Handler {
        C02611() {
        }

        public void handleMessage(Message msg) {
            SLog.m12v(ApplicationManager.TAG, "onInstalledPackaged.packageInstalled(" + ApplicationManager.this.pkgname + "," + ApplicationManager.this.returncode + ");");
            ApplicationManager.this.onInstalledPackaged.packageInstalled(ApplicationManager.this.pkgname, ApplicationManager.this.returncode);
        }
    }

    public interface OnInstalledPackaged {
        void packageInstalled(String str, int i);
    }

    /* renamed from: com.samsung.android.app.ledcover.update.ApplicationManager.2 */
    class C04212 implements IMultiUserInstallerCallback {
        C04212() {
        }

        public void onResult(boolean bSuccess, String msg) {
            SLog.m12v(ApplicationManager.TAG, "installPackageSession onResult : " + bSuccess);
        }
    }

    public class PackageInstallObserver extends Stub {
        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            if (ApplicationManager.this.onInstalledPackaged != null) {
                SLog.m12v(ApplicationManager.TAG, "packageInstalled");
                ApplicationManager.this.onInstalledPackaged.packageInstalled(packageName, returnCode);
                ApplicationManager.this.pkgname = packageName;
                ApplicationManager.this.returncode = returnCode;
                ApplicationManager.this.handler.sendMessage(ApplicationManager.this.handler.obtainMessage());
            }
        }
    }

    public ApplicationManager(Context context) throws SecurityException, NoSuchMethodException {
        this.INSTALL_REPLACE_EXISTING = 2;
        this.handler = new C02611();
        this.observer = new PackageInstallObserver();
        this.pm = context.getPackageManager();
        this.mContext = context;
        this.method = this.pm.getClass().getMethod("installPackage", new Class[]{Uri.class, IPackageInstallObserver.class, Integer.TYPE, String.class});
    }

    public void setOnInstalledPackaged(OnInstalledPackaged onInstalledPackaged) {
        this.onInstalledPackaged = onInstalledPackaged;
    }

    public void installPackage(String pkgname, String apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        installPackage(pkgname, new File(apkFile));
    }

    public void installPackage(String pkgname, File apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (apkFile.exists()) {
            Uri packageURI = Uri.fromFile(apkFile);
            if (VERSION.SDK_INT >= 23) {
                installPackageSession(pkgname, packageURI);
                return;
            } else {
                installPackageReflection(packageURI);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public void installPackageSession(String pkgname, Uri apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        SLog.m12v(TAG, "installPackageSession:" + apkFile);
        AppsPackageInstaller installer = new AppsPackageInstaller(this.mContext, this.observer);
        installer.addListener(new C04212());
        installer.installPackage(pkgname, apkFile);
    }

    public void installPackageReflection(Uri apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        SLog.m12v(TAG, "installPackageReflection:" + apkFile);
        this.method.invoke(this.pm, new Object[]{apkFile, this.observer, Integer.valueOf(2), " "});
    }
}
