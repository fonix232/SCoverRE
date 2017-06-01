package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.UserHandle;
import com.samsung.android.bridge.multiwindow.MultiWindowManagerBridge;
import com.samsung.android.contextaware.ContextAwareManager;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaAutoBrightnessTableManager;
import com.samsung.android.contextaware.utilbundle.CaBootStatus;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.SensorHubCommManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class PowerResetNotiParser implements ISensorHubParser, ISensorHubResetObservable {
    private static final String PERMISSION_SENSORHUB_RESET = "com.samsung.permission.SENSORHUB_RESET";
    private static final int SLPI_CMD_RESET_STATE = 0;
    private static final int SLPI_CMD_RESTORE_STATE = 1;
    private static final int SLPI_CMD_UNKNOWN_STATE = -1;
    private static final String SLPI_RESET_STATUS = "restrict";
    private static final String SLPI_RESET_STATUS_PATH = "/sys/class/sensors/ssc_core/operation_mode";
    private static final String SLPI_RESTORE_STATUS = "normal";
    private static final String SLPI_UNKNOWN_STATUS = "unknown";
    Context mContext = null;
    private String mLastStatus = "unknown";
    private final CopyOnWriteArrayList<ISensorHubResetObserver> mListeners = new CopyOnWriteArrayList();
    private SLPIResetObserver mResetObserver;
    private long mSensorHubResetCnt = 0;

    class SLPIResetObserver extends FileObserver {
        private Context mContext;

        SLPIResetObserver(String str, Context context) {
            super(str);
            this.mContext = context;
        }

        public void onEvent(int i, String str) {
            if (PowerResetNotiParser.this.isSLPISupported() && (i & 2) == 2) {
                String -wrap1 = PowerResetNotiParser.this.getFileData(PowerResetNotiParser.SLPI_RESET_STATUS_PATH);
                CaLogger.info("CTS status : " + -wrap1);
                if (PowerResetNotiParser.this.mLastStatus.compareTo(-wrap1) != 0) {
                    int sendCmdToSensorHub;
                    if ("normal".compareTo(-wrap1) == 0) {
                        CaLogger.info("SLPI status : SLPI_RESTORE_STATUS");
                        sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(CaConvertUtil.intToByteArr(1, 1), new byte[]{ISensorHubCmdProtocol.INST_LIB_PUTVALUE, ISensorHubCmdProtocol.TYPE_SLPI_RESET_STATE});
                        if (sendCmdToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
                            CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
                        }
                        PowerResetNotiParser.this.notifySensorHubResetObserver(-43);
                    } else if (PowerResetNotiParser.SLPI_RESET_STATUS.compareTo(-wrap1) == 0) {
                        CaLogger.info("SLPI status : SLPI_RESET_STATUS");
                        sendCmdToSensorHub = SensorHubCommManager.getInstance().sendCmdToSensorHub(CaConvertUtil.intToByteArr(0, 1), new byte[]{ISensorHubCmdProtocol.INST_LIB_PUTVALUE, ISensorHubCmdProtocol.TYPE_SLPI_RESET_STATE});
                        if (sendCmdToSensorHub != SensorHubErrors.SUCCESS.getCode()) {
                            CaLogger.error(SensorHubErrors.getMessage(sendCmdToSensorHub));
                        }
                    } else {
                        CaLogger.info("Status of SLPI is invalid");
                        return;
                    }
                    PowerResetNotiParser.this.mLastStatus = -wrap1;
                } else {
                    CaLogger.info("Status of SLPI is same so skip event!!");
                }
            }
        }
    }

    public PowerResetNotiParser(Context context) {
        this.mContext = context;
        if (isSLPISupported()) {
            this.mResetObserver = new SLPIResetObserver(SLPI_RESET_STATUS_PATH, context);
            if (this.mResetObserver != null) {
                this.mResetObserver.startWatching();
                CaLogger.info("SLPIResetObserver : start");
                return;
            }
            CaLogger.info("SLPIResetObserver : observer is null");
        }
    }

    private String getFileData(String str) {
        Throwable th;
        String str2 = MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET;
        StringBuffer stringBuffer = new StringBuffer(MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
        InputStreamReader inputStreamReader = null;
        if (str == null) {
            CaLogger.error("File Path is null!!");
            return str2;
        }
        try {
            InputStreamReader fileReader = new FileReader(str);
            try {
                if (fileReader.ready()) {
                    while (true) {
                        int read = fileReader.read();
                        if (read == -1) {
                            break;
                        }
                        stringBuffer.append((char) read);
                    }
                    str2 = stringBuffer.toString().replace("\n", MultiWindowManagerBridge.SNAP_WINDOW_VISBLE_CALLER_UNSET);
                }
                try {
                    fileReader.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                inputStreamReader = fileReader;
            } catch (FileNotFoundException e2) {
                inputStreamReader = fileReader;
                try {
                    CaLogger.error("File is not found");
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e3) {
                        e3.printStackTrace();
                    }
                    return str2;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        inputStreamReader.close();
                    } catch (Throwable e32) {
                        e32.printStackTrace();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                inputStreamReader = fileReader;
                CaLogger.error("File is not found");
                try {
                    inputStreamReader.close();
                } catch (Throwable e322) {
                    e322.printStackTrace();
                }
                return str2;
            } catch (Throwable th3) {
                th = th3;
                inputStreamReader = fileReader;
                inputStreamReader.close();
                throw th;
            }
        } catch (FileNotFoundException e5) {
            CaLogger.error("File is not found");
            inputStreamReader.close();
            return str2;
        } catch (IOException e6) {
            CaLogger.error("File is not found");
            inputStreamReader.close();
            return str2;
        }
        return str2;
    }

    private boolean isSLPISupported() {
        return new File(SLPI_RESET_STATUS_PATH).exists();
    }

    public final void notifySensorHubResetObserver(int i) {
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ISensorHubResetObserver iSensorHubResetObserver = (ISensorHubResetObserver) it.next();
            if (iSensorHubResetObserver != null) {
                iSensorHubResetObserver.updateSensorHubResetStatus(i);
            }
        }
    }

    public final int parse(byte[] bArr, int i) {
        int i2 = i;
        if ((bArr.length - i) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        BaseBundle bundle = new Bundle();
        i2 = i + 1;
        byte b = bArr[i];
        bundle.putString("Noti", Integer.toString(b));
        int i3 = 0;
        this.mSensorHubResetCnt++;
        if (bArr.length > 3) {
            int i4 = i2 + 1;
            i3 = bArr[i2];
            i2 = i4;
        }
        if (b == ISensorHubResetObservable.SENSORHUB_RESET) {
            CaLogger.debug("================= Noti (Power) =================");
            CaLogger.info("Noti Type : SensorHub Reset");
            CaAutoBrightnessTableManager.getInstance().sendAutoBrightnessTableToSensorHub();
            notifySensorHubResetObserver(b);
            if (CaBootStatus.getInstance().isBootComplete() && this.mContext != null) {
                Intent intent = new Intent(ContextAwareManager.SENSORHUB_RESET_ACTION);
                intent.putExtra("sensorhub_reset_reason", i3);
                intent.putExtra("sensorhub_reset_cnt", this.mSensorHubResetCnt);
                intent.putExtra("sensorhub_reset_timestamp", System.currentTimeMillis());
                CaLogger.info("Sensorhub reset status = " + i3 + " Sensorhub reset cnt = " + this.mSensorHubResetCnt);
                this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, PERMISSION_SENSORHUB_RESET);
            }
        }
        return i2;
    }

    public final void registerSensorHubResetObserver(ISensorHubResetObserver iSensorHubResetObserver) {
        if (!this.mListeners.contains(iSensorHubResetObserver)) {
            this.mListeners.add(iSensorHubResetObserver);
        }
    }

    public final void unregisterSensorHubResetObserver(ISensorHubResetObserver iSensorHubResetObserver) {
        if (this.mListeners.contains(iSensorHubResetObserver)) {
            this.mListeners.remove(iSensorHubResetObserver);
        }
    }
}
