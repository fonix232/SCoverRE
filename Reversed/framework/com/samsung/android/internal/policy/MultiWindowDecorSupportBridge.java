package com.samsung.android.internal.policy;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ThreadedRenderer;
import com.android.internal.policy.DecorView;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.widget.DecorCaptionView;

public class MultiWindowDecorSupportBridge {
    private static final String CLASS_NAME = "com.android.internal.policy.MultiWindowDecorSupport";
    private static final String TAG = "MultiWindowDecorSupportBridge";
    private IMultiWindowDecorSupportBridge IBridge;

    public interface IMultiWindowDecorSupportBridge {
        void addDecorCaptionWindow();

        boolean checkReadyToResizeFreeform(MotionEvent motionEvent);

        void createResizingFrameRenderNode(ThreadedRenderer threadedRenderer);

        void dispatchHoverEvent(MotionEvent motionEvent);

        void dispatchRequestedOrientation(int i);

        void dispatchWindowSystemUiVisiblityChanged(int i);

        void drawFreeformFrameIfNeeded(Canvas canvas);

        void drawResizingFrameRenderNode(ThreadedRenderer threadedRenderer, int i, int i2, int i3, int i4, Rect rect);

        int getStackId();

        boolean hasDecorCaptionView();

        boolean hasDecorCaptionWindow();

        boolean hasEdgeFlag();

        void init(DecorView decorView, PhoneWindow phoneWindow);

        boolean isDesktopMode();

        boolean isFullScreenFreeform();

        boolean isFullWindow();

        boolean isImmersiveMode(int i);

        boolean isInMultiWindowMode(boolean z);

        boolean isResizing();

        boolean isUseFreeformBorder();

        void notifyMovingTask(boolean z);

        void onConfigurationChanged(Configuration configuration, int i, int i2, boolean z);

        void onLayout(boolean z, int i, int i2, int i3, int i4);

        void onMultiWindowModeChanged(boolean z);

        void onWindowFocusChanged(boolean z);

        void removeDecorCaptionWindow();

        void removeResizingFrameRenderNode(ThreadedRenderer threadedRenderer);

        void requestInvalidateRenderNode(String str);

        void setBlockDrawingFreeformFrame(boolean z);

        void setDarkDecorCaptionShade(DecorCaptionView decorCaptionView);

        void setLightDecorCaptionShade(DecorCaptionView decorCaptionView);

        void setResizeMode(int i);
    }

    public MultiWindowDecorSupportBridge() {
        try {
            this.IBridge = (IMultiWindowDecorSupportBridge) Class.forName(CLASS_NAME).newInstance();
        } catch (Exception e) {
            Log.w(TAG, "Not created this class : com.android.internal.policy.MultiWindowDecorSupport");
        }
    }

    public void addDecorCaptionWindow() {
        if (this.IBridge != null) {
            this.IBridge.addDecorCaptionWindow();
        }
    }

    public boolean checkReadyToResizeFreeform(MotionEvent motionEvent) {
        return this.IBridge != null ? this.IBridge.checkReadyToResizeFreeform(motionEvent) : false;
    }

    public void createResizingFrameRenderNode(ThreadedRenderer threadedRenderer) {
        if (this.IBridge != null) {
            this.IBridge.createResizingFrameRenderNode(threadedRenderer);
        }
    }

    public void dispatchHoverEvent(MotionEvent motionEvent) {
        if (this.IBridge != null) {
            this.IBridge.dispatchHoverEvent(motionEvent);
        }
    }

    public void dispatchRequestedOrientation(int i) {
        if (this.IBridge != null) {
            this.IBridge.dispatchRequestedOrientation(i);
        }
    }

    public void dispatchWindowSystemUiVisiblityChanged(int i) {
        if (this.IBridge != null) {
            this.IBridge.dispatchWindowSystemUiVisiblityChanged(i);
        }
    }

