package com.samsung.android.app.ledcover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.info.LCoverAppInfo;
import java.util.ArrayList;

public class LCoverNotiIconAppListAdapter extends BaseAdapter {
    public static String TAG;
    protected ArrayList<LCoverAppInfo> mAppInfo;
    protected Context mContext;
    private LayoutInflater mInflater;
    private boolean mIsSamsungDevice;

    static class ViewHolder {
        public CheckBox appCheckBox;
        public LinearLayout appDesc;
        public ImageView appImage;
        public TextView appName;

        ViewHolder() {
        }
    }

    static {
        TAG = "[LED_COVER]LCoverNotiIconAppListAdapter";
    }

    public LCoverNotiIconAppListAdapter(Context context, ArrayList<LCoverAppInfo> list, boolean isSamsungDevice) {
        this.mContext = context;
        this.mAppInfo = list;
        this.mIsSamsungDevice = isSamsungDevice;
        this.mInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.mAppInfo.size();
    }

    public LCoverAppInfo getItem(int position) {
        return (LCoverAppInfo) this.mAppInfo.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void addItems(ArrayList<LCoverAppInfo> list) {
        this.mAppInfo.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItems(ArrayList<LCoverAppInfo> list) {
        this.mAppInfo.removeAll(list);
        notifyDataSetChanged();
    }

    public void setItemChecked(int position, boolean b) {
        ((LCoverAppInfo) this.mAppInfo.get(position)).setIsChecked(Boolean.valueOf(b));
    }

    public ArrayList<LCoverAppInfo> getAppList() {
        return this.mAppInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(C0198R.layout.lcover_noti_icon_app_list, null);
            viewHolder = new ViewHolder();
            viewHolder.appName = (TextView) convertView.findViewById(C0198R.id.app_name);
            viewHolder.appImage = (ImageView) convertView.findViewById(C0198R.id.app_image);
            viewHolder.appCheckBox = (CheckBox) convertView.findViewById(C0198R.id.cb_selected);
            viewHolder.appDesc = (LinearLayout) convertView.findViewById(C0198R.id.list_desc_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appName.setText(((LCoverAppInfo) this.mAppInfo.get(position)).getAppName());
        viewHolder.appImage.setImageDrawable(((LCoverAppInfo) this.mAppInfo.get(position)).getAppIcon());
        viewHolder.appCheckBox.setChecked(((LCoverAppInfo) this.mAppInfo.get(position)).getIsChecked().booleanValue());
        return convertView;
    }
}
