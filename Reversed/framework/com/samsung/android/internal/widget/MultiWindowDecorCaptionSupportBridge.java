package com.samsung.android.internal.widget;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.widget.DecorCaptionView;

public class MultiWindowDecorCaptionSupportBridge {
    private static final String CLASS_NAME = "com.android.internal.widget.MultiWindowDecorCaptionSupport";
    public static final int CLICK_TARGET_INSET_IN_DIP = 3;
    public static final int DRAG_SLOP_IN_DIP = 8;
    public static final int GONE_CAPTION = 0;
    public static final int SHOW_CAPTION = 1;
    public static final int SHOW_CAPTION_OVERLAY = 2;
    private static final String TAG = "MultiWindowDecorCaptionSupportBridge";
    private IMultiWindowDecorCaptionSupportBridge IBridge;

    public interface IMultiWindowDecorCaptionSupportBridge {
        void clearMaximizeRequested();

        void dispatchRequestedOrientation(int i);

        boolean getLastMeasuredWithCaptionHeight();

        ViewOutlineProvider getMultiWindowOutlineProvider();

        boolean hasClickTarget();

        void init(PhoneWindow phoneWindow, DecorCaptionView decorCaptionView, GestureDetector gestureDetector);

        boolean isDesktopMode();

        boolean isFullScreenFreeform();

        void notifyMovingTask(boolean z);

        boolean onInterceptTouchEvent(MotionEvent motionEvent);

        void onLayout(boolean z, int i, int i2, int i3, int i4);

        void onLongPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);

        boolean onTouch(View view, MotionEvent motionEvent);

        boolean onTouchEvent(MotionEvent motionEvent);

        void removeContentView();

        void setCaptionBackground();

        void setCaptionView(View view);

        void setContentView(View view);

        void setLastMeasuredWithCaptionHeight(boolean z);

        void toggleMaximizeButton();

        void updateButtonFocus(boolean z);

        void updateButtonVisibilityByFeature();

        void updateFullScreenFreeformWindowCaptionVisibility(int i, boolean z);
    }

    public MultiWindowDecorCaptionSupportBridge() {
        try {
            this.IBridge = (IMultiWindowDecorCaptionSupportBridge) Class.forName(CLASS_NAME).newInstance();
        } catch (Exception e) {
            Log.w(TAG, "Not created this class : com.android.internal.widget.MultiWindowDecorCaptionSupport");
        }
    }

    public void clearMaximizeRequested() {
        if (this.IBridge != null) {
            this.IBridge.clearMaximizeRequested();
        }
    }

    public void dispatchRequestedOrientation(int i) {
        if (this.IBridge != null) {
            this.IBridge.dispatchRequestedOrientation(i);
        }
    }

    public boolean getLastMeasuredWithCaptionHeight() {
        return this.IBridge != null ? this.IBridge.getLastMeasuredWithCaptionHeight() : false;
    }

    public ViewOutlineProvider getMultiWindowOutlineProvider() {
        return this.IBridge != null ? this.IBridge.getMultiWindowOutlineProvider() : null;
    }

    public boolean hasClickTarget() {
        return this.IBridge != null ? this.IBridge.hasClickTarget() : false;
    }

    public void init(PhoneWindow phoneWindow, DecorCaptionView decorCaptionView, GestureDetector gestureDetector) {
        if (this.IBridge != null) {
            this.IBridge.init(phoneWindow, decorCaptionView, gestureDetector);
        }
    }

    public boolean isDesktopMode() {
        return this.IBridge != null ? this.IBridge.isDesktopMode() : false;
    }

    public boolean isFullScreenFreeform() {
        return this.IBridge != null ? this.IBridge.isFullScreenFreeform() : false;
    }

    public void notifyMovingTask(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.notifyMovingTask(z);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.IBridge != null ? this.IBridge.onInterceptTouchEvent(motionEvent) : false;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.IBridge != null) {
            this.IBridge.onLayout(z, i, i2, i3, i4);
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.IBridge != null) {
            this.IBridge.onLongPress(motionEvent);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return this.IBridge != null ? this.IBridge.onSingleTapUp(motionEvent) : false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.IBridge != null ? this.IBridge.onTouch(view, motionEvent) : false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.IBridge != null ? this.IBridge.onTouchEvent(motionEvent) : false;
    }

    public void removeContentView() {
        if (this.IBridge != null) {
            this.IBridge.removeContentView();
        }
    }

    public void setCaptionBackground() {
        if (this.IBridge != null) {
            this.IBridge.setCaptionBackground();
        }
    }

    public void setCaptionView(View view) {
        if (this.IBridge != null) {
            this.IBridge.setCaptionView(view);
        }
    }

    public void setContentView(View view) {
        if (this.IBridge != null) {
            this.IBridge.setContentView(view);
        }
    }

    public void setLastMeasuredWithCaptionHeight(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.setLastMeasuredWithCaptionHeight(z);
        }
    }

    public void toggleMaximizeButton() {
        if (this.IBridge != null) {
            this.IBridge.toggleMaximizeButton();
        }
    }

    public void updateButtonFocus(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.updateButtonFocus(z);
        }
    }

    public void updateButtonVisibilityByFeature() {
        if (this.IBridge != null) {
            this.IBridge.updateButtonVisibilityByFeature();
        }
    }

    public void updateFullScreenFreeformWindowCaptionVisibility(int i, boolean z) {
        if (this.IBridge != null) {
            this.IBridge.updateFullScreenFreeformWindowCaptionVisibility(i, z);
        }
    }
}
