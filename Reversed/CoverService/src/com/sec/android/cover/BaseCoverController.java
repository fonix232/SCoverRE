package com.sec.android.cover;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import com.samsung.android.sdk.cover.ScoverState;
import com.sec.android.cover.ledcover.reflection.hardware.RefInputManager;
import com.sec.android.cover.monitor.CoverUpdateMonitor.RemoteViewInfo;

public class BaseCoverController {
    private static final String TAG = "BaseCoverController";
    protected Context mContext;
    private ScoverState mCoverState;

    public interface OnRemoteViewUpdateListener {
        void onRemoteViewUpdated(RemoteViewInfo remoteViewInfo);
    }

    public BaseCoverController(Context context) {
        this.mContext = context;
        Log.d(TAG, getClass().getSimpleName() + " created");
    }

    public ScoverState getCoverState() {
        return this.mCoverState;
    }

    public void setCoverState(ScoverState coverState) {
        this.mCoverState = coverState;
    }

    public boolean isCoverOpen() {
        if (this.mCoverState == null) {
            Log.e(TAG, "isCoverOpen : mCoverState is null!");
            return true;
        } else if (!this.mCoverState.getSwitchState()) {
            return false;
        } else {
            return true;
        }
    }

    public int getCoverType() {
        return this.mCoverState.getType();
    }

    public void onCoverAttached(ScoverState state) {
        this.mCoverState = state;
    }

    public void onCoverDetatched(ScoverState state) {
        this.mCoverState = state;
    }

    public void onCoverEvent(ScoverState state) {
        this.mCoverState = state;
    }

    protected int getCurrentUserId() {
        return ActivityManager.semGetCurrentUser();
    }

    protected void coverEventFinished() {
        RefInputManager.get().coverEventFinished(RefInputManager.get().getInstance());
    }
}
