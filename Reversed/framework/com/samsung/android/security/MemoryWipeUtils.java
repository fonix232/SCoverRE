package com.samsung.android.security;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.SpannableStringBuilder;
import android.util.Slog;
import android.widget.TextView;

public class MemoryWipeUtils {
    public static final int DUMMY_SEND_COUNT = 64;

    public static void clear() {
        clear(null, new String(new byte[16]), 0, 16);
    }

    public static void clear(IBinder iBinder, String str, int i, int i2) {
        if (CCManager.isMdfEnforced() || "encrypted".equals(System.getProperty("ro.crypto.state"))) {
            long currentTimeMillis = System.currentTimeMillis();
            String str2 = "                                ";
            if (i2 < 0) {
                i2 = 0;
            }
            i2 = (i2 / 32) + 1;
            if (iBinder != null) {
                for (int i3 = 0; i3 < 64; i3++) {
                    Parcel obtain = Parcel.obtain();
                    Parcel obtain2 = Parcel.obtain();
                    try {
                        obtain.writeInterfaceToken(str);
                        for (int i4 = 0; i4 < i2; i4++) {
                            obtain.writeString(str2);
                        }
                        iBinder.transact(i, obtain, obtain2, 0);
                        obtain2.readException();
                        int readInt = obtain2.readInt();
                    } catch (RemoteException e) {
                    } finally {
                        obtain2.recycle();
                        obtain.recycle();
                    }
                }
            }
            Slog.d("MDPP", new Exception().getStackTrace()[1].getClassName() + "::count = " + 64 + ", delay = " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
    }

    public static void clear(char[] cArr) {
        for (int i = 0; i < cArr.length; i++) {
            cArr[i] = '\u0000';
        }
    }

    public static char[] getChars(TextView textView) {
        SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) textView.getEditableText();
        char[] cArr = new char[spannableStringBuilder.length()];
        for (int i = 0; i < spannableStringBuilder.length(); i++) {
            cArr[i] = spannableStringBuilder.charAt(i);
        }
        spannableStringBuilder.clear();
        return cArr;
    }
}
