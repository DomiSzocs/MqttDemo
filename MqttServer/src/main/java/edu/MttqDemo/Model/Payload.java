package edu.MttqDemo.Model;

import java.io.Serializable;

public class Payload implements Serializable {
    private double temperature;

    public Payload() {
    }

    public Payload(double temperature) {
        this.temperature = temperature;
    }

    // Getter and setter methods for temperature (optional)
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
