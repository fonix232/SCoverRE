package com.samsung.android.multiwindow;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.framework.res.C0078R;

public class MultiWindowGuideWindow extends FrameLayout {
    private static final int CTRL_BOTTOM = 8;
    private static final int CTRL_LEFT = 1;
    private static final int CTRL_NONE = 0;
    private static final int CTRL_RIGHT = 2;
    private static final int CTRL_TOP = 4;
    private static final boolean DEBUG = true;
    public static final int STATE_DOCKING = 3;
    public static final int STATE_NONE = -1;
    public static final int STATE_NOT_SUPPORT = 2;
    public static final int STATE_RESIZE = 0;
    public static final int STATE_WARNING = 1;
    public static final int STROKE_RESIZE = 4;
    public static final int STROKE_WARNING = 5;
    private static final String TAG = "MultiWindowGuideWindow";
    private boolean mAttached = false;
    private Rect mBounds = new Rect();
    private int[] mColor = new int[10];
    private ImageView mColorView = null;
    private boolean mIsScreenCornerR = false;
    private ImageView mNotSupportView = null;
    private int mState = -1;
    private int mStrokeWidth;
    private WindowManager mWindowManager = ((WindowManager) getContext().getSystemService("window"));

    public MultiWindowGuideWindow(Context context) {
        super(context);
        this.mColor[0] = 1077657579;
        this.mColor[1] = 1090487685;
        this.mColor[4] = -868499477;
        this.mColor[5] = -855669371;
        this.mStrokeWidth = getContext().getResources().getDimensionPixelSize(C0078R.dimen.multiwindow_decor_frame_thickness) * 2;
        this.mIsScreenCornerR = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_CORNER_R");
    }

    private LayoutParams generateLayoutParam() {
        ViewGroup.LayoutParams layoutParams = new LayoutParams();
        layoutParams.setTitle("GuideWindow");
        layoutParams.gravity = 8388659;
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.format = -2;
        layoutParams.type = 2301;
        layoutParams.flags = 792;
        layoutParams.privateFlags |= 16;
        return layoutParams;
    }

    private void refreshUI() {
        if (this.mAttached && this.mColorView != null && this.mNotSupportView != null) {
            GradientDrawable gradientDrawable = (GradientDrawable) this.mColorView.getDrawable();
            this.mColorView.setVisibility(4);
            this.mNotSupportView.setVisibility(4);
            switch (this.mState) {
                case 0:
                case 3:
                    gradientDrawable.setColor(this.mColor[0]);
                    gradientDrawable.setStroke(this.mStrokeWidth, this.mColor[4]);
                    this.mColorView.setVisibility(0);
                    break;
                case 1:
                    gradientDrawable.setColor(this.mColor[1]);
                    gradientDrawable.setStroke(this.mStrokeWidth, this.mColor[5]);
                    this.mColorView.setVisibility(0);
                    break;
                case 2:
                    this.mNotSupportView.setVisibility(0);
                    break;
            }
        }
    }

    public void dismiss() {
        synchronized (this) {
            if (this.mAttached) {
                this.mWindowManager.removeViewImmediate(this);
                removeAllViews();
                this.mColorView = null;
                this.mNotSupportView = null;
                this.mAttached = false;
                this.mState = -1;
            }
        }
    }

    public int getGuideState() {
        return this.mState;
    }

    public void initialize() {
        if (!this.mAttached) {
            setLayoutDirection(0);
            if (this.mColorView == null) {
                this.mColorView = new ImageView(this.mContext);
                this.mColorView.setImageDrawable(this.mContext.getDrawable(C0078R.drawable.samsung_multiwindow_guideview));
                this.mColorView.setVisibility(4);
                this.mColorView.setScaleType(ScaleType.FIT_XY);
            }
            if (this.mNotSupportView == null) {
                this.mNotSupportView = new ImageView(this.mContext);
                if (this.mIsScreenCornerR) {
                    this.mNotSupportView.setImageDrawable(this.mContext.getDrawable(C0078R.drawable.samsung_multiwindow_guideview_not_support_dream));
                } else {
                    this.mNotSupportView.setImageDrawable(this.mContext.getDrawable(C0078R.drawable.samsung_multiwindow_guideview_not_support));
                }
                this.mNotSupportView.setVisibility(4);
                this.mNotSupportView.setScaleType(ScaleType.FIT_XY);
            }
            addView(this.mColorView);
            addView(this.mNotSupportView);
            this.mWindowManager.addView(this, generateLayoutParam());
            this.mAttached = true;
        }
    }

    public boolean isAttached() {
        return this.mAttached;
    }

    public void setGuideState(int i) {
        if (this.mState != i) {
            this.mState = i;
            refreshUI();
        }
    }

