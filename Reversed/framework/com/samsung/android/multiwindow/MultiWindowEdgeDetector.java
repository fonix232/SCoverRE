package com.samsung.android.multiwindow;

import android.content.Context;
import android.graphics.Rect;
import android.os.Debug;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.util.TypedValue;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.MotionEvent;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.framework.feature.MultiWindowFeatures;
import com.samsung.android.framework.res.C0078R;

public class MultiWindowEdgeDetector {
    private static final int CHECK_TYPE_CIRCLE = 2;
    private static final int CHECK_TYPE_LINE = 1;
    private static final int CHECK_TYPE_UNDEFINED = 0;
    private static final boolean DEBUG = true;
    public static final int EDGE_LEFT_TOP = 5;
    public static final int EDGE_NONE = 0;
    public static final int EDGE_RIGHT_TOP = 9;
    private static final int MAX_EFFECTIVE_DEGREES = 70;
    private static final boolean SAFE_DEBUG = Debug.semIsProductDev();
    private static final String TAG = "MultiWindowEdgeDetector";
    private static final float WIDTH_SCALE_FOR_LANDSCAPE_CORNER_R = 1.25f;
    private int mCheckAreaType = 0;
    private Context mContext;
    private int mEdgeFlgas = 0;
    private int mHeight;
    private boolean mIsScreenCornerR = false;
    private int mMaxDegrees;
    private int mScreenHeight;
    private int mScreenOrientation = 0;
    private int mScreenWidth;
    private int mStartHeight;
    private int mStartWidth;
    private int mWidth;

    public static class ResizeInfo {
        private static final String TAG = "ResizeInfo";
        public Rect mBounds = new Rect();
        public int mDisplayId;
        public int mTaskId;

        public ResizeInfo(int i, int i2, Rect rect) {
            this.mDisplayId = i;
            this.mTaskId = i2;
            if (rect != null) {
                this.mBounds.set(rect);
            }
            Slog.i(TAG, "Creator: displayId=" + i + ", taskId=" + i2 + ", bounds=" + rect);
        }
    }

    public static class Utils {
        public static boolean adjustMinimalTaskBounds(Rect rect, int i, int i2, int i3) {
            if (rect == null || i2 < 1 || i3 < 1) {
                return false;
            }
            Object obj = rect.width() < i2 ? 1 : null;
            boolean z = rect.height() < i3;
            switch (i) {
                case 5:
                    if (obj != null) {
                        rect.left = rect.right - i2;
                    }
                    if (z) {
                        rect.top = rect.bottom - i3;
                        break;
                    }
                    break;
                case 9:
                    if (obj != null) {
                        rect.right = rect.left + i2;
                    }
                    if (z) {
                        rect.top = rect.bottom - i3;
                        break;
                    }
                    break;
            }
            if (obj == null) {
                z = false;
            }
            return z;
        }

        public static void applyResizeRect(Rect rect, int i, int i2, int i3) {
            if (rect != null) {
                switch (i) {
                    case 5:
                        rect.left = i2;
                        rect.top = i3;
                        break;
                    case 9:
                        rect.right = i2;
                        rect.top = i3;
                        break;
                }
            }
        }

        public static int convertDesktopMouseIcon(int i) {
            switch (i) {
                case 1000:
                    return 10121;
                case 1014:
                    return 10122;
                case 1015:
                    return 10123;
                case 1016:
                    return 10125;
                case 1017:
                    return 10124;
                default:
                    return i;
            }
        }

        public static int dipToPixel(int i, DisplayMetrics displayMetrics) {
            return (int) TypedValue.applyDimension(1, (float) i, displayMetrics);
        }

        public static String edgeFlagToString(int i) {
            switch (i) {
                case 5:
                    return "EDGE_LEFT_TOP";
                case 9:
                    return "EDGE_RIGHT_TOP";
                default:
                    return Integer.toHexString(i);
            }
        }

        public static boolean isLeftSide(int i, int i2) {
            return i / 2 > i2;
        }

