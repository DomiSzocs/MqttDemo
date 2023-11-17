package edu.MttqDemo.Temperature;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.MttqDemo.Model.Core;
import edu.MttqDemo.Model.Payload;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemperatureReporter {
    @Autowired
    private Core core;

    @Autowired
    private IMqttClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    @Scheduled(fixedRate = 5000)
    public void report() {
        try {
            Payload payload = new Payload(core.getTemperature());
            String jsonString = mapper.writeValueAsString(payload);

            MqttMessage message = new MqttMessage(jsonString.getBytes());
            client.publish("temperature", message);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
