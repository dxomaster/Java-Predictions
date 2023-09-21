package DTO;

public class TerminationDTO {
    private final Integer ticks;
    private final Integer seconds;

    public Integer getTicks() {
        return ticks;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public TerminationDTO(Integer ticks, Integer seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }
}
