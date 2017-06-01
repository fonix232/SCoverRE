package com.samsung.android.app.ledcover.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;

public class NetworkCheckUtils {
    static final String TAG = "NetworkCheckUtils";

    public static boolean isNetworkAvailable(Context context) {
        SLog.m12v(TAG, "isNetworkAvailable");
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static boolean isConnected(Context context, int type) {
        SLog.m12v(TAG, "isConnected type: " + type);
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (VERSION.SDK_INT >= 21) {
            for (Network mNetwork : connManager.getAllNetworks()) {
                NetworkInfo networkInfo = connManager.getNetworkInfo(mNetwork);
                if (networkInfo.getType() == type && networkInfo.getState().equals(State.CONNECTED)) {
                    SLog.m12v(TAG, "Connected");
                    return true;
                }
            }
        } else if (connManager != null) {
            NetworkInfo[] info = connManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getType() == type && anInfo.getState() == State.CONNECTED) {
                        SLog.m12v(TAG, "Connected");
                        return true;
                    }
                }
            }
        }
        SLog.m12v(TAG, "Disconnected");
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        SLog.m12v(TAG, "isWifiConnected");
        return isConnected(context, 1);
    }

    public static boolean isMobileConnected(Context context) {
        SLog.m12v(TAG, "isMobileConnected");
        return isConnected(context, 0);
    }

    public static boolean isRoaming(Context context) {
        SLog.m12v(TAG, "isRoaming");
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isRoaming();
    }

    public static boolean checkNetwork(Context context) {
        if (isWifiConnected(context)) {
            SLog.m12v(TAG, "Wifi is connected");
            return true;
        }
        SLog.m12v(TAG, "Wifi is disconnected");
        if (isMobileConnected(context)) {
            SLog.m12v(TAG, "Mobile data is connected");
            return true;
        } else if (isRoaming(context)) {
            SLog.m12v(TAG, "Network is on roaming");
            return false;
        } else {
            SLog.m12v(TAG, "There is no connection");
            return false;
        }
    }
}
