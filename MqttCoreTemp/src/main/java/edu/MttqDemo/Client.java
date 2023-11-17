package edu.MttqDemo;

import edu.MttqDemo.Config.ClientConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class Client {
    @Autowired
    ClientConfig clientConfig;

    @Autowired
    MqttCallback callback;

    @Bean
    public IMqttClient getClient() throws MqttException, FileNotFoundException, CertificateException, NoSuchAlgorithmException, KeyManagementException {

        MqttClient mqttClient = createMqttClient();
        mqttClient.setCallback(callback);
        mqttClient.subscribe("increaseTemp", 1);
        mqttClient.subscribe("decreaseTemp", 1);
        return mqttClient;
    }

    private MqttClient createMqttClient() throws MqttException, CertificateException, FileNotFoundException, NoSuchAlgorithmException, KeyManagementException {
        X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new FileInputStream(clientConfig.getCaFilePath()));

        TrustManager[] trustManagers = {new CaTrustManager(caCertificate)};
        SSLSocketFactory socketFactory = createSSLSocketFactory(trustManagers);

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

    private SSLSocketFactory createSSLSocketFactory(TrustManager[] trustManagers) throws NoSuchAlgorithmException, KeyManagementException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, null);
        return sslContext.getSocketFactory();
    }
}
