package frontend;

import FileManagement.FileManager;
import board.Board;
import board.PropertySpace;
import board.Space;
import entities.Player;
import game.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import org.eclipse.jetty.util.IO;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

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
    @FXML
    private AnchorPane dynamicBoard;
    @FXML
    private DynamicBoardController dynamicBoardController;
    @FXML
    private Label mapNameLabel;

    @FunctionalInterface
    public interface IOExceptionActionEvent {
        void a() throws IOException;
    }

    /**
     * Initializes the controller
     */
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

        dynamicBoard.getStylesheets().clear();
        dynamicBoard.getStylesheets().add("fxml/mapPreview.css");
        dynamicBoard.applyCss();

        setMapPreview(new File("./assets/maps/" + mapCombo.getValue()));

        mapCombo.setOnAction(event -> {
            dynamicBoardController.clearMap();
            setMapPreview(new File("./assets/maps/" + mapCombo.getValue()));
        });

        turnLimitCombo.getItems().addAll(-1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        turnLimitCombo.getSelectionModel().select(turnLimitCombo.getItems().get(0));
    }


    /**
     * Starts the game
     * @param event Action event that triggers the action
     * @throws Exception
     */
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm");
            String gameName = dtf.format(LocalDateTime.now()) + "$" + Arrays.hashCode(players);
            System.out.println("GAME NAME: " + gameName);
            Game game = new Game(gameName, map, currentHumanPlayers, players, turnLimitCombo.getValue(), controller);
            controller.setGame(game);
            game.startGame();
        }
        catch (Exception e){
            e.printStackTrace();
            FileManager.getInstance().log(e);
        }
    }


    /**
     * Adds a human player to the game
     * @param event Action event that triggers the action
     */
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
                    else if(name.contains("_")){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.UNDECORATED);
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setHeaderText("Name is not accepted.");
                        alert.setContentText("Underscore character is not accepted in player names. Please enter another name.");
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
            } while ( names.contains(name) || name.contains("_") );

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

    /**
     * Fills the rest with computer players
     * @param playerNo The number of the player
     * @return The Computer player box
     */
    private BorderPane createComputerPlayerBox(int playerNo) {
        BorderPane playerBox = new BorderPane();
        Label l = new Label("Player " + playerNo + ": Computer");
        playerBox.setLeft(l);
        BorderPane.setAlignment(l, Pos.CENTER);
        playerBox.getStyleClass().add("playerBox");
        return playerBox;
    }

    /**
     * Creates a human player box
     * @param playerNo Number of the player
     * @param name The name of the player
     * @return The human player box
     */
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

    /**
     * Removes a human player from the game
     * @param event Action event that triggers the action
     */
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

    private void setMapPreview(File map) {
        Board board = new Board(map);
        mapNameLabel.setText(board.getMapName());
        dynamicBoardController.setDynamicBoard(board);

    }

}
