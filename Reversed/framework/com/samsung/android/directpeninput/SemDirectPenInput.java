package com.samsung.android.directpeninput;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.BaseBundle;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.samsung.android.directpeninput.ISemDirectPenInputClient.Stub;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.mateservice.action.ActionBase;
import com.samsung.android.share.SShareConstants;
import java.lang.ref.WeakReference;

public final class SemDirectPenInput {
    public static final int BIND_MSG_REQUEST_IME_RECT = 2;
    public static final int BIND_MSG_REQUEST_TARGET_RECT = 1;
    public static final int BIND_MSG_RESULT_IME_CLOSED = 2;
    public static final int BIND_MSG_RESULT_IME_OPENED = 1;
    public static final String BUTTON_DONE = "ACTION_DONE";
    public static final String BUTTON_GO = "ACTION_GO";
    public static final String BUTTON_SEARCH = "ACTION_SEARCH";
    public static final String BUTTON_SEND = "ACTION_SEND";
    private static final int CLIENT_SEQUENCE_MASK = 255;
    private static final int CLIENT_UNIQUE_ID_MASK = -256;
    private static final boolean DEBUG = "eng".equals(Build.TYPE);
    public static final int FLAG_HELP_MODE = 4;
    public static final int FLAG_IMAGE_WRITING = 1;
    public static final int FLAG_MATH_WRITING = 2;
    public static final int FLAG_MMS_MODE = 8;
    public static final int FLAG_START_DRAWING_MODE = 16;
    private static final String HELP_MODE_RESULT_CLOSED = "CLOSED";
    private static final String HELP_MODE_RESULT_HOVERED = "HOVERED";
    private static final String HELP_MODE_RESULT_HOVER_CANCELED = "HOVER_CANCELED";
    private static final String HELP_MODE_RESULT_OPENED = "OPENED";
    private static final String HELP_MODE_RESULT_TEXT_INSERTED = "TEXT_INSERTED";
    public static final String IME_CMD_CANCEL_CLOSE = "com.samsung.android.directpeninput/CANCEL_CLOSE";
    public static final String IME_CMD_SEND_BINDER = "com.samsung.android.directpeninput/SEND_BINDER";
    private static final int MAX_WAIT = 10;
    private static final String MMS_DATA_DELETE = "MMS_DATA_DELETE";
    private static final int MSG_CANCEL_WRITINGBUDDY_CUE = 6;
    private static final int MSG_EDITOR_ACTION_DOWN = 7;
    private static final int MSG_SERVICE_RESULT_RECEIVED = 3;
    private static final int MSG_SERVICE_TEXT_DELETED = 2;
    private static final int MSG_SERVICE_TEXT_INSERTED = 1;
    private static final int MSG_SERVICE_UPDATE_DIALOG = 8;
    private static final int MSG_SERVICE_UPDATE_POSITION = 4;
    private static final int MSG_SERVICE_UPDATE_POSITION_CHECK = 9;
    private static final int MSG_SHOW_WRITINGBUDDY_CUE = 5;
    public static final String RESULT_FIELD_DELIMITER = "//";
    public static final String RESULT_STRING_DELIMITER = "//";
    public static final String SERVICE_CB_CLIENT_CHANGED = "service_cb_client_changed";
    public static final String SERVICE_CB_CLOSED = "service_cb_closed";
    public static final String SERVICE_CB_DATA_CHANGED = "service_cb_perform_editor_action";
    public static final String SERVICE_CB_INFLATE_DONE = "service_cb_inflate_done";
    public static final String SERVICE_CB_INIT_TEXT = "service_cb_init_text";
    public static final String SERVICE_CB_PRIVATE = "service_cb_private";
    public static final String SERVICE_CB_WRITING_DONE = "service_cb_writing_done";
    public static final int SERVICE_EVENT_DATA_CHANGED = 2;
    public static final int SERVICE_EVENT_WRITING_CANCEL = 1;
    public static final int SERVICE_EVENT_WRITING_DONE = 0;
    private static final int START_DELAY_TIME_MS = 150;
    private static final int STATE_EVENT_SERVICE_CALLBACK_CLOSED = 2;
    private static final int STATE_EVENT_SERVICE_CALLBACK_INFLATE_DONE = 1;
    private static final int STATE_EVENT_TYPE_MOTION = 1;
    private static final int STATE_EVENT_TYPE_SERVICE_CALLBACK = 2;
    private static final int STATE_RESET_COUNT = 3;
    private static final int STATE_STEP_STANDBY = 0;
    private static final int STATE_STEP_WRITING = 1;
    private static final String TAG = "DirectPenInput";
    public static final int TEMPLATE_EDITOR = 22;
    public static final int TYPE_BOARD_EDITOR = 1;
    public static final int TYPE_BOARD_NONE = 0;
    public static final int TYPE_BOARD_TEMPLATE = 2;
    public static final int TYPE_EDITOR_NONE = 0;
    public static final int TYPE_EDITOR_NUMBER = 1;
    public static final int TYPE_EDITOR_TEXT = 2;
    private View mAnchorView = null;
    private int mBoardTemplate;
    private int mBoardType;
    private OnButtonClickListener mButtonClickListener;
    private boolean mCanShowAutoCompletePopup = true;
    private boolean mCanStartDirectPenInput = false;
    private ISemDirectPenInputManager mDPIManager;
    private Rect mDPIRect = null;
    private int mEditCount = 0;
    private int mEditorType;
    private Handler mHandler;
    private boolean mIgnoreSizeChange = false;
    private ImageWritingListener mImageWritingListener;
    private Rect mInitRect = null;
    private boolean mIsCursorBlinkDisabled = false;
    private boolean mIsForceMode = false;
    private boolean mIsHelpModeEnabled;
    private boolean mIsHoverState = false;
    private boolean mIsImageWritingEnabled;
    private boolean mIsMathWritingEnabled;
    private boolean mIsMultiLineEditor = false;
    private boolean mIsPerformingAction = false;
    private boolean mIsPopupCueShowMSGCalled = false;
    private boolean mIsReceiveActionButtonEnabled;
    private boolean mIsWaitingHideSoftInput = false;
    private boolean mIsWatchActionEnabled;
    private OnScrollChangedListener mOnScrollChangedListener = new C00531();
    private View mParentView;
    private PopupCue mPopupCue;
    private Rect mScrRectUpdated;
    ISemDirectPenInputClient mServiceCallback = new C00542();
    private ServiceEventListener mServiceEventListener;
    private int mShowCnt = 0;
    private int mState = 0;
    private int mStateResetCnt = 0;
    private TextUpdateListener mTextUpdateListener;
    private TextWritingListener mTextWritingListener;
    private int mViewID = 0;
    private int mWindowMode = 0;
    private MotionEvent motionEvent = null;

    class C00531 implements OnScrollChangedListener {
        C00531() {
        }

        public void onScrollChanged() {
            SemDirectPenInput.this.notifyPositionChanged(11);
        }
    }

    class C00542 extends Stub {
        C00542() {
        }

        public void onResultReceived(int i, Bundle bundle) throws RemoteException {
            Slog.d(SemDirectPenInput.TAG, "mServiceCallback onResultReceived() clientID : " + Integer.toHexString(i));
            SemDirectPenInput.this.mHandler.obtainMessage(3, i, 0, bundle).sendToTarget();
        }

        public void onTextDeleted(int i, int i2, int i3) throws RemoteException {
            Slog.d(SemDirectPenInput.TAG, "mServiceCallback onTextDeleted()");
            SemDirectPenInput.this.mHandler.obtainMessage(2, i2, i3).sendToTarget();
        }

        public void onTextInserted(int i, int i2, CharSequence charSequence, int i3) throws RemoteException {
            Slog.d(SemDirectPenInput.TAG, "mServiceCallback onTextInserted()");
            SemDirectPenInput.this.mHandler.obtainMessage(1, i2, i3, charSequence).sendToTarget();
        }

        public void onUpdateDialog(int i) throws RemoteException {
            Slog.d(SemDirectPenInput.TAG, "mServiceCallback onUpdateDialog() clientID : " + Integer.toHexString(i));
            SemDirectPenInput.this.mHandler.obtainMessage(8).sendToTarget();
        }
    }

    class C00553 implements OnHoverListener {
        C00553() {
        }

        public boolean onHover(View view, MotionEvent motionEvent) {
            return SemDirectPenInput.this.handleMotionEvent(view, motionEvent);
        }
    }

