package com.samsung.android.app.ledcover.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.res.TypedArray;
import android.nfc.NfcAdapter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.adapter.LCoverRootDescriptionViewPagerAdapter;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.SharedPreferencesUtil;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.service.LCoverFOTAUpdate;
import com.samsung.android.app.ledcover.update.StubData;
import com.samsung.android.app.ledcover.update.StubListener;
import com.samsung.android.app.ledcover.update.StubUtil;
import com.samsung.android.app.ledcover.wrapperlibrary.C0270R;
import java.util.ArrayList;

public class LCoverRootActivity extends Activity implements StubListener {
    private static final int REQUEST_NFC_OFF = 5;
    public static final String TAG = "[LED_COVER]LCoverRootActivity";
    public static Handler apkHandler;
    private AlertDialog alert;
    private AlertDialog alertNfc;
    OnClickListener callMenuClick;
    private LinearLayout ll_call_menu;
    private LinearLayout ll_noti_menu;
    private LinearLayout ll_shortcut_menu;
    private Context mAppContext;
    private Context mContext;
    private boolean mCustomPopUp_contact;
    private boolean mCustomPopUp_phone;
    private CustomViewPager mDescPager;
    private long mHiddenMenuLastTimeClicked;
    private int mHiddenMenuNumClicked;
    ArrayList<ImageView> mIndicatorList;
    private LCoverDbAccessor mLedCoverDbAccessor;
    public OnPageChangeListener mOnPageChangeListener;
    private int mPageBefore;
    private LCoverRootDescriptionViewPagerAdapter mPagerAdapter;
    private LinearLayout mPagerIndicatorView;
    private int mTypeDialogPermission;
    OnClickListener notiMenuClick;
    OnClickListener shortcutMenuClick;
    private Switch sw_shortcut;

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.1 */
    class C02161 implements OnClickListener {
        C02161() {
        }

