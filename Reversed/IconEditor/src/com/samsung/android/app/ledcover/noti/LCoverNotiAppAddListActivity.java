package com.samsung.android.app.ledcover.noti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.adapter.LCoverNotiIconAppAddListAdapter;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.wrapperlibrary.ScrollViewWrapper;
import java.util.ArrayList;
import java.util.Iterator;

public class LCoverNotiAppAddListActivity extends Activity {
    public static final String TAG = "[LED_COVER]LCoverNotiAppAddListActivity";
    private View abView;
    private String icon_name;
    private int icon_reg_cnt;
    public boolean isSelectedAll;
    private ListView lv_notiApplist;
    private ArrayList<LCoverAppInfo> mCheckList;
    private Context mContext;
    private String[] mIconRegPkgNames;
    private ArrayList<LCoverAppInfo> mLCoverAppInfoList;
    private LCoverNotiIconAppAddListAdapter mLCoverNotiIconAppAddListAdapter;
    private LCoverNotiUtils mLCoverNotiUtils;
    private LCoverDbAccessor mNotiDbManager;
    private int mNotificationSelectedAppNum;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Menu mOptionsMenu;
    private CheckBox mSelectAllCB;
    private TextView mSelectedNum;
    private ArrayList<LCoverAppInfo> mUnCheckList;
    private int selected_id;

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppAddListActivity.1 */
    class C02491 implements OnClickListener {
        C02491() {
        }

        public void onClick(View v) {
            boolean z;
            SLog.m12v(LCoverNotiAppAddListActivity.TAG, "select all click.");
            LCoverNotiAppAddListActivity lCoverNotiAppAddListActivity = LCoverNotiAppAddListActivity.this;
            if (LCoverNotiAppAddListActivity.this.isSelectedAll) {
                z = false;
            } else {
                z = true;
            }
            lCoverNotiAppAddListActivity.isSelectedAll = z;
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_ADD_LIST_ACTIVITY, Defines.SA_NOTI_APP_ADD_LIST_EVENT_SELECT_ALL, "Select all", LCoverNotiAppAddListActivity.this.isSelectedAll ? 1 : 0);
            LCoverNotiAppAddListActivity.this.mSelectAllCB.setChecked(LCoverNotiAppAddListActivity.this.isSelectedAll);
            Iterator<LCoverAppInfo> ir;
            LCoverAppInfo app;
            if (LCoverNotiAppAddListActivity.this.isSelectedAll) {
                ir = LCoverNotiAppAddListActivity.this.mLCoverAppInfoList.iterator();
                while (ir.hasNext()) {
                    app = (LCoverAppInfo) ir.next();
                    SLog.m12v(LCoverNotiAppAddListActivity.TAG, "app pkg : " + app.getPackageName() + ", isChecked : " + app.getIsChecked());
                    if (!app.getIsChecked().booleanValue()) {
                        LCoverNotiAppAddListActivity.this.mCheckList.add(app);
                        LCoverNotiAppAddListActivity.this.mUnCheckList.remove(app);
                        app.setIsChecked(Boolean.valueOf(true));
                    }
                }
            } else {
                ir = LCoverNotiAppAddListActivity.this.mLCoverAppInfoList.iterator();
                while (ir.hasNext()) {
                    app = (LCoverAppInfo) ir.next();
                    SLog.m12v(LCoverNotiAppAddListActivity.TAG, "app pkg : " + app.getPackageName() + ", isChecked : " + app.getIsChecked());
                    if (app.getIsChecked().booleanValue()) {
                        LCoverNotiAppAddListActivity.this.mCheckList.remove(app);
                        LCoverNotiAppAddListActivity.this.mUnCheckList.add(app);
                        app.setIsChecked(Boolean.valueOf(false));
                    }
                }
            }
            LCoverNotiAppAddListActivity.this.mLCoverNotiIconAppAddListAdapter.notifyDataSetChanged();
            LCoverNotiAppAddListActivity.this.refreshSelectAllCheckBox();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppAddListActivity.2 */
    class C02502 implements OnItemClickListener {
        C02502() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long arg3) {
            SLog.m12v(LCoverNotiAppAddListActivity.TAG, "onItemClick");
            LCoverAppInfo app = LCoverNotiAppAddListActivity.this.mLCoverNotiIconAppAddListAdapter.getItem(position);
            boolean isChecked = app.getIsChecked().booleanValue();
            LCoverNotiAppAddListActivity.this.mLCoverNotiIconAppAddListAdapter.setItemChecked(position, !isChecked);
            if (isChecked) {
                LCoverNotiAppAddListActivity.this.mUnCheckList.add(app);
                LCoverNotiAppAddListActivity.this.mCheckList.remove(app);
            } else {
                LCoverNotiAppAddListActivity.this.mCheckList.add(app);
                LCoverNotiAppAddListActivity.this.mUnCheckList.remove(app);
            }
            LCoverNotiAppAddListActivity.this.refreshSelectAllCheckBox();
            LCoverNotiAppAddListActivity.this.mLCoverNotiIconAppAddListAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppAddListActivity.3 */
    class C02513 implements OnItemLongClickListener {
        C02513() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            SLog.m12v(LCoverNotiAppAddListActivity.TAG, "onItemLongClick");
            return false;
        }
    }

