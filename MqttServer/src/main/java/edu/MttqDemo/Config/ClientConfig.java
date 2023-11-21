package edu.MttqDemo.Config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration()
public class ClientConfig {

    @Value("${mqttUser}")
    private String username;

    @Value("${mqttPassword}")
    private String password;

    @Value("${serverUri}")
    private String serverUri;

    @Value("${caFilePath}")
    private String caFilePath;

    @Value("${clientId}")
    private String clientId;

    @Value("${keyStorePath}")
    String keyStorePath;

    @Value("${keyStorePassword}")
    String keyStorePassword;

}
