package com.samsung.android.graphics.spr.document.debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.shape.SprObjectBase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SprDebug {
    public static final int DEBUG_HIGH = 3;
    public static final int DEBUG_LOW = 1;
    public static final int DEBUG_MID = 2;
    public static boolean IsDebug;
    private static Integer mDebugLevel;
    private static Paint mTextOutlinePaint = null;
    private static Paint mTextPaint = null;

    static {
        IsDebug = false;
        mDebugLevel = null;
        if ("eng".equals(Build.TYPE)) {
            try {
                Class cls = Class.forName("android.os.SystemProperties");
                mDebugLevel = (Integer) cls.getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(cls, new Object[]{"persist.sys.spr.debug", Integer.valueOf(0)});
            } catch (Throwable e) {
                e.printStackTrace();
                mDebugLevel = Integer.valueOf(0);
            }
        } else {
            mDebugLevel = Integer.valueOf(0);
        }
        IsDebug = mDebugLevel.intValue() >= 1;
    }

    public static void drawDebugInfo(Canvas canvas, SprDocument sprDocument, int i, int i2, int i3) {
        if (mDebugLevel.intValue() >= 2) {
            StringBuilder stringBuilder = new StringBuilder();
            String str = sprDocument.isNinePatch() ? "N" : sprDocument.isIntrinsic() ? "" : "C";
            drawText(canvas, stringBuilder.append(str).append(String.valueOf(sprDocument.hashCode() % 10000)).toString(), 20);
            drawText(canvas, sprDocument.mName, 40);
            drawText(canvas, i3 + ")" + i + "x" + i2, 60);
            if (mDebugLevel.intValue() >= 3) {
                drawText(canvas, sprDocument.getLoadingTime() + "ms E:" + sprDocument.getTotalElementCount() + " S:" + sprDocument.getTotalSegmentCount() + " A:" + sprDocument.getTotalAttributeCount(), 80);
            }
        }
    }

    public static void drawRect(Canvas canvas, SprDocument sprDocument, int i, int i2) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(5.0f);
        paint.setColor(-65536);
        canvas.drawRect(sprDocument.mLeft, sprDocument.mTop, ((float) i) + sprDocument.mLeft, ((float) i2) + sprDocument.mTop, paint);
    }

    private static void drawText(Canvas canvas, String str, int i) {
        if (mTextOutlinePaint == null) {
            mTextOutlinePaint = new Paint();
            mTextOutlinePaint.setAntiAlias(true);
            mTextOutlinePaint.setTextSize(20.0f);
            mTextOutlinePaint.setStyle(Style.STROKE);
            mTextOutlinePaint.setColor(-16777216);
            mTextOutlinePaint.setStrokeWidth(4.0f);
        }
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(20.0f);
            mTextPaint.setStyle(Style.FILL);
            mTextPaint.setColor(-1);
        }
        canvas.drawText(str, 5.0f, (float) i, mTextOutlinePaint);
        canvas.drawText(str, 5.0f, (float) i, mTextPaint);
    }

    public static void preDraw(SprObjectBase sprObjectBase) {
        if (mDebugLevel.intValue() >= 3 && sprObjectBase.strokePaint != null) {
            sprObjectBase.isVisibleStroke = true;
            sprObjectBase.strokePaint.setColor(Color.rgb(255, 0, 255));
            if (sprObjectBase.strokePaint.getStrokeWidth() < SprDocument.DEFAULT_DENSITY_SCALE) {
                sprObjectBase.strokePaint.setStrokeWidth(SprDocument.DEFAULT_DENSITY_SCALE);
            }
        }
    }

    public void dumpPNG(SprDocument sprDocument, int i, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        sprDocument.draw(new Canvas(createBitmap), i, i2, 0, i3);
        try {
            File file = new File("/sdcard/spr_debug");
            if (file.mkdir() || file.isDirectory()) {
                OutputStream fileOutputStream = new FileOutputStream(new File(file, String.valueOf(sprDocument.hashCode() % 10000) + ".png"));
                createBitmap.compress(CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
    }
}
