package event;

import board.Board;
import entities.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class CardEvent {
    private Player affectedPlayer;

    abstract void handleEvent(Player affectedPlayer, Player[] players, Board board);
}
