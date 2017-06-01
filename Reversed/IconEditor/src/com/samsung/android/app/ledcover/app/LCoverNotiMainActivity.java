package com.samsung.android.app.ledcover.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.support.v4.media.TransportMediator;
import android.text.BidiFormatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.adapter.LCoverNotiIconListAdapter;
import com.samsung.android.app.ledcover.adapter.LCoverNotiIconListAdapter.OnAdapterClickedListener;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity;
import com.samsung.android.app.ledcover.wrapperlibrary.FloatingFeatureWrapper;
import com.samsung.android.app.ledcover.wrapperlibrary.ScrollViewWrapper;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p010a.C0305c;
import java.util.ArrayList;

public class LCoverNotiMainActivity extends Activity implements OnClickListener {
    private static final int MENU_EDIT = 1;
    private static final int MENU_REMOVE = 2;
    public static final String TAG = "[LED_COVER]LCoverNotiMainActivity";
    public static Handler apkHandler;
    private OnAdapterClickedListener callBack;
    private CheckBox cb_id_check;
    private int customIconCount;
    OnItemClickListener customListOnItemClick;
    OnItemLongClickListener customListOnItemLongClick;
    int customTopPositionX;
    int customTopPositionY;
    private boolean firstScreen;
    private CustomGridView gv_customList;
    private CustomGridView gv_presetList;
    private LinearLayout ll_gridview;
    private Dialog loadingProgress;
    private int mBeforeAct;
    private OnClickListener mCheckBoxAllClickListener;
    private Context mContext;
    private int mCountVersionInfoToast;
    private Boolean mEditMode;
    private ImageButton mFloatingActionButton;
    private Handler mHandler;
    private LCoverNotiIconListAdapter mLCoverNotiCustomIconListAdapter;
    private LCoverNotiIconListAdapter mLCoverNotiPresetIconListAdapter;
    private long mLastClickTime;
    private Configuration mLastConfiguration;
    private LCoverDbAccessor mLedCoverDbAccessor;
    private ArrayList<LCoverAppInfo> mLedNotiAppInfoList;
    private Menu mMenu;
    private ArrayList<LCoverIconInfo> mNotiCustomIDInfoList;
    private ArrayList<LCoverIconInfo> mNotiPresetIDInfoList;
    private ScrollView mScroll;
    int prePositionX;
    int prePositionY;
    OnItemClickListener presetListOnItemClick;
    private String printTemp;
    private TextView tv_divider;
    private TextView tv_id_count;

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.1 */
    class C02091 implements Runnable {
        C02091() {
        }

