package com.samsung.android.mateservice.common;

public interface LoggerContract {
    void append(long j, String str, Object... objArr);

    void append(String str, Object... objArr);
}
