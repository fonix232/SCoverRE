package com.sec.android.cover.ledcover.fsm.dream;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.ScoverManager;
import com.sec.android.cover.CoverUtils;
import com.sec.android.cover.ledcover.Manifest.permission;
import com.sec.android.cover.ledcover.fsm.dream.LedStateMachine.LedStateMachineListener;
import java.util.Arrays;

public class LedPowerOnOffStateController implements LedStateMachineListener {
    public static final int CMD_LED_OFF = 18;
    private static final int COMMAND_POSITION = 6;
    private static final int HANDLER_THREAD_FAILED_START = 1;
    private static final int HANDLER_THREAD_FAILED_TRANSCEIVE = 2;
    private static final int HANDLER_THREAD_STOP_DETACHED = 1;
    private static final int HANDLER_THREAD_STOP_LED_OFF = 0;
    private static final int HANDLER_THREAD_SUCCESSFUL_TRANSCEIVE = 0;
    private static final int LED_COVER_RETRY_COUNT_MAX = 13;
    private static final long LED_COVER_RETRY_DELAY = 500;
    private static final String LED_COVER_RETRY_DONE_INTENT_ACTION = "android.intent.action.NFC_LED_COVER_MAX_RETRY_DONE";
    private static final byte[] LED_OFF_COMMAND = new byte[]{(byte) 0, (byte) -95, (byte) 0, (byte) 0, TOUCH_CHECK_REPLY_CENTER_TAP, TOUCH_CHECK_REPLY_CENTER_TAP, (byte) 18, (byte) 0, (byte) 0};
    private static final int MSG_COVER_TOUCH_CHECK = 1;
    private static final int MSG_COVER_VERSION_CHECK = 2;
    private static final int MSG_HANDLER_THREAD_RESPONSE = 1000;
    private static final int MSG_HANDLER_THREAD_STOP = 1001;
    private static final int MSG_SEND_TOUCH_REPLY = 3;
    private static final int MSG_START_STOP_TRANSCEIVE_RETRY_LED = 0;
    private static final int RESPONSE_COMMAND_POSITION = 0;
    private static final byte[] RESPONSE_PATTERN = new byte[]{(byte) 0, RESPONSE_RESULT_SUCCESS};
    private static final byte RESPONSE_RESULT_SUCCESS = (byte) -47;
    private static final boolean SAFE_DEBUG = true;
    private static final String SETTING_SECURE_FACTORY_TEST_COUNT = "led_cover_factory_test_count";
    private static final String SETTING_SECURE_FIRMWARE_VERSION = "led_cover_firmware_version";
    private static final int START_LED_COVER = 1;
    private static final int STOP_LED_COVER = 0;
    private static final String TAG = LedPowerOnOffStateController.class.getSimpleName();
    private static final long TOUCH_CHECK_INTERVAL = 100;
    private static final long TOUCH_CHECK_INTERVAL_DELAY = 500;
    private static final byte[] TOUCH_CHECK_REPLY = new byte[]{(byte) 17, (byte) 66};
    private static final byte TOUCH_CHECK_REPLY_ACCEPT = (byte) 1;
    private static final byte TOUCH_CHECK_REPLY_CENTER_TAP = (byte) 4;
    private static final byte TOUCH_CHECK_REPLY_LEFT_TAP = (byte) 3;
    private static final int TOUCH_CHECK_REPLY_POSITION = 1;
    private static final byte TOUCH_CHECK_REPLY_REJECT = (byte) 2;
    private static final byte TOUCH_CHECK_REPLY_RIGTH_TAP = (byte) 5;
    private static final byte[] TOUCH_CHECK_REQUEST = new byte[]{(byte) 0, (byte) -95, (byte) 0, (byte) 0, TOUCH_CHECK_REPLY_CENTER_TAP, TOUCH_CHECK_REPLY_CENTER_TAP, (byte) 17, (byte) 0, (byte) 0};
    private static final byte[] VERSION_CHECK_COMMAND = new byte[]{(byte) 0, (byte) -95, (byte) 0, (byte) 0, TOUCH_CHECK_REPLY_CENTER_TAP, TOUCH_CHECK_REPLY_CENTER_TAP, (byte) 113, (byte) 0, (byte) 0};
    private static final int VERSION_CHECK_RESPONSE_LENGTH = 5;
    private static final long WC_CONTROL_RESET_INTERVAL = 30000;
    private int TEST;
    private boolean isCoverAttached = false;
    private boolean isCoverClosed = false;
    private Context mContext;
    private ScoverManager mCoverManager;
    private boolean mFactoryTestEnabled;
    private LedStateController mFactoryTestState;
    private boolean mFactoryTransceiveResponseIntentSent;
    private String mFirmwareVersion;
    private boolean mFotaInProgress;
    private LedOffCallback mFotaLedOffCallback;
    private Callback mHandlerCallback = new C00291();
    private Handler mHtHandler;
    private long mLastTouchValidEventTime;
    private long mLastWcControlResetTime;
    private int mLedCoverStartRetryCount = 0;
    private int mLedCoverTransceiveRetryCount = 0;
    private LedOffCallback mLedOffCallback;
    private WakeLock mLedOnOffWakeLock;
    private LedStateMachine mLedStateMachine;
    private WakeLock mLedTouchCheckWakeLock;
    private WakeLock mLedVersionCheckWakeLock;
    private NfcAdapter mNfcAdapter;
    private OnOffState mOnOffState = OnOffState.OFF;
    private PowerManager mPowerManager;
    private TransceiveCallback mTransceiveCallback;
    private Handler mUiHandler;
    private HandlerThread mWorkerThread;
    private int testCount;