        public void run() {
            LCoverNotiMainActivity.this.mScroll.fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.2 */
    class C02102 implements Runnable {
        C02102() {
        }

        public void run() {
            LCoverNotiMainActivity.this.loadingProgress.dismiss();
            if (LCoverNotiMainActivity.this.printTemp != null) {
                Toast.makeText(LCoverNotiMainActivity.this.getApplicationContext(), LCoverNotiMainActivity.this.printTemp, LCoverNotiMainActivity.MENU_EDIT).show();
            }
            SLog.m12v(LCoverNotiMainActivity.TAG, "End Progress for loading ");
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.3 */
    class C02113 implements OnItemClickListener {
        C02113() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (SystemClock.elapsedRealtime() - LCoverNotiMainActivity.this.mLastClickTime >= 1000) {
                LCoverNotiMainActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                SLog.m12v(LCoverNotiMainActivity.TAG, "presetListOnItemClick " + parent.getId());
                SLog.m12v(LCoverNotiMainActivity.TAG, "mLCoverNotiPresetIconListAdapter : " + position + " mId : " + LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getId(position));
                SLog.m12v(LCoverNotiMainActivity.TAG, "mLCoverNotiPresetIconListAdapter : " + position + " getIconName : " + LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getIconName(position));
                Intent result;
                if (LCoverNotiMainActivity.this.mBeforeAct == 4) {
                    Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_CHANGE_MODE, Defines.SA_NOTI_MAIN_CHANGE_MODE_EVENT_CHANGE_LED_ICON, "Change LED icons", (long) (position + LCoverNotiMainActivity.MENU_EDIT));
                    result = new Intent();
                    result.putExtra("selected_id", LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getId(position));
                    result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getIconName(position));
                    LCoverNotiMainActivity.this.setResult(-1, result);
                    LCoverNotiMainActivity.this.finish();
                    return;
                }
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_ACTIVITY_EVENT_LED_ICONS, "LED icons", (long) (position + LCoverNotiMainActivity.MENU_EDIT));
                result = new Intent(LCoverNotiMainActivity.this.mContext, LCoverNotiAppListActivity.class);
                result.putExtra("selected_id", LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getId(position));
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverNotiMainActivity.this.mLCoverNotiPresetIconListAdapter.getIconName(position));
                result.putExtra("before_activity", LCoverNotiMainActivity.MENU_EDIT);
                LCoverNotiMainActivity.this.startActivity(result);
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.4 */
    class C02124 implements OnItemLongClickListener {
        C02124() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            boolean z;
            int i = 0;
            CheckBox checkbox = (CheckBox) view.findViewById(C0198R.id.cb_delete);
            LCoverNotiMainActivity.this.callBack.adapterLongClicked(position, checkbox.isChecked());
            if (checkbox.isChecked()) {
                z = false;
            } else {
                z = true;
            }
            checkbox.setChecked(z);
            LinearLayout layout = (LinearLayout) view.findViewById(C0198R.id.view_bg);
            if (!checkbox.isChecked()) {
                i = 8;
            }
            layout.setVisibility(i);
            return true;
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.5 */
    class C02135 implements OnItemClickListener {
        C02135() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            boolean z;
            int i = 0;
            CheckBox checkbox = (CheckBox) view.findViewById(C0198R.id.cb_delete);
            LCoverNotiMainActivity.this.callBack.adapterClicked(position, checkbox.isChecked());
            if (checkbox.isChecked()) {
                z = false;
            } else {
                z = true;
            }
            checkbox.setChecked(z);
            LinearLayout layout = (LinearLayout) view.findViewById(C0198R.id.view_bg);
            if (!checkbox.isChecked()) {
                i = 8;
            }
            layout.setVisibility(i);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.7 */
    class C02147 implements OnClickListener {
        C02147() {
        }

        public void onClick(View v) {
            if (LCoverNotiMainActivity.this.mBeforeAct == 4) {
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_CHANGE_MODE, Defines.SA_NOTI_MAIN_CHANGE_MODE_EVENT_UP_BUTTON, "Up button");
            } else {
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_ACTIVITY_EVENT_UP_BUTTON, "Up button");
            }
            LCoverNotiMainActivity.this.onBackPressed();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.8 */
    class C02158 implements OnClickListener {
        C02158() {
        }

        public void onClick(View v) {
            LCoverNotiMainActivity.this.cb_id_check.setChecked(!LCoverNotiMainActivity.this.cb_id_check.isChecked());
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_SELECTION_MODE, Defines.SA_NOTI_MAIN_SELECTION_MODE_EVENT_SELECT_ALL, "Select all", LCoverNotiMainActivity.this.cb_id_check.isChecked() ? 1 : 0);
            MenuItem removeMenu = LCoverNotiMainActivity.this.mMenu.findItem(LCoverNotiMainActivity.MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(true);
            }
            SLog.m12v(LCoverNotiMainActivity.TAG, "mCheckBoxAllClickListener");
            for (int i = 0; i < LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getCount(); i += LCoverNotiMainActivity.MENU_EDIT) {
                LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.setItemChecked(i, LCoverNotiMainActivity.this.cb_id_check.isChecked());
                LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
            }
            LCoverNotiMainActivity.this.setSelectedAppCount();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverNotiMainActivity.6 */
    class C04156 implements OnAdapterClickedListener {
        C04156() {
        }

        public void adapterClicked(int position, boolean val) {
            boolean z = true;
            SLog.m12v(LCoverNotiMainActivity.TAG, " adapterClicked pos : " + position + " value : " + val);
            if (LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.isEditClick()) {
                MenuItem removeMenu = LCoverNotiMainActivity.this.mMenu.findItem(LCoverNotiMainActivity.MENU_REMOVE);
                if (removeMenu != null) {
                    removeMenu.setVisible(true);
                }
                Boolean ischecked = LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getItem(position).getIsChecked();
                LCoverNotiIconListAdapter access$800 = LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter;
                if (ischecked.booleanValue()) {
                    z = false;
                }
                access$800.setItemChecked(position, z);
                LCoverNotiMainActivity.this.setSelectedAppCount();
            } else if (LCoverNotiMainActivity.this.mBeforeAct == 4) {
                result = new Intent();
                result.putExtra("selected_id", LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getId(position));
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getIconName(position));
                LCoverNotiMainActivity.this.setResult(-1, result);
                LCoverNotiMainActivity.this.finish();
            } else {
                result = new Intent(LCoverNotiMainActivity.this.mContext, LCoverNotiAppListActivity.class);
                result.putExtra("selected_id", LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getId(position));
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getIconName(position));
                result.putExtra("before_activity", LCoverNotiMainActivity.MENU_EDIT);
                LCoverNotiMainActivity.this.startActivity(result);
            }
        }

        public void adapterLongClicked(int position, boolean val) {
            boolean z = true;
            SLog.m12v(LCoverNotiMainActivity.TAG, " adapterLongClicked pos : " + position + " value : " + val);
            if (LCoverNotiMainActivity.this.mBeforeAct != 4) {
                if (LCoverNotiMainActivity.this.gv_presetList.getVisibility() == 0) {
                    Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_ACTIVITY_EVENT_SELECTION_MODE, "Selection mode");
                    Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_SELECTION_MODE);
                    LCoverNotiMainActivity.this.mEditMode = Boolean.valueOf(true);
                    LCoverNotiMainActivity.this.setEditCustomActionbar();
                    MenuItem removeMenu = LCoverNotiMainActivity.this.mMenu.findItem(LCoverNotiMainActivity.MENU_REMOVE);
                    if (removeMenu != null) {
                        removeMenu.setVisible(true);
                    }
                }
                Boolean ischecked = LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getItem(position).getIsChecked();
                LCoverNotiIconListAdapter access$800 = LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter;
                if (ischecked.booleanValue()) {
                    z = false;
                }
                access$800.setItemChecked(position, z);
                LCoverNotiMainActivity.this.setSelectedAppCount();
            }
        }

        public void adapterEditClicked(int position) {
            SLog.m12v(LCoverNotiMainActivity.TAG, "adapterEditClicked");
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_SELECTION_MODE_EVENT_MODIFY, "Modify");
            Intent result = new Intent(LCoverNotiMainActivity.this.mContext, LCoverCreatePatternActivity.class);
            result.putExtra("arrayName", LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getIconName(position));
            result.putExtra("arrayID", LCoverNotiMainActivity.this.mLCoverNotiCustomIconListAdapter.getId(position));
            LCoverNotiMainActivity.this.startActivityForResult(result, 10);
        }
    }

