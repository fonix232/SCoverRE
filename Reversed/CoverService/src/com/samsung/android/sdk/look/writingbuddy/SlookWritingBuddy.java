package com.samsung.android.sdk.look.writingbuddy;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.samsung.android.sdk.look.Slook;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonFrequentContactAdapter;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.writingbuddy.WritingBuddyImpl;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnImageWritingListener;
import com.samsung.android.writingbuddy.WritingBuddyImpl.OnTextWritingListener;

public final class SlookWritingBuddy {
    private static final String TAG = "WritingBuddy";
    public static final int TYPE_EDITOR_NUMBER = 1;
    public static final int TYPE_EDITOR_TEXT = 2;
    private ImageWritingListener mImageWritingListener;
    private View mParentView;
    private Slook mSlook = new Slook();
    private TextWritingListener mTextWritingListener;
    private WritingBuddyImpl mWritingBuddyImpl;

    class C00031 implements OnTextWritingListener {
        C00031() {
        }

        public void onTextReceived(CharSequence text) {
            SlookWritingBuddy.this.mTextWritingListener.onTextReceived(text);
        }
    }

    class C00042 implements OnImageWritingListener {
        C00042() {
        }

        public void onImageReceived(Bitmap image) {
            SlookWritingBuddy.this.mImageWritingListener.onImageReceived(image);
        }
    }

    public interface ImageWritingListener {
        void onImageReceived(Bitmap bitmap);
    }

    public interface TextWritingListener {
        void onTextReceived(CharSequence charSequence);
    }

    public SlookWritingBuddy(ViewGroup parentView) {
        if (isSupport(1)) {
            this.mParentView = parentView;
            this.mWritingBuddyImpl = new WritingBuddyImpl(parentView);
            try {
                insertLogForAPI("SlookWritingBuddy");
                return;
            } catch (SecurityException e) {
                throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
            }
        }
        Log.e(TAG, "This is not supported in device");
    }

    public SlookWritingBuddy(EditText editText) {
        if (isSupport(1)) {
            this.mParentView = editText;
            this.mWritingBuddyImpl = new WritingBuddyImpl(editText);
            try {
                insertLogForAPI("SlookWritingBuddy");
                return;
            } catch (SecurityException e) {
                throw new SecurityException("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission is required.");
            }
        }
        Log.e(TAG, "This is not supported in device");
    }

    public void setEditorType(int type) {
        if (isSupport(1)) {
            this.mWritingBuddyImpl.setEditorType(type);
        }
    }

    public int getEditorType() {
        if (isSupport(1)) {
            return this.mWritingBuddyImpl.getEditorType();
        }
        return 0;
    }

    public void setTextWritingListener(TextWritingListener l) {
        if (isSupport(1)) {
            this.mTextWritingListener = l;
            this.mWritingBuddyImpl.setOnTextWritingListener(new C00031());
        }
    }

    public void setImageWritingListener(ImageWritingListener l) {
        if (isSupport(1)) {
            this.mImageWritingListener = l;
            if (l == null) {
                this.mWritingBuddyImpl.setImageWritingEnabled(false);
                this.mWritingBuddyImpl.setOnImageWritingListener(null);
                return;
            }
            this.mWritingBuddyImpl.setImageWritingEnabled(true);
            this.mWritingBuddyImpl.setOnImageWritingListener(new C00042());
        }
    }

    public void setEnabled(boolean enabled) {
        if (!isSupport(1)) {
            return;
        }
        if (this.mParentView == null) {
            throw new IllegalStateException("mParentView is null.");
        } else if (this.mParentView instanceof EditText) {
            this.mParentView.setWritingBuddyEnabled(enabled);
        } else if (this.mParentView.getWritingBuddy(false) != null) {
            this.mParentView.setWritingBuddyEnabled(enabled);
        } else {
            throw new IllegalStateException("WritingBuddy was not enabled.");
        }
    }

    public boolean isEnabled() {
        if (!isSupport(1)) {
            return false;
        }
        if (this.mParentView != null) {
            return this.mParentView.isWritingBuddyEnabled();
        }
        throw new IllegalStateException("mParentView is null.");
    }

    private boolean isSupport(int ver) {
        if (this.mSlook.isFeatureEnabled(3)) {
            return true;
        }
        return false;
    }

    private void insertLogForAPI(String apiName) {
        if (this.mParentView.getContext() != null) {
            int version = -1;
            Slook temp = new Slook();
            String appId = temp.getClass().getPackage().getName();
            String feature = new StringBuilder(String.valueOf(this.mParentView.getContext().getPackageName())).append("#").append(temp.getVersionCode()).toString();
            try {
                version = this.mParentView.getContext().getPackageManager().getPackageInfo("com.samsung.android.providers.context", SlookCocktailManager.COCKTAIL_DISPLAY_POLICY_NOT_PROVISION).versionCode;
            } catch (NameNotFoundException e) {
                Log.d("SM_SDK", "Could not find ContextProvider");
            }
            Log.d("SM_SDK", "context framework's  versionCode: " + version);
            if (version <= 1) {
                Log.d("SM_SDK", "Add com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY permission");
            } else if (this.mParentView.getContext().checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY") != 0) {
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
                this.mParentView.getContext().sendBroadcast(broadcastIntent);
            }
        }
    }
}