    public void drawFreeformFrameIfNeeded(Canvas canvas) {
        if (this.IBridge != null) {
            this.IBridge.drawFreeformFrameIfNeeded(canvas);
        }
    }

    public void drawResizingFrameRenderNode(ThreadedRenderer threadedRenderer, int i, int i2, int i3, int i4, Rect rect) {
        if (this.IBridge != null) {
            this.IBridge.drawResizingFrameRenderNode(threadedRenderer, i, i2, i3, i4, rect);
        }
    }

    public int getStackId() {
        return this.IBridge != null ? this.IBridge.getStackId() : -1;
    }

    public boolean hasDecorCaptionView() {
        return this.IBridge != null ? this.IBridge.hasDecorCaptionView() : false;
    }

    public boolean hasDecorCaptionWindow() {
        return this.IBridge != null ? this.IBridge.hasDecorCaptionWindow() : false;
    }

    public boolean hasEdgeFlag() {
        return this.IBridge != null ? this.IBridge.hasEdgeFlag() : false;
    }

    public void init(DecorView decorView, PhoneWindow phoneWindow) {
        if (this.IBridge != null) {
            this.IBridge.init(decorView, phoneWindow);
        }
    }

    public boolean isDesktopMode() {
        return this.IBridge != null ? this.IBridge.isDesktopMode() : false;
    }

    public boolean isFullScreenFreeform() {
        return this.IBridge != null ? this.IBridge.isFullScreenFreeform() : false;
    }

    public boolean isFullWindow() {
        return this.IBridge != null ? this.IBridge.isFullWindow() : false;
    }

    public boolean isImmersiveMode(int i) {
        return this.IBridge != null ? this.IBridge.isImmersiveMode(i) : false;
    }

    public boolean isInMultiWindowMode(boolean z) {
        return this.IBridge != null ? this.IBridge.isInMultiWindowMode(z) : false;
    }

    public boolean isResizing() {
        return this.IBridge != null ? this.IBridge.isResizing() : false;
    }

    public boolean isUseFreeformBorder() {
        return this.IBridge != null ? this.IBridge.isUseFreeformBorder() : false;
    }

    public void notifyMovingTask(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.notifyMovingTask(z);
        }
    }

    public void onConfigurationChanged(Configuration configuration, int i, int i2, boolean z) {
        if (this.IBridge != null) {
            this.IBridge.onConfigurationChanged(configuration, i, i2, z);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.IBridge != null) {
            this.IBridge.onLayout(z, i, i2, i3, i4);
        }
    }

    public void onMultiWindowModeChanged(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.onMultiWindowModeChanged(z);
        }
    }

    public void onWindowFocusChanged(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.onWindowFocusChanged(z);
        }
    }

    public void removeDecorCaptionWindow() {
        if (this.IBridge != null) {
            this.IBridge.removeDecorCaptionWindow();
        }
    }

    public void removeResizingFrameRenderNode(ThreadedRenderer threadedRenderer) {
        if (this.IBridge != null) {
            this.IBridge.removeResizingFrameRenderNode(threadedRenderer);
        }
    }

    public void requestInvalidateRenderNode(String str) {
        if (this.IBridge != null) {
            this.IBridge.requestInvalidateRenderNode(str);
        }
    }

    public void setBlockDrawingFreeformFrame(boolean z) {
        if (this.IBridge != null) {
            this.IBridge.setBlockDrawingFreeformFrame(z);
        }
    }

    public void setDarkDecorCaptionShade(DecorCaptionView decorCaptionView) {
        if (this.IBridge != null) {
            this.IBridge.setDarkDecorCaptionShade(decorCaptionView);
        }
    }

    public void setLightDecorCaptionShade(DecorCaptionView decorCaptionView) {
        if (this.IBridge != null) {
            this.IBridge.setLightDecorCaptionShade(decorCaptionView);
        }
    }

    public void setResizeMode(int i) {
        if (this.IBridge != null) {
            this.IBridge.setResizeMode(i);
        }
    }
}
