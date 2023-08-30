package DTO;

public class RunEndDTO implements java.io.Serializable {
    private final String UUID;
    private final String finishedReason;
    private final String formattedDate;

    public RunEndDTO(String UUID, String finishedReason, String formattedDate) {
        this.UUID = UUID;
        this.finishedReason = finishedReason;
        this.formattedDate = formattedDate;
    }

    public String getUUID() {
        return UUID;
    }

    public String getFinishedReason() {
        return finishedReason;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String toString() {
        return "Run ID: " + UUID + "\n" +
                "Finished by: " + finishedReason + "\n" +
                "Date: " + formattedDate + "\n";
    }
}
