package DTO;

public class SimulationArtifactDTO {
    private final String UUID;
    private final String finishedReason;

    public SimulationArtifactDTO(String UUID, String finishedReason) {
        this.UUID = UUID;
        this.finishedReason = finishedReason;

    }

    public String getUUID() {
        return UUID;
    }

    public String getFinishedReason() {
        return finishedReason;
    }
}
