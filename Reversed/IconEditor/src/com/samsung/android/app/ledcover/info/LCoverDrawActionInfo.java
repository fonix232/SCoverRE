package com.samsung.android.app.ledcover.info;

public class LCoverDrawActionInfo {
    private int endPosition;
    private boolean isDraw;
    private int startPosition;

    public LCoverDrawActionInfo(int startPosition, int endPosition, boolean isDraw) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isDraw = isDraw;
    }

    public int getStartPosition() {
        return this.startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return this.endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public boolean isDraw() {
        return this.isDraw;
    }

    public void setDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }
}
