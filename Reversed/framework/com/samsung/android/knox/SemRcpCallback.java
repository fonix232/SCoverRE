package com.samsung.android.knox;

import com.samsung.android.knox.SemIRCPCallback.Stub;
import java.util.List;

public abstract class SemRcpCallback {
    private SemIRCPCallback f13s = new SubSemRcpCallback(this);

    public class SubSemRcpCallback extends Stub {
        SemRcpCallback parent = null;

        public SubSemRcpCallback(SemRcpCallback semRcpCallback) {
            this.parent = semRcpCallback;
        }

        public void onComplete(List<String> list, int i, int i2) {
            if (this.parent != null) {
                this.parent.onComplete(list, i, i2);
            }
        }

        public void onDone(String str, int i) {
            if (this.parent != null) {
                this.parent.onDone(str, i);
            }
        }

        public void onFail(String str, int i, int i2) {
            if (this.parent != null) {
                this.parent.onFail(str, i, i2);
            }
        }

        public void onProgress(String str, int i, int i2) {
            if (this.parent != null) {
                this.parent.onProgress(str, i, i2);
            }
        }
    }

    public SemIRCPCallback getChild() {
        return this.f13s;
    }

    public abstract void onComplete(List<String> list, int i, int i2);

    public abstract void onDone(String str, int i);

    public abstract void onFail(String str, int i, int i2);

    public abstract void onProgress(String str, int i, int i2);
}
