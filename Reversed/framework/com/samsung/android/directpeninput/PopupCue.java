package com.samsung.android.directpeninput;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.TypedValue;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.samsung.android.fingerprint.FingerprintEvent;

public class PopupCue {
    private static final boolean DEBUG = "eng".equals(Build.TYPE);
    private static final boolean ENABLE_FLOATING_VISUAL_CUE_POSITION_X = false;
    private static final boolean ENABLE_FLOATING_VISUAL_CUE_POSITION_Y = false;
    private static final String TAG = "WritingBuddyPopupCue";
    public static final int TYPE_MULTILINE_EDITOR = 2;
    public static final int TYPE_NONFORM_VIEW = 3;
    public static final int TYPE_SINGLELINE_EDITOR = 1;
    private View mAnchorView;
    private Context mContext;
    private CueContainer mCueContainerView;
    private OnHoverListener mHoverListner;
    private IWindowManager mIWindowManager = null;
    private boolean mIsAirButtonClicked;
    private boolean mIsShowing;
    private int mPopupHeight;
    private int mPopupPosX;
    private int mPopupPosY;
    private int mPopupWidth;
    private int mPopupXfromAnchor;
    private int mPopupYfromAnchor;
    private OnTouchListener mTouchListner;
    private int mType;
    private WindowManager mWindowManager;
    private IBinder mWindowToken;

    private class CueContainer extends FrameLayout {
        private Context mContext;
        private View mHoverCue;
        private View mTouchCue;

        public CueContainer(Context context) {
            super(context);
            this.mContext = context;
            initLayout();
        }

        private void initLayout() {
            this.mTouchCue = new View(this.mContext);
            this.mTouchCue.setBackgroundResource(17304354);
            this.mHoverCue = new View(this.mContext);
            this.mHoverCue.setBackgroundResource(17304353);
            addView(this.mTouchCue);
            addView(this.mHoverCue);
            this.mHoverCue.setVisibility(0);
            this.mTouchCue.setVisibility(4);
        }

        public Drawable getHoverCueDrawable() {
            return this.mHoverCue != null ? this.mHoverCue.getBackground() : this.mContext.getResources().getDrawable(17304353);
        }

