package com.samsung.android.graphics.spr;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Gravity;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.graphics.spr.animation.SprDrawableAnimation;
import com.samsung.android.graphics.spr.animation.SprDrawableAnimationFrame;
import com.samsung.android.graphics.spr.animation.SprDrawableAnimationValue;
import com.samsung.android.graphics.spr.cache.SprCacheManager;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeFill;
import com.samsung.android.graphics.spr.document.attribute.impl.SprGradientBase;
import com.samsung.android.graphics.spr.document.attribute.impl.SprLinearGradient;
import com.samsung.android.graphics.spr.document.debug.SprDebug;
import com.samsung.android.graphics.spr.document.fileAttribute.SprFileAttributeNinePatch;
import com.samsung.android.graphics.spr.document.shape.SprObjectBase;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeRectangle;
import com.samsung.android.transcode.core.Encode.BitRate;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class SemPathRenderingDrawable extends Drawable implements Animatable {
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;
    private static final String NA_NAME = "n/a";
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static int mBitmapDrawable_alpha = 0;
    private static int mBitmapDrawable_autoMirrored = 0;
    private static int mBitmapDrawable_gravity = 0;
    private static int mBitmapDrawable_src = 0;
    private static int mBitmapDrawable_tileMode = 0;
    private static int mBitmapDrawable_tileModeX = 0;
    private static int mBitmapDrawable_tileModeY = 0;
    private static int mBitmapDrawable_tint = 0;
    private static int mBitmapDrawable_tintMode = 0;
    private static final Method mCanApplyTheme;
    private static final Method mExtractThemeAttrs;
    private static final Method mGetLayoutDirection;
    private static final Method mObtainForTheme;
    private static final Method mParseTintMode;
    private static final Method mResolveAttributes;
    private static int[] mStyleableBitmapDrawable = null;
    private static final Method mUpdateTintFilter;
    private static final int mVersion = 151023;
    private Bitmap mAnimationBitmap;
    private Bitmap mCacheBitmap;
    private int mCacheDensityDpi;
    protected SprDocument mDocument;
    private Rect mDstRect;
    private Matrix mIdentityMatrix;
    private Matrix mMirrorMatrix;
    private boolean mMutated;
    private SprDrawableAnimation mSprAnimation;
    private SprState mState;
    private PorterDuffColorFilter mTintFilter;
    private final float[] mTmpFloats;
    private final Matrix mTmpMatrix;

    static final class SprState extends ConstantState {
        private boolean mAutoMirrored = false;
        private final Paint mBitmapPaint;
        private SprCacheManager mCacheManager = null;
        private int mChangingConfigurations;
        private int mDensityDpi = 0;
        private SprDocument mDocument = null;
        private int mGravity = 119;
        private SprFileAttributeNinePatch mMultiNinePatch = null;
        private boolean mNinePatch = false;
        private Bitmap mNinePatchBitmap = null;
        private NinePatch mNinePatchRenderer = null;
        private boolean mRebuildShader = false;
        private int[] mThemeAttrs = null;
        private TileMode mTileModeX = null;
        private TileMode mTileModeY = null;
        private ColorStateList mTint = null;
        private Mode mTintMode = Mode.SRC_IN;

        SprState(SprState sprState) {
            this.mDocument = sprState.mDocument;
            this.mThemeAttrs = sprState.mThemeAttrs;
            this.mNinePatch = sprState.mNinePatch;
            this.mBitmapPaint = new Paint(sprState.mBitmapPaint);
            if (sprState.mNinePatch && sprState.mNinePatchRenderer == null) {
                sprState.createNinePatchRenderer();
            }
            this.mCacheManager = sprState.mCacheManager;
            this.mNinePatchBitmap = sprState.mNinePatchBitmap;
            this.mNinePatchRenderer = sprState.mNinePatchRenderer;
            this.mMultiNinePatch = sprState.mMultiNinePatch;
            this.mTint = sprState.mTint;
            this.mTintMode = sprState.mTintMode;
            this.mAutoMirrored = sprState.mAutoMirrored;
            this.mGravity = sprState.mGravity;
            this.mChangingConfigurations = sprState.mChangingConfigurations;
            this.mRebuildShader = sprState.mRebuildShader;
            this.mTileModeX = sprState.mTileModeX;
            this.mTileModeY = sprState.mTileModeY;
            this.mDensityDpi = sprState.mDensityDpi;
        }

        SprState(SprDocument sprDocument) {
            setDocument(sprDocument);
            this.mBitmapPaint = new Paint();
            this.mBitmapPaint.setFilterBitmap(true);
        }

        private void createNinePatchRenderer() {
            if (this.mNinePatchRenderer == null && this.mDocument != null) {
                if (!this.mDocument.isPredraw()) {
                    this.mDocument.preDraw(0);
                }
                int intrinsicWidth = getIntrinsicWidth();
                int intrinsicHeight = getIntrinsicHeight();
                this.mNinePatchBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
                if (this.mNinePatchBitmap != null) {
                    this.mDocument.draw(new Canvas(this.mNinePatchBitmap), intrinsicWidth, intrinsicHeight, 0, this.mDensityDpi);
                }
                if (this.mNinePatch && this.mMultiNinePatch == null) {
                    float densityScale = getDensityScale();
                    int round = Math.round(this.mDocument.mNinePatchLeft * densityScale);
                    int round2 = Math.round(this.mDocument.mNinePatchTop * densityScale);
                    int round3 = intrinsicWidth - Math.round(this.mDocument.mNinePatchRight * densityScale);
                    int round4 = intrinsicHeight - Math.round(this.mDocument.mNinePatchBottom * densityScale);
                    if (round3 <= round) {
                        round3 = round + 1;
                    }
                    if (round4 <= round2) {
                        round4 = round2 + 1;
                    }
                    this.mNinePatchRenderer = new NinePatch(this.mNinePatchBitmap, getNinePatchChunk(round, round2, round3, round4).array());
                    return;
                }
                this.mNinePatchRenderer = new NinePatch(this.mNinePatchBitmap, getNinePatchChunk(this.mMultiNinePatch).array());
            }
        }

        private ByteBuffer getNinePatchChunk(int i, int i2, int i3, int i4) {
            ByteBuffer order = ByteBuffer.allocate(84).order(ByteOrder.nativeOrder());
            order.put((byte) 1);
            order.put((byte) 2);
            order.put((byte) 2);
            order.put((byte) 9);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(i);
            order.putInt(i3);
            order.putInt(i2);
            order.putInt(i4);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            return order;
        }

        private ByteBuffer getNinePatchChunk(SprFileAttributeNinePatch sprFileAttributeNinePatch) {
            int i;
            float densityScale = getDensityScale();
            int i2 = 0;
            int i3 = 0;
            int[] iArr = new int[sprFileAttributeNinePatch.xSize];
            int[] iArr2 = new int[sprFileAttributeNinePatch.xSize];
            int[] iArr3 = new int[sprFileAttributeNinePatch.ySize];
            int[] iArr4 = new int[sprFileAttributeNinePatch.ySize];
            int i4 = -1;
            for (i = 0; i < sprFileAttributeNinePatch.xSize; i++) {
                int round = Math.round(sprFileAttributeNinePatch.xStart[i] * densityScale);
                int round2 = Math.round(sprFileAttributeNinePatch.xEnd[i] * densityScale);
                if (round2 <= round) {
                    round2 = round + 1;
                }
                if (round <= i4) {
                    iArr2[i2 - 1] = round2;
                } else {
                    iArr[i2] = round;
                    iArr2[i2] = round2;
                    i2++;
                }
                i4 = round2;
            }
            i4 = -1;
            for (i = 0; i < sprFileAttributeNinePatch.ySize; i++) {
                round = Math.round(sprFileAttributeNinePatch.yStart[i] * densityScale);
                round2 = Math.round(sprFileAttributeNinePatch.yEnd[i] * densityScale);
                if (round2 <= round) {
                    round2 = round + 1;
                }
                if (round <= i4) {
                    iArr4[i3 - 1] = round2;
                } else {
                    iArr3[i3] = round;
                    iArr4[i3] = round2;
                    i3++;
                }
                i4 = round2;
            }
            int i5 = ((i2 * 2) + 1) * ((i3 * 2) + 1);
            ByteBuffer order = ByteBuffer.allocate((((i2 * 8) + 42) + (i3 * 8)) + (i5 * 4)).order(ByteOrder.nativeOrder());
            order.put((byte) 1);
            order.put((byte) (sprFileAttributeNinePatch.xSize * 2));
            order.put((byte) (sprFileAttributeNinePatch.ySize * 2));
            order.put((byte) i5);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            for (i = 0; i < i2; i++) {
                order.putInt(iArr[i]);
                order.putInt(iArr2[i]);
            }
            for (i = 0; i < i3; i++) {
                order.putInt(iArr3[i]);
                order.putInt(iArr4[i]);
            }
            for (i = 0; i < i5; i++) {
                order.putInt(1);
            }
            return order;
        }

        public boolean canApplyTheme() {
            boolean booleanValue;
            try {
                booleanValue = ((Boolean) SemPathRenderingDrawable.mCanApplyTheme.invoke(this.mTint, new Object[0])).booleanValue();
            } catch (Exception e) {
                booleanValue = false;
            }
            return this.mThemeAttrs == null ? this.mTint != null ? booleanValue : false : true;
        }

        public int getChangingConfigurations() {
            if (VERSION.SDK_INT < 23) {
                return this.mChangingConfigurations;
            }
            return (this.mTint != null ? this.mTint.getChangingConfigurations() : 0) | this.mChangingConfigurations;
        }

        public float getDensityScale() {
            return this.mDocument == null ? (((float) this.mDensityDpi) / 160.0f) / 3.0f : (((float) this.mDensityDpi) / 160.0f) / this.mDocument.mDensity;
        }

        public int getIntrinsicHeight() {
            float densityScale = getDensityScale();
            return this.mDocument != null ? Math.round(this.mDocument.mBottom * densityScale) - Math.round(this.mDocument.mTop * densityScale) : 0;
        }

        public int getIntrinsicWidth() {
            float densityScale = getDensityScale();
            return this.mDocument != null ? Math.round(this.mDocument.mRight * densityScale) - Math.round(this.mDocument.mLeft * densityScale) : 0;
        }

        public Drawable newDrawable() {
            return new SemPathRenderingDrawable(this, null);
        }

        public Drawable newDrawable(Resources resources) {
            return new SemPathRenderingDrawable(this, resources);
        }

        public void setDocument(SprDocument sprDocument) {
            if (sprDocument != null) {
                if (this.mDocument == null || !(this.mDocument.mName == null || this.mDocument.mName.equals(sprDocument.mName))) {
                    this.mDocument = sprDocument;
                    boolean z = (this.mDocument.mNinePatchLeft == 0.0f && this.mDocument.mNinePatchTop == 0.0f && this.mDocument.mNinePatchRight == 0.0f) ? this.mDocument.mNinePatchBottom != 0.0f : true;
                    this.mNinePatch = z;
                    for (int i = 0; i < this.mDocument.getFileAttributeSize(); i++) {
                        SprFileAttributeNinePatch sprFileAttributeNinePatch = (SprFileAttributeNinePatch) this.mDocument.getFileAttribute(i);
                        if (sprFileAttributeNinePatch != null && sprFileAttributeNinePatch.mType == (byte) 1) {
                            this.mNinePatch = true;
                            this.mMultiNinePatch = sprFileAttributeNinePatch;
                            break;
                        }
                    }
                    this.mDensityDpi = SemPathRenderingDrawable.getDeviceDensityDpi(null);
                    if (this.mCacheManager != null) {
                        if (SprDebug.IsDebug) {
                            this.mCacheManager.printDebug();
                            new Exception().printStackTrace();
                        }
                        this.mCacheManager = null;
                    }
                    this.mCacheManager = new SprCacheManager(this.mDocument.mName, this.mDocument.hashCode());
                }
            }
        }
    }

    static {
        Method method = null;
        try {
            method = Drawable.class.getDeclaredMethod("updateTintFilter", new Class[]{PorterDuffColorFilter.class, ColorStateList.class, Mode.class});
        } catch (Exception e) {
        }
        mUpdateTintFilter = method;
        try {
            method = Drawable.class.getMethod("parseTintMode", new Class[]{Integer.TYPE, Mode.class});
        } catch (Exception e2) {
        }
        mParseTintMode = method;
        try {
            method = Drawable.class.getMethod("getLayoutDirection", new Class[0]);
        } catch (Exception e3) {
        }
        mGetLayoutDirection = method;
        try {
            method = TypedArray.class.getDeclaredMethod("extractThemeAttrs", new Class[0]);
        } catch (Exception e4) {
        }
        mExtractThemeAttrs = method;
        try {
            method = Theme.class.getDeclaredMethod("resolveAttributes", new Class[]{int[].class, int[].class});
        } catch (Exception e5) {
        }
        mResolveAttributes = method;
        try {
            method = ColorStateList.class.getDeclaredMethod("obtainForTheme", new Class[]{Theme.class});
        } catch (Exception e6) {
        }
        mObtainForTheme = method;
        try {
            method = ColorStateList.class.getDeclaredMethod("canApplyTheme", new Class[0]);
        } catch (Exception e7) {
        }
        mCanApplyTheme = method;
        try {
            Class cls = Class.forName("com.android.internal.R$styleable");
            mStyleableBitmapDrawable = (int[]) cls.getDeclaredField("BitmapDrawable").get(null);
            mBitmapDrawable_src = cls.getDeclaredField("BitmapDrawable_src").getInt(null);
            mBitmapDrawable_alpha = cls.getDeclaredField("BitmapDrawable_alpha").getInt(null);
            mBitmapDrawable_autoMirrored = cls.getDeclaredField("BitmapDrawable_autoMirrored").getInt(null);
            mBitmapDrawable_gravity = cls.getDeclaredField("BitmapDrawable_gravity").getInt(null);
            mBitmapDrawable_tileMode = cls.getDeclaredField("BitmapDrawable_tileMode").getInt(null);
            mBitmapDrawable_tileModeX = cls.getDeclaredField("BitmapDrawable_tileModeX").getInt(null);
            mBitmapDrawable_tileModeY = cls.getDeclaredField("BitmapDrawable_tileModeY").getInt(null);
            mBitmapDrawable_tint = cls.getDeclaredField("BitmapDrawable_tint").getInt(null);
            mBitmapDrawable_tintMode = cls.getDeclaredField("BitmapDrawable_tintMode").getInt(null);
        } catch (Exception e8) {
        }
    }

    public SemPathRenderingDrawable() {
        this.mState = null;
        this.mMutated = false;
        this.mCacheBitmap = null;
        this.mCacheDensityDpi = 0;
        this.mDocument = null;
        this.mTintFilter = null;
        this.mSprAnimation = null;
        this.mAnimationBitmap = null;
        this.mDstRect = new Rect();
        this.mMirrorMatrix = null;
        this.mIdentityMatrix = null;
        this.mTmpMatrix = new Matrix();
        this.mTmpFloats = new float[9];
        this.mState = new SprState(this.mDocument);
    }

    public SemPathRenderingDrawable(SprState sprState, Resources resources) {
        this.mState = null;
        this.mMutated = false;
        this.mCacheBitmap = null;
        this.mCacheDensityDpi = 0;
        this.mDocument = null;
        this.mTintFilter = null;
        this.mSprAnimation = null;
        this.mAnimationBitmap = null;
        this.mDstRect = new Rect();
        this.mMirrorMatrix = null;
        this.mIdentityMatrix = null;
        this.mTmpMatrix = new Matrix();
        this.mTmpFloats = new float[9];
        this.mState = sprState;
        this.mDocument = this.mState.mDocument;
        if (this.mDocument != null) {
            float densityScale = this.mState.getDensityScale();
            super.setBounds(Math.round(this.mDocument.mLeft * densityScale), Math.round(this.mDocument.mTop * densityScale), Math.round(this.mDocument.mRight * densityScale), Math.round(this.mDocument.mBottom * densityScale));
            this.mTintFilter = updateTintFilterInternal(this.mTintFilter, sprState.mTint, sprState.mTintMode);
        }
        if (resources != null) {
            updateLocalState(resources);
        }
    }

    public SemPathRenderingDrawable(SprDocument sprDocument) {
        this.mState = null;
        this.mMutated = false;
        this.mCacheBitmap = null;
        this.mCacheDensityDpi = 0;
        this.mDocument = null;
        this.mTintFilter = null;
        this.mSprAnimation = null;
        this.mAnimationBitmap = null;
        this.mDstRect = new Rect();
        this.mMirrorMatrix = null;
        this.mIdentityMatrix = null;
        this.mTmpMatrix = new Matrix();
        this.mTmpFloats = new float[9];
        this.mState = new SprState(sprDocument);
        this.mDocument = this.mState.mDocument;
        if (this.mDocument != null) {
            float densityScale = this.mState.getDensityScale();
            super.setBounds(Math.round(this.mDocument.mLeft * densityScale), Math.round(this.mDocument.mTop * densityScale), Math.round(this.mDocument.mRight * densityScale), Math.round(this.mDocument.mBottom * densityScale));
        }
    }

    public static SemPathRenderingDrawable createFromPathName(String str) {
        Throwable e;
        FileInputStream fileInputStream = null;
        try {
            InputStream fileInputStream2 = new FileInputStream(str);
            try {
                SprDocument createFromStreamInternal = createFromStreamInternal(str, fileInputStream2);
                fileInputStream2.close();
                return new SemPathRenderingDrawable(createFromStreamInternal);
            } catch (Exception e2) {
                e = e2;
                InputStream inputStream = fileInputStream2;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e3) {
                        e3.printStackTrace();
                    }
                }
                e.printStackTrace();
                return getErrorDrawable(str);
            }
        } catch (Exception e4) {
            e = e4;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            e.printStackTrace();
            return getErrorDrawable(str);
        }
    }

    public static SemPathRenderingDrawable createFromResourceStream(Resources resources, int i) {
        InputStream inputStream = null;
        try {
            inputStream = resources.openRawResource(i);
            SprDocument createFromStreamInternal = createFromStreamInternal(resources.getString(i), inputStream);
            inputStream.close();
            SemPathRenderingDrawable semPathRenderingDrawable = new SemPathRenderingDrawable(createFromStreamInternal);
            semPathRenderingDrawable.updateLocalState(resources);
            return semPathRenderingDrawable;
        } catch (Throwable e) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            e.printStackTrace();
            return getErrorDrawable(resources.getString(i));
        }
    }

    @Deprecated
    public static SemPathRenderingDrawable createFromStream(InputStream inputStream) {
        try {
            return new SemPathRenderingDrawable(createFromStreamInternal(NA_NAME, inputStream));
        } catch (Throwable e) {
            e.printStackTrace();
            return getErrorDrawable(NA_NAME);
        }
    }

    public static SemPathRenderingDrawable createFromStream(String str, InputStream inputStream) throws IOException {
        try {
            return new SemPathRenderingDrawable(createFromStreamInternal(str, inputStream));
        } catch (Throwable e) {
            e.printStackTrace();
            return getErrorDrawable(str);
        }
    }

    private static SprDocument createFromStreamInternal(String str, InputStream inputStream) throws IOException {
        InputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] bArr = new byte[3];
        if (str == null) {
            str = NA_NAME;
        }
        bufferedInputStream.mark(3);
        if (bufferedInputStream.read(bArr) < 3) {
            bufferedInputStream.close();
            throw new IOException("file is too short");
        }
        bufferedInputStream.reset();
        if ((bArr[0] == (byte) 83 && bArr[1] == (byte) 86 && bArr[2] == (byte) 70) || (bArr[0] == (byte) 83 && bArr[1] == (byte) 80 && bArr[2] == (byte) 82)) {
            return new SprDocument(str, bufferedInputStream);
        }
        try {
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(bufferedInputStream, null);
            return new SprDocument(str, newPullParser);
        } catch (Throwable e) {
            throw new IOException(e.getCause());
        }
    }

    private static int getDeviceDensityDpi(Resources resources) {
        return resources == null ? Resources.getSystem().getDisplayMetrics().densityDpi : resources.getDisplayMetrics().densityDpi;
    }

    private static SemPathRenderingDrawable getErrorDrawable(String str) {
        SprDocument sprDocument = new SprDocument(str, 0.0f, 0.0f, (float) BitRate.MIN_VIDEO_VGA_BITRATE, (float) 275);
        SprObjectBase sprObjectShapeRectangle = new SprObjectShapeRectangle(0.0f, 0.0f, (float) 50, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 200, 200, 200)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 50, 0.0f, (float) 100, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, -256));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 100, 0.0f, (float) 150, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 255, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 150, 0.0f, (float) 200, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 255, 0)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 200, 0.0f, (float) 250, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 255, 0, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 250, 0.0f, (float) 300, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 255, 0, 0)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 300, 0.0f, (float) BitRate.MIN_VIDEO_VGA_BITRATE, (float) 200);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 0, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle(0.0f, (float) 200, (float) 50, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 0, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 50, (float) 200, (float) 100, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 0, 0)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 100, (float) 200, (float) 150, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 255, 0, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 150, (float) 200, (float) 200, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 0, 0)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 200, (float) 200, (float) 250, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 255, 255)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 250, (float) 200, (float) 300, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 0, 0, 0)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle((float) 300, (float) 200, (float) BitRate.MIN_VIDEO_VGA_BITRATE, (float) 225);
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 1, Color.argb(255, 200, 200, 200)));
        sprDocument.appendObject(sprObjectShapeRectangle);
        sprObjectShapeRectangle = new SprObjectShapeRectangle(0.0f, (float) 225, (float) BitRate.MIN_VIDEO_VGA_BITRATE, (float) 275);
        SprGradientBase sprLinearGradient = new SprLinearGradient();
        sprLinearGradient.spreadMode = (byte) 1;
        sprLinearGradient.x1 = 0.0f;
        sprLinearGradient.y1 = (float) 225;
        sprLinearGradient.x2 = (float) BitRate.MIN_VIDEO_VGA_BITRATE;
        sprLinearGradient.y2 = (float) 225;
        sprLinearGradient.colors = new int[]{-1, -16777216};
        sprLinearGradient.positions = new float[]{0.0f, 1.0f};
        sprLinearGradient.updateGradient();
        sprObjectShapeRectangle.appendAttribute(new SprAttributeFill((byte) 3, sprLinearGradient));
        sprDocument.appendObject(sprObjectShapeRectangle);
        return new SemPathRenderingDrawable(sprDocument) {
            public void draw(Canvas canvas) {
                super.draw(canvas);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setTextSize(20.0f);
                paint.setStyle(Style.STROKE);
                paint.setColor(-16777216);
                paint.setStrokeWidth(4.0f);
                Paint paint2 = new Paint();
                paint2.setAntiAlias(true);
                paint2.setTextSize(20.0f);
                paint2.setStyle(Style.FILL);
                paint2.setColor(-1);
                canvas.drawText(this.mDocument.mName, 5.0f, 40.0f, paint);
                canvas.drawText(this.mDocument.mName, 5.0f, 40.0f, paint2);
            }
        };
    }

    public static int getVersion() {
        return mVersion;
    }

    private boolean needMirroring() {
        try {
            boolean z = isAutoMirrored() && ((Integer) mGetLayoutDirection.invoke(this, new Object[0])).intValue() == 1;
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    static TypedArray obtainAttributes(Resources resources, Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }

    private static TileMode parseTileMode(int i) {
        switch (i) {
            case 0:
                return TileMode.CLAMP;
            case 1:
                return TileMode.REPEAT;
            case 2:
                return TileMode.MIRROR;
            default:
                return null;
        }
    }

    private void updateCachedBitmap(int i, int i2, int i3) {
        if ((this.mCacheBitmap == null || this.mCacheBitmap.getWidth() != i || this.mCacheBitmap.getHeight() != i2 || this.mCacheDensityDpi != i3) && this.mDocument != null) {
            if (this.mCacheBitmap != null) {
                this.mState.mCacheManager.unlock(this.mCacheBitmap);
                this.mCacheBitmap = null;
                this.mCacheDensityDpi = 0;
            }
            this.mCacheBitmap = this.mState.mCacheManager.getCache(i, i2, i3);
            this.mCacheDensityDpi = i3;
            if (this.mCacheBitmap == null) {
                if (!this.mDocument.isPredraw()) {
                    this.mDocument.preDraw(0);
                }
                this.mCacheBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                if (this.mCacheBitmap != null) {
                    Canvas canvas = new Canvas(this.mCacheBitmap);
                    this.mDocument.draw(canvas, i, i2, 0, this.mState.mDensityDpi);
                    this.mState.mCacheManager.addCache(this.mCacheBitmap, this.mCacheDensityDpi);
                }
            }
            this.mState.mCacheManager.lock(this.mCacheBitmap);
        }
    }

    private void updateDensity(Resources resources) {
        int deviceDensityDpi = getDeviceDensityDpi(resources);
        if (this.mState.mDensityDpi != deviceDensityDpi) {
            this.mState.mDensityDpi = deviceDensityDpi;
            if (this.mCacheBitmap != null) {
                this.mState.mCacheManager.unlock(this.mCacheBitmap);
                this.mCacheBitmap = null;
                this.mCacheDensityDpi = 0;
            }
            this.mState.mNinePatchRenderer = null;
            this.mState.mNinePatchBitmap = null;
        }
    }

    private void updateDstRectAndInsetsIfDirty() {
        if (this.mState.mTileModeX == null && this.mState.mTileModeY == null) {
            try {
                Gravity.apply(this.mState.mGravity, getIntrinsicWidth(), getIntrinsicHeight(), getBounds(), this.mDstRect, ((Integer) mGetLayoutDirection.invoke(this, new Object[0])).intValue());
                return;
            } catch (Exception e) {
                return;
            }
        }
        copyBounds(this.mDstRect);
    }

    private void updateLocalState(Resources resources) {
        setTintList(this.mState.mTint);
        updateDensity(resources);
        if (this.mDocument != null) {
            float densityScale = this.mState.getDensityScale();
            super.setBounds(Math.round(this.mDocument.mLeft * densityScale), Math.round(this.mDocument.mTop * densityScale), Math.round(this.mDocument.mRight * densityScale), Math.round(this.mDocument.mBottom * densityScale));
        }
    }

    private void updateStateFromTypedArray(TypedArray typedArray) throws XmlPullParserException, IOException {
        Resources resources = typedArray.getResources();
        SprState sprState = this.mState;
        sprState.mChangingConfigurations = sprState.mChangingConfigurations | typedArray.getChangingConfigurations();
        try {
            sprState.mThemeAttrs = (int[]) mExtractThemeAttrs.invoke(typedArray, new Object[0]);
        } catch (Exception e) {
            sprState.mThemeAttrs = null;
        }
        int resourceId = typedArray.getResourceId(mBitmapDrawable_src, 0);
        if (resourceId != 0) {
            InputStream inputStream = null;
            try {
                inputStream = resources.openRawResource(resourceId);
                this.mState.setDocument(createFromStreamInternal(resources.getString(resourceId), inputStream));
                this.mDocument = this.mState.mDocument;
            } catch (Throwable e2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e3) {
                        e3.printStackTrace();
                    }
                }
                throw new IOException(e2);
            }
        }
        if (typedArray.getInt(mBitmapDrawable_tintMode, -1) != -1) {
            try {
                this.mState.mTintMode = (Mode) mParseTintMode.invoke(null, new Object[]{Integer.valueOf(r12), Mode.SRC_IN});
            } catch (Exception e4) {
                this.mState.mTintMode = Mode.SRC_IN;
            }
        }
        ColorStateList colorStateList = typedArray.getColorStateList(mBitmapDrawable_tint);
        if (colorStateList != null) {
            this.mState.mTint = colorStateList;
        }
        this.mState.mGravity = typedArray.getInt(mBitmapDrawable_gravity, 119);
        this.mState.mAutoMirrored = typedArray.getBoolean(mBitmapDrawable_autoMirrored, this.mState.mAutoMirrored);
        this.mState.mBitmapPaint.setAlpha((int) (typedArray.getFloat(mBitmapDrawable_alpha, 1.0f) * 255.0f));
        int i = typedArray.getInt(mBitmapDrawable_tileMode, -2);
        if (i != -2) {
            TileMode parseTileMode = parseTileMode(i);
            setTileModeXY(parseTileMode, parseTileMode);
        }
        int i2 = typedArray.getInt(mBitmapDrawable_tileModeX, -2);
        if (i2 != -2) {
            setTileModeX(parseTileMode(i2));
        }
        int i3 = typedArray.getInt(mBitmapDrawable_tileModeY, -2);
        if (i3 != -2) {
            setTileModeY(parseTileMode(i3));
        }
        updateDensity(resources);
    }

    private PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter porterDuffColorFilter, ColorStateList colorStateList, Mode mode) {
        return (colorStateList == null || mode == null) ? null : new PorterDuffColorFilter(colorStateList.getColorForState(getState(), 0), mode);
    }

    public void applyTheme(Theme theme) {
        boolean booleanValue;
        super.applyTheme(theme);
        SprState sprState = this.mState;
        if (sprState != null) {
            if (sprState.mThemeAttrs != null) {
                TypedArray typedArray = null;
                try {
                    typedArray = (TypedArray) mResolveAttributes.invoke(theme, new Object[]{sprState.mThemeAttrs, mStyleableBitmapDrawable});
                    updateStateFromTypedArray(typedArray);
                    if (typedArray != null) {
                        typedArray.recycle();
                    }
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                } catch (Exception e2) {
                    if (typedArray != null) {
                        typedArray.recycle();
                    }
                } catch (Throwable th) {
                    if (typedArray != null) {
                        typedArray.recycle();
                    }
                }
            }
            try {
                booleanValue = ((Boolean) mCanApplyTheme.invoke(sprState.mTint, new Object[0])).booleanValue();
            } catch (Exception e3) {
                booleanValue = false;
            }
            if (sprState.mTint != null && r2) {
                try {
                    sprState.mTint = (ColorStateList) mObtainForTheme.invoke(sprState.mTint, new Object[]{theme});
                } catch (Exception e4) {
                }
            }
            updateLocalState(theme.getResources());
        }
    }

    public boolean canApplyTheme() {
        return this.mState != null ? this.mState.canApplyTheme() : false;
    }

    public void draw(Canvas canvas) {
        if (this.mDstRect.width() > 0 && this.mDstRect.height() > 0 && this.mDocument != null) {
            int height;
            int min;
            if (this.mState.mTileModeX == null && this.mState.mTileModeY == null) {
                canvas.getMatrix(this.mTmpMatrix);
                this.mTmpMatrix.getValues(this.mTmpFloats);
                float abs = Math.abs(this.mTmpFloats[0]);
                float abs2 = Math.abs(this.mTmpFloats[4]);
                if (this.mTmpFloats[1] == 0.0f && this.mTmpFloats[3] == 0.0f) {
                    height = (int) (((float) this.mDstRect.height()) * abs2);
                    min = Math.min(2048, (int) (((float) this.mDstRect.width()) * abs));
                    height = Math.min(2048, height);
                } else if (this.mCacheBitmap != null) {
                    min = this.mCacheBitmap.getWidth();
                    height = this.mCacheBitmap.getHeight();
                } else {
                    min = this.mDstRect.width();
                    height = this.mDstRect.height();
                }
            } else {
                min = getIntrinsicWidth();
                height = getIntrinsicHeight();
            }
            if (min > 0 && height > 0) {
                Object obj;
                boolean isRunning = isRunning();
                Paint -get1 = this.mState.mBitmapPaint;
                synchronized (this.mState) {
                    if (this.mState.mNinePatch) {
                        if (this.mState.mNinePatchRenderer == null) {
                            this.mState.createNinePatchRenderer();
                        }
                    } else if (isRunning) {
                        Canvas canvas2;
                        int animationIndex = this.mSprAnimation.getAnimationIndex();
                        this.mDocument.preDraw(animationIndex);
                        if (this.mAnimationBitmap != null && this.mAnimationBitmap.getWidth() == min) {
                            if (this.mAnimationBitmap.getHeight() == height) {
                                canvas2 = new Canvas(this.mAnimationBitmap);
                                canvas2.drawColor(0, Mode.CLEAR);
                                this.mDocument.draw(canvas2, min, height, animationIndex, this.mState.mDensityDpi);
                            }
                        }
                        this.mAnimationBitmap = Bitmap.createBitmap(min, height, Config.ARGB_8888);
                        canvas2 = new Canvas(this.mAnimationBitmap);
                        this.mDocument.draw(canvas2, min, height, animationIndex, this.mState.mDensityDpi);
                    } else {
                        updateCachedBitmap(min, height, this.mState.mDensityDpi);
                    }
                    if (this.mState.mRebuildShader || isRunning) {
                        if (this.mState.mTileModeX == null && this.mState.mTileModeY == null) {
                            -get1.setShader(null);
                        } else {
                            TileMode -get11 = this.mState.mTileModeX;
                            TileMode -get12 = this.mState.mTileModeY;
                            Bitmap bitmap = this.mAnimationBitmap != null ? this.mAnimationBitmap : this.mCacheBitmap;
                            if (-get11 == null) {
                                -get11 = TileMode.CLAMP;
                            }
                            if (-get12 == null) {
                                -get12 = TileMode.CLAMP;
                            }
                            -get1.setShader(new BitmapShader(bitmap, -get11, -get12));
                        }
                        this.mState.mRebuildShader = false;
                    }
                }
                if (this.mTintFilter == null || -get1.getColorFilter() != null) {
                    obj = null;
                } else {
                    -get1.setColorFilter(this.mTintFilter);
                    obj = 1;
                }
                Shader shader = -get1.getShader();
                boolean needMirroring = needMirroring();
                if (shader != null) {
                    if (needMirroring) {
                        if (this.mMirrorMatrix == null) {
                            this.mMirrorMatrix = new Matrix();
                        }
                        this.mMirrorMatrix.setTranslate((float) (this.mDstRect.right - this.mDstRect.left), 0.0f);
                        this.mMirrorMatrix.preScale(-1.0f, 1.0f);
                        shader.setLocalMatrix(this.mMirrorMatrix);
                        -get1.setShader(shader);
                    } else if (this.mMirrorMatrix != null) {
                        this.mMirrorMatrix = null;
                        if (this.mIdentityMatrix == null) {
                            this.mIdentityMatrix = new Matrix();
                        }
                        shader.setLocalMatrix(this.mIdentityMatrix);
                        -get1.setShader(shader);
                    }
                    canvas.drawRect(this.mDstRect, -get1);
                } else if (!this.mState.mNinePatch) {
                    if (needMirroring) {
                        canvas.save();
                        canvas.translate((float) (this.mDstRect.right - this.mDstRect.left), 0.0f);
                        canvas.scale(-1.0f, 1.0f);
                    }
                    canvas.drawBitmap(this.mAnimationBitmap != null ? this.mAnimationBitmap : this.mCacheBitmap, null, this.mDstRect, -get1);
                    if (isRunning) {
                        this.mSprAnimation.update();
                    }
                    if (needMirroring) {
                        canvas.restore();
                    }
                } else if (this.mState.mNinePatchRenderer != null) {
                    this.mState.mNinePatchRenderer.draw(canvas, this.mDstRect, -get1);
                }
                if (obj != null) {
                    -get1.setColorFilter(null);
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        stop();
        if (this.mCacheBitmap != null) {
            this.mState.mCacheManager.unlock(this.mCacheBitmap);
            this.mCacheBitmap = null;
            this.mCacheDensityDpi = 0;
        }
        this.mState = null;
        this.mDocument = null;
        this.mTintFilter = null;
        this.mSprAnimation = null;
        this.mAnimationBitmap = null;
        this.mDstRect = null;
    }

    public int getAlpha() {
        return this.mState.mBitmapPaint.getAlpha();
    }

    public Bitmap getBitmap() {
        updateCachedBitmap(getIntrinsicWidth(), getIntrinsicHeight(), this.mState.mDensityDpi);
        return this.mCacheBitmap;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mState.getChangingConfigurations();
    }

    public ConstantState getConstantState() {
        SprState sprState = this.mState;
        sprState.mChangingConfigurations = sprState.mChangingConfigurations | getChangingConfigurations();
        return this.mState;
    }

    public SprDocument getDocument() {
        return this.mDocument;
    }

    public int getGravity() {
        return this.mState.mGravity;
    }

    public int getIntrinsicHeight() {
        return this.mState.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mState.getIntrinsicWidth();
    }

    public int getOpacity() {
        int i = -3;
        if (this.mState.mGravity != 119) {
            return -3;
        }
        Bitmap bitmap = this.mCacheBitmap;
        if (!(bitmap == null || bitmap.hasAlpha() || this.mState.mBitmapPaint.getAlpha() < 255)) {
            i = -1;
        }
        return i;
    }

    public boolean getPadding(Rect rect) {
        boolean z = false;
        if (this.mDocument == null) {
            rect.set(0, 0, 0, 0);
            return false;
        }
        float densityScale = this.mState.getDensityScale();
        rect.set(Math.round(this.mDocument.mPaddingLeft * densityScale), Math.round(this.mDocument.mPaddingTop * densityScale), Math.round(this.mDocument.mPaddingRight * densityScale), Math.round(this.mDocument.mPaddingBottom * densityScale));
        if (!(this.mDocument.mPaddingLeft == 0.0f || this.mDocument.mPaddingTop == 0.0f || this.mDocument.mPaddingRight == 0.0f || this.mDocument.mPaddingBottom == 0.0f)) {
            z = true;
        }
        return z;
    }

    public TileMode getTileModeX() {
        return this.mState.mTileModeX;
    }

    public TileMode getTileModeY() {
        return this.mState.mTileModeY;
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        inflate(resources, xmlPullParser, attributeSet, null);
    }

    @SuppressLint({"NewApi"})
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        TypedArray obtainAttributes = obtainAttributes(resources, theme, attributeSet, mStyleableBitmapDrawable);
        updateStateFromTypedArray(obtainAttributes);
        obtainAttributes.recycle();
        updateLocalState(resources);
    }

    public boolean isAutoMirrored() {
        return this.mState.mAutoMirrored;
    }

    public boolean isRunning() {
        return this.mSprAnimation != null && this.mSprAnimation.isRunning();
    }

    public boolean isStateful() {
        return !super.isStateful() ? (this.mState == null || this.mState.mTint == null) ? false : this.mState.mTint.isStateful() : true;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = new SprState(this.mState);
            this.mMutated = true;
        }
        return this;
    }

    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateDstRectAndInsetsIfDirty();
    }

    protected boolean onStateChange(int[] iArr) {
        SprState sprState = this.mState;
        if (sprState.mTint == null || sprState.mTintMode == null) {
            return false;
        }
        this.mTintFilter = updateTintFilterInternal(this.mTintFilter, sprState.mTint, sprState.mTintMode);
        invalidateSelf();
        return true;
    }

    public void setAlpha(int i) {
        if (i != this.mState.mBitmapPaint.getAlpha()) {
            this.mState.mBitmapPaint.setAlpha(i);
        }
    }

    public void setAutoMirrored(boolean z) {
        if (this.mState.mAutoMirrored != z) {
            this.mState.mAutoMirrored = z;
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mState.mBitmapPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setGravity(int i) {
        if (this.mState.mGravity != i) {
            this.mState.mGravity = i;
            updateDstRectAndInsetsIfDirty();
            invalidateSelf();
        }
    }

    public void setTileModeX(TileMode tileMode) {
        setTileModeXY(tileMode, this.mState.mTileModeY);
    }

    public void setTileModeXY(TileMode tileMode, TileMode tileMode2) {
        SprState sprState = this.mState;
        if (sprState.mTileModeX != tileMode || sprState.mTileModeY != tileMode2) {
            sprState.mTileModeX = tileMode;
            sprState.mTileModeY = tileMode2;
            sprState.mRebuildShader = true;
            updateDstRectAndInsetsIfDirty();
            invalidateSelf();
        }
    }

    public final void setTileModeY(TileMode tileMode) {
        setTileModeXY(this.mState.mTileModeX, tileMode);
    }

    public void setTintList(ColorStateList colorStateList) {
        SprState sprState = this.mState;
        sprState.mTint = colorStateList;
        this.mTintFilter = updateTintFilterInternal(this.mTintFilter, colorStateList, sprState.mTintMode);
        invalidateSelf();
    }

    public void setTintMode(Mode mode) {
        SprState sprState = this.mState;
        sprState.mTintMode = mode;
        this.mTintFilter = updateTintFilterInternal(this.mTintFilter, sprState.mTint, mode);
        invalidateSelf();
    }

    public void start() {
        stop();
        if (this.mDocument != null) {
            if (this.mDocument.getFrameAnimationCount() > 1) {
                this.mSprAnimation = new SprDrawableAnimationFrame(this, this.mDocument);
            } else if (this.mDocument.getValueAnimationObjects().size() > 0) {
                if (this.mDocument.isIntrinsic()) {
                    try {
                        this.mDocument = this.mDocument.clone();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                this.mSprAnimation = new SprDrawableAnimationValue(this, this.mDocument);
            }
            if (this.mSprAnimation != null) {
                this.mSprAnimation.start();
            }
        }
    }

    public void stop() {
        if (this.mSprAnimation != null) {
            this.mSprAnimation.stop();
            this.mSprAnimation = null;
        }
    }

    public void toSPR(OutputStream outputStream) throws IOException {
        if (this.mDocument != null) {
            this.mDocument.toSPR(outputStream);
        }
    }

    public String toString() {
        return this.mDocument == null ? "SprDocument is null" : this.mDocument.mLeft + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mDocument.mTop + "-" + this.mDocument.mRight + FingerprintManager.FINGER_PERMISSION_DELIMITER + this.mDocument.mBottom + "\nLoading:" + this.mDocument.getLoadingTime() + "ms\nElement:" + this.mDocument.getTotalElementCount() + "\nSegment:" + this.mDocument.getTotalSegmentCount() + "\nAttribute:" + this.mDocument.getTotalAttributeCount();
    }

    PorterDuffColorFilter updateTintFilterInternal(PorterDuffColorFilter porterDuffColorFilter, ColorStateList colorStateList, Mode mode) {
        if (mUpdateTintFilter == null) {
            return updateTintFilter(porterDuffColorFilter, colorStateList, mode);
        }
        PorterDuffColorFilter porterDuffColorFilter2 = null;
        mUpdateTintFilter.setAccessible(true);
        try {
            porterDuffColorFilter2 = (PorterDuffColorFilter) mUpdateTintFilter.invoke(this, new Object[]{porterDuffColorFilter, colorStateList, mode});
        } catch (Exception e) {
        }
        mUpdateTintFilter.setAccessible(false);
        return porterDuffColorFilter2;
    }
}
