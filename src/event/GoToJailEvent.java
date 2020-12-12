package event;

import board.Board;
import entities.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GoToJailEvent extends CardEvent{
    private boolean canCollectSalary;

    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        affectedPlayer.setJailed(true);
    }
}
