package edu.MttqDemo.Temperature;

import edu.MttqDemo.Model.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TemperatureChangeSimulator {
    @Autowired
    private Core core;

    @Scheduled(fixedRate = 1000)
    public void applyChange() {
        core.applyIncrement();
    }
}
