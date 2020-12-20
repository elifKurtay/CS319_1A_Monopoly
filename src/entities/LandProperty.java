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

    /**
     * Initializes the propertyname, value, mortgage value, rents, property group, house cost, hotel cost
     * @param propertyName
     * @param value
     * @param mortgageValue
     * @param rents
     * @param propertyGroup
     * @param houseCost
     * @param hotelCost
     */
    public LandProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup, int houseCost, int hotelCost) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        numOfHouses = 0;
    }

    /**
     * Calculates net worth
     * @return net worth
     */
    @Override
    public int getWorth() {
        if (numOfHouses == 5) {
            return super.getWorth() + houseCost * 4 + hotelCost;
        }
        else {
            return super.getWorth() + houseCost * numOfHouses;
        }
    }

    /**
     * Build house method
     */
    public void buildHouse() {
        if (numOfHouses <= 5) {
            numOfHouses++;
        }
    }

    //do we have sell? is this to be used in mortgaging?

    /**
     * Sell house method
     */
    public void sellHouse () {
        numOfHouses--;
    }

    /**
     * Returns the rent
     * @param playerToPay
     * @return rent
     */
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

    /**
     * Checks if an house or an hotel can be build on the screen.
     * @return boolean
     */
    public boolean canBuild() {
        ArrayList<Property> properties = owner.getAllPropertiesFromSameGroup(this);
        if (properties.size() == Property.numberOfPropertiesInGroups[propertyGroup]) {
            int houseSum = 0;
            for (Property p : properties) {
                houseSum += ((LandProperty) p).getNumOfHouses();
            }
            // subtract the floor of the average number of houses from the number of houses on this property
            // if the result is 0 a house can be built on this property
            return (numOfHouses - houseSum/Property.numberOfPropertiesInGroups[propertyGroup]) == 0;
        }
        return false;
    }

    /**
     * Checks if the house can be selled
     * @return boolean
     */
    public boolean canSellHouse() {
        ArrayList<Property> properties = owner.getAllPropertiesFromSameGroup(this);
        if (properties.size() != Property.numberOfPropertiesInGroups[propertyGroup]) {
            int houseSum = 0;
            for (Property p : properties) {
                houseSum += ((LandProperty) p).getNumOfHouses();
            }
            return (numOfHouses - 1 - houseSum/Property.numberOfPropertiesInGroups[propertyGroup]) == 0;
        }
        return false;
    }
}
