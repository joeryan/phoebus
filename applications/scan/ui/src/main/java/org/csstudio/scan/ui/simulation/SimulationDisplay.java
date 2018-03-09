/*******************************************************************************
 * Copyright (c) 2018 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.scan.ui.simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.csstudio.scan.info.SimulationResult;
import org.phoebus.framework.jobs.JobManager;
import org.phoebus.framework.spi.AppDescriptor;
import org.phoebus.framework.spi.AppInstance;
import org.phoebus.ui.application.Messages;
import org.phoebus.ui.dialog.SaveAsDialog;
import org.phoebus.ui.docking.DockItem;
import org.phoebus.ui.docking.DockPane;
import org.phoebus.ui.javafx.ImageCache;

import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser.ExtensionFilter;

/** Display {@link SimulationResult}
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class SimulationDisplay implements AppInstance
{
    private static final ExtensionFilter[] file_extensions = new ExtensionFilter[]
    {
        new ExtensionFilter("All Files", "*.*"),
        new ExtensionFilter("Text", "*.txt")
    };

    /** Keep that last file used while JVM is running */
    private static File last_file = null;

    private final SimulationDisplayApplication app;

    // Log could be large.
    //
    // ListView<String> only displays N lines on the screen,
    // while TextArea would slow UI because it renders the complete text.
    //
    // ListView<String> remains responsive when displaying the 2e6 line simulation of
    // <commands>
    //   <loop>
    //     <device>xpos</device>
    //     <start>0.0</start>
    //     <end>1000000.0</end>
    //     <step>1.0</step>
    //     <body>
    //       <delay>
    //         <seconds>1.0</seconds>
    //       </delay>
    //     </body>
    //   </loop>
    // </commands>
    private final ListView<String> log = new ListView<>();

    public SimulationDisplay(final SimulationDisplayApplication app)
    {
        this.app = app;

        log.setPlaceholder(new Label("Getting scan simulation..."));
        final DockItem tab = new DockItem(this, log);
        DockPane.getActiveDockPane().addTab(tab);

        createContextMenu();
    }

    @Override
    public AppDescriptor getAppDescriptor()
    {
        return app;
    }

    @Override
    public boolean isTransient()
    {
        return true;
    }

    private void createContextMenu()
    {
        final MenuItem save = new MenuItem("Save as text file",
                                           ImageCache.getImageView(ImageCache.class, "/icons/save_edit.png"));
        save.setOnAction(event -> save());
        final ContextMenu menu = new ContextMenu(save);
        log.setContextMenu(menu);
    }

    /** Set simulation result
     *
     *  <p>Should be called off the UI thread
     *
     *  @param simulation SimulationResult to show
     */
    public void show(final SimulationResult simulation)
    {
        // Split log into lines
        final String[] lines = simulation.getSimulationLog().split("\n");

        // On UI thread, update the displayed log
        Platform.runLater(() ->  log.getItems().setAll(lines));
    }

    private void save()
    {
        // Prompt for file
        final File file = new SaveAsDialog().promptForFile(log.getScene().getWindow(),
                                                           Messages.Save, last_file, file_extensions);
        if (file == null)
            return;
        last_file = file;

        // Save in background thread
        JobManager.schedule("Save simulation", monitor ->
        {
            try
            (
                final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            )
            {
                for (String line : log.getItems())
                {
                    writer.write(line);
                    writer.write("\n");
                }
            }
        });
    }
}
