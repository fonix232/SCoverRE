package com.samsung.android.graphics.spr.document.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import com.samsung.android.graphics.spr.document.SprDocument;
import com.samsung.android.graphics.spr.document.SprInputStream;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeBase;
import com.samsung.android.graphics.spr.document.attribute.SprAttributeShadow;
import com.samsung.android.share.SShareConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SprObjectShapeGroup extends SprObjectBase {
    private static final String TAG = "SPRObjectShapeGroup";
    private boolean mIsInitialized;
    private final boolean mIsRoot;
    private ArrayList<SprObjectBase> mObjectList;

    public SprObjectShapeGroup(boolean z) {
        super((byte) 16);
        this.mIsInitialized = false;
        this.mObjectList = null;
        this.mObjectList = new ArrayList();
        this.mIsInitialized = true;
        this.mIsRoot = z;
    }

    public SprObjectShapeGroup(boolean z, SprInputStream sprInputStream) throws IOException {
        super((byte) 16);
        this.mIsInitialized = false;
        this.mObjectList = null;
        this.mObjectList = new ArrayList();
        this.mIsInitialized = true;
        this.mIsRoot = z;
        fromSPR(sprInputStream);
    }

    public SprObjectShapeGroup(boolean z, XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        super((byte) 16);
        this.mIsInitialized = false;
        this.mObjectList = null;
        this.mObjectList = new ArrayList();
        this.mIsInitialized = true;
        this.mIsRoot = z;
        fromXml(xmlPullParser);
    }

    public void appendObject(int i, SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            this.mObjectList.add(i, sprObjectBase);
        } else {
            Log.d(TAG, "Already finalize");
        }
    }

    public void appendObject(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            this.mObjectList.add(sprObjectBase);
        } else {
            Log.d(TAG, "Already finalize");
        }
    }

    public SprObjectShapeGroup clone() throws CloneNotSupportedException {
        SprObjectShapeGroup sprObjectShapeGroup = (SprObjectShapeGroup) super.clone();
        sprObjectShapeGroup.mObjectList = new ArrayList();
        for (SprObjectBase clone : this.mObjectList) {
            sprObjectShapeGroup.mObjectList.add(clone.clone());
        }
        return sprObjectShapeGroup;
    }

    public void draw(SprDocument sprDocument, Canvas canvas, float f, float f2, float f3) {
        canvas.save(31);
        float f4 = f3 * this.alpha;
        if (this.mAttributeList.size() > 0) {
            applyAttribute(sprDocument, canvas, f4);
        }
        int objectCount = getObjectCount();
        for (int i = 0; i < objectCount; i++) {
            SprObjectBase object = getObject(i);
            if (object != null) {
                object.draw(sprDocument, canvas, f, f2, f4);
            }
        }
        canvas.restore();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.mObjectList.clear();
        this.mIsInitialized = false;
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        int readInt = sprInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            byte readByte = sprInputStream.readByte();
            int i2 = 0;
            if (sprInputStream.mMajorVersion >= SprDocument.MAJOR_VERSION && sprInputStream.mMinorVersion >= (short) 12338) {
                i2 = sprInputStream.readInt();
            }
            long position = sprInputStream.getPosition();
            switch (readByte) {
                case (byte) 1:
                    this.mObjectList.add(new SprObjectShapeCircle(sprInputStream));
                    break;
                case (byte) 2:
                    this.mObjectList.add(new SprObjectShapeEllipse(sprInputStream));
                    break;
                case (byte) 3:
                    this.mObjectList.add(new SprObjectShapeLine(sprInputStream));
                    break;
                case (byte) 4:
                    this.mObjectList.add(new SprObjectShapePath(sprInputStream));
                    break;
                case (byte) 5:
                    this.mObjectList.add(new SprObjectShapeRectangle(sprInputStream));
                    break;
                case (byte) 16:
                    this.mObjectList.add(new SprObjectShapeGroup(false, sprInputStream));
                    break;
                case (byte) 17:
                    this.mObjectList.add(new SprObjectShapeUse(sprInputStream));
                    break;
                default:
                    Log.e(TAG, "unknown element type:" + readByte);
                    sprInputStream.skip((long) i2);
                    break;
            }
            if (sprInputStream.mMajorVersion >= SprDocument.MAJOR_VERSION && sprInputStream.mMinorVersion >= (short) 12338) {
                position = sprInputStream.getPosition() - position;
                if (position != ((long) i2)) {
                    throw new RuntimeException("Wrong skip size : " + position);
                }
            }
        }
        if (!this.mIsRoot) {
            super.fromSPR(sprInputStream);
        }
    }

    public void fromXml(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if (!("name".equals(attributeName) || "rotation".equals(attributeName) || "pivotX".equals(attributeName) || "pivotY".equals(attributeName) || "translateX".equals(attributeName) || "translateX".equals(attributeName) || "scaleX".equals(attributeName) || "scaleX".equals(attributeName) || !"alpha".equals(attributeName))) {
            }
        }
        int next = xmlPullParser.next();
        while (next != 1) {
            String name = xmlPullParser.getName();
            if (next == 2) {
                if (SShareConstants.SURVEY_DETAIL_FEATURE_CONTACTGROUP.equals(name)) {
                    this.mObjectList.add(new SprObjectShapeGroup(false, xmlPullParser));
                } else if ("path".equals(name)) {
                    this.mObjectList.add(new SprObjectShapePath(xmlPullParser));
                } else if ("clip-path".equals(name)) {
                }
            } else if (next == 3 && SShareConstants.SURVEY_DETAIL_FEATURE_CONTACTGROUP.equals(name)) {
                return;
            }
            next = xmlPullParser.next();
        }
    }

    public SprObjectBase getObject(int i) {
        if (this.mIsInitialized) {
            return (SprObjectBase) this.mObjectList.get(i);
        }
        Log.d(TAG, "Already finalize");
        return null;
    }

    public int getObjectCount() {
        if (this.mIsInitialized) {
            return this.mObjectList.size();
        }
        Log.d(TAG, "Already finalize");
        return 0;
    }

    public int getSPRSize() {
        int i = 4;
        for (SprAttributeBase sPRSize : this.mAttributeList) {
            i += sPRSize.getSPRSize() + 5;
        }
        return !this.mIsRoot ? i + super.getSPRSize() : i;
    }

    public int getTotalAttributeCount() {
        int i = 0;
        for (SprObjectBase totalAttributeCount : this.mObjectList) {
            i += totalAttributeCount.getTotalAttributeCount();
        }
        return this.mAttributeList.size() + i;
    }

    public int getTotalElementCount() {
        int i = 0;
        for (SprObjectBase totalElementCount : this.mObjectList) {
            i += totalElementCount.getTotalElementCount();
        }
        return i;
    }

    public int getTotalSegmentCount() {
        int i = 0;
        for (SprObjectBase totalSegmentCount : this.mObjectList) {
            i += totalSegmentCount.getTotalSegmentCount();
        }
        return i;
    }

    public void preDraw(SprDocument sprDocument, Paint paint, Paint paint2, boolean z, boolean z2, SprAttributeShadow sprAttributeShadow) {
        super.preDraw(sprDocument, paint, paint2, z, z2, sprAttributeShadow);
        int objectCount = getObjectCount();
        for (int i = 0; i < objectCount; i++) {
            SprObjectBase object = getObject(i);
            if (object != null) {
                object.preDraw(sprDocument, this.strokePaint, this.fillPaint, this.isVisibleStroke, this.isVisibleFill, this.shadow);
            }
        }
    }

    public SprObjectBase removeObject(int i) {
        if (this.mIsInitialized) {
            return (SprObjectBase) this.mObjectList.remove(i);
        }
        Log.d(TAG, "Already finalize");
        return null;
    }

    public boolean removeObject(SprObjectBase sprObjectBase) {
        if (this.mIsInitialized) {
            for (SprObjectBase sprObjectBase2 : this.mObjectList) {
                if (sprObjectBase2.mType == (byte) 16 && ((SprObjectShapeGroup) sprObjectBase2).removeObject(sprObjectBase)) {
                    return true;
                }
            }
            return this.mObjectList.remove(sprObjectBase);
        }
        Log.d(TAG, "Already finalize");
        return false;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.mObjectList.size());
        for (SprObjectBase sprObjectBase : this.mObjectList) {
            dataOutputStream.writeByte(sprObjectBase.mType);
            dataOutputStream.writeInt(sprObjectBase.getSPRSize());
            sprObjectBase.toSPR(dataOutputStream);
        }
        if (!this.mIsRoot) {
            super.toSPR(dataOutputStream);
        }
    }
}
