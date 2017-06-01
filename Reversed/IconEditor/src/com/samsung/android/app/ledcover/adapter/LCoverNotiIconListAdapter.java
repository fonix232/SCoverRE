package com.samsung.android.app.ledcover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.app.LCoverNoti_CustomDotView;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LCoverNotiIconListAdapter extends BaseAdapter {
    public static String TAG;
    private int CUSTOM_TYPE;
    private int PRESET_TYPE;
    private LCoverIconInfo UserData;
    private boolean isChangeMode;
    private OnAdapterClickedListener mCallback;
    private Context mContext;
    protected ArrayList<LCoverIconInfo> mLCoverIconInfo;
    private LCoverDbAccessor mLedCoverDbAccessor;
    private Boolean mValue;

    /* renamed from: com.samsung.android.app.ledcover.adapter.LCoverNotiIconListAdapter.1 */
    class C02001 implements OnClickListener {
        final /* synthetic */ int val$position;

        C02001(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            SLog.m12v(LCoverNotiIconListAdapter.TAG, "[edit.setOnLongClick] position : " + this.val$position);
            LCoverNotiIconListAdapter.this.mCallback.adapterEditClicked(this.val$position);
        }
    }

    public interface OnAdapterClickedListener {
        void adapterClicked(int i, boolean z);

        void adapterEditClicked(int i);

        void adapterLongClicked(int i, boolean z);
    }

    static class ViewHolder {
        CheckBox checkbox;
        TextView count;
        ImageView edit;
        ImageView icon;
        LinearLayout layout;
        TextView name;
        LCoverNoti_CustomDotView preview;
        LinearLayout view;

        ViewHolder() {
        }
    }

    static {
        TAG = "[LED_COVER]LCoverNotiIconListAdapter";
    }

    public LCoverNotiIconListAdapter(Context context, ArrayList<LCoverIconInfo> objects) {
        this.mValue = Boolean.valueOf(false);
        this.PRESET_TYPE = 1;
        this.CUSTOM_TYPE = 2;
        this.isChangeMode = false;
        SLog.m12v(TAG, "<<LCoverNotiIconListAdapter>>");
        this.mContext = context;
        this.mLCoverIconInfo = objects;
        this.mCallback = null;
        this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this.mContext);
    }

    public int getCount() {
        return this.mLCoverIconInfo.size();
    }

    public LCoverIconInfo getItem(int position) {
        return (LCoverIconInfo) this.mLCoverIconInfo.get(position);
    }

    public int getItemViewType(int position) {
        if (((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getId() <= 54) {
            return this.PRESET_TYPE;
        }
        return this.CUSTOM_TYPE;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(C0198R.layout.lednoti_id_setting_item, null);
            if (type == this.PRESET_TYPE) {
                holder.icon = (ImageView) convertView.findViewById(C0198R.id.iv_icon_image);
                holder.preview = (LCoverNoti_CustomDotView) convertView.findViewById(C0198R.id.gv_prevIcon);
                holder.preview.setVisibility(8);
                holder.icon.setVisibility(0);
                holder.icon.setImageResource(getIconArrInt(position));
            } else if (type == this.CUSTOM_TYPE) {
                holder.view = (LinearLayout) convertView.findViewById(C0198R.id.view_bg);
                holder.layout = (LinearLayout) convertView.findViewById(C0198R.id.caller_id_bg);
                holder.checkbox = (CheckBox) convertView.findViewById(C0198R.id.cb_delete);
                holder.edit = (ImageView) convertView.findViewById(C0198R.id.iv_edit);
                holder.icon = (ImageView) convertView.findViewById(C0198R.id.iv_icon_image);
                holder.preview = (LCoverNoti_CustomDotView) convertView.findViewById(C0198R.id.gv_prevIcon);
                holder.icon.setVisibility(8);
                holder.preview.setVisibility(0);
                holder.preview.setFocusable(false);
                holder.preview.setClickable(false);
                holder.preview.setEnabled(false);
                holder.edit.setClickable(true);
                holder.checkbox.setClickable(false);
            }
            holder.name = (TextView) convertView.findViewById(C0198R.id.tv_icon_name);
            holder.count = (TextView) convertView.findViewById(C0198R.id.tv_icon_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == this.CUSTOM_TYPE) {
            if (isEditClick()) {
                holder.checkbox.setVisibility(0);
                holder.edit.setVisibility(0);
            } else {
                holder.checkbox.setVisibility(4);
                holder.edit.setVisibility(4);
                holder.view.setVisibility(8);
            }
            updateCheckedState(holder, ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getIsChecked().booleanValue());
            holder.edit.setOnClickListener(new C02001(position));
            LCoverSingleton.getInstance().LoadingCustomIconDataBase((LCoverIconInfo) this.mLCoverIconInfo.get(position));
            this.UserData = LCoverSingleton.getInstance().getCustomLEDList(((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getId());
            if (this.UserData != null) {
                holder.preview.destroyDrawingCache();
                holder.preview.setUserData(this.UserData.getDotDataClass());
                holder.preview.invalidate();
            }
        }
        holder.name.setText(getIconName(position));
        holder.count.setText(this.mContext.getString(C0198R.string.sview_led_cover_caller_id_count, new Object[]{NumberFormat.getInstance().format((long) getCnt(position))}));
        return convertView;
    }

    private void updateCheckedState(ViewHolder holder, boolean value) {
        if (!this.isChangeMode) {
            holder.checkbox.setChecked(value);
            if (holder.checkbox.isChecked()) {
                holder.view.setVisibility(0);
            } else {
                holder.view.setVisibility(8);
            }
        }
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getId(int position) {
        return ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getId();
    }

    public String getIconArr(int position) {
        return ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getIconArray();
    }

    public int getIconArrInt(int position) {
        return ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getIconArrayInt();
    }

    public String getIconName(int position) {
        return ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).getIconName();
    }

    public int getCnt(int position) {
        return this.mLedCoverDbAccessor.getIconDbCount(getId(position));
    }

    public void setEditClick(boolean value) {
        this.mValue = Boolean.valueOf(value);
    }

    public boolean isEditClick() {
        return this.mValue.booleanValue();
    }

    public void setItemChecked(int position, boolean b) {
        SLog.m12v(TAG, "[setItemChecked] position : " + position + ", isChecked b : " + b);
        ((LCoverIconInfo) this.mLCoverIconInfo.get(position)).setIsChecked(Boolean.valueOf(b));
    }

    public int getItemCheckedCount() {
        int count = 0;
        int size = this.mLCoverIconInfo.size();
        for (int i = 0; i < size; i++) {
            if (((LCoverIconInfo) this.mLCoverIconInfo.get(i)).getIsChecked().booleanValue()) {
                count++;
            }
        }
        return count;
    }

    public void setOnClickListener(OnAdapterClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public void setIsChangeMode(boolean isChangeMode) {
        this.isChangeMode = isChangeMode;
    }
}