    public void setGuideWindowShapeWhenDocking(int i) {
        if (this.mIsScreenCornerR && this.mState == 3 && i != 0 && this.mColorView != null) {
            int i2 = this.mStrokeWidth;
            int i3 = i2;
            int i4 = i2;
            int i5 = i2;
            this.mColorView.setImageDrawable(this.mContext.getDrawable(C0078R.drawable.samsung_multiwindow_guideview_when_docking_dream));
            switch (i) {
                case 1:
                    i4 = 0;
                    break;
                case 2:
                    i5 = 0;
                    break;
                case 4:
                    i2 = 0;
                    break;
                case 8:
                    i3 = 0;
                    break;
            }
            GradientDrawable gradientDrawable = (GradientDrawable) this.mColorView.getDrawable();
            gradientDrawable.setColor(this.mColor[0]);
            gradientDrawable.setStroke(this.mStrokeWidth, this.mColor[4]);
            Drawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable});
            layerDrawable.setLayerInset(0, -i5, -i3, -i4, -i2);
            this.mColorView.setBackground(layerDrawable);
        }
    }

    public void show(Rect rect) {
        show(rect, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void show(android.graphics.Rect r6, boolean r7) {
        /*
        r5 = this;
        r4 = 2;
        monitor-enter(r5);
        r1 = r5.mAttached;	 Catch:{ all -> 0x0070 }
        if (r1 == 0) goto L_0x000a;
    L_0x0006:
        r1 = r5.mColorView;	 Catch:{ all -> 0x0070 }
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r5);
        return;
    L_0x000c:
        r1 = r5.mNotSupportView;	 Catch:{ all -> 0x0070 }
        if (r1 == 0) goto L_0x000a;
    L_0x0010:
        if (r6 == 0) goto L_0x000a;
    L_0x0012:
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r1.set(r6);	 Catch:{ all -> 0x0070 }
        if (r7 == 0) goto L_0x0024;
    L_0x0019:
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r2 = r5.mStrokeWidth;	 Catch:{ all -> 0x0070 }
        r2 = -r2;
        r3 = r5.mStrokeWidth;	 Catch:{ all -> 0x0070 }
        r3 = -r3;
        r1.inset(r2, r3);	 Catch:{ all -> 0x0070 }
    L_0x0024:
        r1 = r5.mState;	 Catch:{ all -> 0x0070 }
        if (r1 != r4) goto L_0x005c;
    L_0x0028:
        r1 = r5.mNotSupportView;	 Catch:{ all -> 0x0070 }
        r0 = r1.getLayoutParams();	 Catch:{ all -> 0x0070 }
        r0 = (android.widget.FrameLayout.LayoutParams) r0;	 Catch:{ all -> 0x0070 }
    L_0x0030:
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r1 = r1.left;	 Catch:{ all -> 0x0070 }
        r0.leftMargin = r1;	 Catch:{ all -> 0x0070 }
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r1 = r1.top;	 Catch:{ all -> 0x0070 }
        r0.topMargin = r1;	 Catch:{ all -> 0x0070 }
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r1 = r1.width();	 Catch:{ all -> 0x0070 }
        r0.width = r1;	 Catch:{ all -> 0x0070 }
        r1 = r5.mBounds;	 Catch:{ all -> 0x0070 }
        r1 = r1.height();	 Catch:{ all -> 0x0070 }
        r0.height = r1;	 Catch:{ all -> 0x0070 }
        r1 = r5.mState;	 Catch:{ all -> 0x0070 }
        if (r1 != r4) goto L_0x0065;
    L_0x0050:
        r1 = r5.mNotSupportView;	 Catch:{ all -> 0x0070 }
        r1.requestLayout();	 Catch:{ all -> 0x0070 }
        r1 = r5.mNotSupportView;	 Catch:{ all -> 0x0070 }
        r1.invalidate();	 Catch:{ all -> 0x0070 }
    L_0x005a:
        monitor-exit(r5);
        return;
    L_0x005c:
        r1 = r5.mColorView;	 Catch:{ all -> 0x0070 }
        r0 = r1.getLayoutParams();	 Catch:{ all -> 0x0070 }
        r0 = (android.widget.FrameLayout.LayoutParams) r0;	 Catch:{ all -> 0x0070 }
        goto L_0x0030;
    L_0x0065:
        r1 = r5.mColorView;	 Catch:{ all -> 0x0070 }
        r1.requestLayout();	 Catch:{ all -> 0x0070 }
        r1 = r5.mColorView;	 Catch:{ all -> 0x0070 }
        r1.invalidate();	 Catch:{ all -> 0x0070 }
        goto L_0x005a;
    L_0x0070:
        r1 = move-exception;
        monitor-exit(r5);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.multiwindow.MultiWindowGuideWindow.show(android.graphics.Rect, boolean):void");
    }
}
