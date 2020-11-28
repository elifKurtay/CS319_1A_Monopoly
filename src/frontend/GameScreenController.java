package frontend;

import board.Board;
import board.PropertySpace;
import board.Space;
import entities.Player;
import entities.Property;
import game.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Optional;

public class GameScreenController {


    @FXML
    private DynamicBoardController dynamicBoardController;

    public void initialize() {

    }

    public void setMap(Board board) {
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

    public int[] rollDice(String name) {
        int[] dice = new int[2];

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(name + " is rolling");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Roll");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();

        dice[0] = (int) (Math.random() * 6 + 1);
        dice[1] = (int) (Math.random() * 6 + 1);

        if (dice[0] == dice[1]) {
            alert.setHeaderText(name + " has rolled double: " + dice[0] + " " + dice[1]);
        }
        else {
            alert.setHeaderText(name + " has rolled: " + dice[0] + " " + dice[1]);
        }
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
        alert.showAndWait();
        return dice;
    }

    public int chooseToken(String name) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.setHeaderText(name + " is choosing token");
        dialog.getItems().addAll("Battleship", "Boot", "Horse", "Iron",
                "Race Car", "Thimble", "Top Hat", "Wheel Barrow");
        dialog.showAndWait();

        return dialog.getItems().indexOf(dialog.getResult()) + 1;
    }

    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public boolean buyProperty(PropertySpace property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(property.getName() + " is unowned");
        int value = property.getAssociatedProperty().getValue();
        alert.setContentText("Do you want to buy it for M" + value +"? Otherwise it will be auctioned");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public void finishTurn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Finish Turn");
        alert.showAndWait();
    }
}
