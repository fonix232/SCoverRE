package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeShadow;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprObjectShapeUse extends SprObjectBase {
    public int link = 0;

    public SprObjectShapeUse() {
        super((byte) 17);
    }

    public SprObjectShapeUse(SprInputStream sprInputStream) throws IOException {
        super((byte) 17);
        fromSPR(sprInputStream);
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        SprObjectBase reference = sprDocument.getReference(this.link);
        if (reference != null) {
            reference.draw(sprDocument, canvas, f, f2, f4);
        }
        canvas.restore();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.link = sprInputStream.readInt();
        super.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return super.getSPRSize() + 4;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return 1;
    }

    public void preDraw(SprDocument sprDocument, Paint paint, Paint paint2, boolean z, boolean z2, SprAttributeShadow sprAttributeShadow) {
        SprObjectBase reference = sprDocument.getReference(this.link);
        if (reference != null) {
            reference.preDraw(sprDocument, paint, paint2, z, z2, sprAttributeShadow);
        }
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.link);
        super.toSPR(dataOutputStream);
    }
}
