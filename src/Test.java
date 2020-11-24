import entities.Player;
import javafx.application.Application;

public class Test {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
        Player player = new Player();
        player.getMoney();
    }
}
