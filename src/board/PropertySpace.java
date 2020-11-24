package board;
import entities.Player;
import entities.Property;

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
/*
    public boolean buySpace() {
        if (owner == null) {
            // Assuming payBank returns a boolean value, true if payment has succeeded
            if (getLatestPlayerOnSpace().payBank(value)) {
                owner = getLatestPlayerOnSpace();
                getLatestPlayerOnSpace().getProperties().add(associatedProperty);
                return true;
            }
        }
        return false;
    }

    public boolean payRent() {
        if (owner != null) {
            // Assuming the return value from payPlayer is boolean
            return getLatestPlayerOnSpace().payPlayer(owner, calculateRent());
        }
        return false;
    }

    public double calculateRent() {
        //return getLatestPlayerOnSpace().getToken().getRentPayMultiplier()
          //      * (owner.getToken().getRentCollectMultiplier() * associatedProperty.calculateRent());
        //diceSum is the dice sum of the player that will pay the rent
        return getLatestPlayerOnSpace().calculateRent(owner, diceSum);
    } */
}
