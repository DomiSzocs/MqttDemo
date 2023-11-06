package edu.MttqDemo;

import edu.MttqDemo.Model.Core;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreFactory {
    @Bean
    public Core getCore() {
        return new Core();
    }
}
