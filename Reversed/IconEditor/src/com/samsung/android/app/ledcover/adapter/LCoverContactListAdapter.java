package com.samsung.android.app.ledcover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.info.ContactInfo;
import java.util.ArrayList;

public class LCoverContactListAdapter extends BaseAdapter {
    public static String TAG;
    private ArrayList<ContactInfo> mContactInfo;
    private Context mContext;
    private LayoutInflater mInflater;

    static class ViewHolder {
        public CheckBox check;
        public ImageView checkIcon;
        public ImageView icon;
        public TextView text;

        ViewHolder() {
        }
    }

    static {
        TAG = "[LED_COVER]ContactListAdapter";
    }

    public LCoverContactListAdapter(Context context, ArrayList<ContactInfo> contactInfo) {
        this.mContext = context;
        this.mContactInfo = contactInfo;
        this.mInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.mContactInfo.size();
    }

    public ContactInfo getItem(int position) {
        return (ContactInfo) this.mContactInfo.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void addItems(ArrayList<ContactInfo> list) {
        this.mContactInfo.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItems(ArrayList<ContactInfo> list) {
        this.mContactInfo.removeAll(list);
        notifyDataSetChanged();
    }

    public void setItemChecked(int position, boolean b) {
        ((ContactInfo) this.mContactInfo.get(position)).setIsChecked(b);
    }

    public ArrayList<ContactInfo> getContactList() {
        return this.mContactInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(C0198R.layout.led_cover_caller_id_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(C0198R.id.caller_id_name);
            viewHolder.icon = (ImageView) convertView.findViewById(C0198R.id.caller_id);
            viewHolder.checkIcon = (ImageView) convertView.findViewById(C0198R.id.imgContactCheck);
            viewHolder.check = (CheckBox) convertView.findViewById(C0198R.id.cb_selected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).getName() != null) {
            viewHolder.text.setText(getItem(position).getName().toString());
            viewHolder.icon.setImageDrawable(getItem(position).getIconBitmap());
            boolean checkContact = getItem(position).getIsChecked();
            viewHolder.check.setChecked(checkContact);
            if (checkContact) {
                viewHolder.checkIcon.setVisibility(0);
            } else {
                viewHolder.checkIcon.setVisibility(8);
            }
            viewHolder.check.setEnabled(true);
            viewHolder.check.setAlpha(0.85f);
        }
        return convertView;
    }
}
