package board;
import entities.*;

public class PropertySpace extends Space{
    private enum PropertyType {
        LAND, TRANSPORT, UTILITY
    }

    private Player owner;
    private int value;
    private Property associatedProperty;
    private PropertyType type;

    public PropertySpace(String name, String propertyType, int value) {

        owner = null;
        // Need to associate PropertySpaces with Properties at instantiation
        associatedProperty = null;

        setName(name);
        this.value = value;

        if (propertyType.equals("LAND")) {
            this.type = PropertyType.LAND;
        }
        else if (propertyType.equals("TRANSPORT")) {
            this.type = PropertyType.TRANSPORT;
        }
        else {
            this.type = PropertyType.UTILITY;
        }
    }

    public boolean buySpace() {
        if (owner == null) {
            // Assuming payBank returns a boolean value, true if payment has succeeded
            Player p = getLatestPlayerOnSpace();
            if (p.payBank(value)) {
                owner = getLatestPlayerOnSpace();
                getLatestPlayerOnSpace().getProperties().add(associatedProperty);
                return true;
            }
        }
        return false;
    }

    public boolean payRent(int diceSum) {
        if (owner != null) {
            // Assuming the return value from payPlayer is boolean
            // Casting calculated rent to integer, may change later
            return getLatestPlayerOnSpace().payPlayer(owner, (int) calculateRent(diceSum));
        }
        return false;
    }

    //diceSum is the dice sum of the player that will pay the rent
    public double calculateRent(int diceSum) {
        Player p = getLatestPlayerOnSpace();
        int[] rents = associatedProperty.getCard().getRent();
        int rent;

        if (type == PropertyType.LAND) {
            if (associatedProperty.hasHotel()) {
                rent = rents[5];
            }
            else if (associatedProperty.getNumOfHouses() == 0) {
                rent = rents[0];
                if (owner.ownsAllTitlesFromSameGroup(associatedProperty)) {
                    rent *= 2;
                }
            }
            else {
                rent = rents[associatedProperty.getNumOfHouses()];
            }
        }
        else if (type == PropertyType.TRANSPORT) {
            rent = rents[owner.getTransportPropertyCount()];
        }
        else { // type == PropertyType.TRANSPORT
            rent = rents[owner.getUtilityPropertyCount()] * diceSum;
        }
        return rent * p.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier();
    }
}
