package com.samsung.android.app.ledcover.update;

import android.os.AsyncTask;

public class StubRequest extends AsyncTask<String, Void, StubData> {
    private boolean isChina;
    private StubListener listener;
    private int type;
    private String url;

    public void setType(int type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsChina(boolean isChina) {
        this.isChina = isChina;
    }

    public void setListener(StubListener listener) {
        this.listener = listener;
    }

    public void run() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{this.url});
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected com.samsung.android.app.ledcover.update.StubData doInBackground(java.lang.String... r25) {
        /*
        r24 = this;
        r16 = new com.samsung.android.app.ledcover.update.StubData;
        r16.<init>();
        r12 = 0;
        r20 = 0;
        r14 = r25[r20];	 Catch:{ Exception -> 0x00a7 }
        r0 = r24;
        r0 = r0.isChina;	 Catch:{ Exception -> 0x00a7 }
        r20 = r0;
        if (r20 == 0) goto L_0x0028;
    L_0x0012:
        r2 = com.samsung.android.app.ledcover.update.StubUtil.getChinaURL();	 Catch:{ Exception -> 0x00a7 }
        r20 = "";
        r0 = r20;
        r20 = r2.equals(r0);	 Catch:{ Exception -> 0x00a7 }
        if (r20 != 0) goto L_0x0028;
    L_0x0020:
        r20 = "vas.samsungapps.com";
        r0 = r20;
        r14 = r14.replaceFirst(r0, r2);	 Catch:{ Exception -> 0x00a7 }
    L_0x0028:
        r18 = new java.net.URL;	 Catch:{ Exception -> 0x00a7 }
        r0 = r18;
        r0.<init>(r14);	 Catch:{ Exception -> 0x00a7 }
        com.samsung.android.app.ledcover.update.StubUtil.log(r14);	 Catch:{ Exception -> 0x00a7 }
        r3 = r18.openConnection();	 Catch:{ Exception -> 0x00a7 }
        r3 = (java.net.HttpURLConnection) r3;	 Catch:{ Exception -> 0x00a7 }
        r20 = 1;
        r0 = r20;
        r3.setInstanceFollowRedirects(r0);	 Catch:{ Exception -> 0x00a7 }
        r20 = "https";
        r21 = r18.getProtocol();	 Catch:{ Exception -> 0x00a7 }
        r20 = r20.equals(r21);	 Catch:{ Exception -> 0x00a7 }
        if (r20 == 0) goto L_0x0058;
    L_0x004b:
        r0 = r3;
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ Exception -> 0x00a7 }
        r20 = r0;
        r21 = new org.apache.http.conn.ssl.StrictHostnameVerifier;	 Catch:{ Exception -> 0x00a7 }
        r21.<init>();	 Catch:{ Exception -> 0x00a7 }
        r20.setHostnameVerifier(r21);	 Catch:{ Exception -> 0x00a7 }
    L_0x0058:
        r11 = r3.getRequestProperties();	 Catch:{ Exception -> 0x00a7 }
        r20 = r11.keySet();	 Catch:{ Exception -> 0x00a7 }
        r21 = r20.iterator();	 Catch:{ Exception -> 0x00a7 }
    L_0x0064:
        r20 = r21.hasNext();	 Catch:{ Exception -> 0x00a7 }
        if (r20 == 0) goto L_0x00b1;
    L_0x006a:
        r8 = r21.next();	 Catch:{ Exception -> 0x00a7 }
        r8 = (java.lang.String) r8;	 Catch:{ Exception -> 0x00a7 }
        r20 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a7 }
        r20.<init>();	 Catch:{ Exception -> 0x00a7 }
        r0 = r20;
        r20 = r0.append(r8);	 Catch:{ Exception -> 0x00a7 }
        r22 = ": ";
        r0 = r20;
        r1 = r22;
        r22 = r0.append(r1);	 Catch:{ Exception -> 0x00a7 }
        r20 = r11.get(r8);	 Catch:{ Exception -> 0x00a7 }
        r20 = (java.util.List) r20;	 Catch:{ Exception -> 0x00a7 }
        r23 = 0;
        r0 = r20;
        r1 = r23;
        r20 = r0.get(r1);	 Catch:{ Exception -> 0x00a7 }
        r20 = (java.lang.String) r20;	 Catch:{ Exception -> 0x00a7 }
        r0 = r22;
        r1 = r20;
        r20 = r0.append(r1);	 Catch:{ Exception -> 0x00a7 }
        r20 = r20.toString();	 Catch:{ Exception -> 0x00a7 }
        com.samsung.android.app.ledcover.update.StubUtil.log(r20);	 Catch:{ Exception -> 0x00a7 }
        goto L_0x0064;
    L_0x00a7:
        r5 = move-exception;
    L_0x00a8:
        com.samsung.android.app.ledcover.update.StubUtil.log(r5);	 Catch:{ all -> 0x00e6 }
        r16 = 0;
        com.samsung.android.app.ledcover.update.StubUtil.close(r12);
    L_0x00b0:
        return r16;
    L_0x00b1:
        r20 = "\n";
        com.samsung.android.app.ledcover.update.StubUtil.log(r20);	 Catch:{ Exception -> 0x00a7 }
        r7 = r3.getHeaderFields();	 Catch:{ Exception -> 0x00a7 }
        r20 = r7.keySet();	 Catch:{ Exception -> 0x00a7 }
        r21 = r20.iterator();	 Catch:{ Exception -> 0x00a7 }
    L_0x00c2:
        r20 = r21.hasNext();	 Catch:{ Exception -> 0x00a7 }
        if (r20 == 0) goto L_0x0122;
    L_0x00c8:
        r8 = r21.next();	 Catch:{ Exception -> 0x00a7 }
        r8 = (java.lang.String) r8;	 Catch:{ Exception -> 0x00a7 }
        if (r8 != 0) goto L_0x00eb;
    L_0x00d0:
        r20 = r7.get(r8);	 Catch:{ Exception -> 0x00a7 }
        r20 = (java.util.List) r20;	 Catch:{ Exception -> 0x00a7 }
        r22 = 0;
        r0 = r20;
        r1 = r22;
        r20 = r0.get(r1);	 Catch:{ Exception -> 0x00a7 }
        r20 = (java.lang.String) r20;	 Catch:{ Exception -> 0x00a7 }
        com.samsung.android.app.ledcover.update.StubUtil.log(r20);	 Catch:{ Exception -> 0x00a7 }
        goto L_0x00c2;
    L_0x00e6:
        r20 = move-exception;
    L_0x00e7:
        com.samsung.android.app.ledcover.update.StubUtil.close(r12);
        throw r20;
    L_0x00eb:
        r20 = r7.get(r8);	 Catch:{ Exception -> 0x00a7 }
        r20 = (java.util.List) r20;	 Catch:{ Exception -> 0x00a7 }
        r20 = r20.iterator();	 Catch:{ Exception -> 0x00a7 }
    L_0x00f5:
        r22 = r20.hasNext();	 Catch:{ Exception -> 0x00a7 }
        if (r22 == 0) goto L_0x00c2;
    L_0x00fb:
        r19 = r20.next();	 Catch:{ Exception -> 0x00a7 }
        r19 = (java.lang.String) r19;	 Catch:{ Exception -> 0x00a7 }
        r22 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a7 }
        r22.<init>();	 Catch:{ Exception -> 0x00a7 }
        r0 = r22;
        r22 = r0.append(r8);	 Catch:{ Exception -> 0x00a7 }
        r23 = ": ";
        r22 = r22.append(r23);	 Catch:{ Exception -> 0x00a7 }
        r0 = r22;
        r1 = r19;
        r22 = r0.append(r1);	 Catch:{ Exception -> 0x00a7 }
        r22 = r22.toString();	 Catch:{ Exception -> 0x00a7 }
        com.samsung.android.app.ledcover.update.StubUtil.log(r22);	 Catch:{ Exception -> 0x00a7 }
        goto L_0x00f5;
    L_0x0122:
        r20 = "\n";
        com.samsung.android.app.ledcover.update.StubUtil.log(r20);	 Catch:{ Exception -> 0x00a7 }
        r20 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r21 = r3.getResponseCode();	 Catch:{ Exception -> 0x00a7 }
        r0 = r20;
        r1 = r21;
        if (r0 == r1) goto L_0x015c;
    L_0x0133:
        r20 = new java.io.IOException;	 Catch:{ Exception -> 0x00a7 }
        r21 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00a7 }
        r21.<init>();	 Catch:{ Exception -> 0x00a7 }
        r22 = "status code ";
        r21 = r21.append(r22);	 Catch:{ Exception -> 0x00a7 }
        r22 = r3.getResponseCode();	 Catch:{ Exception -> 0x00a7 }
        r21 = r21.append(r22);	 Catch:{ Exception -> 0x00a7 }
        r22 = " != ";
        r21 = r21.append(r22);	 Catch:{ Exception -> 0x00a7 }
        r22 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r21 = r21.append(r22);	 Catch:{ Exception -> 0x00a7 }
        r21 = r21.toString();	 Catch:{ Exception -> 0x00a7 }
        r20.<init>(r21);	 Catch:{ Exception -> 0x00a7 }
        throw r20;	 Catch:{ Exception -> 0x00a7 }
    L_0x015c:
        r15 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x00a7 }
        r15.<init>();	 Catch:{ Exception -> 0x00a7 }
        r13 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x00a7 }
        r20 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x00a7 }
        r21 = r3.getInputStream();	 Catch:{ Exception -> 0x00a7 }
        r20.<init>(r21);	 Catch:{ Exception -> 0x00a7 }
        r0 = r20;
        r13.<init>(r0);	 Catch:{ Exception -> 0x00a7 }
    L_0x0171:
        r9 = r13.readLine();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r9 == 0) goto L_0x0182;
    L_0x0177:
        com.samsung.android.app.ledcover.update.StubUtil.log(r9);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r15.append(r9);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x0171;
    L_0x017e:
        r5 = move-exception;
        r12 = r13;
        goto L_0x00a8;
    L_0x0182:
        r20 = "\n";
        com.samsung.android.app.ledcover.update.StubUtil.log(r20);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r6 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r10 = r6.newPullParser();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r20 = new java.io.StringReader;	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r21 = r15.toString();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r20.<init>(r21);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r20;
        r10.setInput(r0);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r4 = r10.getEventType();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
    L_0x01a1:
        r20 = 1;
        r0 = r20;
        if (r4 == r0) goto L_0x02f8;
    L_0x01a7:
        switch(r4) {
            case 2: goto L_0x01af;
            default: goto L_0x01aa;
        };	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
    L_0x01aa:
        r4 = r10.next();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01a1;
    L_0x01af:
        r17 = r10.getName();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r20 = "appId";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x01cf;
    L_0x01bf:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setAppId(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x01cb:
        r20 = move-exception;
        r12 = r13;
        goto L_0x00e7;
    L_0x01cf:
        r20 = "resultCode";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x01e7;
    L_0x01db:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setResultCode(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x01e7:
        r20 = "resultMsg";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x01ff;
    L_0x01f3:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setResultMsg(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x01ff:
        r20 = "versionCode";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x0217;
    L_0x020b:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setVersionCode(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x0217:
        r20 = "versionName";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x0230;
    L_0x0223:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setVersionName(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x0230:
        r20 = "contentSize";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x0249;
    L_0x023c:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setContentSize(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x0249:
        r20 = "downloadURI";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x0262;
    L_0x0255:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setDownloadURI(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x0262:
        r20 = "deltaDownloadURI";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x027b;
    L_0x026e:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setDeltaDownloadURI(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x027b:
        r20 = "deltaContentSize";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x0294;
    L_0x0287:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setDeltaContentSize(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x0294:
        r20 = "signature";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x02ad;
    L_0x02a0:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setSignature(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x02ad:
        r20 = "gSignatureDownloadURL";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x02c6;
    L_0x02b9:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setgSignatureDownloadURL(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x02c6:
        r20 = "productId";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x02df;
    L_0x02d2:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setProductId(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x02df:
        r20 = "productName";
        r0 = r17;
        r1 = r20;
        r20 = r0.equalsIgnoreCase(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        if (r20 == 0) goto L_0x01aa;
    L_0x02eb:
        r20 = r10.nextText();	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        r0 = r16;
        r1 = r20;
        r0.setProductName(r1);	 Catch:{ Exception -> 0x017e, all -> 0x01cb }
        goto L_0x01aa;
    L_0x02f8:
        com.samsung.android.app.ledcover.update.StubUtil.close(r13);
        r12 = r13;
        goto L_0x00b0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.app.ledcover.update.StubRequest.doInBackground(java.lang.String[]):com.samsung.android.app.ledcover.update.StubData");
    }

    protected void onPostExecute(StubData data) {
        if (this.type == 1) {
            if (data == null || StubUtil.isError(data)) {
                this.listener.onUpdateCheckFail(data);
            } else if (StubUtil.isNoMatchingApplication(data)) {
                this.listener.onNoMatchingApplication(data);
            } else if (StubUtil.isUpdateNotNecessary(data)) {
                this.listener.onUpdateNotNecessary(data);
            } else if (StubUtil.isUpdateAvailable(data)) {
                this.listener.onUpdateAvailable(data);
            }
        } else if (this.type != 2) {
        } else {
            if (data == null || StubUtil.isDownloadNotAvailable(data)) {
                this.listener.onGetDownloadUrlFail(data);
            } else if (StubUtil.isDownloadAvailable(data)) {
                this.listener.onGetDownloadUrlSuccess(data);
            }
        }
    }
}
