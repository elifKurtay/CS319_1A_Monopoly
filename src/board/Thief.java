package board;

import entities.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Thief {
    private Player target;
    private Space currentSpace;
    private int lapCount; //out in 5 laps?
    private final static int STEAL_AMOUNT = 1000;
    //steal a specific amount or all the money ?

    public Thief(Player target) {
        this.target = target;
        lapCount = 0;
        currentSpace = null;
    }

    public void steal() {
        target.setMoney(target.getMoney() - STEAL_AMOUNT);
    }

    public boolean move(Space space) {
        currentSpace = space;

        return currentSpace == target.getCurrentSpace();
    }

    public int rollDice() {
        if(lapCount > 5)
            return -1; //disappear without stealing

        int dice = 0;
        int difference = target.getCurrentSpace().getIndex() - currentSpace.getIndex();
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
