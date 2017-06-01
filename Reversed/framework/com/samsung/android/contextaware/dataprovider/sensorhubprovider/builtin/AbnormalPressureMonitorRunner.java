package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbnormalPressureMonitorRunner extends LibTypeProvider {
    private static final String LOG_FILE = "/data/log/CAE/phone_state.txt";
    private static final String LOG_FILE_DIR = "/data/log/CAE";

    public AbnormalPressureMonitorRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, null, iSensorHubResetObservable);
    }

    private String getDate(long j) {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(j));
    }

    private void recordAbnormalPressure() {
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
                    fileOutputStream2.write((getDate(System.currentTimeMillis()) + " - ABNORMAL PRESSURE DETECTED\n").getBytes());
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
        super.disable();
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ABNORMAL_PRESSURE_MONITOR.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"xaxis", "yaxis", "zaxis", "barometer"};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ABNORMAL_PRESSURE_MONITOR;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public int parse(byte[] bArr, int i) {
        int i2 = i;
        CaLogger.trace();
        String[] contextValueNames = getContextValueNames();
        if ((bArr.length - i) - 16 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        ContextBean contextBean = super.getContextBean();
        String str = contextValueNames[0];
        r5 = new byte[4];
        i2 = i + 1;
        r5[0] = bArr[i];
        int i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = contextValueNames[1];
        r5 = new byte[4];
        i2 = i3 + 1;
        r5[0] = bArr[i3];
        i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = contextValueNames[2];
        r5 = new byte[4];
        i2 = i3 + 1;
        r5[0] = bArr[i3];
        i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        contextBean = super.getContextBean();
        str = contextValueNames[3];
        r5 = new byte[4];
        i2 = i3 + 1;
        r5[0] = bArr[i3];
        i3 = i2 + 1;
        r5[1] = bArr[i2];
        i2 = i3 + 1;
        r5[2] = bArr[i3];
        i3 = i2 + 1;
        r5[3] = bArr[i2];
        contextBean.putContext(str, ((float) ByteBuffer.wrap(r5).getInt()) / 1000.0f);
        super.notifyObserver();
        return i3;
    }
}
