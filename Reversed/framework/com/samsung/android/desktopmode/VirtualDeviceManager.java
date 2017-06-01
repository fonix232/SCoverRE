package com.samsung.android.desktopmode;

import android.app.InternalPresentation;
import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.framework.res.C0078R;
import java.util.ArrayList;
import java.util.List;

public class VirtualDeviceManager {
    private static final boolean DEBUG = DesktopModeFeature.DEBUG;
    private static final String TAG = VirtualDeviceManager.class.getSimpleName();
    public static final int TOUCHPAD_DOCK_MODE = 1;
    public static final int TOUCHPAD_IRIS_MODE = 2;
    public static final int TOUCHPAD_NORMAL_MODE = 0;
    private final Context mContext;
    Display mDefaultDisplay = this.mDisplayManager.getDisplay(0);
    private final DisplayManager mDisplayManager = ((DisplayManager) this.mContext.getSystemService("display"));
    private List<VirtualDeviceScreen> mVirtualDeviceList;

    private class VirtualDeviceScreen extends InternalPresentation {
        final Context mContext;
        final int mLayoutId;
        final int mScreenMode;
        private FrameLayout mTouchpadLayout;
        final Window mWindow = getWindow();
        final int mWindowType;

        VirtualDeviceScreen(Context context, int i, int i2, int i3) {
            super(context, VirtualDeviceManager.this.mDefaultDisplay);
            this.mContext = context;
            this.mWindowType = i;
            this.mLayoutId = i2;
            this.mScreenMode = i3;
        }

        protected void onCreate(Bundle bundle) {
            this.mWindow.setType(this.mWindowType);
            this.mWindow.addFlags(9496);
            this.mWindow.clearFlags(65536);
            LayoutParams attributes = this.mWindow.getAttributes();
            attributes.samsungFlags |= 131072;
            setCancelable(false);
            setContentView(this.mLayoutId);
            if (this.mScreenMode == 0) {
                this.mTouchpadLayout = (FrameLayout) findViewById(C0078R.id.touchpad_landscape);
                this.mTouchpadLayout.setVisibility(0);
            } else if (this.mScreenMode == 1) {
                this.mTouchpadLayout = (FrameLayout) findViewById(C0078R.id.touchpad_portrait);
                this.mTouchpadLayout.setVisibility(0);
                attributes.inputFeatures = FingerprintManager.PRIVILEGED_TYPE_KEYGUARD;
                this.mWindow.setAttributes(attributes);
            } else if (this.mScreenMode == 2) {
                this.mTouchpadLayout = (FrameLayout) findViewById(C0078R.id.touchpad_portrait);
                this.mTouchpadLayout.setVisibility(0);
                attributes.inputFeatures = -1073741824;
                this.mWindow.setAttributes(attributes);
                Point point = new Point();
                VirtualDeviceManager.this.mDefaultDisplay.getSize(point);
                this.mWindow.setLayout((int) (((double) point.x) * 0.5d), -1);
                this.mWindow.setGravity(53);
            }
        }

        public String toString() {
            return super.toString() + " mWindowType=" + this.mWindowType + " mLayoutId=" + this.mLayoutId + " isShowing=" + isShowing() + " mScreenMode=" + this.mScreenMode + "\n";
        }
    }

    public VirtualDeviceManager(Context context) {
        this.mContext = context;
    }

    private VirtualDeviceScreen createVirtualDevice(int i, int i2, int i3) {
        if (this.mVirtualDeviceList == null) {
            this.mVirtualDeviceList = new ArrayList();
        } else if (!this.mVirtualDeviceList.isEmpty()) {
            VirtualDeviceScreen findMatchedDevice = findMatchedDevice(i, i2);
            if (findMatchedDevice != null) {
                return findMatchedDevice;
            }
        }
        VirtualDeviceScreen virtualDeviceScreen = new VirtualDeviceScreen(this.mContext, i, i2, i3);
        this.mVirtualDeviceList.add(virtualDeviceScreen);
        return virtualDeviceScreen;
    }

    private VirtualDeviceScreen findMatchedDevice(int i, int i2) {
        if (this.mVirtualDeviceList == null) {
            return null;
        }
        for (VirtualDeviceScreen virtualDeviceScreen : this.mVirtualDeviceList) {
            if (virtualDeviceScreen.mWindowType == i && virtualDeviceScreen.mLayoutId == i2) {
                return virtualDeviceScreen;
            }
        }
        return null;
    }

    public boolean isVirtualDeviceShowing() {
        if (this.mVirtualDeviceList != null) {
            for (VirtualDeviceScreen isShowing : this.mVirtualDeviceList) {
                if (isShowing.isShowing()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeAll() {
        if (DEBUG) {
            Log.d(TAG, "removeAll()");
        }
        if (this.mVirtualDeviceList != null) {
            for (VirtualDeviceScreen dismiss : this.mVirtualDeviceList) {
                dismiss.dismiss();
            }
            this.mVirtualDeviceList.clear();
            this.mVirtualDeviceList = null;
        }
    }

    public void removeVirtualDevice(int i, int i2) {
        VirtualDeviceScreen findMatchedDevice = findMatchedDevice(i, i2);
        if (findMatchedDevice != null) {
            findMatchedDevice.dismiss();
            this.mVirtualDeviceList.remove(findMatchedDevice);
            return;
        }
        Log.d(TAG, "removeVirtualDevice(). Cannot find this device, type=" + i + ", layoutId=" + i2);
    }

    public void showVirtualDevice(int i, int i2, int i3) {
        if (DEBUG) {
            Log.d(TAG, "showVirtualDevice()  type=" + i + ", layoutId=" + i2 + ", mode=" + i3);
        }
        createVirtualDevice(i, i2, i3).show();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(512);
        if (this.mVirtualDeviceList != null) {
            stringBuilder.append("{");
            for (VirtualDeviceScreen append : this.mVirtualDeviceList) {
                stringBuilder.append(append);
            }
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }
}
