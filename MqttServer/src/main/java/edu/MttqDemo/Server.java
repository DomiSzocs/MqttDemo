package edu.MttqDemo;

import edu.MttqDemo.CallBack.ServerCallBack;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

@Configuration
public class Server {
    @Bean
    public IMqttClient getClient() throws MqttException {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        IMqttClient client = factory.getClientInstance("tcp://localhost:1883", "CoreControlServer");
        MqttConnectOptions options = new MqttConnectOptions();

        options.setUserName("admin");
        options.setPassword("12345678".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(60);

        client.setCallback(new ServerCallBack(client));
        client.connect(options);
        client.subscribe("temperature", 1);

        return client;
    }
}