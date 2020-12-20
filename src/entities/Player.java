package entities;

import board.Space;
import card.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Player implements Serializable {

    //properties
    private String playerName;
    private int money;
    private Space currentSpace;
    private boolean bankrupt; //or forfeit
    private ArrayList<Property> properties;
    private int getOutOfJailFreeCount;
    private ArrayList<Card> postponedCards;
    private boolean jailed;
    private Token token;
    private int jailedLapCount;
    private int currentDiceSum;

    //constructor

    /**
     * Initializes the player
     * @param playerName
     */
    public Player(String playerName) {
        this.playerName = playerName;
        money = 1500;
        currentSpace = null;
        bankrupt = false;
        properties = new ArrayList<>();
        getOutOfJailFreeCount = 0;
        postponedCards = new ArrayList<>();
        jailed = false;
        token = null;
        jailedLapCount = 0;
        currentDiceSum = 0;
    }

    //methods

    /**
     * Calculates net worth
     * @return net worth
     */
    public int getNetWorth(){
        if (bankrupt) {
            return 0;
        }

        int netWorth = money;
        for(Property property: properties){
            netWorth += property.getWorth();
        }
        return netWorth;
    }

    /**
     * Sets the attributes of the player when he is lost
     * @param count
     */
    public void lost(int count) {
        money = count;
        bankrupt = true;
        jailed = false;
        currentSpace = null;
        getOutOfJailFreeCount = 0;
        postponedCards = new ArrayList<>();
        jailedLapCount = 0;
        currentDiceSum = 0;
        for(Property p: properties)
            p.setOwner(null);
        properties = new ArrayList<>();
    }

    /**
     * Pays the player the amount given
     * @param receiver
     * @param amount
     */
    public void payPlayer(@NotNull Player receiver, int amount){
        receiver.setMoney(receiver.getMoney() + amount);
        money = money - amount;
    }

    /**
     * Returns all properties from the same group of the given property
     * @param propertyToCheck
     * @return
     */
    public ArrayList<Property> getAllPropertiesFromSameGroup( Property propertyToCheck) {
        ArrayList<Property> titles = new ArrayList<>();

        int group = propertyToCheck.getPropertyGroup();
        for (Property p : properties) {
            if (p.getPropertyGroup() == group) {
                titles.add(p);
            }
        }
        return titles;
    }

    /**
     *
     * @param propertyToCheck
     * @return
     */
    public int numberOfPropertiesFromSameGroup( Property propertyToCheck) {
        int[] propertyGroupCounts = new int[10];
        for (Property p : properties) {
            propertyGroupCounts[p.getPropertyGroup()] += 1;
        }

        return propertyGroupCounts[propertyToCheck.getPropertyGroup()];
    }

    /**
     * Checks if the player owns all the properties from that group
     * @param propertyToCheck
     * @return boolean
     */
    public boolean ownsAllPropertiesFromSameGroup(Property propertyToCheck) {
        return numberOfPropertiesFromSameGroup(propertyToCheck) == Property.numberOfPropertiesInGroups[propertyToCheck.getPropertyGroup()];
    }

    /**
     * Adds the property to the property list
     * @param property
     * @return boolean
     */
    public boolean addProperty(Property property) {
        if(properties.contains(property))
            return false;
        properties.add(property);
        return true;
    }

    /**
     * Resets the attributes of the player
     */
    public void reset() {
        money = 1500;
        bankrupt = false;
        for(Property p: properties) {
            p.setOwner(null);
        }
        properties = new ArrayList<>();
        getOutOfJailFreeCount = 0;
        postponedCards = new ArrayList<>();
        jailed = false;
        jailedLapCount = 0;
        currentDiceSum = 0;
    }

    /**
     * Returns the properties of the player
     * @return properties
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Overrided toString method
     * @return string
     */
    @Override
    public String toString() {
        return playerName;
    }
}

