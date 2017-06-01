package com.samsung.android.app.ledcover.creationpattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Surface.OutOfResourcesException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;

public class LedMatrixAdapter extends BaseAdapter {
    public static final String TAG = "[LED_COVER]LedMatrixAdapter";
    private boolean isViewMode;
    private Context mContext;
    private LCoverIconInfo userData;

    static class ViewHolder {
        ImageView dot;

        ViewHolder() {
        }
    }

    public LedMatrixAdapter(Context context, LCoverIconInfo userData) {
        this.isViewMode = false;
        this.mContext = context;
        this.userData = userData;
    }

    public LedMatrixAdapter(Context context, LCoverIconInfo userData, boolean isViewMode) {
        this.isViewMode = false;
        this.mContext = context;
        this.userData = userData;
        this.isViewMode = isViewMode;
    }

    public int getCount() {
        return Defines.getLedMatrixTotal();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void setTest() {
        for (int i = 0; i < Defines.getLedMatrixTotal(); i++) {
            if (this.userData.getDotDataClass()[i].getDotByteData() == Defines.DOT_DISABLE) {
                this.userData.getDotDataClass()[i].getDotImageData().setVisibility(4);
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (convertView == null) {
            holder = new ViewHolder();
            try {
                convertView = inflater.inflate(C0198R.layout.creation_dot_led, null);
                holder.dot = (ImageView) convertView;
                convertView.setTag(holder);
            } catch (OutOfMemoryError e) {
                SLog.m12v(TAG, "OutOfMemoryError");
            } catch (OutOfResourcesException e2) {
                SLog.m12v(TAG, "OutOfResourcesException");
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.dot.setSelected(this.userData.getDotDataClass()[position].isDotEnable());
        if (holder.dot.isSelected()) {
            holder.dot.setBackground(this.mContext.getDrawable(C0198R.drawable.led_dot_selected));
        } else if (this.isViewMode) {
            holder.dot.setBackground(null);
        } else {
            holder.dot.setBackground(this.mContext.getDrawable(C0198R.drawable.led_dot_normal));
        }
        this.userData.getDotDataClass()[position].setDotImageData(holder.dot);
        return convertView;
    }
}