        public void onClick(View v) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - LCoverRootActivity.this.mHiddenMenuLastTimeClicked < 500) {
                LCoverRootActivity.this.mHiddenMenuNumClicked = LCoverRootActivity.this.mHiddenMenuNumClicked + 1;
            } else {
                LCoverRootActivity.this.mHiddenMenuNumClicked = 0;
            }
            LCoverRootActivity.this.mHiddenMenuLastTimeClicked = currentTime;
            if (LCoverRootActivity.this.mHiddenMenuNumClicked > 6 && LCoverRootActivity.this.mHiddenMenuNumClicked < 9) {
                Toast.makeText(LCoverRootActivity.this.mAppContext, (9 - LCoverRootActivity.this.mHiddenMenuNumClicked) + " remain!!!", 0).show();
            }
            if (LCoverRootActivity.this.mHiddenMenuNumClicked == 9) {
                LCoverRootActivity.this.mHiddenMenuNumClicked = 0;
                int mNfcGetState = NfcAdapter.getDefaultAdapter(LCoverRootActivity.this.getApplicationContext()).semGetAdapterState();
                if (mNfcGetState == 3 || mNfcGetState == LCoverRootActivity.REQUEST_NFC_OFF) {
                    LCoverRootActivity.this.nfcDialog();
                    return;
                }
                Toast.makeText(LCoverRootActivity.this.mAppContext, "Checking for update...", 0).show();
                Intent intent = new Intent(LCoverRootActivity.this.getApplicationContext(), LCoverFOTAUpdate.class);
                intent.putExtra(LCoverFOTAUpdate.MSG_TYPE, 3);
                LCoverRootActivity.this.startService(intent);
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.2 */
    class C02172 implements DialogInterface.OnClickListener {
        C02172() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.3 */
    class C02183 implements DialogInterface.OnClickListener {
        C02183() {
        }

        public void onClick(DialogInterface dialog, int id) {
            LCoverRootActivity.this.startActivityForResult(new Intent("android.settings.NFC_SETTINGS"), LCoverRootActivity.REQUEST_NFC_OFF);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.5 */
    class C02195 implements OnClickListener {
        C02195() {
        }

        public void onClick(View v) {
            Utils.sendEventSALog(Defines.SA_SCREEN_ROOT_ACTIVITY, Defines.SA_ROOT_ACTIVITY_EVENT_LED_CALLER_ICONS, "LED caller icons");
            if (ContextCompat.checkSelfPermission(LCoverRootActivity.this.mAppContext, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
                LCoverRootActivity.this.startActivity(new Intent(LCoverRootActivity.this.getApplicationContext(), LCoverCallMainActivity.class));
                return;
            }
            LCoverRootActivity.this.checkRuntimePermission(Defines.READ_PERMISSIONS_STRING_CONTACTS);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.6 */
    class C02206 implements OnClickListener {
        C02206() {
        }

        public void onClick(View v) {
            Utils.sendEventSALog(Defines.SA_SCREEN_ROOT_ACTIVITY, Defines.SA_ROOT_ACTIVITY_EVENT_LED_NOTIFICATION_ICONS, "LED notification icons");
            LCoverRootActivity.this.startActivity(new Intent(LCoverRootActivity.this.getApplicationContext(), LCoverNotiMainActivity.class));
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.7 */
    class C02217 implements OnClickListener {
        C02217() {
        }

        public void onClick(View v) {
            boolean setValue = !SharedPreferencesUtil.loadShortcutEnable(LCoverRootActivity.this);
            SLog.m12v(LCoverRootActivity.TAG, "shortcut switch : " + setValue);
            Utils.sendEventSALog(Defines.SA_SCREEN_ROOT_ACTIVITY, Defines.SA_ROOT_ACTIVITY_EVENT_LED_ICON_EDITOR_SHORTCUT, "Show LED icon editor shortcut", setValue ? 1 : 0);
            Intent bIntent = new Intent();
            bIntent.setAction(Defines.BROADCAST_ACTION_SHORTCUT_ENABLE_CHANGED);
            bIntent.putExtra("isChecked", setValue);
            LCoverRootActivity.this.sendBroadcast(bIntent, Defines.PERMISSION_LCOVER_LAUNCH);
            SharedPreferencesUtil.saveShortcutEnable(LCoverRootActivity.this, setValue);
            LCoverRootActivity.this.sw_shortcut.setChecked(setValue);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.8 */
    class C02228 implements OnClickListener {
        C02228() {
        }

        public void onClick(View v) {
            SLog.m12v(LCoverRootActivity.TAG, "btn_settings is clicked");
            Intent intent = new Intent(Defines.MANAGE_APP_PERMISSIONS);
            intent.putExtra(Defines.MANAGE_APP_PERMISSIONS_EXTRA, LCoverRootActivity.this.getPackageName());
            LCoverRootActivity.this.startActivityForResult(intent, LCoverRootActivity.this.mTypeDialogPermission);
            LCoverRootActivity.this.alert.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.9 */
    class C02239 implements OnClickListener {
        C02239() {
        }

        public void onClick(View v) {
            SLog.m12v(LCoverRootActivity.TAG, "btn_cancel is clicked");
            LCoverRootActivity.this.alert.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverRootActivity.4 */
    class C04164 implements OnPageChangeListener {
        C04164() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            if (LCoverRootActivity.this.mPageBefore >= 0) {
                LCoverRootActivity.this.setIndicator((ImageView) LCoverRootActivity.this.mPagerIndicatorView.getChildAt(LCoverRootActivity.this.mPageBefore), false);
            }
            if (Utils.isRtl()) {
                position = (LCoverRootActivity.this.mPagerAdapter.getCount() - 1) - position;
            }
            LCoverRootActivity.this.setIndicator((ImageView) LCoverRootActivity.this.mPagerIndicatorView.getChildAt(position), true);
            LCoverRootActivity.this.mPageBefore = position;
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public LCoverRootActivity() {
        this.alert = null;
        this.mCustomPopUp_phone = false;
        this.mCustomPopUp_contact = false;
        this.mTypeDialogPermission = 0;
        this.mPagerAdapter = null;
        this.mPagerIndicatorView = null;
        this.mPageBefore = -1;
        this.mHiddenMenuLastTimeClicked = 0;
        this.mHiddenMenuNumClicked = 0;
        this.mOnPageChangeListener = new C04164();
        this.callMenuClick = new C02195();
        this.notiMenuClick = new C02206();
        this.shortcutMenuClick = new C02217();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0198R.layout.activity_led_cover_root);
        this.mContext = this;
        this.mAppContext = getApplicationContext();
        this.ll_call_menu = (LinearLayout) findViewById(C0198R.id.ll_call_menu);
        this.ll_noti_menu = (LinearLayout) findViewById(C0198R.id.ll_noti_menu);
        this.ll_shortcut_menu = (LinearLayout) findViewById(C0198R.id.ll_shortcut_menu);
        this.sw_shortcut = (Switch) findViewById(C0198R.id.sw_shortcut);
        this.ll_call_menu.setOnClickListener(this.callMenuClick);
        this.ll_noti_menu.setOnClickListener(this.notiMenuClick);
        this.ll_shortcut_menu.setOnClickListener(this.shortcutMenuClick);
        this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
        if (SharedPreferencesUtil.loadManagerInitialInstalled(this)) {
            SLog.m12v(TAG, "Initial installed!");
            SharedPreferencesUtil.saveManagerInitialInstalled(this, false);
            SharedPreferencesUtil.saveShortcutEnable(this, true);
            addPresetIconToDb();
        }
        this.sw_shortcut.setChecked(SharedPreferencesUtil.loadShortcutEnable(this));
        if (Utils.isSamsungMobile()) {
            apkHandler = StubUtil.createHandler(this);
            StubUtil.init(this);
            checkUpdate();
        }
        View actionBar = getWindow().getDecorView().findViewById(getResources().getIdentifier("action_bar", "id", "android"));
        if (actionBar != null) {
            actionBar.setOnClickListener(new C02161());
        }
    }

    private void nfcDialog() {
        SLog.m12v(TAG, "nfcDialog()");
        if (this.alertNfc == null || !this.alertNfc.isShowing()) {
            Builder alt_bld = new Builder(this);
            alt_bld.setMessage("Please inactivated NFC and press Back key to return to the application!").setCancelable(false).setPositiveButton("Yes", new C02183()).setNegativeButton("No", new C02172());
            this.alertNfc = alt_bld.create();
            this.alertNfc.setTitle(getResources().getString(C0270R.string.app_name));
            this.alertNfc.show();
        }
    }

    protected void onStart() {
        super.onStart();
        createDescriptionViewPager();
    }

    protected void onStop() {
        super.onStop();
        if (this.mDescPager != null) {
            this.mDescPager.clearOnPageChangeListeners();
            SLog.m12v(TAG, "Clear mDescPager");
            Utils.recursiveRecycle(this.mDescPager);
        }
        SLog.m12v(TAG, "Clear mPagerIndicatorView");
        Utils.recursiveRecycle(this.mPagerIndicatorView);
    }

    protected void onDestroy() {
        System.runFinalization();
        System.gc();
        super.onDestroy();
    }

    private void createDescriptionViewPager() {
        this.mDescPager = (CustomViewPager) findViewById(C0198R.id.pager);
        this.mPagerIndicatorView = (LinearLayout) findViewById(C0198R.id.pager_indicator);
        this.mIndicatorList = new ArrayList();
        this.mPagerAdapter = new LCoverRootDescriptionViewPagerAdapter(this);
        this.mDescPager.setAdapter(this.mPagerAdapter);
        this.mDescPager.addOnPageChangeListener(this.mOnPageChangeListener);
        this.mDescPager.setOffscreenPageLimit(this.mPagerAdapter.getCount() - 1);
        this.mPagerIndicatorView.setClickable(true);
        for (int i = 0; i < this.mPagerAdapter.getCount(); i++) {
            ImageView indicator = new ImageView(this);
            setIndicator(indicator, false);
            int indicatorRadius = (int) getResources().getDimension(C0198R.dimen.led_cover_indicator_width_height);
            LayoutParams param = new LayoutParams(indicatorRadius, indicatorRadius);
            if (i != 0) {
                param.setMarginStart((int) getResources().getDimension(C0198R.dimen.led_cover_main_indicator_gap));
            }
            this.mPagerIndicatorView.addView(indicator, param);
        }
        if (Utils.isRtl()) {
            this.mDescPager.setCurrentItem(this.mPagerAdapter.getCount() - 1);
        } else {
            this.mDescPager.setCurrentItem(0);
            setIndicator((ImageView) this.mPagerIndicatorView.getChildAt(0), true);
            this.mPageBefore = 0;
        }
        setIndicator((ImageView) this.mPagerIndicatorView.getChildAt(0), true);
    }

    private void setIndicator(ImageView imageView, boolean enable) {
        if (imageView != null) {
            imageView.setImageDrawable(getResources().getDrawable(enable ? C0198R.drawable.view_pager_dot_indicator_selected : C0198R.drawable.view_pager_dot_indicator_default, null));
        }
    }

    protected void onResume() {
        super.onResume();
        Utils.sendScreenViewSALog(Defines.SA_SCREEN_ROOT_ACTIVITY);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Defines.READ_PERMISSIONS_STRING_CONTACTS)) {
            this.mCustomPopUp_contact = true;
        }
    }

    public void resizePager(int position) {
        View view = this.mDescPager.findViewWithTag(Integer.valueOf(position));
        if (view != null) {
            view.measure(-2, -2);
            this.mDescPager.setLayoutParams(new RelativeLayout.LayoutParams(view.getMeasuredWidth(), view.getMeasuredHeight()));
        }
    }

    public void addPresetIconToDb() {
        TypedArray name_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_string_entries);
        ArrayList<LCoverIconInfo> iconInfoArray = new ArrayList();
        TypedArray index_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_index_entries);
        for (int i = 0; i < 54; i++) {
            iconInfoArray.add(new LCoverIconInfo(index_array.getInt(i, 0), Defines.PRESET_ICON_ARRAY, name_array.getText(i).toString(), 0));
        }
        this.mLedCoverDbAccessor.addLedPresetIcons(iconInfoArray);
        name_array.recycle();
        index_array.recycle();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        SLog.m12v(TAG, "onRequestPermissionsResult requestCode : ");
        if (requestCode == 17) {
            if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
                this.mCustomPopUp_contact = false;
                startActivity(new Intent(getApplicationContext(), LCoverCallMainActivity.class));
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Defines.READ_PERMISSIONS_STRING_CONTACTS)) {
                this.mCustomPopUp_contact = true;
            }
        } else if (requestCode != 16) {
        } else {
            if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_PHONE_STATE) == 0) {
                this.mCustomPopUp_phone = false;
                startUpdateNow();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Defines.READ_PERMISSIONS_STRING_PHONE_STATE)) {
                this.mCustomPopUp_phone = true;
            }
        }
    }

    private void showPermissionSettingDialog(String type) {
        SLog.m12v(TAG, "showPermissionsSettingDialog");
        int icons = 0;
        String perName = null;
        String popName = null;
        PackageManager pm = getPackageManager();
        Builder builder = new Builder(this);
        View view = getLayoutInflater().inflate(C0198R.layout.dialog_permission_setting, null);
        TextView tv_body = (TextView) view.findViewById(C0198R.id.tv_body);
        ImageView iv_permission = (ImageView) view.findViewById(C0198R.id.iv_permission);
        TextView tv_permission = (TextView) view.findViewById(C0198R.id.tv_permission);
        TextView btn_settings = (TextView) view.findViewById(C0198R.id.tv_settings);
        TextView btn_cancel = (TextView) view.findViewById(C0198R.id.tv_cancel);
        builder.setView(view);
        this.alert = builder.create();
        this.alert.setCancelable(false);
        this.alert.setCanceledOnTouchOutside(false);
        if (type.equals(Defines.READ_PERMISSIONS_STRING_CONTACTS)) {
            this.mTypeDialogPermission = 17;
            popName = getResources().getString(C0198R.string.led_caller_icons);
        } else {
            if (type.equals(Defines.READ_PERMISSIONS_STRING_PHONE_STATE)) {
                this.mTypeDialogPermission = 16;
                popName = getResources().getString(C0198R.string.software_update);
            }
        }
        if (this.mTypeDialogPermission != 0) {
            try {
                PermissionGroupInfo groupInfo = pm.getPermissionGroupInfo(pm.getPermissionInfo(type, TransportMediator.FLAG_KEY_MEDIA_NEXT).group, TransportMediator.FLAG_KEY_MEDIA_NEXT);
                perName = getResources().getString(groupInfo.labelRes);
                icons = groupInfo.icon;
                SLog.m12v(TAG, "icons:" + icons + ", permissions:" + perName);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            String string;
            Object[] objArr;
            if (VERSION.SDK_INT < 24) {
                string = getString(C0198R.string.permission_desc);
                objArr = new Object[1];
                objArr[0] = "<b>" + popName + "</b>";
                tv_body.setText(Html.fromHtml(String.format(string, objArr)));
            } else {
                string = getString(C0198R.string.permission_desc);
                objArr = new Object[1];
                objArr[0] = "<b>" + popName + "</b>";
                tv_body.setText(Html.fromHtml(String.format(string, objArr), 0));
            }
            iv_permission.setImageResource(icons);
            tv_permission.setText(perName);
            btn_settings.setOnClickListener(new C02228());
        }
        btn_cancel.setOnClickListener(new C02239());
        this.alert.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SLog.m12v(TAG, "onActivityResult " + requestCode + " , " + resultCode + " , " + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 16) {
            if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_PHONE_STATE) == 0) {
                this.mCustomPopUp_phone = false;
                startUpdateNow();
            }
        } else if (requestCode == 17) {
            if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
                this.mCustomPopUp_contact = false;
                startActivity(new Intent(getApplicationContext(), LCoverCallMainActivity.class));
            }
        } else if (requestCode == REQUEST_NFC_OFF) {
            int mNfcGetState = NfcAdapter.getDefaultAdapter(getApplicationContext()).semGetAdapterState();
            if (mNfcGetState != 3 && mNfcGetState != REQUEST_NFC_OFF) {
                Toast.makeText(this.mAppContext, "Checking for update...", 0).show();
                Intent intent = new Intent(getApplicationContext(), LCoverFOTAUpdate.class);
                intent.putExtra(LCoverFOTAUpdate.MSG_TYPE, 3);
                startService(intent);
            }
        }
    }

    public void checkRuntimePermission(String type) {
        boolean popUpKey = false;
        int request_key = 0;
        SLog.m12v(TAG, "permission requestRuntimePermission type : " + type);
        if (type.equals(Defines.READ_PERMISSIONS_STRING_PHONE_STATE)) {
            popUpKey = this.mCustomPopUp_phone;
            request_key = 16;
        } else if (type.equals(Defines.READ_PERMISSIONS_STRING_CONTACTS)) {
            popUpKey = this.mCustomPopUp_contact;
            request_key = 17;
        }
        if (ContextCompat.checkSelfPermission(this, type) != 0) {
            if (popUpKey) {
                SLog.m12v(TAG, "[checkRuntimePermission] custom pop up");
                showPermissionSettingDialog(type);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, type)) {
                SLog.m12v(TAG, "[checkRuntimePermission] permission 2nd question pop up");
                ActivityCompat.requestPermissions(this, new String[]{type}, request_key);
            } else {
                SLog.m12v(TAG, "[checkRuntimePermission] permission 1st question pop up");
                ActivityCompat.requestPermissions(this, new String[]{type}, request_key);
            }
        } else if (type.equals(Defines.READ_PERMISSIONS_STRING_PHONE_STATE)) {
            startUpdateNow();
        }
    }

    private void startUpdateNow() {
        StubUtil.showPopupForConnectServer(this);
        if (StubUtil.isNetworkAvailable(this)) {
            apkStartDownload();
        } else {
            StubUtil.showPopupForNoNetwork(this);
        }
    }

    public void checkUpdate() {
        if (StubUtil.isNetworkAvailable(this)) {
            StubUtil.checkUpdate(this);
        } else {
            SLog.m12v(TAG, "<<Update>> isNetworkAvailable");
        }
    }

    public void onUpdateCheckFail(StubData data) {
        SLog.m12v(TAG, "<<Update>> onUpdateCheckFail");
    }

    public void onNoMatchingApplication(StubData data) {
        SLog.m12v(TAG, "<<Update>> onNoMatchingApplication");
    }

    public void onUpdateNotNecessary(StubData data) {
        SLog.m12v(TAG, "<<Update>> onUpdateNotNecessary");
    }

    public void onUpdateAvailable(StubData data) {
        SLog.m12v(TAG, "<<Update>> onUpdateAvailable");
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Defines.READ_PERMISSIONS_STRING_PHONE_STATE)) {
            this.mCustomPopUp_phone = true;
        }
        checkRuntimePermission(Defines.READ_PERMISSIONS_STRING_PHONE_STATE);
    }

    public void onGetDownloadUrlFail(StubData stubData) {
        SLog.m12v(TAG, "<<Update>> onGetDownloadUrlFail");
    }

    public void onGetDownloadUrlSuccess(StubData stubData) {
        SLog.m12v(TAG, "<<Update>> onGetDownloadUrlSuccess");
        Utils.sendScreenViewSALog(Defines.SA_SCREEN_UPDATE_SOFTWARE);
        StubUtil.showPopupForDownloading(this, stubData);
    }

    public void onDownloadApkFail() {
        SLog.m12v(TAG, "<<Update>> onDownloadApkFail");
        Toast.makeText(this, getResources().getText(C0198R.string.update_fail), 1).show();
    }

    public void onDownloadApkSuccess(String apkFilePath) {
        SLog.m12v(TAG, "<<Update>> onDownloadApkSuccess");
        StubUtil.onSilenceInstall(apkFilePath, this);
    }

    private void callGalaxyApps() {
        SLog.m12v(TAG, "<<Update>> callGalaxyApps");
        StubUtil.callGalaxyApps(this);
    }

    private void getDownloadUrl() {
        SLog.m12v(TAG, "<<Update>> getDownloadUrl");
        StubUtil.getDownloadUrl(getApplicationContext(), this);
    }

    public void apkStartDownload() {
        SLog.m12v(TAG, "<<Update>> apkStartDownload");
        getDownloadUrl();
    }
}
