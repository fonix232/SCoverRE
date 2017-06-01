package com.samsung.android.content.clipboard;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.sec.clipboard.ClipboardConverter;
import android.sec.clipboard.IClipboardDataPasteEvent.Stub;
import android.sec.clipboard.IClipboardService;
import android.sec.clipboard.IClipboardWorkingFormUiInterface;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.util.FileHelper;
import android.sec.clipboard.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.android.internal.C0717R;
import com.samsung.android.cocktailbar.SemCocktailBarManager;
import com.samsung.android.content.clipboard.data.SemClipData;
import com.samsung.android.content.clipboard.data.SemHtmlClipData;
import com.samsung.android.content.clipboard.data.SemImageClipData;
import com.samsung.android.content.clipboard.data.SemTextClipData;
import com.samsung.android.content.clipboard.data.SemUriClipData;
import com.samsung.android.emergencymode.SemEmergencyManager;
import com.samsung.android.knox.SemPersonaManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SemClipboardManager {
    public static final String ACTION_ADD_CLIP = "com.samsung.android.content.clipboard.action.ADD_CLIP";
    public static final String ACTION_CLIPBOARD_CLOSED = "com.samsung.android.content.clipboard.action.CLIPBOARD_CLOSED";
    public static final String ACTION_CLIPBOARD_OPENED = "com.samsung.android.content.clipboard.action.CLIPBOARD_OPENED";
    public static final String ACTION_DISMISS_CLIPBOARD = "com.samsung.android.content.clipboard.action.DISMISS_CLIPBOARD";
    public static final String ACTION_INTRODUCE_EDGE = "com.samsung.android.content.clipboard.action.INTRODUCE_EDGE";
    public static final String ACTION_REMOVE_CLIP = "com.samsung.android.content.clipboard.action.REMOVE_CLIP";
    public static final int CLIPBOARD_TYPE_FILTER = 255;
    public static final String EXTRA_DARK_THEME = "darkTheme";
    public static final String EXTRA_EXTRA_PATH = "extra_path";
    public static final String EXTRA_NO_TOAST = "noToast";
    public static final String EXTRA_PATH = "path";
    public static final String EXTRA_TYPE = "type";
    private static final String TAG = "SemClipboardManager";
    private static IClipboardService sService = null;
    private final int FAIL_SET_DATA = 1;
    private final String KEY_DATA = "data";
    private final String KEY_FILTER = "filter";
    private final String KEY_USERID = "user_id";
    private final int PROTECTED_DATA_MAX = 3;
    private final int SUCCESS_AND_SAVE_BITMAP = 2;
    private final int SUCCESS_SET_DATA = 0;
    private Stub mClipboardPasteEvent = new C10203();
    private SemCocktailBarManager mCocktailBarManager = null;
    private Context mContext = null;
    private Handler mHandler;
    private boolean mIsFiltered = false;
    private boolean mIsMaximumSize = false;
    private final IOnClipboardEventListener.Stub mOnClipboardEventServiceListener = new C10181();
    private ArrayList<SemClipboardEventListener> mOnClipboardEventServiceListeners = new ArrayList();
    private final IOnUserChangedListener.Stub mOnUserChangedListener = new C10192();
    private ArrayList<OnUserChangedListener> mOnUserChangedListeners = new ArrayList();
    private OnPasteListener mPasteListener = null;
    private SemPersonaManager mPersonaManager = null;
    private IClipboardWorkingFormUiInterface mRegInterface = null;
    private Handler mSetDataHandler = null;
    private int mTypeId = 0;

    public interface OnPasteListener {
        void onPaste(SemClipData semClipData);
    }

    class C10181 extends IOnClipboardEventListener.Stub {
        C10181() {
        }

        public void onClipboardEvent(int i, SemClipData semClipData) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("data", semClipData);
            message.what = i;
            message.setData(bundle);
            SemClipboardManager.this.mHandler.sendMessage(message);
        }

        public void onUpdateFilter(int i) {
            Message message = new Message();
            BaseBundle bundle = new Bundle();
            bundle.putInt("filter", i);
            message.what = 5;
            message.setData(bundle);
            SemClipboardManager.this.mHandler.sendMessage(message);
        }
    }

    class C10192 extends IOnUserChangedListener.Stub {
        C10192() {
        }

        public void onUserChanged(int i) {
            Message message = new Message();
            BaseBundle bundle = new Bundle();
            bundle.putInt("user_id", i);
            message.what = 6;
            message.setData(bundle);
            SemClipboardManager.this.mHandler.sendMessage(message);
        }
    }

    class C10203 extends Stub {
        C10203() {
        }

        public void onPaste(SemClipData semClipData) throws RemoteException {
            if (SemClipboardManager.this.mPasteListener != null) {
                SemClipboardManager.this.mPasteListener.onPaste(semClipData);
            } else {
                Log.secD(SemClipboardManager.TAG, "mPasteListener is null");
            }
        }
    }

    class C10225 extends Handler {
        C10225() {
        }

        public void handleMessage(Message message) {
            AccessibilityManager accessibilityManager = null;
            if (SemClipboardManager.this.mContext != null) {
                accessibilityManager = (AccessibilityManager) SemClipboardManager.this.mContext.getSystemService("accessibility");
            }
            switch (message.what) {
                case 0:
                    if (SemClipboardManager.this.mContext != null) {
                        if (!(accessibilityManager == null || accessibilityManager.isTwoFingerGestureRecognitionEnabled())) {
                            if (SemClipboardManager.this.mIsMaximumSize) {
                                Toast.makeText(SemClipboardManager.this.mContext, (int) C0717R.string.clipboard_copied_to_clipboard_maximum_exceeded, 0).show();
                                SemClipboardManager.this.mIsMaximumSize = false;
                            } else {
                                Toast.makeText(SemClipboardManager.this.mContext, (int) C0717R.string.clipboard_copied_to_clipboard, 0).show();
                            }
                        }
                        Log.m3e(SemClipboardManager.TAG, "success set data ");
                        break;
                    }
                    break;
                case 1:
                    if (SemClipboardManager.this.mContext != null) {
                        if (SemClipboardManager.this.isClipboardAllowedToUse(SemClipboardManager.this.getPersonaId())) {
                            if (!(accessibilityManager == null || accessibilityManager.isTwoFingerGestureRecognitionEnabled())) {
                                Toast.makeText(SemClipboardManager.this.mContext, (int) C0717R.string.tw_clipboard_already_exists, 0).show();
                            }
                            Log.m3e(SemClipboardManager.TAG, "Fail set data ");
                            break;
                        }
                        Toast.makeText(SemClipboardManager.this.mContext, (int) C0717R.string.clipboard_restrict, 0).show();
                        return;
                    }
                    break;
                case 3:
                    if (SemClipboardManager.this.mContext != null) {
                        Toast.makeText(SemClipboardManager.this.mContext, String.format(SemClipboardManager.this.mContext.getString(C0717R.string.clipboard_exceed_msg), new Object[]{Integer.valueOf(10)}), 0).show();
                        break;
                    }
                    break;
            }
        }
    }

    public class ClipboardEvent {
        public static final int CLIPS_CLEARED = 3;
        public static final int CLIP_ADDED = 1;
        public static final int CLIP_REMOVED = 2;
        public static final int CLIP_UPDATED = 4;
        @Deprecated
        public static final int FILTER_UPDATED = 5;
        public static final int USER_CHANGED = 6;

        private ClipboardEvent() {
        }
    }

    public interface OnAddClipResultListener {

        public static class Error {
            public static final int REASON_DUPLICATED = 2;
            public static final int REASON_EMPTY_DATA = 3;
            public static final int REASON_NOT_ALLOWED_TO_USE = 4;
            public static final int REASON_UNKNOWN = 1;

            private Error() {
            }
        }

        void onFailure(int i);

        void onSuccess();
    }

    public interface OnUserChangedListener {
        void onUserChanged(int i);
    }

    public static class Type {
        public static final int ALL = -1;
        public static final int HTML = 4;
        public static final int IMAGE = 2;
        public static final int INTENT = 8;
        public static final int NONE = 0;
        public static final int TEXT = 1;
        public static final int URI = 16;
        public static final int URI_LIST = 32;

        private Type() {
        }
    }

    public SemClipboardManager(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = new Handler(this.mContext.getMainLooper()) {
            public void handleMessage(Message message) {
                SemClipboardManager.this.notifyEvent(message);
            }
        };
        this.mPersonaManager = (SemPersonaManager) this.mContext.getSystemService("persona");
        if (!ClipboardConstants.HAS_KNOX_FEATURE) {
            Log.secD(TAG, "no knox");
        }
    }

    private int add(SemClipData semClipData, boolean z, boolean z2, boolean z3, int i) {
        int i2 = 1;
        try {
            Log.secV(TAG, "result : " + z + ", " + z2 + ", " + z3);
            if (semClipData == null) {
                Log.secE(TAG, "addData - clipdata is null!");
            } else if (makeFileDescriptor(semClipData)) {
                int clipType = semClipData.getClipType();
                i2 = z3 ? this.mContext == null ? 1 : getService().setClipDataToSem(clipType, semClipData, this.mContext.getOpPackageName(), i) : z2 ? this.mContext == null ? 1 : getService().setClipData(clipType, semClipData, this.mContext.getOpPackageName(), i) : getService().setClipDataFromOriginal(clipType, semClipData);
                sendResult(z, i2);
                semClipData.closeParcelFileDescriptor();
            } else {
                Log.secE(TAG, "failed making file descriptor!");
                sendResult(z, 1);
                return 1;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return i2;
    }

    private int addData(Context context, SemClipData semClipData, boolean z, boolean z2, boolean z3) {
        if (!isEnabled("addData")) {
            return 1;
        }
        this.mContext = context;
        if (getService() == null || semClipData == null) {
            Log.secW(TAG, "addData - Fail~ Service is null, " + semClipData);
            return 1;
        }
        if (semClipData.getClipType() == 4) {
            SemHtmlClipData semHtmlClipData = (SemHtmlClipData) semClipData.getAlternateClipData(4);
            if (semHtmlClipData == null || semHtmlClipData.getPlainText() == null || semHtmlClipData.getPlainText().length() < 131072) {
                this.mIsMaximumSize = false;
            } else {
                this.mIsMaximumSize = true;
            }
        } else if (semClipData.getClipType() == 1) {
            SemTextClipData semTextClipData = (SemTextClipData) semClipData.getAlternateClipData(1);
            if (semTextClipData == null || semTextClipData.getText() == null || semTextClipData.getText().length() < 131072) {
                this.mIsMaximumSize = false;
            } else {
                this.mIsMaximumSize = true;
            }
        } else {
            this.mIsMaximumSize = false;
        }
        if (this.mSetDataHandler == null) {
            this.mSetDataHandler = new C10225();
        }
        return add(semClipData, z, z2, z3, getPersonaId());
    }

    private static IClipboardService getService() {
        if (sService != null) {
            return sService;
        }
        sService = IClipboardService.Stub.asInterface(ServiceManager.getService("semclipboard"));
        if (sService == null && ClipboardConstants.DEBUG) {
            Log.secE(TAG, "Failed to get semclipboard service.");
        }
        return sService;
    }

    private int getUserId() {
        int userId = UserHandle.getUserId(Binder.getCallingUid());
        if (!SemPersonaManager.isBBCContainer(userId)) {
            return userId;
        }
        Log.secD(TAG, "getUserId is BBC");
        return 0;
    }

    private boolean isCocktailBarDisplayed() {
        if (this.mCocktailBarManager == null) {
            this.mCocktailBarManager = (SemCocktailBarManager) this.mContext.getSystemService("CocktailBarService");
        }
        return this.mCocktailBarManager == null ? false : this.mCocktailBarManager.getCocktailBarWindowType() == 1 ? false : this.mCocktailBarManager.getCocktailBarWindowType() == 2;
    }

    private boolean isEmergencyMode() {
        SemEmergencyManager instance = SemEmergencyManager.getInstance(this.mContext);
        if (instance == null) {
            return false;
        }
        switch (instance.getModeType()) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    private boolean isEnabled(String str) {
        if (isEnabled()) {
            return true;
        }
        Log.secD(TAG, "not enabled! from " + str);
        return false;
    }

    private boolean isUPSMode() {
        SemEmergencyManager instance = SemEmergencyManager.getInstance(this.mContext);
        if (!SemEmergencyManager.isUltraPowerSavingModeSupported() || instance == null) {
            return false;
        }
        switch (instance.getModeType()) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    private boolean makeFileDescriptor(SemClipData semClipData) {
        FileHelper instance = FileHelper.getInstance();
        String bitmapPath;
        File file;
        switch (semClipData.getClipType()) {
            case 2:
                SemImageClipData semImageClipData = (SemImageClipData) semClipData;
                bitmapPath = semImageClipData.getBitmapPath();
                if (bitmapPath == null || bitmapPath.length() <= 0) {
                    Log.secD(TAG, "no bitmap file");
                } else {
                    file = new File(bitmapPath);
                    if (instance.checkFile(file)) {
                        try {
                            semImageClipData.setParcelFileDescriptor(ParcelFileDescriptor.open(file, 939524096));
                        } catch (Throwable e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    Log.secD(TAG, "it's not file. : " + file.getAbsolutePath());
                    return false;
                }
                if (!semImageClipData.HasExtraData()) {
                    Log.secD(TAG, "no extra bitmap file");
                    break;
                }
                String extraDataPath = semImageClipData.getExtraDataPath();
                if (extraDataPath != null && extraDataPath.length() > 0) {
                    file = new File(extraDataPath);
                    if (instance.checkFile(file)) {
                        try {
                            semImageClipData.setExtraParcelFileDescriptor(ParcelFileDescriptor.open(file, 939524096));
                            break;
                        } catch (Throwable e2) {
                            e2.printStackTrace();
                            return false;
                        }
                    }
                    Log.secD(TAG, "it's not file. : " + file.getAbsolutePath());
                    return false;
                }
                break;
            case 4:
                SemHtmlClipData semHtmlClipData = (SemHtmlClipData) semClipData;
                bitmapPath = semHtmlClipData.getThumbnailImagePath();
                if ((bitmapPath == null || (bitmapPath != null && bitmapPath.length() <= 0)) && instance.setFirstImagePathFromHtmlData(semHtmlClipData)) {
                    bitmapPath = semHtmlClipData.getThumbnailImagePath();
                }
                if (bitmapPath == null || bitmapPath.length() <= 0) {
                    Log.secD(TAG, "no first image file");
                    break;
                }
                file = new File(bitmapPath);
                if (instance.checkFile(file)) {
                    try {
                        semHtmlClipData.setParcelFileDescriptor(ParcelFileDescriptor.open(file, 939524096));
                        break;
                    } catch (Throwable e22) {
                        e22.printStackTrace();
                        return false;
                    }
                }
                Log.secD(TAG, "it's not file. : " + file.getAbsolutePath());
                return false;
                break;
            case 16:
                SemUriClipData semUriClipData = (SemUriClipData) semClipData;
                bitmapPath = semUriClipData.getThumbnailPath();
                if ((bitmapPath == null || (bitmapPath != null && bitmapPath.length() <= 0)) && instance.setThumbnailImagePathFromUriData(semUriClipData)) {
                    bitmapPath = semUriClipData.getThumbnailPath();
                }
                if (bitmapPath == null || bitmapPath.length() <= 0) {
                    Log.secD(TAG, "no preview image file");
                    break;
                }
                file = new File(bitmapPath);
                if (instance.checkFile(file)) {
                    try {
                        semUriClipData.setParcelFileDescriptor(ParcelFileDescriptor.open(file, 939524096));
                        break;
                    } catch (Throwable e222) {
                        e222.printStackTrace();
                        return false;
                    }
                }
                Log.secD(TAG, "it's not file. : " + file.getAbsolutePath());
                return false;
                break;
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void notifyEvent(android.os.Message r10) {
        /*
        r9 = this;
        r1 = 0;
        r7 = r10.what;
        switch(r7) {
            case 1: goto L_0x0007;
            case 2: goto L_0x0007;
            case 3: goto L_0x0007;
            case 4: goto L_0x0007;
            case 5: goto L_0x003e;
            case 6: goto L_0x0071;
            default: goto L_0x0006;
        };
    L_0x0006:
        return;
    L_0x0007:
        r2 = 0;
        r1 = r10.getData();
        if (r1 == 0) goto L_0x0017;
    L_0x000e:
        r7 = "data";
        r2 = r1.getParcelable(r7);
        r2 = (com.samsung.android.content.clipboard.data.SemClipData) r2;
    L_0x0017:
        r8 = r9.mOnClipboardEventServiceListeners;
        monitor-enter(r8);
        r7 = r9.mOnClipboardEventServiceListeners;	 Catch:{ all -> 0x003b }
        r0 = r7.size();	 Catch:{ all -> 0x003b }
        if (r0 > 0) goto L_0x0024;
    L_0x0022:
        monitor-exit(r8);
        return;
    L_0x0024:
        r7 = r9.mOnClipboardEventServiceListeners;	 Catch:{ all -> 0x003b }
        r5 = r7.toArray();	 Catch:{ all -> 0x003b }
        monitor-exit(r8);
        r4 = 0;
    L_0x002c:
        r7 = r5.length;
        if (r4 >= r7) goto L_0x0006;
    L_0x002f:
        r7 = r5[r4];
        r7 = (com.samsung.android.content.clipboard.SemClipboardEventListener) r7;
        r8 = r10.what;
        r7.onClipboardUpdated(r8, r2);
        r4 = r4 + 1;
        goto L_0x002c;
    L_0x003b:
        r7 = move-exception;
        monitor-exit(r8);
        throw r7;
    L_0x003e:
        r3 = 0;
        r1 = r10.getData();
        if (r1 == 0) goto L_0x004c;
    L_0x0045:
        r7 = "filter";
        r3 = r1.getInt(r7);
    L_0x004c:
        r8 = r9.mOnClipboardEventServiceListeners;
        monitor-enter(r8);
        r7 = r9.mOnClipboardEventServiceListeners;	 Catch:{ all -> 0x006e }
        r0 = r7.size();	 Catch:{ all -> 0x006e }
        if (r0 > 0) goto L_0x0059;
    L_0x0057:
        monitor-exit(r8);
        return;
    L_0x0059:
        r7 = r9.mOnClipboardEventServiceListeners;	 Catch:{ all -> 0x006e }
        r5 = r7.toArray();	 Catch:{ all -> 0x006e }
        monitor-exit(r8);
        r4 = 0;
    L_0x0061:
        r7 = r5.length;
        if (r4 >= r7) goto L_0x0006;
    L_0x0064:
        r7 = r5[r4];
        r7 = (com.samsung.android.content.clipboard.SemClipboardEventListener) r7;
        r7.onFilterUpdated(r3);
        r4 = r4 + 1;
        goto L_0x0061;
    L_0x006e:
        r7 = move-exception;
        monitor-exit(r8);
        throw r7;
    L_0x0071:
        r6 = 0;
        r1 = r10.getData();
        if (r1 == 0) goto L_0x007f;
    L_0x0078:
        r7 = "user_id";
        r6 = r1.getInt(r7);
    L_0x007f:
        r8 = r9.mOnUserChangedListeners;
        monitor-enter(r8);
        r7 = r9.mOnUserChangedListeners;	 Catch:{ all -> 0x00a1 }
        r0 = r7.size();	 Catch:{ all -> 0x00a1 }
        if (r0 > 0) goto L_0x008c;
    L_0x008a:
        monitor-exit(r8);
        return;
    L_0x008c:
        r7 = r9.mOnUserChangedListeners;	 Catch:{ all -> 0x00a1 }
        r5 = r7.toArray();	 Catch:{ all -> 0x00a1 }
        monitor-exit(r8);
        r4 = 0;
    L_0x0094:
        r7 = r5.length;
        if (r4 >= r7) goto L_0x0006;
    L_0x0097:
        r7 = r5[r4];
        r7 = (com.samsung.android.content.clipboard.SemClipboardManager.OnUserChangedListener) r7;
        r7.onUserChanged(r6);
        r4 = r4 + 1;
        goto L_0x0094;
    L_0x00a1:
        r7 = move-exception;
        monitor-exit(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.content.clipboard.SemClipboardManager.notifyEvent(android.os.Message):void");
    }

    private void sendResult(boolean z, int i) {
        if (!z) {
            Message obtainMessage = this.mSetDataHandler.obtainMessage();
            if (i == 0) {
                obtainMessage.what = 0;
            } else {
                obtainMessage.what = 1;
            }
            this.mSetDataHandler.sendMessage(obtainMessage);
        }
    }

    private void startClipboardUIServiceService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.samsung.android.clipboarduiservice", "com.samsung.android.clipboarduiservice.ClipboardUIServiceStarter"));
            this.mContext.startServiceAsUser(intent, UserHandle.OWNER);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void addClip(Context context, SemClipData semClipData, OnAddClipResultListener onAddClipResultListener) {
        int addData = addData(context, semClipData, false, true, false);
        if (onAddClipResultListener == null) {
            return;
        }
        if (addData == 0) {
            onAddClipResultListener.onSuccess();
        } else {
            onAddClipResultListener.onFailure(addData);
        }
    }

    public boolean clearFilter(int i, OnPasteListener onPasteListener) {
        if (isCocktailBarDisplayed()) {
            return false;
        }
        if (onPasteListener == null) {
            Log.secE(TAG, "Wrong usage: clearFilter - parameter listener is null. Application should set listener!");
            return false;
        }
        if (onPasteListener.equals(this.mPasteListener)) {
            this.mIsFiltered = false;
            this.mTypeId = 0;
            this.mPasteListener = null;
            try {
                if (getService() != null) {
                    getService().updateFilter(0, null);
                    return true;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void dismissDialog() {
        try {
            if (getService() == null) {
                Log.secW(TAG, "dismissDialog - Fail~ Service is null.");
                return;
            }
            getService().dismissDialog();
            Log.secD(TAG, "dismissDialog");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void filterClip(int i, OnPasteListener onPasteListener) {
        if (isEnabled("filterClip")) {
            this.mIsFiltered = false;
            if (i == 4) {
                i = -1;
                Log.secD(TAG, "setFilter - Format changed");
            }
            updateFilter(i, onPasteListener);
            if (onPasteListener != null) {
                this.mIsFiltered = true;
            }
            Log.secD(TAG, "setFilter - Format:" + i + ", " + onPasteListener);
            if (!isCocktailBarDisplayed()) {
                this.mTypeId = i;
                this.mPasteListener = onPasteListener;
            }
        }
    }

    public SemClipData getClip(String str) throws RemoteException {
        if (getService() != null) {
            return getService().getClip(str);
        }
        Log.secE(TAG, "getService() is null.");
        return null;
    }

    public int getClipboardExServiceFilter() {
        try {
            return getService().getFilter();
        } catch (Exception e) {
            return 0;
        }
    }

    public List<SemClipData> getClips() throws RemoteException {
        if (getService() != null) {
            return getService().getClips();
        }
        Log.secE(TAG, "getService() is null.");
        return null;
    }

    public int getCount() {
        if (!isEnabled("getCount")) {
            return 0;
        }
        int i = -1;
        try {
            if (getService() == null) {
                Log.secW(TAG, "getCount - Fail~ Service is null.");
                return -1;
            }
            i = getService().getCount();
            return i;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public SemClipData getLatestClip(int i) {
        if (!isEnabled("getData")) {
            return null;
        }
        SemClipData semClipData = null;
        try {
            if (getService() == null) {
                Log.secW(TAG, "getData - Fail~ Service is null.");
                return null;
            }
            getService().loadSEClipboard();
            int i2 = this.mTypeId != 0 ? this.mTypeId : i;
            Log.secW(TAG, "getLatestData : " + i2 + ", " + this.mTypeId + ", " + this.mPasteListener);
            semClipData = getService().getClipData(i2);
            return semClipData;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int getPersonaId() {
        if (!ClipboardConstants.HAS_KNOX_FEATURE) {
            return getUserId();
        }
        if (this.mContext == null) {
            return 0;
        }
        if (this.mPersonaManager == null) {
            this.mPersonaManager = (SemPersonaManager) this.mContext.getSystemService("persona");
        }
        return this.mPersonaManager != null ? this.mPersonaManager.getFocusedUser() : ActivityManager.getCurrentUser();
    }

    public boolean isClipboardAllowedToUse(int i) {
        boolean z = false;
        if (this.mContext == null || getService() == null) {
            return false;
        }
        try {
            if (getService().isClipboardAllowed(i)) {
                z = getService().isPackageAllowed(i);
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled() {
        if (isUPSMode() || isEmergencyMode()) {
            return false;
        }
        try {
            if (getService() != null) {
                return sService.isEnabled();
            }
            Log.secD(TAG, "isEnabled(): ClipboardExService is null, returning false");
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isFilterRequired() {
        return true;
    }

    public boolean isFiltered() {
        return !isEnabled("isFiltered") ? false : this.mIsFiltered;
    }

    public boolean isShowing() {
        try {
            if (getService() != null) {
                return getService().isShowing();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean paste(String str) throws RemoteException {
        return getService() != null ? getService().pasteClip(str) : false;
    }

    @Deprecated
    public void pasteClip(String str) throws RemoteException {
        if (getService() == null || !getService().pasteClip(str)) {
            Toast.makeText(this.mContext, (int) C0717R.string.clipboard_cant_paste_item, 0).show();
        }
    }

    public boolean registClipboardWorkingFormUiInterface(IClipboardWorkingFormUiInterface iClipboardWorkingFormUiInterface) {
        if (!isEnabled("RegistClipboardWorkingFormUiInterface")) {
            return false;
        }
        boolean z = false;
        if (this.mRegInterface == null || this.mRegInterface != iClipboardWorkingFormUiInterface) {
            this.mRegInterface = iClipboardWorkingFormUiInterface;
        }
        if (getService() == null) {
            Log.secW(TAG, "RegistClipboardWorkingFormUiInterface - Fail~ Service is null.");
            return false;
        }
        try {
            getService().registClipboardWorkingFormUiInterfaces(this.mRegInterface);
            Log.secI(TAG, "Regist ClipboardWorkingFormUiInterface - Success.");
            z = true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return z;
    }

    public void registerClipboardEventListener(SemClipboardEventListener semClipboardEventListener) {
        if (!isEnabled("registerClipboardEventListener")) {
            return;
        }
        if (getService() == null) {
            Log.secW(TAG, "registerClipboardUIInterface: Service is null.");
            return;
        }
        synchronized (this.mOnClipboardEventServiceListeners) {
            if (this.mOnClipboardEventServiceListeners.size() == 0) {
                try {
                    getService().addClipboardEventListener(this.mOnClipboardEventServiceListener, this.mContext.getOpPackageName());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mOnClipboardEventServiceListeners.add(semClipboardEventListener);
        }
    }

    public void registerOnUserChangedListener(OnUserChangedListener onUserChangedListener) {
        if (!isEnabled("registerOnUserChangedListener")) {
            return;
        }
        if (getService() == null) {
            Log.secW(TAG, "registerOnUserChangedListener: Service is null.");
            return;
        }
        synchronized (this.mOnUserChangedListeners) {
            if (this.mOnUserChangedListeners.size() == 0) {
                try {
                    getService().addUserChangedListener(this.mOnUserChangedListener, this.mContext.getOpPackageName());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mOnUserChangedListeners.add(onUserChangedListener);
        }
    }

    public boolean removeAll() throws RemoteException {
        return getService() != null ? getService().removeAll() : false;
    }

    public boolean removeClip(String str) throws RemoteException {
        if (getService() != null) {
            return getService().removeClip(str);
        }
        Log.secE(TAG, "getService() is null.");
        return false;
    }

    public void requestPaste(SemClipData semClipData) {
        if (isEnabled("requestPaste")) {
            Log.secD(TAG, "requestPaste : " + (semClipData != null ? semClipData.getClipType() : -1));
            if (isFiltered()) {
                try {
                    if (getService() == null) {
                        Log.secW(TAG, "requestPaste - Fail~ Service is null.");
                    } else if (semClipData == null) {
                        Log.secE(TAG, "clipdata is null");
                    } else if (semClipData == null || !semClipData.canAlternateClipData(this.mTypeId)) {
                        Log.secE(TAG, "Can't convert format type : " + this.mTypeId + ", " + semClipData.getClipType());
                    } else {
                        this.mPasteListener.onPaste(semClipData);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                Log.secE(TAG, "no app clipboard listener!");
            }
        }
    }

    public int setData(Context context, ClipData clipData, boolean z) {
        boolean z2 = true;
        SemClipData ClipDataToClipboardData = ClipboardConverter.ClipDataToClipboardData(clipData);
        if (ClipDataToClipboardData == null) {
            return 1;
        }
        if (z) {
            z2 = false;
        }
        return addData(context, ClipDataToClipboardData, z2, z, false);
    }

    public int setDataWithoutNoti(Context context, SemClipData semClipData) {
        return addData(context, semClipData, true, true, false);
    }

    public int setDataWithoutSendingOrginalClipboard(Context context, SemClipData semClipData) {
        return addData(context, semClipData, false, false, true);
    }

    public void showDialog() {
        if (!isEnabled("showDialog")) {
            return;
        }
        if (isClipboardAllowedToUse(getPersonaId())) {
            startClipboardUIServiceService();
            try {
                if (getService() == null) {
                    Log.secW(TAG, "showDialog - Fail~ Service is null.");
                    return;
                }
                if (isFiltered()) {
                    getService().showDialogWithType(this.mTypeId, this.mClipboardPasteEvent);
                    Log.secD(TAG, "showDialog - " + this.mTypeId + " : " + this.mClipboardPasteEvent);
                } else {
                    getService().showDialog();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this.mContext, (int) C0717R.string.clipboard_restrict, 0).show();
        }
    }

    public boolean showDialog(int i, OnPasteListener onPasteListener) {
        if (!isEnabled("showDialog")) {
            return false;
        }
        if (isClipboardAllowedToUse(getPersonaId())) {
            boolean z = true;
            startClipboardUIServiceService();
            Log.secI(TAG, "showDialog : " + i);
            try {
                if (getService() == null) {
                    Log.secW(TAG, "showDialog - Fail~ Service is null.");
                    return false;
                }
                if (isFiltered()) {
                    getService().showDialogWithType(this.mTypeId, this.mClipboardPasteEvent);
                    Log.secD(TAG, "showDialog - " + i + ", " + this.mTypeId + " : " + onPasteListener);
                } else {
                    this.mPasteListener = onPasteListener;
                    getService().showDialogWithType(i, this.mClipboardPasteEvent);
                    Log.secD(TAG, "showDialog - " + i + " : " + onPasteListener);
                }
                return z;
            } catch (Throwable e) {
                e.printStackTrace();
                z = false;
            }
        } else {
            Toast.makeText(this.mContext, (int) C0717R.string.clipboard_restrict, 0).show();
            return false;
        }
    }

    public void unRegistClipboardWorkingFormUiInterface() {
        if (isEnabled("unRegistClipboardWorkingFormUiInterface")) {
            try {
                if (getService() == null) {
                    Log.secW(TAG, "unRegistClipboardWorkingFormUiInterface - Fail~ Service is null.");
                    return;
                }
                if (this.mRegInterface != null) {
                    getService().unRegistClipboardWorkingFormUiInterfaces(this.mRegInterface);
                } else {
                    Log.secE(TAG, "reg interface is null!");
                }
            } catch (Throwable e) {
                Log.secE(TAG, "unRegistClipboardWorkingFormUiInterface(RemoteException): ");
                e.printStackTrace();
            }
        }
    }

    public void unregisterClipboardEventListener(SemClipboardEventListener semClipboardEventListener) {
        synchronized (this.mOnClipboardEventServiceListeners) {
            this.mOnClipboardEventServiceListeners.remove(semClipboardEventListener);
            if (this.mOnClipboardEventServiceListeners.size() == 0) {
                try {
                    getService().removeClipboardEventListener(this.mOnClipboardEventServiceListener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void unregisterOnUserChangedListener(OnUserChangedListener onUserChangedListener) {
        synchronized (this.mOnUserChangedListeners) {
            this.mOnUserChangedListeners.remove(onUserChangedListener);
            if (this.mOnUserChangedListeners.size() == 0) {
                try {
                    getService().removeUserChangedListener(this.mOnUserChangedListener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public boolean updateClip(String str, SemClipData semClipData) throws RemoteException {
        if (getService() != null) {
            return getService().updateClip(str, semClipData);
        }
        Log.secE(TAG, "getService() is null.");
        return false;
    }

    public void updateDialogShowingState(boolean z) throws RemoteException {
        if (getService() != null) {
            getService().updateDialogShowingState(z);
        }
    }

    public void updateFilter(int i, int i2, OnPasteListener onPasteListener) {
        if (!isEnabled("updateFilter")) {
            return;
        }
        if (isFiltered()) {
            Log.secD(TAG, "updateFilter return : " + this.mPasteListener);
            return;
        }
        try {
            if (getService() == null) {
                Log.secW(TAG, "updateFilter - Fail~ Service is null.");
                return;
            }
            if (!isCocktailBarDisplayed()) {
                this.mPasteListener = onPasteListener;
                getService().updateFilterWithInputType(i, i2, this.mClipboardPasteEvent);
                Log.secD(TAG, "updateFilter - " + i + ", " + this.mPasteListener);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void updateFilter(int i, OnPasteListener onPasteListener) {
        if (!isEnabled("updateFilter")) {
            return;
        }
        if (isFiltered()) {
            Log.secD(TAG, "updateFilter return : " + this.mPasteListener);
            return;
        }
        try {
            if (getService() == null) {
                Log.secW(TAG, "updateFilter - Fail~ Service is null.");
                return;
            }
            if (!isCocktailBarDisplayed()) {
                this.mPasteListener = onPasteListener;
                getService().updateFilter(i, this.mClipboardPasteEvent);
                Log.secD(TAG, "updateFilter - " + i + ", " + this.mPasteListener);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
