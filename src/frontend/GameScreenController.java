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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        showScoreboard();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
        alert.initOwner(stage);
        alert.setX(420);
        alert.setY(420);

        Label l1 = new Label("Restart?");
        l1.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        l1.setAlignment(Pos.CENTER);

        Label l2 = new Label("Do you want to restart the game? \nIf you click on \"Yes\", this action would " +
                "\nmean that the progress of the game will \nbe deleted and players will have \nto start from the initializing lap. ");
        l2.setStyle("-fx-font-size: 12px;");
        l2.setAlignment(Pos.CENTER);

        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        v.setSpacing(15);
        v.getChildren().add(l1);
        v.getChildren().add(l2);

        alert.getDialogPane().setContent(v);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            game.restartGame();
        }

    }

    @FXML
    protected void saveButtonAction(ActionEvent event) throws Exception {
        game.saveGame();
    }

    @FXML
    protected void tradeButtonAction(ActionEvent event) {
        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(game.getPlayers()));
        players.remove(game.getCurrentPlayer());
        for (Player p : game.getPlayers()) {
            if (p.isBankrupt()) {
                players.remove(p);
            }
        }
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
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
        alert.initOwner(stage);

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Send Offer");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel Trade");

        // http://www.java2s.com/Tutorials/Java/JavaFX/0640__JavaFX_ListView.htm GOT MOST OF THE CODE FROM HERE
        GridPane gp = new GridPane();
        alert.getDialogPane().setContent(gp);
        //game.getCurrentPlayer() -> currentPlayer
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

        int moneyOfferer = currentPlayer.getMoney();
        int goojcOfferer = currentPlayer.getGetOutOfJailFreeCount();
        Label moneyLabelOfferer = new Label(Integer.toString(moneyOfferer));
        Label goojcLabelOfferer = new Label(Integer.toString(goojcOfferer));

        gp.add(moneyLabelOfferer, 0, 2);
        gp.add(goojcLabelOfferer, 0, 3);

        int moneyTarget = playerToTrade.getMoney();
        int goojcTarget = playerToTrade.getGetOutOfJailFreeCount();
        Label moneyLabelTarget = new Label(Integer.toString(moneyTarget));
        Label goojcLabelTarget = new Label(Integer.toString(goojcTarget));

        gp.add(moneyLabelTarget, 2, 2);
        gp.add(goojcLabelTarget, 2, 3);

        Label errorLabel = new Label();
        gp.add(errorLabel, 1, 4);
        errorLabel.setStyle("-fx-text-fill: red");


        int[] given = new int[2];
        int[] requested = new int[2];
        // make this a placeholder, then set it again to placeholder when a value is processed
        TextField moneyField = new TextField();
        moneyField.setPromptText("Money");

        Button giveMoney = new Button(" Give ");
        giveMoney.setOnAction((ActionEvent e) -> {
            String t = moneyField.getText();
            try {
                int amount = Integer.parseInt(t);
                if (amount > 0 && amount > currentPlayer.getMoney()) {
                    //enter a valid amount of money
                    errorLabel.setText("Please enter a valid amount of money");
                }
                else {
                    //add/subtract money to player labels
                    moneyLabelOfferer.setText(moneyOfferer + "(-" + amount + ")");
                    moneyLabelTarget.setText(moneyTarget + "(+" + amount + ")");
                    given[0] = amount;
                    requested[0] = 0;
                    errorLabel.setText("");
                }
            } catch (NumberFormatException nfe) {
                errorLabel.setText("Please enter a valid number");
            }
        });

        Button requestMoney = new Button(" Request ");
        requestMoney.setOnAction((ActionEvent e) -> {
            String t = moneyField.getText();
            try {
                int amount = Integer.parseInt(t);
                if (amount > 0 && amount > playerToTrade.getMoney()) {
                    //enter a valid amount of money
                    errorLabel.setText("Please enter a valid amount of money");
                }
                else {
                    //add/subtract money to player labels
                    moneyLabelOfferer.setText(moneyOfferer + "(+" + amount + ")");
                    moneyLabelTarget.setText(moneyTarget + "(-" + amount + ")");
                    requested[0] = amount;
                    given[0] = amount;
                    errorLabel.setText("");
                }
            } catch (NumberFormatException nfe) {
                errorLabel.setText("Please enter a valid number");
            }
        });
        HBox moneyHB = new HBox();
        moneyHB.getChildren().addAll(giveMoney, moneyField, requestMoney);

        gp.add(moneyHB,1, 2);

        TextField goojcField = new TextField();
        goojcField.setPromptText("Goojc");

        Button giveGoojc = new Button(" Give ");
        giveGoojc.setOnAction((ActionEvent e) -> {
            String t = goojcField.getText();
            try {
                int amount = Integer.parseInt(t);
                if (amount > 0 && amount > currentPlayer.getGetOutOfJailFreeCount()) {
                    //enter a valid amount of money
                    errorLabel.setText("Please enter a valid amount of goojc");
                }
                else {
                    //add/subtract money to player labels
                    goojcLabelOfferer.setText(goojcOfferer + "(-" + amount + ")");
                    goojcLabelTarget.setText(goojcTarget + "(+" + amount + ")");
                    given[1] = amount;
                    requested[1] = 0;
                    errorLabel.setText("");
                }
            } catch (NumberFormatException nfe) {
                errorLabel.setText("Please enter a valid number");
            }
        });

        Button requestGoojc = new Button(" Request ");
        requestGoojc.setOnAction((ActionEvent e) -> {
            String t = goojcField.getText();
            try {
                int amount = Integer.parseInt(t);
                if (amount > 0 && amount > playerToTrade.getGetOutOfJailFreeCount()) {
                    //enter a valid amount of money
                    errorLabel.setText("Please enter a valid amount of goojc");
                }
                else {
                    //add/subtract money to player labels
                    goojcLabelTarget.setText(goojcTarget + "(-" + amount + ")");
                    goojcLabelOfferer.setText(goojcOfferer + "(+" + amount + ")");
                    requested[1] = -amount;
                    given[0] = 0;
                    errorLabel.setText("");
                }
            } catch (NumberFormatException nfe) {
                errorLabel.setText("Please enter a valid number");
            }
        });
        HBox goojcHB = new HBox();
        goojcHB.getChildren().addAll(giveGoojc, goojcField, requestGoojc);

        gp.add(goojcHB, 1, 3);

        alert.showAndWait();

        boolean sentOffer = alert.getResult() == ButtonType.OK;
        if (sentOffer) {
            trade.offer(offeredProperties, given[0], given[1]);
            trade.want(wantedProperties,requested[0], requested[1]);
            tradeProposalDialog(trade);
        }
    }

    public void tradeProposalDialog(Trade t) {
        ;
        String message = t.getTarget() + ", do you accept the offer  from " + t.getOfferer() + "?\n\nYou Get: \n\n";
        for (Property p : t.getOfferedProperties()) {
            message += p + "\n";
        }
        if (t.getOfferedMoney() != 0) {
            message += "M" + t.getOfferedMoney() + "\n";
        }
        if (t.getOfferedGOOJC() != 0) {
            message += t.getOfferedGOOJC() + " GOOJ cards\n";
        }

        message += "\nYou Give:\n";
        for (Property p : t.getWantedProperties()) {
            message += p + "\n";
        }
        if (t.getWantedMoney() != 0) {
            message += "M" + t.getWantedMoney() + "\n";
        }
        if (t.getWantedGOOJC() != 0) {
            message += t.getWantedGOOJC() + " GOOJ cards";
        }

        t.acceptOffer(twoChoiceDialog(message, "Accept", "Deny"));
        t.closeTrade();
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
                    boolean build = twoChoiceDialog("Do you want to build or sell a house?", "Build", "Sell");
                    if (build) {
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
                                message = "Do you want to build a hotel for " + propertyToBuild.getHotelCost() * (game.getCurrentPlayer().getToken().getBuildingCostMultiplier()) / 2 + " on " + propertyToBuild.getPropertyName() + "?";
                                cost *= propertyToBuild.getHotelCost();
                            }
                            else {
                                message = "Do you want to build a house for " + propertyToBuild.getHouseCost() * (game.getCurrentPlayer().getToken().getBuildingCostMultiplier()) / 2 + " on " + propertyToBuild.getPropertyName() + "?";
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
                        if (propertyToBuild.getNumOfHouses() == 0) {
                            showMessage("Cannot sell, there isn't a house on this property!", null);
                        }
                        else if (!propertyToBuild.canSellHouse()) {
                            showMessage("You must sell evenly between properties," +
                                    "sell on other properties in the same group before selling here!", null);
                        }
                        else {
                            String message;
                            double sellAmount = 0.5 * currentPlayer.getToken().getBuildingCostMultiplier();
                            if (propertyToBuild.getNumOfHouses() == 5) {
                                sellAmount *= propertyToBuild.getHotelCost();
                                message = "Do you want to sell the hotel for " + sellAmount + " on " + propertyToBuild.getPropertyName() + "?";
                            }
                            else {
                                sellAmount *= propertyToBuild.getHouseCost();
                                message = "Do you want to sell a house for " + sellAmount + " on " + propertyToBuild.getPropertyName() + "?";
                            }

                            if (twoChoiceDialog(message, "Yes", "No")) {
                                propertyToBuild.sellHouse();
                                currentPlayer.setMoney(currentPlayer.getMoney() - (int) sellAmount);
                                drawPlayerBoxes(game.getPlayers());
                                dynamicBoardController.drawHouse(propertyToBuild.getAssociatedPropertySpace().getIndex(), propertyToBuild.getNumOfHouses());
                            }
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

        assetsDialog.initStyle(StageStyle.UNDECORATED);
        assetsDialog.initModality(Modality.APPLICATION_MODAL);

        assetsDialog.setHeaderText(currentPlayer.getPlayerName() + ", choose to mortgage");
        assetsDialog.setGraphic(null);
        assetsDialog.getDialogPane().getStylesheets().add("fxml/style.css");
        assetsDialog.getDialogPane().getStyleClass().add("alertDialogue");


        HBox hb = new HBox();
        hb.setStyle("-fx-background-color: #FF8A00; -fx-alignment: center");
        ((Button) assetsDialog.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-background-color: #BB55FA");
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
                vb.setAlignment(Pos.CENTER);
                vb.setSpacing(15);
                hb.getChildren().add(vb);
            }
        }
        assetsDialog.showAndWait();
    }

    @FXML
    protected void redeemButtonAction(ActionEvent event) {
        Player currentPlayer = game.getCurrentPlayer();
        Alert assetsDialog = new Alert(Alert.AlertType.INFORMATION);

        assetsDialog.initStyle(StageStyle.UNDECORATED);
        assetsDialog.initModality(Modality.APPLICATION_MODAL);

        assetsDialog.setHeaderText(currentPlayer.getPlayerName() + ", choose to redeem");
        assetsDialog.setGraphic(null);
        assetsDialog.getDialogPane().getStylesheets().add("fxml/style.css");
        assetsDialog.getDialogPane().getStyleClass().add("alertDialogue");

        ((Button) assetsDialog.getDialogPane().lookupButton(ButtonType.OK)).setStyle("-fx-background-color: #BB55FA");

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setStyle("-fx-background-color: #FF8A00");
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
                vb.setAlignment(Pos.CENTER);
                vb.setSpacing(15);
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

        assetsDialog.initStyle(StageStyle.UNDECORATED);
        assetsDialog.initModality(Modality.APPLICATION_MODAL);

        assetsDialog.setHeaderText(player.getPlayerName() + "'s" + " Assets");
        assetsDialog.setGraphic(null);
        assetsDialog.getDialogPane().getStylesheets().add("fxml/style.css");
        assetsDialog.getDialogPane().getStyleClass().add("alertDialogue");

        VBox vbTop = new VBox();
        HBox hb = new HBox();
        vbTop.getChildren().add(hb);
        vbTop.setStyle("-fx-background-color:  #FF8A00");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
        alert.initOwner(stage);

        Label label = new Label();
        label.setStyle("-fx-font-size: 18px;");
        VBox diceBox = new VBox();
        diceBox.setSpacing(10);
        diceBox.setAlignment(Pos.CENTER);

        System.out.println("turn of " + name);

        if(!digital) {
            label.setText(name + " is rolling");
            diceBox.getChildren().add(label);
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Roll");
            alert.setX(420);
            alert.setY(420);
            alert.getDialogPane().setContent(diceBox);
            alert.showAndWait();
        }
        dice[0] = (int) (Math.random() * 6 + 1);
        dice[1] = (int) (Math.random() * 6 + 1);

        alert.setX(420);
        alert.setY(420);
        if (dice[0] == dice[1]) {
            label.setText(name + " has rolled double: " + dice[0] + " " + dice[1]);
        }
        else {
            label.setText(name + " has rolled: " + dice[0] + " " + dice[1]);
        }
        try {
            obj.playDiceSound();
            FileInputStream f = new FileInputStream("assets/img/gifFiles/dice.gif");
            Image image = new Image(f);
            ImageView view = new ImageView(image);
            view.setFitHeight(225);
            view.setFitWidth(300);


            diceBox.getChildren().remove(label);
            diceBox.getChildren().add(label);
            diceBox.getChildren().add(view);
            alert.getDialogPane().setContent(diceBox);
        } catch(Exception e){
            e.printStackTrace();
        }
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Continue");
        alert.showAndWait();
        return dice;
    }

    private ArrayList<String> tokens;
    private final String[] tokenNames = {"Thimble", "Wheel Barrow", "Boot", "Horse", "Race Car",
            "Iron", "Top Hat", "Battleship"};
    private boolean first = true;

    public int chooseToken(String name, boolean digital, boolean restart) {
        if(first || restart) {
            tokens = new ArrayList<>();
            tokens.addAll(Arrays.asList(tokenNames));
            first = false;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().getButtonTypes().remove(ButtonType.OK);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");


        if(digital) {
            String chosen = tokens.get(0);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setX(420);
            alert2.setY(420);
            alert2.initStyle(StageStyle.UNDECORATED);
            alert2.initModality(Modality.APPLICATION_MODAL);
            alert2.setHeaderText(name + " has chosen: " + chosen);
            alert2.setGraphic(null);
            alert2.getDialogPane().getStylesheets().add("fxml/style.css");
            alert2.getDialogPane().getStyleClass().add("alertDialogue");
            try {
                Thread.sleep(500);
                alert2.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            alert2.showAndWait();

            tokens.remove(chosen);
            return IntStream.range(0, tokenNames.length).filter(i -> tokenNames[i].equals(chosen))
                    .findFirst().orElse(-1) + 1;
        }

        VBox chooseTokenBox = new VBox();
        chooseTokenBox.setStyle("-fx-alignment: center");
        chooseTokenBox.setSpacing(20);
        Label info = new Label(name + " CHOOSE YOUR PIECE!");
        info.setStyle("-fx-font-size: 16px; -fx-font-weight: bold");
        chooseTokenBox.getChildren().add(info);
        HBox[] rows = new HBox[2];
        VBox[] pieceBox = new VBox[8];

        Image[] images = new Image[8];
        images[0] = new Image("img\\token\\cropped\\thimble.png");
        images[1] = new Image("img\\token\\cropped\\wheelbarrow.png");
        images[2] = new Image("img\\token\\cropped\\boot.png");
        images[3] = new Image("img\\token\\cropped\\horse.png");
        images[4] = new Image("img\\token\\cropped\\racecar.png");
        images[5] = new Image("img\\token\\cropped\\iron.png");
        images[6] = new Image("img\\token\\cropped\\tophat.png");
        images[7] = new Image("img\\token\\cropped\\battleship.png");

        Label[] tokenInfo = new Label[8];
        tokenInfo[0] = new Label("+ Tax multiplier \n" +
                "    of 0.8\n" +
                "-  Rent collect \n" +
                "    multiplier of 0.8\n");
        tokenInfo[1] = new Label("+ Building cost is half\n" +
                "- Salary change \n" +
                "    of -M50\n" +
                "  (Total salary is M150)\n");
        tokenInfo[2] = new Label("+ Property cost \n" +
                "    multiplier 0.8\n" +
                "-  Salary change of \n" +
                "    -M100 " +
                " (Total salary is M100)\n");
        tokenInfo[3] = new Label("+ Rent collect \n" +
                "    multiplier of 1.3\n" +
                "- Property cost \n" +
                "   multiplier of 1.1\n");
        tokenInfo[4] = new Label("+ Bonus salary \n" +
                "    is M200 \n" +
                " (Total salary is M400)\n" +
                "- Jail Time of " +
                "   4 rounds\n");
        tokenInfo[5] = new Label("+ Building cost \n" +
                "   multiplier of 0.8\n" +
                "- Rent pay \n" +
                "   multiplier of 1.2\n");
        tokenInfo[6] = new Label("+ Jail Time of \n" +
                "    2 rounds\n" +
                "-  Tax Multiplier \n" +
                "     of 1.2\n");
        tokenInfo[7] = new Label("+ Rent pay \n" +
                "    multiplier of 0.7\n" +
                "-  Building cost \n" +
                "    multiplier is 1.5\n");

        ImageView view;
        final int[] cnt = new int[1];
        for(int i = 0; i < rows.length; i++){
            rows[i] = new HBox();
            rows[i].setAlignment(Pos.CENTER);
            rows[i].setSpacing(10);
            for(int j = 0; j < 4; j++){
                HBox frame = new HBox();
                frame.setStyle("-fx-border-width: 3; -fx-border-style: solid;");
                view = new ImageView(images[i*4+j]);
                frame.getChildren().add(view);
                pieceBox[i*4+j] = new VBox();
                pieceBox[i*4+j].setSpacing(5);
                pieceBox[i*4+j].setStyle("-fx-background-color: #F8E6C5");
                pieceBox[i*4+j].setAlignment(Pos.CENTER);
                Label pieceName = new Label(tokenNames[i*4+j]);
                pieceName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold");
                pieceBox[i*4+j].getChildren().add(pieceName);
                pieceBox[i*4+j].getChildren().add(frame);
                int finalJ = j;
                int finalI = i;
                Button b = new Button("Select");
                b.setStyle("-fx-background-color: #9FFA09;");
                if(tokens.contains(tokenNames[i*4+j])){
                    final String tokenToBeRemoved = tokenNames[i*4+j];
                    b.setOnAction(event -> {
                        tokens.remove(tokenToBeRemoved);
                        cnt[0] = (finalI * 4) + finalJ;
                        alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        alert.close();
                    });
                }
                else
                    b.setDisable(true);

                pieceBox[i*4+j].getChildren().add(b);
                pieceBox[i*4+j].getChildren().add(tokenInfo[i*4+j]);
                rows[i].getChildren().add(pieceBox[i*4+j]);
            }
            chooseTokenBox.getChildren().add(rows[i]);
        }

        alert.getDialogPane().setContent(chooseTokenBox);

        alert.showAndWait();
        return cnt[0] + 1;
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

        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
        Label l = new Label(msg);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(l);

        System.out.println(message);

        if(message.equals("sent to jail!")) {
            obj.playPoliceSound();
            try{
                FileInputStream f = new FileInputStream("assets/img/gifFiles/handcuff.gif");
                Image image = new Image(f);
                ImageView view = new ImageView(image);
                view.setFitHeight(300);
                view.setFitWidth(300);
                vbox.getChildren().add(view);
                alert.getDialogPane().setContent(vbox);
                System.out.println("sajksda");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        alert.getDialogPane().setContent(vbox);
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
    }

    public boolean buyProperty(PropertySpace property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label l1 = new Label(property.getName() + " is unowned");
        int value = property.getAssociatedProperty().getValue();
        Label l2 = new Label("Do you want to buy it for M" + value +"? Otherwise it will be auctioned");
        l1.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
        l2.setStyle("-fx-font-size: 12px");

        vbox.getChildren().add(l1);
        vbox.getChildren().add(l2);
        alert.getDialogPane().setContent(vbox);


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
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");


        FileInputStream f = new FileInputStream("assets/img/gifFiles/wheelOfFortune.gif");
        Image image = new Image(f);
        ImageView view = new ImageView(image);
        Alert gifAlert = new Alert(Alert.AlertType.INFORMATION);
        gifAlert.initStyle(StageStyle.UNDECORATED);
        gifAlert.initModality(Modality.APPLICATION_MODAL);
        gifAlert.initOwner(stage);
        gifAlert.setHeaderText("");
        gifAlert.setGraphic(null);
        gifAlert.getDialogPane().getStylesheets().add("fxml/style.css");
        gifAlert.getDialogPane().getStyleClass().add("alertDialogue");

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
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
        Label l = new Label("Welcome to Wheel of Fortune!");
        vbox.getChildren().add(l);
        vbox.getChildren().add(spinButton);
        alert.getDialogPane().setContent(vbox);
        if(digitalPlayer){
            System.out.println("Fire button");
            spinButton.fire();
        }
        else {
            if (result.contains("house")) {
                String[] propertyName = result.split("a house on ", 0);
                for (Space s : game.getBoard().getSpaces()) {
                    if (s.getName().equals(propertyName)) {
                        dynamicBoardController.drawHouse(s.getIndex(), ((LandProperty)((PropertySpace) s).getAssociatedProperty()).getNumOfHouses());
                    }
                }
            }
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
    private boolean restart = false;

    public void finishTurn(Boolean digital) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.NONE);
        alert.initOwner(stage);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");

        alert.setX(420);
        alert.setY(420);
        Label l = new Label();
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        l.setAlignment(Pos.CENTER);

        if(digital) {
            l.setText("Player has finished their turn." );
            try {
                Thread.sleep(500);
                alert.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            l.setText("Finish Turn");
        }
        alert.getDialogPane().setContent(l);
        alert.showAndWait();
    }

    public boolean postponeCard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
        alert.initOwner(stage);

        Label l = new Label("Do you want to open the card now?");
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");
        l.setAlignment(Pos.CENTER);
        alert.getDialogPane().setContent(l);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Open Now");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Postpone");
        alert.setX(420);
        alert.setY(420);
        alert.showAndWait();
        return alert.getResult() == ButtonType.CANCEL;
    }

    //used when digital players open a card
    public void openCard(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");
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

            Label money = new Label("M" + player.getMoney());
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
                VBox assetFF = new VBox(2);
                assetFF.getChildren().addAll(assetsButton, forfeitButton);
                assetFF.setAlignment(Pos.CENTER);
                hb.getChildren().addAll(vb, money, assetFF);
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
        gp.setStyle("-fx-background-color: #FF8A00");
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
            bids[i].setStyle("-fx-background-color: #E5F900; -fx-font-weight: bold;");
            folds[i] = new Button(" Fold ");
            folds[i].setStyle("-fx-background-color: #E5F900; -fx-font-weight: bold;");
            textFields[i] = new TextField();
        }

        for (int j = 0; j < 4; j++) {
            labels[j] = new Label(players[j].getPlayerName());
            labels[j].setPrefSize(100, 30);
            labels[j].setAlignment(Pos.CENTER);

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
                foldEvent(finalJ, auc, alert, players, labels, bids, folds, textFields);
            });

            HBox hBox = new HBox(4);
            hBox.getChildren().addAll(labels[j], textFields[j], bids[j], folds[j]);
            gp.add(hBox, 0, j + 1);
        }

        for (int i = 0; i < 4; i++) {
            if (players[i].isBankrupt()) folds[i].fire();
        }

        int firstPlayer = 0;
        for (int i = 0; i < 4; i++) {
            if (!players[i].isBankrupt()){
                firstPlayer = i;
                break;
            }
        }

        disableExcept(firstPlayer, bids, folds, textFields);

        if(players[firstPlayer] instanceof DigitalPlayer){
            int digitalBid = ((DigitalPlayer) players[firstPlayer]).bidOnAuction(auc.getAuctionedProperty(), auc.getHighestBid());
            System.out.println("Digital Player bids: " + digitalBid);
            if(digitalBid < 0){
                folds[firstPlayer].fire();
            }
            else {
                textFields[firstPlayer].setText(String.valueOf(digitalBid));
                bids[firstPlayer].fire();
            }
        }

        boolean allDigitalPlayers = true;
        for (int i = 0; i < 4; i++) {
            if (!(players[i] instanceof DigitalPlayer) ){
                allDigitalPlayers = false;
                break;
            }
        }

        alert.setX(300);
        alert.setY(300);
        gp.setHgap(30);
        gp.setVgap(30);
        if (!allDigitalPlayers) alert.showAndWait();
        drawPlayerBoxes(players);
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

    private void foldEvent(int bidNum, Auction auc, Alert alert, Player[] players, Label[] labels, Button[] bids, Button[] folds, TextField[] textFields){
        int leftBidders = 0;
        int lastBidder = -1;
        for (int i = 0; i < 4; i++) {
            if(players[i] != players[bidNum] && auc.getBids()[i] > -1){
                leftBidders++;
                lastBidder = i;
            }
        }
        if (leftBidders == 1 && auc.getHighestBid() == 0){
            auc.bid(players[lastBidder], 1,lastBidder);
        }

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

        labels[bidNum].setStyle("-fx-background-color: red; -fx-font-weight: bold");
        System.out.println(labels[bidNum].getStyle());

        //labels[bidNum].setDisable(true);

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


    public void showScoreboard() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("");
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add("fxml/style.css");
        alert.getDialogPane().getStyleClass().add("alertDialogue");

        alert.initOwner(stage);
        Player[] players = game.getPlayers();
        VBox box = new VBox();
        TableView<Object> scoreBoard;
        TableColumn<Object, Integer> rank;
        TableColumn<Object, String> name;
        TableColumn<Object, Integer> netWorth;

        scoreBoard = new TableView<>();
        scoreBoard.setEditable(true);

        rank = new TableColumn<Object, Integer>("Rank");
        name = new TableColumn<Object, String>("Name");
        netWorth = new TableColumn<Object, Integer>("Net Worth");
        rank.setPrefWidth(60);
        name.setPrefWidth(100);
        netWorth.setPrefWidth(100);

        rank.setStyle("-fx-alignment: CENTER;");
        name.setStyle("-fx-alignment: CENTER;");
        netWorth.setStyle("-fx-alignment: CENTER;");


        scoreBoard.getColumns().addAll(rank, name, netWorth);

        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        netWorth.setCellValueFactory(new PropertyValueFactory<>("netWorth"));


        Score[] scores = new Score[4];
        for (int i = 0; i < 4; i++) {
            scores[i] = new Score((i + 1), players[i].getPlayerName(),
                    players[i].getNetWorth());
        }

        for(int i = 0; i < 4; i++) {
            for(int j = i + 1; j < 4; j++){
                if(scores[j].getNetWorth() > scores[i].getNetWorth()){
                    Score temp = scores[i];
                    scores[i] = scores[j];
                    scores[j] = temp;
                }
            }
            scores[i].setRank(i + 1);
        }


        scoreBoard.getItems().addAll(scores);


        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center");
        hBox.setSpacing(20);
        Button cancel = new Button("Cancel");
        Button endGame = new Button("End Game");
        cancel.setStyle("-fx-background-color: #FF4848; -fx-font-size: 14px");
        endGame.setStyle("-fx-background-color: #FF4848; -fx-font-size: 14px");

        endGame.setOnAction(event -> {
            if (twoChoiceDialog("Do you really want to exit?", "Yes", "No")) {
                if (twoChoiceDialog("Do you want to save the game?", "Yes", "No")) {
                    try {
                        saveButtonAction(null);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                try {
                    /*stage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
                    Parent root = loader.load();
                    MainMenuController controller = loader.getController();
                    Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
                    stage.setScene(s);
                    controller.setStage(stage);

                     */
                    Platform.exit();

                } catch (Exception e){

                }
            }
        });

        cancel.setOnAction(event -> {
            alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            alert.close();
        });

        hBox.getChildren().add(cancel);
        hBox.getChildren().add(endGame);

        Label scoreBoardLabel = new Label("Scoreboard");
        scoreBoardLabel.setStyle("-fx-alignment: center; -fx-text-fill: red; " +
                "-fx-font-weight: bold; -fx-font-size: 28px");

        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);
        box.getChildren().add(scoreBoardLabel);
        box.getChildren().add(scoreBoard);
        box.getChildren().add(hBox);


        alert.getDialogPane().setContent(box);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setVisible(false);
        alert.showAndWait();
    }

    @Getter
    @Setter
    public class Score{
        private String name;
        private int rank, netWorth;
        public Score(int rank, String name, int netWorth){
            this.rank = rank;
            this.name = name;
            this.netWorth = netWorth;
        }
    }
}
