package com.samsung.android.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.app.ResolverActivity;
import com.android.internal.app.ResolverActivity.DisplayResolveInfo;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Method;
import java.util.List;

public class SShareShareLink {
    private static final boolean DEBUG = false;
    private static final String TAG = "SShareShareLink";
    private static boolean mEasySignUpCertificated = false;
    private int defaultTextSize;
    private Activity mActivity;
    private SShareBixby mBixby;
    private Context mContext;
    private List<Intent> mExtraIntentList;
    private SShareCommon mFeature;
    private Intent mIntent;
    private SShareLogging mLogging;
    private Intent mOrigIntent;
    private boolean mShareLinkEnabled = true;
    private DisplayResolveInfo mSimpleSharingDri;

    class C02431 implements OnClickListener {
        C02431() {
        }

        public void onClick(View view) {
            SShareShareLink.this.shareLinkItemClick();
            SShareShareLink.this.mActivity.finish();
        }
    }

    public SShareShareLink(Activity activity, Context context, SShareCommon sShareCommon, Intent intent, List<Intent> list, SShareBixby sShareBixby) {
        this.mActivity = activity;
        this.mContext = context;
        this.mFeature = sShareCommon;
        this.mOrigIntent = intent;
        this.mExtraIntentList = list;
        this.mBixby = sShareBixby;
        checkEasySignUpCertificated();
        checkShareLinkEnabled();
        if (this.mFeature.getSupportLogging()) {
            this.mLogging = new SShareLogging(this.mContext, this.mOrigIntent);
        }
    }

    private void checkEasySignUpCertificated() {
        mEasySignUpCertificated = SShareSignUpManager.isJoined(this.mContext, 2);
        Log.d(TAG, "isJoined=" + mEasySignUpCertificated);
    }

    private void checkMaxFontScale(TextView textView, int i) {
        float f = this.mContext.getResources().getConfiguration().fontScale;
        if (f > SShareConstants.MAX_FONT_SCALE) {
            textView.setTextSize(0, (((float) i) / f) * SShareConstants.MAX_FONT_SCALE);
        }
    }

    private void checkShareLinkEnabled() {
        this.mShareLinkEnabled = true;
    }

    public Intent getShareLinkIntent() {
        Parcelable intent = new Intent(this.mOrigIntent);
        intent.setComponent(null);
        this.mIntent = new Intent(SShareConstants.INTENT_ACTION_SHARELINK_SEND);
        this.mIntent.addFlags(67108864);
        this.mIntent.putExtra("android.intent.extra.INTENT", intent);
        if (this.mExtraIntentList != null) {
            int size = this.mExtraIntentList.size();
            Parcelable[] parcelableArr = new Intent[size];
            for (int i = 0; i < size; i++) {
                parcelableArr[i] = (Intent) this.mExtraIntentList.get(i);
            }
            this.mIntent.putExtra("android.intent.extra.INITIAL_INTENTS", parcelableArr);
        }
        return this.mIntent;
    }

    public boolean isEasySignUpCertificated() {
        return mEasySignUpCertificated;
    }

    public boolean isShareLinkEnabled() {
        return this.mShareLinkEnabled;
    }

    public void setShareLinkDri(DisplayResolveInfo displayResolveInfo) {
        if (displayResolveInfo == null) {
            Log.e(TAG, "setShareLinkDri : dri is null!!");
        }
        this.mSimpleSharingDri = displayResolveInfo;
    }

    public void setShareLinkView() {
        View findViewById = this.mActivity.findViewById(16909522);
        if (findViewById != null) {
            findViewById.setOnClickListener(new C02431());
            Drawable drawable = null;
            String str = null;
            String str2 = null;
            try {
                Class cls = Class.forName(SShareConstants.SIMPLE_SHARING_PANEL_COMPONENT_CLASS_NAME, true, new PathClassLoader(this.mContext.getPackageManager().getApplicationInfo(SShareConstants.SIMPLE_SHARING_PKG, 128).sourceDir, ClassLoader.getSystemClassLoader()));
                Context createPackageContext = this.mContext.createPackageContext(SShareConstants.SIMPLE_SHARING_PKG, 0);
                Class[] clsArr = new Class[]{Context.class};
                Method method = cls.getMethod("getIcon", clsArr);
                Method method2 = cls.getMethod("getTitle", clsArr);
                Method method3 = cls.getMethod("getDescription", clsArr);
                Object newInstance = cls.newInstance();
                drawable = (Drawable) method.invoke(newInstance, new Object[]{createPackageContext});
                str = (String) method2.invoke(newInstance, new Object[]{createPackageContext});
                str2 = (String) method3.invoke(newInstance, new Object[]{createPackageContext});
            } catch (Throwable e) {
                e.printStackTrace();
            } catch (Throwable e2) {
                e2.printStackTrace();
            } catch (Throwable e3) {
                e3.printStackTrace();
            } catch (Throwable e4) {
                e4.printStackTrace();
            } catch (Throwable e5) {
                e5.printStackTrace();
            } catch (Throwable e6) {
                e6.printStackTrace();
            }
            TextView textView = (TextView) findViewById.findViewById(16908308);
            this.defaultTextSize = this.mContext.getResources().getDimensionPixelSize(17105539);
            if (textView != null) {
                checkMaxFontScale(textView, this.defaultTextSize);
                if (str != null) {
                    CharSequence fromHtml = Html.fromHtml(str);
                    this.mFeature.setShareLinkReflectionTitle(fromHtml.toString());
                    textView.setText(fromHtml);
                    Log.i(TAG, "reflectionTitle " + str);
                }
            }
            TextView textView2 = (TextView) findViewById.findViewById(16908309);
            this.defaultTextSize = this.mContext.getResources().getDimensionPixelSize(17105540);
            if (textView2 != null) {
                checkMaxFontScale(textView2, this.defaultTextSize);
                if (str2 != null) {
                    textView2.setText(Html.fromHtml(str2));
                    Log.i(TAG, "reflectionDescription " + str2);
                }
            }
            ImageView imageView = (ImageView) findViewById.findViewById(16909523);
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }

    public void shareLinkItemClick() {
        if (this.mLogging != null) {
            this.mLogging.insertLog(SShareConstants.SURVEY_FEATURE_SHARELINK, null);
        }
        try {
            ((ResolverActivity) this.mActivity).safelyStartActivity(this.mSimpleSharingDri);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "shareLinkItemClick : startActivity failed!!");
        }
    }

    public void shareLinkTipClick() {
        Intent intent = new Intent(SShareConstants.INTENT_ACTION_SHARELINK_TIP);
        intent.addFlags(67108864);
        try {
            this.mActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "shareLinkTipClick : startActivity failed!!");
        }
    }
}
