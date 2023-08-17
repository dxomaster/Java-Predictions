package engine.rule.utils;

import java.util.Random;

public class Activation implements java.io.Serializable {
    private Integer ticks;
    private Double probability;

    public Activation() {
        this.ticks = 1;
        this.probability = 1d;
    }

    public Activation(Integer ticks, Double probability) {
        this.setTicks(ticks);
        this.setProbability(probability);
    }

    public Activation(Integer ticks) {
        this.setTicks(ticks);
        this.probability = 1d;
    }

    public Activation(Double probability) {
        this.ticks = 1;
        this.setProbability(probability);
    }

    private void setProbability(Double probability) {
        if (probability < 0 || probability > 1)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        this.probability = probability;
    }

    private void setTicks(Integer ticks) {
        if (ticks < 1) throw new IllegalArgumentException("Ticks must be greater than 0");
        this.ticks = ticks;
    }

    @Override
    public String toString() {
        return "Ticks: " + ticks +
                ", Probability: " + probability;
    }

    public boolean isActivated(Integer tick) {
        Random random = new Random();
        return tick % ticks == 0 && random.nextDouble() < probability;
    }
}
