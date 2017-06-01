package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class AbsCocktailLoadablePanel {
    public static final String LOADABLE_CONTENT_CLASS = "content";
    public static final String PACKAGE_NAME = "package";
    public static final int PANEL_STATE_HIDE = 1;
    public static final int PANEL_STATE_VISIBLE = 0;
    private static final String TAG = AbsCocktailLoadablePanel.class.getSimpleName();
    protected Context mCocktailContext = null;
    protected Context mContext = null;
    private CocktailLoadablePanelListener mListener = null;
    private OnCocktailClickHandler mOnCocktailClickHandler = null;

    public interface CocktailLoadablePanelListener {
        void sendOptions(Bundle bundle);
    }

    public static class OnCocktailClickHandler {
        public boolean onClickHandler(View view, PendingIntent pendingIntent) {
            try {
                view.getContext().startIntentSender(pendingIntent.getIntentSender(), new Intent(), 268435456, 268435456, 0);
                return true;
            } catch (Throwable e) {
                Log.m32e(AbsCocktailLoadablePanel.TAG, "Cannot send pending intent: ", e);
                return false;
            } catch (Throwable e2) {
                Log.m32e(AbsCocktailLoadablePanel.TAG, "Cannot send pending intent due to unknown exception: ", e2);
                return false;
            }
        }
    }

    public AbsCocktailLoadablePanel(Context context) {
        this.mContext = context;
    }

    public AbsCocktailLoadablePanel(Context context, Context context2) {
        this.mContext = context;
        this.mCocktailContext = context2;
    }

    public CocktailLoadablePanelListener getListener() {
        return this.mListener;
    }

    public OnCocktailClickHandler getOnCocktailClickHander() {
        return this.mOnCocktailClickHandler;
    }

    public abstract View getView();

    @Deprecated
    public void onChangedDisplayPolicy(int i) {
    }

    @Deprecated
    public void onChangedReversedView(boolean z) {
    }

    public abstract void onClosePanel();

    @Deprecated
    public void onOrientationChanged(int i) {
    }

    public void onPanelVisibilityChanged(int i) {
    }

    public void setData(Bundle bundle) {
    }

    public void setListener(CocktailLoadablePanelListener cocktailLoadablePanelListener) {
        this.mListener = cocktailLoadablePanelListener;
    }

    public void setOnCocktailClickHandler(OnCocktailClickHandler onCocktailClickHandler) {
        this.mOnCocktailClickHandler = onCocktailClickHandler;
    }
}
