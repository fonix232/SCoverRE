package com.samsung.android.animation;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

public class SemAnimatorUtils {
    public static final TypeEvaluator<Rect> BOUNDS_EVALUATOR = new C09571();
    private static final boolean DEBUGGABLE_LOW = Debug.semIsProductDev();

    static class C09571 implements TypeEvaluator<Rect> {
        C09571() {
        }

        public Rect evaluate(float f, Rect rect, Rect rect2) {
            return new Rect(interpolate(rect.left, rect2.left, f), interpolate(rect.top, rect2.top, f), interpolate(rect.right, rect2.right, f), interpolate(rect.bottom, rect2.bottom, f));
        }

        public int interpolate(int i, int i2, float f) {
            return (int) (((float) i) + (((float) (i2 - i)) * f));
        }
    }

    public static BitmapDrawable getBitmapDrawableFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getResources().getDisplayMetrics(), view.getWidth(), view.getHeight(), Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return new BitmapDrawable(view.getResources(), createBitmap);
    }

    static int getViewCenterX(View view) {
        return (view.getLeft() + view.getRight()) / 2;
    }

    static int getViewCenterY(View view) {
        return (view.getTop() + view.getBottom()) / 2;
    }

    public static boolean isTalkBackEnabled(Context context) {
        boolean z = false;
        AccessibilityManager instance = AccessibilityManager.getInstance(context);
        if (instance != null && instance.isEnabled() && instance.isTouchExplorationEnabled()) {
            z = true;
        }
        if (DEBUGGABLE_LOW) {
            Log.m29d("SemAnimatorUtils", "isTalkBackEnabled=" + z);
        }
        return z;
    }
}
