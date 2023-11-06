package edu.MttqDemo;

import edu.MttqDemo.CallBack.TemperatureChangeCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

@Configuration
public class Client {
    @Autowired
    TemperatureChangeCallback callback;

    @Bean
    public IMqttClient getClient() throws MqttException {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        IMqttClient client = factory.getClientInstance("tcp://localhost:1883", "CoreTemp");
        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName("admin");
        options.setPassword("12345678".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(60);

        client.setCallback(callback);
        client.connect(options);
        client.subscribe("increaseTemp", 1);
        client.subscribe("decreaseTemp", 1);

        return client;
    }
}