        public static boolean isTopSide(int i, int i2) {
            return i / 2 > i2;
        }
    }

    public MultiWindowEdgeDetector(Context context) {
        this.mContext = context;
        this.mIsScreenCornerR = SemFloatingFeature.getInstance().getBoolean("SEC_FLOATING_FEATURE_COMMON_SUPPORT_CORNER_R");
        loadResources();
        this.mCheckAreaType = 2;
    }

    private void ensureScreenInfo() {
        int i = this.mContext.getResources().getDisplayMetrics().widthPixels > this.mContext.getResources().getDisplayMetrics().heightPixels ? 2 : 1;
        if (this.mScreenOrientation != i) {
            Slog.w(TAG, "ensureScreenInfo: ScreenInfo is wrong, mScreenOr=" + this.mScreenOrientation + ", currentOr=" + i);
            updateScreenInfo();
        }
    }

    private boolean isNotSupportEdge(MotionEvent motionEvent) {
        return (motionEvent == null || (motionEvent.getButtonState() & 2) == 0) ? false : true;
    }

    private void loadResources() {
        this.mWidth = this.mContext.getResources().getDimensionPixelSize(this.mIsScreenCornerR ? C0078R.dimen.multiwindow_freeform_gesture_action_down_width_corner_r : C0078R.dimen.multiwindow_freeform_gesture_action_down_width);
        this.mHeight = this.mContext.getResources().getDimensionPixelSize(C0078R.dimen.multiwindow_freeform_gesture_action_down_height);
        this.mStartWidth = this.mContext.getResources().getDimensionPixelSize(C0078R.dimen.multiwindow_freeform_gesture_guide_start_width);
        this.mStartHeight = this.mContext.getResources().getDimensionPixelSize(C0078R.dimen.multiwindow_freeform_gesture_guide_start_height);
        this.mMaxDegrees = 70;
        updateScreenInfo();
        if (SAFE_DEBUG) {
            updateFromSystemProperties();
        }
    }

    private void setEdgeFlags(int i) {
        this.mEdgeFlgas = i;
    }

    private void updateFromSystemProperties() {
        if (SAFE_DEBUG) {
            int i = SystemProperties.getInt("persist.dev.freeform.gesture.w", -1);
            int i2 = SystemProperties.getInt("persist.dev.freeform.gesture.h", -1);
            int i3 = SystemProperties.getInt("persist.dev.freeform.gesture.sw", -1);
            int i4 = SystemProperties.getInt("persist.dev.freeform.gesture.sh", -1);
            int i5 = SystemProperties.getInt("persist.dev.freeform.gesture.dr", -1);
            Object obj = null;
            if (i >= 0 && this.mWidth != i) {
                this.mWidth = i;
                obj = 1;
            }
            if (i2 >= 0 && this.mHeight != i2) {
                this.mHeight = i2;
                obj = 1;
            }
            if (i3 >= 0 && this.mStartWidth != i3) {
                this.mStartWidth = i3;
                obj = 1;
            }
            if (i4 >= 0 && this.mStartHeight != i4) {
                this.mStartHeight = i4;
                obj = 1;
            }
            if (i5 >= 0 && this.mMaxDegrees != i5) {
                this.mMaxDegrees = i5;
                obj = 1;
            }
            if (obj != null) {
                Log.i(TAG, "updateFromSystemProperties: mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mStartWidth=" + this.mStartWidth + ", mStartHeight=" + this.mStartHeight + ", mMaxDegrees=" + this.mMaxDegrees);
            }
        }
    }

