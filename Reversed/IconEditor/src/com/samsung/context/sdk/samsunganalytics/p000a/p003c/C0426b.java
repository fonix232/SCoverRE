package com.samsung.context.sdk.samsunganalytics.p000a.p003c;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.c.b */
public class C0426b implements C0283a {
    public String m166a(String str, Throwable th) {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        String obj = stringWriter.toString();
        printWriter.close();
        return "thread(" + str + ") : " + obj;
    }
}
