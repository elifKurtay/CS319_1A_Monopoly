package board;

public class WheelOfFortuneSpace extends Space {

    public void spinWheel() {
        int random = (int) Math.random() * 100;
        if (random < 10) {
            // Lose property
        }
        else if (random < 25) {
            // Lose building on a property if possible
        }
        else if (random < 50) {
            // Lose some amount of money
            getLatestPlayerOnSpace().setMoney(getMoney() - 200 * Math.random());
        }
        else if (random < 55) {
            // Gain a get out of jail free card
        }
        else if (random < 75) {
            // Gain some amount of money
            getLatestPlayerOnSpace().setMoney(getMoney() + 200 * Math.random());
        }
        else if (random < 90) {
            // Gain a building on a property if possible
        }
        else {
            // Gain property
        }
    }
}
