package com.samsung.android.cocktailbar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.BaseBundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class SemAbsCocktailLoadablePanel {
    public static final String EXTRA_CONFIGURATION_KEY_POSITION = "cocktail_position";
    public static final int POSITION_ON_BOTTOM = 3;
    public static final int POSITION_ON_LEFT = 0;
    public static final int POSITION_ON_RIGHT = 1;
    public static final int POSITION_ON_TOP = 2;
    private static final String TAG = SemAbsCocktailLoadablePanel.class.getSimpleName();
    protected Context mCocktailContext = null;
    protected Context mContext = null;
    private CocktailLoadablePanelListener mListener = null;
    private OnCocktailClickHandler mOnCocktailClickHandler = null;

    public interface CocktailLoadablePanelListener {
        void sendOptions(Bundle bundle);
    }

    public interface OnCocktailClickHandler {
        boolean onClickHandler(View view, PendingIntent pendingIntent);
    }

    public SemAbsCocktailLoadablePanel(Context context) {
        this.mContext = context;
    }

    public SemAbsCocktailLoadablePanel(Context context, Context context2) {
        this.mContext = context;
        this.mCocktailContext = context2;
    }

    public abstract View getView();

    public void onConfigurationChanged(Configuration configuration, Bundle bundle) {
    }

    public abstract void onCreate();

    public abstract void onDestroy();

    public void onPause() {
    }

    public void onPostResume() {
    }

    public void onReceiveContentInfo(Bundle bundle) {
    }

    public void onResume() {
    }

    public boolean performOnClickInCocktailBar(View view, PendingIntent pendingIntent) {
        if (this.mOnCocktailClickHandler != null) {
            return this.mOnCocktailClickHandler.onClickHandler(view, pendingIntent);
        }
        Log.m31e(TAG, "CocktailClickHandler was not set yet");
        return false;
    }

    public void requestCocktailBarOpen() {
        if (this.mListener != null) {
            BaseBundle bundle = new Bundle();
            bundle.putBoolean("open_panel", true);
            this.mListener.sendOptions(bundle);
            return;
        }
        Log.m31e(TAG, "CocktailLoadablePanelListener was not set yet");
    }

    public void setListener(CocktailLoadablePanelListener cocktailLoadablePanelListener) {
        this.mListener = cocktailLoadablePanelListener;
    }

    public void setOnCocktailClickHander(OnCocktailClickHandler onCocktailClickHandler) {
        this.mOnCocktailClickHandler = onCocktailClickHandler;
    }
}
