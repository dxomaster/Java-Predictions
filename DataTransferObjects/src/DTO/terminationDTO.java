package DTO;

public class terminationDTO {
    private int ticks;
    private int seconds;

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public terminationDTO(int ticks, int seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }
}
