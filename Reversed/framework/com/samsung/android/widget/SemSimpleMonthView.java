package com.samsung.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.System;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.IntArray;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.widget.ExploreByTouchHelper;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.fingerprint.FingerprintManager;
import com.sec.android.app.CscFeatureTagCalendar;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

class SemSimpleMonthView extends View {
    private static final int DEFAULT_NUM_DAYS = 7;
    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final int DEFAULT_WEEK_START = 1;
    private static final int LEAP_MONTH = 1;
    private static final float LEAP_MONTH_WEIGHT = 0.5f;
    private static final int MIN_HEIGHT = 10;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final String TAG = "SemSimpleMonthView";
    private final int mAbnormalStartEndDateBackgroundAlpha;
    private final Calendar mCalendar;
    private int mCalendarHeight;
    private int mCalendarWidth;
    private Method mConvertLunarToSolarMethod;
    private final int mDateBackgroundBetweenStartEndAlpha;
    private int[] mDayColorSet;
    private SimpleDateFormat mDayFormatter;
    private int mDayHeight;
    private final Calendar mDayLabelCalendar;
    private Method mDayLengthMethod;
    private final int mDayNumberDisabledAlpha;
    private Paint mDayNumberEndPaint;
    private Paint mDayNumberKeySelectedPaint;
    private Paint mDayNumberPaint;
    private Paint mDayNumberSelectedPaint;
    private int mDayOfWeekStart;
    private int mDaySelectedCircleSize;
    private int mDaySelectedCircleStroke;
    private int mDaySelectedStartEndAdjust;
    private Paint mDayStartEndPaint;
    private String mDefaultWeekdayFeatureString;
    private int mEnabledDayEnd;
    private int mEnabledDayStart;
    private int mEndDay;
    private int mEndDayColor;
    private int mEndMonth;
    private int mEndYear;
    private final Formatter mFormatter;
    private Method mGetDayMethod;
    private Method mGetMonthMethod;
    private Method mGetWeekDayMethod;
    private Method mGetYearMethod;
    private boolean mHasToday;
    private int mIsLeapEndMonth;
    private boolean mIsLeapMonth;
    private int mIsLeapStartMonth;
    private boolean mIsLunar;
    private boolean mIsRTL;
    private boolean mLockAccessibilityDelegate;
    private int mMiniDayNumberTextSize;
    private int mMode;
    private int mMonth;
    private int mNormalTextColor;
    private int mNumCells;
    private int mNumDays;
    private OnDayClickListener mOnDayClickListener;
    private int mPadding;
    private PathClassLoader mPathClassLoader;
    private int mSaturdayTextColor;
    private int mSelectedDay;
    private int mSelectedDayColor;
    private Object mSolarLunarConverter;
    private int mStartDay;
    private int mStartMonth;
    private int mStartYear;
    private final StringBuilder mStringBuilder;
    private int mSundayTextColor;
    private TimeZone mTimeZone;
    private int mToday;
    private final MonthViewTouchHelper mTouchHelper;
    private int mWeekHeight;
    private int mWeekStart;
    private String mWeekdayFeatureString;
    private int mYear;

    public interface OnDayClickListener {
        void onDayClick(SemSimpleMonthView semSimpleMonthView, int i, int i2, int i3);
    }

    private class MonthViewTouchHelper extends ExploreByTouchHelper {
        private static final String DATE_FORMAT = "dd MMMM yyyy";
        private final Calendar mTempCalendar = Calendar.getInstance();
        private final Rect mTempRect = new Rect();

        public MonthViewTouchHelper(View view) {
            super(view);
        }

        private void getItemBounds(int i, Rect rect) {
            int -get11 = SemSimpleMonthView.this.mPadding;
            int i2 = (int) (SemSimpleMonthView.this.mContext.getResources().getDisplayMetrics().density * -1.0f);
            int -get15 = SemSimpleMonthView.this.mWeekHeight;
            int -get0 = SemSimpleMonthView.this.mCalendarWidth / SemSimpleMonthView.this.mNumDays;
            int -wrap0 = (i - 1) + SemSimpleMonthView.this.findDayOffset();
            int -get10 = -get11 + ((-wrap0 % SemSimpleMonthView.this.mNumDays) * -get0);
            int -get102 = i2 + ((-wrap0 / SemSimpleMonthView.this.mNumDays) * -get15);
            rect.set(-get10, -get102, -get10 + -get0, -get102 + -get15);
        }

