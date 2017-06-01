package com.samsung.android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.widget.SemAbstractIndexer.IndexInfo;
import java.util.ArrayList;
import java.util.HashMap;

public class SemIndexScrollView extends FrameLayout implements OnScrollListener {
    private static final char FAVORITE_CHAR = '☆';
    public static final int GRAVITY_INDEX_BAR_LEFT = 0;
    public static final int GRAVITY_INDEX_BAR_RIGHT = 1;
    private static final int NO_PREV_LANG = 0;
    private static final float OUT_OF_BOUNDARY = -9999.0f;
    private static final String TAG = "SemIndexScrollView";
    private static Typeface mSECRobotoLightRegularFont;
    private int DEFAULT_MAX_DEPTH = 1;
    private int FEW_ELEMENT_LOGIC = 0;
    private int MANY_ELEMENTS_REPRESENTED_BY_DOT = 8;
    private int MANY_ELEMENT_LOGIC = 1;
    private final boolean debug = false;
    private boolean mAnimEnd = true;
    private String mCalculatedIndexStr;
    private int mColorPrimary = -1;
    private int mColorPrimaryDark = -1;
    private Context mContext;
    private String mCurrentIndex;
    private int mFirstLanguageGap = -1;
    private boolean mHasOverlayChild = false;
    private int mIndexBarGravity = 1;
    IndexScroll mIndexScroll;
    private IndexScrollPreview mIndexScrollPreview;
    private SemAbstractIndexer mIndexer;
    private final IndexerObserver mIndexerObserver = new IndexerObserver();
    private int[] mLangDbEndPositions;
    private int[] mLangDbStartPositions;
    private int[] mLangScrollEndPositions;
    private int[] mLangScrollStartPositions;
    private int mNumberOfLanguages;
    private OnIndexBarEventListener mOnIndexBarEventListener = null;
    private int mPrevSetLang = 0;
    private final Runnable mPreviewDelayRunnable = new C02961();
    private boolean mRegisteredDataSetObserver = false;
    private int mScrollLogic = this.FEW_ELEMENT_LOGIC;
    private boolean mSipResizeAnimationState = false;
    private long mStartTouchDown = 0;
    private float mTouchY = OUT_OF_BOUNDARY;
    private ViewGroupOverlay mViewGroupOverlay;
    private boolean m_bNoSubIndexes;
    private boolean m_bSimpleIndexScroll = false;

    class C02961 implements Runnable {
        C02961() {
        }

        public void run() {
            if (SemIndexScrollView.this.mIndexScrollPreview != null) {
                SemIndexScrollView.this.mIndexScrollPreview.fadeOutAnimation();
            }
        }
    }

    class IndexScroll {
        public static final int FIRST_LETTER_NOT_RELEVANT_NOT_MULTI_LANGUAGE = -1;
        public static final int GRAVITY_INDEX_BAR_LEFT = 0;
        public static final int GRAVITY_INDEX_BAR_RIGHT = 1;
        public static final int LAST_LETTER_NOT_RELEVANT_NOT_MULTI_LANGUAGE = -1;
        public static final int NO_SELECTED_DOT_INDEX = -1;
        public static final int NO_SELECTED_INDEX = -1;
        private static final String TAG = "IndexScroll";
        private float FLOAT_DIV_MULT_ERROR = 0.001f;
        private final boolean debug = false;
        private int mAdditionalSpace;
        private String[] mAlphabetArray = null;
        private int mAlphabetArrayFirstLetterIndex = -1;
        private int mAlphabetArrayLastLetterIndex = -1;
        private String[] mAlphabetArrayToDraw;
        private String[] mAlphabetArrayWithDots;
        private int mAlphabetSize;
        private int mAlphabetToDrawSize;
        private int mAlphabetWithDotsSize;
        private Drawable mBgDrawableDefault = null;
        private Rect mBgRect;
        private boolean mBgRectParamsSet;
        private int mBgRectWidth;
        private int mBgTintColor;
        private String mBigText;
        private float mContentMinHeight;
        private int mContentPadding;
        private Context mContext;
        private int mDepth;
        private int mDotHeight;
        private HashMap<Integer, String[]> mDotRepresentations = null;
        private int mEffectTextColor;
        LangAttributeValues mFirstLang;
        private int mHeight;
        private float mIndexScrollPreviewRadius;
        private float mItemHeight;
        private int mItemWidth;
        private int mItemWidthGap;
        private int mMaxDepth;
        private int mOrigSelectedDotIndex = -1;
        private int mOrigSelectedIndex = -1;
        private Paint mPaint;
        private int mPosition = 0;
        private float mPreviewLimitY;
        private int mScreenHeight;
        private int mScrollBottomMargin;
        private Drawable mScrollThumbBgDrawable = null;
        private Rect mScrollThumbBgRect;
        private int mScrollThumbBgRectHeight;
        private int mScrollThumbBgRectPadding;
        private int mScrollTop;
        private int mScrollTopMargin;
        LangAttributeValues mSecondLang;
        private int mSelectedIndex = -1;
        private int[] mSelectedIndexPositionInOrigAlphabet;
        private float mSeparatorHeight;
        private String mSmallText;
        private IndexScroll mSubIndexScroll;
        private Rect mTextBounds;
        private int mTextColorDimmed;
        private int mTextSize;
        private int mThumbColor = 0;
        private int mWidth;
        private int mWidthShift;
        private boolean m_bAlphabetArrayWithDotsUsed;
        private boolean m_bIsAlphabetInit = false;
        private boolean m_bSubIndexScrollExists;
        private boolean mbSetDimensionns;

        class LangAttributeValues {
            String[] alphabetArray;
            int dotCount;
            float height;
            int indexCount;
            float separatorHeight;
            int totalCount;

            public LangAttributeValues(int i, int i2, int i3, float f, float f2) {
                this.indexCount = i;
                this.dotCount = i2;
                this.totalCount = i3;
                this.height = f;
                this.separatorHeight = f2;
            }
        }

        public IndexScroll(Context context, int i, int i2) {
            this.mHeight = i;
            this.mWidth = i2;
            this.m_bSubIndexScrollExists = false;
            this.mDepth = 1;
            this.mMaxDepth = SemIndexScrollView.this.DEFAULT_MAX_DEPTH;
            this.mWidthShift = 0;
            this.mScrollTop = 0;
            this.mTextBounds = new Rect();
            this.mBgRectParamsSet = false;
            this.mContext = context;
            init();
        }

        public IndexScroll(Context context, int i, int i2, int i3) {
            this.mHeight = i;
            this.mWidth = i2;
            this.mPosition = i3;
            this.m_bSubIndexScrollExists = false;
            this.mDepth = 1;
            this.mMaxDepth = SemIndexScrollView.this.DEFAULT_MAX_DEPTH;
            this.mWidthShift = 0;
            this.mScrollTop = 0;
            this.mTextBounds = new Rect();
            this.mBgRectParamsSet = false;
            this.mContext = context;
            init();
        }

        public IndexScroll(Context context, int i, String[] strArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            this.mScreenHeight = i3;
            this.mHeight = i4;
            this.mWidth = i5;
            this.mWidthShift = i6;
            this.mPosition = i;
            this.mBgRectParamsSet = false;
            this.mMaxDepth = i7;
            this.mDepth = i8;
            this.mScrollTop = i2;
            this.m_bSubIndexScrollExists = false;
            this.mTextBounds = new Rect();
            this.mContext = context;
            init();
            setAlphabetArray(strArr, -1, -1);
            manageIndexScrollHeight();
        }

