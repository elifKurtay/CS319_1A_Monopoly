package frontend;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuController {

    private Stage stage;
    private Audio obj = Audio.getInstance();

    /**
     * Initializes the controller
     */
    public void initialize(){
        obj.playGameMusic();
    }

    /**
     * Sets the stage for the controller
     * @param stage The stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Creates a new game
     * @param event Action event that triggers the action
     * @throws Exception
     */
    @FXML private void newGameButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewGameMenu.fxml"));
        Parent root = loader.load();
        NewGameMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setPreviousScene(stage.getScene());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());

        stage.setScene(s);
    }

    /**
     * Loads a saved game
     * @param event Action event that triggers the action
     * @throws Exception
     */
    @FXML private void loadGameButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadGameMenu.fxml"));
        Parent root = loader.load();
        LoadGameMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setPreviousScene(stage.getScene());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
    }

    /**
     * Shows the settings
     * @param event Action event that triggers the action
     * @throws Exception
     */
    @FXML private void settingsButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsMenu.fxml"));
        Parent root = loader.load();
        SettingsMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setPreviousScene(stage.getScene());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
    }

    /**
     * Shows the credits
     * @param event Action event that triggers the action
     * @throws Exception
     */
    @FXML private void creditsButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Credits.fxml"));
        Parent root = loader.load();
        CreditsController controller = loader.getController();
        controller.setStage(stage);
        controller.setPreviousScene(stage.getScene());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
    }

    /**
     * Quits the game
     * @param event Action event that triggers the action
     */
    @FXML private void quitButtonAction(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Shows the help page
     * @param event Action event that triggers the action
     */
    @FXML private void helpButtonAction(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File("CS319_1A_Final_Report_Version_2.pdf"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}