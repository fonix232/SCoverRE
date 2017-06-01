package com.samsung.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;

public class SemColorPicker extends LinearLayout {
    static final int RECENT_COLOR_SIZE = 6;
    private static final int RIPPLE_EFFECT_OPACITY = 61;
    private static final String TAG = "SemColorPicker";
    private boolean bUserInput = false;
    private String[] mColorDescription = null;
    private Context mContext;
    private View mCurrentColorFocus;
    private TextView mCurrentColorText;
    private ImageView mCurrentColorView;
    private int mCurrentOrientation = 1;
    private SemGradientColorSeekBar mGradientColorSeekBar;
    private FrameLayout mGradientColorSeekBarFrame;
    private SemGradientColorWheel mGradientColorWheel;
    OnClickListener mImageButtonClickListener = new C02631();
    private OnColorChangedListener mOnColorChangedListener;
    private PickedColor mPickedColor;
    private View mPickedColorFocus;
    private TextView mPickedColorText;
    private ImageView mPickedColorView;
    private SemRecentColorInfo mRecentColorInfo;
    private ViewGroup mRecentColorLayout;
    private LinearLayout mRecentColorListLayout;
    private GradientDrawable mSelectedColorBackground;
    private ArrayList<View> recentColorItemViews;
    private LinkedList<Integer> recentColorValues;

    class C02631 implements OnClickListener {
        C02631() {
        }

        public void onClick(View view) {
            int size = SemColorPicker.this.recentColorValues.size();
            int i = 0;
            while (i < size && i < 6) {
                if (SemColorPicker.this.mRecentColorListLayout.getChildAt(i).equals(view)) {
                    SemColorPicker.this.bUserInput = true;
                    SemColorPicker.this.mPickedColor.setColor(((Integer) SemColorPicker.this.recentColorValues.get(i)).intValue());
                    SemColorPicker.this.mapColorOnColorWheel(SemColorPicker.this.mPickedColor.getColor());
                    if (SemColorPicker.this.mOnColorChangedListener != null) {
                        SemColorPicker.this.mOnColorChangedListener.onColorChanged(SemColorPicker.this.mPickedColor.getColor());
                    }
                }
                i++;
            }
        }
    }

    class C02642 implements OnWheelColorChangedListener {
        C02642() {
        }

        public void onWheelColorChanged(float f, float f2) {
            SemColorPicker.this.bUserInput = true;
            SemColorPicker.this.mPickedColor.setHS(f, f2);
            SemColorPicker.this.updateCurrentColor();
        }
    }

