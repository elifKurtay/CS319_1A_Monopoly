package board;

import entities.Player;

public class Thief {
    private Player target;
    private Space currentSpace;
    private final static int AMOUNT = 125;

    public Thief(Player target) {
        this.target = target;
    }

    public void steal() {
        target.setMoney(target.getMoney() - AMOUNT);
    }

    public void move(Space space) {
        currentSpace = space;
    }
}
