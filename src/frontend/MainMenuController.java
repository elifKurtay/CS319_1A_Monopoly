package frontend;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private void newGameButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewGameMenu.fxml"));
        Parent root = loader.load();
        NewGameMenuController controller = loader.getController();
        controller.setStage(stage);

        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());

        stage.setScene(s);
    }


    @FXML private void loadGameButtonAction(ActionEvent event) throws Exception {

    }

    @FXML private void settingsButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsMenu.fxml"));
        Parent root = loader.load();

        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());

        stage.setScene(s);
    }

    @FXML private void creditsButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Credits.fxml"));
        Parent root = loader.load();
        CreditsController controller = loader.getController();
        controller.setStage(stage);
        System.out.println(root);
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        System.out.println("dadsa");
        stage.setScene(s);
    }

    @FXML private void quitButtonAction(ActionEvent event) {
        Platform.exit();
    }

}