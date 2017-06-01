package com.samsung.android.sdk.look.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.samsung.android.cocktailbar.CocktailBarManager;
import com.samsung.android.cocktailbar.CocktailBarManager.CocktailBarFeedsListener;
import com.samsung.android.cocktailbar.CocktailBarManager.CocktailBarStateListener;
import com.samsung.android.cocktailbar.FeedsInfo.Builder;
import com.samsung.android.sdk.look.Slook;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public final class SlookCocktailManager {
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL = 65536;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_CALLING = 65538;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_COMMAND = 65543;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_HEADSET = 65541;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_INCOMING_CALL = 65537;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_NOTIFICATION = 65540;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_SPEN = 65542;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_TICKER = 65539;
    public static final int COCKTAIL_CATEGORY_GLOBAL = 1;
    public static final int COCKTAIL_DISPLAY_POLICY_ALL = 159;
    public static final int COCKTAIL_DISPLAY_POLICY_GENERAL = 1;
    public static final int COCKTAIL_DISPLAY_POLICY_INDEX_MODE = 16;
    public static final int COCKTAIL_DISPLAY_POLICY_LOCKSCREEN = 2;
    public static final int COCKTAIL_DISPLAY_POLICY_NOT_PROVISION = 128;
    public static final int COCKTAIL_DISPLAY_POLICY_SCOVER = 4;
    public static final int COCKTAIL_DISPLAY_POLICY_TABLE_MODE = 8;
    public static final int COCKTAIL_VISIBILITY_HIDE = 2;
    public static final int COCKTAIL_VISIBILITY_SHOW = 1;
    private static final String TAG = "SlookCocktailManager";
    static WeakHashMap<Context, WeakReference<SlookCocktailManager>> sManagerCache = new WeakHashMap();
    private CocktailBarManager mCocktailBarManager;
    private Context mContext;
    private FeedsListenerWrapper mFeedsUpdatedListenerWrapper;
    private Slook mSlook = new Slook();
    private StateChangeListenerWrapper mStateChangeListenerWrapper;

    public static class CocktailInfo {
        private int mCategory;
        private ComponentName mClassInfo;
        private Bundle mContentInfo;
        private RemoteViews mContentView;
        private int mDisplayPolicy;
        private RemoteViews mHelpView;

        public static final class Builder {
            private int mCategory = 1;
            private ComponentName mClassInfo;
            private Bundle mContentInfo;
            private RemoteViews mContentView;
            private int mDisplayPolicy = 1;
            private RemoteViews mHelpView;
            private int mIcon = 0;

            public Builder(RemoteViews contentView) {
                this.mContentView = contentView;
            }

            public Builder(Bundle contentInfo) {
                this.mContentInfo = contentInfo;
            }

            public Builder(ComponentName classInfo) {
                this.mClassInfo = classInfo;
            }

            public Builder setDisplayPolicy(int displayPolicy) {
                this.mDisplayPolicy = displayPolicy;
                return this;
            }

            public Builder setCategory(int category) {
                this.mCategory = category;
                return this;
            }

            public Builder setHelpView(RemoteViews helpView) {
                this.mHelpView = helpView;
                return this;
            }

            public Builder setContentInfo(Bundle contentInfo) {
                this.mContentInfo = contentInfo;
                return this;
            }

            public CocktailInfo build() {
                return new CocktailInfo(this.mDisplayPolicy, this.mCategory, this.mIcon, this.mContentView, this.mHelpView, this.mContentInfo, this.mClassInfo);
            }
        }

        private CocktailInfo(int displayPolicy, int category, int icon, RemoteViews contentView, RemoteViews helpView, Bundle contentInfo, ComponentName classInfo) {
            this.mDisplayPolicy = 1;
            this.mCategory = 1;
            this.mDisplayPolicy = displayPolicy;
            this.mCategory = category;
            this.mContentView = contentView;
            this.mHelpView = helpView;
            this.mContentInfo = contentInfo;
            this.mClassInfo = classInfo;
        }
    }

    public static class FeedsInfo {
        CharSequence feedsText;
        int icon;
        Bitmap largeIcon;

        public static final class Builder {
            private CharSequence mFeedsText;
            private int mIcon;
            private Bitmap mLargeIcon;

            public Builder(CharSequence feedsText) {
                this.mFeedsText = feedsText;
            }

            public Builder setIcon(int icon) {
                this.mIcon = icon;
                return this;
            }

            public Builder setLargeIcon(Bitmap icon) {
                this.mLargeIcon = icon;
                return this;
            }

            public FeedsInfo build() {
                return new FeedsInfo(this.mIcon, this.mFeedsText, this.mLargeIcon);
            }
        }

        private FeedsInfo(int icon, CharSequence feedsText, Bitmap largeIcon) {
            this.icon = icon;
            this.feedsText = feedsText;
            this.largeIcon = largeIcon;
        }
    }

    private class FeedsListenerWrapper extends CocktailBarFeedsListener {
        private OnFeedUpdatedListener mOnFeedUpdatedListener = null;

        public FeedsListenerWrapper(OnFeedUpdatedListener listener) {
            this.mOnFeedUpdatedListener = listener;
        }

        public void setOnFeedsUpdatedListener(OnFeedUpdatedListener listener) {
            this.mOnFeedUpdatedListener = listener;
        }

        public void onFeedsUpdated(int cocktailId, List<com.samsung.android.cocktailbar.FeedsInfo> feedsInfoList) {
            if (this.mOnFeedUpdatedListener != null) {
                List<FeedsInfo> sFeedsInfoList = new ArrayList();
                String packageName = null;
                for (com.samsung.android.cocktailbar.FeedsInfo fi : feedsInfoList) {
                    sFeedsInfoList.add(new Builder(fi.feedsText).setIcon(fi.icon).setLargeIcon(fi.largeIcon).build());
                    if (packageName == null) {
                        packageName = fi.packageName;
                    }
                }
                this.mOnFeedUpdatedListener.onFeedUpdated(cocktailId, sFeedsInfoList, packageName);
            }
        }
    }

    public interface OnFeedUpdatedListener {
        void onFeedUpdated(int i, List<FeedsInfo> list, String str);
    }

    public interface OnStateChangeListener {
        void onBackgroundTypeChanged(int i);

        void onCocktailBarWindowTypeChanged(int i);

        void onPositionChanged(int i);

        void onVisibilityChanged(int i);
    }

    private class StateChangeListenerWrapper extends CocktailBarStateListener {
        private OnStateChangeListener mOnStateChangeListener = null;

        public StateChangeListenerWrapper(OnStateChangeListener listener) {
            this.mOnStateChangeListener = listener;
        }

        public void setOnStateChangeListener(OnStateChangeListener listener) {
            this.mOnStateChangeListener = listener;
        }

        public void onCocktailBarVisibilityChanged(int visibility) {
            if (this.mOnStateChangeListener != null) {
                this.mOnStateChangeListener.onVisibilityChanged(visibility);
            }
        }

        public void onCocktailBarBackgroundTypeChanged(int bgType) {
            if (this.mOnStateChangeListener != null) {
                this.mOnStateChangeListener.onBackgroundTypeChanged(bgType);
            }
        }

        public void onCocktailBarPositionChanged(int position) {
            if (this.mOnStateChangeListener != null) {
                this.mOnStateChangeListener.onPositionChanged(position);
            }
        }

        public void onCocktailBarWindowTypeChanged(int windowType) {
            if (this.mOnStateChangeListener != null) {
                this.mOnStateChangeListener.onCocktailBarWindowTypeChanged(windowType);
            }
        }
    }

    public interface States {
        public static final int BACKGROUND_DIM = 2;
        public static final int BACKGROUND_OPAQUE = 1;
        public static final int BACKGROUND_TRANSPARENT = 3;
        public static final int FULLSCREEN_TYPE = 2;
        public static final int MINIMIZE_TYPE = 1;
        public static final int POSITION_BOTTOM = 4;
        public static final int POSITION_LEFT = 1;
        public static final int POSITION_RIGHT = 2;
        public static final int POSITION_TOP = 3;
        public static final int STATE_INVISIBLE = 2;
        public static final int STATE_VISIBLE = 1;
        public static final int UNKNOWN_TYPE = 0;
    }

    public static SlookCocktailManager getInstance(Context context) {
        SlookCocktailManager slookCocktailManager;
        synchronized (sManagerCache) {
            if (context == null) {
                throw new IllegalArgumentException("context is null.");
            } else if ((context instanceof ContextWrapper) && ((ContextWrapper) context).getBaseContext() == null) {
                throw new IllegalArgumentException("Base context is null.");
            } else {
                WeakReference<SlookCocktailManager> ref = (WeakReference) sManagerCache.get(context);
                slookCocktailManager = null;
                if (ref != null) {
                    slookCocktailManager = (SlookCocktailManager) ref.get();
                }
                if (slookCocktailManager == null) {
                    slookCocktailManager = new SlookCocktailManager(context);
                    sManagerCache.put(context, new WeakReference(slookCocktailManager));
                }
            }
        }
        return slookCocktailManager;
    }

    private SlookCocktailManager(Context context) {
        this.mContext = context;
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(context);
        }
    }

    private void ensureCocktailBarManager(Context context) {
        if (this.mCocktailBarManager == null) {
            this.mCocktailBarManager = CocktailBarManager.getInstance(context);
        }
    }

    public void updateCocktail(int cocktailId, CocktailInfo info) {
        if (!this.mSlook.isFeatureEnabled(7)) {
            return;
        }
        if (info == null) {
            throw new IllegalArgumentException("CocktailInfo is null.");
        }
        ensureCocktailBarManager(this.mContext);
        if (VERSION.SDK_INT >= 23) {
            this.mCocktailBarManager.updateCocktail(cocktailId, info.mDisplayPolicy, info.mCategory, info.mContentView, info.mHelpView, info.mContentInfo, info.mClassInfo);
            return;
        }
        this.mCocktailBarManager.updateCocktail(cocktailId, info.mDisplayPolicy, info.mCategory, info.mContentView, info.mContentInfo);
    }

    public void partiallyUpdateCocktail(int cocktailId, RemoteViews contentView) {
        if (!this.mSlook.isFeatureEnabled(7)) {
            return;
        }
        if (contentView == null) {
            throw new IllegalArgumentException("contentView is null.");
        }
        ensureCocktailBarManager(this.mContext);
        this.mCocktailBarManager.partiallyUpdateCocktail(cocktailId, contentView);
    }

    public void showCocktail(int cocktailId) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            this.mCocktailBarManager.showCocktail(cocktailId);
        }
    }

    public void closeCocktail(int cocktailId, int category) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            this.mCocktailBarManager.closeCocktail(cocktailId, category);
        }
    }

    public void notifyCocktailViewDataChanged(int cocktailId, int viewId) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            this.mCocktailBarManager.notifyCocktailViewDataChanged(cocktailId, viewId);
        }
    }

    public int[] getCocktailIds(ComponentName provider) {
        if (!this.mSlook.isFeatureEnabled(7)) {
            return new int[0];
        }
        ensureCocktailBarManager(this.mContext);
        return this.mCocktailBarManager.getCocktailIds(provider);
    }

    public void disableCocktail(ComponentName provider) {
        if (VERSION.SDK_INT >= 23 && this.mSlook.isFeatureEnabled(7)) {
            try {
                ensureCocktailBarManager(this.mContext);
                this.mCocktailBarManager.disableCocktail(provider);
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEnabledCocktail(ComponentName provider) {
        if (!this.mSlook.isFeatureEnabled(7)) {
            return false;
        }
        ensureCocktailBarManager(this.mContext);
        return this.mCocktailBarManager.isEnabledCocktail(provider);
    }

    public int getCocktailBarWindowType() {
        if (VERSION.SDK_INT >= 23 && this.mSlook.isFeatureEnabled(7)) {
            try {
                ensureCocktailBarManager(this.mContext);
                return this.mCocktailBarManager.getCocktailBarWindowType();
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void setLongPressEnabled(boolean enabled) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            this.mCocktailBarManager.updateLongpressGesture(enabled);
        }
    }

    public void setCocktailBarStatus(boolean shift, boolean transparent) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            this.mCocktailBarManager.setCocktailBarStatus(shift, transparent);
        }
    }

    public void updateFeeds(int cocktailId, List<FeedsInfo> feedsInfoList) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            List<com.samsung.android.cocktailbar.FeedsInfo> cFeedsInfoList = new ArrayList();
            for (FeedsInfo fi : feedsInfoList) {
                cFeedsInfoList.add(new Builder(fi.feedsText, this.mCocktailBarManager.getContext().getPackageName()).setIcon(fi.icon).setLargeIcon(fi.largeIcon).build());
            }
            this.mCocktailBarManager.updateFeeds(cocktailId, cFeedsInfoList);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            if (listener == null) {
                this.mCocktailBarManager.unregisterListener(this.mStateChangeListenerWrapper);
                this.mStateChangeListenerWrapper = null;
            } else if (this.mStateChangeListenerWrapper == null) {
                this.mStateChangeListenerWrapper = new StateChangeListenerWrapper(listener);
                this.mCocktailBarManager.registerListener(this.mStateChangeListenerWrapper);
            } else {
                this.mStateChangeListenerWrapper.setOnStateChangeListener(listener);
            }
        }
    }

    public void setOnFeedUpdatedListener(OnFeedUpdatedListener listener) {
        if (this.mSlook.isFeatureEnabled(7)) {
            ensureCocktailBarManager(this.mContext);
            if (listener == null) {
                this.mCocktailBarManager.unregisterOnFeedsUpdatedListener(this.mFeedsUpdatedListenerWrapper);
                this.mFeedsUpdatedListenerWrapper = null;
            } else if (this.mFeedsUpdatedListenerWrapper == null) {
                this.mFeedsUpdatedListenerWrapper = new FeedsListenerWrapper(listener);
                this.mCocktailBarManager.registerOnFeedsUpdatedListener(this.mFeedsUpdatedListenerWrapper);
            } else {
                this.mFeedsUpdatedListenerWrapper.setOnFeedsUpdatedListener(listener);
            }
        }
    }
}
