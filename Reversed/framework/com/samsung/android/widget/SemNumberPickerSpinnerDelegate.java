package com.samsung.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.AccessibilityRecord;
import android.view.animation.PathInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.widget.SemNumberPicker.CustomEditText;
import com.samsung.android.widget.SemNumberPicker.Formatter;
import com.samsung.android.widget.SemNumberPicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemNumberPicker.OnScrollListener;
import com.samsung.android.widget.SemNumberPicker.OnValueChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class SemNumberPickerSpinnerDelegate extends AbstractSemNumberPickerDelegate {
    private static final int DECREASE_BUTTON = 2;
    private static final int DECREASE_BUTTON_EX = 1;
    private static final int DEFAULT_CHANGE_VALUE_BY = 1;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final char[] DIGIT_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩', '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹', '०', '१', '२', '३', '४', '५', '६', '७', '८', '९', '০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯', '೦', '೧', '೨', '೩', '೪', '೫', '೬', '೭', '೮', '೯'};
    private static final int INCREASE_BUTTON = 4;
    private static final int INCREASE_BUTTON_EX = 5;
    private static final int INPUT = 3;
    private static final int LONG_PRESSED_SCROLL_COUNT = 10;
    private static final int SELECTOR_ADJUSTMENT_DURATION_MILLIS = 300;
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 4;
    private static final int SELECTOR_MIDDLE_ITEM_INDEX = 2;
    private static final int SELECTOR_WHEEL_ITEM_COUNT = 5;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SNAP_SCROLL_DURATION = 500;
    private static final int START_ANIMATION_SCROLL_DURATION = 857;
    private static final int START_ANIMATION_SCROLL_DURATION_2016B = 557;
    private static final int UNSCALED_DEFAULT_SELECTION_DIVIDER_HEIGHT = 2;
    private AccessibilityNodeProviderImpl mAccessibilityNodeProvider;
    private final Scroller mAdjustScroller;
    private final PathInterpolator mAlphaPathInterpolator = new PathInterpolator(0.85f, 0.25f, 1.0f, 1.0f);
    private SemAnimationListener mAnimationListener;
    AudioManager mAudioManager;
    private BeginSoftInputOnLongPressCommand mBeginSoftInputOnLongPressCommand;
    private int mBelowBottomSelectionDividerBottom;
    private int mBottomSelectionDividerBottom;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private int mChangeValueBy = 1;
    private final boolean mComputeMaxWidth;
    private int mCurrentScrollOffset;
    private final Scroller mCustomScroller;
    private boolean mCustomTypefaceSet = false;
    private boolean mDecrementVirtualButtonPressed;
    private final int mDefaultEdgeHeight;
    private Typeface mDefaultTypeface;
    private String[] mDisplayedValues;
    private Scroller mFlingScroller;
    private Formatter mFormatter;
    private final float mHeightRatio;
    private boolean mHideWheelUntilFocused;
    private boolean mIgnoreMoveEvents;
    private boolean mIncrementVirtualButtonPressed;
    private int mInitialScrollOffset = FingerprintManager.PRIVILEGED_TYPE_KEYGUARD;
    private final EditText mInputText;
    private boolean mIsAmPm;
    private boolean mIsEditTextMode;
    private boolean mIsLongPressed = false;
    private boolean mIsStartingAnimation = false;
    private long mLastDownEventTime;
    private float mLastDownEventY;
    private float mLastDownOrMoveEventY;
    private int mLastFocusedChildVirtualViewId;
    private int mLastHoveredChildVirtualViewId;
    private final Scroller mLinearScroller;
    private int mLongPressCount;
    private long mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
    private boolean mLongPressed_FIRST_SCROLL;
    private boolean mLongPressed_SECOND_SCROLL;
    private boolean mLongPressed_THIRD_SCROLL;
    private final int mMaxHeight;
    private int mMaxValue;
    private int mMaxWidth;
    private int mMaximumFlingVelocity;
    private final int mMinHeight;
    private int mMinValue;
    private final int mMinWidth;
    private int mMinimumFlingVelocity;
    private int mModifiedTxtHeight;
    private OnEditTextModeChangedListener mOnEditTextModeChangedListener;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private int mOverTopSelectionDividerTop;
    private PathInterpolator mPathInterpolator;
    private boolean mPerformClickOnTap;
    private Typeface mPickerBoldTypeface;
    private String mPickerContentDescription;
    private Typeface mPickerTypeface;
    private final PressedStateHelper mPressedStateHelper;
    private int mPreviousScrollerY;
    private boolean mReservedStartAnimation = false;
    private int mScrollState = 0;
    private int mSelectedPickerColor;
    private final int mSelectionDividerHeight;
    private int mSelectorElementHeight;
    private final SparseArray<String> mSelectorIndexToStringCache = new SparseArray();
    private final int[] mSelectorIndices = new int[5];
    private int mSelectorTextGapHeight;
    private Paint mSelectorWheelPaint;
    private SetSelectionCommand mSetSelectionCommand;
    private boolean mSkipNumbers;
    private final int mSubTextColor;
    private int mSubTextSize;
    private final int mTextColor;
    private int mTextSize;
    private String mToastText;
    private int mTopSelectionDividerTop;
    private int mTouchSlop;
    private int mValue;
    private VelocityTracker mVelocityTracker;
    private final Drawable mVirtualButtonFocusedDrawable;
    private boolean mWrapSelectorWheel;

    class C02991 implements OnFocusChangeListener {
        C02991() {
        }

        public void onFocusChange(View view, boolean z) {
            if (z) {
                SemNumberPickerSpinnerDelegate.this.setEditTextMode(true);
                SemNumberPickerSpinnerDelegate.this.mInputText.selectAll();
                return;
            }
            SemNumberPickerSpinnerDelegate.this.mInputText.setSelection(0, 0);
            SemNumberPickerSpinnerDelegate.this.validateInputTextView(view);
        }
    }

    class C03002 implements OnTouchListener {
        C03002() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!(view instanceof EditText) || motionEvent.getActionMasked() != 0) {
                return false;
            }
            view.selectAll();
            SemNumberPickerSpinnerDelegate.this.showSoftInput();
            return true;
        }
    }

    class C03023 implements Runnable {

        class C03011 implements Runnable {
            C03011() {
            }

            public void run() {
                InputMethodManager peekInstance = InputMethodManager.peekInstance();
                if (peekInstance != null && SemNumberPickerSpinnerDelegate.this.mIsEditTextMode && SemNumberPickerSpinnerDelegate.this.mInputText.isFocused()) {
                    peekInstance.showSoftInput(SemNumberPickerSpinnerDelegate.this.mInputText, 0);
                }
            }
        }

        C03023() {
        }

        public void run() {
            InputMethodManager peekInstance = InputMethodManager.peekInstance();
            if (peekInstance != null && SemNumberPickerSpinnerDelegate.this.mIsEditTextMode && SemNumberPickerSpinnerDelegate.this.mInputText.isFocused() && !peekInstance.showSoftInput(SemNumberPickerSpinnerDelegate.this.mInputText, 0)) {
                SemNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(new C03011(), 20);
            }
        }
    }

    class AccessibilityNodeProviderImpl extends AccessibilityNodeProvider {
        private static final int UNDEFINED = Integer.MIN_VALUE;
        private static final int VIRTUAL_VIEW_ID_DECREMENT = 2;
        private static final int VIRTUAL_VIEW_ID_DECREMENT_EX = 1;
        private static final int VIRTUAL_VIEW_ID_INCREMENT = 4;
        private static final int VIRTUAL_VIEW_ID_INCREMENT_EX = 5;
        private static final int VIRTUAL_VIEW_ID_INPUT = 3;
        private int mAccessibilityFocusedView = Integer.MIN_VALUE;
        private final int[] mTempArray = new int[2];
        private final Rect mTempRect = new Rect();

        AccessibilityNodeProviderImpl() {
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForNumberPicker(int i, int i2, int i3, int i4) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setClassName(NumberPicker.class.getName());
            obtain.setPackageName(SemNumberPickerSpinnerDelegate.this.mContext.getPackageName());
            obtain.setSource(SemNumberPickerSpinnerDelegate.this.mDelegator);
            if (hasVirtualDecrementExButton()) {
                obtain.addChild(SemNumberPickerSpinnerDelegate.this.mDelegator, 1);
            }
            if (hasVirtualDecrementButton()) {
                obtain.addChild(SemNumberPickerSpinnerDelegate.this.mDelegator, 2);
            }
            obtain.addChild(SemNumberPickerSpinnerDelegate.this.mDelegator, 3);
            if (hasVirtualIncrementButton()) {
                obtain.addChild(SemNumberPickerSpinnerDelegate.this.mDelegator, 4);
            }
            if (hasVirtualIncrementExButton()) {
                obtain.addChild(SemNumberPickerSpinnerDelegate.this.mDelegator, 5);
            }
            obtain.setParent((View) SemNumberPickerSpinnerDelegate.this.mDelegator.getParentForAccessibility());
            obtain.setEnabled(SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
            obtain.setScrollable(true);
            float f = SemNumberPickerSpinnerDelegate.this.mContext.getResources().getCompatibilityInfo().applicationScale;
            Rect rect = this.mTempRect;
            rect.set(i, i2, i3, i4);
            rect.scale(f);
            obtain.setBoundsInParent(rect);
            obtain.setVisibleToUser(SemNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper());
            Rect rect2 = rect;
            int[] iArr = this.mTempArray;
            SemNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            rect.scale(f);
            obtain.setBoundsInScreen(rect);
            if (this.mAccessibilityFocusedView != -1) {
                obtain.addAction(64);
            }
            if (this.mAccessibilityFocusedView == -1) {
                obtain.addAction(128);
            }
            if (SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                if (SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() < SemNumberPickerSpinnerDelegate.this.getMaxValue()) {
                    obtain.addAction(4096);
                }
                if (SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() > SemNumberPickerSpinnerDelegate.this.getMinValue()) {
                    obtain.addAction(8192);
                }
            }
            return obtain;
        }

        private AccessibilityNodeInfo createAccessibilityNodeInfoForVirtualButton(int i, String str, int i2, int i3, int i4, int i5) {
            AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
            obtain.setClassName(Button.class.getName());
            obtain.setPackageName(SemNumberPickerSpinnerDelegate.this.mContext.getPackageName());
            obtain.setSource(SemNumberPickerSpinnerDelegate.this.mDelegator, i);
            obtain.setParent(SemNumberPickerSpinnerDelegate.this.mDelegator);
            obtain.setText(str);
            obtain.setClickable(true);
            obtain.setLongClickable(true);
            obtain.setEnabled(SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
            Rect rect = this.mTempRect;
            rect.set(i2, i3, i4, i5);
            obtain.setVisibleToUser(SemNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper(rect));
            obtain.setBoundsInParent(rect);
            Rect rect2 = rect;
            int[] iArr = this.mTempArray;
            SemNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            obtain.setBoundsInScreen(rect);
            if (this.mAccessibilityFocusedView != i) {
                obtain.addAction(64);
            }
            if (this.mAccessibilityFocusedView == i) {
                obtain.addAction(128);
            }
            if (SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                obtain.addAction(16);
            }
            return obtain;
        }

        private AccessibilityNodeInfo createAccessibiltyNodeInfoForInputText(int i, int i2, int i3, int i4) {
            AccessibilityNodeInfo createAccessibilityNodeInfo = SemNumberPickerSpinnerDelegate.this.mInputText.createAccessibilityNodeInfo();
            createAccessibilityNodeInfo.setSource(SemNumberPickerSpinnerDelegate.this.mDelegator, 3);
            if (this.mAccessibilityFocusedView != 3) {
                createAccessibilityNodeInfo.addAction(64);
            }
            if (this.mAccessibilityFocusedView == 3) {
                createAccessibilityNodeInfo.addAction(128);
            }
            if (SemNumberPickerSpinnerDelegate.this.mIsAmPm) {
                createAccessibilityNodeInfo.setClassName(TextView.class.getName());
                createAccessibilityNodeInfo.setText(getVirtualCurrentButtonText());
            }
            Rect rect = this.mTempRect;
            rect.set(i, i2, i3, i4);
            createAccessibilityNodeInfo.setVisibleToUser(SemNumberPickerSpinnerDelegate.this.mDelegator.isVisibleToUserWrapper(rect));
            createAccessibilityNodeInfo.setBoundsInParent(rect);
            Rect rect2 = rect;
            int[] iArr = this.mTempArray;
            SemNumberPickerSpinnerDelegate.this.mDelegator.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            createAccessibilityNodeInfo.setBoundsInScreen(rect);
            return createAccessibilityNodeInfo;
        }

        private void findAccessibilityNodeInfosByTextInChild(String str, int i, List<AccessibilityNodeInfo> list) {
            Object virtualDecrementExButtonText;
            switch (i) {
                case 1:
                    virtualDecrementExButtonText = getVirtualDecrementExButtonText();
                    if (!TextUtils.isEmpty(virtualDecrementExButtonText) && virtualDecrementExButtonText.toString().toLowerCase().contains(str)) {
                        list.add(createAccessibilityNodeInfo(1));
                    }
                    return;
                case 2:
                    virtualDecrementExButtonText = getVirtualDecrementButtonText();
                    if (!TextUtils.isEmpty(virtualDecrementExButtonText) && virtualDecrementExButtonText.toString().toLowerCase().contains(str)) {
                        list.add(createAccessibilityNodeInfo(2));
                    }
                    return;
                case 3:
                    CharSequence text = SemNumberPickerSpinnerDelegate.this.mInputText.getText();
                    if (TextUtils.isEmpty(text) || !text.toString().toLowerCase().contains(str)) {
                        CharSequence text2 = SemNumberPickerSpinnerDelegate.this.mInputText.getText();
                        if (!TextUtils.isEmpty(text2) && text2.toString().toLowerCase().contains(str)) {
                            list.add(createAccessibilityNodeInfo(3));
                            return;
                        }
                    }
                    list.add(createAccessibilityNodeInfo(3));
                    return;
                    break;
                case 4:
                    virtualDecrementExButtonText = getVirtualIncrementButtonText();
                    if (!TextUtils.isEmpty(virtualDecrementExButtonText) && virtualDecrementExButtonText.toString().toLowerCase().contains(str)) {
                        list.add(createAccessibilityNodeInfo(4));
                    }
                    return;
                case 5:
                    virtualDecrementExButtonText = getVirtualIncrementExButtonText();
                    if (!TextUtils.isEmpty(virtualDecrementExButtonText) && virtualDecrementExButtonText.toString().toLowerCase().contains(str)) {
                        list.add(createAccessibilityNodeInfo(5));
                    }
                    return;
            }
        }

        private String getVirtualCurrentButtonText() {
            int -get26 = SemNumberPickerSpinnerDelegate.this.mValue;
            if (SemNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                -get26 = SemNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(-get26);
            }
            if (-get26 > SemNumberPickerSpinnerDelegate.this.mMaxValue) {
                return null;
            }
            return (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null ? SemNumberPickerSpinnerDelegate.this.formatNumber(-get26) : SemNumberPickerSpinnerDelegate.this.mDisplayedValues[-get26 - SemNumberPickerSpinnerDelegate.this.mMinValue]) + ", " + SemNumberPickerSpinnerDelegate.this.mPickerContentDescription + " ";
        }

        private String getVirtualDecrementButtonText() {
            int -get26 = SemNumberPickerSpinnerDelegate.this.mValue - 1;
            if (SemNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                -get26 = SemNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(-get26);
            }
            if (-get26 < SemNumberPickerSpinnerDelegate.this.mMinValue) {
                return null;
            }
            return (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null ? SemNumberPickerSpinnerDelegate.this.formatNumber(-get26) : SemNumberPickerSpinnerDelegate.this.mDisplayedValues[-get26 - SemNumberPickerSpinnerDelegate.this.mMinValue]) + ", " + SemNumberPickerSpinnerDelegate.this.mPickerContentDescription + ", ";
        }

        private String getVirtualDecrementExButtonText() {
            int -get26 = SemNumberPickerSpinnerDelegate.this.mValue - 2;
            if (SemNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                -get26 = SemNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(-get26);
            }
            if (-get26 < SemNumberPickerSpinnerDelegate.this.mMinValue) {
                return null;
            }
            return (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null ? SemNumberPickerSpinnerDelegate.this.formatNumber(-get26) : SemNumberPickerSpinnerDelegate.this.mDisplayedValues[-get26 - SemNumberPickerSpinnerDelegate.this.mMinValue]) + ", " + SemNumberPickerSpinnerDelegate.this.mPickerContentDescription + ", ";
        }

        private String getVirtualIncrementButtonText() {
            int -get26 = SemNumberPickerSpinnerDelegate.this.mValue + 1;
            if (SemNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                -get26 = SemNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(-get26);
            }
            if (-get26 > SemNumberPickerSpinnerDelegate.this.mMaxValue) {
                return null;
            }
            return (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null ? SemNumberPickerSpinnerDelegate.this.formatNumber(-get26) : SemNumberPickerSpinnerDelegate.this.mDisplayedValues[-get26 - SemNumberPickerSpinnerDelegate.this.mMinValue]) + ", " + SemNumberPickerSpinnerDelegate.this.mPickerContentDescription + ", ";
        }

        private String getVirtualIncrementExButtonText() {
            int -get26 = SemNumberPickerSpinnerDelegate.this.mValue + 2;
            if (SemNumberPickerSpinnerDelegate.this.mWrapSelectorWheel) {
                -get26 = SemNumberPickerSpinnerDelegate.this.getWrappedSelectorIndex(-get26);
            }
            if (-get26 > SemNumberPickerSpinnerDelegate.this.mMaxValue) {
                return null;
            }
            return (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null ? SemNumberPickerSpinnerDelegate.this.formatNumber(-get26) : SemNumberPickerSpinnerDelegate.this.mDisplayedValues[-get26 - SemNumberPickerSpinnerDelegate.this.mMinValue]) + ", " + SemNumberPickerSpinnerDelegate.this.mPickerContentDescription + ", ";
        }

        private boolean hasVirtualDecrementButton() {
            return SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() > SemNumberPickerSpinnerDelegate.this.getMinValue();
        }

        private boolean hasVirtualDecrementExButton() {
            return SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() > SemNumberPickerSpinnerDelegate.this.getMinValue() + 1;
        }

        private boolean hasVirtualIncrementButton() {
            return SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() < SemNumberPickerSpinnerDelegate.this.getMaxValue();
        }

        private boolean hasVirtualIncrementExButton() {
            return SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() || SemNumberPickerSpinnerDelegate.this.getValue() < SemNumberPickerSpinnerDelegate.this.getMaxValue() - 1;
        }

        private void sendAccessibilityEventForVirtualButton(int i, int i2, String str) {
            if (AccessibilityManager.getInstance(SemNumberPickerSpinnerDelegate.this.mContext).isEnabled()) {
                AccessibilityRecord obtain = AccessibilityEvent.obtain(i2);
                obtain.setClassName(Button.class.getName());
                obtain.setPackageName(SemNumberPickerSpinnerDelegate.this.mContext.getPackageName());
                obtain.getText().add(str);
                obtain.setEnabled(SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled());
                obtain.setSource(SemNumberPickerSpinnerDelegate.this.mDelegator, i);
                SemNumberPickerSpinnerDelegate.this.mDelegator.requestSendAccessibilityEvent(SemNumberPickerSpinnerDelegate.this.mDelegator, obtain);
            }
        }

        private void sendAccessibilityEventForVirtualText(int i) {
            if (AccessibilityManager.getInstance(SemNumberPickerSpinnerDelegate.this.mContext).isEnabled()) {
                AccessibilityRecord obtain = AccessibilityEvent.obtain(i);
                SemNumberPickerSpinnerDelegate.this.mInputText.onInitializeAccessibilityEvent(obtain);
                SemNumberPickerSpinnerDelegate.this.mInputText.onPopulateAccessibilityEvent(obtain);
                obtain.setSource(SemNumberPickerSpinnerDelegate.this.mDelegator, 3);
                SemNumberPickerSpinnerDelegate.this.mDelegator.requestSendAccessibilityEvent(SemNumberPickerSpinnerDelegate.this.mDelegator, obtain);
            }
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            int left = SemNumberPickerSpinnerDelegate.this.mDelegator.getLeft();
            int right = SemNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int top = SemNumberPickerSpinnerDelegate.this.mDelegator.getTop();
            int bottom = SemNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            int scrollX = SemNumberPickerSpinnerDelegate.this.mDelegator.getScrollX();
            int scrollY = SemNumberPickerSpinnerDelegate.this.mDelegator.getScrollY();
            if (!(SemNumberPickerSpinnerDelegate.this.mLastFocusedChildVirtualViewId == -1 && SemNumberPickerSpinnerDelegate.this.mLastHoveredChildVirtualViewId == Integer.MIN_VALUE)) {
                switch (i) {
                    case -1:
                        return createAccessibilityNodeInfoForNumberPicker(scrollX, scrollY, (right - left) + scrollX, (bottom - top) + scrollY);
                    case 1:
                        return createAccessibilityNodeInfoForVirtualButton(1, getVirtualDecrementExButtonText(), scrollX, scrollY, scrollX + (right - left), SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop + SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight);
                    case 2:
                        return createAccessibilityNodeInfoForVirtualButton(2, getVirtualDecrementButtonText(), scrollX, SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop + scrollY, scrollX + (right - left), SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop + SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight);
                    case 3:
                        return createAccessibiltyNodeInfoForInputText(scrollX, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop + SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight, (right - left) + scrollX, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom - SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight);
                    case 4:
                        return createAccessibilityNodeInfoForVirtualButton(4, getVirtualIncrementButtonText(), scrollX, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom - SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight, scrollX + (right - left), SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom);
                    case 5:
                        return createAccessibilityNodeInfoForVirtualButton(5, getVirtualIncrementExButtonText(), scrollX, SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom - SemNumberPickerSpinnerDelegate.this.mSelectionDividerHeight, scrollX + (right - left), scrollY + (bottom - top));
                }
            }
            AccessibilityNodeInfo createAccessibilityNodeInfo = super.createAccessibilityNodeInfo(i);
            if (createAccessibilityNodeInfo == null) {
                createAccessibilityNodeInfo = AccessibilityNodeInfo.obtain();
            }
            return createAccessibilityNodeInfo;
        }

        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i) {
            if (TextUtils.isEmpty(str)) {
                return Collections.emptyList();
            }
            String toLowerCase = str.toLowerCase();
            List<AccessibilityNodeInfo> arrayList = new ArrayList();
            switch (i) {
                case -1:
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, 1, arrayList);
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, 2, arrayList);
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, 3, arrayList);
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, 4, arrayList);
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, 5, arrayList);
                    return arrayList;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    findAccessibilityNodeInfosByTextInChild(toLowerCase, i, arrayList);
                    return arrayList;
                default:
                    return super.findAccessibilityNodeInfosByText(str, i);
            }
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            if (SemNumberPickerSpinnerDelegate.this.mIsStartingAnimation) {
                return false;
            }
            int right = SemNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SemNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            switch (i) {
                case -1:
                    switch (i2) {
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.requestAccessibilityFocus();
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.clearAccessibilityFocus();
                            return true;
                        case 4096:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || (!SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() && SemNumberPickerSpinnerDelegate.this.getValue() >= SemNumberPickerSpinnerDelegate.this.getMaxValue())) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByOne(true);
                            return true;
                        case 8192:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || (!SemNumberPickerSpinnerDelegate.this.getWrapSelectorWheel() && SemNumberPickerSpinnerDelegate.this.getValue() <= SemNumberPickerSpinnerDelegate.this.getMinValue())) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByOne(false);
                            return true;
                        default:
                            break;
                    }
                case 1:
                    switch (i2) {
                        case 16:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByTwo(false);
                            sendAccessibilityEventForVirtualView(i, 1);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            sendAccessibilityEventForVirtualView(i, 32768);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(i, 65536);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop);
                            return true;
                        default:
                            return false;
                    }
                case 2:
                    switch (i2) {
                        case 16:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByOne(false);
                            sendAccessibilityEventForVirtualView(i, 1);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            sendAccessibilityEventForVirtualView(i, 32768);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop, right, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(i, 65536);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mOverTopSelectionDividerTop, right, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return true;
                        default:
                            return false;
                    }
                case 3:
                    switch (i2) {
                        case 1:
                            return (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || SemNumberPickerSpinnerDelegate.this.mInputText.isFocused()) ? false : SemNumberPickerSpinnerDelegate.this.mInputText.requestFocus();
                        case 2:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled() || !SemNumberPickerSpinnerDelegate.this.mInputText.isFocused()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.mInputText.clearFocus();
                            return true;
                        case 16:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.performClick();
                            return true;
                        case 32:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.performLongClick();
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            sendAccessibilityEventForVirtualView(i, 32768);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop, right, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(i, 65536);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop, right, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom);
                            return true;
                        default:
                            return SemNumberPickerSpinnerDelegate.this.mInputText.performAccessibilityAction(i2, bundle);
                    }
                case 4:
                    switch (i2) {
                        case 16:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByOne(true);
                            sendAccessibilityEventForVirtualView(i, 1);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            sendAccessibilityEventForVirtualView(i, 32768);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(i, 65536);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom);
                            return true;
                        default:
                            return false;
                    }
                case 5:
                    switch (i2) {
                        case 16:
                            if (!SemNumberPickerSpinnerDelegate.this.mDelegator.isEnabled()) {
                                return false;
                            }
                            SemNumberPickerSpinnerDelegate.this.changeValueByTwo(true);
                            sendAccessibilityEventForVirtualView(i, 1);
                            return true;
                        case 64:
                            if (this.mAccessibilityFocusedView == i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = i;
                            sendAccessibilityEventForVirtualView(i, 32768);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom, right, bottom);
                            return true;
                        case 128:
                            if (this.mAccessibilityFocusedView != i) {
                                return false;
                            }
                            this.mAccessibilityFocusedView = Integer.MIN_VALUE;
                            sendAccessibilityEventForVirtualView(i, 65536);
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBelowBottomSelectionDividerBottom, right, bottom);
                            return true;
                        default:
                            return false;
                    }
            }
            return super.performAction(i, i2, bundle);
        }

        public void sendAccessibilityEventForVirtualView(int i, int i2) {
            switch (i) {
                case 1:
                    if (hasVirtualDecrementExButton()) {
                        sendAccessibilityEventForVirtualButton(i, i2, getVirtualDecrementButtonText());
                        return;
                    }
                    return;
                case 2:
                    if (hasVirtualDecrementButton()) {
                        sendAccessibilityEventForVirtualButton(i, i2, getVirtualDecrementButtonText());
                        return;
                    }
                    return;
                case 3:
                    sendAccessibilityEventForVirtualText(i2);
                    return;
                case 4:
                    if (hasVirtualIncrementButton()) {
                        sendAccessibilityEventForVirtualButton(i, i2, getVirtualIncrementButtonText());
                        return;
                    }
                    return;
                case 5:
                    if (hasVirtualIncrementExButton()) {
                        sendAccessibilityEventForVirtualButton(i, i2, getVirtualIncrementButtonText());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    class BeginSoftInputOnLongPressCommand implements Runnable {
        BeginSoftInputOnLongPressCommand() {
        }

        public void run() {
            SemNumberPickerSpinnerDelegate.this.performLongClick();
        }
    }

    class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        ChangeCurrentByOneFromLongPressCommand() {
        }

        private void setStep(boolean z) {
            this.mIncrement = z;
        }

        public void run() {
            SemNumberPickerSpinnerDelegate.this.changeValueByOne(this.mIncrement);
            SemNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, SemNumberPickerSpinnerDelegate.this.mLongPressUpdateInterval);
        }
    }

    class InputTextFilter extends NumberKeyListener {
        InputTextFilter() {
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter;
            if (SemNumberPickerSpinnerDelegate.this.mDisplayedValues == null) {
                filter = super.filter(charSequence, i, i2, spanned, i3, i4);
                if (filter == null) {
                    filter = charSequence.subSequence(i, i2);
                }
                String str = String.valueOf(spanned.subSequence(0, i3)) + filter + spanned.subSequence(i4, spanned.length());
                if ("".equals(str)) {
                    return str;
                }
                if (SemNumberPickerSpinnerDelegate.this.getSelectedPos(str) <= SemNumberPickerSpinnerDelegate.this.mMaxValue && str.length() <= SemNumberPickerSpinnerDelegate.this.formatNumber(SemNumberPickerSpinnerDelegate.this.mMaxValue).length()) {
                    return filter;
                }
                if (SemNumberPickerSpinnerDelegate.this.mIsEditTextMode) {
                    Toast.makeText(SemNumberPickerSpinnerDelegate.this.mContext, SemNumberPickerSpinnerDelegate.this.mToastText, 0).show();
                }
                return "";
            }
            filter = String.valueOf(charSequence.subSequence(i, i2));
            CharSequence toLowerCase = String.valueOf(String.valueOf(spanned.subSequence(0, i3)) + filter + spanned.subSequence(i4, spanned.length())).toLowerCase();
            for (String toLowerCase2 : SemNumberPickerSpinnerDelegate.this.mDisplayedValues) {
                String toLowerCase22 = toLowerCase22.toLowerCase();
                if (SemNumberPickerSpinnerDelegate.this.needCompareEqualMonthLanguage()) {
                    if (toLowerCase22.equals(toLowerCase)) {
                        return filter;
                    }
                } else if (toLowerCase22.startsWith(toLowerCase)) {
                    return filter;
                }
            }
            if (SemNumberPickerSpinnerDelegate.this.mIsEditTextMode && !TextUtils.isEmpty(toLowerCase)) {
                Toast.makeText(SemNumberPickerSpinnerDelegate.this.mContext, SemNumberPickerSpinnerDelegate.this.mToastText, 0).show();
            }
            return "";
        }

        protected char[] getAcceptedChars() {
            return SemNumberPickerSpinnerDelegate.DIGIT_CHARACTERS;
        }

        public int getInputType() {
            return 1;
        }
    }

    class PressedStateHelper implements Runnable {
        public static final int BUTTON_DECREMENT = 2;
        public static final int BUTTON_INCREMENT = 1;
        private final int MODE_PRESS = 1;
        private final int MODE_TAPPED = 2;
        private int mManagedButton;
        private int mMode;

        PressedStateHelper() {
        }

        public void buttonPressDelayed(int i) {
            cancel();
            this.mMode = 1;
            this.mManagedButton = i;
            SemNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getTapTimeout());
        }

        public void buttonTapped(int i) {
            cancel();
            this.mMode = 2;
            this.mManagedButton = i;
            SemNumberPickerSpinnerDelegate.this.mDelegator.post(this);
        }

        public void cancel() {
            int right = SemNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SemNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            this.mMode = 0;
            this.mManagedButton = 0;
            SemNumberPickerSpinnerDelegate.this.mDelegator.removeCallbacks(this);
            if (SemNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed) {
                SemNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed = false;
                SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
            }
            if (SemNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed) {
                SemNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed = false;
                SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
            }
        }

        public void run() {
            int right = SemNumberPickerSpinnerDelegate.this.mDelegator.getRight();
            int bottom = SemNumberPickerSpinnerDelegate.this.mDelegator.getBottom();
            switch (this.mMode) {
                case 1:
                    switch (this.mManagedButton) {
                        case 1:
                            SemNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed = true;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return;
                        case 2:
                            SemNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed = true;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return;
                        default:
                            return;
                    }
                case 2:
                    SemNumberPickerSpinnerDelegate semNumberPickerSpinnerDelegate;
                    switch (this.mManagedButton) {
                        case 1:
                            if (!SemNumberPickerSpinnerDelegate.this.mIncrementVirtualButtonPressed) {
                                SemNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                            }
                            semNumberPickerSpinnerDelegate = SemNumberPickerSpinnerDelegate.this;
                            semNumberPickerSpinnerDelegate.mIncrementVirtualButtonPressed = semNumberPickerSpinnerDelegate.mIncrementVirtualButtonPressed ^ true;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, SemNumberPickerSpinnerDelegate.this.mBottomSelectionDividerBottom, right, bottom);
                            return;
                        case 2:
                            if (!SemNumberPickerSpinnerDelegate.this.mDecrementVirtualButtonPressed) {
                                SemNumberPickerSpinnerDelegate.this.mDelegator.postDelayed(this, (long) ViewConfiguration.getPressedStateDuration());
                            }
                            semNumberPickerSpinnerDelegate = SemNumberPickerSpinnerDelegate.this;
                            semNumberPickerSpinnerDelegate.mDecrementVirtualButtonPressed = semNumberPickerSpinnerDelegate.mDecrementVirtualButtonPressed ^ true;
                            SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate(0, 0, right, SemNumberPickerSpinnerDelegate.this.mTopSelectionDividerTop);
                            return;
                        default:
                            return;
                    }
                default:
                    return;
            }
        }
    }

    class SetSelectionCommand implements Runnable {
        private int mSelectionEnd;
        private int mSelectionStart;

        SetSelectionCommand() {
        }

        public void run() {
            SemNumberPickerSpinnerDelegate.this.mInputText.setSelection(this.mSelectionStart, this.mSelectionEnd);
        }
    }

    public SemNumberPickerSpinnerDelegate(SemNumberPicker semNumberPicker, Context context, AttributeSet attributeSet, int i, int i2) {
        super(semNumberPicker, context);
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(17105716);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(17105717);
        this.mHeightRatio = ((float) this.mContext.getResources().getDimensionPixelSize(17105718)) / ((float) dimensionPixelSize);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SemNumberPicker, i, i2);
        this.mMinHeight = obtainStyledAttributes.getDimensionPixelSize(0, -1);
        this.mMaxHeight = obtainStyledAttributes.getDimensionPixelSize(1, dimensionPixelSize);
        this.mMinWidth = obtainStyledAttributes.getDimensionPixelSize(2, dimensionPixelSize2);
        this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(3, -1);
        obtainStyledAttributes.recycle();
        if (this.mMinHeight != -1 && this.mMaxHeight != -1 && this.mMinHeight > this.mMaxHeight) {
            throw new IllegalArgumentException("minHeight > maxHeight");
        } else if (this.mMinWidth == -1 || this.mMaxWidth == -1 || this.mMinWidth <= this.mMaxWidth) {
            this.mHideWheelUntilFocused = false;
            this.mSelectionDividerHeight = (int) TypedValue.applyDimension(1, SprDocument.DEFAULT_DENSITY_SCALE, resources.getDisplayMetrics());
            this.mComputeMaxWidth = this.mMaxWidth == -1;
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16843828, typedValue, true);
            if (typedValue.resourceId != 0) {
                this.mSelectedPickerColor = (resources.getColor(typedValue.resourceId) & 16777215) | 855638016;
            } else {
                this.mSelectedPickerColor = (typedValue.data & 16777215) | 855638016;
            }
            this.mVirtualButtonFocusedDrawable = new ColorDrawable(this.mSelectedPickerColor);
            this.mSubTextColor = resources.getColor(17170813);
            this.mPressedStateHelper = new PressedStateHelper();
            this.mDelegator.setWillNotDraw(false);
            ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(17367297, this.mDelegator, true);
            this.mInputText = (EditText) this.mDelegator.findViewById(16909401);
            this.mInputText.setLongClickable(false);
            this.mInputText.setIncludeFontPadding(false);
            this.mDefaultTypeface = Typeface.defaultFromStyle(0);
            this.mPickerTypeface = Typeface.create("sec-roboto-condensed-light", 0);
            if (this.mDefaultTypeface.equals(this.mPickerTypeface)) {
                this.mPickerTypeface = Typeface.create("samsung-neo-num3T", 0);
            }
            String string = System.getString(this.mContext.getContentResolver(), "theme_font_clock");
            if (!(string == null || string.equals(""))) {
                this.mPickerTypeface = getFontTypeface(string);
            }
            if (isMeaLanguage()) {
                this.mInputText.setIncludeFontPadding(true);
                this.mPickerTypeface = this.mDefaultTypeface;
            }
            this.mPickerBoldTypeface = Typeface.create(this.mPickerTypeface, 1);
            this.mInputText.setTypeface(this.mPickerBoldTypeface);
            this.mTextColor = this.mInputText.getTextColors().getColorForState(this.mDelegator.getEnableStateSet(), -1);
            this.mInputText.setOnFocusChangeListener(new C02991());
            this.mInputText.setOnTouchListener(new C03002());
            this.mInputText.setFilters(new InputFilter[]{new InputTextFilter()});
            this.mInputText.setRawInputType(2);
            this.mInputText.setImeOptions(33554438);
            this.mInputText.setCursorVisible(false);
            this.mInputText.setHighlightColor(this.mSelectedPickerColor);
            this.mInputText.semSetHoverPopupType(0);
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
            this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 2;
            this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity() / 4;
            this.mTextSize = (int) this.mInputText.getTextSize();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize((float) this.mTextSize);
            paint.setTypeface(this.mPickerTypeface);
            paint.setColor(this.mSubTextColor);
            this.mSelectorWheelPaint = paint;
            this.mSubTextSize = resources.getDimensionPixelSize(17105724);
            this.mDefaultEdgeHeight = Math.round(((float) this.mSubTextSize) * 0.48f);
            this.mPathInterpolator = new PathInterpolator(0.5f, 0.0f, 0.4f, 1.0f);
            this.mCustomScroller = new Scroller(this.mContext, this.mPathInterpolator, true);
            this.mLinearScroller = new Scroller(this.mContext, null, true);
            this.mFlingScroller = this.mLinearScroller;
            this.mAdjustScroller = new Scroller(this.mContext, new PathInterpolator(0.4f, 0.0f, 0.3f, 1.0f));
            setFormatter(SemNumberPicker.getTwoDigitFormatter());
            updateInputTextView();
            if (this.mDelegator.getImportantForAccessibility() == 0) {
                this.mDelegator.setImportantForAccessibility(1);
            }
            this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
            this.mDelegator.setFocusableInTouchMode(false);
            this.mDelegator.setDescendantFocusability(131072);
            this.mPickerContentDescription = "";
            this.mToastText = resources.getString(17041660);
            this.mInputText.semSetDirectPenInputEnabled(false);
        } else {
            throw new IllegalArgumentException("minWidth > maxWidth");
        }
    }

    private void changeValueByOne(boolean z) {
        this.mInputText.setVisibility(4);
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        int i = 500;
        this.mChangeValueBy = 1;
        if (this.mLongPressed_FIRST_SCROLL) {
            this.mLongPressed_FIRST_SCROLL = false;
            this.mLongPressed_SECOND_SCROLL = true;
        } else if (this.mLongPressed_SECOND_SCROLL) {
            this.mLongPressed_SECOND_SCROLL = false;
            this.mLongPressed_THIRD_SCROLL = true;
            if (getValue() % 10 == 0) {
                this.mChangeValueBy = 10;
            } else if (z) {
                this.mChangeValueBy = 10 - (getValue() % 10);
            } else {
                this.mChangeValueBy = getValue() % 10;
            }
        } else if (this.mLongPressed_THIRD_SCROLL) {
            this.mChangeValueBy = 10;
        }
        if (this.mIsLongPressed && this.mSkipNumbers) {
            i = 200;
            this.mLongPressUpdateInterval = 600;
        } else if (this.mIsLongPressed) {
            i = 100;
            this.mChangeValueBy = 1;
            this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        }
        this.mLongPressCount = this.mChangeValueBy - 1;
        if (z) {
            this.mFlingScroller.startScroll(0, 0, 0, (-this.mSelectorElementHeight) * this.mChangeValueBy, i);
        } else {
            this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight * this.mChangeValueBy, i);
        }
        this.mDelegator.invalidate();
    }

    private void changeValueByTwo(boolean z) {
        this.mInputText.setVisibility(4);
        if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        this.mPreviousScrollerY = 0;
        if (z) {
            this.mFlingScroller.startScroll(0, 0, 0, (-this.mSelectorElementHeight) * 2, 500);
        } else {
            this.mFlingScroller.startScroll(0, 0, 0, this.mSelectorElementHeight * 2, 500);
        }
        this.mDelegator.invalidate();
    }

    private void decrementSelectorIndices(int[] iArr) {
        for (int length = iArr.length - 1; length > 0; length--) {
            iArr[length] = iArr[length - 1];
        }
        int i = iArr[1] - 1;
        if (this.mWrapSelectorWheel && i < this.mMinValue) {
            i = this.mMaxValue;
        }
        iArr[0] = i;
        ensureCachedScrollSelectorValue(i);
    }

    private void ensureCachedScrollSelectorValue(int i) {
        SparseArray sparseArray = this.mSelectorIndexToStringCache;
        if (((String) sparseArray.get(i)) == null) {
            Object obj;
            if (i < this.mMinValue || i > this.mMaxValue) {
                obj = "";
            } else if (this.mDisplayedValues != null) {
                obj = this.mDisplayedValues[i - this.mMinValue];
            } else {
                obj = formatNumber(i);
            }
            sparseArray.put(i, obj);
        }
    }

    private boolean ensureScrollWheelAdjusted() {
        if (this.mInitialScrollOffset == FingerprintManager.PRIVILEGED_TYPE_KEYGUARD) {
            return false;
        }
        int i = this.mInitialScrollOffset - this.mCurrentScrollOffset;
        if (i == 0) {
            return false;
        }
        this.mPreviousScrollerY = 0;
        if (Math.abs(i) > this.mSelectorElementHeight / 2) {
            i += i > 0 ? -this.mSelectorElementHeight : this.mSelectorElementHeight;
        }
        this.mAdjustScroller.startScroll(0, 0, 0, i, SELECTOR_ADJUSTMENT_DURATION_MILLIS);
        this.mDelegator.invalidate();
        return true;
    }

    private void fling(int i) {
        this.mPreviousScrollerY = 0;
        this.mFlingScroller.fling(0, this.mCurrentScrollOffset, 0, i, 0, 0, FingerprintManager.PRIVILEGED_TYPE_KEYGUARD, Integer.MAX_VALUE);
        int round = (Math.round(((float) this.mFlingScroller.getFinalY()) / ((float) this.mSelectorElementHeight)) * this.mSelectorElementHeight) + this.mInitialScrollOffset;
        this.mFlingScroller.setFinalY(i > 0 ? Math.max(round, this.mSelectorElementHeight + this.mInitialScrollOffset) : Math.min(round, (-this.mSelectorElementHeight) + this.mInitialScrollOffset));
        this.mDelegator.invalidate();
    }

    private String formatNumber(int i) {
        return this.mFormatter != null ? this.mFormatter.format(i) : formatNumberWithLocale(i);
    }

    private static String formatNumberWithLocale(int i) {
        return String.format(Locale.getDefault(), "%d", new Object[]{Integer.valueOf(i)});
    }

    private static Typeface getFontTypeface(String str) {
        if (!new File(str).exists()) {
            return null;
        }
        try {
            return Typeface.createFromFile(str);
        } catch (Exception e) {
            return null;
        }
    }

    private int getSelectedPos(String str) {
        if (this.mDisplayedValues == null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return this.mMinValue;
            }
        }
        for (int i = 0; i < this.mDisplayedValues.length; i++) {
            str = str.toLowerCase();
            if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                return this.mMinValue + i;
            }
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            return this.mMinValue;
        }
    }

    private int getWrappedSelectorIndex(int i) {
        return i > this.mMaxValue ? (this.mMinValue + ((i - this.mMaxValue) % (this.mMaxValue - this.mMinValue))) - 1 : i < this.mMinValue ? (this.mMaxValue - ((this.mMinValue - i) % (this.mMaxValue - this.mMinValue))) + 1 : i;
    }

    private void hideSoftInput() {
        InputMethodManager peekInstance = InputMethodManager.peekInstance();
        if (peekInstance != null && peekInstance.isActive(this.mInputText)) {
            peekInstance.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            this.mInputText.setVisibility(4);
        }
    }

    private void incrementSelectorIndices(int[] iArr) {
        for (int i = 0; i < iArr.length - 1; i++) {
            iArr[i] = iArr[i + 1];
        }
        int i2 = iArr[iArr.length - 2] + 1;
        if (this.mWrapSelectorWheel && i2 > this.mMaxValue) {
            i2 = this.mMinValue;
        }
        iArr[iArr.length - 1] = i2;
        ensureCachedScrollSelectorValue(i2);
    }

    private void initializeSelectorWheel() {
        if (this.mIsStartingAnimation) {
            if (!moveToFinalScrollerPosition(this.mFlingScroller)) {
                moveToFinalScrollerPosition(this.mAdjustScroller);
            }
            stopScrollAnimation();
        }
        if (!this.mIsStartingAnimation) {
            initializeSelectorWheelIndices();
        }
        this.mSelectorElementHeight = Math.round((float) ((this.mDelegator.getHeight() - this.mDefaultEdgeHeight) / 4));
        this.mSelectorTextGapHeight = (int) TypedValue.applyDimension(1, 13.0f, this.mContext.getResources().getDisplayMetrics());
        if (this.mIsAmPm) {
            this.mSelectorTextGapHeight = 4;
        }
        this.mInitialScrollOffset = (this.mInputText.getTop() + (this.mModifiedTxtHeight / 2)) - (this.mSelectorElementHeight * 2);
        this.mCurrentScrollOffset = this.mInitialScrollOffset;
        ((CustomEditText) this.mInputText).setEditTextPosition(((int) (((this.mSelectorWheelPaint.descent() - this.mSelectorWheelPaint.ascent()) / SprDocument.DEFAULT_DENSITY_SCALE) - this.mSelectorWheelPaint.descent())) - (this.mInputText.getBaseline() - (this.mModifiedTxtHeight / 2)));
        if (this.mReservedStartAnimation) {
            startAnimation(0, this.mAnimationListener);
            this.mReservedStartAnimation = false;
        }
    }

    private void initializeSelectorWheelIndices() {
        this.mSelectorIndexToStringCache.clear();
        int[] iArr = this.mSelectorIndices;
        int value = getValue();
        for (int i = 0; i < this.mSelectorIndices.length; i++) {
            int i2 = value + (i - 2);
            if (this.mWrapSelectorWheel) {
                i2 = getWrappedSelectorIndex(i2);
            }
            iArr[i] = i2;
            ensureCachedScrollSelectorValue(iArr[i]);
        }
    }

    private boolean isMeaLanguage() {
        String language = Locale.getDefault().getLanguage();
        return "ar".equals(language) || "fa".equals(language);
    }

    private int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        switch (mode) {
            case FingerprintManager.PRIVILEGED_TYPE_KEYGUARD /*-2147483648*/:
                return MeasureSpec.makeMeasureSpec(Math.min(size, i2), 1073741824);
            case 0:
                return MeasureSpec.makeMeasureSpec(i2, 1073741824);
            case 1073741824:
                return i;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
    }

    private boolean moveToFinalScrollerPosition(Scroller scroller) {
        scroller.forceFinished(true);
        int finalY = scroller.getFinalY() - scroller.getCurrY();
        if (this.mSelectorElementHeight == 0) {
            return false;
        }
        int i = this.mInitialScrollOffset - (this.mCurrentScrollOffset + finalY);
        if (i == 0) {
            return false;
        }
        i %= this.mSelectorElementHeight;
        if (Math.abs(i) > this.mSelectorElementHeight / 2) {
            i = i > 0 ? i - this.mSelectorElementHeight : i + this.mSelectorElementHeight;
        }
        scrollBy(0, finalY + i);
        return true;
    }

    private boolean needCompareEqualMonthLanguage() {
        return "vi".equals(Locale.getDefault().getLanguage());
    }

    private void notifyChange(int i, int i2) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled() && !this.mIsStartingAnimation) {
            int wrappedSelectorIndex = getWrappedSelectorIndex(this.mValue);
            CharSequence charSequence = null;
            if (wrappedSelectorIndex <= this.mMaxValue) {
                charSequence = this.mDisplayedValues == null ? formatNumber(wrappedSelectorIndex) : this.mDisplayedValues[wrappedSelectorIndex - this.mMinValue];
            }
            this.mDelegator.announceForAccessibility(charSequence);
            if (this.mIsAmPm) {
                AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                if (accessibilityNodeProviderImpl != null) {
                    accessibilityNodeProviderImpl.performAction(3, 64, null);
                }
            }
        }
        if (this.mOnValueChangeListener != null) {
            this.mOnValueChangeListener.onValueChange(this.mDelegator, i, this.mValue);
        }
    }

    private void onScrollStateChange(int i) {
        if (this.mScrollState != i) {
            this.mScrollState = i;
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScrollStateChange(this.mDelegator, i);
            }
        }
    }

    private void onScrollerFinished(Scroller scroller) {
        if (scroller == this.mFlingScroller) {
            if (!ensureScrollWheelAdjusted()) {
                updateInputTextView();
            }
            onScrollStateChange(0);
        } else if (this.mScrollState != 1) {
            updateInputTextView();
        }
    }

    private void playSoundAndHapticFeedback() {
        this.mAudioManager.playSoundEffect(105);
    }

    private void postBeginSoftInputOnLongPressCommand() {
        if (this.mBeginSoftInputOnLongPressCommand == null) {
            this.mBeginSoftInputOnLongPressCommand = new BeginSoftInputOnLongPressCommand();
        } else {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
        this.mDelegator.postDelayed(this.mBeginSoftInputOnLongPressCommand, (long) ViewConfiguration.getLongPressTimeout());
    }

    private void postChangeCurrentByOneFromLongPress(boolean z, long j) {
        if (this.mChangeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        this.mIsLongPressed = true;
        this.mLongPressed_FIRST_SCROLL = true;
        this.mChangeCurrentByOneFromLongPressCommand.setStep(z);
        this.mDelegator.postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j);
    }

    private void removeAllCallbacks() {
        if (this.mIsLongPressed) {
            this.mIsLongPressed = false;
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
        this.mLongPressed_FIRST_SCROLL = false;
        this.mLongPressed_SECOND_SCROLL = false;
        this.mLongPressed_THIRD_SCROLL = false;
        this.mChangeValueBy = 1;
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        if (this.mSetSelectionCommand != null) {
            this.mDelegator.removeCallbacks(this.mSetSelectionCommand);
        }
        if (this.mBeginSoftInputOnLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
        this.mPressedStateHelper.cancel();
    }

    private void removeBeginSoftInputCommand() {
        if (this.mBeginSoftInputOnLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mBeginSoftInputOnLongPressCommand);
        }
    }

    private void removeChangeCurrentByOneFromLongPress() {
        if (this.mIsLongPressed) {
            this.mIsLongPressed = false;
            this.mCurrentScrollOffset = this.mInitialScrollOffset;
        }
        this.mLongPressed_FIRST_SCROLL = false;
        this.mLongPressed_SECOND_SCROLL = false;
        this.mLongPressed_THIRD_SCROLL = false;
        this.mChangeValueBy = 1;
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            this.mDelegator.removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
    }

    private int resolveSizeAndStateRespectingMinSize(int i, int i2, int i3) {
        return i != -1 ? View.resolveSizeAndState(Math.max(i, i2), i3, 0) : i2;
    }

    private void setValueInternal(int i, boolean z) {
        if (this.mValue == i) {
            if (isMeaLanguage()) {
                updateInputTextView();
                this.mDelegator.invalidate();
            }
            return;
        }
        i = this.mWrapSelectorWheel ? getWrappedSelectorIndex(i) : Math.min(Math.max(i, this.mMinValue), this.mMaxValue);
        int i2 = this.mValue;
        this.mValue = i;
        updateInputTextView();
        if (z) {
            notifyChange(i2, i);
        }
        initializeSelectorWheelIndices();
        this.mDelegator.invalidate();
    }

    private void showSoftInput() {
        InputMethodManager peekInstance = InputMethodManager.peekInstance();
        if (peekInstance != null) {
            this.mInputText.setVisibility(0);
            this.mInputText.requestFocus();
            peekInstance.showSoftInput(this.mInputText, 0);
        }
    }

    private void showSoftInputForWindowFocused() {
        this.mDelegator.postDelayed(new C03023(), 20);
    }

    private void stopScrollAnimation() {
        this.mFlingScroller.abortAnimation();
        this.mAdjustScroller.abortAnimation();
        if (!(this.mIsStartingAnimation || moveToFinalScrollerPosition(this.mFlingScroller))) {
            moveToFinalScrollerPosition(this.mAdjustScroller);
        }
        ensureScrollWheelAdjusted();
    }

    private void tryComputeMaxWidth() {
        if (this.mComputeMaxWidth) {
            int i = 0;
            int i2 = 0;
            this.mSelectorWheelPaint.setTypeface(this.mPickerBoldTypeface);
            int i3;
            if (this.mDisplayedValues == null) {
                float f = 0.0f;
                for (i3 = 0; i3 <= 9; i3++) {
                    float measureText = this.mSelectorWheelPaint.measureText(formatNumberWithLocale(i3));
                    if (measureText > f) {
                        f = measureText;
                    }
                }
                int i4 = 0;
                for (int i5 = this.mMaxValue; i5 > 0; i5 /= 10) {
                    i4++;
                }
                i = (int) (((float) i4) * f);
                i2 = i4;
            } else {
                int length = this.mDisplayedValues.length;
                for (i3 = 0; i3 < length; i3++) {
                    float measureText2 = this.mSelectorWheelPaint.measureText(this.mDisplayedValues[i3]);
                    if (measureText2 > ((float) i)) {
                        i = (int) measureText2;
                        i2 = this.mDisplayedValues[i3].length();
                    }
                }
            }
            i += this.mInputText.getPaddingLeft() + this.mInputText.getPaddingRight();
            if (this.mInputText.isHighContrastTextEnabled()) {
                i += ((int) Math.ceil((double) (this.mSelectorWheelPaint.getHCTStrokeWidth() / SprDocument.DEFAULT_DENSITY_SCALE))) * (i2 + 2);
            }
            if (this.mMaxWidth != i) {
                if (i > this.mMinWidth) {
                    this.mMaxWidth = i;
                } else {
                    this.mMaxWidth = this.mMinWidth;
                }
                this.mDelegator.invalidate();
            }
            this.mSelectorWheelPaint.setTypeface(this.mPickerTypeface);
        }
    }

    private boolean updateInputTextView() {
        CharSequence formatNumber = this.mDisplayedValues == null ? formatNumber(this.mValue) : this.mDisplayedValues[this.mValue - this.mMinValue];
        if (TextUtils.isEmpty(formatNumber) || formatNumber.equals(this.mInputText.getText().toString())) {
            return false;
        }
        this.mInputText.setText(formatNumber);
        Selection.setSelection(this.mInputText.getText(), this.mInputText.getText().length());
        return true;
    }

    private void validateInputTextView(View view) {
        CharSequence valueOf = String.valueOf(((TextView) view).getText());
        int selectedPos = getSelectedPos(valueOf.toString());
        if (TextUtils.isEmpty(valueOf) || this.mValue == selectedPos) {
            updateInputTextView();
        } else {
            setValueInternal(selectedPos, true);
        }
    }

    public void computeScroll() {
        Scroller scroller = this.mFlingScroller;
        if (scroller.isFinished()) {
            scroller = this.mAdjustScroller;
            if (scroller.isFinished()) {
                return;
            }
        }
        scroller.computeScrollOffset();
        int currY = scroller.getCurrY();
        if (this.mPreviousScrollerY == 0) {
            this.mPreviousScrollerY = scroller.getStartY();
        }
        scrollBy(0, currY - this.mPreviousScrollerY);
        this.mPreviousScrollerY = currY;
        if (scroller.isFinished()) {
            onScrollerFinished(scroller);
        } else {
            this.mDelegator.invalidate();
        }
    }

    public int computeVerticalScrollExtent() {
        return this.mDelegator.getHeight();
    }

    public int computeVerticalScrollOffset() {
        return this.mCurrentScrollOffset;
    }

    public int computeVerticalScrollRange() {
        return ((this.mMaxValue - this.mMinValue) + 1) * this.mSelectorElementHeight;
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            int y = (int) motionEvent.getY();
            int i = (!this.mIsEditTextMode || this.mIsAmPm) ? (this.mOverTopSelectionDividerTop > y || y >= this.mTopSelectionDividerTop) ? y < this.mOverTopSelectionDividerTop ? 1 : (this.mBottomSelectionDividerBottom >= y || y > this.mBelowBottomSelectionDividerBottom) ? y > this.mBelowBottomSelectionDividerBottom ? 5 : 3 : 4 : 2 : 3;
            AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
            switch (motionEvent.getActionMasked()) {
                case 7:
                    if (!(this.mLastHoveredChildVirtualViewId == i || this.mLastHoveredChildVirtualViewId == -1)) {
                        accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(this.mLastHoveredChildVirtualViewId, 256);
                        accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i, 128);
                        this.mLastHoveredChildVirtualViewId = i;
                        accessibilityNodeProviderImpl.performAction(i, 64, null);
                        break;
                    }
                case 9:
                    accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i, 128);
                    this.mLastHoveredChildVirtualViewId = i;
                    accessibilityNodeProviderImpl.performAction(i, 64, null);
                    break;
                case 10:
                    accessibilityNodeProviderImpl.sendAccessibilityEventForVirtualView(i, 256);
                    this.mLastHoveredChildVirtualViewId = -1;
                    break;
            }
        }
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case 19:
            case 20:
                if (this.mIsEditTextMode && !this.mIsAmPm) {
                    return false;
                }
                if (keyEvent.getAction() == 0) {
                    if (keyCode == 20) {
                        if (this.mLastFocusedChildVirtualViewId == 2) {
                            this.mLastFocusedChildVirtualViewId = 3;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 3) {
                            if (!this.mWrapSelectorWheel && getValue() == getMaxValue()) {
                                return false;
                            }
                            this.mLastFocusedChildVirtualViewId = 4;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 4) {
                            if (!this.mWrapSelectorWheel && getValue() == getMaxValue() - 1) {
                                return false;
                            }
                            this.mLastFocusedChildVirtualViewId = 5;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 1) {
                            this.mLastFocusedChildVirtualViewId = 2;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 5) {
                            return false;
                        }
                    } else if (keyCode == 19) {
                        if (this.mLastFocusedChildVirtualViewId == 2) {
                            if (!this.mWrapSelectorWheel && getValue() == getMinValue() + 1) {
                                return false;
                            }
                            this.mLastFocusedChildVirtualViewId = 1;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 3) {
                            if (!this.mWrapSelectorWheel && getValue() == getMinValue()) {
                                return false;
                            }
                            this.mLastFocusedChildVirtualViewId = 2;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId == 4) {
                            this.mLastFocusedChildVirtualViewId = 3;
                            this.mDelegator.invalidate();
                            return true;
                        } else if (this.mLastFocusedChildVirtualViewId != 1 && this.mLastFocusedChildVirtualViewId == 5) {
                            this.mLastFocusedChildVirtualViewId = 4;
                            this.mDelegator.invalidate();
                            return true;
                        }
                    }
                } else if (keyEvent.getAction() == 1 && AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                    AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                    if (accessibilityNodeProviderImpl != null) {
                        accessibilityNodeProviderImpl.performAction(this.mLastFocusedChildVirtualViewId, 64, null);
                    }
                    return true;
                }
                break;
            case 21:
            case 22:
                if (keyEvent.getAction() == 0) {
                    View focusSearch;
                    if (keyCode == 22) {
                        focusSearch = this.mDelegator.focusSearch(66);
                        if (focusSearch != null) {
                            focusSearch.requestFocus(66);
                        }
                        return true;
                    } else if (keyCode == 21) {
                        focusSearch = this.mDelegator.focusSearch(17);
                        if (focusSearch != null) {
                            focusSearch.requestFocus(17);
                        }
                        return true;
                    }
                }
                break;
            case 23:
            case SemMotionRecognitionEvent.BLOW /*66*/:
                if ((!this.mIsEditTextMode || this.mIsAmPm) && keyEvent.getAction() == 0) {
                    if (this.mLastFocusedChildVirtualViewId != 3) {
                        if (this.mLastFocusedChildVirtualViewId != 4) {
                            if (this.mLastFocusedChildVirtualViewId != 2) {
                                if (this.mLastFocusedChildVirtualViewId != 5) {
                                    if (this.mLastFocusedChildVirtualViewId == 1 && this.mFlingScroller.isFinished()) {
                                        changeValueByTwo(false);
                                        if (this.mWrapSelectorWheel || getValue() != getMinValue() + 3) {
                                            if (!this.mWrapSelectorWheel && getValue() == getMinValue() + 2) {
                                                this.mLastFocusedChildVirtualViewId = 3;
                                                break;
                                            }
                                        }
                                        this.mLastFocusedChildVirtualViewId = 2;
                                        break;
                                    }
                                } else if (this.mFlingScroller.isFinished()) {
                                    changeValueByTwo(true);
                                    if (this.mWrapSelectorWheel || getValue() != getMaxValue() - 3) {
                                        if (!this.mWrapSelectorWheel && getValue() == getMaxValue() - 2) {
                                            this.mLastFocusedChildVirtualViewId = 3;
                                            break;
                                        }
                                    }
                                    this.mLastFocusedChildVirtualViewId = 4;
                                    break;
                                }
                            } else if (this.mFlingScroller.isFinished()) {
                                changeValueByOne(false);
                                if (!this.mWrapSelectorWheel && getValue() == getMinValue() + 1) {
                                    this.mLastFocusedChildVirtualViewId = 3;
                                    break;
                                }
                            }
                        } else if (this.mFlingScroller.isFinished()) {
                            changeValueByOne(true);
                            if (!this.mWrapSelectorWheel && getValue() == getMaxValue() - 1) {
                                this.mLastFocusedChildVirtualViewId = 3;
                                break;
                            }
                        }
                    } else if (!this.mIsAmPm) {
                        this.mInputText.setVisibility(0);
                        this.mInputText.requestFocus();
                        showSoftInput();
                        removeAllCallbacks();
                        break;
                    } else {
                        return false;
                    }
                }
                break;
        }
        return false;
    }

    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        if ((!this.mInputText.hasFocus() && (!this.mIsAmPm || !this.mDelegator.hasFocus())) || keyEvent.getKeyCode() != 4 || keyEvent.getAction() != 0) {
            return false;
        }
        hideSoftInput();
        setEditTextMode(false);
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 1:
            case 3:
                removeAllCallbacks();
                break;
        }
        return false;
    }

    public void dispatchTrackballEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 1:
            case 3:
                removeAllCallbacks();
                return;
            default:
                return;
        }
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if (this.mAccessibilityNodeProvider == null) {
            this.mAccessibilityNodeProvider = new AccessibilityNodeProviderImpl();
        }
        return this.mAccessibilityNodeProvider;
    }

    public String[] getDisplayedValues() {
        return this.mDisplayedValues;
    }

    public EditText getEditText() {
        return this.mInputText;
    }

    public int getMaxHeight() {
        return 0;
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public int getMaxWidth() {
        return 0;
    }

    public int getMinHeight() {
        return 0;
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public int getMinWidth() {
        return 0;
    }

    public boolean getToggle() {
        return this.mIsAmPm;
    }

    public int getValue() {
        return this.mValue;
    }

    public boolean getWrapSelectorWheel() {
        return this.mWrapSelectorWheel;
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public boolean isEditTextModeNotAmPm() {
        return this.mIsEditTextMode && !this.mIsAmPm;
    }

    public void onAttachedToWindow() {
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mCustomTypefaceSet) {
            if (isMeaLanguage()) {
                this.mInputText.setIncludeFontPadding(true);
                this.mPickerTypeface = this.mDefaultTypeface;
                this.mPickerBoldTypeface = Typeface.create(this.mDefaultTypeface, 1);
                this.mInputText.setTypeface(this.mPickerBoldTypeface);
                return;
            }
            this.mInputText.setIncludeFontPadding(false);
            this.mInputText.setTypeface(this.mPickerBoldTypeface);
            tryComputeMaxWidth();
        }
    }

    public void onDetachedFromWindow() {
        removeAllCallbacks();
    }

    public void onDraw(Canvas canvas) {
        int right = this.mDelegator.getRight();
        int left = this.mDelegator.getLeft();
        int bottom = this.mDelegator.getBottom();
        boolean hasFocus = this.mHideWheelUntilFocused ? this.mDelegator.hasFocus() : true;
        int i = this.mSelectorElementHeight * 2;
        float f = ((float) (right - left)) / SprDocument.DEFAULT_DENSITY_SCALE;
        float f2 = (float) this.mCurrentScrollOffset;
        float f3 = (float) (this.mInitialScrollOffset + i);
        if (hasFocus && this.mVirtualButtonFocusedDrawable != null && this.mScrollState == 0) {
            if (this.mLastFocusedChildVirtualViewId == 2) {
                this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                this.mVirtualButtonFocusedDrawable.setBounds(0, this.mOverTopSelectionDividerTop, right, this.mTopSelectionDividerTop);
                this.mVirtualButtonFocusedDrawable.draw(canvas);
            }
            if (this.mLastFocusedChildVirtualViewId == 4) {
                this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                this.mVirtualButtonFocusedDrawable.setBounds(0, this.mBottomSelectionDividerBottom, right, this.mBelowBottomSelectionDividerBottom);
                this.mVirtualButtonFocusedDrawable.draw(canvas);
            }
            if (this.mLastFocusedChildVirtualViewId == 1) {
                this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                this.mVirtualButtonFocusedDrawable.setBounds(0, 0, right, this.mOverTopSelectionDividerTop);
                this.mVirtualButtonFocusedDrawable.draw(canvas);
            }
            if (this.mLastFocusedChildVirtualViewId == 5) {
                this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                this.mVirtualButtonFocusedDrawable.setBounds(0, this.mBelowBottomSelectionDividerBottom, right, bottom);
                this.mVirtualButtonFocusedDrawable.draw(canvas);
            }
            if (this.mLastFocusedChildVirtualViewId == 3) {
                this.mVirtualButtonFocusedDrawable.setState(this.mDelegator.getDrawableState());
                this.mVirtualButtonFocusedDrawable.setBounds(0, this.mTopSelectionDividerTop, right, this.mBottomSelectionDividerBottom);
                this.mVirtualButtonFocusedDrawable.draw(canvas);
            }
        }
        int[] iArr = this.mSelectorIndices;
        int i2 = 0;
        while (i2 < iArr.length) {
            String str = (String) this.mSelectorIndexToStringCache.get(iArr[i2]);
            if (f2 < ((float) (this.mTopSelectionDividerTop + (this.mInitialScrollOffset * 2))) || f2 > ((float) (this.mBottomSelectionDividerBottom - (this.mInitialScrollOffset * 2)))) {
                if (!(this.mIsAmPm || this.mSelectorWheelPaint.getTypeface() == this.mPickerTypeface)) {
                    this.mSelectorWheelPaint.setTypeface(this.mPickerTypeface);
                }
                if (((this.mSelectorWheelPaint.getColor() ^ this.mSubTextColor) << 8) != 0) {
                    this.mSelectorWheelPaint.setColor(this.mSubTextColor);
                }
            } else {
                if (!(this.mIsAmPm || this.mSelectorWheelPaint.getTypeface() == this.mPickerBoldTypeface)) {
                    this.mSelectorWheelPaint.setTypeface(this.mPickerBoldTypeface);
                }
                if (((this.mSelectorWheelPaint.getColor() ^ this.mTextColor) << 8) != 0) {
                    this.mSelectorWheelPaint.setColor(this.mTextColor);
                }
            }
            float abs = ((f3 - Math.abs(f3 - f2)) - ((float) this.mInitialScrollOffset)) / ((float) i);
            float interpolation = this.mPathInterpolator.getInterpolation(abs);
            float interpolation2 = (0.8f * this.mAlphaPathInterpolator.getInterpolation(abs)) + 0.2f;
            if (interpolation2 < 0.0f) {
                interpolation2 = 0.0f;
            }
            this.mSelectorWheelPaint.setAlpha((int) (255.0f * interpolation2));
            float f4 = ((((float) (this.mTextSize - this.mSubTextSize)) * interpolation) + ((float) this.mSubTextSize)) / ((float) this.mTextSize);
            int descent = (int) ((((this.mSelectorWheelPaint.descent() - this.mSelectorWheelPaint.ascent()) / SprDocument.DEFAULT_DENSITY_SCALE) + f2) - this.mSelectorWheelPaint.descent());
            if (hasFocus && i2 != 2 && (!this.mIsEditTextMode || this.mIsAmPm)) {
                canvas.save();
                canvas.scale(f4, f4, f, f2);
                canvas.drawText(str, f, (float) descent, this.mSelectorWheelPaint);
                canvas.restore();
            } else if (i2 == 2 && this.mInputText.getVisibility() != 0) {
                canvas.save();
                canvas.scale(f4, f4, f, f2);
                canvas.drawText(str, f, (float) descent, this.mSelectorWheelPaint);
                canvas.restore();
            }
            f2 += (float) this.mSelectorElementHeight;
            i2++;
        }
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        if (z) {
            if (!this.mIsEditTextMode || this.mIsAmPm) {
                this.mLastFocusedChildVirtualViewId = 1;
                if (!this.mWrapSelectorWheel && getValue() - getMinValue() < 2) {
                    if (getValue() == getMinValue()) {
                        this.mLastFocusedChildVirtualViewId = 3;
                    } else {
                        this.mLastFocusedChildVirtualViewId = 2;
                    }
                }
            } else {
                this.mLastFocusedChildVirtualViewId = -1;
                if (this.mInputText.getVisibility() == 0) {
                    this.mInputText.requestFocus();
                }
            }
            if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                if (accessibilityNodeProviderImpl != null) {
                    if (this.mIsEditTextMode && !this.mIsAmPm) {
                        this.mLastFocusedChildVirtualViewId = 3;
                    }
                    accessibilityNodeProviderImpl.performAction(this.mLastFocusedChildVirtualViewId, 64, null);
                }
            }
        } else {
            this.mLastFocusedChildVirtualViewId = -1;
            this.mLastHoveredChildVirtualViewId = FingerprintManager.PRIVILEGED_TYPE_KEYGUARD;
        }
        this.mDelegator.invalidate();
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) != 0) {
            switch (motionEvent.getAction()) {
                case 8:
                    float axisValue = motionEvent.getAxisValue(9);
                    if (axisValue != 0.0f) {
                        if (axisValue > 0.0f) {
                            changeValueByOne(false);
                        } else {
                            changeValueByOne(true);
                        }
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.setClassName(NumberPicker.class.getName());
        accessibilityEvent.setScrollable(true);
        accessibilityEvent.setScrollY((this.mMinValue + this.mValue) * this.mSelectorElementHeight);
        accessibilityEvent.setMaxScrollY((this.mMaxValue - this.mMinValue) * this.mSelectorElementHeight);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mDelegator.isEnabled() || ((this.mIsEditTextMode && !this.mIsAmPm) || this.mIsStartingAnimation)) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                removeAllCallbacks();
                this.mInputText.setVisibility(4);
                float y = motionEvent.getY();
                this.mLastDownEventY = y;
                this.mLastDownOrMoveEventY = y;
                this.mLastDownEventTime = motionEvent.getEventTime();
                this.mIgnoreMoveEvents = false;
                this.mPerformClickOnTap = false;
                if (this.mLastDownEventY < ((float) this.mTopSelectionDividerTop)) {
                    if (this.mScrollState == 0) {
                        this.mPressedStateHelper.buttonPressDelayed(2);
                    }
                } else if (this.mLastDownEventY > ((float) this.mBottomSelectionDividerBottom) && this.mScrollState == 0) {
                    this.mPressedStateHelper.buttonPressDelayed(1);
                }
                this.mDelegator.getParent().requestDisallowInterceptTouchEvent(true);
                if (!this.mFlingScroller.isFinished()) {
                    this.mFlingScroller.forceFinished(true);
                    this.mAdjustScroller.forceFinished(true);
                    if (this.mScrollState == 2) {
                        this.mFlingScroller.abortAnimation();
                        this.mAdjustScroller.abortAnimation();
                    }
                    onScrollStateChange(0);
                } else if (!this.mAdjustScroller.isFinished()) {
                    this.mFlingScroller.forceFinished(true);
                    this.mAdjustScroller.forceFinished(true);
                } else if (this.mLastDownEventY < ((float) this.mTopSelectionDividerTop)) {
                    hideSoftInput();
                    postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
                } else if (this.mLastDownEventY > ((float) this.mBottomSelectionDividerBottom)) {
                    hideSoftInput();
                    postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
                } else {
                    this.mPerformClickOnTap = true;
                    postBeginSoftInputOnLongPressCommand();
                }
                return true;
            default:
                return false;
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = this.mDelegator.getMeasuredWidth();
        int measuredHeight = this.mDelegator.getMeasuredHeight();
        int measuredWidth2 = this.mInputText.getMeasuredWidth();
        int max = Math.max(this.mInputText.getMeasuredHeight(), (int) Math.floor((double) (((float) measuredHeight) * this.mHeightRatio)));
        this.mModifiedTxtHeight = max;
        int i5 = (measuredWidth - measuredWidth2) / 2;
        int i6 = (measuredHeight - max) / 2;
        int i7 = i6 + max;
        this.mInputText.layout(i5, i6, i5 + measuredWidth2, i7);
        if (z) {
            initializeSelectorWheel();
            this.mTopSelectionDividerTop = i6;
            this.mBottomSelectionDividerBottom = i7;
            this.mOverTopSelectionDividerTop = i6 - this.mSelectorElementHeight;
            this.mBelowBottomSelectionDividerBottom = this.mSelectorElementHeight + i7;
        }
    }

    public void onMeasure(int i, int i2) {
        this.mDelegator.superOnMeasure(makeMeasureSpec(i, this.mMaxWidth), makeMeasureSpec(i2, this.mMaxHeight));
        this.mDelegator.setMeasuredDimensionWrapper(resolveSizeAndStateRespectingMinSize(this.mMinWidth, this.mDelegator.getMeasuredWidth(), i), resolveSizeAndStateRespectingMinSize(this.mMinHeight, this.mDelegator.getMeasuredHeight(), i2));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mDelegator.isEnabled() || ((this.mIsEditTextMode && !this.mIsAmPm) || this.mIsStartingAnimation)) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 1:
                removeBeginSoftInputCommand();
                removeChangeCurrentByOneFromLongPress();
                this.mPressedStateHelper.cancel();
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumFlingVelocity);
                int yVelocity = (int) velocityTracker.getYVelocity();
                int y = (int) motionEvent.getY();
                int abs = (int) Math.abs(((float) y) - this.mLastDownEventY);
                if (this.mIsAmPm && this.mIgnoreMoveEvents) {
                    ensureScrollWheelAdjusted();
                    onScrollStateChange(0);
                } else if (Math.abs(yVelocity) <= this.mMinimumFlingVelocity) {
                    long eventTime = motionEvent.getEventTime() - this.mLastDownEventTime;
                    if (abs > this.mTouchSlop || eventTime >= ((long) ViewConfiguration.getLongPressTimeout())) {
                        ensureScrollWheelAdjusted();
                    } else if (this.mPerformClickOnTap) {
                        this.mPerformClickOnTap = false;
                        performClick();
                    } else if (y > this.mBelowBottomSelectionDividerBottom) {
                        changeValueByTwo(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    } else if (y < this.mOverTopSelectionDividerTop) {
                        changeValueByTwo(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    } else if (y > this.mBottomSelectionDividerBottom) {
                        changeValueByOne(true);
                        this.mPressedStateHelper.buttonTapped(1);
                    } else if (y < this.mTopSelectionDividerTop) {
                        changeValueByOne(false);
                        this.mPressedStateHelper.buttonTapped(2);
                    } else {
                        ensureScrollWheelAdjusted();
                    }
                    onScrollStateChange(0);
                } else if (abs > this.mTouchSlop || !this.mPerformClickOnTap) {
                    fling(yVelocity);
                    onScrollStateChange(2);
                } else {
                    this.mPerformClickOnTap = false;
                    performClick();
                    onScrollStateChange(0);
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                break;
            case 2:
                if (!this.mIgnoreMoveEvents) {
                    float y2 = motionEvent.getY();
                    if (this.mScrollState == 1) {
                        scrollBy(0, (int) (y2 - this.mLastDownOrMoveEventY));
                        this.mDelegator.invalidate();
                    } else if (((int) Math.abs(y2 - this.mLastDownEventY)) > this.mTouchSlop) {
                        removeAllCallbacks();
                        onScrollStateChange(1);
                    }
                    this.mLastDownOrMoveEventY = y2;
                    break;
                }
                break;
            case 3:
                ensureScrollWheelAdjusted();
                onScrollStateChange(0);
                break;
        }
        return true;
    }

    public void onWindowFocusChanged(boolean z) {
        if (z && this.mIsEditTextMode && this.mInputText.isFocused()) {
            showSoftInputForWindowFocused();
        }
        if (!this.mIsStartingAnimation) {
            if (!this.mFlingScroller.isFinished()) {
                this.mFlingScroller.forceFinished(true);
            }
            if (!this.mAdjustScroller.isFinished()) {
                this.mAdjustScroller.forceFinished(true);
            }
            ensureScrollWheelAdjusted();
        }
    }

    public void onWindowVisibilityChanged(int i) {
    }

    public void performClick() {
        if (!this.mIsAmPm) {
            showSoftInput();
        }
    }

    public void performClick(boolean z) {
        if (!this.mIsAmPm) {
            changeValueByOne(z);
        } else if (this.mValue == this.mMaxValue) {
            changeValueByOne(false);
        } else {
            changeValueByOne(true);
        }
    }

    public void performLongClick() {
        this.mIgnoreMoveEvents = true;
        if (!this.mIsAmPm) {
            showSoftInput();
        }
    }

    public void scrollBy(int i, int i2) {
        int[] iArr = this.mSelectorIndices;
        if (i2 != 0 && this.mSelectorElementHeight > 0) {
            if (!this.mWrapSelectorWheel && this.mCurrentScrollOffset + i2 > this.mInitialScrollOffset && iArr[2] <= this.mMinValue) {
                i2 = this.mInitialScrollOffset - this.mCurrentScrollOffset;
                if (this.mIsAmPm && this.mLastDownOrMoveEventY > ((float) this.mDelegator.getBottom())) {
                    this.mIgnoreMoveEvents = true;
                    return;
                }
            }
            if (!this.mWrapSelectorWheel && this.mCurrentScrollOffset + r8 < this.mInitialScrollOffset && iArr[2] >= this.mMaxValue) {
                i2 = this.mInitialScrollOffset - this.mCurrentScrollOffset;
                if (this.mIsAmPm && this.mLastDownOrMoveEventY < ((float) this.mDelegator.getTop())) {
                    this.mIgnoreMoveEvents = true;
                    return;
                }
            }
            this.mCurrentScrollOffset += i2;
            while (this.mCurrentScrollOffset - this.mInitialScrollOffset >= this.mSelectorElementHeight - this.mSelectorTextGapHeight) {
                this.mCurrentScrollOffset -= this.mSelectorElementHeight;
                decrementSelectorIndices(iArr);
                if (!this.mIsStartingAnimation) {
                    setValueInternal(iArr[2], true);
                    if (this.mLongPressCount > 0) {
                        this.mLongPressCount--;
                    } else {
                        playSoundAndHapticFeedback();
                    }
                }
                if (!this.mWrapSelectorWheel && iArr[2] <= this.mMinValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
            while (this.mCurrentScrollOffset - this.mInitialScrollOffset <= (-(this.mSelectorElementHeight - this.mSelectorTextGapHeight))) {
                this.mCurrentScrollOffset += this.mSelectorElementHeight;
                incrementSelectorIndices(iArr);
                if (!this.mIsStartingAnimation) {
                    setValueInternal(iArr[2], true);
                    if (this.mLongPressCount > 0) {
                        this.mLongPressCount--;
                    } else {
                        playSoundAndHapticFeedback();
                    }
                }
                if (!this.mWrapSelectorWheel && iArr[2] >= this.mMaxValue) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                }
            }
        }
    }

    public void setDisplayedValues(String[] strArr) {
        if (this.mDisplayedValues != strArr) {
            this.mDisplayedValues = strArr;
            if (this.mDisplayedValues != null) {
                this.mInputText.setRawInputType(524289);
            } else {
                this.mInputText.setRawInputType(2);
            }
            updateInputTextView();
            initializeSelectorWheelIndices();
            tryComputeMaxWidth();
        }
    }

    public void setEditTextMode(boolean z) {
        if (this.mIsEditTextMode != z) {
            this.mIsEditTextMode = z;
            if (!this.mIsEditTextMode || this.mIsAmPm) {
                this.mInputText.setVisibility(4);
                this.mDelegator.setDescendantFocusability(131072);
            } else {
                tryComputeMaxWidth();
                removeAllCallbacks();
                if (!this.mIsStartingAnimation) {
                    this.mCurrentScrollOffset = this.mInitialScrollOffset;
                    this.mFlingScroller.abortAnimation();
                    onScrollStateChange(0);
                }
                this.mDelegator.setDescendantFocusability(262144);
                updateInputTextView();
                this.mInputText.setVisibility(0);
                if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                    AccessibilityNodeProviderImpl accessibilityNodeProviderImpl = (AccessibilityNodeProviderImpl) getAccessibilityNodeProvider();
                    if (accessibilityNodeProviderImpl != null) {
                        accessibilityNodeProviderImpl.performAction(3, 128, null);
                    }
                }
            }
            this.mLastFocusedChildVirtualViewId = -1;
            this.mDelegator.invalidate();
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDelegator, this.mIsEditTextMode);
            }
        }
    }

    public void setEnabled(boolean z) {
        this.mInputText.setEnabled(z);
        if (!z && this.mScrollState != 0) {
            stopScrollAnimation();
            onScrollStateChange(0);
        }
    }

    public void setFormatter(Formatter formatter) {
        if (formatter != this.mFormatter) {
            this.mFormatter = formatter;
            initializeSelectorWheelIndices();
            updateInputTextView();
        }
    }

    public void setImeOptions(int i) {
        this.mInputText.setImeOptions(i);
    }

    public void setMaxInputLength(int i) {
        InputFilter inputFilter = this.mInputText.getFilters()[0];
        LengthFilter lengthFilter = new LengthFilter(i);
        this.mInputText.setFilters(new InputFilter[]{inputFilter, lengthFilter});
    }

    public void setMaxValue(int i) {
        boolean z = false;
        if (this.mMaxValue != i) {
            if (i < 0) {
                throw new IllegalArgumentException("maxValue must be >= 0");
            }
            this.mMaxValue = i;
            if (this.mMaxValue < this.mValue) {
                this.mValue = this.mMaxValue;
            }
            if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length) {
                z = true;
            }
            setWrapSelectorWheel(z);
            initializeSelectorWheelIndices();
            updateInputTextView();
            tryComputeMaxWidth();
            this.mDelegator.invalidate();
        }
    }

    public void setMinValue(int i) {
        boolean z = false;
        if (this.mMinValue != i) {
            if (i < 0) {
                throw new IllegalArgumentException("minValue must be >= 0");
            }
            this.mMinValue = i;
            if (this.mMinValue > this.mValue) {
                this.mValue = this.mMinValue;
            }
            if (this.mMaxValue - this.mMinValue > this.mSelectorIndices.length) {
                z = true;
            }
            setWrapSelectorWheel(z);
            initializeSelectorWheelIndices();
            updateInputTextView();
            tryComputeMaxWidth();
            this.mDelegator.invalidate();
        }
    }

    public void setMonthInputMode() {
        this.mInputText.setImeOptions(FingerprintEvent.IMAGE_QUALITY_FINGER_TOO_THIN);
        this.mInputText.setPrivateImeOptions("inputType=month_edittext");
        this.mInputText.setText("");
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public void setOnLongPressUpdateInterval(long j) {
        this.mLongPressUpdateInterval = j;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mOnValueChangeListener = onValueChangeListener;
    }

    public void setPickerContentDescription(String str) {
        this.mPickerContentDescription = str;
        ((CustomEditText) this.mInputText).setPickerContentDescription(str);
    }

    public void setSkipValuesOnLongPressEnabled(boolean z) {
        this.mSkipNumbers = z;
    }

    public void setSubTextSize(float f) {
        this.mSubTextSize = (int) TypedValue.applyDimension(1, f, this.mContext.getResources().getDisplayMetrics());
    }

    public void setTextSize(float f) {
        this.mTextSize = (int) TypedValue.applyDimension(1, f, this.mContext.getResources().getDisplayMetrics());
        this.mSelectorWheelPaint.setTextSize((float) this.mTextSize);
        this.mInputText.setTextSize(0, (float) this.mTextSize);
        tryComputeMaxWidth();
    }

    public void setTextTypeface(Typeface typeface) {
        this.mCustomTypefaceSet = true;
        this.mPickerTypeface = typeface;
        this.mPickerBoldTypeface = Typeface.create(typeface, 1);
        this.mSelectorWheelPaint.setTypeface(this.mPickerTypeface);
        this.mInputText.setTypeface(this.mPickerBoldTypeface);
        tryComputeMaxWidth();
    }

    public void setToggle(boolean z) {
        this.mIsAmPm = z;
        if (this.mIsAmPm) {
            Resources resources = this.mContext.getResources();
            this.mTextSize = resources.getDimensionPixelSize(17105720);
            this.mSelectorWheelPaint.setTextSize((float) this.mTextSize);
            this.mInputText.setTextSize(0, (float) this.mTextSize);
            this.mInputText.setAccessibilityDelegate(null);
            this.mPickerTypeface = Typeface.create("sec-roboto-condensed", 0);
            if (SystemProperties.get("persist.sys.flipfontpath").endsWith("#Theme")) {
                String string = System.getString(this.mContext.getContentResolver(), "theme_font_system");
                if (!(string == null || string.equals(""))) {
                    this.mPickerTypeface = getFontTypeface(string);
                }
            }
            this.mPickerBoldTypeface = Typeface.create(this.mPickerTypeface, 1);
            this.mInputText.setTypeface(this.mPickerBoldTypeface);
            this.mSelectorWheelPaint.setTypeface(this.mPickerTypeface);
            this.mSubTextSize = resources.getDimensionPixelSize(17105719);
        }
    }

    public void setValue(int i) {
        if (!this.mFlingScroller.isFinished()) {
            stopScrollAnimation();
        }
        setValueInternal(i, false);
    }

    public void setWrapSelectorWheel(boolean z) {
        Object obj = this.mMaxValue - this.mMinValue >= this.mSelectorIndices.length ? 1 : null;
        if ((!z || obj != null) && z != this.mWrapSelectorWheel) {
            this.mWrapSelectorWheel = z;
        }
    }

    public void setYearDateTimeInputMode() {
        this.mInputText.setImeOptions(FingerprintEvent.IMAGE_QUALITY_FINGER_TOO_THIN);
        this.mInputText.setPrivateImeOptions("inputType=YearDateTime_edittext");
        this.mInputText.setText("");
    }

    public void startAnimation(final int i, SemAnimationListener semAnimationListener) {
        this.mAnimationListener = semAnimationListener;
        if (!this.mIsEditTextMode) {
            int i2 = i;
            this.mDelegator.post(new Runnable() {
                public void run() {
                    if (SemNumberPickerSpinnerDelegate.this.mSelectorElementHeight == 0) {
                        SemNumberPickerSpinnerDelegate.this.mReservedStartAnimation = true;
                        return;
                    }
                    SemNumberPickerSpinnerDelegate.this.mIsStartingAnimation = true;
                    SemNumberPickerSpinnerDelegate.this.mFlingScroller = SemNumberPickerSpinnerDelegate.this.mCustomScroller;
                    int -get23 = SemNumberPickerSpinnerDelegate.this.getValue() != SemNumberPickerSpinnerDelegate.this.getMinValue() ? SemNumberPickerSpinnerDelegate.this.mSelectorElementHeight : -SemNumberPickerSpinnerDelegate.this.mSelectorElementHeight;
                    int -get232 = SemNumberPickerSpinnerDelegate.this.mIsAmPm ? -get23 : SemNumberPickerSpinnerDelegate.this.mSelectorElementHeight * 5;
                    final int -get233 = SemNumberPickerSpinnerDelegate.this.mIsAmPm ? -get23 : (int) (((double) SemNumberPickerSpinnerDelegate.this.mSelectorElementHeight) * 5.4d);
                    SemNumberPickerSpinnerDelegate.this.scrollBy(0, -get232);
                    SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Handler handler = new Handler();
                            final int i = -get233;
                            handler.postDelayed(new Runnable() {

                                class C03031 implements Runnable {
                                    C03031() {
                                    }

                                    public void run() {
                                        SemNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SemNumberPickerSpinnerDelegate.this.mFlingScroller);
                                        SemNumberPickerSpinnerDelegate.this.mFlingScroller.abortAnimation();
                                        SemNumberPickerSpinnerDelegate.this.mAdjustScroller.abortAnimation();
                                        SemNumberPickerSpinnerDelegate.this.ensureScrollWheelAdjusted();
                                        SemNumberPickerSpinnerDelegate.this.mFlingScroller = SemNumberPickerSpinnerDelegate.this.mLinearScroller;
                                        SemNumberPickerSpinnerDelegate.this.mIsStartingAnimation = false;
                                        SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                                        if (SemNumberPickerSpinnerDelegate.this.mAnimationListener != null) {
                                            SemNumberPickerSpinnerDelegate.this.mAnimationListener.onAnimationEnd();
                                        }
                                    }
                                }

                                public void run() {
                                    if (!SemNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SemNumberPickerSpinnerDelegate.this.mFlingScroller)) {
                                        SemNumberPickerSpinnerDelegate.this.moveToFinalScrollerPosition(SemNumberPickerSpinnerDelegate.this.mAdjustScroller);
                                    }
                                    SemNumberPickerSpinnerDelegate.this.mPreviousScrollerY = 0;
                                    SemNumberPickerSpinnerDelegate.this.mFlingScroller.startScroll(0, 0, 0, -i, SemNumberPickerSpinnerDelegate.this.mIsAmPm ? SemNumberPickerSpinnerDelegate.START_ANIMATION_SCROLL_DURATION : SemNumberPickerSpinnerDelegate.START_ANIMATION_SCROLL_DURATION_2016B);
                                    SemNumberPickerSpinnerDelegate.this.mDelegator.invalidate();
                                    new Handler().postDelayed(new C03031(), 857);
                                }
                            }, 100);
                        }
                    }, (long) i);
                }
            });
        }
    }
}
