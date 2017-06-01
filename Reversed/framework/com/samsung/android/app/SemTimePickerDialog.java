package com.samsung.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.C0717R;
import com.samsung.android.widget.SemAnimationListener;
import com.samsung.android.widget.SemTimePicker;
import com.samsung.android.widget.SemTimePicker.OnTimeChangedListener;

public class SemTimePickerDialog extends AlertDialog implements OnClickListener, OnTimeChangedListener {
    private static final String HOUR = "hour";
    private static final String IS_24_HOUR = "is24hour";
    private static final String MINUTE = "minute";
    private static final boolean isNovel = SystemProperties.get("ro.product.name").startsWith("novel");
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private InputMethodManager mImm;
    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private boolean mIsStartAnimation;
    private final SemTimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetCallback;

    class C09951 implements OnFocusChangeListener {
        C09951() {
        }

        public void onFocusChange(View view, boolean z) {
            if (SemTimePickerDialog.this.mTimePicker.isEditTextMode() && z) {
                SemTimePickerDialog.this.mTimePicker.setEditTextMode(false);
            }
        }
    }

    class C09962 implements SemAnimationListener {
        C09962() {
        }

        public void onAnimationEnd() {
            SemTimePickerDialog.this.mIsStartAnimation = false;
        }
    }

    public interface OnTimeSetListener {
        void onTimeSet(SemTimePicker semTimePicker, int i, int i2);
    }

    public SemTimePickerDialog(Context context, int i, OnTimeSetListener onTimeSetListener, int i2, int i3, boolean z) {
        super(context, resolveDialogTheme(context, i));
        this.mBtnFocusChangeListener = new C09951();
        this.mTimeSetCallback = onTimeSetListener;
        this.mInitialHourOfDay = i2;
        this.mInitialMinute = i3;
        this.mIs24HourView = z;
        Context context2 = getContext();
        View inflate = LayoutInflater.from(context2).inflate((int) C0717R.layout.sem_time_picker_spinner_dialog, null);
        setView(inflate);
        getWindow().setGravity(80);
        getWindow().setLayout(-2, -2);
        getWindow().setDimAmount(0.3f);
        getWindow().addFlags(2);
        setButton(-1, context2.getString(C0717R.string.done_label), this);
        setButton(-2, context2.getString(C0717R.string.cancel), this);
        this.mTimePicker = (SemTimePicker) inflate.findViewById(C0717R.id.timePicker);
        this.mTimePicker.setIs24HourView(Boolean.valueOf(this.mIs24HourView));
        this.mTimePicker.setHour(this.mInitialHourOfDay);
        this.mTimePicker.setMinute(this.mInitialMinute);
        this.mTimePicker.setOnTimeChangedListener(this);
        updateTitle();
        this.mImm = (InputMethodManager) getContext().getSystemService("input_method");
        Activity scanForActivity = scanForActivity(context);
        if (scanForActivity != null && scanForActivity.isInMultiWindowMode() && isFreeFormWindow(scanForActivity)) {
            getWindow().getAttributes().windowAnimations = C0717R.style.SemBottomSheetFadeAnimation;
        }
    }

    public SemTimePickerDialog(Context context, OnTimeSetListener onTimeSetListener, int i, int i2, boolean z) {
        this(context, 0, onTimeSetListener, i, i2, z);
    }

    private boolean isFreeFormWindow(Activity activity) {
        if (activity != null) {
            try {
                if (activity.getWindowStackId() == 2) {
                    return true;
                }
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    static int resolveDialogTheme(Context context, int i) {
        return i == 0 ? C0717R.style.SemPickerDialogTheme_TimePicker : i;
    }

    private static Activity scanForActivity(Context context) {
        return context == null ? null : context instanceof Activity ? context : context instanceof ContextWrapper ? scanForActivity(context.getBaseContext()) : null;
    }

    private void updateTitle() {
        setTitle(C0717R.string.time_picker_dialog_title);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mImm != null) {
            this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        switch (i) {
            case -2:
                cancel();
                break;
            case -1:
                if (!this.mIsStartAnimation) {
                    if (this.mTimeSetCallback != null) {
                        this.mTimePicker.clearFocus();
                        this.mTimeSetCallback.onTimeSet(this.mTimePicker, this.mTimePicker.getHour(), this.mTimePicker.getMinute());
                    }
                    dismiss();
                    break;
                }
                return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getButton(-1).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        getButton(-2).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        this.mIsStartAnimation = true;
        this.mTimePicker.startAnimation(283, new C09962());
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        int i = bundle.getInt(HOUR);
        int i2 = bundle.getInt(MINUTE);
        this.mTimePicker.setIs24HourView(Boolean.valueOf(bundle.getBoolean(IS_24_HOUR)));
        this.mTimePicker.setHour(i);
        this.mTimePicker.setMinute(i2);
        updateTitle();
    }

    public Bundle onSaveInstanceState() {
        BaseBundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putInt(HOUR, this.mTimePicker.getHour());
        onSaveInstanceState.putInt(MINUTE, this.mTimePicker.getMinute());
        onSaveInstanceState.putBoolean(IS_24_HOUR, this.mTimePicker.is24HourView());
        return onSaveInstanceState;
    }

    public void onTimeChanged(SemTimePicker semTimePicker, int i, int i2) {
    }

    public void updateTime(int i, int i2) {
        this.mTimePicker.setHour(i);
        this.mTimePicker.setMinute(i2);
    }
}
