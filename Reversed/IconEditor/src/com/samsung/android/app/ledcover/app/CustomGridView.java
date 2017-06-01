package com.samsung.android.app.ledcover.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.GridView;
import android.widget.ScrollView;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.adapter.LCoverCallIconListAdapter;
import com.samsung.android.app.ledcover.adapter.LCoverNotiIconListAdapter;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;

public class CustomGridView extends GridView {
    private int mListItemHeight;
    private ScrollView mParentScrollView;
    private int mParentScrollViewTopPosition;
    private int mScreenHeight;
    boolean stretched;
    private int val;

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.stretched = false;
        this.mParentScrollView = null;
        this.mScreenHeight = 0;
        this.mParentScrollViewTopPosition = 0;
        this.mListItemHeight = 0;
        this.val = 88;
    }

    public boolean isStretched() {
        return this.stretched;
    }

    public int getScollViewX() {
        return this.mParentScrollView.getScrollX();
    }

    public int getScollViewY() {
        return this.mParentScrollView.getScrollY();
    }

    public void setParents(Activity activity, ScrollView scrollView, String type) {
        this.mParentScrollView = scrollView;
        this.mScreenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        if (type.equals(Defines.MENU_CALL)) {
            View listItem = ((LCoverCallIconListAdapter) getAdapter()).getView(0, null, this);
            listItem.measure(0, 0);
            this.mListItemHeight = listItem.getMeasuredHeight();
            this.mParentScrollViewTopPosition = this.val;
            return;
        }
        listItem = ((LCoverNotiIconListAdapter) getAdapter()).getView(0, null, this);
        listItem.measure(0, 0);
        this.mListItemHeight = listItem.getMeasuredHeight();
        this.mParentScrollViewTopPosition = this.val;
    }

    public void onMeasure(int widthValue, int heightValue) {
        if (isStretched()) {
            super.onMeasure(widthValue, MeasureSpec.makeMeasureSpec(ViewCompat.MEASURED_SIZE_MASK, ExploreByTouchHelper.INVALID_ID));
            getLayoutParams().height = getMeasuredHeight();
            return;
        }
        super.onMeasure(widthValue, heightValue);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        View itemView = getChildAt(getSelectedItemPosition());
        if (itemView != null) {
            int[] loc = new int[2];
            itemView.getLocationOnScreen(loc);
            int targetYPosition = loc[1] - this.mListItemHeight;
            if (targetYPosition < 0) {
                this.mParentScrollView.smoothScrollBy(0, targetYPosition);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int[] loc = new int[2];
        View itemView;
        int targetYPosition;
        switch (keyCode) {
            case ConnectionResult.SERVICE_MISSING_PERMISSION /*19*/:
                itemView = getChildAt(getSelectedItemPosition());
                if (itemView != null) {
                    itemView.getLocationOnScreen(loc);
                    targetYPosition = loc[1] - this.mListItemHeight;
                    if (targetYPosition < this.mParentScrollViewTopPosition) {
                        this.mParentScrollView.smoothScrollBy(0, targetYPosition - this.mParentScrollViewTopPosition);
                        break;
                    }
                }
                return super.onKeyDown(keyCode, event);
                break;
            case C0270R.styleable.Toolbar_maxButtonHeight /*20*/:
                itemView = getChildAt(getSelectedItemPosition());
                if (itemView != null) {
                    itemView.getLocationOnScreen(loc);
                    targetYPosition = loc[1] + (this.mListItemHeight * 2);
                    if (targetYPosition > this.mScreenHeight) {
                        this.mParentScrollView.smoothScrollBy(0, targetYPosition - this.mScreenHeight);
                        break;
                    }
                }
                return super.onKeyDown(keyCode, event);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setExpanded(boolean stretched) {
        this.stretched = stretched;
    }
}
