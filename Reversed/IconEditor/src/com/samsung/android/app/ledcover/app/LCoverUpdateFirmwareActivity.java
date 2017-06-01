package com.samsung.android.app.ledcover.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.NetworkCheckUtils;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.service.LCoverFOTAUpdate;

public class LCoverUpdateFirmwareActivity extends Activity {
    public static final String NOTIFICATION_INTENT_TYPE = "notification_intent_type";
    public static final int NOTIFICATION_INTENT_TYPE_SHOW_RESET_UPDATE_FIRMWARE = 3;
    public static final int NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM = 1;
    public static final int NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE = 2;
    public static final String TAG = "[LED_COVER]LCoverUpdateFirmwareActivity";
    private Dialog mConfirmUpdateFirmwareDialog;
    private ServiceConnection mConnection;
    boolean mIsBound;
    private BroadcastReceiver mMessageReceiver;
    Messenger mService;
    int mShowingDialogType;
    private Dialog mUpdatingFirmwareDialog;
    private ProgressBar mUpdatingFirmwareProgressBar;
    private TextView mUpdatingFirmwareProgressPercent;

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.1 */
    class C02241 implements ServiceConnection {
        C02241() {
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            SLog.m12v(LCoverUpdateFirmwareActivity.TAG, "onServiceConnected");
            LCoverUpdateFirmwareActivity.this.mService = new Messenger(service);
            if (LCoverUpdateFirmwareActivity.this.mShowingDialogType == LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE) {
                try {
                    LCoverUpdateFirmwareActivity.this.mService.send(Message.obtain(null, 6));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            SLog.m12v(LCoverUpdateFirmwareActivity.TAG, "onServiceDisconnected");
            LCoverUpdateFirmwareActivity.this.mService = null;
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.2 */
    class C02252 extends BroadcastReceiver {
        C02252() {
        }

        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(LCoverFOTAUpdate.CLIENT_MSG_TYPE, -1)) {
                case LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM /*1*/:
                    int percent = intent.getIntExtra(LCoverFOTAUpdate.CLIENT_MSG_DATA, -1);
                    if (percent < 0) {
                        if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar != null) {
                            LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar.setIndeterminate(true);
                        }
                        if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressPercent != null) {
                            LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressPercent.setText("0%");
                            return;
                        }
                        return;
                    }
                    if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressPercent != null) {
                        LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressPercent.setText(percent + "%");
                    }
                    if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar != null) {
                        if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar.isIndeterminate()) {
                            LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar.setIndeterminate(false);
                        }
                        LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareProgressBar.setProgress(percent);
                    }
                case LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE /*2*/:
                case LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_RESET_UPDATE_FIRMWARE /*3*/:
                    if (LCoverUpdateFirmwareActivity.this.mShowingDialogType == LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE) {
                        if (LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareDialog != null && LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareDialog.isShowing()) {
                            LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareDialog.cancel();
                        }
                    } else if (LCoverUpdateFirmwareActivity.this.mShowingDialogType != LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM) {
                        LCoverUpdateFirmwareActivity.this.finish();
                    } else if (LCoverUpdateFirmwareActivity.this.mConfirmUpdateFirmwareDialog != null && LCoverUpdateFirmwareActivity.this.mConfirmUpdateFirmwareDialog.isShowing()) {
                        LCoverUpdateFirmwareActivity.this.mConfirmUpdateFirmwareDialog.cancel();
                    }
                default:
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.3 */
    class C02263 implements OnClickListener {
        C02263() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.sendEventSALog(Defines.SA_SCREEN_UPDATE_FIRMWARE, Defines.SA_UPDATE_FIRMWARE_EVENT_OK, "OK");
            if (NetworkCheckUtils.checkNetwork(LCoverUpdateFirmwareActivity.this.getApplicationContext())) {
                Intent intent = new Intent(LCoverUpdateFirmwareActivity.this.getApplicationContext(), LCoverFOTAUpdate.class);
                intent.putExtra(LCoverFOTAUpdate.MSG_TYPE, 4);
                LCoverUpdateFirmwareActivity.this.startService(intent);
                LCoverUpdateFirmwareActivity.this.showUpdatingFirmwareDialog();
                LCoverUpdateFirmwareActivity.this.mShowingDialogType = LCoverUpdateFirmwareActivity.NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE;
                return;
            }
            try {
                LCoverUpdateFirmwareActivity.this.mService.send(Message.obtain(null, 5));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SLog.m12v(LCoverUpdateFirmwareActivity.TAG, "Please check network state");
            LCoverUpdateFirmwareActivity.this.showNoNetworkDialog();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.4 */
    class C02274 implements OnClickListener {
        C02274() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            SLog.m12v(LCoverUpdateFirmwareActivity.TAG, "showUpdateFirmwareDialog setNegativeButton");
            LCoverUpdateFirmwareActivity.this.mConfirmUpdateFirmwareDialog.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.5 */
    class C02285 implements OnCancelListener {
        C02285() {
        }

        public void onCancel(DialogInterface dialog) {
            Utils.sendEventSALog(Defines.SA_SCREEN_UPDATE_FIRMWARE, Defines.SA_UPDATE_FIRMWARE_EVENT_LATER, "Later");
            SLog.m12v(LCoverUpdateFirmwareActivity.TAG, "showUpdateFirmwareDialog setOnCancelListener");
            LCoverUpdateFirmwareActivity.this.finish();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.6 */
    class C02296 implements OnClickListener {
        C02296() {
        }

        public void onClick(DialogInterface dialog, int which) {
            LCoverUpdateFirmwareActivity.this.mUpdatingFirmwareDialog.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.7 */
    class C02307 implements OnCancelListener {
        C02307() {
        }

        public void onCancel(DialogInterface dialog) {
            Utils.sendEventSALog(Defines.SA_SCREEN_UPDATING_FIRMWARE, Defines.SA_UPDATE_FIRMWARE_EVENT_HIDE, "Hide");
            LCoverUpdateFirmwareActivity.this.finish();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.8 */
    class C02318 implements OnClickListener {
        C02318() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverUpdateFirmwareActivity.9 */
    class C02329 implements OnDismissListener {
        C02329() {
        }

        public void onDismiss(DialogInterface dialog) {
            LCoverUpdateFirmwareActivity.this.finish();
        }
    }

    public LCoverUpdateFirmwareActivity() {
        this.mConfirmUpdateFirmwareDialog = null;
        this.mUpdatingFirmwareDialog = null;
        this.mService = null;
        this.mShowingDialogType = -1;
        this.mConnection = new C02241();
        this.mMessageReceiver = new C02252();
    }

    protected void onCreate(Bundle savedInstanceState) {
        SLog.m12v(TAG, "LCoverUpdateFirmwareActivity: onCreate");
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mMessageReceiver, new IntentFilter(LCoverFOTAUpdate.CLIENT_MSG_BROADCAST_INTENT_ACTION));
        doBindService();
        Intent intent = getIntent();
        if (intent != null) {
            int type = intent.getIntExtra(NOTIFICATION_INTENT_TYPE, -1);
            if (type == NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM) {
                Utils.sendEventSALog(Defines.SA_SCREEN_UPDATE_FIRMWARE, Defines.SA_UPDATE_FIRMWARE_EVENT_TAP_CONFIRM_UPDATE_NOTI, "Update LED cover software");
                showUpdateFirmwareDialog();
            } else if (type == NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE) {
                Utils.sendEventSALog(Defines.SA_SCREEN_UPDATING_FIRMWARE, Defines.SA_UPDATE_FIRMWARE_EVENT_TAP_UPDATING_NOTI, "Update LED cover software");
                showUpdatingFirmwareDialog();
            } else if (type == NOTIFICATION_INTENT_TYPE_SHOW_RESET_UPDATE_FIRMWARE) {
                Intent intentService = new Intent(getApplicationContext(), LCoverFOTAUpdate.class);
                intentService.putExtra(LCoverFOTAUpdate.MSG_TYPE, 4);
                startService(intentService);
                this.mUpdatingFirmwareDialog = null;
                showUpdatingFirmwareDialog();
                this.mShowingDialogType = NOTIFICATION_INTENT_TYPE_SHOW_UPDATING_FIRMWARE;
            }
            this.mShowingDialogType = type;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        SLog.m12v(TAG, "LCoverUpdateFirmwareActivity: onDestroy");
        super.onDestroy();
        try {
            doUnbindService();
        } catch (Throwable th) {
            SLog.m12v(TAG, "Failed to unbind from the service");
        }
        if (this.mConfirmUpdateFirmwareDialog != null && this.mConfirmUpdateFirmwareDialog.isShowing()) {
            this.mConfirmUpdateFirmwareDialog.dismiss();
        }
        if (this.mUpdatingFirmwareDialog != null && this.mUpdatingFirmwareDialog.isShowing()) {
            this.mUpdatingFirmwareDialog.dismiss();
        }
    }

    void doBindService() {
        bindService(new Intent(this, LCoverFOTAUpdate.class), this.mConnection, NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM);
        this.mIsBound = true;
    }

    void doUnbindService() {
        if (this.mIsBound) {
            unbindService(this.mConnection);
            this.mIsBound = false;
        }
    }

    public void showUpdateFirmwareDialog() {
        if (!isFinishing()) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_UPDATE_FIRMWARE);
            if (this.mConfirmUpdateFirmwareDialog == null) {
                Builder builder = new Builder(this);
                builder.setTitle(C0198R.string.dream_update_led_cover_software_q_pheader);
                builder.setPositiveButton(C0198R.string.common_ok, new C02263());
                builder.setNegativeButton(C0198R.string.dream_later_button9, new C02274());
                builder.setOnCancelListener(new C02285());
                builder.setView(C0198R.layout.update_firmware_dialog_confirm);
                builder.setCancelable(true);
                this.mConfirmUpdateFirmwareDialog = builder.create();
            }
            if (this.mConfirmUpdateFirmwareDialog != null && !this.mConfirmUpdateFirmwareDialog.isShowing()) {
                this.mConfirmUpdateFirmwareDialog.show();
            }
        }
    }

    public void showUpdatingFirmwareDialog() {
        if (!isFinishing()) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_UPDATING_FIRMWARE);
            if (this.mUpdatingFirmwareDialog == null) {
                Builder builder = new Builder(this);
                builder.setTitle(C0198R.string.updating_led_cover_software);
                builder.setNegativeButton(C0198R.string.ids_com_sk_hide, new C02296());
                builder.setOnCancelListener(new C02307());
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(C0198R.layout.update_firmware_dialog_download, null, false);
                TextView textView = (TextView) dialogView.findViewById(C0198R.id.dialog_content_text);
                Resources resources = getResources();
                Object[] objArr = new Object[NOTIFICATION_INTENT_TYPE_SHOW_UPDATE_FIRMWARE_CONFIRM];
                objArr[0] = getResources().getString(C0198R.string.ss_led_view_cover_header);
                textView.setText(resources.getString(C0198R.string.dream_copying_software_update_to_ps_ing_sbody_abb, objArr));
                this.mUpdatingFirmwareProgressBar = (ProgressBar) dialogView.findViewById(C0198R.id.dialog_progress);
                this.mUpdatingFirmwareProgressPercent = (TextView) dialogView.findViewById(C0198R.id.dialog_progress_percent);
                builder.setView(dialogView);
                builder.setCancelable(true);
                this.mUpdatingFirmwareDialog = builder.create();
            }
            if (this.mUpdatingFirmwareDialog != null && !this.mUpdatingFirmwareDialog.isShowing()) {
                this.mUpdatingFirmwareDialog.show();
                if (this.mService != null) {
                    try {
                        this.mService.send(Message.obtain(null, 6));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void showNoNetworkDialog() {
        if (!isFinishing()) {
            Builder builder = new Builder(this);
            builder.setTitle(C0198R.string.no_network_connection);
            builder.setPositiveButton(C0198R.string.common_ok, new C02318());
            builder.setOnDismissListener(new C02329());
            builder.setMessage(C0198R.string.wifi_not_available_msg);
            builder.setCancelable(true);
            builder.create().show();
        }
    }
}
