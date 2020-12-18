package frontend;

import bank.Auction;
import bank.Trade;
import board.Board;
import board.PropertySpace;
import board.Space;
import entities.LandProperty;
import entities.Player;
import entities.Property;
import game.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

import java.util.*;
import java.util.stream.IntStream;
import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private final static boolean DEBUG = false;

    public void initialize() {

    }

    public void setStage(Stage stage) {
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
    protected void settingsButtonAction(ActionEvent event) throws Exception {
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
    protected void saveButtonAction(ActionEvent event) throws Exception {
        game.saveGame();
    }

    @FXML
    protected void tradeButtonAction(ActionEvent event) {
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(game.getPlayers()));
        players.remove(game.getCurrentPlayer());
        ChoiceDialog<Player> dialog = new ChoiceDialog<>(players.get(0), players);
        dialog.setHeaderText("Choose a player to trade with");
        dialog.showAndWait();
        Player playerToTrade = dialog.getResult();
        System.out.println(playerToTrade);

        Trade trade = new Trade(game.getCurrentPlayer(), playerToTrade);
        ArrayList<Property> wantedProperties = new ArrayList<>();
        ArrayList<Property> offeredProperties = new ArrayList<>();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Send Offer");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel Trade");

        // http://www.java2s.com/Tutorials/Java/JavaFX/0640__JavaFX_ListView.htm GOT MOST OF THE CODE FROM HERE
        GridPane gp = new GridPane();
        alert.getDialogPane().setContent(gp);
        ObservableList<Property> currentPlayerProperties = FXCollections.observableArrayList(game.getCurrentPlayer().getProperties());
        ListView<Property> currentPlayerLW = new ListView<Property>(currentPlayerProperties);
        gp.add(currentPlayerLW, 0, 1);

        ObservableList<Property> otherPlayerProperties = FXCollections.observableArrayList(playerToTrade.getProperties());
        ListView<Property> otherPlayerLW = new ListView<Property>(otherPlayerProperties);
        gp.add(otherPlayerLW, 2, 1);

        Button sendRightButton = new Button(" Give ");
        sendRightButton.setOnAction((ActionEvent e) -> {
            Property property = currentPlayerLW.getSelectionModel()
                    .getSelectedItem();
            if (property != null) {
                currentPlayerLW.getSelectionModel().clearSelection();
                currentPlayerProperties.remove(property);
                // ALSO ADD THEM TO TRADE HERE
                otherPlayerProperties.add(property);

                wantedProperties.remove(property);
                offeredProperties.add(property);
            }
        });

        Button sendLeftButton = new Button(" Request ");
        sendLeftButton.setOnAction((ActionEvent e) -> {
            Property property = otherPlayerLW.getSelectionModel().getSelectedItem();
            if (property != null) {
                otherPlayerLW.getSelectionModel().clearSelection();
                otherPlayerProperties.remove(property);
                currentPlayerProperties.add(property);

                wantedProperties.add(property);
                offeredProperties.remove(property);
            }
        });
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        gp.add(vbox, 1, 1);

        alert.showAndWait();

        trade.offer(offeredProperties, 0, 0);
        trade.want(wantedProperties, 0, 0);

        String message = playerToTrade + ", do you accept the offer?\n You Get: \n";
        for (Property p : offeredProperties) {
            message += p + "\n";
        }
        message += "offeredMoney\n";
        message += "offeredGOOJC\n\n";

        message += "You Give:\n";
        for (Property p : wantedProperties) {
            message += p + "\n";
        }
        message += "wantedMoney\n";
        message += "wantedGOOJC";

        trade.acceptOffer(twoChoiceDialog(message, "Accept", "Deny"));
        trade.closeTrade();

        /*
        Bank bank = game.getBank();
        // Player targetPlayer = Playerları listele seçtir
        Trade t = bank.startTrade(targetPlayer);
        bank.finishTrade();
         */
    }

    @FXML
    protected void buildButtonAction(ActionEvent event) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer.getCurrentSpace() instanceof PropertySpace
                && ((PropertySpace) currentPlayer.getCurrentSpace()).getAssociatedProperty() instanceof LandProperty) {
            LandProperty p = (LandProperty) ((PropertySpace) currentPlayer.getCurrentSpace()).getAssociatedProperty();
            if (p.getOwner() == currentPlayer) {
                if (currentPlayer.ownsAllPropertiesFromSameGroup(p)) {
                    // Show build menu
                    ChoiceDialog<Property> dialog = new ChoiceDialog<>();
                    dialog.getItems().addAll(currentPlayer.getAllPropertiesFromSameGroup(p));
                    dialog.setHeaderText("Choose a property to build on");
                    dialog.showAndWait();
                    LandProperty propertyToBuild = (LandProperty) dialog.getResult();
                    if (propertyToBuild.getNumOfHouses() == 5) {
                        showMessage("Cannot build more buildings on this property!");
                    } else if (!propertyToBuild.canBuild()) {
                        showMessage("You must build evenly between properties," +
                                "build on other properties in the same group before building here!");
                    } else {
                        String message;
                        double cost = 1 * currentPlayer.getToken().getBuildingCostMultiplier();
                        if (propertyToBuild.getNumOfHouses() == 4) {
                            message = "Do you want to build a hotel for " + propertyToBuild.getHotelCost() + " on " + propertyToBuild.getPropertyName() + "?";
                            cost *= propertyToBuild.getHotelCost();
                        } else {
                            message = "Do you want to build a house for " + propertyToBuild.getHotelCost() + " on " + propertyToBuild.getPropertyName() + "?";
                            cost *= propertyToBuild.getHouseCost();
                        }

                        if (twoChoiceDialog(message, "Yes", "No")) {
                            propertyToBuild.buildHouse();
                            currentPlayer.setMoney(currentPlayer.getMoney() - (int) cost);
                            drawPlayerBoxes(game.getPlayers());
                            dynamicBoardController.drawHouse(propertyToBuild.getAssociatedPropertySpace().getIndex(), propertyToBuild.getNumOfHouses());
                        }
                    }
                } else {
                    showMessage("Cannot build, you don't own all properties from this group!");
                }
            } else {
                showMessage("Cannot build, you don't own this property");
            }
        } else {
            showMessage("Cannot build here, isn't a land property");
        }
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
        if (DEBUG && !digital) {
            Scanner scan = new Scanner(System.in);
            //int dice1 = Integer.parseInt(scan.nextLine());
            //int dice2 = Integer.parseInt(scan.nextLine());
            int dice1 = scan.nextInt();
            int dice2 = scan.nextInt();
            return new int[]{dice1, dice2};
        } else {
            obj.playPoliceSound();
            int[] dice = new int[2];
            Alert alert;
            System.out.println("turn of " + name);

            if (!digital) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(name + " is rolling");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Roll");
                alert.initStyle(StageStyle.UNDECORATED);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initOwner(stage);
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
            alert.initOwner(stage);
            if (dice[0] == dice[1]) {
                alert.setHeaderText(name + " has rolled double: " + dice[0] + " " + dice[1]);
            } else {
                alert.setHeaderText(name + " has rolled: " + dice[0] + " " + dice[1]);
            }

            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
            alert.showAndWait();
            return dice;
        }
    }

    private ArrayList<String> tokens = new ArrayList<>();
    private final String[] tokenNames = {"Thimble", "Wheel Barrow", "Boot", "Horse", "Race Car",
            "Iron", "Top Hat", "Battleship"};
    private boolean first = true;

    public int chooseToken(String name, boolean digital) {
        if (first) {
            tokens.addAll(Arrays.asList(tokenNames));
            first = false;
        }
        String chosen;

        if (digital) {
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
        ChoiceDialog<String> dialog = new ChoiceDialog<>(tokens.get(0), tokens);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        //((Stage) dialog.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        dialog.setHeaderText(name + " is choosing token");
        dialog.setX(420);
        dialog.setY(420);
        dialog.showAndWait();

        chosen = dialog.getResult();
        tokens.remove(chosen);
        return IntStream.range(0, tokenNames.length).filter(i -> tokenNames[i].equals(chosen))
                .findFirst().orElse(-1) + 1;
    }

    public void drawToken(int playerNo, int oldIndex, int newIndex) {
        dynamicBoardController.drawToken(playerNo, oldIndex, newIndex);
    }

    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
    }

    public boolean buyProperty(PropertySpace property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setHeaderText(property.getName() + " is unowned");
        int value = property.getAssociatedProperty().getValue();
        alert.setContentText("Do you want to buy it for M" + value + "? Otherwise it will be auctioned");
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

    private boolean twoChoiceDialog(String message, String ok, String cancel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setContentText(message);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(ok);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(cancel);

        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    public void finishTurn(Boolean digital) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.NONE);
        alert.initOwner(stage);
        //((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        alert.setHeaderText("Finish Turn");
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
    }

    public boolean postponeCard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
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
            tokenName = tokenName.toLowerCase().replaceAll(" ", "");
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

    //TODO -Take players out of the game
    //TODO-Give error when non-integer bid
    public void startAuction() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setHeaderText("Auction");

        GridPane gp = new GridPane();
        alert.getDialogPane().setContent(gp);

        Player[] players = game.getPlayers();
        Auction auc = (Auction) (game.getObservable());
        System.out.println("Auction for: " + auc.getAuctionedProperty().getPropertyName());

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        VBox vb = buildTitleDeedCard(auc.getAuctionedProperty());
        hb.getChildren().add(vb);

        gp.add(hb, 0, 0);

        Button[] bids = new Button[4];
        Button[] folds = new Button[4];
        for (int i = 0; i < 4; i++) {
            bids[i] = new Button(" Bid ");
            folds[i] = new Button(" Fold ");
        }
        //For player 0
        Label label0 = new Label(players[0].getPlayerName());
        TextField tf0 = new TextField();


        bids[0].setOnAction((ActionEvent e) -> {
            int bid = Integer.parseInt(tf0.getText());
            auc.bid(players[0], bid, 0);
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[1].setDisable(false);
            folds[1].setDisable(false);
        });

        folds[0].setOnAction((ActionEvent e) -> {
            auc.fold(players[0], 0);
            if (auc.getState() == 0) {
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                alert.close();
                System.out.println("Auction closed");
            }
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[1].setDisable(false);
            folds[1].setDisable(false);
        });

        HBox hBox0 = new HBox(4);
        hBox0.getChildren().addAll(label0, tf0, bids[0], folds[0]);
        gp.add(hBox0, 0, 1);

        //For player 1
        Label label1 = new Label(players[1].getPlayerName());
        TextField tf1 = new TextField();

        bids[1].setOnAction((ActionEvent e) -> {
            int bid = Integer.parseInt(tf1.getText());
            auc.bid(players[1], bid, 1);
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[2].setDisable(false);
            folds[2].setDisable(false);
        });

        folds[1].setOnAction((ActionEvent e) -> {
            auc.fold(players[1], 1);
            if (auc.getState() == 0) {
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                alert.close();
                System.out.println("Auction closed");
            }
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[2].setDisable(false);
            folds[2].setDisable(false);
        });

        HBox hBox1 = new HBox(4);
        hBox1.getChildren().addAll(label1, tf1, bids[1], folds[1]);
        gp.add(hBox1, 0, 2);

        //For player 2
        Label label2 = new Label(players[2].getPlayerName());
        TextField tf2 = new TextField();

        bids[2].setOnAction((ActionEvent e) -> {
            int bid = Integer.parseInt(tf2.getText());
            auc.bid(players[2], bid, 2);
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[3].setDisable(false);
            folds[3].setDisable(false);
        });

        folds[2].setOnAction((ActionEvent e) -> {
            auc.fold(players[2], 2);
            if (auc.getState() == 0) {
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                alert.close();
                System.out.println("Auction closed");
            }
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[3].setDisable(false);
            folds[3].setDisable(false);
        });

        HBox hBox2 = new HBox(4);
        hBox2.getChildren().addAll(label2, tf2, bids[2], folds[2]);
        gp.add(hBox2, 0, 3);

        //For player 3
        Label label3 = new Label(players[3].getPlayerName());
        TextField tf3 = new TextField();

        bids[3].setOnAction((ActionEvent e) -> {
            int bid = Integer.parseInt(tf3.getText());
            auc.bid(players[3], bid, 3);
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[0].setDisable(false);
            folds[0].setDisable(false);
        });

        folds[3].setOnAction((ActionEvent e) -> {
            auc.fold(players[3], 3);
            if (auc.getState() == 0) {
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                alert.close();
                System.out.println("Auction closed");
            }
            for (int i = 0; i < 4; i++) {
                bids[i].setDisable(true);
                folds[i].setDisable(true);
            }
            bids[0].setDisable(false);
            folds[0].setDisable(false);
        });

        HBox hBox3 = new HBox(4);
        hBox3.getChildren().addAll(label3, tf3, bids[3], folds[3]);
        gp.add(hBox3, 0, 4);

        for (int i = 1; i < 4; i++) {
            bids[i].setDisable(true);
            folds[i].setDisable(true);
        }

        alert.setX(300);
        alert.setY(300);
        gp.setHgap(30);
        gp.setVgap(30);
        alert.showAndWait();
    }
}
