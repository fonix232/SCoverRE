package com.samsung.android.app.ledcover.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.Utils;
import java.lang.ref.WeakReference;

public class LCoverRootDescriptionViewPagerAdapter extends PagerAdapter {
    Context mContext;
    private final int[] mDescImgArray;
    LayoutInflater mInflater;

    public LCoverRootDescriptionViewPagerAdapter(Context context) {
        this.mContext = null;
        this.mInflater = null;
        this.mDescImgArray = new int[]{C0198R.drawable.led_cover_img_main_01, C0198R.drawable.led_cover_img_main_02, C0198R.drawable.led_cover_img_main_03};
        this.mContext = context;
        this.mInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.mDescImgArray.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if (Utils.isRtl()) {
            position = (getCount() - 1) - position;
        }
        View view = this.mInflater.inflate(C0198R.layout.description_image_view, container, false);
        ImageView desImg = (ImageView) view.findViewById(C0198R.id.description_image);
        TextView desText = (TextView) view.findViewById(C0198R.id.description_text);
        Bitmap tempBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), this.mDescImgArray[position]);
        int width = container.getMeasuredWidth();
        desImg.setImageBitmap((Bitmap) new WeakReference(Bitmap.createScaledBitmap(tempBitmap, width, (int) ((((float) width) / ((float) tempBitmap.getWidth())) * ((float) tempBitmap.getHeight())), false)).get());
        desText.setText(this.mContext.getResources().getStringArray(C0198R.array.main_description)[position]);
        container.addView(view, 0);
        tempBitmap.recycle();
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
