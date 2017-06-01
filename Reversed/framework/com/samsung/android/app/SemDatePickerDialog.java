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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.android.internal.C0717R;
import com.samsung.android.widget.SemDatePicker;
import com.samsung.android.widget.SemDatePicker.OnDateChangedListener;
import com.samsung.android.widget.SemDatePicker.ValidationCallback;
import java.util.Calendar;

public class SemDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    private final OnFocusChangeListener mBtnFocusChangeListener;
    private final Calendar mCalendar;
    private final SemDatePicker mDatePicker;
    private final OnDateSetListener mDateSetListener;
    private InputMethodManager mImm;
    private final ValidationCallback mValidationCallback;

    class C09921 implements OnFocusChangeListener {
        C09921() {
        }

        public void onFocusChange(View view, boolean z) {
            if (SemDatePickerDialog.this.mDatePicker.isEditTextMode() && z) {
                SemDatePickerDialog.this.mDatePicker.setEditTextMode(false);
            }
        }
    }

    class C09932 implements ValidationCallback {
        C09932() {
        }

        public void onValidationChanged(boolean z) {
            TextView button = SemDatePickerDialog.this.getButton(-1);
            if (button != null) {
                button.setEnabled(z);
            }
        }
    }

    public interface OnDateSetListener {
        void onDateSet(SemDatePicker semDatePicker, int i, int i2, int i3);
    }

    public SemDatePickerDialog(Context context, int i, OnDateSetListener onDateSetListener, int i2, int i3, int i4) {
        super(context, resolveDialogTheme(context, i));
        this.mBtnFocusChangeListener = new C09921();
        this.mValidationCallback = new C09932();
        this.mDateSetListener = onDateSetListener;
        this.mCalendar = Calendar.getInstance();
        Context context2 = getContext();
        View inflate = LayoutInflater.from(context2).inflate((int) C0717R.layout.sem_date_picker_dialog_mtrl, null);
        setView(inflate);
        getWindow().setGravity(80);
        getWindow().setLayout(-2, -2);
        getWindow().setDimAmount(0.3f);
        getWindow().addFlags(2);
        setButton(-1, context2.getString(C0717R.string.done_label), this);
        setButton(-2, context2.getString(C0717R.string.cancel), this);
        this.mDatePicker = (SemDatePicker) inflate.findViewById(C0717R.id.sem_datePicker);
        this.mDatePicker.init(i2, i3, i4, this);
        this.mDatePicker.setValidationCallback(this.mValidationCallback);
        this.mImm = (InputMethodManager) context2.getSystemService("input_method");
        Activity scanForActivity = scanForActivity(context);
        if (scanForActivity != null && scanForActivity.isInMultiWindowMode() && isFreeFormWindow(scanForActivity)) {
            getWindow().getAttributes().windowAnimations = C0717R.style.SemBottomSheetFadeAnimation;
        }
    }

    public SemDatePickerDialog(Context context, OnDateSetListener onDateSetListener, int i, int i2, int i3) {
        this(context, 0, onDateSetListener, i, i2, i3);
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
        return i == 0 ? C0717R.style.SemPickerDialogTheme : i;
    }

    private static Activity scanForActivity(Context context) {
        return context == null ? null : context instanceof Activity ? context : context instanceof ContextWrapper ? scanForActivity(context.getBaseContext()) : null;
    }

    private void updateTitle(int i, int i2, int i3) {
    }

    public SemDatePicker getDatePicker() {
        return this.mDatePicker;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.mImm != null) {
            this.mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        switch (i) {
            case -2:
                cancel();
                return;
            case -1:
                if (this.mDateSetListener != null) {
                    this.mDatePicker.clearFocus();
                    this.mDateSetListener.onDateSet(this.mDatePicker, this.mDatePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getButton(-1).setOnFocusChangeListener(this.mBtnFocusChangeListener);
        getButton(-2).setOnFocusChangeListener(this.mBtnFocusChangeListener);
    }

    public void onDateChanged(SemDatePicker semDatePicker, int i, int i2, int i3) {
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mDatePicker.init(bundle.getInt("year"), bundle.getInt(MONTH), bundle.getInt(DAY), this);
    }

    public Bundle onSaveInstanceState() {
        BaseBundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putInt("year", this.mDatePicker.getYear());
        onSaveInstanceState.putInt(MONTH, this.mDatePicker.getMonth());
        onSaveInstanceState.putInt(DAY, this.mDatePicker.getDayOfMonth());
        return onSaveInstanceState;
    }

    public void updateDate(int i, int i2, int i3) {
        this.mDatePicker.updateDate(i, i2, i3);
    }
}