    private void updateScreenInfo() {
        Display display = this.mContext.getDisplay();
        if (display == null) {
            Slog.w(TAG, "display is null, mContext=" + this.mContext);
            return;
        }
        DisplayInfo displayInfo = new DisplayInfo();
        display.getDisplayInfo(displayInfo);
        this.mScreenWidth = displayInfo.logicalWidth;
        this.mScreenHeight = displayInfo.logicalHeight;
        this.mScreenOrientation = this.mScreenWidth > this.mScreenHeight ? 2 : 1;
        if (this.mIsScreenCornerR && this.mScreenOrientation == 2) {
            this.mWidth = (int) ((((float) this.mWidth) * WIDTH_SCALE_FOR_LANDSCAPE_CORNER_R) + 0.5f);
        }
        if (SAFE_DEBUG) {
            Slog.i(TAG, "updateScreenInfo: mScreenWidth=" + this.mScreenWidth + ", mScreenHeight=" + this.mScreenHeight + ", mScreenOrientation=" + this.mScreenOrientation + ", mWidth=" + this.mWidth);
        }
    }

    public int checkEdgeFlags(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (rawY > ((float) this.mHeight)) {
            return 0;
        }
        int i = 1;
        if (rawX < ((float) this.mWidth)) {
            i = 5;
        } else if (rawX > ((float) (this.mScreenWidth - this.mWidth))) {
            i = 9;
        }
        Log.i(TAG, "getEdgeFlags: " + Utils.edgeFlagToString(i) + ", [" + rawX + FingerprintManager.FINGER_PERMISSION_DELIMITER + rawY + "], w=" + this.mWidth + ", h=" + this.mHeight + ", screenWidth=" + this.mScreenWidth + ", caller=" + Debug.getCallers(5));
        return i;
    }

    public int getEdgeFlgas() {
        return this.mEdgeFlgas;
    }

    public boolean isEdge() {
        return this.mEdgeFlgas == 5 || this.mEdgeFlgas == 9;
    }

    public boolean isEffectiveAngle(int i, int i2) {
        boolean z = true;
        if (this.mCheckAreaType != 2) {
            return true;
        }
        int i3 = Integer.MAX_VALUE;
        switch (this.mEdgeFlgas) {
            case 5:
                i3 = (int) Math.toDegrees(Math.atan2((double) i2, (double) i));
                break;
            case 9:
                i3 = (int) Math.toDegrees(Math.atan2((double) i2, (double) (this.mScreenWidth - i)));
                break;
        }
        Log.i(TAG, "isEffectiveAngle: " + (i3 <= this.mMaxDegrees) + ", degrees=" + i3 + ", mMaxDegrees=" + this.mMaxDegrees);
        if (i3 > this.mMaxDegrees) {
            z = false;
        }
        return z;
    }

    public void onConfigurationChanged() {
        loadResources();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!MultiWindowFeatures.FREEFORM_SUPPORT) {
            return false;
        }
        boolean isEdge = isEdge();
        switch (motionEvent.getActionMasked()) {
            case 0:
                ensureScreenInfo();
                this.mEdgeFlgas = checkEdgeFlags(motionEvent);
                if (isNotSupportEdge(motionEvent)) {
                    reset();
                }
                isEdge = isEdge();
                break;
            case 1:
            case 3:
                reset();
                break;
        }
        return isEdge;
    }

    public boolean readyToFreeform(int i, int i2) {
        boolean z = true;
        boolean z2 = false;
        if (this.mCheckAreaType == 1) {
            switch (this.mEdgeFlgas) {
                case 5:
                    if (i <= this.mStartWidth || i2 <= this.mStartHeight) {
                        z = false;
                    }
                    return z;
                case 9:
                    if (i < this.mScreenWidth - this.mStartWidth && i2 > this.mStartHeight) {
                        z2 = true;
                    }
                    return z2;
            }
        } else if (this.mCheckAreaType == 2) {
            int i3 = this.mStartWidth * this.mStartWidth;
            int i4 = 0;
            switch (this.mEdgeFlgas) {
                case 5:
                    i4 = (i * i) + (i2 * i2);
                    break;
                case 9:
                    i4 = ((this.mScreenWidth - i) * (this.mScreenWidth - i)) + (i2 * i2);
                    break;
            }
            if (i3 >= i4) {
                z = false;
            }
            return z;
        }
        return false;
    }

    public void reset() {
        this.mEdgeFlgas = 0;
    }
}
