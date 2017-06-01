package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.INSTRUCTION;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.LIB_TYPE;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.sensorhub.SensorHubEventListener;
import com.samsung.android.sensorhub.SensorHubManager;

public class SensorHubParserProvider extends SensorHubParserProtocol implements IApPowerObserver {
    private static final int PACKET_MAX_SIZE = 16384;
    private static volatile SensorHubParserProvider instance;
    private HandlerThread handlerThread = null;
    private int mApStatus;
    private TypeParser mExtLibParser;
    private TypeParser mLibParser;
    private TypeParser mRequestLibParser;
    private final SensorHubEventListener mSensorHubListener = new C10801();
    private SensorHubManager mSensorHubManager;

    class C10801 implements SensorHubEventListener {
        C10801() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void onGetSensorHubData(com.samsung.android.sensorhub.SensorHubEvent r5) {
            /*
            r4 = this;
            monitor-enter(r4);
            r0 = "AP_NONE";
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.this;	 Catch:{ all -> 0x00a0 }
            r2 = r2.mApStatus;	 Catch:{ all -> 0x00a0 }
            r3 = -46;
            if (r2 != r3) goto L_0x0053;
        L_0x000e:
            r0 = "AP_SLEEP";
            r2 = com.samsung.android.contextaware.utilbundle.CaTimeManager.getInstance();	 Catch:{ all -> 0x00a0 }
            r2.sendCurTimeToSensorHub();	 Catch:{ all -> 0x00a0 }
        L_0x0018:
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a0 }
            r2.<init>();	 Catch:{ all -> 0x00a0 }
            r3 = "onGetSensorHubData Event [event buffer len :";
            r2 = r2.append(r3);	 Catch:{ all -> 0x00a0 }
            r3 = r5.length;	 Catch:{ all -> 0x00a0 }
            r3 = java.lang.Integer.toString(r3);	 Catch:{ all -> 0x00a0 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x00a0 }
            r3 = "], ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x00a0 }
            r2 = r2.append(r0);	 Catch:{ all -> 0x00a0 }
            r2 = r2.toString();	 Catch:{ all -> 0x00a0 }
            com.samsung.android.contextaware.utilbundle.logger.CaLogger.debug(r2);	 Catch:{ all -> 0x00a0 }
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.this;	 Catch:{ all -> 0x00a0 }
            r2 = r2.mLibParser;	 Catch:{ all -> 0x00a0 }
            if (r2 != 0) goto L_0x0061;
        L_0x0048:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors.ERROR_LIBRARY_PARSER_NULL_EXEPTION;	 Catch:{ all -> 0x00a0 }
            r2 = r2.getMessage();	 Catch:{ all -> 0x00a0 }
            com.samsung.android.contextaware.utilbundle.logger.CaLogger.warning(r2);	 Catch:{ all -> 0x00a0 }
            monitor-exit(r4);
            return;
        L_0x0053:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.this;	 Catch:{ all -> 0x00a0 }
            r2 = r2.mApStatus;	 Catch:{ all -> 0x00a0 }
            r3 = -47;
            if (r2 != r3) goto L_0x0018;
        L_0x005d:
            r0 = "AP_WAKEUP";
            goto L_0x0018;
        L_0x0061:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.this;	 Catch:{ all -> 0x00a0 }
            r2 = r2.mLibParser;	 Catch:{ all -> 0x00a0 }
            r2 = r2.checkParserMap();	 Catch:{ all -> 0x00a0 }
            if (r2 != 0) goto L_0x0078;
        L_0x006d:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors.ERROR_EMPTY_PARSER_MAP;	 Catch:{ all -> 0x00a0 }
            r2 = r2.getMessage();	 Catch:{ all -> 0x00a0 }
            com.samsung.android.contextaware.utilbundle.logger.CaLogger.warning(r2);	 Catch:{ all -> 0x00a0 }
            monitor-exit(r4);
            return;
        L_0x0078:
            r2 = r5.length;	 Catch:{ all -> 0x00a0 }
            if (r2 > 0) goto L_0x0087;
        L_0x007c:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors.ERROR_GET_SENSOR_HUB_EVENT;	 Catch:{ all -> 0x00a0 }
            r2 = r2.getMessage();	 Catch:{ all -> 0x00a0 }
            com.samsung.android.contextaware.utilbundle.logger.CaLogger.warning(r2);	 Catch:{ all -> 0x00a0 }
            monitor-exit(r4);
            return;
        L_0x0087:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.this;	 Catch:{ all -> 0x00a0 }
            r3 = r5.buffer;	 Catch:{ all -> 0x00a0 }
            r1 = r2.parse(r3);	 Catch:{ all -> 0x00a0 }
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors.SUCCESS;	 Catch:{ all -> 0x00a0 }
            r2 = r2.getCode();	 Catch:{ all -> 0x00a0 }
            if (r1 == r2) goto L_0x009e;
        L_0x0097:
            r2 = com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors.getMessage(r1);	 Catch:{ all -> 0x00a0 }
            com.samsung.android.contextaware.utilbundle.logger.CaLogger.error(r2);	 Catch:{ all -> 0x00a0 }
        L_0x009e:
            monitor-exit(r4);
            return;
        L_0x00a0:
            r2 = move-exception;
            monitor-exit(r4);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider.1.onGetSensorHubData(com.samsung.android.sensorhub.SensorHubEvent):void");
        }
    }

    private boolean checkInstruction(byte b) {
        for (INSTRUCTION instruction : INSTRUCTION.values()) {
            if (instruction.value == b) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLibType(byte b) {
        for (LIB_TYPE lib_type : LIB_TYPE.values()) {
            if (lib_type.value == b) {
                return true;
            }
        }
        return false;
    }

    private int checkPacketSize(byte[] bArr) {
        return bArr.length <= 0 ? SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode() : bArr.length > 16384 ? SensorHubErrors.ERROR_PACKET_LENGTH_OVERFLOW.getCode() : SensorHubErrors.SUCCESS.getCode();
    }

    public static SensorHubParserProvider getInstance() {
        if (instance == null) {
            synchronized (SensorHubParserProvider.class) {
                if (instance == null) {
                    instance = new SensorHubParserProvider();
                }
            }
        }
        return instance;
    }

    private int parse(byte[] bArr) {
        byte[] bArr2 = (byte[]) bArr.clone();
        CaLogger.info("buffer size = " + Integer.toString(bArr2.length));
        int checkPacketSize = checkPacketSize(bArr2);
        if (checkPacketSize != SensorHubErrors.SUCCESS.getCode()) {
            return checkPacketSize;
        }
        String byteArrToString = CaConvertUtil.byteArrToString(bArr2);
        if (byteArrToString == null || byteArrToString.isEmpty()) {
            CaLogger.warning("Packet is null");
        } else {
            CaLogger.info(byteArrToString);
        }
        int i = 0;
        while (i < bArr2.length) {
            if ((bArr2.length - i) - 2 < 0) {
                return SensorHubErrors.ERROR_PACKET_HEADER_LENGTH.getCode();
            }
            if (!checkInstruction(bArr2[i])) {
                return SensorHubErrors.ERROR_INSTRUCTION_FIELD_PARSING.getCode();
            }
            byte b = bArr2[i];
            i++;
            if (!checkLibType(bArr2[i])) {
                return SensorHubErrors.ERROR_TYPE_FIELD_PARSING.getCode();
            }
            i = parseData(b, bArr2[i], bArr2, i + 1);
            if (i < 0) {
                return SensorHubErrors.ERROR_DATA_FIELD_PARSING.getCode();
            }
        }
        return SensorHubErrors.SUCCESS.getCode();
    }

    private int parseData(byte b, byte b2, byte[] bArr, int i) {
        int i2 = i;
        if (this.mLibParser == null || !this.mLibParser.checkParserMap()) {
            CaLogger.error(SensorHubErrors.ERROR_LIBRARY_PARSER_OBJECT.getMessage());
            return -1;
        }
        int parseNotiPowerData = parseNotiPowerData(b, b2, bArr, i);
        if (parseNotiPowerData > 0) {
            return parseNotiPowerData;
        }
        parseNotiPowerData = parseDebugMsg(b, b2, bArr, i);
        if (parseNotiPowerData > 0) {
            return parseNotiPowerData;
        }
        if (b != INSTRUCTION.INST_LIBRARY.value) {
            CaLogger.error(SensorHubErrors.ERROR_INSTRUCTION_VALUE.getMessage());
            return -1;
        }
        if (b2 == LIB_TYPE.TYPE_LIBRARY.value) {
            i2 = this.mLibParser.parse(bArr, i);
        } else if (b2 == LIB_TYPE.TYPE_LIBRARY_EXT.value) {
            i2 = this.mExtLibParser.parse(bArr, i);
        } else if (b2 == LIB_TYPE.TYPE_LIBRARY_REQUEST.value) {
            i2 = this.mRequestLibParser.parse(bArr, i);
        } else {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
            i2 = -1;
        }
        return i2;
    }

    private int parseDebugMsg(byte b, byte b2, byte[] bArr, int i) {
        int i2 = i;
        ISensorHubParser parser = this.mLibParser.getParser(LIB_TYPE.TYPE_SENSORHUB_DEBUG_MSG.toString());
        return (b == INSTRUCTION.INST_LIBRARY.value && b2 == LIB_TYPE.TYPE_SENSORHUB_DEBUG_MSG.value && parser != null) ? parser.parse(bArr, i) : -1;
    }

    private int parseNotiPowerData(byte b, byte b2, byte[] bArr, int i) {
        int i2 = i;
        ISensorHubParser parser = this.mLibParser.getParser(LIB_TYPE.TYPE_NOTI_POWER.toString());
        return (b == INSTRUCTION.INST_NOTI.value && b2 == LIB_TYPE.TYPE_NOTI_POWER.value && parser != null) ? parser.parse(bArr, i) : -1;
    }

    public final TypeParser getExtLibParser() {
        return this.mExtLibParser;
    }

    public final TypeParser getLibParser() {
        return this.mLibParser;
    }

    public final TypeParser getRequestLibParser() {
        return this.mRequestLibParser;
    }

    public final void initialize(Context context) {
        if (this.mSensorHubManager == null) {
            this.mSensorHubManager = (SensorHubManager) context.getSystemService("sensorhub");
            if (this.mSensorHubManager == null) {
                CaLogger.error("mSensorHubManager is null.");
                return;
            }
            this.handlerThread = new HandlerThread("CAESHubEvtHler");
            this.handlerThread.start();
            Looper looper = this.handlerThread.getLooper();
            if (looper == null) {
                this.handlerThread.quitSafely();
                this.handlerThread = null;
                CaLogger.error(ContextAwareServiceErrors.ERROR_LOOPER_NULL_EXCEPTION.getMessage());
                return;
            }
            this.mSensorHubManager.registerListener(this.mSensorHubListener, this.mSensorHubManager.getDefaultSensorHub(1), 0, new Handler(looper));
        }
        this.mLibParser = new LibTypeParser();
        this.mExtLibParser = new ExtLibTypeParser();
        this.mRequestLibParser = new RequestLibTypeParser();
        this.mApStatus = 0;
        CaPowerManager.getInstance().registerApPowerObserver(this);
    }

    public final void initializePreparedSubCollection() {
    }

    public final void parseForScenarioTesting(byte[] bArr) {
        int parse = parse(bArr);
        if (parse != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(parse));
        }
    }

    public final void terminate() {
        if (this.mSensorHubManager != null) {
            this.mApStatus = 0;
            CaPowerManager.getInstance().unregisterApPowerObserver(this);
            this.mSensorHubManager.unregisterListener(this.mSensorHubListener);
            this.mSensorHubManager = null;
            this.mLibParser = null;
            this.mExtLibParser = null;
            this.mRequestLibParser = null;
            if (this.handlerThread != null) {
                this.handlerThread.quit();
                this.handlerThread = null;
            }
        }
    }

    public final void updateApPowerStatus(int i, long j) {
        this.mApStatus = i;
        String str = "AP_NONE";
        if (this.mApStatus == -46) {
            str = "AP_SLEEP";
        } else if (this.mApStatus == -47) {
            str = "AP_WAKEUP";
        }
        CaLogger.info(str);
    }
}
