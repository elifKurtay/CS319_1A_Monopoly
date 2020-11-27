package frontend;

import entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NewGameMenuController {
    private static final int PLAYER_COUNT = 4;
    private Stage stage;
    private String[] players;
    private int currentHumanPlayers;

    @FXML
    private VBox playerList;

    public void initialize() {
        players = new String[PLAYER_COUNT];
        currentHumanPlayers = 0;

        for (int i = 0; i < PLAYER_COUNT; i++) {
            playerList.getChildren().add(createComputerPlayerBox(i+1));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML protected void startButtonAction(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));

        Parent root = loader.load();
        Pane p1 = (Pane) root.lookup("#myPane");
        p1.getChildren().add(new BoardPane());
        Scene s = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(s);
    }

    @FXML protected void backButtonAction(ActionEvent event) {

    }

    @FXML protected void addPlayerButtonAction(ActionEvent event) {
        if (currentHumanPlayers < PLAYER_COUNT) {
            TextInputDialog td = new TextInputDialog("Please enter the player name");
            td.showAndWait();
            if (td.getResult() != null) {
                String name = td.getEditor().getText();
                players[currentHumanPlayers] = name;
                playerList.getChildren().remove(currentHumanPlayers);
                playerList.getChildren().add(currentHumanPlayers, createHumanPlayerBox(currentHumanPlayers+1, name));
                currentHumanPlayers++;
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot add more than 4 players");
            alert.showAndWait();
        }
    }

    private HBox createComputerPlayerBox(int playerNo) {
        HBox playerBox = new HBox();
        playerBox.getChildren().add(new Label("Player " + playerNo + ": Computer"));

        return playerBox;
    }

    private HBox createHumanPlayerBox(int playerNo, String name) {
        HBox playerBox = new HBox();
        playerBox.getChildren().add(new Label("Player " + playerNo + ": " + name));
        Button b = new Button();
        b.setOnAction(this::removePlayerButtonAction);
        playerBox.getChildren().add(b);
        playerBox.getStyleClass().add("playerPane");

        return playerBox;
    }

    private void removePlayerButtonAction(ActionEvent event) {
        System.out.println(event.getSource() + " " + event.getTarget());
        Button b = (Button) event.getSource();
        int removedIndex = playerList.getChildren().indexOf(b.getParent());

        currentHumanPlayers--;
        String[] newPlayers = new String[4];
        for (int i = 0, j = 0; i < currentHumanPlayers; i++) {
            if (i == removedIndex) {
                j++;
            }
            newPlayers[i] = players[i+j];
        }
        players = newPlayers;

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
