package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.BoardPane;

public class NewGameMenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML protected void startButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));

        Parent root = loader.load();
        Pane p1 = (Pane) root.lookup("#myPane");
        p1.getChildren().add(new BoardPane());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
    }

    @FXML protected void backButtonAction(ActionEvent event) {

    }

    @FXML protected void addPlayerButtonAction(ActionEvent event) {

    }
}
