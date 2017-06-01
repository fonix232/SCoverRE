package com.samsung.android.graphics.spr.document.animator;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import com.samsung.android.gesture.SemMotionRecognitionEvent;
import com.samsung.android.graphics.spr.animation.interpolator.BackEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.BackEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.BackEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.BounceEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.BounceEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.BounceEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.CircEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.CircEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.CircEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.CubicEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.CubicEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.CubicEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.ElasticEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.ElasticEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.ElasticEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.ExpoEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.ExpoEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.ExpoEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuadEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.QuadEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuadEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuartEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.QuartEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuartEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuintEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.QuintEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuintEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.QuintOut50;
import com.samsung.android.graphics.spr.animation.interpolator.QuintOut80;
import com.samsung.android.graphics.spr.animation.interpolator.SineEaseIn;
import com.samsung.android.graphics.spr.animation.interpolator.SineEaseInOut;
import com.samsung.android.graphics.spr.animation.interpolator.SineEaseOut;
import com.samsung.android.graphics.spr.animation.interpolator.SineIn33;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut33;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut50;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut60;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut70;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut80;
import com.samsung.android.graphics.spr.animation.interpolator.SineInOut90;
import com.samsung.android.graphics.spr.animation.interpolator.SineOut33;
import com.samsung.android.graphics.spr.document.SprInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class SprAnimatorBase extends ValueAnimator implements Cloneable {
    public static final byte INTERPOLATOR_TYPE_ACCELERATE = (byte) 2;
    public static final byte INTERPOLATOR_TYPE_ACCELERATE_DECELERATE = (byte) 1;
    public static final byte INTERPOLATOR_TYPE_ANTICIPATE = (byte) 3;
    public static final byte INTERPOLATOR_TYPE_ANTICIPATE_OVERSHOOT = (byte) 4;
    public static final byte INTERPOLATOR_TYPE_BACKEASEIN = (byte) 10;
    public static final byte INTERPOLATOR_TYPE_BACKEASEINOUT = (byte) 12;
    public static final byte INTERPOLATOR_TYPE_BACKEASEOUT = (byte) 11;
    public static final byte INTERPOLATOR_TYPE_BOUNCE = (byte) 5;
    public static final byte INTERPOLATOR_TYPE_BOUNCEEASEIN = (byte) 13;
    public static final byte INTERPOLATOR_TYPE_BOUNCEEASEINOUT = (byte) 15;
    public static final byte INTERPOLATOR_TYPE_BOUNCEEASEOUT = (byte) 14;
    public static final byte INTERPOLATOR_TYPE_CIRCEASEIN = (byte) 16;
    public static final byte INTERPOLATOR_TYPE_CIRCEASEINOUT = (byte) 18;
    public static final byte INTERPOLATOR_TYPE_CIRCEASEOUT = (byte) 17;
    public static final byte INTERPOLATOR_TYPE_CUBICEASEIN = (byte) 19;
    public static final byte INTERPOLATOR_TYPE_CUBICEASEINOUT = (byte) 21;
    public static final byte INTERPOLATOR_TYPE_CUBICEASEOUT = (byte) 20;
    public static final byte INTERPOLATOR_TYPE_CYCLE = (byte) 6;
    public static final byte INTERPOLATOR_TYPE_DECELERATE = (byte) 7;
    public static final byte INTERPOLATOR_TYPE_ELASTICEASEIN = (byte) 22;
    public static final byte INTERPOLATOR_TYPE_ELASTICEASEINOUT = (byte) 24;
    public static final byte INTERPOLATOR_TYPE_ELASTICEASEOUT = (byte) 23;
    public static final byte INTERPOLATOR_TYPE_EXPOEASEIN = (byte) 25;
    public static final byte INTERPOLATOR_TYPE_EXPOEASEINOUT = (byte) 27;
    public static final byte INTERPOLATOR_TYPE_EXPOEASEOUT = (byte) 26;
    public static final byte INTERPOLATOR_TYPE_LINEAR = (byte) 8;
    public static final byte INTERPOLATOR_TYPE_OVERSHOOT = (byte) 9;
    public static final byte INTERPOLATOR_TYPE_QUADEASEIN = (byte) 28;
    public static final byte INTERPOLATOR_TYPE_QUADEASEINOUT = (byte) 30;
    public static final byte INTERPOLATOR_TYPE_QUADEASEOUT = (byte) 29;
    public static final byte INTERPOLATOR_TYPE_QUARTEASEIN = (byte) 31;
    public static final byte INTERPOLATOR_TYPE_QUARTEASEINOUT = (byte) 33;
    public static final byte INTERPOLATOR_TYPE_QUARTEASEOUT = (byte) 32;
    public static final byte INTERPOLATOR_TYPE_QUINTEASEIN = (byte) 34;
    public static final byte INTERPOLATOR_TYPE_QUINTEASEINOUT = (byte) 36;
    public static final byte INTERPOLATOR_TYPE_QUINTEASEOUT = (byte) 35;
    public static final byte INTERPOLATOR_TYPE_QUINTOUT50 = (byte) 40;
    public static final byte INTERPOLATOR_TYPE_QUINTOUT80 = (byte) 41;
    public static final byte INTERPOLATOR_TYPE_SINEEASEIN = (byte) 37;
    public static final byte INTERPOLATOR_TYPE_SINEEASEINOUT = (byte) 39;
    public static final byte INTERPOLATOR_TYPE_SINEEASEOUT = (byte) 38;
    public static final byte INTERPOLATOR_TYPE_SINEIN33 = (byte) 42;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT33 = (byte) 43;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT50 = (byte) 44;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT60 = (byte) 45;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT70 = (byte) 46;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT80 = (byte) 47;
    public static final byte INTERPOLATOR_TYPE_SINEINOUT90 = (byte) 48;
    public static final byte INTERPOLATOR_TYPE_SINEOUT33 = (byte) 49;
    public static final byte REPEAT_MODE_RESTART = (byte) 2;
    public static final byte REPEAT_MODE_REVERSE = (byte) 1;
    public static final byte TYPE_ALPHA = (byte) 6;
    public static final byte TYPE_FILL_COLOR = (byte) 5;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_ROTATE = (byte) 3;
    public static final byte TYPE_SCALE = (byte) 2;
    public static final byte TYPE_STROKE_COLOR = (byte) 4;
    public static final byte TYPE_TRANSLATE = (byte) 1;
    private float mInterpolatorAmplitude = 0.0f;
    private float mInterpolatorCycle = 0.0f;
    private float mInterpolatorOvershot = 0.0f;
    private float mInterpolatorPeriod = 0.0f;
    private byte mInterpolatorType = (byte) 1;
    protected final SprAnimatorBase mIntrinsic;
    public final byte mType;

    public static class UpdateParameter {
        public float alpha;
        public int fillColor;
        public boolean isLastFrame;
        public boolean isUpdatedFillColor;
        public boolean isUpdatedRotate;
        public boolean isUpdatedScale;
        public boolean isUpdatedStrokeColor;
        public boolean isUpdatedTranslate;
        public float rotateDegree;
        public float rotatePivotX;
        public float rotatePivotY;
        public float scalePivotX;
        public float scalePivotY;
        public float scaleX;
        public float scaleY;
        public int strokeColor;
        public float translateDx;
        public float translateDy;
    }

    protected SprAnimatorBase(byte b) {
        this.mType = b;
        this.mIntrinsic = this;
    }

    public SprAnimatorBase clone() {
        return (SprAnimatorBase) super.clone();
    }

    public void fromSPR(SprInputStream sprInputStream) throws IOException {
        this.mInterpolatorType = sprInputStream.readByte();
        int readInt = sprInputStream.readInt();
        byte[] bArr = new byte[readInt];
        sprInputStream.read(bArr, 0, readInt);
        if (this.mInterpolatorType == (byte) 6) {
            this.mInterpolatorCycle = ByteBuffer.wrap(bArr).getFloat();
            setInterpolatorCycle(this.mInterpolatorType, this.mInterpolatorCycle);
        } else if (this.mInterpolatorType >= (byte) 10 && this.mInterpolatorType <= INTERPOLATOR_TYPE_BACKEASEOUT) {
            this.mInterpolatorOvershot = ByteBuffer.wrap(bArr).getFloat();
            setInterpolatorBackEase(this.mInterpolatorType, this.mInterpolatorOvershot);
        } else if (this.mInterpolatorType < INTERPOLATOR_TYPE_ELASTICEASEIN || this.mInterpolatorType > INTERPOLATOR_TYPE_ELASTICEASEOUT) {
            setInterpolator(this.mInterpolatorType);
        } else {
            this.mInterpolatorAmplitude = ByteBuffer.wrap(bArr).getFloat();
            this.mInterpolatorPeriod = ByteBuffer.wrap(bArr).getFloat();
            setInterpolatorElastic(this.mInterpolatorType, this.mInterpolatorAmplitude, this.mInterpolatorPeriod);
        }
        int readInt2 = sprInputStream.readInt();
        int readInt3 = sprInputStream.readInt();
        setStartDelay((long) readInt2);
        setDuration((long) readInt3);
        switch (sprInputStream.readByte()) {
            case (byte) 1:
                setRepeatMode(2);
                break;
            case (byte) 2:
                setRepeatMode(1);
                break;
            default:
                setRepeatMode(1);
                break;
        }
        setRepeatCount(sprInputStream.readInt());
    }

    public int getSPRSize() {
        return this.mInterpolatorType == (byte) 6 ? 22 : 18;
    }

    public void setInterpolator(byte b) {
        switch (b) {
            case (byte) 1:
                setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case (byte) 3:
                setInterpolator(new AnticipateInterpolator());
                break;
            case (byte) 4:
                setInterpolator(new AccelerateDecelerateInterpolator());
                break;
            case (byte) 5:
                setInterpolator(new BounceInterpolator());
                break;
            case (byte) 7:
                setInterpolator(new DecelerateInterpolator());
                break;
            case (byte) 8:
                setInterpolator(new LinearInterpolator());
                break;
            case (byte) 9:
                setInterpolator(new OvershootInterpolator());
                break;
            case (byte) 10:
                setInterpolator(new BackEaseIn());
                break;
            case (byte) 11:
                setInterpolator(new BackEaseOut());
                break;
            case (byte) 12:
                setInterpolator(new BackEaseInOut());
                break;
            case (byte) 13:
                setInterpolator(new BounceEaseIn());
                break;
            case (byte) 14:
                setInterpolator(new BounceEaseOut());
                break;
            case (byte) 15:
                setInterpolator(new BounceEaseInOut());
                break;
            case (byte) 16:
                setInterpolator(new CircEaseIn());
                break;
            case (byte) 17:
                setInterpolator(new CircEaseOut());
                break;
            case (byte) 18:
                setInterpolator(new CircEaseInOut());
                break;
            case (byte) 19:
                setInterpolator(new CubicEaseIn());
                break;
            case (byte) 20:
                setInterpolator(new CubicEaseOut());
                break;
            case (byte) 21:
                setInterpolator(new CubicEaseInOut());
                break;
            case (byte) 22:
                setInterpolator(new ElasticEaseIn());
                break;
            case (byte) 23:
                setInterpolator(new ElasticEaseOut());
                break;
            case (byte) 24:
                setInterpolator(new ElasticEaseInOut());
                break;
            case (byte) 25:
                setInterpolator(new ExpoEaseIn());
                break;
            case (byte) 26:
                setInterpolator(new ExpoEaseOut());
                break;
            case (byte) 27:
                setInterpolator(new ExpoEaseInOut());
                break;
            case (byte) 28:
                setInterpolator(new QuadEaseIn());
                break;
            case (byte) 29:
                setInterpolator(new QuadEaseOut());
                break;
            case (byte) 30:
                setInterpolator(new QuadEaseInOut());
                break;
            case (byte) 31:
                setInterpolator(new QuartEaseIn());
                break;
            case (byte) 32:
                setInterpolator(new QuartEaseOut());
                break;
            case (byte) 33:
                setInterpolator(new QuartEaseInOut());
                break;
            case (byte) 34:
                setInterpolator(new QuintEaseIn());
                break;
            case (byte) 35:
                setInterpolator(new QuintEaseOut());
                break;
            case (byte) 36:
                setInterpolator(new QuintEaseInOut());
                break;
            case SemMotionRecognitionEvent.SHORT_SHAKE /*37*/:
                setInterpolator(new SineEaseIn());
                break;
            case SemMotionRecognitionEvent.SHORT_SHAKE_START /*38*/:
                setInterpolator(new SineEaseOut());
                break;
            case (byte) 39:
                setInterpolator(new SineEaseInOut());
                break;
            case SemMotionRecognitionEvent.BT_SHARING_RECEIVE_READY /*40*/:
                setInterpolator(new QuintOut50());
                break;
            case (byte) 41:
                setInterpolator(new QuintOut80());
                break;
            case (byte) 42:
                setInterpolator(new SineIn33());
                break;
            case (byte) 43:
                setInterpolator(new SineInOut33());
                break;
            case (byte) 44:
                setInterpolator(new SineInOut50());
                break;
            case SemMotionRecognitionEvent.ROTATE_HORIZONTAL /*45*/:
                setInterpolator(new SineInOut60());
                break;
            case (byte) 46:
                setInterpolator(new SineInOut70());
                break;
            case (byte) 47:
                setInterpolator(new SineInOut80());
                break;
            case (byte) 48:
                setInterpolator(new SineInOut90());
                break;
            case (byte) 49:
                setInterpolator(new SineOut33());
                break;
            default:
                throw new RuntimeException("Unexpected interpolatorType : " + b);
        }
        this.mInterpolatorType = b;
    }

    public void setInterpolatorBackEase(byte b, float f) {
        switch (b) {
            case (byte) 10:
                setInterpolator(new BackEaseIn(f));
                break;
            case (byte) 11:
                setInterpolator(new BackEaseOut(f));
                break;
            case (byte) 12:
                setInterpolator(new BackEaseInOut(f));
                break;
            default:
                throw new RuntimeException("Unexpected interpolatorType : " + b);
        }
        this.mInterpolatorType = b;
        this.mInterpolatorOvershot = f;
    }

    public void setInterpolatorCycle(byte b, float f) {
        switch (b) {
            case (byte) 6:
                setInterpolator(new CycleInterpolator(f));
                this.mInterpolatorType = b;
                this.mInterpolatorCycle = f;
                return;
            default:
                throw new RuntimeException("Unexpected interpolatorType : " + b);
        }
    }

    public void setInterpolatorElastic(byte b, float f, float f2) {
        switch (b) {
            case (byte) 22:
                setInterpolator(new ElasticEaseIn(f, f2));
                break;
            case (byte) 23:
                setInterpolator(new ElasticEaseOut(f, f2));
                break;
            case (byte) 24:
                setInterpolator(new ElasticEaseInOut(f, f2));
                break;
            default:
                throw new RuntimeException("Unexpected interpolatorType : " + b);
        }
        this.mInterpolatorType = b;
        this.mInterpolatorAmplitude = f;
        this.mInterpolatorPeriod = f2;
    }

    public void toSPR(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.mInterpolatorType);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.mInterpolatorType == (byte) 6) {
            new DataOutputStream(byteArrayOutputStream).writeFloat(this.mInterpolatorCycle);
        } else if (this.mInterpolatorType >= (byte) 10 && this.mInterpolatorType <= INTERPOLATOR_TYPE_BACKEASEOUT) {
            new DataOutputStream(byteArrayOutputStream).writeFloat(this.mInterpolatorOvershot);
        } else if (this.mInterpolatorType >= INTERPOLATOR_TYPE_ELASTICEASEIN && this.mInterpolatorType <= INTERPOLATOR_TYPE_ELASTICEASEOUT) {
            DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream2.writeFloat(this.mInterpolatorAmplitude);
            dataOutputStream2.writeFloat(this.mInterpolatorPeriod);
        }
        dataOutputStream.writeInt(byteArrayOutputStream.size());
        if (byteArrayOutputStream.size() > 0) {
            dataOutputStream.write(byteArrayOutputStream.toByteArray());
        }
        dataOutputStream.writeInt((int) getStartDelay());
        dataOutputStream.writeInt((int) getDuration());
        switch (getRepeatMode()) {
            case 1:
                dataOutputStream.writeByte(2);
                break;
            case 2:
                dataOutputStream.writeByte(1);
                break;
            default:
                dataOutputStream.writeByte(2);
                break;
        }
        dataOutputStream.writeInt(getRepeatCount());
    }

    public boolean update(UpdateParameter updateParameter) {
        synchronized (this) {
            if (getCurrentPlayTime() > 0 || updateParameter.isLastFrame) {
                boolean updateValues = updateValues(updateParameter);
                return updateValues;
            }
            return false;
        }
    }

    public abstract boolean updateValues(UpdateParameter updateParameter);
}
