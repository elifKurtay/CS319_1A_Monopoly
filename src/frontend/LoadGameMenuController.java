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

import java.io.File;
import java.util.ArrayList;

public class LoadGameMenuController extends MenuController {

    @FXML private TableView<SaveData> loadTable;
    @FXML private TableColumn<Object, String> noColumn, dateColumn, timeColumn, player1, player2, player3, player4, delete;
    private final FileManager fileManager = FileManager.getInstance();

    /**
     * Initializes the controller
     */
    public void initialize() {
        //find all files in savedGames directory
        File folder = new File("savedGames");
        File[] listOfFiles = folder.listFiles();

        //file Names
        ArrayList<String> fileNames = new ArrayList<>();

        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                fileNames.add(listOfFile.getName());
                System.out.print(listOfFile.getName());
            }

            noColumn.setCellValueFactory(new PropertyValueFactory<>("gameNo"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            player1.setCellValueFactory(new PropertyValueFactory<>("player1"));
            player2.setCellValueFactory(new PropertyValueFactory<>("player2"));
            player3.setCellValueFactory(new PropertyValueFactory<>("player3"));
            player4.setCellValueFactory(new PropertyValueFactory<>("player4"));
            delete.setCellValueFactory(new PropertyValueFactory<>("delete"));


            //table input
            String[] info;
            SaveData s;

            for (int i = 0; i < fileNames.size(); i++) {
                info = fileNames.get(i).split("_");
                s = new SaveData(i, info[4].split(" ")[0], info[4].split(" ")[1],
                        info[0], info[1], info[2], info[3], info[5] );
                loadTable.getItems().add(s);
            }
        }
    }

    public class SaveData {
        @Getter private final String date, time, player1, player2, player3, player4;
        @FXML @Getter private final Button gameNo, delete;
        private final String gameName;

        /**
         * Constructor for the save data
         * @param gameNo Game id
         * @param date Date of the game
         * @param time Time of the game
         * @param player1 Player 1
         * @param player2 Player 2
         * @param player3 Player 3
         * @param player4 Player 4
         * @param gameName Name of the game
         */
        public SaveData(int gameNo, String date, String time, String player1, String player2, String player3, String player4, String gameName) {
            this.gameName = gameName;
            this.gameNo = new Button("" + gameNo);
            this.gameNo.setOnAction(event -> {
                try {
                    action();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            this.gameNo.setStyle("-fx-background-color: #FFE55C");
            this.date = date;
            this.time = time;
            this.player1 = player1;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
            this.delete = new Button("X");
            this.delete.setStyle("-fx-background-color: #FFE55C");
            this.delete.setOnAction(event -> {
                try {
                    delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        /**
         * Save action
         * @throws Exception
         */
        public void action() throws Exception{
            String folderName = "savedGames\\" + player1 + "_" + player2 + "_" + player3 + "_" + player4 + "_" + date + " " + time + "_" + gameName ;
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

        /**
         * Delete action
         */
        public void delete() {
            String folderName = "savedGames\\" + player1 + "_" + player2 + "_" + player3 + "_" + player4 + "_" + date + " " + time + "_" + gameName;
            fileManager.delete(folderName);
            loadTable.getItems().remove(this);
            loadTable.refresh();
        }
    }
}
