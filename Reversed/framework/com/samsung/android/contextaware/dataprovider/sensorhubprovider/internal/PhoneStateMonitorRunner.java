package com.samsung.android.contextaware.dataprovider.sensorhubprovider.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings.System;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IntLibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCoverManager;
import com.samsung.android.contextaware.utilbundle.ICoverStatusChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.cover.CoverState;
import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneStateMonitorRunner extends IntLibTypeProvider implements ICoverStatusChangeObserver {
    private static final int COVER_CLOSE = 0;
    private static final int COVER_OPEN = 1;
    private static final int COVER_TYPE_FLIP = 1;
    private static final int COVER_TYPE_NONE = 0;
    private static final int COVER_TYPE_VIEW = 2;
    private static final String GET_PHONE_STATE_ACTION = "com.samsung.android.contextaware.GET_PHONE_STATE";
    private static final String LOG_FILE = "/data/log/CAE/phone_state.txt";
    private static final String LOG_FILE_DIR = "/data/log/CAE";
    private static final int MSG_COVER_STATE = 65261;
    private static final int MSG_TIMER_EXPIRED = 65263;
    private static final int TURN_OVER_LIGHTING_DISABLED = 0;
    private static final int TURN_OVER_LIGHTING_ENABLED = 1;
    private static final long WAIT_RESPONSE_TIME = 200;
    private Context mContext = null;
    private Handler mHandler;
    private final Looper mLooper;
    private final BroadcastReceiver mReceiver = new C00001();

    class C00001 extends BroadcastReceiver {
        C00001() {
        }

        public final void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaLogger.error(" context is null");
            } else if (intent == null) {
                CaLogger.error(" intent is null");
            } else if (intent.getAction().equals(PhoneStateMonitorRunner.GET_PHONE_STATE_ACTION)) {
                CaLogger.info(intent.toString());
                PhoneStateMonitorRunner.this.getState();
            }
        }
    }

    private enum ContextName {
        Movement(0),
        LcdDirect(1),
        Embower(2),
        FinalLcdOff(3),
        LcdOffInference(4),
        LcdOffRecommend(5),
        TimeStamp(6);
        
        private int val;

        private ContextName(int i) {
            this.val = i;
        }
    }

    public PhoneStateMonitorRunner(int i, Context context, Looper looper, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, looper, iSensorHubResetObservable);
        this.mContext = context;
        this.mLooper = looper;
        createHandler();
    }

    private void createHandler() {
        this.mHandler = new Handler(this.mLooper) {
            public void handleMessage(Message message) {
                if (message.what == PhoneStateMonitorRunner.MSG_TIMER_EXPIRED) {
                    String[] contextValueNames = PhoneStateMonitorRunner.this.getContextValueNames();
                    if (super.isDisable()) {
                        CaLogger.debug("runner disabled");
                        return;
                    }
                    PhoneStateMonitorRunner.this.getContextBean().putContext(contextValueNames[ContextName.Movement.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(contextValueNames[ContextName.LcdDirect.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(contextValueNames[ContextName.Embower.val], 0);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(contextValueNames[ContextName.FinalLcdOff.val], false);
                    PhoneStateMonitorRunner.this.getContextBean().putContext(contextValueNames[ContextName.LcdOffInference.val], false);
                    PhoneStateMonitorRunner.this.notifyObserver();
                }
            }
        };
    }

    private String getDate(long j) {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(j));
    }

    private void getState() {
        int i = 1;
        if (!super.isDisable()) {
            byte[] bArr = new byte[4];
            bArr[0] = (byte) 0;
            bArr[1] = (byte) 3;
            if (!CaCoverManager.getInstance(this.mLooper).getCoverState()) {
                i = 0;
            }
            bArr[2] = (byte) i;
            bArr[3] = (byte) CaCoverManager.getInstance(this.mLooper).getCoverType();
            sendCmdToSensorHub((byte) -72, getInstLibType(), bArr);
            this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
            this.mHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, WAIT_RESPONSE_TIME);
        }
    }

    private int isTurnOverLighting() {
        return (System.getInt(this.mContext.getContentResolver(), "turn_over_lighting", 0) == 1 ? 1 : null) != null ? 1 : 0;
    }

    private void recordPhoneDrop() {
        Throwable e;
        Throwable th;
        File file = new File(LOG_FILE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(new File(LOG_FILE), true);
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.write((getDate(System.currentTimeMillis()) + " - PHONE DROP DETECTED\n").getBytes());
                } catch (IOException e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                    try {
                        CaLogger.error(e.toString());
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e3) {
                                e3.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
            }
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (Throwable e322) {
                    e322.printStackTrace();
                }
            }
            fileOutputStream = fileOutputStream2;
        } catch (IOException e4) {
            e322 = e4;
            CaLogger.error(e322.toString());
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public final void disable() {
        CaLogger.trace();
        CaCoverManager.getInstance(this.mLooper).unregisterObserver(this);
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        IntentFilter intentFilter = new IntentFilter(GET_PHONE_STATE_ACTION);
        CaCoverManager.getInstance(this.mLooper).registerObserver(this);
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_PHONE_STATE_MONITOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"movement", "lcddirect", "embower", "finalLcdOff", "lcdOffInference", "lcdOffRecommend", "timestamp"};
    }

    protected final byte[] getDataPacketToRegisterLib() {
        int i = 1;
        byte[] bArr = new byte[5];
        bArr[0] = (byte) 0;
        bArr[1] = (byte) 1;
        bArr[2] = (byte) isTurnOverLighting();
        bArr[3] = (byte) CaCoverManager.getInstance(this.mLooper).getCoverType();
        if (!CaCoverManager.getInstance(this.mLooper).getCoverState()) {
            i = 0;
        }
        bArr[4] = (byte) i;
        return bArr;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 2};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT80;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void notifyInitContext() {
        if (ListenerListManager.getInstance().getUsedTotalCount(getContextType()) == 1) {
            super.notifyInitContext();
        }
    }

    public void onCoverStatusChanged(CoverState coverState) {
        int i = coverState.getSwitchState() ? 1 : 0;
        CaLogger.info("Cover status:" + i);
        if (super.isDisable()) {
            CaLogger.warning("runner disabled");
        } else {
            sendPropertyValueToSensorHub(SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT80, (byte) 1, CaConvertUtil.intToByteArr(i, 1));
        }
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.info("parse start:" + i);
        String[] contextValueNames = getContextValueNames();
        if ((bArr.length - i) - 6 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        i2 = ((i + 1) + 1) + 1;
        int i3 = i2 + 1;
        boolean z = bArr[i2] != (byte) 0;
        i2 = i3 + 1;
        int i4 = bArr[i3];
        i3 = i2 + 1;
        int i5 = bArr[i2];
        super.getContextBean().putContext(contextValueNames[ContextName.LcdDirect.val], i4);
        super.getContextBean().putContext(contextValueNames[ContextName.Embower.val], i5);
        super.getContextBean().putContext(contextValueNames[ContextName.LcdOffRecommend.val], z);
        super.getContextBean().putContext(contextValueNames[ContextName.TimeStamp.val], System.currentTimeMillis());
        super.notifyObserver();
        return i3;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i == 1) {
            getState();
        }
        return true;
    }
}
