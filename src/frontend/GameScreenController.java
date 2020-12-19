package frontend;

import bank.Auction;
import bank.Trade;
import board.Board;
import board.PropertySpace;
import board.Space;
import card.Card;
import entities.DigitalPlayer;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class GameScreenController {
    @FXML
    private VBox playerBoxes;
    @Getter
    @Setter
    private Game game;

    @FXML
    private DynamicBoardController dynamicBoardController;


    @FXML
    private Label turn_count;

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

    @FXML
    protected void exitButtonAction(ActionEvent event) throws Exception{
        // Scoreboard ?
        if (twoChoiceDialog("Do you really want to exit?", "Yes", "No")) {
            if (twoChoiceDialog("Do you want to save the game?", "Yes", "No")) {
                saveButtonAction(null);
            }
            Platform.exit();
        }
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
        Player[] players = game.getPlayers();
        game.restartGame();
        drawPlayerBoxes(players);
        for (int i = 0; i <players.length; i++) {
            dynamicBoardController.drawToken(i, players[i].getCurrentSpace().getIndex(), -1);
        }
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

        boolean sentOffer = alert.getResult() == ButtonType.OK;
        if (sentOffer) {
            trade.offer(offeredProperties, 0 ,0);
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
        }


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
                        showMessage("Cannot build more buildings on this property!", null);
                    }
                    else if (!propertyToBuild.canBuild()) {
                        showMessage("You must build evenly between properties," +
                                "build on other properties in the same group before building here!", null);
                    }
                    else {
                        String message;
                        double cost = 1 * currentPlayer.getToken().getBuildingCostMultiplier();
                        if (propertyToBuild.getNumOfHouses() == 4) {
                            message = "Do you want to build a hotel for " + propertyToBuild.getHotelCost() + " on " + propertyToBuild.getPropertyName() + "?";
                            cost *= propertyToBuild.getHotelCost();
                        }
                        else {
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
                }
                else {
                    showMessage("Cannot build, you don't own all properties from this group!", null);
                }
            }
            else {
                showMessage("Cannot build, you don't own this property", null);
            }
        }
        else {
            showMessage("Cannot build here, isn't a land property", null);
        }
    }

    @FXML
    protected void mortgageButtonAction(ActionEvent event) {
        Player currentPlayer = game.getCurrentPlayer();
        Alert assetsDialog = new Alert(Alert.AlertType.INFORMATION);
        HBox hb = new HBox();
        assetsDialog.getDialogPane().setContent(hb);
        for (Property p :currentPlayer.getProperties()) {
            if (!p.isMortgaged()) {
                VBox vb = new VBox();
                Button mortgageButton = new Button("Mortgage");
                mortgageButton.setOnAction((ActionEvent e) -> {
                    p.mortgage();
                    mortgageButton.setDisable(true);
                    drawPlayerBoxes(game.getPlayers());
                });
                vb.getChildren().addAll(buildTitleDeedCard(p), mortgageButton);
                hb.getChildren().add(vb);
            }
        }
        assetsDialog.showAndWait();
    }

    @FXML
    protected void redeemButtonAction(ActionEvent event) {
        Player currentPlayer = game.getCurrentPlayer();
        Alert assetsDialog = new Alert(Alert.AlertType.INFORMATION);
        HBox hb = new HBox();
        assetsDialog.getDialogPane().setContent(hb);
        for (Property p :currentPlayer.getProperties()) {
            if (p.isMortgaged()) {
                VBox vb = new VBox();
                Button mortgageButton = new Button("Redeem");
                mortgageButton.setOnAction((ActionEvent e) -> {
                    p.liftMortgage();
                    mortgageButton.setDisable(true);
                    drawPlayerBoxes(game.getPlayers());
                });
                vb.getChildren().addAll(buildTitleDeedCard(p), mortgageButton);
                hb.getChildren().add(vb);
            }
        }
        assetsDialog.showAndWait();
    }

    @FXML
    public void playerAssetsButtonAction(ActionEvent actionEvent) {
        String buttonID = ((Button) actionEvent.getSource()).getId();
        int playerNo = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
        Player player = game.getPlayers()[playerNo];
        //System.out.println("Player " + playerNo + " GOOJC count: " + player.getGetOutOfJailFreeCount());
        Alert assetsDialog = new Alert(Alert.AlertType.INFORMATION);
        VBox vbTop = new VBox();
        HBox hb = new HBox();
        vbTop.getChildren().add(hb);
        assetsDialog.getDialogPane().setContent(vbTop);
        for (int i = 0; i < player.getProperties().size(); i++) {
            //hb.getChildren().add(new Label(player.getProperties().get(i).getPropertyName()));
            VBox vb = buildTitleDeedCard(player.getProperties().get(i));
            System.out.println("Style: " + vb.getStyle());
            hb.getChildren().add(vb);
            //hb.setContent(new Label(player.getProperties().get(i).getCard().getPropertyName()));
        }
        HBox hbCard = new HBox();
        vbTop.getChildren().add(hbCard);
        for (Card c : player.getPostponedCards()) {
            VBox vb = new VBox();
            vb.getChildren().add(new Label("Postponed Card"));
            if (player == game.getCurrentPlayer()) {
                Button openButton = new Button("Open");
                openButton.setOnAction((ActionEvent e) -> {
                    openButton.setDisable(true);
                    showMessage(c.getCardText(), null);
                    int oldIndex = game.getCurrentPlayer().getCurrentSpace().getIndex();
                    c.getCardEvent().handleEvent(game.getCurrentPlayer(), game.getPlayers(), game.getBoard());
                    drawToken(playerNo, oldIndex, game.getCurrentPlayer().getCurrentSpace().getIndex());
                    drawPlayerBoxes(game.getPlayers());
                });
                vb.getChildren().add(openButton);

            }
            hbCard.getChildren().add(vb);
        }
        assetsDialog.showAndWait();
    }

    @FXML
    public void playerForfeitButtonAction(ActionEvent actionEvent) {
        String buttonID = ((Button) actionEvent.getSource()).getId();
        int playerNo = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
        Player player = game.getPlayers()[playerNo];
        //System.out.println("Player " + playerNo + " GOOJC count: " + player.getGetOutOfJailFreeCount());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setX(420);
        alert.setY(420);
        alert.setHeaderText("Forfeit?");
        alert.setContentText("Do you want to forfeit the game? If you click on \"Yes\", this action would " +
                "mean that you will lose the game and no longer be able to play in this game. ");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            player.lost();
        }
    }

    @FXML
    public void labelUpdate( int lapCount) {
        turn_count.setText("Turn Count: " + lapCount + " ");
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
            int dice1 = scan.nextInt();
            int dice2 = scan.nextInt();
            return new int[]{dice1, dice2};
        }

        int[] dice = new int[2];
        Alert alert;
        System.out.println("turn of " + name);

        if(!digital) {
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
        }
        else {
            alert.setHeaderText(name + " has rolled: " + dice[0] + " " + dice[1]);
        }
        try {
            obj.playDiceSound();
            FileInputStream f = new FileInputStream("assets/img/gifFiles/dice.gif");
            Image image = new Image(f);
            ImageView view = new ImageView(image);
            view.setFitHeight(225);
            view.setFitWidth(300);

            HBox diceBox = new HBox();
            diceBox.setAlignment(Pos.CENTER);
            diceBox.getChildren().add(view);
            alert.getDialogPane().setContent(diceBox);
        } catch(Exception e){
            e.printStackTrace();
        }
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
        alert.showAndWait();
        return dice;
    }

    private final ArrayList<String> tokens = new ArrayList<>();
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
                Thread.sleep(500);
                alert.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            alert.showAndWait();

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

    public void drawToken(int playerNo,int oldIndex, int newIndex) {
        dynamicBoardController.drawToken(playerNo, oldIndex, newIndex);
    }

    public void showMessage(String message, Player currentPlayer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String msg;

        if(currentPlayer == null)
            msg = "";
        else if(currentPlayer instanceof DigitalPlayer)
            msg = currentPlayer.getPlayerName() + " is ";
        else
            msg = "You are ";
        msg += message;

        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setX(420);
        alert.setY(420);
        if(message.equals("You are sent to jail!")) {
            obj.playPoliceSound();
            try{
                FileInputStream f = new FileInputStream("assets/img/gifFiles/handcuff.gif");
                Image image = new Image(f);
                ImageView view = new ImageView(image);
                view.setFitHeight(300);
                view.setFitWidth(300);

                HBox diceBox = new HBox();
                diceBox.setAlignment(Pos.CENTER);
                diceBox.getChildren().add(view);
                alert.getDialogPane().setContent(diceBox);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        alert.showAndWait();
    }

    public boolean buyProperty(PropertySpace property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setHeaderText(property.getName() + " is unowned");
        int value = property.getAssociatedProperty().getValue();
        alert.setContentText("Do you want to buy it for M" + value +"? Otherwise it will be auctioned");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

        alert.setX(420);
        alert.setY(420);

        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            obj.playCashSound();
            return true;
        }
        return false;
    }

    public void spinWheelOfFortune(String result, boolean digitalPlayer) throws Exception{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setVisible(false);


        FileInputStream f = new FileInputStream("assets/img/gifFiles/wheelOfFortune.gif");
        Image image = new Image(f);
        ImageView view = new ImageView(image);
        Alert gifAlert = new Alert(Alert.AlertType.INFORMATION);
        gifAlert.initStyle(StageStyle.UNDECORATED);
        gifAlert.initModality(Modality.APPLICATION_MODAL);
        gifAlert.initOwner(stage);

        Button spinButton = new Button("Spin");
        spinButton.setStyle("-fx-background-color: burlywood");
        spinButton.setOnAction(event -> {
            obj.playWheelSound();
            VBox wheel = new VBox();
            wheel.setAlignment(Pos.CENTER);
            wheel.getChildren().add(view);
            Label l = new Label(result);
            l.setStyle("-fx-background-color: chartreuse; -fx-font-size: 32; -fx-font-weight: bold");

            wheel.getChildren().add(l);
            gifAlert.getDialogPane().setContent(wheel);
            gifAlert.showAndWait();
            //closing previous alert
            if(!digitalPlayer) {
                alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                alert.close();
            }
        });
        alert.getDialogPane().setContent(spinButton);
        if(digitalPlayer){
            System.out.println("Fire button");
            spinButton.fire();
        }
        else {
            alert.showAndWait();
        }
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
        alert.setX(420);
        alert.setY(420);

        if(digital) {
            alert.setHeaderText("Player has finished their turn." );
            try {
                Thread.sleep(500);
                alert.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            alert.setHeaderText("Finish Turn");
        }
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

    //used when digital players open a card
    public void openCard(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
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

            if(! (player instanceof DigitalPlayer)) {
                Button forfeitButton = new Button("Forfeit");
                forfeitButton.setId("forfeitPlayer" + i);
                forfeitButton.getStyleClass().add("forfeitButton");
                forfeitButton.setOnAction(this::playerForfeitButtonAction);
                hb.getChildren().addAll(vb, money, assetsButton, forfeitButton);
            } else
                hb.getChildren().addAll(vb, money, assetsButton);

            playerPane.getChildren().add(hb);

            playerBoxes.getChildren().add(playerPane);

        }
    }

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
        TextField[] textFields = new TextField[4];
        Label[] labels = new Label[4];
        for (int i = 0; i < 4; i++) {
            bids[i] = new Button(" Bid ");
            folds[i] = new Button(" Fold ");
            textFields[i] = new TextField();
        }

        //test
        for (int j = 0; j < 4; j++) {
            labels[j] = new Label(players[j].getPlayerName());
            labels[j].setPrefSize(100, 30);

            int finalJ = j;
            bids[j].setOnAction((ActionEvent e) -> {
                try {
                    bidEvent(finalJ, auc, players, bids, folds, textFields);
                }
                catch (NumberFormatException nfe){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("You must enter a number as your bid");
                    errorAlert.showAndWait();
                }
            });

            folds[j].setOnAction((ActionEvent e) -> {
                foldEvent(finalJ, auc, alert, players, bids, folds, textFields);
            });

            HBox hBox = new HBox(4);
            hBox.getChildren().addAll(labels[j], textFields[j], bids[j], folds[j]);
            gp.add(hBox, 0, j + 1);
        }

        disableExcept(0, bids, folds, textFields);

        if(players[0] instanceof DigitalPlayer){
            int digitalBid = ((DigitalPlayer) players[0]).bidOnAuction(auc.getAuctionedProperty(), auc.getHighestBid());
            System.out.println("Digital Player bids: " + digitalBid);
            if(digitalBid < 0){
                folds[0].fire();
            }
            else {
                textFields[0].setText(String.valueOf(digitalBid));
                bids[0].fire();
            }
        }

        alert.setX(300);
        alert.setY(300);
        gp.setHgap(30);
        gp.setVgap(30);
        alert.showAndWait();


    }

    private void disableExcept(int exception, Button[] bids, Button[] folds, TextField[] textFields){
        for (int i = 0; i < 4; i++) {
            bids[i].setDisable(true);
            folds[i].setDisable(true);
            textFields[i].setDisable(true);
        }
        bids[exception].setDisable(false);
        folds[exception].setDisable(false);
        textFields[exception].setDisable(false);
    }

    private void bidEvent(int bidNum, Auction auc, Player[] players, Button[] bids, Button[] folds, TextField[] textFields){
        int bid = Integer.parseInt(textFields[bidNum].getText());

        if(bid > players[bidNum].getMoney()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You cannot bid more money than you have");
            errorAlert.showAndWait();
        }

        if(bid <= auc.getHighestBid()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You must either raise the bid or fold");
            errorAlert.showAndWait();
        }
        else {
            auc.bid(players[bidNum], bid, bidNum);

            while (auc.getBids()[((bidNum + 1) % 4)] == -1) {
                bidNum = (bidNum + 1) % 4;
            }

            disableExcept(((bidNum + 1) % 4), bids, folds, textFields);
            if(players[((bidNum + 1) % 4)] instanceof DigitalPlayer){
                int digitalBid = ((DigitalPlayer) players[((bidNum + 1) % 4)]).bidOnAuction(auc.getAuctionedProperty(), auc.getHighestBid());
                System.out.println("Digital Player bids: " + digitalBid);
                if(digitalBid < 0){
                    folds[((bidNum + 1) % 4)].fire();
                }
                else {
                    textFields[((bidNum + 1) % 4)].setText(String.valueOf(digitalBid));
                    bids[((bidNum + 1) % 4)].fire();
                }
            }
        }
    }

    private void foldEvent(int bidNum, Auction auc, Alert alert, Player[] players, Button[] bids, Button[] folds, TextField[] textFields){
        auc.fold(players[bidNum], bidNum);
        if (auc.getState() == 0 &&
                !alert.getDialogPane().getButtonTypes().contains(ButtonType.CANCEL)) {
            alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            alert.close();
            System.out.println("Auction closed");
            this.showMessage("The auction for " + auc.getAuctionedProperty().getPropertyName() + " is won by "
                    + auc.getHighestBidder().getPlayerName() + " with a bid of " + auc.getHighestBid(), null);
            return;
        }

        while (auc.getBids()[((bidNum + 1) % 4)] == -1) {
            bidNum = (bidNum + 1) % 4;
        }

        disableExcept(((bidNum + 1) % 4), bids, folds, textFields);
        if(players[((bidNum + 1) % 4)] instanceof DigitalPlayer){
            int digitalBid = ((DigitalPlayer) players[((bidNum + 1) % 4)]).bidOnAuction(auc.getAuctionedProperty(), auc.getHighestBid());
            System.out.println("Digital Player bids: " + digitalBid);
            if(digitalBid < 0){
                folds[((bidNum + 1) % 4)].fire();
            }
            else {
                textFields[((bidNum + 1) % 4)].setText(String.valueOf(digitalBid));
                bids[((bidNum + 1) % 4)].fire();
            }
        }
    }
}
