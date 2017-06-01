package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.view.View;

public class SmartClipCroppedAreaImpl implements SemSmartClipCroppedArea {
    private Rect mRect = null;

    public SmartClipCroppedAreaImpl(Rect rect) {
        this.mRect = rect;
    }

    public Rect getRect() {
        return new Rect(this.mRect);
    }

    public boolean intersects(Rect rect) {
        return (rect == null || this.mRect == null) ? false : Rect.intersects(getRect(), rect);
    }

    public boolean intersects(View view) {
        return (view == null || this.mRect == null) ? false : intersects(SmartClipUtils.getViewBoundsOnScreen(view));
    }
}
