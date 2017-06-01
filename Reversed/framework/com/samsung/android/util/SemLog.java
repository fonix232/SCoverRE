package com.samsung.android.util;

import android.util.secutil.Log;
import android.util.secutil.Slog;

public final class SemLog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    private SemLog() {
    }

    public static int m16d(String str, String str2) {
        return Slog.d(str, str2);
    }

    public static int m17d(String str, String str2, Throwable th) {
        return Slog.d(str, str2, th);
    }

    public static int m18e(String str, String str2) {
        return Slog.e(str, str2);
    }

    public static int m19e(String str, String str2, Throwable th) {
        return Slog.e(str, str2, th);
    }

    public static String getStackTraceString(Throwable th) {
        return Log.getStackTraceString(th);
    }

    public static int m20i(String str, String str2) {
        return Slog.i(str, str2);
    }

    public static int m21i(String str, String str2, Throwable th) {
        return Slog.i(str, str2, th);
    }

    public static boolean isLoggable(String str, int i) {
        return Log.isLoggable(str, i);
    }

    public static int println(int i, String str, String str2) {
        return Slog.println(i, str, str2);
    }

    public static int secD(String str, String str2) {
        return Slog.secD(str, str2);
    }

    public static int secD(String str, String str2, Throwable th) {
        return Slog.secD(str, str2, th);
    }

    public static int secE(String str, String str2) {
        return Slog.secE(str, str2);
    }

    public static int secE(String str, String str2, Throwable th) {
        return Slog.secE(str, str2, th);
    }

    public static int secI(String str, String str2) {
        return Slog.secI(str, str2);
    }

    public static int secI(String str, String str2, Throwable th) {
        return Slog.secI(str, str2, th);
    }

    public static int secV(String str, String str2) {
        return Slog.secV(str, str2);
    }

    public static int secV(String str, String str2, Throwable th) {
        return Slog.secV(str, str2, th);
    }

    public static int secW(String str, String str2) {
        return Slog.secW(str, str2);
    }

    public static int secW(String str, String str2, Throwable th) {
        return Slog.secW(str, str2, th);
    }

    public static int secW(String str, Throwable th) {
        return Slog.secW(str, th);
    }

    public static int secWtf(String str, String str2) {
        return Slog.secWtf(str, str2);
    }

    public static int secWtf(String str, String str2, Throwable th) {
        return Slog.secWtf(str, str2, th);
    }

    public static int secWtf(String str, Throwable th) {
        return Slog.secWtf(str, th);
    }

    public static int secWtfStack(String str, String str2) {
        return Slog.secWtfStack(str, str2);
    }

    public static int m22v(String str, String str2) {
        return Slog.v(str, str2);
    }

    public static int m23v(String str, String str2, Throwable th) {
        return Slog.v(str, str2, th);
    }

    public static int m24w(String str, String str2) {
        return Slog.w(str, str2);
    }

    public static int m25w(String str, String str2, Throwable th) {
        return Slog.w(str, str2, th);
    }

    public static int m26w(String str, Throwable th) {
        return Slog.w(str, th);
    }

    public static int wtf(String str, String str2) {
        return Slog.wtf(str, str2);
    }

    public static int wtf(String str, String str2, Throwable th) {
        return Slog.wtf(str, str2, th);
    }

    public static int wtf(String str, Throwable th) {
        return Slog.wtf(str, th);
    }

    public static int wtfStack(String str, String str2) {
        return Slog.wtfStack(str, str2);
    }
}
