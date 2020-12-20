package board;

import entities.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Thief implements Serializable {
    private Player target;
    private Space currentSpace;
    private int lapCount; //out in 5 laps?
    public final static int STEAL_AMOUNT = 1000;
    //steal a specific amount or all the money ?

    /**
     * This constructor initializes the thief.
     * The attribute target is set.
     * @param target
     */
    public Thief(Player target) {
        this.target = target;
        lapCount = 0;
        currentSpace = null;
    }

    /**
     * This method subtracts the steal amount from the target's money.
     */
    public void steal() {
        System.out.println("Stole" + STEAL_AMOUNT + " from " + target.getPlayerName());
        target.setMoney(target.getMoney() - STEAL_AMOUNT);
    }

    /**
     * Moves the thief to the current space and checks if the target player is on that space.
     * @param space
     * @return true if target is on that space and false if target is not on that space
     */
    public boolean move(Space space) {
        currentSpace = space;

        return currentSpace == target.getCurrentSpace();
    }

    /**
     * Rolls dice for the thief and returns the result
     * @return dice result
     */
    public int rollDice() {
        if(lapCount > 5)
            return -1; //disappear without stealing

        int dice = 0;
        int difference = target.getCurrentSpace().getIndex() - currentSpace.getIndex();
        if (difference < 0)
            difference = 40 - difference;
        if(difference > 12) {
            dice = (int) (Math.random() * 5 + 8); //[8,12]
        } else if (difference > 6)
            dice = (int) (Math.random() * 6 + 6); //[6,12]
        else
            dice = difference; //caught!

        lapCount++;
        return dice;
    }
}
