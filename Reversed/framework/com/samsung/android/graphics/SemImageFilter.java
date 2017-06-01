package com.samsung.android.graphics;

import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.os.SystemProperties;
import android.view.View;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SemImageFilter implements Cloneable {
    public static final String DEBUG_HWUI_IMAGE_FILTER_ENABLE_PROPERTY = "debug.hwui.ImageFilter.enable";
    public static final String DEBUG_HWUI_IMAGE_FILTER_LOG_PROPERTY = "debug.hwui.ImageFilter.log";
    protected static final String LOG_TAG = "HWUI_IMAGE_FILTER";
    public static final int TYPE_BITMAP_COLOR = 52;
    public static final int TYPE_BLENDING = 53;
    public static final int TYPE_BLUR = 54;
    public static final int TYPE_COLORIZE = 16;
    public static final int TYPE_COLOR_CLAMP = 18;
    public static final int TYPE_COSINE_BLUR = 4;
    public static final int TYPE_CUSTOM_FILTER = 238;
    public static final int TYPE_DESATURATION = 17;
    public static final int TYPE_DIRECTIONAL_BLUR = 2;
    public static final int TYPE_DISTORTION = 49;
    public static final int TYPE_DROP_SHADOW = 55;
    public static final int TYPE_GAUSSIAN_BLUR = 1;
    public static final int TYPE_GRADIENT_GAUSSIAN_BLUR = 57;
    public static final int TYPE_KNITTED = 64;
    public static final int TYPE_MOSAIC = 51;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_SGI_BLUR = 5;
    public static final int TYPE_TILT_SHIFT = 56;
    public static final int TYPE_VIGNETTE = 50;
    public static final int TYPE_ZOOM_BLUR = 3;
    private static int mGlobalAnimationId = 0;
    protected static final boolean sLogingEnabled = SystemProperties.getBoolean(DEBUG_HWUI_IMAGE_FILTER_LOG_PROPERTY, false);
    protected IImageFilterListener mListener;
    protected long mNativeImageFilter;
    protected View mView;

    public interface IAnimationListener {
        void animate(float f, ImageFilterAnimator imageFilterAnimator);
    }

    public interface IImageFilterListener {
        void onAttachedToView();

        void onParamsChanged();

        void onViewSizeChanged();
    }

    public static final class ImageFilterAnimator {
        private final TimeInterpolator mInterpolator;
        private final IAnimationListener mListener;
        private long mNativeImageFilter;

        ImageFilterAnimator(IAnimationListener iAnimationListener, TimeInterpolator timeInterpolator) {
            this.mListener = iAnimationListener;
            this.mInterpolator = timeInterpolator;
        }

        void notify(float f, long j) {
            this.mNativeImageFilter = j;
            if (this.mInterpolator != null) {
                f = this.mInterpolator.getInterpolation(f);
            }
            this.mListener.animate(f, this);
        }

        public void setUniformf(String str, float[] fArr, int i) {
            SemImageFilter.native_setUniformfDirect(this.mNativeImageFilter, str, fArr, i);
        }

        public void setUniformi(String str, int[] iArr, int i) {
            SemImageFilter.native_setUniformiDirect(this.mNativeImageFilter, str, iArr, i);
        }
    }

    protected SemImageFilter() {
        this(0);
    }

    protected SemImageFilter(int i) {
        this.mView = null;
        this.mListener = null;
        this.mNativeImageFilter = native_init(i);
    }

    protected SemImageFilter(SemImageFilter semImageFilter) {
        this.mView = null;
        this.mListener = null;
        this.mNativeImageFilter = native_copy(semImageFilter.mNativeImageFilter);
    }

    public static SemCustomFilter createCustomFilter(String str, String str2) {
        return (str == null || str2 == null) ? null : new SemCustomFilter(str, str2);
    }

    public static SemImageFilter createImageFilter(int i) {
        switch (i) {
            case 1:
                return new SemGaussianBlurFilter();
            case 2:
                return new SemDirectionalBlurFilter();
            case 3:
                return new SemZoomBlurFilter();
            case 4:
                return new SemCosineBlurFilter();
            case 5:
                return new SemSgiBlurFilter();
            case 16:
                return new SemColorizeFilter();
            case 17:
                return new SemDesaturationFilter();
            case 18:
                return new SemColorClampFilter();
            case 49:
                return new SemDistortionFilter();
            case 50:
                return new SemVignetteFilter();
            case 51:
                return new SemMosaicFilter();
            case 52:
                return new SemBitmapColorMaskFilter();
            case 53:
                return new SemBlendingFilter();
            case 54:
                return new SemBlurFilter();
            case 55:
                return new SemDropShadowFilter();
            case 56:
                return new SemTiltShiftFilter();
            case 57:
                return new SemGradientGaussianBlurFilter();
            case 64:
                return new SemKnittedFilter();
            default:
                return null;
        }
    }

    private static native void finalizer(long j);

    private String getMd5FromStr(String str) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"))).toString(32);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException();
        }
    }

    private static native void native_addAnimation(long j, ImageFilterAnimator imageFilterAnimator, int i, long j2, long j3);

    private static native long native_copy(long j);

    private static native long native_getSamplerNativeBitmap(long j, String str);

    private static native void native_getUniformMatrix(long j, String str, int i, int i2, float[] fArr);

    private static native void native_getUniformf(long j, String str, int i, int i2, float[] fArr);

    private static native void native_getUniformi(long j, String str, int i, int i2, int[] iArr);

    private static native void native_getUpdateMargin(long j, int[] iArr);

    private static native float native_getValue(long j, int i);

    private static native long native_init(int i);

    private static native void native_killAnimation(long j, int i);

    private static native void native_setFragmentShader(long j, String str, String str2);

    private static native void native_setSamplerBitmap(long j, String str, long j2);

    private static native void native_setSamplerFiltering(long j, String str, int i);

    private static native void native_setSamplerWrap(long j, String str, int i);

    private static native void native_setType(long j, int i);

    private static native void native_setUniformMatrix(long j, String str, int i, int i2, float[] fArr);

    private static native void native_setUniformf(long j, String str, int i, int i2, float[] fArr);

    private static native void native_setUniformfDirect(long j, String str, float[] fArr, int i);

    private static native void native_setUniformi(long j, String str, int i, int i2, int[] iArr);

    private static native void native_setUniformiDirect(long j, String str, int[] iArr, int i);

    private static native void native_setUpdateMargin(long j, int i, int i2, int i3, int i4);

    private static native void native_setValue(long j, int i, float f);

    private static native void native_setVertexShader(long j, String str, String str2);

    public int addAnimation(IAnimationListener iAnimationListener, long j, long j2, TimeInterpolator timeInterpolator) {
        final int i = mGlobalAnimationId + 1;
        mGlobalAnimationId = i;
        if (this.mNativeImageFilter != 0) {
            invalidateView();
            final IAnimationListener iAnimationListener2 = iAnimationListener;
            final TimeInterpolator timeInterpolator2 = timeInterpolator;
            final long j3 = j;
            final long j4 = j2;
            Runnable c01081 = new Runnable() {
                public void run() {
                    SemImageFilter.native_addAnimation(SemImageFilter.this.mNativeImageFilter, new ImageFilterAnimator(iAnimationListener2, timeInterpolator2), i, j3, j4);
                }
            };
            if (this.mView.isAttachedToWindow()) {
                c01081.run();
            } else {
                this.mView.postDelayed(c01081, 1);
            }
            return i;
        }
        throw new IllegalStateException("SemImageFilter has no native object.");
    }

    public SemImageFilter clone() throws CloneNotSupportedException {
        SemImageFilter semImageFilter = (SemImageFilter) super.clone();
        semImageFilter.mNativeImageFilter = native_copy(this.mNativeImageFilter);
        semImageFilter.setView(null);
        return semImageFilter;
    }

    protected void finalize() throws Throwable {
        try {
            finalizer(this.mNativeImageFilter);
        } finally {
            super.finalize();
        }
    }

    protected IImageFilterListener getListener() {
        return this.mListener;
    }

    protected long getNativeBitmap(String str) {
        return native_getSamplerNativeBitmap(this.mNativeImageFilter, str);
    }

    public long getNativeImageFilter() {
        return this.mNativeImageFilter;
    }

    public int getType() {
        return this instanceof SemGaussianBlurFilter ? 1 : this instanceof SemDirectionalBlurFilter ? 2 : this instanceof SemZoomBlurFilter ? 3 : this instanceof SemCosineBlurFilter ? 4 : this instanceof SemSgiBlurFilter ? 5 : this instanceof SemColorizeFilter ? 16 : this instanceof SemDesaturationFilter ? 17 : this instanceof SemColorClampFilter ? 18 : this instanceof SemDistortionFilter ? 49 : this instanceof SemVignetteFilter ? 50 : this instanceof SemMosaicFilter ? 51 : this instanceof SemBitmapColorMaskFilter ? 52 : this instanceof SemBlendingFilter ? 53 : this instanceof SemBlurFilter ? 54 : this instanceof SemDropShadowFilter ? 55 : this instanceof SemTiltShiftFilter ? 56 : this instanceof SemGradientGaussianBlurFilter ? 57 : this instanceof SemKnittedFilter ? 64 : 0;
    }

    protected void getUniformMatrix(String str, int i, int i2, float[] fArr) {
        if (i >= 2 && i <= 4 && i2 >= 2 && i2 <= 4) {
            native_getUniformMatrix(this.mNativeImageFilter, str, i, i2, fArr);
        }
    }

    protected void getUniformf(String str, int i, int i2, float[] fArr) {
        if (i > 0 && i <= 4 && i2 > 0) {
            native_getUniformf(this.mNativeImageFilter, str, i, i2, fArr);
        }
    }

    protected void getUniformi(String str, int i, int i2, int[] iArr) {
        if (i > 0 && i <= 4 && i2 > 0) {
            native_getUniformi(this.mNativeImageFilter, str, i, i2, iArr);
        }
    }

    protected void getUpdateMargin(int[] iArr) {
        native_getUpdateMargin(this.mNativeImageFilter, iArr);
    }

    protected float getValue(int i) {
        return native_getValue(this.mNativeImageFilter, i);
    }

    protected void invalidateView() {
        if (this.mView != null) {
            this.mView.invalidate();
        }
    }

    public void killAnimation(final int i) {
        if (this.mNativeImageFilter != 0) {
            invalidateView();
            Runnable c01092 = new Runnable() {
                public void run() {
                    SemImageFilter.native_killAnimation(SemImageFilter.this.mNativeImageFilter, i);
                }
            };
            if (this.mView.isAttachedToWindow()) {
                c01092.run();
                return;
            } else {
                this.mView.postDelayed(c01092, 1);
                return;
            }
        }
        throw new IllegalStateException("SemImageFilter has no native object.");
    }

    public void onAttachedToView() {
        if (this.mListener != null) {
            this.mListener.onAttachedToView();
        }
    }

    public void onViewSizeChanged() {
        if (this.mListener != null) {
            this.mListener.onViewSizeChanged();
        }
    }

    protected void setBitmap(String str, Bitmap bitmap) {
        invalidateView();
        native_setSamplerBitmap(this.mNativeImageFilter, str, bitmap == null ? 0 : bitmap.getNativePtr());
    }

    protected void setBitmapFiltering(String str, int i) {
        invalidateView();
        native_setSamplerFiltering(this.mNativeImageFilter, str, i);
    }

    protected void setBitmapWrap(String str, int i) {
        invalidateView();
        native_setSamplerWrap(this.mNativeImageFilter, str, i);
    }

    protected void setFragmentShader(String str) {
        invalidateView();
        native_setFragmentShader(this.mNativeImageFilter, str, getMd5FromStr(str));
    }

    protected void setListener(IImageFilterListener iImageFilterListener) {
        this.mListener = iImageFilterListener;
    }

    protected void setUniformMatrix(String str, int i, int i2, float[] fArr) {
        if (i >= 2 && i <= 4 && i2 >= 2 && i2 <= 4) {
            invalidateView();
            native_setUniformMatrix(this.mNativeImageFilter, str, i, i2, fArr);
        }
    }

    protected void setUniformf(String str, int i, int i2, float[] fArr) {
        if (i > 0 && i <= 4 && i2 > 0) {
            invalidateView();
            native_setUniformf(this.mNativeImageFilter, str, i, i2, fArr);
        }
    }

    protected void setUniformi(String str, int i, int i2, int[] iArr) {
        if (i > 0 && i <= 4 && i2 > 0) {
            invalidateView();
            native_setUniformi(this.mNativeImageFilter, str, i, i2, iArr);
        }
    }

    protected void setUpdateMargin(int i, int i2, int i3, int i4) {
        invalidateView();
        native_setUpdateMargin(this.mNativeImageFilter, i, i2, i3, i4);
    }

    public void setValue(int i, float f) {
        invalidateView();
        native_setValue(this.mNativeImageFilter, i, f);
    }

    protected void setVertexShader(String str) {
        invalidateView();
        native_setVertexShader(this.mNativeImageFilter, str, getMd5FromStr(str));
    }

    public void setView(View view) {
        this.mView = view;
    }
}
