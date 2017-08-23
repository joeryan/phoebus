package org.phoebus.applications.probe;

import java.io.IOException;

import org.phoebus.framework.spi.ToolbarEntry;
import org.phoebus.ui.docking.DockItem;
import org.phoebus.ui.docking.DockStage;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

@SuppressWarnings("rawtypes")
// @ProviderFor(ToolbarEntry.class)
public class LaunchProbe implements ToolbarEntry {

    private static final String NAME = "Probe";
    private TitledPane mainLayout;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object call() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("view/ProbeView.fxml"));
            mainLayout = loader.load();

            final DockItem tab = new DockItem("Probe", mainLayout);
            final Stage stage = new Stage();
            DockStage.configureStage(stage, tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
