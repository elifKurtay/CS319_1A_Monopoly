package event;

import board.Board;
import entities.Player;

public class ThiefEvent extends CardEvent{

    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        board.deployThief(players[(int)(Math.random()*4)]);
    }

}
