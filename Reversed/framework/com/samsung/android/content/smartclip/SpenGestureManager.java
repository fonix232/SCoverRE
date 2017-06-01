package com.samsung.android.content.smartclip;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ServiceManager;
import android.view.InputEvent;
import com.samsung.android.content.smartclip.ISpenGestureService.Stub;
import com.samsung.android.util.SemLog;
import java.util.ArrayList;

public class SpenGestureManager {
    private static String TAG = "SpenGestureManager";
    private Context mContext = null;
    private ISpenGestureService mService = null;

    public SpenGestureManager(Context context) {
        this.mContext = context;
        getService();
    }

    private synchronized ISpenGestureService getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("spengestureservice"));
            if (this.mService == null) {
                SemLog.w("SpenGestureManager", "warning: no SpenGestureManager");
            }
        }
        return this.mService;
    }

    public Bundle getScrollableAreaInfo(Rect rect, IBinder iBinder) {
        try {
            ISpenGestureService service = getService();
            return service != null ? service.getScrollableAreaInfo(rect, iBinder) : null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Bundle getScrollableViewInfo(Rect rect, int i, IBinder iBinder) {
        try {
            ISpenGestureService service = getService();
            return service != null ? service.getScrollableViewInfo(rect, i, iBinder) : null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public SemSmartClipDataRepository getSmartClipDataByScreenRect(Rect rect, IBinder iBinder, int i) {
        try {
            ISpenGestureService service = getService();
            return service != null ? service.getSmartClipDataByScreenRect(rect, iBinder, i) : null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void injectInputEvent(int i, int i2, ArrayList<InputEvent> arrayList, boolean z, IBinder iBinder) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.injectInputEvent(i, i2, (InputEvent[]) arrayList.toArray(new InputEvent[arrayList.size()]), z, iBinder);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean isServiceAvailable() {
        if (Stub.asInterface(ServiceManager.getService("spengestureservice")) != null) {
            return true;
        }
        SemLog.w(TAG, "isServiceAvailable : Service not available");
        return false;
    }

    public void registerHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.registerHoverListener(iSpenGestureHoverListener);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult smartClipRemoteRequestResult) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.sendSmartClipRemoteRequestResult(smartClipRemoteRequestResult);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setHoverStayDetectEnabled(boolean z) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.setHoverStayDetectEnabled(z);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void setHoverStayValues(int i, int i2, int i3) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.setHoverStayValues(i, i2, i3);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void unregisterHoverListener(ISpenGestureHoverListener iSpenGestureHoverListener) {
        try {
            ISpenGestureService service = getService();
            if (service != null) {
                service.unregisterHoverListener(iSpenGestureHoverListener);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
