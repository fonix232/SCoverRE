package com.samsung.android.share.executor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.share.SShareConstants;
import com.samsung.android.share.executor.data.NlgRequestInfo;
import com.samsung.android.share.executor.data.State;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.Observable;
import java.util.Observer;
import org.json.JSONException;
import org.json.JSONObject;

public class ExecutorCommandHandler {
    public static final String EMES_REQUEST_CONTEXT = "emes_request_context";
    public static final String EMES_REQUEST_PARAM_FILLING = "emes_request_param_filling";
    public static final String EMES_STATE = "emes_state";
    public static final String ESEM_CONTEXT_RESULT = "esem_context_result";
    public static final String ESEM_PARAM_FILLING_RESULT = "esem_param_filling_result";
    public static final String ESEM_REQUEST_NLG = "esem_request_nlg";
    public static final String ESEM_STATE_LOG = "esem_state_log";
    public static final String ESEM_STATE_RESULT = "esem_state_result";
    public static final String RESULT_FAILURE = "failure";
    public static final String RESULT_SUCCESS = "success";
    private static final int SEQ_NUM_RULE_CANCEL = -1;
    private static final String TAG = ExecutorCommandHandler.class.getSimpleName();
    private static ExecutorCommandHandler mInstance = null;
    private String ACTIION_EXECUTOR_MANAGER_COMMAND = "com.samsung.android.bixby.agent.ACTION_COMMAND";
    private String BIXBY_COMMAND = "bixby_command";
    private Observer mCmdObserver = new C02461();
    private Context mContext = null;
    private String mCurrentAppName = null;
    private Command mLastRecvCmd = null;
    private Command mLastRecvStateCmd = null;
    private IExecutorCommandListener mListener = null;

    class C02461 implements Observer {
        C02461() {
        }

        public void update(Observable observable, Object obj) {
            ExecutorCommandHandler.this.handleCommand((Intent) obj);
        }
    }

    public enum NlgParamMode {
        NONE,
        TARGETED,
        MULTIPLE;

        public String toString() {
            String str = "\"nlgParamMode\":";
            switch (m15x7a3b8137()[ordinal()]) {
                case 1:
                    return "\"nlgParamMode\":\"multiple\"";
                case 2:
                    return "\"nlgParamMode\":\"none\"";
                case 3:
                    return "\"nlgParamMode\":\"targeted\"";
                default:
                    return super.toString();
            }
        }
    }

    private ExecutorCommandHandler() {
    }

    public static synchronized ExecutorCommandHandler createInstance(Context context, String str, IExecutorCommandListener iExecutorCommandListener) {
        ExecutorCommandHandler executorCommandHandler;
        synchronized (ExecutorCommandHandler.class) {
            if (mInstance == null) {
                mInstance = new ExecutorCommandHandler();
            }
            mInstance.setContext(context);
            mInstance.setCurrentAppName(str);
            mInstance.setListener(iExecutorCommandListener);
            mInstance.registerCommandObserver();
            executorCommandHandler = mInstance;
        }
        return executorCommandHandler;
    }

    private String createLogStateData(String str, String str2) {
        return new StringBuffer().append("\"appName\":\"").append(this.mCurrentAppName).append("\",").append("\"logType\":\"").append(str).append("\",").append("\"stateIds\":\"").append(str2).append("\"").toString();
    }

    private Context getContext() {
        return this.mContext;
    }

    public static synchronized ExecutorCommandHandler getInstance() {
        ExecutorCommandHandler executorCommandHandler;
        synchronized (ExecutorCommandHandler.class) {
            executorCommandHandler = mInstance;
        }
        return executorCommandHandler;
    }

