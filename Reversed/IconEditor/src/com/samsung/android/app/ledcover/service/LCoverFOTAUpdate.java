package com.samsung.android.app.ledcover.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat.Builder;
import android.widget.Toast;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.android.app.ledcover.fota.CoverHexParser;
import com.samsung.android.app.ledcover.fota.FOTAManager;
import com.samsung.android.app.ledcover.fota.OnDownloadListener;
import com.samsung.android.app.ledcover.fota.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class LCoverFOTAUpdate extends Service {
    public static final String CLIENT_MSG_BROADCAST_INTENT_ACTION = "LCoverFOTAUpdate";
    public static final String CLIENT_MSG_DATA = "CLIENT_MSG_DATA";
    public static final String CLIENT_MSG_TYPE = "CLIENT_MSG_TYPE";
    public static final int CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED = 2;
    public static final int CLIENT_MSG_UPDATE_FIRMWARE_FAILED = 3;
    public static final int CLIENT_MSG_UPDATING_FIRMWARE_PERCENT = 1;
    private static final String LOCATION = "global";
    public static final int MSG_CHECK_UPDATE = 3;
    public static final int MSG_DOWNLOAD_UPDATE = 4;
    public static final int MSG_SHOW_CHECK_UPDATE_NOTIFICATION = 5;
    public static final String MSG_TYPE = "MSG_TYPE";
    public static final int MSG_UPDATING_FIRMWARE_PERCENT = 6;
    public static final int NOTIFICATION_UPDATE_FIRMWARE_ID = 10;
    private static final String PROD_ID = "Test-EF-NG95X";
    public static final String TAG = "[LED_COVER]LCoverFOTAUpdate";
    private static int mUpdateProgress;
    private long allSize;
    private String authPath;
    private boolean authSuccessKey;
    private String[] fileName;
    private String flashAddress;
    private byte[] flashByte;
    private int flashDataLength;
    OnDownloadListener listener;
    private FOTAManager mFOTAManager;
    private Messenger mMessenger;
    private NfcAdapter mNfcAdapter;
    private NotificationManager mNotificationManager;
    private ServiceHandler mServiceHandler;
    private Looper mServiceLooper;
    private FotaTask mTaskProgress;
    private Builder mUpdatingFirmwareNotificationBuilder;
    private String mcuPath;
    private boolean passKey;
    private int passMakeCmdCount;
    private byte[] res;
    private boolean successKey;
    private HandlerThread thread;
    private int value;

    class FotaTask extends AsyncTask<Integer, Integer, Void> {
        int size;

        FotaTask() {
            this.size = 0;
        }

        protected void onPreExecute() {
            LCoverFOTAUpdate.this.value = 0;
            if (LCoverFOTAUpdate.this.authPath != null) {
                LCoverFOTAUpdate.this.authSuccessKey = Util.authStartNfcMode(LCoverFOTAUpdate.this.mNfcAdapter);
            } else if (LCoverFOTAUpdate.this.mcuPath != null) {
                LCoverFOTAUpdate.this.successKey = true;
            }
            if (!(LCoverFOTAUpdate.this.successKey || LCoverFOTAUpdate.this.authSuccessKey)) {
                LCoverFOTAUpdate.this.updateFail();
                SLog.m12v(LCoverFOTAUpdate.TAG, "[onPreExecute] :  + Error Code");
            }
            SLog.m12v(LCoverFOTAUpdate.TAG, "[onPreExecute] : " + LCoverFOTAUpdate.this.successKey);
        }

        protected Void doInBackground(Integer... values) {
            try {
                if (LCoverFOTAUpdate.this.authPath != null && LCoverFOTAUpdate.this.authSuccessKey) {
                    runAuthFota();
                    Util.authStopNfcMode(LCoverFOTAUpdate.this.mNfcAdapter);
                }
                if (LCoverFOTAUpdate.this.mcuPath != null) {
                    if (Util.mcuStartMode(LCoverFOTAUpdate.this.mNfcAdapter)) {
                        for (int i = LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT; i <= 15; i += LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT) {
                            Thread.sleep(100);
                            this.size += i;
                            LCoverFOTAUpdate.this.value = (int) ((((double) this.size) / ((double) LCoverFOTAUpdate.this.allSize)) * 100.0d);
                            SLog.m12v(LCoverFOTAUpdate.TAG, this.size + " ," + LCoverFOTAUpdate.this.value);
                            Integer[] numArr = new Integer[LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT];
                            numArr[0] = Integer.valueOf(LCoverFOTAUpdate.this.value);
                            publishProgress(numArr);
                        }
                        runMcuFota();
                    } else {
                        LCoverFOTAUpdate.this.updateFail();
                        SLog.m12v(LCoverFOTAUpdate.TAG, "Fail MCU FOTA");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            if (Util.checkErrorCode(LCoverFOTAUpdate.this.res)) {
                SLog.m12v(LCoverFOTAUpdate.TAG, "[onProgressUpdate] :  + Error Code");
            }
            SLog.m12v(LCoverFOTAUpdate.TAG, "Update Flash process : " + values[0] + " updateprocess = " + LCoverFOTAUpdate.mUpdateProgress);
            if (LCoverFOTAUpdate.mUpdateProgress != (values[0].intValue() / LCoverFOTAUpdate.CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED) + 50) {
                LCoverFOTAUpdate.mUpdateProgress = (values[0].intValue() / LCoverFOTAUpdate.CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED) + 50;
                LCoverFOTAUpdate.this.updateProgress();
            }
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (LCoverFOTAUpdate.this.mcuPath != null) {
                Util.mcuStopMode(LCoverFOTAUpdate.this.mNfcAdapter);
            }
            LCoverFOTAUpdate.this.updateSuccess();
        }

        protected void onCancelled() {
            SLog.m12v(LCoverFOTAUpdate.TAG, "[onCancelled]");
        }

        private void runAuthFota() throws IOException {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(LCoverFOTAUpdate.this.authPath)));
            LCoverFOTAUpdate.this.flashByte = new byte[Constants.AUTH_ONELINE_DATA_SIZE];
            LCoverFOTAUpdate.this.flashDataLength = 0;
            LCoverFOTAUpdate.this.passMakeCmdCount = 0;
            while (true) {
                String oneLine = bufferReader.readLine();
                if (oneLine != null) {
                    CoverHexParser ih = new CoverHexParser(oneLine);
                    String addressStr = ih.addressString();
                    String lengthStr = Integer.toHexString(Constants.AUTH_ONELINE_DATA_SIZE);
                    if (ih.address() != 0) {
                        if (LCoverFOTAUpdate.this.passMakeCmdCount == 0) {
                            LCoverFOTAUpdate.this.flashAddress = addressStr;
                        }
                        LCoverFOTAUpdate.this.flashDataLength = LCoverFOTAUpdate.this.flashDataLength + LCoverFOTAUpdate.this.makeOneLineCommand(ih.data());
                        LCoverFOTAUpdate.this.passMakeCmdCount = LCoverFOTAUpdate.this.passMakeCmdCount + LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT;
                        if (LCoverFOTAUpdate.this.passMakeCmdCount == 15) {
                            byte[] temp = Util.authMakeFlashWriteCommand(LCoverFOTAUpdate.this.flashAddress, lengthStr, LCoverFOTAUpdate.this.flashByte);
                            LCoverFOTAUpdate.this.passMakeCmdCount = 0;
                            LCoverFOTAUpdate.this.flashDataLength = 0;
                            LCoverFOTAUpdate.this.flashByte = new byte[Constants.AUTH_ONELINE_DATA_SIZE];
                            LCoverFOTAUpdate.this.res = LCoverFOTAUpdate.this.mNfcAdapter.semTransceiveDataWithLedCover(temp);
                            if (Util.checkErrorCode(LCoverFOTAUpdate.this.res)) {
                                SLog.m12v(LCoverFOTAUpdate.TAG, "[AUTH FOTA] Error Code : " + Util.getByteDataString(LCoverFOTAUpdate.this.res));
                                LCoverFOTAUpdate.this.updateFail();
                            }
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash Address : " + LCoverFOTAUpdate.this.flashAddress);
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash oneLine : " + Util.getByteDataString(temp));
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash Res : " + Util.getByteDataString(LCoverFOTAUpdate.this.res));
                        }
                    }
                    this.size = (this.size + oneLine.getBytes().length) + "\r\n".getBytes().length;
                    LCoverFOTAUpdate.this.value = (int) ((((double) this.size) / ((double) LCoverFOTAUpdate.this.allSize)) * 100.0d);
                    Integer[] numArr = new Integer[LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT];
                    numArr[0] = Integer.valueOf(LCoverFOTAUpdate.this.value);
                    publishProgress(numArr);
                } else {
                    bufferReader.close();
                    return;
                }
            }
        }

        private void runMcuFota() throws IOException {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(LCoverFOTAUpdate.this.mcuPath)));
            LCoverFOTAUpdate.this.flashByte = new byte[64];
            LCoverFOTAUpdate.this.flashDataLength = 0;
            LCoverFOTAUpdate.this.passMakeCmdCount = 0;
            while (true) {
                String oneLine = bufferReader.readLine();
                if (oneLine != null) {
                    CoverHexParser ih = new CoverHexParser(oneLine);
                    String addressStr = ih.addressString();
                    String lengthStr = ih.length();
                    int addressValue = ih.address();
                    if (addressStr.equals(Constants.MCU_ONELINE_PASSCODE)) {
                        LCoverFOTAUpdate.this.passKey = true;
                    }
                    if (LCoverFOTAUpdate.this.passKey && addressValue != 0) {
                        if (LCoverFOTAUpdate.this.passMakeCmdCount == 0) {
                            LCoverFOTAUpdate.this.flashAddress = addressStr;
                        }
                        LCoverFOTAUpdate.this.flashDataLength = LCoverFOTAUpdate.this.flashDataLength + LCoverFOTAUpdate.this.makeOneLineCommand(ih.data());
                        LCoverFOTAUpdate.this.passMakeCmdCount = LCoverFOTAUpdate.this.passMakeCmdCount + LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT;
                        if (LCoverFOTAUpdate.this.passMakeCmdCount == LCoverFOTAUpdate.MSG_DOWNLOAD_UPDATE) {
                            byte[] temp = Util.mcuMakeFlashWriteCommand(LCoverFOTAUpdate.this.flashAddress, LCoverFOTAUpdate.this.flashByte);
                            LCoverFOTAUpdate.this.passMakeCmdCount = 0;
                            LCoverFOTAUpdate.this.flashDataLength = 0;
                            LCoverFOTAUpdate.this.flashByte = new byte[64];
                            LCoverFOTAUpdate.this.res = LCoverFOTAUpdate.this.mNfcAdapter.semTransceiveDataWithLedCover(temp);
                            if (Util.checkErrorCode(LCoverFOTAUpdate.this.res)) {
                                SLog.m12v(LCoverFOTAUpdate.TAG, "[MCU FOTA] Error Code : " + Util.getByteDataString(LCoverFOTAUpdate.this.res));
                                LCoverFOTAUpdate.this.updateFail();
                            }
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash Address : " + LCoverFOTAUpdate.this.flashAddress);
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash oneLine : " + Util.getByteDataString(temp));
                            SLog.m12v(LCoverFOTAUpdate.TAG, "Write Flash Res : " + Util.getByteDataString(LCoverFOTAUpdate.this.res));
                        }
                    }
                    this.size = (this.size + oneLine.getBytes().length) + "\r\n".getBytes().length;
                    LCoverFOTAUpdate.this.value = (int) ((((double) this.size) / ((double) LCoverFOTAUpdate.this.allSize)) * 100.0d);
                    Integer[] numArr = new Integer[LCoverFOTAUpdate.CLIENT_MSG_UPDATING_FIRMWARE_PERCENT];
                    numArr[0] = Integer.valueOf(LCoverFOTAUpdate.this.value);
                    publishProgress(numArr);
                } else {
                    bufferReader.close();
                    return;
                }
            }
        }
    }

    private final class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LCoverFOTAUpdate.MSG_CHECK_UPDATE /*3*/:
                    SLog.m12v(LCoverFOTAUpdate.TAG, "ServiceHandler: handleMessage: MSG_CHECK_UPDATE");
                    LCoverFOTAUpdate.this.mFOTAManager.getFirmwareInfo(LCoverFOTAUpdate.PROD_ID, LCoverFOTAUpdate.LOCATION);
                    break;
                case LCoverFOTAUpdate.MSG_DOWNLOAD_UPDATE /*4*/:
                    SLog.m12v(LCoverFOTAUpdate.TAG, "ServiceHandler: handleMessage: MSG_CHECK_UPDATE");
                    LCoverFOTAUpdate.mUpdateProgress = -1;
                    LCoverFOTAUpdate.this.mFOTAManager.startDownload(LCoverFOTAUpdate.PROD_ID, LCoverFOTAUpdate.LOCATION);
                    break;
                case LCoverFOTAUpdate.MSG_SHOW_CHECK_UPDATE_NOTIFICATION /*5*/:
                    SLog.m12v(LCoverFOTAUpdate.TAG, "ServiceHandler: handleMessage: MSG_SHOW_CHECK_UPDATE_NOTIFICATION");
                    LCoverFOTAUpdate.this.mNotificationManager.notify(LCoverFOTAUpdate.NOTIFICATION_UPDATE_FIRMWARE_ID, LCoverFOTAUpdate.this.buildUpdateFirmwareNotification());
                    break;
                case LCoverFOTAUpdate.MSG_UPDATING_FIRMWARE_PERCENT /*6*/:
                    LCoverFOTAUpdate.this.sendPercentValueToDialog(LCoverFOTAUpdate.mUpdateProgress);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
            LCoverFOTAUpdate.this.stopSelf(msg.arg1);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.service.LCoverFOTAUpdate.1 */
    class C04181 implements OnDownloadListener {
        C04181() {
        }

        public void onGetFirmwareInfo(String name, String version) {
            SLog.m12v(LCoverFOTAUpdate.TAG, "OnDownloadListener: onGetFirmwareInfo");
            LCoverFOTAUpdate.this.mNotificationManager.notify(LCoverFOTAUpdate.NOTIFICATION_UPDATE_FIRMWARE_ID, LCoverFOTAUpdate.this.buildUpdateFirmwareNotification());
        }

        public void onStarted() {
            SLog.m12v(LCoverFOTAUpdate.TAG, "OnDownloadListener: onStarted: ");
            LCoverFOTAUpdate.this.buildUpdatingFirmwareNotification();
            ((NotificationManager) LCoverFOTAUpdate.this.getSystemService("notification")).notify(LCoverFOTAUpdate.NOTIFICATION_UPDATE_FIRMWARE_ID, LCoverFOTAUpdate.this.mUpdatingFirmwareNotificationBuilder.build());
        }

        public void onProgress(int progress) {
            LCoverFOTAUpdate.mUpdateProgress = progress / LCoverFOTAUpdate.CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED;
            LCoverFOTAUpdate.this.updateProgress();
        }

        public void onCompleted(String path, int size, ArrayList<String> unzipFiles) {
            SLog.m12v(LCoverFOTAUpdate.TAG, "OnDownloadListener: onCompleted with path =" + path);
            LCoverFOTAUpdate.this.startFota(unzipFiles);
        }

        public void onFailed(int error) {
            SLog.m12v(LCoverFOTAUpdate.TAG, "OnDownloadListener: onFailed");
            Toast.makeText(LCoverFOTAUpdate.this.getApplicationContext(), "Couldn't copy software update to LED View Cover.", 0).show();
            LCoverFOTAUpdate.this.updateFail();
        }
    }

    public LCoverFOTAUpdate() {
        this.passMakeCmdCount = 0;
        this.flashDataLength = 0;
        this.flashByte = new byte[Constants.AUTH_ONELINE_DATA_SIZE];
        this.listener = new C04181();
    }

    static {
        mUpdateProgress = -1;
    }

    private void updateProgress() {
        this.mUpdatingFirmwareNotificationBuilder.setContentText(mUpdateProgress + "%");
        this.mUpdatingFirmwareNotificationBuilder.setProgress(100, mUpdateProgress, false);
        ((NotificationManager) getSystemService("notification")).notify(NOTIFICATION_UPDATE_FIRMWARE_ID, this.mUpdatingFirmwareNotificationBuilder.build());
        SLog.m12v(TAG, "Update progress dialog : process = " + mUpdateProgress);
        sendPercentValueToDialog(mUpdateProgress);
    }

    private void updateFail() {
        ((NotificationManager) getSystemService("notification")).notify(NOTIFICATION_UPDATE_FIRMWARE_ID, buildUpdateFirmwareFailNotification());
        mUpdateProgress = -1;
        sendMessageToClients(MSG_CHECK_UPDATE);
    }

    private void updateSuccess() {
        mUpdateProgress = -1;
        this.mNotificationManager.cancel(NOTIFICATION_UPDATE_FIRMWARE_ID);
        sendMessageToClients(CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED);
        Toast.makeText(getApplicationContext(), "LED view cover software updated.", 0).show();
    }

    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }

    public void onCreate() {
        super.onCreate();
        SLog.m12v(TAG, "onCreate");
        this.mFOTAManager = new FOTAManager(this, this.listener);
        this.mNotificationManager = (NotificationManager) getSystemService("notification");
        this.thread = new HandlerThread("LCoverFOTAUpdateServiceThread", NOTIFICATION_UPDATE_FIRMWARE_ID);
        this.thread.start();
        this.mServiceLooper = this.thread.getLooper();
        this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
        this.mMessenger = new Messenger(this.mServiceHandler);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        SLog.m12v(TAG, "onStartCommand");
        Message msg = this.mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.what = intent.getIntExtra(MSG_TYPE, -1);
        this.mServiceHandler.sendMessage(msg);
        return CLIENT_MSG_UPDATING_FIRMWARE_PERCENT;
    }

    public void onDestroy() {
        SLog.m12v(TAG, "onDestroy");
        if (this.mServiceLooper != null) {
            this.mServiceLooper.quit();
        }
        super.onDestroy();
    }

    private void sendPercentValueToDialog(int percent) {
        SLog.m12v(TAG, "sendPercentValueToDialog: " + percent);
        Intent intent = new Intent(CLIENT_MSG_BROADCAST_INTENT_ACTION);
        intent.putExtra(CLIENT_MSG_TYPE, CLIENT_MSG_UPDATING_FIRMWARE_PERCENT);
        intent.putExtra(CLIENT_MSG_DATA, percent);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendMessageToClients(int messageType) {
        SLog.m12v(TAG, "sendMessageToClients: " + messageType);
        Intent intent = new Intent(CLIENT_MSG_BROADCAST_INTENT_ACTION);
        intent.putExtra(CLIENT_MSG_TYPE, messageType);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public Notification buildUpdateFirmwareNotification() {
        Builder builder = new Builder(getApplicationContext());
        builder.setSmallIcon(C0198R.drawable.ic_launcher_settings);
        Intent resultIntent = new Intent(getApplicationContext(), LCoverUpdateFirmwareActivity.class);
        resultIntent.addFlags(603979776);
        resultIntent.putExtra(LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE, CLIENT_MSG_UPDATING_FIRMWARE_PERCENT);
        builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 134217728));
        builder.setOngoing(false);
        builder.setWhen(0);
        builder.setAutoCancel(true);
        builder.setCategory(NotificationCompatApi24.CATEGORY_EVENT);
        builder.setPriority(CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED);
        builder.setContentTitle(getResources().getString(C0198R.string.dream_update_led_cover_software_q_pheader));
        builder.setContentText(getResources().getString(C0198R.string.f4x3c99569d));
        return builder.build();
    }

    public void buildUpdatingFirmwareNotification() {
        Builder builder = new Builder(getApplicationContext());
        builder.setSmallIcon(C0198R.drawable.ic_launcher_settings);
        Intent resultIntent = new Intent(getApplicationContext(), LCoverUpdateFirmwareActivity.class);
        resultIntent.addFlags(603979776);
        resultIntent.putExtra(LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE, CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED);
        builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 134217728));
        builder.setOngoing(true);
        builder.setWhen(0);
        builder.setAutoCancel(false);
        builder.setCategory(NotificationCompatApi24.CATEGORY_EVENT);
        builder.setPriority(CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED);
        Resources resources = getResources();
        Object[] objArr = new Object[CLIENT_MSG_UPDATING_FIRMWARE_PERCENT];
        objArr[0] = getResources().getString(C0198R.string.ss_led_view_cover_header);
        builder.setContentTitle(resources.getString(C0198R.string.dream_copying_software_update_to_ps_ing_sbody_abb, objArr));
        builder.setContentText("0%");
        builder.setProgress(0, 0, true);
        this.mUpdatingFirmwareNotificationBuilder = builder;
    }

    public Notification buildUpdateFirmwareFailNotification() {
        Builder builder = new Builder(getApplicationContext());
        builder.setSmallIcon(C0198R.drawable.ic_launcher_settings);
        Intent resultIntent = new Intent(getApplicationContext(), LCoverUpdateFirmwareActivity.class);
        resultIntent.addFlags(603979776);
        resultIntent.putExtra(LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE, MSG_CHECK_UPDATE);
        builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 134217728));
        builder.setOngoing(false);
        builder.setWhen(0);
        builder.setAutoCancel(true);
        builder.setCategory(NotificationCompatApi24.CATEGORY_EVENT);
        builder.setPriority(CLIENT_MSG_UPDATE_FIRMWARE_COMPLETED);
        builder.setContentTitle(getResources().getString(C0198R.string.dream_update_led_cover_software_q_pheader));
        Resources resources = getResources();
        Object[] objArr = new Object[CLIENT_MSG_UPDATING_FIRMWARE_PERCENT];
        objArr[0] = getResources().getString(C0198R.string.ss_led_view_cover_header);
        builder.setContentText(resources.getString(C0198R.string.f3x776b26d5, objArr));
        return builder.build();
    }

    public void startFota(ArrayList<String> paths) {
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Iterator it = paths.iterator();
        while (it.hasNext()) {
            String path = (String) it.next();
            if (path.contains("auth")) {
                this.authPath = path;
                this.allSize += new File(path).length();
            } else if (path.contains("mcu")) {
                this.mcuPath = path;
                this.allSize += new File(path).length();
            }
            if (this.authPath != null && this.mcuPath != null) {
                break;
            }
        }
        if (this.authPath == null || this.mcuPath == null) {
            SLog.m12v(TAG, "Please, Check for binary name\n Auth name: auth_xxx \n MCU name: mcu_xxx in 16842794");
            updateFail();
            return;
        }
        this.mTaskProgress = new FotaTask();
        this.mTaskProgress.execute(new Integer[0]);
    }

    public int makeOneLineCommand(byte[] payload) {
        int length = payload.length;
        System.arraycopy(payload, 0, this.flashByte, this.flashDataLength, payload.length);
        return length;
    }
}
