package entities;

import board.PropertySpace;
import board.Space;
import card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Player {

    //properties
    private String playerName;
    private int money;
    private Space currentSpace;
    private boolean bankrupt;
    private ArrayList<Property> properties;
    private int getOutOfJailFreeCount;
    private ArrayList<Card> postponedCard;
    private boolean jailed;
    private Token token;

    //constructor
    public Player() {}

    public Player(String playerName, int money, Space currentSpace, boolean bankrupt,
                  ArrayList<Property> properties, int getOutOfJailFreeCount,
                  ArrayList<Card> postponedCard, boolean jailed, Token token) {
        this.playerName = playerName;
        this.money = money;
        this.currentSpace = currentSpace;
        this.bankrupt = bankrupt;
        this.properties = new ArrayList<Property>(properties);
        this.getOutOfJailFreeCount = getOutOfJailFreeCount;
        this.postponedCard = new ArrayList<Card>(postponedCard);
        this.jailed = jailed;
        this.token = token;
    }

    //methods
    public void move(Space space){
        currentSpace = space;
    }

    public int getNetWorth(){
        int sumOfUnmortgagedProperty = 0;
        int sumOfMortgageValues = 0;
        int sumOfBuildings = 0;
        for(Property property: properties){
            if(property.isMortgaged())
                sumOfMortgageValues += property.getCard().getMortgageValue();
            else {
                sumOfUnmortgagedProperty += property.getValue();
                if(property.getCard() instanceof LandTitleDeedCard){
                    sumOfBuildings += property.getNumOfHouses() * ((LandTitleDeedCard) property.getCard()).getHouseCost();
                    if(property.isHotel())
                        sumOfBuildings += ((LandTitleDeedCard) property.getCard()).getHotelCost();
                }
            }
        }
        int netWorth = money + sumOfUnmortgagedProperty + sumOfMortgageValues + sumOfBuildings;
        return netWorth;
    }

    public void openPostponedCard(Card card){
        if(postponedCard.contains(card)) {
            card.open();
            postponedCard.remove(card);
        }
    }

    // Returns true if player is able to an has paid, false otherwise
    public boolean payPlayer(Player receiver, int amount){
        if (money >= amount) {
            receiver.setMoney(receiver.getMoney() + amount);
            money -= amount;
            return true;
        }
        return false;
    }

    public boolean ownsAllTitlesFromSameGroup(Property propertyToCheck) {
        int propertyGroupCardNo;
        int propertyGroup = ((LandTitleDeedCard) propertyToCheck.getCard()).getPropertyGroup();
        int count = 0;

        if (propertyGroup == 0 | propertyGroup == 7) {
            propertyGroupCardNo = 2;
        }
        else {
            propertyGroupCardNo = 3;
        }

        for (Property p : getProperties()) {
            TitleDeedCard card = p.getCard();
            if (card instanceof LandTitleDeedCard && ((LandTitleDeedCard) card).getPropertyGroup() == propertyGroup) {
                count += 1;
            }
        }

        return count == propertyGroupCardNo;
    }

    public int getTransportPropertyCount() {
        int count = 0;
        for (Property p: properties) {
            if (p.getCard() instanceof TransportTitleDeedCard) {
                count += 1;
            }
        }
        return count;
    }

    public int getUtilityPropertyCount() {
        int count = 0;
        for (Property p: properties) {
            if (p.getCard() instanceof UtilityTitleDeedCard) {
                count += 1;
            }
        }
        return count;
    }

    // Returns true if player is able to an has paid, false otherwise
    public boolean payBank(int amount) {
        if (money >= amount) {
            money = money - amount;
            return true;
        }
        return false;
    }

    public void goToJail() {
        jailed = true;
    }

    public void getOutOfJail() {
        jailed = false;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }
}

