package com.samsung.android.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.C0717R;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CustomBootMsgDialog extends Dialog {
    private static final int RESET_BIG_GEAR_DEGREE = 900;
    final String TAG = "CustomBootMsgDialog";
    private Runnable mAnimationRunnable = new C09891();
    private boolean mAnimationRunning = false;
    private View mBigGear;
    int mCurrent = 0;
    private Handler mHandler;
    int mMax = 0;
    private long mPreviousTime;
    ProgressBar mProgressBar = null;
    private View mSmallGear;
    TextView mUpgradeProgressMsg = null;

    class C09891 implements Runnable {
        C09891() {
        }

        public void run() {
            if (CustomBootMsgDialog.this.mBigGear != null && CustomBootMsgDialog.this.mSmallGear != null) {
                long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                long -get4 = currentAnimationTimeMillis - CustomBootMsgDialog.this.mPreviousTime;
                CustomBootMsgDialog.this.mPreviousTime = currentAnimationTimeMillis;
                if (-get4 != currentAnimationTimeMillis) {
                    float rotation = CustomBootMsgDialog.this.mBigGear.getRotation();
                    float rotation2 = CustomBootMsgDialog.this.mSmallGear.getRotation();
                    if (rotation >= 900.0f) {
                        CustomBootMsgDialog.this.mBigGear.setRotation(0.0f);
                        CustomBootMsgDialog.this.mSmallGear.setRotation(0.0f);
                    } else {
                        CustomBootMsgDialog.this.mBigGear.setRotation(((((float) -get4) * 30.0f) / 1000.0f) + rotation);
                        CustomBootMsgDialog.this.mSmallGear.setRotation(((((float) -get4) * -45.0f) / 1000.0f) + rotation2);
                    }
                }
                if (CustomBootMsgDialog.this.mAnimationRunning) {
                    CustomBootMsgDialog.this.mBigGear.postOnAnimationDelayed(this, 32);
                }
            }
        }
    }

    class C09912 implements OnPreDrawListener {

        class C09901 implements Runnable {
            C09901() {
            }

            public void run() {
                CustomBootMsgDialog.this.mPreviousTime = 0;
                CustomBootMsgDialog.this.mAnimationRunning = true;
                CustomBootMsgDialog.this.mBigGear.postOnAnimation(CustomBootMsgDialog.this.mAnimationRunnable);
            }
        }

        C09912() {
        }

        public boolean onPreDraw() {
            CustomBootMsgDialog.this.mBigGear.getViewTreeObserver().removeOnPreDrawListener(this);
            CustomBootMsgDialog.this.mHandler.postDelayed(new C09901(), 1000);
            return false;
        }
    }

    public CustomBootMsgDialog(Context context, int i) {
        LayoutParams attributes;
        View inflate;
        TextView textView;
        Throwable th;
        super(context, C0717R.style.Theme_Light_NoTitleBar_Fullscreen);
        if (SystemProperties.get("sys.config.fota_low_brightness").equals("true")) {
            String str = "/sys/class/leds/lcd-backlight/brightness";
            FileOutputStream fileOutputStream = null;
            try {
                Slog.m42e("CustomBootMsgDialog", "/sys/class/leds/lcd-backlight/brightness is set at CustomBootMsgDialog");
                FileOutputStream fileOutputStream2 = new FileOutputStream("/sys/class/leds/lcd-backlight/brightness");
                try {
                    fileOutputStream2.write("110".getBytes());
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (FileNotFoundException e2) {
                    fileOutputStream = fileOutputStream2;
                    Slog.m42e("CustomBootMsgDialog", "/sys/class/leds/lcd-backlight/brightness is not found");
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    attributes = getWindow().getAttributes();
                    attributes.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
                    attributes.flags |= 1408;
                    attributes.screenOrientation = 5;
                    attributes.height = -1;
                    attributes.width = -1;
                    getWindow().setAttributes(attributes);
                    inflate = LayoutInflater.from(context).inflate((int) C0717R.layout.tw_upgrade_dialog_layout, null);
                    textView = (TextView) inflate.findViewById(C0717R.id.title_msg);
                    this.mUpgradeProgressMsg = (TextView) inflate.findViewById(C0717R.id.upgrade_progress_msg);
                    this.mProgressBar = (ProgressBar) inflate.findViewById(C0717R.id.upgrade_progressbar);
                    this.mBigGear = inflate.findViewById(C0717R.id.fota_big_gear);
                    this.mSmallGear = inflate.findViewById(C0717R.id.fota_small_gear);
                    this.mHandler = new Handler();
                    this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new C09912());
                    textView.setText(context.getResources().getString(i));
                    setContentView(inflate);
                } catch (IOException e4) {
                    fileOutputStream = fileOutputStream2;
                    try {
                        Slog.m42e("CustomBootMsgDialog", "/sys/class/leds/lcd-backlight/brightness read/write error");
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e5) {
                            }
                        }
                        attributes = getWindow().getAttributes();
                        attributes.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
                        attributes.flags |= 1408;
                        attributes.screenOrientation = 5;
                        attributes.height = -1;
                        attributes.width = -1;
                        getWindow().setAttributes(attributes);
                        inflate = LayoutInflater.from(context).inflate((int) C0717R.layout.tw_upgrade_dialog_layout, null);
                        textView = (TextView) inflate.findViewById(C0717R.id.title_msg);
                        this.mUpgradeProgressMsg = (TextView) inflate.findViewById(C0717R.id.upgrade_progress_msg);
                        this.mProgressBar = (ProgressBar) inflate.findViewById(C0717R.id.upgrade_progressbar);
                        this.mBigGear = inflate.findViewById(C0717R.id.fota_big_gear);
                        this.mSmallGear = inflate.findViewById(C0717R.id.fota_small_gear);
                        this.mHandler = new Handler();
                        this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new C09912());
                        textView.setText(context.getResources().getString(i));
                        setContentView(inflate);
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e6) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e7) {
                Slog.m42e("CustomBootMsgDialog", "/sys/class/leds/lcd-backlight/brightness is not found");
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                attributes = getWindow().getAttributes();
                attributes.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
                attributes.flags |= 1408;
                attributes.screenOrientation = 5;
                attributes.height = -1;
                attributes.width = -1;
                getWindow().setAttributes(attributes);
                inflate = LayoutInflater.from(context).inflate((int) C0717R.layout.tw_upgrade_dialog_layout, null);
                textView = (TextView) inflate.findViewById(C0717R.id.title_msg);
                this.mUpgradeProgressMsg = (TextView) inflate.findViewById(C0717R.id.upgrade_progress_msg);
                this.mProgressBar = (ProgressBar) inflate.findViewById(C0717R.id.upgrade_progressbar);
                this.mBigGear = inflate.findViewById(C0717R.id.fota_big_gear);
                this.mSmallGear = inflate.findViewById(C0717R.id.fota_small_gear);
                this.mHandler = new Handler();
                this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new C09912());
                textView.setText(context.getResources().getString(i));
                setContentView(inflate);
            } catch (IOException e8) {
                Slog.m42e("CustomBootMsgDialog", "/sys/class/leds/lcd-backlight/brightness read/write error");
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                attributes = getWindow().getAttributes();
                attributes.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
                attributes.flags |= 1408;
                attributes.screenOrientation = 5;
                attributes.height = -1;
                attributes.width = -1;
                getWindow().setAttributes(attributes);
                inflate = LayoutInflater.from(context).inflate((int) C0717R.layout.tw_upgrade_dialog_layout, null);
                textView = (TextView) inflate.findViewById(C0717R.id.title_msg);
                this.mUpgradeProgressMsg = (TextView) inflate.findViewById(C0717R.id.upgrade_progress_msg);
                this.mProgressBar = (ProgressBar) inflate.findViewById(C0717R.id.upgrade_progressbar);
                this.mBigGear = inflate.findViewById(C0717R.id.fota_big_gear);
                this.mSmallGear = inflate.findViewById(C0717R.id.fota_small_gear);
                this.mHandler = new Handler();
                this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new C09912());
                textView.setText(context.getResources().getString(i));
                setContentView(inflate);
            }
        }
        attributes = getWindow().getAttributes();
        attributes.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
        attributes.flags |= 1408;
        attributes.screenOrientation = 5;
        attributes.height = -1;
        attributes.width = -1;
        getWindow().setAttributes(attributes);
        inflate = LayoutInflater.from(context).inflate((int) C0717R.layout.tw_upgrade_dialog_layout, null);
        textView = (TextView) inflate.findViewById(C0717R.id.title_msg);
        this.mUpgradeProgressMsg = (TextView) inflate.findViewById(C0717R.id.upgrade_progress_msg);
        this.mProgressBar = (ProgressBar) inflate.findViewById(C0717R.id.upgrade_progressbar);
        this.mBigGear = inflate.findViewById(C0717R.id.fota_big_gear);
        this.mSmallGear = inflate.findViewById(C0717R.id.fota_small_gear);
        this.mHandler = new Handler();
        this.mBigGear.getViewTreeObserver().addOnPreDrawListener(new C09912());
        textView.setText(context.getResources().getString(i));
        setContentView(inflate);
    }

    private void parseDigit(String str) {
        int i;
        int i2 = 0;
        int i3 = 0;
        ArrayList arrayList = new ArrayList();
        for (i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isDigit(charAt)) {
                i3 = (i3 * 10) + Character.getNumericValue(charAt);
            } else {
                if (i3 != 0) {
                    arrayList.add(Integer.valueOf(i3));
                    i2++;
                }
                i3 = 0;
            }
        }
        i3 = 0;
        for (i = 0; i < arrayList.size(); i++) {
            if (i3 == 0) {
                i3 = ((Integer) arrayList.get(i)).intValue();
            } else {
                this.mMax = i3 > ((Integer) arrayList.get(i)).intValue() ? i3 : ((Integer) arrayList.get(i)).intValue();
                this.mCurrent = i3 <= ((Integer) arrayList.get(i)).intValue() ? i3 : ((Integer) arrayList.get(i)).intValue();
            }
        }
    }

    public void dismiss() {
        Log.m31e("CustomBootMsgDialog", "dismiss CustomBootMsg ");
        this.mAnimationRunning = false;
        if (this.mBigGear != null) {
            this.mBigGear.clearAnimation();
        }
        if (this.mSmallGear != null) {
            this.mSmallGear.clearAnimation();
        }
        super.dismiss();
    }

    public void setProgress(String str) {
        Log.m31e("CustomBootMsgDialog", "Booting " + str);
        if (str == null) {
            this.mProgressBar.setVisibility(8);
            return;
        }
        parseDigit(str);
        if (this.mMax != 0) {
            this.mProgressBar.setVisibility(0);
            this.mProgressBar.setMax(this.mMax);
            this.mProgressBar.setProgress(this.mCurrent);
        } else {
            this.mProgressBar.setVisibility(8);
        }
        this.mUpgradeProgressMsg.setText((CharSequence) str);
    }
}
