package event;

import board.Board;
import entities.LandProperty;
import entities.Player;
import entities.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PayPerBuildingEvent extends CardEvent {
    private String to;
    private int[] amount;

    /**
     * Handles the event and performs operations on the players and the board
     * @param affectedPlayer
     * @param players
     * @param board
     */
    @Override
    public void handleEvent(Player affectedPlayer, Player[] players, Board board) {
        int total = 0;
        for (Property p: affectedPlayer.getProperties()) {
            if (p.getPropertyGroup() < 8) {
                LandProperty lp = (LandProperty) p;
                if (lp.getNumOfHouses() == 5) {
                    total += 4 * amount[0] + amount[1];
                }
                else {
                    total += lp.getNumOfHouses() * amount[0];
                }
            }
        }
        affectedPlayer.setMoney(affectedPlayer.getMoney() - total);
    }
}