        private CharSequence getItemDescription(int i) {
            this.mTempCalendar.set(SemSimpleMonthView.this.mYear, SemSimpleMonthView.this.mMonth, i);
            CharSequence formatDateTime = DateUtils.formatDateTime(SemSimpleMonthView.this.mContext, this.mTempCalendar.getTimeInMillis(), 22);
            if (SemSimpleMonthView.this.mIsLunar && SemSimpleMonthView.this.mPathClassLoader != null) {
                SemSimpleMonthView.this.invoke(SemSimpleMonthView.this.mSolarLunarConverter, SemSimpleMonthView.this.mConvertLunarToSolarMethod, Integer.valueOf(SemSimpleMonthView.this.mYear), Integer.valueOf(SemSimpleMonthView.this.mMonth), Integer.valueOf(i), Boolean.valueOf(SemSimpleMonthView.this.mIsLeapMonth));
                Integer -wrap2 = SemSimpleMonthView.this.invoke(SemSimpleMonthView.this.mSolarLunarConverter, SemSimpleMonthView.this.mGetYearMethod, new Object[0]);
                Integer -wrap22 = SemSimpleMonthView.this.invoke(SemSimpleMonthView.this.mSolarLunarConverter, SemSimpleMonthView.this.mGetMonthMethod, new Object[0]);
                Integer -wrap23 = SemSimpleMonthView.this.invoke(SemSimpleMonthView.this.mSolarLunarConverter, SemSimpleMonthView.this.mGetDayMethod, new Object[0]);
                if ((-wrap2 instanceof Integer) && (-wrap22 instanceof Integer) && (-wrap23 instanceof Integer)) {
                    Calendar.getInstance().set(-wrap2.intValue(), -wrap22.intValue(), -wrap23.intValue());
                    try {
                        Class cls = Class.forName("com.android.calendar.lunarDatePicker.LunarDateUtils", true, SemSimpleMonthView.this.mPathClassLoader);
                        if (cls == null) {
                            Log.e(SemSimpleMonthView.TAG, "getItemDescription, Calendar LunarDateUtils class is null");
                            return formatDateTime;
                        }
                        Method -wrap3 = SemSimpleMonthView.this.getMethod(cls, "buildLunarDateString", Calendar.class, Context.class);
                        CharSequence -wrap24 = SemSimpleMonthView.this.invoke(null, -wrap3, r2, SemSimpleMonthView.this.getContext());
                        if (-wrap24 instanceof String) {
                            formatDateTime = -wrap24;
                        }
                    } catch (ClassNotFoundException e) {
                        Log.e(SemSimpleMonthView.TAG, "getItemDescription, Calendar LunarDateUtils class not found");
                        return formatDateTime;
                    }
                }
            }
            return formatDateTime;
        }

        public void clearFocusedVirtualView() {
            int focusedVirtualView = getFocusedVirtualView();
            if (focusedVirtualView != FingerprintManager.PRIVILEGED_TYPE_KEYGUARD) {
                getAccessibilityNodeProvider(SemSimpleMonthView.this).performAction(focusedVirtualView, 128, null);
            }
        }

        protected int getVirtualViewAt(float f, float f2) {
            int -wrap1 = SemSimpleMonthView.this.getDayFromLocation(f, f2);
            return -wrap1 >= 0 ? -wrap1 : FingerprintManager.PRIVILEGED_TYPE_KEYGUARD;
        }

        protected void getVisibleVirtualViews(IntArray intArray) {
            for (int i = 1; i <= SemSimpleMonthView.this.mNumCells; i++) {
                intArray.add(i);
            }
        }

        protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            switch (i2) {
                case 16:
                    SemSimpleMonthView.this.onDayClick(i);
                    return true;
                default:
                    return false;
            }
        }

