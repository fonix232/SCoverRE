package com.samsung.android.sdk.look.cocktailbar;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import com.samsung.android.sdk.look.Slook;

public final class SlookCocktailSubWindow {
    private static Slook mSlook = new Slook();

    public static void setSubContentView(Activity activity, int layoutResID) {
        if (!mSlook.isFeatureEnabled(6)) {
            return;
        }
        if (activity == null) {
            throw new IllegalArgumentException("activity is null.");
        } else if (activity.getSubWindow() == null) {
            throw new IllegalArgumentException("activity is invalid.");
        } else {
            activity.setSubContentView(layoutResID);
        }
    }

    public static void setSubContentView(Activity activity, View view) {
        if (!mSlook.isFeatureEnabled(6)) {
            return;
        }
        if (activity == null) {
            throw new IllegalArgumentException("activity is null.");
        } else if (activity.getSubWindow() == null) {
            throw new IllegalArgumentException("activity is invalid.");
        } else {
            activity.setSubContentView(view);
        }
    }

    public static Window getSubWindow(Activity activity) {
        if (!mSlook.isFeatureEnabled(6)) {
            return null;
        }
        if (activity == null) {
            throw new IllegalArgumentException("activity is null.");
        }
        Window subWindow = activity.getSubWindow();
        if (subWindow != null) {
            return subWindow;
        }
        throw new IllegalArgumentException("activity is invalid.");
    }

    public static void setTransientCocktailBar(Activity activity, boolean disable) {
        if (!mSlook.isFeatureEnabled(6)) {
            return;
        }
        if (activity == null) {
            throw new IllegalArgumentException("activity is null.");
        }
        Window window = activity.getWindow();
        if (window != null) {
            window.setTransientCocktailBar(disable);
        }
    }
}
