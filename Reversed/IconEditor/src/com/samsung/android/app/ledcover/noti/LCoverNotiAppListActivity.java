package com.samsung.android.app.ledcover.noti;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.adapter.LCoverNotiIconAppListAdapter;
import com.samsung.android.app.ledcover.app.LCoverNotiMainActivity;
import com.samsung.android.app.ledcover.call.SineInOut70;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.creationpattern.LedMatrixAdapter;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.wrapperlibrary.ScrollViewWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class LCoverNotiAppListActivity extends Activity {
    public static final String TAG = "[LED_COVER]LCoverNotiAppListActivity";
    private View addAppItem;
    private OnClickListener addApplicationListener;
    private GridView gvIconMatrix;
    private TextView hasAppDescription;
    private View hasApplicationFooterView;
    private ImageView icon;
    public boolean isSelectedAll;
    private ActionBar mActionBar;
    private ArrayList<LCoverAppInfo> mAppInfoList;
    private ArrayList<LCoverAppInfo> mCheckList;
    private Context mContext;
    private TextView mDescTextView;
    private int mIconId;
    private String mIconName;
    private LCoverNotiIconAppListAdapter mIconNotiListAdapter;
    private int mIconResourceName;
    private boolean mIsInEditMode;
    private ListView mNotiAppListView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Menu mOptionsMenu;
    private View mSelectActionview;
    private CheckBox mSelectAllCB;
    private TextView mSelectedNum;
    private View noApplicationFooterView;

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity.1 */
    class C02521 implements OnClickListener {
        C02521() {
        }

        public void onClick(View v) {
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY, Defines.SA_NOTI_APP_LIST_EVENT_ADD, "Add");
            int iconRegAppSize = LCoverNotiAppListActivity.this.mAppInfoList.size();
            Intent result = new Intent(LCoverNotiAppListActivity.this, LCoverNotiAppAddListActivity.class);
            String[] iconRegPkgNames = new String[iconRegAppSize];
            result.putExtra("selected_id", LCoverNotiAppListActivity.this.mIconId);
            result.putExtra(Defines.ICON_COL_ICON_NAME, LCoverNotiAppListActivity.this.mIconName);
            result.putExtra("icon_reg_cnt", iconRegAppSize);
            for (int i = 0; i < iconRegAppSize; i++) {
                iconRegPkgNames[i] = ((LCoverAppInfo) LCoverNotiAppListActivity.this.mAppInfoList.get(i)).getPackageName();
            }
            result.putExtra("icon_reg_pkg_names", iconRegPkgNames);
            try {
                LCoverNotiAppListActivity.this.startActivityForResult(result, 2);
            } catch (ActivityNotFoundException e) {
                SLog.m12v(LCoverNotiAppListActivity.TAG, "Activity Not Found");
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity.2 */
    class C02532 implements Comparator<LCoverAppInfo> {
        C02532() {
        }

        public int compare(LCoverAppInfo o1, LCoverAppInfo o2) {
            if (o1.getAppName() == null) {
                return -1;
            }
            if (o2.getAppName() == null) {
                return 1;
            }
            return o1.getAppName().compareToIgnoreCase(o2.getAppName());
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity.3 */
    class C02543 implements OnClickListener {
        C02543() {
        }

        public void onClick(View arg0) {
            boolean z;
            SLog.m12v(LCoverNotiAppListActivity.TAG, "select all click.");
            LCoverNotiAppListActivity lCoverNotiAppListActivity = LCoverNotiAppListActivity.this;
            if (LCoverNotiAppListActivity.this.isSelectedAll) {
                z = false;
            } else {
                z = true;
            }
            lCoverNotiAppListActivity.isSelectedAll = z;
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE, Defines.SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_SELECT_ALL, "Select all", LCoverNotiAppListActivity.this.isSelectedAll ? 1 : 0);
            LCoverNotiAppListActivity.this.mSelectAllCB.setChecked(LCoverNotiAppListActivity.this.isSelectedAll);
            Iterator<LCoverAppInfo> ir;
            LCoverAppInfo app;
            if (LCoverNotiAppListActivity.this.isSelectedAll) {
                ir = LCoverNotiAppListActivity.this.mAppInfoList.iterator();
                while (ir.hasNext()) {
                    app = (LCoverAppInfo) ir.next();
                    SLog.m12v(LCoverNotiAppListActivity.TAG, "Name : " + app.getAppName() + ", isChecked : " + app.getIsChecked());
                    if (!app.getIsChecked().booleanValue()) {
                        LCoverNotiAppListActivity.this.mCheckList.add(app);
                        app.setIsChecked(Boolean.valueOf(true));
                    }
                }
            } else {
                ir = LCoverNotiAppListActivity.this.mAppInfoList.iterator();
                while (ir.hasNext()) {
                    app = (LCoverAppInfo) ir.next();
                    SLog.m12v(LCoverNotiAppListActivity.TAG, "Name : " + app.getAppName() + ", isChecked : " + app.getIsChecked());
                    if (app.getIsChecked().booleanValue()) {
                        LCoverNotiAppListActivity.this.mCheckList.remove(app);
                        app.setIsChecked(Boolean.valueOf(false));
                    }
                }
            }
            LCoverNotiAppListActivity.this.updateMenuItem();
            LCoverNotiAppListActivity.this.mIconNotiListAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity.4 */
    class C02554 implements OnItemClickListener {
        C02554() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long arg3) {
            SLog.m12v(LCoverNotiAppListActivity.TAG, "onItemClick");
            if (LCoverNotiAppListActivity.this.mIsInEditMode) {
                boolean z;
                LCoverAppInfo app = LCoverNotiAppListActivity.this.mIconNotiListAdapter.getItem(position);
                boolean isChecked = app.getIsChecked().booleanValue();
                if (isChecked) {
                    LCoverNotiAppListActivity.this.mCheckList.remove(app);
                } else {
                    LCoverNotiAppListActivity.this.mCheckList.add(app);
                }
                if (isChecked) {
                    z = false;
                } else {
                    z = true;
                }
                app.setIsChecked(Boolean.valueOf(z));
                LCoverNotiAppListActivity.this.mIconNotiListAdapter.notifyDataSetChanged();
                if (LCoverNotiAppListActivity.this.mCheckList.size() != LCoverNotiAppListActivity.this.mAppInfoList.size()) {
                    LCoverNotiAppListActivity.this.mSelectAllCB.setChecked(false);
                    LCoverNotiAppListActivity.this.isSelectedAll = false;
                } else {
                    LCoverNotiAppListActivity.this.mSelectAllCB.setChecked(true);
                    LCoverNotiAppListActivity.this.isSelectedAll = true;
                }
                LCoverNotiAppListActivity.this.updateMenuItem();
                return;
            }
            SLog.m12v(LCoverNotiAppListActivity.TAG, "Not in edit mode : Item click event ignored!");
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.noti.LCoverNotiAppListActivity.5 */
    class C02565 implements OnItemLongClickListener {
        C02565() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position >= LCoverNotiAppListActivity.this.mIconNotiListAdapter.getCount()) {
                return false;
            }
            LCoverAppInfo app = LCoverNotiAppListActivity.this.mIconNotiListAdapter.getItem(position);
            if (LCoverNotiAppListActivity.this.mIsInEditMode) {
                return false;
            }
            SLog.m12v(LCoverNotiAppListActivity.TAG, "onItemLongClick setEditMode true ");
            Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY, Defines.SA_NOTI_APP_LIST_EVENT_SELECTION_MODE, "Selection mode");
            app.setIsChecked(Boolean.valueOf(true));
            LCoverNotiAppListActivity.this.mIconNotiListAdapter.notifyDataSetChanged();
            LCoverNotiAppListActivity.this.mCheckList.add(app);
            LCoverNotiAppListActivity.this.setEditMode(true);
            return true;
        }
    }

    public LCoverNotiAppListActivity() {
        this.mAppInfoList = null;
        this.mCheckList = null;
        this.mIconId = 0;
        this.mIconResourceName = 0;
        this.mIsInEditMode = false;
        this.isSelectedAll = false;
        this.mSelectAllCB = null;
        this.mDescTextView = null;
        this.hasAppDescription = null;
        this.mSelectedNum = null;
        this.mSelectActionview = null;
        this.mOnItemClickListener = new C02554();
        this.mOnItemLongClickListener = new C02565();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.m12v(TAG, "onCreate");
        this.mContext = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mIconId = bundle.getInt("selected_id");
            this.mIconName = bundle.getString(Defines.ICON_COL_ICON_NAME);
            SLog.m12v(TAG, "onActivityCreated() with Icon ID : " + this.mIconId + ", Icon name : " + this.mIconName);
        }
        initView();
        this.mCheckList = new ArrayList();
        this.mAppInfoList = getIconRegistedAppList();
        makeList();
        this.mActionBar = getActionBar();
        updateActionBar();
        ScrollViewWrapper.semSetGoToTopEnabled(this.mNotiAppListView, true);
    }

    private void initView() {
        setContentView(C0198R.layout.led_cover_caller_id_list);
        this.mNotiAppListView = (ListView) findViewById(C0198R.id.lv_contact);
        TypedArray name_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_string_entries);
        TypedArray icon_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_image_entries);
        TypedArray index_array = getResources().obtainTypedArray(C0198R.array.ledcover_preset_index_entries);
        boolean isTemplateIcon = false;
        int templateIndex = 0;
        for (int index = 0; index < index_array.length(); index++) {
            if (String.valueOf(this.mIconId).equals(index_array.getString(index))) {
                isTemplateIcon = true;
                templateIndex = index;
                this.mIconResourceName = name_array.getResourceId(index, 0);
                break;
            }
        }
        this.icon = (ImageView) findViewById(C0198R.id.iv_icon_image);
        this.gvIconMatrix = (GridView) findViewById(C0198R.id.pattern_drawlayout);
        if (isTemplateIcon) {
            this.gvIconMatrix.setVisibility(8);
            this.icon.setImageResource(new LCoverIconInfo(index_array.getInt(templateIndex, 0), icon_array.getResourceId(templateIndex, 0), name_array.getString(templateIndex), 0).getIconArrayInt());
        } else {
            this.icon.setVisibility(8);
            LCoverIconInfo iconInfo = LCoverSingleton.getInstance().getCustomLEDList(this.mIconId);
            if (iconInfo != null) {
                LCoverSingleton.getInstance().LoadingCustomIconDataBase(iconInfo);
                this.gvIconMatrix.setVerticalScrollBarEnabled(false);
                LedMatrixAdapter ledMatrixAdapter = new LedMatrixAdapter(this, iconInfo, true);
                this.gvIconMatrix.invalidateViews();
                this.gvIconMatrix.setAdapter(ledMatrixAdapter);
            }
        }
        name_array.recycle();
        icon_array.recycle();
        index_array.recycle();
        this.hasApplicationFooterView = LayoutInflater.from(this).inflate(C0198R.layout.led_noti_list_has_item_footer, null);
        this.hasAppDescription = (TextView) this.hasApplicationFooterView.findViewById(C0198R.id.tv_has_item_description);
        this.noApplicationFooterView = findViewById(C0198R.id.ln_no_item);
        ((TextView) this.noApplicationFooterView.findViewById(C0198R.id.tv_no_item_description)).setText(getString(C0198R.string.noti_add_application_message));
        this.mDescTextView = (TextView) this.hasApplicationFooterView.findViewById(C0198R.id.tv_contact_count);
        Button addApp = (Button) this.noApplicationFooterView.findViewById(C0198R.id.btn_add_contact);
        addApp.setText(getString(C0198R.string.ts_add_application_button_abb));
        this.addApplicationListener = new C02521();
        addApp.setOnClickListener(this.addApplicationListener);
        this.addAppItem = LayoutInflater.from(this).inflate(C0198R.layout.activity_led_noti_add_app_list_footer, null);
        ((ImageView) this.addAppItem.findViewById(C0198R.id.img_create_icon)).setColorFilter(getColor(C0198R.color.create_icon_color), Mode.SRC_ATOP);
        ((TextView) this.addAppItem.findViewById(C0198R.id.caller_id_name)).setText(C0198R.string.ts_add_application_button_abb);
        this.addAppItem.setOnClickListener(this.addApplicationListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        SLog.m12v(TAG, "onCreateOptionsMenu");
        this.mOptionsMenu = menu;
        this.mOptionsMenu.clear();
        menu.add(0, 4, 0, C0198R.string.menu_edit).setShowAsAction(1);
        menu.add(0, 2, 0, C0198R.string.menu_change).setShowAsAction(1);
        menu.add(0, 5, 0, C0198R.string.menu_remove).setShowAsAction(1);
        updateMenuItem();
        return true;
    }

    protected void onResume() {
        super.onResume();
        SLog.m12v(TAG, "onResume ");
        if (this.mIsInEditMode) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initView();
        createEditModeActionBar();
        makeList();
        if (this.mIsInEditMode) {
            showCheckBox();
            showEditModeActionBar();
            updateMenuItem();
        }
    }

    protected void onDestroy() {
        SLog.m12v(TAG, "onDestroy");
        if (this.mAppInfoList != null) {
            this.mAppInfoList.clear();
            this.mAppInfoList = null;
        }
        if (this.mCheckList != null) {
            this.mCheckList.clear();
            this.mCheckList = null;
        }
        SLog.m12v(TAG, "Clear Noti App List");
        Utils.recursiveRecycle(this.mNotiAppListView);
        super.onDestroy();
    }

    public ArrayList<LCoverAppInfo> getIconRegistedAppList() {
        SLog.m12v(TAG, "getIconRegistedAppList(), Icon Id = " + this.mIconId);
        if (this.mAppInfoList == null) {
            this.mAppInfoList = new ArrayList();
        } else {
            this.mAppInfoList.clear();
        }
        ArrayList<LCoverAppInfo> mAppSelectInfoList = new ArrayList();
        Iterator it = LCoverSingleton.getInstance().getDbAccessor(this.mContext).getSelectedAppsInfo().iterator();
        while (it.hasNext()) {
            LCoverAppInfo appInfo = (LCoverAppInfo) it.next();
            if (appInfo.getIconId() == this.mIconId) {
                try {
                    ApplicationInfo app = getPackageManager().getApplicationInfo(appInfo.getPackageName(), 0);
                    Drawable appIcon = getPackageManager().getApplicationIcon(app);
                    String appLabel = getPackageManager().getApplicationLabel(app).toString();
                    appInfo.setAppIcon(appIcon);
                    appInfo.setAppName(appLabel);
                } catch (NameNotFoundException e) {
                    SLog.m12v(TAG, "Application Not Found : " + appInfo.getPackageName());
                }
                ArrayList arrayList = this.mAppInfoList;
                r17.add(new LCoverAppInfo(appInfo.getId(), appInfo.getPackageName(), appInfo.getAppIcon(), appInfo.getAppName(), appInfo.getIsChecked().booleanValue(), appInfo.getIconId(), appInfo.getIconArray()));
                SLog.m12v(TAG, "app count: " + this.mAppInfoList.size());
            }
        }
        Collections.sort(this.mAppInfoList, new C02532());
        return this.mAppInfoList;
    }

    private void makeList() {
        SLog.m12v(TAG, "Create app list");
        this.mIconNotiListAdapter = new LCoverNotiIconAppListAdapter(this, this.mAppInfoList, true);
        this.mNotiAppListView.setAdapter(this.mIconNotiListAdapter);
        this.mNotiAppListView.setOnItemClickListener(this.mOnItemClickListener);
        this.mNotiAppListView.setOnItemLongClickListener(this.mOnItemLongClickListener);
        updateListDescription();
        LayoutParams params = new LayoutParams(-1, -2);
        params.setMarginStart((int) getResources().getDimension(C0198R.dimen.check_list_margin));
        this.mNotiAppListView.setLayoutParams(params);
    }

    private void updateActionBar() {
        SLog.m12v(TAG, "updateActionBar");
        if (this.mIsInEditMode) {
            this.mActionBar.setDisplayHomeAsUpEnabled(false);
            this.mActionBar.setHomeButtonEnabled(false);
            this.mActionBar.setDisplayShowCustomEnabled(true);
            showEditModeActionBar();
            return;
        }
        this.mActionBar.setDisplayShowCustomEnabled(true);
        this.mActionBar.setDisplayHomeAsUpEnabled(true);
        this.mActionBar.setDisplayShowTitleEnabled(true);
        this.mActionBar.setHomeButtonEnabled(true);
        this.mActionBar.setDisplayShowCustomEnabled(false);
        if (this.mIconResourceName > 0) {
            this.mActionBar.setTitle(this.mIconResourceName);
        } else {
            this.mActionBar.setTitle(this.mIconName);
        }
    }

    private void createEditModeActionBar() {
        SLog.m12v(TAG, "createEditModeActionBar");
        this.mSelectActionview = LayoutInflater.from(this).inflate(C0198R.layout.led_cover_caller_id_selection_mode_actionbar, null);
        this.mSelectedNum = (TextView) this.mSelectActionview.findViewById(C0198R.id.number_selected_text);
        Utils.setLargeTextSize(this.mContext, this.mSelectedNum, (float) this.mContext.getResources().getDimensionPixelSize(C0198R.dimen.led_cover_main_abar_desc_textview_text_size));
        this.mSelectAllCB = (CheckBox) this.mSelectActionview.findViewById(C0198R.id.toggle_selection_check);
        this.mSelectActionview.findViewById(C0198R.id.select_all_layout).setOnClickListener(new C02543());
    }

    private void showEditModeActionBar() {
        boolean z = false;
        SLog.m12v(TAG, "showEditModeActionBar");
        if (this.mSelectActionview == null) {
            createEditModeActionBar();
        }
        this.mActionBar.setCustomView(this.mSelectActionview, new ActionBar.LayoutParams(-1, -1, 16));
        ((Toolbar) this.mSelectActionview.getParent()).setContentInsetsAbsolute(0, 0);
        if (this.mCheckList.size() == this.mAppInfoList.size()) {
            z = true;
        }
        this.isSelectedAll = z;
        this.mSelectAllCB.setChecked(this.isSelectedAll);
    }

    private void updateMenuItem() {
        if (this.mIsInEditMode) {
            this.mOptionsMenu.findItem(4).setVisible(false);
            if (this.mCheckList.size() == 0) {
                this.mSelectedNum.setText(C0198R.string.notification_select_apps);
                this.mOptionsMenu.findItem(2).setVisible(false);
                this.mOptionsMenu.findItem(5).setVisible(false);
                return;
            }
            this.mSelectedNum.setText(getString(C0198R.string.selected_numberof_fingerprint, new Object[]{Integer.valueOf(checkedCount)}));
            this.mOptionsMenu.findItem(2).setVisible(true);
            this.mOptionsMenu.findItem(5).setVisible(true);
            return;
        }
        this.mOptionsMenu.findItem(2).setVisible(false);
        this.mOptionsMenu.findItem(5).setVisible(false);
        if (this.mAppInfoList == null || this.mAppInfoList.size() <= 0) {
            this.mOptionsMenu.findItem(4).setVisible(false);
        } else {
            this.mOptionsMenu.findItem(4).setVisible(true);
        }
    }

    private void updateListDescription() {
        SLog.m12v(TAG, "updateListDescription, Edit mode: " + this.mIsInEditMode);
        if (this.mIsInEditMode) {
            this.mNotiAppListView.removeFooterView(this.hasApplicationFooterView);
            this.mNotiAppListView.removeFooterView(this.addAppItem);
            this.noApplicationFooterView.setVisibility(8);
            return;
        }
        int appCount = this.mAppInfoList.size();
        if (appCount == 0) {
            if (this.mNotiAppListView.getFooterViewsCount() > 0) {
                this.mNotiAppListView.removeFooterView(this.addAppItem);
                this.mNotiAppListView.removeFooterView(this.hasApplicationFooterView);
            }
            this.noApplicationFooterView.setVisibility(0);
            return;
        }
        this.noApplicationFooterView.setVisibility(8);
        boolean isChanged = true;
        if (this.mNotiAppListView.getFooterViewsCount() == 0) {
            this.mNotiAppListView.addFooterView(this.addAppItem);
            this.mNotiAppListView.addFooterView(this.hasApplicationFooterView, null, false);
            isChanged = false;
        }
        if (isChanged) {
            this.mIconNotiListAdapter.notifyDataSetChanged();
        }
        if (appCount == 1) {
            this.mDescTextView.setText(C0198R.string.notification_list_desc_one_app);
            this.hasAppDescription.setText(getString(C0198R.string.noti_has_application_message));
            return;
        }
        this.hasAppDescription.setText(getString(C0198R.string.f5x2b91a502));
        this.mDescTextView.setText(String.format(getString(C0198R.string.notification_list_desc_apps), new Object[]{Integer.valueOf(appCount)}));
    }

    private void setEditMode(boolean isInEditMode) {
        if (isInEditMode) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY);
        }
        if (isInEditMode && !this.mIsInEditMode) {
            showCheckBox();
            this.mIsInEditMode = true;
        }
        if (!isInEditMode && this.mIsInEditMode) {
            hideCheckBox();
            this.mIsInEditMode = false;
        }
        updateActionBar();
        updateMenuItem();
        updateListDescription();
    }

    private void finishEditMode() {
        SLog.m12v(TAG, "finishEditMode");
        cleanUpFloatableCheckboxList();
        setEditMode(false);
        if (this.mAppInfoList.size() == 0) {
            finish();
        }
    }

    private void showCheckBox() {
        SLog.m12v(TAG, "showCheckBox()");
        LayoutParams params = (LayoutParams) this.mNotiAppListView.getLayoutParams();
        params.setMarginEnd((int) TypedValue.applyDimension(1, 50.0f, getResources().getDisplayMetrics()));
        this.mNotiAppListView.setLayoutParams(params);
        this.mNotiAppListView.animate().translationX((float) (((int) getResources().getDimension(C0198R.dimen.check_list_anim_margin)) * getResources().getInteger(C0198R.integer.direction_value))).setInterpolator(new SineInOut70());
        this.mNotiAppListView.setDivider(getDrawable(C0198R.drawable.list_border_with_checkbox));
    }

    private void hideCheckBox() {
        SLog.m12v(TAG, "hideCheckBox()");
        LayoutParams params = (LayoutParams) this.mNotiAppListView.getLayoutParams();
        params.setMarginEnd(0);
        this.mNotiAppListView.setLayoutParams(params);
        this.mNotiAppListView.animate().translationX(0.0f).setInterpolator(new SineInOut70());
        this.mNotiAppListView.setDivider(getDrawable(C0198R.drawable.list_border));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        SLog.m12v(TAG, "onActivityResult");
        if (requestCode == 1 || requestCode == 2) {
            if (intent != null && resultCode == -1) {
                Bundle bundle = intent.getExtras();
                int dupAppCount = bundle.getInt("duplicated_app_count");
                int changeAppCount = bundle.getInt("changed_app_count");
                boolean isDupChange = false;
                String dupMsg = new String();
                String changeMsg = new String();
                String toastMsg = new String();
                if (dupAppCount > 0) {
                    if (dupAppCount == 1) {
                        String duplicatedAppName = bundle.getString("duplicated_app_name");
                        dupMsg = String.format(getString(C0198R.string.notification_duplicated_one), new Object[]{duplicatedAppName});
                    } else {
                        isDupChange = true;
                    }
                }
                if (changeAppCount > 0) {
                    if (changeAppCount == 1) {
                        String changedAppName = bundle.getString("changed_app_name");
                        changeMsg = String.format(getString(C0198R.string.notification_changed_one), new Object[]{changedAppName});
                    } else if (dupAppCount > 1 || changeAppCount < 2) {
                        isDupChange = true;
                    } else {
                        changeMsg = String.format(getString(C0198R.string.notification_changed_two), new Object[]{Integer.valueOf(changeAppCount)});
                    }
                }
                if (isDupChange) {
                    toastMsg = String.format(getString(C0198R.string.notification_duplicated_changed), new Object[]{Integer.valueOf(dupAppCount), Integer.valueOf(changeAppCount)});
                } else if (dupMsg.length() > 0 && changeMsg.length() == 0) {
                    toastMsg = dupMsg;
                } else if (dupMsg.length() == 0 && changeMsg.length() > 0) {
                    toastMsg = changeMsg;
                } else if (dupMsg.length() > 0 && changeMsg.length() > 0) {
                    toastMsg = dupMsg + "\n" + changeMsg;
                }
                if (toastMsg.length() > 0) {
                    Toast.makeText(this, toastMsg, 0).show();
                }
                getIconRegistedAppList();
                updateListDescription();
                updateMenuItem();
            }
        } else if (requestCode == 3 && resultCode == -1) {
            int ret_icon_id = intent.getIntExtra("selected_id", 0);
            String ret_icon_name = intent.getStringExtra(Defines.ICON_COL_ICON_NAME);
            SLog.m12v(TAG, "Select icon_id : " + ret_icon_id + ", icon_name : " + ret_icon_name);
            if (ret_icon_id > 0) {
                LCoverDbAccessor dbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
                Iterator<LCoverAppInfo> it = this.mCheckList.iterator();
                int checkedCnt = getNumOfCheckedList();
                String[] packages = new String[checkedCnt];
                for (int i = 0; i < checkedCnt; i++) {
                    if (it.hasNext()) {
                        String pkgName = ((LCoverAppInfo) it.next()).getPackageName();
                        dbAccessor.updateSelectedApps(pkgName, ret_icon_id);
                        packages[i] = pkgName;
                    }
                }
                Toast.makeText(this, getString(C0198R.string.notification_id_changed_edit), 0).show();
                Intent bIntent = new Intent();
                bIntent.setAction(Defines.BROADCAST_ACTION_APP_CHANGED_FROM_ICON);
                bIntent.putExtra("packages", packages);
                bIntent.putExtra("isChanged", true);
                sendBroadcast(bIntent, Defines.PERMISSION_LCOVER_LAUNCH);
                Intent intent2 = new Intent(this, LCoverNotiAppListActivity.class);
                intent2.putExtra("selected_id", ret_icon_id);
                intent2.putExtra(Defines.ICON_COL_ICON_NAME, ret_icon_name);
                intent2.putExtra("before_activity", 4);
                startActivity(intent2);
                finish();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED /*2*/:
                SLog.m12v(TAG, "onOptionsItemSelected() action [CHANGE]");
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE, Defines.SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_CHANGE, "Change");
                Intent intent = new Intent(this, LCoverNotiMainActivity.class);
                intent.putExtra("before_activity", 4);
                startActivityForResult(intent, 3);
                break;
            case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                SLog.m12v(TAG, "onOptionsItemSelected() action [EDIT]");
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY, Defines.SA_NOTI_APP_LIST_EVENT_EDIT, "Edit");
                setEditMode(true);
                break;
            case ConnectionResult.INVALID_ACCOUNT /*5*/:
                SLog.m12v(TAG, "onOptionsItemSelected() action [REMOVE]");
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_SELECTION_MODE, Defines.SA_NOTI_APP_LIST_SELECTION_MODE_EVENT_REMOVE, "Remove");
                StringBuilder placeholders = new StringBuilder();
                int checkedCnt = getNumOfCheckedList();
                String[] whereArgs = new String[checkedCnt];
                Iterator<LCoverAppInfo> it = this.mCheckList.iterator();
                for (int i = 0; i < checkedCnt; i++) {
                    if (it.hasNext()) {
                        if (i != 0) {
                            placeholders.append(", ");
                        }
                        placeholders.append("?");
                        whereArgs[i] = ((LCoverAppInfo) it.next()).getPackageName();
                        SLog.m12v(TAG, "Selected pkgName : " + whereArgs[i]);
                    }
                }
                String where = "name IN (" + placeholders.toString() + ") " + "and " + "icon_index=" + this.mIconId;
                SLog.m12v(TAG, where);
                if (LCoverSingleton.getInstance().getDbAccessor(this.mContext).deleteSelectedApps(where, whereArgs)) {
                    SLog.m12v(TAG, "deleteSelectedApps");
                    Intent bIntent = new Intent();
                    bIntent.setAction(Defines.BROADCAST_ACTION_APP_CHANGED_FROM_ICON);
                    bIntent.putExtra("packages", whereArgs);
                    bIntent.putExtra("isChanged", false);
                    sendBroadcast(bIntent, Defines.PERMISSION_LCOVER_LAUNCH);
                    this.mIconNotiListAdapter.removeItems(this.mCheckList);
                    finishEditMode();
                    break;
                }
                break;
            case 16908332:
                SLog.m12v(TAG, "onOptionsItemSelected() action [home]");
                Utils.sendEventSALog(Defines.SA_SCREEN_NOTI_APP_LIST_ACTIVITY, Defines.SA_NOTI_APP_LIST_EVENT_UP_BUTTON, "Up button");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        SLog.m12v(TAG, "onBackPressed!!");
        if (this.mIsInEditMode) {
            finishEditMode();
        } else {
            finish();
        }
    }

    private int getNumOfCheckedList() {
        return this.mCheckList.size();
    }

    private void cleanUpFloatableCheckboxList() {
        SLog.m12v(TAG, "cleanUpFloatableCheckboxList() ");
        this.mSelectAllCB.setChecked(false);
        this.isSelectedAll = false;
        this.mCheckList.clear();
        Iterator<LCoverAppInfo> ir = this.mAppInfoList.iterator();
        while (ir.hasNext()) {
            LCoverAppInfo app = (LCoverAppInfo) ir.next();
            SLog.m12v(TAG, "app pkg : " + app.getPackageName() + ", isChecked : " + app.getIsChecked());
            if (app.getIsChecked().booleanValue()) {
                this.mCheckList.remove(app);
                app.setIsChecked(Boolean.valueOf(false));
            }
        }
        this.mIconNotiListAdapter.notifyDataSetChanged();
    }
}
