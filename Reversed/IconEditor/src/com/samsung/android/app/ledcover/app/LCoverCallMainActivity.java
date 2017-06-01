package com.samsung.android.app.ledcover.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.samsung.android.app.ledcover.adapter.LCoverCallIconListAdapter;
import com.samsung.android.app.ledcover.adapter.LCoverCallIconListAdapter.OnAdapterClickedListener;
import com.samsung.android.app.ledcover.call.LCoverContactsListActivity;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.fota.Constants;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.wrapperlibrary.ScrollViewWrapper;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.util.ArrayList;
import java.util.HashMap;

public class LCoverCallMainActivity extends Activity {
    private static final int MENU_EDIT = 1;
    private static final int MENU_REMOVE = 2;
    public static final String TAG = "[LED_COVER]LCoverCallMainActivity";
    private OnAdapterClickedListener callBack;
    private CheckBox cb_id_check;
    private int customIconCount;
    OnItemClickListener customListOnItemClick;
    OnItemLongClickListener customListOnItemLongClick;
    int customTopPositionX;
    int customTopPositionY;
    OnClickListener fabOnClickListener;
    private boolean firstScreen;
    private CustomGridView gv_customCallList;
    private CustomGridView gv_presetCallList;
    private int isChangeMode;
    private LinearLayout ll_gridview;
    private Dialog loadingProgress;
    private ArrayList<LCoverIconInfo> mCallCustomIDInfoList;
    private ArrayList<LCoverIconInfo> mCallPresetIDInfoList;
    private OnClickListener mCheckBoxAllClickListener;
    private Context mContext;
    private Boolean mEditMode;
    private ImageButton mFloatingActionButton;
    private Handler mHandler;
    private LCoverCallIconListAdapter mLCoverCallCustomIconListAdapter;
    private LCoverCallIconListAdapter mLCoverCallPresetIconListAdapter;
    private long mLastClickTime;
    private Configuration mLastConfiguration;
    private LCoverDbAccessor mLedCoverDbAccessor;
    private Menu mMenu;
    private ScrollView mScroll;
    int prePositionX;
    int prePositionY;
    OnItemClickListener presetListOnItemClick;
    private String printTemp;
    private TextView tv_divider;
    private TextView tv_id_count;

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.1 */
    class C02011 implements Runnable {
        C02011() {
        }

