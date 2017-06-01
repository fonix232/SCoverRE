package com.samsung.android.sdk.look.airbutton;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.samsung.android.airbutton.AirButtonImpl;
import com.samsung.android.airbutton.AirButtonImpl.OnItemSelectedListener;
import com.samsung.android.sdk.look.Slook;

public class SlookAirButton {
    public static final int DIRECTION_AUTO = -1;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_LOWER = 2;
    public static final int DIRECTION_RIGHT = 4;
    public static final int DIRECTION_UPPER = 1;
    public static final int GRAVITY_AUTO = -1;
    public static final int GRAVITY_BOTTOM = 2;
    public static final int GRAVITY_HOVER_POINT = 5;
    public static final int GRAVITY_LEFT = 3;
    public static final int GRAVITY_RIGHT = 4;
    public static final int GRAVITY_TOP = 1;
    private static final String TAG = "AirButtonWidget";
    public static final int UI_TYPE_LIST = 2;
    public static final int UI_TYPE_MENU = 1;
    private AirButtonImpl mAirButtonImpl = null;
    private ItemSelectListener mItemSelectListener;
    private Slook mSlook = new Slook();
    private int mUIType = 2;

    class C00011 implements OnItemSelectedListener {
        C00011() {
        }

        public void onItemSelected(View view, int itemIndex, Object data) {
            SlookAirButton.this.mItemSelectListener.onItemSelected(view, itemIndex, data);
        }
    }

    public interface ItemSelectListener {
        void onItemSelected(View view, int i, Object obj);
    }

    public SlookAirButton(View parentView, SlookAirButtonAdapter adapter, int uiType) {
        if (!isSupport(1)) {
            return;
        }
        if (adapter == null) {
            throw new IllegalArgumentException("adapter is null");
        }
        this.mUIType = uiType;
        this.mAirButtonImpl = new AirButtonImpl(parentView, adapter, uiType);
    }

    public void setItemSelectListener(ItemSelectListener listener) {
        if (isSupport(1)) {
            this.mItemSelectListener = listener;
            if (this.mItemSelectListener != null) {
                this.mAirButtonImpl.setOnItemSelectedListener(new C00011());
            }
        }
    }

    public void setGravity(int gravity) {
        if (isSupport(1)) {
            switch (gravity) {
                case -1:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                default:
                    Log.e(TAG, "The gravity(" + gravity + ") was wrong.");
                    break;
            }
            if (this.mUIType == 1 && (gravity == 3 || gravity == 4)) {
                throw new IllegalStateException("The Gravity is wrong. You cann't use GRAVITY_LEFT/GRAVITY_RIGHT with UI_TYPE_MENU.");
            }
            this.mAirButtonImpl.setGravity(gravity);
        }
    }

    public int getGravity() {
        if (isSupport(1)) {
            return this.mAirButtonImpl.getGravity();
        }
        return 0;
    }

    public void setDirection(int direction) {
        if (isSupport(1)) {
            switch (direction) {
                case -1:
                case 1:
                case 2:
                case 3:
                case 4:
                    break;
                default:
                    Log.e(TAG, "The direction(" + direction + ") was wrong.");
                    break;
            }
            if (this.mUIType == 1 && (direction == 1 || direction == 2)) {
                throw new IllegalStateException("The Direction is wrong.you cann't use UPPER/LOWER direction with UI_TYPE_MENU.");
            } else if (this.mUIType == 2 && (direction == 3 || direction == 4)) {
                throw new IllegalStateException("The Direction is wrong.you cann't use LEFT/RIGHT direction with UI_TYPE_LIST.");
            } else {
                this.mAirButtonImpl.setDirection(direction);
            }
        }
    }

    public int getDirection() {
        if (isSupport(1)) {
            return this.mAirButtonImpl.getDirection();
        }
        return 0;
    }

    public void setPosition(int x, int y) {
        if (isSupport(1)) {
            this.mAirButtonImpl.setPosition(x, y);
        }
    }

    public void setScrollEnabled(boolean enable) {
        if (isSupport(1)) {
            this.mAirButtonImpl.setScrollEnabled(enable);
        }
    }

    public boolean isScrollEnabled() {
        if (isSupport(1)) {
            return this.mAirButtonImpl.isScrollEnabled();
        }
        return false;
    }

    private boolean isSupport(int ver) {
        if (this.mSlook.isFeatureEnabled(1)) {
            return true;
        }
        return false;
    }

    public void setBounceEffectEnabled(boolean enable) {
        if (isSupport(1)) {
            this.mAirButtonImpl.setBounceEffectEnabled(enable);
        }
    }

    public boolean isBounceEffectEnabled() {
        if (isSupport(1)) {
            return this.mAirButtonImpl.isBounceEffectEnabled();
        }
        return false;
    }

    public void hide() {
        if (isSupport(1)) {
            this.mAirButtonImpl.hide();
        }
    }

    public void dismiss() {
        if (isSupport(1)) {
            this.mAirButtonImpl.dismiss();
        }
    }

    public void setAutoControlEnabled(boolean enable) {
        if (isSupport(1)) {
            this.mAirButtonImpl.setEnabled(enable);
        }
    }

    public boolean isAutoControlEnabled() {
        if (isSupport(1)) {
            return this.mAirButtonImpl.isEnabled();
        }
        return false;
    }

    public void startAnimationIcon(MotionEvent event) {
        if (!isSupport(1)) {
            return;
        }
        if (this.mAirButtonImpl.isEnabled()) {
            throw new IllegalStateException("The mode is auto control. Please disable auto control.");
        } else if (event.getAction() != 9) {
            throw new IllegalArgumentException("The event isn't ACTION_HOVER_ENTER");
        } else {
            this.mAirButtonImpl.onHoverEnter(event);
        }
    }

    public void moveAnimationIcon(MotionEvent event) {
        if (!isSupport(1)) {
            return;
        }
        if (this.mAirButtonImpl.isEnabled()) {
            throw new IllegalStateException("The mode is auto control. Please disable auto control.");
        } else if (event.getAction() != 7) {
            throw new IllegalArgumentException("The event isn't ACTION_HOVER_MOVE.");
        } else {
            this.mAirButtonImpl.onHoverMove(event);
        }
    }

    public void stopAnimationIcon(MotionEvent event) {
        if (!isSupport(1)) {
            return;
        }
        if (this.mAirButtonImpl.isEnabled()) {
            throw new IllegalStateException("The mode is auto control. Please disable auto control.");
        } else if (event.getAction() != 10) {
            throw new IllegalArgumentException("The event isn't ACTION_HOVER_EXIT.");
        } else {
            this.mAirButtonImpl.onHoverExit(event);
        }
    }

    public void show(MotionEvent event) {
        if (!isSupport(1)) {
            return;
        }
        if (this.mAirButtonImpl.isEnabled()) {
            throw new IllegalStateException("The mode is auto control. Please disable auto control.");
        } else if (event.getToolType(0) != 2) {
            throw new IllegalArgumentException("The event isn't TOOL_TYPE_STYLUS.");
        } else {
            this.mAirButtonImpl.onHoverButtonSecondary(event);
        }
    }
}
