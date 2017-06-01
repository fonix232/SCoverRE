package com.samsung.android.graphics.spr.document.attribute;

import android.graphics.Matrix;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.impl.SprMatrix;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprAttributeMatrix extends SprAttributeBase {
    private final SprAttributeMatrix mIntrinsic;
    public Matrix matrix;

    public SprAttributeMatrix() {
        super(SprAttributeBase.TYPE_MATRIX);
        this.mIntrinsic = (SprAttributeMatrix) this.mIntrinsic;
        this.matrix = new Matrix();
    }

    public SprAttributeMatrix(Matrix matrix) {
        super(SprAttributeBase.TYPE_MATRIX);
        this.mIntrinsic = (SprAttributeMatrix) this.mIntrinsic;
        this.matrix = matrix;
    }

    public SprAttributeMatrix(SprInputStream sprInputStream) throws IOException {
        super(SprAttributeBase.TYPE_MATRIX);
        this.mIntrinsic = (SprAttributeMatrix) this.mIntrinsic;
        fromSPR(sprInputStream);
    }

    public SprAttributeMatrix clone() throws CloneNotSupportedException {
        SprAttributeMatrix sprAttributeMatrix = (SprAttributeMatrix) super.clone();
        sprAttributeMatrix.matrix = new Matrix(this.matrix);
        return sprAttributeMatrix;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.matrix = SprMatrix.fromSPR(sprInputStream);
    }

    public int getSPRSize() {
        return 24;
    }

    public void reset() {
        this.matrix.set(this.mIntrinsic.matrix);
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        SprMatrix.toSPR(dataOutputStream, this.matrix);
    }
}
