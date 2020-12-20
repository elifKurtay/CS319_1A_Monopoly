package event;

import board.Board;
import entities.Player;

public class ReceiveGetOutOfJailEvent extends CardEvent{

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        affectedPlayer.setGetOutOfJailFreeCount(affectedPlayer.getGetOutOfJailFreeCount() + 1);
    }
}
