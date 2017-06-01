package com.samsung.android.graphics.spr.document.attribute.impl;

import android.graphics.Matrix;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SprMatrix {
    public static Matrix fromSPR(SprInputStream sprInputStream) throws IOException {
        Matrix matrix = new Matrix();
        float readFloat = sprInputStream.readFloat();
        float readFloat2 = sprInputStream.readFloat();
        float readFloat3 = sprInputStream.readFloat();
        float readFloat4 = sprInputStream.readFloat();
        float readFloat5 = sprInputStream.readFloat();
        float readFloat6 = sprInputStream.readFloat();
        matrix.setValues(new float[]{readFloat, readFloat2, readFloat3, readFloat4, readFloat5, readFloat6, 0.0f, 0.0f, 1.0f});
        return matrix;
    }

    public static void toSPR(DataOutputStream dataOutputStream, Matrix matrix) throws IOException {
        if (matrix == null) {
            dataOutputStream.writeFloat(1.0f);
            dataOutputStream.writeFloat(0.0f);
            dataOutputStream.writeFloat(0.0f);
            dataOutputStream.writeFloat(0.0f);
            dataOutputStream.writeFloat(1.0f);
            dataOutputStream.writeFloat(0.0f);
            return;
        }
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        dataOutputStream.writeFloat(fArr[0]);
        dataOutputStream.writeFloat(fArr[1]);
        dataOutputStream.writeFloat(fArr[2]);
        dataOutputStream.writeFloat(fArr[3]);
        dataOutputStream.writeFloat(fArr[4]);
        dataOutputStream.writeFloat(fArr[5]);
    }
}
