package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
public class FlueryAlgorithm {

    @FXML
    private ImageView graphImage = new ImageView();

    @FXML
    private Button showGraphBtn = new Button();

    @FXML
    private ProgressBar progressPar = new ProgressBar();

    @FXML
    private Label resultLbl = new Label();

    @FXML
    void showGraph(ActionEvent event) {
        try {
            graphImage.setImage(new Image(new File("graph.png").toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        resultLbl.setOpacity(1);
    }

    public void getGraph(Graph graph , String graphType)
    {
        if(graphType.equals("Directed"))
        {
            String result = graph.directedFleuryAlgorithm();
            resultLbl.setText(result);
            executeDrawingProgram();
            setProgressPar();
            showGraphBtn.setDisable(false);

        }
        else if(graphType.equals("Undirected"))
        {
            String result = graph.fleuryAlgorithm();
            resultLbl.setText(result);
            executeDrawingProgram();
            setProgressPar();
            showGraphBtn.setDisable(false);

        }

    }
    public void setProgressPar()
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(6), e-> {
                }, new KeyValue(progressPar.progressProperty(), 1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeline.stop();



                    }
                },
                6000
        );
    }
    public void executeDrawingProgram()
    {
        try {
            Runtime.getRuntime().exec("main.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
