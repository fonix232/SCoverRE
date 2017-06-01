package com.samsung.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.widget.SemDatePicker.OnDateChangedListener;
import com.samsung.android.widget.SemNumberPicker.OnEditTextModeChangedListener;
import com.samsung.android.widget.SemNumberPicker.OnValueChangeListener;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import libcore.icu.ICU;

class SemDatePickerSpinnerLayout extends LinearLayout {
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String TAG = "SemDatePickerSpinnerLayout";
    private static final boolean sem_DEBUG = false;
    private final int FORMAT_DDMMYYYY;
    private final int FORMAT_MMDDYYYY;
    private final int FORMAT_YYYYDDMM;
    private final int FORMAT_YYYYMMDD;
    private final int PICKER_DAY;
    private final int PICKER_MONTH;
    private final int PICKER_YEAR;
    private boolean isMonthJan;
    private Calendar mCurrentDate;
    private Locale mCurrentLocale;
    private final DateFormat mDateFormat;
    private SemDatePicker mDatePicker;
    private final SemNumberPicker mDaySpinner;
    private final EditText mDaySpinnerInput;
    private OnEditorActionListener mEditorActionListener;
    private Method mGetDayLengthOfMethod;
    private boolean mIsEditTextMode;
    private boolean mIsLeapMonth;
    private Method mIsLeapMonthMethod;
    private boolean mIsLunar;
    private int mLunarCurrentDay;
    private int mLunarCurrentMonth;
    private int mLunarCurrentYear;
    private int mLunarTempDay;
    private int mLunarTempMonth;
    private int mLunarTempYear;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private OnEditTextModeChangedListener mModeChangeListener;
    private final SemNumberPicker mMonthSpinner;
    private final EditText mMonthSpinnerInput;
    private int mNumberOfMonths;
    private int mNumberTextSize;
    private OnDateChangedListener mOnDateChangedListener;
    private SemDatePicker.OnEditTextModeChangedListener mOnEditTextModeChangedListener;
    private OnSpinnerDateChangedListener mOnSpinnerDateChangedListener;
    PathClassLoader mPathClassLoader;
    private EditText[] mPickerTexts;
    private final View mPrimaryEmptyView;
    private final View mSecondaryEmptyView;
    private String[] mShortMonths;
    private Object mSolarLunarTables;
    private final LinearLayout mSpinners;
    private int mSubNumberTextSize;
    private int mSubTextSize;
    private Calendar mTempDate;
    private int mTextSize;
    private String mToastText;
    private OnEditorActionListener mYearEditorActionListener;
    private final SemNumberPicker mYearSpinner;
    private final EditText mYearSpinnerInput;

    public interface OnSpinnerDateChangedListener {
        void onDateChanged(SemDatePickerSpinnerLayout semDatePickerSpinnerLayout, int i, int i2, int i3);
    }

    class C02771 implements OnEditTextModeChangedListener {
        C02771() {
        }

        public void onEditTextModeChanged(SemNumberPicker semNumberPicker, boolean z) {
            SemDatePickerSpinnerLayout.this.setEditTextMode(z);
            SemDatePickerSpinnerLayout.this.updateModeState(semNumberPicker, z);
        }
    }

