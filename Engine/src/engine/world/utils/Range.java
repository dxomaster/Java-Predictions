package engine.world.utils;
import java.util.Random;
import java.util.TimerTask;

public class Range<T>{
    private final T from;
    private final T to;

    public boolean isInRange(T value) {
        return (float)value >= (float)from && (float)value <= (float)to;
    }
    public Range(T from, T to ){
        this.from = from;
        this.to = to;
    }
    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    public T generateRandomValueInRange()
    {
        Random random = new Random();
        if (from.getClass() == Integer.class)
        {
            return (T)Integer.valueOf(random.nextInt((Integer)to - (Integer)from) + (Integer)from);
        }
        else {
            return (T)Float.valueOf(random.nextFloat() * ((Float)to - (Float)from) + (Float)from);
        }

    }
}
