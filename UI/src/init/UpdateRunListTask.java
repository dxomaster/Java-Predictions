package init;

import DTO.RunEndDTO;
import engine.Engine;
import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpdateRunListTask extends javafx.concurrent.Task<Void> {
    Engine engine;
    ListView<String> listView;

    UpdateRunListTask(Engine engine, ListView<String> listView) {
        this.engine = engine;
        this.listView = listView;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            List<RunEndDTO> newRuns = engine.getPastArtifacts();
            sortRunsByDate(newRuns);

            Platform.runLater(() -> {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                if (selectedIndex == -1)
                    selectedIndex = 0;
                listView.getItems().clear();
                for (RunEndDTO run : newRuns) {
                    listView.getItems().add(run.toString());
                }
                listView.getSelectionModel().select(selectedIndex);
                listView.getFocusModel().focus(selectedIndex);

                if (listView.getItems().isEmpty()) {
                    listView.getItems().add("No runs to display");
                }
            });
            Thread.sleep(1000);
        }
    }

    private void sortRunsByDate(List<RunEndDTO> list) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        list.sort((o1, o2) -> {
            try {
                Date date1 = dateFormat.parse(o1.getFormattedDate());
                Date date2 = dateFormat.parse(o2.getFormattedDate());
                if (date1.before(date2))
                    return 1;
                else if (date1.after(date2))
                    return -1;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
