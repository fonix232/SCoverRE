package com.samsung.android.desktopmode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.samsung.android.framework.res.C0078R;

public class DesktopModeLaunchConfirmDialog extends AlertDialog {
    private OnClickListener mListener;
    private final View.OnClickListener mViewOnClickListener = new C00511();

    class C00511 implements View.OnClickListener {
        C00511() {
        }

        public void onClick(View view) {
            if (DesktopModeLaunchConfirmDialog.this.mListener != null) {
                int id = view.getId();
                if (id == C0078R.id.dex_dialog_launch_button_positive) {
                    DesktopModeLaunchConfirmDialog.this.mListener.onClick(DesktopModeLaunchConfirmDialog.this, -1);
                } else if (id == C0078R.id.dex_dialog_launch_button_negative) {
                    DesktopModeLaunchConfirmDialog.this.mListener.onClick(DesktopModeLaunchConfirmDialog.this, -2);
                }
            }
        }
    }

    public DesktopModeLaunchConfirmDialog(Context context) {
        super(context);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0078R.layout.desktop_mode_dialog_launch_confirm);
        ((TextView) findViewById(C0078R.id.title)).setText(Html.fromHtml(getContext().getResources().getString(C0078R.string.dex_intro_welcome_to, new Object[]{"<br/><b>" + getContext().getResources().getString(C0078R.string.dex_intro_samsung_dex) + "</b>"}), 0));
        Button button = (Button) findViewById(C0078R.id.dex_dialog_launch_button_positive);
        button.setOnClickListener(this.mViewOnClickListener);
        button.setWidth((int) (((float) getContext().getResources().getDisplayMetrics().widthPixels) * getContext().getResources().getFraction(C0078R.fraction.dex_dialog_button_fraction, 1, 1)));
        Button button2 = (Button) findViewById(C0078R.id.dex_dialog_launch_button_negative);
        button2.setOnClickListener(this.mViewOnClickListener);
        button2.setWidth((int) (((float) getContext().getResources().getDisplayMetrics().widthPixels) * getContext().getResources().getFraction(C0078R.fraction.dex_dialog_button_fraction, 1, 1)));
    }

    public DesktopModeLaunchConfirmDialog setListener(OnClickListener onClickListener) {
        this.mListener = onClickListener;
        return this;
    }
}
