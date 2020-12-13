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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class GameScreenController {
    @FXML
    private VBox playerBoxes;
    @Getter
    @Setter
    private Game game;

    @FXML
    private DynamicBoardController dynamicBoardController;

    private Stage stage;

    private Audio obj = Audio.getInstance();


    public void initialize() {

    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setMap(Board board) {
        dynamicBoardController.setDynamicBoard(board);
    }

    public void resetMap(Board board) {

    }

    // UNUSED ???
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("NEW TRADE");
        alert.showAndWait();

        /*
        Bank bank = game.getBank();
        // Player targetPlayer = Playerları listele seçtir
        Trade t = bank.startTrade(targetPlayer);
        bank.finishTrade();
         */
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
        String buttonID = ((Button) actionEvent.getSource()).getId();
        int playerNo = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
        Player player = game.getPlayers()[playerNo];
        //System.out.println("Player " + playerNo + " GOOJC count: " + player.getGetOutOfJailFreeCount());
        Alert assetsDialog = new Alert(Alert.AlertType.INFORMATION);
        HBox hb = new HBox();
        assetsDialog.getDialogPane().setContent(hb);
        for (int i = 0; i < player.getProperties().size(); i++) {
            //hb.getChildren().add(new Label(player.getProperties().get(i).getPropertyName()));
            VBox vb = buildTitleDeedCard(player.getProperties().get(i));
            System.out.println("Style: " + vb.getStyle());
            hb.getChildren().add(vb);
            //hb.setContent(new Label(player.getProperties().get(i).getCard().getPropertyName()));
        }
        assetsDialog.showAndWait();
    }

    public VBox buildTitleDeedCard(Property p) {
        VBox vb = new VBox();
        vb.getChildren().add(new Label("Title Deed Card"));
        vb.getChildren().add(new Label(p.getPropertyName()));
        vb.setAlignment(Pos.CENTER);
        vb.getStyleClass().clear();
        vb.getStyleClass().add("titleCard");
        vb.getStylesheets().add("fxml/board.css");
        return vb;
    }

    public int[] rollDice(String name, boolean digital) {
        obj.playPoliceSound();
        int[] dice = new int[2];
        Alert alert;
        System.out.println("turn of " + name);

        if(!digital) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(name + " is rolling");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Roll");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setX(420);
            alert.setY(420);
            alert.showAndWait();
        }
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

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
        alert.showAndWait();
        return dice;
    }

    private ArrayList<String> tokens = new ArrayList<>();
    private final String[] tokenNames = {"Thimble", "Wheel Barrow", "Boot", "Horse", "Race Car",
            "Iron", "Top Hat", "Battleship"};
    private boolean first = true;

    public int chooseToken(String name, boolean digital) {
        if(first) {
            tokens.addAll(Arrays.asList(tokenNames));
            first = false;
        }
        String chosen;

        if(digital) {
            chosen = tokens.get(0);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setX(420);
            alert.setY(420);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(name + " has chosen: " + chosen);
            alert.showAndWait();
            try {
                Thread.sleep(1000);
                alert.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tokens.remove(chosen);
            return IntStream.range(0, tokenNames.length).filter(i -> tokenNames[i].equals(chosen))
                    .findFirst().orElse(-1) + 1;
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeaderText(name + " is choosing token");
        dialog.getItems().addAll(tokens);
        dialog.setX(420);
        dialog.setY(420);
        dialog.showAndWait();

        chosen = dialog.getResult();
        tokens.remove(chosen);
        return IntStream.range(0, tokenNames.length).filter(i -> tokenNames[i].equals(chosen))
                .findFirst().orElse(-1) + 1;
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
            tokenName = tokenName.toLowerCase().replaceAll(" ","");
            Image token = new Image("img/token/normal/" + tokenName + ".png");
            ImageView iv = new ImageView(token);
            iv.setFitHeight(100);
            iv.setFitWidth(120);

            Label name = new Label(player.getPlayerName());
            name.getStyleClass().add("namelabel");
            vb.getChildren().addAll(iv, name);

            Label money = new Label("M" + Integer.toString(player.getMoney()));
            money.getStyleClass().add("moneylabel");
            money.setPrefSize(150, 70);

            Button assetsButton = new Button("Assets");
            assetsButton.setId("playerAssets" + i);
            assetsButton.getStyleClass().add("assetsButton");
            assetsButton.setOnAction(this::playerAssetsButtonAction);

            hb.getChildren().addAll(vb, money, assetsButton);
            playerPane.getChildren().add(hb);

            playerBoxes.getChildren().add(playerPane);

        }
    }
}
