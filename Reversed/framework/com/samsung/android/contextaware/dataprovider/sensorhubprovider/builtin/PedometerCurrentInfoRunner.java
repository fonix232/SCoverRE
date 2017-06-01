package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.PedometerProviderForExtLib;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserBean;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class PedometerCurrentInfoRunner extends PedometerProviderForExtLib {
    private static final int DEFAULT_COLLECTION_TIME = 0;
    private int mCollectionTime = 0;

    public PedometerCurrentInfoRunner(int i, Context context, ISensorHubResetObservable iSensorHubResetObservable) {
        super(i, context, iSensorHubResetObservable);
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected void doTimeOutChecking(Listener listener, Bundle bundle) {
        listener.setContextCollectionResultNotifyCompletion(true);
        if (bundle == null) {
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getCode()));
        } else {
            notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), bundle);
        }
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final String getContextType() {
        return ContextType.REQUEST_SENSORHUB_PEDOMETER_CURRENT_INFO.getCode();
    }

    public final String[] getContextValueNames() {
        return null;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 1, (byte) this.mCollectionTime};
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeOutCheckObserver getTimeOutCheckObserver() {
        return this;
    }

    protected final int parse(int i, byte[] bArr) {
        SensorHubParserBean libParser = SensorHubParserProvider.getInstance().getLibParser();
        if (libParser == null) {
            CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
            return -1;
        }
        ISensorHubParser parser = libParser.getParser(DATA_TYPE.LIBRARY_DATATYPE_PEDOMETER.toString());
        if (parser != null) {
            return parser.parse(bArr, i);
        }
        CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PARSER_NOT_EXIST.getCode()));
        return -1;
    }

    public final <E> boolean setPropertyValue(int i, E e) {
        if (i != 19) {
            return false;
        }
        this.mCollectionTime = ((Integer) ((ContextAwarePropertyBundle) e).getValue()).intValue();
        CaLogger.info("Collection Time = " + Integer.toString(this.mCollectionTime));
        return true;
    }
}
