package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.graphics.spr.document.animator.SprAnimatorBase;

class CaOperationStressTest extends InnerProcessStressTest {
    protected CaOperationStressTest(int i) {
        super(i);
    }

    private byte[] getEnvironmentSensorPacket() {
        return new byte[]{(byte) 1, (byte) 1, SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEINOUT, (byte) 2, (byte) 1, (byte) 6, (byte) 0, (byte) 3, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_EXPOEASEOUT, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_CUBICEASEINOUT, (byte) 0, (byte) 16, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_QUINTEASEINOUT, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_QUARTEASEIN, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_QUINTEASEINOUT, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT70, (byte) 0, (byte) 41, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT70};
    }

    private byte[] getEnvironmentSensorPacket1() {
        return new byte[]{(byte) 1, (byte) 1, SprAnimatorBase.INTERPOLATOR_TYPE_BACKEASEINOUT, (byte) 1, (byte) 1, (byte) 6, (byte) 0, (byte) 1, (byte) 0, (byte) 5, (byte) 0, (byte) 6, (byte) 0, (byte) 7};
    }

    private byte[] getPedometer() {
        return new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 0, (byte) 1, (byte) 0, SprAnimatorBase.INTERPOLATOR_TYPE_ELASTICEASEOUT, (byte) 41, (byte) 75, SprAnimatorBase.INTERPOLATOR_TYPE_SINEINOUT33, (byte) 0, (byte) 3};
    }

    private byte[] getServicePacket(byte b) {
        return new byte[]{(byte) 1, (byte) 1, b, (byte) 1};
    }

    protected final byte[] getPacket(int i) {
        switch (i) {
            case 0:
                return getServicePacket((byte) 5);
            case 1:
                return getServicePacket(SprAnimatorBase.INTERPOLATOR_TYPE_BOUNCEEASEIN);
            case 2:
                return getServicePacket((byte) 7);
            default:
                return new byte[0];
        }
    }
}
