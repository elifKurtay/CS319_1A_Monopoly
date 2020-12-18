package frontend;

import FileManagement.FileManager;
import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class LoadGameMenuController extends MenuController {

    @FXML private TableView loadTable;
    @FXML private TableColumn noColumn, dateColumn, timeColumn, player1, player2, player3, player4;
    private FileManager fileManager = FileManager.getInstance();

    public void initialize() {
        //find all files in savedGames directory
        File folder = new File("savedGames");
        File[] listOfFiles = folder.listFiles();

        //file Names
        ArrayList<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            fileNames.add(listOfFiles[i].getName());
            System.out.print(listOfFiles[i].getName());
        }

        noColumn.setCellValueFactory(new PropertyValueFactory<>("gameNo"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        player1.setCellValueFactory(new PropertyValueFactory<>("player1"));
        player2.setCellValueFactory(new PropertyValueFactory<>("player2"));
        player3.setCellValueFactory(new PropertyValueFactory<>("player3"));
        player4.setCellValueFactory(new PropertyValueFactory<>("player4"));


        //table input
        String[] info;
        SaveData s;

        for(int i = 0; i < fileNames.size(); i++){
            info = fileNames.get(i).split("_");
            s = new SaveData(i, info[4].split(" ")[0], info[4].split(" ")[1],
                    info[0], info[1], info[2], info[3]);
            loadTable.getItems().add(s);
        }
    }

    public class SaveData {
        @Getter private String date, time, player1, player2, player3, player4;
        @FXML @Getter private Button gameNo;

        public SaveData(int gameNo, String date, String time, String player1, String player2, String player3, String player4) {
            this.gameNo = new Button("" + gameNo);
            this.gameNo.setOnAction(event -> {
                try {
                    action();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            this.gameNo.setStyle("-fx-background-color: FFE55C");
            this.date = date;
            this.time = time;
            this.player1 = player1;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
        }

        public void action() throws Exception{

            String folderName = "savedGames\\" + player1 + "_" + player2 + "_" + player3 + "_" + player4 + "_" + date + " " + time;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScreen.fxml"));
            Parent root = loader.load();
            GameScreenController controller = loader.getController();
            controller.setStage(LoadGameMenuController.this.getStage());
            Scene s = new Scene(root, getStage().getWidth(), getStage().getHeight());
            getStage().setScene(s);
            Game g = fileManager.loadGame(folderName, controller);
            controller.setGame(g);
            g.continueGame();
        }
    }
}