    public interface LedOffCallback {
        void onLedOff();
    }

    public interface TransceiveCallback {
        void onMediaTouchEvent(int i);

        void onStateSent(LedStateController ledStateController);

        void onTouchEvent(boolean z);
    }

    class C00291 implements Callback {
        C00291() {
        }

        public boolean handleMessage(Message msg) {
            Log.d(LedPowerOnOffStateController.TAG, "handleMessage msg=" + String.valueOf(msg));
            switch (msg.what) {
                case 0:
                    LedPowerOnOffStateController.this.mHtHandler.removeMessages(1);
                    if (LedPowerOnOffStateController.this.mLedTouchCheckWakeLock.isHeld()) {
                        LedPowerOnOffStateController.this.mLedTouchCheckWakeLock.release();
                    }
                    if (msg.arg1 != 1) {
                        LedPowerOnOffStateController.this.handleStopLedCover();
                        break;
                    }
                    LedPowerOnOffStateController.this.handleStartLedCover();
                    break;
                case 1:
                    LedPowerOnOffStateController.this.handleTouchCheckLedCover(msg.arg1);
                    break;
                case 2:
                    LedPowerOnOffStateController.this.handleCoverVersionCheck();
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedVersionCheckWakeLock);
                    break;
                case 3:
                    LedPowerOnOffStateController.this.handleSendTouchCallback(msg.arg1, msg.arg2);
                    break;
                case LedPowerOnOffStateController.MSG_HANDLER_THREAD_RESPONSE /*1000*/:
                    LedStateController state = null;
                    if (msg.arg1 == 0) {
                        state = msg.obj;
                    }
                    LedPowerOnOffStateController.this.mTransceiveCallback.onStateSent(state);
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedOnOffWakeLock);
                    break;
                case LedPowerOnOffStateController.MSG_HANDLER_THREAD_STOP /*1001*/:
                    int stopRreason = msg.arg1;
                    if (stopRreason == 1) {
                        LedPowerOnOffStateController.this.stopHandlerThread();
                    } else if (stopRreason == 0) {
                        if (LedPowerOnOffStateController.this.mLedOffCallback != null) {
                            LedPowerOnOffStateController.this.mLedOffCallback.onLedOff();
                            LedPowerOnOffStateController.this.mLedOffCallback = null;
                        }
                        if (LedPowerOnOffStateController.this.mFotaInProgress && LedPowerOnOffStateController.this.mFotaLedOffCallback != null) {
                            LedPowerOnOffStateController.this.mFotaLedOffCallback.onLedOff();
                            LedPowerOnOffStateController.this.mFotaLedOffCallback = null;
                        }
                    }
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedOnOffWakeLock);
                    break;
            }
            return false;
        }
    }

    public interface FirmwareVersionReceivedListener {
        void onFirmwareDataReceived(byte b, byte b2, byte b3, byte b4);
    }

    public enum OnOffState {
        ON,
        STARTING,
        OFF,
        RETRY
    }

    public static final class OutgoingSystemEvent {
        private static final String KEY_TOUCH_LISTENER_RESPONSE = "lcd_touch_listener_respone";
        private static final String KEY_TOUCH_LISTENER_TYPE = "lcd_touch_listener_type";
        public static final String KEY_TYPE = "event_type";
        public static final int SYSTEM_EVENT_FOTA_IN_PROGRESS_RESPONSE = 8;
        private static final int TYPE_DISABLE_LDC_OFF_BY_COVER = 1;
        private static final int TYPE_TOUCH_RESPONSE = 0;
    }

    public LedPowerOnOffStateController(Context context, LedStateMachine ledStateMachine, TransceiveCallback callback) {
        if (context == null || ledStateMachine == null) {
            throw new IllegalArgumentException();
        }
        this.mContext = context;
        this.mLedStateMachine = ledStateMachine;
        this.mPowerManager = (PowerManager) context.getSystemService("power");
        this.mLedOnOffWakeLock = this.mPowerManager.newWakeLock(1, "onoff ledcover");
        this.mLedOnOffWakeLock.setReferenceCounted(false);
        this.mLedTouchCheckWakeLock = this.mPowerManager.newWakeLock(1, "touch check ledcover");
        this.mLedTouchCheckWakeLock.setReferenceCounted(false);
        this.mLedVersionCheckWakeLock = this.mPowerManager.newWakeLock(1, "version check ledcover");
        this.mLedVersionCheckWakeLock.setReferenceCounted(false);
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.mUiHandler = new Handler(this.mHandlerCallback);
        this.mTransceiveCallback = callback;
        this.mCoverManager = new ScoverManager(this.mContext);
        this.TEST = Secure.getInt(context.getContentResolver(), "nfc_led_cover_test", 0);
    }

    private void startHandlerThread() {
        this.mWorkerThread = new HandlerThread("myWorkerThread");
        this.mWorkerThread.start();
        this.mHtHandler = new Handler(this.mWorkerThread.getLooper(), this.mHandlerCallback);
    }

    private void stopHandlerThread() {
        if (this.mWorkerThread != null) {
            this.mWorkerThread.quit();
            this.mWorkerThread = null;
            this.mHtHandler = null;
        }
    }

    private void handleStartLedCover() {
        LedStateController stateToSend;
        Log.d(TAG, "Trying to start NFC LED Cover mOnOffState=" + this.mOnOffState);
        this.mLedOffCallback = null;
        switch (this.mOnOffState) {
            case STARTING:
            case ON:
                Log.d(TAG, "NFC LED Cover already started");
                break;
            case OFF:
                this.mOnOffState = OnOffState.STARTING;
                Log.d(TAG, "Disable Wireless Charger");
                this.mLastWcControlResetTime = System.currentTimeMillis();
                this.mNfcAdapter.semSetWirelessChargeEnabled(false);
                break;
            case RETRY:
                break;
        }
        byte[] coverStartData = this.mNfcAdapter.semStartLedCoverMode();
        Log.d(TAG, "Start result: " + getByteDataString(coverStartData));
        if (isValidCoverStartData(coverStartData)) {
            Log.d(TAG, "Started NFC LED Cover");
            this.mLedCoverStartRetryCount = 0;
            this.mOnOffState = OnOffState.ON;
        } else if (this.mLedCoverStartRetryCount < 13) {
            Log.w(TAG, "Failed to start NFC LED Cover, retry after some time");
            if (this.mFactoryTestEnabled && this.mFactoryTestState != null && this.mFactoryTestState.getCommandCodeByte() == MockeryLedStateController.CMD_NV_READ) {
                Secure.putInt(this.mContext.getContentResolver(), SETTING_SECURE_FACTORY_TEST_COUNT, -1);
            }
            if (isTestModeEnabled()) {
                Log.d(TAG, "Test mode enabled, so ignore failed start");
                this.mLedCoverStartRetryCount = 0;
                this.mOnOffState = OnOffState.ON;
            } else {
                this.mLedCoverStartRetryCount++;
                retryStartLedCover();
                sendNfcFailIntentForFactoryMode(coverStartData);
                return;
            }
        } else {
            Log.e(TAG, "Failed to start NFC LED Cover");
            this.mLedCoverStartRetryCount = 0;
            Log.d(TAG, "Stop LedCover for FINAL retry, result: " + String.valueOf(this.mNfcAdapter.semStopLedCoverMode()));
            handleStopLedCover();
            Message msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_RESPONSE;
            msg.arg1 = 1;
            this.mUiHandler.sendMessage(msg);
            sendNfcFailIntentForFactoryMode(coverStartData);
            this.mFactoryTransceiveResponseIntentSent = false;
            return;
        }
        if (this.mFactoryTestEnabled) {
            stateToSend = this.mFactoryTestState;
            if (stateToSend == null) {
                Log.e(TAG, "handleStartLedCover: factory test state is null");
                stopLedCover();
                return;
            }
        }
        stateToSend = this.mLedStateMachine.getCurrentLedState();
        handleSendDataToNfcLedCover(stateToSend);
    }

    private void retryStartLedCover() {
        this.mOnOffState = OnOffState.RETRY;
        Log.d(TAG, "Stop LedCover for retry, result: " + String.valueOf(this.mNfcAdapter.semStopLedCoverMode()));
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 1;
        this.mHtHandler.sendMessageDelayed(msg, 500);
    }

    private void handleStopLedCover() {
        Log.d(TAG, "Stop LedCover mOnOffState: " + this.mOnOffState);
        if (this.mHtHandler.hasMessages(0)) {
            this.mHtHandler.removeMessages(0);
        }
        OnOffState oldState = this.mOnOffState;
        this.mOnOffState = OnOffState.OFF;
        switch (oldState) {
            case STARTING:
            case ON:
                if (this.isCoverAttached) {
                    try {
                        byte[] returnValue = this.mNfcAdapter.semTransceiveDataWithLedCover(LED_OFF_COMMAND);
                        if (returnValue != null) {
                            Log.d(TAG, "Response data for LED_OFF command: " + getByteDataString(returnValue));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in trancieve LED_OFF command", e);
                    }
                }
                Log.d(TAG, "Stop LedCover, result: " + String.valueOf(this.mNfcAdapter.semStopLedCoverMode()));
                break;
            case RETRY:
                break;
        }
        Log.d(TAG, "LED_OFF, Start wireless charger");
        this.mNfcAdapter.semSetWirelessChargeEnabled(SAFE_DEBUG);
        this.mLedCoverStartRetryCount = 0;
        this.mLedCoverTransceiveRetryCount = 0;
        Message msg;
        if (!this.isCoverAttached) {
            Thread uiHandlerThread = this.mUiHandler.getLooper().getThread();
            if (uiHandlerThread == null || !uiHandlerThread.isAlive()) {
                Log.w(TAG, "LedCoverService thread already died, direclty handle stopHandlerThread");
                stopHandlerThread();
                CoverUtils.releaseWakeLockSafely(this.mLedOnOffWakeLock);
                return;
            }
            msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_STOP;
            msg.arg1 = 1;
            this.mUiHandler.sendMessage(msg);
        } else if ((this.mLedOffCallback == null && this.mFotaLedOffCallback == null) || this.mHtHandler.hasMessages(0)) {
            CoverUtils.releaseWakeLockSafely(this.mLedOnOffWakeLock);
        } else {
            msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_STOP;
            msg.arg1 = 0;
            this.mUiHandler.sendMessage(msg);
            if (this.mFirmwareVersion == null) {
                CoverUtils.acquireWakeLockSafely(this.mLedVersionCheckWakeLock);
                this.mHtHandler.sendEmptyMessageDelayed(2, 500);
            }
        }
    }

    private void handleSendDataToNfcLedCover(LedStateController state) {
        Log.d(TAG, "handleSendDataToLedCover new state to transceive is: " + state);
        Log.d(TAG, "Firmware version: " + this.mFirmwareVersion);
        if (this.mOnOffState != OnOffState.ON) {
            Log.e(TAG, "Something went wrong, mOnOffState should be ON and is: " + this.mOnOffState);
        } else {
            boolean nullData = false;
            byte[] data = state.getCommand(this.mLedStateMachine.getLedContext());
            state.onSendingState(this.mLedStateMachine.getLedContext());
            byte command = state.getCommandCodeByte();
            Log.d(TAG, "handleSendDataToLedCover : transceive data : " + getByteDataString(data));
            if (data == null) {
                data = LED_OFF_COMMAND;
                nullData = SAFE_DEBUG;
            }
            byte[] returnValue = null;
            try {
                resetWcControlTimer();
                returnValue = this.mNfcAdapter.semTransceiveDataWithLedCover(data);
                if (returnValue != null) {
                    Log.d(TAG, "Response data: " + getByteDataString(returnValue));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in trancieve command", e);
            }
            if (!isValidResponse(data, returnValue)) {
                Log.e(TAG, "Error parsing response");
                if (isTestModeEnabled()) {
                    Log.d(TAG, "Test enabled, ignore transceive error");
                } else {
                    processInvalidCommand(command, returnValue);
                    return;
                }
            }
            this.mFactoryTransceiveResponseIntentSent = false;
            this.mContext.sendBroadcastAsUser(new Intent(LED_COVER_RETRY_DONE_INTENT_ACTION), UserHandle.SEM_ALL);
            Log.d(TAG, "Sent done intent, sucess");
            if (!nullData) {
                onCommandSentHandleTouch(state);
            }
        }
        this.mLedCoverTransceiveRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = MSG_HANDLER_THREAD_RESPONSE;
        msg.arg1 = 0;
        msg.obj = state;
        this.mUiHandler.sendMessage(msg);
    }

    private void handleCoverVersionCheck() {
        if (!isTestModeEnabled() && !this.mFotaInProgress) {
            if (this.mFirmwareVersion != null) {
                Log.e(TAG, "Firmware version already retrieved: " + this.mFirmwareVersion);
            } else if (this.mOnOffState == OnOffState.ON) {
                Log.d(TAG, "Led is on, try checking version later");
            } else {
                byte[] coverStartData = this.mNfcAdapter.semStartLedCoverMode();
                Log.d(TAG, "Version check start result: " + getByteDataString(coverStartData));
                if (isValidCoverStartData(coverStartData)) {
                    if (transceiveVersionCheck()) {
                        Log.e(TAG, "Firmware version retrieved: " + this.mFirmwareVersion);
                        Secure.putString(this.mContext.getContentResolver(), SETTING_SECURE_FIRMWARE_VERSION, this.mFirmwareVersion);
                    }
                    this.mNfcAdapter.semStopLedCoverMode();
                    if (this.mOnOffState == OnOffState.OFF) {
                        this.mNfcAdapter.semSetWirelessChargeEnabled(SAFE_DEBUG);
                        return;
                    }
                    return;
                }
                this.mNfcAdapter.semStopLedCoverMode();
                if (this.mOnOffState == OnOffState.OFF) {
                    this.mNfcAdapter.semSetWirelessChargeEnabled(SAFE_DEBUG);
                }
            }
        }
    }

    private boolean transceiveVersionCheck() {
        byte[] response = this.mNfcAdapter.semTransceiveDataWithLedCover(VERSION_CHECK_COMMAND);
        Log.d(TAG, "Version check response: " + getByteDataString(response));
        boolean validResponse = (response == null || response.length < 5 || response[0] != VERSION_CHECK_COMMAND[6]) ? false : SAFE_DEBUG;
        if (validResponse) {
            this.mFirmwareVersion = String.format("%02X %02X %02X %02X", new Object[]{Byte.valueOf(response[1]), Byte.valueOf(response[2]), Byte.valueOf(response[3]), Byte.valueOf(response[4])});
        }
        return validResponse;
    }

    private void onCommandSentHandleTouch(LedStateController state) {
        int listenerType = state.getTouchEventListenerType();
        if (listenerType == -1) {
            this.mHtHandler.removeMessages(1);
            try {
                if (this.mLedTouchCheckWakeLock.isHeld()) {
                    this.mLedTouchCheckWakeLock.release();
                    return;
                }
                return;
            } catch (IllegalStateException e) {
                Log.e(TAG, "Shouldn't happen", e);
                return;
            }
        }
        if (isTestModeEnabled()) {
            this.testCount = 0;
        }
        scheduleTouchCheck(listenerType);
    }

    private void scheduleTouchCheck(int listenerType) {
        try {
            if (!this.mLedTouchCheckWakeLock.isHeld()) {
                this.mLedTouchCheckWakeLock.acquire();
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Shouldn't happen", e);
        }
        Message touchCheckMsg = this.mHtHandler.obtainMessage(1);
        touchCheckMsg.arg1 = listenerType;
        this.mHtHandler.sendMessageDelayed(touchCheckMsg, TOUCH_CHECK_INTERVAL);
    }

    private void handleTouchCheckLedCover(int listenerType) {
        if (isTestModeEnabled()) {
            this.testCount++;
        }
        byte[] returnData = null;
        try {
            if (!isTestModeEnabled()) {
                resetWcControlTimer();
                returnData = this.mNfcAdapter.semTransceiveDataWithLedCover(TOUCH_CHECK_REQUEST);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error sending data to NFC", e);
        }
        if (isTestModeEnabled() && this.testCount > 19) {
            returnData = buildTestTouchReply();
        }
        Log.d(TAG, "handleTouchCheckLedCover TEST: " + this.TEST + " reply data: " + getByteDataString(returnData));
        if (!isFinishedTouchReply(returnData)) {
            Log.d(TAG, "No touch reply, keep trying");
            scheduleTouchCheck(listenerType);
        } else if (System.currentTimeMillis() - this.mLastTouchValidEventTime < 500) {
            Log.d(TAG, "Next touch event occurred to early, ignore it and keep trying");
            scheduleTouchCheck(listenerType);
        } else {
            Log.d(TAG, "Touch reply for listener type: " + listenerType + " reply: " + returnData[1]);
            switch (returnData[1]) {
                case (byte) 1:
                    this.mTransceiveCallback.onTouchEvent(false);
                    break;
                case (byte) 2:
                    this.mTransceiveCallback.onTouchEvent(SAFE_DEBUG);
                    break;
                case (byte) 3:
                    if (LedStateMachine.isInternalTouchListenerType(listenerType)) {
                        this.mTransceiveCallback.onMediaTouchEvent(88);
                        break;
                    }
                    break;
                case (byte) 4:
                    if (LedStateMachine.isInternalTouchListenerType(listenerType)) {
                        this.mTransceiveCallback.onMediaTouchEvent(85);
                        break;
                    }
                    break;
                case (byte) 5:
                    if (LedStateMachine.isInternalTouchListenerType(listenerType)) {
                        this.mTransceiveCallback.onMediaTouchEvent(87);
                        break;
                    }
                    break;
            }
            if (!LedStateMachine.isInternalTouchListenerType(listenerType)) {
                handleSendTouchCallback(listenerType, returnData[1]);
            }
            this.mLastTouchValidEventTime = System.currentTimeMillis();
            scheduleTouchCheck(listenerType);
        }
    }

    private void handleSendTouchCallback(int listenerType, int touchEvent) {
        Bundle touchData = new Bundle();
        touchData.putInt(OutgoingSystemEvent.KEY_TYPE, 0);
        touchData.putInt("lcd_touch_listener_type", listenerType);
        touchData.putInt("lcd_touch_listener_respone", touchEvent);
        try {
            this.mCoverManager.sendSystemEvent(touchData);
        } catch (SsdkUnsupportedException e) {
            Log.e(TAG, "Shouldn't happen", e);
        }
    }

    private byte[] buildTestTouchReply() {
        byte[] returnData = Arrays.copyOf(TOUCH_CHECK_REPLY, TOUCH_CHECK_REPLY.length);
        switch (this.TEST) {
            case 2:
                returnData[1] = TOUCH_CHECK_REPLY_REJECT;
                break;
            case 3:
                returnData[1] = TOUCH_CHECK_REPLY_ACCEPT;
                break;
            case 4:
                returnData[1] = TOUCH_CHECK_REPLY_CENTER_TAP;
                break;
            case 5:
                returnData[1] = TOUCH_CHECK_REPLY_LEFT_TAP;
                break;
            case 6:
                returnData[1] = TOUCH_CHECK_REPLY_RIGTH_TAP;
                break;
            default:
                returnData[1] = (byte) 0;
                break;
        }
        return returnData;
    }

    private boolean isFinishedTouchReply(byte[] returnData) {
        if (returnData == null || returnData.length < TOUCH_CHECK_REPLY.length) {
            return false;
        }
        int i = 0;
        while (i < TOUCH_CHECK_REPLY.length) {
            if (returnData[i] != TOUCH_CHECK_REPLY[i]) {
                if (i != 1) {
                    return false;
                }
                if (!(returnData[i] == TOUCH_CHECK_REPLY_ACCEPT || returnData[i] == TOUCH_CHECK_REPLY_REJECT || returnData[i] == TOUCH_CHECK_REPLY_CENTER_TAP || returnData[i] == TOUCH_CHECK_REPLY_LEFT_TAP || returnData[i] == TOUCH_CHECK_REPLY_RIGTH_TAP)) {
                    return false;
                }
            }
            i++;
        }
        return SAFE_DEBUG;
    }

    private void startLedCover() {
        if (this.mFotaInProgress) {
            Log.e(TAG, "FOTA in progress - do not send any commands");
            return;
        }
        CoverUtils.acquireWakeLockSafely(this.mLedOnOffWakeLock);
        if (this.mHtHandler.hasMessages(0)) {
            this.mHtHandler.removeMessages(0);
        }
        this.mLedCoverStartRetryCount = 0;
        this.mLedCoverTransceiveRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 1;
        this.mHtHandler.sendMessage(msg);
    }

    private void stopLedCover() {
        CoverUtils.acquireWakeLockSafely(this.mLedOnOffWakeLock);
        this.mLedCoverStartRetryCount = 0;
        this.mLedCoverTransceiveRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 0;
        this.mHtHandler.sendMessageAtFrontOfQueue(msg);
    }

    private boolean isValidCoverStartData(byte[] coverStartData) {
        return (coverStartData == null || coverStartData.length <= 1) ? false : SAFE_DEBUG;
    }

    private boolean isValidResponse(byte[] data, byte[] response) {
        if (response == null) {
            return false;
        }
        if (response.length < RESPONSE_PATTERN.length) {
            Log.e(TAG, "isValidResponse length to short");
            return false;
        } else if (data[6] == MockeryLedStateController.CMD_NV_READ || data[6] == MockeryLedStateController.CMD_NV_WRITE) {
            return isValidNvCommand(data, response);
        } else {
            for (int i = 0; i < RESPONSE_PATTERN.length; i++) {
                if (i == 0) {
                    if (response[0] != data[6]) {
                        return false;
                    }
                } else if (response[i] != RESPONSE_PATTERN[i]) {
                    return false;
                }
            }
            return SAFE_DEBUG;
        }
    }

    private boolean isValidNvCommand(byte[] data, byte[] response) {
        if (response[0] == MockeryLedStateController.CMD_NV_READ) {
            int readCount = response[1] & 255;
            Log.d(TAG, "'NV Read' test count : " + readCount);
            Secure.putInt(this.mContext.getContentResolver(), SETTING_SECURE_FACTORY_TEST_COUNT, readCount);
            return SAFE_DEBUG;
        } else if (response[0] != MockeryLedStateController.CMD_NV_WRITE) {
            if (data[6] == MockeryLedStateController.CMD_NV_READ) {
                Secure.putInt(this.mContext.getContentResolver(), SETTING_SECURE_FACTORY_TEST_COUNT, -1);
            }
            return false;
        } else if (data[8] != response[1]) {
            return false;
        } else {
            return SAFE_DEBUG;
        }
    }

    private void processInvalidCommand(byte command, byte[] returnValue) {
        sendNfcFailIntentForFactoryMode(returnValue);
        if (!(returnValue == null || command == (byte) 18)) {
            if (returnValue.length != 1 || !processSingleByteError(command, returnValue)) {
                if (returnValue.length > 1) {
                    checkMcuError(returnValue);
                    if (this.mLedCoverTransceiveRetryCount < 13) {
                        Log.e(TAG, "Parsing response error restart led cover; count: " + this.mLedCoverTransceiveRetryCount);
                        this.mLedCoverTransceiveRetryCount++;
                        retryStartLedCover();
                        return;
                    }
                    this.mFactoryTransceiveResponseIntentSent = false;
                    this.mLedCoverTransceiveRetryCount = 0;
                    Log.e(TAG, "Could not transceive command to cover so turn off led cover");
                }
            } else {
                return;
            }
        }
        this.mContext.sendBroadcastAsUser(new Intent(LED_COVER_RETRY_DONE_INTENT_ACTION), UserHandle.SEM_ALL);
        Log.d(TAG, "Sent done intent, fail transceive");
        handleStopLedCover();
        Message msg = Message.obtain();
        msg.what = MSG_HANDLER_THREAD_RESPONSE;
        msg.arg1 = 2;
        this.mUiHandler.sendMessage(msg);
    }

    public boolean processSingleByteError(byte command, byte[] returnValue) {
        if (returnValue == null || command == (byte) 18 || returnValue.length > 1) {
            return false;
        }
        switch (returnValue[0]) {
            case (byte) -80:
            case (byte) -79:
            case (byte) -78:
            case (byte) -32:
            case (byte) -15:
            case (byte) -14:
            case (byte) -13:
            case (byte) -12:
            case (byte) -11:
            case (byte) -10:
            case (byte) -9:
            case (byte) 1:
            case (byte) 3:
            case (byte) 5:
            case (byte) 6:
                if (this.mLedCoverTransceiveRetryCount >= 13) {
                    this.mLedCoverTransceiveRetryCount = 0;
                    Log.e(TAG, "Could not transceive command to cover so turn off led cover");
                    break;
                }
                Log.e(TAG, "Repeat command " + command + " count: " + this.mLedCoverTransceiveRetryCount);
                this.mLedCoverTransceiveRetryCount++;
                retryStartLedCover();
                return SAFE_DEBUG;
            default:
                Log.e(TAG, "Transceive error - unknown error value: " + returnValue[0]);
                break;
        }
        return false;
    }

    private void checkMcuError(byte[] returnValue) {
        if (returnValue != null && returnValue.length > 1 && returnValue[0] == (byte) 105 && returnValue[1] == (byte) -123) {
            this.mContext.sendBroadcastAsUser(new Intent("com.sec.android.cover.ledcover.action.MCU_ERROR"), UserHandle.SEM_ALL, permission.LED_FOTA_ACCESS);
        }
    }

    private String getByteDataString(byte[] data) {
        if (data == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02X", new Object[]{Byte.valueOf(data[i])})).append(" ");
        }
        return sb.toString();
    }

    private void resetWcControlTimer() {
        long currTime = System.currentTimeMillis();
        if (currTime - this.mLastWcControlResetTime > WC_CONTROL_RESET_INTERVAL) {
            this.mNfcAdapter.semSetWirelessChargeEnabled(false);
            this.mLastWcControlResetTime = currTime;
        }
    }

    public void onCoverAttached() {
        Log.d(TAG, "onCoverAttached ");
        this.isCoverAttached = SAFE_DEBUG;
        startHandlerThread();
        CoverUtils.acquireWakeLockSafely(this.mLedVersionCheckWakeLock);
        this.mHtHandler.sendEmptyMessage(2);
    }

    public void onCoverDetached() {
        Log.d(TAG, "onCoverDetached");
        Secure.putInt(this.mContext.getContentResolver(), SETTING_SECURE_FACTORY_TEST_COUNT, -1);
        Secure.putString(this.mContext.getContentResolver(), SETTING_SECURE_FIRMWARE_VERSION, "");
        this.isCoverAttached = false;
        this.isCoverClosed = false;
        this.mFirmwareVersion = null;
        this.mHtHandler.removeMessages(2);
        CoverUtils.releaseWakeLockSafely(this.mLedVersionCheckWakeLock);
        stopLedCover();
    }

    public void onCoverOpened() {
        Log.d(TAG, "onCoverOpened");
        if (!this.isCoverAttached) {
            Log.e(TAG, "Cover is not attached, Ignore state change");
        } else if (this.isCoverClosed) {
            this.isCoverClosed = false;
            stopLedCover();
        }
    }

    public void onCoverClosed() {
        Log.d(TAG, "onCoverClosed");
        if (!this.isCoverAttached) {
            Log.e(TAG, "Cover is not attached, Ignore state change");
        } else if (!this.isCoverClosed) {
            this.isCoverClosed = SAFE_DEBUG;
            startLedCover();
        }
    }

    public boolean isLedTurnedOnOrRestarting() {
        return this.mOnOffState != OnOffState.OFF ? SAFE_DEBUG : false;
    }

    public void onLedTimeOut(LedOffCallback ledOffCallback) {
        if (this.isCoverAttached) {
            this.mLedOffCallback = ledOffCallback;
            stopLedCover();
            return;
        }
        Log.e(TAG, "Cover is not attached, Ignore onLedTimeOut");
    }

    public void onFactoryTestStart() {
        this.mFactoryTestEnabled = SAFE_DEBUG;
    }

    public void onFactoryTestSop() {
        this.mFactoryTestEnabled = false;
        this.mFactoryTestState = null;
        stopLedCover();
    }

    public void onFactoryTestCase(LedStateController testState) throws IllegalArgumentException {
        if (this.mFotaInProgress) {
            Log.e(TAG, "FOTA in progress - do not send any commands");
        } else if (!this.mFactoryTestEnabled) {
            Log.e(TAG, "Cannot send " + testState + ", FactoryTest is not enabled");
        } else if (testState == null) {
            throw new IllegalArgumentException("Test state cannot be null");
        } else {
            this.mFactoryTestState = testState;
            startLedCover();
        }
    }

    public void onFactoryTestLedOff() {
        if (this.mFotaInProgress) {
            Log.e(TAG, "FOTA in progress - do not send any commands");
        } else if (this.mFactoryTestEnabled) {
            stopLedCover();
        } else {
            Log.e(TAG, "Cannot send LED Off, FactoryTest is not enabled");
        }
    }

    public void onFotaStart(LedOffCallback callback) {
        this.mFotaLedOffCallback = callback;
        this.mFotaInProgress = SAFE_DEBUG;
        stopLedCover();
    }

    public void onFotaStop() {
        this.mFotaInProgress = false;
        Log.d(TAG, "onFotaStop: old FW version=" + this.mFirmwareVersion + ", FW version will be re-checked");
        this.mFirmwareVersion = null;
        Secure.putString(this.mContext.getContentResolver(), SETTING_SECURE_FIRMWARE_VERSION, "");
        CoverUtils.acquireWakeLockSafely(this.mLedVersionCheckWakeLock);
        this.mHtHandler.sendEmptyMessage(2);
    }

    public void onPowerKeyToCover(boolean shouldLedTurnOn) {
        if (!this.isCoverAttached) {
            Log.e(TAG, "Cover is not attached, Ignore onPowerKeyToCover");
        } else if (shouldLedTurnOn && this.isCoverClosed) {
            startLedCover();
        } else {
            stopLedCover();
        }
    }

    public void onStateChange(LedState ledState, LedContext ledContext, boolean shouldTurnLedOn) {
        Log.d(TAG, "onStateChange ledState=" + String.valueOf(ledState) + " ledContext=" + String.valueOf(ledContext) + " shouldTurnLedOn=" + String.valueOf(shouldTurnLedOn) + " isCoverAttached=" + String.valueOf(this.isCoverAttached) + " isCoverClosed=" + String.valueOf(this.isCoverClosed) + " mOnOffState=" + String.valueOf(this.mOnOffState));
        if (shouldTurnLedOn && ledState.getCommand(this.mLedStateMachine.getLedContext()) == null) {
            Log.e(TAG, "onStateChange ledState is null skip this state");
            this.mTransceiveCallback.onStateSent(null);
        } else if (this.isCoverAttached && this.isCoverClosed && shouldTurnLedOn) {
            startLedCover();
        } else if (this.mOnOffState != OnOffState.OFF) {
            stopLedCover();
        }
    }

    private void sendNfcFailIntentForFactoryMode(byte[] returnValue) {
        if (this.mFactoryTestEnabled && !this.mFactoryTransceiveResponseIntentSent && returnValue != null && returnValue.length > 2 && returnValue[0] == (byte) 105 && returnValue[1] == (byte) -123 && returnValue[2] == (byte) 0) {
            this.mFactoryTransceiveResponseIntentSent = SAFE_DEBUG;
            this.mContext.sendBroadcastAsUser(new Intent("android.intent.action.NFC_LED_COVER_FPCB_DISCONNECT"), UserHandle.SEM_ALL);
        }
    }

    private boolean isTestModeEnabled() {
        return (this.TEST <= 0 || this.TEST == 42) ? false : SAFE_DEBUG;
    }
}
