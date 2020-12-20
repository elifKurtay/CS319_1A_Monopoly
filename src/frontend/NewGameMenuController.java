package frontend;

import FileManagement.FileManager;
import game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class NewGameMenuController extends MenuController {
    private static final int PLAYER_COUNT = 4;
    private String[] players;
    private int currentHumanPlayers;

    @FXML
    private VBox playerList;
    @FXML
    private ComboBox<String> mapCombo;
    @FXML
    private ComboBox<Integer> turnLimitCombo;

    public void initialize() {
        players = new String[PLAYER_COUNT];
        currentHumanPlayers = 0;

        for (int i = 0; i < PLAYER_COUNT; i++) {
            playerList.getChildren().add(createComputerPlayerBox(i+1));
        }

        File mapFolder = new File("./assets/maps/");
        File[] maps = mapFolder.listFiles();

        for (File map: maps) {
            if (map.isFile()) {
                mapCombo.getItems().add(map.getName());
            }
        }
        mapCombo.getSelectionModel().select(mapCombo.getItems().get(0));

        turnLimitCombo.getItems().addAll(-1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        turnLimitCombo.getSelectionModel().select(turnLimitCombo.getItems().get(0));
    }


    @FXML protected void startButtonAction(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));
            Parent root = loader.load();
            //Game controller = loader.getController();
            GameScreenController controller = loader.getController();
            controller.setStage(getStage());

            File map = (new File("./assets/maps/" + mapCombo.getValue()));

            //controller.startGame(map, currentHumanPlayers, players, turnLimitCombo.getValue());
            Scene s = new Scene(root, getStage().getWidth(), getStage().getHeight());
            getStage().setScene(s);
            Game game = new Game(map, currentHumanPlayers, players, turnLimitCombo.getValue(), controller);
            controller.setGame(game);
            game.startGame();
        }
        catch (Exception e){
            e.printStackTrace();
            FileManager.getInstance().log(e);
        }
    }



    @FXML protected void addPlayerButtonAction(ActionEvent event) {
        // check if there are already the max amount of players allowed
        if (currentHumanPlayers < PLAYER_COUNT) {
            String name = "";
            List<String> names;
            TextInputDialog td = new TextInputDialog();
            td.setHeaderText("Please enter the player name");
            td.initStyle(StageStyle.UNDECORATED);
            td.initModality(Modality.APPLICATION_MODAL);
            td.initOwner(getStage());
            td.setGraphic(null);
            td.getDialogPane().getStylesheets().add("fxml/style.css");
            td.getDialogPane().getStyleClass().add("alertDialogue");
            do {
                td.showAndWait();

                // if the user presses cancel on the dialog, getResult() returns null
                if (td.getResult() != null) {
                    name = td.getEditor().getText();
                    names = Arrays.asList(players);
                    if(names.contains(name)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setHeaderText("Name is not accepted.");
                        alert.setContentText("Players cannot have the same names. Please enter another name.");
                        alert.setGraphic(null);
                        alert.getDialogPane().getStylesheets().add("fxml/style.css");
                        alert.getDialogPane().getStyleClass().add("alertDialogue");
                        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("OK");
                        alert.showAndWait();
                    }
                } else {
                    name = "";
                    break;
                }
            } while ( names.contains(name) );

            if(!name.equals("")) {
                players[currentHumanPlayers] = name;
                playerList.getChildren().remove(currentHumanPlayers);
                playerList.getChildren().add(currentHumanPlayers, createHumanPlayerBox(currentHumanPlayers + 1, name));
                currentHumanPlayers++;
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot add more than 4 players");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(getStage());
            alert.showAndWait();
        }
    }

    private BorderPane createComputerPlayerBox(int playerNo) {
        BorderPane playerBox = new BorderPane();
        Label l = new Label("Player " + playerNo + ": Computer");
        playerBox.setLeft(l);
        BorderPane.setAlignment(l, Pos.CENTER);
        playerBox.getStyleClass().add("playerBox");
        return playerBox;
    }

    //private HBox createHumanPlayerBox(int playerNo, String name) {
    private BorderPane createHumanPlayerBox(int playerNo, String name) {
        //HBox playerBox = new HBox();
        BorderPane playerBox = new BorderPane();
        playerBox.setStyle("-fx-background-color: green");
        playerBox.getStyleClass().add("playerBox");
        Label l = new Label("Player " + playerNo + ": " + name);
        playerBox.setLeft(l);
        BorderPane.setAlignment(l, Pos.CENTER);

        Button b = new Button("X");
        b.getStyleClass().add("playerRemoveButton");
        b.setMaxSize(25, 25);
        b.setOnAction(this::removePlayerButtonAction);
        playerBox.setRight(b);
        BorderPane.setAlignment(b, Pos.CENTER);

        return playerBox;
    }

    private void removePlayerButtonAction(ActionEvent event) {
        // get the index of the player box to remove in VBox playerList
        Button b = (Button) event.getSource();
        int removedIndex = playerList.getChildren().indexOf(b.getParent());

        currentHumanPlayers--;

        // remove the player from players array
        String[] newPlayers = new String[4];
        for (int i = 0, j = 0; i < currentHumanPlayers; i++) {
            if (i == removedIndex) {
                j++;
            }
            newPlayers[i] = players[i+j];
        }
        players = newPlayers;

        // remove all children (HBoxes) from "VBox playerList"
        // then add the current players' boxes and fill the remaining with computer player boxes
        // this implementation could be improved to be more efficient,
        // but since the number of players is so low, it is not really needed to further complicate the code
        playerList.getChildren().remove(0, PLAYER_COUNT);
        for (int i = 0; i < PLAYER_COUNT; i++) {
            if (i < currentHumanPlayers) {
                playerList.getChildren().add(createHumanPlayerBox(i+1, players[i]));
            }
            else {
                playerList.getChildren().add(createComputerPlayerBox(i+1));
            }
        }
    }
}