    public LCoverNotiMainActivity() {
        this.mNotiPresetIDInfoList = null;
        this.mNotiCustomIDInfoList = null;
        this.mLedNotiAppInfoList = null;
        this.mBeforeAct = 0;
        this.mCountVersionInfoToast = 0;
        this.mEditMode = Boolean.valueOf(false);
        this.ll_gridview = null;
        this.presetListOnItemClick = new C02113();
        this.customListOnItemLongClick = new C02124();
        this.customListOnItemClick = new C02135();
        this.callBack = new C04156();
        this.mCheckBoxAllClickListener = new C02158();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.m12v(TAG, "onCreate");
        this.mHandler = new Handler();
        this.loadingProgress = Utils.loadingProgressBar(this);
        Intent intent = getIntent();
        if (intent.hasExtra("before_activity")) {
            this.mBeforeAct = intent.getExtras().getInt("before_activity");
        }
        this.mContext = this;
        this.firstScreen = true;
        this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
        setupView();
        setCustomActionbar();
        sendLoggingforPresetIcon();
        sendLoggingforCustomIcon();
    }

    private void setupView() {
        setContentView(C0198R.layout.activity_led_notifications);
        this.mScroll = (ScrollView) findViewById(C0198R.id.sv_scroll);
        ScrollViewWrapper.semSetGoToTopEnabled(this.mScroll, true);
        this.mFloatingActionButton = (ImageButton) findViewById(C0198R.id.fab_noti);
        this.mFloatingActionButton.setOnClickListener(this);
        this.gv_presetList = (CustomGridView) findViewById(C0198R.id.gv_presetList);
        this.gv_customList = (CustomGridView) findViewById(C0198R.id.gv_customList);
        this.ll_gridview = (LinearLayout) findViewById(C0198R.id.ll_gridview);
        String deviceName = BidiFormatter.getInstance().unicodeWrap(Utils.getDeviceName(this.mContext));
        this.tv_divider = (TextView) findViewById(C0198R.id.tv_divider);
        TextView textView = this.tv_divider;
        String string = getString(C0198R.string.custom_icon_subtitle);
        Object[] objArr = new Object[MENU_EDIT];
        objArr[0] = deviceName;
        textView.setText(String.format(string, objArr));
        this.tv_divider.setOnClickListener(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mLastConfiguration == null || (this.mLastConfiguration.diff(newConfig) & Constants.DOWNLOAD_BUFFER_SIZE) != 0) {
            setupView();
        }
        this.mLastConfiguration = new Configuration(newConfig);
        if (this.mNotiPresetIDInfoList != null && this.mNotiPresetIDInfoList.size() > 0) {
            if (this.mLCoverNotiPresetIconListAdapter == null) {
                this.mLCoverNotiPresetIconListAdapter = new LCoverNotiIconListAdapter(this, this.mNotiPresetIDInfoList);
            }
            this.gv_presetList.setAdapter(this.mLCoverNotiPresetIconListAdapter);
            this.gv_presetList.setParents(this, this.mScroll, Defines.MENU_NOTI);
            this.gv_presetList.setExpanded(true);
            this.gv_presetList.setEnabled(true);
            this.gv_presetList.setOnItemClickListener(this.presetListOnItemClick);
        }
        if (this.mNotiCustomIDInfoList == null || this.mNotiCustomIDInfoList.size() <= 0) {
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), (int) getResources().getDimension(C0198R.dimen.led_cover_main_gridview_padding_bottom));
            this.tv_divider.setVisibility(8);
        } else {
            if (this.mLCoverNotiCustomIconListAdapter == null) {
                this.mLCoverNotiCustomIconListAdapter = new LCoverNotiIconListAdapter(this, this.mNotiCustomIDInfoList);
            }
            this.gv_customList.setAdapter(this.mLCoverNotiCustomIconListAdapter);
            this.gv_customList.setParents(this, this.mScroll, Defines.MENU_NOTI);
            this.gv_customList.setExpanded(true);
            this.gv_customList.setEnabled(true);
            this.gv_customList.setOnItemClickListener(this.customListOnItemClick);
            this.gv_customList.setOnItemLongClickListener(this.customListOnItemLongClick);
            this.mLCoverNotiCustomIconListAdapter.setOnClickListener(this.callBack);
            if (this.mBeforeAct == 4) {
                this.mLCoverNotiCustomIconListAdapter.setIsChangeMode(true);
            }
            this.tv_divider.setVisibility(0);
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), 0);
        }
        if (this.mEditMode.booleanValue()) {
            setEditCustomActionbar();
            setSelectedAppCount();
            return;
        }
        setCustomActionbar();
    }

    protected void onResume() {
        SLog.m12v(TAG, "onResume() and editmode : " + this.mEditMode);
        if (this.mBeforeAct == 4) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_CHANGE_MODE);
        } else if (this.mEditMode.booleanValue()) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY);
        }
        this.mCountVersionInfoToast = 0;
        if (!this.mEditMode.booleanValue()) {
            makePresetNotiIDList();
            int tempCustomIconCount = makeCustomNotiIDList();
            setCustomActionbar();
            if (this.mMenu != null) {
                MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
                MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
                if (!(editMenu == null || removeMenu == null)) {
                    if (this.mLCoverNotiCustomIconListAdapter == null || this.mNotiCustomIDInfoList.size() <= 0) {
                        editMenu.setVisible(false);
                        removeMenu.setVisible(false);
                    } else {
                        SLog.m12v(TAG, "onResume() mLCoverNotiCustomIconListAdapter is not null!!! ");
                        editMenu.setVisible(true);
                        removeMenu.setVisible(false);
                    }
                }
            }
            SLog.m12v(TAG, "customIconCount : " + this.customIconCount);
            if (this.firstScreen || tempCustomIconCount == 0 || tempCustomIconCount == this.customIconCount) {
                this.mScroll.scrollTo(this.prePositionX, this.prePositionY);
            } else {
                this.mScroll.postDelayed(new C02091(), 300);
            }
            this.customIconCount = tempCustomIconCount;
        } else if (this.mLCoverNotiCustomIconListAdapter != null) {
            this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    protected void onStop() {
        SLog.m12v(TAG, "onStop()");
        recycleList();
        super.onStop();
    }

    protected void onPause() {
        SLog.m12v(TAG, "onPause()");
        recycleList();
        this.prePositionX = this.gv_presetList.getScollViewX();
        this.prePositionY = this.gv_presetList.getScollViewY();
        super.onPause();
    }

    protected void onDestroy() {
        SLog.m12v(TAG, "onDestroy()");
        clearList();
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.printTemp = null;
        if (data != null) {
            String userName = data.getStringExtra("username");
            String string;
            Object[] objArr;
            if (resultCode == 11) {
                string = getResources().getString(C0198R.string.icon_modify);
                objArr = new Object[MENU_EDIT];
                objArr[0] = userName;
                this.printTemp = String.format(string, objArr);
            } else if (resultCode == 12) {
                string = getResources().getString(C0198R.string.icon_save);
                objArr = new Object[MENU_EDIT];
                objArr[0] = userName;
                this.printTemp = String.format(string, objArr);
            }
        }
        if (requestCode == 10) {
            SLog.m12v(TAG, "Start Progress for loading ");
            this.loadingProgress.show();
            this.mHandler.postDelayed(new C02102(), 1200);
        }
    }

    public void clearList() {
        if (this.mNotiCustomIDInfoList != null) {
            this.mNotiCustomIDInfoList.clear();
            this.mNotiCustomIDInfoList = null;
        }
        if (this.mNotiPresetIDInfoList != null) {
            this.mNotiPresetIDInfoList.clear();
            this.mNotiPresetIDInfoList = null;
        }
        if (this.mLCoverNotiCustomIconListAdapter != null) {
            this.mLCoverNotiCustomIconListAdapter = null;
        }
        if (this.mLCoverNotiPresetIconListAdapter != null) {
            this.mLCoverNotiPresetIconListAdapter = null;
        }
        recycleList();
    }

    public void recycleList() {
        if (this.gv_customList != null) {
            SLog.m12v(TAG, "Clear gv_customList");
            Utils.recursiveRecycle(this.gv_customList);
            this.gv_customList.removeAllViewsInLayout();
        }
        System.gc();
    }

    public void onBackPressed() {
        if (this.mLCoverNotiCustomIconListAdapter == null) {
            finish();
        } else if (this.mEditMode.booleanValue()) {
            SLog.m12v(TAG, "onBackPressed Editmode");
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY);
            setCustomActionbar();
            for (int i = 0; i < this.mLCoverNotiCustomIconListAdapter.getCount(); i += MENU_EDIT) {
                this.mLCoverNotiCustomIconListAdapter.setItemChecked(i, false);
            }
            this.mMenu.findItem(MENU_REMOVE).setVisible(false);
            this.mMenu.findItem(MENU_EDIT).setVisible(true);
            this.mLCoverNotiCustomIconListAdapter.setEditClick(false);
            this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
            this.mScroll.scrollTo(this.prePositionX, this.prePositionY);
        } else {
            finish();
        }
    }

    public void onClick(View v) {
        if (v.getId() != C0198R.id.tv_divider) {
            this.mCountVersionInfoToast = 0;
        }
        switch (v.getId()) {
            case C0198R.id.tv_divider /*2131624082*/:
                if (!this.mEditMode.booleanValue()) {
                    this.mCountVersionInfoToast += MENU_EDIT;
                    if (this.mCountVersionInfoToast >= 10) {
                        Toast.makeText(this, Secure.getString(getContentResolver(), "led_cover_firmware_version"), MENU_EDIT).show();
                        this.mCountVersionInfoToast = 0;
                    }
                }
            case C0198R.id.fab_noti /*2131624084*/:
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_ACTIVITY_EVENT_CREATE_LED_ICON, "Create LED icon");
                this.firstScreen = false;
                if (this.mLedCoverDbAccessor.getCustomIconInfo().size() >= 60) {
                    String string = getResources().getString(C0198R.string.save_error_maximum_char_reached);
                    Object[] objArr = new Object[MENU_EDIT];
                    objArr[0] = Integer.valueOf(60);
                    Toast.makeText(this, String.format(string, objArr), MENU_EDIT).show();
                    return;
                }
                Intent intet = new Intent(this, LCoverCreatePatternActivity.class);
                intet.putExtra("arrayName", "new");
                intet.putExtra("arrayID", -33);
                startActivityForResult(intet, 10);
            default:
        }
    }

    private void makePresetNotiIDList() {
        if (this.mNotiPresetIDInfoList == null) {
            this.mNotiPresetIDInfoList = new ArrayList();
            TypedArray name_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_string_entries);
            TypedArray icon_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_image_entries);
            TypedArray index_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_index_entries);
            for (int i = 0; i < name_array.length(); i += MENU_EDIT) {
                this.mNotiPresetIDInfoList.add(new LCoverIconInfo(index_array.getInt(i, 0), icon_array.getResourceId(i, 0), name_array.getString(i), 0));
            }
            name_array.recycle();
            icon_array.recycle();
            index_array.recycle();
            this.mLCoverNotiPresetIconListAdapter = new LCoverNotiIconListAdapter(this, this.mNotiPresetIDInfoList);
            this.gv_presetList.setAdapter(this.mLCoverNotiPresetIconListAdapter);
            this.gv_presetList.setExpanded(true);
            this.gv_presetList.setEnabled(true);
            this.gv_presetList.setParents(this, this.mScroll, Defines.MENU_NOTI);
            this.gv_presetList.setOnItemClickListener(this.presetListOnItemClick);
            return;
        }
        this.mLCoverNotiPresetIconListAdapter.notifyDataSetChanged();
    }

    private int makeCustomNotiIDList() {
        if (this.mNotiCustomIDInfoList == null) {
            this.mNotiCustomIDInfoList = new ArrayList();
        }
        this.mNotiCustomIDInfoList.clear();
        this.mNotiCustomIDInfoList.addAll(this.mLedCoverDbAccessor.getCustomIconInfo());
        LCoverSingleton.getInstance().setCustomLEDList(this.mNotiCustomIDInfoList);
        int iconCount = this.mNotiCustomIDInfoList.size();
        if (iconCount > 0) {
            if (this.mLCoverNotiCustomIconListAdapter == null) {
                SLog.m12v(TAG, "create mLCoverNotiCustomIconListAdapter");
                this.mLCoverNotiCustomIconListAdapter = new LCoverNotiIconListAdapter(this, this.mNotiCustomIDInfoList);
                SLog.m12v(TAG, "mLCoverNotiCustomIconListAdapter is created");
            }
            this.gv_customList.setAdapter(this.mLCoverNotiCustomIconListAdapter);
            this.gv_customList.setExpanded(true);
            this.gv_customList.setEnabled(true);
            this.gv_customList.setParents(this, this.mScroll, Defines.MENU_NOTI);
            this.gv_customList.setOnItemClickListener(this.customListOnItemClick);
            this.gv_customList.setOnItemLongClickListener(this.customListOnItemLongClick);
            this.mLCoverNotiCustomIconListAdapter.setOnClickListener(this.callBack);
            if (this.mBeforeAct == 4) {
                this.mLCoverNotiCustomIconListAdapter.setIsChangeMode(true);
            }
            this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
            this.tv_divider.setVisibility(0);
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), 0);
        } else {
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), (int) getResources().getDimension(C0198R.dimen.led_cover_main_gridview_padding_bottom));
            this.tv_divider.setVisibility(8);
        }
        return iconCount;
    }

    private void setCustomActionbar() {
        this.mEditMode = Boolean.valueOf(false);
        ActionBar customActionbar = getActionBar();
        customActionbar.setDisplayShowCustomEnabled(true);
        customActionbar.setDisplayHomeAsUpEnabled(false);
        customActionbar.setDisplayShowTitleEnabled(false);
        View customView = LayoutInflater.from(this).inflate(C0198R.layout.action_bar_custom_main, null);
        customActionbar.setCustomView(customView);
        ((Toolbar) customView.getParent()).setContentInsetsAbsolute(0, 0);
        TextView actionbarTitle = (TextView) customView.findViewById(C0198R.id.actionbar_title);
        Utils.setLargeTextSize(this.mContext, actionbarTitle, (float) this.mContext.getResources().getDimensionPixelSize(C0198R.dimen.led_cover_main_abar_desc_textview_text_size));
        if (this.mBeforeAct == 4) {
            actionbarTitle.setText(C0198R.string.menu_change);
        } else {
            actionbarTitle.setText(C0198R.string.led_notification_icons);
        }
        customView.findViewById(C0198R.id.actionbar_icon).setOnClickListener(new C02147());
        this.gv_presetList.setAlpha(1.0f);
        this.gv_presetList.setEnabled(true);
        if (this.mBeforeAct == 4) {
            this.mFloatingActionButton.setVisibility(8);
        } else {
            this.mFloatingActionButton.setVisibility(0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isShowEdit = false;
        SLog.m12v(TAG, "onCreateOptionsMenu editmode : " + this.mEditMode);
        this.mMenu = menu;
        this.mMenu.clear();
        if (this.mBeforeAct != 4) {
            menu.add(0, MENU_EDIT, 0, C0198R.string.menu_edit).setShowAsAction(MENU_EDIT);
            menu.add(0, MENU_REMOVE, 0, C0198R.string.menu_remove).setShowAsAction(MENU_EDIT);
            if (!(this.mEditMode.booleanValue() || this.mLCoverNotiCustomIconListAdapter == null)) {
                isShowEdit = true;
            }
            boolean isShowRemove = this.mEditMode.booleanValue();
            menu.findItem(MENU_EDIT).setVisible(isShowEdit);
            menu.findItem(MENU_REMOVE).setVisible(isShowRemove);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EDIT /*1*/:
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY, Defines.SA_NOTI_MAIN_ACTIVITY_EVENT_EDIT, "Edit");
                Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_SELECTION_MODE);
                this.mEditMode = Boolean.valueOf(true);
                setEditCustomActionbar();
                this.mScroll.scrollTo(this.customTopPositionX, this.customTopPositionY);
                break;
            case MENU_REMOVE /*2*/:
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_MAIN_SELECTION_MODE, Defines.SA_NOTI_MAIN_SELECTION_MODE_EVENT_REMOVE, "Remove");
                Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_MAIN_ACTIVITY);
                deleteIcons();
                break;
            case 16908332:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEditCustomActionbar() {
        SLog.m12v(TAG, "setEditCustomActionbar : " + this.mEditMode);
        if (this.mEditMode.booleanValue()) {
            MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
            if (editMenu != null) {
                editMenu.setVisible(false);
            }
            MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(false);
            }
            ActionBar customActionbar = getActionBar();
            customActionbar.setDisplayShowCustomEnabled(true);
            customActionbar.setDisplayHomeAsUpEnabled(false);
            customActionbar.setDisplayShowTitleEnabled(false);
            View mCustomView = LayoutInflater.from(this).inflate(C0198R.layout.action_bar_custom_main_edit, null);
            customActionbar.setCustomView(mCustomView);
            ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0, 0);
            this.cb_id_check = (CheckBox) findViewById(C0198R.id.cb_icon_check);
            findViewById(C0198R.id.select_all_layout).setOnClickListener(this.mCheckBoxAllClickListener);
            this.tv_id_count = (TextView) findViewById(C0198R.id.tv_id_count);
            Utils.setLargeTextSize(this.mContext, this.tv_id_count, (float) this.mContext.getResources().getDimensionPixelSize(C0198R.dimen.led_cover_main_abar_desc_textview_text_size));
            if (this.mLCoverNotiCustomIconListAdapter != null) {
                this.mLCoverNotiCustomIconListAdapter.setEditClick(true);
                this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
            }
            this.mFloatingActionButton.setVisibility(8);
            this.gv_presetList.setAlpha(0.48f);
            this.gv_presetList.setEnabled(false);
            this.prePositionX = this.gv_presetList.getScollViewX();
            this.prePositionY = this.gv_presetList.getScollViewY();
            this.customTopPositionX = this.tv_divider.getLeft();
            this.customTopPositionY = this.tv_divider.getTop();
        }
    }

    private void setSelectedAppCount() {
        int checkedAppCount = this.mLCoverNotiCustomIconListAdapter.getItemCheckedCount();
        int listAppCount = this.mLCoverNotiCustomIconListAdapter.getCount();
        SLog.m12v(TAG, "setSelectedAppCount :: " + checkedAppCount + " , " + listAppCount);
        MenuItem removeMenu;
        if (checkedAppCount > 0) {
            TextView textView = this.tv_id_count;
            Object[] objArr = new Object[MENU_EDIT];
            objArr[0] = Integer.valueOf(checkedAppCount);
            textView.setText(getString(C0198R.string.selected_numberof_fingerprint, objArr));
            removeMenu = this.mMenu.findItem(MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(true);
            }
        } else {
            this.tv_id_count.setText(C0198R.string.edit_icon_title);
            removeMenu = this.mMenu.findItem(MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(false);
            }
        }
        if (checkedAppCount == listAppCount) {
            this.cb_id_check.setChecked(true);
        } else {
            this.cb_id_check.setChecked(false);
        }
    }

    private void deleteIcons() {
        int i;
        int size = this.mLedCoverDbAccessor.getCustomIconInfo().size();
        ArrayList<LCoverIconInfo> temp = new ArrayList();
        ArrayList<Integer> iconList = new ArrayList();
        temp.addAll(this.mNotiCustomIDInfoList);
        SLog.m12v(TAG, "Array Size : " + size);
        ArrayList<View> viewList = new ArrayList();
        for (i = 0; i < temp.size(); i += MENU_EDIT) {
            if (((LCoverIconInfo) temp.get(i)).getIsChecked().booleanValue()) {
                SLog.m12v(TAG, "DELETE CLICKED position : " + i + " isChecked? : " + ((LCoverIconInfo) temp.get(i)).getIsChecked());
                SLog.m12v(TAG, "DELETE CLICKED getId : " + ((LCoverIconInfo) temp.get(i)).getId());
                SLog.m12v(TAG, "DELETE CLICKED getName : " + ((LCoverIconInfo) temp.get(i)).getIconName());
                iconList.add(new Integer(((LCoverIconInfo) temp.get(i)).getId()));
                viewList.add(this.gv_customList.getChildAt(this.mNotiCustomIDInfoList.indexOf(temp.get(i))));
            }
        }
        removeLEDIcons(iconList);
        int iconListSize = iconList.size();
        StringBuilder placeholders = new StringBuilder();
        String[] whereArgs = new String[iconListSize];
        for (i = 0; i < iconListSize; i += MENU_EDIT) {
            if (i != 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
            whereArgs[i] = C0316a.f163d + iconList.get(i);
            SLog.m12v(TAG, "Selected icon_id : " + whereArgs[i]);
        }
        if (this.mLedCoverDbAccessor.deleteLedIcons("icon_id IN (" + placeholders.toString() + ")", whereArgs)) {
            SLog.m12v(TAG, "listViewSize: " + this.gv_customList.getChildCount());
            SLog.m12v(TAG, "listItemSize: " + this.mNotiCustomIDInfoList.size());
            for (i = 0; i < viewList.size(); i += MENU_EDIT) {
                this.gv_customList.removeViewInLayout((View) viewList.get(i));
                SLog.m12v(TAG, "listViewSize: " + this.gv_customList.getChildCount());
            }
            this.mLCoverNotiCustomIconListAdapter.setEditClick(false);
            this.mLCoverNotiCustomIconListAdapter.notifyDataSetChanged();
            viewList.clear();
        } else {
            SLog.m12v(TAG, "deleteLedIcons Failed!");
        }
        makeCustomNotiIDList();
        setCustomActionbar();
        MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
        MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
        if (removeMenu != null) {
            removeMenu.setVisible(false);
        }
        SLog.m12v(TAG, "mNotiCustomIDInfoList size: " + this.mNotiCustomIDInfoList.size());
        if (this.mNotiCustomIDInfoList.size() == 0) {
            this.tv_divider.setVisibility(8);
            if (editMenu != null) {
                editMenu.setVisible(false);
            }
        } else if (editMenu != null) {
            editMenu.setVisible(true);
        }
    }

    private void removeLEDIcons(ArrayList<Integer> iconList) {
        SLog.m12v(TAG, "removeLEDIcons()");
        ArrayList<String> pkgArray = new ArrayList();
        this.mLedNotiAppInfoList = this.mLedCoverDbAccessor.getSelectedAppsInfo();
        if (this.mLedNotiAppInfoList != null) {
            SLog.m12v(TAG, "removeLEDIcons mLedNotiAppInfoList.size : " + this.mLedNotiAppInfoList.size());
            for (int i = 0; i < iconList.size(); i += MENU_EDIT) {
                for (int j = 0; j < this.mLedNotiAppInfoList.size(); j += MENU_EDIT) {
                    if (((LCoverAppInfo) this.mLedNotiAppInfoList.get(j)).getIconId() == ((Integer) iconList.get(i)).intValue()) {
                        SLog.m12v(TAG, "removeLEDIcons icon_id : " + ((Integer) iconList.get(i)).intValue() + ", package_id : " + ((LCoverAppInfo) this.mLedNotiAppInfoList.get(j)).getPackageName().toString());
                        pkgArray.add(((LCoverAppInfo) this.mLedNotiAppInfoList.get(j)).getPackageName().toString());
                    }
                }
            }
            String[] pkgNameArray = (String[]) pkgArray.toArray(new String[pkgArray.size()]);
            Intent bIntent = new Intent();
            bIntent.setAction(Defines.BROADCAST_ACTION_APP_CHANGED_FROM_ICON);
            bIntent.putExtra("packages", pkgNameArray);
            bIntent.putExtra("isChanged", false);
            sendBroadcast(bIntent, Defines.PERMISSION_LCOVER_LAUNCH);
        }
    }

    public void insertLog(String appId, String feature, String extra, int value) {
        if (FloatingFeatureWrapper.getBoolean("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE")) {
            SLog.m12v(TAG, "insertLog");
            ContentValues cv = new ContentValues();
            cv.put("app_id", appId);
            cv.put("feature", feature);
            cv.put("extra", extra);
            cv.put("value", Integer.valueOf(value));
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.REPORT_APP_STATUS_SURVEY");
            broadcastIntent.putExtra(C0305c.f129c, cv);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            sendBroadcast(broadcastIntent);
        }
    }

    public void insertMultipleStatusLog(String appId, String[] features, String[] extras, int[] values) {
        if (FloatingFeatureWrapper.getBoolean("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE")) {
            SLog.m12v(TAG, "insertMultipleStatusLog");
            ContentValues[] cvs = new ContentValues[features.length];
            for (int i = 0; i < features.length; i += MENU_EDIT) {
                cvs[i] = new ContentValues();
                cvs[i].put("app_id", appId);
                cvs[i].put("feature", features[i]);
                cvs[i].put("extra", extras[i]);
                cvs[i].put("value", Integer.valueOf(values[i]));
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.REPORT_MULTI_APP_STATUS_SURVEY");
            broadcastIntent.putExtra(C0305c.f129c, cvs);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            sendBroadcast(broadcastIntent);
        }
    }

    public void sendLoggingforPresetIcon() {
        String[] name = new String[54];
        String[] feat = new String[54];
        int[] cnt = new int[54];
        String[] name_array = getResources().getStringArray(C0198R.array.ledcover_preset_name_entries_logging);
        TypedArray index_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_index_entries);
        for (int i = 0; i < name_array.length; i += MENU_EDIT) {
            name[i] = name_array[i];
            if (i < 10) {
                feat[i] = "LC0" + i;
            } else {
                feat[i] = "LC" + i;
            }
            cnt[i] = this.mLedCoverDbAccessor.getIconDbCount(index_array.getInt(i, 0));
        }
        insertMultipleStatusLog(Defines.PKG_NAME, feat, name, cnt);
        index_array.recycle();
    }

    public void sendLoggingforCustomIcon() {
        if (this.mNotiCustomIDInfoList != null) {
            insertLog(Defines.PKG_NAME, "LCCS", "CUSTOM ICON NUM", this.mNotiCustomIDInfoList.size());
            return;
        }
        insertLog(Defines.PKG_NAME, "LCCS", "CUSTOM ICON NUM", 0);
    }
}
