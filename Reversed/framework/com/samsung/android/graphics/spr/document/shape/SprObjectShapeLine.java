package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprObjectShapeLine extends SprObjectBase {
    public float x1 = 0.0f;
    public float x2 = 0.0f;
    public float y1 = 0.0f;
    public float y2 = 0.0f;

    public SprObjectShapeLine() {
        super((byte) 3);
    }

    public SprObjectShapeLine(SprInputStream sprInputStream) throws IOException {
        super((byte) 3);
        fromSPR(sprInputStream);
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        setShadowLayer();
        if (this.isVisibleStroke) {
            canvas.drawLine(this.x1, this.y1, this.x2, this.y2, this.strokePaint);
        }
        clearShadowLayer();
        canvas.restore();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.x1 = sprInputStream.readFloat();
        this.y1 = sprInputStream.readFloat();
        this.x2 = sprInputStream.readFloat();
        this.y2 = sprInputStream.readFloat();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 16;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return 2;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeFloat(this.x1);
        dataOutputStream.writeFloat(this.y1);
        dataOutputStream.writeFloat(this.x2);
        dataOutputStream.writeFloat(this.y2);
        super.toSPR(dataOutputStream);
    }
}
