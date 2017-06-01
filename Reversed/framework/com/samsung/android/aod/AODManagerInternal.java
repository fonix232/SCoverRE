package com.samsung.android.aod;

import com.samsung.android.aod.AODManager.AODChangeListener;
import java.util.List;

public abstract class AODManagerInternal {
    public abstract boolean isAODState();

    public abstract void screenTurningOn(AODChangeListener aODChangeListener);

    public abstract boolean startAOD();

    public abstract boolean stopAOD();

    public abstract void updateCalendarData(List<String> list, List<String> list2);

    public abstract void updateNotificationKeys(int i, List<String> list);

    public abstract boolean wakeUp();
}