        public void run() {
            LCoverCallMainActivity.this.mScroll.fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.2 */
    class C02022 implements Runnable {
        C02022() {
        }

        public void run() {
            LCoverCallMainActivity.this.loadingProgress.dismiss();
            if (LCoverCallMainActivity.this.printTemp != null) {
                Toast.makeText(LCoverCallMainActivity.this.getApplicationContext(), LCoverCallMainActivity.this.printTemp, LCoverCallMainActivity.MENU_EDIT).show();
            }
            SLog.m12v(LCoverCallMainActivity.TAG, "End Progress for loading ");
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.3 */
    class C02033 implements OnItemClickListener {
        C02033() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (SystemClock.elapsedRealtime() - LCoverCallMainActivity.this.mLastClickTime >= 1000) {
                LCoverCallMainActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                SLog.m12v(LCoverCallMainActivity.TAG, "presetCallListOnItemClick " + parent.getId());
                SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallPresetIconListAdapter pos : " + position + " mId : " + LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getId(position));
                SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallPresetIconListAdapter  getIconName : " + LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getIconName(position));
                SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallPresetIconListAdapter mCallerIDCount : " + LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getItem(position).mCallerIDCount);
                SLog.m12v(LCoverCallMainActivity.TAG, "isChangeMode : " + LCoverCallMainActivity.this.isChangeMode);
                Intent result;
                if (LCoverCallMainActivity.this.isChangeMode == LCoverCallMainActivity.MENU_EDIT) {
                    Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_CHANGE_MODE, Defines.SA_CALL_MAIN_CHANGE_MODE_EVENT_CHANGE_LED_ICON, "Change LED icons", (long) (position + LCoverCallMainActivity.MENU_EDIT));
                    result = new Intent();
                    result.putExtra("selected_id", LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getItem(position).mId);
                    result.putExtra("contact_count", LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getItem(position).mCallerIDCount);
                    result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getIconName(position));
                    LCoverCallMainActivity.this.setResult(-1, result);
                    LCoverCallMainActivity.this.finish();
                    return;
                }
                Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_ACTIVITY_EVENT_LED_ICONS, "LED icons", (long) (position + LCoverCallMainActivity.MENU_EDIT));
                result = new Intent(LCoverCallMainActivity.this.mContext, LCoverContactsListActivity.class);
                result.putExtra("selected_id", LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getItem(position).mId);
                result.putExtra("contact_count", LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getItem(position).mCallerIDCount);
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverCallMainActivity.this.mLCoverCallPresetIconListAdapter.getIconName(position));
                LCoverCallMainActivity.this.startActivity(result);
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.4 */
    class C02044 implements OnItemLongClickListener {
        C02044() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            boolean z;
            int i = 0;
            CheckBox checkbox = (CheckBox) view.findViewById(C0198R.id.cb_delete);
            LCoverCallMainActivity.this.callBack.adapterLongClicked(position, checkbox.isChecked());
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

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.5 */
    class C02055 implements OnItemClickListener {
        C02055() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            boolean z;
            int i = 0;
            CheckBox checkbox = (CheckBox) view.findViewById(C0198R.id.cb_delete);
            LCoverCallMainActivity.this.callBack.adapterClicked(position, checkbox.isChecked());
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

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.7 */
    class C02067 implements OnClickListener {
        C02067() {
        }

        public void onClick(View v) {
            if (LCoverCallMainActivity.this.isChangeMode == LCoverCallMainActivity.MENU_EDIT) {
                Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_CHANGE_MODE, Defines.SA_CALL_MAIN_CHANGE_MODE_EVENT_UP_BUTTON, "Up button");
            } else {
                Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_ACTIVITY_EVENT_UP_BUTTON, "Up button");
            }
            LCoverCallMainActivity.this.onBackPressed();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.8 */
    class C02078 implements OnClickListener {
        C02078() {
        }

        public void onClick(View v) {
            LCoverCallMainActivity.this.cb_id_check.setChecked(!LCoverCallMainActivity.this.cb_id_check.isChecked());
            Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_SELECTION_MODE, Defines.SA_CALL_MAIN_SELECTION_MODE_EVENT_SELECT_ALL, "Select all", LCoverCallMainActivity.this.cb_id_check.isChecked() ? 1 : 0);
            MenuItem removeMenu = LCoverCallMainActivity.this.mMenu.findItem(LCoverCallMainActivity.MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(true);
            }
            SLog.m12v(LCoverCallMainActivity.TAG, "mCheckBoxAllClickListener");
            for (int i = 0; i < LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getCount(); i += LCoverCallMainActivity.MENU_EDIT) {
                LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.setItemChecked(i, LCoverCallMainActivity.this.cb_id_check.isChecked());
                LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
            }
            LCoverCallMainActivity.this.setSelectedIconCount();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.9 */
    class C02089 implements OnClickListener {
        C02089() {
        }

        public void onClick(View v) {
            Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_ACTIVITY_EVENT_CREATE_LED_ICON, "Create LED icon");
            LCoverCallMainActivity.this.firstScreen = false;
            if (LCoverCallMainActivity.this.mLedCoverDbAccessor.getCustomIconInfo().size() >= 60) {
                String string = LCoverCallMainActivity.this.getResources().getString(C0198R.string.save_error_maximum_char_reached);
                Object[] objArr = new Object[LCoverCallMainActivity.MENU_EDIT];
                objArr[0] = Integer.valueOf(60);
                Toast.makeText(LCoverCallMainActivity.this.mContext, String.format(string, objArr), LCoverCallMainActivity.MENU_EDIT).show();
                return;
            }
            Intent intet = new Intent(LCoverCallMainActivity.this.mContext, LCoverCreatePatternActivity.class);
            intet.putExtra("arrayName", "new");
            intet.putExtra("arrayID", -33);
            LCoverCallMainActivity.this.startActivityForResult(intet, 10);
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.app.LCoverCallMainActivity.6 */
    class C04146 implements OnAdapterClickedListener {
        C04146() {
        }

        public void adapterClicked(int position, boolean val) {
            boolean z = true;
            SLog.m12v(LCoverCallMainActivity.TAG, " adapterClicked pos : " + position + " value : " + val);
            SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallCustomIconListAdapter pos : " + position + " mId : " + LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getId(position));
            SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallCustomIconListAdapter  getIconName : " + LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getIconName(position));
            SLog.m12v(LCoverCallMainActivity.TAG, "mLCoverCallCustomIconListAdapter mCallerIDCount : " + LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).mCallerIDCount);
            SLog.m12v(LCoverCallMainActivity.TAG, "isChangeMode : " + LCoverCallMainActivity.this.isChangeMode);
            if (LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.isEditClick()) {
                MenuItem removeMenu = LCoverCallMainActivity.this.mMenu.findItem(LCoverCallMainActivity.MENU_REMOVE);
                if (removeMenu != null) {
                    removeMenu.setVisible(true);
                }
                Boolean ischecked = LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).getIsChecked();
                LCoverCallIconListAdapter access$800 = LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter;
                if (ischecked.booleanValue()) {
                    z = false;
                }
                access$800.setItemChecked(position, z);
                LCoverCallMainActivity.this.setSelectedIconCount();
            } else if (LCoverCallMainActivity.this.isChangeMode == LCoverCallMainActivity.MENU_EDIT) {
                result = new Intent();
                result.putExtra("selected_id", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).mId);
                result.putExtra("contact_count", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).mCallerIDCount);
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getIconName(position));
                LCoverCallMainActivity.this.setResult(-1, result);
                LCoverCallMainActivity.this.finish();
            } else {
                result = new Intent(LCoverCallMainActivity.this.mContext, LCoverContactsListActivity.class);
                result.putExtra("selected_id", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).mId);
                result.putExtra("contact_count", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).mCallerIDCount);
                result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getIconName(position));
                LCoverCallMainActivity.this.startActivity(result);
            }
        }

        public void adapterLongClicked(int position, boolean val) {
            boolean z = true;
            SLog.m12v(LCoverCallMainActivity.TAG, " adapterLongClicked pos : " + position + " value : " + val);
            if (LCoverCallMainActivity.this.isChangeMode != LCoverCallMainActivity.MENU_EDIT) {
                if (LCoverCallMainActivity.this.gv_presetCallList.getVisibility() == 0) {
                    Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_ACTIVITY_EVENT_SELECTION_MODE, "Selection mode");
                    Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_SELECTION_MODE);
                    LCoverCallMainActivity.this.mEditMode = Boolean.valueOf(true);
                    LCoverCallMainActivity.this.setEditCustomActionbar();
                    MenuItem removeMenu = LCoverCallMainActivity.this.mMenu.findItem(LCoverCallMainActivity.MENU_REMOVE);
                    if (removeMenu != null) {
                        removeMenu.setVisible(true);
                    }
                }
                Boolean ischecked = LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getItem(position).getIsChecked();
                LCoverCallIconListAdapter access$800 = LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter;
                if (ischecked.booleanValue()) {
                    z = false;
                }
                access$800.setItemChecked(position, z);
                LCoverCallMainActivity.this.setSelectedIconCount();
            }
        }

        public void adapterEditClicked(int position) {
            SLog.m12v(LCoverCallMainActivity.TAG, "adapterEditClicked");
            Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_SELECTION_MODE_EVENT_MODIFY, "Modify");
            Intent result = new Intent(LCoverCallMainActivity.this.mContext, LCoverCreatePatternActivity.class);
            result.putExtra("arrayName", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getIconName(position));
            result.putExtra("arrayID", LCoverCallMainActivity.this.mLCoverCallCustomIconListAdapter.getId(position));
            LCoverCallMainActivity.this.startActivityForResult(result, 10);
        }
    }

    public LCoverCallMainActivity() {
        this.mCallPresetIDInfoList = null;
        this.mCallCustomIDInfoList = null;
        this.mEditMode = Boolean.valueOf(false);
        this.ll_gridview = null;
        this.isChangeMode = 0;
        this.presetListOnItemClick = new C02033();
        this.customListOnItemLongClick = new C02044();
        this.customListOnItemClick = new C02055();
        this.callBack = new C04146();
        this.mCheckBoxAllClickListener = new C02078();
        this.fabOnClickListener = new C02089();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        this.mHandler = new Handler();
        this.loadingProgress = Utils.loadingProgressBar(this);
        Intent intent = getIntent();
        if (intent.hasExtra("change_mode")) {
            this.isChangeMode = intent.getExtras().getInt("change_mode");
        }
        this.firstScreen = true;
        this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
        setupView();
        setCustomActionbar();
    }

    private void setupView() {
        setContentView(C0198R.layout.activity_led_notifications);
        this.mScroll = (ScrollView) findViewById(C0198R.id.sv_scroll);
        ScrollViewWrapper.semSetGoToTopEnabled(this.mScroll, true);
        this.mFloatingActionButton = (ImageButton) findViewById(C0198R.id.fab_noti);
        this.mFloatingActionButton.setOnClickListener(this.fabOnClickListener);
        this.gv_presetCallList = (CustomGridView) findViewById(C0198R.id.gv_presetList);
        this.gv_presetCallList.setExpanded(true);
        this.gv_presetCallList.setEnabled(true);
        this.gv_presetCallList.setOnItemClickListener(this.presetListOnItemClick);
        this.gv_customCallList = (CustomGridView) findViewById(C0198R.id.gv_customList);
        this.gv_customCallList.setExpanded(true);
        this.gv_customCallList.setEnabled(true);
        this.gv_customCallList.setOnItemClickListener(this.customListOnItemClick);
        this.gv_customCallList.setOnItemLongClickListener(this.customListOnItemLongClick);
        this.ll_gridview = (LinearLayout) findViewById(C0198R.id.ll_gridview);
        this.ll_gridview.setFocusable(false);
        this.tv_divider = (TextView) findViewById(C0198R.id.tv_divider);
        String deviceName = BidiFormatter.getInstance().unicodeWrap(Utils.getDeviceName(this.mContext));
        TextView textView = this.tv_divider;
        Object[] objArr = new Object[MENU_EDIT];
        objArr[0] = deviceName;
        textView.setText(getString(C0198R.string.custom_icon_subtitle, objArr));
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mLastConfiguration == null || (this.mLastConfiguration.diff(newConfig) & Constants.DOWNLOAD_BUFFER_SIZE) != 0) {
            setupView();
        }
        this.mLastConfiguration = new Configuration(newConfig);
        if (this.mCallPresetIDInfoList != null && this.mCallPresetIDInfoList.size() > 0) {
            if (this.mLCoverCallPresetIconListAdapter == null) {
                this.mLCoverCallPresetIconListAdapter = new LCoverCallIconListAdapter(this, this.mCallPresetIDInfoList);
            }
            this.gv_presetCallList.setAdapter(this.mLCoverCallPresetIconListAdapter);
            this.gv_presetCallList.setParents(this, this.mScroll, Defines.MENU_CALL);
            this.gv_presetCallList.setOnItemClickListener(this.presetListOnItemClick);
        }
        if (this.mCallCustomIDInfoList == null || this.mCallCustomIDInfoList.size() <= 0) {
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), (int) getResources().getDimension(C0198R.dimen.led_cover_main_gridview_padding_bottom));
            this.tv_divider.setVisibility(8);
        } else {
            if (this.mLCoverCallCustomIconListAdapter == null) {
                this.mLCoverCallCustomIconListAdapter = new LCoverCallIconListAdapter(this, this.mCallCustomIDInfoList);
            }
            this.gv_customCallList.setAdapter(this.mLCoverCallCustomIconListAdapter);
            this.gv_customCallList.setParents(this, this.mScroll, Defines.MENU_CALL);
            this.gv_customCallList.setOnItemClickListener(this.customListOnItemClick);
            this.gv_customCallList.setOnItemLongClickListener(this.customListOnItemLongClick);
            this.mLCoverCallCustomIconListAdapter.setOnClickListener(this.callBack);
            if (this.isChangeMode == MENU_EDIT) {
                this.mLCoverCallCustomIconListAdapter.setIsChangeMode(true);
            }
            this.tv_divider.setVisibility(0);
            this.ll_gridview.setPadding(this.ll_gridview.getPaddingLeft(), this.ll_gridview.getPaddingTop(), this.ll_gridview.getPaddingRight(), 0);
        }
        if (this.mEditMode.booleanValue()) {
            setEditCustomActionbar();
            setSelectedIconCount();
            return;
        }
        setCustomActionbar();
    }

    protected void onResume() {
        SLog.m12v(TAG, "onResume() and EditMode : " + this.mEditMode);
        if (this.isChangeMode == MENU_EDIT) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_CHANGE_MODE);
        } else if (this.mEditMode.booleanValue()) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY);
        }
        if (!this.mEditMode.booleanValue()) {
            makePresetCallIDList();
            int tempCustomIconCount = makeCustomCallIDList();
            setCustomActionbar();
            if (this.mMenu != null) {
                MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
                MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
                if (!(editMenu == null || removeMenu == null)) {
                    if (this.mLCoverCallCustomIconListAdapter == null || this.mCallCustomIDInfoList.size() <= 0) {
                        editMenu.setVisible(false);
                        removeMenu.setVisible(false);
                    } else {
                        editMenu.setVisible(true);
                        removeMenu.setVisible(false);
                    }
                }
            }
            SLog.m12v(TAG, "tempCustomIconCount : " + tempCustomIconCount);
            SLog.m12v(TAG, "customIconCount : " + this.customIconCount);
            if (this.firstScreen || tempCustomIconCount == 0 || tempCustomIconCount == this.customIconCount) {
                this.mScroll.scrollTo(this.prePositionX, this.prePositionY);
            } else {
                this.mScroll.postDelayed(new C02011(), 300);
            }
            this.customIconCount = tempCustomIconCount;
        } else if (this.mLCoverCallCustomIconListAdapter != null) {
            this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
        }
        if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
            checkCallerIDContactCount();
        } else {
            checkRuntimePermission();
        }
        if (this.mScroll != null) {
            this.mScroll.requestFocus();
        }
        super.onResume();
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
            this.mHandler.postDelayed(new C02022(), 1200);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        SLog.m12v(TAG, "onRequestPermissionsResult requestCode : ");
        if (requestCode != 17) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
            checkCallerIDContactCount();
        } else {
            finish();
        }
    }

    public void checkRuntimePermission() {
        SLog.m12v(TAG, "permission requestRuntimePermission type : ");
        if (ContextCompat.checkSelfPermission(this, Defines.READ_PERMISSIONS_STRING_CONTACTS) == 0) {
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Defines.READ_PERMISSIONS_STRING_CONTACTS)) {
            SLog.m12v(TAG, "[checkRuntimePermission] permission 2nd question pop up");
            String[] strArr = new String[MENU_EDIT];
            strArr[0] = Defines.READ_PERMISSIONS_STRING_CONTACTS;
            ActivityCompat.requestPermissions(this, strArr, 17);
            return;
        }
        SLog.m12v(TAG, "[checkRuntimePermission] permission 1st question pop up");
        strArr = new String[MENU_EDIT];
        strArr[0] = Defines.READ_PERMISSIONS_STRING_CONTACTS;
        ActivityCompat.requestPermissions(this, strArr, 17);
    }

    protected void onStop() {
        SLog.m12v(TAG, "onStop()");
        recycleList();
        super.onStop();
    }

    protected void onPause() {
        this.prePositionX = this.gv_presetCallList.getScollViewX();
        this.prePositionY = this.gv_presetCallList.getScollViewY();
        recycleList();
        super.onPause();
    }

    protected void onDestroy() {
        SLog.m12v(TAG, "onDestroy()");
        clearList();
        super.onDestroy();
    }

    public void clearList() {
        if (this.mCallCustomIDInfoList != null) {
            this.mCallCustomIDInfoList.clear();
            this.mCallCustomIDInfoList = null;
        }
        if (this.mCallPresetIDInfoList != null) {
            this.mCallPresetIDInfoList.clear();
            this.mCallPresetIDInfoList = null;
        }
        if (this.mLCoverCallCustomIconListAdapter != null) {
            this.mLCoverCallCustomIconListAdapter = null;
        }
        if (this.mLCoverCallPresetIconListAdapter != null) {
            this.mLCoverCallPresetIconListAdapter = null;
        }
        recycleList();
    }

    public void recycleList() {
        if (this.gv_customCallList != null) {
            SLog.m12v(TAG, "Clear gv_customCallList");
            Utils.recursiveRecycle(this.gv_customCallList);
            this.gv_customCallList.removeAllViewsInLayout();
        }
        System.gc();
    }

    public void onBackPressed() {
        if (this.mLCoverCallCustomIconListAdapter == null) {
            finish();
        } else if (this.mEditMode.booleanValue()) {
            SLog.m12v(TAG, "onBackPressed Editmode");
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY);
            setCustomActionbar();
            for (int i = 0; i < this.mLCoverCallCustomIconListAdapter.getCount(); i += MENU_EDIT) {
                this.mLCoverCallCustomIconListAdapter.setItemChecked(i, false);
            }
            MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
            MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
            if (removeMenu != null) {
                removeMenu.setVisible(false);
            }
            if (editMenu != null) {
                editMenu.setVisible(true);
            }
            this.mLCoverCallCustomIconListAdapter.setEditClick(false);
            this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
            this.mScroll.scrollTo(this.prePositionX, this.prePositionY);
        } else {
            finish();
        }
    }

    private void makePresetCallIDList() {
        if (this.mCallPresetIDInfoList == null) {
            this.mCallPresetIDInfoList = new ArrayList();
            TypedArray name_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_string_entries);
            TypedArray icon_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_image_entries);
            TypedArray index_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_index_entries);
            for (int i = 0; i < name_array.length(); i += MENU_EDIT) {
                this.mCallPresetIDInfoList.add(new LCoverIconInfo(index_array.getInt(i, 0), icon_array.getResourceId(i, 0), name_array.getString(i), 0));
            }
            name_array.recycle();
            icon_array.recycle();
            index_array.recycle();
            this.mLCoverCallPresetIconListAdapter = new LCoverCallIconListAdapter(this, this.mCallPresetIDInfoList);
            this.gv_presetCallList.setAdapter(this.mLCoverCallPresetIconListAdapter);
            this.gv_presetCallList.setExpanded(true);
            this.gv_presetCallList.setEnabled(true);
            this.gv_presetCallList.setParents(this, this.mScroll, Defines.MENU_CALL);
            this.gv_presetCallList.setOnItemClickListener(this.presetListOnItemClick);
            return;
        }
        this.mLCoverCallPresetIconListAdapter.notifyDataSetChanged();
    }

    private int makeCustomCallIDList() {
        if (this.mCallCustomIDInfoList == null) {
            this.mCallCustomIDInfoList = new ArrayList();
        }
        this.mCallCustomIDInfoList.clear();
        this.mCallCustomIDInfoList.addAll(this.mLedCoverDbAccessor.getCustomIconInfo());
        LCoverSingleton.getInstance().setCustomLEDList(this.mCallCustomIDInfoList);
        int iconCount = this.mCallCustomIDInfoList.size();
        if (iconCount > 0) {
            if (this.mLCoverCallCustomIconListAdapter == null) {
                this.mLCoverCallCustomIconListAdapter = new LCoverCallIconListAdapter(this, this.mCallCustomIDInfoList);
            }
            this.gv_customCallList.setAdapter(this.mLCoverCallCustomIconListAdapter);
            this.gv_customCallList.setExpanded(true);
            this.gv_customCallList.setEnabled(true);
            this.gv_customCallList.setParents(this, this.mScroll, Defines.MENU_CALL);
            this.gv_customCallList.setOnItemClickListener(this.customListOnItemClick);
            this.gv_customCallList.setOnItemLongClickListener(this.customListOnItemLongClick);
            this.mLCoverCallCustomIconListAdapter.setOnClickListener(this.callBack);
            if (this.isChangeMode == MENU_EDIT) {
                this.mLCoverCallCustomIconListAdapter.setIsChangeMode(true);
            }
            this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
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
        if (this.isChangeMode == MENU_EDIT) {
            actionbarTitle.setText(C0198R.string.menu_change);
        } else {
            actionbarTitle.setText(C0198R.string.led_caller_icons);
        }
        customView.findViewById(C0198R.id.actionbar_icon).setOnClickListener(new C02067());
        this.gv_presetCallList.setAlpha(1.0f);
        this.gv_presetCallList.setEnabled(true);
        if (this.isChangeMode == MENU_EDIT) {
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
        if (this.isChangeMode != MENU_EDIT) {
            menu.add(0, MENU_EDIT, 0, C0198R.string.menu_edit).setShowAsAction(MENU_EDIT);
            menu.add(0, MENU_REMOVE, 0, C0198R.string.menu_remove).setShowAsAction(MENU_EDIT);
            if (!(this.mEditMode.booleanValue() || this.mLCoverCallCustomIconListAdapter == null)) {
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
                Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY, Defines.SA_CALL_MAIN_ACTIVITY_EVENT_EDIT, "Edit");
                Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_SELECTION_MODE);
                this.mEditMode = Boolean.valueOf(true);
                setEditCustomActionbar();
                this.mScroll.scrollTo(this.customTopPositionX, this.customTopPositionY);
                break;
            case MENU_REMOVE /*2*/:
                Utils.sendEventSALog(Defines.SA_SCREEN_CALL_MAIN_SELECTION_MODE, Defines.SA_CALL_MAIN_SELECTION_MODE_EVENT_REMOVE, "Remove");
                Utils.sendScreenViewSALog(Defines.SA_SCREEN_CALL_MAIN_ACTIVITY);
                deleteIcons();
                break;
            case 16908332:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEditCustomActionbar() {
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
            if (this.mLCoverCallCustomIconListAdapter != null) {
                this.mLCoverCallCustomIconListAdapter.setEditClick(true);
                this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
            }
            this.mFloatingActionButton.setVisibility(8);
            this.gv_presetCallList.setAlpha(0.48f);
            this.gv_presetCallList.setEnabled(false);
            this.prePositionX = this.gv_presetCallList.getScollViewX();
            this.prePositionY = this.gv_presetCallList.getScollViewY();
            this.customTopPositionX = this.tv_divider.getLeft();
            this.customTopPositionY = this.tv_divider.getTop();
        }
    }

    private void setSelectedIconCount() {
        int checkedAppCount = this.mLCoverCallCustomIconListAdapter.getItemCheckedCount();
        int listAppCount = this.mLCoverCallCustomIconListAdapter.getCount();
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
        temp.addAll(this.mCallCustomIDInfoList);
        SLog.m12v(TAG, "Array Size : " + size);
        ArrayList<View> viewList = new ArrayList();
        for (i = 0; i < temp.size(); i += MENU_EDIT) {
            if (((LCoverIconInfo) temp.get(i)).getIsChecked().booleanValue()) {
                SLog.m12v(TAG, "DELETE CLICKED position : " + i + " isChecked? : " + ((LCoverIconInfo) temp.get(i)).getIsChecked());
                SLog.m12v(TAG, "DELETE CLICKED getId : " + ((LCoverIconInfo) temp.get(i)).getId());
                SLog.m12v(TAG, "DELETE CLICKED getName : " + ((LCoverIconInfo) temp.get(i)).getIconName());
                iconList.add(new Integer(((LCoverIconInfo) temp.get(i)).getId()));
                viewList.add(this.gv_customCallList.getChildAt(this.mCallCustomIDInfoList.indexOf(temp.get(i))));
            }
        }
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
            removeIdFromContactDB(whereArgs[i]);
        }
        if (this.mLedCoverDbAccessor.deleteLedIcons("icon_id IN (" + placeholders.toString() + ")", whereArgs)) {
            SLog.m12v(TAG, "listViewSize: " + this.gv_customCallList.getChildCount());
            SLog.m12v(TAG, "listItemSize: " + this.mCallCustomIDInfoList.size());
            for (i = 0; i < viewList.size(); i += MENU_EDIT) {
                this.gv_customCallList.removeViewInLayout((View) viewList.get(i));
                SLog.m12v(TAG, "listViewSize: " + this.gv_customCallList.getChildCount());
            }
            this.mLCoverCallCustomIconListAdapter.setEditClick(false);
            this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
            viewList.clear();
        } else {
            SLog.m12v(TAG, "deleteLedIcons Failed!");
        }
        makeCustomCallIDList();
        checkCallerIDContactCount();
        setCustomActionbar();
        MenuItem editMenu = this.mMenu.findItem(MENU_EDIT);
        MenuItem removeMenu = this.mMenu.findItem(MENU_REMOVE);
        if (removeMenu != null) {
            removeMenu.setVisible(false);
        }
        if (this.mCallCustomIDInfoList.size() == 0) {
            this.tv_divider.setVisibility(8);
            if (editMenu != null) {
                editMenu.setVisible(false);
            }
        } else if (editMenu != null) {
            editMenu.setVisible(true);
        }
    }

    private void checkCallerIDContactCount() {
        if (this.mCallPresetIDInfoList == null) {
            SLog.m12v(TAG, "checkCallerIDContactCount, mCallerIDInfoList is null");
            return;
        }
        int preloadListSize = this.mCallPresetIDInfoList.size();
        if (preloadListSize == 0) {
            SLog.m12v(TAG, "checkCallerIDContactCount, mCallerIDInfoList size is 0");
            return;
        }
        int i;
        HashMap<Integer, Integer> ListIndexByCallerIDIndex = new HashMap();
        for (i = 0; i < preloadListSize; i += MENU_EDIT) {
            ((LCoverIconInfo) this.mCallPresetIDInfoList.get(i)).resetCallerIDCount();
            ListIndexByCallerIDIndex.put(Integer.valueOf(((LCoverIconInfo) this.mCallPresetIDInfoList.get(i)).mId), Integer.valueOf(i));
        }
        if (this.mCallCustomIDInfoList.size() > 0) {
            for (i = 0; i < this.mCallCustomIDInfoList.size(); i += MENU_EDIT) {
                ((LCoverIconInfo) this.mCallCustomIDInfoList.get(i)).resetCallerIDCount();
                ListIndexByCallerIDIndex.put(Integer.valueOf(((LCoverIconInfo) this.mCallCustomIDInfoList.get(i)).mId), Integer.valueOf(i));
            }
        }
        Cursor c = null;
        try {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = Contacts.CONTENT_URI;
            String[] strArr = new String[MENU_REMOVE];
            strArr[0] = Defines.PKG_COL_KEY;
            strArr[MENU_EDIT] = "sec_led";
            c = contentResolver.query(uri, strArr, null, null, null);
            if (c != null) {
                int contactCount = c.getCount();
                SLog.m12v(TAG, "checkCallerIDCount() Count : " + contactCount);
                if (contactCount > 0) {
                    while (c.moveToNext()) {
                        String contact_id = c.getString(0);
                        String callerID = c.getString(MENU_EDIT);
                        if (!(callerID == null || callerID.equals(C0316a.f163d))) {
                            Integer listIndex = (Integer) ListIndexByCallerIDIndex.get(Integer.valueOf(Integer.parseInt(callerID)));
                            if (listIndex != null) {
                                if (Integer.parseInt(callerID) <= this.mCallPresetIDInfoList.size()) {
                                    ((LCoverIconInfo) this.mCallPresetIDInfoList.get(listIndex.intValue())).increaseCallerIDCount();
                                    SLog.m12v(TAG, "increaseCallerIDCount(preload) contact_id : " + contact_id + ", caller ID : " + callerID);
                                } else if (this.mCallCustomIDInfoList.size() > 0) {
                                    ((LCoverIconInfo) this.mCallCustomIDInfoList.get(listIndex.intValue())).increaseCallerIDCount();
                                    SLog.m12v(TAG, "increaseCallerIDCount(custom) contact_id : " + contact_id + ", caller ID : " + callerID);
                                }
                            }
                        }
                    }
                }
                if (this.mLCoverCallPresetIconListAdapter != null) {
                    this.mLCoverCallPresetIconListAdapter.notifyDataSetChanged();
                }
                if (this.mCallCustomIDInfoList.size() > 0 && this.mLCoverCallCustomIconListAdapter != null) {
                    this.mLCoverCallCustomIconListAdapter.notifyDataSetChanged();
                }
                c.close();
            }
        } catch (SQLiteException e) {
            SLog.m12v(TAG, "SQL Exception : " + e);
            c.close();
        }
    }

    private void removeIdFromContactDB(String id) {
        SLog.m12v(TAG, "removeIdFromContactDB id : " + id);
        Cursor c = null;
        try {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = Contacts.CONTENT_URI;
            String[] strArr = new String[MENU_REMOVE];
            strArr[0] = Defines.PKG_COL_KEY;
            strArr[MENU_EDIT] = "sec_led";
            c = contentResolver.query(uri, strArr, null, null, null);
            if (c != null) {
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        String contact_id = c.getString(0);
                        String callerID = c.getString(MENU_EDIT);
                        if (callerID != null && callerID.equals(id)) {
                            StringBuffer selection = new StringBuffer();
                            selection.append("_id IN (");
                            selection.append("'");
                            selection.append(contact_id);
                            selection.append("'");
                            selection.append(")");
                            ContentValues v = new ContentValues();
                            SLog.m12v(TAG, "removeIdFromContactDB() [REMOVE] id : " + callerID + "  val : " + selection.toString());
                            v.put("sec_led", C0316a.f163d);
                            try {
                                getContentResolver().update(Contacts.CONTENT_URI, v, selection.toString(), null);
                            } catch (SQLiteException e) {
                                SLog.m12v(TAG, "SQL Exception : " + e);
                            }
                        }
                        SLog.m12v(TAG, "removeIdFromContactDB  contact_id : " + contact_id + " callerID : " + callerID);
                    }
                }
                c.close();
            }
        } catch (SQLiteException e2) {
            SLog.m12v(TAG, "SQL Exception : " + e2);
            c.close();
        }
    }
}
