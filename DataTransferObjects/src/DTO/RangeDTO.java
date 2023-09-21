package DTO;

public class RangeDTO {
    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }

    private final float from;
    private final float to;

    public RangeDTO(float from, float to) {
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return "(" + from +
                " - " + to + ")";
    }
}
