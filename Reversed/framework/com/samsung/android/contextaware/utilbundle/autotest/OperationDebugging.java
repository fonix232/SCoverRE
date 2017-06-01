package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;
import java.util.List;

abstract class OperationDebugging extends CaAutoTest {
    private final List<byte[]> mPacketList = new ArrayList();

    protected OperationDebugging(int i) {
        super(i);
    }

    protected final void addPacket(byte[] bArr) {
        this.mPacketList.add(bArr);
    }

    protected final void clearPacket() {
        this.mPacketList.clear();
    }

    protected abstract void doDebugging(byte[] bArr);

    protected final void removePacket(byte[] bArr) {
        this.mPacketList.remove(bArr);
    }

    public final void run() {
        int i = 0;
        while (i < this.mPacketList.size()) {
            try {
                Thread.sleep((long) super.getDelayTime());
                if (!super.isStopTest()) {
                    CaLogger.debug("Scenario [" + Integer.toString(i) + "]");
                    for (byte num : (byte[]) this.mPacketList.get(i)) {
                        CaLogger.info("Packet = " + Integer.toString(num));
                    }
                    doDebugging((byte[]) this.mPacketList.get(i));
                    i++;
                } else {
                    return;
                }
            } catch (Throwable e) {
                CaLogger.exception(e);
                return;
            }
        }
    }
}
