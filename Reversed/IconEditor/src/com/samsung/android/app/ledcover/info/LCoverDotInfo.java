package com.samsung.android.app.ledcover.info;

import android.widget.ImageView;

public class LCoverDotInfo {
    private char dotByteData;
    private boolean dotEnable;
    private ImageView dotImageData;
    private int dotPosition;

    public LCoverDotInfo() {
        this.dotByteData = Defines.DOT_DISABLE;
        this.dotPosition = 0;
        this.dotEnable = false;
        this.dotImageData = null;
    }

    public LCoverDotInfo(int position, boolean selected) {
        this.dotPosition = position;
        this.dotEnable = selected;
        if (this.dotEnable) {
            this.dotByteData = Defines.DOT_ENABLE;
        } else {
            this.dotByteData = Defines.DOT_DISABLE;
        }
    }

    public int getDotPosition() {
        return this.dotPosition;
    }

    public void setDotPosition(int dotPosition) {
        this.dotPosition = dotPosition;
    }

    public boolean isDotEnable() {
        return this.dotEnable;
    }

    public void setDotEnable(boolean dotEnable) {
        this.dotEnable = dotEnable;
        if (dotEnable) {
            this.dotByteData = Defines.DOT_ENABLE;
        } else {
            this.dotByteData = Defines.DOT_DISABLE;
        }
    }

    public ImageView getDotImageData() {
        return this.dotImageData;
    }

    public void setDotImageData(ImageView dotImageData) {
        this.dotImageData = dotImageData;
    }

    public char getDotByteData() {
        return this.dotByteData;
    }
}