        public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 7 && motionEvent.getButtonState() == 2) {
                PopupCue.this.mIsAirButtonClicked = true;
            }
            return true;
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        public void switchCueButton(boolean z) {
            if (z) {
                this.mHoverCue.setVisibility(4);
                this.mTouchCue.setVisibility(0);
                return;
            }
            this.mHoverCue.setVisibility(0);
            this.mTouchCue.setVisibility(4);
        }
    }

    public PopupCue(View view) {
        this.mAnchorView = view;
        this.mContext = view.getContext();
        initPopup();
    }

    private void computePosition(int i, MotionEvent motionEvent) {
        int i2;
        int height;
        int i3;
        int i4;
        int i5 = 0;
        int i6 = 0;
        Drawable hoverCueDrawable = this.mCueContainerView.getHoverCueDrawable();
        if (hoverCueDrawable != null) {
            i5 = hoverCueDrawable.getIntrinsicWidth();
            i6 = hoverCueDrawable.getIntrinsicHeight();
        }
        this.mWindowToken = this.mAnchorView.getApplicationWindowToken();
        Rect visibleRectOnScreen = getVisibleRectOnScreen(this.mAnchorView);
        Rect rect = new Rect(visibleRectOnScreen);
        rect.left = visibleRectOnScreen.left;
        rect.right = visibleRectOnScreen.right;
        if (motionEvent != null) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
        }
        if (this.mAnchorView instanceof EditText) {
            EditText editText = (EditText) this.mAnchorView;
            Layout layout = editText.getLayout();
            int compoundPaddingStart = editText.getCompoundPaddingStart();
            int compoundPaddingTop = editText.getCompoundPaddingTop();
            int compoundPaddingBottom = editText.getCompoundPaddingBottom();
            int dimension = (int) this.mContext.getResources().getDimension(17105822);
            int dimension2 = (int) this.mContext.getResources().getDimension(17105823);
            if (layout == null || editText.getBaseline() <= 0) {
                i2 = this.mAnchorView.getLayoutDirection() == 1 ? ((rect.right - compoundPaddingStart) - i5) - dimension : (rect.left + compoundPaddingStart) + dimension;
                height = (rect.top + (compoundPaddingTop + ((rect.height() - (compoundPaddingTop + compoundPaddingBottom)) / 2))) - (i6 - dimension2);
            } else {
                height = (rect.top + ((editText.getBaseline() + layout.getLineDescent(0)) - ((layout.getLineBottom(0) - layout.getLineTop(0)) / 2))) - (i6 - dimension2);
                i2 = this.mAnchorView.getLayoutDirection() == 1 ? ((rect.right - compoundPaddingStart) - i5) - dimension : (rect.left + compoundPaddingStart) + dimension;
            }
            if (i2 + i5 > rect.right) {
                i2 = rect.right - i5;
            }
            if (i2 < 0) {
                i2 = 0;
            }
            int statusBarHeight = getStatusBarHeight();
            if (height < statusBarHeight && isStatusBarShowing()) {
                height = statusBarHeight;
            }
            int lowestListParentY = getLowestListParentY(this.mAnchorView);
            if (lowestListParentY >= 0 && r25 < lowestListParentY) {
                height = lowestListParentY;
            }
            if (height < 0) {
                height = 0;
            }
            i3 = i2 - rect.left;
            i4 = height - rect.top;
        } else {
            i2 = rect.left + ((int) this.mContext.getResources().getDimension(17105820));
            height = rect.top + ((int) this.mContext.getResources().getDimension(17105821));
            if (i2 + i5 > rect.right) {
                i2 = rect.right - i5;
            }
            if (height + i6 > rect.bottom) {
                height = rect.bottom - i6;
            }
            if (i2 < rect.left) {
                i2 = rect.left;
            }
            if (height < rect.top) {
                height = rect.top;
            }
            i3 = i2 - rect.left;
            i4 = height - rect.top;
        }
        this.mPopupXfromAnchor = i3;
        this.mPopupYfromAnchor = i4;
        this.mPopupPosX = i2;
        this.mPopupPosY = height;
        this.mPopupWidth = i5;
        this.mPopupHeight = i6;
        if (DEBUG) {
            Slog.d(TAG, "computePosition x : " + this.mPopupPosX + " y : " + this.mPopupPosY + " w : " + this.mPopupWidth + " h : " + this.mPopupHeight);
        }
    }

    private int convertDPtoPX(float f, DisplayMetrics displayMetrics) {
        DisplayMetrics displayMetrics2 = displayMetrics;
        if (displayMetrics == null) {
            displayMetrics2 = this.mAnchorView.getContext().getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, f, displayMetrics2) + 0.5f);
    }

    private void createPopup() {
        if (this.mCueContainerView == null) {
            this.mCueContainerView = new CueContainer(this.mContext);
            if (this.mTouchListner != null) {
                this.mCueContainerView.setOnTouchListener(this.mTouchListner);
            }
            if (this.mHoverListner != null) {
                this.mCueContainerView.setOnHoverListener(this.mHoverListner);
            }
        }
    }

    private LayoutParams createPopupLayoutParam() {
        if (DEBUG) {
            Slog.d(TAG, "createPopupLayoutParam() x : " + this.mPopupPosX + " y :" + this.mPopupPosY + "  w : " + this.mPopupWidth + " h : " + this.mPopupHeight);
        }
        ViewGroup.LayoutParams layoutParams = new LayoutParams();
        layoutParams.gravity = 51;
        layoutParams.width = this.mPopupWidth;
        layoutParams.height = this.mPopupHeight;
        layoutParams.x = this.mPopupPosX;
        layoutParams.y = this.mPopupPosY;
        layoutParams.token = this.mWindowToken;
        layoutParams.format = -3;
        layoutParams.setTitle("WritingBuddyCue : " + Integer.toHexString(hashCode()));
        layoutParams.type = 1000;
        layoutParams.flags |= 256;
        layoutParams.flags |= 8;
        layoutParams.flags |= 512;
        layoutParams.flags |= FingerprintEvent.IMAGE_QUALITY_WET_FINGER;
        layoutParams.windowAnimations = 16975135;
        return layoutParams;
    }

    private int getLowestListParentY(View view) {
        ViewParent parent = view.getParent();
        int i = -1;
        while (parent instanceof View) {
            View view2 = (View) parent;
            if ((view2 instanceof AbsListView) || view2.getClass().getSimpleName().contains("RecyclerView")) {
                View view3 = view2;
                int[] iArr = new int[2];
                view2.getLocationInWindow(iArr);
                if (iArr[1] > i) {
                    i = iArr[1];
                }
            }
            parent = view2.getParent();
        }
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

    private int getStatusBarHeight() {
        int i = 0;
        try {
            i = this.mContext.getResources().getDimensionPixelSize(17104919);
        } catch (Throwable e) {
            Slog.d(TAG, e.toString());
        }
        return i;
    }

    private Rect getVisibleRectInWindow(View view) {
        Rect rectInWindow = getRectInWindow(view);
        View view2 = view;
        ViewParent parent = view.getParent();
        int i = 0;
        int i2 = 0;
        while (parent instanceof View) {
            View view3 = (View) parent;
            i += (int) view2.getY();
            if (view3.getScrollY() > 0) {
                if (view3.getScrollY() > i) {
                    rectInWindow.top += view3.getScrollY() - i;
                    i = 0;
                } else {
                    i -= view3.getScrollY();
                }
            }
            int y = (((int) view2.getY()) + view2.getHeight()) - view3.getScrollY();
            if (y + i2 < view3.getHeight()) {
                i2 = -(view3.getHeight() - (y + i2));
            } else {
                rectInWindow.bottom -= (y + i2) - view3.getHeight();
                i2 = 0;
            }
            view2 = view3;
            parent = view3.getParent();
        }
        Slog.d(TAG, "getVisibleRectInWindow : " + rectInWindow.toShortString());
        return rectInWindow;
    }

    private Rect getVisibleRectOnScreen(View view) {
        Rect rectOnScreen = getRectOnScreen(view);
        View view2 = view;
        ViewRootImpl parent = view.getParent();
        int i = 0;
        int i2 = 0;
        while (parent instanceof View) {
            View view3 = (View) parent;
            i += (int) view2.getY();
            if (view3.getScrollY() > 0) {
                if (view3.getScrollY() > i) {
                    rectOnScreen.top += view3.getScrollY() - i;
                    i = 0;
                } else {
                    i -= view3.getScrollY();
                }
            }
            int y = (((int) view2.getY()) + view2.getHeight()) - view3.getScrollY();
            if (y + i2 < view3.getHeight()) {
                i2 = -(view3.getHeight() - (y + i2));
            } else {
                rectOnScreen.bottom -= (y + i2) - view3.getHeight();
                i2 = 0;
            }
            view2 = view3;
            parent = view3.getParent();
        }
        if (parent instanceof ViewRootImpl) {
            boolean z = true;
            if (this.mContext instanceof Activity) {
                z = ((Activity) this.mContext).isInMultiWindowMode();
            }
            if (z) {
                Rect rectOnScreen2 = getRectOnScreen(parent.mParentDecorView);
                rectOnScreen.left -= rectOnScreen2.left;
                rectOnScreen.top -= rectOnScreen2.top;
            }
        }
        Slog.d(TAG, "getVisibleRectOnScreen : " + rectOnScreen.toShortString());
        return rectOnScreen;
    }

    private void initPopup() {
        this.mPopupWidth = 0;
        this.mPopupHeight = 0;
        this.mPopupPosX = 0;
        this.mPopupPosY = 0;
        this.mType = 3;
        this.mIsShowing = false;
        this.mWindowToken = null;
        this.mWindowManager = null;
        this.mCueContainerView = null;
        this.mTouchListner = null;
        this.mHoverListner = null;
    }

    private boolean pointInView(View view, float f, float f2) {
        return f >= 0.0f && f < ((float) (view.getRight() - view.getLeft())) && f2 >= 0.0f && f2 < ((float) (view.getBottom() - view.getTop()));
    }

    public void dismiss(boolean z) {
        if (isShowing() && this.mCueContainerView != null) {
            ViewGroup.LayoutParams layoutParams = this.mCueContainerView.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ViewGroup.LayoutParams layoutParams2 = layoutParams;
                int i = z ? 16975135 : 0;
                if (layoutParams2.windowAnimations != i) {
                    layoutParams.windowAnimations = i;
                    this.mWindowManager.updateViewLayout(this.mCueContainerView, layoutParams);
                }
            }
            this.mWindowManager.removeView(this.mCueContainerView);
        }
        this.mCueContainerView = null;
        this.mIsShowing = false;
        this.mIsAirButtonClicked = false;
    }

    public IWindowManager getIWindowManager() {
        if (this.mIWindowManager == null) {
            this.mIWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        }
        return this.mIWindowManager;
    }

    public Rect getRectInAnchor() {
        Rect rect = new Rect();
        rect.left = this.mPopupXfromAnchor;
        rect.top = this.mPopupYfromAnchor;
        rect.right = rect.left + this.mPopupWidth;
        rect.bottom = rect.top + this.mPopupHeight;
        return rect;
    }

    public boolean isAirButtonClicked() {
        return this.mIsAirButtonClicked;
    }

    public boolean isPointInPopup(float f, float f2) {
        return f >= 0.0f && f <= ((float) this.mPopupWidth) && f2 >= 0.0f && f2 <= ((float) this.mPopupHeight);
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public boolean isStatusBarShowing() {
        return false;
    }

    public void setOnHoverListener(OnHoverListener onHoverListener) {
        this.mHoverListner = onHoverListener;
        if (this.mCueContainerView != null) {
            this.mCueContainerView.setOnHoverListener(onHoverListener);
        }
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mTouchListner = onTouchListener;
        if (this.mCueContainerView != null) {
            this.mCueContainerView.setOnTouchListener(onTouchListener);
        }
    }

    public void setPosition(int i, int i2) {
        this.mPopupPosX = i;
        this.mPopupPosY = i2;
    }

    public void setSize(int i, int i2) {
        this.mPopupWidth = i;
        this.mPopupHeight = i2;
    }

    public void setWindowToken(IBinder iBinder) {
        this.mWindowToken = iBinder;
    }

    public void show(int i, MotionEvent motionEvent) {
        this.mType = i;
        this.mIsAirButtonClicked = false;
        if (!isShowing()) {
            createPopup();
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
            }
            computePosition(i, motionEvent);
            this.mWindowManager.addView(this.mCueContainerView, createPopupLayoutParam());
            this.mIsShowing = true;
        }
    }

    public void switchCueButton(boolean z) {
        this.mCueContainerView.switchCueButton(z);
    }

    public void updatePopupPosition(MotionEvent motionEvent) {
        LayoutParams layoutParams = (LayoutParams) this.mCueContainerView.getLayoutParams();
        if (DEBUG) {
            Slog.d(TAG, "updatePopupPosition()");
        }
        computePosition(this.mType, null);
        layoutParams.x = this.mPopupPosX;
        layoutParams.y = this.mPopupPosY;
        layoutParams.width = this.mPopupWidth;
        layoutParams.height = this.mPopupHeight;
        this.mWindowManager.updateViewLayout(this.mCueContainerView, layoutParams);
    }
}
