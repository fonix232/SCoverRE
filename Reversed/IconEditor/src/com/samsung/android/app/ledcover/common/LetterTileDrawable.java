package com.samsung.android.app.ledcover.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.graphics.spr.SemPathRenderingDrawable;

public class LetterTileDrawable extends Drawable {
    private static Bitmap DEFAULT_BUSINESS_AVATAR = null;
    private static Bitmap DEFAULT_PERSON_AVATAR = null;
    protected static final String TITLE_FONT = "sec-roboto-condensed-light";
    public static final int TYPE_BUSINESS = 2;
    public static final int TYPE_DEFAULT = 1;
    public static final int TYPE_PERSON = 1;
    private static int sColors;
    private static int sDefaultColor;
    private static final char[] sFirstChar;
    private static float sLetterToTileRatio;
    protected static final Paint sPaint;
    protected static final Rect sRect;
    protected static final RectF sRectF;
    protected static int sStrokeColor;
    protected static final Paint sStrokePaint;
    protected static int sStrokeWidth;
    private static int sTileFontColor;
    private boolean mAvailableNumber;
    private int mContactType;
    private String mDisplayName;
    protected boolean mIsCircle;
    protected final Paint mPaint;
    protected Resources mResources;
    protected float mScale;

    static {
        sPaint = new Paint();
        sRect = new Rect();
        sRectF = new RectF();
        sFirstChar = new char[TYPE_BUSINESS];
        sStrokePaint = new Paint();
    }

    public LetterTileDrawable(Resources res) {
        this.mScale = 1.0f;
        this.mContactType = TYPE_PERSON;
        this.mIsCircle = true;
        this.mResources = res;
        this.mPaint = new Paint();
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setDither(true);
        if (sColors == 0) {
            sColors = res.getColor(C0198R.color.default_caller_id_bg_color, null);
            sDefaultColor = res.getColor(C0198R.color.letter_tile_default_color, null);
            sTileFontColor = res.getColor(C0198R.color.contacts_list_tile_color, null);
            sLetterToTileRatio = res.getFraction(C0198R.fraction.letter_to_tile_ratio, TYPE_PERSON, TYPE_PERSON);
            preloadDefaultPhotos(res);
            sPaint.setTypeface(Typeface.create(TITLE_FONT, 0));
            sPaint.setTextAlign(Align.CENTER);
            sPaint.setAntiAlias(true);
        }
        this.mPaint.setColor(sColors);
        sStrokeColor = res.getColor(C0198R.color.contacts_list_tile_color, null);
        sStrokePaint.setStyle(Style.STROKE);
        sStrokeWidth = res.getInteger(C0198R.integer.caller_id_lettertile_stroke_width);
        sStrokePaint.setStrokeWidth((float) sStrokeWidth);
        sStrokePaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty()) {
            drawLetterTile(canvas);
        }
    }

    protected void drawLetterTile(Canvas canvas) {
        sPaint.setColor(sDefaultColor);
        sPaint.setAlpha(this.mPaint.getAlpha());
        sStrokePaint.setColor(sStrokeColor);
        Rect bounds = getBounds();
        RectF strokeRect = new RectF((float) (bounds.left + sStrokeWidth), (float) (bounds.top + sStrokeWidth), (float) (bounds.right - sStrokeWidth), (float) (bounds.bottom - sStrokeWidth));
        int minDimension = Math.min(bounds.width(), bounds.height());
        if (this.mIsCircle) {
            float cornerRadius = ((float) bounds.height()) / 2.0f;
            sRectF.set(bounds);
            canvas.drawRoundRect(strokeRect, cornerRadius, cornerRadius, sPaint);
            canvas.drawRoundRect(strokeRect, cornerRadius, cornerRadius, sStrokePaint);
        } else {
            canvas.drawRect(bounds, sPaint);
            canvas.drawRect(bounds, sStrokePaint);
        }
        if (this.mContactType != TYPE_PERSON || this.mDisplayName == null || TextUtils.isEmpty(this.mDisplayName) || !(Character.isLetter(this.mDisplayName.charAt(0)) || this.mAvailableNumber)) {
            Bitmap bitmap = getBitmapForDefaultAvatar(this.mContactType);
            if (bitmap != null) {
                drawBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), canvas);
                return;
            }
            return;
        }
        int numberOfLetters = TYPE_PERSON;
        sFirstChar[0] = Character.toUpperCase(this.mDisplayName.charAt(0));
        if (this.mAvailableNumber && this.mDisplayName.length() > TYPE_PERSON) {
            numberOfLetters = TYPE_BUSINESS;
            sFirstChar[TYPE_PERSON] = Character.toUpperCase(this.mDisplayName.charAt(TYPE_PERSON));
        }
        sPaint.setTextSize((this.mScale * sLetterToTileRatio) * ((float) minDimension));
        sPaint.getTextBounds(sFirstChar, 0, numberOfLetters, sRect);
        sPaint.setColor(sTileFontColor);
        canvas.drawText(sFirstChar, 0, numberOfLetters, (float) bounds.centerX(), (((float) bounds.height()) / 2.0f) - ((sPaint.descent() + sPaint.ascent()) / 2.0f), sPaint);
    }

    protected void drawBitmap(Bitmap bitmap, int width, int height, Canvas canvas) {
        Rect destRect = copyBounds();
        int halfLength = (int) ((this.mScale * ((float) Math.min(destRect.width(), destRect.height()))) / 2.0f);
        destRect.set((destRect.centerX() - halfLength) + sStrokeWidth, (destRect.centerY() - halfLength) + sStrokeWidth, (destRect.centerX() + halfLength) - sStrokeWidth, (destRect.centerY() + halfLength) - sStrokeWidth);
        sRect.set(0, 0, width, height);
        canvas.drawBitmap(bitmap, sRect, destRect, this.mPaint);
    }

    protected Bitmap getBitmapForDefaultAvatar(int contactType) {
        if (contactType == TYPE_BUSINESS) {
            return DEFAULT_BUSINESS_AVATAR;
        }
        return DEFAULT_PERSON_AVATAR;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -1;
    }

    public void setContactDetails(String displayName) {
        this.mDisplayName = displayName;
    }

    public void setContactType(int contactType) {
        this.mContactType = contactType;
    }

    public void setAvailableNumber(boolean availableNumber) {
        this.mAvailableNumber = availableNumber;
    }

    private void preloadDefaultPhotos(Resources res) {
        Drawable drawable = res.getDrawable(C0198R.drawable.contacts_default_caller_id_list, null);
        if (drawable instanceof BitmapDrawable) {
            DEFAULT_PERSON_AVATAR = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof SemPathRenderingDrawable) {
            DEFAULT_PERSON_AVATAR = ((SemPathRenderingDrawable) drawable).getBitmap();
        }
        drawable = res.getDrawable(C0198R.drawable.contacts_default_nearby_list, null);
        if (drawable instanceof BitmapDrawable) {
            DEFAULT_BUSINESS_AVATAR = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof SemPathRenderingDrawable) {
            DEFAULT_BUSINESS_AVATAR = ((SemPathRenderingDrawable) drawable).getBitmap();
        }
    }
}
