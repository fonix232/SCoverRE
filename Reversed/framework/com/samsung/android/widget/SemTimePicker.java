package com.samsung.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.Locale;

public class SemTimePicker extends FrameLayout {
    public static final int PICKER_AMPM = 2;
    public static final int PICKER_HOUR = 0;
    public static final int PICKER_MINUTE = 1;
    private SemTimePickerDelegate mDelegate;

    interface SemTimePickerDelegate {
        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        int getBaseline();

        int getDefaultHeight();

        int getDefaultWidth();

        EditText getEditText(int i);

        int getHour();

        int getMinute();

        SemNumberPicker getNumberPicker(int i);

        boolean is24HourView();

        boolean isEditTextMode();

        boolean isEnabled();

        void onConfigurationChanged(Configuration configuration);

        void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo);

        void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        void onRestoreInstanceState(Parcelable parcelable);

        Parcelable onSaveInstanceState(Parcelable parcelable);

        void setCurrentLocale(Locale locale);

        void setEditTextMode(boolean z);

        void setEnabled(boolean z);

        void setHour(int i);

        void setIs24HourView(Boolean bool);

        void setMinute(int i);

        void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener);

        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);

        void startAnimation(int i, SemAnimationListener semAnimationListener);
    }

    static abstract class AbstractSemTimePickerDelegate implements SemTimePickerDelegate {
        protected Context mContext;
        protected Locale mCurrentLocale;
        protected SemTimePicker mDelegator;
        protected OnEditTextModeChangedListener mOnEditTextModeChangedListener;
        protected OnTimeChangedListener mOnTimeChangedListener;

        public AbstractSemTimePickerDelegate(SemTimePicker semTimePicker, Context context) {
            this.mDelegator = semTimePicker;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        public void setCurrentLocale(Locale locale) {
            if (!locale.equals(this.mCurrentLocale)) {
                this.mCurrentLocale = locale;
            }
        }
    }

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SemTimePicker semTimePicker, boolean z);
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(SemTimePicker semTimePicker, int i, int i2);
    }

    public SemTimePicker(Context context) {
        this(context, null);
    }

    public SemTimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843933);
    }

    public SemTimePicker(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SemTimePicker(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDelegate = new SemTimePickerSpinnerDelegate(this, context, attributeSet, i, i2);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    public int getBaseline() {
        return this.mDelegate.getBaseline();
    }

    public EditText getEditText(int i) {
        return this.mDelegate.getEditText(i);
    }

    public int getHour() {
        return this.mDelegate.getHour();
    }

    public int getMinute() {
        return this.mDelegate.getMinute();
    }

    public SemNumberPicker getNumberPicker(int i) {
        return this.mDelegate.getNumberPicker(i);
    }

    public boolean is24HourView() {
        return this.mDelegate.is24HourView();
    }

    public boolean isEditTextMode() {
        return this.mDelegate.isEditTextMode();
    }

    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDelegate.onConfigurationChanged(configuration);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        this.mDelegate.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        this.mDelegate.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
    }

    protected void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (mode == FingerprintManager.PRIVILEGED_TYPE_KEYGUARD) {
            i = MeasureSpec.makeMeasureSpec(this.mDelegate.getDefaultWidth(), 1073741824);
        }
        if (mode2 == FingerprintManager.PRIVILEGED_TYPE_KEYGUARD) {
            i2 = MeasureSpec.makeMeasureSpec(this.mDelegate.getDefaultHeight(), 1073741824);
        }
        super.onMeasure(i, i2);
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        this.mDelegate.onPopulateAccessibilityEvent(accessibilityEvent);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        BaseSavedState baseSavedState = (BaseSavedState) parcelable;
        super.onRestoreInstanceState(baseSavedState.getSuperState());
        this.mDelegate.onRestoreInstanceState(baseSavedState);
    }

    protected Parcelable onSaveInstanceState() {
        return this.mDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    public void setEditTextMode(boolean z) {
        this.mDelegate.setEditTextMode(z);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mDelegate.setEnabled(z);
    }

    public void setHour(int i) {
        this.mDelegate.setHour(i);
    }

    public void setIs24HourView(Boolean bool) {
        this.mDelegate.setIs24HourView(bool);
    }

    public void setLargeBackground() {
    }

    public void setLocale(Locale locale) {
        this.mDelegate.setCurrentLocale(locale);
    }

    public void setMinute(int i) {
        this.mDelegate.setMinute(i);
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mDelegate.setOnEditTextModeChangedListener(onEditTextModeChangedListener);
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    public void startAnimation(int i, SemAnimationListener semAnimationListener) {
        this.mDelegate.startAnimation(i, semAnimationListener);
    }
}
