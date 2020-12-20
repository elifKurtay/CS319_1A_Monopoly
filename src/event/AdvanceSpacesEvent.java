package event;

import board.Board;
import board.Space;
import entities.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdvanceSpacesEvent extends CardEvent {
    private int moveAmount;
    private boolean canCollectSalary;

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        Space targetSpace = board.getSpaces()[(affectedPlayer.getCurrentSpace().getIndex() + moveAmount + 40) % 40];
        if (affectedPlayer.getCurrentSpace().getIndex() + moveAmount >= 40 && canCollectSalary) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() + 200 + affectedPlayer.getToken().getSalaryChange());
        }
        affectedPlayer.setCurrentSpace(targetSpace);
    }
}