    public LCoverNotiAppAddListActivity() {
        this.mLCoverAppInfoList = null;
        this.mLCoverNotiUtils = null;
        this.mNotiDbManager = null;
        this.selected_id = 0;
        this.icon_reg_cnt = 0;
        this.mIconRegPkgNames = null;
        this.mOnItemClickListener = new C02502();
        this.mOnItemLongClickListener = new C02513();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.m12v(TAG, "onCreate");
        this.mContext = getApplicationContext();
        setContentView(C0198R.layout.activity_led_notification_list);
        Bundle bundle = getIntent().getExtras();
        this.selected_id = bundle.getInt("selected_id");
        this.icon_name = bundle.getString(Defines.ICON_COL_ICON_NAME);
        this.icon_reg_cnt = bundle.getInt("icon_reg_cnt");
        if (this.icon_reg_cnt > 0) {
            this.mIconRegPkgNames = bundle.getStringArray("icon_reg_pkg_names");
            for (int i = 0; i < this.icon_reg_cnt; i++) {
                SLog.m12v(TAG, "Registered PkgName : " + this.mIconRegPkgNames[i]);
            }
        }
        initActionBar();
        this.lv_notiApplist = (ListView) findViewById(C0198R.id.lv_icon_noti_list);
        this.mLCoverNotiUtils = LCoverSingleton.getInstance().getmLCoverNotiUtils(this.mContext);
        this.mCheckList = new ArrayList();
        this.mUnCheckList = new ArrayList();
        makeList();
        ScrollViewWrapper.semSetGoToTopEnabled(this.lv_notiApplist, true);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SLog.m12v(TAG, "onConfigurationChanged()");
        this.mLCoverNotiIconAppAddListAdapter = new LCoverNotiIconAppAddListAdapter(this, this.mLCoverAppInfoList, true);
        this.lv_notiApplist.setAdapter(this.mLCoverNotiIconAppAddListAdapter);
        initActionBar();
        refreshSelectAllCheckBox();
    }

    private void initActionBar() {
        getActionBar().setDisplayOptions(16);
        this.abView = LayoutInflater.from(this).inflate(C0198R.layout.led_cover_caller_id_selection_mode_actionbar, null);
        getActionBar().setCustomView(this.abView);
        this.mSelectedNum = (TextView) this.abView.findViewById(C0198R.id.number_selected_text);
        Utils.setLargeTextSize(this.mContext, this.mSelectedNum, (float) this.mContext.getResources().getDimensionPixelSize(C0198R.dimen.led_cover_main_abar_desc_textview_text_size));
        this.mSelectAllCB = (CheckBox) this.abView.findViewById(C0198R.id.toggle_selection_check);
        this.abView.findViewById(C0198R.id.select_all_layout).setOnClickListener(new C02491());
        Toolbar parent = (Toolbar) this.abView.getParent();
        if (parent != null) {
            parent.setContentInsetsAbsolute(0, 0);
        }
    }

    private void makeList() {
        SLog.m12v(TAG, "makeList");
        this.mLCoverAppInfoList = this.mLCoverNotiUtils.getInstalledAppList();
        this.mLCoverNotiIconAppAddListAdapter = new LCoverNotiIconAppAddListAdapter(this, this.mLCoverAppInfoList, true);
        this.lv_notiApplist.setAdapter(this.mLCoverNotiIconAppAddListAdapter);
        this.lv_notiApplist.setOnItemClickListener(this.mOnItemClickListener);
        this.lv_notiApplist.setOnItemLongClickListener(this.mOnItemLongClickListener);
    }

    protected void onResume() {
        super.onResume();
        Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_APP_ADD_LIST_ACTIVITY);
    }

