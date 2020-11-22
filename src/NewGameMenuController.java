import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewGameMenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML protected void startButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
        Parent root = loader.load();

        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());

        stage.setScene(s);
    }

    @FXML protected void backButtonAction(ActionEvent event) {

    }

    @FXML protected void addPlayerButtonAction(ActionEvent event) {

    }
}
