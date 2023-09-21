package DTO;

public class RunEndDTO implements java.io.Serializable {
    private final String UUID;
    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    private String finishedReason;
    private final String formattedDate;

    public String getStatus() {
        return status;
    }

    private final String status;

    public RunEndDTO(String UUID, String finishedReason, String formattedDate, String errorMessage, String status) {
        this.UUID = UUID;
        this.finishedReason = finishedReason;
        this.formattedDate = formattedDate;
        this.errorMessage = errorMessage;
        this.status = status;
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
        return "Run ID: " + UUID + "\n"
                + (this.errorMessage.isEmpty() ? "Finished by: " + finishedReason : "Simulation stopped because of an error!") +
                "\nDate: " + formattedDate;
    }


    public void setFinishedReason(String finishedReason) {
        this.finishedReason = finishedReason;
    }
}
