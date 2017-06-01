package com.samsung.android.sdk.cover;

import android.content.Context;
import com.samsung.android.sdk.SsdkInterface;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;

public final class Scover implements SsdkInterface {
    private Context mContext;

    public int getVersionCode() {
        return 16842752;
    }

    public String getVersionName() {
        return String.format("%d.%d.%d", new Object[]{Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)});
    }

    public void initialize(Context context) throws SsdkUnsupportedException, IllegalArgumentException {
        this.mContext = context;
        if (context == null) {
            throw new IllegalArgumentException("context may not be null!!");
        } else if (SsdkVendorCheck.isSamsungDevice()) {
            ScoverManager coverManager = new ScoverManager(this.mContext);
            if (coverManager == null || !coverManager.isSupportCover()) {
                throw new SsdkUnsupportedException("This device is not supported Scover!!!", 1);
            }
        } else {
            throw new SsdkUnsupportedException("This is not Samsung device!!!", 0);
        }
    }

    public boolean isFeatureEnabled(int type) {
        return new ScoverManager(this.mContext).isSupportTypeOfCover(type);
    }
}
