package init;

import engine.Engine;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class UpdateQueuePoolTask extends javafx.concurrent.Task<Void>{

    Label queuePoolLabel;
    Engine engine;

    public UpdateQueuePoolTask(Label queuePoolLabel, Engine engine){
        this.queuePoolLabel = queuePoolLabel;
        this.engine = engine;
    }

    @Override
    protected Void call() throws Exception {

        while(true)
        {
            int finishedThreads = engine.getAmountOfFinishedThreads();
            int idleThreads = engine.getAmountOfIdleThreads();
            int runningThreads = engine.getAmountOfRunningThreads();
            String queuePool = "Idle Simulations: " + idleThreads + "\nRunning Simulations: " + runningThreads +
                    "\nFinished Simulations: " + finishedThreads + "\nThread Pool Size: " + engine.getThreadPoolSize();

            Platform.runLater(() -> queuePoolLabel.setText(queuePool));
            Thread.sleep(200);
        }
    }

}
