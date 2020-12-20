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
    public int getNetWorth(){
        int netWorth = money;
        for(Property property: properties){
            netWorth += property.getWorth();
        }
        return netWorth;
    }

    public void lost() {
        money = 0;
        bankrupt = true;
        jailed = false;
        currentSpace = null;
        getOutOfJailFreeCount = 0;
        postponedCards = null;
        jailedLapCount = 0;
        currentDiceSum = 0;
        for(Property p: getProperties())
            p.setOwner(null);
        properties = null;
    }

    public void payPlayer(@NotNull Player receiver, int amount){
        receiver.setMoney(receiver.getMoney() + amount);
        money = money - amount;
    }

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

    public int numberOfPropertiesFromSameGroup( Property propertyToCheck) {
        int[] propertyGroupCounts = new int[10];
        for (Property p : properties) {
            propertyGroupCounts[p.getPropertyGroup()] += 1;
        }

        return propertyGroupCounts[propertyToCheck.getPropertyGroup()];
    }

    public boolean ownsAllPropertiesFromSameGroup(Property propertyToCheck) {
        return numberOfPropertiesFromSameGroup(propertyToCheck) == Property.numberOfPropertiesInGroups[propertyToCheck.getPropertyGroup()];
    }
    /*
    //what should happen if there is not enough money
    public boolean payBank(int amount) {
        money = money - amount;
        return true;
    }
     */

    public boolean addProperty(Property property) {
        if(properties.contains(property))
            return false;
        properties.add(property);
        return true;
    }

    public void reset() {
        money = 1500;
        currentSpace = null;
        bankrupt = false;
        for(Property p: properties) {
            p.setOwner(null);
        }
        properties = new ArrayList<>();
        getOutOfJailFreeCount = 0;
        postponedCards = new ArrayList<>();
        jailed = false;
        token = null;
        jailedLapCount = 0;
        currentDiceSum = 0;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return playerName;
    }
}

