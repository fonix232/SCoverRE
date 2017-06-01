package com.samsung.android.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.internal.R;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.samsung.android.framework.res.C0078R;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.media.mediacapture.SemMediaCapture;
import com.samsung.android.smartface.SmartFaceManager;

public class SemHoverPopupWindow {
    private static final String AIRCOMMAND_MORPH_USP = SystemProperties.get("ro.aircommand.morph.usp");
    private static final boolean ANIMATION_BY_POINTER_POSITION_ENABLED = "2016B".equals(SystemProperties.get("ro.build.scafe.version"));
    static final boolean DEBUG = false;
    private static final String DEVICE_TYPE = SystemProperties.get("ro.build.characteristics");
    private static final int HOVER_DETECT_TIME_MS = 300;
    private static final int HOVER_DETECT_TIME_MS_DEX = 750;
    private static final int MSG_DISMISS_POPUP = 2;
    private static final int MSG_SHOW_POPUP = 1;
    private static final int MSG_TIMEOUT = 1;
    private static final int POPUP_TIMEOUT_MS = 10000;
    private static final int POPUP_TIMEOUT_MS_DEX = 5000;
    private static final boolean SUPPORT_DEX_MODE = true;
    static final String TAG = "SemHoverPopupWindow";
    private static final int TIMEOUT_DELAY = 500;
    private static final int TIMEOUT_DELAY_LONG = 2000;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_TOOLTIP = 1;
    public static final int TYPE_USER_CUSTOM = 3;
    public static final int TYPE_WIDGET_DEFAULT = 2;
    private static final int UI_THREAD_BUSY_TIME_MS = 1000;
    private static boolean mIsCheckedRealDisplayMetricsInDexMode = false;
    private static boolean mIsTaskBarInBottomInDexMode = true;
    private static DisplayMetrics mRealDisplayMetricsInDexMode = new DisplayMetrics();
    private final int ANCHORVIEW_COORDINATES_TYPE_NONE = 0;
    private final int ANCHORVIEW_COORDINATES_TYPE_SCREEN = 2;
    private final int ANCHORVIEW_COORDINATES_TYPE_WINDOW = 1;
    private float f26H = 10.0f;
    private final int ID_TOOLTIP_VIEW = 117506049;
    private final int MARGIN_FOR_HOVER_RING = 8;
    private int MOVE_CENTER = 2;
    private int MOVE_LEFT = 1;
    private int MOVE_LEFT_TO_CENTER = 3;
    private int MOVE_RIGHT = 0;
    private int MOVE_RIGHT_TO_CENTER = 4;
    private final int SHOW_ANIMATION_DURATION = 500;
    private float TW = 15.0f;
    private float f27W = 0.0f;
    private Rect mAnchorRect = null;
    private View mAnchorView;
    protected int mAnimationStyle;
    private PointF mCenterPoint = null;
    private int mContainerLeftOnWindow = 0;
    private HoverPopupContainer mContentContainer;
    private int mContentHeight = 0;
    private LayoutParams mContentLP;
    private int mContentResId;
    protected CharSequence mContentText;
    protected View mContentView;
    private int mContentWidth = 0;
    private final Context mContext;
    private int mCoordinatesOfAnchorView;
    private ICoverManager mCoverManager = null;
    private int mDirection = this.MOVE_CENTER;
    private Handler mDismissHandler = null;
    private Runnable mDismissPopupRunnable = null;
    private boolean mDismissTouchableHPWOnActionUp = true;
    private Rect mDisplayFrame = null;
    private int mDisplayFrameLeft = 0;
    private int mDisplayFrameRight = 0;
    private int mDisplayWidthToComputeAniWidth = 0;
    private boolean mEnabled;
    private float mFontScale = 0.0f;
    private int mFullTextPopupRightLimit = -1;
    private int mGuideLineColor;
    protected int mGuideLineFadeOffset;
    private int mGuideRingDrawableId;
    private Handler mHandler = null;
    private int mHashCodeForViewState;
    protected int mHoverDetectTimeMS;
    private int mHoverPaddingBottom;
    private int mHoverPaddingLeft;
    private int mHoverPaddingRight;
    private int mHoverPaddingTop;
    private int mHoveringPointX;
    private int mHoveringPointY;
    private boolean mIsFHAnimationEnabled;
    private boolean mIsFHAnimationEnabledByApp;
    private boolean mIsFHGuideLineEnabled;
    private boolean mIsFHGuideLineEnabledByApp;
    private boolean mIsFHSoundAndHapticEnabled;
    protected boolean mIsGuideLineEnabled;
    private boolean mIsHoverPaddingEnabled = false;
    private boolean mIsInfoPickerMoveEabled;
    private boolean mIsInfoPickerMoveEabledByApp;
    private boolean mIsPopupTouchable;
    private boolean mIsProgressBar;
    private boolean mIsSPenPointChanged;
    private boolean mIsSetInfoPickerColorToAndMoreBottomImg;
    private boolean mIsShowMessageSent = false;
    private boolean mIsSkipPenPointEffect;
    private boolean mIsTryingShowPopup;
    private boolean mIsUspFeature = false;
    private PointF mLeftPoint = null;
    private OnSetContentViewListener mListener;
    private boolean mNeedNotWindowOffset = false;
    private boolean mNeedToMeasureContentView = false;
    private boolean mOverTopBoundary;
    protected final View mParentView;
    private Point mPenWindowStartPos = null;
    private float mPickerPadding = 54.0f;
    private int mPickerXoffset = 0;
    private PopupWindow mPopup;
    protected int mPopupGravity;
    private int mPopupOffsetX;
    private int mPopupOffsetY;
    private int mPopupPosX;
    private int mPopupPosY;
    protected int mPopupType = 0;
    private HoverPopupPreShowListener mPreShowListener;
    private Rect mReferncedAnchorRect = null;
    private PointF mRightPoint = null;
    private Runnable mShowPopupRunnable = null;
    private int mToolType = 0;
    private TouchablePopupContainer mTouchableContainer;
    private int mWindowGapX;
    private int mWindowGapY;
    private boolean misDialer;
    private boolean misGravityBottomUnder;
    private float objAnimationValue;
    private ValueAnimator objAnimator;

    class C02871 extends Handler {
        C02871() {
        }