    class C02653 implements OnSeekBarChangeListener {
        C02653() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                SemColorPicker.this.bUserInput = true;
            }
            SemColorPicker.this.mPickedColor.setV(((float) seekBar.getProgress()) / ((float) seekBar.getMax()));
            if (SemColorPicker.this.mSelectedColorBackground != null) {
                SemColorPicker.this.mSelectedColorBackground.setColor(SemColorPicker.this.mPickedColor.getColor());
            }
            if (SemColorPicker.this.mOnColorChangedListener != null) {
                SemColorPicker.this.mOnColorChangedListener.onColorChanged(SemColorPicker.this.mPickedColor.getColor());
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    class C02664 implements OnTouchListener {
        C02664() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    SemColorPicker.this.mGradientColorSeekBar.setSelected(true);
                    return true;
                case 1:
                case 3:
                    SemColorPicker.this.mGradientColorSeekBar.setSelected(false);
                    return false;
                default:
                    return false;
            }
        }
    }

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    private static class PickedColor {
        private int mColor = -1;
        private float[] mHsv = new float[3];

        public int getColor() {
            return this.mColor;
        }

        public float getV() {
            return this.mHsv[2];
        }

        public void setColor(int i) {
            this.mColor = i;
            Color.colorToHSV(i, this.mHsv);
        }

        public void setHS(float f, float f2) {
            this.mHsv[0] = f;
            this.mHsv[1] = f2;
            this.mHsv[2] = 1.0f;
            this.mColor = Color.HSVToColor(this.mHsv);
        }

        public void setV(float f) {
            this.mHsv[2] = f;
            this.mColor = Color.HSVToColor(this.mHsv);
        }
    }

    public SemColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        LayoutInflater.from(getContext()).inflate(17367291, this);
        this.mRecentColorInfo = new SemRecentColorInfo(context);
        this.recentColorValues = this.mRecentColorInfo.getRecentColorInfo();
        init();
    }

    private void init() {
        this.mPickedColor = new PickedColor();
        initCurrentColorView();
        initGradientColorSeekBar();
        initGradientColorWheel();
        initRecentColorLayout();
        updateCurrentColor();
        setInitialColors();
    }

    private void initCurrentColorView() {
        this.mCurrentColorView = (ImageView) findViewById(16909467);
        this.mPickedColorView = (ImageView) findViewById(16909471);
        this.mCurrentColorText = (TextView) findViewById(16909466);
        this.mPickedColorText = (TextView) findViewById(16909472);
        this.mCurrentColorFocus = findViewById(16909469);
        this.mPickedColorFocus = findViewById(16909470);
        this.mSelectedColorBackground = (GradientDrawable) this.mPickedColorView.getBackground();
        this.mSelectedColorBackground.setColor(this.mPickedColor.getColor());
        this.mCurrentColorFocus.setContentDescription(getResources().getString(17040410) + " " + getResources().getString(17040417));
        this.mPickedColorFocus.setContentDescription(getResources().getString(17040411) + " " + getResources().getString(17040417));
    }

    private void initGradientColorSeekBar() {
        this.mGradientColorSeekBar = (SemGradientColorSeekBar) findViewById(16909477);
        this.mGradientColorSeekBarFrame = (FrameLayout) findViewById(16909476);
        this.mGradientColorSeekBar.init(this.mPickedColor.getColor());
        this.mGradientColorSeekBar.setOnSeekBarChangeListener(new C02653());
        this.mGradientColorSeekBar.setOnTouchListener(new C02664());
        this.mGradientColorSeekBarFrame.setContentDescription(getResources().getString(17040415) + ", " + getResources().getString(17040414) + ", " + getResources().getString(17040419));
    }

    private void initGradientColorWheel() {
        this.mCurrentOrientation = this.mContext.getResources().getConfiguration().orientation;
        int dimensionPixelSize = this.mCurrentOrientation == 1 ? getResources().getDimensionPixelSize(17105751) : getResources().getDimensionPixelSize(17105788);
        this.mGradientColorWheel = (SemGradientColorWheel) findViewById(16909475);
        this.mGradientColorWheel.init(dimensionPixelSize);
        this.mGradientColorWheel.setColor(this.mPickedColor.getColor());
        this.mGradientColorWheel.setOnColorWheelInterface(new C02642());
        this.mGradientColorWheel.setContentDescription(getResources().getString(17040413) + ", " + getResources().getString(17040412) + ", " + getResources().getString(17040419));
    }

    private void initRecentColorLayout() {
        this.mRecentColorLayout = (ViewGroup) findViewById(16909479);
        this.mRecentColorListLayout = (LinearLayout) this.mRecentColorLayout.findViewById(16909481);
        this.mColorDescription = new String[]{getResources().getString(17040420), getResources().getString(17040421), getResources().getString(17040422), getResources().getString(17040423), getResources().getString(17040424), getResources().getString(17040425)};
    }

    private void mapColorOnColorWheel(int i) {
        this.mPickedColor.setColor(i);
        if (this.mGradientColorWheel != null) {
            this.mGradientColorWheel.setColor(i);
        }
        if (this.mGradientColorSeekBar != null) {
            this.mGradientColorSeekBar.restoreColor(i);
        }
        if (this.mSelectedColorBackground != null) {
            this.mSelectedColorBackground.setColor(i);
        }
        if (this.mGradientColorWheel != null) {
            float v = this.mPickedColor.getV();
            this.mPickedColor.setV(1.0f);
            this.mGradientColorWheel.updateCursorColor(this.mPickedColor.getColor());
            this.mPickedColor.setV(v);
        }
    }

    private void setImageColor(View view, Integer num) {
        Drawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setStroke(getResources().getDimensionPixelSize(17105776), 17170842);
        gradientDrawable.setColor(num.intValue());
        int argb = Color.argb(61, 0, 0, 0);
        view.setBackground(new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{argb}), gradientDrawable, null));
        view.setOnClickListener(this.mImageButtonClickListener);
    }

    private void setInitialColors() {
        mapColorOnColorWheel(this.mPickedColor.getColor());
    }

    private void updateCurrentColor() {
        int color = this.mPickedColor.getColor();
        if (this.mGradientColorSeekBar != null) {
            this.mGradientColorSeekBar.changeColorBase(color);
        }
        if (this.mSelectedColorBackground != null) {
            this.mSelectedColorBackground.setColor(color);
        }
        if (this.mGradientColorWheel != null) {
            this.mGradientColorWheel.updateCursorColor(color);
        }
        if (this.mOnColorChangedListener != null) {
            this.mOnColorChangedListener.onColorChanged(color);
        }
    }

    public SemRecentColorInfo getRecentColorInfo() {
        return this.mRecentColorInfo;
    }

    public boolean isUserInputValid() {
        return this.bUserInput;
    }

    public void saveSelectedColor() {
        this.mRecentColorInfo.saveSelectedColor(this.mPickedColor.getColor());
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.mOnColorChangedListener = onColorChangedListener;
    }

    public void updateRecentColorLayout() {
        this.recentColorItemViews = new ArrayList();
        int i = 0;
        if (this.recentColorValues != null) {
            i = this.recentColorValues.size();
        }
        for (int i2 = 0; i2 < 6; i2++) {
            View childAt = this.mRecentColorListLayout.getChildAt(i2);
            if (i2 < i) {
                setImageColor(childAt, (Integer) this.recentColorValues.get(i2));
                childAt.setContentDescription(this.mColorDescription[i2] + ", " + getResources().getString(17040418) + ", " + getResources().getString(17040419));
                childAt.setFocusable(true);
                childAt.setClickable(true);
                childAt.setVisibility(0);
            } else {
                setImageColor(childAt, Integer.valueOf(getResources().getColor(17170843)));
                childAt.setFocusable(false);
                childAt.setClickable(false);
                childAt.setVisibility(0);
            }
            this.recentColorItemViews.add(childAt);
        }
        if (this.mRecentColorInfo.getCurrentColor() != null) {
            ((GradientDrawable) this.mCurrentColorView.getBackground()).setColor(this.mRecentColorInfo.getCurrentColor().intValue());
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(this.mRecentColorInfo.getCurrentColor().intValue());
            mapColorOnColorWheel(this.mRecentColorInfo.getCurrentColor().intValue());
        } else if (i != 0) {
            ((GradientDrawable) this.mCurrentColorView.getBackground()).setColor(((Integer) this.recentColorValues.get(0)).intValue());
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(((Integer) this.recentColorValues.get(0)).intValue());
            mapColorOnColorWheel(((Integer) this.recentColorValues.get(0)).intValue());
        }
        if (this.mRecentColorInfo.getNewColor() != null) {
            ((GradientDrawable) this.mPickedColorView.getBackground()).setColor(this.mRecentColorInfo.getNewColor().intValue());
            mapColorOnColorWheel(this.mRecentColorInfo.getNewColor().intValue());
        }
    }
}
