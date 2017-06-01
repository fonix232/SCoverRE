package com.samsung.android.app.ledcover.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverDotInfo;
import java.lang.ref.WeakReference;

public class LCoverNoti_CustomDotView extends View {
    private Rect drawBitmapRect;
    private Bitmap mBM;
    private Canvas mBackCanvas;
    private Paint mPaint;
    private LCoverDotInfo[] userData;

    public LCoverNoti_CustomDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.drawBitmapRect = new Rect();
        this.mPaint = new Paint(3);
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.FILL);
    }

    public void setUserData(LCoverDotInfo[] userData) {
        this.userData = userData;
        if (userData != null) {
            float diameter = getResources().getDimension(C0198R.dimen.custom_dotview_diameter);
            float margin = getResources().getDimension(C0198R.dimen.custom_dotview_margin);
            float padding = getResources().getDimension(C0198R.dimen.custom_dotview_view_padding);
            float radius = diameter / 2.0f;
            int ledMatrixRows = Defines.getLedMatrixRows();
            int ledMatrixCols = Defines.getLedMatrixColumns();
            int ledMatrixTotal = Defines.getLedMatrixTotal();
            if (this.mBM == null) {
                float f = ((2.0f * padding) + (((float) ledMatrixCols) * diameter)) + (((float) (ledMatrixCols - 1)) * margin);
                f = ((2.0f * padding) + (((float) ledMatrixRows) * diameter)) + (((float) (ledMatrixRows - 1)) * margin);
                this.mBM = (Bitmap) new WeakReference(Bitmap.createBitmap((int) r16, (int) r16, Config.ARGB_8888)).get();
            }
            if (this.mBackCanvas == null) {
                this.mBackCanvas = new Canvas(this.mBM);
            }
            float cx = padding + radius;
            float cy = padding + radius;
            int numColumn = 0;
            for (int i = 0; i < ledMatrixTotal; i++) {
                if (numColumn >= ledMatrixCols) {
                    cx = padding + radius;
                    cy += diameter + margin;
                    numColumn = 0;
                }
                if (userData[i].getDotByteData() == '1') {
                    this.mBackCanvas.drawCircle(cx, cy, radius, this.mPaint);
                }
                cx += diameter + margin;
                numColumn++;
            }
        }
    }

    public void destroyDrawingCache() {
        if (this.mBM != null) {
            this.mBM.recycle();
            this.mBM = null;
        }
        this.mBackCanvas = null;
        super.destroyDrawingCache();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int bitmapWidth;
        int bitmapHeight;
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (this.mBM != null) {
            bitmapWidth = this.mBM.getWidth();
            bitmapHeight = this.mBM.getHeight();
        } else {
            bitmapWidth = (int) getResources().getDimension(C0198R.dimen.led_cover_adapter_icon_width);
            bitmapHeight = (int) getResources().getDimension(C0198R.dimen.led_cover_adapter_icon_height);
        }
        float ratioBitmap = ((float) bitmapWidth) / ((float) bitmapHeight);
        float ratioView = ((float) viewWidth) / ((float) viewHeight);
        boolean keepMaxWidth = false;
        if (ratioBitmap > 1.0f) {
            if (ratioView < 1.0f) {
                keepMaxWidth = true;
            } else if (ratioBitmap >= ratioView) {
                keepMaxWidth = true;
            }
        } else if (ratioView < 1.0f && ratioBitmap >= ratioView) {
            keepMaxWidth = true;
        }
        int offsetX = 0;
        int offsetY = 0;
        if (keepMaxWidth) {
            bitmapHeight = (int) ((((float) viewWidth) / ((float) bitmapWidth)) * ((float) bitmapHeight));
            bitmapWidth = viewWidth;
            offsetY = (viewHeight - bitmapHeight) / 2;
        } else {
            bitmapWidth = (int) ((((float) viewHeight) / ((float) bitmapHeight)) * ((float) bitmapWidth));
            bitmapHeight = viewHeight;
            offsetX = (viewWidth - bitmapWidth) / 2;
        }
        this.drawBitmapRect.set(offsetX, offsetY, bitmapWidth + offsetX, bitmapHeight + offsetY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (this.userData != null && this.mBM != null) {
            canvas.drawBitmap(this.mBM, null, this.drawBitmapRect, this.mPaint);
        }
    }
}
