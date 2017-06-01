package com.samsung.android.view;

import android.graphics.Rect;
import android.view.MagnificationSpec;
import java.io.PrintWriter;

public interface IWindowStateBridge {
    boolean applyAspectRatio(Rect rect);

    void applyBlurEffect();

    int applyNavbarAlwaysEnabled(int i);

    void configureReducedScreenSpec();

    void disableHideSViewCoverOnce(boolean z);

    void dump(String str, PrintWriter printWriter);

    Rect getAspectRatioFrame();

    int getCoverMode();

    int getDisplayId();

    MagnificationSpec getReducedScreenScaleSpecLocked();

    int getReducedSideTouchArea();

    int getSystemUiVisibility();

    boolean hasFixedOrientation();

    boolean hasMoved();

    void initPackageConfigurations();

    boolean isAspectRatioWindow();

    boolean isBackgroundSurfaceNeeded();

    boolean isConventionalMode();

    boolean isDexCompatMode();

    boolean isFixedOrientation();

    boolean isHideBySViewCover();

    boolean isHomeTask();

    boolean isOnScreen();

    void resetEffects();

    void setHideBySViewCover(boolean z);

    void setOwner(Object obj);

    void setSystemUiVisibility(int i);

    boolean willBeHideSViewCoverOnce();
}
