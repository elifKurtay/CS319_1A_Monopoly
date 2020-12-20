package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public abstract class MenuController {
    @Getter
    @Setter
    private Stage stage;
    @Setter
    private Scene previousScene;

    /**
     * Goes back to the previous page
     * @param actionEvent Action event that triggers the action
     * @throws Exception
     */
    @FXML
    public void backButtonAction(ActionEvent actionEvent) throws Exception{
        double height = getStage().getHeight();
        double width = getStage().getWidth();
        stage.setScene(previousScene);
        stage.setHeight(height);
        stage.setWidth(width);
    }
}
