package com.samsung.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings.System;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.widget.SemNumberPicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemNumberPicker.OnValueChangeListener;
import com.samsung.android.widget.SemTimePicker.OnTimeChangedListener;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.LocaleData;

class SemTimePickerSpinnerDelegate extends AbstractSemTimePickerDelegate {
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final char[] DIGIT_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩', '۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹', '०', '१', '२', '३', '४', '५', '६', '७', '८', '९', '০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯', '೦', '೧', '೨', '೩', '೪', '೫', '೬', '೭', '೮', '೯'};
    private static final int HOURS_IN_HALF_DAY = 12;
    private boolean SEM_DEBUG = false;
    private final View mAmPmMarginInside;
    private final View mAmPmMarginOutside;
    private final SemNumberPicker mAmPmSpinner;
    private final EditText mAmPmSpinnerInput;
    private final String[] mAmPmStrings;
    private final int mDefaultHeight;
    private final int mDefaultWidth;
    private final TextView mDivider;
    private OnEditorActionListener mEditorActionListener = new C03082();
    private final View mEmpty1;
    private final View mEmpty2;
    private char mHourFormat;
    private final SemNumberPicker mHourSpinner;
    private final EditText mHourSpinnerInput;
    private boolean mHourWithTwoDigit;
    private boolean mIs24HourView;
    private boolean mIsAm;
    private boolean mIsAmPmAutoFlipped = false;
    private boolean mIsEditTextMode;
    private boolean mIsEnabled = true;
    private final SemNumberPicker mMinuteSpinner;
    private final EditText mMinuteSpinnerInput;
    private OnEditTextModeChangedListener mModeChangeListener = new C03071();
    private EditText[] mPickerTexts = new EditText[3];
    private Calendar mTempCalendar;

    class C03071 implements OnEditTextModeChangedListener {
        C03071() {
        }

        public void onEditTextModeChanged(SemNumberPicker semNumberPicker, boolean z) {
            SemTimePickerSpinnerDelegate.this.setEditTextMode(z);
            SemTimePickerSpinnerDelegate.this.updateModeState(semNumberPicker, z);
        }
    }

