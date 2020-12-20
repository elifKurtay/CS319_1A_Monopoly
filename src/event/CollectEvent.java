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

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        if (from.equals("BANK")) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() + amount);
        }
        else if (from.equals("PLAYERS")) {
            int total = 0;
            for (Player p : players) {
                if (p != affectedPlayer && !p.isBankrupt()) {
                    p.setMoney(p.getMoney() - amount);
                    total += amount;
                }
            }
            affectedPlayer.setMoney(affectedPlayer.getMoney() + total);
        }
    }
}
