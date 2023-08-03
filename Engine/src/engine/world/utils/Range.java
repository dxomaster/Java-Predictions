package engine.world.utils;
import java.util.Random;
import java.util.TimerTask;

public class Range{
    private final float from;
    private final float to;


    public boolean isInRange(float value) {
        return (float)value >= (float)from && (float)value <= (float)to;
    }
    public Range(float from, float to ){
        if (from > to) throw new IllegalArgumentException("from must be less than to");
        this.from = from;
        this.to = to;
    }
    public Range(int from, int to ){
        if (from > to) throw new IllegalArgumentException("from must be less than to");
        this.from = from;
        this.to = to;
    }
    public float getFrom() {
        return from;
    }

    public float getTo() {
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
