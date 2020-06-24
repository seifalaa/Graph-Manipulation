package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.io.File;

public class ExitListener implements EventHandler<WindowEvent> {
    @Override
    public void handle(WindowEvent event) {
        File folder = new File(".");
        for(File file : folder.listFiles()){
            if(file.getName().contains(".png") && !file.getName().equals("graph.png")){
                file.delete();
            }
        }
        Platform.exit();
        System.exit(0);
    }
}
