package com.example.admin.hn.volley;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by geek on 2016/12/29.
 * 生成SSL证书
 */
public class InitSSLSocketFactory {

    //SSL证书
    public static final String load = "";


    /**
     * 生成SSLSocketFactory
     *
     * @return 返回SSLSocketFactory
     */
    public static SSLSocketFactory initSSLSocketFactory() {
        //生成证书:Certificate
        CertificateFactory cf = null;
        SSLSocketFactory factory = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new ByteArrayInputStream(load.getBytes());
            Certificate ca = null;
            try {
                ca = cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //初始化公钥:keyStore
            String keyType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            //初始化TrustManagerFactory
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory managerFactory = TrustManagerFactory.getInstance(algorithm);
            managerFactory.init(keyStore);

            //初始化sslContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, managerFactory.getTrustManagers(), null);
            factory = sslContext.getSocketFactory();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return factory;
    }

    /**
     * 根据ssl证书生成
     *
     * @param context
     * @param certRawResId 证书地址
     * @return 返回SSLSocketFactory
     */
    private static SSLSocketFactory buildSSLSocketFactory(Context context, int certRawResId) {
        KeyStore keyStore = null;
        try {
            keyStore = buildKeyStore(context, certRawResId);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();

    }

    private static KeyStore buildKeyStore(Context context, int certRawResId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceID) {
        InputStream inputStream = context.getResources().openRawResource(certResourceID);
        Certificate ca = null;

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(inputStream);

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return ca;
    }


}
