package engine.world.utils;
import java.util.Random;
import java.util.TimerTask;

public class Range{
    private final double from;
    private final double to;


    public boolean isInRange(float value) {
        return (float)value >= (float)from && (float)value <= (float)to;
    }
    public Range(float from, float to ){
        if (from > to) throw new IllegalArgumentException("from must be less than to");
        this.from = from;
        this.to = to;
    }
    public Range(double from, double to ){
        if (from > to) throw new IllegalArgumentException("from must be less than to");
        this.from = from;
        this.to = to;
    }
    public Range(int from, int to ){
        if (from > to) throw new IllegalArgumentException("from must be less than to");
        this.from = from;
        this.to = to;
    }
    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
