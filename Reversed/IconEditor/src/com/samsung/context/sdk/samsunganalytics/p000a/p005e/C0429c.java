package com.samsung.context.sdk.samsunganalytics.p000a.p005e;

import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.samsung.context.sdk.samsunganalytics.C0316a;
import com.samsung.context.sdk.samsunganalytics.p000a.C0276a;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0272a;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0273b;
import com.samsung.context.sdk.samsunganalytics.p000a.p001a.C0274c;
import com.samsung.context.sdk.samsunganalytics.p000a.p004d.C0287b;
import com.samsung.context.sdk.samsunganalytics.p000a.p006f.C0297a;
import com.samsung.context.sdk.samsunganalytics.p000a.p013i.C0311a;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.samsung.context.sdk.samsunganalytics.a.e.c */
public class C0429c implements C0287b {
    private String f179a;
    private Map<String, String> f180b;
    private C0272a f181c;
    private HttpsURLConnection f182d;
    private SharedPreferences f183e;
    private C0276a<Void, Boolean> f184f;

    public C0429c(C0272a c0272a, String str, Map<String, String> map, SharedPreferences sharedPreferences, C0276a<Void, Boolean> c0276a) {
        this.f182d = null;
        this.f179a = str;
        this.f181c = c0272a;
        this.f180b = map;
        this.f183e = sharedPreferences;
        this.f184f = c0276a;
    }

    private void m170a(BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                return;
            }
        }
        if (this.f182d != null) {
            this.f182d.disconnect();
        }
    }

    public void m171a() {
        try {
            Builder buildUpon = Uri.parse(this.f181c.m13a()).buildUpon();
            for (String str : this.f180b.keySet()) {
                buildUpon.appendQueryParameter(str, (String) this.f180b.get(str));
            }
            String str2 = SimpleDateFormat.getTimeInstance(2, Locale.US).format(new Date());
            buildUpon.appendQueryParameter("ts", str2).appendQueryParameter("tid", this.f179a).appendQueryParameter("hc", C0294e.m84a(this.f179a + str2 + C0294e.f85c));
            this.f182d = (HttpsURLConnection) new URL(buildUpon.build().toString()).openConnection();
            this.f182d.setSSLSocketFactory(C0297a.m90a().m92b().getSocketFactory());
            this.f182d.setRequestMethod(this.f181c.m14b());
            this.f182d.setConnectTimeout(C0292b.f80p);
        } catch (Exception e) {
            C0311a.m149e("Fail to get Policy");
        }
    }

    public void m172a(JSONObject jSONObject) {
        try {
            this.f183e.edit().putInt(C0292b.f71g, jSONObject.getInt(C0292b.f71g) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT).putInt(C0292b.f67c, jSONObject.getInt(C0292b.f67c) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT).putInt(C0292b.f70f, jSONObject.getInt(C0292b.f70f) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT).putInt(C0292b.f66b, jSONObject.getInt(C0292b.f66b) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT).putString(C0292b.f72h, "https://" + jSONObject.getString(C0292b.f72h)).putString(C0292b.f73i, jSONObject.getString(C0292b.f73i)).putString(C0292b.f74j, jSONObject.getString(C0292b.f74j)).putString(C0292b.f75k, jSONObject.getString(C0292b.f75k)).putInt(C0292b.f65a, jSONObject.getInt(C0292b.f65a)).putLong(C0292b.f78n, System.currentTimeMillis()).commit();
            C0274c.DLS.m18a("https://" + jSONObject.getString(C0292b.f72h));
            C0273b.DLS_DIR.m16a(jSONObject.getString(C0292b.f73i));
            C0273b.DLS_DIR_BAT.m16a(jSONObject.getString(C0292b.f74j));
            C0311a.m143a("dq-3g: " + (jSONObject.getInt(C0292b.f67c) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) + ", dq-w: " + (jSONObject.getInt(C0292b.f66b) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) + ", oq-3g: " + (jSONObject.getInt(C0292b.f71g) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) + ", oq-w: " + (jSONObject.getInt(C0292b.f70f) * AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT));
        } catch (JSONException e) {
            C0311a.m149e("Fail to get Policy");
            C0311a.m143a("[GetPolicyClient] " + e.getMessage());
        }
    }

    public int m173b() {
        BufferedReader bufferedReader;
        Throwable th;
        int i = 0;
        BufferedReader bufferedReader2 = null;
        try {
            if (this.f182d.getResponseCode() != 200) {
                C0311a.m149e("Fail to get Policy. Response code : " + this.f182d.getResponseCode());
                i = -61;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(this.f182d.getInputStream()));
            try {
                JSONObject jSONObject = new JSONObject(bufferedReader.readLine());
                int i2 = jSONObject.getInt("rc");
                if (i2 != CredentialsApi.ACTIVITY_RESULT_ADD_ACCOUNT) {
                    C0311a.m149e("Fail to get Policy; Invalid Message. Result code : " + i2);
                    i2 = -61;
                } else {
                    C0311a.m144a("GetPolicyClient", "Get Policy Success");
                    if (TextUtils.isEmpty(this.f183e.getString(C0292b.f75k, C0316a.f163d)) && this.f184f != null) {
                        String string = jSONObject.getString(C0292b.f75k);
                        if (string != null && string.equals(C0292b.f77m)) {
                            this.f184f.m20a(Boolean.valueOf(true));
                        }
                    }
                    m172a(jSONObject);
                    i2 = i;
                }
                if (this.f182d != null) {
                    this.f182d.disconnect();
                }
                m170a(bufferedReader);
                return i2;
            } catch (Exception e) {
                bufferedReader2 = bufferedReader;
                try {
                    C0311a.m149e("Fail to get Policy");
                    m170a(bufferedReader2);
                    return -61;
                } catch (Throwable th2) {
                    bufferedReader = bufferedReader2;
                    th = th2;
                    m170a(bufferedReader);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                m170a(bufferedReader);
                throw th;
            }
        } catch (Exception e2) {
            C0311a.m149e("Fail to get Policy");
            m170a(bufferedReader2);
            return -61;
        } catch (Throwable th22) {
            bufferedReader = null;
            th = th22;
            m170a(bufferedReader);
            throw th;
        }
    }
}
