import frontend.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/fxml/MainMenu.fxml"));
        System.out.println(loader.getLocation());
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setStage(primaryStage);

        Scene s = new Scene(root,800, 450);

        primaryStage.setScene(s);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
