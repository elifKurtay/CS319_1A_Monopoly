package event;

import board.Board;
import entities.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CardEvent {
    abstract public void handleEvent(Player affectedPlayer, Player[] players, Board board);
}
