package Main;

import DTO.*;
import Exception.ERROR.ErrorException;
import Exception.WARN.WarnException;
import engine.Engine;
import menu.Menu;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Engine engine = new Engine();

    public static Engine getEngine() {
        return engine;
    }

    public static void main(String[] args) {
        while (true) {
            try {

                Menu.printMenu();
                getUserInput();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    public static void viewEntityCount(RunStatisticsDTO runStatisticsDTO) {
        for (EntityDTO entityDTO : runStatisticsDTO.getEntityDefinitionDTOList()) {

            int population = entityDTO.getPopulation();
            int finalPopulation = entityDTO.getFinalPopulation();
            System.out.println("Entity name: " + entityDTO.getName());
            System.out.println("Initial population: " + population);
            System.out.println("Final population: " + finalPopulation + "\n");
        }
    }

    public static void getUserInput() throws ErrorException, WarnException {
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
                System.out.println("\nFile loaded successfully\n");
                break;
            case 2:
                WorldPrintDTO worldToPrint = engine.getSimulationParameters();
                System.out.println(worldToPrint);
                break;
            case 3:
                setEnvironmentVariables();
                System.out.println("Starting simulation...");
                RunEndDTO run = engine.runSimulation();
                System.out.println("Simulation finished successfully");
                System.out.println("Run ID: " + run.getUUID());
                System.out.println("Finished by: " + run.getFinishedReason());
                break;
            case 4:
                Menu.PrintOldSimulationMenu();
                break;
            case 5:
                System.out.println("Enter filename to save:");
                String filename = scanner.nextLine();
                engine.saveEngineToFile(filename);
                System.out.println("Engine saved successfully to " + filename + ".save\n");
                break;
            case 6:
                System.out.println("Enter filename to load: (including the .save extension)");
                String filenameToLoad = scanner.nextLine();
                engine.loadEngineFromFile(filenameToLoad);
                System.out.println("Engine loaded \n");
                break;
            case 7:
                System.out.println("Bye Bye");
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException("Invalid input");

        }

    }

    public static void displayEnvironmentVariables(List<EnvDTO> envDTOList) {
        System.out.println("\nChoose an environment variable to modify:");
        for (int i = 0; i < envDTOList.size(); i++) {
            System.out.println((i + 1) + ". " + envDTOList.get(i).getName() + " = " + envDTOList.get(i).getValue());
        }
        System.out.println(envDTOList.size() + 1 + ". Run simulation");
    }

    private static List<EnvDTO> modifyEnvironmentVariable(List<EnvDTO> envDTOList, int chosenIndex) {
        EnvDTO chosenDTO = envDTOList.get(chosenIndex - 1); // Adjust for 0-based index
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println(chosenDTO.toString());
            System.out.println("Enter a new value: (Press Enter to go back)");
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                chosenDTO.setValue(input);
                engine.setEnvVariableWithDTO(chosenDTO);
            }
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            envDTOList = engine.getRequiredEnvDTO();
        }
        return envDTOList;
    }

    public static void setEnvironmentVariables() {

        List<EnvDTO> requiredEnvDTO = engine.getRequiredEnvDTO();
        Scanner scanner = new Scanner(System.in);
        int chosenIndex;
        boolean continueLoop = true;
        while (continueLoop) {
            displayEnvironmentVariables(requiredEnvDTO);
            try {
                chosenIndex = Integer.parseInt(scanner.nextLine());
            }
            catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                continue;
            }
            if (chosenIndex == requiredEnvDTO.size()+1) {
                continueLoop = false;
            } else if (chosenIndex >= 1 && chosenIndex <= requiredEnvDTO.size()) {
                // Valid index chosen
                requiredEnvDTO = modifyEnvironmentVariable(requiredEnvDTO, chosenIndex);
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}

//    public static void setEnvironmentVariables() {
//        List<EnvDTO> requiredEnvDTO = engine.getRequiredEnvDTO();
//        System.out.println("Required Environment Variables:");
//        Scanner scanner = new Scanner(System.in);
//        EnvDTO[] requiredEnvDTOArr = requiredEnvDTO.toArray(new EnvDTO[0]);
//        for (int i = 0; i < requiredEnvDTOArr.length; i++) {
//            EnvDTO envDTO = requiredEnvDTOArr[i];
//            System.out.println( i+1 + ". " + envDTO.getName() + " (" + envDTO.getType().getSimpleName() + "):");
//            if (envDTO.getFrom() != null && envDTO.getTo() != null)
//                System.out.println("Range: " + envDTO.getFrom() + " - " + envDTO.getTo());
//            System.out.println("Default value: " + envDTO.getValue());
//            System.out.println("Press enter to use default value, or enter new value below:");
//            try {
//                String input = scanner.nextLine();
//                if (!input.isEmpty())
//                    envDTO.setValue(input);
//                engine.setEnvVariableWithDTO(envDTO);
//
//            } catch (Exception e) {
//                i--;//stay an iteration
//                System.out.println("Invalid input:");
//                System.out.println(e.getMessage());
//                requiredEnvDTO = engine.getRequiredEnvDTO();
//                requiredEnvDTOArr = requiredEnvDTO.toArray(new EnvDTO[0]);
//            }
//
//        }
//
//        System.out.println("Environment Variables Values:");
//        for (EnvDTO envDTO : requiredEnvDTOArr) {
//            System.out.println(envDTO.getName() + ": " + envDTO.getValue());
//        }
//    }
//}