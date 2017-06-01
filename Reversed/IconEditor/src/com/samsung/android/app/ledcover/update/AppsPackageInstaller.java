package com.samsung.android.app.ledcover.update;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageInstaller.Session;
import android.content.pm.PackageInstaller.SessionCallback;
import android.content.pm.PackageInstaller.SessionParams;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.gms.auth.api.proxy.ProxyResponse;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.update.ApplicationManager.PackageInstallObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

public class AppsPackageInstaller {
    private static final String INTENT_NAME = "install_complete";
    private static final String TAG = "[LED_COVER]AppsPackageInstaller";
    private Handler handler;
    private PackageInstallObserver installObserver;
    private Vector<IMultiUserInstallerCallback> listeners;
    private Uri mApkPath;
    private Context mContext;
    private PackageInstallerListener mListener;
    private String mPackageName;
    private PackageInstaller packageInstaller;

    /* renamed from: com.samsung.android.app.ledcover.update.AppsPackageInstaller.1 */
    class C02621 implements Runnable {
        final /* synthetic */ boolean val$bSuccess;
        final /* synthetic */ String val$msg;

        C02621(boolean z, String str) {
            this.val$bSuccess = z;
            this.val$msg = str;
        }

        public void run() {
            Iterator it = AppsPackageInstaller.this.listeners.iterator();
            while (it.hasNext()) {
                ((IMultiUserInstallerCallback) it.next()).onResult(this.val$bSuccess, this.val$msg);
            }
            AppsPackageInstaller.this.listeners.clear();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.AppsPackageInstaller.2 */
    class C02632 implements Runnable {
        final /* synthetic */ int val$id;

        C02632(int i) {
            this.val$id = i;
        }

        public void run() {
            AppsPackageInstaller.this.writeSession(this.val$id);
            AppsPackageInstaller.this.commitSession(this.val$id);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.update.AppsPackageInstaller.3 */
    class C02643 extends SessionCallback {
        C02643() {
        }

        public void onCreated(int i) {
        }

        public void onBadgingChanged(int i) {
        }

        public void onActiveChanged(int i, boolean b) {
        }

        public void onProgressChanged(int i, float v) {
        }

        public void onFinished(int i, boolean b) {
        }
    }

    public interface IMultiUserInstallerCallback {
        void onResult(boolean z, String str);
    }

    class PackageInstallerListener extends BroadcastReceiver {
        boolean install;

        PackageInstallerListener() {
            this.install = true;
        }

        void install(boolean value) {
            this.install = value;
        }

        public void onReceive(Context context, Intent intent) {
            SLog.m12v(AppsPackageInstaller.TAG, "onReceive [" + intent + "], install [" + this.install + "]");
            int result = intent.getIntExtra("android.content.pm.extra.STATUS", 1);
            String message = intent.getStringExtra("android.content.pm.extra.STATUS_MESSAGE");
            String packageName = intent.getStringExtra("android.content.pm.extra.PACKAGE_NAME");
            SLog.m12v(AppsPackageInstaller.TAG, "PackageInstallerCallback: result [" + result + "], message [" + message + "], packageName [" + packageName + "]");
            switch (result) {
                case ProxyResponse.STATUS_CODE_NO_CONNECTION /*-1*/:
                    SLog.m12v(AppsPackageInstaller.TAG, "STATUS_PENDING_USER_ACTION");
                    AppsPackageInstaller.this.mContext.startActivity((Intent) intent.getParcelableExtra("android.intent.extra.INTENT"));
                case ConnectionResult.SUCCESS /*0*/:
                    if (this.install) {
                        SLog.m12v(AppsPackageInstaller.TAG, " INSTALL STATUS_SUCCESS");
                        AppsPackageInstaller.this.notifyResult(true, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
                        try {
                            AppsPackageInstaller.this.installObserver.packageInstalled(packageName, 1);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    AppsPackageInstaller.this.destroyReceiver();
                default:
                    if (this.install) {
                        SLog.m12v(AppsPackageInstaller.TAG, " INSTALL failed.");
                        AppsPackageInstaller.this.notifyResult(false, AppsPackageInstaller.this.getInstallErrCode(message));
                        try {
                            AppsPackageInstaller.this.installObserver.packageInstalled(packageName, Integer.valueOf(AppsPackageInstaller.this.getInstallErrCode(message)).intValue());
                        } catch (RemoteException e2) {
                            e2.printStackTrace();
                        }
                    }
                    AppsPackageInstaller.this.destroyReceiver();
            }
        }
    }

    public AppsPackageInstaller(Context context, PackageInstallObserver ob) {
        this.listeners = new Vector();
        this.handler = new Handler();
        this.mContext = context;
        this.packageInstaller = context.getPackageManager().getPackageInstaller();
        this.installObserver = ob;
    }

    public void addListener(IMultiUserInstallerCallback listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IMultiUserInstallerCallback listener) {
        this.listeners.remove(listener);
    }

    private void notifyResult(boolean bSuccess, String msg) {
        this.handler.post(new C02621(bSuccess, msg));
    }

    public void installPackage(String packageName, Uri apkPath) {
        this.mApkPath = apkPath;
        this.mPackageName = packageName;
        execute();
    }

    public void execute() {
        new Thread(new C02632(createSession())).start();
    }

    private int createSession() {
        if (this.mListener != null) {
            destroyReceiver();
        }
        SessionParams params = new SessionParams(1);
        params.setInstallLocation(1);
        params.setAppPackageName(this.mPackageName);
        int sessionId = -1;
        try {
            sessionId = this.packageInstaller.createSession(params);
        } catch (IOException e) {
        } catch (Exception e2) {
        } catch (Error e3) {
        }
        this.packageInstaller.registerSessionCallback(new C02643());
        return sessionId;
    }

    private int writeSession(int sessionId) {
        IOException e;
        Throwable th;
        Exception e2;
        Error e3;
        long sizeBytes = -1;
        String splitName = "Name";
        File file = new File(this.mApkPath.getPath());
        if (file.isFile()) {
            sizeBytes = file.length();
        }
        Session session = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            session = this.packageInstaller.openSession(sessionId);
            InputStream in2 = new FileInputStream(this.mApkPath.getPath());
            try {
                out = session.openWrite("Name", 0, sizeBytes);
                int total = 0;
                byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_CUT];
                while (true) {
                    int c = in2.read(buffer);
                    if (c == -1) {
                        break;
                    }
                    total += c;
                    out.write(buffer, 0, c);
                }
                session.fsync(out);
                in = in2;
            } catch (IOException e4) {
                e = e4;
                in = in2;
                try {
                    e.printStackTrace();
                    notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e5) {
                        } catch (Exception e6) {
                        } catch (Error e7) {
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (session != null) {
                        session.close();
                    }
                    return 0;
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e8) {
                            throw th;
                        } catch (Exception e9) {
                            throw th;
                        } catch (Error e10) {
                            throw th;
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (session != null) {
                        session.close();
                    }
                    throw th;
                }
            } catch (Exception e11) {
                e2 = e11;
                in = in2;
                e2.printStackTrace();
                notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (session != null) {
                    session.close();
                }
                return 0;
            } catch (Error e12) {
                e3 = e12;
                in = in2;
                e3.printStackTrace();
                notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (session != null) {
                    session.close();
                }
                return 0;
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (session != null) {
                    session.close();
                }
                throw th;
            }
        } catch (IOException e13) {
            e = e13;
            e.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (session != null) {
                session.close();
            }
            return 0;
        } catch (Exception e14) {
            e2 = e14;
            e2.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (session != null) {
                session.close();
            }
            return 0;
        } catch (Error e15) {
            e3 = e15;
            e3.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (session != null) {
                session.close();
            }
            return 0;
        }
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        if (session != null) {
            session.close();
        }
        return 0;
    }

    private void commitSession(int sessionId) {
        Session session = null;
        try {
            session = this.packageInstaller.openSession(sessionId);
            PendingIntent sender = PendingIntent.getActivity(this.mContext, 0, new Intent(), 0);
            if (session != null) {
                session.commit(createIntentSender(this.mContext, sessionId));
                getListener().install(true);
                this.mContext.getApplicationContext().registerReceiver(getListener(), new IntentFilter(INTENT_NAME), null, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
        } catch (Exception e2) {
            e2.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
        } catch (Error e3) {
            e3.printStackTrace();
            notifyResult(false, StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION);
        } catch (Throwable th) {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e4) {
                } catch (Error e5) {
                }
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (Exception e6) {
            } catch (Error e7) {
            }
        }
    }

    private IntentSender createIntentSender(Context context, int sessionId) {
        return PendingIntent.getBroadcast(context, sessionId, new Intent(INTENT_NAME), 0).getIntentSender();
    }

    private PackageInstallerListener getListener() {
        if (this.mListener == null) {
            this.mListener = new PackageInstallerListener();
        }
        return this.mListener;
    }

    private String getInstallErrCode(String errorMsg) {
        String[][] LOOKUP_ERROR_CODES = new String[45][];
        LOOKUP_ERROR_CODES[0] = new String[]{"INSTALL_REPLACE_EXISTING", StubCodes.UPDATE_CHECK_UPDATE_AVAILABLE};
        LOOKUP_ERROR_CODES[1] = new String[]{"INSTALL_SUCCEEDED", StubCodes.UPDATE_CHECK_UPDATE_NOT_NECESSARY};
        LOOKUP_ERROR_CODES[2] = new String[]{"INSTALL_FAILED_ALREADY_EXISTS", "-1"};
        LOOKUP_ERROR_CODES[3] = new String[]{"INSTALL_FAILED_INVALID_APK", "-2"};
        LOOKUP_ERROR_CODES[4] = new String[]{"INSTALL_FAILED_INVALID_URI", "-3"};
        LOOKUP_ERROR_CODES[5] = new String[]{"INSTALL_FAILED_INSUFFICIENT_STORAGE", "-4"};
        LOOKUP_ERROR_CODES[6] = new String[]{"INSTALL_FAILED_DUPLICATE_PACKAGE", "-5"};
        LOOKUP_ERROR_CODES[7] = new String[]{"INSTALL_FAILED_NO_SHARED_USER", "-6"};
        LOOKUP_ERROR_CODES[8] = new String[]{"INSTALL_FAILED_UPDATE_INCOMPATIBLE", "-7"};
        LOOKUP_ERROR_CODES[9] = new String[]{"INSTALL_FAILED_SHARED_USER_INCOMPATIBLE", "-8"};
        LOOKUP_ERROR_CODES[10] = new String[]{"INSTALL_FAILED_MISSING_SHARED_LIBRARY", "-9"};
        LOOKUP_ERROR_CODES[11] = new String[]{"INSTALL_FAILED_REPLACE_COULDNT_DELETE", "-10"};
        LOOKUP_ERROR_CODES[12] = new String[]{"INSTALL_FAILED_DEXOPT", "-11"};
        LOOKUP_ERROR_CODES[13] = new String[]{"INSTALL_FAILED_OLDER_SDK", "-12"};
        LOOKUP_ERROR_CODES[14] = new String[]{"INSTALL_FAILED_CONFLICTING_PROVIDER", "-13"};
        LOOKUP_ERROR_CODES[15] = new String[]{"INSTALL_FAILED_NEWER_SDK", "-14"};
        LOOKUP_ERROR_CODES[16] = new String[]{"INSTALL_FAILED_TEST_ONLY", "-15"};
        LOOKUP_ERROR_CODES[17] = new String[]{"INSTALL_FAILED_CPU_ABI_INCOMPATIBLE", "-16"};
        LOOKUP_ERROR_CODES[18] = new String[]{"INSTALL_FAILED_MISSING_FEATURE", "-17"};
        LOOKUP_ERROR_CODES[19] = new String[]{"INSTALL_FAILED_CONTAINER_ERROR", "-18"};
        LOOKUP_ERROR_CODES[20] = new String[]{"INSTALL_FAILED_INVALID_INSTALL_LOCATION", "-19"};
        LOOKUP_ERROR_CODES[21] = new String[]{"INSTALL_FAILED_MEDIA_UNAVAILABLE", "-20"};
        LOOKUP_ERROR_CODES[22] = new String[]{"INSTALL_FAILED_VERIFICATION_TIMEOUT", "-21"};
        LOOKUP_ERROR_CODES[23] = new String[]{"INSTALL_FAILED_VERIFICATION_FAILURE", "-22"};
        LOOKUP_ERROR_CODES[24] = new String[]{"INSTALL_FAILED_PACKAGE_CHANGED", "-23"};
        LOOKUP_ERROR_CODES[25] = new String[]{"INSTALL_FAILED_UID_CHANGED", "-24"};
        LOOKUP_ERROR_CODES[26] = new String[]{"INSTALL_FAILED_VERSION_DOWNGRADE", "-25"};
        LOOKUP_ERROR_CODES[27] = new String[]{"INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE", "-26"};
        LOOKUP_ERROR_CODES[28] = new String[]{"INSTALL_PARSE_FAILED_NOT_APK", "-100"};
        LOOKUP_ERROR_CODES[29] = new String[]{"INSTALL_PARSE_FAILED_BAD_MANIFEST", "-101"};
        LOOKUP_ERROR_CODES[30] = new String[]{"INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION", "-102"};
        LOOKUP_ERROR_CODES[31] = new String[]{"INSTALL_PARSE_FAILED_NO_CERTIFICATES", "-103"};
        LOOKUP_ERROR_CODES[32] = new String[]{"INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES", "-104"};
        LOOKUP_ERROR_CODES[33] = new String[]{"INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING", "-105"};
        LOOKUP_ERROR_CODES[34] = new String[]{"INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME", "-106"};
        LOOKUP_ERROR_CODES[35] = new String[]{"INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID", "-107"};
        LOOKUP_ERROR_CODES[36] = new String[]{"INSTALL_PARSE_FAILED_MANIFEST_MALFORMED", "-108"};
        LOOKUP_ERROR_CODES[37] = new String[]{"INSTALL_PARSE_FAILED_MANIFEST_EMPTY", "-109"};
        LOOKUP_ERROR_CODES[38] = new String[]{"INSTALL_FAILED_INTERNAL_ERROR", "-110"};
        LOOKUP_ERROR_CODES[39] = new String[]{"INSTALL_FAILED_USER_RESTRICTED", "-111"};
        LOOKUP_ERROR_CODES[40] = new String[]{"INSTALL_FAILED_DUPLICATE_PERMISSION", "-112"};
        LOOKUP_ERROR_CODES[41] = new String[]{"INSTALL_FAILED_NO_MATCHING_ABIS", "-113"};
        LOOKUP_ERROR_CODES[42] = new String[]{"NO_NATIVE_LIBRARIES", "-114"};
        LOOKUP_ERROR_CODES[43] = new String[]{"INSTALL_FAILED_ABORTED", "-115"};
        LOOKUP_ERROR_CODES[44] = new String[]{"INSTALL_FAILED_EAS_POLICY_REJECTED_PERMISSION", "-116"};
        int lookupSize = LOOKUP_ERROR_CODES.length;
        String[] str = errorMsg.split(":");
        for (int i = 0; i < lookupSize; i++) {
            if (str[0].equals(LOOKUP_ERROR_CODES[i][0])) {
                return LOOKUP_ERROR_CODES[i][1];
            }
        }
        return StubCodes.UPDATE_CHECK_NO_MATCHING_APPLICATION;
    }

    private void destroyReceiver() {
        if (this.mListener != null) {
            try {
                this.mContext.getApplicationContext().unregisterReceiver(this.mListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mListener = null;
        }
    }
}
