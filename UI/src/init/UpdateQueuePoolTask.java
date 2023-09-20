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
        int idleThreads = engine.getAmountOfIdleThreads();
        int runningThreads = engine.getAmountOfRunningThreads();
        while(true)
        {
            idleThreads = engine.getAmountOfIdleThreads();
            runningThreads = engine.getAmountOfRunningThreads();
            String queuePool = "Idle Threads: " + idleThreads + "\nRunning Threads: " + runningThreads +
                    "\nThread Pool Size: " + engine.getThreadPoolSize();
            Platform.runLater(() -> queuePoolLabel.setText(queuePool));
            Thread.sleep(200);
        }
    }

}
