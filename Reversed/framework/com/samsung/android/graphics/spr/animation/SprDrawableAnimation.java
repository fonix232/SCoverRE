package com.samsung.android.graphics.spr.animation;

import android.graphics.drawable.Drawable;
import com.samsung.android.graphics.spr.document.SprDocument;

public abstract class SprDrawableAnimation implements Runnable {
    private static final int DEFAULT_FRAME_DURATION = 16;
    public static final byte TYPE_FRAMEANIMATION = (byte) 2;
    public static final byte TYPE_NONE = (byte) 0;
    public static final byte TYPE_VALUEANIMATION = (byte) 1;
    protected final SprDocument mDocument;
    protected final Drawable mDrawable;
    protected final int mInterval;
    protected boolean mIsRunning = false;
    public final byte mType;

    public SprDrawableAnimation(byte b, Drawable drawable, SprDocument sprDocument) {
        int i = 16;
        if (drawable == null) {
            throw new RuntimeException("A drawable is not allocated.");
        } else if (sprDocument == null) {
            throw new RuntimeException("A document is not allocated.");
        } else {
            this.mType = b;
            this.mDrawable = drawable;
            this.mDocument = sprDocument;
            if (this.mDocument.mAnimationInterval >= 16) {
                i = this.mDocument.mAnimationInterval;
            }
            this.mInterval = i;
        }
    }

    public int getAnimationIndex() {
        return 0;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public void start() {
        if (this.mIsRunning) {
            stop();
        }
        this.mIsRunning = true;
    }

    public void stop() {
        this.mDrawable.unscheduleSelf(this);
        this.mIsRunning = false;
    }

    public void update() {
    }
}
