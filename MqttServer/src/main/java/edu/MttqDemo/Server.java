package edu.MttqDemo;

import edu.MttqDemo.Config.ClientConfig;
import edu.MttqDemo.Listener.TemperatureMessageListener;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;

@Configuration
public class Server {

    @Autowired
    ClientConfig clientConfig;

    @Bean
    public IMqttClient getClient() throws Exception {
        MqttClient mqttClient = createMqttClient();
        mqttClient.subscribe("temperature", new TemperatureMessageListener(mqttClient));
        return mqttClient;
    }

    private MqttClient createMqttClient() throws Exception {
        X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new FileInputStream(clientConfig.getCaFilePath()));

        KeyManager[] keyManagers = getKeyManagers(clientConfig.getKeyStorePath(), clientConfig.getKeyStorePassword());

        TrustManager[] trustManagers = {new CaTrustManager(caCertificate)};
        SSLSocketFactory socketFactory = createSSLSocketFactory(keyManagers, trustManagers);

        MqttClient client = new MqttClient(
                clientConfig.getServerUri(),
                clientConfig.getClientId(),
                new MemoryPersistence()
        );

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setSocketFactory(socketFactory);
        connOpts.setUserName(clientConfig.getUsername());
        connOpts.setPassword(clientConfig.getPassword().toCharArray());
        client.connect(connOpts);
        return client;
    }

    private SSLSocketFactory createSSLSocketFactory(KeyManager[] keyManagers, TrustManager[] trustManagers) throws NoSuchAlgorithmException, KeyManagementException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);
        return sslContext.getSocketFactory();
    }

    private KeyManager[] getKeyManagers(String keystorePath, String keystorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream keystoreInputStream = new FileInputStream(keystorePath)) {
            keyStore.load(keystoreInputStream, keystorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        return keyManagerFactory.getKeyManagers();
    }
}