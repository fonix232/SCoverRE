package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SprObjectShapePath extends SprObjectBase {
    public static final byte TYPE_BEZIER_CURVETO = (byte) 4;
    public static final byte TYPE_CLOSE = (byte) 6;
    public static final byte TYPE_ELLIPTICAL_ARC = (byte) 5;
    public static final byte TYPE_LINETO = (byte) 2;
    public static final byte TYPE_MOVETO = (byte) 1;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_QUADRATIC_CURVETO = (byte) 3;
    public final SprObjectShapePath mIntrinsic;
    public ArrayList<PathInfo> mPathInfoList;
    public Path path;

    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegSign;

        private ExtractFloatResult() {
        }
    }

    public static class PathInfo implements Cloneable {
        public byte type = (byte) 0;
        public float f10x = 0.0f;
        public float x1 = 0.0f;
        public float x2 = 0.0f;
        public float f11y = 0.0f;
        public float y1 = 0.0f;
        public float y2 = 0.0f;

        protected PathInfo clone() throws CloneNotSupportedException {
            return (PathInfo) super.clone();
        }
    }

    public SprObjectShapePath() {
        super((byte) 4);
        this.mPathInfoList = null;
        this.path = null;
        this.mIntrinsic = (SprObjectShapePath) this.mIntrinsic;
        this.path = new Path();
        this.mPathInfoList = new ArrayList();
    }

    public SprObjectShapePath(SprInputStream sprInputStream) throws IOException {
        super((byte) 4);
        this.mPathInfoList = null;
        this.path = null;
        this.mIntrinsic = (SprObjectShapePath) this.mIntrinsic;
        this.path = new Path();
        fromSPR(sprInputStream);
    }

    public SprObjectShapePath(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        super((byte) 4);
        this.mPathInfoList = null;
        this.path = null;
        this.mIntrinsic = (SprObjectShapePath) this.mIntrinsic;
        this.path = new Path();
        this.mPathInfoList = new ArrayList();
        fromXml(xmlPullParser);
    }

    private void addCommand(float[] fArr, char c, char c2, float[] fArr2) {
        int i = 2;
        float f = fArr[0];
        float f2 = fArr[1];
        float f3 = fArr[2];
        float f4 = fArr[3];
        switch (c2) {
            case SemMotionRecognitionEvent.TWO_TIPPING_GYRO /*65*/:
            case 'a':
                i = 7;
                break;
            case SemMotionRecognitionEvent.SMART_ALERT /*67*/:
            case 'c':
                i = 6;
                break;
            case SemMotionRecognitionEvent.TILT /*72*/:
            case SemMotionRecognitionEvent.FLIP_SCREEN_UP /*86*/:
            case 'h':
            case 'v':
                i = 1;
                break;
            case SemMotionRecognitionEvent.CALLPOSE_L /*76*/:
            case SemMotionRecognitionEvent.CALLPOSE_R /*77*/:
            case SemMotionRecognitionEvent.TILT_LEVEL_ZERO /*84*/:
            case SemMotionRecognitionEvent.SMART_SCROLL_TILT_DOWN_START_LAND /*108*/:
            case SemMotionRecognitionEvent.SMART_SCROLL_TILT_FACE_IN_STOP_LAND /*109*/:
            case 't':
                i = 2;
                break;
            case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1 /*81*/:
            case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_3 /*83*/:
            case SemMotionRecognitionEvent.SMART_RELAY /*113*/:
            case 's':
                i = 4;
                break;
            case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1_LAND /*90*/:
            case FingerprintEvent.STATUS_IDENTIFY_FAILURE_DATABASE_FAILURE /*122*/:
                close();
                return;
        }
        for (int i2 = 0; i2 < fArr2.length; i2 += i) {
            float f5;
            float f6;
            switch (c2) {
                case SemMotionRecognitionEvent.TWO_TIPPING_GYRO /*65*/:
                    drawArc(f, f2, fArr2[i2 + 5], fArr2[i2 + 6], fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3] != 0.0f, fArr2[i2 + 4] != 0.0f);
                    f = fArr2[i2 + 5];
                    f2 = fArr2[i2 + 6];
                    f3 = f;
                    f4 = f2;
                    break;
                case SemMotionRecognitionEvent.SMART_ALERT /*67*/:
                    cubicTo(fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3], fArr2[i2 + 4], fArr2[i2 + 5]);
                    f = fArr2[i2 + 4];
                    f2 = fArr2[i2 + 5];
                    f3 = fArr2[i2 + 2];
                    f4 = fArr2[i2 + 3];
                    break;
                case SemMotionRecognitionEvent.TILT /*72*/:
                    f = fArr2[i2 + 0];
                    lineTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.CALLPOSE_L /*76*/:
                    f = fArr2[i2 + 0];
                    f2 = fArr2[i2 + 1];
                    lineTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.CALLPOSE_R /*77*/:
                    f = fArr2[i2 + 0];
                    f2 = fArr2[i2 + 1];
                    moveTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_1 /*81*/:
                    quadTo(fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3]);
                    f3 = fArr2[i2 + 0];
                    f4 = fArr2[i2 + 1];
                    f = fArr2[i2 + 2];
                    f2 = fArr2[i2 + 3];
                    break;
                case SemMotionRecognitionEvent.TILT_DOWN_LEVEL_3 /*83*/:
                    f5 = f;
                    f6 = f2;
                    if (!(c == 'c' || c == 's' || c == 'C')) {
                        if (c == 'S') {
                        }
                        cubicTo(f5, f6, fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3]);
                        f3 = fArr2[i2 + 0];
                        f4 = fArr2[i2 + 1];
                        f = fArr2[i2 + 2];
                        f2 = fArr2[i2 + 3];
                        break;
                    }
                    f5 = (SprDocument.DEFAULT_DENSITY_SCALE * f) - f3;
                    f6 = (SprDocument.DEFAULT_DENSITY_SCALE * f2) - f4;
                    cubicTo(f5, f6, fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3]);
                    f3 = fArr2[i2 + 0];
                    f4 = fArr2[i2 + 1];
                    f = fArr2[i2 + 2];
                    f2 = fArr2[i2 + 3];
                case SemMotionRecognitionEvent.TILT_LEVEL_ZERO /*84*/:
                    f5 = f;
                    f6 = f2;
                    if (!(c == 'q' || c == 't' || c == 'Q')) {
                        if (c == 'T') {
                        }
                        quadTo(f5, f6, fArr2[i2 + 0], fArr2[i2 + 1]);
                        f3 = f5;
                        f4 = f6;
                        f = fArr2[i2 + 0];
                        f2 = fArr2[i2 + 1];
                        break;
                    }
                    f5 = (SprDocument.DEFAULT_DENSITY_SCALE * f) - f3;
                    f6 = (SprDocument.DEFAULT_DENSITY_SCALE * f2) - f4;
                    quadTo(f5, f6, fArr2[i2 + 0], fArr2[i2 + 1]);
                    f3 = f5;
                    f4 = f6;
                    f = fArr2[i2 + 0];
                    f2 = fArr2[i2 + 1];
                case SemMotionRecognitionEvent.FLIP_SCREEN_UP /*86*/:
                    f2 = fArr2[i2 + 0];
                    lineTo(f, f2);
                    break;
                case 'a':
                    drawArc(f, f2, fArr2[i2 + 5] + f, fArr2[i2 + 6] + f2, fArr2[i2 + 0], fArr2[i2 + 1], fArr2[i2 + 2], fArr2[i2 + 3] != 0.0f, fArr2[i2 + 4] != 0.0f);
                    f += fArr2[i2 + 5];
                    f2 += fArr2[i2 + 6];
                    f3 = f;
                    f4 = f2;
                    break;
                case 'c':
                    cubicTo(f + fArr2[i2 + 0], f2 + fArr2[i2 + 1], f + fArr2[i2 + 2], f2 + fArr2[i2 + 3], f + fArr2[i2 + 4], f2 + fArr2[i2 + 5]);
                    f3 = f + fArr2[i2 + 2];
                    f4 = f2 + fArr2[i2 + 3];
                    f += fArr2[i2 + 4];
                    f2 += fArr2[i2 + 5];
                    break;
                case 'h':
                    f += fArr2[i2 + 0];
                    lineTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.SMART_SCROLL_TILT_DOWN_START_LAND /*108*/:
                    f += fArr2[i2 + 0];
                    f2 += fArr2[i2 + 1];
                    lineTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.SMART_SCROLL_TILT_FACE_IN_STOP_LAND /*109*/:
                    f += fArr2[i2 + 0];
                    f2 += fArr2[i2 + 1];
                    moveTo(f, f2);
                    break;
                case SemMotionRecognitionEvent.SMART_RELAY /*113*/:
                    quadTo(fArr2[i2 + 0] + f, fArr2[i2 + 1] + f2, fArr2[i2 + 2] + f, fArr2[i2 + 3] + f2);
                    f3 = f + fArr2[i2 + 0];
                    f4 = f2 + fArr2[i2 + 1];
                    f += fArr2[i2 + 2];
                    f2 += fArr2[i2 + 3];
                    break;
                case 's':
                    f5 = 0.0f;
                    f6 = 0.0f;
                    if (!(c == 'c' || c == 's' || c == 'C')) {
                        if (c == 'S') {
                        }
                        cubicTo(f5 + f, f6 + f2, f + fArr2[i2 + 0], f2 + fArr2[i2 + 1], f + fArr2[i2 + 2], f2 + fArr2[i2 + 3]);
                        f3 = f + fArr2[i2 + 0];
                        f4 = f2 + fArr2[i2 + 1];
                        f += fArr2[i2 + 2];
                        f2 += fArr2[i2 + 3];
                        break;
                    }
                    f5 = f - f3;
                    f6 = f2 - f4;
                    cubicTo(f5 + f, f6 + f2, f + fArr2[i2 + 0], f2 + fArr2[i2 + 1], f + fArr2[i2 + 2], f2 + fArr2[i2 + 3]);
                    f3 = f + fArr2[i2 + 0];
                    f4 = f2 + fArr2[i2 + 1];
                    f += fArr2[i2 + 2];
                    f2 += fArr2[i2 + 3];
                case 't':
                    f5 = 0.0f;
                    f6 = 0.0f;
                    if (!(c == 'q' || c == 't' || c == 'Q')) {
                        if (c == 'T') {
                        }
                        quadTo(f + f5, f2 + f6, fArr2[i2 + 0] + f, fArr2[i2 + 1] + f2);
                        f3 = f + f5;
                        f4 = f2 + f6;
                        f += fArr2[i2 + 0];
                        f2 += fArr2[i2 + 1];
                        break;
                    }
                    f5 = f - f3;
                    f6 = f2 - f4;
                    quadTo(f + f5, f2 + f6, fArr2[i2 + 0] + f, fArr2[i2 + 1] + f2);
                    f3 = f + f5;
                    f4 = f2 + f6;
                    f += fArr2[i2 + 0];
                    f2 += fArr2[i2 + 1];
                case 'v':
                    f2 += fArr2[i2 + 0];
                    lineTo(f, f2);
                    break;
                default:
                    break;
            }
            c = c2;
        }
        fArr[0] = f;
        fArr[1] = f2;
        fArr[2] = f3;
        fArr[3] = f4;
    }

    private void arcToBezier(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        int abs = Math.abs((int) Math.ceil((4.0d * d9) / 3.141592653589793d));
        double d10 = d8;
        double cos = Math.cos(d7);
        double sin = Math.sin(d7);
        double cos2 = Math.cos(d8);
        double sin2 = Math.sin(d8);
        double d11 = (((-d3) * cos) * sin2) - ((d4 * sin) * cos2);
        double d12 = (((-d3) * sin) * sin2) + ((d4 * cos) * cos2);
        double d13 = d9 / ((double) abs);
        for (int i = 0; i < abs; i++) {
            double d14 = d10 + d13;
            double sin3 = Math.sin(d14);
            double cos3 = Math.cos(d14);
            double d15 = (((d3 * cos) * cos3) + d) - ((d4 * sin) * sin3);
            double d16 = (((d3 * sin) * cos3) + d2) + ((d4 * cos) * sin3);
            double d17 = (((-d3) * cos) * sin3) - ((d4 * sin) * cos3);
            double d18 = (((-d3) * sin) * sin3) + ((d4 * cos) * cos3);
            double tan = Math.tan((d14 - d10) / 2.0d);
            double sin4 = (Math.sin(d14 - d10) * (Math.sqrt(((3.0d * tan) * tan) + 4.0d) - 1.0d)) / 3.0d;
            cubicTo((float) (d5 + (sin4 * d11)), (float) (d6 + (sin4 * d12)), (float) (d15 - (sin4 * d17)), (float) (d16 - (sin4 * d18)), (float) d15, (float) d16);
            d10 = d14;
            d5 = d15;
            d6 = d16;
            d11 = d17;
            d12 = d18;
        }
    }

    private void createNodesFromPathData(String str) {
        if (str != null) {
            int i = 0;
            int i2 = 1;
            char c = 'm';
            float[] fArr = new float[4];
            while (i2 < str.length()) {
                i2 = nextStart(str, i2);
                String trim = str.substring(i, i2).trim();
                if (trim.length() > 0) {
                    addCommand(fArr, c, trim.charAt(0), getFloats(trim));
                    c = trim.charAt(0);
                }
                i = i2;
                i2++;
            }
            if (i2 - i == 1 && i < str.length()) {
                addCommand(fArr, c, str.charAt(i), new float[0]);
            }
        }
    }

    private void drawArc(float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean z, boolean z2) {
        double toRadians = Math.toRadians((double) f7);
        double cos = Math.cos(toRadians);
        double sin = Math.sin(toRadians);
        double d = ((((double) f) * cos) + (((double) f2) * sin)) / ((double) f5);
        double d2 = ((((double) (-f)) * sin) + (((double) f2) * cos)) / ((double) f6);
        double d3 = ((((double) f3) * cos) + (((double) f4) * sin)) / ((double) f5);
        double d4 = ((((double) (-f3)) * sin) + (((double) f4) * cos)) / ((double) f6);
        double d5 = d - d3;
        double d6 = d2 - d4;
        double d7 = (d + d3) / 2.0d;
        double d8 = (d2 + d4) / 2.0d;
        double d9 = (d5 * d5) + (d6 * d6);
        if (d9 != 0.0d) {
            double d10 = (1.0d / d9) - 0.25d;
            if (d10 < 0.0d) {
                float sqrt = (float) (Math.sqrt(d9) / 1.99999d);
                drawArc(f, f2, f3, f4, f5 * sqrt, f6 * sqrt, f7, z, z2);
                return;
            }
            double d11;
            double d12;
            double sqrt2 = Math.sqrt(d10);
            double d13 = sqrt2 * d5;
            double d14 = sqrt2 * d6;
            if (z == z2) {
                d11 = d7 - d14;
                d12 = d8 + d13;
            } else {
                d11 = d7 + d14;
                d12 = d8 - d13;
            }
            double atan2 = Math.atan2(d2 - d12, d - d11);
            double atan22 = Math.atan2(d4 - d12, d3 - d11) - atan2;
            if (z2 != (atan22 >= 0.0d)) {
                atan22 = atan22 > 0.0d ? atan22 - 6.283185307179586d : atan22 + 6.283185307179586d;
            }
            d11 *= (double) f5;
            d12 *= (double) f6;
            arcToBezier((d11 * cos) - (d12 * sin), (d11 * sin) + (d12 * cos), (double) f5, (double) f6, (double) f, (double) f2, toRadians, atan2, atan22);
        }
    }

    private void drawPath(PathInfo pathInfo) {
        switch (pathInfo.type) {
            case (byte) 1:
                this.path.moveTo(pathInfo.f10x, pathInfo.f11y);
                return;
            case (byte) 2:
                this.path.lineTo(pathInfo.f10x, pathInfo.f11y);
                return;
            case (byte) 3:
                this.path.quadTo(pathInfo.x1, pathInfo.y1, pathInfo.f10x, pathInfo.f11y);
                return;
            case (byte) 4:
                this.path.cubicTo(pathInfo.x1, pathInfo.y1, pathInfo.x2, pathInfo.y2, pathInfo.f10x, pathInfo.f11y);
                return;
            case (byte) 5:
                this.path.arcTo(new RectF(pathInfo.f10x, pathInfo.f11y, pathInfo.x1, pathInfo.y1), pathInfo.x2, pathInfo.y2);
                return;
            case (byte) 6:
                this.path.close();
                return;
            default:
                return;
        }
    }

    private void extract(String str, int i, ExtractFloatResult extractFloatResult) {
        int i2;
        Object obj = null;
        extractFloatResult.mEndWithNegSign = false;
        for (i2 = i; i2 < str.length(); i2++) {
            switch (str.charAt(i2)) {
                case ' ':
                case ',':
                    obj = 1;
                    break;
                case SemMotionRecognitionEvent.ROTATE_HORIZONTAL /*45*/:
                    if (i2 != i) {
                        obj = 1;
                        extractFloatResult.mEndWithNegSign = true;
                        break;
                    }
                    break;
            }
            if (obj != null) {
                extractFloatResult.mEndPosition = i2;
            }
        }
        extractFloatResult.mEndPosition = i2;
    }

    private float[] getFloats(String str) {
        if (str.charAt(0) == 'z' || str.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] fArr = new float[str.length()];
            int i = 1;
            ExtractFloatResult extractFloatResult = new ExtractFloatResult();
            int length = str.length();
            int i2 = 0;
            while (i < length) {
                int i3;
                extract(str, i, extractFloatResult);
                int i4 = extractFloatResult.mEndPosition;
                if (i < i4) {
                    i3 = i2 + 1;
                    fArr[i2] = Float.parseFloat(str.substring(i, i4));
                } else {
                    i3 = i2;
                }
                i = extractFloatResult.mEndWithNegSign ? i4 : i4 + 1;
                i2 = i3;
            }
            return Arrays.copyOf(fArr, i2);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    private int nextStart(String str, int i) {
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if ((charAt - 65) * (charAt - 90) <= 0 || (charAt - 97) * (charAt - 122) <= 0) {
                return i;
            }
            i++;
        }
        return i;
    }

    public void arcTo(float f, float f2, float f3, float f4, float f5, float f6) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 5;
        pathInfo.f10x = f;
        pathInfo.f11y = f2;
        pathInfo.x1 = f3;
        pathInfo.y1 = f4;
        pathInfo.x2 = f5;
        pathInfo.y2 = f6;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public SprObjectBase clone() throws CloneNotSupportedException {
        SprObjectShapePath sprObjectShapePath = (SprObjectShapePath) super.clone();
        if (this.mPathInfoList != null) {
            sprObjectShapePath.mPathInfoList = new ArrayList();
            for (PathInfo clone : this.mPathInfoList) {
                sprObjectShapePath.mPathInfoList.add(clone.clone());
            }
        }
        sprObjectShapePath.path = new Path(this.path);
        return sprObjectShapePath;
    }

    public void close() {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 6;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 4;
        pathInfo.f10x = f5;
        pathInfo.f11y = f6;
        pathInfo.x1 = f;
        pathInfo.y1 = f2;
        pathInfo.x2 = f3;
        pathInfo.y2 = f4;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        setShadowLayer();
        if (this.isVisibleFill) {
            canvas.drawPath(this.path, this.fillPaint);
        }
        if (this.isVisibleStroke) {
            canvas.drawPath(this.path, this.strokePaint);
        }
        clearShadowLayer();
        canvas.restore();
    }

    public void drawPath() {
        if (this.mPathInfoList != null) {
            this.path.reset();
            for (PathInfo drawPath : this.mPathInfoList) {
                drawPath(drawPath);
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (this.mPathInfoList != null) {
            this.mPathInfoList.clear();
        }
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        int readInt = sprInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            byte readByte = sprInputStream.readByte();
            switch (readByte) {
                case (byte) 1:
                    moveTo(sprInputStream.readFloat(), sprInputStream.readFloat());
                    break;
                case (byte) 2:
                    lineTo(sprInputStream.readFloat(), sprInputStream.readFloat());
                    break;
                case (byte) 3:
                    quadTo(sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat());
                    break;
                case (byte) 4:
                    cubicTo(sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat(), sprInputStream.readFloat());
                    break;
                case (byte) 5:
                    float readFloat = sprInputStream.readFloat();
                    float readFloat2 = sprInputStream.readFloat();
                    arcTo(readFloat, readFloat2, sprInputStream.readFloat() + readFloat, sprInputStream.readFloat() + readFloat2, sprInputStream.readFloat(), sprInputStream.readFloat());
                    break;
                case (byte) 6:
                    close();
                    break;
                default:
                    throw new RuntimeException("unsupported command type:" + readByte);
            }
        }
        super.fromSPR(sprInputStream);
    }

    public void fromXml(org.xmlpull.v1.XmlPullParser r1) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.samsung.android.graphics.spr.document.shape.SprObjectShapePath.fromXml(org.xmlpull.v1.XmlPullParser):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: not-int
	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:568)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:56)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:102)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.graphics.spr.document.shape.SprObjectShapePath.fromXml(org.xmlpull.v1.XmlPullParser):void");
    }

    public int getSPRSize() {
        if (this.mPathInfoList == null) {
            return 4;
        }
        int size = this.mPathInfoList.size() + 4;
        for (PathInfo pathInfo : this.mPathInfoList) {
            switch (pathInfo.type) {
                case (byte) 1:
                case (byte) 2:
                    size += 8;
                    break;
                case (byte) 3:
                    size += 16;
                    break;
                case (byte) 4:
                    size += 24;
                    break;
                case (byte) 5:
                    size += 24;
                    break;
                case (byte) 6:
                    break;
                default:
                    break;
            }
        }
        return super.getSPRSize() + size;
    }

    public int getTotalElementCount() {
        return 1;
    }

    public int getTotalSegmentCount() {
        return this.mPathInfoList == null ? 0 : this.mPathInfoList.size();
    }

    public void lineTo(float f, float f2) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 2;
        pathInfo.f10x = f;
        pathInfo.f11y = f2;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public void moveTo(float f, float f2) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 1;
        pathInfo.f10x = f;
        pathInfo.f11y = f2;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public void quadTo(float f, float f2, float f3, float f4) {
        PathInfo pathInfo = new PathInfo();
        pathInfo.type = (byte) 3;
        pathInfo.f10x = f3;
        pathInfo.f11y = f4;
        pathInfo.x1 = f;
        pathInfo.y1 = f2;
        if (this.mPathInfoList != null) {
            this.mPathInfoList.add(pathInfo);
        }
        drawPath(pathInfo);
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        if (this.mPathInfoList == null) {
            dataOutputStream.writeInt(0);
            return;
        }
        dataOutputStream.writeInt(this.mPathInfoList.size());
        for (PathInfo pathInfo : this.mPathInfoList) {
            dataOutputStream.writeByte(pathInfo.type);
            switch (pathInfo.type) {
                case (byte) 1:
                case (byte) 2:
                    dataOutputStream.writeFloat(pathInfo.f10x);
                    dataOutputStream.writeFloat(pathInfo.f11y);
                    break;
                case (byte) 3:
                    dataOutputStream.writeFloat(pathInfo.x1);
                    dataOutputStream.writeFloat(pathInfo.y1);
                    dataOutputStream.writeFloat(pathInfo.f10x);
                    dataOutputStream.writeFloat(pathInfo.f11y);
                    break;
                case (byte) 4:
                    dataOutputStream.writeFloat(pathInfo.x1);
                    dataOutputStream.writeFloat(pathInfo.y1);
                    dataOutputStream.writeFloat(pathInfo.x2);
                    dataOutputStream.writeFloat(pathInfo.y2);
                    dataOutputStream.writeFloat(pathInfo.f10x);
                    dataOutputStream.writeFloat(pathInfo.f11y);
                    break;
                case (byte) 5:
                    dataOutputStream.writeFloat(pathInfo.f10x);
                    dataOutputStream.writeFloat(pathInfo.f11y);
                    dataOutputStream.writeFloat(pathInfo.x1 - pathInfo.f10x);
                    dataOutputStream.writeFloat(pathInfo.y1 - pathInfo.f11y);
                    dataOutputStream.writeFloat(pathInfo.x2);
                    dataOutputStream.writeFloat(pathInfo.y2);
                    break;
                case (byte) 6:
                    break;
                default:
                    break;
            }
        }
        super.toSPR(dataOutputStream);
    }
}