    class C02782 implements OnEditorActionListener {
        C02782() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 9999) {
                SemDatePickerSpinnerLayout.this.twLog("EditorAction 9999 arrived");
                SemDatePickerSpinnerLayout.this.isMonthJan = true;
            }
            if (i == 6) {
                SemDatePickerSpinnerLayout.this.updateInputState();
                SemDatePickerSpinnerLayout.this.setEditTextMode(false);
            }
            return false;
        }
    }

    class C02793 implements OnEditorActionListener {
        C02793() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6) {
                SemDatePickerSpinnerLayout.this.updateInputState();
                SemDatePickerSpinnerLayout.this.setEditTextMode(false);
            }
            return false;
        }
    }

    class C02804 implements OnValueChangeListener {
        C02804() {
        }

        public void onValueChange(SemNumberPicker semNumberPicker, int i, int i2) {
            int actualMaximum;
            SemDatePickerSpinnerLayout.this.mTempDate.setTimeInMillis(SemDatePickerSpinnerLayout.this.mCurrentDate.getTimeInMillis());
            if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                SemDatePickerSpinnerLayout.this.mLunarTempYear = SemDatePickerSpinnerLayout.this.mLunarCurrentYear;
                SemDatePickerSpinnerLayout.this.mLunarTempMonth = SemDatePickerSpinnerLayout.this.mLunarCurrentMonth;
                SemDatePickerSpinnerLayout.this.mLunarTempDay = SemDatePickerSpinnerLayout.this.mLunarCurrentDay;
            }
            boolean z = false;
            boolean z2 = false;
            SemDatePickerSpinnerLayout semDatePickerSpinnerLayout;
            if (semNumberPicker == SemDatePickerSpinnerLayout.this.mDaySpinner) {
                actualMaximum = SemDatePickerSpinnerLayout.this.mTempDate.getActualMaximum(5);
                if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                    actualMaximum = SemDatePickerSpinnerLayout.this.getLunarMaxDayOfMonth(SemDatePickerSpinnerLayout.this.mTempDate.get(1), SemDatePickerSpinnerLayout.this.mTempDate.get(2), SemDatePickerSpinnerLayout.this.mIsLeapMonth);
                }
                if (i == actualMaximum && i2 == 1) {
                    SemDatePickerSpinnerLayout.this.mTempDate.set(5, i2);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        SemDatePickerSpinnerLayout.this.mLunarTempDay = i2;
                    }
                } else if (i == 1 && i2 == actualMaximum) {
                    SemDatePickerSpinnerLayout.this.mTempDate.set(5, i2);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        SemDatePickerSpinnerLayout.this.mLunarTempDay = i2;
                    }
                } else {
                    SemDatePickerSpinnerLayout.this.mTempDate.add(5, i2 - i);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        semDatePickerSpinnerLayout = SemDatePickerSpinnerLayout.this;
                        semDatePickerSpinnerLayout.mLunarTempDay = semDatePickerSpinnerLayout.mLunarTempDay + (i2 - i);
                    }
                }
            } else if (semNumberPicker == SemDatePickerSpinnerLayout.this.mMonthSpinner) {
                if (i == 11 && i2 == 0) {
                    SemDatePickerSpinnerLayout.this.mTempDate.set(2, i2);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        SemDatePickerSpinnerLayout.this.mLunarTempMonth = i2;
                    }
                } else if (i == 0 && i2 == 11) {
                    SemDatePickerSpinnerLayout.this.mTempDate.set(2, i2);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        SemDatePickerSpinnerLayout.this.mLunarTempMonth = i2;
                    }
                } else {
                    SemDatePickerSpinnerLayout.this.mTempDate.add(2, i2 - i);
                    if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                        semDatePickerSpinnerLayout = SemDatePickerSpinnerLayout.this;
                        semDatePickerSpinnerLayout.mLunarTempMonth = semDatePickerSpinnerLayout.mLunarTempMonth + (i2 - i);
                    }
                }
                z2 = true;
            } else if (semNumberPicker == SemDatePickerSpinnerLayout.this.mYearSpinner) {
                SemDatePickerSpinnerLayout.this.mTempDate.add(1, i2 - i);
                if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                    semDatePickerSpinnerLayout = SemDatePickerSpinnerLayout.this;
                    semDatePickerSpinnerLayout.mLunarTempYear = semDatePickerSpinnerLayout.mLunarTempYear + (i2 - i);
                }
                z = true;
                z2 = true;
            } else {
                throw new IllegalArgumentException();
            }
            if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                actualMaximum = SemDatePickerSpinnerLayout.this.getLunarMaxDayOfMonth(SemDatePickerSpinnerLayout.this.mLunarTempYear, SemDatePickerSpinnerLayout.this.mLunarTempMonth, SemDatePickerSpinnerLayout.this.mIsLeapMonth);
                if (SemDatePickerSpinnerLayout.this.mLunarTempMonth > actualMaximum) {
                    SemDatePickerSpinnerLayout.this.mLunarTempDay = actualMaximum;
                }
                if (SemDatePickerSpinnerLayout.this.mIsLeapMonth) {
                    Boolean -wrap2 = SemDatePickerSpinnerLayout.this.invoke(SemDatePickerSpinnerLayout.this.mSolarLunarTables, SemDatePickerSpinnerLayout.this.mIsLeapMonthMethod, Integer.valueOf(SemDatePickerSpinnerLayout.this.mLunarTempYear), Integer.valueOf(SemDatePickerSpinnerLayout.this.mLunarTempMonth));
                    if ((-wrap2 instanceof Boolean) && !-wrap2.booleanValue()) {
                        SemDatePickerSpinnerLayout.this.mIsLeapMonth = false;
                    }
                }
            }
            int i3 = SemDatePickerSpinnerLayout.this.mTempDate.get(1);
            int i4 = SemDatePickerSpinnerLayout.this.mTempDate.get(2);
            int i5 = SemDatePickerSpinnerLayout.this.mTempDate.get(5);
            if (SemDatePickerSpinnerLayout.this.mIsLunar) {
                i3 = SemDatePickerSpinnerLayout.this.mLunarTempYear;
                i4 = SemDatePickerSpinnerLayout.this.mLunarTempMonth;
                i5 = SemDatePickerSpinnerLayout.this.mLunarTempDay;
            }
            SemDatePickerSpinnerLayout.this.setDate(i3, i4, i5);
            if (null != null || z || z2) {
                SemDatePickerSpinnerLayout.this.updateSpinners(false, false, z, z2);
            }
            SemDatePickerSpinnerLayout.this.notifyDateChanged(true);
        }
    }

    private class TwKeyListener implements OnKeyListener {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            SemDatePickerSpinnerLayout.this.twLog(keyEvent.toString());
            if (keyEvent.getAction() != 1) {
                return false;
            }
            switch (i) {
                case 23:
                    if (SemDatePickerSpinnerLayout.this.getResources().getConfiguration().keyboard == 3) {
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

    public class TwTextWatcher implements TextWatcher {
        private int changedLen = 0;
        private boolean isMonth;
        private int mCheck;
        private int mId;
        private int mMaxLen;
        private int mNext;
        private String prevText;

        public TwTextWatcher(int i, int i2, boolean z) {
            this.mMaxLen = i;
            this.mId = i2;
            this.isMonth = z;
            this.mCheck = this.mId - 1;
            if (this.mCheck < 0) {
                this.mCheck = 2;
            }
            this.mNext = this.mId + 1 >= 3 ? -1 : this.mId + 1;
        }

        private void changeFocus() {
            if (!AccessibilityManager.getInstance(SemDatePickerSpinnerLayout.this.mContext).isTouchExplorationEnabled()) {
                SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "changeFocus() mNext : " + this.mNext + ", mCheck : " + this.mCheck);
                if (this.mNext >= 0) {
                    if (!SemDatePickerSpinnerLayout.this.mPickerTexts[this.mCheck].isFocused()) {
                        SemDatePickerSpinnerLayout.this.mPickerTexts[this.mNext].requestFocus();
                    }
                    if (SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused()) {
                        SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].clearFocus();
                    }
                }
            }
        }

        private boolean isFarsiLanguage() {
            return "fa".equals(SemDatePickerSpinnerLayout.this.mCurrentLocale.getLanguage());
        }

        private boolean isMeaLanguage() {
            String language = SemDatePickerSpinnerLayout.this.mCurrentLocale.getLanguage();
            return "ar".equals(language) || "fa".equals(language) || "ur".equals(language);
        }

        private boolean isMonthStr(String str) {
            boolean z = false;
            for (int i = 0; i < SemDatePickerSpinnerLayout.this.mNumberOfMonths; i++) {
                if (str.equals(SemDatePickerSpinnerLayout.this.mShortMonths[i])) {
                    z = true;
                }
            }
            return z;
        }

        private boolean isNumericStr(CharSequence charSequence) {
            return Character.isDigit(charSequence.charAt(0));
        }

        private boolean isSwaLanguage() {
            String language = SemDatePickerSpinnerLayout.this.mCurrentLocale.getLanguage();
            return "hi".equals(language) || "ta".equals(language) || "ml".equals(language) || "te".equals(language) || "or".equals(language) || "ne".equals(language) || "as".equals(language) || "bn".equals(language) || "gu".equals(language) || "si".equals(language) || "pa".equals(language) || "kn".equals(language) || "mr".equals(language);
        }

        private void showInvalidValueEnteredToast(String str, int i) {
            SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].setText(str);
            if (i != 0) {
                SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].setSelection(i);
            }
            Toast.makeText(SemDatePickerSpinnerLayout.this.mContext, SemDatePickerSpinnerLayout.this.mToastText, 0).show();
        }

        public void afterTextChanged(Editable editable) {
            SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "aftertextchanged: " + editable.toString());
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "beforeTextChanged: " + charSequence + ", " + i + ", " + i2 + ", " + i3);
            this.prevText = charSequence.toString();
            this.changedLen = i3;
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "onTextChanged: " + this.prevText + " -> " + charSequence);
            String str = (String) SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].getTag();
            if (str == null || !(str.equals("onClick") || str.equals("onLongClick"))) {
                if (!this.isMonth) {
                    if (this.prevText.length() < charSequence.length() && charSequence.length() == this.mMaxLen && this.changedLen == 1 && SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused()) {
                        if (this.mMaxLen < 3) {
                            if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mDaySpinner.getMinValue()) {
                                if (Character.getNumericValue(charSequence.toString().charAt(0)) < 4) {
                                    showInvalidValueEnteredToast(Character.toString(charSequence.toString().charAt(0)), 1);
                                } else {
                                    showInvalidValueEnteredToast("", 0);
                                }
                                return;
                            }
                        } else if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mYearSpinner.getMinValue() || Integer.parseInt(charSequence.toString()) > SemDatePickerSpinnerLayout.this.mYearSpinner.getMaxValue()) {
                            showInvalidValueEnteredToast(charSequence.toString().substring(0, 3), 3);
                            return;
                        }
                        changeFocus();
                    }
                    if (this.changedLen == 1 && SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused()) {
                        if (this.mMaxLen >= 3) {
                            int length = charSequence.length();
                            int pow = (int) (1000.0d / Math.pow(10.0d, (double) (length - 1)));
                            String str2 = "";
                            if (length != 1) {
                                str2 = charSequence.toString().substring(0, length - 1);
                            }
                            if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mYearSpinner.getMinValue() / pow || Integer.parseInt(charSequence.toString()) > SemDatePickerSpinnerLayout.this.mYearSpinner.getMaxValue() / pow) {
                                showInvalidValueEnteredToast(str2, length - 1);
                                return;
                            }
                        } else if ((SemDatePickerSpinnerLayout.this.mDaySpinner.getMinValue() >= 10 && charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE)) || ((SemDatePickerSpinnerLayout.this.mDaySpinner.getMinValue() >= 20 && (charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE) || charSequence.toString().equals(SmartFaceManager.PAGE_BOTTOM))) || (SemDatePickerSpinnerLayout.this.mDaySpinner.getMinValue() >= 30 && (charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE) || charSequence.toString().equals(SmartFaceManager.PAGE_BOTTOM) || charSequence.toString().equals("2"))))) {
                            showInvalidValueEnteredToast("", 0);
                            return;
                        } else if (!(charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE) || charSequence.toString().equals(SmartFaceManager.PAGE_BOTTOM) || charSequence.toString().equals("2") || charSequence.toString().equals("3"))) {
                            if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mDaySpinner.getMinValue()) {
                                showInvalidValueEnteredToast("", 0);
                                return;
                            }
                            changeFocus();
                        }
                    }
                } else if (SemDatePickerSpinnerLayout.this.usingNumericMonths()) {
                    SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "Samsung Keypad Num Month");
                    if (this.changedLen == 1) {
                        if (charSequence.length() == this.mMaxLen) {
                            if (SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused()) {
                                if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mMonthSpinner.getMinValue()) {
                                    if (Character.getNumericValue(charSequence.toString().charAt(0)) < 2) {
                                        showInvalidValueEnteredToast(Character.toString(charSequence.toString().charAt(0)), 1);
                                    } else {
                                        showInvalidValueEnteredToast("", 0);
                                    }
                                    return;
                                }
                                changeFocus();
                            }
                        } else if (charSequence.length() > 0) {
                            if (SemDatePickerSpinnerLayout.this.mMonthSpinner.getMinValue() >= 10 && charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE) && SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused()) {
                                showInvalidValueEnteredToast("", 0);
                                return;
                            } else if (!(charSequence.toString().equals(SmartFaceManager.PAGE_BOTTOM) || charSequence.toString().equals(SmartFaceManager.PAGE_MIDDLE) || !SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused())) {
                                if (Integer.parseInt(charSequence.toString()) < SemDatePickerSpinnerLayout.this.mMonthSpinner.getMinValue()) {
                                    showInvalidValueEnteredToast("", 0);
                                    return;
                                }
                                changeFocus();
                            }
                        }
                    }
                } else if (!(!SemDatePickerSpinnerLayout.this.mPickerTexts[this.mId].isFocused() || this.prevText.equals(SmartFaceManager.PAGE_MIDDLE) || this.prevText.equals(SmartFaceManager.PAGE_BOTTOM) || this.prevText.equals("2") || this.prevText.equals("3") || this.prevText.equals("4") || this.prevText.equals("5") || this.prevText.equals("6") || this.prevText.equals("7") || this.prevText.equals("8") || this.prevText.equals("9") || this.prevText.equals("10") || this.prevText.equals("11"))) {
                    if (charSequence.length() >= this.mMaxLen) {
                        if (!isMeaLanguage()) {
                            changeFocus();
                        } else if (TextUtils.isEmpty(this.prevText) && isMonthStr(charSequence.toString())) {
                            changeFocus();
                        }
                    } else if ((isSwaLanguage() || isFarsiLanguage()) && charSequence.length() > 0 && !isNumericStr(charSequence)) {
                        changeFocus();
                    }
                }
                return;
            }
            SemDatePickerSpinnerLayout.this.twLog("[" + this.mId + "] " + "TAG exists: " + str);
        }
    }

    public SemDatePickerSpinnerLayout(Context context) {
        this(context, null);
    }

    public SemDatePickerSpinnerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843612);
    }

    public SemDatePickerSpinnerLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SemDatePickerSpinnerLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.mModeChangeListener = new C02771();
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mPathClassLoader = null;
        this.mPickerTexts = new EditText[3];
        this.isMonthJan = false;
        this.PICKER_DAY = 0;
        this.PICKER_MONTH = 1;
        this.PICKER_YEAR = 2;
        this.FORMAT_MMDDYYYY = 0;
        this.FORMAT_DDMMYYYY = 1;
        this.FORMAT_YYYYMMDD = 2;
        this.FORMAT_YYYYDDMM = 3;
        this.mEditorActionListener = new C02782();
        this.mYearEditorActionListener = new C02793();
        LayoutInflater.from(this.mContext).inflate(17367295, this, true);
        this.mCurrentLocale = Locale.getDefault();
        setCurrentLocale(this.mCurrentLocale);
        OnValueChangeListener c02804 = new C02804();
        this.mSpinners = (LinearLayout) findViewById(16909497);
        this.mPrimaryEmptyView = findViewById(16909499);
        this.mSecondaryEmptyView = findViewById(16909501);
        this.mDaySpinner = (SemNumberPicker) findViewById(16909498);
        this.mDaySpinnerInput = (EditText) this.mDaySpinner.findViewById(16909401);
        this.mDaySpinner.setFormatter(SemNumberPicker.getTwoDigitFormatter());
        this.mDaySpinner.setOnValueChangedListener(c02804);
        this.mDaySpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mDaySpinner.setMaxInputLength(2);
        this.mDaySpinner.setYearDateTimeInputMode();
        this.mMonthSpinner = (SemNumberPicker) findViewById(16909500);
        this.mMonthSpinnerInput = (EditText) this.mMonthSpinner.findViewById(16909401);
        if (usingNumericMonths()) {
            this.mMonthSpinner.setMinValue(1);
            this.mMonthSpinner.setMaxValue(12);
            this.mMonthSpinner.setYearDateTimeInputMode();
            this.mMonthSpinner.setMaxInputLength(2);
        } else {
            this.mMonthSpinner.setMinValue(0);
            this.mMonthSpinner.setMaxValue(this.mNumberOfMonths - 1);
            this.mMonthSpinner.setFormatter(null);
            this.mMonthSpinner.setDisplayedValues(this.mShortMonths);
            this.mMonthSpinnerInput.setInputType(1);
            this.mMonthSpinner.setMonthInputMode();
        }
        this.mMonthSpinner.setOnValueChangedListener(c02804);
        this.mMonthSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mYearSpinner = (SemNumberPicker) findViewById(16909502);
        this.mYearSpinnerInput = (EditText) this.mYearSpinner.findViewById(16909401);
        this.mYearSpinner.setOnValueChangedListener(c02804);
        this.mYearSpinner.setOnEditTextModeChangedListener(this.mModeChangeListener);
        this.mYearSpinner.setMaxInputLength(4);
        this.mYearSpinner.setYearDateTimeInputMode();
        this.mDaySpinner.setTextTypeface(Typeface.create("sec-roboto-condensed-light", 0));
        this.mMonthSpinner.setTextTypeface(Typeface.create("sec-roboto-condensed-light", 0));
        this.mYearSpinner.setTextTypeface(Typeface.create("sec-roboto-condensed-light", 0));
        Resources resources = context.getResources();
        this.mNumberTextSize = resources.getInteger(17105705);
        this.mSubNumberTextSize = resources.getInteger(17105706);
        this.mTextSize = this.mNumberTextSize - 2;
        this.mSubTextSize = this.mSubNumberTextSize;
        this.mToastText = resources.getString(17041660);
        String language = this.mCurrentLocale.getLanguage();
        if ("my".equals(language) || "ml".equals(language) || "bn".equals(language) || "ar".equals(language) || "fa".equals(language)) {
            this.mTextSize = resources.getInteger(17105707);
            this.mSubTextSize = resources.getInteger(17105708);
        } else if ("ga".equals(language)) {
            this.mTextSize = resources.getInteger(17105707) - 1;
            this.mSubTextSize = resources.getInteger(17105708) - 1;
        }
        this.mDaySpinner.setTextSize((float) this.mNumberTextSize);
        this.mYearSpinner.setTextSize((float) this.mNumberTextSize);
        this.mDaySpinner.setSubTextSize((float) this.mSubNumberTextSize);
        this.mYearSpinner.setSubTextSize((float) this.mSubNumberTextSize);
        if (usingNumericMonths()) {
            this.mMonthSpinner.setTextSize((float) this.mNumberTextSize);
            this.mMonthSpinner.setSubTextSize((float) this.mSubNumberTextSize);
        } else {
            this.mMonthSpinner.setTextSize((float) this.mTextSize);
            this.mMonthSpinner.setSubTextSize((float) this.mSubTextSize);
        }
        this.mDaySpinner.setPickerContentDescription(context.getResources().getString(17041655));
        this.mMonthSpinner.setPickerContentDescription(context.getResources().getString(17041656));
        this.mYearSpinner.setPickerContentDescription(context.getResources().getString(17041657));
        this.mCurrentDate.setTimeInMillis(System.currentTimeMillis());
        init(this.mCurrentDate.get(1), this.mCurrentDate.get(2), this.mCurrentDate.get(5), null);
        reorderSpinners();
    }

    private Calendar getCalendarForLocale(Calendar calendar, Locale locale) {
        if (calendar == null) {
            return Calendar.getInstance(locale);
        }
        long timeInMillis = calendar.getTimeInMillis();
        Calendar instance = Calendar.getInstance(locale);
        instance.setTimeInMillis(timeInMillis);
        return instance;
    }

    private int getLunarMaxDayOfMonth(int i, int i2, boolean z) {
        if (this.mSolarLunarTables == null) {
            return 0;
        }
        Integer invoke = invoke(this.mSolarLunarTables, this.mGetDayLengthOfMethod, Integer.valueOf(i), Integer.valueOf(i2), Boolean.valueOf(z));
        int i3 = 0;
        if (invoke instanceof Integer) {
            i3 = invoke.intValue();
        }
        return i3;
    }

    private <T> Method getMethod(Class<T> cls, String str, Class<?>... clsArr) {
        Method method = null;
        try {
            method = cls.getMethod(str, clsArr);
        } catch (Throwable e) {
            Log.e(TAG, str + " NoSuchMethodException", e);
        }
        return method;
    }

    private Object invoke(Object obj, Method method, Object... objArr) {
        if (method == null) {
            Log.e(TAG, "method is null");
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (Throwable e) {
            Log.e(TAG, method.getName() + " IllegalAccessException", e);
            return null;
        } catch (Throwable e2) {
            Log.e(TAG, method.getName() + " IllegalArgumentException", e2);
            return null;
        } catch (Throwable e3) {
            Log.e(TAG, method.getName() + " InvocationTargetException", e3);
            return null;
        }
    }

    private boolean isNewDate(int i, int i2, int i3) {
        return (this.mCurrentDate.get(1) == i && this.mCurrentDate.get(2) == i2 && this.mCurrentDate.get(5) == i3) ? false : true;
    }

    private void notifyDateChanged(boolean z) {
        sendAccessibilityEvent(4);
        if (this.mOnSpinnerDateChangedListener != null && z) {
            this.mOnSpinnerDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDayOfMonth());
        }
    }

    private boolean parseDate(String str, Calendar calendar) {
        try {
            calendar.setTime(this.mDateFormat.parse(str));
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void reorderSpinners() {
        this.mSpinners.removeAllViews();
        char[] dateFormatOrder = ICU.getDateFormatOrder(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd"));
        int length = dateFormatOrder.length;
        for (int i = 0; i < length; i++) {
            switch (dateFormatOrder[i]) {
                case SemMotionRecognitionEvent.CALLPOSE_R /*77*/:
                    this.mSpinners.addView(this.mMonthSpinner);
                    setImeOptions(this.mMonthSpinner, length, i);
                    break;
                case 'd':
                    this.mSpinners.addView(this.mDaySpinner);
                    setImeOptions(this.mDaySpinner, length, i);
                    break;
                case FingerprintEvent.STATUS_IDENTIFY_FAILURE_SERVICE_FAILURE /*121*/:
                    this.mSpinners.addView(this.mYearSpinner);
                    setImeOptions(this.mYearSpinner, length, i);
                    break;
                default:
                    throw new IllegalArgumentException(Arrays.toString(dateFormatOrder));
            }
            switch (i) {
                case 0:
                    this.mSpinners.addView(this.mPrimaryEmptyView);
                    break;
                case 1:
                    this.mSpinners.addView(this.mSecondaryEmptyView);
                    break;
                default:
                    break;
            }
        }
        char c = dateFormatOrder[0];
        char c2 = dateFormatOrder[1];
        if (c == 'M') {
            setTextWatcher(0);
        } else if (c == 'd') {
            setTextWatcher(1);
        } else if (c == 'y' && c2 == 'd') {
            setTextWatcher(3);
        } else if (c == 'y') {
            setTextWatcher(2);
        }
    }

    private void setDate(int i, int i2, int i3) {
        this.mCurrentDate.set(i, i2, i3);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = i;
            this.mLunarCurrentMonth = i2;
            this.mLunarCurrentDay = i3;
        }
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        } else if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
    }

    private void setImeOptions(SemNumberPicker semNumberPicker, int i, int i2) {
        ((TextView) semNumberPicker.findViewById(16909401)).setImeOptions(i2 < i + -1 ? 33554437 : 33554438);
    }

    private void setTextWatcher(int i) {
        twLog("setTextWatcher() usingNumericMonths  : " + usingNumericMonths() + "format  : " + i);
        switch (i) {
            case 0:
                this.mPickerTexts[0] = this.mMonthSpinner.getEditText();
                this.mPickerTexts[1] = this.mDaySpinner.getEditText();
                this.mPickerTexts[2] = this.mYearSpinner.getEditText();
                if (usingNumericMonths()) {
                    this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(2, 0, true));
                    this.mPickerTexts[0].setOnEditorActionListener(this.mEditorActionListener);
                } else {
                    this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(3, 0, true));
                }
                this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, false));
                this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(4, 2, false));
                this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                break;
            case 1:
                this.mPickerTexts[0] = this.mDaySpinner.getEditText();
                this.mPickerTexts[1] = this.mMonthSpinner.getEditText();
                this.mPickerTexts[2] = this.mYearSpinner.getEditText();
                this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(2, 0, false));
                if (usingNumericMonths()) {
                    this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, true));
                    this.mPickerTexts[1].setOnEditorActionListener(this.mEditorActionListener);
                } else {
                    this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(3, 1, true));
                }
                this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(4, 2, false));
                this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                break;
            case 2:
                this.mPickerTexts[0] = this.mYearSpinner.getEditText();
                this.mPickerTexts[1] = this.mMonthSpinner.getEditText();
                this.mPickerTexts[2] = this.mDaySpinner.getEditText();
                this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(4, 0, false));
                if (usingNumericMonths()) {
                    this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, true));
                    this.mPickerTexts[1].setOnEditorActionListener(this.mEditorActionListener);
                } else {
                    this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(3, 1, true));
                }
                this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(2, 2, false));
                this.mPickerTexts[2].setOnEditorActionListener(this.mYearEditorActionListener);
                break;
            case 3:
                this.mPickerTexts[0] = this.mYearSpinner.getEditText();
                this.mPickerTexts[1] = this.mDaySpinner.getEditText();
                this.mPickerTexts[2] = this.mMonthSpinner.getEditText();
                this.mPickerTexts[0].addTextChangedListener(new TwTextWatcher(4, 0, false));
                this.mPickerTexts[1].addTextChangedListener(new TwTextWatcher(2, 1, false));
                if (!usingNumericMonths()) {
                    this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(3, 2, true));
                    break;
                }
                this.mPickerTexts[2].addTextChangedListener(new TwTextWatcher(2, 2, true));
                this.mPickerTexts[2].setOnEditorActionListener(this.mEditorActionListener);
                break;
        }
        this.mPickerTexts[0].setOnKeyListener(new TwKeyListener());
        this.mPickerTexts[1].setOnKeyListener(new TwKeyListener());
        this.mPickerTexts[2].setOnKeyListener(new TwKeyListener());
    }

    private void trySetContentDescription(View view, int i, int i2) {
        View findViewById = view.findViewById(i);
        if (findViewById != null) {
            findViewById.setContentDescription(this.mContext.getString(i2));
        }
    }

    private void twLog(String str) {
    }

    private void updateModeState(SemNumberPicker semNumberPicker, boolean z) {
        if (!(this.mIsEditTextMode == z || z)) {
            if (this.mDaySpinner.isEditTextMode()) {
                this.mDaySpinner.setEditTextMode(false);
            }
            if (this.mMonthSpinner.isEditTextMode()) {
                this.mMonthSpinner.setEditTextMode(false);
            }
            if (this.mYearSpinner.isEditTextMode()) {
                this.mYearSpinner.setEditTextMode(false);
            }
        }
    }

    private void updateSpinners(boolean z, boolean z2, boolean z3, boolean z4) {
        int i;
        int i2;
        if (z2) {
            this.mYearSpinner.setMinValue(this.mMinDate.get(1));
            this.mYearSpinner.setMaxValue(this.mMaxDate.get(1));
            this.mYearSpinner.setWrapSelectorWheel(false);
        }
        if (z3) {
            int i3;
            int i4;
            if (this.mMaxDate.get(1) - this.mMinDate.get(1) == 0) {
                i3 = this.mMinDate.get(2);
                i4 = this.mMaxDate.get(2);
            } else {
                i = this.mCurrentDate.get(1);
                if (this.mIsLunar) {
                    i = this.mLunarCurrentYear;
                }
                if (i == this.mMinDate.get(1)) {
                    i3 = this.mMinDate.get(2);
                    i4 = 11;
                } else if (i == this.mMaxDate.get(1)) {
                    i3 = 0;
                    i4 = this.mMaxDate.get(2);
                } else {
                    i3 = 0;
                    i4 = 11;
                }
            }
            if (usingNumericMonths()) {
                i3++;
                i4++;
            }
            this.mMonthSpinner.setDisplayedValues(null);
            this.mMonthSpinner.setMinValue(i3);
            this.mMonthSpinner.setMaxValue(i4);
            if (!usingNumericMonths()) {
                this.mMonthSpinner.setDisplayedValues((String[]) Arrays.copyOfRange(this.mShortMonths, this.mMonthSpinner.getMinValue(), this.mMonthSpinner.getMaxValue() + 1));
            }
        }
        if (z4) {
            int i5;
            int i6;
            int i7 = this.mMaxDate.get(2) - this.mMinDate.get(2);
            if (this.mMaxDate.get(1) - this.mMinDate.get(1) == 0 && i7 == 0) {
                i5 = this.mMinDate.get(5);
                i6 = this.mMaxDate.get(5);
            } else {
                i = this.mCurrentDate.get(1);
                i2 = this.mCurrentDate.get(2);
                if (this.mIsLunar) {
                    i = this.mLunarCurrentYear;
                    i2 = this.mLunarCurrentMonth;
                }
                if (i == this.mMinDate.get(1) && i2 == this.mMinDate.get(2)) {
                    i5 = this.mMinDate.get(5);
                    i6 = this.mCurrentDate.getActualMaximum(5);
                    if (this.mIsLunar) {
                        i6 = getLunarMaxDayOfMonth(i, i2, this.mIsLeapMonth);
                    }
                } else if (i == this.mMaxDate.get(1) && i2 == this.mMaxDate.get(2)) {
                    i5 = 1;
                    i6 = this.mMaxDate.get(5);
                } else {
                    i5 = 1;
                    i6 = this.mCurrentDate.getActualMaximum(5);
                    if (this.mIsLunar) {
                        i6 = getLunarMaxDayOfMonth(i, i2, this.mIsLeapMonth);
                    }
                }
            }
            this.mDaySpinner.setMinValue(i5);
            this.mDaySpinner.setMaxValue(i6);
        }
        if (z) {
            this.mYearSpinner.setValue(this.mCurrentDate.get(1));
            if (this.mYearSpinnerInput.hasFocus()) {
                Selection.setSelection(this.mYearSpinnerInput.getText(), this.mYearSpinnerInput.getText().length());
            }
            i2 = this.mCurrentDate.get(2);
            if (this.mIsLunar) {
                i2 = this.mLunarCurrentMonth;
            }
            if (usingNumericMonths()) {
                this.mMonthSpinner.setValue(i2 + 1);
            } else {
                this.mMonthSpinner.setValue(i2);
            }
            int i8 = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                i8 = this.mLunarCurrentDay;
            }
            this.mDaySpinner.setValue(i8);
            if (usingNumericMonths()) {
                this.mMonthSpinnerInput.setRawInputType(2);
            }
        }
    }

    private boolean usingNumericMonths() {
        return Character.isDigit(this.mShortMonths[0].charAt(0));
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public int getDayOfMonth() {
        return this.mIsLunar ? this.mLunarCurrentDay : this.mCurrentDate.get(5);
    }

    public EditText getEditText(int i) {
        return i == 0 ? this.mDaySpinner.getEditText() : i == 1 ? this.mMonthSpinner.getEditText() : i == 2 ? this.mYearSpinner.getEditText() : this.mDaySpinner.getEditText();
    }

    public Calendar getMaxDate() {
        return this.mMaxDate;
    }

    public Calendar getMinDate() {
        return this.mMinDate;
    }

    public int getMonth() {
        return this.mIsLunar ? this.mLunarCurrentMonth : this.mCurrentDate.get(2);
    }

    public SemNumberPicker getNumberPicker(int i) {
        return i == 0 ? this.mDaySpinner : i == 1 ? this.mMonthSpinner : i == 2 ? this.mYearSpinner : this.mDaySpinner;
    }

    public int getYear() {
        return this.mIsLunar ? this.mLunarCurrentYear : this.mCurrentDate.get(1);
    }

    public void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener) {
        setDate(i, i2, i3);
        updateSpinners(true, true, true, true);
        this.mOnDateChangedListener = onDateChangedListener;
    }

    public boolean isEditTextMode() {
        return this.mIsEditTextMode;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.getText().add(DateUtils.formatDateTime(this.mContext, this.mCurrentDate.getTimeInMillis(), 20));
    }

    protected void setCurrentLocale(Locale locale) {
        int i;
        this.mTempDate = getCalendarForLocale(this.mTempDate, locale);
        this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
        this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
        this.mNumberOfMonths = this.mTempDate.getActualMaximum(2) + 1;
        this.mShortMonths = new DateFormatSymbols().getShortMonths();
        for (i = 0; i < this.mShortMonths.length; i++) {
            this.mShortMonths[i] = this.mShortMonths[i].toUpperCase();
        }
        if (usingNumericMonths()) {
            this.mShortMonths = new String[this.mNumberOfMonths];
            for (i = 0; i < this.mNumberOfMonths; i++) {
                this.mShortMonths[i] = String.format("%d", new Object[]{Integer.valueOf(i + 1)});
            }
        }
    }

    public void setEditTextMode(boolean z) {
        if (this.mIsEditTextMode != z) {
            this.mIsEditTextMode = z;
            InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService("input_method");
            this.mDaySpinner.setEditTextMode(z);
            this.mMonthSpinner.setEditTextMode(z);
            this.mYearSpinner.setEditTextMode(z);
            if (this.mIsEditTextMode) {
                if (!(inputMethodManager == null || inputMethodManager.isInputMethodShown())) {
                    inputMethodManager.showSoftInput(this.mDaySpinner, 0);
                }
            } else if (inputMethodManager != null && inputMethodManager.isInputMethodShown()) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
            if (this.mOnEditTextModeChangedListener != null) {
                this.mOnEditTextModeChangedListener.onEditTextModeChanged(this.mDatePicker, z);
            }
        }
    }

    public void setEnabled(boolean z) {
        this.mDaySpinner.setEnabled(z);
        this.mMonthSpinner.setEnabled(z);
        this.mYearSpinner.setEnabled(z);
    }

    public void setIsLeapMonth(boolean z) {
        this.mIsLeapMonth = z;
    }

    public void setLunar(boolean z, boolean z2, PathClassLoader pathClassLoader) {
        this.mIsLunar = z;
        this.mIsLeapMonth = z2;
        if (this.mIsLunar && this.mPathClassLoader == null) {
            String calendarPackageName = SemDatePicker.getCalendarPackageName();
            this.mPathClassLoader = pathClassLoader;
            try {
                Class cls = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                if (cls == null) {
                    Log.e(TAG, "setLunar, Calendar Feature class is null");
                    return;
                }
                this.mSolarLunarTables = invoke(null, getMethod(cls, "getSolarLunarTables", new Class[0]), new Object[0]);
                try {
                    Class cls2 = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarTables", true, this.mPathClassLoader);
                    if (cls2 == null) {
                        Log.e(TAG, "setLunar, Calendar Tables class is null");
                        return;
                    }
                    this.mIsLeapMonthMethod = getMethod(cls2, "isLeapMonth", Integer.TYPE, Integer.TYPE);
                    this.mGetDayLengthOfMethod = getMethod(cls2, "getDayLengthOf", Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "setLunar, Calendar Tables class not found");
                    return;
                }
            } catch (ClassNotFoundException e2) {
                Log.e(TAG, "setLunar, Calendar Feature class not found");
                return;
            }
        }
        updateSpinners(false, true, true, true);
    }

    public void setMaxDate(long j) {
        this.mMaxDate.setTimeInMillis(j);
        if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
        updateSpinners(true, true, true, true);
    }

    public void setMinDate(long j) {
        this.mMinDate.setTimeInMillis(j);
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        }
        updateSpinners(true, true, true, true);
    }

    public void setOnEditTextModeChangedListener(SemDatePicker semDatePicker, SemDatePicker.OnEditTextModeChangedListener onEditTextModeChangedListener) {
        if (this.mDatePicker == null) {
            this.mDatePicker = semDatePicker;
        }
        this.mOnEditTextModeChangedListener = onEditTextModeChangedListener;
    }

    public void setOnSpinnerDateChangedListener(OnSpinnerDateChangedListener onSpinnerDateChangedListener) {
        this.mOnSpinnerDateChangedListener = onSpinnerDateChangedListener;
    }

    public void updateDate(int i, int i2, int i3) {
        if (isNewDate(i, i2, i3)) {
            setDate(i, i2, i3);
            updateSpinners(true, true, true, true);
            if (this.mIsLunar) {
                notifyDateChanged(false);
            } else {
                notifyDateChanged(true);
            }
        }
    }

    public void updateInputState() {
        InputMethodManager peekInstance = InputMethodManager.peekInstance();
        if (peekInstance == null) {
            return;
        }
        if (peekInstance.isActive(this.mYearSpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mYearSpinnerInput.clearFocus();
        } else if (peekInstance.isActive(this.mMonthSpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mMonthSpinnerInput.clearFocus();
        } else if (peekInstance.isActive(this.mDaySpinnerInput)) {
            peekInstance.hideSoftInputFromWindow(getWindowToken(), 0);
            this.mDaySpinnerInput.clearFocus();
        }
    }
}