        private void adjustSeparatorHeight() {
            if (SemIndexScrollView.this.mNumberOfLanguages == 1) {
                this.mFirstLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mFirstLang.height = (float) this.mHeight;
                return;
            }
            if (this.mFirstLang.height > ((float) this.mHeight) * 0.6f) {
                this.mFirstLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.6d) - ((double) (this.mDotHeight * this.mFirstLang.dotCount)))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.4d) - ((double) (this.mDotHeight * this.mSecondLang.dotCount)))) / ((float) this.mSecondLang.indexCount);
                this.mFirstLang.height = ((float) this.mHeight) * 0.6f;
                this.mSecondLang.height = ((float) this.mHeight) * 0.4f;
            } else if (this.mFirstLang.height <= ((float) this.mHeight) * 0.5f) {
                this.mFirstLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.5d) - ((double) (this.mDotHeight * this.mFirstLang.dotCount)))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = ((float) ((((double) this.mHeight) * 0.5d) - ((double) (this.mDotHeight * this.mSecondLang.dotCount)))) / ((float) this.mSecondLang.indexCount);
                LangAttributeValues langAttributeValues = this.mFirstLang;
                float f = ((float) this.mHeight) * 0.5f;
                this.mSecondLang.height = f;
                langAttributeValues.height = f;
            } else {
                this.mFirstLang.separatorHeight = (this.mFirstLang.height - ((float) (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mSecondLang.separatorHeight = (this.mSecondLang.height - ((float) (this.mDotHeight * this.mSecondLang.dotCount))) / ((float) this.mSecondLang.indexCount);
            }
            if (this.mSecondLang.totalCount == 0) {
                this.mFirstLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mFirstLang.dotCount))) / ((float) this.mFirstLang.indexCount);
                this.mFirstLang.height = (float) this.mHeight;
                this.mSecondLang.separatorHeight = 0.0f;
                this.mSecondLang.height = 0.0f;
            } else if (this.mFirstLang.totalCount == 0) {
                this.mSecondLang.separatorHeight = ((float) (this.mHeight - (this.mDotHeight * this.mSecondLang.dotCount))) / ((float) this.mSecondLang.indexCount);
                this.mSecondLang.height = (float) this.mHeight;
                this.mFirstLang.separatorHeight = 0.0f;
                this.mFirstLang.height = 0.0f;
            }
        }

        private void adjustSeparatorHeightIfRequired() {
            if (this.mHeight != 0 && Float.compare(this.mSeparatorHeight, this.mContentMinHeight) == 0) {
                this.mSeparatorHeight = ((float) this.mHeight) / ((float) ((int) (((float) this.mHeight) / this.mSeparatorHeight)));
            }
        }

        private void allocateBgRectangle() {
            int i;
            int i2;
            if (this.mPosition == 1) {
                i = this.mWidth - this.mWidthShift;
                i2 = i - this.mBgRectWidth;
            } else {
                i = this.mWidthShift + this.mBgRectWidth;
                i2 = this.mWidthShift;
            }
            this.mBgRect = new Rect(i2, (this.mScrollTop + this.mScrollTopMargin) - this.mContentPadding, i, ((this.mHeight + this.mScrollTop) + this.mScrollTopMargin) + this.mContentPadding);
            this.mScrollThumbBgRectHeight = (int) (this.mContentMinHeight * 3.0f);
            i2 += this.mScrollThumbBgRectPadding;
            i -= this.mScrollThumbBgRectPadding;
            int -get15 = (int) (SemIndexScrollView.this.mTouchY - ((float) (this.mScrollThumbBgRectHeight / 2)));
            int -get152 = (int) (SemIndexScrollView.this.mTouchY + ((float) (this.mScrollThumbBgRectHeight / 2)));
            if ((-get15 < this.mBgRect.top + this.mScrollThumbBgRectPadding && -get152 > this.mBgRect.bottom - this.mScrollThumbBgRectPadding) || this.mScrollThumbBgRectHeight >= (this.mBgRect.bottom - this.mBgRect.top) - (this.mScrollThumbBgRectPadding * 2)) {
                -get15 = this.mBgRect.top + this.mScrollThumbBgRectPadding;
                -get152 = this.mBgRect.bottom - this.mScrollThumbBgRectPadding;
            } else if (-get15 < this.mBgRect.top + this.mScrollThumbBgRectPadding) {
                -get15 = this.mBgRect.top + this.mScrollThumbBgRectPadding;
                -get152 = -get15 + this.mScrollThumbBgRectHeight;
            } else if (-get152 > this.mBgRect.bottom - this.mScrollThumbBgRectPadding) {
                -get152 = this.mBgRect.bottom - this.mScrollThumbBgRectPadding;
                -get15 = -get152 - this.mScrollThumbBgRectHeight;
            }
            this.mScrollThumbBgRect = new Rect(i2, -get15, i, -get152);
        }

        private void calcDotPosition(LangAttributeValues langAttributeValues, int i, int i2, int i3) {
            int i4;
            int i5 = langAttributeValues.indexCount - i;
            int i6 = 0;
            Object obj = null;
            for (i4 = i2; i4 < langAttributeValues.totalCount + i2; i4++) {
                langAttributeValues.alphabetArray[i4 - i2] = this.mAlphabetArray[i4];
            }
            while (langAttributeValues.separatorHeight < this.mContentMinHeight && this.mAlphabetArrayToDraw.length > 0) {
                int i7 = i5 - i3;
                int i8 = (i7 / 2) - 1;
                String[] strArr;
                int i9;
                if (langAttributeValues.dotCount >= i8 || r13 != null) {
                    obj = 1;
                    Object obj2 = null;
                    int i10 = 0;
                    switch ((langAttributeValues.totalCount - i) - i3) {
                        case 0:
                            if (i3 <= 0) {
                                if (i > 0) {
                                    i--;
                                    break;
                                }
                            }
                            i3--;
                            break;
                            break;
                        case 1:
                            if (i != 0 && langAttributeValues.dotCount == 0) {
                                langAttributeValues.indexCount--;
                                langAttributeValues.dotCount++;
                                obj2 = 1;
                            } else if (i == 0 || langAttributeValues.dotCount != 1) {
                                langAttributeValues.indexCount--;
                                langAttributeValues.totalCount--;
                            } else {
                                langAttributeValues.dotCount--;
                                langAttributeValues.totalCount--;
                            }
                            i6++;
                            break;
                        case 2:
                            langAttributeValues.dotCount--;
                            langAttributeValues.totalCount--;
                            break;
                        case 3:
                            langAttributeValues.indexCount--;
                            langAttributeValues.totalCount--;
                            i6++;
                            break;
                        default:
                            if (((langAttributeValues.indexCount - langAttributeValues.dotCount) - i) - i3 != 1) {
                                langAttributeValues.indexCount--;
                                langAttributeValues.totalCount--;
                                i6++;
                                break;
                            }
                            langAttributeValues.dotCount--;
                            langAttributeValues.totalCount--;
                            break;
                    }
                    if (langAttributeValues.totalCount <= 0 || langAttributeValues.dotCount < 0 || langAttributeValues.indexCount < 0) {
                        adjustSeparatorHeight();
                        return;
                    }
                    strArr = new String[langAttributeValues.totalCount];
                    int i11 = 0;
                    int i12 = 0;
                    if (langAttributeValues.dotCount > 0) {
                        i11 = i6 / langAttributeValues.dotCount;
                        i12 = i6 % langAttributeValues.dotCount;
                    }
                    for (i4 = 0; i4 < i; i4++) {
                        strArr[i4] = this.mAlphabetArray[i4];
                    }
                    i9 = i;
                    for (i4 = i; i4 < langAttributeValues.totalCount - i3; i4++) {
                        if (i9 < this.mAlphabetArray.length - i3) {
                            if (obj2 == null) {
                                strArr[i4] = this.mAlphabetArray[i9 + i2];
                                i9++;
                                if (i10 < langAttributeValues.dotCount) {
                                    obj2 = 1;
                                }
                            } else {
                                strArr[i4] = ".";
                                i10++;
                                i9 += i11;
                                if (i12 > 0) {
                                    i12--;
                                    i9++;
                                }
                                obj2 = null;
                            }
                        }
                    }
                    if (i3 > 0) {
                        strArr[langAttributeValues.totalCount - i3] = this.mAlphabetArray[this.mAlphabetArray.length - 1];
                    }
                    langAttributeValues.alphabetArray = strArr;
                } else {
                    strArr = new String[langAttributeValues.totalCount];
                    langAttributeValues.dotCount++;
                    langAttributeValues.indexCount--;
                    i6++;
                    int i13 = (i7 / (langAttributeValues.dotCount + 1)) + 1;
                    if (langAttributeValues.dotCount == i8) {
                        i13 = 2;
                    }
                    int i14 = langAttributeValues.dotCount;
                    int i15 = 0;
                    while (i14 != 0) {
                        if (i14 != langAttributeValues.dotCount) {
                            i14 = langAttributeValues.dotCount;
                        }
                        for (i4 = i2; i4 < langAttributeValues.totalCount + i2; i4++) {
                            strArr[i4 - i2] = this.mAlphabetArray[i4];
                        }
                        for (int i16 = 1; i16 < langAttributeValues.dotCount + 1; i16++) {
                            i9 = (i13 * i16) - (i15 * i16);
                            if (i > 1) {
                                i9 += i - 1;
                            }
                            if (i9 > 0 && i9 < i7) {
                                strArr[i9] = ".";
                                i14--;
                            } else if (i9 >= i7 && i14 > 0) {
                                if (i9 - (i13 / 2) < i7) {
                                    strArr[i9 - (i13 / 2)] = ".";
                                    i14--;
                                } else {
                                    i15 = 1;
                                }
                            }
                        }
                    }
                    langAttributeValues.alphabetArray = strArr;
                }
                adjustSeparatorHeight();
            }
        }

        private int calculateShift(int i, int i2, int i3) {
            int i4 = i2;
            int i5 = (this.mAlphabetWithDotsSize - 1) - i3;
            return Math.abs(i2 - i5) > 1 ? i5 > i2 ? Math.min((i5 - i2) / 2, ((this.mAlphabetWithDotsSize - i) - i3) - 1) : Math.max((i5 - i2) / 2, (this.mAlphabetArrayFirstLetterIndex - i2) + 1) : 0;
        }

        private void drawAlphabetCharacters(Canvas canvas) {
            this.mPaint.setColor(this.mTextColorDimmed);
            this.mPaint.setTextSize((float) this.mTextSize);
            if (this.mAlphabetArrayToDraw != null && this.mFirstLang.totalCount != 0) {
                float f = (float) (this.mScrollTop + this.mScrollTopMargin);
                for (int i = 0; i < this.mFirstLang.totalCount + this.mSecondLang.totalCount; i++) {
                    String str;
                    float f2;
                    float f3;
                    if (i < this.mFirstLang.totalCount) {
                        str = this.mFirstLang.alphabetArray[i];
                        f2 = this.mFirstLang.separatorHeight;
                    } else {
                        str = this.mSecondLang.alphabetArray[i - this.mFirstLang.totalCount];
                        f2 = this.mSecondLang.separatorHeight;
                    }
                    this.mPaint.getTextBounds(str, 0, str.length(), this.mTextBounds);
                    float centerX = ((float) this.mBgRect.centerX()) - (0.5f * this.mPaint.measureText(str));
                    if (str == ".") {
                        f3 = f + ((float) ((((double) this.mDotHeight) * 0.5d) - ((double) (((float) this.mTextBounds.top) * 0.5f))));
                        f += (float) this.mDotHeight;
                    } else {
                        f3 = f + ((float) ((((double) f2) * 0.5d) - ((double) (((float) this.mTextBounds.top) * 0.5f))));
                        f += f2;
                    }
                    canvas.drawText(str, centerX, f3, this.mPaint);
                }
            }
        }

        private void drawBgRectangle(Canvas canvas) {
            if (!this.mBgRectParamsSet) {
                setBgRectParams();
                this.mBgRectParamsSet = true;
            }
            this.mBgDrawableDefault.draw(canvas);
            if (SemIndexScrollView.this.mTouchY != SemIndexScrollView.OUT_OF_BOUNDARY) {
                this.mScrollThumbBgDrawable.draw(canvas);
            }
        }

        private int getColorWithAlpha(int i, float f) {
            return Color.argb(Math.round(((float) Color.alpha(i)) * f), Color.red(i), Color.green(i), Color.blue(i));
        }

        private String getDotIndexByY(int i) {
            if (this.m_bSubIndexScrollExists && this.mSelectedIndex == this.mOrigSelectedIndex && this.mOrigSelectedDotIndex != -1 && this.mOrigSelectedDotIndex < ((String[]) this.mDotRepresentations.get(Integer.valueOf(this.mSelectedIndex))).length) {
                return ((String[]) this.mDotRepresentations.get(Integer.valueOf(this.mSelectedIndex)))[this.mOrigSelectedDotIndex];
            }
            int i2;
            float f;
            int length = ((String[]) this.mDotRepresentations.get(Integer.valueOf(this.mSelectedIndex))).length;
            if (this.mDepth != 1 || length < SemIndexScrollView.this.MANY_ELEMENTS_REPRESENTED_BY_DOT) {
                SemIndexScrollView.this.mScrollLogic = SemIndexScrollView.this.FEW_ELEMENT_LOGIC;
                i2 = (int) (((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mSeparatorHeight * ((float) this.mSelectedIndex)));
                f = this.mSeparatorHeight;
            } else {
                SemIndexScrollView.this.mScrollLogic = SemIndexScrollView.this.MANY_ELEMENT_LOGIC;
                i2 = (int) (((double) (this.mScrollTop + this.mScrollTopMargin)) + (((double) this.mSeparatorHeight) * (((double) this.mSelectedIndex) - 0.5d)));
                f = this.mSeparatorHeight * SprDocument.DEFAULT_DENSITY_SCALE;
            }
            int floor = (int) Math.floor((double) ((((float) length) * (((float) i) - ((float) i2))) / f));
            if (floor >= length) {
                floor = length - 1;
            } else if (floor < 0) {
                floor = 0;
            }
            this.mOrigSelectedDotIndex = floor;
            this.mOrigSelectedIndex = this.mSelectedIndex;
            return ((String[]) this.mDotRepresentations.get(Integer.valueOf(this.mSelectedIndex)))[floor];
        }

        private int getIndex(int i) {
            float f = (float) (this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex);
            int i2 = ((float) i) < ((float) (this.mScrollTop + this.mScrollTopMargin)) + this.mFirstLang.height ? (int) (((float) ((i - this.mScrollTop) - this.mScrollTopMargin)) / (this.mFirstLang.height / f)) : (int) (((float) ((int) ((((float) ((i - this.mScrollTop) - this.mScrollTopMargin)) - this.mFirstLang.height) / (this.mSecondLang.height / ((float) this.mAlphabetArrayLastLetterIndex))))) + f);
            return i2 < 0 ? 0 : i2 >= this.mAlphabetToDrawSize ? this.mAlphabetToDrawSize - 1 : i2;
        }

        private String getIndexByY(int i) {
            if (i <= this.mBgRect.top - this.mAdditionalSpace || i >= this.mBgRect.bottom + this.mAdditionalSpace) {
                return "";
            }
            if (i < this.mBgRect.top) {
                this.mSelectedIndex = 0;
            } else if (i > this.mBgRect.bottom) {
                this.mSelectedIndex = this.mAlphabetToDrawSize - 1;
            } else {
                this.mSelectedIndex = getIndex(i);
                if (this.mSelectedIndex == this.mAlphabetToDrawSize) {
                    this.mSelectedIndex--;
                }
            }
            if (this.mSelectedIndex == this.mAlphabetToDrawSize || this.mSelectedIndex == this.mAlphabetToDrawSize + 1) {
                this.mSelectedIndex = this.mAlphabetToDrawSize - 1;
            }
            return !isSelectedIndexDot(this.mSelectedIndex) ? (this.mAlphabetArrayToDraw == null || this.mSelectedIndex <= -1 || this.mSelectedIndex > this.mAlphabetToDrawSize) ? "" : this.mAlphabetArrayToDraw[this.mSelectedIndex] : getDotIndexByY(i);
        }

        private void init() {
            Resources resources = this.mContext.getResources();
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            if (SemIndexScrollView.mSECRobotoLightRegularFont == null) {
                SemIndexScrollView.mSECRobotoLightRegularFont = Typeface.create("sec-roboto-light", 0);
            }
            this.mPaint.setTypeface(SemIndexScrollView.mSECRobotoLightRegularFont);
            this.mScrollTopMargin = 0;
            this.mScrollBottomMargin = 0;
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(16843828, typedValue, true);
            SemIndexScrollView.this.mColorPrimaryDark = this.mContext.getResources().getColor(typedValue.resourceId);
            this.mItemWidth = 1;
            this.mItemWidthGap = 1;
            this.mBgRectWidth = (int) resources.getDimension(17105796);
            this.mTextSize = (int) resources.getDimension(17105797);
            this.mScrollTop = (int) resources.getDimension(17105798);
            this.mWidthShift = (int) resources.getDimension(17105799);
            this.mContentPadding = (int) resources.getDimension(17105800);
            this.mContentMinHeight = resources.getDimension(17105801);
            this.mAdditionalSpace = (int) resources.getDimension(17105802);
            this.mIndexScrollPreviewRadius = resources.getDimension(17105793);
            this.mPreviewLimitY = resources.getDimension(17105795);
            this.mEffectTextColor = resources.getColor(17170823);
            this.mContext.getTheme().resolveAttribute(16843827, typedValue, true);
            SemIndexScrollView.this.mColorPrimary = this.mContext.getResources().getColor(typedValue.resourceId);
            this.mFirstLang = new LangAttributeValues(0, 0, 0, 0.0f, 0.0f);
            this.mSecondLang = new LangAttributeValues(0, 0, 0, 0.0f, 0.0f);
            this.mScrollThumbBgRectPadding = (int) resources.getDimension(17105804);
            this.mDotHeight = (int) resources.getDimension(17105805);
            SemIndexScrollView.this.mIndexScrollPreview.setBackgroundColor(getColorWithAlpha(SemIndexScrollView.this.mColorPrimary, 0.8f));
            this.mScrollThumbBgDrawable = resources.getDrawable(17303526);
            this.mScrollThumbBgDrawable.setTint(SemIndexScrollView.this.mColorPrimary);
            this.mThumbColor = SemIndexScrollView.this.mColorPrimary;
            this.mTextColorDimmed = resources.getColor(17170824);
            this.mBgTintColor = resources.getColor(17170825);
            this.mBgDrawableDefault = resources.getDrawable(17303525);
            this.mBgDrawableDefault.setTintMode(Mode.MULTIPLY);
            this.mBgDrawableDefault.setTint(this.mBgTintColor);
            setBgRectParams();
        }

        private void initAlphabetArrayWithDotsIfRequired() {
            if (this.m_bIsAlphabetInit) {
                this.m_bAlphabetArrayWithDotsUsed = false;
                if (this.mSeparatorHeight * ((float) this.mAlphabetSize) <= ((float) this.mHeight) + this.FLOAT_DIV_MULT_ERROR) {
                    this.mAlphabetArrayToDraw = this.mAlphabetArray;
                    this.mAlphabetToDrawSize = this.mAlphabetSize;
                    return;
                }
                this.mDotRepresentations = new HashMap();
                this.mAlphabetWithDotsSize = (int) (((float) this.mHeight) / this.mSeparatorHeight);
                if (this.mAlphabetWithDotsSize >= 0) {
                    boolean perfectDotsSpreadingExists;
                    int i;
                    this.mSelectedIndexPositionInOrigAlphabet = new int[this.mAlphabetWithDotsSize];
                    this.mAlphabetArrayWithDots = new String[this.mAlphabetWithDotsSize];
                    int i2 = this.mAlphabetSize - this.mAlphabetWithDotsSize;
                    int sqrt = ((int) Math.sqrt((double) i2)) + 1;
                    int i3 = this.mAlphabetArrayLastLetterIndex == -1 ? 1 : this.mAlphabetArrayLastLetterIndex + 1;
                    if ((this.mAlphabetWithDotsSize - i3) / sqrt <= 1) {
                        sqrt = (this.mAlphabetWithDotsSize - i3) / 2;
                        if (sqrt == 0) {
                            sqrt = 1;
                        }
                    }
                    if ((this.mAlphabetWithDotsSize - i3) / sqrt == 2) {
                        sqrt = (this.mAlphabetWithDotsSize - i3) / 3;
                        if (sqrt == 0) {
                            sqrt = 1;
                        }
                    }
                    if (this.mAlphabetArrayFirstLetterIndex != -1 && (this.mAlphabetWithDotsSize - i3) / r11 < this.mAlphabetArrayFirstLetterIndex + 2 && this.mAlphabetWithDotsSize - i3 > this.mAlphabetArrayFirstLetterIndex + 1) {
                        sqrt = (this.mAlphabetWithDotsSize - i3) / (this.mAlphabetArrayFirstLetterIndex + 2);
                    }
                    if (sqrt <= 0) {
                        sqrt = 1;
                    }
                    int i4 = sqrt;
                    int i5 = ((i2 + sqrt) / sqrt) + 1;
                    while ((i5 - 1) * sqrt >= i2 + sqrt) {
                        i5--;
                    }
                    int i6 = 0;
                    int i7 = 0;
                    int i8 = (i2 + sqrt) % i5;
                    int i9 = 0;
                    if (i8 == 1) {
                        perfectDotsSpreadingExists = perfectDotsSpreadingExists(sqrt - 1, i3);
                        i = (this.mAlphabetWithDotsSize + 1) / sqrt;
                    } else {
                        perfectDotsSpreadingExists = perfectDotsSpreadingExists(sqrt, i3);
                        i = (this.mAlphabetWithDotsSize + 1) / (sqrt + 1);
                    }
                    int i10 = 0;
                    if (!perfectDotsSpreadingExists) {
                        i = (this.mAlphabetWithDotsSize - i3) / sqrt;
                        if (i == 0) {
                            i = 1;
                        }
                        i10 = i - 1;
                        i9 = calculateShift(i3, i10, i8 == 1 ? ((sqrt - 1) * i) - 1 : (i * sqrt) - 1);
                    }
                    while (i6 < this.mAlphabetWithDotsSize && i7 < this.mAlphabetSize) {
                        this.mSelectedIndexPositionInOrigAlphabet[i6] = i7;
                        if ((!perfectDotsSpreadingExists || (i6 + 1) % r6 <= 0) && (perfectDotsSpreadingExists || ((r9 <= 0 || i6 > r16) && ((i6 + 1) - r9) % r6 <= 0 && i4 != 0 && !(i4 == 1 && i8 == 1)))) {
                            this.mAlphabetArrayWithDots[i6] = ".";
                            int i11 = i7;
                            i7 = i4 > 1 ? i7 + i5 : this.mAlphabetSize - ((this.mAlphabetWithDotsSize - 1) - i6);
                            Object obj = new String[(i7 - i11)];
                            for (int i12 = i11; i12 < i7; i12++) {
                                obj[i12 - i11] = this.mAlphabetArray[i12];
                            }
                            this.mDotRepresentations.put(Integer.valueOf(i6), obj);
                            i4--;
                        } else {
                            this.mAlphabetArrayWithDots[i6] = this.mAlphabetArray[i7];
                            i7++;
                        }
                        i6++;
                    }
                    this.m_bAlphabetArrayWithDotsUsed = true;
                    this.mAlphabetArrayToDraw = this.mAlphabetArrayWithDots;
                    this.mAlphabetToDrawSize = this.mAlphabetArrayToDraw.length;
                }
            }
        }

        private boolean isInSelectedIndexRect(int i) {
            boolean z = true;
            boolean z2 = false;
            if (this.mSelectedIndex == -1 || this.mSelectedIndex >= this.mAlphabetToDrawSize) {
                return false;
            }
            if (SemIndexScrollView.this.mScrollLogic == SemIndexScrollView.this.FEW_ELEMENT_LOGIC) {
                if (i < ((int) (((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mSeparatorHeight * ((float) this.mSelectedIndex))))) {
                    z = false;
                } else if (i > ((int) (((float) (this.mScrollTop + this.mScrollTopMargin)) + (this.mSeparatorHeight * ((float) (this.mSelectedIndex + 1)))))) {
                    z = false;
                }
                return z;
            }
            if (i >= ((int) (((double) (this.mScrollTop + this.mScrollTopMargin)) + (((double) this.mSeparatorHeight) * (((double) this.mSelectedIndex) - 0.5d)))) && i <= ((int) (((double) (this.mScrollTop + this.mScrollTopMargin)) + (((double) this.mSeparatorHeight) * (((double) this.mSelectedIndex) + 1.5d))))) {
                z2 = true;
            }
            return z2;
        }

        private boolean isSelectedIndexDot(int i) {
            return this.m_bAlphabetArrayWithDotsUsed ? this.mDotRepresentations.containsKey(Integer.valueOf(i)) : false;
        }

        private void manageIndexScrollHeight() {
            if (this.m_bIsAlphabetInit && SemIndexScrollView.this.mNumberOfLanguages <= 2) {
                if (this.mAlphabetArrayFirstLetterIndex == -1) {
                    this.mAlphabetArrayFirstLetterIndex = 0;
                }
                if (this.mAlphabetArrayLastLetterIndex == -1) {
                    this.mAlphabetArrayLastLetterIndex = 0;
                }
                this.mFirstLang.indexCount = this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex;
                this.mFirstLang.totalCount = this.mFirstLang.indexCount;
                this.mFirstLang.alphabetArray = new String[this.mFirstLang.totalCount];
                this.mFirstLang.dotCount = 0;
                this.mSecondLang.indexCount = this.mAlphabetSize - this.mFirstLang.indexCount;
                this.mSecondLang.totalCount = this.mSecondLang.indexCount;
                this.mSecondLang.alphabetArray = new String[this.mSecondLang.totalCount];
                this.mSecondLang.dotCount = 0;
                this.mFirstLang.height = ((float) this.mFirstLang.indexCount) * this.mContentMinHeight;
                this.mSecondLang.height = ((float) this.mHeight) - this.mFirstLang.height;
                this.mAlphabetArrayToDraw = this.mAlphabetArray;
                this.mAlphabetToDrawSize = this.mAlphabetSize;
                adjustSeparatorHeight();
                int i = 0;
                if (this.mAlphabetArrayFirstLetterIndex > 0 && SemIndexScrollView.this.mIndexer.isUseDigitIndex()) {
                    i = 1;
                }
                if (SemIndexScrollView.this.mNumberOfLanguages == 1) {
                    calcDotPosition(this.mFirstLang, this.mAlphabetArrayFirstLetterIndex, 0, i);
                } else {
                    calcDotPosition(this.mFirstLang, this.mAlphabetArrayFirstLetterIndex, 0, 0);
                    calcDotPosition(this.mSecondLang, 0, this.mAlphabetSize - this.mAlphabetArrayLastLetterIndex, i);
                }
            }
        }

        private boolean perfectDotsSpreadingExists(int i, int i2) {
            boolean z = false;
            if (i < 0) {
                i = 1;
            }
            if ((this.mAlphabetWithDotsSize + 1) % (i + 1) > 0) {
                return false;
            }
            int i3 = (this.mAlphabetWithDotsSize + 1) / (i + 1);
            if (i3 - 1 > this.mAlphabetArrayFirstLetterIndex && this.mAlphabetWithDotsSize - i3 < this.mAlphabetWithDotsSize - i2) {
                z = true;
            }
            return z;
        }

        private void setBgRectParams() {
            allocateBgRectangle();
            this.mBgDrawableDefault.setBounds(this.mBgRect);
            this.mScrollThumbBgDrawable.setBounds(this.mScrollThumbBgRect);
        }

        public void addSubIndex(String[] strArr) {
            if (this.m_bIsAlphabetInit && this.mDepth != this.mMaxDepth && strArr != null && strArr.length != 0) {
                if (this.m_bSubIndexScrollExists) {
                    this.mSubIndexScroll.addSubIndex(strArr);
                } else if (this.mSelectedIndex != -1) {
                    this.m_bSubIndexScrollExists = true;
                    int min = Math.min((int) ((this.mSeparatorHeight * ((float) strArr.length)) + 1.0f), this.mScreenHeight);
                    int max = Math.max(0, Math.min(this.mScrollTop + ((int) (((double) this.mSeparatorHeight) * (((double) this.mSelectedIndex) + 0.5d))), this.mScreenHeight - min));
                    this.mSubIndexScroll = new IndexScroll(this.mContext, this.mPosition, strArr, max, this.mScreenHeight, min, this.mWidth, (this.mItemWidth + this.mItemWidthGap) + this.mWidthShift, this.mMaxDepth, this.mDepth + 1);
                }
            }
        }

        public void draw(Canvas canvas) {
            if (this.m_bIsAlphabetInit) {
                drawScroll(canvas);
                if (this.m_bSubIndexScrollExists) {
                    this.mSubIndexScroll.draw(canvas);
                }
            }
        }

        public void drawEffect(float f) {
            if (this.mSelectedIndex != -1) {
                this.mSmallText = this.mAlphabetArrayToDraw[this.mSelectedIndex];
                this.mPaint.getTextBounds(this.mSmallText, 0, this.mSmallText.length(), this.mTextBounds);
                float centerX = ((float) this.mBgRect.centerX()) - (0.5f * this.mPaint.measureText(this.mSmallText));
                float descent = ((((float) this.mScrollTop) + ((float) this.mScrollTopMargin)) + ((float) ((((double) this.mSeparatorHeight) * (((double) this.mSelectedIndex) + 0.5d)) - ((double) (((float) this.mTextBounds.top) * 0.5f))))) + ((this.mPaint.descent() + this.mPaint.ascent()) / SprDocument.DEFAULT_DENSITY_SCALE);
                float f2 = (((float) this.mScrollTopMargin) + this.mPreviewLimitY) + this.mIndexScrollPreviewRadius;
                float f3 = ((((float) this.mScreenHeight) - ((float) this.mScrollBottomMargin)) - this.mPreviewLimitY) - this.mIndexScrollPreviewRadius;
                if (((float) this.mScreenHeight) <= (((this.mIndexScrollPreviewRadius * SprDocument.DEFAULT_DENSITY_SCALE) + this.mPreviewLimitY) + ((float) this.mScrollTopMargin)) + ((float) this.mScrollBottomMargin)) {
                    f2 = (((float) this.mScrollTop) + ((float) this.mScrollTopMargin)) + ((float) (((double) this.mFirstLang.separatorHeight) * 0.5d));
                    f3 = ((((((float) this.mScrollTop) + ((float) this.mScrollTopMargin)) - ((float) this.mScrollBottomMargin)) + this.mFirstLang.height) + this.mSecondLang.height) - ((float) (((double) this.mFirstLang.separatorHeight) * 0.5d));
                }
                float f4 = SemIndexScrollView.OUT_OF_BOUNDARY;
                if (f > f2 && f < f3) {
                    f4 = f;
                } else if (f <= f2) {
                    f4 = f2;
                } else if (f >= f3) {
                    f4 = f3;
                }
                if (f4 != SemIndexScrollView.OUT_OF_BOUNDARY) {
                    SemIndexScrollView.this.mIndexScrollPreview.open(f4, this.mBigText);
                    if (SemIndexScrollView.this.mOnIndexBarEventListener != null) {
                        SemIndexScrollView.this.mOnIndexBarEventListener.onPressed(f4);
                    }
                }
            }
        }

        public void drawScroll(Canvas canvas) {
            drawBgRectangle(canvas);
            drawAlphabetCharacters(canvas);
            if (this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetSize) {
                if (SemIndexScrollView.this.mIndexScrollPreview != null) {
                    SemIndexScrollView.this.mIndexScrollPreview.close();
                }
                if (SemIndexScrollView.this.mOnIndexBarEventListener != null) {
                    SemIndexScrollView.this.mOnIndexBarEventListener.onReleased(0.0f);
                }
            }
        }

        public int getDepth() {
            return !this.m_bSubIndexScrollExists ? this.mDepth : this.mSubIndexScroll.getDepth();
        }

        public int getHeight() {
            return this.mHeight;
        }

        public String getIndexByPosition(int i, int i2) {
            if (this.mBgRect == null) {
                return "";
            }
            if (!this.m_bIsAlphabetInit) {
                return "";
            }
            if ((this.mPosition == 0 && i < this.mBgRect.left - this.mAdditionalSpace) || (this.mPosition == 1 && i > this.mBgRect.right + this.mAdditionalSpace)) {
                return "";
            }
            if (i >= this.mBgRect.left - this.mAdditionalSpace && i <= this.mBgRect.right + this.mAdditionalSpace) {
                return (this.mDepth == 1 && isInSelectedIndexRect(i2)) ? !isSelectedIndexDot(this.mSelectedIndex) ? (this.mAlphabetArrayToDraw == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetArrayToDraw.length) ? "" : getIndexByY(i2) : getDotIndexByY(i2) : getIndexByY(i2);
            } else {
                if (!this.m_bSubIndexScrollExists) {
                    return (this.mDepth > this.mMaxDepth || ((this.mPosition != 0 || i < (this.mWidthShift + this.mItemWidth) + this.mItemWidthGap) && (this.mPosition != 1 || i > (this.mWidth - this.mWidthShift) - (this.mItemWidth + this.mItemWidthGap)))) ? (this.mDepth == 1 && isInSelectedIndexRect(i2)) ? !isSelectedIndexDot(this.mSelectedIndex) ? (this.mAlphabetArrayToDraw == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetArrayToDraw.length) ? "" : this.mAlphabetArrayToDraw[this.mSelectedIndex] : getDotIndexByY(i2) : getIndexByY(i2) : null;
                } else {
                    String str;
                    if (this.mSelectedIndex == -1) {
                        str = "";
                    } else if (isSelectedIndexDot(this.mSelectedIndex)) {
                        str = getDotIndexByY(i2);
                    } else {
                        try {
                            if (this.mAlphabetArrayToDraw == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mAlphabetArrayToDraw.length) {
                                return "";
                            }
                            str = this.mAlphabetArrayToDraw[this.mSelectedIndex];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            str = "";
                        }
                    }
                    String indexByPosition = this.mSubIndexScroll.getIndexByPosition(i, i2);
                    return indexByPosition == null ? null : str + indexByPosition;
                }
            }
        }

        public float getItemHeight() {
            return this.mItemHeight;
        }

        public int getItemPlusSpaceWidth() {
            return this.mItemWidth + this.mItemWidthGap;
        }

        public String[] getLangAlphabetArray() {
            return this.mAlphabetArray;
        }

        public int getNumberOfSmallerOrEqualIndexes(int i) {
            return ((this.mPosition == 0 ? i : this.mWidth - i) / (this.mItemWidth + this.mItemWidthGap)) + 1;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public int getSelectedIndex() {
            return !this.m_bAlphabetArrayWithDotsUsed ? this.mSelectedIndex : (this.mSelectedIndexPositionInOrigAlphabet == null || this.mSelectedIndex < 0 || this.mSelectedIndex >= this.mSelectedIndexPositionInOrigAlphabet.length) ? -1 : (!isSelectedIndexDot(this.mSelectedIndex) || this.mOrigSelectedDotIndex == -1) ? this.mSelectedIndexPositionInOrigAlphabet[this.mSelectedIndex] : this.mSelectedIndexPositionInOrigAlphabet[this.mSelectedIndex] + this.mOrigSelectedDotIndex;
        }

        public boolean hasSubIndex() {
            return this.m_bSubIndexScrollExists;
        }

        public boolean isAlphabetInit() {
            return this.m_bIsAlphabetInit;
        }

        public void removeAllSubIndexes() {
            if (this.m_bSubIndexScrollExists) {
                if (this.mSubIndexScroll.hasSubIndex()) {
                    this.mSubIndexScroll.removeAllSubIndexes();
                    this.mOrigSelectedIndex = -1;
                    this.mOrigSelectedDotIndex = -1;
                    this.m_bSubIndexScrollExists = false;
                    this.mSubIndexScroll = null;
                } else {
                    this.mOrigSelectedIndex = -1;
                    this.mOrigSelectedDotIndex = -1;
                    this.m_bSubIndexScrollExists = false;
                    this.mSubIndexScroll = null;
                }
            }
        }

        public void removeSubIndex() {
            if (this.m_bSubIndexScrollExists) {
                if (this.mSubIndexScroll.hasSubIndex()) {
                    this.mSubIndexScroll.removeSubIndex();
                } else {
                    this.mOrigSelectedIndex = -1;
                    this.mOrigSelectedDotIndex = -1;
                    this.m_bSubIndexScrollExists = false;
                    this.mSubIndexScroll = null;
                }
            }
        }

        public void resetSelectedIndex() {
            this.mSelectedIndex = -1;
        }

        public void setAlphabetArray(String[] strArr, int i, int i2) {
            if (strArr != null) {
                this.mAlphabetArray = strArr;
                this.mAlphabetSize = this.mAlphabetArray.length;
                this.mAlphabetArrayFirstLetterIndex = i;
                this.mAlphabetArrayLastLetterIndex = i2;
                this.mItemHeight = ((float) this.mHeight) / ((float) this.mAlphabetSize);
                this.mSeparatorHeight = Math.max(this.mItemHeight, this.mContentMinHeight);
                this.m_bIsAlphabetInit = true;
                this.mbSetDimensionns = true;
            }
        }

        public void setDimensionns(int i, int i2) {
            if (!this.m_bIsAlphabetInit) {
                return;
            }
            if (this.mWidth != i || this.mHeight != i2 || this.mbSetDimensionns) {
                this.mbSetDimensionns = false;
                this.mWidth = i;
                this.mHeight = ((i2 - (this.mScrollTop * 2)) - this.mScrollTopMargin) - this.mScrollBottomMargin;
                this.mScreenHeight = i2;
                this.mItemHeight = ((float) this.mHeight) / ((float) this.mAlphabetSize);
                this.mSeparatorHeight = Math.max(this.mItemHeight, this.mContentMinHeight);
                setBgRectParams();
                if (!(this.mFirstLang == null || this.mSecondLang == null)) {
                    this.mFirstLang.separatorHeight = this.mContentMinHeight;
                    this.mSecondLang.separatorHeight = this.mContentMinHeight;
                    manageIndexScrollHeight();
                }
            }
        }

        public void setEffectText(String str) {
            this.mBigText = str;
        }

        public void setIndexScrollBgMargin(int i, int i2) {
            this.mScrollTopMargin = i;
            this.mScrollBottomMargin = i2;
        }

        public void setMaxDepth(int i) {
            this.mMaxDepth = i;
        }

        public void setPosition(int i) {
            if (!this.m_bSubIndexScrollExists) {
                this.mPosition = i;
                setBgRectParams();
            }
        }

        public void setSimpleIndexScrollWidth(int i) {
            if (i > 0) {
                this.mItemWidth = i;
                this.mBgRectWidth = i;
                allocateBgRectangle();
            }
        }
    }

    class IndexScrollPreview extends View {
        private boolean mIsOpen = false;
        private float mPreviewCenterMargin;
        private float mPreviewCenterX;
        private float mPreviewCenterY;
        private float mPreviewRadius;
        private String mPreviewText;
        private Paint mShapePaint;
        private Rect mTextBounds;
        private Paint mTextPaint;
        private int mTextSize;
        private int mTextWidhtLimit;

        public IndexScrollPreview(Context context) {
            super(context);
            init(context);
        }

        private void fadeOutAnimation() {
            if (this.mIsOpen) {
                startAnimation();
                this.mIsOpen = false;
            }
        }

        private void init(Context context) {
            Resources resources = context.getResources();
            this.mShapePaint = new Paint();
            this.mShapePaint.setStyle(Style.FILL);
            this.mShapePaint.setAntiAlias(true);
            this.mTextSize = (int) resources.getDimension(17105791);
            this.mTextWidhtLimit = (int) resources.getDimension(17105792);
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTypeface(SemIndexScrollView.mSECRobotoLightRegularFont);
            this.mTextPaint.setTextAlign(Align.CENTER);
            this.mTextPaint.setTextSize((float) this.mTextSize);
            this.mTextPaint.setColor(resources.getColor(17170823));
            this.mTextBounds = new Rect();
            this.mPreviewRadius = resources.getDimension(17105793);
            this.mPreviewCenterMargin = resources.getDimension(17105794);
            this.mIsOpen = false;
        }

        public void close() {
            long currentTimeMillis = System.currentTimeMillis() - SemIndexScrollView.this.mStartTouchDown;
            removeCallbacks(SemIndexScrollView.this.mPreviewDelayRunnable);
            if (currentTimeMillis <= 100) {
                postDelayed(SemIndexScrollView.this.mPreviewDelayRunnable, 100);
            } else {
                fadeOutAnimation();
            }
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (SemIndexScrollView.this.mAnimEnd && this.mIsOpen) {
                canvas.drawCircle(this.mPreviewCenterX, this.mPreviewCenterY, this.mPreviewRadius, this.mShapePaint);
                this.mTextPaint.getTextBounds(this.mPreviewText, 0, this.mPreviewText.length() - 1, this.mTextBounds);
                canvas.drawText(this.mPreviewText, this.mPreviewCenterX, this.mPreviewCenterY - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / SprDocument.DEFAULT_DENSITY_SCALE), this.mTextPaint);
            }
        }

        public void open(float f, String str) {
            int i = this.mTextSize;
            this.mPreviewCenterY = f;
            this.mPreviewText = str;
            this.mTextPaint.setTextSize((float) i);
            while (this.mTextPaint.measureText(str) > ((float) this.mTextWidhtLimit)) {
                i--;
                this.mTextPaint.setTextSize((float) i);
            }
            if (!this.mIsOpen) {
                startAnimation();
                this.mIsOpen = true;
            }
        }

        public void setBackgroundColor(int i) {
            this.mShapePaint.setColor(i);
        }

        public void setLayout(int i, int i2, int i3, int i4) {
            layout(i, i2, i3, i4);
            if (SemIndexScrollView.this.mIndexBarGravity == 0) {
                this.mPreviewCenterX = this.mPreviewCenterMargin;
            } else {
                this.mPreviewCenterX = ((float) i3) - this.mPreviewCenterMargin;
            }
        }

        public void setTextColor(int i) {
            this.mTextPaint.setColor(i);
        }

        public void startAnimation() {
            Animator ofFloat;
            if (this.mIsOpen) {
                ofFloat = ObjectAnimator.ofFloat(SemIndexScrollView.this.mIndexScrollPreview, "alpha", new float[]{1.0f, 0.0f});
            } else {
                ofFloat = ObjectAnimator.ofFloat(SemIndexScrollView.this.mIndexScrollPreview, "alpha", new float[]{0.0f, 1.0f});
            }
            ofFloat.setDuration(167);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(ofFloat);
            animatorSet.start();
        }
    }

    class IndexerObserver extends DataSetObserver {
        private final long INDEX_UPDATE_DELAY = 200;
        boolean mDataInvalid = false;
        Runnable mUpdateIndex = new C02981();

        class C02981 implements Runnable {
            C02981() {
            }

            public void run() {
                IndexerObserver.this.mDataInvalid = false;
            }
        }

        IndexerObserver() {
        }

        private void notifyDataSetChange() {
            this.mDataInvalid = true;
            SemIndexScrollView.this.removeCallbacks(this.mUpdateIndex);
            SemIndexScrollView.this.postDelayed(this.mUpdateIndex, 200);
            SemIndexScrollView.this.initLangPositionBounds();
        }

        public boolean hasIndexerDataValid() {
            return !this.mDataInvalid;
        }

        public void onChanged() {
            super.onChanged();
            notifyDataSetChange();
        }

        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChange();
        }
    }

    public interface OnIndexBarEventListener {
        void onIndexChanged(int i);

        void onPressed(float f);

        void onReleased(float f);
    }

    public SemIndexScrollView(Context context) {
        super(context);
        this.mContext = context;
        this.mCurrentIndex = null;
        init(context, this.mIndexBarGravity);
    }

    public SemIndexScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mIndexBarGravity = 1;
        this.m_bNoSubIndexes = false;
        init(context, this.mIndexBarGravity);
    }

    private void addMissingSubIndexes(int i, int i2, int i3) {
        int i4 = 0;
        int itemPlusSpaceWidth = this.mIndexScroll.getItemPlusSpaceWidth();
        int depth = itemPlusSpaceWidth * this.mIndexScroll.getDepth();
        Object obj = null;
        int position = this.mIndexScroll.getPosition();
        int width = getWidth();
        while (i4 < i3 && r4 == null) {
            String[] subIndexes = getSubIndexes(this.mCurrentIndex);
            if (subIndexes == null || subIndexes.length == 0) {
                obj = 1;
            } else {
                this.mIndexScroll.addSubIndex(subIndexes);
                this.mCalculatedIndexStr = this.mIndexScroll.getIndexByPosition(position == 0 ? depth : width - depth, i2);
                depth += itemPlusSpaceWidth;
                i4++;
                int listViewPosition = getListViewPosition(this.mCalculatedIndexStr);
                if (listViewPosition != -1) {
                    notifyIndexChange(listViewPosition);
                }
                this.mCurrentIndex = this.mCalculatedIndexStr;
            }
        }
    }

    private int getDbPositionLanguage(int i) {
        if (this.mLangDbStartPositions == null || this.mLangDbEndPositions == null) {
            return -1;
        }
        int i2 = 0;
        while (i2 < this.mNumberOfLanguages && (i < this.mLangDbStartPositions[i2] || i > this.mLangDbEndPositions[i2])) {
            i2++;
        }
        if (i2 == this.mNumberOfLanguages) {
            i2 = this.mPrevSetLang;
        }
        return i2;
    }

    private int getFirstAlphabetCharacterIndex() {
        int currentLang = this.mIndexer.getCurrentLang();
        int length = this.mIndexer.getAlphabetArray().length;
        int i = 0;
        while (i < length && currentLang != this.mIndexer.getLangbyIndex(i)) {
            i++;
        }
        return i < length ? i : -1;
    }

    private int getLastAlphabetCharacterIndex() {
        if (this.mIndexer == null) {
            return -1;
        }
        int currentLang = this.mIndexer.getCurrentLang();
        int length = this.mIndexer.getAlphabetArray().length;
        int i = length - 1;
        while (i >= 0 && this.mIndexer != null && currentLang != this.mIndexer.getLangbyIndex(i)) {
            i--;
        }
        return i > 0 ? (length - 1) - i : -1;
    }

    private int getListViewPosition(String str) {
        int i = -1;
        if (str == null || this.mIndexer == null) {
            return -1;
        }
        if (this.mIndexer != null) {
            return this.mIndexer.getCachingValue(this.mIndexScroll.getSelectedIndex());
        }
        int i2;
        int currentLang = this.mIndexer.getCurrentLang();
        Object obj = null;
        Object obj2 = str;
        ArrayList arrayList = null;
        if (str.length() > 1) {
            String substring = str.substring(0, str.length() - 1);
            obj2 = str.substring(str.length() - 1);
            arrayList = this.mIndexer.getIndexInfo(substring, true);
        } else if (str.length() == 1) {
            int langbyIndex = this.mIndexer.getLangbyIndex(this.mIndexScroll.getSelectedIndex());
            arrayList = this.mIndexer.getIndexInfo();
            if (arrayList == null || arrayList.size() <= 0) {
                return -1;
            }
            for (i2 = 0; i2 < arrayList.size(); i2++) {
                if (((IndexInfo) arrayList.get(i2)).mIndexString.equals(str)) {
                    i = ((IndexInfo) arrayList.get(i2)).mPosition;
                }
            }
            int dbPositionLanguage = getDbPositionLanguage(i);
            if (dbPositionLanguage == -1 || langbyIndex == currentLang) {
                return i;
            }
            obj = 1;
            this.mIndexer.setMultiLangIndexer(dbPositionLanguage);
            arrayList = this.mIndexer.getIndexInfo();
        }
        if (arrayList == null || arrayList.size() <= 0) {
            return i;
        }
        for (i2 = 0; i2 < arrayList.size(); i2++) {
            if (((IndexInfo) arrayList.get(i2)).mIndexString.equals(obj2)) {
                i = ((IndexInfo) arrayList.get(i2)).mPosition;
            }
        }
        if (obj != null) {
            this.mIndexer.setMultiLangIndexer(currentLang);
            arrayList = this.mIndexer.getIndexInfo();
        }
        return i;
    }

    private int getNumberOfMissingSubIndexes(int i) {
        return (this.mIndexScroll.getNumberOfSmallerOrEqualIndexes(i) - this.mIndexScroll.getDepth()) - 1;
    }

    private String[] getSubIndexes(String str) {
        ArrayList indexInfo = this.mIndexer.getIndexInfo(str, true);
        if (indexInfo == null || (indexInfo != null && indexInfo.size() == 0)) {
            return null;
        }
        String[] strArr = new String[indexInfo.size()];
        for (int i = 0; i < indexInfo.size(); i++) {
            if (((IndexInfo) indexInfo.get(i)).mExists) {
                strArr[i] = new String(((IndexInfo) indexInfo.get(i)).mIndexString);
            }
        }
        return strArr;
    }

    private boolean handleMotionEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        float x = motionEvent.getX();
        int listViewPosition;
        switch (action) {
            case 0:
                this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                this.mStartTouchDown = System.currentTimeMillis();
                if (this.mCurrentIndex != null) {
                    if (!(!this.mIndexScroll.isAlphabetInit() || this.mCurrentIndex == null || this.mCurrentIndex.length() == 0)) {
                        this.mIndexScroll.setEffectText(this.mCurrentIndex);
                        this.mIndexScroll.drawEffect(y);
                        this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
                        this.mIndexScrollPreview.invalidate();
                        this.mTouchY = y;
                    }
                    listViewPosition = !this.m_bSimpleIndexScroll ? getListViewPosition(this.mCurrentIndex) : this.mIndexScroll.getSelectedIndex();
                    if (listViewPosition != -1) {
                        notifyIndexChange(listViewPosition);
                        break;
                    }
                }
                return false;
                break;
            case 1:
            case 3:
                this.mCurrentIndex = null;
                this.m_bNoSubIndexes = false;
                this.mIndexScroll.removeAllSubIndexes();
                this.mIndexScroll.resetSelectedIndex();
                this.mIndexScrollPreview.close();
                if (this.mOnIndexBarEventListener != null) {
                    this.mOnIndexBarEventListener.onReleased(y);
                }
                this.mTouchY = OUT_OF_BOUNDARY;
                break;
            case 2:
                int numberOfMissingSubIndexes = getNumberOfMissingSubIndexes((int) x);
                if ((!this.m_bNoSubIndexes || numberOfMissingSubIndexes < 1) && this.mAnimEnd) {
                    this.mCalculatedIndexStr = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                    if (this.mCurrentIndex == null || this.mCalculatedIndexStr != null || this.m_bSimpleIndexScroll) {
                        if (this.mCurrentIndex != null && this.mCalculatedIndexStr != null && this.mCalculatedIndexStr.length() < this.mCurrentIndex.length()) {
                            this.m_bNoSubIndexes = false;
                            int length = this.mCurrentIndex.length() - this.mCalculatedIndexStr.length();
                            for (int i = 0; i < length; i++) {
                                this.mIndexScroll.removeSubIndex();
                            }
                            this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                            listViewPosition = !this.m_bSimpleIndexScroll ? getListViewPosition(this.mCurrentIndex) : this.mIndexScroll.getSelectedIndex();
                            if (listViewPosition != -1) {
                                notifyIndexChange(listViewPosition);
                                break;
                            }
                        }
                        this.m_bNoSubIndexes = false;
                        this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                        if (!(!this.mIndexScroll.isAlphabetInit() || this.mCurrentIndex == null || this.mCurrentIndex.length() == 0)) {
                            this.mIndexScroll.setEffectText(this.mCurrentIndex);
                            this.mIndexScroll.drawEffect(y);
                            this.mTouchY = y;
                        }
                        listViewPosition = !this.m_bSimpleIndexScroll ? getListViewPosition(this.mCurrentIndex) : this.mIndexScroll.getSelectedIndex();
                        if (listViewPosition != -1) {
                            notifyIndexChange(listViewPosition);
                            break;
                        }
                    }
                    if (numberOfMissingSubIndexes > 0) {
                        this.mCalculatedIndexStr = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                    }
                    this.mCurrentIndex = this.mIndexScroll.getIndexByPosition((int) x, (int) y);
                    listViewPosition = getListViewPosition(this.mCalculatedIndexStr);
                    if (listViewPosition != -1) {
                        notifyIndexChange(listViewPosition);
                        break;
                    }
                }
                return true;
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private void init(Context context, int i) {
        this.mViewGroupOverlay = getOverlay();
        if (this.mIndexScrollPreview == null) {
            this.mIndexScrollPreview = new IndexScrollPreview(this.mContext);
            this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
            this.mViewGroupOverlay.add(this.mIndexScrollPreview);
        }
        this.mHasOverlayChild = true;
        this.mIndexScroll = new IndexScroll(this.mContext, getHeight(), getWidth(), i);
        this.mIndexScroll.setMaxDepth(this.DEFAULT_MAX_DEPTH);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean initIndexerLanguagesBounds() {
        /*
        r3 = this;
        r2 = 0;
        r1 = r3.mIndexer;
        if (r1 == 0) goto L_0x0009;
    L_0x0005:
        r1 = r3.m_bSimpleIndexScroll;
        if (r1 == 0) goto L_0x000a;
    L_0x0009:
        return r2;
    L_0x000a:
        r1 = r3.mIndexer;
        r1 = r1.getLangAlphabetArray();
        if (r1 != 0) goto L_0x0013;
    L_0x0012:
        return r2;
    L_0x0013:
        r0 = 0;
        r1 = r3.mIndexer;
        r1 = r1.getLangAlphabetArray();
        r1 = r1.length;
        r3.mNumberOfLanguages = r1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.widget.SemIndexScrollView.initIndexerLanguagesBounds():boolean");
    }

    private void initLangPositionBounds() {
        this.mLangScrollStartPositions = null;
        this.mLangScrollEndPositions = null;
        this.mLangDbStartPositions = null;
        this.mLangDbEndPositions = null;
    }

    private void notifyIndexChange(int i) {
        try {
            if (this.mOnIndexBarEventListener != null) {
                this.mOnIndexBarEventListener.onIndexChanged(i);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setLangPosition(SemIndexScrollView semIndexScrollView, int i, int i2) {
        if (this.mIndexer != null && !this.m_bSimpleIndexScroll) {
            if (this.mLangScrollStartPositions == null || this.mLangScrollEndPositions == null) {
                if (this.mFirstLanguageGap == -1) {
                    this.mFirstLanguageGap = i2 - (this.mIndexer.getItemCount() + 1);
                }
                if (!initIndexerLanguagesBounds()) {
                    return;
                }
            }
            int i3 = 0;
            while (i3 < this.mNumberOfLanguages && (i < this.mLangScrollStartPositions[i3] || i > this.mLangScrollEndPositions[i3])) {
                i3++;
            }
            if (i3 == this.mNumberOfLanguages) {
                i3 = this.mPrevSetLang;
            }
            if (i3 != this.mPrevSetLang) {
                this.mIndexer.setMultiLangIndexer(i3);
                this.mIndexScroll.removeAllSubIndexes();
                this.mIndexScroll.resetSelectedIndex();
                this.mIndexScroll.setAlphabetArray(this.mIndexer.getAlphabetArray(), getFirstAlphabetCharacterIndex(), getLastAlphabetCharacterIndex());
                try {
                    startAnimation(this, this.mPrevSetLang, i3, 0.0f);
                } catch (NoClassDefFoundError e) {
                }
                this.mPrevSetLang = i3;
                invalidate();
            }
        }
    }

    private void setSimpleIndexWidth(int i) {
        if (this.mIndexScroll != null) {
            this.mIndexScroll.setSimpleIndexScrollWidth(i);
        }
    }

    private void startAnimation(Object obj, int i, int i2, float f) throws NoClassDefFoundError {
        float top = (float) getTop();
        try {
            Animator ofFloat = ObjectAnimator.ofFloat(obj, "y", new float[]{((float) this.mIndexScroll.getHeight()) - this.mIndexScroll.getItemHeight(), top});
            Animator ofFloat2 = ObjectAnimator.ofFloat(obj, "y", new float[]{this.mIndexScroll.getItemHeight() - ((float) this.mIndexScroll.getHeight()), top});
            final Animator animatorSet = new AnimatorSet();
            if (i < i2) {
                animatorSet.play(ofFloat);
            } else {
                animatorSet.play(ofFloat2);
            }
            animatorSet.setDuration(300);
            animatorSet.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    animatorSet.removeAllListeners();
                    animatorSet.cancel();
                    SemIndexScrollView.this.mAnimEnd = true;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    SemIndexScrollView.this.mAnimEnd = false;
                }
            });
            animatorSet.start();
        } catch (NoClassDefFoundError e) {
            throw e;
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mIndexScroll != null) {
            if (!this.mSipResizeAnimationState) {
                this.mIndexScroll.setDimensionns(getWidth(), getHeight());
            }
            if (!(this.mCurrentIndex == null || this.mCurrentIndex.length() == 0 || this.mIndexScrollPreview == null)) {
                this.mIndexScrollPreview.setLayout(0, 0, getWidth(), getHeight());
                this.mIndexScrollPreview.invalidate();
            }
            if (this.mIndexScroll != null && this.mIndexScroll.isAlphabetInit()) {
                this.mIndexScroll.draw(canvas);
            }
        }
    }

    protected boolean dispatchSipResizeAnimationState(boolean z) {
        this.mSipResizeAnimationState = z;
        return false;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mHasOverlayChild) {
            this.mViewGroupOverlay.add(this.mIndexScrollPreview);
            this.mHasOverlayChild = true;
        }
        if (this.mIndexer != null && !this.mRegisteredDataSetObserver) {
            this.mIndexer.registerDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = true;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mHasOverlayChild) {
            this.mViewGroupOverlay.remove(this.mIndexScrollPreview);
            this.mHasOverlayChild = false;
        }
        if (this.mIndexer != null && this.mRegisteredDataSetObserver) {
            this.mIndexer.unregisterDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = false;
        }
        if (this.mPreviewDelayRunnable != null) {
            removeCallbacks(this.mPreviewDelayRunnable);
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if (getVisibility() != 0 || getParent() != null) {
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return handleMotionEvent(motionEvent);
    }

    public void setEffectBackgroundColor(int i) {
        this.mIndexScrollPreview.setBackgroundColor(this.mIndexScroll.getColorWithAlpha(i, 0.8f));
    }

    public void setEffectTextColor(int i) {
        this.mIndexScrollPreview.setTextColor(i);
    }

    public void setIndexBarBackgroundDrawable(Drawable drawable) {
        this.mIndexScroll.mBgDrawableDefault = drawable;
        this.mIndexScroll.mBgDrawableDefault.setTintList(null);
    }

    public void setIndexBarGravity(int i) {
        this.mIndexBarGravity = i;
        this.mIndexScroll.setPosition(i);
    }

    public void setIndexBarPressedTextColor(int i) {
        this.mIndexScroll.mScrollThumbBgDrawable.setTintList(null);
        this.mIndexScroll.mScrollThumbBgDrawable.setTint(i);
        this.mIndexScroll.mThumbColor = i;
    }

    public void setIndexBarTextColor(int i) {
        this.mIndexScroll.mTextColorDimmed = i;
    }

    public void setIndexScrollMargin(int i, int i2) {
        if (this.mIndexScroll != null) {
            this.mIndexScroll.setIndexScrollBgMargin(i, i2);
        }
    }

    public void setIndexer(SemAbstractIndexer semAbstractIndexer) {
        if (semAbstractIndexer == null) {
            throw new IllegalArgumentException("SemIndexView.setIndexer(indexer) : indexer=null.");
        }
        if (this.mIndexer != null && this.mRegisteredDataSetObserver) {
            this.mIndexer.unregisterDataSetObserver(this.mIndexerObserver);
            this.mRegisteredDataSetObserver = false;
        }
        this.m_bSimpleIndexScroll = false;
        this.mIndexer = semAbstractIndexer;
        this.mIndexer.registerDataSetObserver(this.mIndexerObserver);
        this.mRegisteredDataSetObserver = true;
        if (this.mIndexScroll.mScrollThumbBgDrawable != null) {
            this.mIndexScroll.mScrollThumbBgDrawable.setTint(this.mIndexScroll.mThumbColor);
        }
        if (this.mPrevSetLang != 0) {
            this.mIndexer.setMultiLangIndexer(this.mPrevSetLang);
            this.mIndexer.getIndexInfo(this.mIndexer.getAlphabetArray()[this.mIndexer.getCurrentLangStartIndex()]);
            this.mIndexScroll.removeAllSubIndexes();
            this.mIndexScroll.resetSelectedIndex();
        } else {
            this.mIndexer.getIndexInfo();
        }
        this.mIndexScroll.setAlphabetArray(this.mIndexer.getAlphabetArray(), getFirstAlphabetCharacterIndex(), getLastAlphabetCharacterIndex());
        this.mLangScrollStartPositions = null;
        this.mLangScrollEndPositions = null;
        this.mLangDbStartPositions = null;
        this.mLangDbEndPositions = null;
        initIndexerLanguagesBounds();
    }

    public void setOnIndexBarEventListener(OnIndexBarEventListener onIndexBarEventListener) {
        this.mOnIndexBarEventListener = onIndexBarEventListener;
    }

    public void setSimpleIndexScroll(String[] strArr, int i) {
        if (strArr == null) {
            throw new IllegalArgumentException("SemIndexView.setSimpleIndexScroll(indexBarChar) ");
        }
        this.m_bSimpleIndexScroll = true;
        setSimpleIndexWidth((int) this.mContext.getResources().getDimension(17105803));
        if (i != 0) {
            setSimpleIndexWidth(i);
        }
        if (this.mIndexScroll.mScrollThumbBgDrawable != null) {
            this.mIndexScroll.mScrollThumbBgDrawable.setTint(this.mIndexScroll.mThumbColor);
        }
        this.mIndexScroll.setAlphabetArray(strArr, -1, -1);
    }
}
