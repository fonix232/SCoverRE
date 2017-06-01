package com.samsung.android.app.ledcover.creationpattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.gms.common.ConnectionResult;
import com.samsung.android.app.ledcover.C0198R;
import com.samsung.android.app.ledcover.common.LCoverSingleton;
import com.samsung.android.app.ledcover.common.SLog;
import com.samsung.android.app.ledcover.common.Utils;
import com.samsung.android.app.ledcover.creationpattern.ShowButtonBackgroundSettingObserver.OnSettingValueChangeListener;
import com.samsung.android.app.ledcover.db.LCoverDbAccessor;
import com.samsung.android.app.ledcover.info.Defines;
import com.samsung.android.app.ledcover.info.LCoverDotInfo;
import com.samsung.android.app.ledcover.info.LCoverDrawActionInfo;
import com.samsung.android.app.ledcover.info.LCoverIconInfo;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import java.util.ArrayList;
import java.util.Stack;

public class LCoverCreatePatternActivity extends Activity {
    public static final String TAG = "[LED_COVER]LCoverCreatePatternActivity ";
    private LCoverIconInfo UserData;
    private String arrayName;
    private EditText arrayNameEditText;
    private Stack<LCoverDotInfo> dotLedStack;
    private int dotPointer;
    private LinearLayout drawBtn;
    private ImageView drawImg;
    private TextView drawTxt;
    private LinearLayout edit_cancle;
    private LinearLayout edit_done;
    private int endPointer;
    private LinearLayout eraseBtn;
    private ImageView eraseImg;
    private TextView eraseTxt;
    private GridView gv_prevMatrix;
    private boolean isDrawning;
    private int lastDotLedListSize;
    private View mActionBar;
    private ArrayList<LCoverIconInfo> mCustomInfoList;
    private LCoverDbAccessor mLedCoverDbAccessor;
    private LedMatrixAdapter mLedMatrixAdapter;
    private boolean mModifyData;
    private OnClickListener mOnClick;
    private final OnSettingValueChangeListener mOnSettingValueChangeListener;
    private OnTouchListener mOnTouchView;
    private ShowButtonBackgroundSettingObserver mShowButtonBackgroundSettingObserver;
    private boolean modifyFile;
    private Stack<LCoverDrawActionInfo> reDoStack;
    private LinearLayout rec_drawBtn;
    private LinearLayout rec_eraseBtn;
    private LinearLayout rec_redoBtn;
    private LinearLayout rec_rotate;
    private LinearLayout rec_undoBtn;
    private LinearLayout redoBtn;
    private ImageView redoImg;
    private boolean redoKey;
    private TextView redoTxt;
    private LinearLayout rotate;
    private ImageView rotateImg;
    private TextView rotateTxt;
    private boolean showButtonshape_modify;
    private boolean startModify;
    private int startPointer;
    private ScrollView sv_draw;
    private int touchIconID;
    private int touchTextID;
    private Stack<LCoverDrawActionInfo> unDoStack;
    private LinearLayout undoBtn;
    private boolean undoKey;
    private ImageView untoImg;
    private TextView untoTxt;

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.2 */
    class C02382 implements TextWatcher {
        C02382() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (LCoverCreatePatternActivity.this.arrayName.equals(s.toString())) {
                LCoverCreatePatternActivity.this.mModifyData = false;
            } else {
                LCoverCreatePatternActivity.this.mModifyData = true;
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.3 */
    class C02393 implements Runnable {
        C02393() {
        }

        public void run() {
            LCoverCreatePatternActivity.this.arrayNameEditText.setSelection(LCoverCreatePatternActivity.this.arrayNameEditText.getText().length());
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.4 */
    class C02404 implements OnClickListener {
        C02404() {
        }

        public void onClick(View v) {
            if (!LCoverCreatePatternActivity.this.isDrawning) {
                if (LCoverCreatePatternActivity.this.touchTextID != 0) {
                    ((TextView) LCoverCreatePatternActivity.this.findViewById(LCoverCreatePatternActivity.this.touchTextID)).setSelected(false);
                }
                int i;
                switch (v.getId()) {
                    case C0198R.id.edit_cancle_btn /*2131624023*/:
                        SLog.m12v(LCoverCreatePatternActivity.TAG, "[Cancle] Cancle creation Icon");
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_CANCEL, "Cancel");
                        if (LCoverCreatePatternActivity.this.mModifyData) {
                            LCoverCreatePatternActivity.this.showDestroyDialog();
                        } else {
                            LCoverCreatePatternActivity.this.finish();
                        }
                    case C0198R.id.edit_done /*2131624025*/:
                        SLog.m12v(LCoverCreatePatternActivity.TAG, "[Done] save new creation Icon ");
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_SAVE, "Save");
                        if (LCoverCreatePatternActivity.this.saveIconInfo()) {
                            LCoverCreatePatternActivity.this.finish();
                        }
                    case C0198R.id.pattern_draw /*2131624040*/:
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_DRAW, "Draw");
                        LCoverCreatePatternActivity.this.initMenuColor();
                        LCoverCreatePatternActivity.this.UserData.setEnableDraw(true);
                        LCoverCreatePatternActivity.this.drawTxt.setTextColor(LCoverCreatePatternActivity.this.getResources().getColor(C0198R.color.dot_create_menu_select));
                        LCoverCreatePatternActivity.this.drawTxt.setSelected(true);
                        LCoverCreatePatternActivity.this.drawImg.setBackgroundTintList(ColorStateList.valueOf(LCoverCreatePatternActivity.this.getResources().getColor(C0198R.color.dot_create_menu_select)));
                        LCoverCreatePatternActivity.this.touchIconID = LCoverCreatePatternActivity.this.drawImg.getId();
                        LCoverCreatePatternActivity.this.touchTextID = LCoverCreatePatternActivity.this.drawTxt.getId();
                    case C0198R.id.pattern_erase /*2131624044*/:
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_ERASER, "Erase");
                        LCoverCreatePatternActivity.this.initMenuColor();
                        LCoverCreatePatternActivity.this.UserData.setEnableDraw(false);
                        LCoverCreatePatternActivity.this.eraseTxt.setTextColor(LCoverCreatePatternActivity.this.getResources().getColor(C0198R.color.dot_create_menu_select));
                        LCoverCreatePatternActivity.this.eraseTxt.setSelected(true);
                        LCoverCreatePatternActivity.this.eraseImg.setBackgroundTintList(ColorStateList.valueOf(LCoverCreatePatternActivity.this.getResources().getColor(C0198R.color.dot_create_menu_select)));
                        LCoverCreatePatternActivity.this.touchIconID = LCoverCreatePatternActivity.this.eraseImg.getId();
                        LCoverCreatePatternActivity.this.touchTextID = LCoverCreatePatternActivity.this.eraseTxt.getId();
                    case C0198R.id.pattern_undo /*2131624048*/:
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_UNDO, "Undo");
                        if (!LCoverCreatePatternActivity.this.unDoStack.isEmpty()) {
                            LCoverCreatePatternActivity.this.undoKey = LCoverCreatePatternActivity.this.startModify = true;
                            LCoverDrawActionInfo unDoAct = (LCoverDrawActionInfo) LCoverCreatePatternActivity.this.unDoStack.pop();
                            if (LCoverCreatePatternActivity.this.dotPointer >= 0) {
                                if (LCoverCreatePatternActivity.this.redoKey) {
                                    LCoverCreatePatternActivity.this.redoKey = false;
                                }
                                for (i = unDoAct.getEndPosition(); i >= unDoAct.getStartPosition(); i--) {
                                    LCoverCreatePatternActivity.this.dotPointer = i;
                                    LCoverCreatePatternActivity.this.optionDrawDotLed();
                                }
                            }
                            LCoverCreatePatternActivity.this.touchIconID = LCoverCreatePatternActivity.this.untoImg.getId();
                            LCoverCreatePatternActivity.this.touchTextID = LCoverCreatePatternActivity.this.untoTxt.getId();
                            LCoverCreatePatternActivity.this.reDoStack.push(unDoAct);
                            LCoverCreatePatternActivity.this.updateMenuColor();
                        }
                    case C0198R.id.pattern_redo /*2131624052*/:
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_REDO, "Redo");
                        if (!LCoverCreatePatternActivity.this.reDoStack.isEmpty()) {
                            LCoverCreatePatternActivity.this.redoKey = true;
                            LCoverDrawActionInfo reDoAct = (LCoverDrawActionInfo) LCoverCreatePatternActivity.this.reDoStack.pop();
                            if (LCoverCreatePatternActivity.this.dotPointer < LCoverCreatePatternActivity.this.dotLedStack.size() && LCoverCreatePatternActivity.this.startModify) {
                                if (LCoverCreatePatternActivity.this.undoKey) {
                                    LCoverCreatePatternActivity.this.undoKey = false;
                                }
                                for (i = reDoAct.getEndPosition(); i >= reDoAct.getStartPosition(); i--) {
                                    LCoverCreatePatternActivity.this.dotPointer = i;
                                    LCoverCreatePatternActivity.this.optionDrawDotLed();
                                }
                            }
                            LCoverCreatePatternActivity.this.touchIconID = LCoverCreatePatternActivity.this.redoImg.getId();
                            LCoverCreatePatternActivity.this.touchTextID = LCoverCreatePatternActivity.this.redoTxt.getId();
                            LCoverCreatePatternActivity.this.unDoStack.push(reDoAct);
                            LCoverCreatePatternActivity.this.updateMenuColor();
                        }
                    case C0198R.id.pattern_rotate /*2131624056*/:
                        Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_ENLARGE, "Enlarge");
                        if (LCoverCreatePatternActivity.this.getResources().getConfiguration().orientation == 2) {
                            LCoverCreatePatternActivity.this.setRequestedOrientation(1);
                            return;
                        }
                        if (LCoverCreatePatternActivity.this.arrayNameEditText != null) {
                            LCoverCreatePatternActivity.this.UserData.setIconName(LCoverCreatePatternActivity.this.arrayNameEditText.getText().toString());
                            ((InputMethodManager) LCoverCreatePatternActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(LCoverCreatePatternActivity.this.arrayNameEditText.getWindowToken(), 0);
                        }
                        LCoverCreatePatternActivity.this.setRequestedOrientation(0);
                    default:
                }
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.5 */
    class C02415 implements OnTouchListener {
        C02415() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 0) {
                LCoverCreatePatternActivity.this.isDrawning = true;
            } else if (event.getAction() == 1) {
                LCoverCreatePatternActivity.this.isDrawning = false;
            }
            GridView parent = (GridView) v;
            boolean onErase = false;
            int position = parent.pointToPosition((int) event.getX(), (int) event.getY());
            View childView = parent.getChildAt(position);
            if (LCoverCreatePatternActivity.this.sv_draw != null) {
                LCoverCreatePatternActivity.this.sv_draw.requestDisallowInterceptTouchEvent(true);
            }
            if (position < Defines.getLedMatrixTotal() && position >= 0 && childView != null) {
                LCoverCreatePatternActivity.this.mModifyData = true;
                ImageView dotLedImage = (ImageView) childView;
                Animation expandIn = AnimationUtils.loadAnimation(LCoverCreatePatternActivity.this, C0198R.anim.creation_icon_ani);
                if (LCoverCreatePatternActivity.this.UserData.isEnableDraw()) {
                    if (!LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].isDotEnable()) {
                        LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].setDotEnable(true);
                        dotLedImage.setBackground(LCoverCreatePatternActivity.this.getDrawable(C0198R.drawable.led_dot_selected));
                        dotLedImage.startAnimation(expandIn);
                        LCoverCreatePatternActivity.this.dotLedStack.push(new LCoverDotInfo(position, true));
                        LCoverCreatePatternActivity.this.dotPointer = LCoverCreatePatternActivity.this.dotLedStack.size() - 1;
                        LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].setDotImageData(dotLedImage);
                    }
                } else if (LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].isDotEnable()) {
                    onErase = true;
                    LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].setDotEnable(false);
                    dotLedImage.setBackground(LCoverCreatePatternActivity.this.getDrawable(C0198R.drawable.led_dot_normal));
                    dotLedImage.startAnimation(expandIn);
                    LCoverCreatePatternActivity.this.dotLedStack.push(new LCoverDotInfo(position, false));
                    LCoverCreatePatternActivity.this.dotPointer = LCoverCreatePatternActivity.this.dotLedStack.size() - 1;
                    LCoverCreatePatternActivity.this.UserData.getDotDataClass()[position].setDotImageData(dotLedImage);
                }
                if (event.getAction() == 0 || event.getAction() == 2) {
                    if (LCoverCreatePatternActivity.this.dotLedStack.size() - LCoverCreatePatternActivity.this.lastDotLedListSize == 1) {
                        LCoverCreatePatternActivity.this.startPointer = LCoverCreatePatternActivity.this.dotPointer;
                    }
                } else if (event.getAction() == 1 && LCoverCreatePatternActivity.this.dotLedStack.size() > LCoverCreatePatternActivity.this.lastDotLedListSize) {
                    LCoverCreatePatternActivity.this.endPointer = LCoverCreatePatternActivity.this.dotPointer;
                    LCoverCreatePatternActivity.this.unDoStack.push(new LCoverDrawActionInfo(LCoverCreatePatternActivity.this.startPointer, LCoverCreatePatternActivity.this.endPointer, !onErase));
                    LCoverCreatePatternActivity.this.lastDotLedListSize = LCoverCreatePatternActivity.this.dotLedStack.size();
                    LCoverCreatePatternActivity.this.reDoStack.clear();
                    LCoverCreatePatternActivity.this.updateMenuColor();
                }
            } else if (event.getAction() == 1 && LCoverCreatePatternActivity.this.dotLedStack.size() > LCoverCreatePatternActivity.this.lastDotLedListSize) {
                SLog.m12v(LCoverCreatePatternActivity.TAG, "End Pointer : " + LCoverCreatePatternActivity.this.dotPointer);
                LCoverCreatePatternActivity.this.endPointer = LCoverCreatePatternActivity.this.dotPointer;
                LCoverCreatePatternActivity.this.unDoStack.push(new LCoverDrawActionInfo(LCoverCreatePatternActivity.this.startPointer, LCoverCreatePatternActivity.this.endPointer, null == null));
                LCoverCreatePatternActivity.this.lastDotLedListSize = LCoverCreatePatternActivity.this.dotLedStack.size();
                LCoverCreatePatternActivity.this.reDoStack.clear();
                LCoverCreatePatternActivity.this.updateMenuColor();
            }
            return false;
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.6 */
    class C02426 implements DialogInterface.OnClickListener {
        C02426() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_DIALOG_DISCARD, "Discard");
            LCoverCreatePatternActivity.this.finish();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.7 */
    class C02437 implements DialogInterface.OnClickListener {
        C02437() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_DIALOG_CANCEL, "Cancel");
            dialog.cancel();
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.8 */
    class C02448 implements DialogInterface.OnClickListener {
        C02448() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_DIALOG_SAVE, "Save");
            if (LCoverCreatePatternActivity.this.saveIconInfo()) {
                LCoverCreatePatternActivity.this.finish();
            } else {
                dialog.cancel();
            }
        }
    }

    private class NameInputFilter implements InputFilter {
        private TextView input_text_name;

        public NameInputFilter() {
            this.input_text_name = (TextView) LCoverCreatePatternActivity.this.findViewById(C0198R.id.error_text);
            this.input_text_name.setText(String.format(LCoverCreatePatternActivity.this.getResources().getString(C0198R.string.edit_text_error), new Object[]{Integer.valueOf(12)}));
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            LCoverCreatePatternActivity.this.UserData.setIconName(((new String() + dest.subSequence(0, dstart)) + source.subSequence(start, end)) + dest.subSequence(dend, dest.length()));
            int keep = 12 - (dest.length() - (dend - dstart));
            if (end > 0 && keep <= 0) {
                Editable editable = LCoverCreatePatternActivity.this.arrayNameEditText.getText();
                LCoverCreatePatternActivity.this.arrayNameEditText.setText(editable);
                LCoverCreatePatternActivity.this.arrayNameEditText.setSelection(editable.length());
                this.input_text_name.setVisibility(0);
                return C0316a.f163d;
            } else if (keep < end - start) {
                return source.subSequence(start, start + keep);
            } else {
                this.input_text_name.setVisibility(8);
                return null;
            }
        }
    }

    /* renamed from: com.samsung.android.app.ledcover.creationpattern.LCoverCreatePatternActivity.1 */
    class C04171 implements OnSettingValueChangeListener {
        C04171() {
        }

        public void onChange(boolean isOn) {
            LCoverCreatePatternActivity.this.showButtonshape_modify = isOn;
            LCoverCreatePatternActivity.this.initShowButtonBackgroundButton();
        }
    }

    public LCoverCreatePatternActivity() {
        this.dotLedStack = new Stack();
        this.unDoStack = new Stack();
        this.reDoStack = new Stack();
        this.modifyFile = true;
        this.mModifyData = false;
        this.showButtonshape_modify = false;
        this.mCustomInfoList = null;
        this.isDrawning = false;
        this.mOnSettingValueChangeListener = new C04171();
        this.redoKey = true;
        this.undoKey = true;
        this.mOnClick = new C02404();
        this.mOnTouchView = new C02415();
    }

    public void initShowButtonBackgroundButton() {
        this.rec_drawBtn = (LinearLayout) findViewById(C0198R.id.pattern_draw_rec);
        this.rec_eraseBtn = (LinearLayout) findViewById(C0198R.id.pattern_erase_rec);
        this.rec_undoBtn = (LinearLayout) findViewById(C0198R.id.pattern_undo_rec);
        this.rec_redoBtn = (LinearLayout) findViewById(C0198R.id.pattern_redo_rec);
        this.rec_rotate = (LinearLayout) findViewById(C0198R.id.pattern_rotate_rec);
        if (this.showButtonshape_modify) {
            Drawable temp = getResources().getDrawable(C0198R.drawable.led_cover_button_rectangle_item_bg);
            this.rec_drawBtn.setBackground(temp);
            this.rec_eraseBtn.setBackground(temp);
            this.rec_undoBtn.setBackground(temp);
            this.rec_redoBtn.setBackground(temp);
            this.rec_rotate.setBackground(temp);
            return;
        }
        this.rec_drawBtn.setBackground(null);
        this.rec_eraseBtn.setBackground(null);
        this.rec_undoBtn.setBackground(null);
        this.rec_redoBtn.setBackground(null);
        this.rec_rotate.setBackground(null);
    }

    public void onStart() {
        super.onStart();
        if (this.mShowButtonBackgroundSettingObserver != null) {
            this.mShowButtonBackgroundSettingObserver.setOnContentChangeListener(this.mOnSettingValueChangeListener);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(new Configuration(newConfig), getResources().getDisplayMetrics());
        SLog.m12v(TAG, "onConfigurationChanged()");
        setContentView(C0198R.layout.activity_led_cover_create_pattern);
        initMenuButton();
        if (!(this.touchIconID == 0 || this.touchTextID == 0)) {
            ImageView tempIcon = (ImageView) findViewById(this.touchIconID);
            ((TextView) findViewById(this.touchTextID)).setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
            tempIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
        }
        if (newConfig.orientation == 1) {
            SLog.m12v(TAG, "ORIENTATION_PORTRAIT");
            this.rotateImg.setBackgroundResource(C0198R.drawable.led_cover_create_ic_expand);
            this.rotateTxt.setText(C0198R.string.creation_enlarge);
        } else if (newConfig.orientation == 2) {
            SLog.m12v(TAG, "ORIENTATION_LANDSCAPE");
            this.rotateImg.setBackgroundResource(C0198R.drawable.led_cover_create_ic_reduce);
            this.rotateTxt.setText(C0198R.string.creation_reduce);
        }
        initScreen();
        initShowButtonBackgroundButton();
        updateMenuColor();
        if (this.modifyFile) {
            initEditActionBar();
        }
        SLog.m12v(TAG, "[Rotate] The dot size after rocate screen : " + this.dotLedStack.size());
        SLog.m12v(TAG, "[Rotate] The pointer after rocate screen : " + this.dotPointer);
    }

    public void setRequestedOrientation(int requestedOrientation) {
        SLog.m12v(TAG, "setRequestedOrientation()");
        super.setRequestedOrientation(requestedOrientation);
        if (requestedOrientation == 1) {
            getActionBar().show();
            getWindow().clearFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        } else if (requestedOrientation == 0) {
            getActionBar().hide();
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean z;
        getActionBar().setDisplayShowTitleEnabled(!this.modifyFile);
        ActionBar actionBar = getActionBar();
        if (this.modifyFile) {
            z = false;
        } else {
            z = true;
        }
        actionBar.setDisplayHomeAsUpEnabled(z);
        actionBar = getActionBar();
        if (this.modifyFile) {
            z = false;
        } else {
            z = true;
        }
        actionBar.setHomeButtonEnabled(z);
        if (this.modifyFile) {
            initEditActionBar();
        } else {
            getActionBar().setDisplayShowCustomEnabled(false);
            menu.add(0, 3, 0, C0198R.string.save).setShowAsAction(1);
            getActionBar().setTitle(C0198R.string.creation_title);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initEditActionBar() {
        this.mActionBar = LayoutInflater.from(getApplicationContext()).inflate(C0198R.layout.action_bar_creation_pattern, null);
        getActionBar().setCustomView(this.mActionBar);
        getActionBar().setDisplayOptions(16);
        Toolbar tool = (Toolbar) this.mActionBar.getParent();
        if (tool != null) {
            tool.setContentInsetsAbsolute(0, 0);
        }
        this.edit_cancle = (LinearLayout) findViewById(C0198R.id.edit_cancle_btn);
        this.edit_done = (LinearLayout) findViewById(C0198R.id.edit_done);
        this.edit_cancle.setOnClickListener(this.mOnClick);
        this.edit_done.setOnClickListener(this.mOnClick);
        if (this.showButtonshape_modify) {
            this.edit_cancle.setBackground(getDrawable(C0198R.drawable.action_bar_button_background_with_ripple_effect));
            this.edit_done.setBackground(getDrawable(C0198R.drawable.action_bar_button_background_with_ripple_effect));
        } else {
            this.edit_cancle.setBackground(getDrawable(C0198R.drawable.ripple_effect));
            this.edit_done.setBackground(getDrawable(C0198R.drawable.ripple_effect));
        }
        TextView doneButtonText = (TextView) findViewById(C0198R.id.edit_done_rec);
        Utils.setLargeTextSize(this, (TextView) findViewById(C0198R.id.edit_cancle_btn_rec), (float) getResources().getDimensionPixelSize(C0198R.dimen.btn_actionbar_txt_size));
        Utils.setLargeTextSize(this, doneButtonText, (float) getResources().getDimensionPixelSize(C0198R.dimen.btn_actionbar_txt_size));
        Window window = getWindow();
        window.addFlags(ExploreByTouchHelper.INVALID_ID);
        window.clearFlags(67108864);
        window.setStatusBarColor(getColor(C0198R.color.modify_icon_bg));
    }

    protected void onResume() {
        super.onResume();
        Utils.sendScreenViewSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY);
    }

    public void onBackPressed() {
        SLog.m12v(TAG, "[onBackPressed]");
        if (getResources().getConfiguration().orientation == 2) {
            setRequestedOrientation(1);
        } else if (this.mModifyData) {
            showDestroyDialog();
        } else {
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ConnectionResult.SERVICE_DISABLED /*3*/:
                SLog.m12v(TAG, "[Done] save modify creation Icon ");
                Utils.sendEventSALog(Defines.SA_SCREEN_CREATE_LED_ICON_ACTIVITY, Defines.SA_CREATE_LED_ICON_EVENT_SAVE, "Save");
                if (saveIconInfo()) {
                    finish();
                    break;
                }
                break;
            case 16908332:
                SLog.m12v(TAG, "[Cancle] Cancle creation Icon");
                if (!this.mModifyData) {
                    finish();
                    break;
                }
                showDestroyDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        SLog.m12v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        this.mLedCoverDbAccessor = LCoverSingleton.getInstance().getDbAccessor(this);
        this.mModifyData = false;
        this.mShowButtonBackgroundSettingObserver = new ShowButtonBackgroundSettingObserver(getContentResolver());
        setContentView(C0198R.layout.activity_led_cover_create_pattern);
        Intent intent = getIntent();
        this.arrayName = intent.getStringExtra("arrayName");
        int arrayID = intent.getIntExtra("arrayID", -1);
        SLog.m12v(TAG, "<<arrayName>>  : " + this.arrayName);
        SLog.m12v(TAG, "<<arrayID>>  : " + arrayID);
        initMenuButton();
        LCoverIconInfo tempUserData = LCoverSingleton.getInstance().getCustomLEDList(arrayID);
        if (tempUserData == null) {
            SLog.m12v(TAG, "<<UserData null >>");
            LCoverSingleton.getInstance().setCustomLEDList(this.mLedCoverDbAccessor.getCustomIconInfo());
            tempUserData = LCoverSingleton.getInstance().getCustomLEDList(arrayID);
        }
        if (this.arrayName.equals("new")) {
            this.modifyFile = false;
            this.UserData = new LCoverIconInfo(arrayID, this.arrayName, 0);
        } else if (tempUserData != null) {
            this.modifyFile = true;
            this.UserData = new LCoverIconInfo(tempUserData);
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (arrayID != -1) {
            initScreen();
        } else {
            SLog.m12v(TAG, "get Integer value Error");
        }
        this.UserData.setEnableDraw(true);
        this.drawTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
        this.drawImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
        this.touchIconID = this.drawImg.getId();
        this.touchTextID = this.drawTxt.getId();
        if (!this.arrayName.equals("new")) {
            this.arrayNameEditText.setText(new String(this.arrayName));
        }
        if (getResources().getConfiguration().orientation == 2) {
            setRequestedOrientation(1);
        }
    }

    protected void onDestroy() {
        if (this.UserData != null) {
            this.UserData = null;
        }
        if (this.gv_prevMatrix != null) {
            SLog.m12v(TAG, "Clear Creation Icon");
            Utils.recursiveRecycle(this.gv_prevMatrix);
            this.gv_prevMatrix.removeAllViewsInLayout();
            this.gv_prevMatrix.setOnTouchListener(null);
            this.gv_prevMatrix = null;
        }
        if (this.dotLedStack != null) {
            this.dotLedStack.clear();
            this.dotLedStack = null;
        }
        if (this.unDoStack != null) {
            this.unDoStack.clear();
            this.unDoStack = null;
        }
        if (this.reDoStack != null) {
            this.reDoStack.clear();
            this.reDoStack = null;
        }
        if (this.mShowButtonBackgroundSettingObserver != null) {
            this.mShowButtonBackgroundSettingObserver.setOnContentChangeListener(null);
            this.mShowButtonBackgroundSettingObserver.releaaseObserver();
            this.mShowButtonBackgroundSettingObserver = null;
        }
        System.gc();
        super.onDestroy();
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        SLog.m12v(TAG, "onTrimMemeory levle: " + level);
    }

    private void initScreen() {
        SLog.m12v(TAG, "initScreen()");
        this.gv_prevMatrix = (GridView) findViewById(C0198R.id.pattern_drawlayout);
        SLog.m12v(TAG, "findViewById");
        this.gv_prevMatrix.setVerticalScrollBarEnabled(false);
        SLog.m12v(TAG, "setVerticalScrollBarEnabled");
        this.mLedMatrixAdapter = new LedMatrixAdapter(this, this.UserData);
        this.gv_prevMatrix.setOnTouchListener(this.mOnTouchView);
        this.gv_prevMatrix.invalidateViews();
        this.gv_prevMatrix.setAdapter(this.mLedMatrixAdapter);
    }

    private void initMenuButton() {
        this.sv_draw = (ScrollView) findViewById(C0198R.id.pattern_scrollview);
        this.drawBtn = (LinearLayout) findViewById(C0198R.id.pattern_draw);
        this.eraseBtn = (LinearLayout) findViewById(C0198R.id.pattern_erase);
        this.undoBtn = (LinearLayout) findViewById(C0198R.id.pattern_undo);
        this.redoBtn = (LinearLayout) findViewById(C0198R.id.pattern_redo);
        this.rotate = (LinearLayout) findViewById(C0198R.id.pattern_rotate);
        this.drawTxt = (TextView) findViewById(C0198R.id.pattern_draw_txt);
        this.eraseTxt = (TextView) findViewById(C0198R.id.pattern_erase_txt);
        this.untoTxt = (TextView) findViewById(C0198R.id.pattern_undo_txt);
        this.redoTxt = (TextView) findViewById(C0198R.id.pattern_redo_txt);
        this.rotateTxt = (TextView) findViewById(C0198R.id.pattern_rotate_txt);
        this.drawImg = (ImageView) findViewById(C0198R.id.pattern_draw_btn);
        this.eraseImg = (ImageView) findViewById(C0198R.id.pattern_erase_btn);
        this.untoImg = (ImageView) findViewById(C0198R.id.pattern_undo_btn);
        this.redoImg = (ImageView) findViewById(C0198R.id.pattern_redo_btn);
        this.rotateImg = (ImageView) findViewById(C0198R.id.pattern_rotate_btn);
        this.arrayNameEditText = (EditText) findViewById(C0198R.id.array_name_edit);
        String temp = getResources().getString(C0198R.string.des_edit) + " ," + getResources().getString(C0198R.string.des_name) + "," + getResources().getString(C0198R.string.des_tap);
        if (this.arrayNameEditText != null) {
            this.arrayNameEditText.setContentDescription(temp);
            if (this.UserData != null) {
                String tempName = this.UserData.getIconName();
                if (tempName.equals("new")) {
                    tempName = C0316a.f163d;
                }
                this.arrayNameEditText.setText(tempName);
            }
            InputFilter inputFilter = new NameInputFilter();
            this.arrayNameEditText.setFilters(new InputFilter[]{inputFilter});
            this.arrayNameEditText.addTextChangedListener(new C02382());
            this.arrayNameEditText.post(new C02393());
            this.arrayNameEditText.requestFocus();
        }
        this.drawBtn.setOnClickListener(this.mOnClick);
        this.eraseBtn.setOnClickListener(this.mOnClick);
        this.undoBtn.setOnClickListener(this.mOnClick);
        this.redoBtn.setOnClickListener(this.mOnClick);
        this.rotate.setOnClickListener(this.mOnClick);
        this.redoBtn.setEnabled(false);
        this.undoBtn.setEnabled(false);
        this.redoBtn.setClickable(false);
        this.undoBtn.setClickable(false);
    }

    private int drawCount() {
        int drawCount = 0;
        for (LCoverDotInfo dotByteData : this.UserData.getDotDataClass()) {
            if (dotByteData.getDotByteData() == Defines.DOT_ENABLE) {
                drawCount++;
            }
        }
        return drawCount;
    }

    private void initMenuColor() {
        this.drawTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_nomal));
        this.drawImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_nomal)));
        this.eraseTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_nomal));
        this.eraseImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_nomal)));
    }

    private void updateMenuColor() {
        if (this.UserData.isEnableDraw()) {
            this.drawTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
            this.drawTxt.setSelected(true);
            this.drawImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
            this.eraseTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_nomal));
            this.eraseTxt.setSelected(false);
            this.eraseImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_nomal)));
        } else {
            this.drawTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_nomal));
            this.drawTxt.setSelected(false);
            this.drawImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_nomal)));
            this.eraseTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
            this.eraseTxt.setSelected(true);
            this.eraseImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
        }
        if (this.unDoStack.isEmpty()) {
            this.untoTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_dim));
            this.untoImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_dim)));
            this.undoBtn.setEnabled(false);
            this.undoBtn.setClickable(false);
        } else {
            this.untoTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
            this.untoImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
            this.undoBtn.setEnabled(true);
            this.undoBtn.setClickable(true);
        }
        if (this.reDoStack.isEmpty()) {
            this.redoTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_dim));
            this.redoImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_dim)));
            this.redoBtn.setEnabled(false);
            this.redoBtn.setClickable(false);
            return;
        }
        this.redoTxt.setTextColor(getResources().getColor(C0198R.color.dot_create_menu_select));
        this.redoImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(C0198R.color.dot_create_menu_select)));
        this.redoBtn.setEnabled(true);
        this.redoBtn.setClickable(true);
    }

    private boolean checkIconNameDuplicate(String name, int id) {
        boolean duplicated = false;
        this.mCustomInfoList = this.mLedCoverDbAccessor.getCustomIconInfo();
        if (this.mCustomInfoList != null) {
            int i = 0;
            while (i < this.mCustomInfoList.size()) {
                if (id != ((LCoverIconInfo) this.mCustomInfoList.get(i)).mId && name.equals(((LCoverIconInfo) this.mCustomInfoList.get(i)).mName)) {
                    duplicated = true;
                }
                i++;
            }
        }
        return duplicated;
    }

    public boolean saveIconInfo() {
        if (this.arrayNameEditText == null) {
            return false;
        }
        if (this.arrayNameEditText.getText().toString().trim().equals(C0316a.f163d) && drawCount() == 0) {
            Toast.makeText(this, getResources().getString(C0198R.string.save_error_msg_no_data_no_name_to_save), 1).show();
            return false;
        } else if (drawCount() == 0) {
            Toast.makeText(this, getResources().getString(C0198R.string.save_error_msg_no_data_to_save), 1).show();
            return false;
        } else if (this.arrayNameEditText.getText().toString().trim().equals(C0316a.f163d)) {
            Toast.makeText(this, getResources().getString(C0198R.string.save_error_msg_no_name_to_save), 1).show();
            return false;
        } else {
            String userName = this.arrayNameEditText.getText().toString();
            if (checkIconNameDuplicate(userName, this.UserData.getId())) {
                Toast.makeText(this, getResources().getString(C0198R.string.save_error_msg_name_duplicated), 1).show();
                return false;
            }
            StringBuffer dotDataPacket = new StringBuffer();
            for (LCoverDotInfo dotByteData : this.UserData.getDotDataClass()) {
                dotDataPacket.append(dotByteData.getDotByteData());
            }
            String str = dotDataPacket.toString();
            this.UserData.setIconName(userName);
            this.UserData.setIconArray(str);
            LCoverSingleton.getInstance().LoadingCustomIconDataBase(this.UserData);
            SLog.m12v(TAG, "[DB Save info] User name : " + userName);
            SLog.m12v(TAG, "[DB Save info] Data Packet : " + dotDataPacket);
            Intent temp = new Intent(this, LCoverCreatePatternActivity.class);
            if (this.modifyFile) {
                SLog.m12v(TAG, "modifyFile getId : " + this.UserData.getId());
                this.mLedCoverDbAccessor.updateLedIcon(this.UserData);
                temp.putExtra("username", userName);
                setResult(11, temp);
            } else {
                this.mLedCoverDbAccessor.addLedIcon(this.UserData);
                temp.putExtra("username", userName);
                setResult(12, temp);
            }
            return true;
        }
    }

    public void optionDrawDotLed() {
        if (this.dotPointer >= 0 && this.dotPointer <= this.dotLedStack.size() - 1) {
            boolean drawKey = !((LCoverDotInfo) this.dotLedStack.get(this.dotPointer)).isDotEnable();
            int lastTouchPosition = ((LCoverDotInfo) this.dotLedStack.get(this.dotPointer)).getDotPosition();
            ImageView dotLedImage = this.UserData.getDotDataClass()[lastTouchPosition].getDotImageData();
            dotLedImage.setSelected(drawKey);
            if (drawKey) {
                dotLedImage.setBackground(getDrawable(C0198R.drawable.led_dot_selected));
            } else {
                dotLedImage.setBackground(getDrawable(C0198R.drawable.led_dot_normal));
            }
            ((LCoverDotInfo) this.dotLedStack.get(this.dotPointer)).setDotEnable(drawKey);
            this.UserData.getDotDataClass()[lastTouchPosition].setDotImageData(dotLedImage);
            this.UserData.getDotDataClass()[lastTouchPosition].setDotEnable(drawKey);
        }
    }

    public void showDestroyDialog() {
        if (drawCount() == 0 && this.arrayNameEditText.getText() == null) {
            finish();
            return;
        }
        Builder builder = new Builder(this);
        builder.setMessage(getResources().getString(C0198R.string.creation_dlg_text)).setCancelable(false).setPositiveButton(getResources().getString(C0198R.string.save), new C02448()).setNeutralButton(getResources().getString(C0198R.string.cancel), new C02437()).setNegativeButton(getResources().getString(C0198R.string.discard), new C02426());
        builder.create().show();
    }
}
