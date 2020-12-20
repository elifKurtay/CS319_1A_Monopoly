package board;

import entities.LandProperty;
import entities.Property;

import java.util.ArrayList;

public class WheelOfFortuneSpace extends Space {

    public WheelOfFortuneSpace(int index) {
        super("Wheel of Fortune", index);
    }

    /**
     * Spins the wheel of fortune and returns a result
     * @return the result of wheel of fortune
     */
    public String spinWheel() {
        int random;
        while(true){
            System.out.println("deneme2d");
            random = (int) (Math.random() * 100);
            ArrayList<Property> properties = getLatestPlayer().getProperties();
            if (random < 15 && !properties.isEmpty()) {
                //Lose property
                return "You lost this property " +
                        properties.remove((int) (properties.size() * Math.random())).getPropertyName();
            }
            else if (random < 30) {
                // Lose building on a property if possible
                for(Property p: properties){
                    if(p instanceof LandProperty){
                        int numOfHouses = ((LandProperty)p).getNumOfHouses();
                        if ( numOfHouses != 0) {
                            ((LandProperty) p).setNumOfHouses(numOfHouses - 1);
                            return "You lost a house in " + p.getPropertyName();
                        }
                    }
                }
            }
            else if (random < 45) {
                // Lose some amount of money
                int lostMoney = (int) (200 * Math.random());
                getLatestPlayer().setMoney((int) (getLatestPlayer().getMoney() - lostMoney));
                return "You lost M" + lostMoney;
            }
            else if (random < 60) {
                // Gain a get out of jail free card
                getLatestPlayer().setGetOutOfJailFreeCount(getLatestPlayer().getGetOutOfJailFreeCount() + 1);
            }
            else if (random < 75) {
                // Gain some amount of money
                int gainMoney = (int) (200 * Math.random());
                getLatestPlayer().setMoney((getLatestPlayer().getMoney() + gainMoney));
                return "You won M" + gainMoney;
            }
            else if (random < 95) {
                // Gain a building on a property if possible
                for(Property p: properties) {
                    if(p instanceof LandProperty)
                        if (((LandProperty) p).canBuild()) {
                            ((LandProperty)p).buildHouse();
                            return "You won a house on " + p.getPropertyName();
                        }
                }
            }
            else {
                // Gain some amount of money
                getLatestPlayer().setMoney((getLatestPlayer().getMoney() + 250));
                return "You won M" + 250;
            }
        }
    }
}
