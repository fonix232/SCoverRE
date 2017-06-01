package com.samsung.android.desktopmode;

import android.app.InternalPresentation;
import android.app.Presentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsung.android.framework.res.C0078R;
import com.samsung.android.graphics.SemGaussianBlurFilter;
import com.samsung.android.graphics.SemImageFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DesktopModePresentationManager {
    private static final boolean DEBUG = DesktopModeFeature.DEBUG;
    private static final String TAG = DesktopModePresentationManager.class.getSimpleName();
    public static final int TYPE_ANY = 0;
    public static final int TYPE_BLACK_SCREEN = 4;
    public static final int TYPE_INTRO = 1;
    public static final int TYPE_LOADING_SCREEN_ENTER = 2;
    public static final int TYPE_LOADING_SCREEN_EXIT = 3;
    public static final int WHERE_ANY = 0;
    public static final int WHERE_EXTERNAL = 2;
    public static final int WHERE_INTERNAL = 1;
    private final Context mContext;
    private final DisplayManager mDisplayManager = ((DisplayManager) this.mContext.getSystemService("display"));
    private List<ExternalPresentation> mExternalPresentations;
    private InternalPresentationScreen mInternalPresentationScreen;

    private static class ExternalPresentation extends Presentation {
        PackageManager mPackageManager;
        final int mType;
        final Window mWindow = getWindow();

        ExternalPresentation(Context context, Display display, int i) {
            super(context, display);
            this.mType = i;
            this.mPackageManager = context.getPackageManager();
        }

        private Drawable getDefaultWallpaper() {
            Drawable drawable = null;
            try {
                Resources resourcesForApplication = this.mPackageManager.getResourcesForApplication("com.sec.android.app.desktoplauncher");
                int identifier = resourcesForApplication.getIdentifier("dex_wallpaper_001", "drawable", "com.sec.android.app.desktoplauncher");
                if (identifier > 0) {
                    drawable = resourcesForApplication.getDrawable(identifier, null);
                }
            } catch (Throwable e) {
                Log.w(DesktopModePresentationManager.TAG, "Failed to get default wallpaper", e);
            }
            return drawable;
        }

        private void updateLayout() {
            Point point = new Point();
            getDisplay().getSize(point);
            LinearLayout linearLayout = (LinearLayout) findViewById(C0078R.id.intro_layout);
            LayoutParams layoutParams = linearLayout.getLayoutParams();
            layoutParams.width = (int) (((float) point.x) * getResources().getFraction(C0078R.fraction.dex_intro_layout_width, 1, 1));
            layoutParams.height = (int) (((float) point.y) * getResources().getFraction(C0078R.fraction.dex_intro_layout_height, 1, 1));
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = (TextView) findViewById(C0078R.id.intro_title);
            textView.setTextSize(0, ((float) layoutParams.height) * getResources().getFraction(C0078R.fraction.dex_intro_title_size, 1, 1));
            textView.setText(Html.fromHtml(getResources().getString(C0078R.string.dex_intro_welcome_to, new Object[]{"<b>" + getResources().getString(C0078R.string.dex_intro_samsung_dex) + "</b>"}), 0));
            TextView textView2 = (TextView) findViewById(C0078R.id.intro_msg);
            textView2.setText(getResources().getString(C0078R.string.dex_intro_msg) + "\n\n" + String.format(getResources().getString(C0078R.string.dex_intro_msg_additional), new Object[]{getResources().getString(C0078R.string.dex_dialog_launch_positive)}));
            textView2.setTextSize(0, ((float) layoutParams.height) * getResources().getFraction(C0078R.fraction.dex_intro_msg_size, 1, 1));
            ImageView imageView = (ImageView) findViewById(C0078R.id.intro_image);
            LayoutParams layoutParams2 = imageView.getLayoutParams();
            layoutParams2.width = (int) (((float) layoutParams.width) * getResources().getFraction(C0078R.fraction.dex_intro_img_width, 1, 1));
            layoutParams2.height = (int) (((float) layoutParams.height) * getResources().getFraction(C0078R.fraction.dex_intro_img_height, 1, 1));
            imageView.setLayoutParams(layoutParams2);
            Drawable defaultWallpaper = getDefaultWallpaper();
            if (defaultWallpaper != null) {
                ImageView imageView2 = (ImageView) findViewById(C0078R.id.wallpaper_background);
                imageView2.setImageDrawable(defaultWallpaper);
                SemGaussianBlurFilter semGaussianBlurFilter = (SemGaussianBlurFilter) SemImageFilter.createImageFilter(1);
                semGaussianBlurFilter.setRadius(10.0f);
                imageView2.semSetImageFilter(semGaussianBlurFilter);
            }
        }

        public void dismiss() {
            if (DesktopModePresentationManager.DEBUG) {
                Log.d(DesktopModePresentationManager.TAG, "ExternalPresentation dismissed=" + toString());
            }
            super.dismiss();
        }

        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            this.mWindow.setType(2230);
            if (this.mType == 1) {
                setContentView(C0078R.layout.desktop_mode_intro);
                updateLayout();
            } else if (this.mType == 2 || this.mType == 3) {
                this.mWindow.setWindowAnimations(C0078R.style.loading_screen_animation);
                setContentView(C0078R.layout.desktop_mode_loading_screen);
            } else if (this.mType == 4) {
                setContentView(C0078R.layout.desktop_mode_black_screen);
            }
        }

        public void show() {
            if (DesktopModePresentationManager.DEBUG) {
                Log.d(DesktopModePresentationManager.TAG, "ExternalPresentation shown=" + toString());
            }
            super.show();
            if (this.mType == 2) {
                final ImageView imageView = (ImageView) findViewById(C0078R.id.splash_image);
                imageView.setImageDrawable(getResources().getDrawable(C0078R.drawable.desktop_mode_loading_animation_list));
                DesktopModeUiThread.getHandler().post(new Runnable() {
                    public void run() {
                        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                        if (animationDrawable != null && (animationDrawable instanceof AnimationDrawable)) {
                            animationDrawable.start();
                        }
                    }
                });
            }
        }

        public String toString() {
            return super.toString() + " mType=" + DesktopModePresentationManager.typeToString(this.mType) + " display=" + getDisplay() + " isShowing=" + isShowing() + "\n";
        }
    }

    private static class InternalPresentationScreen extends InternalPresentation {
        final Context mContext;
        int mType;
        final Window mWindow = getWindow();

        InternalPresentationScreen(Context context, Display display, int i) {
            super(context, display);
            this.mContext = context;
            this.mType = i;
        }

        private void dismissByDesktopMode() {
            if (DesktopModePresentationManager.DEBUG) {
                Log.d(DesktopModePresentationManager.TAG, "InternalPresentation dismissed=" + toString());
            }
            super.dismiss();
        }

        private void setType(int i) {
            if (DesktopModePresentationManager.DEBUG) {
                Log.d(DesktopModePresentationManager.TAG, "InternalPresentation type changed to=" + DesktopModePresentationManager.typeToString(i));
            }
            this.mType = i;
            if (i == 2 || i == 3) {
                this.mWindow.addFlags(2097280);
                setContentView(C0078R.layout.desktop_mode_loading_screen_internal);
            } else if (i == 4) {
                this.mWindow.clearFlags(2097280);
                setContentView(C0078R.layout.desktop_mode_black_screen);
            }
        }

        public void dismiss() {
        }

        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setCancelable(false);
            this.mWindow.clearFlags(65536);
            WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
            attributes.type = 2421;
            attributes.flags |= 533784;
            attributes.privateFlags |= 16;
            attributes.samsungFlags |= 131072;
            this.mWindow.setAttributes(attributes);
            setType(this.mType);
        }

        public void show() {
            if (DesktopModePresentationManager.DEBUG) {
                Log.d(DesktopModePresentationManager.TAG, "InternalPresentation shown=" + toString());
            }
            super.show();
        }

        public String toString() {
            return super.toString() + " mType=" + DesktopModePresentationManager.typeToString(this.mType) + " display=" + getDisplay() + " isShowing=" + isShowing() + "\n";
        }
    }

    public DesktopModePresentationManager(Context context) {
        this.mContext = context;
    }

    private void dismissInternal() {
        if (this.mInternalPresentationScreen != null) {
            this.mInternalPresentationScreen.dismissByDesktopMode();
            this.mInternalPresentationScreen = null;
        }
    }

    public static String typeToString(int i) {
        switch (i) {
            case 0:
                return "TYPE_ANY";
            case 1:
                return "TYPE_INTRO";
            case 2:
                return "TYPE_LOADING_SCREEN_ENTER";
            case 3:
                return "TYPE_LOADING_SCREEN_EXIT";
            case 4:
                return "TYPE_BLACK_SCREEN";
            default:
                return "Unknown=" + i;
        }
    }

    public void dismissAll() {
        if (DEBUG) {
            Log.d(TAG, "dismissAll()");
        }
        dismissExternal(0);
        dismissInternal();
    }

    public void dismissExternal(int i) {
        if (this.mExternalPresentations != null) {
            Iterator it = this.mExternalPresentations.iterator();
            while (it.hasNext()) {
                ExternalPresentation externalPresentation = (ExternalPresentation) it.next();
                if (i == 0 || externalPresentation.mType == i) {
                    externalPresentation.dismiss();
                    it.remove();
                }
            }
            if (this.mExternalPresentations.isEmpty()) {
                this.mExternalPresentations = null;
            }
        }
    }

    public boolean exists(int i, int i2) {
        boolean z = true;
        if ((i == 0 || i == 2) && this.mExternalPresentations != null) {
            for (ExternalPresentation externalPresentation : this.mExternalPresentations) {
                if (externalPresentation.mType == i2) {
                    return true;
                }
            }
        }
        if ((i != 0 && i != 1) || this.mInternalPresentationScreen == null) {
            z = false;
        } else if (this.mInternalPresentationScreen.mType != i2) {
            z = false;
        }
        return z;
    }

    public void showExternal(int i) {
        for (Display display : this.mDisplayManager.getDisplays()) {
            if (display.getType() != 1) {
                ExternalPresentation externalPresentation = new ExternalPresentation(this.mContext, display, i);
                try {
                    externalPresentation.show();
                    if (this.mExternalPresentations == null) {
                        this.mExternalPresentations = new ArrayList();
                    }
                    this.mExternalPresentations.add(externalPresentation);
                } catch (Throwable e) {
                    if (DEBUG) {
                        Log.w(TAG, "Display was removed in the meantime.", e);
                    }
                }
            }
        }
    }

    public void showInternal(int i) {
        if (this.mInternalPresentationScreen == null) {
            this.mInternalPresentationScreen = new InternalPresentationScreen(this.mContext, this.mDisplayManager.getDisplay(0), i);
        } else {
            this.mInternalPresentationScreen.setType(i);
        }
        this.mInternalPresentationScreen.show();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(256);
        stringBuilder.append("{");
        if (this.mInternalPresentationScreen != null) {
            stringBuilder.append("\n");
            stringBuilder.append(this.mInternalPresentationScreen);
        }
        if (this.mExternalPresentations != null) {
            stringBuilder.append("\n");
            for (ExternalPresentation append : this.mExternalPresentations) {
                stringBuilder.append(append);
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
