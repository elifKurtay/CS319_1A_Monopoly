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
    private boolean toBank;
    private ArrayList<Player> receivers;
    private int amount;

    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {

    }
}
