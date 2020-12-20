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

    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        if (to.equals("BANK")) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() - amount);
        }
        else if (to.equals("PLAYERS")) {
            int total = 0;
            for (Player p : players) {
                if (p != affectedPlayer && !p.isBankrupt()) {
                    p.setMoney(p.getMoney() + amount);
                    total += amount;
                }
            }
            affectedPlayer.setMoney(affectedPlayer.getMoney() - amount);
        }
    }
}
