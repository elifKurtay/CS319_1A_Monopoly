package frontend;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class GameScreenController {

    public Label money1;
    public Label name1;
    public Image piece1;
    public Image piece2;
    public Label name2;
    public Label money2;
    public Image piece3;
    public Label name3;
    public Label money3;
    public Label money4;
    public Label name4;
    public Image piece4;




    @FXML
    protected void backButtonAction(ActionEvent event) {

    }

    @FXML
    protected void exitButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void settingsButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void restartButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void saveButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void tradeButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void buildButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void mortgageButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    protected void redeemButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void player1ButtonAction(ActionEvent actionEvent) {
    }
}
