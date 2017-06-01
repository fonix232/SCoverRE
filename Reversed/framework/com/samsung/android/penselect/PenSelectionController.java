package com.samsung.android.penselect;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class PenSelectionController {
    private static final String TAG = "PenSelectController";
    private static PenSelectionController sInstance;

    static class PenSelectionContents {
        public String mContentStr;

        PenSelectionContents() {
        }
    }

    private PenSelectionController() {
    }

    public static PenSelectionController getInstance() {
        if (sInstance == null) {
            sInstance = new PenSelectionController();
        }
        return sInstance;
    }

    private boolean getPenSelectionContents(Context context, View view, PenSelectionContents penSelectionContents) {
        boolean z = false;
        if (!isVisibleView(view)) {
            return false;
        }
        if (view instanceof TextView) {
            View view2 = view;
            if (!view2.hasMultiSelection()) {
                return false;
            }
            CharSequence multiSelectionText = view2.getMultiSelectionText();
            if (TextUtils.isEmpty(multiSelectionText)) {
                return false;
            }
            if (TextUtils.isEmpty(penSelectionContents.mContentStr)) {
                penSelectionContents.mContentStr = multiSelectionText.toString();
            } else {
                penSelectionContents.mContentStr += "\n" + multiSelectionText.toString();
            }
            return true;
        } else if (!(view instanceof ViewGroup)) {
            return false;
        } else {
            View view3 = view;
            int childCount = view3.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getPenSelectionContents(context, view3.getChildAt(i), penSelectionContents)) {
                    z = true;
                }
            }
            return z;
        }
    }

    private boolean isVisibleView(View view) {
        return view != null && view.getVisibility() == 0 && view.getWidth() > 0 && view.getHeight() > 0;
    }

    public boolean checkRectInView(View view, Rect rect) {
        if (view.getVisibility() != 0) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight()).contains(rect);
    }

    public boolean clearAllPenSelection(Context context, View view) {
        View view2 = view;
        if (view instanceof TextView) {
            View view3 = view;
            if (view3.hasMultiSelection()) {
                view3.clearMultiSelection();
            }
        } else if (view instanceof ViewGroup) {
            View view4 = view;
            int childCount = view4.getChildCount();
            for (int i = 0; i < childCount; i++) {
                clearAllPenSelection(context, view4.getChildAt(i));
            }
        }
        return true;
    }

    public View findTargetTextView(Context context, View view, Rect rect) {
        View view2 = view;
        if (!checkRectInView(view, rect)) {
            return null;
        }
        if (!(view instanceof ViewGroup)) {
            return view;
        }
        View view3 = view;
        View view4 = null;
        int childCount = view3.getChildCount();
        if (childCount == 0) {
            if (view instanceof WebView) {
                return view;
            }
            Drawable background = view3.getBackground();
            if (background != null && background.isVisible() && background.getOpacity() > -2) {
                return view;
            }
        }
        for (int i = childCount - 1; i >= 0; i--) {
            view4 = findTargetTextView(context, view3.getChildAt(i), rect);
            if (view4 != null) {
                break;
            }
        }
        return view4;
    }

    public String getPenSelectionContents(Context context, View view) {
        PenSelectionContents penSelectionContents = new PenSelectionContents();
        getPenSelectionContents(context, view, penSelectionContents);
        return penSelectionContents.mContentStr;
    }

    public boolean isPenSelectionArea(Context context, View view, int i, int i2) {
        View view2 = view;
        if (view instanceof TextView) {
            View view3 = view;
            if (view3.hasMultiSelection() && view3.isMultiSelectionLinkArea(i, i2)) {
                return true;
            }
        } else if (view instanceof ViewGroup) {
            View view4 = view;
            int childCount = view4.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                if (isPenSelectionArea(context, view4.getChildAt(i3), i, i2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
