package com.sec.knox.container.util;

import com.google.android.collect.Lists;
import com.samsung.android.transcode.core.Encode.BitRate;
import java.util.ArrayList;

public class DaemonEvent {
    private final int mCmdNumber;
    private final int mCode;
    private String mMessage;
    private String[] mParsed = null;
    private String mRawEvent;

    private DaemonEvent(int i, int i2, String str, String str2) {
        this.mCmdNumber = i;
        this.mCode = i2;
        this.mMessage = str;
        this.mRawEvent = str2;
    }

    public static String[] filterMessageList(DaemonEvent[] daemonEventArr, int i) {
        ArrayList newArrayList = Lists.newArrayList();
        for (DaemonEvent daemonEvent : daemonEventArr) {
            if (daemonEvent.getCode() == i) {
                newArrayList.add(daemonEvent.getMessage());
            }
        }
        return (String[]) newArrayList.toArray(new String[newArrayList.size()]);
    }

    private static boolean isClassUnsolicited(int i) {
        return i >= 600 && i < 700;
    }

    public static DaemonEvent parseRawEvent(String str) {
        String[] split = str.split(" ");
        if (split.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments");
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            int length = split[0].length() + 1;
            int i = -1;
            if (!isClassUnsolicited(parseInt)) {
                if (split.length < 3) {
                    throw new IllegalArgumentException("Insufficient arguemnts");
                }
                try {
                    i = Integer.parseInt(split[1]);
                    length += split[1].length() + 1;
                } catch (Throwable e) {
                    throw new IllegalArgumentException("problem parsing cmdNumber", e);
                }
            }
            String substring = str.substring(length);
            if (split != null) {
                for (String str2 : split) {
                    if (str2 != null) {
                        str2.clear();
                    }
                }
            }
            return new DaemonEvent(i, parseInt, substring, str);
        } catch (Throwable e2) {
            throw new IllegalArgumentException("problem parsing code", e2);
        }
    }

    public static String[] unescapeArgs(String str) {
        String str2 = "unescapeArgs";
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        int i = 0;
        Object obj = null;
        if (str.charAt(0) == '\"') {
            obj = 1;
            i = 1;
        }
        while (i < length) {
            int i2;
            if (obj != null) {
                i2 = i;
                while (true) {
                    i2 = str.indexOf(34, i2);
                    if (i2 == -1 || str.charAt(i2 - 1) != '\\') {
                        break;
                    }
                    i2++;
                }
            } else {
                i2 = str.indexOf(32, i);
            }
            if (i2 == -1) {
                i2 = length;
            }
            String substring = str.substring(i, i2);
            i += substring.length();
            if (obj == null) {
                substring = substring.trim();
            } else {
                i++;
            }
            arrayList.add(substring.replace("\\\\", "\\").replace("\\\"", "\""));
            int indexOf = str.indexOf(32, i);
            int indexOf2 = str.indexOf(" \"", i);
            if (indexOf2 <= -1 || indexOf2 > indexOf) {
                obj = null;
                if (indexOf > -1) {
                    i = indexOf + 1;
                }
            } else {
                obj = 1;
                i = indexOf2 + 2;
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void checkCode(int i) {
        if (this.mCode != i) {
            throw new IllegalStateException("Expected " + i + " but was: " + this);
        }
    }

    public void clear() {
        clearMessage();
        clearRawEvent();
        clearParsed();
    }

    public void clearMessage() {
        if (this.mMessage != null) {
            this.mMessage.clear();
        }
    }

    public void clearParsed() {
        if (this.mParsed != null) {
            for (String str : this.mParsed) {
                if (str != null) {
                    str.clear();
                }
            }
        }
    }

    public void clearRawEvent() {
        if (this.mRawEvent != null) {
            this.mRawEvent.clear();
        }
    }

    public int getCmdNumber() {
        return this.mCmdNumber;
    }

    public int getCode() {
        return this.mCode;
    }

    public String getField(int i) {
        if (this.mParsed == null) {
            this.mParsed = unescapeArgs(this.mRawEvent);
        }
        i += 2;
        return i > this.mParsed.length ? null : this.mParsed[i];
    }

    public String getMessage() {
        return this.mMessage;
    }

    @Deprecated
    public String getRawEvent() {
        return this.mRawEvent;
    }

    public int getSubErrorCode() {
        int i = 0;
        if (this.mMessage == null) {
            return 0;
        }
        int indexOf = this.mMessage.indexOf(45) + 1;
        if (indexOf <= 0) {
            return 0;
        }
        while (indexOf < this.mMessage.length() && this.mMessage.charAt(indexOf) >= '0' && this.mMessage.charAt(indexOf) <= '9') {
            i = (i * 10) + (this.mMessage.charAt(indexOf) - 48);
            indexOf++;
        }
        return i > 0 ? i * -1 : 0;
    }

    public boolean isClassClientError() {
        return this.mCode >= BitRate.MIN_VIDEO_D1_BITRATE && this.mCode < 600;
    }

    public boolean isClassContinue() {
        return this.mCode >= 100 && this.mCode < 200;
    }

    public boolean isClassOk() {
        return this.mCode >= 200 && this.mCode < 300;
    }

    public boolean isClassServerError() {
        return this.mCode >= 400 && this.mCode < BitRate.MIN_VIDEO_D1_BITRATE;
    }

    public boolean isClassUnsolicited() {
        return isClassUnsolicited(this.mCode);
    }

    public String toString() {
        return this.mRawEvent;
    }
}
