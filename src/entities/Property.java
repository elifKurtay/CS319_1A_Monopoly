package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Property {
    //properties
    private String propertyName;
    private int mortgageValue;
    private int value;
    protected boolean mortgaged;
    protected Player owner;
    protected int[] rents;
    protected int propertyGroup;
    protected static int[] numberOfPropertiesInGroups;

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
    public void mortgage () {
        mortgaged = true;
    }

    public void liftMortgage () {
        mortgaged = false;
    }

    public int getWorth() {
        if (mortgaged) {
            return mortgageValue;
        }
        else {
            return value;
        }
    }

    public abstract int getRent(Player playerToPay);

    public static void setNumberOfPropertiesInGroups(int[] numberOfPropertiesInGroups) {
        Property.numberOfPropertiesInGroups = new int[numberOfPropertiesInGroups.length];
        for (int i = 0; i <numberOfPropertiesInGroups.length; i++) {
            Property.numberOfPropertiesInGroups[i] = numberOfPropertiesInGroups[i];
        }
    }

}

