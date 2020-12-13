package event;

import board.Board;
import entities.Player;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CollectEvent extends CardEvent{
    private String from;
    private int amount;


    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        if (from.equals("BANK")) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() + amount);
        }
        else if (from.equals("PLAYERS")) {
            for (Player p : players) {
                if (p != affectedPlayer) {
                    p.setMoney(p.getMoney() - amount);
                }
            }
            affectedPlayer.setMoney(affectedPlayer.getMoney() + 3 * amount);
        }
    }
}
