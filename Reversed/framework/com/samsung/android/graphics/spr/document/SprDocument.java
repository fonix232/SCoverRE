package com.samsung.android.graphics.spr.document;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Region.Op;
import android.util.Log;
import android.util.SparseArray;
import com.samsung.android.graphics.spr.animation.interpolator.SprTimeInterpolatorFactory;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeAnimatorSet;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeBase;
import com.samsung.android.graphics.spr.document.debug.SprDebug;
import com.samsung.android.graphics.spr.document.fileAttribute.SprFileAttributeBase;
import com.samsung.android.graphics.spr.document.fileAttribute.SprFileAttributeNinePatch;
import com.samsung.android.graphics.spr.document.shape.SprObjectBase;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeCircle;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeEllipse;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeGroup;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeLine;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapePath;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeRectangle;
import com.samsung.android.graphics.spr.document.shape.SprObjectShapeUse;
import com.samsung.android.widget.SemHoverPopupWindow.Gravity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SprDocument implements Cloneable {
    public static final int ANIMATION_MODE_BATTERY = 10;
    public static final int ANIMATION_MODE_NONE = 0;
    public static final int ANIMATION_MODE_STORAGE_SPACE = 11;
    public static final int ANIMATION_MODE_TIME_DAY_IN_WEEK = 9;
    public static final int ANIMATION_MODE_TIME_HOUR_IN_DAY = 4;
    public static final int ANIMATION_MODE_TIME_HOUR_IN_WEEK = 8;
    public static final int ANIMATION_MODE_TIME_MILLISECOND_IN_DAY = 1;
    public static final int ANIMATION_MODE_TIME_MILLISECOND_IN_WEEK = 5;
    public static final int ANIMATION_MODE_TIME_MINUTE_IN_DAY = 3;
    public static final int ANIMATION_MODE_TIME_MINUTE_IN_WEEK = 7;
    public static final int ANIMATION_MODE_TIME_SECOND_IN_DAY = 2;
    public static final int ANIMATION_MODE_TIME_SECOND_IN_WEEK = 6;
    public static final float DEFAULT_DENSITY_SCALE = 2.0f;
    public static final int HEADER_SIZE = 97;
    public static final short MAJOR_VERSION = (short) 12336;
    public static final short MINOR_VERSION = (short) 12340;
    public static final byte REPEAT_MODE_RESTART = (byte) 2;
    public static final byte REPEAT_MODE_REVERSE = (byte) 1;
    public static final int RESERVED_SIZE = 0;
    public static final int SPRTAG = 1397772800;
    public static final int SVFTAG = 1398162944;
    private static final String TAG = "SPRDocument";
    private static Paint mBasePaint = new Paint();
    private boolean isPredraw = false;
    public final int mAnimationInterval;
    public final int mAnimationMode;
    private ArrayList<SprObjectBase> mAnimationObject = new ArrayList();
    public final float mBottom;
    public final float mDensity;
    private ArrayList<SprObjectShapeGroup> mDocuments = new ArrayList();
    private ArrayList<SprFileAttributeBase> mFileAttributes = new ArrayList();
    protected final SprDocument mIntrinsic = this;
    private boolean mIsInitialized = false;
    public final float mLeft;
    private long mLoadingTime = 0;
    public final String mName;
    public final float mNinePatchBottom;
    public final float mNinePatchLeft;
    public final float mNinePatchRight;
    public final float mNinePatchTop;
    public final float mPaddingBottom;
    public final float mPaddingLeft;
    public final float mPaddingRight;
    public final float mPaddingTop;
    private SparseArray<SprObjectBase> mReferenceMap = new SparseArray();
    public final int mRepeatCount;
    public final byte mRepeatMode;
    public final float mRight;
    public final float mTop;

    public SprDocument(String str, float f, float f2, float f3, float f4) {
        this.mName = str.substring(str.lastIndexOf("/") + 1);
        this.mLeft = f;
        this.mTop = f2;
        this.mRight = f3;
        this.mBottom = f4;
        this.mNinePatchBottom = 0.0f;
        this.mNinePatchRight = 0.0f;
        this.mNinePatchTop = 0.0f;
        this.mNinePatchLeft = 0.0f;
        this.mPaddingBottom = 0.0f;
        this.mPaddingRight = 0.0f;
        this.mPaddingTop = 0.0f;
        this.mPaddingLeft = 0.0f;
        this.mDensity = DEFAULT_DENSITY_SCALE;
        this.mRepeatCount = 0;
        this.mRepeatMode = (byte) 2;
        this.mDocuments.add(new SprObjectShapeGroup(true));
        this.mIsInitialized = true;
        this.mAnimationMode = 0;
        this.mAnimationInterval = 0;
    }

    public SprDocument(String str, InputStream inputStream) throws IOException {
        this.mName = str.substring(str.lastIndexOf("/") + 1);
        SprInputStream sprInputStream = new SprInputStream(inputStream);
        sprInputStream.mAnimationObject = this.mAnimationObject;
        long currentTimeMillis = System.currentTimeMillis();
        int readInt = sprInputStream.readInt();
        sprInputStream.mMajorVersion = sprInputStream.readShort();
        sprInputStream.mMinorVersion = sprInputStream.readShort();
        int readInt2 = sprInputStream.readInt();
        int readInt3 = sprInputStream.readInt();
        int readInt4 = sprInputStream.readInt();
        sprInputStream.readInt();
        sprInputStream.readInt();
        this.mLeft = sprInputStream.readFloat();
        this.mTop = sprInputStream.readFloat();
        this.mRight = sprInputStream.readFloat();
        this.mBottom = sprInputStream.readFloat();
        this.mNinePatchLeft = sprInputStream.readFloat();
        this.mNinePatchTop = sprInputStream.readFloat();
        this.mNinePatchRight = sprInputStream.readFloat();
        this.mNinePatchBottom = sprInputStream.readFloat();
        this.mPaddingLeft = sprInputStream.readFloat();
        this.mPaddingTop = sprInputStream.readFloat();
        this.mPaddingRight = sprInputStream.readFloat();
        this.mPaddingBottom = sprInputStream.readFloat();
        this.mDensity = sprInputStream.readFloat();
        int i = 1;
        if (sprInputStream.mMajorVersion < (short) 12336 || sprInputStream.mMinorVersion < (short) 12339) {
            this.mRepeatCount = 0;
            this.mRepeatMode = (byte) 2;
        } else {
            i = sprInputStream.readInt();
            this.mRepeatCount = sprInputStream.readInt();
            this.mRepeatMode = sprInputStream.readByte();
        }
        if (sprInputStream.mMajorVersion < (short) 12336 || sprInputStream.mMinorVersion < (short) 12340) {
            this.mAnimationMode = 0;
            this.mAnimationInterval = 0;
        } else {
            this.mAnimationMode = sprInputStream.readInt();
            this.mAnimationInterval = sprInputStream.readInt();
        }
        if (readInt == 1397772800 || readInt == 1398162944) {
            int readInt5;
            int i2;
            byte readByte;
            int readInt6;
            if (readInt4 != 0) {
                sprInputStream.skip(((long) readInt4) - sprInputStream.getPosition());
                readInt5 = sprInputStream.readInt();
                for (i2 = 0; i2 < readInt5; i2++) {
                    Object sprFileAttributeNinePatch;
                    readByte = sprInputStream.readByte();
                    readInt6 = sprInputStream.readInt();
                    switch (readByte) {
                        case (byte) 1:
                            sprFileAttributeNinePatch = new SprFileAttributeNinePatch(sprInputStream);
                            break;
                        default:
                            sprFileAttributeNinePatch = null;
                            Log.e(TAG, "unknown element type:" + readByte);
                            sprInputStream.skip((long) readInt6);
                            break;
                    }
                    if (sprFileAttributeNinePatch != null) {
                        this.mFileAttributes.add(sprFileAttributeNinePatch);
                    }
                }
            }
            sprInputStream.skip(((long) readInt2) - sprInputStream.getPosition());
            readInt5 = sprInputStream.readInt();
            for (i2 = 0; i2 < readInt5; i2++) {
                Object sprObjectShapeCircle;
                sprInputStream.readInt();
                readByte = sprInputStream.readByte();
                readInt6 = 0;
                if (sprInputStream.mMajorVersion >= (short) 12336 && sprInputStream.mMinorVersion >= (short) 12338) {
                    readInt6 = sprInputStream.readInt();
                }
                switch (readByte) {
                    case (byte) 1:
                        sprObjectShapeCircle = new SprObjectShapeCircle(sprInputStream);
                        break;
                    case (byte) 2:
                        sprObjectShapeCircle = new SprObjectShapeEllipse(sprInputStream);
                        break;
                    case (byte) 3:
                        sprObjectShapeCircle = new SprObjectShapeLine(sprInputStream);
                        break;
                    case (byte) 4:
                        sprObjectShapeCircle = new SprObjectShapePath(sprInputStream);
                        break;
                    case (byte) 5:
                        sprObjectShapeCircle = new SprObjectShapeRectangle(sprInputStream);
                        break;
                    case (byte) 16:
                        sprObjectShapeCircle = new SprObjectShapeGroup(false, sprInputStream);
                        break;
                    case (byte) 17:
                        sprObjectShapeCircle = new SprObjectShapeUse(sprInputStream);
                        break;
                    default:
                        sprObjectShapeCircle = null;
                        Log.e(TAG, "unknown element type:" + readByte);
                        sprInputStream.skip((long) readInt6);
                        break;
                }
                if (sprObjectShapeCircle != null) {
                    this.mReferenceMap.append(i2, sprObjectShapeCircle);
                }
            }
            sprInputStream.skip(((long) readInt3) - sprInputStream.getPosition());
            for (i2 = 0; i2 < i; i2++) {
                this.mDocuments.add(new SprObjectShapeGroup(true, sprInputStream));
            }
            if (this.mAnimationMode >= 1 && this.mAnimationMode <= 8) {
                applyTimeAnimationMode();
            }
            this.mLoadingTime = System.currentTimeMillis() - currentTimeMillis;
            this.mIsInitialized = true;
            return;
        }
        throw new RuntimeException("wrong file format");
    }

    public SprDocument(String str, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        this.mName = str.substring(str.lastIndexOf("/") + 1);
        int next;
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            if ("width".equals(attributeName)) {
                if (attributeValue.endsWith("dp")) {
                    f3 = Float.valueOf(attributeValue.substring(0, attributeValue.length() - 2)).floatValue();
                }
            } else if (!"height".equals(attributeName)) {
                if ("viewportHeight".equals(attributeName)) {
                    f2 = Float.valueOf(attributeValue).floatValue();
                } else if ("viewportWidth".equals(attributeName)) {
                    f = Float.valueOf(attributeValue).floatValue();
                } else if (!("autoMirrored".equals(attributeName) || "tintMode".equals(attributeName) || !"tint".equals(attributeName))) {
                }
            }
        }
        this.mTop = 0.0f;
        this.mLeft = 0.0f;
        this.mRight = f;
        this.mBottom = f2;
        this.mDensity = this.mRight / f3;
        this.mNinePatchBottom = 0.0f;
        this.mNinePatchRight = 0.0f;
        this.mNinePatchTop = 0.0f;
        this.mNinePatchLeft = 0.0f;
        this.mPaddingBottom = 0.0f;
        this.mPaddingRight = 0.0f;
        this.mPaddingTop = 0.0f;
        this.mPaddingLeft = 0.0f;
        this.mRepeatCount = 0;
        this.mRepeatMode = (byte) 2;
        this.mAnimationMode = 0;
        this.mAnimationInterval = 0;
        SprObjectShapeGroup sprObjectShapeGroup = new SprObjectShapeGroup(true);
        sprObjectShapeGroup.appendObject(new SprObjectShapeGroup(false, xmlPullParser));
        this.mDocuments.add(sprObjectShapeGroup);
        this.mIsInitialized = true;
    }

    private void updateAnimationObjectList(SprObjectBase sprObjectBase) {
        int objectCount;
        int i;
        for (SprAttributeBase sprAttributeBase : sprObjectBase.mAttributeList) {
            if (sprAttributeBase.mType == SprAttributeBase.TYPE_ANIMATOR_SET) {
                for (Animator animator : ((SprAttributeAnimatorSet) sprAttributeBase).getAnimators()) {
                    switch (((SprAnimatorBase) animator).mType) {
                        case (byte) 4:
                            sprObjectBase.hasStrokeAnimation = true;
                            break;
                        case (byte) 5:
                            sprObjectBase.hasFillAnimation = true;
                            break;
                        default:
                            break;
                    }
                }
                this.mAnimationObject.add(sprObjectBase);
                if (sprObjectBase.mType == (byte) 16) {
                    objectCount = ((SprObjectShapeGroup) sprObjectBase).getObjectCount();
                    for (i = 0; i < objectCount; i++) {
                        updateAnimationObjectList(((SprObjectShapeGroup) sprObjectBase).getObject(i));
                    }
                }
            }
        }
        if (sprObjectBase.mType == (byte) 16) {
            objectCount = ((SprObjectShapeGroup) sprObjectBase).getObjectCount();
            for (i = 0; i < objectCount; i++) {
                updateAnimationObjectList(((SprObjectShapeGroup) sprObjectBase).getObject(i));
            }
        }
    }

    public void appendAnimator(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            this.mAnimationObject.add(sprObjectBase);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public void appendFileAttribute(SprFileAttributeBase sprFileAttributeBase) {
        if (this.mIsInitialized) {
            this.mFileAttributes.add(sprFileAttributeBase);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public void appendObject(int i, SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            ((SprObjectShapeGroup) this.mDocuments.get(0)).appendObject(i, sprObjectBase);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public void appendObject(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            ((SprObjectShapeGroup) this.mDocuments.get(0)).appendObject(sprObjectBase);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public void appendReference(int i, SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            this.mReferenceMap.append(i, sprObjectBase);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public void applyTimeAnimationMode() {
        for (SprObjectBase sprObjectBase : this.mAnimationObject) {
            for (SprAttributeBase sprAttributeBase : sprObjectBase.mAttributeList) {
                if (sprAttributeBase.mType == SprAttributeBase.TYPE_ANIMATOR_SET) {
                    SprAttributeAnimatorSet sprAttributeAnimatorSet = (SprAttributeAnimatorSet) sprAttributeBase;
                    int i = sprAttributeAnimatorSet.duration;
                    int i2 = 1;
                    int i3 = 1;
                    switch (this.mAnimationMode) {
                        case 1:
                            i2 = 1;
                            break;
                        case 2:
                            i2 = 1000;
                            break;
                        case 3:
                            i2 = 60000;
                            break;
                        case 4:
                            i2 = 3600000;
                            break;
                        case 5:
                            i3 = 2;
                            i2 = 1;
                            break;
                        case 6:
                            i3 = 2;
                            i2 = 1000;
                            break;
                        case 7:
                            i3 = 2;
                            i2 = 60000;
                            break;
                        case 8:
                            i3 = 2;
                            i2 = 3600000;
                            break;
                        case 9:
                            i3 = 2;
                            i2 = 86400000;
                            break;
                    }
                    sprAttributeAnimatorSet.updateAnimatorInterpolator(SprTimeInterpolatorFactory.get(this.mAnimationMode, i, i3, i2));
                }
            }
        }
    }

    public SprDocument clone() throws CloneNotSupportedException {
        SprDocument sprDocument = (SprDocument) super.clone();
        sprDocument.mReferenceMap = this.mReferenceMap.clone();
        sprDocument.mDocuments = new ArrayList();
        sprDocument.mAnimationObject = new ArrayList();
        for (SprObjectShapeGroup clone : this.mDocuments) {
            sprDocument.mDocuments.add(clone.clone());
            sprDocument.updateAnimationObjectList((SprObjectBase) sprDocument.mDocuments.get(sprDocument.mDocuments.size() - 1));
        }
        if (this.mAnimationMode >= 1 && this.mAnimationMode <= 8) {
            applyTimeAnimationMode();
        }
        return sprDocument;
    }

    public void close() {
        if (this.mIsInitialized) {
            this.mReferenceMap.clear();
            this.mReferenceMap = null;
            this.mDocuments.clear();
            this.mDocuments = null;
            this.mAnimationObject.clear();
            this.mAnimationObject = null;
            this.mIsInitialized = false;
            return;
        }
        Log.d(TAG, "Already closed");
    }

    public void draw(Canvas canvas, int i, int i2, int i3, int i4) {
        if (SprDebug.IsDebug) {
            SprDebug.drawRect(canvas, this, i, i2);
        }
        float f = ((float) i) / (this.mRight - this.mLeft);
        float f2 = ((float) i2) / (this.mBottom - this.mTop);
        canvas.save(31);
        Canvas canvas2 = canvas;
        canvas2.clipRect(this.mLeft, this.mTop, ((float) i) + this.mLeft, ((float) i2) + this.mTop, Op.INTERSECT);
        canvas.scale(f, f2);
        if (i3 < 0) {
            getObject().draw(this, canvas, f, f2, 1.0f);
        } else if (i3 < this.mDocuments.size()) {
            ((SprObjectShapeGroup) this.mDocuments.get(i3)).draw(this, canvas, f, f2, 1.0f);
        } else {
            ((SprObjectShapeGroup) this.mDocuments.get(this.mDocuments.size() - 1)).draw(this, canvas, f, f2, 1.0f);
        }
        canvas.restore();
        if (SprDebug.IsDebug) {
            SprDebug.drawDebugInfo(canvas, this, i, i2, i4);
        }
    }

    protected void finalize() throws Throwable {
        close();
    }

    public SprFileAttributeBase getFileAttribute(int i) {
        if (this.mIsInitialized) {
            return (SprFileAttributeBase) this.mFileAttributes.get(i);
        }
        Log.d(TAG, "Already closed");
        return null;
    }

    public int getFileAttributeSize() {
        if (this.mIsInitialized) {
            return this.mFileAttributes.size();
        }
        Log.d(TAG, "Already closed");
        return 0;
    }

    public int getFrameAnimationCount() {
        return this.mDocuments.size();
    }

    public int getLoadingTime() {
        return (int) this.mLoadingTime;
    }

    public SprObjectBase getObject() {
        return (SprObjectBase) this.mDocuments.get(0);
    }

    public SprObjectBase getReference(int i) {
        if (this.mIsInitialized) {
            return (SprObjectBase) this.mReferenceMap.get(i);
        }
        Log.d(TAG, "Already closed");
        return null;
    }

    public int getReferenceSize() {
        if (this.mIsInitialized) {
            return this.mReferenceMap.size();
        }
        Log.d(TAG, "Already closed");
        return 0;
    }

    public int getTotalAttributeCount() {
        return ((SprObjectShapeGroup) this.mDocuments.get(0)).getTotalAttributeCount();
    }

    public int getTotalElementCount() {
        return ((SprObjectShapeGroup) this.mDocuments.get(0)).getTotalElementCount();
    }

    public int getTotalSegmentCount() {
        return ((SprObjectShapeGroup) this.mDocuments.get(0)).getTotalSegmentCount();
    }

    public ArrayList<SprObjectBase> getValueAnimationObjects() {
        return this.mAnimationObject;
    }

    public boolean isIntrinsic() {
        return this.mIntrinsic == this;
    }

    public boolean isNinePatch() {
        return this.mNinePatchLeft > 0.0f || this.mNinePatchTop > 0.0f || this.mNinePatchRight > 0.0f || this.mNinePatchBottom > 0.0f;
    }

    public boolean isPredraw() {
        return this.isPredraw;
    }

    public void preDraw(int i) {
        Paint paint = new Paint(mBasePaint);
        Paint paint2 = new Paint(mBasePaint);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint2.setAntiAlias(true);
        paint2.setStyle(Style.FILL);
        if (i < 0) {
            getObject().preDraw(this, paint, paint2, false, false, null);
        } else if (i < this.mDocuments.size()) {
            ((SprObjectShapeGroup) this.mDocuments.get(i)).preDraw(this, paint, paint2, false, false, null);
        } else {
            ((SprObjectShapeGroup) this.mDocuments.get(this.mDocuments.size() - 1)).preDraw(this, paint, paint2, false, false, null);
        }
        if (i <= 0) {
            this.isPredraw = true;
        }
    }

    public boolean removeAnimator(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            return this.mAnimationObject.remove(sprObjectBase);
        }
        Log.d(TAG, "Already closed");
        return false;
    }

    public SprObjectBase removeObject(int i) {
        if (this.mIsInitialized) {
            return ((SprObjectShapeGroup) this.mDocuments.get(0)).removeObject(i);
        }
        Log.d(TAG, "Already closed");
        return null;
    }

    public boolean removeObject(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            return ((SprObjectShapeGroup) this.mDocuments.get(0)).removeObject(sprObjectBase);
        }
        Log.d(TAG, "Already closed");
        return false;
    }

    public void removeReference(int i, SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            this.mReferenceMap.remove(i);
        } else {
            Log.d(TAG, "Already closed");
        }
    }

    public boolean toSPR(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        float f = this.mNinePatchLeft;
        float f2 = this.mNinePatchTop;
        float f3 = this.mNinePatchRight;
        float f4 = this.mNinePatchBottom;
        if (this.mIsInitialized) {
            int i;
            int i2 = 0;
            int i3 = 0;
            if (!this.mFileAttributes.isEmpty()) {
                for (SprFileAttributeBase sprFileAttributeBase : this.mFileAttributes) {
                    if (!sprFileAttributeBase.isValid()) {
                        switch (sprFileAttributeBase.mType) {
                            case (byte) 1:
                                SprFileAttributeNinePatch sprFileAttributeNinePatch = (SprFileAttributeNinePatch) sprFileAttributeBase;
                                if (sprFileAttributeNinePatch.xSize == 1 && sprFileAttributeNinePatch.ySize == 1) {
                                    f = sprFileAttributeNinePatch.xStart[0];
                                    f2 = sprFileAttributeNinePatch.yStart[0];
                                    f3 = this.mRight - sprFileAttributeNinePatch.xEnd[0];
                                    f4 = this.mBottom - sprFileAttributeNinePatch.yEnd[0];
                                    break;
                                }
                            default:
                                break;
                        }
                    }
                    i2 += sprFileAttributeBase.getSPRSize() + 5;
                    i3++;
                }
                i2 += i2 == 0 ? 0 : 4;
            }
            int i4 = 4;
            for (i = 0; i < this.mReferenceMap.size(); i++) {
                i4 += ((SprObjectBase) this.mReferenceMap.valueAt(i)).getSPRSize();
            }
            dataOutputStream.writeInt(SPRTAG);
            dataOutputStream.writeShort(Gravity.TOP_ABOVE);
            dataOutputStream.writeShort(EGL10.EGL_TRANSPARENT_TYPE);
            dataOutputStream.writeInt(i2 + 97);
            dataOutputStream.writeInt((i2 + 97) + i4);
            dataOutputStream.writeInt(i2 == 0 ? 0 : 97);
            dataOutputStream.writeInt(0);
            dataOutputStream.writeInt(0);
            dataOutputStream.writeFloat(this.mLeft);
            dataOutputStream.writeFloat(this.mTop);
            dataOutputStream.writeFloat(this.mRight);
            dataOutputStream.writeFloat(this.mBottom);
            dataOutputStream.writeFloat(f);
            dataOutputStream.writeFloat(f2);
            dataOutputStream.writeFloat(f3);
            dataOutputStream.writeFloat(f4);
            dataOutputStream.writeFloat(this.mPaddingLeft);
            dataOutputStream.writeFloat(this.mPaddingTop);
            dataOutputStream.writeFloat(this.mPaddingRight);
            dataOutputStream.writeFloat(this.mPaddingBottom);
            dataOutputStream.writeFloat(this.mDensity);
            dataOutputStream.writeInt(this.mDocuments.size());
            dataOutputStream.writeInt(this.mRepeatCount);
            dataOutputStream.writeByte(this.mRepeatMode);
            dataOutputStream.writeInt(this.mAnimationMode);
            dataOutputStream.writeInt(this.mAnimationInterval);
            if (i2 != 0) {
                dataOutputStream.writeInt(i3);
                for (SprFileAttributeBase sprFileAttributeBase2 : this.mFileAttributes) {
                    if (sprFileAttributeBase2.isValid()) {
                        dataOutputStream.writeByte(sprFileAttributeBase2.mType);
                        dataOutputStream.writeInt(sprFileAttributeBase2.getSPRSize());
                        sprFileAttributeBase2.toSPR(dataOutputStream);
                    }
                }
            }
            dataOutputStream.writeInt(this.mReferenceMap.size());
            int size = this.mReferenceMap.size();
            for (i = 0; i < size; i++) {
                SprObjectBase sprObjectBase = (SprObjectBase) this.mReferenceMap.valueAt(i);
                dataOutputStream.writeInt(this.mReferenceMap.keyAt(i));
                dataOutputStream.writeByte(sprObjectBase.mType);
                sprObjectBase.toSPR(dataOutputStream);
            }
            for (SprObjectShapeGroup toSPR : this.mDocuments) {
                toSPR.toSPR(dataOutputStream);
            }
            return true;
        }
        Log.d(TAG, "Already closed");
        return false;
    }
}
