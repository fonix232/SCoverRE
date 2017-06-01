package com.samsung.context.sdk.samsunganalytics.p000a.p007g.p009c.p011b;

import com.samsung.context.sdk.samsunganalytics.p000a.p007g.C0309d;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.g.c.b.a */
public class C0307a {
    private static final int f134b = 25;
    protected LinkedBlockingQueue<C0309d> f135a;

    public C0307a() {
        this.f135a = new LinkedBlockingQueue(f134b);
    }

    public Queue<C0309d> m128a() {
        return this.f135a;
    }

    public void m129a(C0309d c0309d) {
        if (!this.f135a.offer(c0309d)) {
            C0311a.m144a("QueueManager", "queue size over. remove oldest log");
            this.f135a.poll();
            this.f135a.offer(c0309d);
        }
    }

    public boolean m130b() {
        return this.f135a.isEmpty();
    }
}
