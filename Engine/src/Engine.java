import engine.factory.WorldFactory;
import engine.jaxb.schema.generated.PRDWorld;
import engine.world.World;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Engine implements engine.Engine{

    @Override
    public void runSimulation() {

    }

    @Override
    public void loadSimulationParametersFromFile(String filename) {
        try {

            File file = new File(filename);
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PRDWorld jaxbWorld = (PRDWorld) jaxbUnmarshaller.unmarshal(file);
            World world = WorldFactory.createWorld(jaxbWorld);
            System.out.println(world);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void viewSimulationParameters() {

    }

    @Override
    public void viewOldSimulationRuns() {

    }

    @Override
    public void viewSingleSimulationRun(String runId) {

    }
}
