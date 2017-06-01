package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.util.Log;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeAnimatorSet;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeBase;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeClip;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeClipPath;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeFill;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeMatrix;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeShadow;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStroke;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStrokeLinecap;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStrokeLinejoin;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStrokeMiterlimit;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeStrokeWidth;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SprObjectBase implements Cloneable {
    private static final String TAG = SprObjectBase.class.getSimpleName();
    public static final byte TYPE_CIRCLE = (byte) 1;
    public static final byte TYPE_ELLIPSE = (byte) 2;
    public static final byte TYPE_GROUP = (byte) 16;
    public static final byte TYPE_LINE = (byte) 3;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_PATH = (byte) 4;
    public static final byte TYPE_RECTANGLE = (byte) 5;
    public static final byte TYPE_USE = (byte) 17;
    private static final Cap[] sCapArray = new Cap[]{Cap.BUTT, Cap.ROUND, Cap.SQUARE};
    private static final Join[] sJoinArray = new Join[]{Join.MITER, Join.ROUND, Join.BEVEL};
    public float alpha = 1.0f;
    public Paint fillPaint;
    public boolean hasFillAnimation = false;
    public boolean hasStrokeAnimation = false;
    public boolean isVisibleFill = false;
    public boolean isVisibleStroke = false;
    public ArrayList<SprAttributeBase> mAttributeList = new ArrayList();
    protected final SprObjectBase mIntrinsic = this;
    public final byte mType;
    public SprAttributeShadow shadow = null;
    public Paint strokePaint;

    protected SprObjectBase(byte b) {
        this.mType = b;
    }

    private void applyPreAttribute(Paint paint, Paint paint2) {
        for (SprAttributeBase sprAttributeBase : this.mAttributeList) {
            switch (sprAttributeBase.mType) {
                case (byte) 1:
                case (byte) 3:
                case (byte) 64:
                case (byte) 97:
                    break;
                case (byte) 32:
                    SprAttributeFill sprAttributeFill = (SprAttributeFill) sprAttributeBase;
                    switch (sprAttributeFill.colorType) {
                        case (byte) 0:
                            this.isVisibleFill = false;
                            break;
                        case (byte) 1:
                            this.isVisibleFill = true;
                            paint2.setShader(null);
                            paint2.setColor(sprAttributeFill.color);
                            break;
                        case (byte) 2:
                            break;
                        case (byte) 3:
                        case (byte) 4:
                            this.isVisibleFill = true;
                            paint2.setShader(sprAttributeFill.gradient.shader);
                            break;
                        default:
                            break;
                    }
                case (byte) 35:
                    SprAttributeStroke sprAttributeStroke = (SprAttributeStroke) sprAttributeBase;
                    switch (sprAttributeStroke.colorType) {
                        case (byte) 0:
                            this.isVisibleStroke = false;
                            break;
                        case (byte) 1:
                            this.isVisibleStroke = true;
                            paint.setShader(null);
                            paint.setColor(sprAttributeStroke.color);
                            break;
                        case (byte) 2:
                            break;
                        case (byte) 3:
                        case (byte) 4:
                            this.isVisibleStroke = true;
                            paint.setShader(sprAttributeStroke.gradient.shader);
                            break;
                        default:
                            break;
                    }
                case SemMotionRecognitionEvent.SHORT_SHAKE /*37*/:
                    paint.setStrokeCap(sCapArray[((SprAttributeStrokeLinecap) sprAttributeBase).linecap - 1]);
                    break;
                case SemMotionRecognitionEvent.SHORT_SHAKE_START /*38*/:
                    paint.setStrokeJoin(sJoinArray[((SprAttributeStrokeLinejoin) sprAttributeBase).linejoin - 1]);
                    break;
                case SemMotionRecognitionEvent.BT_SHARING_RECEIVE_READY /*40*/:
                    paint.setStrokeWidth(((SprAttributeStrokeWidth) sprAttributeBase).strokeWidth);
                    break;
                case (byte) 41:
                    paint.setStrokeMiter(((SprAttributeStrokeMiterlimit) sprAttributeBase).miterLimit);
                    break;
                case SemMotionRecognitionEvent.SMART_SCROLL_CAMERA_ON /*112*/:
                    this.shadow = (SprAttributeShadow) sprAttributeBase;
                    break;
                default:
                    Log.d(TAG, "Attribute type = " + sprAttributeBase.mType + "is not supported type");
                    break;
            }
        }
    }

    private void loadAttributeFromSPR(SprInputStream sprInputStream) throws IOException {
        this.mAttributeList.clear();
        int readInt = sprInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            byte readByte = sprInputStream.readByte();
            int i2 = 0;
            if (sprInputStream.mMajorVersion >= SprDocument.MAJOR_VERSION && sprInputStream.mMinorVersion >= (short) 12338) {
                i2 = sprInputStream.readInt();
            }
            switch (readByte) {
                case (byte) 0:
                    break;
                case (byte) 1:
                    this.mAttributeList.add(new SprAttributeClip(sprInputStream));
                    break;
                case (byte) 3:
                    this.mAttributeList.add(new SprAttributeClipPath(sprInputStream));
                    break;
                case (byte) 32:
                    this.mAttributeList.add(new SprAttributeFill(sprInputStream));
                    break;
                case (byte) 35:
                    this.mAttributeList.add(new SprAttributeStroke(sprInputStream));
                    break;
                case SemMotionRecognitionEvent.SHORT_SHAKE /*37*/:
                    this.mAttributeList.add(new SprAttributeStrokeLinecap(sprInputStream));
                    break;
                case SemMotionRecognitionEvent.SHORT_SHAKE_START /*38*/:
                    this.mAttributeList.add(new SprAttributeStrokeLinejoin(sprInputStream));
                    break;
                case SemMotionRecognitionEvent.BT_SHARING_RECEIVE_READY /*40*/:
                    this.mAttributeList.add(new SprAttributeStrokeWidth(sprInputStream));
                    break;
                case (byte) 41:
                    this.mAttributeList.add(new SprAttributeStrokeMiterlimit(sprInputStream));
                    break;
                case (byte) 64:
                    this.mAttributeList.add(new SprAttributeMatrix(sprInputStream));
                    break;
                case (byte) 97:
                    this.mAttributeList.add(new SprAttributeAnimatorSet(sprInputStream));
                    sprInputStream.mAnimationObject.add(this);
                    break;
                case SemMotionRecognitionEvent.SMART_SCROLL_CAMERA_ON /*112*/:
                    this.mAttributeList.add(new SprAttributeShadow(sprInputStream));
                    break;
                default:
                    Log.e(TAG, "Unknown attribute id:" + readByte);
                    sprInputStream.skip((long) i2);
                    break;
            }
        }
    }

    private void saveAttributeToSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.mAttributeList.size());
        for (SprAttributeBase sprAttributeBase : this.mAttributeList) {
            dataOutputStream.writeByte(sprAttributeBase.mType);
            dataOutputStream.writeInt(sprAttributeBase.getSPRSize());
            sprAttributeBase.toSPR(dataOutputStream);
        }
    }

    public void appendAttribute(SprAttributeBase sprAttributeBase) {
        this.mAttributeList.add(sprAttributeBase);
    }

    public void applyAttribute(SprDocument sprDocument, Canvas canvas, float f) {
        for (SprAttributeBase sprAttributeBase : this.mAttributeList) {
            switch (sprAttributeBase.mType) {
                case (byte) 1:
                    SprAttributeClip sprAttributeClip = (SprAttributeClip) sprAttributeBase;
                    canvas.clipRect(sprAttributeClip.left, sprAttributeClip.top, sprAttributeClip.right, sprAttributeClip.bottom, Op.INTERSECT);
                    break;
                case (byte) 3:
                    SprObjectBase reference = sprDocument.getReference(((SprAttributeClipPath) sprAttributeBase).link);
                    if (reference == null) {
                        break;
                    }
                    Path path;
                    switch (reference.mType) {
                        case (byte) 1:
                            path = new Path();
                            path.addCircle(((SprObjectShapeCircle) reference).cx, ((SprObjectShapeCircle) reference).cy, ((SprObjectShapeCircle) reference).cr, Direction.CW);
                            canvas.clipPath(path);
                            break;
                        case (byte) 2:
                            path = new Path();
                            path.addOval(new RectF(((SprObjectShapeEllipse) reference).left, ((SprObjectShapeEllipse) reference).top, ((SprObjectShapeEllipse) reference).right, ((SprObjectShapeEllipse) reference).bottom), Direction.CW);
                            canvas.clipPath(path);
                            break;
                        case (byte) 3:
                        case (byte) 16:
                        case (byte) 17:
                            break;
                        case (byte) 4:
                            canvas.clipPath(((SprObjectShapePath) reference).path, Op.INTERSECT);
                            break;
                        case (byte) 5:
                            canvas.clipRect(((SprObjectShapeRectangle) reference).left, ((SprObjectShapeRectangle) reference).top, ((SprObjectShapeRectangle) reference).right, ((SprObjectShapeRectangle) reference).bottom, Op.INTERSECT);
                            break;
                        default:
                            break;
                    }
                case (byte) 32:
                    if (this.isVisibleFill && this.fillPaint != null) {
                        if (getIntrinsic().fillPaint == null) {
                            this.fillPaint.setAlpha((int) (255.0f * f));
                            break;
                        } else {
                            this.fillPaint.setAlpha((int) (((float) getIntrinsic().fillPaint.getAlpha()) * f));
                            break;
                        }
                    }
                case (byte) 35:
                    if (this.isVisibleStroke && this.strokePaint != null) {
                        if (getIntrinsic().strokePaint == null) {
                            this.strokePaint.setAlpha((int) (255.0f * f));
                            break;
                        } else {
                            this.strokePaint.setAlpha((int) (((float) getIntrinsic().strokePaint.getAlpha()) * f));
                            break;
                        }
                    }
                case SemMotionRecognitionEvent.SHORT_SHAKE /*37*/:
                case SemMotionRecognitionEvent.SHORT_SHAKE_START /*38*/:
                case SemMotionRecognitionEvent.BT_SHARING_RECEIVE_READY /*40*/:
                case (byte) 41:
                case (byte) 97:
                    break;
                case (byte) 64:
                    canvas.concat(((SprAttributeMatrix) sprAttributeBase).matrix);
                    break;
                default:
                    Log.d(TAG, "Attribute type = " + sprAttributeBase.mType + "is not supported type");
                    break;
            }
        }
    }

    protected void clearShadowLayer() {
        if (this.shadow != null) {
            this.fillPaint.clearShadowLayer();
            this.strokePaint.clearShadowLayer();
        }
    }

    public SprObjectBase clone() throws CloneNotSupportedException {
        SprObjectBase sprObjectBase = (SprObjectBase) super.clone();
        sprObjectBase.mAttributeList = new ArrayList();
        for (SprAttributeBase clone : this.mAttributeList) {
            sprObjectBase.mAttributeList.add(clone.clone());
        }
        if (this.strokePaint != null) {
            sprObjectBase.strokePaint = new Paint(this.strokePaint);
        }
        if (this.fillPaint != null) {
            sprObjectBase.fillPaint = new Paint(this.fillPaint);
        }
        sprObjectBase.alpha = this.alpha;
        return sprObjectBase;
    }

    public abstract void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3);

    protected void finalize() throws Throwable {
        super.finalize();
        this.mAttributeList.clear();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        loadAttributeFromSPR(sprInputStream);
    }

    public SprObjectBase getIntrinsic() {
        return this.mIntrinsic;
    }

    public int getSPRSize() {
        int i = 4;
        for (SprAttributeBase sPRSize : this.mAttributeList) {
            i += sPRSize.getSPRSize() + 5;
        }
        return i;
    }

    public int getTotalAttributeCount() {
        return this.mAttributeList.size();
    }

    public abstract int getTotalElementCount();

    public abstract int getTotalSegmentCount();

    public void preDraw(SprDocument sprDocument) {
        if (this.strokePaint != null && this.fillPaint != null) {
            preDraw(sprDocument, this.strokePaint, this.fillPaint, this.isVisibleStroke, this.isVisibleFill, this.shadow);
        }
    }

    public void preDraw(SprDocument sprDocument, Paint paint, Paint paint2, boolean z, boolean z2, SprAttributeShadow sprAttributeShadow) {
        Paint paint3 = paint;
        Paint paint4 = paint2;
        this.isVisibleStroke = z;
        this.isVisibleFill = z2;
        this.shadow = sprAttributeShadow;
        if (this.mAttributeList.size() > 0) {
            if (this.strokePaint == null) {
                paint3 = paint != null ? new Paint(paint) : new Paint();
            } else {
                paint3 = this.strokePaint;
                if (paint != null) {
                    paint3.setShader(paint.getShader());
                    paint3.setColorFilter(paint.getColorFilter());
                }
            }
            if (this.fillPaint == null) {
                paint4 = paint2 != null ? new Paint(paint2) : new Paint();
            } else {
                paint4 = this.fillPaint;
                if (paint2 != null) {
                    paint4.setShader(paint2.getShader());
                    paint4.setColorFilter(paint2.getColorFilter());
                }
            }
            applyPreAttribute(paint3, paint4);
        }
        this.fillPaint = paint4;
        this.strokePaint = paint3;
    }

    public void removeAttribute(SprAttributeBase sprAttributeBase) {
        this.mAttributeList.remove(sprAttributeBase);
    }

    protected void setShadowLayer() {
        if (this.shadow != null) {
            float f;
            if (this.isVisibleFill) {
                f = this.shadow.radius;
                if (this.isVisibleStroke) {
                    f += this.strokePaint.getStrokeWidth();
                }
                if (f > 0.5f) {
                    f = (f - 0.5f) / 0.57735f;
                }
                this.fillPaint.setShadowLayer(f, this.shadow.dx, this.shadow.dy, this.shadow.shadowColor);
            } else if (this.isVisibleStroke) {
                f = this.shadow.radius;
                if (f > 0.5f) {
                    f = (f - 0.5f) / 0.57735f;
                }
                this.strokePaint.setShadowLayer(this.shadow.radius, this.shadow.dx, this.shadow.dy, this.shadow.shadowColor);
            }
        }
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        saveAttributeToSPR(dataOutputStream);
    }
}
