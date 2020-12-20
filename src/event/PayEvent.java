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

public class PayEvent extends CardEvent{
    private String to;
    private int amount;

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        if (to.equals("BANK")) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() - amount);
        }
        else if (to.equals("PLAYERS")) {
            for (Player p : players) {
                if (p != affectedPlayer) {
                    p.setMoney(p.getMoney() + amount);
                }
            }
            affectedPlayer.setMoney(affectedPlayer.getMoney() - 3 * amount);
        }
    }
}
