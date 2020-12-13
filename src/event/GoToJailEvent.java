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
public class GoToJailEvent extends CardEvent{
    private Space targetSpace;
    private boolean canCollectSalary;

    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        // Current fixed implementation for salary 200, can change for a constant read from map
        if (targetSpace.getIndex() < affectedPlayer.getCurrentSpace().getIndex() && canCollectSalary) {
            affectedPlayer.setMoney(affectedPlayer.getMoney() + 200 + affectedPlayer.getToken().getSalaryChange());
        }
        affectedPlayer.setJailed(true);
        affectedPlayer.setCurrentSpace(targetSpace);
    }
}
