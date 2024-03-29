package edu;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.Commands.Executor;
import edu.touro.mco152.bm.Commands.ReadCmd;
import edu.touro.mco152.bm.Commands.WriteCmd;
import edu.touro.mco152.bm.DiskWorker;
import edu.touro.mco152.bm.NonSwingUI;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is used to test the commands and the Executor directly without using App or DiskWorker directly
 */
public class CommandAndExecutorTest {
    @BeforeAll
    public static void setupDefaultAsPerProperties() {
        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath() + File.separator + App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        } else {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }
    NonSwingUI currentUI = new NonSwingUI();
    Executor executor = new Executor();

    @Test
    void readCmdTest(){
        boolean result = executor.executeCommand(new ReadCmd(currentUI,25,128,2048, DiskRun.BlockSequence.SEQUENTIAL));
        ArrayList<Integer> intermediateResults = currentUI.getProgresses();
        //Act
        for (Integer num : intermediateResults) {
            //Assert
            assertTrue(num >= 0 && num <= 100);
        }
        assertTrue(result);
    }

    @Test
    void writeCmdTest(){
        boolean result = executor.executeCommand(new WriteCmd(currentUI,25,128,2048, DiskRun.BlockSequence.SEQUENTIAL));
        ArrayList<Integer> intermediateResults = currentUI.getProgresses();
        //Act
        for (Integer num : intermediateResults) {
            //Assert
            assertTrue(num >= 0 && num <= 100);
        }
        assertTrue(result);
    }
}