        public void handleMessage(Message message) {
            if (SemHoverPopupWindow.this.mIsFHAnimationEnabled) {
                if (!((!SemHoverPopupWindow.this.mOverTopBoundary && !SemHoverPopupWindow.this.misGravityBottomUnder) || SemHoverPopupWindow.this.mPopup == null || !SemHoverPopupWindow.this.mPopup.isShowing() || SemHoverPopupWindow.this.mAnchorView == null || SemHoverPopupWindow.this.mContentView == null)) {
                    int width = (SemHoverPopupWindow.this.mAnchorView.getWidth() - SemHoverPopupWindow.this.mContentView.getWidth()) / 2;
                    if (width < 0) {
                        if (message.what == 0) {
                            int -get28 = SemHoverPopupWindow.this.mPopupPosX + ((SemHoverPopupWindow.this.mAnchorView.getWidth() * 2) / 3);
                            width = SemHoverPopupWindow.this.mContentView.getWidth() + -get28 > SemHoverPopupWindow.this.mDisplayWidthToComputeAniWidth ? -get28 - ((SemHoverPopupWindow.this.mContentView.getWidth() + -get28) - SemHoverPopupWindow.this.mDisplayWidthToComputeAniWidth) : -get28;
                        } else if (message.what == 1) {
                            width = SemHoverPopupWindow.this.mPopupPosX;
                        }
                    }
                    if (message.what == 0) {
                        SemHoverPopupWindow.this.mDirection = SemHoverPopupWindow.this.MOVE_RIGHT;
                        SemHoverPopupWindow.this.setAnimator(width, SemHoverPopupWindow.this.mDirection);
                        SemHoverPopupWindow.this.objAnimator.start();
                    } else if (message.what == 1) {
                        SemHoverPopupWindow.this.mDirection = SemHoverPopupWindow.this.MOVE_LEFT;
                        SemHoverPopupWindow.this.setAnimator(width, SemHoverPopupWindow.this.mDirection);
                        SemHoverPopupWindow.this.objAnimator.start();
                    } else if (message.what == 2) {
                        if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_LEFT) {
                            SemHoverPopupWindow.this.mDirection = SemHoverPopupWindow.this.MOVE_LEFT_TO_CENTER;
                        } else if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_RIGHT) {
                            SemHoverPopupWindow.this.mDirection = SemHoverPopupWindow.this.MOVE_RIGHT_TO_CENTER;
                        }
                        SemHoverPopupWindow.this.setAnimator(width, SemHoverPopupWindow.this.mDirection);
                        SemHoverPopupWindow.this.objAnimator.start();
                    }
                }
            }
        }
    }

    class C02882 extends Handler {
        C02882() {
        }

        public void handleMessage(Message message) {
            if (SemHoverPopupWindow.this.mPopup != null && SemHoverPopupWindow.this.mPopup.isShowing() && message.what == 1) {
                Log.d(SemHoverPopupWindow.TAG, "mDismissHandler handleMessage: Call dismiss");
                SemHoverPopupWindow.this.dismiss();
            }
        }
    }

    class C02893 implements AnimatorUpdateListener {
        C02893() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SemHoverPopupWindow.this.objAnimationValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (SemHoverPopupWindow.this.mPopup == null) {
                return;
            }
            if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_LEFT && ((float) SemHoverPopupWindow.this.mDisplayFrameLeft) < (((float) SemHoverPopupWindow.this.mPopupPosX) - SemHoverPopupWindow.this.objAnimationValue) + ((float) SemHoverPopupWindow.this.mContentView.getWidth())) {
                SemHoverPopupWindow.this.mPopup.update((int) (((float) SemHoverPopupWindow.this.mPopupPosX) - SemHoverPopupWindow.this.objAnimationValue), SemHoverPopupWindow.this.mPopupPosY, -1, -1);
            } else if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_RIGHT && ((float) SemHoverPopupWindow.this.mDisplayFrameRight) > (((float) SemHoverPopupWindow.this.mPopupPosX) + SemHoverPopupWindow.this.objAnimationValue) + ((float) SemHoverPopupWindow.this.mContentView.getWidth())) {
                SemHoverPopupWindow.this.mPopup.update((int) (((float) SemHoverPopupWindow.this.mPopupPosX) + SemHoverPopupWindow.this.objAnimationValue), SemHoverPopupWindow.this.mPopupPosY, -1, -1);
            } else if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_LEFT_TO_CENTER) {
                SemHoverPopupWindow.this.mPopup.update((int) (((float) SemHoverPopupWindow.this.mPopupPosX) - SemHoverPopupWindow.this.objAnimationValue), SemHoverPopupWindow.this.mPopupPosY, -1, -1);
            } else if (SemHoverPopupWindow.this.mDirection == SemHoverPopupWindow.this.MOVE_RIGHT_TO_CENTER) {
                SemHoverPopupWindow.this.mPopup.update((int) (((float) SemHoverPopupWindow.this.mPopupPosX) + SemHoverPopupWindow.this.objAnimationValue), SemHoverPopupWindow.this.mPopupPosY, -1, -1);
            }
        }
    }

    class C02904 implements Runnable {
        C02904() {
        }

        public void run() {
            SemHoverPopupWindow.this.dismissPopup();
        }
    }

    class C02915 implements Runnable {
        C02915() {
        }

        public void run() {
            SemHoverPopupWindow.this.showPopup();
            if (SemHoverPopupWindow.this.mPopupType != 1 || !SemHoverPopupWindow.this.isShowing()) {
                return;
            }
            if (ViewRootImpl.isDesktopmode()) {
                SemHoverPopupWindow.this.mParentView.postDelayed(SemHoverPopupWindow.this.mDismissPopupRunnable, 5000);
            } else {
                SemHoverPopupWindow.this.mParentView.postDelayed(SemHoverPopupWindow.this.mDismissPopupRunnable, 10000);
            }
        }
    }

    class C02926 implements Runnable {
        C02926() {
        }

        public void run() {
            SemHoverPopupWindow.this.dismiss();
        }
    }

    public static final class Gravity {
        public static final int BOTTOM = 80;
        public static final int BOTTOM_UNDER = 20560;
        public static final int CENTER = 17;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_HORIZONTAL_ON_POINT = 513;
        public static final int CENTER_HORIZONTAL_ON_WINDOW = 257;
        public static final int CENTER_VERTICAL = 16;
        public static final int HORIZONTAL_GRAVITY_MASK = 3855;
        public static final int LEFT = 3;
        public static final int LEFT_CENTER_AXIS = 259;
        public static final int LEFT_OUTSIDE = 771;
        public static final int NO_GRAVITY = 0;
        public static final int RIGHT = 5;
        public static final int RIGHT_CENTER_AXIS = 261;
        public static final int RIGHT_OUTSIDE = 1285;
        public static final int TOP = 48;
        public static final int TOP_ABOVE = 12336;
        public static final int VERTICAL_GRAVITY_MASK = 61680;

        private Gravity() {
        }
    }

    private class HoverPopupContainer extends FrameLayout {
        static final boolean DEBUG = false;
        static final String TAG = "HoverPopupContainer";
        private final float DEFAULT_BG_OUTLINE_THICKNESS = 1.5f;
        private final float DEFAULT_BG_PADDING = 10.0f;
        private int POPUPSTATE_CENTER = 2;
        private int POPUPSTATE_LEFT = 1;
        private int POPUPSTATE_RIGHT = 0;
        private Animation ani = null;
        private boolean isFHmoveAnimation = false;
        private int mAnimationAreaOffset = 100;
        private float mBGPaddingBottomPX = -1.0f;
        private float mBGPaddingTopPX = -1.0f;
        private Context mFHPopCContext = null;
        private boolean mIsFHEnabled = false;
        private boolean mIsRingEnabled = false;
        private int mLeftLimit = -1;
        private int mLineEndX;
        private int mLineEndY;
        private int mLineOverlappedHeight = 0;
        private Paint mLinePaint;
        private int mLineStartX;
        private int mLineStartY;
        private int mLineThickness = 0;
        private int mOldLineEndX = -1;
        private int mOldLineEndY = -1;
        protected boolean mOverTopBoundaryEnabled = false;
        private float mPickerHeightPX = 0.0f;
        private int mPickerLineColor = -1;
        private int mPickerLineColorOnBottom = -1;
        private int mPickerOutlineThicknessPX = 0;
        private int mPickerSpaceColor = -1;
        private float mPickerWidthPX = 0.0f;
        private int mPopupState = -1;
        private int mRightLimit = -1;
        private Drawable mRingDrawable;
        private int mRingHeight;
        private int mRingWidth;
        private int mTopPickerOffset = 0;
        private float mTotalLeftLimit = 0.0f;
        private float mTotalRightLimit = 0.0f;
        private boolean misMovetoRight = false;

        public HoverPopupContainer(Context context) {
            super(context);
            this.mFHPopCContext = context;
            this.mPopupState = this.POPUPSTATE_CENTER;
            TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
            this.mPickerLineColor = obtainStyledAttributes.getColor(359, -12095358);
            this.mPickerLineColorOnBottom = obtainStyledAttributes.getColor(360, -10846063);
            this.mPickerSpaceColor = obtainStyledAttributes.getColor(361, -13674908);
            obtainStyledAttributes.recycle();
            this.mBGPaddingBottomPX = -1.0f;
            this.mBGPaddingTopPX = -1.0f;
            this.mPickerOutlineThicknessPX = this.mContext.getResources().getDimensionPixelSize(17105812);
            this.mPickerWidthPX = (float) SemHoverPopupWindow.this.convertDPtoPX(SemHoverPopupWindow.this.TW, null);
            this.mPickerHeightPX = (float) SemHoverPopupWindow.this.convertDPtoPX(SemHoverPopupWindow.this.f26H, null);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (getChildCount() != 0 && getChildAt(0) != null) {
                if (this.mRingDrawable == null) {
                    setGuideLine(17302382, -8810071);
                }
                if (this.mIsRingEnabled) {
                    canvas.save();
                    canvas.translate((float) (this.mLineEndX - (this.mRingWidth / 2)), (float) (this.mLineEndY - (this.mRingHeight / 2)));
                    if (!this.mIsFHEnabled) {
                        this.mRingDrawable.draw(canvas);
                    }
                    canvas.restore();
                    if (!this.mIsFHEnabled) {
                        if (this.mLineStartY < this.mLineEndY) {
                            canvas.drawLine((float) this.mLineStartX, (float) (this.mLineStartY - this.mLineOverlappedHeight), (float) this.mLineEndX, (float) ((this.mLineEndY - (this.mRingHeight / 2)) + this.mLineOverlappedHeight), this.mLinePaint);
                        } else if (this.mLineStartY > this.mLineEndY) {
                            canvas.drawLine((float) this.mLineStartX, (float) (this.mLineStartY + this.mLineOverlappedHeight), (float) this.mLineEndX, (float) ((this.mLineEndY + (this.mRingHeight / 2)) - this.mLineOverlappedHeight), this.mLinePaint);
                        }
                    }
                } else if (!this.mIsFHEnabled) {
                    canvas.drawLine((float) this.mLineStartX, (float) this.mLineStartY, (float) this.mLineEndX, (float) this.mLineEndY, this.mLinePaint);
                }
                if (!SemHoverPopupWindow.this.mIsFHGuideLineEnabled) {
                    return;
                }
                if (SemHoverPopupWindow.this.mContentView == null) {
                    Log.d(TAG, "HoverPopupContainer.draw(): mContentView is null, return");
                    return;
                }
                float f;
                float f2;
                float f3;
                float f4;
                float f5;
                float f6;
                float f7;
                if (SemHoverPopupWindow.this.mContentContainer != null) {
                    SemHoverPopupWindow.this.f27W = (float) SemHoverPopupWindow.this.mContentContainer.getWidth();
                }
                if (SemHoverPopupWindow.this.mCenterPoint == null) {
                    SemHoverPopupWindow.this.mCenterPoint = new PointF(SemHoverPopupWindow.this.f27W / SprDocument.DEFAULT_DENSITY_SCALE, this.mPickerHeightPX);
                    SemHoverPopupWindow.this.mLeftPoint = new PointF((SemHoverPopupWindow.this.f27W / SprDocument.DEFAULT_DENSITY_SCALE) - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE), 0.0f);
                    SemHoverPopupWindow.this.mRightPoint = new PointF((SemHoverPopupWindow.this.f27W / SprDocument.DEFAULT_DENSITY_SCALE) + (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE), 0.0f);
                }
                if (this.mBGPaddingTopPX < 0.0f && this.mBGPaddingBottomPX < 0.0f) {
                    this.mBGPaddingTopPX = (float) this.mContext.getResources().getDimensionPixelSize(17105810);
                    this.mBGPaddingBottomPX = (float) this.mContext.getResources().getDimensionPixelSize(17105811);
                    if (getChildCount() > 0) {
                        Drawable drawable = null;
                        View childAt = getChildAt(0);
                        if (childAt != null) {
                            drawable = childAt.getBackground();
                        }
                        if (drawable != null) {
                            Rect rect = new Rect();
                            drawable.getPadding(rect);
                            if (rect.top < rect.bottom) {
                                this.mBGPaddingTopPX -= (float) (rect.bottom - rect.top);
                            }
                        }
                    }
                }
                SemHoverPopupWindow.this.mCenterPoint.x = (float) getLineEndX();
                if (this.mOverTopBoundaryEnabled) {
                    SemHoverPopupWindow.this.mCenterPoint.y = ((float) getLineStartY()) - (this.mPickerHeightPX - this.mBGPaddingTopPX);
                } else {
                    SemHoverPopupWindow.this.mCenterPoint.y = ((float) getLineStartY()) + (this.mPickerHeightPX - this.mBGPaddingBottomPX);
                }
                if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                    SemHoverPopupWindow.this.mLeftPoint.x = SemHoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                } else {
                    SemHoverPopupWindow.this.mLeftPoint.x = SemHoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                }
                if (this.mOverTopBoundaryEnabled) {
                    SemHoverPopupWindow.this.mLeftPoint.y = (((float) getLineStartY()) + this.mBGPaddingTopPX) + ((float) this.mPickerOutlineThicknessPX);
                } else {
                    SemHoverPopupWindow.this.mLeftPoint.y = (((float) getLineStartY()) - this.mBGPaddingBottomPX) - ((float) this.mPickerOutlineThicknessPX);
                }
                SemHoverPopupWindow.this.mRightPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                SemHoverPopupWindow.this.mRightPoint.y = SemHoverPopupWindow.this.mLeftPoint.y;
                if (this.mOverTopBoundaryEnabled && SemHoverPopupWindow.this.mIsFHAnimationEnabled) {
                    int width = SemHoverPopupWindow.this.mContentView.getWidth() / 2;
                    this.mTotalLeftLimit = (float) (this.mLeftLimit + width);
                    this.mTotalRightLimit = (float) ((this.mRightLimit - width) + 10);
                } else {
                    this.mTotalLeftLimit = (((float) this.mLeftLimit) + SemHoverPopupWindow.this.mPickerPadding) + ((float) this.mAnimationAreaOffset);
                    this.mTotalRightLimit = (((float) this.mRightLimit) - SemHoverPopupWindow.this.mPickerPadding) - ((float) this.mAnimationAreaOffset);
                }
                int width2 = (SemHoverPopupWindow.this.mAnchorView.getWidth() - SemHoverPopupWindow.this.mContentView.getWidth()) / 2;
                if (SemHoverPopupWindow.this.mLeftPoint.x < this.mTotalLeftLimit && this.mLeftLimit != -1 && this.mPopupState == this.POPUPSTATE_CENTER) {
                    f = SemHoverPopupWindow.this.mRightPoint.x;
                    f2 = SemHoverPopupWindow.this.mLeftPoint.x;
                    f3 = SemHoverPopupWindow.this.mCenterPoint.x;
                    SemHoverPopupWindow.this.mLeftPoint.x = ((float) this.mLeftLimit) + SemHoverPopupWindow.this.mPickerPadding;
                    SemHoverPopupWindow.this.mRightPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                    if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                        SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                    } else {
                        SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                    }
                    if (((float) (SemHoverPopupWindow.this.mAnchorView.getLeft() - SemHoverPopupWindow.this.mContainerLeftOnWindow)) < SemHoverPopupWindow.this.mLeftPoint.x || width2 > 0 || !SemHoverPopupWindow.this.mIsFHAnimationEnabled) {
                        this.mPopupState = this.POPUPSTATE_RIGHT;
                        SemHoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_RIGHT);
                    } else {
                        SemHoverPopupWindow.this.mRightPoint.x = f;
                        SemHoverPopupWindow.this.mLeftPoint.x = f2;
                        SemHoverPopupWindow.this.mCenterPoint.x = f3;
                    }
                }
                if (SemHoverPopupWindow.this.mRightPoint.x > this.mTotalRightLimit && this.mRightLimit != -1 && this.mPopupState == this.POPUPSTATE_CENTER) {
                    f = SemHoverPopupWindow.this.mRightPoint.x;
                    f2 = SemHoverPopupWindow.this.mLeftPoint.x;
                    f3 = SemHoverPopupWindow.this.mCenterPoint.x;
                    SemHoverPopupWindow.this.mRightPoint.x = ((float) this.mRightLimit) - SemHoverPopupWindow.this.mPickerPadding;
                    SemHoverPopupWindow.this.mLeftPoint.x = SemHoverPopupWindow.this.mRightPoint.x - this.mPickerWidthPX;
                    if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                        SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                    } else {
                        SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                    }
                    if (SemHoverPopupWindow.this.mAnchorView.getRight() - SemHoverPopupWindow.this.mContainerLeftOnWindow <= 0 || ((float) (SemHoverPopupWindow.this.mAnchorView.getRight() - SemHoverPopupWindow.this.mContainerLeftOnWindow)) > SemHoverPopupWindow.this.mRightPoint.x || width2 > 0 || !SemHoverPopupWindow.this.mIsFHAnimationEnabled) {
                        this.mPopupState = this.POPUPSTATE_LEFT;
                        SemHoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_LEFT);
                    } else {
                        SemHoverPopupWindow.this.mRightPoint.x = f;
                        SemHoverPopupWindow.this.mLeftPoint.x = f2;
                        SemHoverPopupWindow.this.mCenterPoint.x = f3;
                    }
                }
                if (this.mPopupState == this.POPUPSTATE_RIGHT) {
                    if (SemHoverPopupWindow.this.mLeftPoint.x <= this.mTotalLeftLimit || this.mLeftLimit == -1) {
                        SemHoverPopupWindow.this.mLeftPoint.x = ((float) this.mLeftLimit) + SemHoverPopupWindow.this.mPickerPadding;
                        SemHoverPopupWindow.this.mRightPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                        if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                            SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                        } else {
                            SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                        }
                    } else {
                        this.mPopupState = this.POPUPSTATE_CENTER;
                        SemHoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_CENTER);
                    }
                }
                if (this.mPopupState == this.POPUPSTATE_LEFT) {
                    if (SemHoverPopupWindow.this.mRightPoint.x >= this.mTotalRightLimit || this.mRightLimit == -1) {
                        SemHoverPopupWindow.this.mRightPoint.x = ((float) this.mRightLimit) - SemHoverPopupWindow.this.mPickerPadding;
                        SemHoverPopupWindow.this.mLeftPoint.x = SemHoverPopupWindow.this.mRightPoint.x - this.mPickerWidthPX;
                        if ("americano".equals(SystemProperties.get("ro.build.scafe"))) {
                            SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                        } else {
                            SemHoverPopupWindow.this.mCenterPoint.x = SemHoverPopupWindow.this.mRightPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                        }
                    } else {
                        this.mPopupState = this.POPUPSTATE_CENTER;
                        SemHoverPopupWindow.this.mHandler.sendEmptyMessage(this.POPUPSTATE_CENTER);
                    }
                }
                if (!SemHoverPopupWindow.this.mIsInfoPickerMoveEabled) {
                    int i = 0;
                    if (SemHoverPopupWindow.this.mReferncedAnchorRect != null) {
                        i = (SemHoverPopupWindow.this.mReferncedAnchorRect.left + (SemHoverPopupWindow.this.mAnchorView.getWidth() / 2)) - SemHoverPopupWindow.this.mContainerLeftOnWindow;
                    }
                    if (!(i == 0 || SemHoverPopupWindow.this.mFullTextPopupRightLimit == -1 || SemHoverPopupWindow.this.mPickerXoffset + i >= SemHoverPopupWindow.this.mFullTextPopupRightLimit)) {
                        SemHoverPopupWindow.this.mCenterPoint.x = (float) (SemHoverPopupWindow.this.mPickerXoffset + i);
                        SemHoverPopupWindow.this.mLeftPoint.x = SemHoverPopupWindow.this.mCenterPoint.x - (this.mPickerWidthPX / SprDocument.DEFAULT_DENSITY_SCALE);
                        SemHoverPopupWindow.this.mRightPoint.x = SemHoverPopupWindow.this.mLeftPoint.x + this.mPickerWidthPX;
                    }
                }
                int i2 = (this.mPickerOutlineThicknessPX % 2 != 0 ? this.mPickerOutlineThicknessPX + 1 : this.mPickerOutlineThicknessPX) / 2;
                if (this.mPickerOutlineThicknessPX != 4) {
                    f4 = SemHoverPopupWindow.this.mLeftPoint.x - ((float) i2);
                    f5 = SemHoverPopupWindow.this.mRightPoint.x + ((float) i2);
                    if (this.mOverTopBoundaryEnabled) {
                        f6 = SemHoverPopupWindow.this.mLeftPoint.y + ((float) i2);
                        f7 = SemHoverPopupWindow.this.mRightPoint.y + ((float) i2);
                    } else {
                        f6 = SemHoverPopupWindow.this.mLeftPoint.y - ((float) i2);
                        f7 = SemHoverPopupWindow.this.mRightPoint.y - ((float) i2);
                    }
                } else {
                    f4 = SemHoverPopupWindow.this.mLeftPoint.x;
                    f5 = SemHoverPopupWindow.this.mRightPoint.x;
                    f6 = SemHoverPopupWindow.this.mLeftPoint.y;
                    f7 = SemHoverPopupWindow.this.mRightPoint.y;
                }
                if (!this.mOverTopBoundaryEnabled || this.mPopupState != this.POPUPSTATE_CENTER || SemHoverPopupWindow.this.mPopupType != 3 || SemHoverPopupWindow.this.mIsInfoPickerMoveEabled || !SemHoverPopupWindow.this.mIsFHAnimationEnabled) {
                    TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
                    if (!SemHoverPopupWindow.this.mIsSetInfoPickerColorToAndMoreBottomImg || this.mOverTopBoundaryEnabled) {
                        this.mPickerSpaceColor = obtainStyledAttributes.getColor(361, -10654339);
                    } else {
                        this.mPickerSpaceColor = obtainStyledAttributes.getColor(362, -10654339);
                    }
                    obtainStyledAttributes.recycle();
                    Paint paint = new Paint(1);
                    paint.setStrokeWidth((float) this.mPickerOutlineThicknessPX);
                    paint.setColor(this.mPickerSpaceColor);
                    paint.setAntiAlias(true);
                    Path path = new Path();
                    path.setFillType(FillType.EVEN_ODD);
                    path.moveTo(f4, f6);
                    path.lineTo(SemHoverPopupWindow.this.mCenterPoint.x, SemHoverPopupWindow.this.mCenterPoint.y);
                    path.lineTo(f5, f7);
                    path.close();
                    paint.setStyle(Style.FILL);
                    canvas.drawPath(path, paint);
                    Path path2 = new Path();
                    if (this.mOverTopBoundaryEnabled) {
                        paint.setColor(this.mPickerLineColorOnBottom);
                    } else {
                        paint.setColor(this.mPickerLineColor);
                    }
                    paint.setStrokeWidth((float) this.mPickerOutlineThicknessPX);
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeJoin(Join.ROUND);
                    path2.moveTo(f4, f6);
                    path2.lineTo(SemHoverPopupWindow.this.mCenterPoint.x, SemHoverPopupWindow.this.mCenterPoint.y);
                    path2.lineTo(f5, f7);
                    path2.close();
                    canvas.drawPath(path2, paint);
                    Path path3 = new Path();
                    int i3 = this.mPickerOutlineThicknessPX % 2;
                    if (this.mPickerOutlineThicknessPX == 4) {
                        f4 = SemHoverPopupWindow.this.mLeftPoint.x - ((float) i2);
                        f5 = SemHoverPopupWindow.this.mRightPoint.x + ((float) i2);
                    }
                    path3.moveTo(f4, f6);
                    path3.lineTo(f5, f7);
                    paint.setStrokeWidth((float) (this.mPickerOutlineThicknessPX + i3));
                    paint.setAntiAlias(false);
                    paint.setColor(this.mPickerSpaceColor);
                    paint.setStyle(Style.STROKE);
                    path3.close();
                    canvas.drawPath(path3, paint);
                }
            }
        }

        public int getLineEndX() {
            return this.mLineEndX;
        }

        public int getLineOverlappedHeight() {
            return this.mLineOverlappedHeight;
        }

        public int getLineStartY() {
            return this.mLineStartY;
        }

        protected boolean pointInValidPaddingArea(int i, int i2) {
            return getPaddingTop() > getPaddingBottom() ? i < getWidth() && i2 <= getPaddingTop() : getPaddingTop() < getPaddingBottom() ? i < getWidth() && i2 >= getHeight() - getPaddingBottom() : false;
        }

        public void setFHGuideLineForCotainer(boolean z) {
            this.mIsFHEnabled = z;
        }

        public void setFHmoveAnimation(boolean z) {
            this.isFHmoveAnimation = z;
        }

        public void setFHmoveAnimationOffset(int i) {
            Log.d(TAG, "HoverPopupContainer(): setFHmoveAnimationOffset: offset = " + i);
            this.mAnimationAreaOffset = i;
            Log.d(TAG, "HoverPopupContainer(): setFHmoveAnimationOffset: mAnimationAreaOffset = " + this.mAnimationAreaOffset);
        }

        public void setGuideLine(int i, int i2) {
            this.mLineOverlappedHeight = SemHoverPopupWindow.this.convertDPtoPX(1.0f, null);
            this.mLineThickness = SemHoverPopupWindow.this.convertDPtoPX(1.5f, null);
            this.mRingDrawable = getResources().getDrawable(i);
            if (this.mRingDrawable != null) {
                this.mRingWidth = this.mRingDrawable.getIntrinsicWidth();
                this.mRingHeight = this.mRingDrawable.getIntrinsicHeight();
                this.mRingDrawable.setBounds(0, 0, this.mRingWidth, this.mRingHeight);
            }
            this.mLinePaint = new Paint();
            this.mLinePaint.setStrokeWidth((float) this.mLineThickness);
            this.mLinePaint.setStrokeCap(Cap.ROUND);
            this.mLinePaint.setColor(i2);
            this.mLinePaint.setAntiAlias(true);
        }

        public void setGuideLine(int i, int i2, int i3, int i4, boolean z, boolean z2) {
            this.mLineStartX = i;
            this.mLineStartY = i2;
            this.mLineEndX = i3;
            this.mLineEndY = i4;
            this.mIsRingEnabled = z;
            this.mIsFHEnabled = z2;
        }

        public void setGuideLineEndPoint(int i, int i2) {
            this.mLineEndX = i;
            this.mLineEndY = i2;
        }

        public void setOverTopForCotainer(boolean z) {
            Log.d(TAG, "HoverPopupContainer.setOverTopForCotainer: enabled = " + z);
            this.mOverTopBoundaryEnabled = z;
            Log.d(TAG, "HoverPopupContainer.setOverTopForCotainer: mOverTopBoundaryEnabled = " + this.mOverTopBoundaryEnabled);
        }

        public void setOverTopPickerOffset(int i) {
            this.mTopPickerOffset = i;
        }

        public void setPickerLimit(int i, int i2) {
            this.mLeftLimit = i;
            this.mRightLimit = i2;
        }

        public void setPopupState(int i) {
            this.mPopupState = i;
        }

        public void updateDecoration() {
            invalidate();
        }
    }

    public interface HoverPopupPreShowListener {
        boolean onHoverPopupPreShow();
    }

    public interface OnSetContentViewListener {
        boolean onSetContentView(View view, SemHoverPopupWindow semHoverPopupWindow);
    }

    public static class QuintEaseOut implements Interpolator {
        public float getInterpolation(float f) {
            f = (f / 1.0f) - 1.0f;
            return ((((f * f) * f) * f) * f) + 1.0f;
        }
    }

    protected class TouchablePopupContainer extends FrameLayout {
        private static final int MSG_TIMEOUT = 1;
        private static final int TIMEOUT_DELAY = 500;
        private static final int TIMEOUT_DELAY_LONG = 2000;
        protected Handler mContainerDismissHandler;
        private Runnable mDismissPopupRunnable;
        private boolean mIsHoverExitCalled;

        class C02931 extends Handler {
            C02931() {
            }

            public void handleMessage(Message message) {
                Log.d(SemHoverPopupWindow.TAG, "TouchablePopupContainer: ***** mContainerDismissHandler handleMessage *****");
                if (SemHoverPopupWindow.this.mPopup != null && SemHoverPopupWindow.this.mPopup.isShowing() && message.what == 1) {
                    Log.d(SemHoverPopupWindow.TAG, "TouchablePopupContainer: mContainerDismissHandler handleMessage: Call dismiss");
                    SemHoverPopupWindow.this.dismiss();
                }
            }
        }

        class C02942 implements Runnable {
            C02942() {
            }

            public void run() {
                SemHoverPopupWindow.this.dismiss();
            }
        }

        class C02953 implements Runnable {
            C02953() {
            }

            public void run() {
                SemHoverPopupWindow.this.dismiss();
            }
        }

        public TouchablePopupContainer(Context context) {
            super(context);
            this.mIsHoverExitCalled = false;
            this.mDismissPopupRunnable = null;
            this.mContainerDismissHandler = null;
            this.mContainerDismissHandler = new C02931();
        }

        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 10) {
                if (pointInView(motionEvent.getX(), motionEvent.getY(), -2.0f)) {
                    this.mIsHoverExitCalled = true;
                    this.mDismissPopupRunnable = new C02953();
                    postDelayed(this.mDismissPopupRunnable, 100);
                } else {
                    boolean dispatchHoverEvent = super.dispatchHoverEvent(motionEvent);
                    SemHoverPopupWindow.this.dismiss();
                    return dispatchHoverEvent;
                }
            } else if (action == 7 && SemHoverPopupWindow.this.mToolType != 3) {
                resetTimeout();
            }
            return super.dispatchHoverEvent(motionEvent);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (this.mIsHoverExitCalled && this.mDismissPopupRunnable != null) {
                removeCallbacks(this.mDismissPopupRunnable);
                this.mDismissPopupRunnable = null;
                this.mIsHoverExitCalled = false;
            }
            boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
            if (motionEvent.getAction() == 1 && SemHoverPopupWindow.this.mDismissTouchableHPWOnActionUp) {
                postDelayed(new C02942(), 100);
            }
            return dispatchTouchEvent;
        }

        public void resetTimeout() {
            if (this.mContainerDismissHandler != null) {
                if (this.mContainerDismissHandler.hasMessages(1)) {
                    this.mContainerDismissHandler.removeMessages(1);
                }
                if (Build.PRODUCT == null || !(Build.PRODUCT.startsWith("gt5note") || Build.PRODUCT.startsWith("noble") || Build.PRODUCT.startsWith("gts3"))) {
                    this.mContainerDismissHandler.sendMessageDelayed(this.mContainerDismissHandler.obtainMessage(1), 500);
                } else {
                    this.mContainerDismissHandler.sendMessageDelayed(this.mContainerDismissHandler.obtainMessage(1), 2000);
                }
            }
        }
    }

    public SemHoverPopupWindow(View view, int i) {
        this.mParentView = view;
        this.mContext = view.getContext();
        this.mPopupType = i;
        initInstance();
        setInstanceByType(i);
        if (ViewRootImpl.isDesktopmode()) {
            this.mHoverDetectTimeMS = HOVER_DETECT_TIME_MS_DEX;
        }
        this.mHandler = new C02871();
        this.mDismissHandler = new C02882();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void computePopupPosition(android.view.View r58, int r59, int r60, int r61) {
        /*
        r57 = this;
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        if (r52 != 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        if (r58 == 0) goto L_0x0595;
    L_0x000b:
        r52 = r58;
    L_0x000d:
        r0 = r52;
        r1 = r57;
        r1.mAnchorView = r0;
        r0 = r59;
        r1 = r57;
        r1.mPopupGravity = r0;
        r0 = r60;
        r1 = r57;
        r1.mPopupOffsetX = r0;
        r0 = r61;
        r1 = r57;
        r1.mPopupOffsetY = r0;
        if (r58 == 0) goto L_0x059d;
    L_0x0027:
        r7 = r58;
    L_0x0029:
        r0 = r57;
        r0 = r0.mContext;
        r52 = r0;
        r52 = r52.getResources();
        r19 = r52.getDisplayMetrics();
        r6 = 0;
        r52 = 2;
        r0 = r52;
        r5 = new int[r0];
        r52 = 2;
        r0 = r52;
        r4 = new int[r0];
        r7.getLocationOnScreen(r5);
        r7.getLocationInWindow(r4);
        r25 = 1;
        r33 = new android.graphics.Rect;
        r33.<init>();
        r52 = r7.updateDisplayListIfDirty();
        r52 = r52.hasIdentityMatrix();
        if (r52 != 0) goto L_0x05a3;
    L_0x005b:
        r25 = 0;
    L_0x005d:
        if (r25 != 0) goto L_0x00a8;
    L_0x005f:
        r0 = r33;
        r7.getBoundsOnScreen(r0);
        r42 = 0;
        r43 = 0;
        r52 = 0;
        r52 = r5[r52];
        r53 = 0;
        r53 = r4[r53];
        r42 = r52 - r53;
        r52 = 1;
        r52 = r5[r52];
        r53 = 1;
        r53 = r4[r53];
        r43 = r52 - r53;
        r0 = r33;
        r0 = r0.left;
        r52 = r0;
        r52 = r52 - r42;
        r53 = 0;
        r4[r53] = r52;
        r0 = r33;
        r0 = r0.top;
        r52 = r0;
        r52 = r52 - r43;
        r53 = 1;
        r4[r53] = r52;
        r0 = r33;
        r0 = r0.left;
        r52 = r0;
        r53 = 0;
        r5[r53] = r52;
        r0 = r33;
        r0 = r0.top;
        r52 = r0;
        r53 = 1;
        r5[r53] = r52;
    L_0x00a8:
        r18 = new android.graphics.Rect;
        r18.<init>();
        r0 = r18;
        r7.getWindowVisibleDisplayFrame(r0);
        r35 = new android.graphics.Rect;
        r35.<init>();
        r0 = r57;
        r0 = r0.mAnchorView;
        r52 = r0;
        r36 = r52.getRootView();
        r38 = r36.getWidth();
        r37 = r36.getHeight();
        r52 = 0;
        r52 = r5[r52];
        r53 = 0;
        r53 = r4[r53];
        r52 = r52 - r53;
        r0 = r52;
        r1 = r35;
        r1.left = r0;
        r0 = r35;
        r0 = r0.left;
        r52 = r0;
        r52 = r52 + r38;
        r0 = r52;
        r1 = r35;
        r1.right = r0;
        r52 = 1;
        r52 = r5[r52];
        r53 = 1;
        r53 = r4[r53];
        r52 = r52 - r53;
        r0 = r52;
        r1 = r35;
        r1.top = r0;
        r0 = r35;
        r0 = r0.top;
        r52 = r0;
        r52 = r52 + r37;
        r0 = r52;
        r1 = r35;
        r1.bottom = r0;
        r0 = r18;
        r0 = r0.left;
        r52 = r0;
        r0 = r35;
        r0 = r0.left;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0129;
    L_0x0117:
        r0 = r18;
        r0 = r0.right;
        r52 = r0;
        r0 = r35;
        r0 = r0.right;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 == r1) goto L_0x05c4;
    L_0x0129:
        r0 = r35;
        r0 = r0.left;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.left = r0;
        r0 = r35;
        r0 = r0.right;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.right = r0;
        r0 = r35;
        r0 = r0.top;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.top = r0;
        r0 = r35;
        r0 = r0.bottom;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.bottom = r0;
    L_0x0159:
        r52 = r7.getApplicationWindowToken();
        r53 = r7.getWindowToken();
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0631;
    L_0x0167:
        r52 = 0;
        r52 = r5[r52];
        r53 = 0;
        r53 = r4[r53];
        r52 = r52 - r53;
        r0 = r52;
        r1 = r57;
        r1.mWindowGapX = r0;
        r52 = 1;
        r52 = r5[r52];
        r53 = 1;
        r53 = r4[r53];
        r52 = r52 - r53;
        r0 = r52;
        r1 = r57;
        r1.mWindowGapY = r0;
        r52 = 1;
        r0 = r52;
        r1 = r57;
        r1.mCoordinatesOfAnchorView = r0;
        if (r25 == 0) goto L_0x05ea;
    L_0x0191:
        r6 = new android.graphics.Rect;
        r52 = 0;
        r52 = r4[r52];
        r53 = 1;
        r53 = r4[r53];
        r54 = 0;
        r54 = r4[r54];
        r55 = r7.getWidth();
        r54 = r54 + r55;
        r55 = 1;
        r55 = r4[r55];
        r56 = r7.getHeight();
        r55 = r55 + r56;
        r0 = r52;
        r1 = r53;
        r2 = r54;
        r3 = r55;
        r6.<init>(r0, r1, r2, r3);
    L_0x01ba:
        r0 = r18;
        r0 = r0.left;
        r52 = r0;
        if (r52 >= 0) goto L_0x024b;
    L_0x01c2:
        r0 = r18;
        r0 = r0.top;
        r52 = r0;
        if (r52 >= 0) goto L_0x024b;
    L_0x01ca:
        r0 = r57;
        r0 = r0.mParentView;
        r52 = r0;
        r34 = r52.getRootView();
        r49 = r34.getLayoutParams();
        r0 = r49;
        r0 = r0 instanceof android.view.WindowManager.LayoutParams;
        r52 = r0;
        if (r52 == 0) goto L_0x024b;
    L_0x01e0:
        r51 = r49;
        r51 = (android.view.WindowManager.LayoutParams) r51;
        r0 = r51;
        r0 = r0.systemUiVisibility;
        r52 = r0;
        r0 = r51;
        r0 = r0.subtreeSystemUiVisibility;
        r53 = r0;
        r52 = r52 | r53;
        r0 = r52;
        r0 = r0 & 1028;
        r52 = r0;
        if (r52 != 0) goto L_0x06d0;
    L_0x01fa:
        r27 = 1;
    L_0x01fc:
        r41 = 0;
        r0 = r51;
        r0 = r0.flags;
        r52 = r0;
        r0 = r52;
        r0 = r0 & 512;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0225;
    L_0x0212:
        if (r27 == 0) goto L_0x0225;
    L_0x0214:
        r0 = r57;
        r0 = r0.mContext;
        r52 = r0;
        r52 = r52.getResources();
        r53 = 17104919; // 0x1050017 float:2.4428306E-38 double:8.450953E-317;
        r41 = r52.getDimensionPixelSize(r53);
    L_0x0225:
        r52 = 0;
        r0 = r52;
        r1 = r18;
        r1.left = r0;
        r0 = r41;
        r1 = r18;
        r1.top = r0;
        r0 = r19;
        r0 = r0.widthPixels;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.right = r0;
        r0 = r19;
        r0 = r0.heightPixels;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.bottom = r0;
    L_0x024b:
        r0 = r18;
        r0 = r0.left;
        r52 = r0;
        r53 = 0;
        r0 = r53;
        r1 = r52;
        r52 = java.lang.Math.max(r0, r1);
        r0 = r52;
        r1 = r57;
        r1.mDisplayFrameLeft = r0;
        r0 = r18;
        r0 = r0.top;
        r52 = r0;
        r53 = 0;
        r0 = r53;
        r1 = r52;
        r52 = java.lang.Math.max(r0, r1);
        r0 = r52;
        r1 = r57;
        r1.mDisplayFrameRight = r0;
        r0 = r57;
        r0 = r0.mDisplayFrameRight;
        r52 = r0;
        r0 = r57;
        r0 = r0.mDisplayFrameLeft;
        r53 = r0;
        r52 = r52 - r53;
        r0 = r52;
        r1 = r57;
        r1.mDisplayWidthToComputeAniWidth = r0;
        r0 = r57;
        r0 = r0.mContentLP;
        r52 = r0;
        if (r52 != 0) goto L_0x06d4;
    L_0x0293:
        r0 = r19;
        r0 = r0.widthPixels;
        r52 = r0;
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r50 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
        r0 = r19;
        r0 = r0.heightPixels;
        r52 = r0;
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r22 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
    L_0x02ab:
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        r0 = r52;
        r1 = r50;
        r2 = r22;
        r0.measure(r1, r2);
        r52 = 0;
        r0 = r52;
        r1 = r57;
        r1.mNeedToMeasureContentView = r0;
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        r17 = r52.getMeasuredWidth();
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        r15 = r52.getMeasuredHeight();
        r0 = r57;
        r0 = r0.mPopup;
        r52 = r0;
        r0 = r52;
        r1 = r17;
        r0.setWidth(r1);
        r0 = r57;
        r0 = r0.mPopup;
        r52 = r0;
        r0 = r52;
        r0.setHeight(r15);
        r0 = r57;
        r1 = r18;
        r2 = r17;
        r0.computePopupPositionInternal(r6, r1, r2, r15);
        r52 = new android.graphics.Rect;
        r0 = r6.left;
        r53 = r0;
        r0 = r6.top;
        r54 = r0;
        r0 = r6.right;
        r55 = r0;
        r0 = r6.bottom;
        r56 = r0;
        r52.<init>(r53, r54, r55, r56);
        r0 = r52;
        r1 = r57;
        r1.mReferncedAnchorRect = r0;
        r0 = r57;
        r0 = r0.mPopupPosX;
        r31 = r0;
        r0 = r57;
        r0 = r0.mPopupPosY;
        r32 = r0;
        r8 = 0;
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 2;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0745;
    L_0x032d:
        r52 = r32 + r15;
        r0 = r6.top;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 <= r1) goto L_0x0343;
    L_0x0339:
        r0 = r6.bottom;
        r52 = r0;
        r0 = r32;
        r1 = r52;
        if (r0 < r1) goto L_0x0731;
    L_0x0343:
        r8 = 1;
    L_0x0344:
        r0 = r57;
        r0 = r0.mIsGuideLineEnabled;
        r52 = r0;
        if (r52 != 0) goto L_0x0354;
    L_0x034c:
        r0 = r57;
        r0 = r0.mIsFHGuideLineEnabled;
        r52 = r0;
        if (r52 == 0) goto L_0x08ab;
    L_0x0354:
        if (r8 == 0) goto L_0x08ab;
    L_0x0356:
        r26 = 1;
        r52 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r0 = r57;
        r1 = r52;
        r2 = r19;
        r28 = r0.convertDPtoPX(r1, r2);
        r9 = 0;
        r14 = 0;
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 2;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x076c;
    L_0x0374:
        r0 = r6.left;
        r52 = r0;
        r0 = r31;
        r1 = r52;
        r52 = java.lang.Math.min(r0, r1);
        r53 = 0;
        r9 = java.lang.Math.max(r52, r53);
        r52 = r31 + r17;
        r0 = r6.right;
        r53 = r0;
        r52 = java.lang.Math.max(r52, r53);
        r0 = r19;
        r0 = r0.widthPixels;
        r53 = r0;
        r14 = java.lang.Math.min(r52, r53);
    L_0x039a:
        r0 = r57;
        r0.mContainerLeftOnWindow = r9;
        r52 = r6.centerY();
        r0 = r32;
        r1 = r52;
        if (r0 <= r1) goto L_0x07cf;
    L_0x03a8:
        r26 = 0;
    L_0x03aa:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        if (r52 != 0) goto L_0x03f2;
    L_0x03b2:
        r52 = new com.samsung.android.widget.SemHoverPopupWindow$HoverPopupContainer;
        r0 = r57;
        r0 = r0.mContext;
        r53 = r0;
        r0 = r52;
        r1 = r57;
        r2 = r53;
        r0.<init>(r2);
        r0 = r52;
        r1 = r57;
        r1.mContentContainer = r0;
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r52.setBackgroundColor(r53);
        r52 = "SemHoverPopupWindow";
        r53 = "FingerSemHoverPopupWindow: kdhpoint2";
        android.util.Log.d(r52, r53);
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r0 = r57;
        r0 = r0.mGuideRingDrawableId;
        r53 = r0;
        r0 = r57;
        r0 = r0.mGuideLineColor;
        r54 = r0;
        r52.setGuideLine(r53, r54);
    L_0x03f2:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        if (r52 == 0) goto L_0x041e;
    L_0x03fa:
        r0 = r57;
        r0 = r0.mOverTopBoundary;
        r52 = r0;
        if (r52 != 0) goto L_0x040a;
    L_0x0402:
        r0 = r57;
        r0 = r0.misGravityBottomUnder;
        r52 = r0;
        if (r52 == 0) goto L_0x07d3;
    L_0x040a:
        r52 = "SemHoverPopupWindow";
        r53 = "FingerSemHoverPopupWindow: Call setOverTopForCotainer(true)";
        android.util.Log.d(r52, r53);
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 1;
        r52.setOverTopForCotainer(r53);
    L_0x041e:
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        r16 = r52.getLayoutParams();
        if (r16 != 0) goto L_0x07e9;
    L_0x042a:
        r0 = r57;
        r0 = r0.mContentView;
        r52 = r0;
        r53 = new android.widget.FrameLayout$LayoutParams;
        r0 = r53;
        r1 = r17;
        r0.<init>(r1, r15);
        r52.setLayoutParams(r53);
    L_0x043c:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r52 = r52.getChildCount();
        if (r52 == 0) goto L_0x0460;
    L_0x0448:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r52 = r52.getChildAt(r53);
        r0 = r57;
        r0 = r0.mContentView;
        r53 = r0;
        r52 = r52.equals(r53);
        if (r52 == 0) goto L_0x07f5;
    L_0x0460:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r52 = r52.getChildCount();
        if (r52 != 0) goto L_0x047b;
    L_0x046c:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r0 = r57;
        r0 = r0.mContentView;
        r53 = r0;
        r52.addView(r53);
    L_0x047b:
        r0 = r57;
        r0 = r0.mPopup;
        r52 = r0;
        r53 = -2;
        r52.setWidth(r53);
        r0 = r57;
        r0 = r0.mPopup;
        r52 = r0;
        r53 = -2;
        r52.setHeight(r53);
        r52 = r9 - r31;
        r11 = java.lang.Math.abs(r52);
        r52 = r31 + r17;
        r52 = r14 - r52;
        r12 = java.lang.Math.abs(r52);
        r13 = 0;
        r10 = 0;
        r0 = r57;
        r0 = r0.mIsGuideLineEnabled;
        r52 = r0;
        if (r52 == 0) goto L_0x0815;
    L_0x04a9:
        r0 = r57;
        r0 = r0.mIsFHGuideLineEnabled;
        r52 = r0;
        if (r52 == 0) goto L_0x0815;
    L_0x04b1:
        if (r26 == 0) goto L_0x0800;
    L_0x04b3:
        r10 = r28;
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r0 = r52;
        r1 = r53;
        r2 = r28;
        r0.setPadding(r11, r1, r12, r2);
    L_0x04c6:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 2;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0889;
    L_0x04d4:
        if (r26 == 0) goto L_0x04d8;
    L_0x04d6:
        r31 = r9;
    L_0x04d8:
        r0 = r57;
        r0 = r0.mHoveringPointX;
        r52 = r0;
        r52 = r52 - r31;
        r0 = r57;
        r0 = r0.mWindowGapX;
        r53 = r0;
        r23 = r52 - r53;
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r52 = r52 - r32;
        r0 = r57;
        r0 = r0.mWindowGapY;
        r53 = r0;
        r24 = r52 - r53;
        if (r26 == 0) goto L_0x08a3;
    L_0x04fa:
        r48 = r7.getViewRootImpl();
        if (r48 == 0) goto L_0x0500;
    L_0x0500:
        r52 = r17 / 2;
        r39 = r11 + r52;
        r0 = r57;
        r0 = r0.mGuideLineFadeOffset;
        r52 = r0;
        r40 = r15 - r52;
        r20 = r23;
        r21 = r24;
        r52 = r11 + 10;
        r0 = r23;
        r1 = r52;
        if (r0 >= r1) goto L_0x0518;
    L_0x0518:
        r0 = r31;
        r1 = r57;
        r1.mPopupPosX = r0;
        r0 = r32;
        r1 = r57;
        r1.mPopupPosY = r0;
        r52 = ANIMATION_BY_POINTER_POSITION_ENABLED;
        if (r52 == 0) goto L_0x0561;
    L_0x0528:
        r52 = r17 * 1;
        r52 = r52 / 3;
        r29 = r31 + r52;
        r52 = r17 * 2;
        r52 = r52 / 3;
        r44 = r31 + r52;
        r52 = r15 * 1;
        r52 = r52 / 3;
        r30 = r32 + r52;
        r52 = r15 * 2;
        r52 = r52 / 3;
        r45 = r32 + r52;
        r0 = r57;
        r0 = r0.mHoveringPointX;
        r52 = r0;
        r0 = r52;
        r1 = r29;
        if (r0 >= r1) goto L_0x09a5;
    L_0x054c:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 <= r1) goto L_0x096b;
    L_0x0558:
        r52 = 16975126; // 0x1030516 float:2.406455E-38 double:8.3868266E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
    L_0x0561:
        r0 = r57;
        r0 = r0.mPopup;
        r52 = r0;
        r0 = r57;
        r0 = r0.mAnimationStyle;
        r53 = r0;
        r52.setAnimationStyle(r53);
        r0 = r57;
        r0 = r0.mIsFHAnimationEnabled;
        r52 = r0;
        if (r52 != 0) goto L_0x0594;
    L_0x0578:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        if (r52 == 0) goto L_0x0594;
    L_0x0580:
        r52 = "SemHoverPopupWindow";
        r53 = "SemHoverPopupWindow.computePopupPosition() : Call setFHmoveAnimationOffset(0)";
        android.util.Log.d(r52, r53);
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r52.setFHmoveAnimationOffset(r53);
    L_0x0594:
        return;
    L_0x0595:
        r0 = r57;
        r0 = r0.mParentView;
        r52 = r0;
        goto L_0x000d;
    L_0x059d:
        r0 = r57;
        r7 = r0.mParentView;
        goto L_0x0029;
    L_0x05a3:
        r47 = r7.getParent();
    L_0x05a7:
        r0 = r47;
        r0 = r0 instanceof android.view.View;
        r52 = r0;
        if (r52 == 0) goto L_0x005d;
    L_0x05af:
        r46 = r47;
        r46 = (android.view.View) r46;
        r52 = r46.updateDisplayListIfDirty();
        r52 = r52.hasIdentityMatrix();
        if (r52 != 0) goto L_0x05bf;
    L_0x05bd:
        r25 = 0;
    L_0x05bf:
        r47 = r46.getParent();
        goto L_0x05a7;
    L_0x05c4:
        r0 = r18;
        r0 = r0.top;
        r52 = r0;
        r0 = r35;
        r0 = r0.top;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0129;
    L_0x05d6:
        r0 = r18;
        r0 = r0.bottom;
        r52 = r0;
        r0 = r35;
        r0 = r0.bottom;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 == r1) goto L_0x0159;
    L_0x05e8:
        goto L_0x0129;
    L_0x05ea:
        r6 = new android.graphics.Rect;
        r0 = r33;
        r0 = r0.left;
        r52 = r0;
        r0 = r18;
        r0 = r0.left;
        r53 = r0;
        r52 = r52 - r53;
        r0 = r33;
        r0 = r0.top;
        r53 = r0;
        r0 = r18;
        r0 = r0.top;
        r54 = r0;
        r53 = r53 - r54;
        r0 = r33;
        r0 = r0.right;
        r54 = r0;
        r0 = r18;
        r0 = r0.left;
        r55 = r0;
        r54 = r54 - r55;
        r0 = r33;
        r0 = r0.bottom;
        r55 = r0;
        r0 = r18;
        r0 = r0.top;
        r56 = r0;
        r55 = r55 - r56;
        r0 = r52;
        r1 = r53;
        r2 = r54;
        r3 = r55;
        r6.<init>(r0, r1, r2, r3);
        goto L_0x01ba;
    L_0x0631:
        r52 = 2;
        r0 = r52;
        r1 = r57;
        r1.mCoordinatesOfAnchorView = r0;
        r52 = 0;
        r0 = r52;
        r1 = r57;
        r1.mWindowGapX = r0;
        r52 = 0;
        r0 = r52;
        r1 = r57;
        r1.mWindowGapY = r0;
        if (r25 == 0) goto L_0x06ae;
    L_0x064b:
        r6 = new android.graphics.Rect;
        r52 = 0;
        r52 = r5[r52];
        r53 = 1;
        r53 = r5[r53];
        r54 = 0;
        r54 = r5[r54];
        r55 = r7.getWidth();
        r54 = r54 + r55;
        r55 = 1;
        r55 = r5[r55];
        r56 = r7.getHeight();
        r55 = r55 + r56;
        r0 = r52;
        r1 = r53;
        r2 = r54;
        r3 = r55;
        r6.<init>(r0, r1, r2, r3);
    L_0x0674:
        r0 = r18;
        r0 = r0.left;
        r52 = r0;
        if (r52 >= 0) goto L_0x01ba;
    L_0x067c:
        r0 = r18;
        r0 = r0.top;
        r52 = r0;
        if (r52 >= 0) goto L_0x01ba;
    L_0x0684:
        r52 = 0;
        r0 = r52;
        r1 = r18;
        r1.left = r0;
        r0 = r19;
        r0 = r0.widthPixels;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.right = r0;
        r52 = 0;
        r0 = r52;
        r1 = r18;
        r1.top = r0;
        r0 = r19;
        r0 = r0.heightPixels;
        r52 = r0;
        r0 = r52;
        r1 = r18;
        r1.bottom = r0;
        goto L_0x01ba;
    L_0x06ae:
        r6 = new android.graphics.Rect;
        r52 = 0;
        r52 = r5[r52];
        r53 = 1;
        r53 = r5[r53];
        r0 = r33;
        r0 = r0.right;
        r54 = r0;
        r0 = r33;
        r0 = r0.bottom;
        r55 = r0;
        r0 = r52;
        r1 = r53;
        r2 = r54;
        r3 = r55;
        r6.<init>(r0, r1, r2, r3);
        goto L_0x0674;
    L_0x06d0:
        r27 = 0;
        goto L_0x01fc;
    L_0x06d4:
        r0 = r57;
        r0 = r0.mContentLP;
        r52 = r0;
        r0 = r52;
        r0 = r0.width;
        r52 = r0;
        if (r52 < 0) goto L_0x0716;
    L_0x06e2:
        r0 = r57;
        r0 = r0.mContentLP;
        r52 = r0;
        r0 = r52;
        r0 = r0.width;
        r52 = r0;
        r53 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r50 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
    L_0x06f4:
        r0 = r57;
        r0 = r0.mContentLP;
        r52 = r0;
        r0 = r52;
        r0 = r0.height;
        r52 = r0;
        if (r52 < 0) goto L_0x0723;
    L_0x0702:
        r0 = r57;
        r0 = r0.mContentLP;
        r52 = r0;
        r0 = r52;
        r0 = r0.height;
        r52 = r0;
        r53 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r22 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
        goto L_0x02ab;
    L_0x0716:
        r0 = r19;
        r0 = r0.widthPixels;
        r52 = r0;
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r50 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
        goto L_0x06f4;
    L_0x0723:
        r0 = r19;
        r0 = r0.heightPixels;
        r52 = r0;
        r53 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r22 = android.view.View.MeasureSpec.makeMeasureSpec(r52, r53);
        goto L_0x02ab;
    L_0x0731:
        r0 = r18;
        r0 = r0.top;
        r52 = r0;
        r52 = r52 + r32;
        r0 = r6.bottom;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 < r1) goto L_0x0344;
    L_0x0743:
        goto L_0x0343;
    L_0x0745:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0344;
    L_0x0753:
        r52 = r32 + r15;
        r0 = r6.top;
        r53 = r0;
        r0 = r52;
        r1 = r53;
        if (r0 <= r1) goto L_0x0769;
    L_0x075f:
        r0 = r6.bottom;
        r52 = r0;
        r0 = r32;
        r1 = r52;
        if (r0 < r1) goto L_0x0344;
    L_0x0769:
        r8 = 1;
        goto L_0x0344;
    L_0x076c:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x039a;
    L_0x077a:
        r0 = r6.left;
        r52 = r0;
        r0 = r31;
        r1 = r52;
        r52 = java.lang.Math.min(r0, r1);
        r0 = r18;
        r0 = r0.left;
        r53 = r0;
        r0 = r53;
        r0 = -r0;
        r53 = r0;
        r9 = java.lang.Math.max(r52, r53);
        r52 = r31 + r17;
        r0 = r6.right;
        r53 = r0;
        r52 = java.lang.Math.max(r52, r53);
        r0 = r18;
        r0 = r0.right;
        r53 = r0;
        r0 = r18;
        r0 = r0.left;
        r54 = r0;
        r53 = r53 - r54;
        r14 = java.lang.Math.min(r52, r53);
        r0 = r57;
        r0 = r0.mFullTextPopupRightLimit;
        r52 = r0;
        r53 = -1;
        r0 = r52;
        r1 = r53;
        if (r0 == r1) goto L_0x039a;
    L_0x07bf:
        r0 = r57;
        r0 = r0.mFullTextPopupRightLimit;
        r52 = r0;
        r0 = r52;
        if (r14 <= r0) goto L_0x039a;
    L_0x07c9:
        r0 = r57;
        r14 = r0.mFullTextPopupRightLimit;
        goto L_0x039a;
    L_0x07cf:
        r26 = 1;
        goto L_0x03aa;
    L_0x07d3:
        r52 = "SemHoverPopupWindow";
        r53 = "FingerSemHoverPopupWindow: Call setOverTopForCotainer(false)";
        android.util.Log.d(r52, r53);
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r52.setOverTopForCotainer(r53);
        goto L_0x041e;
    L_0x07e9:
        r0 = r17;
        r1 = r16;
        r1.width = r0;
        r0 = r16;
        r0.height = r15;
        goto L_0x043c;
    L_0x07f5:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r52.removeAllViews();
        goto L_0x0460;
    L_0x0800:
        r13 = r28;
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r0 = r52;
        r1 = r28;
        r2 = r53;
        r0.setPadding(r11, r1, r12, r2);
        goto L_0x04c6;
    L_0x0815:
        if (r26 == 0) goto L_0x0851;
    L_0x0817:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 2;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0838;
    L_0x0825:
        r10 = r28;
    L_0x0827:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r0 = r52;
        r1 = r53;
        r0.setPadding(r11, r1, r12, r10);
        goto L_0x04c6;
    L_0x0838:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0827;
    L_0x0846:
        r0 = r6.bottom;
        r52 = r0;
        r52 = r52 + r28;
        r53 = r32 + r15;
        r10 = r52 - r53;
        goto L_0x0827;
    L_0x0851:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 2;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0872;
    L_0x085f:
        r13 = r28;
    L_0x0861:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r53 = 0;
        r0 = r52;
        r1 = r53;
        r0.setPadding(r11, r13, r12, r1);
        goto L_0x04c6;
    L_0x0872:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x0861;
    L_0x0880:
        r0 = r6.top;
        r52 = r0;
        r52 = r52 - r28;
        r13 = r32 - r52;
        goto L_0x0861;
    L_0x0889:
        r0 = r57;
        r0 = r0.mCoordinatesOfAnchorView;
        r52 = r0;
        r53 = 1;
        r0 = r52;
        r1 = r53;
        if (r0 != r1) goto L_0x04d8;
    L_0x0897:
        if (r26 == 0) goto L_0x089d;
    L_0x0899:
        r31 = r9;
        goto L_0x04d8;
    L_0x089d:
        r31 = r9;
        r32 = r32 - r13;
        goto L_0x04d8;
    L_0x08a3:
        r48 = r7.getViewRootImpl();
        if (r48 == 0) goto L_0x0518;
    L_0x08a9:
        goto L_0x0518;
    L_0x08ab:
        r0 = r57;
        r0 = r0.mIsPopupTouchable;
        r52 = r0;
        if (r52 == 0) goto L_0x08bb;
    L_0x08b3:
        r0 = r57;
        r0 = r0.mIsGuideLineEnabled;
        r52 = r0;
        if (r52 == 0) goto L_0x08d6;
    L_0x08bb:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        if (r52 == 0) goto L_0x0518;
    L_0x08c3:
        r0 = r57;
        r0 = r0.mContentContainer;
        r52 = r0;
        r52.removeAllViews();
        r52 = 0;
        r0 = r52;
        r1 = r57;
        r1.mContentContainer = r0;
        goto L_0x0518;
    L_0x08d6:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        if (r52 != 0) goto L_0x08f5;
    L_0x08de:
        r52 = new com.samsung.android.widget.SemHoverPopupWindow$TouchablePopupContainer;
        r0 = r57;
        r0 = r0.mContext;
        r53 = r0;
        r0 = r52;
        r1 = r57;
        r2 = r53;
        r0.<init>(r2);
        r0 = r52;
        r1 = r57;
        r1.mTouchableContainer = r0;
    L_0x08f5:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r52 = r52.getChildCount();
        if (r52 != 0) goto L_0x093a;
    L_0x0901:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r0 = r57;
        r0 = r0.mContentView;
        r53 = r0;
        r52.addView(r53);
    L_0x0910:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        if (r52 == 0) goto L_0x0518;
    L_0x0918:
        r0 = r57;
        r0 = r0.mToolType;
        r52 = r0;
        r53 = 3;
        r0 = r52;
        r1 = r53;
        if (r0 == r1) goto L_0x0518;
    L_0x0926:
        r52 = "SemHoverPopupWindow";
        r53 = "computePopupPosition: Call resetTimeout()";
        android.util.Log.d(r52, r53);
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r52.resetTimeout();
        goto L_0x0518;
    L_0x093a:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r53 = 0;
        r52 = r52.getChildAt(r53);
        r0 = r57;
        r0 = r0.mContentView;
        r53 = r0;
        r52 = r52.equals(r53);
        if (r52 != 0) goto L_0x0910;
    L_0x0952:
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r52.removeAllViews();
        r0 = r57;
        r0 = r0.mTouchableContainer;
        r52 = r0;
        r0 = r57;
        r0 = r0.mContentView;
        r53 = r0;
        r52.addView(r53);
        goto L_0x0910;
    L_0x096b:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 > r1) goto L_0x098e;
    L_0x0977:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 < r1) goto L_0x098e;
    L_0x0983:
        r52 = 16975127; // 0x1030517 float:2.4064552E-38 double:8.386827E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x098e:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 >= r1) goto L_0x0561;
    L_0x099a:
        r52 = 16975128; // 0x1030518 float:2.4064555E-38 double:8.3868276E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x09a5:
        r0 = r57;
        r0 = r0.mHoveringPointX;
        r52 = r0;
        r0 = r29;
        r1 = r52;
        if (r0 > r1) goto L_0x0a0e;
    L_0x09b1:
        r0 = r57;
        r0 = r0.mHoveringPointX;
        r52 = r0;
        r0 = r52;
        r1 = r44;
        if (r0 > r1) goto L_0x0a0e;
    L_0x09bd:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 <= r1) goto L_0x09d4;
    L_0x09c9:
        r52 = 16975129; // 0x1030519 float:2.4064557E-38 double:8.386828E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x09d4:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 > r1) goto L_0x09f7;
    L_0x09e0:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 < r1) goto L_0x09f7;
    L_0x09ec:
        r52 = 16975130; // 0x103051a float:2.406456E-38 double:8.3868286E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x09f7:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 >= r1) goto L_0x0561;
    L_0x0a03:
        r52 = 16975131; // 0x103051b float:2.4064563E-38 double:8.386829E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x0a0e:
        r0 = r57;
        r0 = r0.mHoveringPointX;
        r52 = r0;
        r0 = r52;
        r1 = r44;
        if (r0 <= r1) goto L_0x0561;
    L_0x0a1a:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 <= r1) goto L_0x0a31;
    L_0x0a26:
        r52 = 16975132; // 0x103051c float:2.4064566E-38 double:8.3868296E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x0a31:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r45;
        if (r0 > r1) goto L_0x0a54;
    L_0x0a3d:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 < r1) goto L_0x0a54;
    L_0x0a49:
        r52 = 16975133; // 0x103051d float:2.4064569E-38 double:8.38683E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
    L_0x0a54:
        r0 = r57;
        r0 = r0.mHoveringPointY;
        r52 = r0;
        r0 = r52;
        r1 = r30;
        if (r0 >= r1) goto L_0x0561;
    L_0x0a60:
        r52 = 16975134; // 0x103051e float:2.406457E-38 double:8.3868305E-317;
        r0 = r52;
        r1 = r57;
        r1.mAnimationStyle = r0;
        goto L_0x0561;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.widget.SemHoverPopupWindow.computePopupPosition(android.view.View, int, int, int):void");
    }

    private void computePopupPositionInternal(Rect rect, Rect rect2, int i, int i2) {
        DisplayMetrics displayMetrics;
        LayoutParams layoutParams;
        Object obj;
        this.mAnchorRect = rect;
        this.mDisplayFrame = rect2;
        this.mContentWidth = i;
        this.mContentHeight = i2;
        int i3 = this.mPopupOffsetX;
        int i4 = this.mPopupOffsetY;
        int i5 = this.mPopupGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int i6 = this.mPopupGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(17105808);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(17105809);
        if (this.mPopupGravity != 0) {
            switch (i5) {
                case 1:
                    i3 = rect.centerX() - (i / 2);
                    break;
                case 3:
                    i3 = rect.left;
                    break;
                case 5:
                    i3 = rect.right - i;
                    break;
                case 257:
                    i3 = rect2.centerX() - (i / 2);
                    break;
                case 259:
                    i3 = rect.centerX() - i;
                    break;
                case Gravity.RIGHT_CENTER_AXIS /*261*/:
                    i3 = rect.centerX();
                    break;
                case 513:
                    if (!ViewRootImpl.isDesktopmode() || !this.mNeedNotWindowOffset) {
                        i3 = (this.mHoveringPointX - (i / 2)) - this.mWindowGapX;
                        break;
                    } else {
                        i3 = this.mHoveringPointX - (i / 2);
                        break;
                    }
                    break;
                case 771:
                    i3 = rect.left - i;
                    break;
                case 1285:
                    i3 = rect.right;
                    break;
                default:
                    i3 = this.mPopupOffsetX;
                    break;
            }
            i3 += this.mPopupOffsetX;
            switch (i6) {
                case 16:
                    i4 = rect.centerY() - (i2 / 2);
                    break;
                case 48:
                    i4 = rect.top;
                    break;
                case 80:
                    i4 = rect.bottom - i2;
                    break;
                case Gravity.TOP_ABOVE /*12336*/:
                    i4 = rect.top - i2;
                    break;
                case Gravity.BOTTOM_UNDER /*20560*/:
                    i4 = this.mPopupType == 1 ? rect.bottom + dimensionPixelSize2 : rect.bottom;
                    this.misGravityBottomUnder = true;
                    break;
                default:
                    i4 = this.mPopupOffsetY;
                    break;
            }
            i4 += this.mPopupOffsetY;
        } else if (this.mCoordinatesOfAnchorView == 2) {
            i3 = this.mPopupOffsetX + rect2.left;
            i4 = this.mPopupOffsetY + rect2.top;
        } else if (this.mCoordinatesOfAnchorView == 1) {
            i3 = this.mPopupOffsetX;
            i4 = this.mPopupOffsetY;
        }
        if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            layoutParams = this.mParentView.getRootView().getLayoutParams();
            obj = null;
            if (layoutParams instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams layoutParams2 = (WindowManager.LayoutParams) layoutParams;
                obj = ((layoutParams2.systemUiVisibility | layoutParams2.subtreeSystemUiVisibility) & 1028) == 0 ? 1 : null;
            }
            if (obj != null) {
                int dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(17104919);
            }
            if (i4 + i2 > displayMetrics.heightPixels && i6 == 20560 && this.mPopupType == 1) {
                i3 = (rect.centerX() - (i / 2)) + this.mPopupOffsetX;
            }
        } else if (this.mCoordinatesOfAnchorView == 1 && i4 + i2 > rect2.bottom - rect2.top && i6 == 20560 && this.mPopupType == 1) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            if (rect.top >= i2) {
                if (rect2.top != this.mContext.getResources().getDimensionPixelSize(17104919) || i4 + i2 > rect2.bottom) {
                    i3 = (rect.centerX() - (i / 2)) + this.mPopupOffsetX;
                }
            } else {
                if ((rect2.top + i4) + i2 > displayMetrics.heightPixels) {
                    i3 = (rect.centerX() - (i / 2)) + this.mPopupOffsetX;
                }
            }
        }
        Log.d(TAG, "computePopupPositionInternal: check window boundary ");
        int dimensionPixelSize4;
        if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = getRealDisplayMetrics();
            dimensionPixelSize4 = this.mContext.getResources().getDimensionPixelSize(17105813);
            if (i3 < 0) {
                i3 = Math.max(dimensionPixelSize4, i3);
            } else {
                if (i3 + i > displayMetrics.widthPixels) {
                    i3 = Math.min(i3, (displayMetrics.widthPixels - i) - dimensionPixelSize4);
                }
            }
            if (this.mPopupType == 1 && i > displayMetrics.widthPixels - dimensionPixelSize4) {
                i3 = 0;
            }
        } else if (this.mCoordinatesOfAnchorView == 1) {
            WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
            displayMetrics = getRealDisplayMetrics();
            IWindowManager asInterface;
            if (rect2.left + i3 <= 0) {
                dimensionPixelSize4 = this.mContext.getResources().getDimensionPixelSize(17105813);
                i3 = Math.max((-rect2.left) + dimensionPixelSize4, Math.min(i3, (rect2.right - rect2.left) - i));
                if (this.mPopupType == 1 && i > displayMetrics.widthPixels - dimensionPixelSize4) {
                    i3 = 0;
                }
                asInterface = Stub.asInterface(ServiceManager.getService("window"));
            } else {
                if ((rect2.left + i3) + i >= displayMetrics.widthPixels) {
                    dimensionPixelSize4 = this.mContext.getResources().getDimensionPixelSize(17105813);
                    try {
                        asInterface = Stub.asInterface(ServiceManager.getService("window"));
                        DisplayMetrics realDisplayMetrics = getRealDisplayMetrics();
                        if ((rect2.left + i3) + i >= realDisplayMetrics.widthPixels) {
                            i3 = Math.min(i3, ((realDisplayMetrics.widthPixels - rect2.left) - i) - dimensionPixelSize4);
                        }
                        this.mFullTextPopupRightLimit = i3 + i;
                    } catch (Exception e) {
                        Log.d(TAG, "SemHoverPopupWindow:computePopupPositionInternal : WINDOW_SERVICE remote exception occurred. ");
                    }
                    if (this.mPopupType == 1 && i > displayMetrics.widthPixels - dimensionPixelSize4) {
                        i3 = 0;
                    }
                } else if (rect2.left >= 0) {
                    asInterface = Stub.asInterface(ServiceManager.getService("window"));
                    dimensionPixelSize4 = this.mContext.getResources().getDimensionPixelSize(17105813);
                    if (rect2.right - rect2.left < i) {
                        i3 = Math.min(i3, (rect2.right - rect2.left) - i);
                    } else {
                        if ((rect2.left + i3) + i > rect2.right) {
                            if (rect2.right - rect2.left >= i + dimensionPixelSize4) {
                                i3 = Math.min(i3, ((rect2.right - rect2.left) - i) - dimensionPixelSize4);
                            } else if (rect2.right - rect2.left >= i) {
                                i3 = Math.min(i3, (rect2.right - rect2.left) - i);
                            }
                        } else {
                            i3 = Math.max(i3, dimensionPixelSize4);
                        }
                    }
                }
            }
        }
        if (this.mCoordinatesOfAnchorView == 2) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            layoutParams = this.mParentView.getRootView().getLayoutParams();
            obj = null;
            if (layoutParams instanceof WindowManager.LayoutParams) {
                layoutParams2 = (WindowManager.LayoutParams) layoutParams;
                obj = ((layoutParams2.systemUiVisibility | layoutParams2.subtreeSystemUiVisibility) & 1028) == 0 ? 1 : null;
            }
            dimensionPixelSize3 = 0;
            if (obj != null) {
                dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(17104919);
            }
            if (i4 >= dimensionPixelSize3) {
                if (i4 + i2 > displayMetrics.heightPixels) {
                    if (i6 != 20560) {
                        Log.d(TAG, "computePopupPositionInternal: #5 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        i4 = rect.top - i2;
                    } else if (rect.top >= i2) {
                        i4 = (rect.top - i2) - this.mPopupOffsetY;
                        Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3: misGravityBottomUnder = " + this.misGravityBottomUnder);
                        if (this.misGravityBottomUnder) {
                            this.misGravityBottomUnder = false;
                            Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        }
                    }
                } else if (i6 == 12336) {
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #6 set mOverTopBoundary = " + this.mOverTopBoundary);
                }
            } else if (i6 != 12336) {
                Log.d(TAG, "computePopupPositionInternal #2-1: mOverTopBoundary = " + this.mOverTopBoundary);
                i4 = Math.max(rect2.top, i4);
            } else if (displayMetrics.heightPixels - rect.bottom >= i2) {
                i4 = rect.bottom + this.mPopupOffsetY;
                Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                this.mOverTopBoundary = true;
            } else {
                if (displayMetrics.heightPixels - rect.bottom > rect.top - dimensionPixelSize3) {
                    i4 = rect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                    this.mOverTopBoundary = true;
                } else {
                    i4 = dimensionPixelSize3;
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #2: mOverTopBoundary = " + this.mOverTopBoundary);
                }
            }
        } else if (this.mCoordinatesOfAnchorView == 1) {
            layoutParams = this.mParentView.getRootView().getLayoutParams();
            obj = null;
            if (layoutParams instanceof WindowManager.LayoutParams) {
                layoutParams2 = (WindowManager.LayoutParams) layoutParams;
                obj = ((layoutParams2.systemUiVisibility | layoutParams2.subtreeSystemUiVisibility) & 1028) == 0 ? 1 : null;
            }
            int i7 = 0;
            dimensionPixelSize3 = this.mContext.getResources().getDimensionPixelSize(17104919);
            if (obj != null) {
                i7 = dimensionPixelSize3;
            }
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
            if (rect2.top + i4 >= dimensionPixelSize3) {
                if (i4 + i2 <= rect2.bottom - rect2.top) {
                    if (i6 == 12336) {
                        this.mOverTopBoundary = false;
                        this.misGravityBottomUnder = false;
                        if (i4 < dimensionPixelSize3 && (i4 + i2) + dimensionPixelSize3 > rect.top && rect2.top + rect.bottom < displayMetrics.heightPixels) {
                            i4 = rect.bottom;
                            this.misGravityBottomUnder = true;
                        }
                        Log.d(TAG, "computePopupPositionInternal: #6 set mOverTopBoundary = " + this.mOverTopBoundary);
                    } else if (i4 < dimensionPixelSize3 && rect2.top == dimensionPixelSize3) {
                        i4 = dimensionPixelSize3;
                    }
                    if (ViewRootImpl.isDesktopmode() && mIsTaskBarInBottomInDexMode) {
                        int dimensionPixelSize5 = this.mContext.getResources().getDimensionPixelSize(C0078R.dimen.task_bar_height);
                        DisplayMetrics realDisplayMetrics2 = getRealDisplayMetrics();
                        if ((rect2.top + i4) + i2 > realDisplayMetrics2.heightPixels - dimensionPixelSize5) {
                            if (i6 == 20560 && this.mPopupType == 1) {
                                i3 = (((rect2.left + rect.centerX()) - (i / 2)) + this.mPopupOffsetX) + i <= realDisplayMetrics2.widthPixels ? (rect.centerX() - (i / 2)) + this.mPopupOffsetX : ((realDisplayMetrics2.widthPixels - rect2.left) - i) - this.mContext.getResources().getDimensionPixelSize(17105813);
                            }
                            i4 = rect.top - i2;
                        }
                    }
                } else if (i6 != 20560) {
                    Log.d(TAG, "computePopupPositionInternal: #5 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                    i4 = rect2.top != i7 ? Math.min((rect2.bottom - rect2.top) - i2, i4) : Math.min(rect2.bottom - i2, i4);
                } else if (rect.top < i2) {
                    if ((rect2.top + i4) + i2 > displayMetrics.heightPixels) {
                        i4 = (rect.top - i2) - this.mPopupOffsetY;
                        Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-2: misGravityBottomUnder = " + this.misGravityBottomUnder);
                        if (this.misGravityBottomUnder) {
                            this.misGravityBottomUnder = false;
                            Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                        }
                    }
                } else if (rect2.top != dimensionPixelSize3 || i4 + i2 > rect2.bottom) {
                    i4 = (rect.top - i2) - this.mPopupOffsetY;
                    Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-2: misGravityBottomUnder = " + this.misGravityBottomUnder);
                    if (this.misGravityBottomUnder) {
                        this.misGravityBottomUnder = false;
                        Log.d(TAG, "computePopupPositionInternal: #4 set misGravityBottomUnder = " + this.misGravityBottomUnder);
                    }
                } else {
                    Log.d(TAG, "computePopupPositionInternal: Gravity.BOTTOM_UNDER #3-1: misGravityBottomUnder = " + this.misGravityBottomUnder);
                }
            } else if (i6 != 12336) {
                Log.d(TAG, "computePopupPositionInternal #2-1: mOverTopBoundary = " + this.mOverTopBoundary);
                i4 = Math.max(dimensionPixelSize3, i4);
            } else if (((rect2.bottom - rect2.top) - rect.bottom) - dimensionPixelSize3 >= i2) {
                i4 = rect.bottom;
                if ((((rect2.bottom - rect2.top) - rect.bottom) - dimensionPixelSize3) - this.mPopupOffsetY >= i2) {
                    i4 += this.mPopupOffsetY;
                }
                Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                this.mOverTopBoundary = true;
            } else {
                if (((rect2.bottom - rect2.top) - rect.bottom) - dimensionPixelSize3 > rect.top) {
                    i4 = rect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1");
                    this.mOverTopBoundary = true;
                } else if (((rect2.top + rect.top) - i2) - i7 > 0) {
                    this.mOverTopBoundary = false;
                } else if ((displayMetrics.heightPixels - (rect2.top + rect.bottom)) - i2 > 0) {
                    i4 = rect.bottom;
                    Log.d(TAG, "computePopupPositionInternal: Set mOverTopBoundary = true #1-2");
                    this.mOverTopBoundary = true;
                } else {
                    i4 = dimensionPixelSize3;
                    this.mOverTopBoundary = false;
                    Log.d(TAG, "computePopupPositionInternal: #2: mOverTopBoundary = " + this.mOverTopBoundary);
                }
            }
        }
        this.mPopupPosX = i3;
        this.mPopupPosY = i4;
    }

    private void dismissPopup() {
        if (!this.mIsShowMessageSent && this.mShowPopupRunnable == null) {
            if (this.mDismissPopupRunnable != null) {
            }
            if (this.mPopup != null) {
                this.mPopup.dismiss();
                this.mPopup = null;
            }
        }
        this.mParentView.removeCallbacks(this.mShowPopupRunnable);
        this.mParentView.removeCallbacks(this.mDismissPopupRunnable);
        this.mShowPopupRunnable = null;
        this.mDismissPopupRunnable = null;
        this.mIsShowMessageSent = false;
        if (this.mPopup != null) {
            this.mPopup.dismiss();
            this.mPopup = null;
        }
    }

    private int getStateHashCode() {
        int i = this.mPopupType;
        if (this.mParentView == null) {
            return i;
        }
        i |= (((((this.mParentView.getWindowVisibility() << 1) | (this.mParentView.getVisibility() << 2)) | (this.mParentView.getLeft() << 4)) | (this.mParentView.getRight() << 8)) | (this.mParentView.getTop() << 12)) | (this.mParentView.getBottom() << 16);
        int[] iArr = new int[2];
        this.mParentView.getLocationOnScreen(iArr);
        return i | ((iArr[0] << 20) | (iArr[1] << 24));
    }

    private CharSequence getTooltipText() {
        return !TextUtils.isEmpty(this.mContentText) ? this.mContentText : !TextUtils.isEmpty(this.mParentView.getContentDescription()) ? this.mParentView.getContentDescription() : null;
    }

    private void initCoverManager() {
        if (this.mCoverManager == null) {
            this.mCoverManager = ICoverManager.Stub.asInterface(ServiceManager.getService("cover"));
            if (this.mCoverManager == null) {
                Log.e(TAG, "warning: no COVER_MANAGER_SERVICE");
            }
        }
    }

    private boolean isTalkbackEnabledForDeX() {
        if (!ViewRootImpl.isDesktopmode()) {
            return false;
        }
        boolean z = false;
        String stringForUser = Secure.getStringForUser(this.mContext.getContentResolver(), "enabled_accessibility_services", -3);
        if (stringForUser != null) {
            z = !stringForUser.matches("(?i).*com.samsung.android.app.talkback.TalkBackService.*") ? stringForUser.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*") : true;
        }
        return z;
    }

    private boolean isViewCoverClose() {
        boolean z = true;
        try {
            if (this.mCoverManager != null) {
                CoverState coverState = this.mCoverManager.getCoverState();
                if (coverState != null) {
                    z = coverState.getSwitchState();
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException in getCoverState: ", e);
        }
        return !z;
    }

    private void makeToolTipContentView() {
        CharSequence tooltipText = getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            this.mContentView = null;
            return;
        }
        float f = this.mContext.getResources().getConfiguration().fontScale;
        if (this.mContentView != null && this.mContentView.getId() == 117506049) {
            if (this.mFontScale != f) {
            }
            ((TextView) this.mContentView).setText(tooltipText);
        }
        if (this.mFontScale != f) {
            this.mFontScale = f;
        }
        this.mContentView = LayoutInflater.from(new ContextThemeWrapper(this.mContext, 16974123)).inflate(17367159, null);
        this.mContentView.semSetHoverPopupType(0);
        this.mContentView.setId(117506049);
        ((TextView) this.mContentView).setText(tooltipText);
    }

    private void playSoundAndHapticFeedback() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.VIBRATE") == 0) {
            this.mParentView.performHapticFeedback(50025);
        }
    }

    private boolean pointInValidHoverArea(float f, float f2) {
        return f >= ((float) this.mHoverPaddingLeft) && f < ((float) ((this.mParentView.getRight() - this.mParentView.getLeft()) - this.mHoverPaddingRight)) && f2 >= ((float) this.mHoverPaddingTop) && f2 < ((float) ((this.mParentView.getBottom() - this.mParentView.getTop()) - this.mHoverPaddingBottom));
    }

    private void resetTimeout() {
        if (this.mDismissHandler != null) {
            if (this.mDismissHandler.hasMessages(1)) {
                this.mDismissHandler.removeMessages(1);
            }
            if (Build.PRODUCT == null || !(Build.PRODUCT.startsWith("gt5note") || Build.PRODUCT.startsWith("noble") || Build.PRODUCT.startsWith("gts3"))) {
                this.mDismissHandler.sendMessageDelayed(this.mDismissHandler.obtainMessage(1), 500);
            } else {
                this.mDismissHandler.sendMessageDelayed(this.mDismissHandler.obtainMessage(1), 2000);
            }
        }
    }

    private void setAnimator(int i, int i2) {
        if (i2 == this.MOVE_LEFT || i2 == this.MOVE_RIGHT) {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{0.0f, (float) i});
        } else if (i2 == this.MOVE_LEFT_TO_CENTER || i2 == this.MOVE_RIGHT_TO_CENTER) {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{(float) i, 0.0f});
        } else {
            this.objAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 0.0f});
        }
        this.objAnimator.setInterpolator(new QuintEaseOut());
        this.objAnimator.setDuration(500);
        this.objAnimator.addUpdateListener(new C02893());
    }

    private void setPopupContent() {
        switch (this.mPopupType) {
            case 0:
                this.mContentView = null;
                break;
            case 1:
                makeToolTipContentView();
                break;
            case 2:
                makeDefaultContentView();
                break;
            case 3:
                if (this.mContentView == null && this.mContentResId != 0) {
                    try {
                        this.mContentView = LayoutInflater.from(new ContextThemeWrapper(this.mContext, 16974123)).inflate(this.mContentResId, null);
                        break;
                    } catch (InflateException e) {
                        this.mContentView = null;
                        break;
                    }
                }
            default:
                this.mContentView = null;
                break;
        }
        if (this.mListener != null) {
            this.mListener.onSetContentView(this.mParentView, this);
        }
    }

    private void showPopup() {
        if (this.mHashCodeForViewState != getStateHashCode()) {
            if (this.mIsUspFeature && this.mParentView.getWindowVisibility() == 0 && this.mParentView.getVisibility() == 0) {
                dismiss();
                show();
            } else {
                dismiss();
            }
        } else if (this.mParentView.getIsDetachedFromWindow()) {
            dismiss();
        } else {
            if (!this.mIsSkipPenPointEffect) {
                showPenPointEffect(true);
            }
            this.mIsSkipPenPointEffect = false;
            setFHGuideLineEnabled(false);
            if (this.mPopup != null) {
                this.mPopup.dismiss();
            }
            createPopupWindow();
            setPopupContent();
            update();
        }
    }

    private void updateHoverPopup(View view, int i, int i2, int i3) {
        if (this.mPopup != null) {
            computePopupPosition(view, i, i2, i3);
            if (this.mContentWidth != 0 || this.mContentHeight != 0) {
                if (this.mIsPopupTouchable && this.mTouchableContainer != null) {
                    this.mPopup.setContentView(this.mTouchableContainer);
                } else if (!this.mIsGuideLineEnabled || this.mContentContainer == null) {
                    this.mPopup.setContentView(this.mContentView);
                } else {
                    this.mPopup.setContentView(this.mContentContainer);
                }
                if (this.mPopup.getContentView() != null) {
                    if (this.mPopup.isShowing()) {
                        this.mPopup.update(this.mPopupPosX, this.mPopupPosY, this.mContentWidth, this.mContentHeight);
                    } else if (view.getApplicationWindowToken() == null || view.getApplicationWindowToken() == view.getWindowToken()) {
                        this.mPopup.showAtLocation(view, 0, this.mPopupPosX, this.mPopupPosY);
                    } else {
                        this.mPopup.showAtLocation(view.getApplicationWindowToken(), 0, this.mPopupPosX, this.mPopupPosY);
                    }
                }
            }
        }
    }

    protected int convertDPtoPX(float f, DisplayMetrics displayMetrics) {
        if (displayMetrics == null) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, f, displayMetrics) + 0.5f);
    }

    protected PopupWindow createPopupWindow() {
        if (this.mPopup == null) {
            this.mPopup = new PopupWindow(this.mParentView.getContext());
            this.mPopup.setWidth(-2);
            this.mPopup.setHeight(-2);
            this.mPopup.setTouchable(this.mIsPopupTouchable);
            this.mPopup.setClippingEnabled(false);
            this.mPopup.setBackgroundDrawable(null);
            this.mPopup.setWindowLayoutType(SemMediaCapture.KEY_PARAMETER_HEIGHT);
            View view = this.mAnchorView != null ? this.mAnchorView : this.mParentView;
            if (view.getApplicationWindowToken() != view.getWindowToken()) {
                this.mPopup.setLayoutInScreenEnabled(true);
            }
            this.mPopup.setAnimationStyle(this.mAnimationStyle);
        }
        return this.mPopup;
    }

    public void dismiss() {
        if (!this.mIsSkipPenPointEffect) {
            showPenPointEffect(false);
        }
        dismissPopup();
        this.mLeftPoint = null;
        this.mRightPoint = null;
        this.mCenterPoint = null;
        this.mPenWindowStartPos = null;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public boolean getFHAnimationEnabled() {
        return this.mIsFHAnimationEnabled;
    }

    public boolean getFHGuideLineEnabled() {
        return this.mIsFHGuideLineEnabled;
    }

    public boolean getInfoPickerMoveEabled() {
        return this.mIsInfoPickerMoveEabled;
    }

    public boolean getIsDismissTouchableHPWOnActionUp() {
        return this.mDismissTouchableHPWOnActionUp;
    }

    public View getParentView() {
        return this.mParentView;
    }

    protected DisplayMetrics getRealDisplayMetrics() {
        if (mIsCheckedRealDisplayMetricsInDexMode || !ViewRootImpl.isDesktopmode()) {
            return mRealDisplayMetricsInDexMode;
        }
        mIsCheckedRealDisplayMetricsInDexMode = true;
        IWindowManager asInterface = Stub.asInterface(ServiceManager.getService("window"));
        Point point = new Point();
        try {
            asInterface.getDefaultDisplaySize(point);
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException windowManager.getDefaultDisplaySize in getRealDisplayMetrics: ", e);
        }
        mRealDisplayMetricsInDexMode.widthPixels = point.x;
        mRealDisplayMetricsInDexMode.heightPixels = point.y;
        return mRealDisplayMetricsInDexMode;
    }

    protected void initInstance() {
        this.mPopup = null;
        this.mEnabled = true;
        this.mHoverDetectTimeMS = HOVER_DETECT_TIME_MS;
        this.mPopupGravity = 12849;
        this.mPopupPosX = 0;
        this.mPopupPosY = 0;
        this.mHoveringPointX = 0;
        this.mHoveringPointY = 0;
        this.mPopupOffsetX = 0;
        this.mPopupOffsetY = 0;
        this.mWindowGapX = 0;
        this.mWindowGapY = 0;
        this.mHoverPaddingLeft = 0;
        this.mHoverPaddingRight = 0;
        this.mHoverPaddingTop = 0;
        this.mHoverPaddingBottom = 0;
        this.mListener = null;
        this.mContentText = null;
        if (ViewRootImpl.isDesktopmode()) {
            this.mAnimationStyle = 16975125;
        } else {
            this.mAnimationStyle = 16975124;
        }
        this.mIsGuideLineEnabled = false;
        this.mIsFHGuideLineEnabled = false;
        this.misDialer = false;
        this.mIsProgressBar = false;
        this.mIsFHAnimationEnabled = true;
        this.mIsInfoPickerMoveEabled = true;
        this.mIsFHGuideLineEnabledByApp = false;
        this.mIsFHAnimationEnabledByApp = false;
        this.mIsInfoPickerMoveEabledByApp = false;
        this.mIsSetInfoPickerColorToAndMoreBottomImg = false;
        this.mIsFHSoundAndHapticEnabled = true;
        this.mCoordinatesOfAnchorView = 0;
        this.mOverTopBoundary = false;
        this.misGravityBottomUnder = false;
        this.mGuideLineFadeOffset = 0;
        this.mContentView = null;
        this.mContentContainer = null;
        this.mTouchableContainer = null;
        this.mAnchorView = null;
        this.mIsSPenPointChanged = false;
        this.mIsPopupTouchable = false;
        this.mIsTryingShowPopup = false;
        this.mIsSkipPenPointEffect = false;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        this.mGuideRingDrawableId = obtainStyledAttributes.getResourceId(358, 17302382);
        this.mGuideLineColor = obtainStyledAttributes.getColor(357, -8810071);
        obtainStyledAttributes.recycle();
        this.mFullTextPopupRightLimit = -1;
        this.mPenWindowStartPos = new Point();
        this.mPenWindowStartPos.x = 0;
        this.mPenWindowStartPos.y = 0;
        this.mFontScale = 0.0f;
        this.mIsUspFeature = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.spen_usp");
        initCoverManager();
    }

    public boolean isDialer() {
        return this.misDialer;
    }

    protected boolean isFingerHoveringSettingsEnabled(int i) {
        if ((System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view", 0, -3) == 1) && i != 1) {
            if (System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view_information_preview", 0, -3) == 1) {
                return (System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view_pregress_bar_preview", 0, -3) == 0 && isProgressBar()) ? false : System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view_speed_dial_tip", 0, -3) == 1 || !isDialer();
            } else {
                if (System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view_speed_dial_tip", 0, -3) == 1 && isDialer()) {
                    return true;
                }
                if (System.getIntForUser(this.mContext.getContentResolver(), "finger_air_view_pregress_bar_preview", 0, -3) == 1 && isProgressBar()) {
                    return true;
                }
            }
        }
    }

    public boolean isHoverPopupPossible() {
        return this.mPopupType == 0 ? false : this.mPopupType == 1 ? (this.mParentView == null || TextUtils.isEmpty(getTooltipText())) ? false : true : this.mPopupType == 2 ? false : this.mPopupType == 3 ? true : true;
    }

    protected boolean isHoveringSettingEnabled(int i) {
        return this.mToolType == 2 ? isSPenHoveringSettingsEnabled(i) : this.mToolType == 1 ? isFingerHoveringSettingsEnabled(i) : this.mToolType == 3 ? isMouseHoveringSettingsEnabled(i) : false;
    }

    public boolean isLockScreenMode() {
        return ((KeyguardManager) this.mContext.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    protected boolean isMouseHoveringSettingsEnabled(int i) {
        return ViewRootImpl.isDesktopmode();
    }

    public boolean isProgressBar() {
        return this.mIsProgressBar;
    }

    protected boolean isSPenHoveringSettingsEnabled(int i) {
        return System.getIntForUser(this.mContext.getContentResolver(), "pen_hovering", 0, -3) == 1;
    }

    public boolean isShowing() {
        return this.mPopup != null ? this.mPopup.isShowing() : false;
    }

    public boolean isUseOldAirviewSettingsMenu() {
        return (Build.PRODUCT != null && (Build.PRODUCT.startsWith("hlte") || Build.PRODUCT.startsWith("h3g") || Build.PRODUCT.startsWith("ha3g"))) || SmartFaceManager.TRUE.equals(AIRCOMMAND_MORPH_USP);
    }

    protected boolean isUspFeature() {
        return this.mIsUspFeature;
    }

    protected void makeDefaultContentView() {
        makeToolTipContentView();
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (action == 9) {
            if (SystemClock.uptimeMillis() - motionEvent.getEventTime() > 1000) {
                return true;
            }
            if (this.mIsHoverPaddingEnabled) {
                if (pointInValidHoverArea(x, y)) {
                    this.mIsTryingShowPopup = true;
                } else {
                    this.mIsTryingShowPopup = false;
                }
            }
        } else if (action == 7) {
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            setHoveringPoint(rawX, rawY);
            if (this.mIsHoverPaddingEnabled) {
                boolean pointInValidHoverArea = pointInValidHoverArea(x, y);
                if (!pointInValidHoverArea || this.mIsTryingShowPopup) {
                    if (!(pointInValidHoverArea || !this.mIsTryingShowPopup || this.mIsPopupTouchable)) {
                        this.mIsTryingShowPopup = false;
                        dismiss();
                        return true;
                    }
                } else if (SystemClock.uptimeMillis() - motionEvent.getEventTime() > 1000) {
                    this.mIsTryingShowPopup = false;
                    return true;
                } else {
                    this.mIsTryingShowPopup = true;
                    show();
                    return true;
                }
            }
            if ((this.mIsGuideLineEnabled || this.mIsFHGuideLineEnabled) && isShowing()) {
                View contentView = this.mPopup.getContentView();
                if (contentView instanceof HoverPopupContainer) {
                    HoverPopupContainer hoverPopupContainer = (HoverPopupContainer) contentView;
                    if (!(this.mContentContainer == null || this.mContentView == null)) {
                        int paddingLeft = this.mContentContainer.getPaddingLeft();
                        this.mContentContainer.setPickerLimit(paddingLeft, this.mContentView.getWidth() + paddingLeft);
                    }
                    if ((this.mAnchorView != null ? this.mAnchorView : this.mParentView).getViewRootImpl() != null) {
                        PointF pointF = new PointF(0.0f, 0.0f);
                        PointF pointF2 = new PointF(0.0f, 0.0f);
                        if (pointF.x == 1.0f && pointF.y == 1.0f) {
                            hoverPopupContainer.setGuideLineEndPoint((rawX - this.mPopupPosX) - this.mWindowGapX, (rawY - this.mPopupPosY) - this.mWindowGapY);
                        } else {
                            Rect rect = new Rect();
                            if (!(this.mPenWindowStartPos == null || (this.mPenWindowStartPos.x == 0 && this.mPenWindowStartPos.y == 0))) {
                                rect.offset(this.mPenWindowStartPos.x, this.mPenWindowStartPos.y);
                            }
                            pointF2.x = ((((float) rawX) - (((float) this.mPopupPosX) * pointF.x)) - ((float) rect.left)) / pointF.x;
                            pointF2.y = ((((float) rawY) - (((float) this.mPopupPosY) * pointF.y)) - ((float) rect.top)) / pointF.y;
                            hoverPopupContainer.setGuideLineEndPoint((int) pointF2.x, (int) pointF2.y);
                        }
                    }
                    if (!this.mPopup.isShowing()) {
                        hoverPopupContainer.updateDecoration();
                    } else if (this.mIsFHAnimationEnabled || this.mIsFHGuideLineEnabled) {
                        if (this.mIsFHGuideLineEnabled) {
                            hoverPopupContainer.setFHGuideLineForCotainer(true);
                        }
                        hoverPopupContainer.updateDecoration();
                    }
                }
            }
            if (this.mToolType != 3) {
                resetTimeout();
            }
            return true;
        } else if (action == 10) {
            if (this.mContentContainer != null) {
                this.mContentContainer.setPopupState(2);
            }
            if (this.mIsPopupTouchable) {
                if (this.mDismissHandler != null && this.mDismissHandler.hasMessages(1)) {
                    this.mDismissHandler.removeMessages(1);
                }
                if (isShowing()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void postDismiss(int i) {
        this.mParentView.postDelayed(new C02926(), (long) i);
    }

    public void setAnimationStyle(int i) {
        this.mAnimationStyle = i;
        if (this.mPopup != null) {
            this.mPopup.setAnimationStyle(this.mAnimationStyle);
        }
    }

    public void setContent(View view) {
        LayoutParams layoutParams = null;
        if (view != null) {
            layoutParams = view.getLayoutParams();
        }
        setContent(view, layoutParams);
    }

    public void setContent(View view, LayoutParams layoutParams) {
        this.mContentView = view;
        this.mContentLP = layoutParams;
        this.mNeedToMeasureContentView = true;
    }

    public void setContent(CharSequence charSequence) {
        this.mContentText = charSequence;
        this.mNeedToMeasureContentView = true;
    }

    public void setDismissTouchableHPWOnActionUp(boolean z) {
        this.mDismissTouchableHPWOnActionUp = z;
    }

    public void setFHAnimationEnabled(boolean z) {
        this.mIsFHAnimationEnabledByApp = true;
        setFHAnimationEnabledByApp(z, true);
    }

    public void setFHAnimationEnabledByApp(boolean z, boolean z2) {
        if (z2) {
            this.mIsFHAnimationEnabled = z;
        } else if (!this.mIsFHAnimationEnabledByApp) {
            this.mIsFHAnimationEnabled = z;
        }
    }

    public void setFHGuideLineEnabled(boolean z) {
        this.mIsFHGuideLineEnabledByApp = true;
        setFHGuideLineEnabledByApp(z, true);
    }

    public void setFHGuideLineEnabledByApp(boolean z, boolean z2) {
        if (z2) {
            this.mIsFHGuideLineEnabled = z;
            if (this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = true;
            } else if (!this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = false;
            }
        } else if (!this.mIsFHGuideLineEnabledByApp) {
            this.mIsFHGuideLineEnabled = z;
            if (this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = true;
            } else if (!this.mIsFHGuideLineEnabled) {
                this.mIsGuideLineEnabled = false;
            }
        }
    }

    public void setFHSoundAndHapticEnabled(boolean z) {
        this.mIsFHSoundAndHapticEnabled = z;
    }

    public void setGravity(int i) {
        this.mPopupGravity = i;
    }

    public void setGuideLineEnabled(boolean z) {
        this.mIsGuideLineEnabled = z;
    }

    public void setGuideLineFadeOffset(int i) {
        this.mGuideLineFadeOffset = convertDPtoPX((float) i, null);
    }

    public void setGuideLineStyle(int i, int i2) {
        this.mGuideRingDrawableId = i;
        this.mGuideLineColor = i2;
    }

    public void setHoverDetectTime(int i) {
        this.mHoverDetectTimeMS = i;
    }

    public void setHoverPaddingArea(int i, int i2, int i3, int i4) {
        this.mHoverPaddingLeft = i;
        this.mHoverPaddingRight = i3;
        this.mHoverPaddingTop = i2;
        this.mHoverPaddingBottom = i4;
        if (this.mHoverPaddingLeft == 0 && this.mHoverPaddingRight == 0 && this.mHoverPaddingTop == 0) {
            if (this.mHoverPaddingBottom == 0) {
                return;
            }
        }
        this.mIsHoverPaddingEnabled = true;
    }

    public void setHoverPopupPreShowListener(HoverPopupPreShowListener hoverPopupPreShowListener) {
        this.mPreShowListener = hoverPopupPreShowListener;
    }

    public void setHoverPopupToolType(int i) {
        this.mToolType = i;
    }

    public void setHoveringPoint(int i, int i2) {
        this.mHoveringPointX = i;
        this.mHoveringPointY = i2;
    }

    public void setInfoPickerColorToAndMoreBottomImg(boolean z) {
        this.mIsSetInfoPickerColorToAndMoreBottomImg = z;
    }

    public void setInfoPickerMoveEabled(boolean z) {
        this.mIsInfoPickerMoveEabledByApp = true;
        setInfoPickerMoveEabledByApp(z, true);
    }

    public void setInfoPickerMoveEabledByApp(boolean z, boolean z2) {
        if (z2) {
            this.mIsInfoPickerMoveEabled = z;
        } else if (!this.mIsInfoPickerMoveEabledByApp) {
            this.mIsInfoPickerMoveEabled = z;
        }
    }

    protected void setInstanceByType(int i) {
        if (i == 1) {
            this.mPopupGravity = 20819;
            if (ViewRootImpl.isDesktopmode()) {
                this.mHoverDetectTimeMS = HOVER_DETECT_TIME_MS_DEX;
                this.mAnimationStyle = 16975125;
                return;
            }
            this.mHoverDetectTimeMS = HOVER_DETECT_TIME_MS;
            this.mAnimationStyle = 16975124;
        }
    }

    public void setInstanceOfDialer(boolean z) {
        this.misDialer = z;
    }

    public void setInstanceOfProgressBar(boolean z) {
        this.mIsProgressBar = z;
    }

    public void setNeedNotWindowOffset(boolean z) {
        this.mNeedNotWindowOffset = z;
    }

    public void setOffset(int i, int i2) {
        this.mPopupOffsetX = i;
        this.mPopupOffsetY = i2;
    }

    public void setOnSetContentViewListener(OnSetContentViewListener onSetContentViewListener) {
        this.mListener = onSetContentViewListener;
    }

    public void setOverTopPickerOffset(int i) {
        if (this.mContentContainer != null) {
            this.mContentContainer.setOverTopPickerOffset(i);
        }
    }

    public void setPickerXOffset(int i) {
        this.mPickerXoffset = i;
    }

    public void setTouchable(boolean z) {
        this.mIsPopupTouchable = z;
        if (this.mPopup != null) {
            this.mPopup.setTouchable(this.mIsPopupTouchable);
        }
    }

    public void show() {
        int semGetHoverPopupType = (this.mAnchorView != null ? this.mAnchorView : this.mParentView).semGetHoverPopupType();
        if (semGetHoverPopupType != this.mPopupType) {
            this.mPopupType = semGetHoverPopupType;
            setInstanceByType(semGetHoverPopupType);
        }
        if ((this.mPreShowListener == null || this.mPreShowListener.onHoverPopupPreShow()) && this.mEnabled && semGetHoverPopupType != 0 && !this.mIsShowMessageSent && ((!this.mIsHoverPaddingEnabled || this.mIsTryingShowPopup) && isHoverPopupPossible() && isHoveringSettingEnabled(semGetHoverPopupType) && !isShowing() && this.mParentView.getHandler() != null && !isViewCoverClose() && !isLockScreenMode() && !isTalkbackEnabledForDeX())) {
            LayoutParams layoutParams = this.mParentView.getRootView().getLayoutParams();
            if (layoutParams instanceof WindowManager.LayoutParams) {
                LayoutParams layoutParams2 = layoutParams;
                if (layoutParams2.type == 2220 || layoutParams2.type == 98) {
                    setFHGuideLineEnabled(false);
                }
            }
            this.mHashCodeForViewState = getStateHashCode();
            if (!this.mIsSkipPenPointEffect) {
                showPenPointEffect(true);
            }
            if (this.mIsFHSoundAndHapticEnabled && this.mToolType == 1 && System.getInt(this.mContext.getContentResolver(), "finger_air_view_sound_and_haptic_feedback", 0) == 1) {
                playSoundAndHapticFeedback();
            }
            if (this.mPopupType == 1) {
                this.mDismissPopupRunnable = new C02904();
            }
            this.mShowPopupRunnable = new C02915();
            this.mParentView.postDelayed(this.mShowPopupRunnable, (long) this.mHoverDetectTimeMS);
            this.mIsShowMessageSent = true;
        }
    }

    protected void showPenPointEffect(boolean z) {
        if (this.mToolType != 2) {
            return;
        }
        if (z) {
            InputManager.getInstance().setPointerIconType(20010);
            this.mIsSPenPointChanged = true;
        } else if (!z && this.mIsSPenPointChanged) {
            InputManager.getInstance().setPointerIconType(20001);
            this.mIsSPenPointChanged = false;
        }
    }

    public void update() {
        if (this.mPopup == null || !this.mPopup.isShowing() || this.mNeedToMeasureContentView) {
            updateHoverPopup(this.mAnchorView != null ? this.mAnchorView : this.mParentView, this.mPopupGravity, this.mPopupOffsetX, this.mPopupOffsetY);
            return;
        }
        computePopupPositionInternal(this.mAnchorRect, this.mDisplayFrame, this.mContentWidth, this.mContentHeight);
        this.mPopup.update(this.mPopupPosX, this.mPopupPosY, -1, -1);
    }
}
