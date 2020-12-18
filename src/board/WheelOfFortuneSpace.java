package board;

import entities.LandProperty;
import entities.Property;

import java.util.ArrayList;

public class WheelOfFortuneSpace extends Space {

    public WheelOfFortuneSpace(int index) {
        super("Wheel of Fortune", index);
    }

    public String spinWheel() {
        int random;
        while(true){
            System.out.println("deneme2d");
            random = (int) (Math.random() * 100);
            ArrayList<Property> properties = getLatestPlayer().getProperties();
            if (random < 10 && !properties.isEmpty()) {
                //Lose property
                return "You lost this property" +
                        properties.remove((int) (properties.size() * Math.random())).getPropertyName();
            }
            else if (random < 25) {
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
            else if (random < 50) {
                // Lose some amount of money
                int lostMoney = (int) (200 * Math.random());
                getLatestPlayer().setMoney((int) (getLatestPlayer().getMoney() - lostMoney));
                return "You lost " + lostMoney + "M";
            }
            else if (random < 55) {
                // Gain a get out of jail free card

            }
            else if (random < 75) {
                // Gain some amount of money
                int gainMoney = (int) (200 * Math.random());
                getLatestPlayer().setMoney((int) (getLatestPlayer().getMoney() - gainMoney));
                return "You won " + gainMoney + "M";
            }
            else if (random < 90) {
                // Gain a building on a property if possible
                for(Property p: properties) {
                    if(p instanceof LandProperty)
                        if (getLatestPlayer().ownsAllPropertiesFromSameGroup(p)) {
                            ((LandProperty)p).buildHouse();
                            return "You won a house on " + p.getPropertyName();
                        }
                }
            }
            else {
                // Gain property
                //?
            }
        }
    }
}
