package com.samsung.android.contextaware.utilbundle.autotest;

import android.content.Context;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;

public class CaAutoTestScenarioManager {
    public static final int AP_POWER_AUTO_TEST = 3;
    public static final int BYPASS_AUTO_TEST = 1;
    public static final int CA_OPERATION_AUTO_TEST = 4;
    public static final int CA_OPERATION_DEBUGGING = 5;
    public static final int LIBRARY_AUTO_TEST = 2;
    public static final int SENSOR_HUB_OPERATION_DEBUGGING = 6;
    private final CaOperationDebugging mCaOperationDebugging = new CaOperationDebugging(0);
    private final Context mContext;
    private final ArrayList<ICaAutoTest> mScenarioListForTest = new ArrayList();
    private final SensorHubOperationDebugging mSensorHubOperationDebugging;

    public CaAutoTestScenarioManager(Context context) {
        this.mContext = context;
        this.mSensorHubOperationDebugging = new SensorHubOperationDebugging(context, 0);
    }

    public final void initilizeAutoTest() {
        if (this.mScenarioListForTest != null) {
            this.mScenarioListForTest.clear();
        }
        this.mCaOperationDebugging.clearPacket();
        this.mSensorHubOperationDebugging.clearPacket();
    }

    public final boolean setScenarioForDebugging(int i, int i2, byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        switch (i) {
            case 5:
                this.mCaOperationDebugging.setDelayTime(i2);
                this.mCaOperationDebugging.addPacket(bArr);
                if (!this.mScenarioListForTest.contains(this.mCaOperationDebugging)) {
                    this.mScenarioListForTest.add(this.mCaOperationDebugging);
                    break;
                }
                break;
            case 6:
                this.mSensorHubOperationDebugging.setDelayTime(i2);
                this.mSensorHubOperationDebugging.addPacket(bArr);
                if (!this.mScenarioListForTest.contains(this.mSensorHubOperationDebugging)) {
                    this.mScenarioListForTest.add(this.mSensorHubOperationDebugging);
                    break;
                }
                break;
        }
        return true;
    }

    public final boolean setScenarioForTest(int i, int i2) {
        switch (i) {
            case 1:
                this.mScenarioListForTest.add(new BypassStressTest(this.mContext, i2));
                break;
            case 2:
                this.mScenarioListForTest.add(new LibraryStressTest(this.mContext, i2));
                break;
            case 3:
                this.mScenarioListForTest.add(new ApPowerStressTest(i2));
                break;
            case 4:
                this.mScenarioListForTest.add(new CaOperationStressTest(i2));
                break;
        }
        return true;
    }

    public final void startAutoTest() {
        if (this.mScenarioListForTest == null || this.mScenarioListForTest.isEmpty()) {
            CaLogger.error("Scenario list is empty.");
            return;
        }
        for (ICaAutoTest iCaAutoTest : this.mScenarioListForTest) {
            iCaAutoTest.setStopFlag(false);
            new Thread(iCaAutoTest).start();
        }
    }

    public final void stopAutoTest() {
        if (this.mScenarioListForTest != null && !this.mScenarioListForTest.isEmpty()) {
            for (ICaAutoTest stopAutoTest : this.mScenarioListForTest) {
                stopAutoTest.stopAutoTest();
            }
            initilizeAutoTest();
        }
    }
}
