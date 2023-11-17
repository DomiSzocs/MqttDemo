package edu.MttqDemo.CallBack;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.MttqDemo.Model.Core;
import edu.MttqDemo.Model.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemperatureChangeCallback implements MqttCallback {

    @Autowired
    private Core core;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void connectionLost(Throwable cause) {
        log.error("Connection to MQTT broker lost.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        ServerResponse response = mapper.readValue(message.getPayload(), ServerResponse.class);
        log.info("Server Advice: {}", response.getAdvice());
        switch (topic) {
            case "increaseTemp" : {
                core.increaseIncrement();
                break;
            }
            case "decreaseTemp" : {
                core.decreaseIncrement();
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
