package com.samsung.android.mateservice.executable;

import android.os.Bundle;
import com.samsung.android.mateservice.action.Action;
import com.samsung.android.mateservice.action.ActionExecutable;
import com.samsung.android.mateservice.common.BundleArgs;
import com.samsung.android.mateservice.common.BundleArgs.Builder;
import com.samsung.android.mateservice.util.UtilLog;
import com.samsung.android.mateservice.util.UtilStringCrypto;

public class ExecStringCrypto implements ActionExecutable {
    private static final String TAG = "StringCrypto";

    private Bundle getDecryptedData(Bundle bundle) {
        Builder builder = null;
        if (bundle.getString(BundleArgs.SECURITY_DATA_TYPE, null) == null) {
            String string = bundle.getString(BundleArgs.SECURITY_DECRYPT_DATA, null);
            if (string != null) {
                builder = Builder.get().put(BundleArgs.SECURITY_DECRYPT_RESULT, UtilStringCrypto.decryption(string));
            }
        } else {
            byte[] byteArray = bundle.getByteArray(BundleArgs.SECURITY_DECRYPT_DATA);
            if (byteArray != null) {
                builder = Builder.get().put(BundleArgs.SECURITY_DECRYPT_RESULT, UtilStringCrypto.decryption(byteArray));
            }
        }
        return builder != null ? builder.build() : null;
    }

    private Bundle getEncryptedData(Bundle bundle) {
        Builder builder = null;
        if (bundle.getString(BundleArgs.SECURITY_DATA_TYPE, null) == null) {
            String string = bundle.getString(BundleArgs.SECURITY_ENCRYPT_DATA, null);
            if (string != null) {
                builder = Builder.get().put(BundleArgs.SECURITY_ENCRYPT_RESULT, UtilStringCrypto.encryption(string));
            }
        } else {
            byte[] byteArray = bundle.getByteArray(BundleArgs.SECURITY_ENCRYPT_DATA);
            if (byteArray != null) {
                builder = Builder.get().put(BundleArgs.SECURITY_ENCRYPT_RESULT, UtilStringCrypto.encryption(byteArray));
            }
        }
        return builder != null ? builder.build() : null;
    }

    public Bundle execute(Bundle bundle, int i, int i2) {
        UtilLog.m9v(TAG, "ActionSecurityUtil", new Object[0]);
        if (bundle != null) {
            switch (i2) {
                case Action.AGENT_SYS_ENCRYPTION /*1179651*/:
                    return getEncryptedData(bundle);
                case Action.AGENT_SYS_DECRYPTION /*1179652*/:
                    return getDecryptedData(bundle);
            }
        }
        return null;
    }
}
