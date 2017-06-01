package com.samsung.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import com.android.internal.C0717R;
import com.samsung.android.widget.SemColorPicker;

public class SemColorPickerDialog extends AlertDialog implements OnClickListener {
    final String TAG;
    SemColorPicker mColorPicker;
    private Integer mCurrentColor;
    private OnColorSetListener mOnColorSetListener;

    public interface OnColorSetListener {
        void onColorSet(int i);
    }

    public SemColorPickerDialog(Context context, OnColorSetListener onColorSetListener) {
        super(context, C0717R.style.Theme_DeviceDefault_Light_Dialog);
        this.TAG = "SemColorPickerDialog";
        this.mCurrentColor = null;
        this.mOnColorSetListener = onColorSetListener;
        Context context2 = getContext();
        View inflate = LayoutInflater.from(getContext()).inflate((int) C0717R.layout.sem_color_picker_dialog, null);
        setView(inflate);
        setButton(-1, context2.getString(C0717R.string.done_label), this);
        setButton(-2, context2.getString(C0717R.string.cancel), this);
        requestWindowFeature(1);
        this.mColorPicker = (SemColorPicker) inflate.findViewById(C0717R.id.sem_colorpicker_content_view);
    }

    public SemColorPickerDialog(Context context, OnColorSetListener onColorSetListener, int i) {
        this(context, onColorSetListener);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(Integer.valueOf(i));
        this.mCurrentColor = Integer.valueOf(i);
        this.mColorPicker.updateRecentColorLayout();
    }

    public SemColorPickerDialog(Context context, OnColorSetListener onColorSetListener, int i, int[] iArr) {
        this(context, onColorSetListener);
        this.mColorPicker.getRecentColorInfo().saveRecentColorInfo(iArr);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(Integer.valueOf(i));
        this.mCurrentColor = Integer.valueOf(i);
        this.mColorPicker.updateRecentColorLayout();
    }

    public SemColorPickerDialog(Context context, OnColorSetListener onColorSetListener, int[] iArr) {
        this(context, onColorSetListener);
        this.mColorPicker.getRecentColorInfo().saveRecentColorInfo(iArr);
        this.mColorPicker.updateRecentColorLayout();
    }

    public SemColorPicker getColorPicker() {
        return this.mColorPicker;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case -1:
                this.mColorPicker.saveSelectedColor();
                if (this.mOnColorSetListener == null) {
                    return;
                }
                if (this.mColorPicker.isUserInputValid() || this.mCurrentColor == null) {
                    this.mOnColorSetListener.onColorSet(this.mColorPicker.getRecentColorInfo().getSelectedColor().intValue());
                    return;
                } else {
                    this.mOnColorSetListener.onColorSet(this.mCurrentColor.intValue());
                    return;
                }
            default:
                return;
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setNewColor(Integer num) {
        this.mColorPicker.getRecentColorInfo().setNewColor(num);
        this.mColorPicker.updateRecentColorLayout();
    }
}
