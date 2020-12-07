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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class GameScreenController {
    @FXML
    private VBox playerBoxes;

    @FXML
    private DynamicBoardController dynamicBoardController;

    private Stage stage;


    public void initialize() {

    }

    public void setStage(Stage stage){
        this.stage = stage;
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
    protected void settingsButtonAction(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsMenu.fxml"));
        Parent root = loader.load();
        SettingsMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setPreviousScene(stage.getScene());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
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
    public void playerAssetsButtonAction(ActionEvent actionEvent) {
    }

    public int[] rollDice(String name) {
        int[] dice = new int[2];

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(name + " is rolling");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Roll");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();

        dice[0] = (int) (Math.random() * 6 + 1);
        dice[1] = (int) (Math.random() * 6 + 1);

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setX(420);
        alert.setY(420);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        if (dice[0] == dice[1]) {
            alert.setHeaderText(name + " has rolled double: " + dice[0] + " " + dice[1]);
        }
        else {
            alert.setHeaderText(name + " has rolled: " + dice[0] + " " + dice[1]);
        }
        System.out.println(alert.getX() + " " + alert.getY());
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
        alert.showAndWait();
        return dice;
    }

    public int chooseToken(String name) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeaderText(name + " is choosing token");
        dialog.getItems().addAll( "thimble", "wheel barrow", "boot", "horse", "race car",
                "iron", "top Hat", "battleship");
        dialog.setX(420);
        dialog.setY(420);
        dialog.showAndWait();

        return dialog.getItems().indexOf(dialog.getResult()) + 1;
    }

    public void drawToken(int playerNo,int oldIndex, int newIndex) {
        dynamicBoardController.drawToken(playerNo, oldIndex, newIndex);
    }

    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
    }

    public boolean buyProperty(PropertySpace property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText(property.getName() + " is unowned");
        int value = property.getAssociatedProperty().getValue();
        alert.setContentText("Do you want to buy it for M" + value +"? Otherwise it will be auctioned");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        alert.setX(420);
        alert.setY(420);

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public void finishTurn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Finish Turn");
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
    }

    public boolean postponeCard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Do you want to open the card now?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Open Now");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Postpone");
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.CANCEL) {
            return true;
        }
        return false;
    }

    public void setTokenImage(int playerNo, String tokenName) {
        dynamicBoardController.setTokenImage(playerNo, tokenName);
    }

    public void drawPlayerBoxes(Player[] players) {
        playerBoxes.getChildren().clear();
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];

            Pane playerPane = new Pane();
            playerPane.getStyleClass().add("player-pane");
            HBox hb = new HBox(40);
            hb.setAlignment(Pos.CENTER);
            VBox vb = new VBox();
            vb.getStyleClass().add("vbox-style");

            String tokenName = player.getToken().getTokenName();
            tokenName.toLowerCase().replaceAll(" ","");
            Image token = new Image("img/token/normal/" + tokenName + ".png");
            ImageView iv = new ImageView(token);
            iv.setFitHeight(100);
            iv.setFitWidth(120);

            Label name = new Label(player.getPlayerName());
            name.getStyleClass().add("namelabel");
            vb.getChildren().addAll(iv, name);

            Label money = new Label("M" + Integer.toString(player.getMoney()));
            money.getStyleClass().add("moneylabel");
            money.setPrefHeight(70);
            money.setPrefWidth(150);

            Button assetsButton = new Button("Assets");
            assetsButton.getStyleClass().add("assetsButton");

            hb.getChildren().addAll(vb, money, assetsButton);
            playerPane.getChildren().add(hb);

            playerBoxes.getChildren().add(playerPane);

        }
    }
}
