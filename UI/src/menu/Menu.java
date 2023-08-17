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
        System.out.println("1. Load simulation parameters from file");
        System.out.println("2. View simulation parameters");
        System.out.println("3. Run simulation");
        System.out.println("4. View old simulation runs");
        System.out.println("5. Save system state");
        System.out.println("6. Load system state");
        System.out.println("7. Exit");

        if (Main.getEngine().isSimulationLoaded())
            System.out.println("\nCurrent simulation loaded: " + Main.getEngine().getSimulationName());

    }

    public static void PrintOldSimulationMenu() throws ErrorException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        List<RunEndDTO> pastSimulationArtifactDTOMap = Main.getEngine().getPastArtifacts();
        int exit = pastSimulationArtifactDTOMap.size() + 1;
        if (pastSimulationArtifactDTOMap.isEmpty()) {
            throw new ErrorException("No past simulation runs");
        }

        do {
            int counter = 1;
            System.out.println("Choose run to view: ");
            for (RunEndDTO entry : pastSimulationArtifactDTOMap) {
                System.out.println(counter + ". Run ID: " + entry.getUUID() + " Run Date: " + entry.getFormattedDate());
                System.out.println();
                counter++;
            }
            System.out.println(exit + ". Go back");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }
            if (input == exit)
                return;
        }
        while ((input < 1 || input > exit));

        chooseViewMode(pastSimulationArtifactDTOMap.get(input - 1));

    }

    public static void chooseViewMode(RunEndDTO selectedRun) throws ErrorException {
        Scanner scanner = new Scanner(System.in);
        RunStatisticsDTO runStatisticsDTO = Main.getEngine().getPastSimulationArtifactDTO(selectedRun.getUUID());
        int input = -1;
        do {
            System.out.println("\n1.View Entity count");
            System.out.println("2.View Entity properties histogram\n");
            System.out.println("3.Go back");
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        while (input < 1 || input > 3);
        switch (input) {
            case 1:
                Main.viewEntityCount(runStatisticsDTO);
                break;
            case 2:
                viewEntityPropertiesHistogramMenu(runStatisticsDTO);
                break;
            case 3:
        }
    }

    private static void viewEntityPropertiesHistogramMenu(RunStatisticsDTO runStatisticsDTO) {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        int counter;
        do {
            System.out.println("\nChoose Entity to view properties: ");
            counter = 1;
            for (EntityDTO entityDTO : runStatisticsDTO.getEntityDefinitionDTOList()) {
                System.out.println(counter + ". " + entityDTO.getName());


                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());

                }
            }
        }
        while (input < 1 || input > counter + 1);
        EntityDTO entityDTO = runStatisticsDTO.getEntityDefinitionDTOList().get(input - 1);
        do {
            counter = 1;
            System.out.println("Entity properties: ");
            for (String property : entityDTO.getProperties()) {
                System.out.println(counter + ". " + property);
                counter++;
            }
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());

            }

        } while (input < 1 || input > counter + 1);
        viewPropertyHistogram(entityDTO.getPropertyDTOList().get(input - 1));
    }

    public static void viewPropertyHistogram(PropertyDTO propertyDTO) {
        System.out.println("Property name: " + propertyDTO.getName());
        System.out.println("Property type: " + propertyDTO.getType());
        System.out.println("Property Histogram: (value: frequency)");
        for (Map.Entry<String, Integer> entry : propertyDTO.getValueFrequency().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}
