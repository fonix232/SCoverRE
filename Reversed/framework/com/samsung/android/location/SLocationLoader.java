package com.samsung.android.location;

import android.content.Context;
import android.os.IBinder;
import com.samsung.android.location.ISLocationManager.Stub;

public class SLocationLoader {
    private static final String CLASS_SLocationService = "com.samsung.android.location.SLocationService";
    private static final String METHOD_systemReady = "systemReady";
    private static final String TAG = "SLocation";

    private static Class getClassFromLib(Context context, String str) throws Throwable {
        return context.createPackageContext("com.samsung.android.location", 3).getClassLoader().loadClass(str);
    }

    public static Object getSLocationManager(IBinder iBinder) throws Throwable {
        return new SemLocationManager(Stub.asInterface(iBinder));
    }

    public static IBinder getSLocationService(Context context) throws Throwable {
        Class classFromLib = getClassFromLib(context, CLASS_SLocationService);
        if (classFromLib == null) {
            return null;
        }
        return (IBinder) classFromLib.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
    }

    public static void systemReady(Context context, IBinder iBinder) throws Throwable {
        Class classFromLib = getClassFromLib(context, CLASS_SLocationService);
        ISLocationManager asInterface = Stub.asInterface(iBinder);
        if (classFromLib != null) {
            classFromLib.getDeclaredMethod(METHOD_systemReady, new Class[0]).invoke(asInterface, new Object[0]);
        }
    }
}
