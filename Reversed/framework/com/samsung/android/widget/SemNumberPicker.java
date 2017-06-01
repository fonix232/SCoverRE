package com.samsung.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.util.Locale;
import libcore.icu.LocaleData;

public class SemNumberPicker extends LinearLayout {
    private static final TwoDigitFormatter sTwoDigitFormatter = new TwoDigitFormatter();
    private SemNumberPickerDelegate mDelegate;

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SemNumberPicker semNumberPicker, boolean z);
    }

    public interface OnValueChangeListener {
        void onValueChange(SemNumberPicker semNumberPicker, int i, int i2);
    }

    interface SemNumberPickerDelegate {
        void computeScroll();

        int computeVerticalScrollExtent();

        int computeVerticalScrollOffset();

        int computeVerticalScrollRange();

        boolean dispatchHoverEvent(MotionEvent motionEvent);

        boolean dispatchKeyEvent(KeyEvent keyEvent);

        boolean dispatchKeyEventPreIme(KeyEvent keyEvent);

        boolean dispatchTouchEvent(MotionEvent motionEvent);

        void dispatchTrackballEvent(MotionEvent motionEvent);

        AccessibilityNodeProvider getAccessibilityNodeProvider();

        String[] getDisplayedValues();

        EditText getEditText();

        int getMaxHeight();

        int getMaxValue();

        int getMaxWidth();

        int getMinHeight();

        int getMinValue();

        int getMinWidth();

        boolean getToggle();

        int getValue();

        boolean getWrapSelectorWheel();

        boolean isEditTextMode();

        boolean isEditTextModeNotAmPm();

        void onAttachedToWindow();

        void onConfigurationChanged(Configuration configuration);

        void onDetachedFromWindow();

        void onDraw(Canvas canvas);

        void onFocusChanged(boolean z, int i, Rect rect);

        boolean onGenericMotionEvent(MotionEvent motionEvent);

        void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        boolean onInterceptTouchEvent(MotionEvent motionEvent);

        void onLayout(boolean z, int i, int i2, int i3, int i4);

        void onMeasure(int i, int i2);

        boolean onTouchEvent(MotionEvent motionEvent);

        void onWindowFocusChanged(boolean z);

        void onWindowVisibilityChanged(int i);

        void performClick();

        void performClick(boolean z);

        void performLongClick();

        void scrollBy(int i, int i2);

        void setDisplayedValues(String[] strArr);

        void setEditTextMode(boolean z);

        void setEnabled(boolean z);

        void setFormatter(Formatter formatter);

        void setImeOptions(int i);

        void setMaxInputLength(int i);

        void setMaxValue(int i);

        void setMinValue(int i);

        void setMonthInputMode();

        void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener);

        void setOnLongPressUpdateInterval(long j);

        void setOnScrollListener(OnScrollListener onScrollListener);

        void setOnValueChangedListener(OnValueChangeListener onValueChangeListener);

        void setPickerContentDescription(String str);

        void setSkipValuesOnLongPressEnabled(boolean z);

        void setSubTextSize(float f);

        void setTextSize(float f);

        void setTextTypeface(Typeface typeface);

        void setToggle(boolean z);

        void setValue(int i);

        void setWrapSelectorWheel(boolean z);

        void setYearDateTimeInputMode();

        void startAnimation(int i, SemAnimationListener semAnimationListener);
    }

    static abstract class AbstractSemNumberPickerDelegate implements SemNumberPickerDelegate {
        protected Context mContext;
        protected SemNumberPicker mDelegator;

        public AbstractSemNumberPickerDelegate(SemNumberPicker semNumberPicker, Context context) {
            this.mDelegator = semNumberPicker;
            this.mContext = context;
        }
    }

    public static class CustomEditText extends EditText {
        private int mAdjustEditTextPosition;
        private String mPickerContentDescription = "";

        public CustomEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        private CharSequence getTextForAccessibility() {
            CharSequence text = getText();
            return !this.mPickerContentDescription.equals("") ? !TextUtils.isEmpty(text) ? text.toString() + ", " + this.mPickerContentDescription : ", " + this.mPickerContentDescription : text;
        }

        protected void onDraw(Canvas canvas) {
            canvas.translate(0.0f, (float) this.mAdjustEditTextPosition);
            super.onDraw(canvas);
        }

        public void onEditorAction(int i) {
            super.onEditorAction(i);
            if (i == 6) {
                clearFocus();
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(getTextForAccessibility());
        }

        public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            int size = accessibilityEvent.getText().size();
            super.onPopulateAccessibilityEvent(accessibilityEvent);
            int size2 = accessibilityEvent.getText().size();
            if (size2 > size) {
                accessibilityEvent.getText().remove(size2 - 1);
            }
            int eventType = accessibilityEvent.getEventType();
            CharSequence text = (eventType == 16 || eventType == 8192) ? getText() : getTextForAccessibility();
            if (!TextUtils.isEmpty(text)) {
                accessibilityEvent.getText().add(text);
            }
        }

        public void setEditTextPosition(int i) {
            this.mAdjustEditTextPosition = i;
        }

        public void setPickerContentDescription(String str) {
            this.mPickerContentDescription = str;
        }
    }

    public interface Formatter {
        String format(int i);
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScrollStateChange(SemNumberPicker semNumberPicker, int i);
    }

    private static class TwoDigitFormatter implements Formatter {
        final Object[] mArgs = new Object[1];
        final StringBuilder mBuilder = new StringBuilder();
        java.util.Formatter mFmt;
        char mZeroDigit;

        TwoDigitFormatter() {
            init(Locale.getDefault());
        }

        private java.util.Formatter createFormatter(Locale locale) {
            return new java.util.Formatter(this.mBuilder, locale);
        }

        private static char getZeroDigit(Locale locale) {
            return LocaleData.get(locale).zeroDigit;
        }

        private void init(Locale locale) {
            this.mFmt = createFormatter(locale);
            this.mZeroDigit = getZeroDigit(locale);
        }

        public String format(int i) {
            Locale locale = Locale.getDefault();
            if (this.mZeroDigit != getZeroDigit(locale)) {
                init(locale);
            }
            this.mArgs[0] = Integer.valueOf(i);
            this.mBuilder.delete(0, this.mBuilder.length());
            this.mFmt.format("%02d", this.mArgs);
            return this.mFmt.toString();
        }
    }

    public SemNumberPicker(Context context) {
        this(context, null);
    }

    public SemNumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SemNumberPicker(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SemNumberPicker(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDelegate = new SemNumberPickerSpinnerDelegate(this, context, attributeSet, i, i2);
    }

    public static final Formatter getTwoDigitFormatter() {
        return sTwoDigitFormatter;
    }

    public void computeScroll() {
        this.mDelegate.computeScroll();
    }

    protected int computeVerticalScrollExtent() {
        return this.mDelegate.computeVerticalScrollExtent();
    }

    protected int computeVerticalScrollOffset() {
        return this.mDelegate.computeVerticalScrollOffset();
    }

    protected int computeVerticalScrollRange() {
        return this.mDelegate.computeVerticalScrollRange();
    }

    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mDelegate.isEditTextModeNotAmPm() ? super.dispatchHoverEvent(motionEvent) : this.mDelegate.dispatchHoverEvent(motionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.mDelegate.dispatchKeyEvent(keyEvent) ? true : super.dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        return this.mDelegate.dispatchKeyEventPreIme(keyEvent) ? true : super.dispatchKeyEventPreIme(keyEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.mDelegate.dispatchTouchEvent(motionEvent);
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        this.mDelegate.dispatchTrackballEvent(motionEvent);
        return super.dispatchTrackballEvent(motionEvent);
    }

    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        return this.mDelegate.isEditTextModeNotAmPm() ? super.getAccessibilityNodeProvider() : this.mDelegate.getAccessibilityNodeProvider();
    }

    public String[] getDisplayedValues() {
        return this.mDelegate.getDisplayedValues();
    }

    public EditText getEditText() {
        return this.mDelegate.getEditText();
    }

    public int[] getEnableStateSet() {
        return ENABLED_STATE_SET;
    }

    public int getMaxValue() {
        return this.mDelegate.getMaxValue();
    }

    public int getMinValue() {
        return this.mDelegate.getMinValue();
    }

    public boolean getToggle() {
        return this.mDelegate.getToggle();
    }

    public int getValue() {
        return this.mDelegate.getValue();
    }

    public boolean getWrapSelectorWheel() {
        return this.mDelegate.getWrapSelectorWheel();
    }

    public boolean isEditTextMode() {
        return this.mDelegate.isEditTextMode();
    }

    public boolean isVisibleToUserWrapper() {
        return isVisibleToUser(null);
    }

    public boolean isVisibleToUserWrapper(Rect rect) {
        return isVisibleToUser(rect);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDelegate.onAttachedToWindow();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDelegate.onConfigurationChanged(configuration);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mDelegate.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            super.onDraw(canvas);
        } else {
            this.mDelegate.onDraw(canvas);
        }
    }

    protected void onFocusChanged(boolean z, int i, Rect rect) {
        this.mDelegate.onFocusChanged(z, i, rect);
        super.onFocusChanged(z, i, rect);
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return (this.mDelegate.isEditTextMode() || !this.mDelegate.onGenericMotionEvent(motionEvent)) ? super.onGenericMotionEvent(motionEvent) : true;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        this.mDelegate.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mDelegate.onInterceptTouchEvent(motionEvent);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mDelegate.onLayout(z, i, i2, i3, i4);
    }

    protected void onMeasure(int i, int i2) {
        this.mDelegate.onMeasure(i, i2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mDelegate.onTouchEvent(motionEvent);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.mDelegate.onWindowFocusChanged(z);
    }

    protected void onWindowVisibilityChanged(int i) {
        this.mDelegate.onWindowVisibilityChanged(i);
        super.onWindowVisibilityChanged(i);
    }

    public void performClick(boolean z) {
        this.mDelegate.performClick(z);
    }

    public boolean performClick() {
        if (this.mDelegate.isEditTextModeNotAmPm()) {
            return super.performClick();
        }
        if (!super.performClick()) {
            this.mDelegate.performClick();
        }
        return true;
    }

    public boolean performLongClick() {
        if (!super.performLongClick()) {
            this.mDelegate.performLongClick();
        }
        return true;
    }

    public void scrollBy(int i, int i2) {
        this.mDelegate.scrollBy(i, i2);
    }

    public void setDisplayedValues(String[] strArr) {
        this.mDelegate.setDisplayedValues(strArr);
    }

    public void setEditTextMode(boolean z) {
        this.mDelegate.setEditTextMode(z);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mDelegate.setEnabled(z);
    }

    public void setFormatter(Formatter formatter) {
        this.mDelegate.setFormatter(formatter);
    }

    public void setImeOptions(int i) {
        this.mDelegate.setImeOptions(i);
    }

    public void setLargeBackground() {
    }

    public void setMaxInputLength(int i) {
        this.mDelegate.setMaxInputLength(i);
    }

    public void setMaxValue(int i) {
        this.mDelegate.setMaxValue(i);
    }

    public void setMeasuredDimensionWrapper(int i, int i2) {
        setMeasuredDimension(i, i2);
    }

    public void setMinValue(int i) {
        this.mDelegate.setMinValue(i);
    }

    public void setMonthInputMode() {
        this.mDelegate.setMonthInputMode();
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mDelegate.setOnEditTextModeChangedListener(onEditTextModeChangedListener);
    }

    public void setOnLongPressUpdateInterval(long j) {
        this.mDelegate.setOnLongPressUpdateInterval(j);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mDelegate.setOnScrollListener(onScrollListener);
    }

    public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
        this.mDelegate.setOnValueChangedListener(onValueChangeListener);
    }

    public void setPickerContentDescription(String str) {
        this.mDelegate.setPickerContentDescription(str);
    }

    public void setSkipValuesOnLongPressEnabled(boolean z) {
        this.mDelegate.setSkipValuesOnLongPressEnabled(z);
    }

    public void setSubTextSize(float f) {
        this.mDelegate.setSubTextSize(f);
    }

    public void setTextSize(float f) {
        this.mDelegate.setTextSize(f);
    }

    public void setTextTypeface(Typeface typeface) {
        this.mDelegate.setTextTypeface(typeface);
    }

    public void setToggle(boolean z) {
        this.mDelegate.setToggle(z);
    }

    public void setValue(int i) {
        this.mDelegate.setValue(i);
    }

    public void setWrapSelectorWheel(boolean z) {
        this.mDelegate.setWrapSelectorWheel(z);
    }

    public void setYearDateTimeInputMode() {
        this.mDelegate.setYearDateTimeInputMode();
    }

    public void skipValuesOnLongPress(boolean z) {
        setSkipValuesOnLongPressEnabled(z);
    }

    public void startAnimation(int i, SemAnimationListener semAnimationListener) {
        this.mDelegate.startAnimation(i, semAnimationListener);
    }

    public void superOnMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }
}
