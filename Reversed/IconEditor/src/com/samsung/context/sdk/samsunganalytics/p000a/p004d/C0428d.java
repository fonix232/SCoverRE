package com.samsung.context.sdk.samsunganalytics.p000a.p004d;

import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.d.d */
public class C0428d implements C0288c {
    private static ExecutorService f177a;
    private static C0428d f178b;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.d.d.1 */
    class C02891 implements ThreadFactory {
        final /* synthetic */ C0428d f57a;

        C02891(C0428d c0428d) {
            this.f57a = c0428d;
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(1);
            thread.setDaemon(true);
            C0311a.m148d("newThread on Executor");
            return thread;
        }
    }

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.d.d.2 */
    class C02902 implements Runnable {
        final /* synthetic */ C0287b f58a;
        final /* synthetic */ C0428d f59b;

        C02902(C0428d c0428d, C0287b c0287b) {
            this.f59b = c0428d;
            this.f58a = c0287b;
        }

        public void run() {
            this.f58a.m61a();
            this.f58a.m62b();
        }
    }

    public C0428d() {
        f177a = Executors.newSingleThreadExecutor(new C02891(this));
    }

    public static C0288c m168a() {
        if (f178b == null) {
            f178b = new C0428d();
        }
        return f178b;
    }

    public void m169a(C0287b c0287b) {
        f177a.submit(new C02902(this, c0287b));
    }
}
