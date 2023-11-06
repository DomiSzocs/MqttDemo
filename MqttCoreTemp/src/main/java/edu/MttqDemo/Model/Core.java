package edu.MttqDemo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Core implements Serializable {
    private double temperature = 30.0;
    private double increment = 0.7;
    public void applyIncrement() {
        temperature += increment;
    }

    public void increaseIncrement() {
        increment += 0.3;
    }

    public void decreaseIncrement() {
        increment -= 0.3;
    }
}
