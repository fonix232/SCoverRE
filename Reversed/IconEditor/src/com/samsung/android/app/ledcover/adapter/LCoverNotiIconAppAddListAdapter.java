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

public class LCoverNotiIconAppAddListAdapter extends BaseAdapter {
    public static String TAG;
    protected ArrayList<LCoverAppInfo> mAppInfo;
    protected Context mContext;
    protected boolean mEnable;
    private LayoutInflater mInflater;
    private boolean mIsSamsungDevice;

    static class ViewHolder {
        public CheckBox appCheckBox;
        public ImageView appImage;
        public LinearLayout appLayout;
        public TextView appName;

        ViewHolder() {
        }
    }

    static {
        TAG = "[LED_COVER]LCoverNotiIconAppAddListAdapter";
    }

    public LCoverNotiIconAppAddListAdapter(Context context, ArrayList<LCoverAppInfo> list, boolean isSamsungDevice) {
        this.mEnable = true;
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

    public boolean getItemChecked(int position) {
        return ((LCoverAppInfo) this.mAppInfo.get(position)).getIsChecked().booleanValue();
    }

    public void setItemChecked(int position, boolean b) {
        ((LCoverAppInfo) this.mAppInfo.get(position)).setIsChecked(Boolean.valueOf(b));
    }

    public int getCheckedListCount() {
        int count = 0;
        int size = this.mAppInfo.size();
        for (int i = 0; i < size; i++) {
            if (((LCoverAppInfo) this.mAppInfo.get(i)).getIsChecked().booleanValue()) {
                count++;
            }
        }
        return count;
    }

    public void setAllItemChecked(boolean b) {
        for (int i = 0; i < this.mAppInfo.size(); i++) {
            ((LCoverAppInfo) this.mAppInfo.get(i)).setIsChecked(Boolean.valueOf(b));
        }
    }

    public void removeItem(String pkgName) {
        for (int i = 0; i < getCount(); i++) {
            if (((LCoverAppInfo) this.mAppInfo.get(i)).getPackageName().equals(pkgName)) {
                this.mAppInfo.remove(i);
            }
        }
    }

    public void setListEnabled(boolean b) {
        this.mEnable = b;
        notifyDataSetChanged();
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
            viewHolder.appLayout = (LinearLayout) convertView.findViewById(C0198R.id.app_frame);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appName.setText(((LCoverAppInfo) this.mAppInfo.get(position)).getAppName());
        viewHolder.appImage.setImageDrawable(((LCoverAppInfo) this.mAppInfo.get(position)).getAppIcon());
        viewHolder.appCheckBox.setChecked(((LCoverAppInfo) this.mAppInfo.get(position)).getIsChecked().booleanValue());
        return convertView;
    }

    public void addListItem(ArrayList<LCoverAppInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < this.mAppInfo.size(); j++) {
                if (((LCoverAppInfo) list.get(i)).getPackageName().equals(((LCoverAppInfo) this.mAppInfo.get(j)).getPackageName())) {
                    ((LCoverAppInfo) list.get(i)).setIsChecked(((LCoverAppInfo) this.mAppInfo.get(j)).getIsChecked());
                    break;
                }
            }
        }
        this.mAppInfo = list;
    }
}
