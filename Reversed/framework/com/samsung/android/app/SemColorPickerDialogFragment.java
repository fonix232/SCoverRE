package com.samsung.android.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.android.internal.C0717R;
import com.samsung.android.widget.SemColorPicker;
import com.samsung.android.widget.SemColorPicker.OnColorChangedListener;

public class SemColorPickerDialogFragment extends DialogFragment implements OnClickListener {
    private static final String KEY_CURRENT_COLOR = "current_color";
    private static final String KEY_RECENTLY_USED_COLORS = "recently_used_colors";
    static final String TAG = "SemColorPickerDialogFragment";
    private AlertDialog mAlertDialog;
    SemColorPicker mColorPicker;
    private View mColorPickerDialogView;
    private Integer mCurrentColor;
    private Integer mNewColor;
    private OnColorChangedListener mOnColorChangedListener;
    private OnColorSetListener mOnColorSetListener;
    private int[] mRecentlyUsedColors;

    public interface OnColorSetListener {
        void onColorSet(int i);
    }

    public SemColorPickerDialogFragment() {
        this.mCurrentColor = null;
        this.mNewColor = null;
        this.mRecentlyUsedColors = null;
    }

    public SemColorPickerDialogFragment(OnColorSetListener onColorSetListener) {
        this.mCurrentColor = null;
        this.mNewColor = null;
        this.mRecentlyUsedColors = null;
        this.mOnColorSetListener = onColorSetListener;
    }

    public SemColorPickerDialogFragment(OnColorSetListener onColorSetListener, int i) {
        this(onColorSetListener);
        this.mCurrentColor = Integer.valueOf(i);
    }

    public SemColorPickerDialogFragment(OnColorSetListener onColorSetListener, int i, int[] iArr) {
        this(onColorSetListener);
        this.mCurrentColor = Integer.valueOf(i);
        this.mRecentlyUsedColors = iArr;
    }

    public SemColorPickerDialogFragment(OnColorSetListener onColorSetListener, int[] iArr) {
        this(onColorSetListener);
        this.mRecentlyUsedColors = iArr;
    }

    public SemColorPicker getColorPicker() {
        return this.mColorPicker;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case -2:
                dialogInterface.dismiss();
                return;
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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.mRecentlyUsedColors = bundle.getIntArray(KEY_RECENTLY_USED_COLORS);
            this.mCurrentColor = (Integer) bundle.getSerializable(KEY_CURRENT_COLOR);
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Context context = getContext();
        this.mColorPickerDialogView = LayoutInflater.from(getActivity()).inflate((int) C0717R.layout.sem_color_picker_dialog, null);
        this.mColorPicker = (SemColorPicker) this.mColorPickerDialogView.findViewById(C0717R.id.sem_colorpicker_content_view);
        if (this.mCurrentColor != null) {
            this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mCurrentColor);
        }
        if (this.mNewColor != null) {
            this.mColorPicker.getRecentColorInfo().setNewColor(this.mNewColor);
        }
        if (this.mRecentlyUsedColors != null) {
            this.mColorPicker.getRecentColorInfo().saveRecentColorInfo(this.mRecentlyUsedColors);
        }
        this.mColorPicker.updateRecentColorLayout();
        this.mColorPicker.setOnColorChangedListener(this.mOnColorChangedListener);
        this.mAlertDialog = new Builder(getActivity(), C0717R.style.Theme_DeviceDefault_Light_Dialog).setView(this.mColorPickerDialogView).setPositiveButton(context.getString(C0717R.string.done_label), this).setNegativeButton(context.getString(C0717R.string.cancel), this).create();
        return this.mAlertDialog;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mColorPicker.getRecentColorInfo().getSelectedColor());
        bundle.putIntArray(KEY_RECENTLY_USED_COLORS, this.mRecentlyUsedColors);
        bundle.putSerializable(KEY_CURRENT_COLOR, this.mCurrentColor);
    }

    public void setNewColor(Integer num) {
        this.mNewColor = num;
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.mOnColorChangedListener = onColorChangedListener;
    }
}
