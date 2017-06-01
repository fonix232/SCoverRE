package com.samsung.android.share.executor;

import com.samsung.android.share.executor.data.ParamFilling;
import com.samsung.android.share.executor.data.ScreenStateInfo;
import com.samsung.android.share.executor.data.State;

public interface IExecutorCommandListener {
    boolean onParamFillingReceived(ParamFilling paramFilling);

    void onRuleCanceled(String str);

    ScreenStateInfo onScreenStatesRequested();

    void onStateReceived(State state);
}
