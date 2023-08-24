package world.utils;

public class Range implements java.io.Serializable {
    private final PropertyType type;
    private final Object from;
    private final Object to;


    public Range(Object from, Object to, PropertyType type) {
        if (type == PropertyType.DECIMAL) {
            if ((Integer) from > (Integer) to) {
                throw new IllegalArgumentException("From cannot be greater than to");
            }
        } else {
            if ((Float) from > (Float) to) {
                throw new IllegalArgumentException("From cannot be greater than to");
            }
        }
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public boolean isInRange(Object value) {
        if (type == PropertyType.DECIMAL)
            return (Integer) value >= (Integer) from && (Integer) value <= (Integer) to;
        else
            return (Float) value >= (Float) from && (Float) value <= (Float) to;
    }

    public Object getFrom() {
        return from;
    }

    public Object getTo() {
        return to;
    }

    @Override
    public String toString() {

        return "(" + from +
                " - " + to + ")";
    }
}
