package entities;

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

    public Player(String playerName, int money, Space currentSpace, boolean bankrupt, ArrayList<Property> properties, int getOutOfJailFreeCount, ArrayList<Card> postponedCard, boolean jailed, Token token) {
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
                sumOfMortgageValues += property.getCard().mortgageValue;
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

    public void payPlayer(Player receiver, int amount){
        receiver.setMoney(amount);
        this.setMoney(money - amount);
    }

    public int calculateRent (Player receiver, int diceSum) {
        int rent = 0;
        if(currentSpace instanceof PropertySpace){
            Property p = currentSpace.getAssociatedProperty();
            if(p.getCard() instanceof TransportTitleDeedCard){
                int[] rentArray = ((TransportTitleDeedCard) p.getCard()).getRent();
                if(p.getNumOfHouses() == 0)
                    rent = rentArray[0];
                else if(p.getNumOfHouses() == 1)
                    rent = rentArray[1];
                else if(p.getNumOfHouses() == 2)
                    rent = rentArray[2];
                else
                    rent = rentArray[3];
            }
            else{
                boolean ownsAllTitlesFromSameGroup = ownsAllTitlesFromSameGroup(receiver, p);
                if (p.getCard() instanceof LandTitleDeedCard){
                    int[] rentArray = ((LandTitleDeedCard) p.getCard()).getRent();
                    if(p.getNumOfHouses() == 0)
                        rent = rentArray[0];
                    else if(p.getNumOfHouses() == 1)
                        rent = rentArray[1];
                    else if(p.getNumOfHouses() == 2)
                        rent = rentArray[2];
                    else if(p.getNumOfHouses() == 3)
                        rent = rentArray[3];
                    else if(p.getNumOfHouses() == 4)
                        rent = rentArray[4];
                    else if(p.isHotel())
                        rent = rentArray[5];
                    if(ownsAllTitlesFromSameGroup)
                        rent *= 2;
                }
                else{
                    int[] diceMultipliers = ((UtilityTitleDeedCard) p.getCard()).getDiceMultipliers();
                    if(ownsAllTitlesFromSameGroup)
                        rent = diceMultipliers[1] * diceSum;
                    else
                        rent = diceMultipliers[0] * diceSum;
                }
            }
        }
        return (int) Math.round(rent * token.getRentPayMultiplier() * token.getRentPayMultiplier());
    }

    public boolean ownsAllTitlesFromSameGroup(Player player, Property propertyToCheck) {
        int maxNoOfAvailableCardsFromSameGroup = 0;
        if(propertyToCheck.getCard() instanceof LandTitleDeedCard){
            int propertyGroup = ((LandTitleDeedCard) propertyToCheck.getCard()).getPropertyGroup();
            if(propertyGroup == 0 | propertyGroup == 7)
                maxNoOfAvailableCardsFromSameGroup = 2;
            else
                maxNoOfAvailableCardsFromSameGroup = 3;
            for(Property p: player.getProperties()){
                if(p.getCard() instanceof LandTitleDeedCard){
                    if(((LandTitleDeedCard) p.getCard()).getPropertyGroup() == propertyGroup)
                        maxNoOfAvailableCardsFromSameGroup--;
                }
            }

        }
        else if(propertyToCheck.getCard() instanceof UtilityTitleDeedCard) {
            maxNoOfAvailableCardsFromSameGroup = 2;
            for (Property p : player.getProperties()) {
                if(p.getCard() instanceof UtilityTitleDeedCard)
                    maxNoOfAvailableCardsFromSameGroup--;
            }
        }
        if(maxNoOfAvailableCardsFromSameGroup != 0)
            return false;
        else
            return true;
    }

    //what should happen if there is not enough money
    public void payBank(int amount){
        money = money - amount;
    }

    public void goToJail() {
        jailed = true;
    }

    public void getOutOfJail() {
        jailed = false;
    }
}

