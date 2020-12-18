package event;

import board.Board;
import entities.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class CardEvent implements Serializable {
    abstract public void handleEvent(Player affectedPlayer, Player[] players, Board board);
}
