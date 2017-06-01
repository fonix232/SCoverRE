package com.samsung.android.mateservice.common;

import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import java.util.ArrayList;

public class BundleArgs {
    public static final String ACCESSORY_LIST = "accessoryList";
    public static final String ATTACHED = "attached";
    public static final String CLIENT_STATE_ACTION = "clientStateAction";
    public static final String CLIENT_STATE_BUNDLE = "clientStateBundle";
    public static final String DATA = "data";
    public static final String EXTRA_DATA = "extraData";
    public static final String KEEP_CONNECTION_STATE = "keepConnectionState";
    private static final String RESULT_BOOLEAN = "resultBoolean";
    public static final String SECURITY_DATA_TYPE = "dataType";
    public static final String SECURITY_DECRYPT_DATA = "decryptData";
    public static final String SECURITY_DECRYPT_RESULT = "decryptResult";
    public static final String SECURITY_ENCRYPT_DATA = "encryptData";
    public static final String SECURITY_ENCRYPT_RESULT = "encryptResult";
    public static final String STATE_ID = "stateId";
    public static final String TIMESTAMP = "timeStamp";

    public static class Builder {
        private Bundle bundle = new Bundle();

        private Builder() {
        }

        public static Builder get() {
            return new Builder();
        }

        public Bundle build() {
            return this.bundle;
        }

        public Builder put(String str, byte b) {
            this.bundle.putByte(str, b);
            return this;
        }

        public Builder put(String str, int i) {
            this.bundle.putInt(str, i);
            return this;
        }

        public Builder put(String str, long j) {
            this.bundle.putLong(str, j);
            return this;
        }

        public Builder put(String str, Bundle bundle) {
            this.bundle.putBundle(str, bundle);
            return this;
        }

        public Builder put(String str, IBinder iBinder) {
            this.bundle.putBinder(str, iBinder);
            return this;
        }

        public Builder put(String str, String str2) {
            this.bundle.putString(str, str2);
            return this;
        }

        public Builder put(String str, ArrayList<? extends Parcelable> arrayList) {
            this.bundle.putParcelableArrayList(str, arrayList);
            return this;
        }

        public Builder put(String str, boolean z) {
            this.bundle.putBoolean(str, z);
            return this;
        }

        public Builder put(String str, byte[] bArr) {
            this.bundle.putByteArray(str, bArr);
            return this;
        }

        public Builder put(String str, int[] iArr) {
            this.bundle.putIntArray(str, iArr);
            return this;
        }

        public Builder put(String str, long[] jArr) {
            this.bundle.putLongArray(str, jArr);
            return this;
        }

        public Builder put(String str, String[] strArr) {
            this.bundle.putStringArray(str, strArr);
            return this;
        }

        public Builder put(String str, boolean[] zArr) {
            this.bundle.putBooleanArray(str, zArr);
            return this;
        }
    }

    public static byte[] byteArray(Bundle bundle, String str) {
        return bundle != null ? bundle.getByteArray(str) : null;
    }

    public static boolean getResultBoolean(Bundle bundle) {
        return bundle != null ? bundle.getBoolean(RESULT_BOOLEAN, false) : false;
    }

    public static Bundle getResultBundle(boolean z) {
        BaseBundle bundle = new Bundle();
        bundle.putBoolean(RESULT_BOOLEAN, z);
        return bundle;
    }
}
