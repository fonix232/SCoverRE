package com.samsung.android.view;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import java.util.List;

public class SemWindowManager {
    public static final int MAX_ASPECT_RATIO_FIXED_ON = 2;
    public static final int MAX_ASPECT_RATIO_OFF = 0;
    public static final int MAX_ASPECT_RATIO_ON = 1;
    private static final String TAG = "SemWindowManager";
    private static SemWindowManager sInstance;
    private IWindowManager mWindowManager = Stub.asInterface(ServiceManager.getService("window"));

    public static class VisibleWindowInfo implements Parcelable {
        public static final Creator<VisibleWindowInfo> CREATOR = new C02611();
        public boolean focused;
        public boolean lastFocused;
        public String name;
        public String packageName;
        public int type;

        static class C02611 implements Creator<VisibleWindowInfo> {
            C02611() {
            }

            public VisibleWindowInfo createFromParcel(Parcel parcel) {
                return new VisibleWindowInfo(parcel);
            }

            public VisibleWindowInfo[] newArray(int i) {
                return new VisibleWindowInfo[i];
            }
        }

        private VisibleWindowInfo(Parcel parcel) {
            readFromParcel(parcel);
        }

        public int describeContents() {
            return 0;
        }

        public void readFromParcel(Parcel parcel) {
            boolean z = true;
            this.packageName = parcel.readString();
            this.name = parcel.readString();
            this.type = parcel.readInt();
            this.focused = parcel.readInt() != 0;
            if (parcel.readInt() == 0) {
                z = false;
            }
            this.lastFocused = z;
        }

        public void writeToParcel(Parcel parcel, int i) {
            int i2 = 1;
            parcel.writeString(this.packageName);
            parcel.writeString(this.name);
            parcel.writeInt(this.type);
            parcel.writeInt(this.focused ? 1 : 0);
            if (!this.lastFocused) {
                i2 = 0;
            }
            parcel.writeInt(i2);
        }
    }

    private SemWindowManager() {
    }

    public static synchronized SemWindowManager getInstance() {
        SemWindowManager semWindowManager;
        synchronized (SemWindowManager.class) {
            if (sInstance == null) {
                sInstance = new SemWindowManager();
            }
            semWindowManager = sInstance;
        }
        return semWindowManager;
    }

    public void clearForcedDisplaySizeDensity() {
        try {
            this.mWindowManager.clearForcedDisplaySizeDensity(0);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to clearForcedDisplaySizeDensity", e);
        }
    }

    public int getInitialDensity() {
        try {
            return this.mWindowManager.getInitialDisplayDensity(0);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to getDefaultDisplayDensity", e);
            return -1;
        }
    }

    public void getInitialDisplaySize(Point point) {
        try {
            this.mWindowManager.getInitialDisplaySize(0, point);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to getDefaultDisplayDensity", e);
        }
    }

    public int getUserDensity() {
        try {
            return this.mWindowManager.getDefaultDisplayDensity(0);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to getDefaultDisplayDensity", e);
            return -1;
        }
    }

    public void getUserDisplaySize(Point point) {
        try {
            this.mWindowManager.getDefaultDisplaySize(point);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to getDefaultDisplayDensity", e);
        }
    }

    public List<VisibleWindowInfo> getVisibleWindowInfo() {
        try {
            return this.mWindowManager.getVisibleWindowInfo();
        } catch (Throwable e) {
            Log.e(TAG, "Failed to getVisibleWindowInfo", e);
            return null;
        }
    }

    public boolean isMaxAspectPackage(String str, int i) {
        try {
            return this.mWindowManager.isMaxAspectPackage(str, i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplayRotation", e);
            return false;
        }
    }

    public int isMaxAspectPackageEx(String str, int i) {
        try {
            return this.mWindowManager.isMaxAspectPackageEx(str, i);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplayRotation", e);
            return 0;
        }
    }

    public boolean isProcessKillforMaxAspect(String str) {
        try {
            return this.mWindowManager.isProcessKillforMaxAspect(str);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplayRotation", e);
            return false;
        }
    }

    public boolean isSystemKeyEventRequested(int i, ComponentName componentName) {
        try {
            return this.mWindowManager.isSystemKeyEventRequested(i, componentName);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to is systemkeyevent", e);
            return false;
        }
    }

    public void requestMetaKeyEvent(ComponentName componentName, boolean z) {
        try {
            this.mWindowManager.requestMetaKeyEvent(componentName, z);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to request metakeyevent", e);
        }
    }

    public boolean requestSystemKeyEvent(int i, ComponentName componentName, boolean z) {
        try {
            return this.mWindowManager.requestSystemKeyEvent(i, componentName, z);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to request systemkeyevent", e);
            return false;
        }
    }

    public void setForcedDisplayRotation(int i, int i2) {
        try {
            this.mWindowManager.setForcedDisplayRotation(i, i2);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplayRotation", e);
        }
    }

    public void setForcedDisplaySizeDensity(int i, int i2, int i3) {
        try {
            this.mWindowManager.setForcedDisplaySizeDensityExt(0, i, i2, i3, false, false);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplaySizeDensity", e);
        }
    }

    public void setMaxAspectPackage(String str, int i, boolean z, boolean z2) {
        try {
            this.mWindowManager.setMaxAspectPackageEx(str, i, z, z2);
        } catch (Throwable e) {
            Log.e(TAG, "Failed to setForcedDisplayRotation", e);
        }
    }

    public void setStartingWindowContentView(ComponentName componentName, int i) {
        if (componentName == null) {
            Log.e(TAG, "componentName is null in setStartingWindowContentView");
            return;
        }
        try {
            PackageInfo packageInfo = AppGlobals.getPackageManager().getPackageInfo(componentName.getPackageName(), 0, UserHandle.getCallingUserId());
            Log.w(TAG, "setStartingWindowContentView : packageInfo=" + packageInfo);
            if (packageInfo != null) {
                try {
                    this.mWindowManager.setStartingWindowContentView(componentName.getPackageName(), i);
                } catch (Throwable e) {
                    Log.e(TAG, "Failed to set StartingWindowContentView", e);
                }
            }
        } catch (Throwable e2) {
            Log.w(TAG, "setStartingWindowContentView error =" + e2.fillInStackTrace());
        }
    }
}
