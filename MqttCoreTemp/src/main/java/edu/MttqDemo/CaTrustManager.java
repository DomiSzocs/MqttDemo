package edu.MttqDemo;

import javax.net.ssl.X509TrustManager;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CaTrustManager implements X509TrustManager {

    private final X509Certificate caCertificate;

    public CaTrustManager(X509Certificate caCertificate) {
        this.caCertificate = caCertificate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        for (X509Certificate cert : x509Certificates) {
            try {
                cert.verify(caCertificate.getPublicKey());
            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                throw new CertificateException(e);
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
