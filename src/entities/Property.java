package entities;

import board.PropertySpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class Property implements Serializable {
    //properties
    private String propertyName;
    private int mortgageValue;
    private int value;
    protected boolean mortgaged;
    protected Player owner;
    protected int[] rents;
    protected int propertyGroup;
    private PropertySpace associatedPropertySpace;
    @Getter public static int[] numberOfPropertiesInGroups;

    /**
     * Initializes the property by setting the property name,
     * mortgage value, rents and property group.
     * @param propertyName
     * @param value
     * @param mortgageValue
     * @param rents
     * @param propertyGroup
     */
    //constructors
    public Property(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup){
        this.propertyName = propertyName;
        this.value = value;
        this.mortgageValue = mortgageValue;
        this.rents = new int[rents.length];
        for (int i = 0; i < rents.length; i++) {
            this.rents[i] = rents[i];
        }
        this.propertyGroup = propertyGroup;
        owner = null;
        mortgaged = false;
    }

    //methods
    /**
     * Sets the mortgage value true and owner's money to its
     * previous value plus the mortgage value of the property
     */
    public void mortgage () {
        mortgaged = true;
        owner.setMoney(owner.getMoney() + mortgageValue);
    }

    /**
     * Sets the mortgage value true and owner's money to its
     * previous value plus the mortgage value of the property
     */
    public void liftMortgage () {
        mortgaged = false;
        owner.setMoney(owner.getMoney() - (int) (mortgageValue * owner.getToken().getMortgageInterest()));
    }

    /**
     * Returns the worth of the property
     * @return value
     */
    public int getWorth() {
        if (mortgaged) {
            return mortgageValue;
        }
        else {
            return value;
        }
    }

    /**
     * Abstract method for calcualting rent
     * @param playerToPay
     * @return
     */
    public abstract int getRent(Player playerToPay);

    /**
     * Sets the number of properties in groups
     * @param numberOfPropertiesInGroups
     */
    public static void setNumberOfPropertiesInGroups(int[] numberOfPropertiesInGroups) {
        Property.numberOfPropertiesInGroups = new int[numberOfPropertiesInGroups.length];
        for (int i = 0; i <numberOfPropertiesInGroups.length; i++) {
            Property.numberOfPropertiesInGroups[i] = numberOfPropertiesInGroups[i];
        }
    }

    /**
     * Overrided toString method
     * @return string
     */
    public String toString() {
        return propertyName;
    }
}

