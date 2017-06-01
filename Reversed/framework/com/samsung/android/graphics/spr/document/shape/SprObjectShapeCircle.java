package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprObjectShapeCircle extends SprObjectBase {
    public float cr = 0.0f;
    public float cx = 0.0f;
    public float cy = 0.0f;

    public SprObjectShapeCircle() {
        super((byte) 1);
    }

    public SprObjectShapeCircle(SprInputStream sprInputStream) throws IOException {
        super((byte) 1);
        fromSPR(sprInputStream);
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        setShadowLayer();
        if (this.isVisibleFill) {
            canvas.drawCircle(this.cx, this.cy, this.cr, this.fillPaint);
        }
        if (this.isVisibleStroke) {
            canvas.drawCircle(this.cx, this.cy, this.cr, this.strokePaint);
        }
        clearShadowLayer();
        canvas.restore();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.cx = sprInputStream.readFloat();
        this.cy = sprInputStream.readFloat();
        this.cr = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 12;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return 4;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.cx);
        dataOutputStream.writeFloat(this.cy);
        dataOutputStream.writeFloat(this.cr);
        super.toSPR(dataOutputStream);
    }
}
