package com.samsung.android.cover;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.samsung.android.cover.ICoverManager.Stub;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CoverManager {
    public static final int COVER_MODE_HIDE_SVIEW_ONCE = 2;
    public static final int COVER_MODE_NONE = 0;
    public static final int COVER_MODE_SVIEW = 1;
    private static final String TAG = "CoverManager";
    private static boolean sIsFilpCoverSystemFeatureEnabled = false;
    private static boolean sIsNfcLedCoverSystemFeatureEnabled = false;
    private static boolean sIsSViewCoverSystemFeatureEnabled = false;
    private static boolean sIsSystemFeatureQueried = false;
    private Context mContext;
    private final CopyOnWriteArrayList<CoverPowerKeyListenerDelegate> mCoverPowerKeyListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<CoverStateListenerDelegate> mCoverStateListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mLcdOffDisableDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<LedSystemEventListenerDelegate> mLedSystemEventListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<NfcLedCoverTouchListenerDelegate> mNfcLedCoverTouchListenerDelegates = new CopyOnWriteArrayList();
    private ICoverManager mService;
    private IBinder mToken = new Binder();

    public static class StateListener {
        public void onCoverStateChanged(CoverState coverState) {
        }
    }

    public static class CoverPowerKeyListener {
        private static final int EVENT_TYPE_POWER_KEY = 10;

        public void onPowerKeyPress() {
        }
    }

    public static class CoverStateListener {
        public void onCoverAttachStateChanged(boolean z) {
        }

        public void onCoverSwitchStateChanged(boolean z) {
        }
    }

    public static class LedSystemEventListener {
        private static final int EVENT_TYPE_SYSTEM = 4;

        public void onSystemCoverEvent(int i, Bundle bundle) {
        }
    }

    public static class NfcLedCoverTouchListener {
        public void onCoverTouchAccept() {
        }

        public void onCoverTouchReject() {
        }
    }

    public CoverManager(Context context) {
        this.mContext = context;
        initSystemFeature();
    }

    private synchronized ICoverManager getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("cover"));
            if (this.mService == null) {
                Slog.w(TAG, "warning: no COVER_MANAGER_SERVICE");
            }
        }
        return this.mService;
    }

    private void initSystemFeature() {
        if (!sIsSystemFeatureQueried) {
            sIsFilpCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.flip");
            sIsSViewCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.sview");
            sIsNfcLedCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.nfcledcover");
            sIsSystemFeatureQueried = true;
        }
    }

    public void addLedNotification(Bundle bundle) {
        Log.d(TAG, "addLedNotification");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "addLedNotification : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (bundle == null) {
            Log.e(TAG, "addLedNotification : Null notification data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.addLedNotification(bundle);
                } catch (Throwable e) {
                    Log.e(TAG, "addLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void disableCoverManager(boolean z) {
        try {
            ICoverManager service = getService();
            if (service != null) {
                service.disableCoverManager(z, this.mToken, this.mContext.getPackageName());
            }
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException in disalbeCoverManager : ", e);
        }
    }

    public boolean disableLcdOffByCover(StateListener stateListener) {
        if (!isSupportCover()) {
            Log.w(TAG, "disableLcdOffByCover : This device does not support cover");
            return false;
        } else if (stateListener == null) {
            Log.w(TAG, "disableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "disableLcdOffByCover");
            IBinder iBinder = null;
            Iterator it = this.mLcdOffDisableDelegates.iterator();
            while (it.hasNext()) {
                CoverListenerDelegate coverListenerDelegate = (CoverListenerDelegate) it.next();
                if (coverListenerDelegate.getListener().equals(stateListener)) {
                    iBinder = coverListenerDelegate;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverListenerDelegate(stateListener, null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null && service.disableLcdOffByCover(iBinder, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    this.mLcdOffDisableDelegates.add(iBinder);
                    return true;
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
            }
            return false;
        }
    }

    public boolean enableLcdOffByCover(StateListener stateListener) {
        if (!isSupportCover()) {
            Log.w(TAG, "enableLcdOffByCover : This device does not support cover");
            return false;
        } else if (stateListener == null) {
            Log.w(TAG, "enableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "enableLcdOffByCover");
            IBinder iBinder = null;
            Iterator it = this.mLcdOffDisableDelegates.iterator();
            while (it.hasNext()) {
                CoverListenerDelegate coverListenerDelegate = (CoverListenerDelegate) it.next();
                if (coverListenerDelegate.getListener().equals(stateListener)) {
                    iBinder = coverListenerDelegate;
                    break;
                }
            }
            if (iBinder == null) {
                Log.e(TAG, "enableLcdOffByCover: Matching listener not found, cannot enable");
                return false;
            }
            try {
                ICoverManager service = getService();
                if (service != null && service.enableLcdOffByCover(iBinder, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    this.mLcdOffDisableDelegates.remove(iBinder);
                    return true;
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
            }
            return false;
        }
    }

    public CoverState getCoverState() {
        if (!isSupportCover()) {
            Log.w(TAG, "getCoverState : This device is not supported cover");
            return null;
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else {
            try {
                ICoverManager service = getService();
                if (service != null) {
                    CoverState coverState = service.getCoverState();
                    if (coverState != null) {
                        return coverState;
                    }
                    Log.e(TAG, "getCoverState : coverState is null");
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in getCoverState: ", e);
            }
            return null;
        }
    }

    public boolean isCoverManagerDisabled() {
        boolean z = false;
        try {
            ICoverManager service = getService();
            if (service != null) {
                z = service.isCoverManagerDisabled();
            }
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException in isCoverManagerDisabled : ", e);
        }
        return z;
    }

    boolean isSupportCover() {
        return (sIsFilpCoverSystemFeatureEnabled || sIsSViewCoverSystemFeatureEnabled) ? true : sIsNfcLedCoverSystemFeatureEnabled;
    }

    boolean isSupportFlipCover() {
        return sIsFilpCoverSystemFeatureEnabled;
    }

    boolean isSupportNfcLedCover() {
        return sIsNfcLedCoverSystemFeatureEnabled;
    }

    boolean isSupportSViewCover() {
        return sIsSViewCoverSystemFeatureEnabled;
    }

    boolean isSupportTypeOfCover(int i) {
        switch (i) {
            case 0:
                return sIsFilpCoverSystemFeatureEnabled;
            case 1:
            case 3:
            case 6:
                return sIsSViewCoverSystemFeatureEnabled;
            default:
                return false;
        }
    }

    public void registerCoverPowerKeyListener(CoverPowerKeyListener coverPowerKeyListener) {
        if (isSupportCover()) {
            Log.d(TAG, "registerCoverPowerKeyListener");
            if (!isSupportFlipCover()) {
                Log.w(TAG, "registerLedSystemListener : This device does not support Flip cover");
                return;
            } else if (coverPowerKeyListener == null) {
                Log.w(TAG, "registerCoverPowerKeyListener : listener is null");
                return;
            } else {
                IBinder iBinder = null;
                Object obj = null;
                Iterator it = this.mCoverPowerKeyListenerDelegates.iterator();
                while (it.hasNext()) {
                    CoverPowerKeyListenerDelegate coverPowerKeyListenerDelegate = (CoverPowerKeyListenerDelegate) it.next();
                    if (coverPowerKeyListenerDelegate.getListener().equals(coverPowerKeyListener)) {
                        iBinder = coverPowerKeyListenerDelegate;
                        obj = 1;
                        break;
                    }
                }
                if (iBinder == null) {
                    iBinder = new CoverPowerKeyListenerDelegate(coverPowerKeyListener, null, this.mContext);
                }
                try {
                    ICoverManager service = getService();
                    if (service != null) {
                        ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                        if (!(iBinder == null || componentName == null)) {
                            service.registerNfcTouchListenerCallback(10, iBinder, componentName);
                            if (obj == null) {
                                this.mCoverPowerKeyListenerDelegates.add(iBinder);
                            }
                        }
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in registerCoverPowerKeyListener: ", e);
                }
                return;
            }
        }
        Log.w(TAG, "registerCoverPowerKeyListener : This device does not support cover");
    }

    public void registerLedSystemListener(LedSystemEventListener ledSystemEventListener) {
        if (isSupportCover()) {
            Log.d(TAG, "registerLedSystemListener");
            if (!isSupportNfcLedCover()) {
                Log.w(TAG, "registerLedSystemListener : This device does not support NFC Led cover");
                return;
            } else if (ledSystemEventListener == null) {
                Log.w(TAG, "registerLedSystemListener : listener is null");
                return;
            } else {
                IBinder iBinder = null;
                Object obj = null;
                Iterator it = this.mLedSystemEventListenerDelegates.iterator();
                while (it.hasNext()) {
                    LedSystemEventListenerDelegate ledSystemEventListenerDelegate = (LedSystemEventListenerDelegate) it.next();
                    if (ledSystemEventListenerDelegate.getListener().equals(ledSystemEventListener)) {
                        iBinder = ledSystemEventListenerDelegate;
                        obj = 1;
                        break;
                    }
                }
                if (iBinder == null) {
                    iBinder = new LedSystemEventListenerDelegate(ledSystemEventListener, null, this.mContext);
                }
                try {
                    ICoverManager service = getService();
                    if (service != null) {
                        ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                        if (!(iBinder == null || componentName == null)) {
                            service.registerNfcTouchListenerCallback(4, iBinder, componentName);
                            if (obj == null) {
                                this.mLedSystemEventListenerDelegates.add(iBinder);
                            }
                        }
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in registerLedSystemListener: ", e);
                }
                return;
            }
        }
        Log.w(TAG, "registerLedSystemListener : This device does not support cover");
    }

    public void registerListener(CoverStateListener coverStateListener) {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (coverStateListener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            IBinder iBinder = null;
            Object obj = null;
            Iterator it = this.mCoverStateListenerDelegates.iterator();
            while (it.hasNext()) {
                CoverStateListenerDelegate coverStateListenerDelegate = (CoverStateListenerDelegate) it.next();
                if (coverStateListenerDelegate.getListener().equals(coverStateListener)) {
                    iBinder = coverStateListenerDelegate;
                    obj = 1;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverStateListenerDelegate(coverStateListener, null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(iBinder == null || componentName == null)) {
                        service.registerListenerCallback(iBinder, componentName, 2);
                        if (obj == null) {
                            this.mCoverStateListenerDelegates.add(iBinder);
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    public void registerListener(StateListener stateListener) {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (stateListener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            IBinder iBinder = null;
            Object obj = null;
            Iterator it = this.mListenerDelegates.iterator();
            while (it.hasNext()) {
                CoverListenerDelegate coverListenerDelegate = (CoverListenerDelegate) it.next();
                if (coverListenerDelegate.getListener().equals(stateListener)) {
                    iBinder = coverListenerDelegate;
                    obj = 1;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverListenerDelegate(stateListener, null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(iBinder == null || componentName == null)) {
                        service.registerCallback(iBinder, componentName);
                        if (obj == null) {
                            this.mListenerDelegates.add(iBinder);
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    public void registerNfcTouchListener(int i, NfcLedCoverTouchListener nfcLedCoverTouchListener) {
        Log.d(TAG, "registerNfcTouchListener");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "registerNfcTouchListener : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (nfcLedCoverTouchListener == null) {
            Log.w(TAG, "registerNfcTouchListener : listener is null");
        } else {
            IBinder iBinder = null;
            Object obj = null;
            Iterator it = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (it.hasNext()) {
                NfcLedCoverTouchListenerDelegate nfcLedCoverTouchListenerDelegate = (NfcLedCoverTouchListenerDelegate) it.next();
                if (nfcLedCoverTouchListenerDelegate.getListener().equals(nfcLedCoverTouchListener)) {
                    iBinder = nfcLedCoverTouchListenerDelegate;
                    obj = 1;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new NfcLedCoverTouchListenerDelegate(nfcLedCoverTouchListener, null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(iBinder == null || componentName == null)) {
                        service.registerNfcTouchListenerCallback(i, iBinder, componentName);
                        if (obj == null) {
                            this.mNfcLedCoverTouchListenerDelegates.add(iBinder);
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in registerNfcTouchListener: ", e);
            }
        }
    }

    public void removeLedNotification(Bundle bundle) {
        Log.d(TAG, "removeLedNotification");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "removeLedNotification : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (bundle == null) {
            Log.e(TAG, "removeLedNotification : Null notification data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.removeLedNotification(bundle);
                } catch (Throwable e) {
                    Log.e(TAG, "removeLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void sendDataToCover(int i, byte[] bArr) {
        ICoverManager service = getService();
        if (service != null) {
            try {
                service.sendDataToCover(i, bArr);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in sendData : ", e);
            }
        }
    }

    public void sendDataToNfcLedCover(int i, byte[] bArr) {
        ICoverManager service = getService();
        if (service != null) {
            try {
                service.sendDataToNfcLedCover(i, bArr);
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in sendData to NFC : ", e);
            }
        }
    }

    public void sendPowerKeyToCover() {
        ICoverManager service = getService();
        if (service != null) {
            try {
                service.sendPowerKeyToCover();
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException in sendPowerKeyToCover() : ", e);
            }
        }
    }

    public void sendSystemEvent(Bundle bundle) {
        if (!isSupportCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support NFC Led cover");
        } else if (bundle == null) {
            Log.e(TAG, "sendSystemEvent : Null system event data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.sendSystemEvent(bundle);
                } catch (Throwable e) {
                    Log.e(TAG, "sendSystemEvent in sendData to NFC : ", e);
                }
            }
        }
    }

    public void setCoverModeToWindow(Window window, int i) {
        if (!isSupportSViewCover()) {
            Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else {
            LayoutParams attributes = window.getAttributes();
            if (attributes != null) {
                attributes.coverMode = i;
                window.setAttributes(attributes);
            }
        }
    }

    public void unregisterCoverPowerKeyListener(CoverPowerKeyListener coverPowerKeyListener) {
        Log.d(TAG, "unregisterCoverPowerKeyListener");
        if (isSupportCover()) {
            Log.d(TAG, "unregisterCoverPowerKeyListener");
            if (!isSupportFlipCover()) {
                Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support Flip Cover");
                return;
            } else if (coverPowerKeyListener == null) {
                Log.w(TAG, "unregisterCoverPowerKeyListener : listener is null");
                return;
            } else {
                IBinder iBinder = null;
                Iterator it = this.mCoverPowerKeyListenerDelegates.iterator();
                while (it.hasNext()) {
                    CoverPowerKeyListenerDelegate coverPowerKeyListenerDelegate = (CoverPowerKeyListenerDelegate) it.next();
                    if (coverPowerKeyListenerDelegate.getListener().equals(coverPowerKeyListener)) {
                        iBinder = coverPowerKeyListenerDelegate;
                        break;
                    }
                }
                if (iBinder != null) {
                    try {
                        ICoverManager service = getService();
                        if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                            this.mCoverPowerKeyListenerDelegates.remove(iBinder);
                        }
                    } catch (Throwable e) {
                        Log.e(TAG, "RemoteException in unregisterCoverPowerKeyListener: ", e);
                    }
                    return;
                }
                return;
            }
        }
        Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support cover");
    }

    public void unregisterLedSystemEventListener(LedSystemEventListener ledSystemEventListener) {
        Log.d(TAG, "unregisterLedSystemEventListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterLedSystemEventListener : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "unregisterLedSystemEventListener : This device does not support NFC Led cover");
        } else if (ledSystemEventListener == null) {
            Log.w(TAG, "unregisterLedSystemEventListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator it = this.mLedSystemEventListenerDelegates.iterator();
            while (it.hasNext()) {
                LedSystemEventListenerDelegate ledSystemEventListenerDelegate = (LedSystemEventListenerDelegate) it.next();
                if (ledSystemEventListenerDelegate.getListener().equals(ledSystemEventListener)) {
                    iBinder = ledSystemEventListenerDelegate;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                        this.mLedSystemEventListenerDelegates.remove(iBinder);
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in unregisterLedSystemEventListener: ", e);
                }
            }
        }
    }

    public void unregisterListener(CoverStateListener coverStateListener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (coverStateListener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator it = this.mCoverStateListenerDelegates.iterator();
            while (it.hasNext()) {
                CoverStateListenerDelegate coverStateListenerDelegate = (CoverStateListenerDelegate) it.next();
                if (coverStateListenerDelegate.getListener().equals(coverStateListener)) {
                    iBinder = coverStateListenerDelegate;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterCallback(iBinder)) {
                        this.mCoverStateListenerDelegates.remove(iBinder);
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public void unregisterListener(StateListener stateListener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (stateListener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator it = this.mListenerDelegates.iterator();
            while (it.hasNext()) {
                CoverListenerDelegate coverListenerDelegate = (CoverListenerDelegate) it.next();
                if (coverListenerDelegate.getListener().equals(stateListener)) {
                    iBinder = coverListenerDelegate;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterCallback(iBinder)) {
                        this.mListenerDelegates.remove(iBinder);
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public void unregisterNfcTouchListener(NfcLedCoverTouchListener nfcLedCoverTouchListener) {
        Log.d(TAG, "unregisterNfcTouchListener");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (nfcLedCoverTouchListener == null) {
            Log.w(TAG, "unregisterNfcTouchListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator it = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (it.hasNext()) {
                NfcLedCoverTouchListenerDelegate nfcLedCoverTouchListenerDelegate = (NfcLedCoverTouchListenerDelegate) it.next();
                if (nfcLedCoverTouchListenerDelegate.getListener().equals(nfcLedCoverTouchListener)) {
                    iBinder = nfcLedCoverTouchListenerDelegate;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                        this.mNfcLedCoverTouchListenerDelegates.remove(iBinder);
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                }
            }
        }
    }
}
