package com.samsung.android.graphics.spr.animation;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import com.samsung.android.graphics.spr.document.SprDocument;

public class SprDrawableAnimationFrame extends SprDrawableAnimation {
    private int mCurrentFrameIndex = 0;
    private final int mFrameCount = this.mDocument.getFrameAnimationCount();
    private final int mTotalFrameCount = (this.mFrameCount * this.mDocument.mRepeatCount);

    public SprDrawableAnimationFrame(Drawable drawable, SprDocument sprDocument) {
        super((byte) 2, drawable, sprDocument);
    }

    public int getAnimationIndex() {
        if (this.mDocument.mRepeatMode == (byte) 2) {
            return this.mCurrentFrameIndex % this.mFrameCount;
        }
        int i = this.mCurrentFrameIndex % (this.mFrameCount * 2);
        return i < this.mFrameCount ? i : (this.mFrameCount - (i % this.mFrameCount)) - 1;
    }

    public void run() {
        this.mCurrentFrameIndex++;
        if (this.mDocument.mRepeatCount == 0 || this.mCurrentFrameIndex < this.mTotalFrameCount) {
            this.mDrawable.scheduleSelf(this, SystemClock.uptimeMillis() + ((long) this.mInterval));
            if (this.mDocument.mRepeatCount == 0 && this.mCurrentFrameIndex > this.mFrameCount * 2) {
                this.mCurrentFrameIndex -= this.mFrameCount * 2;
            }
        } else {
            this.mIsRunning = false;
        }
        this.mDrawable.invalidateSelf();
    }

    public void start() {
        super.start();
        this.mCurrentFrameIndex = 0;
        this.mDrawable.scheduleSelf(this, SystemClock.uptimeMillis());
    }
}
