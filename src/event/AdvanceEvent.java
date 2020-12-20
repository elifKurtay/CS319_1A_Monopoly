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
public class AdvanceEvent extends CardEvent{
    private Space targetSpace;
    private boolean canCollectSalary;

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        if (targetSpace.getIndex() < affectedPlayer.getCurrentSpace().getIndex() && canCollectSalary) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() + 200 + affectedPlayer.getToken().getSalaryChange());
        }
        affectedPlayer.setCurrentSpace(targetSpace);
    }
}
