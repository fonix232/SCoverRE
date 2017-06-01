package com.sec.android.cover.ledcover.fsm.grace;

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
import com.sec.android.cover.ledcover.fsm.grace.LedStateMachine.LedStateMachineListener;
import java.util.Arrays;

public class LedPowerOnOffStateController implements LedStateMachineListener {
    private static final int CMD_LED_OFF = 18;
    private static final int HANDLER_THREAD_FAILED_START = 1;
    private static final int HANDLER_THREAD_FAILED_TRANSCEIVE = 2;
    private static final int HANDLER_THREAD_STOP_DETACHED = 1;
    private static final int HANDLER_THREAD_STOP_LED_OFF = 0;
    private static final int HANDLER_THREAD_SUCCESSFUL_TRANSCEIVE = 0;
    private static final byte[] LED_CLEAR_RESET_COMMAND = new byte[]{(byte) 0, (byte) -94, (byte) 0, (byte) 0, (byte) 6, (byte) 6, (byte) 114, (byte) 0, (byte) 0, (byte) -1, (byte) -1};
    private static final int LED_COVER_CLEAR_COUNT_MAX = 3;
    private static final int LED_COVER_RETRY_COUNT_MAX = 13;
    private static final long LED_COVER_RETRY_DELAY = 500;
    private static final String LED_COVER_RETRY_DONE_INTENT_ACTION = "android.intent.action.NFC_LED_COVER_MAX_RETRY_DONE";
    private static final byte[] LED_OFF_COMMAND = new byte[]{(byte) 0, (byte) -94, (byte) 0, (byte) 0, (byte) 6, (byte) 6, (byte) 18, (byte) 0, (byte) 0, (byte) -1, (byte) -1};
    private static final int MSG_COVER_TOUCH_CHECK = 1;
    private static final int MSG_COVER_VERSION_CHECK = 2;
    private static final int MSG_HANDLER_THREAD_RESPONSE = 1000;
    private static final int MSG_HANDLER_THREAD_STOP = 1001;
    private static final int MSG_START_STOP_TRANSCEIVE_RETRY_LED = 0;
    private static final int RESPONSE_OFFSET = 5;
    private static final int RETRY_LED_COVER = 2;
    private static final boolean SAFE_DEBUG = true;
    private static final String SETTING_SECURE_FIRMWARE_VERSION = "led_cover_firmware_version";
    private static final int START_LED_COVER = 1;
    private static final int STOP_LED_COVER = 0;
    private static final String TAG = LedPowerOnOffStateController.class.getSimpleName();
    private static final long TOUCH_CHECK_INTERVAL = 100;
    private static final byte[] TOUCH_CHECK_REPLY = new byte[]{(byte) 6, (byte) 17, (byte) 0, TOUCH_CHECK_REPLY_ACCEPT, (byte) -1, (byte) -1};
    private static final byte TOUCH_CHECK_REPLY_ACCEPT = (byte) 1;
    private static final int TOUCH_CHECK_REPLY_POSITION = 3;
    private static final byte TOUCH_CHECK_REPLY_REJECT = (byte) 2;
    private static final byte[] TOUCH_CHECK_REQUEST = new byte[]{(byte) 0, (byte) -94, (byte) 0, (byte) 0, (byte) 6, (byte) 6, (byte) 17, (byte) 0, (byte) 0, (byte) -1, (byte) -1};
    private static final byte[] VERSION_CHECK_COMMAND = new byte[]{(byte) 0, (byte) -94, (byte) 0, (byte) 0, (byte) 7, (byte) 7, (byte) 113, (byte) 0, (byte) 0, (byte) 0, (byte) -1, (byte) -1};
    private static final long WC_CONTROL_RESET_INTERVAL = 30000;
    private int TEST;
    private boolean isCoverAttached = false;
    private boolean isCoverClosed = false;
    private TransceiveCallback mCallback;
    private Context mContext;
    private ScoverManager mCoverManager;
    private String mFirmwareVersion;
    private Callback mHandlerCallback = new C00491();
    private Handler mHtHandler;
    private long mLastWcControlResetTime;
    private int mLedCoverClearRetryCount = 0;
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
    private Handler mUiHandler;
    private HandlerThread mWorkerThread;
    private int testCount;

