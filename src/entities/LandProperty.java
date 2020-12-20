package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class LandProperty extends Property {
    private int numOfHouses;
    private int houseCost;
    private int hotelCost;

    public LandProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup, int houseCost, int hotelCost) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        numOfHouses = 0;
    }

    @Override
    public int getWorth() {
        if (numOfHouses == 5) {
            return super.getWorth() + houseCost * 4 + hotelCost;
        }
        else {
            return super.getWorth() + houseCost * numOfHouses;
        }
    }

    public void buildHouse() {
        if (numOfHouses <= 5) {
            numOfHouses++;
        }
    }

    //do we have sell? is this to be used in mortgaging?
    public void sellHouse () {
        numOfHouses--;
    }

    @Override
    public int getRent(Player playerToPay) {
        int numberOfTitlesFromSameGroup = owner.numberOfPropertiesFromSameGroup(this);
        int rent;
        if (numOfHouses == 0 && numberOfTitlesFromSameGroup == numberOfPropertiesInGroups[propertyGroup]) {
            rent = rents[0] * 2;
        }
        else {
            rent = rents[numOfHouses];
        }

        return (int) (rent * playerToPay.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier());
    }

    public boolean canBuild() {
        ArrayList<Property> properties = owner.getAllPropertiesFromSameGroup(this);
        int houseSum = 0;
        for (Property p : properties) {
            houseSum += ((LandProperty) p).getNumOfHouses();
        }
        // subtract the floor of the average number of houses from the number of houses on this property
        // if the result is 0 a house can be built on this property
        return (numOfHouses - houseSum/Property.numberOfPropertiesInGroups[propertyGroup]) == 0;
    }

    public boolean canSellHouse() {
        ArrayList<Property> properties = owner.getAllPropertiesFromSameGroup(this);
        int houseSum = 0;
        for (Property p : properties) {
            houseSum += ((LandProperty) p).getNumOfHouses();
        }
        return (numOfHouses - 1 - houseSum/Property.numberOfPropertiesInGroups[propertyGroup]) == 0;
    }
}