    private String getStateInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.mListener != null) {
            Iterable<String> states = this.mListener.onScreenStatesRequested().getStates();
            if (states != null && states.size() > 0) {
                for (String append : states) {
                    stringBuilder.append(append).append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        } else {
            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }

    private void handleContextCommand(Command command) {
        sendContextCommandResponse();
    }

    private void handleParamFillingCommand(Command command) {
        if (this.mListener == null) {
            Log.e(TAG, "Command Listener is not set");
            return;
        }
        try {
            if (this.mListener.onParamFillingReceived(ParamFillingReader.read(new JSONObject(command.getContent()).getString("slotFillingResult")))) {
                sendParamFillingCommandResponse(RESULT_SUCCESS);
            } else {
                sendParamFillingCommandResponse(RESULT_FAILURE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            sendParamFillingCommandResponse(RESULT_FAILURE);
        }
    }

    private void handleStateCommand(Command command) {
        try {
            State read = StateReader.read(new JSONObject(command.getContent()).get("state").toString());
            if (read.getSeqNum().intValue() == -1) {
                this.mListener.onRuleCanceled(read.getRuleId());
            } else {
                this.mListener.onStateReceived(read);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            sendStateCommandResponse(RESULT_FAILURE);
        }
    }

    private void logState(String str, String str2) throws IllegalArgumentException {
        if (str2 == null) {
            throw new IllegalArgumentException("Log value cannot be null.");
        }
        String createLogStateData = createLogStateData(str, str2);
        Command command = new Command();
        command.setVersion("1.0");
        command.setRequestId(SmartFaceManager.PAGE_BOTTOM);
        command.setCommand(ESEM_STATE_LOG);
        command.setContent("{" + createLogStateData + "}");
        sendResponseToEm(command);
    }

    private void registerCommandObserver() {
        CommandObserver.getInstance().deleteObservers();
        CommandObserver.getInstance().addObserver(this.mCmdObserver);
    }

    public static void requestStateCommand(Context context) {
        try {
            Intent intent = new Intent(SShareConstants.EM_ACTION_CHOOSER_EVENT);
            intent.putExtra(SShareConstants.EXTRA_CHOOSER_EM_COMMAND, "state_request");
            context.sendBroadcast(intent);
        } catch (SecurityException e) {
            Log.e(TAG, "requestStateCommand: SecurityException");
        }
    }

    private void sendContextCommandResponse() {
        if (this.mLastRecvCmd == null || !this.mLastRecvCmd.getCommand().equals(EMES_REQUEST_CONTEXT)) {
            Log.e(TAG, "No Received Command.");
            return;
        }
        String str;
        if (this.mListener != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ ").append(this.mListener.onScreenStatesRequested().toString()).append(FingerprintManager.FINGER_PERMISSION_DELIMITER).append("\"appName\":\"").append(this.mCurrentAppName).append("\"").append("}");
            str = "{\"result\":\"" + RESULT_SUCCESS + "\"," + "\"appContext\":" + stringBuilder.toString() + "}";
        } else {
            Log.e(TAG, "Executor Commnad Listener is not set.");
            str = "{\"result\":\"" + RESULT_FAILURE + "\"," + "}";
        }
        Command command = new Command();
        command.setVersion(this.mLastRecvCmd.getVersion());
        command.setRequestId(this.mLastRecvCmd.getRequestId());
        command.setCommand(ESEM_CONTEXT_RESULT);
        command.setContent(str);
        sendResponseToEm(command);
    }

    private void sendNlgCommand(String str) {
        if (this.mLastRecvStateCmd == null) {
            Log.e(TAG, "No Received State Command.");
            return;
        }
        Command command = new Command();
        command.setVersion(this.mLastRecvStateCmd.getVersion());
        command.setRequestId(this.mLastRecvStateCmd.getRequestId());
        command.setCommand(ESEM_REQUEST_NLG);
        command.setContent("{" + str + "}");
        sendResponseToEm(command);
    }

    private void sendParamFillingCommandResponse(String str) {
        if (this.mLastRecvCmd == null || !this.mLastRecvCmd.getCommand().equals(EMES_REQUEST_PARAM_FILLING)) {
            Log.e(TAG, "No Received Parameter Filling Command.");
            return;
        }
        Command command = new Command();
        command.setVersion(this.mLastRecvCmd.getVersion());
        command.setRequestId(this.mLastRecvCmd.getRequestId());
        command.setCommand(ESEM_PARAM_FILLING_RESULT);
        command.setContent("{\"result\":\"" + str + "\"}");
        sendResponseToEm(command);
    }

    private void sendResponseToEm(Command command) {
        try {
            Intent intent = new Intent();
            intent.setAction(this.ACTIION_EXECUTOR_MANAGER_COMMAND);
            intent.putExtra(this.BIXBY_COMMAND, command.toJson());
            Log.d(TAG, "send response to EM via Intent: " + command.toJson());
            this.mContext.sendBroadcast(intent);
        } catch (SecurityException e) {
            Log.e(TAG, "sendResponseToEm: SecurityException");
        }
    }

    public void clearListener() {
        this.mListener = null;
    }

    public String getCurrentAppName() {
        return this.mCurrentAppName;
    }

    public IExecutorCommandListener getListener() {
        return this.mListener;
    }

    public void handleCommand(Intent intent) {
        String stringExtra = intent.getStringExtra(this.BIXBY_COMMAND);
        if (stringExtra != null) {
            Command command = new Command(stringExtra);
            String command2 = command.getCommand();
            if (command2 == null) {
                Log.e(TAG, "Invalid Command:" + stringExtra);
                return;
            }
            this.mLastRecvCmd = command;
            if (command2.equals(EMES_STATE)) {
                this.mLastRecvStateCmd = command;
                handleStateCommand(command);
            } else if (command2.equals(EMES_REQUEST_CONTEXT)) {
                handleContextCommand(command);
            } else if (command2.equals(EMES_REQUEST_PARAM_FILLING)) {
                handleParamFillingCommand(command);
            } else {
                Log.e(TAG, "Unsupported Command" + command2);
            }
        }
    }

    public void logEnterState(String str) throws IllegalStateException {
        logState("state_enter", str);
    }

    public void logExitState(String str) throws IllegalStateException {
        logState("state_exit", str);
    }

    public void requestNlg(String str, NlgRequestInfo nlgRequestInfo) {
        requestNlg(str, nlgRequestInfo, NlgParamMode.MULTIPLE);
    }

    public void requestNlg(String str, NlgRequestInfo nlgRequestInfo, NlgParamMode nlgParamMode) throws IllegalStateException, IllegalArgumentException {
        if (nlgRequestInfo == null) {
            throw new IllegalArgumentException("NlgRequestInfo cannot be null.");
        }
        String str2 = "\"currentStateIds\":\"" + getStateInfo() + "\"";
        sendNlgCommand(String.format("\"requestedAppName\":\"%s\",%s,%s,%s", new Object[]{str, nlgRequestInfo.toString(), str2, nlgParamMode.toString()}));
    }

    public void sendStateCommandResponse(String str) {
        Throwable e;
        if (this.mLastRecvStateCmd == null) {
            Log.e(TAG, "No Received State Command.");
            return;
        }
        Command command = new Command();
        command.setVersion(this.mLastRecvStateCmd.getVersion());
        command.setRequestId(this.mLastRecvStateCmd.getRequestId());
        command.setCommand(ESEM_STATE_RESULT);
        String str2 = "";
        try {
            JSONObject jSONObject = new JSONObject(this.mLastRecvStateCmd.getContent());
            JSONObject jSONObject2;
            try {
                str2 = jSONObject.get("state").toString();
                jSONObject2 = jSONObject;
            } catch (JSONException e2) {
                e = e2;
                jSONObject2 = jSONObject;
                e.printStackTrace();
                command.setContent("{\"result\":" + "\"" + str + "\"," + "\"state\":" + str2 + "}");
                sendResponseToEm(command);
                this.mLastRecvStateCmd = null;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            command.setContent("{\"result\":" + "\"" + str + "\"," + "\"state\":" + str2 + "}");
            sendResponseToEm(command);
            this.mLastRecvStateCmd = null;
        }
        command.setContent("{\"result\":" + "\"" + str + "\"," + "\"state\":" + str2 + "}");
        sendResponseToEm(command);
        this.mLastRecvStateCmd = null;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setCurrentAppName(String str) {
        this.mCurrentAppName = str;
    }

    public void setListener(IExecutorCommandListener iExecutorCommandListener) {
        this.mListener = iExecutorCommandListener;
    }
}
