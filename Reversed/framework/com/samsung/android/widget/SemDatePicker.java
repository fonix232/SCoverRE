package com.samsung.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.android.internal.R;
import com.android.internal.widget.PagerAdapter;
import com.android.internal.widget.ViewPager;
import com.android.internal.widget.ViewPager.OnPageChangeListener;
import com.samsung.android.feature.SemCscFeature;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.share.SShareConstants;
import com.samsung.android.widget.SemDatePickerSpinnerLayout.OnSpinnerDateChangedListener;
import com.samsung.android.widget.SemSimpleMonthView.OnDayClickListener;
import com.sec.android.app.CscFeatureTagCalendar;
import com.sec.enterprise.knoxcustom.CustomDeviceManagerProxy;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class SemDatePicker extends LinearLayout implements OnDayClickListener, OnClickListener, OnLongClickListener {
    public static final int DATE_MODE_END = 2;
    public static final int DATE_MODE_NONE = 0;
    public static final int DATE_MODE_START = 1;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final long DEFAULT_LONG_PRESS_UPDATE_INTERVAL = 300;
    private static final int DEFAULT_MONTH_PER_YEAR = 12;
    private static final int DEFAULT_START_YEAR = 1902;
    private static final int LEAP_MONTH = 1;
    private static final int MESSAGE_CALENDAR_HEADER_MONTH_BUTTON_SET = 1001;
    private static final int MESSAGE_CALENDAR_HEADER_TEXT_VALUE_SET = 1000;
    private static final int NOT_LEAP_MONTH = 0;
    private static final boolean SEM_DEBUG = false;
    private static final int SIZE_UNSPECIFIED = -1;
    private static final int SPINNER_HAVE_ONLY_ONE_ITEM_ALPHA = 102;
    private static final String TAG = SemDatePicker.class.getSimpleName();
    private static final int USE_LOCALE = 0;
    private static final int VIEW_CALENDAR = 0;
    private static final int VIEW_SPINNER = 1;
    private static PackageManager mPackageManager;
    private ViewAnimator mAnimator;
    private int mBackgroundBorderlessResId;
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private RelativeLayout mCalendarHeader;
    private RelativeLayout mCalendarHeaderLayout;
    private int mCalendarHeaderLayoutHeight;
    private TextView mCalendarHeaderText;
    private int mCalendarHeaderTextSize;
    private CalendarPagerAdapter mCalendarPagerAdapter;
    private final ColorStateList mCalendarTextColor;
    private SemSimpleMonthView mCalendarView;
    private int mCalendarViewMargin;
    private ViewPager mCalendarViewPager;
    private int mCalendarViewPagerHeight;
    private int mCalendarViewPagerWidth;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand;
    private Calendar mCurrentDate;
    private Locale mCurrentLocale;
    private int mCurrentPosition;
    private int mCurrentView;
    private View mCustomButtonView;
    private int mDatePickerHeight;
    private LinearLayout mDatePickerLayout;
    private SimpleDateFormat mDayFormatter;
    private int mDayHeight;
    private LinearLayout mDayOfTheWeekLayout;
    private int mDayOfTheWeekLayoutHeight;
    private int mDayOfTheWeekLayoutWidth;
    private DayOfTheWeekView mDayOfTheWeekView;
    private Calendar mEndDate;
    private int mEndYear;
    private View mFirstBlankSpace;
    private int mFirstBlankSpaceHeight;
    private int mFirstDayOfWeek;
    private Method mGetLunarMethod;
    private Handler mHandler;
    private Field mIndexOfLeapMonthField;
    private boolean mIsConfigurationChanged;
    private boolean mIsEnabled;
    private boolean mIsFarsiLanguage;
    private boolean mIsFirstMeasure;
    private boolean mIsFromSetLunar;
    private boolean mIsFromSystem;
    private int mIsLeapEndMonth;
    private boolean mIsLeapMonth;
    private int mIsLeapStartMonth;
    private boolean mIsLunar;
    private boolean mIsLunarSupported;
    private boolean mIsRTL;
    private boolean mIsSimplifiedChinese;
    private long mLongPressUpdateInterval;
    private boolean mLunarChanged;
    private int mLunarCurrentDay;
    private int mLunarCurrentMonth;
    private int mLunarCurrentYear;
    private int mLunarEndDay;
    private int mLunarEndMonth;
    private int mLunarEndYear;
    private int mLunarStartDay;
    private int mLunarStartMonth;
    private int mLunarStartYear;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private int mMode;
    private OnKeyListener mMonthKeyListener;
    private OnTouchListener mMonthTouchListener;
    private ImageButton mNextButton;
    private int mNumDays;
    private int mNumberOfMonths;
    private int mOldCalendarViewPagerWidth;
    private int mOldSelectedDay;
    private OnDateChangedListener mOnDateChangedListener;
    private int mPadding;
    PathClassLoader mPathClassLoader;
    private int mPositionCount;
    private ImageButton mPrevButton;
    private View mSecondBlankSpace;
    private int mSecondBlankSpaceHeight;
    private String[] mShortMonths;
    private Object mSolarLunarTables;
    private SemDatePickerSpinnerLayout mSpinnerLayout;
    private int mSpinnerLayoutBottomMargin;
    private int mSpinnerLayoutHeight;
    private int mSpinnerLayoutTopMargin;
    private Calendar mStartDate;
    private Field mStartOfLunarYearField;
    private int mStartYear;
    private Calendar mTempDate;
    private Calendar mTempMinMaxDate;
    private int[] mTotalMonthCountWithLeap;
    private ValidationCallback mValidationCallback;
    private int mWeekStart;
    private Field mWidthPerYearField;

    class C02671 implements OnFocusChangeListener {
        C02671() {
        }

        public void onFocusChange(View view, boolean z) {
            if (!z) {
                SemDatePicker.this.removeAllCallbacks();
            }
        }
    }

    class C02693 implements OnTouchListener {
        C02693() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                SemDatePicker.this.removeAllCallbacks();
            }
            return false;
        }
    }

    class C02704 implements OnKeyListener {
        C02704() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (SemDatePicker.this.mIsRTL) {
                SemDatePicker.this.mIsConfigurationChanged = false;
            }
            if (keyEvent.getAction() == 1 || keyEvent.getAction() == 3) {
                SemDatePicker.this.removeAllCallbacks();
            }
            return false;
        }
    }

    class C02715 implements OnSpinnerDateChangedListener {
        C02715() {
        }

        public void onDateChanged(SemDatePickerSpinnerLayout semDatePickerSpinnerLayout, int i, int i2, int i3) {
            SemDatePicker.this.mCurrentDate.set(1, i);
            SemDatePicker.this.mCurrentDate.set(2, i2);
            SemDatePicker.this.mCurrentDate.set(5, i3);
            if (SemDatePicker.this.mIsLunar) {
                SemDatePicker.this.mLunarCurrentYear = i;
                SemDatePicker.this.mLunarCurrentMonth = i2;
                SemDatePicker.this.mLunarCurrentDay = i3;
            }
            if (SemDatePicker.this.mMode == 1) {
                SemDatePicker.this.mStartDate.clear();
                SemDatePicker.this.mStartDate.set(1, i);
                SemDatePicker.this.mStartDate.set(2, i2);
                SemDatePicker.this.mStartDate.set(5, i3);
                if (SemDatePicker.this.mIsLunar) {
                    SemDatePicker.this.mLunarStartYear = i;
                    SemDatePicker.this.mLunarStartMonth = i2;
                    SemDatePicker.this.mLunarStartDay = i3;
                    SemDatePicker.this.mIsLeapStartMonth = 0;
                }
            } else if (SemDatePicker.this.mMode == 2) {
                SemDatePicker.this.mEndDate.clear();
                SemDatePicker.this.mEndDate.set(1, i);
                SemDatePicker.this.mEndDate.set(2, i2);
                SemDatePicker.this.mEndDate.set(5, i3);
                if (SemDatePicker.this.mIsLunar) {
                    SemDatePicker.this.mLunarEndYear = i;
                    SemDatePicker.this.mLunarEndMonth = i2;
                    SemDatePicker.this.mLunarEndDay = i3;
                    SemDatePicker.this.mIsLeapEndMonth = 0;
                }
            } else {
                SemDatePicker.this.mStartDate.clear();
                SemDatePicker.this.mStartDate.set(1, i);
                SemDatePicker.this.mStartDate.set(2, i2);
                SemDatePicker.this.mStartDate.set(5, i3);
                SemDatePicker.this.mEndDate.clear();
                SemDatePicker.this.mEndDate.set(1, i);
                SemDatePicker.this.mEndDate.set(2, i2);
                SemDatePicker.this.mEndDate.set(5, i3);
                if (SemDatePicker.this.mIsLunar) {
                    SemDatePicker.this.mLunarStartYear = i;
                    SemDatePicker.this.mLunarStartMonth = i2;
                    SemDatePicker.this.mLunarStartDay = i3;
                    SemDatePicker.this.mIsLeapStartMonth = 0;
                    SemDatePicker.this.mLunarEndYear = i;
                    SemDatePicker.this.mLunarEndMonth = i2;
                    SemDatePicker.this.mLunarEndDay = i3;
                    SemDatePicker.this.mIsLeapEndMonth = 0;
                }
            }
            boolean z = SemDatePicker.this.mStartDate == null || SemDatePicker.this.mEndDate == null || !SemDatePicker.this.mStartDate.after(SemDatePicker.this.mEndDate);
            SemDatePicker.this.onValidationChanged(z);
            SemDatePicker.this.updateSimpleMonthView(false);
            SemDatePicker.this.onDateChanged(true, true);
        }
    }

    class C02726 implements OnClickListener {
        C02726() {
        }

        public void onClick(View view) {
            SemDatePicker.this.setCurrentView((SemDatePicker.this.mCurrentView + 1) % 2);
        }
    }

    class C02737 implements OnFocusChangeListener {
        C02737() {
        }

        public void onFocusChange(View view, boolean z) {
            if (z && SemDatePicker.this.mCurrentView == 1) {
                SemDatePicker.this.setEditTextMode(false);
            }
        }
    }

    class C02748 implements Runnable {
        C02748() {
        }

        public void run() {
            SemDatePicker.this.updateSimpleMonthView(false);
        }
    }

    class C02759 implements Runnable {
        C02759() {
        }

        public void run() {
            SemDatePicker.this.updateSimpleMonthView(false);
        }
    }

    private class CalendarPageChangeListener implements OnPageChangeListener {
        private CalendarPageChangeListener() {
        }

        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
            if (SemDatePicker.this.mIsRTL) {
                SemDatePicker.this.mIsConfigurationChanged = false;
            }
            if (SemDatePicker.this.mIsFromSetLunar) {
                SemDatePicker.this.mIsFromSetLunar = false;
                return;
            }
            SemDatePicker.this.mCurrentPosition = i;
            int minMonth = i + SemDatePicker.this.getMinMonth();
            int minYear = (minMonth / 12) + SemDatePicker.this.getMinYear();
            int i2 = minMonth % 12;
            int i3 = SemDatePicker.this.mCurrentDate.get(5);
            if (SemDatePicker.this.mIsLunar) {
                LunarDate -wrap0 = SemDatePicker.this.getLunarDateByPosition(i);
                minYear = -wrap0.year;
                i2 = -wrap0.month;
                i3 = SemDatePicker.this.mLunarCurrentDay;
                SemDatePicker.this.mIsLeapMonth = -wrap0.isLeapMonth;
            }
            boolean z = false;
            if (minYear != SemDatePicker.this.mTempDate.get(1)) {
                z = true;
            }
            SemDatePicker.this.mTempDate.set(1, minYear);
            SemDatePicker.this.mTempDate.set(2, i2);
            SemDatePicker.this.mTempDate.set(5, 1);
            if (i3 > SemDatePicker.this.mTempDate.getActualMaximum(5)) {
                i3 = SemDatePicker.this.mTempDate.getActualMaximum(5);
            }
            SemDatePicker.this.mTempDate.set(5, i3);
            Message obtainMessage = SemDatePicker.this.mHandler.obtainMessage();
            obtainMessage.what = 1000;
            obtainMessage.obj = Boolean.valueOf(z);
            SemDatePicker.this.mHandler.sendMessage(obtainMessage);
            Message obtainMessage2 = SemDatePicker.this.mHandler.obtainMessage();
            obtainMessage2.what = 1001;
            SemDatePicker.this.mHandler.sendMessage(obtainMessage2);
            SparseArray sparseArray = SemDatePicker.this.mCalendarPagerAdapter.views;
            if (sparseArray.get(i) != null) {
                ((SemSimpleMonthView) sparseArray.get(i)).clearAccessibilityFocus();
            }
            if (!(i == 0 || sparseArray.get(i - 1) == null)) {
                ((SemSimpleMonthView) sparseArray.get(i - 1)).clearAccessibilityFocus();
            }
            if (!(i == SemDatePicker.this.mPositionCount - 1 || sparseArray.get(i + 1) == null)) {
                ((SemSimpleMonthView) sparseArray.get(i + 1)).clearAccessibilityFocus();
            }
        }
    }

    private class CalendarPagerAdapter extends PagerAdapter {
        SparseArray<SemSimpleMonthView> views = new SparseArray();

        public void destroyItem(View view, int i, Object obj) {
            SemDatePicker.this.semLog("destroyItem : " + i);
            ((ViewPager) view).removeView((View) obj);
            this.views.remove(i);
        }

        public void finishUpdate(View view) {
            SemDatePicker.this.semLog("finishUpdate");
        }

        public int getCount() {
            SemDatePicker.this.mPositionCount = ((SemDatePicker.this.getMaxMonth() - SemDatePicker.this.getMinMonth()) + 1) + ((SemDatePicker.this.getMaxYear() - SemDatePicker.this.getMinYear()) * 12);
            if (SemDatePicker.this.mIsLunar) {
                SemDatePicker.this.mPositionCount = SemDatePicker.this.getTotalMonthCountWithLeap(SemDatePicker.this.getMaxYear());
            }
            return SemDatePicker.this.mPositionCount;
        }

        public int getItemPosition(Object obj) {
            return -2;
        }

        public Object instantiateItem(View view, int i) {
            View semSimpleMonthView = new SemSimpleMonthView(SemDatePicker.this.mContext);
            SemDatePicker.this.semLog("instantiateItem : " + i);
            semSimpleMonthView.setClickable(true);
            semSimpleMonthView.setOnDayClickListener(SemDatePicker.this);
            semSimpleMonthView.setTextColor(SemDatePicker.this.mCalendarTextColor);
            int minMonth = i + SemDatePicker.this.getMinMonth();
            int minYear = (minMonth / 12) + SemDatePicker.this.getMinYear();
            int i2 = minMonth % 12;
            boolean z = false;
            if (SemDatePicker.this.mIsLunar) {
                LunarDate -wrap0 = SemDatePicker.this.getLunarDateByPosition(i);
                minYear = -wrap0.year;
                i2 = -wrap0.month;
                z = -wrap0.isLeapMonth;
            }
            int i3 = -1;
            if (SemDatePicker.this.mCurrentDate.get(1) == minYear && SemDatePicker.this.mCurrentDate.get(2) == i2) {
                i3 = SemDatePicker.this.mCurrentDate.get(5);
            }
            if (SemDatePicker.this.mIsLunar) {
                i3 = -1;
                if (SemDatePicker.this.mLunarCurrentYear == minYear && SemDatePicker.this.mLunarCurrentMonth == i2) {
                    i3 = SemDatePicker.this.mLunarCurrentDay;
                }
            }
            if (SemDatePicker.this.mIsLunarSupported) {
                semSimpleMonthView.setLunar(SemDatePicker.this.mIsLunar, z, SemDatePicker.this.mPathClassLoader);
            }
            int i4 = SemDatePicker.this.mStartDate.get(1);
            int i5 = SemDatePicker.this.mStartDate.get(2);
            int i6 = SemDatePicker.this.mStartDate.get(5);
            int i7 = SemDatePicker.this.mEndDate.get(1);
            int i8 = SemDatePicker.this.mEndDate.get(2);
            int i9 = SemDatePicker.this.mEndDate.get(5);
            if (SemDatePicker.this.mIsLunar) {
                i4 = SemDatePicker.this.mLunarStartYear;
                i5 = SemDatePicker.this.mLunarStartMonth;
                i6 = SemDatePicker.this.mLunarStartDay;
                i7 = SemDatePicker.this.mLunarEndYear;
                i8 = SemDatePicker.this.mLunarEndMonth;
                i9 = SemDatePicker.this.mLunarEndDay;
            }
            semSimpleMonthView.setMonthParams(i3, i2, minYear, SemDatePicker.this.getFirstDayOfWeek(), 1, 31, SemDatePicker.this.mMinDate, SemDatePicker.this.mMaxDate, i4, i5, i6, SemDatePicker.this.mIsLeapStartMonth, i7, i8, i9, SemDatePicker.this.mIsLeapEndMonth, SemDatePicker.this.mMode);
            SemDatePicker.this.mNumDays = semSimpleMonthView.getNumDays();
            SemDatePicker.this.mWeekStart = semSimpleMonthView.getWeekStart();
            ((ViewPager) view).addView(semSimpleMonthView, 0);
            this.views.put(i, semSimpleMonthView);
            return semSimpleMonthView;
        }

        public boolean isViewFromObject(View view, Object obj) {
            SemDatePicker.this.semLog("isViewFromObject : " + (view == obj));
            return view == obj;
        }

        public Parcelable saveState() {
            return null;
        }

        public void startUpdate(View view) {
            SemDatePicker.this.semLog("startUpdate");
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
            if (this.mIncrement) {
                SemDatePicker.this.mCalendarViewPager.setCurrentItem(SemDatePicker.this.mCurrentPosition + 1);
            } else {
                SemDatePicker.this.mCalendarViewPager.setCurrentItem(SemDatePicker.this.mCurrentPosition - 1);
            }
            SemDatePicker.this.postDelayed(this, SemDatePicker.this.mLongPressUpdateInterval);
        }
    }

    public class DayOfTheWeekView extends View {
        private int[] mDayColorSet = new int[7];
        private Calendar mDayLabelCalendar = Calendar.getInstance();
        private String mDefaultWeekdayFeatureString = "XXXXXXR";
        private Paint mMonthDayLabelPaint;
        private int mNormalDayTextColor;
        private int mSaturdayTextColor;
        private int mSundayTextColor;
        private String mWeekdayFeatureString;

        public DayOfTheWeekView(Context context) {
            super(context);
            Resources resources = context.getResources();
            String string = resources.getString(17040835);
            this.mNormalDayTextColor = resources.getColor(17170815);
            this.mSundayTextColor = resources.getColor(17170816);
            this.mSaturdayTextColor = resources.getColor(17170817);
            int dimensionPixelSize = resources.getDimensionPixelSize(17105686);
            this.mWeekdayFeatureString = SemCscFeature.getInstance().getString(CscFeatureTagCalendar.TAG_CSCFEATURE_CALENDAR_SETCOLOROFDAYS, this.mDefaultWeekdayFeatureString);
            this.mMonthDayLabelPaint = new Paint();
            this.mMonthDayLabelPaint.setAntiAlias(true);
            this.mMonthDayLabelPaint.setColor(this.mNormalDayTextColor);
            this.mMonthDayLabelPaint.setTextSize((float) dimensionPixelSize);
            this.mMonthDayLabelPaint.setTypeface(Typeface.create(string, 0));
            this.mMonthDayLabelPaint.setTextAlign(Align.CENTER);
            this.mMonthDayLabelPaint.setStyle(Style.FILL);
            this.mMonthDayLabelPaint.setFakeBoldText(false);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (SemDatePicker.this.mNumDays != 0) {
                int i;
                SemDatePicker.this.mDayHeight = SemDatePicker.this.mDayOfTheWeekLayoutHeight;
                int -get11 = (SemDatePicker.this.mDayHeight * 2) / 3;
                int -get13 = SemDatePicker.this.mDayOfTheWeekLayoutWidth / (SemDatePicker.this.mNumDays * 2);
                for (i = 0; i < SemDatePicker.this.mNumDays; i++) {
                    char charAt = this.mWeekdayFeatureString.charAt(i);
                    int -get36 = (i + 2) % SemDatePicker.this.mNumDays;
                    if (charAt == 'R') {
                        this.mDayColorSet[-get36] = this.mSundayTextColor;
                    } else if (charAt == 'B') {
                        this.mDayColorSet[-get36] = this.mSaturdayTextColor;
                    } else {
                        this.mDayColorSet[-get36] = this.mNormalDayTextColor;
                    }
                }
                for (i = 0; i < SemDatePicker.this.mNumDays; i++) {
                    int -get42 = (SemDatePicker.this.mWeekStart + i) % SemDatePicker.this.mNumDays;
                    this.mDayLabelCalendar.set(7, -get42);
                    String toUpperCase = SemDatePicker.this.mDayFormatter.format(this.mDayLabelCalendar.getTime()).toUpperCase();
                    int -get362 = SemDatePicker.this.mIsRTL ? (((((SemDatePicker.this.mNumDays - 1) - i) * 2) + 1) * -get13) + SemDatePicker.this.mPadding : (((i * 2) + 1) * -get13) + SemDatePicker.this.mPadding;
                    this.mMonthDayLabelPaint.setColor(this.mDayColorSet[-get42]);
                    canvas.drawText(toUpperCase, (float) -get362, (float) -get11, this.mMonthDayLabelPaint);
                }
            }
        }
    }

    public static class LunarDate {
        public int day;
        public boolean isLeapMonth;
        public int month;
        public int year;

        public LunarDate() {
            this.year = 1900;
            this.month = 1;
            this.day = 1;
            this.isLeapMonth = false;
        }

        public LunarDate(int i, int i2, int i3, boolean z) {
            this.year = i;
            this.month = i2;
            this.day = i3;
            this.isLeapMonth = z;
        }

        public void set(int i, int i2, int i3, boolean z) {
            this.year = i;
            this.month = i2;
            this.day = i3;
            this.isLeapMonth = z;
        }
    }

    public static class LunarUtils {
        private static PathClassLoader mClassLoader = null;

        public static PathClassLoader getPathClassLoader(Context context) {
            if (mClassLoader == null) {
                try {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(SemDatePicker.getCalendarPackageName(), 128);
                    if (applicationInfo == null) {
                        Log.e(SemDatePicker.TAG, "getPathClassLoader, appInfo is null");
                        return null;
                    }
                    Object obj = applicationInfo.sourceDir;
                    if (obj == null || TextUtils.isEmpty(obj)) {
                        Log.e(SemDatePicker.TAG, "getPathClassLoader, calendar package source directory is null or empty");
                        return null;
                    }
                    mClassLoader = new PathClassLoader(obj, ClassLoader.getSystemClassLoader());
                } catch (NameNotFoundException e) {
                    Log.e(SemDatePicker.TAG, "getPathClassLoader, calendar package name not found");
                    return null;
                }
            }
            return mClassLoader;
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(SemDatePicker semDatePicker, int i, int i2, int i3);
    }

    public interface OnEditTextModeChangedListener {
        void onEditTextModeChanged(SemDatePicker semDatePicker, boolean z);
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new C02761();
        private final int mListPosition;
        private final long mMaxDate;
        private final long mMinDate;
        private final int mSelectedDay;
        private final int mSelectedMonth;
        private final int mSelectedYear;

        static class C02761 implements Creator<SavedState> {
            C02761() {
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
            this.mSelectedYear = parcel.readInt();
            this.mSelectedMonth = parcel.readInt();
            this.mSelectedDay = parcel.readInt();
            this.mMinDate = parcel.readLong();
            this.mMaxDate = parcel.readLong();
            this.mListPosition = parcel.readInt();
        }

        private SavedState(Parcelable parcelable, int i, int i2, int i3, long j, long j2, int i4) {
            super(parcelable);
            this.mSelectedYear = i;
            this.mSelectedMonth = i2;
            this.mSelectedDay = i3;
            this.mMinDate = j;
            this.mMaxDate = j2;
            this.mListPosition = i4;
        }

        public int getListPosition() {
            return this.mListPosition;
        }

        public long getMaxDate() {
            return this.mMaxDate;
        }

        public long getMinDate() {
            return this.mMinDate;
        }

        public int getSelectedDay() {
            return this.mSelectedDay;
        }

        public int getSelectedMonth() {
            return this.mSelectedMonth;
        }

        public int getSelectedYear() {
            return this.mSelectedYear;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mSelectedYear);
            parcel.writeInt(this.mSelectedMonth);
            parcel.writeInt(this.mSelectedDay);
            parcel.writeLong(this.mMinDate);
            parcel.writeLong(this.mMaxDate);
            parcel.writeInt(this.mListPosition);
        }
    }

    public interface ValidationCallback {
        void onValidationChanged(boolean z);
    }

    public SemDatePicker(Context context) {
        this(context, null);
    }

    public SemDatePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843612);
    }

    public SemDatePicker(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SemDatePicker(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mLongPressUpdateInterval = DEFAULT_LONG_PRESS_UPDATE_INTERVAL;
        this.mCurrentView = -1;
        this.mIsEnabled = true;
        this.mIsFromSystem = false;
        this.mFirstDayOfWeek = 0;
        this.mOldSelectedDay = -1;
        this.mPadding = 0;
        this.mIsFirstMeasure = true;
        this.mIsConfigurationChanged = false;
        this.mBtnFocusChangeListener = new C02671();
        this.mBackgroundBorderlessResId = -1;
        this.mMode = 0;
        this.mIsLunarSupported = false;
        this.mIsLunar = false;
        this.mIsLeapMonth = false;
        this.mIsFromSetLunar = false;
        this.mLunarChanged = false;
        this.mPathClassLoader = null;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 1000:
                        if (SemDatePicker.this.mTempDate.get(1) <= SemDatePicker.this.getMaxYear() && SemDatePicker.this.mTempDate.get(1) >= SemDatePicker.this.getMinYear()) {
                            CharSequence spannableString = new SpannableString(SemDatePicker.this.getMonthAndYearString(SemDatePicker.this.mTempDate).toUpperCase());
                            spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                            SemDatePicker.this.mCalendarHeaderText.setText(spannableString);
                            SemDatePicker.this.mCalendarHeaderText.setContentDescription(spannableString + ", " + (SemDatePicker.this.mCurrentView == 0 ? SemDatePicker.this.mContext.getString(17041653) : SemDatePicker.this.mContext.getString(17041654)));
                            break;
                        }
                        return;
                        break;
                    case 1001:
                        if (SemDatePicker.this.mCurrentView == 1) {
                            SemDatePicker.this.mPrevButton.setAlpha(0.0f);
                            SemDatePicker.this.mNextButton.setAlpha(0.0f);
                            SemDatePicker.this.mPrevButton.setBackground(null);
                            SemDatePicker.this.mNextButton.setBackground(null);
                            SemDatePicker.this.mPrevButton.setEnabled(false);
                            SemDatePicker.this.mNextButton.setEnabled(false);
                            SemDatePicker.this.mPrevButton.setFocusable(false);
                            SemDatePicker.this.mNextButton.setFocusable(false);
                            SemDatePicker.this.mPrevButton.semSetHoverPopupType(0);
                            SemDatePicker.this.mNextButton.semSetHoverPopupType(0);
                            return;
                        }
                        SemDatePicker.this.mPrevButton.semSetHoverPopupType(1);
                        SemDatePicker.this.mNextButton.semSetHoverPopupType(1);
                        if (SemDatePicker.this.mCurrentPosition <= 0 || SemDatePicker.this.mCurrentPosition >= SemDatePicker.this.mPositionCount - 1) {
                            if (SemDatePicker.this.mPositionCount != 1) {
                                if (SemDatePicker.this.mCurrentPosition != 0) {
                                    if (SemDatePicker.this.mCurrentPosition == SemDatePicker.this.mPositionCount - 1) {
                                        SemDatePicker.this.mNextButton.setBackground(null);
                                        SemDatePicker.this.mPrevButton.setBackgroundResource(SemDatePicker.this.mBackgroundBorderlessResId);
                                        SemDatePicker.this.mNextButton.setAlpha(0.4f);
                                        SemDatePicker.this.mNextButton.setEnabled(false);
                                        SemDatePicker.this.mNextButton.setFocusable(false);
                                        SemDatePicker.this.mPrevButton.setAlpha(1.0f);
                                        SemDatePicker.this.mPrevButton.setEnabled(true);
                                        SemDatePicker.this.mPrevButton.setFocusable(true);
                                        SemDatePicker.this.removeAllCallbacks();
                                        break;
                                    }
                                }
                                SemDatePicker.this.mPrevButton.setBackground(null);
                                SemDatePicker.this.mNextButton.setBackgroundResource(SemDatePicker.this.mBackgroundBorderlessResId);
                                SemDatePicker.this.mPrevButton.setAlpha(0.4f);
                                SemDatePicker.this.mPrevButton.setEnabled(false);
                                SemDatePicker.this.mPrevButton.setFocusable(false);
                                SemDatePicker.this.mNextButton.setAlpha(1.0f);
                                SemDatePicker.this.mNextButton.setEnabled(true);
                                SemDatePicker.this.mNextButton.setFocusable(true);
                                SemDatePicker.this.removeAllCallbacks();
                                break;
                            }
                            SemDatePicker.this.mPrevButton.setAlpha(0.4f);
                            SemDatePicker.this.mNextButton.setAlpha(0.4f);
                            SemDatePicker.this.mPrevButton.setBackground(null);
                            SemDatePicker.this.mNextButton.setBackground(null);
                            SemDatePicker.this.mPrevButton.setEnabled(false);
                            SemDatePicker.this.mNextButton.setEnabled(false);
                            SemDatePicker.this.mPrevButton.setFocusable(false);
                            SemDatePicker.this.mNextButton.setFocusable(false);
                            SemDatePicker.this.removeAllCallbacks();
                            return;
                        }
                        SemDatePicker.this.mPrevButton.setAlpha(1.0f);
                        SemDatePicker.this.mNextButton.setAlpha(1.0f);
                        SemDatePicker.this.mPrevButton.setBackgroundResource(SemDatePicker.this.mBackgroundBorderlessResId);
                        SemDatePicker.this.mNextButton.setBackgroundResource(SemDatePicker.this.mBackgroundBorderlessResId);
                        SemDatePicker.this.mPrevButton.setEnabled(true);
                        SemDatePicker.this.mNextButton.setEnabled(true);
                        SemDatePicker.this.mPrevButton.setFocusable(true);
                        SemDatePicker.this.mNextButton.setFocusable(true);
                        return;
                        break;
                }
            }
        };
        this.mMonthTouchListener = new C02693();
        this.mMonthKeyListener = new C02704();
        this.mIsRTL = isRTL();
        this.mIsFarsiLanguage = isFarsiLanguage();
        this.mIsSimplifiedChinese = isSimplifiedChinese();
        if (this.mIsSimplifiedChinese) {
            this.mDayFormatter = new SimpleDateFormat("EEEEE", Locale.getDefault());
        } else {
            this.mDayFormatter = new SimpleDateFormat("EEE", Locale.getDefault());
        }
        Locale locale = Locale.getDefault();
        this.mMinDate = getCalendarForLocale(this.mMinDate, locale);
        this.mMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        this.mTempMinMaxDate = getCalendarForLocale(this.mMaxDate, locale);
        this.mCurrentDate = getCalendarForLocale(this.mCurrentDate, locale);
        this.mTempDate = getCalendarForLocale(this.mCurrentDate, locale);
        setLocale(locale);
        Resources resources = getResources();
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, R.styleable.DatePicker, i, i2);
        this.mStartYear = obtainStyledAttributes.getInt(1, DEFAULT_START_YEAR);
        this.mEndYear = obtainStyledAttributes.getInt(2, 2100);
        this.mMinDate.set(this.mStartYear, 0, 1);
        this.mMaxDate.set(this.mEndYear, 11, 31);
        ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(17367294, this, true);
        int i3 = obtainStyledAttributes.getInt(3, 0);
        if (i3 != 0) {
            setFirstDayOfWeek(i3);
        }
        int highlightColor = new TextView(context).getHighlightColor();
        this.mCalendarTextColor = obtainStyledAttributes.getColorStateList(15);
        obtainStyledAttributes.recycle();
        this.mCalendarViewPager = (ViewPager) findViewById(16909494);
        this.mCalendarPagerAdapter = new CalendarPagerAdapter();
        this.mCalendarViewPager.setAdapter(this.mCalendarPagerAdapter);
        SemDatePicker semDatePicker = this;
        this.mCalendarViewPager.setOnPageChangeListener(new CalendarPageChangeListener());
        this.mPadding = resources.getDimensionPixelOffset(17105682);
        this.mCalendarHeader = (RelativeLayout) findViewById(16909485);
        this.mCalendarHeaderText = (TextView) findViewById(16909487);
        this.mStartDate = getCalendarForLocale(this.mCurrentDate, Locale.getDefault());
        this.mEndDate = getCalendarForLocale(this.mCurrentDate, Locale.getDefault());
        this.mAnimator = (ViewAnimator) findViewById(16909489);
        this.mSpinnerLayout = (SemDatePickerSpinnerLayout) findViewById(16909495);
        this.mSpinnerLayout.setOnSpinnerDateChangedListener(new C02715());
        this.mCurrentView = 0;
        this.mCalendarHeaderText.setOnClickListener(new C02726());
        this.mCalendarHeaderText.setOnFocusChangeListener(new C02737());
        this.mDayOfTheWeekLayoutHeight = resources.getDimensionPixelOffset(17105691);
        checkMaxFontSize();
        this.mCalendarViewPagerWidth = resources.getDimensionPixelOffset(17105683);
        this.mCalendarViewMargin = resources.getDimensionPixelOffset(17105709);
        this.mDayOfTheWeekLayoutWidth = resources.getDimensionPixelOffset(17105683);
        this.mDayOfTheWeekLayout = (LinearLayout) findViewById(16909492);
        this.mDayOfTheWeekView = new DayOfTheWeekView(this.mContext);
        this.mDayOfTheWeekLayout.addView(this.mDayOfTheWeekView);
        this.mDatePickerLayout = (LinearLayout) findViewById(16909483);
        this.mCalendarHeaderLayout = (RelativeLayout) findViewById(16909484);
        if (this.mIsRTL) {
            this.mPrevButton = (ImageButton) findViewById(16909488);
            this.mNextButton = (ImageButton) findViewById(16909486);
            this.mPrevButton.setContentDescription(this.mContext.getString(17041651));
            this.mNextButton.setContentDescription(this.mContext.getString(17041652));
        } else {
            this.mPrevButton = (ImageButton) findViewById(16909486);
            this.mNextButton = (ImageButton) findViewById(16909488);
        }
        this.mPrevButton.setOnClickListener(this);
        this.mNextButton.setOnClickListener(this);
        this.mPrevButton.setOnLongClickListener(this);
        this.mNextButton.setOnLongClickListener(this);
        this.mPrevButton.setOnTouchListener(this.mMonthTouchListener);
        this.mNextButton.setOnTouchListener(this.mMonthTouchListener);
        this.mPrevButton.setOnKeyListener(this.mMonthKeyListener);
        this.mNextButton.setOnKeyListener(this.mMonthKeyListener);
        this.mPrevButton.setOnFocusChangeListener(this.mBtnFocusChangeListener);
        this.mNextButton.setOnFocusChangeListener(this.mBtnFocusChangeListener);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843868, typedValue, true);
        this.mBackgroundBorderlessResId = typedValue.resourceId;
        this.mCalendarHeaderLayoutHeight = resources.getDimensionPixelOffset(17105695);
        this.mCalendarViewPagerHeight = resources.getDimensionPixelOffset(17105696);
        this.mOldCalendarViewPagerWidth = this.mCalendarViewPagerWidth;
        this.mCalendarHeaderText.setFocusable(true);
        this.mPrevButton.setNextFocusRightId(16909487);
        this.mNextButton.setNextFocusLeftId(16909487);
        this.mCalendarHeaderText.setNextFocusRightId(16909488);
        this.mCalendarHeaderText.setNextFocusLeftId(16909486);
        this.mCalendarView = new SemSimpleMonthView(this.mContext);
        this.mCalendarView.setTextColor(this.mCalendarTextColor);
        this.mCalendarView.setClickable(true);
        this.mCalendarView.setOnDayClickListener(this);
        this.mFirstBlankSpace = findViewById(16909491);
        this.mSecondBlankSpace = findViewById(16909493);
        this.mFirstBlankSpaceHeight = resources.getDimensionPixelOffset(17105714);
        this.mSecondBlankSpaceHeight = resources.getDimensionPixelOffset(17105715);
        this.mSpinnerLayoutHeight = resources.getDimensionPixelOffset(17105710);
        this.mSpinnerLayoutTopMargin = resources.getDimensionPixelOffset(17105711);
        this.mSpinnerLayoutBottomMargin = resources.getDimensionPixelOffset(17105712);
        this.mDatePickerHeight = (((this.mCalendarHeaderLayoutHeight + this.mFirstBlankSpaceHeight) + this.mDayOfTheWeekLayoutHeight) + this.mSecondBlankSpaceHeight) + this.mCalendarViewPagerHeight;
        updateSimpleMonthView(true);
    }

    private void checkMaxFontSize() {
        float f = this.mContext.getResources().getConfiguration().fontScale;
        this.mCalendarHeaderTextSize = getResources().getDimensionPixelOffset(17105694);
        if (f > SShareConstants.MAX_FONT_SCALE) {
            this.mCalendarHeaderText.setTextSize(0, (float) Math.floor(Math.ceil((double) (((float) this.mCalendarHeaderTextSize) / f)) * 1.2000000476837158d));
        }
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

    public static String getCalendarPackageName() {
        String str = "com.android.calendar";
        String string = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_CALENDAR_CONFIG_PACKAGE_NAME", "com.android.calendar");
        if (str.equals(string)) {
            return string;
        }
        try {
            mPackageManager.getPackageInfo(string, 0);
            return string;
        } catch (NameNotFoundException e) {
            return str;
        }
    }

    public static int getDaysInMonth(int i, int i2) {
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
                return i2 % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private <T> Field getField(Class<T> cls, String str) {
        Field field = null;
        try {
            field = cls.getField(str);
        } catch (Throwable e) {
            Log.e(TAG, str + " NoSuchMethodException", e);
        }
        return field;
    }

    private int getIndexOfleapMonthOfYear(int i) {
        if (this.mSolarLunarTables == null) {
            return CustomDeviceManagerProxy.SENSOR_ALL;
        }
        Integer object = getObject(this.mSolarLunarTables, this.mStartOfLunarYearField);
        Integer object2 = getObject(this.mSolarLunarTables, this.mWidthPerYearField);
        Integer object3 = getObject(this.mSolarLunarTables, this.mIndexOfLeapMonthField);
        if ((object instanceof Integer) && (object2 instanceof Integer) && (object3 instanceof Integer)) {
            int intValue = (i - object.intValue()) * object2.intValue();
            Byte invoke = invoke(this.mSolarLunarTables, this.mGetLunarMethod, Integer.valueOf(object3.intValue() + intValue));
            int i2 = CustomDeviceManagerProxy.SENSOR_ALL;
            if (invoke instanceof Byte) {
                i2 = invoke.byteValue();
            }
            return i2;
        }
        Log.e(TAG, "getIndexOfleapMonthOfYear, not Integer");
        return CustomDeviceManagerProxy.SENSOR_ALL;
    }

    private LunarDate getLunarDateByPosition(int i) {
        LunarDate lunarDate = new LunarDate();
        int minYear = getMinYear();
        int i2 = 0;
        boolean z = false;
        for (int minYear2 = getMinYear(); minYear2 <= getMaxYear(); minYear2++) {
            if (i < getTotalMonthCountWithLeap(minYear2)) {
                minYear = minYear2;
                int totalMonthCountWithLeap = i - (minYear == getMinYear() ? -getMinMonth() : getTotalMonthCountWithLeap(minYear2 - 1));
                int indexOfleapMonthOfYear = getIndexOfleapMonthOfYear(minYear);
                int i3 = indexOfleapMonthOfYear > 12 ? 12 : 13;
                i2 = totalMonthCountWithLeap < indexOfleapMonthOfYear ? totalMonthCountWithLeap : totalMonthCountWithLeap - 1;
                z = i3 == 13 && indexOfleapMonthOfYear == totalMonthCountWithLeap;
                lunarDate.set(minYear, i2, 1, z);
                return lunarDate;
            }
        }
        lunarDate.set(minYear, i2, 1, z);
        return lunarDate;
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

    private String getMonthAndYearString(Calendar calendar) {
        if (this.mIsFarsiLanguage) {
            return new SimpleDateFormat("LLLL y", Locale.getDefault()).format(calendar.getTime());
        }
        Appendable stringBuilder = new StringBuilder(50);
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());
        stringBuilder.setLength(0);
        long timeInMillis = calendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), formatter, timeInMillis, timeInMillis, 36, Time.getCurrentTimezone()).toString();
    }

    private Object getObject(Object obj, Field field) {
        if (field == null) {
            Log.e(TAG, "field is null");
            return null;
        }
        try {
            return field.get(obj);
        } catch (Throwable e) {
            Log.e(TAG, field.getName() + " IllegalAccessException", e);
            return null;
        } catch (Throwable e2) {
            Log.e(TAG, field.getName() + " IllegalArgumentException", e2);
            return null;
        }
    }

    private int getTotalMonthCountWithLeap(int i) {
        return (this.mTotalMonthCountWithLeap == null || i < getMinYear()) ? 0 : this.mTotalMonthCountWithLeap[i - getMinYear()];
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

    private boolean isFarsiLanguage() {
        return "fa".equals(Locale.getDefault().getLanguage());
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

    private boolean isSimplifiedChinese() {
        return Locale.getDefault().getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage()) ? Locale.getDefault().getCountry().equals(Locale.SIMPLIFIED_CHINESE.getCountry()) : false;
    }

    private boolean isYearSpinnerAtLeft() {
        return DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd").startsWith("y");
    }

    private int makeMeasureSpec(int i, int i2) {
        if (i2 == -1) {
            return i;
        }
        int dimensionPixelSize;
        int mode = MeasureSpec.getMode(i);
        if (mode == FingerprintManager.PRIVILEGED_TYPE_KEYGUARD) {
            int i3 = getResources().getConfiguration().smallestScreenWidthDp;
            dimensionPixelSize = i3 >= 600 ? getResources().getDimensionPixelSize(17105685) : (int) (TypedValue.applyDimension(1, (float) i3, getResources().getDisplayMetrics()) + 0.5f);
        } else {
            dimensionPixelSize = MeasureSpec.getSize(i);
        }
        switch (mode) {
            case FingerprintManager.PRIVILEGED_TYPE_KEYGUARD /*-2147483648*/:
                this.mCalendarViewPagerWidth = dimensionPixelSize - (this.mCalendarViewMargin * 2);
                this.mDayOfTheWeekLayoutWidth = dimensionPixelSize - (this.mCalendarViewMargin * 2);
                i = MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824);
                break;
            case 0:
                i = MeasureSpec.makeMeasureSpec(i2, 1073741824);
                break;
            case 1073741824:
                this.mCalendarViewPagerWidth = dimensionPixelSize - (this.mCalendarViewMargin * 2);
                this.mDayOfTheWeekLayoutWidth = dimensionPixelSize - (this.mCalendarViewMargin * 2);
                break;
            default:
                throw new IllegalArgumentException("Unknown measure mode: " + mode);
        }
        return i;
    }

    private void onDateChanged(boolean z, boolean z2) {
        if (z2 && this.mOnDateChangedListener != null) {
            int i = this.mCurrentDate.get(1);
            int i2 = this.mCurrentDate.get(2);
            int i3 = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                i = this.mLunarCurrentYear;
                i2 = this.mLunarCurrentMonth;
                i3 = this.mLunarCurrentDay;
            }
            this.mOnDateChangedListener.onDateChanged(this, i, i2, i3);
        }
    }

    private void postChangeCurrentByOneFromLongPress(boolean z, long j) {
        if (this.mChangeCurrentByOneFromLongPressCommand == null) {
            this.mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
        }
        this.mChangeCurrentByOneFromLongPressCommand.setStep(z);
        postDelayed(this.mChangeCurrentByOneFromLongPressCommand, j);
    }

    private void removeAllCallbacks() {
        if (this.mChangeCurrentByOneFromLongPressCommand != null) {
            removeCallbacks(this.mChangeCurrentByOneFromLongPressCommand);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SemDatePicker.this.mCalendarViewPager.setCurrentItem(SemDatePicker.this.mCurrentPosition, false);
                }
            }, 200);
        }
    }

    private static Activity scanForActivity(Context context) {
        return context == null ? null : context instanceof Activity ? context : context instanceof ContextWrapper ? scanForActivity(context.getBaseContext()) : null;
    }

    private void semLog(String str) {
    }

    private void setCurrentView(int i) {
        Message obtainMessage;
        switch (i) {
            case 0:
                if (this.mCurrentView != i) {
                    this.mCalendarPagerAdapter.notifyDataSetChanged();
                    this.mSpinnerLayout.updateInputState();
                    this.mSpinnerLayout.setEditTextMode(false);
                    this.mAnimator.setDisplayedChild(0);
                    this.mSpinnerLayout.setVisibility(4);
                    this.mSpinnerLayout.setEnabled(false);
                    this.mCurrentView = i;
                    obtainMessage = this.mHandler.obtainMessage();
                    obtainMessage.what = 1000;
                    this.mHandler.sendMessage(obtainMessage);
                    break;
                }
                break;
            case 1:
                if (this.mCurrentView != i) {
                    if (this.mMode == 1) {
                        int i2 = this.mStartDate.get(1);
                        int i3 = this.mStartDate.get(2);
                        int i4 = this.mStartDate.get(5);
                        if (this.mIsLunar) {
                            i2 = this.mLunarStartYear;
                            i3 = this.mLunarStartMonth;
                            i4 = this.mLunarStartDay;
                        }
                        this.mSpinnerLayout.updateDate(i2, i3, i4);
                    } else if (this.mMode == 2) {
                        int i5 = this.mEndDate.get(1);
                        int i6 = this.mEndDate.get(2);
                        int i7 = this.mEndDate.get(5);
                        if (this.mIsLunar) {
                            i5 = this.mLunarEndYear;
                            i6 = this.mLunarEndMonth;
                            i7 = this.mLunarEndDay;
                        }
                        this.mSpinnerLayout.updateDate(i5, i6, i7);
                    } else {
                        int i8 = this.mCurrentDate.get(1);
                        int i9 = this.mCurrentDate.get(2);
                        int i10 = this.mCurrentDate.get(5);
                        if (this.mIsLunar) {
                            i8 = this.mLunarCurrentYear;
                            i9 = this.mLunarCurrentMonth;
                            i10 = this.mLunarCurrentDay;
                        }
                        this.mSpinnerLayout.updateDate(i8, i9, i10);
                    }
                    this.mAnimator.setDisplayedChild(1);
                    this.mSpinnerLayout.setEnabled(true);
                    this.mCurrentView = i;
                    obtainMessage = this.mHandler.obtainMessage();
                    obtainMessage.what = 1000;
                    this.mHandler.sendMessage(obtainMessage);
                    break;
                }
                break;
        }
        Message obtainMessage2 = this.mHandler.obtainMessage();
        obtainMessage2.what = 1001;
        this.mHandler.sendMessage(obtainMessage2);
    }

    private void setTotalMonthCountWithLeap() {
        if (this.mSolarLunarTables != null && this.mPathClassLoader != null) {
            int i = 0;
            this.mTotalMonthCountWithLeap = new int[((getMaxYear() - getMinYear()) + 1)];
            int minYear = getMinYear() == getMaxYear() ? getMinYear() : getMinYear();
            for (minYear = getMinYear(); minYear <= getMaxYear(); minYear++) {
                int i2;
                int minMonth;
                int indexOfleapMonthOfYear;
                if (minYear == getMinYear()) {
                    minMonth = getMinMonth() + 1;
                    indexOfleapMonthOfYear = getIndexOfleapMonthOfYear(minYear);
                    i2 = indexOfleapMonthOfYear <= 12 ? indexOfleapMonthOfYear < minMonth ? (12 - minMonth) + 1 : (13 - minMonth) + 1 : (12 - minMonth) + 1;
                } else if (minYear == getMaxYear()) {
                    minMonth = getMaxMonth() + 1;
                    indexOfleapMonthOfYear = getIndexOfleapMonthOfYear(minYear);
                    i2 = indexOfleapMonthOfYear <= 12 ? minMonth < indexOfleapMonthOfYear ? minMonth : minMonth + 1 : minMonth;
                } else {
                    i2 = getIndexOfleapMonthOfYear(minYear) > 12 ? 12 : 13;
                }
                i += i2;
                this.mTotalMonthCountWithLeap[minYear - getMinYear()] = i;
            }
        }
    }

    private void updateSimpleMonthView(boolean z) {
        int i = this.mCurrentDate.get(2);
        int i2 = this.mCurrentDate.get(1);
        if (this.mIsLunar) {
            i2 = this.mLunarCurrentYear;
            i = this.mLunarCurrentMonth;
        }
        if (this.mLunarChanged) {
            i = this.mTempDate.get(2);
            i2 = this.mTempDate.get(1);
        }
        int minYear = ((i2 - getMinYear()) * 12) + (i - getMinMonth());
        if (this.mIsLunar) {
            minYear = (i2 == getMinYear() ? -getMinMonth() : getTotalMonthCountWithLeap(i2 - 1)) + (i < getIndexOfleapMonthOfYear(i2) ? i : i + 1);
            if (this.mMode == 1 && i == this.mLunarStartMonth && this.mIsLeapStartMonth == 1) {
                minYear++;
            } else if (this.mMode == 2 && i == this.mLunarEndMonth && this.mIsLeapEndMonth == 1) {
                minYear++;
            } else if (this.mMode == 0 && this.mIsLeapMonth) {
                minYear++;
            }
        }
        this.mCurrentPosition = minYear;
        this.mCalendarViewPager.setCurrentItem(minYear, z);
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 1000;
        obtainMessage.obj = Boolean.valueOf(true);
        this.mHandler.sendMessage(obtainMessage);
        Message obtainMessage2 = this.mHandler.obtainMessage();
        obtainMessage2.what = 1001;
        this.mHandler.sendMessage(obtainMessage2);
    }

    private boolean usingNumericMonths() {
        return Character.isDigit(this.mShortMonths[0].charAt(0));
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    public int getDateMode() {
        return this.mMode;
    }

    public int getDayOfMonth() {
        return this.mIsLunar ? this.mLunarCurrentDay : this.mCurrentDate.get(5);
    }

    public EditText getEditText(int i) {
        return this.mSpinnerLayout.getEditText(i);
    }

    public Calendar getEndDate() {
        return this.mEndDate;
    }

    public int getFirstDayOfWeek() {
        return this.mFirstDayOfWeek != 0 ? this.mFirstDayOfWeek : this.mCurrentDate.getFirstDayOfWeek();
    }

    public boolean getHeaderViewShown() {
        return false;
    }

    public int[] getLunarEndDate() {
        return new int[]{this.mLunarEndYear, this.mLunarEndMonth, this.mLunarEndDay, this.mIsLeapEndMonth};
    }

    public int[] getLunarStartDate() {
        return new int[]{this.mLunarStartYear, this.mLunarStartMonth, this.mLunarStartDay, this.mIsLeapStartMonth};
    }

    public long getMaxDate() {
        return this.mMaxDate.getTimeInMillis();
    }

    public Calendar getMaxDateCalendar() {
        return this.mMaxDate;
    }

    public int getMaxDay() {
        return this.mMaxDate.get(5);
    }

    public int getMaxMonth() {
        return this.mMaxDate.get(2);
    }

    public int getMaxYear() {
        return this.mMaxDate.get(1);
    }

    public long getMinDate() {
        return this.mMinDate.getTimeInMillis();
    }

    public Calendar getMinDateCalendar() {
        return this.mMinDate;
    }

    public int getMinDay() {
        return this.mMinDate.get(5);
    }

    public int getMinMonth() {
        return this.mMinDate.get(2);
    }

    public int getMinYear() {
        return this.mMinDate.get(1);
    }

    public int getMonth() {
        return this.mIsLunar ? this.mLunarCurrentMonth : this.mCurrentDate.get(2);
    }

    public SemNumberPicker getNumberPicker(int i) {
        return this.mSpinnerLayout.getNumberPicker(i);
    }

    public Calendar getSelectedDay() {
        return this.mCurrentDate;
    }

    public Calendar getStartDate() {
        return this.mStartDate;
    }

    public int getYear() {
        return this.mIsLunar ? this.mLunarCurrentYear : this.mCurrentDate.get(1);
    }

    public void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener) {
        this.mCurrentDate.set(1, i);
        this.mCurrentDate.set(2, i2);
        this.mCurrentDate.set(5, i3);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = i;
            this.mLunarCurrentMonth = i2;
            this.mLunarCurrentDay = i3;
        }
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate = getCalendarForLocale(this.mMinDate, Locale.getDefault());
        }
        if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate = getCalendarForLocale(this.mMaxDate, Locale.getDefault());
        }
        this.mOnDateChangedListener = onDateChangedListener;
        updateSimpleMonthView(true);
        onDateChanged(false, true);
        this.mSpinnerLayout.setMinDate(this.mMinDate.getTimeInMillis());
        this.mSpinnerLayout.setMaxDate(this.mMaxDate.getTimeInMillis());
        if (this.mCurrentView == 0) {
            this.mSpinnerLayout.setVisibility(4);
            this.mSpinnerLayout.setEnabled(false);
        }
        this.mStartDate.clear();
        this.mStartDate.set(1, i);
        this.mStartDate.set(2, i2);
        this.mStartDate.set(5, i3);
        this.mEndDate.clear();
        this.mEndDate.set(1, i);
        this.mEndDate.set(2, i2);
        this.mEndDate.set(5, i3);
        if (this.mIsLunar) {
            this.mLunarStartYear = i;
            this.mLunarStartMonth = i2;
            this.mLunarStartDay = i3;
            this.mLunarEndYear = i;
            this.mLunarEndMonth = i2;
            this.mLunarEndDay = i3;
        }
    }

    public boolean isEditTextMode() {
        return this.mCurrentView == 0 ? false : this.mSpinnerLayout.isEditTextMode();
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public boolean isLeapMonth() {
        return this.mIsLeapMonth;
    }

    public boolean isLunar() {
        return this.mIsLunar;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 16909486:
                if (!this.mIsRTL) {
                    if (this.mCurrentPosition != 0) {
                        this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition - 1);
                        break;
                    }
                    return;
                } else if (this.mCurrentPosition != this.mPositionCount - 1) {
                    this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition + 1);
                    break;
                } else {
                    return;
                }
            case 16909488:
                if (!this.mIsRTL) {
                    if (this.mCurrentPosition != this.mPositionCount - 1) {
                        this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition + 1);
                        break;
                    }
                    return;
                } else if (this.mCurrentPosition != 0) {
                    this.mCalendarViewPager.setCurrentItem(this.mCurrentPosition - 1);
                    break;
                } else {
                    return;
                }
        }
    }

    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIsRTL = isRTL();
        this.mIsFarsiLanguage = isFarsiLanguage();
        this.mIsSimplifiedChinese = isSimplifiedChinese();
        if (this.mIsSimplifiedChinese) {
            this.mDayFormatter = new SimpleDateFormat("EEEEE", configuration.locale);
        } else {
            this.mDayFormatter = new SimpleDateFormat("EEE", configuration.locale);
        }
        Resources resources = this.mContext.getResources();
        this.mDatePickerLayout.setGravity(1);
        this.mIsFirstMeasure = true;
        this.mCalendarHeaderLayoutHeight = resources.getDimensionPixelOffset(17105695);
        this.mCalendarViewPagerHeight = resources.getDimensionPixelOffset(17105696);
        this.mDayOfTheWeekLayoutHeight = resources.getDimensionPixelOffset(17105691);
        this.mFirstBlankSpaceHeight = resources.getDimensionPixelOffset(17105714);
        this.mSecondBlankSpaceHeight = resources.getDimensionPixelOffset(17105715);
        this.mSpinnerLayoutHeight = resources.getDimensionPixelOffset(17105710);
        this.mSpinnerLayoutTopMargin = resources.getDimensionPixelOffset(17105711);
        this.mSpinnerLayoutBottomMargin = resources.getDimensionPixelOffset(17105712);
        this.mDatePickerHeight = (((this.mCalendarHeaderLayoutHeight + this.mFirstBlankSpaceHeight) + this.mDayOfTheWeekLayoutHeight) + this.mSecondBlankSpaceHeight) + this.mCalendarViewPagerHeight;
        LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, this.mSpinnerLayoutHeight);
        layoutParams.topMargin = this.mSpinnerLayoutTopMargin;
        layoutParams.bottomMargin = this.mSpinnerLayoutBottomMargin;
        this.mSpinnerLayout.setLayoutParams(layoutParams);
        if (this.mIsRTL) {
            this.mIsConfigurationChanged = true;
        }
        checkMaxFontSize();
    }

    public void onDayClick(SemSimpleMonthView semSimpleMonthView, int i, int i2, int i3) {
        int minDay;
        int maxDay;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        semLog("onDayClick day : " + i + ", " + i2 + ", " + i3);
        int i10 = this.mCurrentDate.get(1);
        int i11 = this.mCurrentDate.get(2);
        if (this.mIsLunar) {
            i10 = this.mLunarCurrentYear;
            i11 = this.mLunarCurrentMonth;
        }
        onDayOfMonthSelected(i, i2, i3);
        if (i == i10 && i2 == r22 && i3 == this.mOldSelectedDay) {
            if (this.mIsLunar) {
            }
            minDay = (getMinMonth() == i2 || getMinYear() != i) ? 1 : getMinDay();
            maxDay = (getMaxMonth() == i2 || getMaxYear() != i) ? 31 : getMaxDay();
            if (this.mIsLunarSupported) {
                semSimpleMonthView.setLunar(this.mIsLunar, this.mIsLeapMonth, this.mPathClassLoader);
            }
            i4 = this.mStartDate.get(1);
            i5 = this.mStartDate.get(2);
            i6 = this.mStartDate.get(5);
            i7 = this.mEndDate.get(1);
            i8 = this.mEndDate.get(2);
            i9 = this.mEndDate.get(5);
            if (this.mIsLunar) {
                i4 = this.mLunarStartYear;
                i5 = this.mLunarStartMonth;
                i6 = this.mLunarStartDay;
                i7 = this.mLunarEndYear;
                i8 = this.mLunarEndMonth;
                i9 = this.mLunarEndDay;
            }
            semSimpleMonthView.setMonthParams(i3, i2, i, getFirstDayOfWeek(), minDay, maxDay, this.mMinDate, this.mMaxDate, i4, i5, i6, this.mIsLeapStartMonth, i7, i8, i9, this.mIsLeapEndMonth, this.mMode);
            semSimpleMonthView.invalidate();
        }
        this.mOldSelectedDay = i3;
        this.mCalendarPagerAdapter.notifyDataSetChanged();
        if (getMinMonth() == i2) {
        }
        if (getMaxMonth() == i2) {
        }
        if (this.mIsLunarSupported) {
            semSimpleMonthView.setLunar(this.mIsLunar, this.mIsLeapMonth, this.mPathClassLoader);
        }
        i4 = this.mStartDate.get(1);
        i5 = this.mStartDate.get(2);
        i6 = this.mStartDate.get(5);
        i7 = this.mEndDate.get(1);
        i8 = this.mEndDate.get(2);
        i9 = this.mEndDate.get(5);
        if (this.mIsLunar) {
            i4 = this.mLunarStartYear;
            i5 = this.mLunarStartMonth;
            i6 = this.mLunarStartDay;
            i7 = this.mLunarEndYear;
            i8 = this.mLunarEndMonth;
            i9 = this.mLunarEndDay;
        }
        semSimpleMonthView.setMonthParams(i3, i2, i, getFirstDayOfWeek(), minDay, maxDay, this.mMinDate, this.mMaxDate, i4, i5, i6, this.mIsLeapStartMonth, i7, i8, i9, this.mIsLeapEndMonth, this.mMode);
        semSimpleMonthView.invalidate();
    }

    public void onDayOfMonthSelected(int i, int i2, int i3) {
        int i4 = 0;
        this.mCurrentDate.set(1, i);
        this.mCurrentDate.set(2, i2);
        this.mCurrentDate.set(5, i3);
        if (this.mIsLunar) {
            this.mLunarCurrentYear = i;
            this.mLunarCurrentMonth = i2;
            this.mLunarCurrentDay = i3;
        }
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 1000;
        this.mHandler.sendMessage(obtainMessage);
        if (this.mMode == 1) {
            this.mStartDate.clear();
            this.mStartDate.set(1, i);
            this.mStartDate.set(2, i2);
            this.mStartDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarStartYear = i;
                this.mLunarStartMonth = i2;
                this.mLunarStartDay = i3;
                if (this.mIsLeapMonth) {
                    i4 = 1;
                }
                this.mIsLeapStartMonth = i4;
            }
        } else if (this.mMode == 2) {
            this.mEndDate.clear();
            this.mEndDate.set(1, i);
            this.mEndDate.set(2, i2);
            this.mEndDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarEndYear = i;
                this.mLunarEndMonth = i2;
                this.mLunarEndDay = i3;
                if (this.mIsLeapMonth) {
                    i4 = 1;
                }
                this.mIsLeapEndMonth = i4;
            }
        } else {
            this.mStartDate.clear();
            this.mEndDate.clear();
            this.mStartDate.set(1, i);
            this.mStartDate.set(2, i2);
            this.mStartDate.set(5, i3);
            this.mEndDate.set(1, i);
            this.mEndDate.set(2, i2);
            this.mEndDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarStartYear = i;
                this.mLunarStartMonth = i2;
                this.mLunarStartDay = i3;
                this.mIsLeapStartMonth = this.mIsLeapMonth ? 1 : 0;
                this.mLunarEndYear = i;
                this.mLunarEndMonth = i2;
                this.mLunarEndDay = i3;
                if (this.mIsLeapMonth) {
                    i4 = 1;
                }
                this.mIsLeapEndMonth = i4;
            }
        }
        if (this.mMode != 0) {
            boolean z = this.mStartDate == null || this.mEndDate == null || !this.mStartDate.after(this.mEndDate);
            onValidationChanged(z);
        }
        onDateChanged(true, true);
    }

    protected void onDetachedFromWindow() {
        removeAllCallbacks();
        super.onDetachedFromWindow();
    }

    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case 16909486:
                if (this.mCurrentPosition != 0) {
                    postChangeCurrentByOneFromLongPress(false, (long) ViewConfiguration.getLongPressTimeout());
                    break;
                }
                break;
            case 16909488:
                if (this.mCurrentPosition != this.mPositionCount - 1) {
                    postChangeCurrentByOneFromLongPress(true, (long) ViewConfiguration.getLongPressTimeout());
                    break;
                }
                break;
        }
        return false;
    }

    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec = makeMeasureSpec(i, this.mCalendarViewPagerWidth);
        Activity scanForActivity = scanForActivity(this.mContext);
        if (scanForActivity != null && scanForActivity.isInMultiWindowMode()) {
            if (MeasureSpec.getSize(i2) < this.mDatePickerHeight) {
                setCurrentView(1);
                this.mCalendarHeaderText.setOnClickListener(null);
                this.mCalendarHeaderText.setClickable(false);
            } else if (!this.mCalendarHeaderText.hasOnClickListeners()) {
                this.mCalendarHeaderText.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        SemDatePicker.this.setCurrentView((SemDatePicker.this.mCurrentView + 1) % 2);
                    }
                });
                this.mCalendarHeaderText.setClickable(true);
            }
        }
        if (this.mIsFirstMeasure || this.mOldCalendarViewPagerWidth != this.mCalendarViewPagerWidth) {
            this.mIsFirstMeasure = false;
            this.mOldCalendarViewPagerWidth = this.mCalendarViewPagerWidth;
            this.mCalendarHeaderLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mCalendarHeaderLayoutHeight));
            this.mDayOfTheWeekLayout.setLayoutParams(new LinearLayout.LayoutParams(this.mDayOfTheWeekLayoutWidth, this.mDayOfTheWeekLayoutHeight));
            this.mDayOfTheWeekView.setLayoutParams(new LinearLayout.LayoutParams(this.mDayOfTheWeekLayoutWidth, this.mDayOfTheWeekLayoutHeight));
            this.mCalendarViewPager.setLayoutParams(new LinearLayout.LayoutParams(this.mCalendarViewPagerWidth, this.mCalendarViewPagerHeight));
            if (this.mIsRTL && this.mIsConfigurationChanged) {
                this.mCalendarViewPager.setConfigurationChanged(true);
            }
            this.mFirstBlankSpace.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mFirstBlankSpaceHeight));
            this.mSecondBlankSpace.setLayoutParams(new LinearLayout.LayoutParams(-1, this.mSecondBlankSpaceHeight));
            super.onMeasure(makeMeasureSpec, i2);
            return;
        }
        super.onMeasure(makeMeasureSpec, i2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(((BaseSavedState) parcelable).getSuperState());
        SavedState savedState = (SavedState) parcelable;
        this.mCurrentDate.set(savedState.getSelectedYear(), savedState.getSelectedMonth(), savedState.getSelectedDay());
        if (this.mIsLunar) {
            this.mLunarCurrentYear = savedState.getSelectedYear();
            this.mLunarCurrentMonth = savedState.getSelectedMonth();
            this.mLunarCurrentDay = savedState.getSelectedDay();
        }
        this.mMinDate.setTimeInMillis(savedState.getMinDate());
        this.mMaxDate.setTimeInMillis(savedState.getMaxDate());
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        int i = this.mCurrentDate.get(1);
        int i2 = this.mCurrentDate.get(2);
        int i3 = this.mCurrentDate.get(5);
        if (this.mIsLunar) {
            i = this.mLunarCurrentYear;
            i2 = this.mLunarCurrentMonth;
            i3 = this.mLunarCurrentDay;
        }
        return new SavedState(onSaveInstanceState, i, i2, i3, this.mMinDate.getTimeInMillis(), this.mMaxDate.getTimeInMillis(), -1);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    protected void onValidationChanged(boolean z) {
        if (this.mValidationCallback != null) {
            this.mValidationCallback.onValidationChanged(z);
        }
    }

    public void setDateMode(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        this.mMode = i;
        if (this.mMode == 1) {
            i2 = this.mStartDate.get(1);
            i3 = this.mStartDate.get(2);
            int i6 = this.mStartDate.get(5);
            if (this.mIsLunar) {
                i2 = this.mLunarStartYear;
                i3 = this.mLunarStartMonth;
                i6 = this.mLunarStartDay;
            }
            this.mSpinnerLayout.updateDate(i2, i3, i6);
        } else if (this.mMode == 2) {
            i4 = this.mEndDate.get(1);
            i5 = this.mEndDate.get(2);
            int i7 = this.mEndDate.get(5);
            if (this.mIsLunar) {
                i4 = this.mLunarEndYear;
                i5 = this.mLunarEndMonth;
                i7 = this.mLunarEndDay;
            }
            this.mSpinnerLayout.updateDate(i4, i5, i7);
        }
        if (this.mCurrentView == 1) {
            this.mSpinnerLayout.setVisibility(0);
            this.mSpinnerLayout.setEnabled(true);
        }
        SemSimpleMonthView semSimpleMonthView = (SemSimpleMonthView) this.mCalendarPagerAdapter.views.get(this.mCurrentPosition);
        if (semSimpleMonthView != null) {
            int i8 = this.mCurrentDate.get(1);
            int i9 = this.mCurrentDate.get(2);
            int i10 = this.mCurrentDate.get(5);
            if (this.mIsLunar) {
                i8 = this.mLunarCurrentYear;
                i9 = this.mLunarCurrentMonth;
                i10 = this.mLunarCurrentDay;
            }
            int minDay = (getMinMonth() == i9 && getMinYear() == i8) ? getMinDay() : 1;
            int maxDay = (getMaxMonth() == i9 && getMaxYear() == i8) ? getMaxDay() : 31;
            i2 = this.mStartDate.get(1);
            i3 = this.mStartDate.get(2);
            int i11 = this.mStartDate.get(5);
            i4 = this.mEndDate.get(1);
            i5 = this.mEndDate.get(2);
            int i12 = this.mEndDate.get(5);
            if (this.mIsLunar) {
                i2 = this.mLunarStartYear;
                i3 = this.mLunarStartMonth;
                i11 = this.mLunarStartDay;
                i4 = this.mLunarEndYear;
                i5 = this.mLunarEndMonth;
                i12 = this.mLunarEndDay;
            }
            semSimpleMonthView.setMonthParams(i10, i9, i8, getFirstDayOfWeek(), minDay, maxDay, this.mMinDate, this.mMaxDate, i2, i3, i11, this.mIsLeapStartMonth, i4, i5, i12, this.mIsLeapEndMonth, this.mMode);
            semSimpleMonthView.invalidate();
        }
        if (this.mIsLunar) {
            updateSimpleMonthView(false);
        }
        this.mCalendarPagerAdapter.notifyDataSetChanged();
    }

    public void setEditTextMode(boolean z) {
        if (this.mCurrentView != 0) {
            this.mSpinnerLayout.setEditTextMode(z);
        }
    }

    public void setEnabled(boolean z) {
        if (isEnabled() != z) {
            super.setEnabled(z);
            this.mIsEnabled = z;
        }
    }

    public void setFirstDayOfWeek(int i) {
        if (i < 1 || i > 7) {
            throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        this.mFirstDayOfWeek = i;
    }

    public void setHeaderViewShown(boolean z) {
    }

    protected void setLocale(Locale locale) {
        if (!locale.equals(this.mCurrentLocale)) {
            this.mCurrentLocale = locale;
            this.mShortMonths = new DateFormatSymbols().getShortMonths();
            this.mNumberOfMonths = this.mCurrentDate.getActualMaximum(2) + 1;
            if (usingNumericMonths()) {
                this.mShortMonths = new String[this.mNumberOfMonths];
                for (int i = 0; i < this.mNumberOfMonths; i++) {
                    this.mShortMonths[i] = String.format("%d", new Object[]{Integer.valueOf(i + 1)});
                }
            }
        }
    }

    public void setLunar(boolean z, boolean z2) {
        if (this.mIsLunarSupported) {
            this.mIsLunar = z;
            this.mIsLeapMonth = z2;
            this.mSpinnerLayout.setLunar(z, z2, this.mPathClassLoader);
            if (z) {
                setTotalMonthCountWithLeap();
            }
            this.mIsFromSetLunar = true;
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mLunarChanged = true;
            updateSimpleMonthView(true);
            this.mLunarChanged = false;
            onDateChanged(false, true);
        }
    }

    public void setLunarEndDate(int i, int i2, int i3, boolean z) {
        this.mLunarEndYear = i;
        this.mLunarEndMonth = i2;
        this.mLunarEndDay = i3;
        this.mIsLeapEndMonth = z ? 1 : 0;
    }

    public void setLunarStartDate(int i, int i2, int i3, boolean z) {
        this.mLunarStartYear = i;
        this.mLunarStartMonth = i2;
        this.mLunarStartDay = i3;
        this.mIsLeapStartMonth = z ? 1 : 0;
    }

    public void setLunarSupported(boolean z, View view) {
        this.mIsLunarSupported = z;
        if (this.mIsLunarSupported) {
            if (this.mCustomButtonView != null) {
                this.mCalendarHeaderLayout.removeView(this.mCustomButtonView);
            }
            this.mCustomButtonView = view;
            if (this.mCustomButtonView != null) {
                Object parent = this.mCustomButtonView.getParent();
                if (parent instanceof ViewGroup) {
                    parent.removeView(this.mCustomButtonView);
                }
                this.mCustomButtonView.setId(16908331);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mCalendarHeader.getLayoutParams();
                layoutParams.addRule(13);
                layoutParams.addRule(16, this.mCustomButtonView.getId());
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mPrevButton.getLayoutParams();
                layoutParams2.leftMargin = 0;
                this.mPrevButton.setLayoutParams(layoutParams2);
                RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.mNextButton.getLayoutParams();
                layoutParams3.rightMargin = 0;
                this.mNextButton.setLayoutParams(layoutParams3);
                layoutParams.leftMargin = getContext().getResources().getDimensionPixelOffset(17105713);
                this.mCalendarHeader.setLayoutParams(layoutParams);
                LayoutParams layoutParams4 = this.mCustomButtonView.getLayoutParams();
                LayoutParams layoutParams5 = layoutParams4 instanceof RelativeLayout.LayoutParams ? layoutParams4 : layoutParams4 instanceof MarginLayoutParams ? new RelativeLayout.LayoutParams(layoutParams4) : layoutParams4 != null ? new RelativeLayout.LayoutParams(layoutParams4) : new RelativeLayout.LayoutParams(-2, -2);
                if (layoutParams5 != null) {
                    layoutParams5.addRule(15);
                    layoutParams5.addRule(21);
                    this.mCustomButtonView.setLayoutParams(layoutParams5);
                }
                this.mCalendarHeaderLayout.addView(this.mCustomButtonView);
            }
        } else {
            this.mIsLunar = false;
            this.mIsLeapMonth = false;
            this.mCustomButtonView = null;
        }
        if (this.mIsLunarSupported && this.mPathClassLoader == null) {
            mPackageManager = this.mContext.getApplicationContext().getPackageManager();
            this.mPathClassLoader = LunarUtils.getPathClassLoader(getContext());
            if (this.mPathClassLoader != null) {
                try {
                    Class cls = Class.forName("com.android.calendar.Feature", true, this.mPathClassLoader);
                    if (cls == null) {
                        Log.e(TAG, "setLunarSupported, Calendar Feature class is null");
                        return;
                    }
                    this.mSolarLunarTables = invoke(null, getMethod(cls, "getSolarLunarTables", new Class[0]), new Object[0]);
                    try {
                        Class cls2 = Class.forName("com.samsung.android.calendar.secfeature.lunarcalendar.SolarLunarTables", true, this.mPathClassLoader);
                        if (cls2 == null) {
                            Log.e(TAG, "setLunarSupported, Calendar Tables class is null");
                            return;
                        }
                        this.mGetLunarMethod = getMethod(cls2, "getLunar", Integer.TYPE);
                        this.mStartOfLunarYearField = getField(cls2, "START_OF_LUNAR_YEAR");
                        this.mWidthPerYearField = getField(cls2, "WIDTH_PER_YEAR");
                        this.mIndexOfLeapMonthField = getField(cls2, "INDEX_OF_LEAP_MONTH");
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "setLunarSupported, Calendar Tables class not found");
                    }
                } catch (ClassNotFoundException e2) {
                    Log.e(TAG, "setLunarSupported, Calendar Feature class not found");
                }
            }
        }
    }

    public void setMaxDate(long j) {
        this.mTempMinMaxDate.setTimeInMillis(j);
        if (this.mTempMinMaxDate.get(1) != this.mMaxDate.get(1) || this.mTempMinMaxDate.get(6) == this.mMaxDate.get(6)) {
            if (this.mIsLunar) {
                setTotalMonthCountWithLeap();
            }
            if (this.mCurrentDate.after(this.mTempMinMaxDate)) {
                this.mCurrentDate.setTimeInMillis(j);
                onDateChanged(false, true);
            }
            this.mMaxDate.setTimeInMillis(j);
            this.mSpinnerLayout.setMaxDate(this.mMaxDate.getTimeInMillis());
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mHandler.postDelayed(new C02759(), 10);
        }
    }

    public void setMinDate(long j) {
        this.mTempMinMaxDate.setTimeInMillis(j);
        if (this.mTempMinMaxDate.get(1) != this.mMinDate.get(1) || this.mTempMinMaxDate.get(6) == this.mMinDate.get(6)) {
            if (this.mIsLunar) {
                setTotalMonthCountWithLeap();
            }
            if (this.mCurrentDate.before(this.mTempMinMaxDate)) {
                this.mCurrentDate.setTimeInMillis(j);
                onDateChanged(false, true);
            }
            this.mMinDate.setTimeInMillis(j);
            this.mSpinnerLayout.setMinDate(this.mMinDate.getTimeInMillis());
            this.mCalendarPagerAdapter.notifyDataSetChanged();
            this.mHandler.postDelayed(new C02748(), 10);
        }
    }

    public void setOnEditTextModeChangedListener(OnEditTextModeChangedListener onEditTextModeChangedListener) {
        this.mSpinnerLayout.setOnEditTextModeChangedListener(this, onEditTextModeChangedListener);
    }

    public void setValidationCallback(ValidationCallback validationCallback) {
        this.mValidationCallback = validationCallback;
    }

    public void tryVibrate() {
        performHapticFeedback(5);
    }

    public void updateDate(int i, int i2, int i3) {
        this.mTempDate.set(1, i);
        this.mTempDate.set(2, i2);
        this.mTempDate.set(5, i3);
        this.mCurrentDate = getCalendarForLocale(this.mTempDate, Locale.getDefault());
        if (this.mIsLunar) {
            this.mLunarCurrentYear = i;
            this.mLunarCurrentMonth = i2;
            this.mLunarCurrentDay = i3;
        }
        if (this.mMode == 1) {
            this.mStartDate.clear();
            this.mStartDate.set(1, i);
            this.mStartDate.set(2, i2);
            this.mStartDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarStartYear = i;
                this.mLunarStartMonth = i2;
                this.mLunarStartDay = i3;
            }
        } else if (this.mMode == 2) {
            this.mEndDate.clear();
            this.mEndDate.set(1, i);
            this.mEndDate.set(2, i2);
            this.mEndDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarEndYear = i;
                this.mLunarEndMonth = i2;
                this.mLunarEndDay = i3;
            }
        } else {
            this.mStartDate.clear();
            this.mStartDate.set(1, i);
            this.mStartDate.set(2, i2);
            this.mStartDate.set(5, i3);
            this.mEndDate.clear();
            this.mEndDate.set(1, i);
            this.mEndDate.set(2, i2);
            this.mEndDate.set(5, i3);
            if (this.mIsLunar) {
                this.mLunarStartYear = i;
                this.mLunarStartMonth = i2;
                this.mLunarStartDay = i3;
                this.mLunarEndYear = i;
                this.mLunarEndMonth = i2;
                this.mLunarEndDay = i3;
            }
        }
        updateSimpleMonthView(true);
        onDateChanged(false, true);
        SemSimpleMonthView semSimpleMonthView = (SemSimpleMonthView) this.mCalendarPagerAdapter.views.get(this.mCurrentPosition);
        if (semSimpleMonthView != null) {
            int minDay = (getMinMonth() == i2 && getMinYear() == i) ? getMinDay() : 1;
            int maxDay = (getMaxMonth() == i2 && getMaxYear() == i) ? getMaxDay() : 31;
            if (this.mIsLunarSupported) {
                semSimpleMonthView.setLunar(this.mIsLunar, this.mIsLeapMonth, this.mPathClassLoader);
            }
            int i4 = this.mStartDate.get(1);
            int i5 = this.mStartDate.get(2);
            int i6 = this.mStartDate.get(5);
            int i7 = this.mEndDate.get(1);
            int i8 = this.mEndDate.get(2);
            int i9 = this.mEndDate.get(5);
            if (this.mIsLunar) {
                i4 = this.mLunarStartYear;
                i5 = this.mLunarStartMonth;
                i6 = this.mLunarStartDay;
                i7 = this.mLunarEndYear;
                i8 = this.mLunarEndMonth;
                i9 = this.mLunarEndDay;
            }
            semSimpleMonthView.setMonthParams(i3, i2, i, getFirstDayOfWeek(), minDay, maxDay, this.mMinDate, this.mMaxDate, i4, i5, i6, this.mIsLeapStartMonth, i7, i8, i9, this.mIsLeapEndMonth, this.mMode);
            semSimpleMonthView.invalidate();
            if (this.mSpinnerLayout != null) {
                this.mSpinnerLayout.updateDate(i, i2, i3);
            }
        }
    }
}
