package com.samsung.android.security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

public class CCManager {
    public static final int AUDIT_LOG_ALERT = 1;
    public static final int AUDIT_LOG_CRITICAL = 2;
    public static final int AUDIT_LOG_ERROR = 3;
    public static final int AUDIT_LOG_GROUP_APPLICATION = 5;
    public static final int AUDIT_LOG_GROUP_EVENTS = 4;
    public static final int AUDIT_LOG_GROUP_NETWORK = 3;
    public static final int AUDIT_LOG_GROUP_SECURITY = 1;
    public static final int AUDIT_LOG_GROUP_SYSTEM = 2;
    public static final int AUDIT_LOG_NOTICE = 5;
    public static final int AUDIT_LOG_WARNING = 4;

    static {
        try {
            System.loadLibrary("cc_manager_jni");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Could not link the library. Error: " + e.getMessage());
        }
    }

    public static void AuditLog(String str, boolean z, int i, String str2) {
        try {
            Class cls = Class.forName("android.sec.enterprise.EnterpriseDeviceManager");
            Object[] objArr = new Object[]{Integer.valueOf(i), Integer.valueOf(3), Boolean.valueOf(z), Integer.valueOf(getPid()), str2, str};
            Class.forName("android.sec.enterprise.auditlog.AuditLog").getMethod("logPrivileged", new Class[]{Integer.TYPE, Integer.TYPE, Boolean.TYPE, Integer.TYPE, String.class, String.class}).invoke(null, objArr);
        } catch (ReflectiveOperationException e) {
            System.err.println("CCManager::AuditLog encountered an exception: " + e.getMessage());
        }
    }

    public static String getName() {
        Reader reader;
        Throwable th;
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            BufferedReader bufferedReader2;
            Reader fileReader2 = new FileReader("/proc/" + getPid() + "/cmdline");
            try {
                bufferedReader2 = new BufferedReader(fileReader2);
            } catch (Exception e) {
                reader = fileReader2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        System.err.println("CCManager::getName encountered an exception: " + e2.getMessage());
                        return null;
                    }
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                reader = fileReader2;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e22) {
                        System.err.println("CCManager::getName encountered an exception: " + e22.getMessage());
                        throw th;
                    }
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                throw th;
            }
            try {
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    int read = bufferedReader2.read();
                    if (read <= 0) {
                        break;
                    }
                    stringBuffer.append((char) read);
                }
                String str = new String(stringBuffer);
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e222) {
                        System.err.println("CCManager::getName encountered an exception: " + e222.getMessage());
                    }
                }
                if (fileReader2 != null) {
                    fileReader2.close();
                }
                return str;
            } catch (Exception e3) {
                fileReader = fileReader2;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                reader = fileReader2;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            throw th;
        }
    }

    public static int getPid() {
        try {
            return ((Integer) Class.forName("android.os.Process").getMethod("myPid", (Class[]) null).invoke(null, new Object[0])).intValue();
        } catch (InvocationTargetException e) {
            return -1;
        }
    }

    public static int getUid() {
        try {
            return ((Integer) Class.forName("android.os.Process").getMethod("myUid", (Class[]) null).invoke(null, new Object[0])).intValue();
        } catch (InvocationTargetException e) {
            return -1;
        }
    }

    public static native boolean isMdfDisabled();

    public static native boolean isMdfEnabled();

    public static native boolean isMdfEnforced();

    public static native boolean isMdfReady();

    public static native boolean isMdfSupported();

    public static native int updateMdfStatus();

    public static native String updateMdfVersion();
}
