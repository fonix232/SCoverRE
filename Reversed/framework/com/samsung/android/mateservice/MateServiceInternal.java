package com.samsung.android.mateservice;

import android.os.Bundle;

public abstract class MateServiceInternal {
    public abstract void accessoryStateChanged(boolean z, byte[] bArr, byte[] bArr2);

    public abstract Bundle executeAction(int i, Bundle bundle);

    public abstract void screenTurnedOff();
}