    class C00564 implements OnTouchListener {
        C00564() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return SemDirectPenInput.this.handleMotionEvent(view, motionEvent);
        }
    }

    class C00615 implements OnTouchListener {

        class C00571 implements Runnable {
            C00571() {
            }

            public void run() {
                SemDirectPenInput.this.mIsWaitingHideSoftInput = false;
                SemDirectPenInput.this.showDirectPenInput();
            }
        }

        class C00582 implements Runnable {
            C00582() {
            }

            public void run() {
                SemDirectPenInput.this.dismissPopupCue(false);
            }
        }

        class C00593 implements Runnable {
            C00593() {
            }

            public void run() {
                SemDirectPenInput.this.mIsWaitingHideSoftInput = false;
                SemDirectPenInput.this.showDirectPenInput();
            }
        }

        class C00604 implements Runnable {
            C00604() {
            }

            public void run() {
                SemDirectPenInput.this.dismissPopupCue(false);
            }
        }

        C00615() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                if (SemDirectPenInput.this.mPopupCue != null && SemDirectPenInput.this.mPopupCue.isShowing()) {
                    SemDirectPenInput.this.mPopupCue.switchCueButton(true);
                }
                if (view != null) {
                    view.playSoundEffect(0);
                }
                SemDirectPenInput.this.getHandler().removeMessages(6);
                InputMethodManager peekInstance = InputMethodManager.peekInstance();
                if (SemDirectPenInput.this.checkDirectPenInputService()) {
                    if (peekInstance == null || !peekInstance.isInputMethodShown()) {
                        Slog.d(SemDirectPenInput.TAG, "Can not find IMM");
                        SemDirectPenInput.this.showDirectPenInput();
                        new Handler().postDelayed(new C00582(), 450);
                    } else {
                        SemDirectPenInput.this.dismissPopupCue(false);
                        SemDirectPenInput.this.mIsWaitingHideSoftInput = true;
                        peekInstance.semForceHideSoftInput();
                        new Handler().postDelayed(new C00571(), 350);
                    }
                } else if (SemDirectPenInput.this.checkUseSamsungIME() || peekInstance == null || !peekInstance.isInputMethodShown()) {
                    Slog.d(SemDirectPenInput.TAG, "Can not find IMM");
                    SemDirectPenInput.this.showDirectPenInput();
                    new Handler().postDelayed(new C00604(), 450);
                } else {
                    SemDirectPenInput.this.dismissPopupCue(false);
                    SemDirectPenInput.this.mIsWaitingHideSoftInput = true;
                    peekInstance.semForceHideSoftInput();
                    new Handler().postDelayed(new C00593(), 350);
                }
            } else if (action == 1 && SemDirectPenInput.this.mBoardType == 1) {
                SemDirectPenInput.this.dismissPopupCue(false);
            }
            return false;
        }
    }

    class C00626 implements OnHoverListener {
        C00626() {
        }

        public boolean onHover(View view, MotionEvent motionEvent) {
            if (SemDirectPenInput.this.getHandler().hasMessages(6)) {
                SemDirectPenInput.this.getHandler().removeMessages(6);
            }
            if (motionEvent.getAction() == 10 && SemDirectPenInput.this.mState == 0) {
                InputManager instance = InputManager.getInstance();
                if (!SemDirectPenInput.this.pointInView(view, motionEvent.getX(), motionEvent.getY())) {
                    Slog.d(SemDirectPenInput.TAG, "Close DirectPenInput cue : 1 " + SemDirectPenInput.this.mIsPopupCueShowMSGCalled);
                    if (SemDirectPenInput.this.mIsPopupCueShowMSGCalled) {
                        SemDirectPenInput.this.mIsPopupCueShowMSGCalled = false;
                        SemDirectPenInput.this.getHandler().removeMessages(6);
                    } else {
                        SemDirectPenInput.this.getHandler().sendEmptyMessageDelayed(6, 150);
                    }
                } else if (instance != null && instance.getScanCodeState(-1, SemDirectPenInput.CLIENT_UNIQUE_ID_MASK, 320) == 0) {
                    Slog.d(SemDirectPenInput.TAG, "Close DirectPenInput cue : 2");
                    SemDirectPenInput.this.dismissPopupCue(true);
                    SemDirectPenInput.this.sendHelpModeResult(SemDirectPenInput.HELP_MODE_RESULT_HOVER_CANCELED);
                } else if (SemDirectPenInput.this.mPopupCue == null || !SemDirectPenInput.this.mPopupCue.isAirButtonClicked()) {
                    Slog.d(SemDirectPenInput.TAG, "Close DirectPenInput cue : 4");
                    SemDirectPenInput.this.getHandler().sendEmptyMessageDelayed(6, 150);
                } else {
                    Slog.d(SemDirectPenInput.TAG, "Close DirectPenInput cue : 3");
                    SemDirectPenInput.this.dismissPopupCue(true);
                }
            }
            return false;
        }
    }

    private static class DPIHandler extends Handler {
        private final WeakReference<SemDirectPenInput> mDirectPenInput;

        DPIHandler(SemDirectPenInput semDirectPenInput) {
            this.mDirectPenInput = new WeakReference(semDirectPenInput);
        }

        public void handleMessage(Message message) {
            SemDirectPenInput semDirectPenInput = (SemDirectPenInput) this.mDirectPenInput.get();
            if (semDirectPenInput != null) {
                semDirectPenInput.handleMessage(message);
            }
        }
    }

    static class EventChecker {
        static int action = -1;
        static float f6x = -1.0f;
        static float f7y = -1.0f;

        EventChecker() {
        }

        public static boolean isDuplicated(MotionEvent motionEvent) {
            if (motionEvent == null) {
                action = -1;
                return false;
            }
            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (action == action && x == f6x && y == f7y) {
                return true;
            }
            action = action;
            f6x = x;
            f7y = y;
            return false;
        }
    }

    public interface ImageWritingListener {
        void onImageReceived(Bitmap bitmap);
    }

    public interface OnButtonClickListener {
        boolean onButtonClick(String str);
    }

    public interface ServiceEventListener {
        boolean onEvent(int i, CharSequence charSequence);
    }

    public interface TextUpdateListener {
        CharSequence onTextUpdated(CharSequence charSequence);
    }

    public interface TextWritingListener {
        void onTextReceived(CharSequence charSequence);
    }

    public SemDirectPenInput(View view) {
        initVariable();
        setParentView(view);
        if (view != null) {
            view.semSetDirectPenInput(this);
        }
        if (view instanceof EditText) {
            setBoardType(1);
        } else {
            setBoardType(2);
        }
    }

    private boolean canStartDirectPenInput(boolean z) {
        boolean z2 = this.mCanStartDirectPenInput;
        if (!z) {
            return z2;
        }
        z2 = this.mParentView != null ? this.mParentView.semIsDirectPenInputEnabled() : false;
        if (z2) {
            z2 = System.getInt(this.mParentView.getContext().getContentResolver(), "pen_writing_buddy", 0) == 1;
        }
        if (z2 && (this.mParentView instanceof EditText) && !(this.mParentView.isEnabled() && this.mParentView.isFocusable())) {
            z2 = false;
        }
        if (z2 && (this.mParentView instanceof EditText)) {
            LayoutParams layoutParams = this.mParentView.getRootView().getLayoutParams();
            if (layoutParams instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams layoutParams2 = (WindowManager.LayoutParams) layoutParams;
                Slog.d(TAG, "canStartDirectPenInput : window type= " + layoutParams2.type);
                if (layoutParams2.type == 1000) {
                    z2 = false;
                }
            }
        }
        Context context = this.mParentView != null ? this.mParentView.getContext() : null;
        this.mWindowMode = getWindowMode();
        if (context != null) {
            if ((context.getResources().getConfiguration().semMobileKeyboardCovered == 1 ? 1 : null) != null) {
                z2 = false;
                Slog.d(TAG, "canStartDirectPenInput(): result 7 = " + false);
            }
        }
        if (z2) {
            ViewParent parent = this.mParentView.getParent();
            while (parent != null && (parent instanceof ViewGroup)) {
                if (((ViewGroup) parent).semIsDirectPenInputEnabled()) {
                    z2 = false;
                    break;
                }
                parent = parent.getParent();
            }
        }
        if (z2 && (this.mParentView instanceof EditText)) {
            int i;
            boolean z3;
            float height;
            EditorInfo editorInfo = new EditorInfo();
            ((EditText) this.mParentView).extractEditorInfo(editorInfo);
            int i2 = editorInfo.inputType & 15;
            int i3 = editorInfo.inputType & 4080;
            if (!(i3 == 128 || i3 == 144 || i3 == 224)) {
                if (i2 == 2 && i3 == 16) {
                }
                if (!(i2 == 2 || i2 == 3)) {
                    if (i2 == 4) {
                    }
                    if (z2 && editorInfo.extras != null && editorInfo.extras.getBoolean("com.samsung.android/disableDirectPenInput", false)) {
                        z2 = false;
                    }
                    if ((editorInfo.inputType & 15) == 1) {
                        i = editorInfo.inputType & 131072;
                        editorInfo.inputType = i;
                        z3 = i > 0;
                    } else {
                        z3 = false;
                    }
                    this.mIsMultiLineEditor = z3;
                    if (z2 && !this.mIsMultiLineEditor && this.mParentView.getCurrentDirectPenInputView() == null) {
                        height = ((float) getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false).height()) / ((float) getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView).height());
                        if (height < 0.2f) {
                            z2 = false;
                            Slog.d(TAG, "View is scrolled." + height);
                        }
                    }
                }
                z2 = false;
                z2 = false;
                if ((editorInfo.inputType & 15) == 1) {
                    z3 = false;
                } else {
                    i = editorInfo.inputType & 131072;
                    editorInfo.inputType = i;
                    if (i > 0) {
                    }
                }
                this.mIsMultiLineEditor = z3;
                if (this.mAnchorView != null) {
                }
                if (this.mAnchorView != null) {
                }
                height = ((float) getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false).height()) / ((float) getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView).height());
                if (height < 0.2f) {
                    z2 = false;
                    Slog.d(TAG, "View is scrolled." + height);
                }
            }
            z2 = false;
            if (i2 == 4) {
                z2 = false;
            }
            z2 = false;
            if ((editorInfo.inputType & 15) == 1) {
                i = editorInfo.inputType & 131072;
                editorInfo.inputType = i;
                if (i > 0) {
                }
            } else {
                z3 = false;
            }
            this.mIsMultiLineEditor = z3;
            if (this.mAnchorView != null) {
            }
            if (this.mAnchorView != null) {
            }
            height = ((float) getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false).height()) / ((float) getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView).height());
            if (height < 0.2f) {
                z2 = false;
                Slog.d(TAG, "View is scrolled." + height);
            }
        }
        this.mCanStartDirectPenInput = z2;
        Slog.d(TAG, "canStartDirectPenInput() : " + z2);
        return z2;
    }

    private boolean canStartTemplateDirectPenInput(boolean z) {
        boolean z2 = this.mCanStartDirectPenInput;
        if (!z) {
            return z2;
        }
        z2 = this.mParentView != null ? this.mParentView.semIsDirectPenInputEnabled() : false;
        if (z2) {
            z2 = System.getInt(this.mParentView.getContext().getContentResolver(), "pen_writing_buddy", 0) == 1;
        }
        Context context = this.mParentView != null ? this.mParentView.getContext() : null;
        this.mWindowMode = getWindowMode();
        if (context != null) {
            if (context.getResources().getConfiguration().semMobileKeyboardCovered == 1) {
                z2 = false;
            }
        }
        if (z2) {
            ViewParent parent = this.mParentView.getParent();
            while (parent != null && (parent instanceof ViewGroup)) {
                if (parent.semIsDirectPenInputEnabled()) {
                    z2 = false;
                    break;
                }
                parent = parent.getParent();
            }
        }
        if (z2) {
            Rect visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            Rect rectInWindow = getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView);
            if (visibleRectInWindow.top != rectInWindow.top) {
                z2 = false;
                if (DEBUG) {
                    Slog.d(TAG, "VisibleRect : " + visibleRectInWindow.toShortString() + " ViewRect : " + rectInWindow.toShortString());
                }
                if (this.mPopupCue != null && this.mPopupCue.isShowing()) {
                    this.mPopupCue.dismiss(false);
                    this.mPopupCue = null;
                }
            }
        }
        this.mCanStartDirectPenInput = z2;
        Slog.d(TAG, "canStartDirectPenInput() : " + z2);
        return z2;
    }

    private boolean checkDirectPenInputService() {
        if (this.mParentView == null) {
            return false;
        }
        try {
            this.mParentView.getContext().getPackageManager().getPackageInfo("com.samsung.android.directpeninputservice", 1);
            return true;
        } catch (NameNotFoundException e) {
            Slog.w(TAG, "Cannot find DirectPenInputSerivce");
            return false;
        }
    }

    private boolean checkUseSamsungIME() {
        InputMethodManager peekInstance = InputMethodManager.peekInstance();
        if (!this.mIsForceMode && peekInstance != null) {
            return peekInstance.isCurrentInputMethodAsSamsungKeyboard();
        }
        Slog.d(TAG, "Can not find IMM");
        return false;
    }

    private boolean closeDirectPenInput(boolean z) {
        try {
            if (this.mDPIManager != null) {
                this.mDPIManager.dismiss(this.mViewID, z);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Can not close DirectPenInput, RemoteException happened");
        }
        return false;
    }

    private int convertDPtoPX(float f, DisplayMetrics displayMetrics) {
        DisplayMetrics displayMetrics2 = displayMetrics;
        if (displayMetrics == null) {
            displayMetrics2 = this.mParentView.getContext().getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, f, displayMetrics2) + 0.5f);
    }

    private void createPopupCue() {
        if (this.mPopupCue == null) {
            this.mPopupCue = new PopupCue(this.mParentView);
            this.mPopupCue.setOnTouchListener(new C00615());
            this.mPopupCue.setOnHoverListener(new C00626());
        }
    }

    private boolean dismissPopupCue(boolean z) {
        Slog.d(TAG, "dismissPopupCue()");
        boolean z2 = false;
        if (this.mHandler != null) {
            this.mHandler.removeMessages(5);
            this.mIsPopupCueShowMSGCalled = false;
        }
        if (this.mPopupCue != null) {
            if (this.mPopupCue.isShowing()) {
                z2 = true;
            }
            this.mPopupCue.dismiss(z);
        }
        return z2;
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.mHandler = new DPIHandler(this);
            } else {
                Slog.e(TAG, "Attempting to create Handler from background thread.");
                this.mHandler = new DPIHandler(this);
            }
        }
        return this.mHandler;
    }

    private int getModeFlag() {
        int i = 0;
        if (this.mIsImageWritingEnabled) {
            i = 1;
        }
        if (this.mIsMathWritingEnabled) {
            i |= 2;
        }
        if (this.mIsHelpModeEnabled) {
            i |= 4;
        }
        if (!(this.mParentView instanceof EditText)) {
            return i;
        }
        boolean z = false;
        boolean z2 = false;
        BaseBundle inputExtras = ((EditText) this.mParentView).getInputExtras(true);
        if (inputExtras != null) {
            z = inputExtras.getBoolean("isMmsMode", false);
            z2 = inputExtras.getBoolean("isStartDrawingMode", false);
        }
        if (z) {
            i |= 8;
        }
        if (!z2) {
            return i;
        }
        i |= 16;
        inputExtras.putBoolean("isStartDrawingMode", false);
        return i;
    }

    private Rect getRectInWindow(View view) {
        Rect rect = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] iArr = new int[]{0, 0};
            view.getLocationInWindow(iArr);
            rect.set(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight());
        }
        return rect;
    }

    private Rect getRectOnScreen(View view) {
        Rect rect = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] iArr = new int[]{0, 0};
            view.getLocationOnScreen(iArr);
            rect.set(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight());
        }
        return rect;
    }

    private Rect getTargetDPIRect(View view, int i) {
        return this.mDPIRect;
    }

    private Rect getVisibleRectInWindow(View view, boolean z) {
        int i = this.mParentView.getContext().getResources().getDisplayMetrics().widthPixels;
        Rect rectInWindow = getRectInWindow(view);
        View view2 = view;
        ViewParent parent = view.getParent();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (parent instanceof View) {
            View view3 = (View) parent;
            int y = (int) view2.getY();
            i2 += y;
            if (y < 0 && i2 < 0) {
                rectInWindow.top += Math.abs(i2);
                i2 = 0;
            }
            if (view3.getScrollY() > 0) {
                if (view3.getScrollY() > i2) {
                    rectInWindow.top += view3.getScrollY() - i2;
                    i2 = 0;
                } else {
                    i2 -= view3.getScrollY();
                }
            }
            int y2 = (((int) view2.getY()) + view2.getHeight()) - view3.getScrollY();
            if (y2 + i3 < view3.getHeight()) {
                i3 = -(view3.getHeight() - (y2 + i3));
            } else {
                rectInWindow.bottom -= (y2 + i3) - view3.getHeight();
                i3 = 0;
            }
            if (z) {
                int x = ((int) view2.getX()) % i;
                i4 += x;
                if (x < 0 && i4 < 0) {
                    rectInWindow.left += Math.abs(i4);
                    i4 = 0;
                }
                int scrollX = view3.getScrollX() % i;
                if (scrollX > 0) {
                    if (scrollX > i4) {
                        rectInWindow.left += scrollX - i4;
                        i4 = 0;
                    } else {
                        i4 -= scrollX;
                    }
                }
                int width = (view2.getWidth() + x) - scrollX;
                if (width + i5 < view3.getWidth()) {
                    i5 = -(view3.getWidth() - (width + i5));
                } else {
                    rectInWindow.right -= (width + i5) - view3.getWidth();
                    i5 = 0;
                }
            }
            view2 = view3;
            parent = view3.getParent();
        }
        if (DEBUG) {
            Slog.d(TAG, "getVisibleRectInWindow : " + rectInWindow.toShortString());
        }
        return rectInWindow;
    }

    private Rect getVisibleRectOnScreen(View view, boolean z) {
        int i = this.mParentView.getContext().getResources().getDisplayMetrics().widthPixels;
        Rect rectOnScreen = getRectOnScreen(view);
        View view2 = view;
        ViewParent parent = view.getParent();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (parent instanceof View) {
            View view3 = (View) parent;
            int y = (int) view2.getY();
            i2 += y;
            if (y < 0 && i2 < 0) {
                rectOnScreen.top += Math.abs(i2);
                i2 = 0;
            }
            if (view3.getScrollY() > 0) {
                if (view3.getScrollY() > i2) {
                    rectOnScreen.top += view3.getScrollY() - i2;
                    i2 = 0;
                } else {
                    i2 -= view3.getScrollY();
                }
            }
            int y2 = (((int) view2.getY()) + view2.getHeight()) - view3.getScrollY();
            if (y2 + i3 < view3.getHeight()) {
                i3 = -(view3.getHeight() - (y2 + i3));
            } else {
                rectOnScreen.bottom -= (y2 + i3) - view3.getHeight();
                i3 = 0;
            }
            if (z) {
                int x = ((int) view2.getX()) % i;
                i4 += x;
                if (x < 0 && i4 < 0) {
                    rectOnScreen.left += Math.abs(i4);
                    i4 = 0;
                }
                int scrollX = view3.getScrollX() % i;
                if (scrollX > 0) {
                    if (scrollX > i4) {
                        rectOnScreen.left += scrollX - i4;
                        i4 = 0;
                    } else {
                        i4 -= scrollX;
                    }
                }
                int width = (view2.getWidth() + x) - scrollX;
                if (width + i5 < view3.getWidth()) {
                    i5 = -(view3.getWidth() - (width + i5));
                } else {
                    rectOnScreen.right -= (width + i5) - view3.getWidth();
                    i5 = 0;
                }
            }
            view2 = view3;
            parent = view3.getParent();
        }
        if (DEBUG) {
            Slog.d(TAG, "getVisibleRectOnScreen : " + rectOnScreen.toShortString());
        }
        return rectOnScreen;
    }

    private int getWindowMode() {
        return FingerprintEvent.IMAGE_QUALITY_WET_FINGER;
    }

    private void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                onTextInserted(message.arg1, (CharSequence) message.obj, message.arg2);
                return;
            case 2:
                onTextDeleted(message.arg1, message.arg2);
                return;
            case 3:
                onResultReceived(new Bundle((Bundle) message.obj), message.arg1);
                return;
            case 4:
                notifyPositionChanged(0);
                return;
            case 5:
                showDirectPenInputCue();
                return;
            case 6:
                dismissPopupCue(true);
                sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                return;
            case 8:
                onUpdateDialog();
                return;
            case 9:
                notifyPositionCheck(0);
                return;
            default:
                return;
        }
    }

    private void initVariable() {
        this.mParentView = null;
        this.mDPIManager = null;
        this.mPopupCue = null;
        this.mDPIRect = new Rect();
        this.mIsImageWritingEnabled = false;
        this.mCanShowAutoCompletePopup = true;
        setBoardType(2);
        setEditorType(2);
    }

    private boolean isDPIShowing() {
        return this.mState == 1 && this.mParentView != null && this.mParentView.semIsDirectPenInputEnabled() && this.mParentView.equals(this.mParentView.getCurrentDirectPenInputView());
    }

    private boolean isPasswordInputType(View view) {
        if (view == null) {
            return false;
        }
        EditText editText = (EditText) view;
        EditorInfo editorInfo = new EditorInfo();
        editText.extractEditorInfo(editorInfo);
        return editorInfo.inputType == 128 || editorInfo.inputType == 144 || editorInfo.inputType == 224 || ((editorInfo.inputType == 2 && editorInfo.inputType == 16) || editorInfo.inputType == 129 || editorInfo.inputType == 145);
    }

    private void notifyPositionCheck(int i) {
        Slog.d(TAG, "notifyPositionCheck code : " + i + " " + this.mState);
        if (this.mState != 0) {
            Rect visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            Rect visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            if (DEBUG) {
                Slog.d(TAG, "Update Position check. wnd : " + visibleRectInWindow + " scr : " + visibleRectOnScreen);
            }
            if (!visibleRectOnScreen.equals(this.mScrRectUpdated)) {
                try {
                    if (this.mDPIManager != null) {
                        this.mDPIManager.updatePosition(this.mViewID, visibleRectInWindow, visibleRectOnScreen);
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Can not start DirectPenInput, RemoteException happened");
                }
            }
        }
    }

    private boolean pointInView(float f, float f2) {
        return pointInView(this.mParentView, f, f2);
    }

    private boolean pointInView(View view, float f, float f2) {
        return f >= 0.0f && f < ((float) (view.getRight() - view.getLeft())) && f2 >= 0.0f && f2 < ((float) (view.getBottom() - view.getTop()));
    }

    @Deprecated
    private void registerEventListener(View view) {
        view.setOnHoverListener(new C00553());
        view.setOnTouchListener(new C00564());
    }

    private void registerPositionChangeListener() {
        if (this.mBoardType == 1) {
            if (this.mParentView instanceof EditText) {
                ((EditText) this.mParentView).setDPIPositionListenerEnalbed(true);
            }
        } else if (this.mParentView != null) {
            ViewTreeObserver viewTreeObserver = this.mParentView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.removeOnScrollChangedListener(this.mOnScrollChangedListener);
                viewTreeObserver.addOnScrollChangedListener(this.mOnScrollChangedListener);
            }
        }
    }

    private void resetPenPointerIcon() {
        try {
            PointerIcon.setHoveringSpenIcon(20001, -1);
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to change Pen Point to HOVERING_SPENICON_MORE");
        }
    }

    private void resetState() {
        if (this.mIsCursorBlinkDisabled && (this.mParentView instanceof EditText)) {
            ((EditText) this.mParentView).stopCursorBlink(false);
            this.mIsCursorBlinkDisabled = false;
        }
        unregisterPositionChangeListener();
        if (this.mParentView != null && this.mParentView.equals(this.mParentView.getCurrentDirectPenInputView())) {
            this.mParentView.reportCurrentDirectPenInputView(null);
            Slog.d(TAG, "Report current DPI : " + null);
        }
        this.mCanShowAutoCompletePopup = true;
        this.mState = 0;
        this.mStateResetCnt = 0;
    }

    private boolean scheduleState(int i, int i2, MotionEvent motionEvent, int i3) {
        return this.mBoardType == 1 ? scheduleStateForEditor(i, i2, motionEvent, i3) : scheduleStateForTemplate(i, i2, motionEvent, i3);
    }

    private boolean scheduleStateForEditor(int i, int i2, MotionEvent motionEvent, int i3) {
        this.motionEvent = motionEvent;
        if (i == 2 && i2 == 2) {
            int i4 = i3;
            if (this.mViewID == i3) {
                resetState();
            }
            Slog.d(TAG, "state : " + this.mState + "." + i + "." + i2 + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null) + ".  " + Integer.toHexString(this.mParentView != null ? this.mParentView.hashCode() : 0));
            return false;
        }
        Object obj = 1;
        if (this.mState == 0) {
            if (i == 1) {
                int action = motionEvent.getAction();
                if (action == 9 && canStartDirectPenInput(true)) {
                    Slog.d(TAG, "ACTION_HOVER_ENTER");
                    if (getHandler().hasMessages(6)) {
                        Slog.d(TAG, "ACTION_HOVER_ENTER_1");
                        getHandler().removeMessages(6);
                    }
                    if (!getHandler().hasMessages(7)) {
                        Slog.d(TAG, "ACTION_HOVER_ENTER_2");
                        getHandler().sendEmptyMessageDelayed(5, 150);
                        this.mIsPopupCueShowMSGCalled = true;
                    }
                } else if (action == 10 && canStartDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_HOVER_EXIT");
                    InputManager instance = InputManager.getInstance();
                    if (!pointInView(motionEvent.getX(), motionEvent.getY())) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_1");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (instance == null || instance.getScanCodeState(-1, CLIENT_UNIQUE_ID_MASK, 320) != 0) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_4");
                        getHandler().sendEmptyMessageDelayed(6, 40);
                    } else {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    }
                } else if (action == 0 && canStartDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_DOWN");
                    dismissPopupCue(false);
                } else if (action == 1 && canStartDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_UP");
                    getHandler().sendEmptyMessageDelayed(7, 30);
                }
                if (action == 7) {
                    obj = null;
                }
            }
        } else if (this.mState == 1) {
            if (i == 1 && motionEvent.getAction() == 9) {
                int i5 = this.mStateResetCnt + 1;
                this.mStateResetCnt = i5;
                if (i5 > 3) {
                    Slog.d(TAG, "Reset state");
                    this.mStateResetCnt = 0;
                    resetState();
                }
            }
            if (i == 2 && i2 == 1) {
                dismissPopupCue(false);
                resetPenPointerIcon();
                if (this.mParentView instanceof EditText) {
                    this.mParentView.requestFocus();
                }
                InputMethodManager peekInstance = InputMethodManager.peekInstance();
                if (peekInstance != null) {
                    peekInstance.semForceHideSoftInput();
                } else {
                    Slog.d(TAG, "Can not find IMM");
                }
                if (this.mParentView instanceof EditText) {
                    ((EditText) this.mParentView).stopCursorBlink(true);
                    this.mIsCursorBlinkDisabled = true;
                }
                sendHelpModeResult(HELP_MODE_RESULT_OPENED);
                sendWatchActionResult(HELP_MODE_RESULT_OPENED, null);
                getHandler().sendEmptyMessageDelayed(4, 150);
            }
        }
        if (obj != null) {
            Slog.d(TAG, "state : " + this.mState + "." + i + "." + i2 + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null) + ".  " + Integer.toHexString(this.mParentView != null ? this.mParentView.hashCode() : 0));
        }
        return false;
    }

    private boolean scheduleStateForTemplate(int i, int i2, MotionEvent motionEvent, int i3) {
        if (EventChecker.isDuplicated(motionEvent)) {
            return false;
        }
        this.motionEvent = motionEvent;
        Object obj = 1;
        if (i == 2 && i2 == 2) {
            dismissPopupCue(false);
            resetState();
            Slog.d(TAG, "state : " + this.mState + "." + i + ". " + i2 + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null));
            return false;
        }
        if (this.mState == 0) {
            if (i == 1) {
                int action = motionEvent.getAction();
                if (action == 9 && canStartTemplateDirectPenInput(true)) {
                    Slog.d(TAG, "ACTION_HOVER_ENTER");
                    if (!getHandler().hasMessages(7)) {
                        getHandler().sendEmptyMessageDelayed(5, 150);
                        this.mIsPopupCueShowMSGCalled = true;
                    }
                } else if (action == 7) {
                    Slog.d(TAG, "ACTION_HOVER_MOVE");
                    if (getHandler().hasMessages(6)) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        getHandler().removeMessages(6);
                    }
                } else if (action == 10 && canStartTemplateDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_HOVER_EXIT");
                    InputManager instance = InputManager.getInstance();
                    if (!pointInView(motionEvent.getX(), motionEvent.getY())) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_1");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (instance != null && instance.getScanCodeState(-1, CLIENT_UNIQUE_ID_MASK, 320) == 0) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_2");
                        if (dismissPopupCue(true)) {
                            sendHelpModeResult(HELP_MODE_RESULT_HOVER_CANCELED);
                        }
                    } else if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_4");
                        getHandler().sendEmptyMessageDelayed(6, 30);
                    } else {
                        Slog.d(TAG, "ACTION_HOVER_EXIT_3");
                        getHandler().sendEmptyMessageDelayed(6, 30);
                    }
                } else if (action == 0 && canStartTemplateDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_DOWN");
                    dismissPopupCue(true);
                } else if (action == 1 && canStartTemplateDirectPenInput(false)) {
                    Slog.d(TAG, "ACTION_UP");
                    getHandler().sendEmptyMessageDelayed(7, 30);
                }
                if (action == 7) {
                    obj = null;
                }
            }
        } else if (this.mState == 1) {
            if (i == 1) {
                if (motionEvent.getAction() == 9) {
                    int i4 = this.mStateResetCnt + 1;
                    this.mStateResetCnt = i4;
                    if (i4 > 3) {
                        Slog.d(TAG, "Reset state");
                        this.mStateResetCnt = 0;
                        resetState();
                    }
                }
            } else if (i == 2 && i2 == 1) {
                InputMethodManager peekInstance = InputMethodManager.peekInstance();
                if (peekInstance != null) {
                    peekInstance.forceHideSoftInput(new ResultReceiver(new Handler()) {
                        protected void onReceiveResult(int i, Bundle bundle) {
                            SemDirectPenInput.this.getHandler().sendEmptyMessageDelayed(4, 150);
                        }
                    });
                } else {
                    Slog.d(TAG, "Can not find IMM");
                }
                dismissPopupCue(false);
                sendHelpModeResult(HELP_MODE_RESULT_OPENED);
                sendWatchActionResult(HELP_MODE_RESULT_OPENED, null);
                resetPenPointerIcon();
                getHandler().sendEmptyMessageDelayed(4, 200);
            }
        }
        if (obj != null) {
            Slog.d(TAG, "state : " + this.mState + "." + i + ". " + i2 + "." + (motionEvent != null ? Integer.valueOf(motionEvent.getAction()) : null));
        }
        return false;
    }

    private void sendActionButtonResult(int i) {
        if (this.mIsReceiveActionButtonEnabled && this.mButtonClickListener != null) {
            String str = i == 4 ? BUTTON_SEND : i == 3 ? BUTTON_SEARCH : i == 2 ? BUTTON_GO : BUTTON_DONE;
            this.mButtonClickListener.onButtonClick(str);
        }
    }

    private void sendHelpModeResult(CharSequence charSequence) {
        if (this.mIsHelpModeEnabled && this.mTextWritingListener != null) {
            this.mTextWritingListener.onTextReceived(charSequence);
        }
    }

    private void sendMMSDataDelete(CharSequence charSequence) {
        if (this.mTextWritingListener != null) {
            this.mTextWritingListener.onTextReceived(charSequence);
        }
    }

    private void sendWatchActionResult(CharSequence charSequence, Bundle bundle) {
        if (this.mIsWatchActionEnabled && this.mServiceEventListener != null && bundle != null) {
            String string = bundle.getString(SShareConstants.EXTRA_CHOOSER_EM_COMMAND, "");
            CharSequence string2 = bundle.getString("result");
            int i = -1;
            if ("action_done_event".equals(string)) {
                i = 0;
            } else if ("action_cancel_event".equals(string)) {
                i = 1;
            } else if ("action_composing_event".equals(string)) {
                i = 2;
            }
            this.mServiceEventListener.onEvent(i, string2);
        }
    }

    private boolean setupInRuntime() {
        boolean z = true;
        if (!checkDirectPenInputService() && checkUseSamsungIME()) {
            return true;
        }
        int myUserId = UserHandle.myUserId();
        String str = "";
        Slog.d(TAG, " setupInRuntime userId:" + myUserId);
        switch (myUserId) {
            case 10:
                str = "directpeninputmanagerservicerestricted0";
                break;
            case 11:
                str = "directpeninputmanagerservicerestricted1";
                break;
            case 12:
                str = "directpeninputmanagerservicerestricted2";
                break;
            case 13:
                str = "directpeninputmanagerservicerestricted3";
                break;
            case 14:
                str = "directpeninputmanagerservicerestricted4";
                break;
            case 15:
                str = "directpeninputmanagerservicerestricted5";
                break;
            case 100:
                str = "directpeninputmanagerserviceknox0";
                break;
            case 101:
                str = "directpeninputmanagerserviceknox1";
                break;
            case 102:
                str = "directpeninputmanagerserviceknox2";
                break;
            case 103:
                str = "directpeninputmanagerserviceknox3";
                break;
            case 104:
                str = "directpeninputmanagerserviceknox4";
                break;
            case 105:
                str = "directpeninputmanagerserviceknox5";
                break;
            default:
                try {
                    str = "directpeninputmanagerservice";
                    break;
                } catch (Exception e) {
                    Slog.e(TAG, "Failed to get ActivityManager :: get default binder to avoid error, mWBManager:" + this.mDPIManager);
                    z = false;
                    break;
                }
        }
        IBinder service = ServiceManager.getService(str);
        this.mDPIManager = ISemDirectPenInputManager.Stub.asInterface(service);
        Slog.d(TAG, "setupInRuntime binder, binder:" + service + ", CURRENT_SERVICE_NAME:" + str);
        if (this.mDPIManager == null) {
            Slog.e(TAG, "Failed to get DirectPenInputService");
            z = false;
        }
        getHandler();
        this.mEditCount = 0;
        return z;
    }

    private boolean showDirectPenInput() {
        if (checkDirectPenInputService()) {
            return showDirectPenInput_dialog();
        }
        if (!checkUseSamsungIME()) {
            return showDirectPenInput_dialog();
        }
        this.mParentView.requestFocus();
        return InputMethodManager.peekInstance().showSoftInput(this.mParentView, 16);
    }

    private void showDirectPenInputCue() {
        boolean z = false;
        getHandler().removeMessages(5);
        this.mIsPopupCueShowMSGCalled = false;
        if (this.mParentView == null) {
            Slog.d(TAG, "Caencel to show directpeninput cue because mParentView is null");
            return;
        }
        if (this.mParentView.getVisibility() == 0) {
            z = true;
        }
        if (z && (this.mParentView.getParent() instanceof View)) {
            z = ((View) this.mParentView.getParent()).isShown();
        }
        if (z) {
            if (this.mBoardType == 2) {
                Rect visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                Rect rectInWindow = getRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView);
                if (visibleRectInWindow.height() < rectInWindow.height() - 100 || visibleRectInWindow.width() < rectInWindow.width() / 2) {
                    Slog.d(TAG, "Caencel to show directpeninput cue. viewRect is smaller than wndRect");
                    Slog.d(TAG, "viewRect : " + rectInWindow.toShortString());
                    return;
                }
            }
            createPopupCue();
            int i = this.mBoardType == 1 ? this.mIsMultiLineEditor ? 2 : 1 : 3;
            this.mPopupCue.show(i, this.motionEvent);
            sendHelpModeResult(HELP_MODE_RESULT_HOVERED);
            return;
        }
        Slog.d(TAG, "Caencel to show directpeninput cue.");
    }

    private boolean showDirectPenInput_dialog() {
        boolean z = false;
        if (!setupInRuntime()) {
            return false;
        }
        IBinder windowToken = this.mParentView.getWindowToken();
        IBinder applicationWindowToken = this.mParentView.getApplicationWindowToken();
        Rect rect = null;
        Rect rect2 = null;
        if (this.mBoardType == 1) {
            rect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            rect2 = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
        } else if (this.mBoardType == 2) {
            rect = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            rect2 = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
        }
        ExtractedText extractedText;
        EditorInfo editorInfo;
        Rect rect3;
        int i;
        int i2;
        int modeFlag;
        if (this.mBoardType == 1) {
            extractedText = new ExtractedText();
            editorInfo = new EditorInfo();
            EditorInfo editorInfo2 = new EditorInfo();
            if (this.mParentView instanceof EditText) {
                TextView textView = (EditText) this.mParentView;
                textView.extractText(new ExtractedTextRequest(), extractedText);
                textView.extractEditorInfo(editorInfo);
                ((EditText) this.mParentView).getDPITextBuffer(true);
                this.mEditCount = 0;
                int i3 = 0;
                View focusSearch = this.mParentView.focusSearch(1);
                if (focusSearch != null && (focusSearch instanceof EditText) && focusSearch.semIsDirectPenInputEnabled()) {
                    focusSearch.extractEditorInfo(editorInfo2);
                    if ((editorInfo2.imeOptions & 255) == 5 && !isPasswordInputType(focusSearch)) {
                        ViewParent parent = focusSearch.getParent();
                        if (!(parent != null ? parent instanceof NumberPicker : false)) {
                            i3 = 1;
                        }
                    }
                }
                View focusSearch2 = this.mParentView.focusSearch(2);
                if (focusSearch2 != null && (focusSearch2 instanceof EditText) && focusSearch2.semIsDirectPenInputEnabled() && (editorInfo.imeOptions & 255) == 5 && !isPasswordInputType(focusSearch2)) {
                    i3 |= 2;
                }
                if (editorInfo.extras != null) {
                    editorInfo.extras.putInt("flagPrevNext", i3);
                }
            }
            if (TextUtils.isEmpty(editorInfo.packageName)) {
                editorInfo.packageName = this.mParentView.getContext().getPackageName();
            }
            if (this.mParentView.hasFocus() && editorInfo.extras != null) {
                editorInfo.extras.putBoolean("hasFocus", true);
            }
            if ((editorInfo.inputType & 4080) == 16 && "com.sec.android.app.sbrowser".equals(editorInfo.packageName) && !this.mParentView.hasFocus()) {
                this.mIgnoreSizeChange = true;
                this.mInitRect = new Rect(rect);
            } else {
                this.mIgnoreSizeChange = false;
            }
            if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                rect3 = new Rect();
                rect3.right = rect.width();
                rect3.bottom = rect.height();
            } else {
                rect3 = this.mPopupCue.getRectInAnchor();
            }
            this.mViewID = ((this.mParentView.hashCode() & ActionBase.MASK_ATTR_NUMBERING) << 20) | ((this.mParentView.getId() & ActionBase.MASK_ATTR_NUMBERING) << 8);
            i = this.mViewID;
            i2 = this.mShowCnt + 1;
            this.mShowCnt = i2;
            this.mViewID = i | (i2 & 255);
            modeFlag = getModeFlag();
            if ((modeFlag & 8) != 0) {
                dismissPopupCue(true);
            }
            try {
                this.mDPIManager.show(this.mViewID, this.mServiceCallback.asBinder(), applicationWindowToken, windowToken, rect, rect2, rect3, extractedText, editorInfo, modeFlag, this.mIsWatchActionEnabled);
                Slog.d(TAG, "startDirectPenInput " + this.mViewID + " " + this.mParentView);
                this.mParentView.reportCurrentDirectPenInputView(this.mParentView);
                Slog.d(TAG, "Report current DPI : " + this.mParentView);
                registerPositionChangeListener();
                if (this.mParentView instanceof EditText) {
                    ((EditText) this.mParentView).hideCursorControllers();
                    ((TextView) this.mParentView).clearAllMultiSelection();
                }
                this.mCanShowAutoCompletePopup = false;
                this.mState = 1;
                z = true;
            } catch (Throwable e) {
                Slog.e(TAG, "Can not start DirectPenInput, RemoteException happened " + e.toString());
                this.mDPIManager = null;
                resetState();
                z = false;
            }
        } else if (this.mBoardType == 2) {
            extractedText = new ExtractedText();
            editorInfo = new EditorInfo();
            if (this.mEditorType == 1) {
                editorInfo.inputType = 2;
            } else {
                editorInfo.inputType = 1;
            }
            editorInfo.imeOptions = 6;
            editorInfo.packageName = this.mParentView.getContext().getPackageName();
            if (this.mTextUpdateListener != null) {
                extractedText.text = this.mTextUpdateListener.onTextUpdated(extractedText.text);
            }
            if (this.mPopupCue == null || !this.mPopupCue.isShowing()) {
                rect3 = new Rect();
                rect3.right = rect.width();
                rect3.bottom = rect.height();
            } else {
                rect3 = this.mPopupCue.getRectInAnchor();
            }
            this.mViewID = ((this.mParentView.hashCode() & ActionBase.MASK_ATTR_NUMBERING) << 20) | ((this.mParentView.getId() & ActionBase.MASK_ATTR_NUMBERING) << 8);
            i = this.mViewID;
            i2 = this.mShowCnt + 1;
            this.mShowCnt = i2;
            this.mViewID = i | (i2 & 255);
            modeFlag = getModeFlag();
            if (this.mParentView.hideCursorControllers(this.mParentView)) {
                Slog.d(TAG, "hideCursorControllers ");
            }
            try {
                this.mDPIManager.showTemplate(this.mViewID, this.mServiceCallback.asBinder(), applicationWindowToken, windowToken, rect, rect2, rect3, this.mBoardTemplate, extractedText, editorInfo, modeFlag);
                Slog.d(TAG, "startDirectPenInput. " + this.mViewID + " " + this.mParentView);
                this.mParentView.reportCurrentDirectPenInputView(this.mParentView);
                Slog.d(TAG, "Report current DPI : " + this.mParentView);
                this.mState = 1;
                z = true;
            } catch (Throwable e2) {
                Slog.e(TAG, "Can not start DirectPenInput, RemoteException happened" + e2.toString());
                this.mDPIManager = null;
                resetState();
                return false;
            }
        }
        getHandler().sendEmptyMessageDelayed(6, 1000);
        return z;
    }

    private void startDirectPenInputService() {
        Context context = null;
        int myUserId;
        Intent intent;
        if (checkDirectPenInputService()) {
            try {
                myUserId = UserHandle.myUserId();
                Slog.w(TAG, "Starting directpeninput service id : " + myUserId);
                intent = new Intent();
                intent.setComponent(new ComponentName("com.samsung.android.directpeninputservice", "com.samsung.android.directpeninputservice.DirectPenInputServiceStarter"));
                if (this.mParentView != null) {
                    context = this.mParentView.getContext();
                }
                if (context != null) {
                    context.startServiceAsUser(intent, new UserHandle(myUserId));
                }
            } catch (Exception e) {
                Slog.w(TAG, "Starting directpeninput service failed: " + e);
            }
        } else if (checkUseSamsungIME()) {
            try {
                myUserId = UserHandle.myUserId();
                Slog.w(TAG, "Starting directpeninput service id : " + myUserId);
                intent = new Intent();
                intent.setComponent(new ComponentName("com.sec.android.inputmethod", "com.sec.android.inputmethod.SamsungKeypad"));
                if (this.mParentView != null) {
                    context = this.mParentView.getContext();
                }
                if (context != null) {
                    context.startServiceAsUser(intent, new UserHandle(myUserId));
                }
            } catch (Exception e2) {
                Slog.w(TAG, "Starting directpeninput service failed: " + e2);
            }
        } else {
            try {
                myUserId = UserHandle.myUserId();
                Slog.w(TAG, "Starting directpeninput service id : " + myUserId);
                intent = new Intent();
                intent.setComponent(new ComponentName("com.sec.android.inputmethod", "com.sec.android.inputmethod.directpeninput.DirectPenInputServiceStarter"));
                if (this.mParentView != null) {
                    context = this.mParentView.getContext();
                }
                if (context != null) {
                    context.startServiceAsUser(intent, new UserHandle(myUserId));
                }
            } catch (Exception e22) {
                Slog.w(TAG, "Starting directpeninput service failed: " + e22);
            }
        }
    }

    private void unregisterPositionChangeListener() {
        if (this.mBoardType == 1) {
            if (this.mParentView instanceof EditText) {
                ((EditText) this.mParentView).setDPIPositionListenerEnalbed(false);
            }
        } else if (this.mParentView != null) {
            ViewTreeObserver viewTreeObserver = this.mParentView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.removeOnScrollChangedListener(this.mOnScrollChangedListener);
            }
        }
    }

    public boolean canShowAutoCompletePopup() {
        return this.mState == 1 ? this.mCanShowAutoCompletePopup : true;
    }

    public void dismiss() {
        Slog.d(TAG, "Finish DirectPenInput");
        if (this.mIsPerformingAction && this.mIsImageWritingEnabled) {
            Slog.d(TAG, "Cancel finish.");
        } else {
            dismiss(true);
        }
    }

    public void dismiss(boolean z) {
        Slog.d(TAG, "Finish : " + z);
    }

    public Bitmap getBitmap() {
        return null;
    }

    public int getEditorType() {
        return this.mEditorType;
    }

    public Rect getExpectedTargetDPIRect() {
        return getTargetDPIRect(this.mParentView, this.mBoardType);
    }

    public Rect getTargetDPIRect() {
        return this.mDPIRect;
    }

    public boolean handleMotionEvent(View view, MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) != 2 || this.mIsWaitingHideSoftInput) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 9) {
            Slog.d(TAG, "handleMotionEvent ACTION_HOVER_ENTER");
            this.mIsHoverState = true;
            if (!setupInRuntime()) {
                startDirectPenInputService();
            }
        } else if (action == 10) {
            Slog.d(TAG, "handleMotionEvent ACTION_HOVER_EXIT");
            this.mIsHoverState = false;
        }
        return scheduleState(1, 0, motionEvent, 0);
    }

    public boolean handleWindowFocusChanged(boolean z) {
        if (z) {
            if (this.mBoardType == 2 && this.mPopupCue != null && this.mPopupCue.isShowing()) {
                this.mPopupCue.dismiss(false);
                this.mPopupCue = null;
            }
            if (this.mBoardType == 1 && isDPIShowing()) {
                Slog.d(TAG, "Update.");
                this.mHandler.sendEmptyMessage(4);
            }
        } else {
            if (this.mPopupCue != null && this.mPopupCue.isShowing()) {
                this.mPopupCue.dismiss(false);
                this.mPopupCue = null;
            }
            if (this.mIsPopupCueShowMSGCalled && this.mHandler != null) {
                this.mHandler.removeMessages(5);
                this.mIsPopupCueShowMSGCalled = false;
            }
        }
        return false;
    }

    public boolean isShowing() {
        return isDPIShowing();
    }

    public void notifyPositionChanged(int i) {
        Slog.d(TAG, "notifyPositionChanged code : " + i + " " + this.mState);
        if (this.mState != 0) {
            Rect visibleRectInWindow;
            Rect visibleRectOnScreen;
            if (this.mBoardType == 1) {
                visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
                visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            } else {
                visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            }
            if (!this.mIgnoreSizeChange || this.mInitRect == null || (this.mInitRect.width() == visibleRectInWindow.width() && this.mInitRect.height() == visibleRectInWindow.height())) {
                if (DEBUG) {
                    Slog.d(TAG, "Update Position. wnd : " + visibleRectInWindow + " scr : " + visibleRectOnScreen);
                }
                try {
                    if (this.mDPIManager != null) {
                        this.mDPIManager.updatePosition(this.mViewID, visibleRectInWindow, visibleRectOnScreen);
                        if (this.mBoardType == 1) {
                            this.mScrRectUpdated = new Rect(visibleRectOnScreen);
                            getHandler().sendEmptyMessageDelayed(9, 300);
                        }
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "Can not start DirectPenInput, RemoteException happened");
                }
            }
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        return null;
    }

    public void onResultReceived(Bundle bundle, int i) {
        Slog.d(TAG, "onResultReceived " + bundle + " receivedClientID : " + Integer.toHexString(i) + " current ClientID : " + Integer.toHexString(this.mViewID));
        if (bundle != null && this.mParentView != null) {
            if (bundle.getInt(SERVICE_CB_CLIENT_CHANGED) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_CLIENT_CHANGED ");
                if (this.mBoardType == 1 && (this.mParentView instanceof EditText)) {
                    if (this.mIsCursorBlinkDisabled) {
                        ((EditText) this.mParentView).stopCursorBlink(false);
                        this.mIsCursorBlinkDisabled = false;
                    }
                    if (this.mState == 1 && this.mEditCount > 0 && this.mParentView.isShown() && this.mParentView.getWindowVisibility() == 0) {
                        ((EditText) this.mParentView).applyDPITextBuffer(true);
                    }
                    ((EditText) this.mParentView).setDPITextBuffer(null);
                    this.mEditCount = 0;
                }
                if (this.mParentView.equals(this.mParentView.getCurrentDirectPenInputView())) {
                    this.mParentView.reportCurrentDirectPenInputView(null);
                    Slog.d(TAG, "Report current DPI : " + null);
                }
                unregisterPositionChangeListener();
                this.mState = 0;
            }
            if (bundle.getInt(SERVICE_CB_INFLATE_DONE) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_INFLATE_DONE ");
                scheduleState(2, 1, null, i);
            }
            int i2 = bundle.getInt(SERVICE_CB_WRITING_DONE);
            if (i2 > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_WRITING_DONE " + i2);
                this.mCanShowAutoCompletePopup = true;
                if (this.mParentView instanceof EditText) {
                    boolean z = this.mParentView.getVisibility() == 0;
                    if (z && (this.mParentView.getParent() instanceof View)) {
                        z = ((View) this.mParentView.getParent()).isShown();
                    }
                    if (this.mEditCount > 0 && r2) {
                        ((EditText) this.mParentView).applyDPITextBuffer(i2 == 1);
                    }
                    ((EditText) this.mParentView).setDPITextBuffer(null);
                    this.mEditCount = 0;
                }
            }
            int i3 = bundle.getInt(SERVICE_CB_DATA_CHANGED, -1);
            if (i3 >= 0) {
                if (this.mParentView instanceof EditText) {
                    Slog.d(TAG, "onResultReceived SERVICE_CB_DATA_CHANGED " + i3);
                    this.mIsPerformingAction = true;
                    ((EditText) this.mParentView).performDPIEditorAction(i3);
                    this.mIsPerformingAction = false;
                }
                sendActionButtonResult(i3);
            }
            if (bundle.getInt(SERVICE_CB_CLOSED) > 0) {
                Slog.d(TAG, "onResultReceived SERVICE_CB_CLOSED ");
                CharSequence string = bundle.getString(SERVICE_CB_INIT_TEXT);
                if (bundle.getInt(SERVICE_CB_CLOSED, -1) == 1) {
                    ((EditText) this.mParentView).setDPITextBuffer(string);
                    ((EditText) this.mParentView).applyDPITextBuffer(true);
                }
                scheduleState(2, 2, null, i);
                sendHelpModeResult(HELP_MODE_RESULT_CLOSED);
                sendWatchActionResult(HELP_MODE_RESULT_CLOSED, null);
            }
            if (bundle.getInt(SERVICE_CB_PRIVATE) > 0) {
                sendWatchActionResult(SERVICE_CB_PRIVATE, bundle);
            }
        }
    }

    public void onTextDeleted(int i, int i2) {
        if (DEBUG) {
            Slog.d(TAG, "onTextDeleted() : " + i + " " + i2);
        }
        if (this.mParentView instanceof EditText) {
            CharSequence dPITextBuffer = ((EditText) this.mParentView).getDPITextBuffer(false);
            if (DEBUG) {
                Slog.d(TAG, "onTextDeleted() : " + (dPITextBuffer != null ? Integer.valueOf(dPITextBuffer.length()) : null));
            }
            if (dPITextBuffer instanceof Editable) {
                Editable editable = (Editable) dPITextBuffer;
                if (dPITextBuffer.length() >= i2 - i) {
                    if (dPITextBuffer.length() < i2) {
                        Slog.d(TAG, "onTextDeleted() : end is out of bound textBuffer length");
                        return;
                    }
                    editable.delete(i, i2);
                } else {
                    return;
                }
            }
            this.mEditCount++;
        }
    }

    public void onTextInserted(int i, CharSequence charSequence, int i2) {
        Object obj = null;
        if (DEBUG) {
            Slog.d(TAG, "onTextInserted() : " + i + " " + (charSequence != null ? Integer.valueOf(charSequence.length()) : null) + " " + i2);
        }
        if (this.mBoardType == 2) {
            if (!(this.mTextWritingListener == null || charSequence == null)) {
                this.mTextWritingListener.onTextReceived(charSequence);
            }
            return;
        }
        if ((this.mParentView instanceof EditText) && this.mState == 1) {
            CharSequence dPITextBuffer = ((EditText) this.mParentView).getDPITextBuffer(false);
            if (DEBUG) {
                String str = TAG;
                StringBuilder append = new StringBuilder().append("onTextInserted() : ");
                if (dPITextBuffer != null) {
                    obj = Integer.valueOf(dPITextBuffer.length());
                }
                Slog.d(str, append.append(obj).toString());
            }
            if (dPITextBuffer instanceof Editable) {
                Editable editable = (Editable) dPITextBuffer;
                if (editable.length() < i) {
                    Slog.d(TAG, "onTextInserted() : where is out of bound editor length");
                    return;
                }
                if (charSequence != null && editable.length() + charSequence.length() < i2) {
                    i2 = editable.length() + charSequence.length();
                    Slog.d(TAG, "onTextInserted() : nextCursor position is more than total text length, set nextCursor to end of text");
                }
                if (charSequence != null) {
                    editable.insert(i, charSequence);
                    Selection.setSelection(editable, i2);
                }
            }
            if (!(charSequence == null || charSequence.length() == 0)) {
                this.mEditCount++;
            }
            if (!TextUtils.isEmpty(charSequence)) {
                sendHelpModeResult(HELP_MODE_RESULT_TEXT_INSERTED);
            }
        }
    }

    public void onUpdateDialog() {
        Slog.d(TAG, "onUpdateDialog code : " + this.mState);
        if (this.mState != 0) {
            Rect visibleRectInWindow;
            Rect visibleRectOnScreen;
            if (this.mBoardType == 1) {
                visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
                visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, false);
            } else {
                visibleRectInWindow = getVisibleRectInWindow(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
                visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView != null ? this.mAnchorView : this.mParentView, true);
            }
            if (DEBUG) {
                Slog.d(TAG, "Update onUpdateDialog. wnd : " + visibleRectInWindow + " scr : " + visibleRectOnScreen);
            }
            try {
                if (this.mDPIManager != null) {
                    this.mDPIManager.updateDialog(this.mViewID, visibleRectInWindow, visibleRectOnScreen);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Can not start DirectPenInput, RemoteException happened");
            }
        }
    }

    public void setAnchorView(View view) {
        this.mAnchorView = view;
    }

    public void setBoardTemplate(int i) {
        this.mBoardTemplate = i;
    }

    public void setBoardType(int i) {
        if (i == 1 || i == 2) {
            this.mBoardType = i;
            return;
        }
        throw new IllegalArgumentException("The board type should be one of TYPE_BOARD_EDITOR or TYPE_BOARD_TEMPLATE");
    }

    public void setEditorType(int i) {
        if (i == 1 || i == 2) {
            this.mEditorType = i;
            return;
        }
        throw new IllegalArgumentException("You should set the Drawable, String, subDescription and Object in Param");
    }

    public void setImageWritingEnabled(boolean z) {
        this.mIsImageWritingEnabled = z;
    }

    public void setImageWritingListener(ImageWritingListener imageWritingListener) {
        setImageWritingEnabled(imageWritingListener != null);
        this.mImageWritingListener = imageWritingListener;
    }

    public void setMathWritingEnabled(boolean z) {
        this.mIsMathWritingEnabled = z;
    }

    public void setOnButtonClickListner(OnButtonClickListener onButtonClickListener) {
        this.mIsReceiveActionButtonEnabled = true;
        this.mButtonClickListener = onButtonClickListener;
    }

    public void setParentView(View view) {
        if (view == null) {
            Slog.d(TAG, "Reset parent View");
            this.mParentView = null;
            this.mAnchorView = null;
            this.mPopupCue = null;
            this.mDPIManager = null;
            this.mHandler = null;
            return;
        }
        this.mParentView = view;
    }

    public void setServiceEventListner(ServiceEventListener serviceEventListener) {
        this.mIsWatchActionEnabled = true;
        this.mServiceEventListener = serviceEventListener;
    }

    public void setTextUpdateListener(TextUpdateListener textUpdateListener) {
        this.mTextUpdateListener = textUpdateListener;
    }

    public void setTextWritingListener(TextWritingListener textWritingListener) {
        this.mTextWritingListener = textWritingListener;
    }

    public boolean show() {
        return false;
    }

    public boolean showAsDialog() {
        return false;
    }

    public void showPopup() {
        Slog.d(TAG, "showPopup");
        try {
            if (this.mDPIManager != null) {
                this.mDPIManager.showPopup(this.mViewID, 0);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Can not start showPopup, RemoteException happened");
        }
    }

    public void startDirectPenInputServiceByForce() {
        Context context = null;
        int myUserId;
        Intent intent;
        if (checkDirectPenInputService()) {
            try {
                myUserId = UserHandle.myUserId();
                Slog.w(TAG, "Starting directpeninput service id : " + myUserId);
                intent = new Intent();
                intent.setComponent(new ComponentName("com.samsung.android.directpeninputservice", "com.samsung.android.directpeninputservice.DirectPenInputServiceStarter"));
                if (this.mParentView != null) {
                    context = this.mParentView.getContext();
                }
                if (context != null) {
                    context.startServiceAsUser(intent, new UserHandle(myUserId));
                    return;
                }
                return;
            } catch (Exception e) {
                Slog.w(TAG, "Starting directpeninput service failed: " + e);
                return;
            }
        }
        try {
            myUserId = UserHandle.myUserId();
            Slog.w(TAG, "Starting directpeninput service id : " + myUserId);
            intent = new Intent();
            intent.setComponent(new ComponentName("com.sec.android.inputmethod", "com.sec.android.inputmethod.directpeninput.DirectPenInputServiceStarter"));
            if (this.mParentView != null) {
                context = this.mParentView.getContext();
            }
            if (context != null) {
                context.startServiceAsUser(intent, new UserHandle(myUserId));
            }
        } catch (Exception e2) {
            Slog.w(TAG, "Starting directpeninput service failed: " + e2);
        }
    }
}
