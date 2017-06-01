package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprObjectShapeRectangle extends SprObjectBase {
    public float bottom = 0.0f;
    public float left = 0.0f;
    public float right = 0.0f;
    public float rx = 0.0f;
    public float ry = 0.0f;
    public float top = 0.0f;

    public SprObjectShapeRectangle() {
        super((byte) 5);
    }

    public SprObjectShapeRectangle(float f, float f2, float f3, float f4) {
        super((byte) 5);
        this.left = f;
        this.top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public SprObjectShapeRectangle(SprInputStream sprInputStream) throws IOException {
        super((byte) 5);
        fromSPR(sprInputStream);
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        RectF rectF = new RectF(this.left, this.top, this.right, this.bottom);
        setShadowLayer();
        if (this.rx == 0.0f && this.ry == 0.0f) {
            if (this.isVisibleFill) {
                canvas.drawRect(rectF, this.fillPaint);
            }
            if (this.isVisibleStroke) {
                canvas.drawRect(rectF, this.strokePaint);
            }
        } else {
            if (this.isVisibleFill) {
                canvas.drawRoundRect(rectF, this.rx, this.ry, this.fillPaint);
            }
            if (this.isVisibleStroke) {
                canvas.drawRoundRect(rectF, this.rx, this.ry, this.strokePaint);
            }
        }
        clearShadowLayer();
        canvas.restore();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.left = sprInputStream.readFloat();
        this.top = sprInputStream.readFloat();
        this.right = sprInputStream.readFloat();
        this.bottom = sprInputStream.readFloat();
        this.rx = sprInputStream.readFloat();
        this.ry = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 24;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return (this.rx == 0.0f && this.ry == 0.0f) ? 4 : 8;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.left);
        dataOutputStream.writeFloat(this.top);
        dataOutputStream.writeFloat(this.right);
        dataOutputStream.writeFloat(this.bottom);
        dataOutputStream.writeFloat(this.rx);
        dataOutputStream.writeFloat(this.ry);
        super.toSPR(dataOutputStream);
    }
}
