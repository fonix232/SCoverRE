package com.samsung.context.sdk.samsunganalytics.p000a.p006f;

import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.f.a */
public class C0297a {
    private static final String f88b = "AndroidCAStore";
    private static final String f89c = "TLS";
    private SSLContext f90a;

    /* renamed from: com.samsung.context.sdk.samsunganalytics.a.f.a.a */
    private static class C0296a {
        private static final C0297a f87a;

        static {
            f87a = new C0297a();
        }

        private C0296a() {
        }
    }

    private C0297a() {
        m91c();
    }

    public static C0297a m90a() {
        return C0296a.f87a;
    }

    private void m91c() {
        try {
            KeyStore instance = KeyStore.getInstance(KeyStore.getDefaultType());
            instance.load(null, null);
            KeyStore instance2 = KeyStore.getInstance(f88b);
            instance2.load(null, null);
            Enumeration aliases = instance2.aliases();
            while (aliases.hasMoreElements()) {
                String str = (String) aliases.nextElement();
                X509Certificate x509Certificate = (X509Certificate) instance2.getCertificate(str);
                if (str.startsWith("system:")) {
                    instance.setCertificateEntry(str, x509Certificate);
                }
            }
            TrustManagerFactory instance3 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance3.init(instance);
            this.f90a = SSLContext.getInstance(f89c);
            this.f90a.init(null, instance3.getTrustManagers(), null);
            C0311a.m143a("pinning success");
        } catch (Exception e) {
            C0311a.m143a("pinning fail : " + e.getMessage());
        }
    }

    public SSLContext m92b() {
        return this.f90a;
    }
}
