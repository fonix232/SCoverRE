package com.samsung.android.content.smartclip;

import android.graphics.Rect;
import android.view.View;

public interface SemSmartClipCroppedArea {
    Rect getRect();

    boolean intersects(Rect rect);

    boolean intersects(View view);
}
