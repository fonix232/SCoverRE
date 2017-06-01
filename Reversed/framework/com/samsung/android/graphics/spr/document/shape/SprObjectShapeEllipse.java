package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprObjectShapeEllipse extends SprObjectBase {
    public float bottom = 0.0f;
    public float left = 0.0f;
    public float right = 0.0f;
    public float top = 0.0f;

    public SprObjectShapeEllipse() {
        super((byte) 2);
    }

    public SprObjectShapeEllipse(SprInputStream sprInputStream) throws IOException {
        super((byte) 2);
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
        if (this.isVisibleFill) {
            canvas.drawOval(rectF, this.fillPaint);
        }
        if (this.isVisibleStroke) {
            canvas.drawOval(rectF, this.strokePaint);
        }
        clearShadowLayer();
        canvas.restore();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.left = sprInputStream.readFloat();
        this.top = sprInputStream.readFloat();
        this.right = sprInputStream.readFloat();
        this.bottom = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 16;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return 4;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.left);
        dataOutputStream.writeFloat(this.top);
        dataOutputStream.writeFloat(this.right);
        dataOutputStream.writeFloat(this.bottom);
        super.toSPR(dataOutputStream);
    }
}
