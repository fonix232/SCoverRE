package com.samsung.android.desktopmode;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

public class DexCompatTranslator {
    private DexCompatTranslator mParentTranslator = null;
    private Point mWindowPosition = new Point();

    private DexCompatTranslator(DexCompatTranslator dexCompatTranslator) {
        this.mParentTranslator = dexCompatTranslator;
    }

    public static DexCompatTranslator getDexCompatTranslator(DexCompatTranslator dexCompatTranslator) {
        return new DexCompatTranslator(dexCompatTranslator);
    }

    private Point getWindowPosition() {
        return this.mWindowPosition;
    }

    public boolean apply(int i, int i2) {
        if (this.mWindowPosition.x == i && this.mWindowPosition.y == i2) {
            return false;
        }
        this.mWindowPosition.x = i;
        this.mWindowPosition.y = i2;
        return true;
    }

    public void translateToScreen(Point point) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            point.offset(windowPosition.x, windowPosition.y);
            return;
        }
        point.offset(this.mWindowPosition.x, this.mWindowPosition.y);
    }

    public void translateToScreen(PointF pointF) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            pointF.offset((float) windowPosition.x, (float) windowPosition.y);
            return;
        }
        pointF.offset((float) this.mWindowPosition.x, (float) this.mWindowPosition.y);
    }

    public void translateToScreen(Rect rect) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            rect.offset(windowPosition.x, windowPosition.y);
            return;
        }
        rect.offset(this.mWindowPosition.x, this.mWindowPosition.y);
    }

    public void translateToScreen(MotionEvent motionEvent) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            motionEvent.setWindowOffset(windowPosition.x, windowPosition.y);
            return;
        }
        motionEvent.setWindowOffset(this.mWindowPosition.x, this.mWindowPosition.y);
    }

    public void translateToWindow(Point point) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            point.offset(-windowPosition.x, -windowPosition.y);
            return;
        }
        point.offset(-this.mWindowPosition.x, -this.mWindowPosition.y);
    }

    public void translateToWindow(PointF pointF) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            pointF.offset((float) (-windowPosition.x), (float) (-windowPosition.y));
            return;
        }
        pointF.offset((float) (-this.mWindowPosition.x), (float) (-this.mWindowPosition.y));
    }

    public void translateToWindow(Rect rect) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            rect.offset(-windowPosition.x, -windowPosition.y);
            return;
        }
        rect.offset(-this.mWindowPosition.x, -this.mWindowPosition.y);
    }

    public void translateToWindow(MotionEvent motionEvent) {
        if (this.mParentTranslator != null) {
            Point windowPosition = this.mParentTranslator.getWindowPosition();
            motionEvent.setWindowOffset(-windowPosition.x, -windowPosition.y);
            return;
        }
        motionEvent.setWindowOffset(-this.mWindowPosition.x, -this.mWindowPosition.y);
    }
}
