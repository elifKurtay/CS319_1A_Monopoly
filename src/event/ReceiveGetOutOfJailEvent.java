package event;

import board.Board;
import entities.Player;

public class ReceiveGetOutOfJailEvent extends CardEvent{
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        affectedPlayer.setGetOutOfJailFreeCount(affectedPlayer.getGetOutOfJailFreeCount() + 1);
    }
}
