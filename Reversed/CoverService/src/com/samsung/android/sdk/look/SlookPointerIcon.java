package com.samsung.android.sdk.look;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnHoverListener;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonFrequentContactAdapter;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;

public class SlookPointerIcon {
    private static final String TAG = "SlookPointerIcon";
    private boolean bLoggingFlag = false;
    private Context mContext;
    private Drawable mDrawable;
    private Slook mSlook = new Slook();

    class C00001 implements OnHoverListener {
        C00001() {
        }

        public boolean onHover(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 9:
                    try {
                        PointerIcon.setHoveringSpenIcon(0, SlookPointerIcon.this.mDrawable);
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }
                case 10:
                    try {
                        PointerIcon.setHoveringSpenIcon(1, -1);
                        break;
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                        break;
                    }
            }
            return false;
        }
    }

    public void setHoverIcon(View view, Drawable d) {
        if (!isSupport(1)) {
            return;
        }
        if (view == null) {
            throw new IllegalArgumentException("view is null.");
        }
        this.mContext = view.getContext();
        if (d == null) {
            view.setOnHoverListener(null);
            try {
                PointerIcon.setHoveringSpenIcon(1, -1);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        this.mDrawable = d;
        view.setOnHoverListener(new C00001());
        try {
            if (!this.bLoggingFlag) {
                insertLogForAPI("setHoverIcon");
                this.bLoggingFlag = true;
            }
        } catch (SecurityException e2) {
            throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
        }
    }

    public void setDefaultCustomIcon(Drawable d) {
        if (isSupport(1) && d != null) {
            this.mDrawable = d;
            try {
                PointerIcon.setCustomDefaultIcon(2, this.mDrawable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDisableDefaultCustomIcon() {
        if (isSupport(1)) {
            try {
                PointerIcon.setDisableCustomDefaultIcon();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSupport(int ver) {
        if (this.mSlook.isFeatureEnabled(4)) {
            return true;
        }
        return false;
    }

    private void insertLogForAPI(String apiName) {
        if (this.mContext != null) {
            int version = -1;
            Slook temp = new Slook();
            String appId = temp.getClass().getPackage().getName();
            String feature = new StringBuilder(String.valueOf(this.mContext.getPackageName())).append("#").append(temp.getVersionCode()).toString();
            try {
                version = this.mContext.getPackageManager().getPackageInfo("com.samsung.android.providers.context", SlookCocktailManager.COCKTAIL_DISPLAY_POLICY_NOT_PROVISION).versionCode;
            } catch (NameNotFoundException e) {
                Log.d("SM_SDK", "Could not find ContextProvider");
            }
            Log.d("SM_SDK", "context framework's  versionCode: " + version);
            if (version <= 1) {
                Log.d("SM_SDK", "Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
            } else if (this.mContext.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
                throw new SecurityException();
            } else {
                ContentValues cv = new ContentValues();
                cv.put("app_id", appId);
                cv.put("feature", feature);
                cv.put("extra", apiName);
                Log.d(TAG, new StringBuilder(String.valueOf(appId)).append(", ").append(feature).append(", ").append(apiName).toString());
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
                broadcastIntent.putExtra(SlookAirButtonFrequentContactAdapter.DATA, cv);
                broadcastIntent.setPackage("com.samsung.android.providers.context");
                this.mContext.sendBroadcast(broadcastIntent);
            }
        }
    }
}
