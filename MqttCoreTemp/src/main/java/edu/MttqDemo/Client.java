package edu.MttqDemo;

import edu.MttqDemo.Config.ClientConfig;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class Client {
    @Autowired
    ClientConfig clientConfig;

    @Autowired
    MqttCallback callback;

    @Bean
    public IMqttClient getClient() throws Exception {

        MqttClient mqttClient = createMqttClient();
        mqttClient.setCallback(callback);
        mqttClient.subscribe("increaseTemp", 1);
        mqttClient.subscribe("decreaseTemp", 1);
        return mqttClient;
    }

    private MqttClient createMqttClient() throws Exception {
        X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new ClassPathResource(clientConfig.getCaFilePath()).getInputStream());

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
        try (InputStream keystoreInputStream = new ClassPathResource(keystorePath).getInputStream()) {
            keyStore.load(keystoreInputStream, keystorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        return keyManagerFactory.getKeyManagers();
    }
}
