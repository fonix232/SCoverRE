package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p014b;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0272a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0286a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0294e;
import com.samsung.context.sdk.samsunganalytics.p000a.p006f.C0297a;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0313b;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.b.a */
public class C0433a implements C0287b {
    private static final C0272a f198a;
    private static final C0272a f199b;
    private static final int f200c = 2000;
    private Queue<C0309d> f201d;
    private C0309d f202e;
    private String f203f;
    private HttpsURLConnection f204g;
    private C0286a f205h;
    private Boolean f206i;

    static {
        f198a = C0272a.SEND_LOG;
        f199b = C0272a.SEND_BUFFERED_LOG;
    }

    public C0433a(C0309d c0309d, String str, C0286a c0286a) {
        this.f204g = null;
        this.f206i = Boolean.valueOf(false);
        this.f202e = c0309d;
        this.f203f = str;
        this.f205h = c0286a;
    }

    public C0433a(Queue<C0309d> queue, String str, C0286a c0286a) {
        this.f204g = null;
        this.f206i = Boolean.valueOf(false);
        this.f201d = queue;
        this.f203f = str;
        this.f205h = c0286a;
        this.f206i = Boolean.valueOf(true);
    }

    private void m182a(int i, String str) {
        if (this.f205h != null) {
            if (i != 200 || !str.equalsIgnoreCase("1000")) {
                if (this.f206i.booleanValue()) {
                    while (!this.f201d.isEmpty()) {
                        C0309d c0309d = (C0309d) this.f201d.poll();
                        this.f205h.m60b(i, c0309d.m135b() + C0316a.f163d, c0309d.m137c());
                    }
                    return;
                }
                this.f205h.m60b(i, this.f202e.m135b() + C0316a.f163d, this.f202e.m137c());
            }
        }
    }

    private void m183a(BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                return;
            }
        }
        if (this.f204g != null) {
            this.f204g.disconnect();
        }
    }

    private String m184c() {
        if (!this.f206i.booleanValue()) {
            return this.f202e.m137c();
        }
        Iterator it = this.f201d.iterator();
        String c = ((C0309d) it.next()).m137c();
        while (it.hasNext()) {
            c = c + C0313b.f153a + ((C0309d) it.next()).m137c();
        }
        return c;
    }

    public void m185a() {
        try {
            C0272a c0272a = this.f206i.booleanValue() ? f199b : f198a;
            Builder buildUpon = Uri.parse(c0272a.m13a()).buildUpon();
            String format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
            buildUpon.appendQueryParameter("ts", format).appendQueryParameter("tid", this.f203f).appendQueryParameter("hc", C0294e.m84a(this.f203f + format + C0294e.f85c));
            this.f204g = (HttpsURLConnection) new URL(buildUpon.build().toString()).openConnection();
            this.f204g.setSSLSocketFactory(C0297a.m90a().m92b().getSocketFactory());
            this.f204g.setRequestMethod(c0272a.m14b());
            this.f204g.addRequestProperty("Content-Encoding", this.f206i.booleanValue() ? "gzip" : "text");
            this.f204g.setConnectTimeout(f200c);
            String c = m184c();
            if (!TextUtils.isEmpty(c)) {
                this.f204g.setDoOutput(true);
                OutputStream bufferedOutputStream = this.f206i.booleanValue() ? new BufferedOutputStream(new GZIPOutputStream(this.f204g.getOutputStream())) : new BufferedOutputStream(this.f204g.getOutputStream());
                bufferedOutputStream.write(c.getBytes());
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            C0311a.m143a("[DLS Client] Send to DLS : " + c);
        } catch (Exception e) {
            C0311a.m149e("[DLS Client] Send fail.");
            C0311a.m143a("[DLS Client] " + e.getMessage());
        }
    }

    public int m186b() {
        int i;
        Exception e;
        Throwable th;
        BufferedReader bufferedReader;
        try {
            int responseCode = this.f204g.getResponseCode();
            bufferedReader = new BufferedReader(new InputStreamReader(this.f204g.getInputStream()));
            try {
                String string = new JSONObject(bufferedReader.readLine()).getString("rc");
                if (responseCode == 200 && string.equalsIgnoreCase("1000")) {
                    i = 1;
                    C0311a.m148d("[DLS Sender] send result success : " + responseCode + " " + string);
                } else {
                    i = -7;
                    C0311a.m148d("[DLS Sender] send result fail : " + responseCode + " " + string);
                }
                m182a(responseCode, string);
                m183a(bufferedReader);
            } catch (Exception e2) {
                e = e2;
                try {
                    C0311a.m149e("[DLS Client] Send fail.");
                    C0311a.m143a("[DLS Client] " + e.getMessage());
                    i = -41;
                    m182a(0, C0316a.f163d);
                    m183a(bufferedReader);
                    return i;
                } catch (Throwable th2) {
                    th = th2;
                    m183a(bufferedReader);
                    throw th;
                }
            }
        } catch (Exception e3) {
            e = e3;
            bufferedReader = null;
            C0311a.m149e("[DLS Client] Send fail.");
            C0311a.m143a("[DLS Client] " + e.getMessage());
            i = -41;
            m182a(0, C0316a.f163d);
            m183a(bufferedReader);
            return i;
        } catch (Throwable th3) {
            th = th3;
            bufferedReader = null;
            m183a(bufferedReader);
            throw th;
        }
        return i;
    }
}
