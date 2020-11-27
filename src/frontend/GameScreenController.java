package frontend;

import board.Board;
import board.Space;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.File;

public class GameScreenController {


    @FXML
    private DynamicBoardController dynamicBoardController;

    public void initialize() {
    }

    public void setDynamicBoard(Board board) {
        dynamicBoardController.setDynamicBoard(board);
    }

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
