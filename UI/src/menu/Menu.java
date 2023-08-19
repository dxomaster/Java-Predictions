package menu;

import DTO.EntityDTO;
import DTO.PropertyDTO;
import DTO.RunEndDTO;
import DTO.RunStatisticsDTO;
import Exception.ERROR.ErrorException;
import Main.Main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    public static void printMenu() {
        System.out.println("Simulation Control Center:");
        System.out.println("1. Load simulation parameters from file");
        System.out.println("2. View simulation parameters");
        System.out.println("3. Run simulation");
        System.out.println("4. View old simulation runs");
        System.out.println("5. Save system state");
        System.out.println("6. Load system state");
        System.out.println("7. Exit\n");

        if (Main.getEngine().isSimulationLoaded())
            System.out.println("Current simulation loaded: " + Main.getEngine().getSimulationName());

        System.out.println("Please enter the number of your choice: ");
    }

    public static void chooseViewMode(RunEndDTO selectedRun) throws ErrorException {
        Scanner scanner = new Scanner(System.in);
        RunStatisticsDTO runStatisticsDTO = Main.getEngine().getPastSimulationArtifactDTO(selectedRun.getUUID());
        int input;
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("\nRun ID: " + selectedRun.getUUID());
            System.out.println("\n1.View Entity count");
            System.out.println("2.View Entity properties histogram");
            System.out.println("3.Go back");

            try {
                input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1:
                        Main.viewEntityCount(runStatisticsDTO);
                        break;
                    case 2:
                        viewEntityPropertiesHistogramMenu(runStatisticsDTO);
                        break;
                    case 3:
                        continueLoop = false;
                    default:
                        System.out.println("Invalid option. please choose from the following:");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void PrintOldSimulationMenu() throws ErrorException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        List<RunEndDTO> pastSimulationArtifactDTOMap = Main.getEngine().getPastArtifacts();
        int goBack = pastSimulationArtifactDTOMap.size() + 1;
        boolean continueLoop = true;
        if (pastSimulationArtifactDTOMap.isEmpty()) {
            throw new ErrorException("No past simulation runs");
        }

        while (continueLoop) {
            int counter = 1;
            System.out.println("Choose run to view: ");
            for (RunEndDTO entry : pastSimulationArtifactDTOMap) {
                System.out.println(counter + ". Run ID: " + entry.getUUID() + " Run Date: " + entry.getFormattedDate());
                counter++;
            }
            System.out.println(goBack + ". Go back");
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input == goBack) {
                    continueLoop = false;
                }
                else if (input > 0 && input <= pastSimulationArtifactDTOMap.size())
                 chooseViewMode(pastSimulationArtifactDTOMap.get(input - 1));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void viewPropertyHistogram(PropertyDTO propertyDTO) {
        System.out.println("Property name: " + propertyDTO.getName());
        System.out.println("Property type: " + propertyDTO.getType());
        System.out.println("Property Histogram: (value: frequency)");
        for (Map.Entry<String, Integer> entry : propertyDTO.getValueFrequency().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void viewEntityPropertiesMenu(EntityDTO entityDTO) {
        Scanner scanner = new Scanner(System.in);
        int counter, input;
        boolean continueLoop = true;
        while (continueLoop) {
            counter = 1;
            System.out.println("Entity properties: ");
            for (String property : entityDTO.getProperties()) {
                System.out.println(counter + ". " + property);
                counter++;
            }
            System.out.println(counter + ". Go back");
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= 1 && input < counter) {
                    viewPropertyHistogram(entityDTO.getPropertyDTOList().get(input - 1));
                }
                else if (input == counter)
                    continueLoop = false;
                else
                    System.out.println("Invalid option. please choose from the following:");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } ;
    }

    private static void viewEntityPropertiesHistogramMenu(RunStatisticsDTO runStatisticsDTO) {
        Scanner scanner = new Scanner(System.in);
        int input;
        int counter;
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("Choose Entity to view properties: ");
            counter = 1;
            for (EntityDTO entityDTO : runStatisticsDTO.getEntityDefinitionDTOList()) {
                System.out.println(counter + ". " + entityDTO.getName());
                counter++;
            }
            System.out.println(runStatisticsDTO.getEntityDefinitionDTOList().size() + 1 + ". Go back");
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= 1 && input <= runStatisticsDTO.getEntityDefinitionDTOList().size()) {
                    EntityDTO entityDTO = runStatisticsDTO.getEntityDefinitionDTOList().get(input - 1);
                    viewEntityPropertiesMenu(entityDTO);
                }
                else if (input == runStatisticsDTO.getEntityDefinitionDTOList().size() + 1)
                    continueLoop = false;
                else
                    System.out.println("Invalid option. please choose from the following:");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
