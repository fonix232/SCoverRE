package com.samsung.android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.IntProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroupOverlay;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SectionIndexer;
import android.widget.SemHorizontalAbsListView;
import android.widget.SemHorizontalListView;
import android.widget.TextView;
import com.android.internal.R;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SemHorizontalFastScroller {
    private static Property<View, Integer> BOTTOM = new IntProperty<View>("bottom") {
        public Integer get(View view) {
            return Integer.valueOf(view.getBottom());
        }

        public void setValue(View view, int i) {
            view.setBottom(i);
        }
    };
    private static final int DURATION_CROSS_FADE = 50;
    private static final int DURATION_FADE_IN = 150;
    private static final int DURATION_FADE_OUT = 300;
    private static final int DURATION_RESIZE = 100;
    private static final long FADE_TIMEOUT = 1500;
    private static Property<View, Integer> LEFT = new IntProperty<View>("left") {
        public Integer get(View view) {
            return Integer.valueOf(view.getLeft());
        }

        public void setValue(View view, int i) {
            view.setLeft(i);
        }
    };
    private static final int MIN_PAGES = 4;
    private static final int OVERLAY_ABOVE_THUMB = 2;
    private static final int OVERLAY_AT_THUMB = 1;
    private static final int OVERLAY_FLOATING = 0;
    private static final int PREVIEW_BOTTOM = 1;
    private static final int PREVIEW_TOP = 0;
    private static Property<View, Integer> RIGHT = new IntProperty<View>("right") {
        public Integer get(View view) {
            return Integer.valueOf(view.getRight());
        }

        public void setValue(View view, int i) {
            view.setRight(i);
        }
    };
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 1;
    private static final long TAP_TIMEOUT = ((long) ViewConfiguration.getTapTimeout());
    private static Property<View, Integer> TOP = new IntProperty<View>("top") {
        public Integer get(View view) {
            return Integer.valueOf(view.getTop());
        }

        public void setValue(View view, int i) {
            view.setTop(i);
        }
    };
    private boolean mAlwaysShow;
    private final Rect mContainerRect = new Rect();
    private int mCurrentSection = -1;
    private AnimatorSet mDecorAnimation;
    private final Runnable mDeferHide = new C02811();
    private boolean mEnabled;
    private int mFirstVisibleItem;
    private int mHeaderCount;
    private int mHeight;
    private float mInitialTouchX;
    private boolean mLayoutFromBottom;
    private final SemHorizontalAbsListView mList;
    private Adapter mListAdapter;
    private boolean mLongList;
    private boolean mMatchDragPosition;
    private int mOldChildCount;
    private int mOldItemCount;
    private final ViewGroupOverlay mOverlay;
    private int mOverlayPosition;
    private long mPendingDrag = -1;
    private AnimatorSet mPreviewAnimation;
    private final View mPreviewImage;
    private int mPreviewMinHeight;
    private int mPreviewMinWidth;
    private int mPreviewPadding;
    private final int[] mPreviewResId = new int[2];
    private final TextView mPrimaryText;
    private int mScaledTouchSlop;
    private int mScrollBarStyle;
    private boolean mScrollCompleted;
    private int mScrollbarPosition = -1;
    private final TextView mSecondaryText;
    private SectionIndexer mSectionIndexer;
    private Object[] mSections;
    private boolean mShowingPreview;
    private boolean mShowingPrimary;
    private int mState;
    private final AnimatorListener mSwitchPrimaryListener = new C02822();
    private final Rect mTempBounds = new Rect();
    private final Rect mTempMargins = new Rect();
    private int mTextAppearance;
    private ColorStateList mTextColor;
    private float mTextSize;
    private Drawable mThumbDrawable;
    private final ImageView mThumbImage;
    private int mThumbMinHeight;
    private int mThumbMinWidth;
    private Drawable mTrackDrawable;
    private final ImageView mTrackImage;
    private boolean mUpdatingLayout;

    class C02811 implements Runnable {
        C02811() {
        }

        public void run() {
            SemHorizontalFastScroller.this.setState(0);
        }
    }

    class C02822 extends AnimatorListenerAdapter {
        C02822() {
        }

        public void onAnimationEnd(Animator animator) {
            SemHorizontalFastScroller.this.mShowingPrimary = !SemHorizontalFastScroller.this.mShowingPrimary;
        }
    }

    public SemHorizontalFastScroller(SemHorizontalAbsListView semHorizontalAbsListView, int i) {
        boolean z = true;
        this.mList = semHorizontalAbsListView;
        this.mOldItemCount = semHorizontalAbsListView.getCount();
        this.mOldChildCount = semHorizontalAbsListView.getChildCount();
        Context context = semHorizontalAbsListView.getContext();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mScrollBarStyle = semHorizontalAbsListView.getScrollBarStyle();
        this.mScrollCompleted = true;
        this.mState = 1;
        if (context.getApplicationInfo().targetSdkVersion < 11) {
            z = false;
        }
        this.mMatchDragPosition = z;
        this.mTrackImage = new ImageView(context);
        this.mTrackImage.setScaleType(ScaleType.FIT_XY);
        this.mThumbImage = new ImageView(context);
        this.mThumbImage.setScaleType(ScaleType.FIT_XY);
        this.mPreviewImage = new View(context);
        this.mPreviewImage.setAlpha(0.0f);
        this.mPrimaryText = createPreviewTextView(context);
        this.mSecondaryText = createPreviewTextView(context);
        setStyle(i);
        ViewGroupOverlay overlay = semHorizontalAbsListView.getOverlay();
        this.mOverlay = overlay;
        overlay.add(this.mTrackImage);
        overlay.add(this.mThumbImage);
        overlay.add(this.mPreviewImage);
        overlay.add(this.mPrimaryText);
        overlay.add(this.mSecondaryText);
        getSectionsFromIndexer();
        updateLongList(this.mOldChildCount, this.mOldItemCount);
        setScrollbarPosition(semHorizontalAbsListView.semGetHorizontalScrollbarPosition());
        postAutoHide();
    }

    private static Animator animateAlpha(View view, float f) {
        return ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{f});
    }

    private static Animator animateBounds(View view, Rect rect) {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(LEFT, new int[]{rect.left});
        PropertyValuesHolder ofInt2 = PropertyValuesHolder.ofInt(TOP, new int[]{rect.top});
        PropertyValuesHolder ofInt3 = PropertyValuesHolder.ofInt(RIGHT, new int[]{rect.right});
        PropertyValuesHolder ofInt4 = PropertyValuesHolder.ofInt(BOTTOM, new int[]{rect.bottom});
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{ofInt, ofInt2, ofInt3, ofInt4});
    }

    private static Animator animateScaleY(View view, float f) {
        return ObjectAnimator.ofFloat(view, View.SCALE_Y, new float[]{f});
    }

    private void applyLayout(View view, Rect rect) {
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
        view.setPivotY((float) (this.mLayoutFromBottom ? rect.bottom - rect.top : 0));
    }

    private void beginDrag() {
        this.mPendingDrag = -1;
        setState(2);
        if (this.mListAdapter == null && this.mList != null) {
            getSectionsFromIndexer();
        }
        if (this.mList != null) {
            this.mList.requestDisallowInterceptTouchEvent(true);
            this.mList.reportScrollStateChange(1);
            cancelFling();
        }
    }

    private void cancelFling() {
        MotionEvent obtain = MotionEvent.obtain(0, 0, 3, 0.0f, 0.0f, 0);
        this.mList.onTouchEvent(obtain);
        obtain.recycle();
    }

    private void cancelPendingDrag() {
        this.mPendingDrag = -1;
    }

    private TextView createPreviewTextView(Context context) {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        View textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MIDDLE);
        textView.setGravity(17);
        textView.setAlpha(0.0f);
        textView.setLayoutDirection(this.mList.getLayoutDirection());
        return textView;
    }

    private float getPosFromItemCount(int i, int i2, int i3) {
        Object obj;
        if (this.mSectionIndexer == null || this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        if (this.mSectionIndexer == null || this.mSections == null) {
            obj = null;
        } else {
            obj = this.mSections.length > 0 ? 1 : null;
        }
        if (obj == null || !this.mMatchDragPosition) {
            return ((float) i) / ((float) (i3 - i2));
        }
        i -= this.mHeaderCount;
        if (i < 0) {
            return 0.0f;
        }
        int positionForSection;
        i3 -= this.mHeaderCount;
        View childAt = this.mList.getChildAt(0);
        float paddingLeft = (childAt == null || childAt.getWidth() == 0) ? 0.0f : ((float) (this.mList.getPaddingLeft() - childAt.getLeft())) / ((float) childAt.getWidth());
        int sectionForPosition = this.mSectionIndexer.getSectionForPosition(i);
        int positionForSection2 = this.mSectionIndexer.getPositionForSection(sectionForPosition);
        int length = this.mSections.length;
        if (sectionForPosition < length - 1) {
            positionForSection = (sectionForPosition + 1 < length ? this.mSectionIndexer.getPositionForSection(sectionForPosition + 1) : i3 - 1) - positionForSection2;
        } else {
            positionForSection = i3 - positionForSection2;
        }
        float f = (((float) sectionForPosition) + (positionForSection == 0 ? 0.0f : ((((float) i) + paddingLeft) - ((float) positionForSection2)) / ((float) positionForSection))) / ((float) length);
        if (i > 0 && i + i2 == i3) {
            int width;
            int width2;
            View childAt2 = this.mList.getChildAt(i2 - 1);
            int paddingRight = this.mList.getPaddingRight();
            if (this.mList.getClipToPadding()) {
                width = childAt2.getWidth();
                width2 = (this.mList.getWidth() - paddingRight) - childAt2.getLeft();
            } else {
                width = childAt2.getWidth() + paddingRight;
                width2 = this.mList.getWidth() - childAt2.getLeft();
            }
            if (width2 > 0 && width > 0) {
                f += (1.0f - f) * (((float) width2) / ((float) width));
            }
        }
        return f;
    }

    private float getPosFromMotionEvent(float f) {
        View view = this.mTrackImage;
        float left = (float) view.getLeft();
        float f2 = left;
        float right = ((float) view.getRight()) - left;
        return right <= 0.0f ? 0.0f : MathUtils.constrain((f - left) / right, 0.0f, 1.0f);
    }

    private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        Adapter adapter = this.mList.getAdapter();
        if (adapter instanceof SemHorizontalHeaderViewListAdapter) {
            this.mHeaderCount = adapter.getHeadersCount();
            adapter = adapter.getWrappedAdapter();
        }
        if (adapter instanceof SectionIndexer) {
            this.mListAdapter = adapter;
            this.mSectionIndexer = (SectionIndexer) adapter;
            this.mSections = this.mSectionIndexer.getSections();
            return;
        }
        this.mListAdapter = adapter;
        this.mSections = null;
    }

    private static Animator groupAnimatorOfFloat(Property<View, Float> property, float f, View... viewArr) {
        Animator animatorSet = new AnimatorSet();
        Builder builder = null;
        for (int length = viewArr.length - 1; length >= 0; length--) {
            Animator ofFloat = ObjectAnimator.ofFloat(viewArr[length], property, new float[]{f});
            if (builder == null) {
                builder = animatorSet.play(ofFloat);
            } else {
                builder.with(ofFloat);
            }
        }
        return animatorSet;
    }

    private boolean isPointInside(float f, float f2) {
        return isPointInsideY(f2) ? this.mTrackDrawable == null ? isPointInsideX(f) : true : false;
    }

    private boolean isPointInsideX(float f) {
        float translationX = this.mThumbImage.getTranslationX();
        return f >= ((float) this.mThumbImage.getLeft()) + translationX && f <= ((float) this.mThumbImage.getRight()) + translationX;
    }

    private boolean isPointInsideY(float f) {
        boolean z = true;
        if (this.mLayoutFromBottom) {
            if (f < ((float) this.mThumbImage.getTop())) {
                z = false;
            }
            return z;
        }
        if (f > ((float) this.mThumbImage.getBottom())) {
            z = false;
        }
        return z;
    }

    private void layoutThumb() {
        Rect rect = this.mTempBounds;
        measureViewToSide(this.mThumbImage, null, null, rect);
        applyLayout(this.mThumbImage, rect);
    }

    private void layoutTrack() {
        View view = this.mTrackImage;
        View view2 = this.mThumbImage;
        Rect rect = this.mContainerRect;
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(rect.height(), FingerprintManager.PRIVILEGED_TYPE_KEYGUARD));
        int measuredHeight = view.getMeasuredHeight();
        int width = view2.getWidth() / 2;
        int top = view2.getTop() + ((view2.getHeight() - measuredHeight) / 2);
        view.layout(rect.left + width, top, rect.right - width, top + measuredHeight);
    }

    private void measureFloating(View view, Rect rect, Rect rect2) {
        int i;
        int i2;
        int i3;
        if (rect == null) {
            i = 0;
            i2 = 0;
            i3 = 0;
        } else {
            i = rect.left;
            i2 = rect.top;
            i3 = rect.bottom;
        }
        Rect rect3 = this.mContainerRect;
        int height = rect3.height();
        View view2 = view;
        view2.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec((height - i2) - i3, FingerprintManager.PRIVILEGED_TYPE_KEYGUARD));
        int width = rect3.width();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = ((width / 10) + i) + rect3.left;
        Rect rect4 = rect2;
        rect4.set(i4, ((height - measuredHeight) / 2) + rect3.top, i4 + view.getMeasuredWidth(), i4 + measuredHeight);
    }

    private void measurePreview(View view, Rect rect) {
        Rect rect2 = this.mTempMargins;
        rect2.left = this.mPreviewImage.getPaddingLeft();
        rect2.top = this.mPreviewImage.getPaddingTop();
        rect2.right = this.mPreviewImage.getPaddingRight();
        rect2.bottom = this.mPreviewImage.getPaddingBottom();
        if (this.mOverlayPosition == 0) {
            measureFloating(view, rect2, rect);
        } else {
            measureViewToSide(view, this.mThumbImage, rect2, rect);
        }
    }

    private void measureViewToSide(View view, View view2, Rect rect, Rect rect2) {
        int i;
        int i2;
        int i3;
        int top;
        int i4;
        if (rect == null) {
            i = 0;
            i2 = 0;
            i3 = 0;
        } else {
            i = rect.left;
            i2 = rect.top;
            i3 = rect.bottom;
        }
        Rect rect3 = this.mContainerRect;
        int height = rect3.height();
        int top2 = view2 == null ? height : this.mLayoutFromBottom ? view2.getTop() : height - view2.getBottom();
        int i5 = (top2 - i2) - i3;
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(i5, FingerprintManager.PRIVILEGED_TYPE_KEYGUARD));
        int min = Math.min(i5, view.getMeasuredHeight());
        if (this.mLayoutFromBottom) {
            top = (view2 == null ? rect3.bottom : view2.getTop()) - i3;
            i4 = top - min;
        } else {
            i4 = (view2 == null ? rect3.top : view2.getBottom()) + i2;
            top = i4 + min;
        }
        int i6 = i;
        rect2.set(i6, i4, i6 + view.getMeasuredWidth(), top);
    }

    private void onStateDependencyChanged(boolean z) {
        if (!isEnabled()) {
            stop();
        } else if (isAlwaysShowEnabled()) {
            setState(1);
        } else if (this.mState == 1) {
            postAutoHide();
        } else if (z) {
            setState(1);
            postAutoHide();
        }
        this.mList.resolvePadding();
    }

    private void postAutoHide() {
        this.mList.removeCallbacks(this.mDeferHide);
        this.mList.postDelayed(this.mDeferHide, FADE_TIMEOUT);
    }

    private void refreshDrawablePressedState() {
        boolean z = this.mState == 2;
        this.mThumbImage.setPressed(z);
        this.mTrackImage.setPressed(z);
    }

    private void scrollTo(float f) {
        int i;
        this.mScrollCompleted = false;
        int count = this.mList.getCount();
        Object[] objArr = this.mSections;
        int length = objArr == null ? 0 : objArr.length;
        if (objArr == null || length <= 1) {
            int constrain = MathUtils.constrain((int) (((float) count) * f), 0, count - 1);
            if (this.mList instanceof SemHorizontalListView) {
                ((SemHorizontalListView) this.mList).setSelectionFromStart(this.mHeaderCount + constrain, 0);
            } else {
                this.mList.setSelection(this.mHeaderCount + constrain);
            }
            i = -1;
        } else {
            int constrain2 = MathUtils.constrain((int) (((float) length) * f), 0, length - 1);
            int i2 = constrain2;
            int positionForSection = this.mSectionIndexer.getPositionForSection(constrain2);
            i = constrain2;
            int i3 = count;
            int i4 = positionForSection;
            int i5 = constrain2;
            int i6 = constrain2 + 1;
            if (constrain2 < length - 1) {
                i3 = this.mSectionIndexer.getPositionForSection(constrain2 + 1);
            }
            if (i3 == positionForSection) {
                while (i2 > 0) {
                    i2--;
                    i4 = this.mSectionIndexer.getPositionForSection(i2);
                    if (i4 == positionForSection) {
                        if (i2 == 0) {
                            i = 0;
                            break;
                        }
                    }
                    i5 = i2;
                    i = i2;
                    break;
                }
            }
            int i7 = i6 + 1;
            while (i7 < length && this.mSectionIndexer.getPositionForSection(i7) == i3) {
                i7++;
                i6++;
            }
            float f2 = ((float) i5) / ((float) length);
            positionForSection = (i5 != constrain2 || f - f2 >= (count == 0 ? Float.MAX_VALUE : 0.125f / ((float) count))) ? i4 + ((int) ((((float) (i3 - i4)) * (f - f2)) / ((((float) i6) / ((float) length)) - f2))) : i4;
            positionForSection = MathUtils.constrain(positionForSection, 0, count - 1);
            if (this.mList instanceof SemHorizontalListView) {
                ((SemHorizontalListView) this.mList).setSelectionFromStart(this.mHeaderCount + positionForSection, 0);
            } else {
                this.mList.setSelection(this.mHeaderCount + positionForSection);
            }
        }
        if (this.mCurrentSection != i) {
            this.mCurrentSection = i;
            boolean transitionPreviewLayout = transitionPreviewLayout(i);
            if (!this.mShowingPreview && transitionPreviewLayout) {
                transitionToDragging();
            } else if (this.mShowingPreview && !transitionPreviewLayout) {
                transitionToVisible();
            }
        }
    }

    private void setState(int i) {
        this.mList.removeCallbacks(this.mDeferHide);
        if (this.mAlwaysShow && i == 0) {
            i = 1;
        }
        if (i != this.mState) {
            switch (i) {
                case 0:
                    transitionToHidden();
                    break;
                case 1:
                    transitionToVisible();
                    break;
                case 2:
                    if (!transitionPreviewLayout(this.mCurrentSection)) {
                        transitionToVisible();
                        break;
                    } else {
                        transitionToDragging();
                        break;
                    }
            }
            this.mState = i;
            refreshDrawablePressedState();
        }
    }

    private void setThumbPos(float f) {
        float f2;
        Rect rect = this.mContainerRect;
        int i = rect.left;
        int i2 = rect.right;
        View view = this.mTrackImage;
        View view2 = this.mThumbImage;
        float left = (float) view.getLeft();
        float f3 = left;
        float right = (f * (((float) view.getRight()) - left)) + left;
        view2.setTranslationX((((float) view2.getWidth()) / SprDocument.DEFAULT_DENSITY_SCALE) + right);
        View view3 = this.mPreviewImage;
        float width = ((float) view3.getWidth()) / SprDocument.DEFAULT_DENSITY_SCALE;
        switch (this.mOverlayPosition) {
            case 1:
                f2 = right;
                break;
            case 2:
                f2 = right - width;
                break;
            default:
                f2 = 0.0f;
                break;
        }
        float constrain = MathUtils.constrain(f2, ((float) i) + width, ((float) i2) - width) - width;
        view3.setTranslationX(constrain);
        this.mPrimaryText.setTranslationX(constrain);
        this.mSecondaryText.setTranslationX(constrain);
    }

    private void startPendingDrag() {
        this.mPendingDrag = SystemClock.uptimeMillis() + TAP_TIMEOUT;
    }

    private boolean transitionPreviewLayout(int i) {
        View view;
        View view2;
        Object[] objArr = this.mSections;
        CharSequence charSequence = null;
        if (objArr != null && i >= 0 && i < objArr.length) {
            Object obj = objArr[i];
            if (obj != null) {
                charSequence = obj.toString();
            }
        }
        Rect rect = this.mTempBounds;
        View view3 = this.mPreviewImage;
        if (this.mShowingPrimary) {
            view = this.mPrimaryText;
            view2 = this.mSecondaryText;
        } else {
            view = this.mSecondaryText;
            view2 = this.mPrimaryText;
        }
        view2.setText(charSequence);
        measurePreview(view2, rect);
        applyLayout(view2, rect);
        if (this.mPreviewAnimation != null) {
            this.mPreviewAnimation.cancel();
        }
        Animator duration = animateAlpha(view2, 1.0f).setDuration(50);
        Animator duration2 = animateAlpha(view, 0.0f).setDuration(50);
        duration2.addListener(this.mSwitchPrimaryListener);
        rect.left -= view3.getPaddingLeft();
        rect.top -= view3.getPaddingTop();
        rect.right += view3.getPaddingRight();
        rect.bottom += view3.getPaddingBottom();
        Animator animateBounds = animateBounds(view3, rect);
        animateBounds.setDuration(100);
        this.mPreviewAnimation = new AnimatorSet();
        Builder with = this.mPreviewAnimation.play(duration2).with(duration);
        with.with(animateBounds);
        int height = (view3.getHeight() - view3.getPaddingTop()) - view3.getPaddingBottom();
        int height2 = view2.getHeight();
        if (height2 > height) {
            view2.setScaleY(((float) height) / ((float) height2));
            with.with(animateScaleY(view2, 1.0f).setDuration(100));
        } else {
            view2.setScaleY(1.0f);
        }
        int height3 = view.getHeight();
        if (height3 > height2) {
            with.with(animateScaleY(view, ((float) height2) / ((float) height3)).setDuration(100));
        }
        this.mPreviewAnimation.start();
        return !TextUtils.isEmpty(charSequence);
    }

    private void transitionToDragging() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage).setDuration(150);
        Animator duration2 = groupAnimatorOfFloat(View.TRANSLATION_Y, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration, duration2});
        this.mDecorAnimation.start();
        this.mShowingPreview = true;
    }

    private void transitionToHidden() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator duration2 = groupAnimatorOfFloat(View.TRANSLATION_Y, (float) (this.mLayoutFromBottom ? this.mThumbImage.getHeight() : -this.mThumbImage.getHeight()), this.mThumbImage, this.mTrackImage).setDuration(300);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration, duration2});
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void transitionToVisible() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator duration = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        Animator duration2 = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator duration3 = groupAnimatorOfFloat(View.TRANSLATION_Y, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{duration, duration2, duration3});
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void updateAppearance() {
        Context context = this.mList.getContext();
        int i = 0;
        this.mTrackImage.setImageDrawable(this.mTrackDrawable);
        if (this.mTrackDrawable != null) {
            i = Math.max(0, this.mTrackDrawable.getIntrinsicHeight());
        }
        this.mThumbImage.setImageDrawable(this.mThumbDrawable);
        this.mThumbImage.setMinimumWidth(this.mThumbMinWidth);
        this.mThumbImage.setMinimumHeight(this.mThumbMinHeight);
        this.mThumbImage.setRotation(270.0f);
        if (this.mThumbDrawable != null) {
            i = Math.max(i, this.mThumbDrawable.getIntrinsicWidth());
        }
        this.mHeight = Math.max(i, this.mThumbMinHeight);
        this.mPreviewImage.setMinimumWidth(this.mPreviewMinWidth);
        this.mPreviewImage.setMinimumHeight(this.mPreviewMinHeight);
        if (this.mTextAppearance != 0) {
            this.mPrimaryText.setTextAppearance(context, this.mTextAppearance);
            this.mSecondaryText.setTextAppearance(context, this.mTextAppearance);
        }
        if (this.mTextColor != null) {
            this.mPrimaryText.setTextColor(this.mTextColor);
            this.mSecondaryText.setTextColor(this.mTextColor);
        }
        if (this.mTextSize > 0.0f) {
            this.mPrimaryText.setTextSize(0, this.mTextSize);
            this.mSecondaryText.setTextSize(0, this.mTextSize);
        }
        int max = Math.max(0, this.mPreviewMinWidth);
        this.mPrimaryText.setMinimumWidth(max);
        this.mPrimaryText.setMinimumHeight(max);
        this.mPrimaryText.setIncludeFontPadding(false);
        this.mSecondaryText.setMinimumWidth(max);
        this.mSecondaryText.setMinimumHeight(max);
        this.mSecondaryText.setIncludeFontPadding(false);
        refreshDrawablePressedState();
    }

    private void updateContainerRect() {
        SemHorizontalAbsListView semHorizontalAbsListView = this.mList;
        semHorizontalAbsListView.resolvePadding();
        Rect rect = this.mContainerRect;
        rect.left = 0;
        rect.top = 0;
        rect.right = semHorizontalAbsListView.getWidth();
        rect.bottom = semHorizontalAbsListView.getHeight();
        int i = this.mScrollBarStyle;
        if (i == FingerprintEvent.IMAGE_QUALITY_WET_FINGER || i == 0) {
            rect.left += semHorizontalAbsListView.getPaddingLeft();
            rect.top += semHorizontalAbsListView.getPaddingTop();
            rect.right -= semHorizontalAbsListView.getPaddingRight();
            rect.bottom -= semHorizontalAbsListView.getPaddingBottom();
            if (i == FingerprintEvent.IMAGE_QUALITY_WET_FINGER) {
                int height = getHeight();
                if (this.mScrollbarPosition == 2) {
                    rect.bottom += height;
                } else {
                    rect.top -= height;
                }
            }
        }
    }

    private void updateLongList(int i, int i2) {
        boolean z = i > 0 && i2 / i >= 4;
        if (this.mLongList != z) {
            this.mLongList = z;
            onStateDependencyChanged(false);
        }
    }

    public int getHeight() {
        return this.mHeight;
    }

    public boolean isAlwaysShowEnabled() {
        return this.mAlwaysShow;
    }

    public boolean isEnabled() {
        return this.mEnabled ? !this.mLongList ? this.mAlwaysShow : true : false;
    }

    public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if ((actionMasked == 9 || actionMasked == 7) && this.mState == 0 && isPointInside(motionEvent.getX(), motionEvent.getY())) {
            setState(1);
            postAutoHide();
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (isPointInside(motionEvent.getX(), motionEvent.getY())) {
                    if (this.mList.isInScrollingContainer()) {
                        this.mInitialTouchX = motionEvent.getX();
                        startPendingDrag();
                        break;
                    }
                    beginDrag();
                    return true;
                }
                break;
            case 1:
            case 3:
                cancelPendingDrag();
                break;
            case 2:
                if (!isPointInside(motionEvent.getX(), motionEvent.getY())) {
                    cancelPendingDrag();
                    break;
                } else if (this.mPendingDrag >= 0 && this.mPendingDrag <= SystemClock.uptimeMillis()) {
                    beginDrag();
                    scrollTo(getPosFromMotionEvent(this.mInitialTouchX));
                    return onTouchEvent(motionEvent);
                }
        }
        return false;
    }

    public void onItemCountChanged(int i, int i2) {
        Object obj = null;
        if (this.mOldItemCount != i2 || this.mOldChildCount != i) {
            this.mOldItemCount = i2;
            this.mOldChildCount = i;
            if (i2 - i > 0) {
                obj = 1;
            }
            if (!(obj == null || this.mState == 2)) {
                setThumbPos(getPosFromItemCount(this.mList.getFirstVisiblePosition(), i, i2));
            }
            updateLongList(i, i2);
        }
    }

    public void onScroll(int i, int i2, int i3) {
        int i4 = 0;
        if (isEnabled()) {
            if (i3 - i2 > 0) {
                i4 = 1;
            }
            if (!(i4 == 0 || this.mState == 2)) {
                setThumbPos(getPosFromItemCount(i, i2, i3));
            }
            this.mScrollCompleted = true;
            if (this.mFirstVisibleItem != i) {
                this.mFirstVisibleItem = i;
                if (this.mState != 2) {
                    setState(1);
                    postAutoHide();
                }
            }
            return;
        }
        setState(0);
    }

    public void onSectionsChanged() {
        this.mListAdapter = null;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        updateLayout();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        float posFromMotionEvent;
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (isPointInside(motionEvent.getX(), motionEvent.getY())) {
                    if (this.mList.isInScrollingContainer()) {
                        this.mInitialTouchX = motionEvent.getX();
                        startPendingDrag();
                        break;
                    }
                    beginDrag();
                    return true;
                }
                break;
            case 1:
                if (this.mPendingDrag >= 0) {
                    beginDrag();
                    posFromMotionEvent = getPosFromMotionEvent(motionEvent.getX());
                    setThumbPos(posFromMotionEvent);
                    scrollTo(posFromMotionEvent);
                }
                if (this.mState == 2) {
                    this.mList.requestDisallowInterceptTouchEvent(false);
                    this.mList.reportScrollStateChange(0);
                    setState(1);
                    postAutoHide();
                    return true;
                }
                break;
            case 2:
                if (this.mPendingDrag >= 0 && Math.abs(motionEvent.getX() - this.mInitialTouchX) > ((float) this.mScaledTouchSlop)) {
                    beginDrag();
                }
                if (this.mState == 2) {
                    posFromMotionEvent = getPosFromMotionEvent(motionEvent.getX());
                    setThumbPos(posFromMotionEvent);
                    if (this.mScrollCompleted) {
                        scrollTo(posFromMotionEvent);
                    }
                    return true;
                }
                break;
            case 3:
                cancelPendingDrag();
                break;
        }
        return false;
    }

    public void remove() {
        this.mOverlay.remove(this.mTrackImage);
        this.mOverlay.remove(this.mThumbImage);
        this.mOverlay.remove(this.mPreviewImage);
        this.mOverlay.remove(this.mPrimaryText);
        this.mOverlay.remove(this.mSecondaryText);
    }

    public void setAlwaysShow(boolean z) {
        if (this.mAlwaysShow != z) {
            this.mAlwaysShow = z;
            onStateDependencyChanged(false);
        }
    }

    public void setEnabled(boolean z) {
        if (this.mEnabled != z) {
            this.mEnabled = z;
            onStateDependencyChanged(true);
        }
    }

    public void setScrollBarStyle(int i) {
        if (this.mScrollBarStyle != i) {
            this.mScrollBarStyle = i;
            updateLayout();
        }
    }

    public void setScrollbarPosition(int i) {
        int i2 = 1;
        if (i == 0) {
            i = this.mList.isLayoutRtl() ? 1 : 2;
        }
        if (this.mScrollbarPosition != i) {
            this.mScrollbarPosition = i;
            this.mLayoutFromBottom = i != 1;
            int[] iArr = this.mPreviewResId;
            if (!this.mLayoutFromBottom) {
                i2 = 0;
            }
            this.mPreviewImage.setBackgroundResource(iArr[i2]);
            Drawable background = this.mPreviewImage.getBackground();
            if (background != null) {
                Rect rect = this.mTempBounds;
                background.getPadding(rect);
                rect.offset(this.mPreviewPadding, this.mPreviewPadding);
                this.mPreviewImage.setPadding(rect.left, rect.top, rect.right, rect.bottom);
            }
            updateLayout();
        }
    }

    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mList.getContext().obtainStyledAttributes(null, R.styleable.FastScroll, 16843767, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            switch (index) {
                case 0:
                    this.mTextAppearance = obtainStyledAttributes.getResourceId(index, 0);
                    break;
                case 1:
                    this.mTextSize = (float) obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 2:
                    this.mTextColor = obtainStyledAttributes.getColorStateList(index);
                    break;
                case 3:
                    this.mPreviewPadding = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 4:
                    this.mPreviewMinWidth = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 5:
                    this.mPreviewMinHeight = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 7:
                    this.mThumbDrawable = obtainStyledAttributes.getDrawable(index);
                    break;
                case 8:
                    this.mThumbMinWidth = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 9:
                    this.mThumbMinHeight = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 10:
                    this.mTrackDrawable = obtainStyledAttributes.getDrawable(index);
                    break;
                case 11:
                    this.mPreviewResId[1] = obtainStyledAttributes.getResourceId(index, 0);
                    break;
                case 12:
                    this.mPreviewResId[0] = obtainStyledAttributes.getResourceId(index, 0);
                    break;
                case 13:
                    this.mOverlayPosition = obtainStyledAttributes.getInt(index, 0);
                    break;
                default:
                    break;
            }
        }
        obtainStyledAttributes.recycle();
        updateAppearance();
    }

    public void stop() {
        setState(0);
    }

    public void updateLayout() {
        if (!this.mUpdatingLayout) {
            this.mUpdatingLayout = true;
            updateContainerRect();
            layoutThumb();
            layoutTrack();
            Rect rect = this.mTempBounds;
            measurePreview(this.mPrimaryText, rect);
            applyLayout(this.mPrimaryText, rect);
            measurePreview(this.mSecondaryText, rect);
            applyLayout(this.mSecondaryText, rect);
            rect.left -= this.mPreviewImage.getPaddingLeft();
            rect.top -= this.mPreviewImage.getPaddingTop();
            rect.right += this.mPreviewImage.getPaddingRight();
            rect.bottom += this.mPreviewImage.getPaddingBottom();
            applyLayout(this.mPreviewImage, rect);
            this.mUpdatingLayout = false;
        }
    }
}
