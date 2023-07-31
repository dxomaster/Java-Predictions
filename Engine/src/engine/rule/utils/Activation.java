package engine.rule.utils;

public class Activation {
    Integer ticks;
    Float probability;

    public Activation(Integer ticks, Float probability) {
        this.setTicks(ticks);
        this.setProbability(probability);
    }
    public Activation(Float probability) {
        this.ticks = 1;
        this.setProbability(probability);
    }
    private void setProbability(Float probability) {
        if (probability < 0 || probability > 1) throw new IllegalArgumentException("Probability must be between 0 and 1");
        this.probability = probability;
    }
    private void setTicks(Integer ticks) {
        if (ticks < 1) throw new IllegalArgumentException("Ticks must be greater than 0");
        this.ticks = ticks;
    }
}
