package com.samsung.android.sdk.cover;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.samsung.android.cover.ICoverManager.Stub;
import com.samsung.android.sdk.SsdkUnsupportedException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScoverManager {
    public static final int COVER_MODE_HIDE_SVIEW_ONCE = 2;
    public static final int COVER_MODE_NONE = 0;
    public static final int COVER_MODE_SVIEW = 1;
    private static final String FEATURE_COVER_CLEAR = "com.sec.feature.cover.clearcover";
    private static final String FEATURE_COVER_FLIP = "com.sec.feature.cover.flip";
    private static final String FEATURE_COVER_NEON = "com.sec.feature.cover.neoncover";
    private static final String FEATURE_COVER_NFCLED = "com.sec.feature.cover.nfcledcover";
    private static final String FEATURE_COVER_SVIEW = "com.sec.feature.cover.sview";
    static final int SDK_VERSION = 16842752;
    private static final String TAG = "ScoverManager";
    private static boolean sIsClearCoverSystemFeatureEnabled;
    private static boolean sIsFilpCoverSystemFeatureEnabled;
    private static boolean sIsNeonCoverSystemFeatureEnabled;
    private static boolean sIsNfcLedCoverSystemFeatureEnabled;
    private static boolean sIsSViewCoverSystemFeatureEnabled;
    private static boolean sIsSystemFeatureQueried;
    private static int sServiceVersion;
    private Context mContext;
    private final CopyOnWriteArrayList<CoverPowerKeyListenerDelegate> mCoverPowerKeyListenerDelegates;
    private final CopyOnWriteArrayList<CoverStateListenerDelegate> mCoverStateListenerDelegates;
    private final CopyOnWriteArrayList<CoverListenerDelegate> mLcdOffDisableDelegates;
    private final CopyOnWriteArrayList<LedSystemEventListenerDelegate> mLedSystemEventListenerDelegates;
    private final CopyOnWriteArrayList<LegacyLedSystemEventListenerDelegate> mLegacyLedSystemEventListenerDelegates;
    private final CopyOnWriteArrayList<CoverListenerDelegate> mListenerDelegates;
    private final CopyOnWriteArrayList<NfcLedCoverTouchListenerDelegate> mNfcLedCoverTouchListenerDelegates;
    private ICoverManager mService;

    public static class CoverPowerKeyListener {
        private static final int EVENT_TYPE_POWER_KEY = 10;

        public void onPowerKeyPress() {
        }
    }

    public static class CoverStateListener {
        public void onCoverSwitchStateChanged(boolean switchState) {
        }

        public void onCoverAttachStateChanged(boolean attached) {
        }
    }

    public static class LedSystemEventListener {
        private static final int EVENT_TYPE_SYSTEM = 4;

        public void onSystemCoverEvent(int event, Bundle args) {
        }
    }

    public static class NfcLedCoverTouchListener {
        public static final int EVENT_TYPE_ALARM = 1;
        public static final int EVENT_TYPE_CALL = 0;
        public static final int EVENT_TYPE_FACTORY = 5;
        public static final int EVENT_TYPE_SCHEDULE = 3;
        public static final int EVENT_TYPE_TIMER = 2;

        public void onCoverTouchAccept() {
        }

        public void onCoverTouchReject() {
        }

        public void onCoverTapLeft() {
        }

        public void onCoverTapMid() {
        }

        public void onCoverTapRight() {
        }
    }

    @Deprecated
    public interface ScoverStateListener {
        void onCoverStateChanged(ScoverState scoverState);
    }

    public static class StateListener {
        public void onCoverStateChanged(ScoverState state) {
        }
    }

    static {
        sIsSystemFeatureQueried = false;
        sIsFilpCoverSystemFeatureEnabled = false;
        sIsSViewCoverSystemFeatureEnabled = false;
        sIsClearCoverSystemFeatureEnabled = false;
        sIsNfcLedCoverSystemFeatureEnabled = false;
        sIsNeonCoverSystemFeatureEnabled = false;
        sServiceVersion = 16777216; // 0x01000000
    }

    public ScoverManager(Context context) {
        this.mListenerDelegates = new CopyOnWriteArrayList();
        this.mCoverStateListenerDelegates = new CopyOnWriteArrayList();
        this.mNfcLedCoverTouchListenerDelegates = new CopyOnWriteArrayList();
        this.mLedSystemEventListenerDelegates = new CopyOnWriteArrayList();
        this.mCoverPowerKeyListenerDelegates = new CopyOnWriteArrayList();
        this.mLegacyLedSystemEventListenerDelegates = new CopyOnWriteArrayList();
        this.mLcdOffDisableDelegates = new CopyOnWriteArrayList();
        this.mContext = context;
        initSystemFeature();
    }

    private void initSystemFeature() {
        if (!sIsSystemFeatureQueried) {
            sIsFilpCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_FLIP);
            sIsSViewCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_SVIEW);
            sIsNfcLedCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_NFCLED);
            sIsClearCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_CLEAR);
            sIsNeonCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_NEON);
            sIsSystemFeatureQueried = true;
            sServiceVersion = getCoverManagerVersion();
        }
    }

    public boolean isSupportCover() {
        return sIsFilpCoverSystemFeatureEnabled || sIsSViewCoverSystemFeatureEnabled || sIsClearCoverSystemFeatureEnabled || sIsNeonCoverSystemFeatureEnabled;
    }

    boolean isSmartCover() {
        ScoverState coverState = getCoverState();
        if (coverState == null || coverState.type != ScoverState.TYPE_NFC_SMART_COVER) {
            return false;
        }
        return true;
    }

    boolean isSupportFlipCover() {
        return sIsFilpCoverSystemFeatureEnabled;
    }

    boolean isSupportSViewCover() {
        return sIsSViewCoverSystemFeatureEnabled;
    }

    boolean isSupportClearCover() {
        return sIsClearCoverSystemFeatureEnabled;
    }

    boolean isSupportNfcLedCover() {
        return sIsNfcLedCoverSystemFeatureEnabled;
    }

    boolean isSupportNeonCover() {
        return sIsNeonCoverSystemFeatureEnabled;
    }

    boolean isSupportTypeOfCover(int type) {
        switch (type) {
            case COVER_MODE_NONE /*0*/:
                return sIsFilpCoverSystemFeatureEnabled;
            case COVER_MODE_SVIEW /*1*/:
            case ConnectionResult.SERVICE_DISABLED /*3*/:
                return sIsSViewCoverSystemFeatureEnabled;
            case ConnectionResult.INTERNAL_ERROR /*8*/:
                return sIsClearCoverSystemFeatureEnabled;
            case ConnectionResult.LICENSE_CHECK_FAILED /*11*/:
                return sIsNeonCoverSystemFeatureEnabled;
            default:
                return false;
        }
    }

    int getCoverManagerVersion() {
        int serviceVersion = ViewCompat.MEASURED_STATE_TOO_SMALL;
        if (isSupportCover()) {
            try {
                serviceVersion = ((Integer) ICoverManager.class.getMethod("getVersion", new Class[COVER_MODE_NONE]).invoke(getService(), new Object[COVER_MODE_NONE])).intValue();
            } catch (Exception e) {
                Log.w(TAG, "getVersion failed : " + e);
            }
        }
        Log.d(TAG, "serviceVersion : " + serviceVersion);
        return serviceVersion;
    }

    public String getServiceVersionName() {
        int majorVersion = (sServiceVersion >> 24) & ScoverState.TYPE_NFC_SMART_COVER;
        int minorVersion = (sServiceVersion >> 16) & ScoverState.TYPE_NFC_SMART_COVER;
        int revisions = sServiceVersion & SupportMenu.USER_MASK;
        return String.format("%d.%d.%d", new Object[]{Integer.valueOf(majorVersion), Integer.valueOf(minorVersion), Integer.valueOf(revisions)});
    }

    static boolean isSupportableVersion(int supportableVersion) {
        int minorVersion = (supportableVersion >> 16) & ScoverState.TYPE_NFC_SMART_COVER;
        int revisions = supportableVersion & SupportMenu.USER_MASK;
        int serviceMinorVersion = (sServiceVersion >> 16) & ScoverState.TYPE_NFC_SMART_COVER;
        int serviceRevisions = sServiceVersion & SupportMenu.USER_MASK;
        if (((sServiceVersion >> 24) & ScoverState.TYPE_NFC_SMART_COVER) < ((supportableVersion >> 24) & ScoverState.TYPE_NFC_SMART_COVER) || serviceMinorVersion < minorVersion || serviceRevisions < revisions) {
            return false;
        }
        return true;
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

    public void setCoverModeToWindow(Window window, int coverMode) {
        if (isSupportSViewCover()) {
            LayoutParams wlp = window.getAttributes();
            if (wlp != null) {
                wlp.coverMode = coverMode;
                window.setAttributes(wlp);
                return;
            }
            return;
        }
        Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
    }

    @Deprecated
    public void registerListener(ScoverStateListener listener) {
        Log.d(TAG, "registerListener : Use deprecated API!! Change ScoverStateListener to StateListener");
    }

    public void registerListener(StateListener listener) {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "registerListener : If cover is smart cover, it does not need to register listener of intenal App");
        } else if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            CoverListenerDelegate coverListener = null;
            boolean hasDelegate = false;
            Iterator<CoverListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    hasDelegate = true;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverListenerDelegate(listener, null, this.mContext);
            }
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (coverListener != null && cm != null) {
                        svc.registerCallback(coverListener, cm);
                        if (!hasDelegate) {
                            this.mListenerDelegates.add(coverListener);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    public void registerListener(CoverStateListener listener) throws SsdkUnsupportedException {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "registerListener : If cover is smart cover, it does not need to register listener of intenal App");
        } else if (!isSupportableVersion(SDK_VERSION)) {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.1.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            CoverStateListenerDelegate coverListener = null;
            boolean hasDelegate = false;
            Iterator<CoverStateListenerDelegate> i = this.mCoverStateListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverStateListenerDelegate delegate = (CoverStateListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    hasDelegate = true;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverStateListenerDelegate(listener, null, this.mContext);
            }
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (coverListener != null && cm != null) {
                        svc.registerListenerCallback(coverListener, cm, COVER_MODE_HIDE_SVIEW_ONCE);
                        if (!hasDelegate) {
                            this.mCoverStateListenerDelegates.add(coverListener);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    @Deprecated
    public void unregisterListener(ScoverStateListener listener) {
        Log.d(TAG, "unregisterListener : Use deprecated API!! Change ScoverStateListener to StateListener");
    }

    public void unregisterListener(StateListener listener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "unregisterListener : If cover is smart cover, it does not need to unregister listener of intenal App");
        } else if (listener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterCallback(coverListener)) {
                        this.mListenerDelegates.remove(coverListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public void unregisterListener(CoverStateListener listener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "unregisterListener : If cover is smart cover, it does not need to unregister listener of intenal App");
        } else if (!isSupportableVersion(SDK_VERSION)) {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.1.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (listener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            CoverStateListenerDelegate coverListener = null;
            Iterator<CoverStateListenerDelegate> i = this.mCoverStateListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverStateListenerDelegate delegate = (CoverStateListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterCallback(coverListener)) {
                        this.mCoverStateListenerDelegates.remove(coverListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public ScoverState getCoverState() {
        if (isSupportCover()) {
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    CoverState coverState = svc.getCoverState();
                    if (coverState == null) {
                        Log.e(TAG, "getCoverState : coverState is null");
                    } else if (coverState.type == ScoverState.TYPE_NFC_SMART_COVER && !coverState.switchState) {
                        Log.e(TAG, "getCoverState : type of cover is nfc smart cover and cover is closed");
                        return null;
                    } else if (isSupportableVersion(17498112)) {
                        return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model, coverState.fakeCover, coverState.fotaMode);
                    } else {
                        if (isSupportableVersion(17235968)) {
                            return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model, coverState.fakeCover);
                        }
                        if (isSupportableVersion(16908288)) {
                            return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model);
                        }
                        if (isSupportableVersion(SDK_VERSION)) {
                            return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached);
                        }
                        return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in getCoverState: ", e);
            }
            return null;
        }
        Log.w(TAG, "getCoverState : This device is not supported cover");
        return null;
    }

    public boolean checkValidPacakge(String pkg) {
        if (!isSupportCover()) {
            Log.w(TAG, "checkValidPacakge : This device is not supported cover");
            return false;
        } else if (pkg == null) {
            Log.w(TAG, "checkValidPacakge : pkg is null");
            return false;
        } else {
            try {
                ICoverManager svc = getService();
                if (svc == null) {
                    return false;
                }
                CoverState coverState = svc.getCoverState();
                if (coverState == null || !coverState.attached) {
                    Log.e(TAG, "checkValidPacakge : coverState is null or cover is detached");
                    return false;
                }
                String coverAppUri = coverState.getSmartCoverAppUri();
                if (TextUtils.isEmpty(coverAppUri) || !pkg.equals(coverAppUri.substring(COVER_MODE_SVIEW, coverAppUri.length()))) {
                    return false;
                }
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in checkCoverAppUri: ", e);
                return false;
            }
        }
    }

    public void sendDataToCover(int command, byte[] data) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendDataToCover : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "sendDataToCover : If cover is smart cover, it does not need to send the data to cover");
        } else if (isSupportableVersion(16908288)) {
            ICoverManager svc = getService();
            if (svc != null) {
                try {
                    svc.sendDataToCover(command, data);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in sendData : ", e);
                }
            }
        } else {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.2.0", COVER_MODE_HIDE_SVIEW_ONCE);
        }
    }

    public void registerNfcTouchListener(int type, NfcLedCoverTouchListener listener) throws SsdkUnsupportedException {
        if (isSupportCover()) {
            Log.d(TAG, "registerNfcTouchListener");
            if (!isSupportNfcLedCover()) {
                Log.w(TAG, "registerNfcTouchListener : This device does not support NFC Led cover");
                return;
            } else if (!isSupportableVersion(16973824)) {
                throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", COVER_MODE_HIDE_SVIEW_ONCE);
            } else if (listener == null) {
                Log.w(TAG, "registerNfcTouchListener : listener is null");
                return;
            } else {
                NfcLedCoverTouchListenerDelegate nfcTouchListener = null;
                boolean hasDelegate = false;
                Iterator<NfcLedCoverTouchListenerDelegate> i = this.mNfcLedCoverTouchListenerDelegates.iterator();
                while (i.hasNext()) {
                    NfcLedCoverTouchListenerDelegate delegate = (NfcLedCoverTouchListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        nfcTouchListener = delegate;
                        hasDelegate = true;
                        break;
                    }
                }
                if (nfcTouchListener == null) {
                    nfcTouchListener = new NfcLedCoverTouchListenerDelegate(listener, null, this.mContext);
                }
                try {
                    ICoverManager svc = getService();
                    if (svc != null) {
                        ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                        if (nfcTouchListener != null && cm != null) {
                            svc.registerNfcTouchListenerCallback(type, nfcTouchListener, cm);
                            if (!hasDelegate) {
                                this.mNfcLedCoverTouchListenerDelegates.add(nfcTouchListener);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in registerNfcTouchListener: ", e);
                    return;
                }
            }
        }
        Log.w(TAG, "registerNfcTouchListener : This device does not support cover");
    }

    public void unregisterNfcTouchListener(NfcLedCoverTouchListener listener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterNfcTouchListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(16973824)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (listener == null) {
            Log.w(TAG, "unregisterNfcTouchListener : listener is null");
        } else {
            NfcLedCoverTouchListenerDelegate nfcTouchListener = null;
            Iterator<NfcLedCoverTouchListenerDelegate> i = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (i.hasNext()) {
                NfcLedCoverTouchListenerDelegate delegate = (NfcLedCoverTouchListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    nfcTouchListener = delegate;
                    break;
                }
            }
            if (nfcTouchListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterNfcTouchListenerCallback(nfcTouchListener)) {
                        this.mNfcLedCoverTouchListenerDelegates.remove(nfcTouchListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                }
            }
        }
    }

    public void sendDataToNfcLedCover(int command, byte[] data) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendDataToNfcLedCover : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "sendDataToNfcLedCover : This device does not support NFC Led cover");
        } else if (isSupportableVersion(16973824)) {
            ICoverManager svc = getService();
            if (svc != null) {
                try {
                    svc.sendDataToNfcLedCover(command, data);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in sendData to NFC : ", e);
                }
            }
        } else {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", COVER_MODE_HIDE_SVIEW_ONCE);
        }
    }

    public void addLedNotification(Bundle data) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "addLedNotification : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "addLedNotification : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17039360)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.4.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (data == null) {
            Log.e(TAG, "addLedNotification : Null notification data!");
        } else {
            ICoverManager svc = getService();
            if (svc != null) {
                try {
                    svc.addLedNotification(data);
                } catch (RemoteException e) {
                    Log.e(TAG, "addLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void removeLedNotification(Bundle data) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "removeLedNotification : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "removeLedNotification : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17039360)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.4.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (data == null) {
            Log.e(TAG, "removeLedNotification : Null notification data!");
        } else {
            ICoverManager svc = getService();
            if (svc != null) {
                try {
                    svc.removeLedNotification(data);
                } catch (RemoteException e) {
                    Log.e(TAG, "removeLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void sendSystemEvent(Bundle data) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17170432)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.6.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (data == null) {
            Log.e(TAG, "sendSystemEvent : Null system event data!");
        } else {
            ICoverManager svc = getService();
            if (svc != null) {
                try {
                    svc.sendSystemEvent(data);
                } catch (RemoteException e) {
                    Log.e(TAG, "sendSystemEvent in sendData to NFC : ", e);
                }
            }
        }
    }

    public void registerLedSystemListener(LedSystemEventListener listener) throws SsdkUnsupportedException {
        if (isSupportCover()) {
            Log.d(TAG, "registerLedSystemListener");
            if (!isSupportNfcLedCover() && !isSupportNeonCover()) {
                Log.w(TAG, "registerLedSystemListener : This device does not support NFC Led cover or Neon Cover");
                return;
            } else if ((!isSupportNfcLedCover() || !isSupportableVersion(16973824)) && (!isSupportNeonCover() || !isSupportableVersion(17301504))) {
                throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0 for NFC LED Cover and v1.8.0 for Neon cover", COVER_MODE_HIDE_SVIEW_ONCE);
            } else if (listener == null) {
                Log.w(TAG, "registerLedSystemListener : listener is null");
                return;
            } else if (supportNewLedSystemEventListener()) {
                LedSystemEventListenerDelegate ledSystemEventListener = null;
                boolean hasDelegate = false;
                Iterator<LedSystemEventListenerDelegate> i = this.mLedSystemEventListenerDelegates.iterator();
                while (i.hasNext()) {
                    LedSystemEventListenerDelegate delegate = (LedSystemEventListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        ledSystemEventListener = delegate;
                        hasDelegate = true;
                        break;
                    }
                }
                if (ledSystemEventListener == null) {
                    ledSystemEventListener = new LedSystemEventListenerDelegate(listener, null, this.mContext);
                }
                try {
                    ICoverManager svc = getService();
                    if (svc != null) {
                        ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                        if (ledSystemEventListener != null && cm != null) {
                            svc.registerNfcTouchListenerCallback(4, ledSystemEventListener, cm);
                            if (!hasDelegate) {
                                this.mLedSystemEventListenerDelegates.add(ledSystemEventListener);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in registerLedSystemListener: ", e);
                    return;
                }
            } else {
                registerLegacyLedSystemListener(listener);
                return;
            }
        }
        Log.w(TAG, "registerLedSystemListener : This device does not support cover");
    }

    public void unregisterLedSystemEventListener(LedSystemEventListener listener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterLedSystemEventListener");
        if (isSupportCover()) {
            Log.d(TAG, "unregisterLedSystemEventListener");
            if (!isSupportNfcLedCover() && !isSupportNeonCover()) {
                Log.w(TAG, "unregisterLedSystemEventListener : This device does not support NFC Led cover or Neon Cover");
                return;
            } else if ((!isSupportNfcLedCover() || !isSupportableVersion(16973824)) && (!isSupportNeonCover() || !isSupportableVersion(17301504))) {
                throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0 for NFC LED Cover and v1.8.0 for Neon cover", COVER_MODE_HIDE_SVIEW_ONCE);
            } else if (listener == null) {
                Log.w(TAG, "unregisterLedSystemEventListener : listener is null");
                return;
            } else if (supportNewLedSystemEventListener()) {
                LedSystemEventListenerDelegate ledSystemEventListener = null;
                Iterator<LedSystemEventListenerDelegate> i = this.mLedSystemEventListenerDelegates.iterator();
                while (i.hasNext()) {
                    LedSystemEventListenerDelegate delegate = (LedSystemEventListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        ledSystemEventListener = delegate;
                        break;
                    }
                }
                if (ledSystemEventListener != null) {
                    try {
                        ICoverManager svc = getService();
                        if (svc != null && svc.unregisterNfcTouchListenerCallback(ledSystemEventListener)) {
                            this.mLedSystemEventListenerDelegates.remove(ledSystemEventListener);
                            return;
                        }
                        return;
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoteException in unregisterLedSystemEventListener: ", e);
                        return;
                    }
                }
                return;
            } else {
                unregisterLegacyLedSystemEventListener(listener);
                return;
            }
        }
        Log.w(TAG, "unregisterLedSystemEventListener : This device does not support cover");
    }

    public void registerCoverPowerKeyListener(CoverPowerKeyListener listener) throws SsdkUnsupportedException {
        if (isSupportCover()) {
            Log.d(TAG, "registerCoverPowerKeyListener");
            if (!isSupportFlipCover() && !isSupportNeonCover()) {
                Log.w(TAG, "registerLedSystemListener : This device does not support Flip cover or Neon Cover");
                return;
            } else if (!isSupportableVersion(17432576)) {
                throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.10.0 for Flip Cover and Neon cover", COVER_MODE_HIDE_SVIEW_ONCE);
            } else if (listener == null) {
                Log.w(TAG, "registerCoverPowerKeyListener : listener is null");
                return;
            } else {
                CoverPowerKeyListenerDelegate powerKeyEventListener = null;
                boolean hasDelegate = false;
                Iterator<CoverPowerKeyListenerDelegate> i = this.mCoverPowerKeyListenerDelegates.iterator();
                while (i.hasNext()) {
                    CoverPowerKeyListenerDelegate delegate = (CoverPowerKeyListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        powerKeyEventListener = delegate;
                        hasDelegate = true;
                        break;
                    }
                }
                if (powerKeyEventListener == null) {
                    powerKeyEventListener = new CoverPowerKeyListenerDelegate(listener, null, this.mContext);
                }
                try {
                    ICoverManager svc = getService();
                    if (svc != null) {
                        ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                        if (powerKeyEventListener != null && cm != null) {
                            svc.registerNfcTouchListenerCallback(10, powerKeyEventListener, cm);
                            if (!hasDelegate) {
                                this.mCoverPowerKeyListenerDelegates.add(powerKeyEventListener);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in registerCoverPowerKeyListener: ", e);
                    return;
                }
            }
        }
        Log.w(TAG, "registerCoverPowerKeyListener : This device does not support cover");
    }

    public void unregisterCoverPowerKeyListener(CoverPowerKeyListener listener) throws SsdkUnsupportedException {
        if (isSupportCover()) {
            Log.d(TAG, "unregisterCoverPowerKeyListener");
            if (!isSupportFlipCover() && !isSupportNeonCover()) {
                Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support Flip cover or Neon Cover");
                return;
            } else if (!isSupportableVersion(17432576)) {
                throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.1.0 for Flip Cover Neon cover", COVER_MODE_HIDE_SVIEW_ONCE);
            } else if (listener == null) {
                Log.w(TAG, "unregisterCoverPowerKeyListener : listener is null");
                return;
            } else {
                CoverPowerKeyListenerDelegate powerKeyEventListener = null;
                Iterator<CoverPowerKeyListenerDelegate> i = this.mCoverPowerKeyListenerDelegates.iterator();
                while (i.hasNext()) {
                    CoverPowerKeyListenerDelegate delegate = (CoverPowerKeyListenerDelegate) i.next();
                    if (delegate.getListener().equals(listener)) {
                        powerKeyEventListener = delegate;
                        break;
                    }
                }
                if (powerKeyEventListener != null) {
                    try {
                        ICoverManager svc = getService();
                        if (svc != null && svc.unregisterNfcTouchListenerCallback(powerKeyEventListener)) {
                            this.mCoverPowerKeyListenerDelegates.remove(powerKeyEventListener);
                            return;
                        }
                        return;
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoteException in unregisterCoverPowerKeyListener: ", e);
                        return;
                    }
                }
                return;
            }
        }
        Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support cover");
    }

    private static boolean supportNewLedSystemEventListener() throws SsdkUnsupportedException {
        return isSupportableVersion(17104896);
    }

    private void registerLegacyLedSystemListener(LedSystemEventListener listener) throws SsdkUnsupportedException {
        if (listener == null) {
            Log.w(TAG, "registerLegacyLedSystemListener : listener is null");
            return;
        }
        LegacyLedSystemEventListenerDelegate ledSystemEventListener = null;
        Iterator<LegacyLedSystemEventListenerDelegate> i = this.mLegacyLedSystemEventListenerDelegates.iterator();
        while (i.hasNext()) {
            LegacyLedSystemEventListenerDelegate delegate = (LegacyLedSystemEventListenerDelegate) i.next();
            if (delegate.getListener().equals(listener)) {
                ledSystemEventListener = delegate;
                break;
            }
        }
        if (ledSystemEventListener == null) {
            ledSystemEventListener = new LegacyLedSystemEventListenerDelegate(listener, null, this.mContext);
            this.mLegacyLedSystemEventListenerDelegates.add(ledSystemEventListener);
        }
        try {
            ICoverManager svc = getService();
            if (svc != null) {
                ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                if (ledSystemEventListener != null && cm != null) {
                    svc.registerNfcTouchListenerCallback(4, ledSystemEventListener, cm);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerLegacyLedSystemListener: ", e);
        }
    }

    private void unregisterLegacyLedSystemEventListener(LedSystemEventListener listener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterLegacyLedSystemEventListener");
        if (listener == null) {
            Log.w(TAG, "unregisterLegacyLedSystemEventListener : listener is null");
            return;
        }
        LegacyLedSystemEventListenerDelegate ledSystemEventListener = null;
        Iterator<LegacyLedSystemEventListenerDelegate> i = this.mLegacyLedSystemEventListenerDelegates.iterator();
        while (i.hasNext()) {
            LegacyLedSystemEventListenerDelegate delegate = (LegacyLedSystemEventListenerDelegate) i.next();
            if (delegate.getListener().equals(listener)) {
                ledSystemEventListener = delegate;
                break;
            }
        }
        if (ledSystemEventListener != null) {
            try {
                ICoverManager svc = getService();
                if (svc != null && svc.unregisterNfcTouchListenerCallback(ledSystemEventListener)) {
                    this.mLegacyLedSystemEventListenerDelegates.remove(ledSystemEventListener);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterLegacyLedSystemEventListener: ", e);
            }
        }
    }

    public boolean disableLcdOffByCover(StateListener listener) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "disableLcdOffByCover : This device does not support cover");
            return false;
        } else if (!isSupportableVersion(17104896)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.5.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (listener == null) {
            Log.w(TAG, "disableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "disableLcdOffByCover");
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mLcdOffDisableDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverListenerDelegate(listener, null, this.mContext);
            }
            try {
                ICoverManager svc = getService();
                if (svc == null || !svc.disableLcdOffByCover(coverListener, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    return false;
                }
                this.mLcdOffDisableDelegates.add(coverListener);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in disableLcdOffByCover: ", e);
                return false;
            }
        }
    }

    public boolean enableLcdOffByCover(StateListener listener) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "enableLcdOffByCover : This device does not support cover");
            return false;
        } else if (!isSupportableVersion(17104896)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.5.0", COVER_MODE_HIDE_SVIEW_ONCE);
        } else if (listener == null) {
            Log.w(TAG, "enableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "enableLcdOffByCover");
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mLcdOffDisableDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                Log.e(TAG, "enableLcdOffByCover: Matching listener not found, cannot enable");
                return false;
            }
            try {
                ICoverManager svc = getService();
                if (svc == null || !svc.enableLcdOffByCover(coverListener, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    return false;
                }
                this.mLcdOffDisableDelegates.remove(coverListener);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                return false;
            }
        }
    }
}
