package com.sec.tima_keychain;

import android.os.ServiceManager;
import android.sec.enterprise.ClientCertificateManager;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.TimaKeystore;
import android.service.tima.ITimaService;
import android.util.Log;
import com.samsung.android.knox.keystore.IClientCertificateManager;
import com.samsung.android.knox.keystore.IClientCertificateManager.Stub;
import com.sec.smartcard.openssl.OpenSSLHelper;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimaKeychain {
    private static final String TAG = "TIMAKeyChain";
    private static final String TIMA_SERVICE = "tima";
    private static Object mLock = new Object();

    public static List<String> getAliasListFromTimaKeystore(String str) {
        Throwable e;
        Object obj = null;
        List<String> emptyList = Collections.emptyList();
        Log.d(TAG, "getAliasListFromTimaKeystore with package");
        if (str == null || "".equals(str)) {
            Log.e(TAG, "getAliasListFromTimaKeystore received empty/null packageName");
            return Collections.emptyList();
        }
        try {
            ClientCertificateManager clientCertificateManager = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
            List certificateAliasesHavingPrivateKey = clientCertificateManager.getCertificateAliasesHavingPrivateKey();
            Iterable<String> aliasesForPackage = clientCertificateManager.getAliasesForPackage(str);
            List aliasesForWiFi = clientCertificateManager.getAliasesForWiFi();
            if (aliasesForPackage == null) {
                return emptyList;
            }
            Object obj2;
            List<String> arrayList = new ArrayList(aliasesForPackage);
            if (aliasesForWiFi != null) {
                try {
                    if (!aliasesForWiFi.isEmpty()) {
                        obj2 = 1;
                        if (!(certificateAliasesHavingPrivateKey == null || certificateAliasesHavingPrivateKey.isEmpty())) {
                            obj = 1;
                        }
                        if (obj != null) {
                            Log.d(TAG, "all the aliases not valid since doenst have private key pair");
                            return arrayList;
                        }
                        if (!(obj2 == null && obj == null)) {
                            for (String str2 : aliasesForPackage) {
                                if (obj2 != null && aliasesForWiFi.contains(str2)) {
                                    arrayList.remove(str2);
                                }
                                if (!(obj == null || certificateAliasesHavingPrivateKey.contains(str2))) {
                                    arrayList.remove(str2);
                                }
                            }
                        }
                        return arrayList;
                    }
                } catch (Exception e2) {
                    e = e2;
                    emptyList = arrayList;
                    Log.e(TAG, "Exception", e);
                    e.printStackTrace();
                    return emptyList;
                }
            }
            obj2 = null;
            obj = 1;
            if (obj != null) {
                for (String str22 : aliasesForPackage) {
                    arrayList.remove(str22);
                    arrayList.remove(str22);
                }
                return arrayList;
            }
            Log.d(TAG, "all the aliases not valid since doenst have private key pair");
            return arrayList;
        } catch (Exception e3) {
            e = e3;
            Log.e(TAG, "Exception", e);
            e.printStackTrace();
            return emptyList;
        }
    }

    public static X509Certificate[] getCertificateChainFromTimaKeystore(String str) {
        X509Certificate[] x509CertificateArr;
        synchronized (mLock) {
            x509CertificateArr = null;
            Log.d(TAG, "getCertificateChainFromTimaKeystore called");
            if (str == null || "".equals(str)) {
                Log.e(TAG, "getCertificateChainFromTimaKeystore received empty/null alias");
            } else {
                try {
                    KeyStore.getInstance("TimaKeyStore").load(null, null);
                    KeyStore instance = KeyStore.getInstance("PKCS11", "SECPkcs11");
                    instance.load(null, null);
                    Certificate[] certificateChain = instance.getCertificateChain(str);
                    if (certificateChain != null) {
                        x509CertificateArr = new X509Certificate[certificateChain.length];
                        for (int i = 0; i < certificateChain.length; i++) {
                            x509CertificateArr[i] = (X509Certificate) certificateChain[i];
                        }
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "java.security.NoSuchProviderException", e);
                } catch (Throwable e2) {
                    Log.e(TAG, "KeyStoreException", e2);
                } catch (Throwable e3) {
                    Log.e(TAG, "IOException", e3);
                } catch (Throwable e4) {
                    Log.e(TAG, "NoSuchAlgorithmException", e4);
                } catch (Throwable e5) {
                    Log.e(TAG, "CertificateException", e5);
                }
            }
        }
        return x509CertificateArr;
    }

    public static PrivateKey getPrivateKeyFromOpenSSL(String str) {
        synchronized (mLock) {
            PrivateKey privateKey = null;
            try {
                KeyStore.getInstance("TimaKeyStore").load(null, null);
                KeyStore.getInstance("PKCS11", "SECPkcs11").load(null, null);
                Log.d(TAG, "getPrivateKeyFromOpenSSL called");
                if (str == null || "".equals(str)) {
                    Log.e(TAG, "getPrivateKeyFromOpenSSL received empty/null alias");
                } else {
                    IClientCertificateManager asInterface = Stub.asInterface(ServiceManager.getService("downloadableccm_svc"));
                    if (asInterface == null) {
                        Log.e(TAG, "Unable start CCMservice");
                        return null;
                    } else if (asInterface.hasGrant(str)) {
                        OpenSSLHelper openSSLHelper = new OpenSSLHelper();
                        if (openSSLHelper.registerEngine(str)) {
                            privateKey = openSSLHelper.getPrivateKey(str);
                        } else {
                            Log.e(TAG, "Unable to register openssl engine");
                        }
                    } else {
                        return null;
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "java.security.NoSuchProviderException", e);
            } catch (Throwable e2) {
                Log.e(TAG, "KeyStoreException", e2);
            } catch (Throwable e3) {
                Log.e(TAG, "IOException", e3);
            } catch (Throwable e4) {
                Log.e(TAG, "NoSuchAlgorithmException", e4);
            } catch (Throwable e5) {
                Log.e(TAG, "CertificateException", e5);
            } catch (Throwable e6) {
                Log.e(TAG, "RemoteException", e6);
            }
        }
        return privateKey;
    }

    public static boolean isTimaKeystoreAndCCMEnabledForCaller() {
        Object obj = null;
        boolean z = false;
        Log.d(TAG, "isTimaKeystoreAndCCMEnabled called");
        try {
            ITimaService asInterface = ITimaService.Stub.asInterface(ServiceManager.getService(TIMA_SERVICE));
            if (asInterface != null && asInterface.getTimaVersion().equals("3.0")) {
                ClientCertificateManager clientCertificateManager = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
                if (clientCertificateManager != null) {
                    obj = (!clientCertificateManager.isCCMPolicyEnabledForCaller() || clientCertificateManager.isAccessControlMethodPassword()) ? null : 1;
                }
                TimaKeystore timaKeystore = EnterpriseDeviceManager.getInstance().getTimaKeystore();
                if (timaKeystore != null) {
                    z = timaKeystore.isTimaKeystoreEnabled();
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "RemoteException", e);
        }
        return obj != null ? z : false;
    }

    public static boolean isTimaKeystoreAndCCMEnabledForPackage(String str) {
        boolean z = false;
        boolean z2 = false;
        Log.d(TAG, "isTimaKeystoreAndCCMEnabledForPackage called");
        if (str == null || "".equals(str)) {
            Log.e(TAG, "isTimaKeystoreAndCCMEnabledForPackage received empty/null package name");
        } else {
            try {
                ITimaService asInterface = ITimaService.Stub.asInterface(ServiceManager.getService(TIMA_SERVICE));
                if (asInterface != null && asInterface.getTimaVersion().equals("3.0")) {
                    ClientCertificateManager clientCertificateManager = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
                    if (clientCertificateManager != null) {
                        z = clientCertificateManager.isCCMPolicyEnabledForPackage(str) ? !clientCertificateManager.isAccessControlMethodPassword() : false;
                    }
                    TimaKeystore timaKeystore = EnterpriseDeviceManager.getInstance().getTimaKeystore();
                    if (timaKeystore != null) {
                        z2 = timaKeystore.isTimaKeystoreEnabledForPackage(str);
                    }
                }
            } catch (Throwable e) {
                Log.e(TAG, "RemoteException", e);
            }
            Log.d(TAG, "isCCMEnabled : " + z);
            Log.d(TAG, "isTimaKeystoreEnabled : " + z2);
        }
        return z ? z2 : false;
    }
}