    public interface LedOffCallback {
        void onLedOff();
    }

    public interface TransceiveCallback {
        void onStateSent(LedState ledState);

        void onTouchEvent(boolean z);
    }

    class C00491 implements Callback {
        C00491() {
        }

        public boolean handleMessage(Message msg) {
            Log.d(LedPowerOnOffStateController.TAG, "handleMessage msg=" + String.valueOf(msg));
            switch (msg.what) {
                case 0:
                    LedPowerOnOffStateController.this.mHtHandler.removeMessages(1);
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedTouchCheckWakeLock);
                    int action = msg.arg1;
                    if (action != 1) {
                        if (action != 2) {
                            LedPowerOnOffStateController.this.handleStopLedCover();
                            break;
                        }
                        LedPowerOnOffStateController.this.retryTransceiveLedCover();
                        break;
                    }
                    LedPowerOnOffStateController.this.handleStartLedCover();
                    break;
                case 1:
                    LedPowerOnOffStateController.this.handleTouchCheckLedCover(msg.arg1);
                    break;
                case 2:
                    if (!LedPowerOnOffStateController.this.isTestModeEnabled()) {
                        LedPowerOnOffStateController.this.handleCoverVersionCheck();
                    }
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedVersionCheckWakeLock);
                    break;
                case LedPowerOnOffStateController.MSG_HANDLER_THREAD_RESPONSE /*1000*/:
                    LedState state = null;
                    if (msg.arg1 == 0) {
                        state = msg.obj;
                    }
                    LedPowerOnOffStateController.this.mCallback.onStateSent(state);
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedOnOffWakeLock);
                    break;
                case LedPowerOnOffStateController.MSG_HANDLER_THREAD_STOP /*1001*/:
                    int stopRreason = msg.arg1;
                    if (stopRreason == 1) {
                        LedPowerOnOffStateController.this.stopHandlerThread();
                    } else if (stopRreason == 0 && LedPowerOnOffStateController.this.mLedOffCallback != null) {
                        LedPowerOnOffStateController.this.mLedOffCallback.onLedOff();
                        LedPowerOnOffStateController.this.mLedOffCallback = null;
                    }
                    CoverUtils.releaseWakeLockSafely(LedPowerOnOffStateController.this.mLedOnOffWakeLock);
                    break;
            }
            return false;
        }
    }

    public enum OnOffState {
        ON,
        STARTING,
        OFF,
        RETRY
    }

    private static final class OutgoingSystemEvent {
        private static final String SYSTEM_EVENT_KEY_TOUCH_LISTENER_RESPONSE = "lcd_touch_listener_respone";
        private static final String SYSTEM_EVENT_KEY_TOUCH_LISTENER_TYPE = "lcd_touch_listener_type";
        private static final String SYSTEM_EVENT_KEY_TYPE = "event_type";
        private static final int SYSTEM_EVENT_TYPE_DISABLE_LDC_OFF_BY_COVER = 1;
        private static final int SYSTEM_EVENT_TYPE_TOUCH_RESPONSE = 0;

        private OutgoingSystemEvent() {
        }
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
        this.mCallback = callback;
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
            handleSendDataToNfcLedCover(this.mLedStateMachine.getCurrentLedState());
        } else if (this.mLedCoverStartRetryCount < 13) {
            Log.w(TAG, "Failed to start NFC LED Cover, retry after some time");
            this.mLedCoverStartRetryCount++;
            retryStartLedCover();
        } else {
            Log.e(TAG, "Failed to start NFC LED Cover");
            this.mLedCoverStartRetryCount = 0;
            Log.d(TAG, "Stop LedCover for FINAL retry, result: " + String.valueOf(this.mNfcAdapter.semStopLedCoverMode()));
            handleStopLedCover();
            Message msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_RESPONSE;
            msg.arg1 = 1;
            this.mUiHandler.sendMessage(msg);
        }
    }

    private void retryTransceiveLedCover() {
        if (this.mLedCoverClearRetryCount < 3) {
            Log.e(TAG, "Repeat command after clear/reset; count: " + this.mLedCoverClearRetryCount);
            this.mLedCoverClearRetryCount++;
            this.mLedCoverTransceiveRetryCount++;
            byte[] clearResult = this.mNfcAdapter.semTransceiveDataWithLedCover(LED_CLEAR_RESET_COMMAND);
            Log.d(TAG, "Transceive clear/reset LED result: " + getByteDataString(clearResult));
            Message msg;
            if (isValidResponse(LED_CLEAR_RESET_COMMAND, clearResult)) {
                this.mOnOffState = OnOffState.ON;
                msg = Message.obtain();
                msg.what = 0;
                msg.arg1 = 1;
                this.mHtHandler.sendMessageDelayed(msg, LED_COVER_RETRY_DELAY);
                return;
            } else if (clearResult == null || (!(clearResult.length == 1 && clearResult[0] == (byte) -78) && clearResult.length <= 1)) {
                this.mLedCoverClearRetryCount = 0;
                retryStartLedCover();
                return;
            } else {
                msg = Message.obtain();
                msg.what = 0;
                msg.arg1 = 2;
                this.mHtHandler.sendMessageDelayed(msg, LED_COVER_RETRY_DELAY);
                return;
            }
        }
        this.mLedCoverClearRetryCount = 0;
        retryStartLedCover();
        Log.e(TAG, "Could not clear/reset cover so reset led cover");
    }

    private void retryStartLedCover() {
        this.mOnOffState = OnOffState.RETRY;
        Log.d(TAG, "Stop LedCover for retry, result: " + String.valueOf(this.mNfcAdapter.semStopLedCoverMode()));
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 1;
        this.mHtHandler.sendMessageDelayed(msg, LED_COVER_RETRY_DELAY);
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
        this.mLedCoverClearRetryCount = 0;
        Message msg;
        if (!this.isCoverAttached) {
            msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_STOP;
            msg.arg1 = 1;
            this.mUiHandler.sendMessage(msg);
        } else if (this.mLedOffCallback == null || this.mHtHandler.hasMessages(0)) {
            CoverUtils.releaseWakeLockSafely(this.mLedOnOffWakeLock);
        } else {
            msg = Message.obtain();
            msg.what = MSG_HANDLER_THREAD_STOP;
            msg.arg1 = 0;
            this.mUiHandler.sendMessage(msg);
            if (this.mFirmwareVersion == null) {
                CoverUtils.acquireWakeLockSafely(this.mLedVersionCheckWakeLock);
                this.mHtHandler.sendEmptyMessageDelayed(2, LED_COVER_RETRY_DELAY);
            }
        }
    }

    private void handleSendDataToNfcLedCover(LedState state) {
        Log.d(TAG, "handleSendDataToLedCover new state to transceive is: " + state);
        Log.d(TAG, "Firmware version: " + this.mFirmwareVersion + " " + " GraceLEDCoverCMD version: " + 18);
        if (this.mOnOffState != OnOffState.ON) {
            Log.e(TAG, "Something went wrong, mOnOffState should be ON and is: " + this.mOnOffState);
        } else {
            boolean nullData = false;
            byte[][] data = state.getCommand(this.mLedStateMachine.getLedContext());
            state.onSendingState(this.mLedStateMachine.getLedContext());
            byte command = state.getCommandCodeByte();
            Log.d(TAG, "handleSendDataToLedCover : transceive data : " + getByteDataString(data));
            if (data == null) {
                data = new byte[][]{LED_OFF_COMMAND};
                nullData = SAFE_DEBUG;
            }
            byte[] returnValue = null;
            int length = data.length;
            int i = 0;
            while (i < length) {
                byte[] segment = data[i];
                try {
                    resetWcControlTimer();
                    returnValue = this.mNfcAdapter.semTransceiveDataWithLedCover(segment);
                    if (returnValue != null) {
                        Log.d(TAG, "Response data: " + getByteDataString(returnValue));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in trancieve command", e);
                }
                if (isValidResponse(segment, returnValue)) {
                    i++;
                } else {
                    Log.e(TAG, "Error parsing response");
                    processInvalidCommand(command, returnValue);
                    return;
                }
            }
            this.mContext.sendBroadcastAsUser(new Intent(LED_COVER_RETRY_DONE_INTENT_ACTION), UserHandle.SEM_ALL);
            Log.d(TAG, "Sent done intent, sucess");
            if (!nullData) {
                onCommandSentHandleTouch(state);
            }
        }
        this.mLedCoverTransceiveRetryCount = 0;
        this.mLedCoverClearRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = MSG_HANDLER_THREAD_RESPONSE;
        msg.arg1 = 0;
        msg.obj = state;
        this.mUiHandler.sendMessage(msg);
    }

    private void handleCoverVersionCheck() {
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

    private boolean transceiveVersionCheck() {
        byte[] response = this.mNfcAdapter.semTransceiveDataWithLedCover(VERSION_CHECK_COMMAND);
        Log.d(TAG, "Version check response: " + getByteDataString(response));
        boolean validResponse = (response != null && response.length >= VERSION_CHECK_COMMAND.length - 5 && response[0] == VERSION_CHECK_COMMAND[5] && response[1] == VERSION_CHECK_COMMAND[6] && response[2] == VERSION_CHECK_COMMAND[7] && response[5] == VERSION_CHECK_COMMAND[10] && response[6] == VERSION_CHECK_COMMAND[11]) ? SAFE_DEBUG : false;
        if (validResponse) {
            this.mFirmwareVersion = String.format("%02X %02X", new Object[]{Byte.valueOf(response[3]), Byte.valueOf(response[4])});
        }
        return validResponse;
    }

    private void onCommandSentHandleTouch(LedState state) {
        int listenerType = state.getTouchEventListenerType();
        if (listenerType == -1) {
            this.mHtHandler.removeMessages(1);
            CoverUtils.releaseWakeLockSafely(this.mLedTouchCheckWakeLock);
            return;
        }
        if (isTestModeEnabled()) {
            this.testCount = 0;
        }
        scheduleTouchCheck(listenerType);
    }

    private void scheduleTouchCheck(int listenerType) {
        CoverUtils.acquireWakeLockSafely(this.mLedTouchCheckWakeLock);
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
        if (isFinishedTouchReply(returnData)) {
            Log.d(TAG, "Touch reply for listener type: " + listenerType + " reply: " + returnData[3]);
            switch (returnData[3]) {
                case (byte) 1:
                    this.mCallback.onTouchEvent(false);
                    break;
                case (byte) 2:
                    this.mCallback.onTouchEvent(SAFE_DEBUG);
                    break;
            }
            Bundle touchData = new Bundle();
            touchData.putInt(com.sec.android.cover.ledcover.fsm.dream.LedPowerOnOffStateController.OutgoingSystemEvent.KEY_TYPE, 0);
            touchData.putInt("lcd_touch_listener_type", listenerType);
            touchData.putInt("lcd_touch_listener_respone", returnData[3]);
            try {
                this.mCoverManager.sendSystemEvent(touchData);
            } catch (SsdkUnsupportedException e2) {
                Log.e(TAG, "Shouldn't happen", e2);
            }
            scheduleTouchCheck(listenerType);
            return;
        }
        Log.d(TAG, "No touch reply, keep trying");
        scheduleTouchCheck(listenerType);
    }

    private byte[] buildTestTouchReply() {
        byte b = TOUCH_CHECK_REPLY_REJECT;
        byte[] returnData = Arrays.copyOf(TOUCH_CHECK_REPLY, TOUCH_CHECK_REPLY.length);
        if (this.TEST == 2) {
            b = TOUCH_CHECK_REPLY_ACCEPT;
        }
        returnData[3] = b;
        return returnData;
    }

    private boolean isFinishedTouchReply(byte[] returnData) {
        boolean result = (returnData == null || returnData.length < TOUCH_CHECK_REPLY.length) ? false : SAFE_DEBUG;
        if (!result) {
            return false;
        }
        int i = 0;
        while (i < TOUCH_CHECK_REPLY.length) {
            if (returnData[i] != TOUCH_CHECK_REPLY[i] && (i != 3 || (returnData[i] != TOUCH_CHECK_REPLY_ACCEPT && returnData[i] != TOUCH_CHECK_REPLY_REJECT))) {
                return false;
            }
            i++;
        }
        return result;
    }

    private void startLedCover() {
        CoverUtils.acquireWakeLockSafely(this.mLedOnOffWakeLock);
        if (this.mHtHandler.hasMessages(0)) {
            this.mHtHandler.removeMessages(0);
        }
        this.mLedCoverStartRetryCount = 0;
        this.mLedCoverTransceiveRetryCount = 0;
        this.mLedCoverClearRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 1;
        this.mHtHandler.sendMessage(msg);
    }

    private void stopLedCover() {
        CoverUtils.acquireWakeLockSafely(this.mLedOnOffWakeLock);
        this.mLedCoverStartRetryCount = 0;
        this.mLedCoverTransceiveRetryCount = 0;
        this.mLedCoverClearRetryCount = 0;
        Message msg = Message.obtain();
        msg.what = 0;
        msg.arg1 = 0;
        this.mHtHandler.sendMessageAtFrontOfQueue(msg);
    }

    private boolean isValidCoverStartData(byte[] coverStartData) {
        return (coverStartData == null || coverStartData.length <= 1) ? false : SAFE_DEBUG;
    }

    private boolean isValidResponse(byte[] data, byte[] response) {
        if (response == null || response.length < data.length - 5) {
            return false;
        }
        for (int i = 0; i < data.length - 5; i++) {
            if (response[i] != data[i + 5]) {
                return false;
            }
        }
        return SAFE_DEBUG;
    }

    private void processInvalidCommand(byte command, byte[] returnValue) {
        if (!(returnValue == null || command == (byte) 18)) {
            if (returnValue.length != 1 || !processSingleByteError(command, returnValue)) {
                if (returnValue.length > 1) {
                    if (this.mLedCoverTransceiveRetryCount >= 13 || this.mLedCoverClearRetryCount >= 3) {
                        this.mLedCoverTransceiveRetryCount = 0;
                        this.mLedCoverClearRetryCount = 0;
                        Log.e(TAG, "Could not transceive command to cover so turn off led cover");
                    } else {
                        Log.e(TAG, "Parsing response error try to clear/reset Nfc Cover with clear Command 0x72");
                        retryTransceiveLedCover();
                        return;
                    }
                }
            }
            return;
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
            case (byte) 1:
            case (byte) 3:
            case (byte) 5:
            case (byte) 6:
                if (this.mLedCoverTransceiveRetryCount >= 13) {
                    this.mLedCoverTransceiveRetryCount = 0;
                    this.mLedCoverClearRetryCount = 0;
                    Log.e(TAG, "Could not transceive command to cover so turn off led cover");
                    break;
                }
                Log.e(TAG, "Repeat command " + command + " count: " + this.mLedCoverTransceiveRetryCount);
                if (returnValue[0] == (byte) -78) {
                    retryTransceiveLedCover();
                    return SAFE_DEBUG;
                }
                this.mLedCoverTransceiveRetryCount++;
                retryStartLedCover();
                return SAFE_DEBUG;
            default:
                Log.e(TAG, "Transceive error - unknown error value: " + returnValue[0]);
                break;
        }
        return false;
    }

    private String getByteDataString(byte[][] data) {
        if (data == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (byte[] byteDataString : data) {
            sb.append(getByteDataString(byteDataString));
        }
        return sb.toString();
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

    public void onLedTimeOut() {
        onLedTimeOut(null);
    }

    public void onLedTimeOut(LedOffCallback ledOffCallback) {
        if (this.isCoverAttached) {
            this.mLedOffCallback = ledOffCallback;
            stopLedCover();
            return;
        }
        Log.e(TAG, "Cover is not attached, Ignore onLedTimeOut");
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
        if (this.isCoverAttached && this.isCoverClosed && shouldTurnLedOn) {
            startLedCover();
        } else if (this.mOnOffState != OnOffState.OFF) {
            stopLedCover();
        }
    }

    private boolean isTestModeEnabled() {
        return (this.TEST <= 0 || this.TEST == 42) ? false : SAFE_DEBUG;
    }
}
