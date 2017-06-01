package com.samsung.android.app.ledcover.call;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.adapter.LCoverContactListAdapter;
import com.samsung.android.app.ledcover.app.LCoverCallMainActivity;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.creationpattern.LedMatrixAdapter;
import com.samsung.android.app.ledcover.info.ContactInfo;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.android.app.ledcover.wrapperlibrary.ScrollViewWrapper;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class LCoverContactsListActivity extends Activity {
    private static final int MAX_CONTACT_COUNT = 100;
    private static final String TAG = "[LED_COVER]LCoverContactsListActivity";
    private static final int UPDATE_DB_REMOVE = 0;
    private View addContactItem;
    private OnClickListener addContactListener;
    private GridView gvIconMatrix;
    private TextView hasContactDescription;
    private View hasContactFooterView;
    private ImageView icon;
    private boolean isSelectedAll;
    private ActionBar mActionBar;
    private ArrayList<ContactInfo> mCheckList;
    private int mContactCount;
    private ArrayList<ContactInfo> mContactInfoList;
    private LCoverContactListAdapter mContactListAdapter;
    private ListView mContactListView;
    private Context mContext;
    private TextView mDescTextView;
    private int mIconId;
    private String mIconName;
    private int mIconResourceName;
    private boolean mIsInEditMode;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Menu mOptionsMenu;
    private View mSelectActionView;
    private CheckBox mSelectAllCB;
    private TextView mSelectedNum;
    private View noContactFooterView;

    /* renamed from: com.samsung.android.app.ledcover.call.LCoverContactsListActivity.1 */
    class C02331 implements OnClickListener {
        C02331() {
        }

        public void onClick(View v) {
            Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY, Defines.SA_CONTACT_LIST_ACTIVITY_EVENT_ADD_CONTACT, "Add contact");
            Intent contIntent = new Intent("intent.action.INTERACTION_LIST");
            contIntent.putExtra("additional", "settings-phone-multi");
            contIntent.putExtra("support_tab", true);
            contIntent.putExtra("maxRecipientCount", LCoverContactsListActivity.MAX_CONTACT_COUNT);
            contIntent.putExtra("existingRecipientCount", 0);
            contIntent.putExtra("excludeProfile", true);
            try {
                LCoverContactsListActivity.this.startActivityForResult(contIntent, 2);
            } catch (ActivityNotFoundException e) {
                SLog.m12v(LCoverContactsListActivity.TAG, "Activity Not Found");
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.call.LCoverContactsListActivity.2 */
    class C02342 implements Comparator<ContactInfo> {
        C02342() {
        }

        public int compare(ContactInfo contactInfo1, ContactInfo contactInfo2) {
            if (contactInfo1.getName() == null) {
                return -1;
            }
            if (contactInfo2.getName() == null) {
                return 1;
            }
            return contactInfo1.getName().compareToIgnoreCase(contactInfo2.getName());
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.call.LCoverContactsListActivity.3 */
    class C02353 implements OnClickListener {
        C02353() {
        }

        public void onClick(View arg0) {
            boolean z;
            SLog.m12v(LCoverContactsListActivity.TAG, "select all click.");
            LCoverContactsListActivity lCoverContactsListActivity = LCoverContactsListActivity.this;
            if (LCoverContactsListActivity.this.isSelectedAll) {
                z = false;
            } else {
                z = true;
            }
            lCoverContactsListActivity.isSelectedAll = z;
            Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_SELECTION_MODE, Defines.SA_CONTACT_LIST_SELECTION_MODE_EVENT_SELECT_ALL, "Select all", LCoverContactsListActivity.this.isSelectedAll ? 1 : 0);
            LCoverContactsListActivity.this.mSelectAllCB.setChecked(LCoverContactsListActivity.this.isSelectedAll);
            Iterator<ContactInfo> ir;
            ContactInfo contact;
            if (LCoverContactsListActivity.this.isSelectedAll) {
                ir = LCoverContactsListActivity.this.mContactInfoList.iterator();
                while (ir.hasNext()) {
                    contact = (ContactInfo) ir.next();
                    SLog.m12v(LCoverContactsListActivity.TAG, "Name : " + contact.getName() + ", isChecked : " + contact.getIsChecked());
                    if (!contact.getIsChecked()) {
                        LCoverContactsListActivity.this.mCheckList.add(contact);
                        contact.setIsChecked(true);
                    }
                }
            } else {
                ir = LCoverContactsListActivity.this.mContactInfoList.iterator();
                while (ir.hasNext()) {
                    contact = (ContactInfo) ir.next();
                    SLog.m12v(LCoverContactsListActivity.TAG, "Name : " + contact.getName() + ", isChecked : " + contact.getIsChecked());
                    if (contact.getIsChecked()) {
                        LCoverContactsListActivity.this.mCheckList.remove(contact);
                        contact.setIsChecked(false);
                    }
                }
            }
            LCoverContactsListActivity.this.updateMenuItem();
            LCoverContactsListActivity.this.mContactListAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.call.LCoverContactsListActivity.4 */
    class C02364 implements OnItemClickListener {
        C02364() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            SLog.m12v(LCoverContactsListActivity.TAG, "onItemClick");
            if (LCoverContactsListActivity.this.mIsInEditMode) {
                boolean z;
                ContactInfo contact = LCoverContactsListActivity.this.mContactListAdapter.getItem(position);
                boolean isChecked = contact.getIsChecked();
                if (isChecked) {
                    LCoverContactsListActivity.this.mCheckList.remove(contact);
                } else {
                    LCoverContactsListActivity.this.mCheckList.add(contact);
                }
                if (isChecked) {
                    z = false;
                } else {
                    z = true;
                }
                contact.setIsChecked(z);
                LCoverContactsListActivity.this.mContactListAdapter.notifyDataSetChanged();
                if (LCoverContactsListActivity.this.mCheckList.size() != LCoverContactsListActivity.this.mContactInfoList.size()) {
                    LCoverContactsListActivity.this.mSelectAllCB.setChecked(false);
                    LCoverContactsListActivity.this.isSelectedAll = false;
                } else {
                    LCoverContactsListActivity.this.mSelectAllCB.setChecked(true);
                    LCoverContactsListActivity.this.isSelectedAll = true;
                }
                LCoverContactsListActivity.this.updateMenuItem();
                return;
            }
            SLog.m12v(LCoverContactsListActivity.TAG, "Not in edit mode : Item click event ignored!");
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.call.LCoverContactsListActivity.5 */
    class C02375 implements OnItemLongClickListener {
        C02375() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position >= LCoverContactsListActivity.this.mContactListAdapter.getCount()) {
                return false;
            }
            ContactInfo contact = LCoverContactsListActivity.this.mContactListAdapter.getItem(position);
            if (LCoverContactsListActivity.this.mIsInEditMode) {
                return false;
            }
            SLog.m12v(LCoverContactsListActivity.TAG, "onItemLongClick setEditMode true ");
            Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY, Defines.SA_CONTACT_LIST_ACTIVITY_EVENT_SELECTION_MODE, "Selection mode");
            contact.setIsChecked(true);
            LCoverContactsListActivity.this.mContactListAdapter.notifyDataSetChanged();
            LCoverContactsListActivity.this.mCheckList.add(contact);
            LCoverContactsListActivity.this.setEditMode(true);
            return true;
        }
    }

    public LCoverContactsListActivity() {
        this.mContactInfoList = null;
        this.mCheckList = null;
        this.mIconId = 0;
        this.mIconResourceName = 0;
        this.mContactCount = 0;
        this.mIsInEditMode = false;
        this.isSelectedAll = false;
        this.mSelectAllCB = null;
        this.mDescTextView = null;
        this.hasContactDescription = null;
        this.mSelectedNum = null;
        this.mSelectActionView = null;
        this.mOnItemClickListener = new C02364();
        this.mOnItemLongClickListener = new C02375();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLog.m12v(TAG, "onCreate");
        this.mContext = getApplicationContext();
        Intent intent = getIntent();
        if (intent != null) {
            this.mIconId = intent.getIntExtra("selected_id", 0);
            this.mIconName = intent.getStringExtra(Defines.ICON_COL_ICON_NAME);
            this.mContactCount = intent.getIntExtra("contact_count", 0);
            SLog.m12v(TAG, "onActivityCreated() with Icon ID : " + this.mIconId + ", Icon name : " + this.mIconName + ", Contact count : " + this.mContactCount);
        }
        initView();
        this.mCheckList = new ArrayList();
        this.mContactInfoList = getRegistedContactList();
        makeList();
        this.mActionBar = getActionBar();
        updateActionBar();
        ScrollViewWrapper.semSetGoToTopEnabled(this.mContactListView, true);
    }

    private void initView() {
        setContentView(C0198R.layout.led_cover_caller_id_list);
        this.mContactListView = (ListView) findViewById(C0198R.id.lv_contact);
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
        this.hasContactFooterView = LayoutInflater.from(this).inflate(C0198R.layout.led_caller_list_has_item_footer, null);
        this.hasContactDescription = (TextView) this.hasContactFooterView.findViewById(C0198R.id.tv_has_item_description);
        this.noContactFooterView = findViewById(C0198R.id.ln_no_item);
        ((TextView) this.noContactFooterView.findViewById(C0198R.id.tv_no_item_description)).setText(getString(C0198R.string.caller_add_contact_message));
        this.mDescTextView = (TextView) this.hasContactFooterView.findViewById(C0198R.id.tv_contact_count);
        Button addContact = (Button) this.noContactFooterView.findViewById(C0198R.id.btn_add_contact);
        addContact.setText(getString(C0198R.string.ss_add_contact));
        this.addContactListener = new C02331();
        addContact.setOnClickListener(this.addContactListener);
        this.addContactItem = LayoutInflater.from(this).inflate(C0198R.layout.activity_led_caller_add_contact_list_footer, null);
        ((ImageView) this.addContactItem.findViewById(C0198R.id.img_create_icon)).setColorFilter(getColor(C0198R.color.create_icon_color), Mode.SRC_ATOP);
        ((TextView) this.addContactItem.findViewById(C0198R.id.caller_id_name)).setText(C0198R.string.ss_add_contact);
        this.addContactItem.setOnClickListener(this.addContactListener);
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
        SLog.m12v(TAG, "onResume");
        if (this.mIsInEditMode) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CONTACT_LIST_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initView();
        SLog.m12v(TAG, "onConfigurationChanged");
        createEditModeActionBar();
        if (this.mIsInEditMode) {
            showEditModeActionBar();
            updateMenuItem();
        }
        makeList();
    }

    protected void onDestroy() {
        SLog.m12v(TAG, "onDestroy");
        if (this.mContactInfoList != null) {
            this.mContactInfoList.clear();
            this.mContactInfoList = null;
        }
        if (this.mCheckList != null) {
            this.mCheckList.clear();
            this.mCheckList = null;
        }
        Utils.recursiveRecycle(this.mContactListView);
        super.onDestroy();
    }

    private ArrayList<ContactInfo> getRegistedContactList() {
        SLog.m12v(TAG, "getRegistedContactList(), icon id = " + this.mIconId);
        if (this.mContactInfoList == null) {
            this.mContactInfoList = new ArrayList();
        } else {
            this.mContactInfoList.clear();
        }
        String selection = "sec_led IS ? AND (photo_id = _id OR photo_id IS NULL)";
        Cursor c = null;
        try {
            c = getContentResolver().query(ContactsContract.AUTHORITY_URI.buildUpon().appendPath("data_groupby").appendQueryParameter("groupby", "contact_id").build(), new String[]{"contact_id", "display_name", "data15"}, selection, new String[]{Integer.toString(this.mIconId)}, null);
            if (c != null) {
                int contactCount = c.getCount();
                SLog.m12v(TAG, "updateContactList() Selected ID : " + this.mIconId + ", Count : " + contactCount);
                if (contactCount > 0) {
                    while (c.moveToNext()) {
                        String contact_id = c.getString(0);
                        String display_name = c.getString(1);
                        this.mContactInfoList.add(new ContactInfo(getBaseContext(), contact_id, display_name, c.getBlob(2), false));
                        SLog.m12v(TAG, "updateContactList() contact_id : " + contact_id + ", display_name : " + display_name);
                    }
                }
                c.close();
            }
            Collections.sort(this.mContactInfoList, new C02342());
            return this.mContactInfoList;
        } catch (SQLiteException e) {
            SLog.m12v(TAG, "SQL Exception : " + e);
            if (c != null) {
                c.close();
            }
            return null;
        }
    }

    private void makeList() {
        SLog.m12v(TAG, "Create contact list");
        this.mContactListAdapter = new LCoverContactListAdapter(this, this.mContactInfoList);
        this.mContactListView.setAdapter(this.mContactListAdapter);
        this.mContactListView.setOnItemClickListener(this.mOnItemClickListener);
        this.mContactListView.setOnItemLongClickListener(this.mOnItemLongClickListener);
        updateListDescription();
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
        this.mSelectActionView = LayoutInflater.from(this).inflate(C0198R.layout.led_cover_caller_id_selection_mode_actionbar, null);
        this.mSelectedNum = (TextView) this.mSelectActionView.findViewById(C0198R.id.number_selected_text);
        Utils.setLargeTextSize(this.mContext, this.mSelectedNum, (float) this.mContext.getResources().getDimensionPixelSize(C0198R.dimen.led_cover_main_abar_desc_textview_text_size));
        this.mSelectAllCB = (CheckBox) this.mSelectActionView.findViewById(C0198R.id.toggle_selection_check);
        this.mSelectActionView.findViewById(C0198R.id.select_all_layout).setOnClickListener(new C02353());
    }

    private void showEditModeActionBar() {
        boolean z = false;
        SLog.m12v(TAG, "showEditModeActionBar");
        if (this.mSelectActionView == null) {
            createEditModeActionBar();
        }
        this.mActionBar.setCustomView(this.mSelectActionView, new LayoutParams(-1, -1, 16));
        ((Toolbar) this.mSelectActionView.getParent()).setContentInsetsAbsolute(0, 0);
        if (this.mCheckList.size() == this.mContactInfoList.size()) {
            z = true;
        }
        this.isSelectedAll = z;
        this.mSelectAllCB.setChecked(this.isSelectedAll);
    }

    private void updateMenuItem() {
        if (this.mIsInEditMode) {
            this.mOptionsMenu.findItem(4).setVisible(false);
            if (this.mCheckList.size() == 0) {
                this.mSelectedNum.setText(C0198R.string.sview_led_cover_select_contact);
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
        if (this.mContactInfoList == null || this.mContactInfoList.size() <= 0) {
            this.mOptionsMenu.findItem(4).setVisible(false);
        } else {
            this.mOptionsMenu.findItem(4).setVisible(true);
        }
    }

    private void updateListDescription() {
        SLog.m12v(TAG, "updateListDescription, Edit mode: " + this.mIsInEditMode);
        if (this.mIsInEditMode) {
            this.mContactListView.removeFooterView(this.hasContactFooterView);
            this.noContactFooterView.setVisibility(8);
            this.mContactListView.removeFooterView(this.addContactItem);
            return;
        }
        int contact = this.mContactInfoList.size();
        if (contact == 0) {
            if (this.mContactListView.getFooterViewsCount() == 2) {
                this.mContactListView.removeFooterView(this.addContactItem);
                this.mContactListView.removeFooterView(this.hasContactFooterView);
            }
            this.noContactFooterView.setVisibility(0);
            return;
        }
        this.noContactFooterView.setVisibility(8);
        boolean isChanged = true;
        if (this.mContactListView.getFooterViewsCount() == 0) {
            this.mContactListView.addFooterView(this.addContactItem);
            this.mContactListView.addFooterView(this.hasContactFooterView, null, false);
            isChanged = false;
        }
        if (isChanged) {
            this.mContactListAdapter.notifyDataSetChanged();
        }
        if (contact == 1) {
            this.mDescTextView.setText(C0198R.string.sview_led_cover_caller_one_contact);
            this.hasContactDescription.setText(getString(C0198R.string.caller_has_contact_add_contact_message));
            return;
        }
        this.hasContactDescription.setText(getString(C0198R.string.f6x821109c4));
        this.mDescTextView.setText(String.format(getString(C0198R.string.sview_led_cover_caller_id_ps_contacts), new Object[]{Integer.valueOf(contact)}));
    }

    private void setEditMode(boolean isInEditMode) {
        if (isInEditMode) {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CONTACT_LIST_SELECTION_MODE);
        } else {
            Utils.sendScreenViewSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY);
        }
        if (isInEditMode && !this.mIsInEditMode) {
            this.mIsInEditMode = true;
        }
        if (!isInEditMode && this.mIsInEditMode) {
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
        if (this.mContactInfoList.size() == 0) {
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        SLog.m12v(TAG, "onActivityResult() requestCode : " + requestCode);
        if (requestCode == 2 || requestCode == 1) {
            if (intent != null) {
                ArrayList<String> mParticipantsList = intent.getStringArrayListExtra("result");
                int i = 0;
                StringBuffer selection = new StringBuffer();
                selection.append("_id IN (");
                while (i < mParticipantsList.size()) {
                    String[] args = ((String) mParticipantsList.get(i)).split(";");
                    selection.append("'");
                    selection.append(args[0]);
                    selection.append("'");
                    i++;
                    if (i < mParticipantsList.size()) {
                        selection.append(",");
                    }
                }
                selection.append(")");
                SLog.m12v(TAG, "onActivityResult() [ADD] sec_led : " + this.mIconId + ", count : " + mParticipantsList.size() + ", selection : " + selection.toString());
                makeContactChangedToastForAddContact(selection);
                ContentValues v = new ContentValues();
                v.put("sec_led", Integer.toString(this.mIconId));
                try {
                    getContentResolver().update(Contacts.CONTENT_URI, v, selection.toString(), null);
                } catch (SQLiteException e) {
                    SLog.m12v(TAG, "SQL Exception : " + e);
                }
                getRegistedContactList();
                updateListDescription();
                updateMenuItem();
            }
        } else if (requestCode == 3 && intent != null && resultCode == -1) {
            int ret_icon_id = intent.getIntExtra("selected_id", 0);
            String ret_icon_name = intent.getStringExtra(Defines.ICON_COL_ICON_NAME);
            int count = intent.getIntExtra("contact_count", 0);
            int checkedCount = getNumOfCheckedList();
            updateContactDB(ret_icon_id);
            makeContactChangedToastForChangeContact(ret_icon_id);
            Intent result = new Intent(this, LCoverContactsListActivity.class);
            result.putExtra("selected_id", ret_icon_id);
            result.putExtra(Defines.ICON_COL_ICON_NAME, ret_icon_name);
            result.putExtra("contact_count", count + checkedCount);
            startActivity(result);
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED /*2*/:
                SLog.m12v(TAG, "onOptionsItemSelected() action [CHANGE]");
                Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_SELECTION_MODE, Defines.SA_CONTACT_LIST_SELECTION_MODE_EVENT_CHANGE, "Change");
                Intent intent = new Intent(this.mContext, LCoverCallMainActivity.class);
                intent.putExtra("change_mode", 1);
                startActivityForResult(intent, 3);
                break;
            case ConnectionResult.SIGN_IN_REQUIRED /*4*/:
                SLog.m12v(TAG, "onOptinsItemSelected() action [EDIT]");
                Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY, Defines.SA_CONTACT_LIST_ACTIVITY_EVENT_EDIT, "Edit");
                setEditMode(true);
                break;
            case ConnectionResult.INVALID_ACCOUNT /*5*/:
                SLog.m12v(TAG, "onOptionsItemSelected() action [REMOVE]");
                Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_SELECTION_MODE, Defines.SA_CONTACT_LIST_SELECTION_MODE_EVENT_REMOVE, "Remove");
                updateContactDB(0);
                this.mContactListAdapter.removeItems(this.mCheckList);
                finishEditMode();
                break;
            case 16908332:
                SLog.m12v(TAG, "onOptionsItemSelected() action [home]");
                Utils.sendEventSALog(Defines.SA_SCREEN_CONTACT_LIST_ACTIVITY, Defines.SA_CONTACT_LIST_ACTIVITY_EVENT_UP_BUTTON, "Up button");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        SLog.m12v(TAG, "onBackPressed");
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
        Iterator<ContactInfo> ir = this.mContactInfoList.iterator();
        while (ir.hasNext()) {
            ContactInfo contact = (ContactInfo) ir.next();
            SLog.m12v(TAG, "Name : " + contact.getName() + ", isChecked : " + contact.getIsChecked());
            if (contact.getIsChecked()) {
                this.mCheckList.remove(contact);
                contact.setIsChecked(false);
            }
        }
        this.mContactListAdapter.notifyDataSetChanged();
    }

    private void updateContactDB(int value) {
        StringBuffer selection = new StringBuffer();
        selection.append("_id IN (");
        int listSize = this.mContactInfoList.size();
        int numofchecked = getNumOfCheckedList();
        for (int index = 0; index < listSize; index++) {
            if (((ContactInfo) this.mContactInfoList.get(index)).getIsChecked()) {
                selection.append("'");
                selection.append(((ContactInfo) this.mContactInfoList.get(index)).getID());
                selection.append("'");
                numofchecked--;
                if (numofchecked > 0) {
                    selection.append(",");
                }
            }
        }
        selection.append(")");
        ContentValues v = new ContentValues();
        if (value == 0) {
            SLog.m12v(TAG, "updateContactDB() [REMOVE] value : " + value + ", count : " + listSize + ", selection : " + selection.toString());
            v.put("sec_led", C0316a.f163d);
        } else {
            SLog.m12v(TAG, "updateContactDB() [CHANGE] value : " + value + ", count : " + listSize + ", selection : " + selection.toString());
            v.put("sec_led", Integer.toString(value));
        }
        try {
            getContentResolver().update(Contacts.CONTENT_URI, v, selection.toString(), null);
        } catch (SQLiteException e) {
            SLog.m12v(TAG, "SQL Exception : " + e);
        }
    }

    public void makeContactChangedToastForAddContact(StringBuffer selection) {
        Cursor c = null;
        try {
            c = getContentResolver().query(Contacts.CONTENT_URI, new String[]{Defines.PKG_COL_KEY, "display_name", "sec_led"}, selection.toString(), null, null);
            String SelectedID = Integer.toString(this.mIconId);
            int overwritten_contactCount = 0;
            String overwritten_contactName = C0316a.f163d;
            int exsit_contactCount = 0;
            String exsit_contactName = C0316a.f163d;
            if (c != null && c.getCount() > 0) {
                c.moveToNext();
                do {
                    String callerID = c.getString(2);
                    if (callerID != null && !callerID.equals(C0316a.f163d) && !callerID.equals(SelectedID)) {
                        overwritten_contactCount++;
                        if (C0316a.f163d.equals(overwritten_contactName)) {
                            overwritten_contactName = c.getString(1);
                        }
                    } else if (!(callerID == null || callerID.equals(C0316a.f163d) || !callerID.equals(SelectedID))) {
                        exsit_contactCount++;
                        if (C0316a.f163d.equals(exsit_contactName)) {
                            exsit_contactName = c.getString(1);
                        }
                    }
                } while (c.moveToNext());
                String overwritten_msg = null;
                String exsit_msg = null;
                if (overwritten_contactCount >= 1) {
                    if (overwritten_contactCount == 1) {
                        overwritten_msg = String.format(getString(C0198R.string.sview_led_cover_toast_overwritten_single), new Object[]{overwritten_contactName});
                    } else {
                        overwritten_msg = String.format(getString(C0198R.string.sview_led_cover_toast_overwritten_multi), new Object[]{Integer.valueOf(overwritten_contactCount)});
                    }
                    SLog.m12v(TAG, "make Overwritten Toast [ADD] " + overwritten_msg);
                }
                if (exsit_contactCount >= 1) {
                    if (exsit_contactCount == 1) {
                        exsit_msg = String.format(getString(C0198R.string.sview_led_cover_toast_already_exist_single), new Object[]{exsit_contactName});
                    } else {
                        overwritten_msg = null;
                        exsit_msg = String.format(getString(C0198R.string.sview_led_cover_toast_already_exist_multi), new Object[]{Integer.valueOf(exsit_contactCount), Integer.valueOf(overwritten_contactCount)});
                    }
                    SLog.m12v(TAG, "make alerady exist Toast [ADD] " + exsit_msg);
                }
                if (!(TextUtils.isEmpty(overwritten_msg) && TextUtils.isEmpty(exsit_msg))) {
                    StringBuilder msg = new StringBuilder();
                    if (!TextUtils.isEmpty(overwritten_msg)) {
                        msg.append(overwritten_msg);
                        if (!TextUtils.isEmpty(exsit_msg)) {
                            msg.append("\n");
                        }
                    }
                    if (!TextUtils.isEmpty(exsit_msg)) {
                        msg.append(exsit_msg);
                    }
                    Toast.makeText(this, msg.toString(), 0).show();
                }
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLiteException e) {
            SLog.m12v(TAG, "SQL Exception : " + e);
            c.close();
        }
    }

    public void makeContactChangedToastForChangeContact(int id) {
        int count = getNumOfCheckedList();
        String contactName = get1stCheckedContactName();
        if (this.mIconId != id && count >= 1) {
            String msg;
            if (count == 1) {
                msg = String.format(getString(C0198R.string.sview_led_cover_toast_overwritten_single), new Object[]{contactName});
            } else {
                msg = String.format(getString(C0198R.string.sview_led_cover_toast_overwritten_multi), new Object[]{Integer.valueOf(count)});
            }
            Toast.makeText(this, msg, 0).show();
            SLog.m12v(TAG, "makeContactChangedToast() [CHANGE] " + msg);
        }
    }

    private String get1stCheckedContactName() {
        if (this.mContactInfoList != null) {
            for (int index = 0; index < this.mContactInfoList.size(); index++) {
                if (((ContactInfo) this.mContactInfoList.get(index)).getIsChecked()) {
                    return ((ContactInfo) this.mContactInfoList.get(index)).getName();
                }
            }
        }
        return null;
    }
}