    class C03082 implements OnEditorActionListener {
        C03082() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6) {
                SemTimePickerSpinnerDelegate.this.updateInputState();
                SemTimePickerSpinnerDelegate.this.setEditTextMode(false);
            }
            return false;
        }
    }

    class C03103 implements OnValueChangeListener {

        class C03091 implements Runnable {
            C03091() {
            }

            public void run() {
                SemTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = false;
                if (SemTimePickerSpinnerDelegate.this.mAmPmSpinner != null) {
                    SemTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(true);
                }
            }
        }

        C03103() {
        }

        public void onValueChange(SemNumberPicker semNumberPicker, int i, int i2) {
            if (!(SemTimePickerSpinnerDelegate.this.is24HourView() || SemTimePickerSpinnerDelegate.this.mIsEditTextMode)) {
                int i3 = 12;
                if (SemTimePickerSpinnerDelegate.this.mHourFormat == 'K') {
                    i3 = 0;
                }
                if (!(i == 11 && i2 == i3)) {
                    if (i == i3 && i2 == 11) {
                    }
                }
                SemTimePickerSpinnerDelegate.this.mIsAm = SemTimePickerSpinnerDelegate.this.mAmPmSpinner.getValue() != 0;
                SemTimePickerSpinnerDelegate.this.mAmPmSpinner.performClick(false);
                SemTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = true;
                SemTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(false);
                new Handler().postDelayed(new C03091(), 500);
            }
            SemTimePickerSpinnerDelegate.this.onTimeChanged();
        }
    }

    class C03114 implements OnValueChangeListener {
        C03114() {
        }

        public void onValueChange(SemNumberPicker semNumberPicker, int i, int i2) {
            SemTimePickerSpinnerDelegate.this.onTimeChanged();
        }
    }

    class C03125 implements OnValueChangeListener {
        C03125() {
        }

        public void onValueChange(SemNumberPicker semNumberPicker, int i, int i2) {
            boolean z = true;
            if (!SemTimePickerSpinnerDelegate.this.mAmPmSpinner.isEnabled()) {
                SemTimePickerSpinnerDelegate.this.mAmPmSpinner.setEnabled(true);
            }
            if (SemTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped) {
                SemTimePickerSpinnerDelegate.this.mIsAmPmAutoFlipped = false;
            } else if (!(SemTimePickerSpinnerDelegate.this.mIsAm && i2 == 0) && (SemTimePickerSpinnerDelegate.this.mIsAm || i2 != 1)) {
                SemTimePickerSpinnerDelegate semTimePickerSpinnerDelegate = SemTimePickerSpinnerDelegate.this;
                if (i2 != 0) {
                    z = false;
                }
                semTimePickerSpinnerDelegate.mIsAm = z;
                SemTimePickerSpinnerDelegate.this.updateAmPmControl();
                SemTimePickerSpinnerDelegate.this.onTimeChanged();
                SemTimePickerSpinnerDelegate.this.validCheck();
            }
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C03131();
        private final int mHour;
        private final int mMinute;

        static class C03131 implements Creator<SavedState> {
            C03131() {
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mHour = parcel.readInt();
            this.mMinute = parcel.readInt();
        }

        private SavedState(Parcelable parcelable, int i, int i2) {
            super(parcelable);
            this.mHour = i;
            this.mMinute = i2;
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mHour);
            parcel.writeInt(this.mMinute);
        }
    }

    private class SemKeyListener implements OnKeyListener {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (SemTimePickerSpinnerDelegate.this.SEM_DEBUG) {
                Log.d("Picker", keyEvent.toString());
            }
            if (keyEvent.getAction() != 1) {
                return false;
            }
            switch (i) {
                case 23:
                    if (SemTimePickerSpinnerDelegate.this.mDelegator.getResources().getConfiguration().keyboard == 3) {
                        return false;
                    }
                    break;
                case SemMotionRecognitionEvent.PANNING_GYRO /*61*/:
                case SemMotionRecognitionEvent.BLOW /*66*/:
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    public class SemTextWatcher implements TextWatcher {
        private int changedLen = 0;
        private int mId;
        private int mMaxLen;
        private int mNext;
        private String prevText;

        public SemTextWatcher(int i, int i2) {
            this.mMaxLen = i;
            this.mId = i2;
            this.mNext = this.mId + 1 >= 2 ? -1 : this.mId + 1;
        }

        private void changeFocus() {
            if (!AccessibilityManager.getInstance(SemTimePickerSpinnerDelegate.this.mContext).isTouchExplorationEnabled()) {
                if (this.mNext >= 0) {
                    SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mNext].requestFocus();
                    if (SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                        SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].clearFocus();
                    }
                } else if (this.mId == 1) {
                    SemTimePickerSpinnerDelegate.this.setMinute(Integer.parseInt(String.valueOf(SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].getText())));
                    SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].selectAll();
                }
            }
        }

        private int convertDigitCharacterToNumber(String str) {
            int i = 0;
            for (char ch : SemTimePickerSpinnerDelegate.DIGIT_CHARACTERS) {
                if (str.equals(Character.toString(ch))) {
                    return i % 10;
                }
                i++;
            }
            return -1;
        }

        public void afterTextChanged(Editable editable) {
            if (SemTimePickerSpinnerDelegate.this.SEM_DEBUG) {
                Log.d("Picker", "aftertextchanged: " + editable.toString());
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (SemTimePickerSpinnerDelegate.this.SEM_DEBUG) {
                Log.d("Picker", "beforeTextChanged: " + charSequence + ", " + i + ", " + i2 + ", " + i3);
            }
            this.prevText = charSequence.toString();
            this.changedLen = i3;
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (SemTimePickerSpinnerDelegate.this.SEM_DEBUG) {
                Log.d("Picker", "onTextChanged: " + this.prevText);
            }
            if (SemTimePickerSpinnerDelegate.this.SEM_DEBUG) {
                Log.d("Picker", "onTextChanged: " + charSequence + ", " + i + ", " + i2 + ", " + i3);
            }
            String str = (String) SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].getTag();
            if (str == null || !(str.equals("onClick") || str.equals("onLongClick"))) {
                int convertDigitCharacterToNumber;
                if (this.mId == 0) {
                    if (this.changedLen == 1) {
                        if (charSequence.length() == this.mMaxLen) {
                            if (SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                            }
                        } else if (charSequence.length() > 0) {
                            convertDigitCharacterToNumber = convertDigitCharacterToNumber(charSequence.toString());
                            if ((convertDigitCharacterToNumber > 2 || (convertDigitCharacterToNumber > 1 && !SemTimePickerSpinnerDelegate.this.is24HourView())) && SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                            }
                        }
                    }
                } else if (this.mId == 1) {
                    if (this.changedLen == 1) {
                        if (charSequence.length() == this.mMaxLen) {
                            if (SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                            }
                        } else if (charSequence.length() > 0) {
                            convertDigitCharacterToNumber = convertDigitCharacterToNumber(charSequence.toString());
                            if (convertDigitCharacterToNumber >= 6 && convertDigitCharacterToNumber <= 9 && SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                                changeFocus();
                            }
                        }
                    }
                } else if (this.prevText.length() < charSequence.length() && charSequence.length() == this.mMaxLen && SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].isFocused()) {
                    changeFocus();
                }
                return;
            }
            SemTimePickerSpinnerDelegate.this.mPickerTexts[this.mId].setTag("");
        }
    }

    public SemTimePickerSpinnerDelegate(SemTimePicker semTimePicker, Context context, AttributeSet attributeSet, int i, int i2) {
        super(semTimePicker, context);
        LayoutInflater.from(this.mContext).inflate(17367320, this.mDelegator, true);
        this.mHourSpinner = (SemNumberPicker) semTimePicker.findViewById(16909527);
        this.mHourSpinner.setPickerContentDescription(context.getResources().getString(17041658));
        this.mHourSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mHourSpinner.setOnValueChangedListener(new C03103());
        this.mHourSpinnerInput = (EditText) this.mHourSpinner.findViewById(16909401);
        this.mHourSpinner.setYearDateTimeInputMode();
        this.mHourSpinnerInput.setImeOptions(33554437);
        this.mHourSpinner.setMaxInputLength(2);
        this.mHourSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mDivider = (TextView) this.mDelegator.findViewById(16909528);
        if (this.mDivider != null) {
            setDividerText();
        }
        Resources resources = this.mDelegator.getResources();
        int i3 = resources.getConfiguration().smallestScreenWidthDp;
        if (i3 >= 600) {
            this.mDefaultWidth = resources.getDimensionPixelSize(17105725);
        } else {
            this.mDefaultWidth = (int) (TypedValue.applyDimension(1, (float) i3, resources.getDisplayMetrics()) + 0.5f);
        }
        this.mDefaultHeight = resources.getDimensionPixelSize(17105721);
        this.mMinuteSpinner = (SemNumberPicker) this.mDelegator.findViewById(16909529);
        this.mMinuteSpinner.setYearDateTimeInputMode();
        this.mMinuteSpinner.setMinValue(0);
        this.mMinuteSpinner.setMaxValue(59);
        this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
        this.mMinuteSpinner.setSkipValuesOnLongPressEnabled(true);
        this.mMinuteSpinner.setFormatter(SemNumberPicker.getTwoDigitFormatter());
        this.mMinuteSpinner.setPickerContentDescription(context.getResources().getString(17041659));
        this.mMinuteSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mMinuteSpinner.setOnValueChangedListener(new C03114());
        this.mMinuteSpinnerInput = (EditText) this.mMinuteSpinner.findViewById(16909401);
        this.mMinuteSpinnerInput.setImeOptions(33554438);
        this.mMinuteSpinner.setMaxInputLength(2);
        this.mMinuteSpinnerInput.setOnEditorActionListener(this.mEditorActionListener);
        this.mMinuteSpinnerInput.setId(View.generateViewId());
        this.mHourSpinnerInput.setNextFocusForwardId(this.mMinuteSpinnerInput.getId());
        this.mAmPmStrings = getAmPmStrings(context);
        View findViewById = this.mDelegator.findViewById(16909531);
        this.mEmpty1 = this.mDelegator.findViewById(16909526);
        this.mEmpty2 = this.mDelegator.findViewById(16909533);
        this.mAmPmMarginInside = this.mDelegator.findViewById(16909530);
        this.mAmPmMarginOutside = this.mDelegator.findViewById(16909532);
        this.mAmPmSpinner = (SemNumberPicker) findViewById;
        this.mAmPmSpinner.setToggle(true);
        this.mAmPmSpinner.setMinValue(0);
        this.mAmPmSpinner.setMaxValue(1);
        this.mAmPmSpinner.setDisplayedValues(this.mAmPmStrings);
        this.mAmPmSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mAmPmSpinner.setOnValueChangedListener(new C03125());
        this.mAmPmSpinnerInput = (EditText) this.mAmPmSpinner.findViewById(16909401);
        this.mAmPmSpinnerInput.setInputType(0);
        this.mAmPmSpinnerInput.setCursorVisible(false);
        this.mAmPmSpinnerInput.setFocusable(false);
        this.mAmPmSpinnerInput.setFocusableInTouchMode(false);
        if (isAmPmAtStart()) {
            ViewGroup viewGroup = (ViewGroup) semTimePicker.findViewById(16909525);
            viewGroup.removeView(this.mAmPmMarginInside);
            viewGroup.removeView(this.mAmPmSpinner);
            viewGroup.removeView(this.mAmPmMarginOutside);
            viewGroup.addView(this.mAmPmMarginInside, 0);
            viewGroup.addView(this.mAmPmSpinner, 0);
            viewGroup.addView(this.mAmPmMarginOutside, 0);
        }
        getHourFormatData();
        updateHourControl();
        updateAmPmControl();
        setHour(this.mTempCalendar.get(11));
        setMinute(this.mTempCalendar.get(12));
        if (!isEnabled()) {
            setEnabled(false);
        }
        if (this.mDelegator.getImportantForAccessibility() == 0) {
            this.mDelegator.setImportantForAccessibility(1);
        }
        setTextWatcher();
        if (resources.getConfiguration().semMobileKeyboardCovered == 1) {
            this.mHourSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            this.mMinuteSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
        }
    }

    public static String[] getAmPmStrings(Context context) {
        String[] strArr = new String[2];
        LocaleData localeData = LocaleData.get(context.getResources().getConfiguration().locale);
        if (isMeaLanguage()) {
            strArr[0] = localeData.amPm[0];
            strArr[1] = localeData.amPm[1];
        } else {
            strArr[0] = localeData.amPm[0].length() > 4 ? localeData.narrowAm : localeData.amPm[0];
            strArr[1] = localeData.amPm[1].length() > 4 ? localeData.narrowPm : localeData.amPm[1];
        }
        return strArr;
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

    private void getHourFormatData() {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int length = bestDateTimePattern.length();
        this.mHourWithTwoDigit = false;
        int i = 0;
        while (i < length) {
            char charAt = bestDateTimePattern.charAt(i);
            if (charAt == 'H' || charAt == 'h' || charAt == 'K' || charAt == 'k') {
                this.mHourFormat = charAt;
                if (i + 1 < length && charAt == bestDateTimePattern.charAt(i + 1)) {
                    this.mHourWithTwoDigit = true;
                    return;
                }
                return;
            }
            i++;
        }
    }

    private boolean isAmPmAtStart() {
        return DateFormat.getBestDateTimePattern(this.mCurrentLocale, "hm").startsWith("a");
    }

    private static boolean isMeaLanguage() {
        String language = Locale.getDefault().getLanguage();
        return "lo".equals(language) || "ar".equals(language) || "fa".equals(language) || "ur".equals(language);
    }

    private void onTimeChanged() {
        this.mDelegator.sendAccessibilityEvent(4);
        if (this.mOnTimeChangedListener != null) {
            this.mOnTimeChangedListener.onTimeChanged(this.mDelegator, getHour(), getMinute());
        }
    }

    private void setCurrentHour(int i, boolean z) {
        if (i == getHour()) {
            if (isMeaLanguage()) {
                this.mHourSpinner.setValue(i);
            }
            return;
        }
        if (!is24HourView()) {
            if (i >= 12) {
                this.mIsAm = false;
                if (i > 12) {
                    i -= 12;
                }
            } else {
                this.mIsAm = true;
                if (i == 0) {
                    i = 12;
                }
            }
            updateAmPmControl();
        }
        this.mHourSpinner.setValue(i);
        if (z) {
            onTimeChanged();
        }
    }

    private void setDividerText() {
        CharSequence charSequence;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(this.mCurrentLocale, this.mIs24HourView ? "Hm" : "hm");
        int lastIndexOf = bestDateTimePattern.lastIndexOf(72);
        if (lastIndexOf == -1) {
            lastIndexOf = bestDateTimePattern.lastIndexOf(104);
        }
        if (lastIndexOf == -1) {
            charSequence = ":";
        } else {
            int indexOf = bestDateTimePattern.indexOf(SemMotionRecognitionEvent.SMART_SCROLL_TILT_FACE_IN_STOP_LAND, lastIndexOf + 1);
            charSequence = indexOf == -1 ? Character.toString(bestDateTimePattern.charAt(lastIndexOf + 1)) : bestDateTimePattern.substring(lastIndexOf + 1, indexOf);
        }
        this.mDivider.setText(charSequence);
        Typeface defaultFromStyle = Typeface.defaultFromStyle(0);
        Typeface create = Typeface.create("sec-roboto-condensed-light", 0);
        if (defaultFromStyle.equals(create)) {
            create = Typeface.create("samsung-neo-num3T", 0);
        }
        String string = System.getString(this.mContext.getContentResolver(), "theme_font_clock");
        if (!(string == null || string.equals(""))) {
            create = getFontTypeface(string);
        }
        this.mDivider.setTypeface(Typeface.create(create, 1));
    }

    private void setTextWatcher() {
        this.mPickerTexts[0] = this.mHourSpinner.getEditText();
        this.mPickerTexts[1] = this.mMinuteSpinner.getEditText();
        this.mPickerTexts[0].addTextChangedListener(new SemTextWatcher(2, 0));
        this.mPickerTexts[1].addTextChangedListener(new SemTextWatcher(2, 1));
        this.mPickerTexts[0].setOnKeyListener(new SemKeyListener());
        this.mPickerTexts[1].setOnKeyListener(new SemKeyListener());
    }

    private void updateAmPmControl() {
        if (is24HourView()) {
            this.mAmPmSpinner.setVisibility(8);
            this.mAmPmMarginInside.setVisibility(8);
            this.mAmPmMarginOutside.setVisibility(8);
            this.mEmpty1.setVisibility(0);
            this.mEmpty2.setVisibility(0);
        } else {
            this.mAmPmSpinner.setValue(this.mIsAm ? 0 : 1);
            this.mAmPmSpinner.setVisibility(0);
            this.mAmPmMarginInside.setVisibility(0);
            this.mAmPmMarginOutside.setVisibility(0);
            this.mEmpty1.setVisibility(8);
            this.mEmpty2.setVisibility(8);
        }
        this.mDelegator.sendAccessibilityEvent(4);
    }

    private void updateHourControl() {
        if (is24HourView()) {
            if (this.mHourFormat == 'k') {
                this.mHourSpinner.setMinValue(1);
                this.mHourSpinner.setMaxValue(24);
            } else {
                this.mHourSpinner.setMinValue(0);
                this.mHourSpinner.setMaxValue(23);
            }
        } else if (this.mHourFormat == 'K') {
            this.mHourSpinner.setMinValue(0);
            this.mHourSpinner.setMaxValue(11);
        } else {
            this.mHourSpinner.setMinValue(1);
            this.mHourSpinner.setMaxValue(12);
        }
        this.mHourSpinner.setFormatter(this.mHourWithTwoDigit ? SemNumberPicker.getTwoDigitFormatter() : null);
    }

    private void updateInputState() {
        InputMethodManager peekInstance = InputMethodManager.peekInstance();
        if (peekInstance == null) {
            return;
        }
        if (peekInstance.isActive(this.mHourSpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mHourSpinnerInput != null) {
                this.mHourSpinnerInput.clearFocus();
            }
        } else if (peekInstance.isActive(this.mMinuteSpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mMinuteSpinnerInput != null) {
                this.mMinuteSpinnerInput.clearFocus();
            }
        } else if (peekInstance.isActive(this.mAmPmSpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            if (this.mAmPmSpinnerInput != null) {
                this.mAmPmSpinnerInput.clearFocus();
            }
        }
    }

    private void updateModeState(SemNumberPicker semNumberPicker, boolean z) {
        if (!(this.mIsEditTextMode == z || z)) {
            if (this.mHourSpinner.isEditTextMode()) {
                this.mHourSpinner.setEditTextMode(false);
            }
            if (this.mMinuteSpinner.isEditTextMode()) {
                this.mMinuteSpinner.setEditTextMode(false);
            }
            if (this.mAmPmSpinner.isEditTextMode()) {
                this.mAmPmSpinner.setEditTextMode(false);
            }
        }
    }

    private void validCheck() {
        if (this.mIsEditTextMode) {
            if (this.mHourSpinnerInput != null && this.mHourSpinnerInput.hasFocus()) {
                if (!TextUtils.isEmpty(this.mHourSpinnerInput.getText())) {
                    int parseInt = Integer.parseInt(String.valueOf(this.mHourSpinnerInput.getText()));
                    if (!is24HourView()) {
                        if (!this.mIsAm && parseInt != 12) {
                            parseInt += 12;
                        } else if (this.mIsAm && parseInt == 12) {
                            parseInt = 0;
                        }
                    }
                    setHour(parseInt);
                    this.mHourSpinnerInput.selectAll();
                } else {
                    return;
                }
            }
            if (this.mMinuteSpinnerInput != null && this.mMinuteSpinnerInput.hasFocus() && !TextUtils.isEmpty(this.mMinuteSpinnerInput.getText())) {
                setMinute(Integer.parseInt(String.valueOf(this.mMinuteSpinnerInput.getText())));
                this.mMinuteSpinnerInput.selectAll();
            }
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public int getBaseline() {
        return this.mHourSpinner.getBaseline();
    }

    public int getDefaultHeight() {
        return this.mDefaultHeight;
    }

    public int getDefaultWidth() {
        return this.mDefaultWidth;
    }

    public EditText getEditText(int i) {
        return i == 0 ? this.mHourSpinner.getEditText() : i == 1 ? this.mMinuteSpinner.getEditText() : i == 2 ? this.mAmPmSpinner.getEditText() : this.mMinuteSpinner.getEditText();
    }

    public int getHour() {
        int value = this.mHourSpinner.getValue();
        return is24HourView() ? value : this.mIsAm ? value % 12 : (value % 12) + 12;
    }

    public int getMinute() {
        return this.mMinuteSpinner.getValue();
    }

    public SemNumberPicker getNumberPicker(int i) {
        return i == 0 ? this.mHourSpinner : i == 1 ? this.mMinuteSpinner : i == 2 ? this.mAmPmSpinner : this.mMinuteSpinner;
    }

    public boolean is24HourView() {
        return this.mIs24HourView;
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void onConfigurationChanged(Configuration configuration) {
        setCurrentLocale(configuration.locale);
        if (configuration.semMobileKeyboardCovered == 1) {
            this.mHourSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            this.mMinuteSpinnerInput.setPrivateImeOptions("inputType=disableMobileCMKey");
            return;
        }
        this.mHourSpinnerInput.setPrivateImeOptions("inputType=YearDateTime_edittext");
        this.mMinuteSpinnerInput.setPrivateImeOptions("inputType=YearDateTime_edittext");
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.setClassName(TimePicker.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        accessibilityNodeInfo.setClassName(TimePicker.class.getName());
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int i = this.mIs24HourView ? 129 : 65;
        this.mTempCalendar.set(11, getHour());
        this.mTempCalendar.set(12, getMinute());
        accessibilityEvent.getText().add(DateUtils.formatDateTime(this.mContext, this.mTempCalendar.getTimeInMillis(), i));
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        setHour(savedState.getHour());
        setMinute(savedState.getMinute());
    }

    public Parcelable onSaveInstanceState(Parcelable parcelable) {
        return new SavedState(parcelable, getHour(), getMinute());
    }

    public void setCurrentLocale(Locale locale) {
        super.setCurrentLocale(locale);
        this.mTempCalendar = Calendar.getInstance(locale);
    }

    public void setEditTextMode(boolean z) {
        if (this.mIsEditTextMode != z) {
            this.mIsEditTextMode = z;
            InputMethodManager peekInstance = InputMethodManager.peekInstance();
            this.mAmPmSpinner.setEditTextMode(z);
            this.mHourSpinner.setEditTextMode(z);
            this.mMinuteSpinner.setEditTextMode(z);
            if (this.mIsEditTextMode) {
                if (!(peekInstance == null || peekInstance.isInputMethodShown())) {
                    peekInstance.showSoftInput(this.mHourSpinnerInput, 0);
                }
            } else if (peekInstance != null) {
                peekInstance.hideSoftInputFromWindow(this.mDelegator.getWindowToken(), 0);
            }
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDelegator, z);
            }
        }
    }

    public void setEnabled(boolean z) {
        this.mMinuteSpinner.setEnabled(z);
        if (this.mDivider != null) {
            this.mDivider.setEnabled(z);
        }
        this.mHourSpinner.setEnabled(z);
        this.mAmPmSpinner.setEnabled(z);
        this.mIsEnabled = z;
    }

    public void setHour(int i) {
        setCurrentHour(i, true);
    }

    public void setIs24HourView(Boolean bool) {
        if (this.mIs24HourView != bool.booleanValue()) {
            int hour = getHour();
            this.mIs24HourView = bool.booleanValue();
            getHourFormatData();
            updateHourControl();
            setCurrentHour(hour, false);
            updateAmPmControl();
        }
    }

    public void setMinute(int i) {
        if (i == getMinute()) {
            if (isMeaLanguage()) {
                this.mMinuteSpinner.setValue(i);
            }
            return;
        }
        this.mMinuteSpinner.setValue(i);
        onTimeChanged();
    }

    public void setOnEditTextModeChangedListener(SemTimePicker.OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mOnTimeChangedListener = onTimeChangedListener;
    }

    public void startAnimation(int i, SemAnimationListener semAnimationListener) {
        if (isAmPmAtStart()) {
            this.mAmPmSpinner.startAnimation(i, null);
            this.mHourSpinner.startAnimation(i + 55, null);
            this.mMinuteSpinner.startAnimation(i + 110, semAnimationListener);
            return;
        }
        this.mHourSpinner.startAnimation(i, null);
        this.mMinuteSpinner.startAnimation(i + 55, semAnimationListener);
        this.mAmPmSpinner.startAnimation(i + 110, null);
    }
}
