package DTO;

public class RangeDTO {
    public float getFrom() {
        return from;
    }

    public float getTo() {
        return to;
    }

    private float from;
    private float to;

    public RangeDTO(float from, float to) {
        this.from = from;
        this.to = to;
    }
}
