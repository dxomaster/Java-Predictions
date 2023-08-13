package menu;

import DTO.EnvDTO;
import DTO.SimulationArtifactDTO;
import Exception.ERROR.ErrorException;
import engine.EngineImp;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final EngineImp engine = new EngineImp();

    public static void main(String[] args) {
        while (true) {
            try {
                printMenu();
                getUserInput();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void printMenu() {
        System.out.println("1. Load simulation parameters from file");
        System.out.println("2. View simulation parameters");
        System.out.println("3. Run simulation");
        System.out.println("4. View old simulation runs");
        System.out.println("5. Exit");

    }

    public static void getUserInput() throws ErrorException {
        Scanner scanner = new Scanner(System.in);
        int input = Integer.parseInt(scanner.nextLine());
        switch (input) {
            case 1:
                System.out.println("Enter file path:");
                String filePath = scanner.nextLine();
                File file = new File(filePath);
                if (!file.exists())
                    throw new IllegalArgumentException("File does not exist");
                engine.loadSimulationParametersFromFile(filePath);
                System.out.println("File loaded successfully");
                break;
            case 2:
                engine.viewSimulationParameters();
                break;
            case 3:
                setEnvironmentVariables();
                SimulationArtifactDTO run = engine.runSimulation();
                System.out.println("Simulation finished successfully");
                System.out.println("Run ID: " + run.getUUID());
                System.out.println("Finished by: " + run.getFinishedReason());
                break;
            case 4:
                engine.viewOldSimulationRuns();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid input");

        }
    }

    public static void setEnvironmentVariables() {
        List<EnvDTO> requiredEnvDTO = engine.getRequiredEnvDTO();
        System.out.println("Required Environment Variables:");
        Scanner scanner = new Scanner(System.in);
        EnvDTO[] requiredEnvDTOArr = requiredEnvDTO.toArray(new EnvDTO[0]);
        for (int i = 0; i < requiredEnvDTOArr.length; i++) {
            EnvDTO envDTO = requiredEnvDTOArr[i];
            System.out.println("Please enter value for " + envDTO.getName() + " (" + envDTO.getType().getSimpleName() + "):");
            if (envDTO.getFrom() != null && envDTO.getTo() != null)
                System.out.println("Range: " + envDTO.getFrom() + " - " + envDTO.getTo());
            System.out.println("Default value: " + envDTO.getValue());
            System.out.println("Press enter to use default value, or enter new value below:");
            try {
                String input = scanner.nextLine();
                if (!input.isEmpty())
                    envDTO.setValue(input);
                engine.setEnvVariableWithDTO(envDTO);

            } catch (Exception e) {
                i--;//stay an iteration
                System.out.println("Invalid input:");
                System.out.println(e.getMessage());
            }

        }
    }
}