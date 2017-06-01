package com.sec.android.cover.manager;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import com.sec.android.cover.BaseCoverObservator;
import com.sec.android.cover.Constants;
import com.sec.android.cover.ledcover.reflection.os.RefPowerManager;

public class CoverPowerManager extends BaseCoverObservator {
    private static final String TAG = "CoverPowerManager";
    private PowerManager mPMS = ((PowerManager) getContext().getSystemService("power"));
    private WakeLock mWakeLock = this.mPMS.newWakeLock(1, "SViewCoverBaseService.mCoverStateWakeLock");

    public CoverPowerManager(Context context) {
        super(context);
        Log.d(Constants.TAG, "create CoverPowerManager");
        this.mWakeLock.setReferenceCounted(false);
    }

    public void wakeUpWithReason() {
        Log.d(TAG, "wakeUpWithReason");
        if (this.mPMS != null) {
            this.mPMS.semWakeUp(SystemClock.uptimeMillis(), RefPowerManager.get().WAKE_UP_REASON_COVER_OPEN);
        }
    }

    public void goToSleep() {
        Log.d(TAG, "goToSleep");
        if (this.mPMS != null) {
            this.mPMS.semGoToSleep(SystemClock.uptimeMillis());
        }
    }

    public float getCurrentBrightness() {
        if (this.mPMS != null) {
            RefPowerManager.get().getCurrentBrightness(this.mPMS, false);
        }
        Log.d(TAG, "current_brightness = " + 0.0f);
        return 0.0f;
    }

    public void start() {
    }

    public void stop() {
    }
}
