package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p014b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.Configuration;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0286a;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0292b;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0293d;
import com.samsung.context.sdk.samsunganalytics.p000a.p005e.C0429c;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0432a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.b.b */
public class C0645b extends C0432a {

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.b.b.1 */
    class C04341 extends C0286a {
        final /* synthetic */ int f207a;
        final /* synthetic */ C0645b f208b;

        C04341(C0645b c0645b, int i) {
            this.f208b = c0645b;
            this.f207a = i;
        }

        public void m187a(int i, String str, String str2) {
        }

        public void m188b(int i, String str, String str2) {
            this.f208b.e.m120a(Long.valueOf(str).longValue(), C0316a.f163d, str2);
            C0293d.m82b(this.f208b.a, this.f207a, str2.getBytes().length * -1);
        }
    }

    public C0645b(Context context, Configuration configuration) {
        super(context, configuration);
    }

    private int m204a(int i) {
        if (i == -4) {
            C0311a.m144a("DLS Sender", "Network unavailable.");
            return -4;
        } else if (C0293d.m80a(this.a)) {
            C0311a.m144a("DLS Sender", "policy expired. request policy");
            return -6;
        } else if (this.b.getRestrictedNetworkType() != i) {
            return 0;
        } else {
            C0311a.m144a("DLS Sender", "Network unavailable by restrict option:" + i);
            return -4;
        }
    }

    private int m205a(int i, C0309d c0309d, C0286a c0286a, boolean z) {
        if (c0309d == null) {
            return -100;
        }
        int length = c0309d.m137c().getBytes().length;
        if (!C0293d.m81a(this.a, i, length)) {
            return -1;
        }
        C0293d.m82b(this.a, i, length);
        C0287b c0433a = new C0433a(c0309d, this.b.getTrackingId(), c0286a);
        if (z) {
            C0311a.m143a("sync send");
            c0433a.m185a();
            return c0433a.m186b();
        }
        this.f.m63a(c0433a);
        return 0;
    }

    private int m206a(int i, Queue<C0309d> queue, C0286a c0286a) {
        while (!queue.isEmpty()) {
            Queue linkedBlockingQueue = new LinkedBlockingQueue();
            int a = C0293d.m74a(this.a, i);
            int i2 = C0292b.f81q > a ? a : 51200;
            int i3 = 0;
            while (!queue.isEmpty()) {
                C0309d c0309d = (C0309d) queue.element();
                if (c0309d.m137c().getBytes().length + i3 > i2) {
                    break;
                }
                i3 += c0309d.m137c().getBytes().length;
                linkedBlockingQueue.add(c0309d);
                queue.poll();
                this.e.m123a(c0309d.m132a());
            }
            if (linkedBlockingQueue.isEmpty()) {
                return -1;
            }
            m208a(i, linkedBlockingQueue, i3, c0286a);
        }
        return 0;
    }

    private void m208a(int i, Queue<C0309d> queue, int i2, C0286a c0286a) {
        C0293d.m82b(this.a, i, i2);
        this.f.m63a(new C0433a((Queue) queue, this.b.getTrackingId(), c0286a));
    }

    private int m209b() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.a.getSystemService("connectivity")).getActiveNetworkInfo();
        return (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) ? -4 : activeNetworkInfo.getType();
    }

    public int m211d(Map<String, String> map) {
        if (!m179a()) {
            return -5;
        }
        int b = m209b();
        int a = m204a(b);
        if (a != 0) {
            m181c(map);
            if (a != -6) {
                return a;
            }
            C0293d.m77a(this.a, this.b, this.f, this.c);
            this.e.m125c();
            return a;
        }
        C0286a c04341 = new C04341(this, b);
        m205a(b, new C0309d(Long.valueOf((String) map.get("ts")).longValue(), m180b(m178a(map))), c04341, false);
        Queue d = this.e.m126d();
        if (this.e.m124b()) {
            m206a(b, d, c04341);
        } else {
            while (!d.isEmpty()) {
                a = m205a(b, (C0309d) d.poll(), c04341, false);
                if (a != 0) {
                    return a;
                }
            }
        }
        return 0;
    }

    public int m212e(Map<String, String> map) {
        if (!m179a()) {
            return -5;
        }
        int b = m209b();
        int a = m204a(b);
        if (a != 0) {
            if (a != -6) {
                return a;
            }
            C0429c a2 = C0293d.m75a(this.a, this.b, this.c, null);
            a2.m171a();
            a = a2.m173b();
            C0311a.m143a("get policy sync " + a);
            if (a != 0) {
                return a;
            }
        }
        return m205a(b, new C0309d(Long.valueOf((String) map.get("ts")).longValue(), m180b(m178a(map))), null, true);
    }
}