    protected void onDestroy() {
        if (this.mCheckList != null) {
            this.mCheckList.clear();
            this.mCheckList = null;
        }
        if (this.mUnCheckList != null) {
            this.mUnCheckList.clear();
            this.mUnCheckList = null;
        }
        if (this.mLCoverAppInfoList != null) {
            this.mLCoverAppInfoList.clear();
            this.mLCoverAppInfoList = null;
        }
        SLog.m12v(TAG, "Clear Noti App Add List");
        Utils.recursiveRecycle(this.lv_notiApplist);
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 3, 0, C0198R.string.done2).setShowAsAction(1);
        this.mOptionsMenu = menu;
        refreshSelectAllCheckBox();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ConnectionResult.SERVICE_DISABLED /*3*/:
                SLog.m12v(TAG, "Done Menu Selected!!");
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_ADD_LIST_ACTIVITY, Defines.SA_NOTI_APP_ADD_LIST_EVENT_DONE, "Done");
                String dupRegAppName = null;
                String changeRegAppName = null;
                int dupRegAppCount = 0;
                int changeRegAppCount = 0;
                this.mNotiDbManager = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
                Iterator<LCoverAppInfo> it = this.mCheckList.iterator();
                ArrayList<String> pkgArray = new ArrayList();
                for (int i = 0; i < this.mNotificationSelectedAppNum; i++) {
                    boolean isRegPkg = false;
                    if (it.hasNext()) {
                        LCoverAppInfo appChecked = (LCoverAppInfo) it.next();
                        if (this.icon_reg_cnt > 0) {
                            int j = 0;
                            while (j < this.icon_reg_cnt) {
                                if (appChecked.getPackageName().equals(this.mIconRegPkgNames[j])) {
                                    isRegPkg = true;
                                    if (dupRegAppName == null) {
                                        dupRegAppName = appChecked.getAppName();
                                    }
                                    dupRegAppCount++;
                                } else {
                                    j++;
                                }
                            }
                        }
                        if (!isRegPkg) {
                            appChecked.setIconId(this.selected_id);
                            if (this.mNotiDbManager.addSelectedApps(appChecked) == 1) {
                                if (changeRegAppName == null) {
                                    changeRegAppName = appChecked.getAppName();
                                }
                                changeRegAppCount++;
                                pkgArray.add(appChecked.getPackageName());
                            }
                        }
                    }
                }
                if (this.mNotificationSelectedAppNum > 0) {
                    Intent result = new Intent(this, LCoverNotiAppListActivity.class);
                    result.putExtra("selected_id", this.selected_id);
                    result.putExtra(Defines.ICON_COL_ICON_NAME, this.icon_name);
                    result.putExtra("before_activity", 3);
                    result.putExtra("duplicated_app_count", dupRegAppCount);
                    result.putExtra("changed_app_count", changeRegAppCount);
                    if (dupRegAppCount > 0) {
                        result.putExtra("duplicated_app_name", dupRegAppName);
                    }
                    if (changeRegAppCount > 0) {
                        result.putExtra("changed_app_name", changeRegAppName);
                        String[] packages = (String[]) pkgArray.toArray(new String[changeRegAppCount]);
                        Intent bIntent = new Intent();
                        bIntent.setAction(Defines.BROADCAST_ACTION_APP_CHANGED_FROM_ICON);
                        bIntent.putExtra("packages", packages);
                        bIntent.putExtra("isChanged", true);
                        sendBroadcast(bIntent, Defines.PERMISSION_LCOVER_LAUNCH);
                    }
                    setResult(-1, result);
                    finish();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }

    private void refreshSelectAllCheckBox() {
        SLog.m12v(TAG, "refreshSelectAllCheckBox()");
        int checkedCount = this.mCheckList.size();
        int unCheckedCount = this.mUnCheckList.size();
        int totalAppCount = this.mLCoverAppInfoList.size();
        int totalCheckedAppCount = checkedCount;
        SLog.m12v(TAG, "totalApp : " + totalAppCount + " / totalCheckedApp : " + totalCheckedAppCount + " / totalUncheckedApp : " + unCheckedCount);
        if (totalAppCount != totalCheckedAppCount) {
            ((CheckBox) findViewById(C0198R.id.toggle_selection_check)).setChecked(false);
            this.isSelectedAll = false;
        } else {
            ((CheckBox) findViewById(C0198R.id.toggle_selection_check)).setChecked(true);
            this.isSelectedAll = true;
        }
        if (totalCheckedAppCount == 0) {
            this.mSelectedNum.setText(C0198R.string.notification_select_apps);
        } else {
            this.mSelectedNum.setText(getString(C0198R.string.selected_numberof_fingerprint, new Object[]{Integer.valueOf(totalCheckedAppCount)}));
        }
        this.mNotificationSelectedAppNum = totalCheckedAppCount;
        updateActionbarState();
    }

    public void updateActionbarState() {
        SLog.m12v(TAG, "updateActionbarState checked: " + this.mCheckList.size());
        if (this.mCheckList.size() == 0) {
            this.mOptionsMenu.findItem(3).setVisible(false);
        } else {
            this.mOptionsMenu.findItem(3).setVisible(true);
        }
    }
}