        protected void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getItemDescription(i));
        }

        protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfo accessibilityNodeInfo) {
            getItemBounds(i, this.mTempRect);
            accessibilityNodeInfo.setContentDescription(getItemDescription(i));
            accessibilityNodeInfo.setBoundsInParent(this.mTempRect);
            accessibilityNodeInfo.addAction(16);
            if (i == SemSimpleMonthView.this.mSelectedDay) {
                accessibilityNodeInfo.addAction(4);
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.setCheckable(true);
                accessibilityNodeInfo.setChecked(true);
            }
        }

        public void setFocusedVirtualView(int i) {
            getAccessibilityNodeProvider(SemSimpleMonthView.this).performAction(i, 64, null);
        }
    }

    public SemSimpleMonthView(Context context) {
        this(context, null);
    }

    public SemSimpleMonthView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843612);
    }

    public SemSimpleMonthView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mDayFormatter = new SimpleDateFormat("EEE", Locale.getDefault());
        this.mPadding = 0;
        this.mHasToday = false;
        this.mSelectedDay = -1;
        this.mToday = -1;
        this.mWeekStart = 1;
        this.mNumDays = 7;
        this.mNumCells = this.mNumDays;
        this.mDayOfWeekStart = 0;
        this.mEnabledDayStart = 1;
        this.mEnabledDayEnd = 31;
        this.mCalendar = Calendar.getInstance();
        this.mDayLabelCalendar = Calendar.getInstance();
        this.mMode = 0;
        this.mDayColorSet = new int[7];
        this.mDefaultWeekdayFeatureString = "XXXXXXR";
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mPathClassLoader = null;
        this.mIsRTL = isRTL();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843828, typedValue, true);
        this.mSelectedDayColor = context.getResources().getColor(typedValue.resourceId);
        Resources resources = context.getResources();
        this.mStringBuilder = new StringBuilder(50);
        this.mFormatter = new Formatter(this.mStringBuilder, Locale.getDefault());
        this.mEndDayColor = resources.getColor(17170821);
        this.mDaySelectedStartEndAdjust = resources.getDimensionPixelOffset(17105690);
        this.mWeekHeight = resources.getDimensionPixelOffset(17105697);
        this.mDayHeight = resources.getDimensionPixelOffset(17105697);
        this.mDaySelectedCircleSize = resources.getDimensionPixelSize(17105698);
        this.mDaySelectedCircleStroke = resources.getDimensionPixelSize(17105699);
        this.mMiniDayNumberTextSize = resources.getDimensionPixelSize(17105700);
        this.mCalendarWidth = resources.getDimensionPixelOffset(17105683);
        this.mPadding = resources.getDimensionPixelOffset(17105682);
        this.mTouchHelper = new MonthViewTouchHelper(this);
        setAccessibilityDelegate(this.mTouchHelper);
        setImportantForAccessibility(1);
        this.mLockAccessibilityDelegate = true;
        if (System.getString(this.mContext.getContentResolver(), "current_sec_active_themepackage") == null) {
            this.mDayNumberDisabledAlpha = resources.getInteger(17105688);
        } else {
            this.mDayNumberDisabledAlpha = resources.getInteger(17105689);
        }
        this.mDateBackgroundBetweenStartEndAlpha = resources.getInteger(17105703);
        this.mAbnormalStartEndDateBackgroundAlpha = resources.getInteger(17105704);
        initView();
    }

    private int calculateNumRows() {
        int i = 0;
        int findDayOffset = findDayOffset();
        int i2 = (this.mNumCells + findDayOffset) / this.mNumDays;
        if ((this.mNumCells + findDayOffset) % this.mNumDays > 0) {
            i = 1;
        }
        return i + i2;
    }

    private void drawDays(Canvas canvas) {
        int i = (this.mWeekHeight * 2) / 3;
        int i2 = this.mCalendarWidth / (this.mNumDays * 2);
        int findDayOffset = findDayOffset();
        int i3 = 1;
        while (i3 <= this.mNumCells) {
            int i4 = this.mIsRTL ? (((((this.mNumDays - 1) - findDayOffset) * 2) + 1) * i2) + this.mPadding : (((findDayOffset * 2) + 1) * i2) + this.mPadding;
            if (this.mSelectedDay == i3 && this.mMode == 0) {
                canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, this.mDayNumberSelectedPaint);
            }
            this.mDayNumberPaint.setColor(this.mDayColorSet[(this.mWeekStart + findDayOffset) % this.mNumDays]);
            if (i3 < this.mEnabledDayStart || i3 > this.mEnabledDayEnd) {
                this.mDayNumberPaint.setAlpha(this.mDayNumberDisabledAlpha);
            }
            int i5 = -1;
            int i6 = -1;
            int i7 = this.mStartYear;
            float f = (float) this.mStartMonth;
            int i8 = this.mStartDay;
            int i9 = this.mEndYear;
            float f2 = (float) this.mEndMonth;
            int i10 = this.mEndDay;
            if (this.mIsLunar && this.mIsLeapStartMonth == 1) {
                f += LEAP_MONTH_WEIGHT;
            }
            if (this.mIsLunar && this.mIsLeapEndMonth == 1) {
                f2 += LEAP_MONTH_WEIGHT;
            }
            int i11 = this.mYear;
            float f3 = (float) this.mMonth;
            if (this.mIsLunar && this.mIsLeapMonth) {
                f3 += LEAP_MONTH_WEIGHT;
            }
            Paint paint;
            if ((((i7 * 10000) + ((int) (100.0f * f))) + i8 > ((i9 * 10000) + ((int) (100.0f * f2))) + i10 ? 1 : null) != null) {
                paint = this.mDayNumberSelectedPaint;
                if (!(i7 == i11 && f == f3 && i8 == i3 && this.mMode == 2)) {
                    if (i9 == i11 && f2 == f3 && i10 == i3 && this.mMode == 1) {
                    }
                    if (!(i9 == i11 && f2 == f3 && i10 == i3 && this.mMode == 2)) {
                        if (i7 == i11 && f == f3 && i8 == i3 && this.mMode == 1) {
                        }
                    }
                    paint.setStyle(Style.FILL);
                    paint.setColor(this.mNormalTextColor);
                    paint.setAlpha(this.mAbnormalStartEndDateBackgroundAlpha);
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
                }
                paint.setStyle(Style.STROKE);
                paint.setColor(this.mSelectedDayColor);
                canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
                paint.setStyle(Style.FILL);
                paint.setColor(this.mNormalTextColor);
                paint.setAlpha(this.mAbnormalStartEndDateBackgroundAlpha);
                canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
            } else {
                float f4;
                float f5;
                Paint paint2 = this.mDayStartEndPaint;
                paint2.setAlpha(this.mDateBackgroundBetweenStartEndAlpha);
                paint = this.mDayNumberSelectedPaint;
                paint.setColor(this.mSelectedDayColor);
                if (i7 == i9 && f == f2 && i11 == i7 && f3 == f) {
                    i5 = i8;
                    i6 = i10;
                } else if ((i7 * 10000) + ((int) (100.0f * f)) < (i11 * 10000) + ((int) (100.0f * f3)) && (i11 * 10000) + ((int) (100.0f * f3)) < (i9 * 10000) + ((int) (100.0f * f2)) && (i11 != i9 || f3 != f2)) {
                    i5 = 0;
                    i6 = this.mNumCells + 1;
                } else if (i11 == i7 && f3 == f) {
                    i5 = i8;
                    i6 = this.mNumCells + 1;
                } else if (i11 == i9 && f3 == f2) {
                    i5 = 0;
                    i6 = i10;
                }
                if (i5 < i3 && i3 < i6) {
                    f4 = (float) (i4 - i2);
                    f5 = ((((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f)) - ((float) this.mDaySelectedCircleSize)) - ((float) this.mDaySelectedStartEndAdjust);
                    canvas.drawRect(f4, f5, f4 + ((float) (i2 * 2)), ((float) (this.mDaySelectedStartEndAdjust * 2)) + (((float) (this.mDaySelectedCircleSize * 2)) + f5), paint2);
                }
                if (i5 != -1 && i5 == i6 && i3 == i5) {
                    paint.setStyle(Style.FILL);
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
                } else if (i6 == i3) {
                    f4 = (float) (this.mIsRTL ? i4 : i4 - i2);
                    f5 = ((((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f)) - ((float) this.mDaySelectedCircleSize)) - ((float) this.mDaySelectedStartEndAdjust);
                    canvas.drawRect(f4, f5, f4 + ((float) i2), ((float) (this.mDaySelectedStartEndAdjust * 2)) + (((float) (this.mDaySelectedCircleSize * 2)) + f5), paint2);
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, this.mDayNumberEndPaint);
                    if (this.mMode == 2) {
                        paint.setStyle(Style.FILL);
                    } else {
                        paint.setStyle(Style.STROKE);
                    }
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
                } else if (i5 == i3) {
                    f4 = (float) (this.mIsRTL ? i4 - i2 : i4);
                    f5 = ((((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f)) - ((float) this.mDaySelectedCircleSize)) - ((float) this.mDaySelectedStartEndAdjust);
                    canvas.drawRect(f4, f5, f4 + ((float) i2), ((float) (this.mDaySelectedStartEndAdjust * 2)) + (((float) (this.mDaySelectedCircleSize * 2)) + f5), paint2);
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, this.mDayNumberEndPaint);
                    if (this.mMode == 1) {
                        paint.setStyle(Style.FILL);
                    } else {
                        paint.setStyle(Style.STROKE);
                    }
                    canvas.drawCircle((float) i4, ((float) i) - (((float) this.mMiniDayNumberTextSize) / 2.7f), (float) this.mDaySelectedCircleSize, paint);
                }
            }
            Paint paint3 = this.mDayNumberPaint;
            if (!((this.mMode == 1 && i3 == r21) || (this.mMode == 2 && i3 == r12))) {
                if (this.mMode == 0 && i3 == r12) {
                }
                canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(i3)}), (float) i4, (float) i, paint3);
                findDayOffset++;
                if (findDayOffset == this.mNumDays) {
                    findDayOffset = 0;
                    i += this.mWeekHeight;
                }
                i3++;
            }
            paint3.setColor(this.mEndDayColor);
            canvas.drawText(String.format("%d", new Object[]{Integer.valueOf(i3)}), (float) i4, (float) i, paint3);
            findDayOffset++;
            if (findDayOffset == this.mNumDays) {
                findDayOffset = 0;
                i += this.mWeekHeight;
            }
            i3++;
        }
    }

    private int findDayOffset() {
        return (this.mDayOfWeekStart < this.mWeekStart ? this.mDayOfWeekStart + this.mNumDays : this.mDayOfWeekStart) - this.mWeekStart;
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

    private int getDayFromLocation(float f, float f2) {
        int i = this.mPadding;
        if (this.mIsRTL) {
            f = ((float) this.mCalendarWidth) - f;
        }
        if (f < ((float) i) || f > ((float) (this.mCalendarWidth + this.mPadding))) {
            return -1;
        }
        int findDayOffset = ((((int) (((f - ((float) i)) * ((float) this.mNumDays)) / ((float) this.mCalendarWidth))) - findDayOffset()) + 1) + (this.mNumDays * (((int) f2) / this.mWeekHeight));
        return (findDayOffset < 1 || findDayOffset > this.mNumCells) ? -1 : findDayOffset;
    }

    private static int getDaysInMonth(int i, int i2) {
        switch (i) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return i2 % 4 == 0 ? (i2 % 100 != 0 || i2 % 400 == 0) ? 29 : 28 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private int getDaysInMonthLunar(int i, int i2, boolean z) {
        int daysInMonth = getDaysInMonth(i, i2);
        if (this.mSolarLunarConverter == null) {
            Log.e(TAG, "getDaysInMonthLunar, mSolarLunarConverter is null");
            return daysInMonth;
        }
        Integer invoke = invoke(this.mSolarLunarConverter, this.mDayLengthMethod, Integer.valueOf(i2), Integer.valueOf(i), Boolean.valueOf(z));
        if (invoke instanceof Integer) {
            return invoke.intValue();
        }
        Log.e(TAG, "getDaysInMonthLunar, dayLength is not Integer");
        return daysInMonth;
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

    private String getMonthAndYearString() {
        this.mStringBuilder.setLength(0);
        long timeInMillis = this.mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), this.mFormatter, timeInMillis, timeInMillis, 52, Time.getCurrentTimezone()).toString();
    }

    private void initView() {
        this.mDayNumberSelectedPaint = new Paint();
        this.mDayNumberSelectedPaint.setAntiAlias(true);
        this.mDayNumberSelectedPaint.setColor(this.mSelectedDayColor);
        this.mDayNumberSelectedPaint.setTextAlign(Align.CENTER);
        this.mDayNumberSelectedPaint.setStyle(Style.STROKE);
        this.mDayNumberSelectedPaint.setStrokeWidth((float) this.mDaySelectedCircleStroke);
        this.mDayNumberSelectedPaint.setFakeBoldText(true);
        this.mDayNumberPaint = new Paint();
        this.mDayNumberPaint.setAntiAlias(true);
        this.mDayNumberPaint.setTextSize((float) this.mMiniDayNumberTextSize);
        this.mDayNumberPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
        this.mDayNumberPaint.setTextAlign(Align.CENTER);
        this.mDayNumberPaint.setStyle(Style.FILL);
        this.mDayNumberPaint.setFakeBoldText(false);
        this.mDayNumberSelectedPaint.setStyle(Style.FILL);
        this.mDayStartEndPaint = new Paint();
        this.mDayStartEndPaint.setColor(this.mSelectedDayColor);
        this.mDayStartEndPaint.setStyle(Style.FILL);
        this.mDayNumberEndPaint = new Paint();
        this.mDayNumberEndPaint.setAntiAlias(true);
        this.mDayNumberEndPaint.setTextSize((float) this.mMiniDayNumberTextSize);
        this.mDayNumberEndPaint.setTypeface(Typeface.create("sec-roboto-light", 0));
        this.mDayNumberEndPaint.setTextAlign(Align.CENTER);
        this.mDayNumberEndPaint.setStyle(Style.FILL);
        this.mDayNumberEndPaint.setFakeBoldText(false);
        this.mDayNumberEndPaint.setColor(this.mEndDayColor);
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

    private boolean isRTL() {
        boolean z = true;
        Locale locale = Locale.getDefault();
        if ("ur".equals(locale.getLanguage())) {
            return false;
        }
        byte directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        if (!(directionality == (byte) 1 || directionality == (byte) 2)) {
            z = false;
        }
        return z;
    }

    private static boolean isValidDayOfWeek(int i) {
        return i >= 1 && i <= 7;
    }

    private static boolean isValidMonth(int i) {
        return i >= 0 && i <= 11;
    }

    private int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        switch (mode) {
            case FingerprintManager.PRIVILEGED_TYPE_KEYGUARD /*-2147483648*/:
                this.mCalendarWidth = Math.min(size, i2);
                return MeasureSpec.makeMeasureSpec(this.mCalendarWidth, 1073741824);
            case 0:
                return MeasureSpec.makeMeasureSpec(i2, 1073741824);
            case 1073741824:
                this.mCalendarWidth = size;
                return i;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
    }

    private void onDayClick(int i) {
        if (this.mOnDayClickListener != null) {
            playSoundEffect(0);
            this.mOnDayClickListener.onDayClick(this, this.mYear, this.mMonth, i);
        }
        this.mTouchHelper.sendEventForVirtualView(i, 1);
    }

    private boolean sameDay(int i, Time time) {
        return this.mYear == time.year && this.mMonth == time.month && i == time.monthDay;
    }

    public void clearAccessibilityFocus() {
        this.mTouchHelper.clearFocusedVirtualView();
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mTouchHelper.dispatchHoverEvent(motionEvent) ? true : super.dispatchHoverEvent(motionEvent);
    }

    Calendar getAccessibilityFocus() {
        int focusedVirtualView = this.mTouchHelper.getFocusedVirtualView();
        if (focusedVirtualView < 0) {
            return null;
        }
        Calendar instance = Calendar.getInstance();
        instance.set(this.mYear, this.mMonth, focusedVirtualView);
        return instance;
    }

    public int getDayHeight() {
        return this.mDayHeight;
    }

    public int getNumDays() {
        return this.mNumDays;
    }

    public int getWeekStart() {
        return this.mWeekStart;
    }

    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsRTL = isRTL();
        this.mDayFormatter = new SimpleDateFormat("EEE", configuration.locale);
        this.mTouchHelper.invalidateRoot();
        Resources resources = this.mContext.getResources();
        this.mWeekHeight = resources.getDimensionPixelOffset(17105697);
        this.mDayHeight = resources.getDimensionPixelOffset(17105697);
        this.mDaySelectedCircleSize = resources.getDimensionPixelSize(17105698);
        this.mMiniDayNumberTextSize = resources.getDimensionPixelSize(17105700);
        this.mCalendarWidth = resources.getDimensionPixelOffset(17105683);
        initView();
    }

    protected void onDraw(Canvas canvas) {
        drawDays(canvas);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(makeMeasureSpec(i, this.mCalendarWidth), i2);
        this.mTouchHelper.invalidateRoot();
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mTouchHelper.invalidateRoot();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
        r4 = this;
        r3 = 1;
        r1 = r5.getAction();
        switch(r1) {
            case 1: goto L_0x0009;
            default: goto L_0x0008;
        };
    L_0x0008:
        return r3;
    L_0x0009:
        r1 = r5.getX();
        r2 = r5.getY();
        r0 = r4.getDayFromLocation(r1, r2);
        r1 = r4.mEnabledDayStart;
        if (r0 < r1) goto L_0x001d;
    L_0x0019:
        r1 = r4.mEnabledDayEnd;
        if (r0 <= r1) goto L_0x001e;
    L_0x001d:
        return r3;
    L_0x001e:
        if (r0 < 0) goto L_0x0008;
    L_0x0020:
        r4.onDayClick(r0);
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.widget.SemSimpleMonthView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    boolean restoreAccessibilityFocus(Calendar calendar) {
        if (calendar.get(1) != this.mYear || calendar.get(2) != this.mMonth || calendar.get(5) > this.mNumCells) {
            return false;
        }
        this.mTouchHelper.setFocusedVirtualView(calendar.get(5));
        return true;
    }

    public void reuse() {
        requestLayout();
    }

    public void setAccessibilityDelegate(AccessibilityDelegate accessibilityDelegate) {
        if (!this.mLockAccessibilityDelegate) {
            super.setAccessibilityDelegate(accessibilityDelegate);
        }
    }

    public void setLunar(boolean z, boolean z2, PathClassLoader pathClassLoader) {
        this.mIsLunar = z;
        this.mIsLeapMonth = z2;
        if (this.mIsLunar && this.mSolarLunarConverter == null) {
            String calendarPackageName = SemDatePicker.getCalendarPackageName();
            this.mPathClassLoader = pathClassLoader;
            try {
                Class cls = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                if (cls == null) {
                    Log.e(TAG, "setLunar, Calendar Feature class is null");
                    return;
                }
                this.mSolarLunarConverter = invoke(null, getMethod(cls, "getSolarLunarConverter", new Class[0]), new Object[0]);
                try {
                    Class cls2 = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarConverter", true, this.mPathClassLoader);
                    if (cls2 == null) {
                        Log.e(TAG, "setLunar, Calendar Converter class is null");
                        return;
                    }
                    this.mConvertLunarToSolarMethod = getMethod(cls2, "convertLunarToSolar", Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                    this.mGetWeekDayMethod = getMethod(cls2, "getWeekday", Integer.TYPE, Integer.TYPE, Integer.TYPE);
                    this.mGetYearMethod = getMethod(cls2, "getYear", new Class[0]);
                    this.mGetMonthMethod = getMethod(cls2, "getMonth", new Class[0]);
                    this.mGetDayMethod = getMethod(cls2, "getDay", new Class[0]);
                    this.mDayLengthMethod = getMethod(cls2, "getDayLengthOf", Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, "setLunar, Calendar Converter class not found");
                }
            } catch (ClassNotFoundException e2) {
                Log.e(TAG, "setLunar, Calendar Feature class not found");
            }
        }
    }

    void setMonthParams(int i, int i2, int i3, int i4, int i5, int i6, Calendar calendar, Calendar calendar2, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
        this.mMode = i15;
        if (this.mWeekHeight < 10) {
            this.mWeekHeight = 10;
        }
        this.mSelectedDay = i;
        if (isValidMonth(i2)) {
            this.mMonth = i2;
        }
        this.mYear = i3;
        (this.mTimeZone != null ? new Time(this.mTimeZone.getID()) : new Time(Time.getCurrentTimezone())).setToNow();
        this.mHasToday = false;
        this.mToday = -1;
        this.mCalendar.clear();
        this.mCalendar.set(2, this.mMonth);
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(5, 1);
        if (!this.mIsLunar || this.mSolarLunarConverter == null) {
            this.mDayOfWeekStart = this.mCalendar.get(7);
            this.mNumCells = getDaysInMonth(this.mMonth, this.mYear);
        } else {
            invoke(this.mSolarLunarConverter, this.mConvertLunarToSolarMethod, Integer.valueOf(this.mYear), Integer.valueOf(this.mMonth), Integer.valueOf(1), Boolean.valueOf(this.mIsLeapMonth));
            Integer invoke = invoke(this.mSolarLunarConverter, this.mGetYearMethod, new Object[0]);
            Integer invoke2 = invoke(this.mSolarLunarConverter, this.mGetMonthMethod, new Object[0]);
            Integer invoke3 = invoke(this.mSolarLunarConverter, this.mGetDayMethod, new Object[0]);
            if ((invoke instanceof Integer) && (invoke2 instanceof Integer) && (invoke3 instanceof Integer)) {
                Integer invoke4 = invoke(this.mSolarLunarConverter, this.mGetWeekDayMethod, Integer.valueOf(invoke.intValue()), Integer.valueOf(invoke2.intValue()), Integer.valueOf(invoke3.intValue()));
                if (invoke4 instanceof Integer) {
                    this.mDayOfWeekStart = invoke4.intValue() + 1;
                }
            } else {
                this.mDayOfWeekStart = this.mCalendar.get(7);
            }
            this.mNumCells = getDaysInMonthLunar(this.mMonth, this.mYear, this.mIsLeapMonth);
        }
        if (isValidDayOfWeek(i4)) {
            this.mWeekStart = i4;
        } else {
            this.mWeekStart = this.mCalendar.getFirstDayOfWeek();
        }
        if (this.mMonth == calendar.get(2) && this.mYear == calendar.get(1)) {
            i5 = calendar.get(5);
        }
        if (this.mMonth == calendar2.get(2) && this.mYear == calendar2.get(1)) {
            i6 = calendar2.get(5);
        }
        if (i5 > 0 && i6 < 32) {
            this.mEnabledDayStart = i5;
        }
        if (i6 > 0 && i6 < 32 && i6 >= i5) {
            this.mEnabledDayEnd = i6;
        }
        this.mTouchHelper.invalidateRoot();
        this.mStartYear = i7;
        this.mStartMonth = i8;
        this.mStartDay = i9;
        this.mIsLeapStartMonth = i10;
        this.mEndYear = i11;
        this.mEndMonth = i12;
        this.mEndDay = i13;
        this.mIsLeapEndMonth = i14;
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.mOnDayClickListener = onDayClickListener;
    }

    void setTextColor(ColorStateList colorStateList) {
        Resources resources = getContext().getResources();
        this.mNormalTextColor = resources.getColor(17170819);
        this.mSundayTextColor = resources.getColor(17170818);
        this.mSaturdayTextColor = resources.getColor(17170817);
        this.mWeekdayFeatureString = SemCscFeature.getInstance().getString(CscFeatureTagCalendar.TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS, this.mDefaultWeekdayFeatureString);
        for (int i = 0; i < this.mNumDays; i++) {
            char charAt = this.mWeekdayFeatureString.charAt(i);
            int i2 = (i + 2) % this.mNumDays;
            if (charAt == 'R') {
                this.mDayColorSet[i2] = this.mSundayTextColor;
            } else if (charAt == 'B') {
                this.mDayColorSet[i2] = this.mSaturdayTextColor;
            } else {
                this.mDayColorSet[i2] = this.mNormalTextColor;
            }
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        if (this.mTimeZone != timeZone) {
            this.mTimeZone = timeZone;
        }
    }
}
