package board;

import entities.LandTitleDeedCard;
import entities.Player;
import entities.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class WheelOfFortuneSpace extends Space {

    public void spinWheel() {
        Player p = getLatestPlayerOnSpace();
        int random = (int) Math.random() * 100;
        if (random < 10) {
            // Lose property
        }
        else if (random < 25) {
            // Lose building on a property if possible
            ArrayList<Property> properties = new ArrayList<>(p.getProperties());
            Collections.shuffle(properties);

            for (Property prop: properties) {
                if (prop.getNumOfHouses() > 0) {
                    if (prop.hasHotel()) {
                        prop.sellHotel();
                        p.setMoney(p.getMoney() - ((LandTitleDeedCard) prop.getCard()).getHotelCost());
                    }
                    else  {
                        prop.sellHouse();
                        p.setMoney(p.getMoney() - ((LandTitleDeedCard) prop.getCard()).getHouseCost());
                    }
                }
            }
        }
        else if (random < 50) {
            // Lose some amount of money
            p.setMoney((int) (p.getMoney() - 200 * Math.random()));
        }
        else if (random < 55) {
            // Gain a get out of jail free card
        }
        else if (random < 75) {
            // Gain some amount of money
            p.setMoney((int) (p.getMoney() + 200 * Math.random()));
        }
        else if (random < 90) {
            // Gain a building on a property if possible
        }
        else {
            // Gain property
        }
    }
}
