package edu.MttqDemo.Listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.MttqDemo.Model.Payload;
import edu.MttqDemo.Model.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemperatureMessageListener implements IMqttMessageListener {
    IMqttClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public TemperatureMessageListener(IMqttClient client) {
        this.client = client;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Payload payload = mapper.readValue(message.getPayload(), Payload.class);
        double temperature =  payload.getTemperature();
        log.info("Current core temperature: {}", temperature);

        ServerResponse response = new ServerResponse();
        if (temperature > 30.0) {
            response.setAdvice("Decrease temperature!");
            String jsonString = mapper.writeValueAsString(response);
            MqttMessage responseMessage = new MqttMessage(jsonString.getBytes());
            client.publish("decreaseTemp", responseMessage);
        } else {
            response.setAdvice("Increase temperature!");
            String jsonString = mapper.writeValueAsString(response);
            MqttMessage responseMessage = new MqttMessage(jsonString.getBytes());
            client.publish("increaseTemp", responseMessage);
        }
    }
}
