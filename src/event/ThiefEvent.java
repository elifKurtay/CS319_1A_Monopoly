package event;

import board.Board;
import entities.Player;
import frontend.GameScreenController;

public class ThiefEvent extends CardEvent{

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        int target;
        do {
            target = (int)(Math.random()*4);
        }while (players[target].isBankrupt());
        if (GameScreenController.DEMO){
            board.deployThief(players[3]);
            return;
        }
        board.deployThief(players[target]);
    }

}
