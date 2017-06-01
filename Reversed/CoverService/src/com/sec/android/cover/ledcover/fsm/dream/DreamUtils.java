package com.sec.android.cover.ledcover.fsm.dream;

import android.util.Log;

public class DreamUtils {
    private static final String TAG = DreamUtils.class.getSimpleName();

    public static byte[] getPayload(String iconData) {
        byte[] bArr = null;
        if (iconData == null) {
            Log.e(TAG, "getPayload(): iconData cannot be null");
        } else if (iconData.length() < 207) {
            Log.e(TAG, "getPayload(): iconData is too short (" + iconData.length() + ")");
        } else {
            bArr = new byte[30];
            bArr[2] = (byte) 0;
            bArr[1] = (byte) 0;
            bArr[0] = (byte) 0;
            int row = 0;
            while (row < 9) {
                int segment = 0;
                while (segment < 3) {
                    int payloadByte = 0;
                    for (int bit = 0; bit < 7; bit++) {
                        if (iconData.charAt(((row * 23) + (segment * 8)) + bit) == '1') {
                            payloadByte += 1 << (7 - bit);
                        }
                    }
                    if (segment < 2 && iconData.charAt(((row * 23) + (segment * 8)) + 7) == '1') {
                        payloadByte++;
                    }
                    bArr[((row * 3) + segment) + 3] = (byte) (payloadByte & 255);
                    segment++;
                }
                row++;
            }
        }
        return bArr;
    }
}
