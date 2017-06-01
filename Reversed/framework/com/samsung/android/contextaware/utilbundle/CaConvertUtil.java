package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaConvertUtil {
    public static String byteArrToString(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            CaLogger.error("Data is null");
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(b + ", ");
        }
        if (stringBuffer.length() > 1) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static int getCompleteOfTwo(int i) {
        return i < 0 ? (i + 255) + 1 : i;
    }

    public static byte[] intToByteArr(int i, int i2) {
        if (i2 > 4) {
            return null;
        }
        int i3;
        byte[] bArr = new byte[i2];
        for (i3 = 0; i3 < i2; i3++) {
            bArr[i3] = (byte) ((i >> (i3 * 8)) & 255);
        }
        byte[] bArr2 = new byte[i2];
        int length = bArr.length;
        for (i3 = 0; i3 < bArr.length; i3++) {
            length--;
            bArr2[i3] = bArr[length];
        }
        return bArr2;
    }

    public static double strToDouble(String str) {
        if (str == null) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(str);
        } catch (Throwable e) {
            CaLogger.exception(e);
            return 0.0d;
        } catch (Throwable e2) {
            CaLogger.exception(e2);
            return 0.0d;
        }
    }

    public static float strToFloat(String str) {
        if (str == null) {
            return 0.0f;
        }
        try {
            float strToInt;
            if (str.contains("/")) {
                int indexOf = str.indexOf(47);
                strToInt = ((float) strToInt(str.substring(0, indexOf))) / ((float) strToInt(str.substring(indexOf + 1)));
            } else {
                strToInt = Float.parseFloat(str);
            }
            return strToInt;
        } catch (Throwable e) {
            CaLogger.exception(e);
            return 0.0f;
        } catch (Throwable e2) {
            CaLogger.exception(e2);
            return 0.0f;
        }
    }

    public static int strToInt(String str) {
        if (str == null) {
            return 0;
        }
        try {
            int parseInt = str.toUpperCase().startsWith("0X") ? Integer.parseInt(str.substring(2), 16) : str.endsWith("B") ? Integer.parseInt(str.substring(0, str.length() - 1), 2) : Integer.parseInt(str);
            return parseInt;
        } catch (Throwable e) {
            CaLogger.exception(e);
            return 0;
        } catch (Throwable e2) {
            CaLogger.exception(e2);
            return 0;
        }
    }

    public static long strToLong(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return str.toUpperCase().startsWith("0X") ? Long.parseLong(str.substring(2), 16) : Long.parseLong(str);
        } catch (Throwable e) {
            CaLogger.exception(e);
            return 0;
        } catch (Throwable e2) {
            CaLogger.exception(e2);
            return 0;
        }
    }

    public static byte[] stringToByteArray(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        int i = 0;
        int indexOf = str.indexOf(44, 0);
        while (indexOf <= str.length()) {
            i++;
            indexOf = str.indexOf(44, indexOf + 1);
            if (indexOf < 0) {
                break;
            }
        }
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            indexOf = str.indexOf(44, i2);
            int i5 = i4 + 1;
            System.arraycopy(intToByteArr(strToInt(str.substring(i2, indexOf)), 1), 0, bArr, i4, 1);
            i2 = indexOf + 2;
            i3++;
            i4 = i5;
        }
        return bArr;
    }
}
