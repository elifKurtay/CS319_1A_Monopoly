package entities;

import board.GoSpace;
import board.Space;
import card.Card;
import card.LandTitleDeedCard;
import card.TransportTitleDeedCard;
import card.UtilityTitleDeedCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class Player {

    //properties
    private String playerName;
    private int money; //default 200M ?
    private Space currentSpace;
    private boolean bankrupt;
    private ArrayList<Property> properties;
    private int getOutOfJailFreeCount;
    private ArrayList<Card> postponedCard;
    private boolean jailed;
    private Token token;
    private int jailedLapCount;

    //constructor
    public Player() {};

    public Player(String playerName) {
        this.playerName = playerName;
        money = 200;
        currentSpace = new GoSpace();
        bankrupt = false;
        properties = null;
        getOutOfJailFreeCount = 0;
        postponedCard = null;
        jailed = false;
        token = null;
        jailedLapCount = 0;
    }

    //methods
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

    public void payPlayer(@NotNull Player receiver, int[] dice){
        int amount = calculateRent(receiver, dice[0] + dice[1]);
        receiver.setMoney(receiver.getMoney() + amount);
        money = money - amount;
        System.out.println("Your rent amount: " + amount);
    }

    public void payPlayer(@NotNull Player receiver, int amount){
        receiver.setMoney(receiver.getMoney() + amount);
        money = money - amount;
    }

    public int calculateRent(Player receiver, int diceSum) {
        int rent = 0;
        Property p = currentSpace.getAssociatedProperty();
        if (p.getCard() instanceof TransportTitleDeedCard) {
            rent = ((TransportTitleDeedCard) p.getCard()).getRent( numberOfTitlesFromSameGroup(p) - 1 );
        } else {
            boolean ownsAllTitlesFromSameGroup = ownsAllTitlesFromSameGroup(receiver, p);
            if (p.getCard() instanceof LandTitleDeedCard) {
                rent = ((LandTitleDeedCard) p.getCard()).getRent(p);
                if ( p.getNumOfHouses() == 0 && ownsAllTitlesFromSameGroup)
                    rent *= 2;
            } else {
                int[] diceMultipliers = ((UtilityTitleDeedCard) p.getCard()).getDiceMultipliers();
                if (ownsAllTitlesFromSameGroup)
                    rent = diceMultipliers[1] * diceSum;
                else
                    rent = diceMultipliers[0] * diceSum;
            }
        }
        return (int) Math.round(rent * token.getRentPayMultiplier() * receiver.getToken().getRentCollectMultiplier());
    }

    public static boolean ownsAllTitlesFromSameGroup( Player player, Property propertyToCheck) {
        int maxNoOfAvailableCardsFromSameGroup = 0;
        if (propertyToCheck.getCard() instanceof LandTitleDeedCard) {
            int propertyGroup = ((LandTitleDeedCard) propertyToCheck.getCard()).getPropertyGroup();
            if (propertyGroup == 0 | propertyGroup == 7)
                maxNoOfAvailableCardsFromSameGroup = 2;
            else
                maxNoOfAvailableCardsFromSameGroup = 3;
            for (Property p : player.getProperties()) {
                if (p.getCard() instanceof LandTitleDeedCard) {
                    if (((LandTitleDeedCard) p.getCard()).getPropertyGroup() == propertyGroup)
                        maxNoOfAvailableCardsFromSameGroup--;
                }
            }

        } else if (propertyToCheck.getCard() instanceof UtilityTitleDeedCard) {
            maxNoOfAvailableCardsFromSameGroup = 2;
            for (Property p : player.getProperties()) {
                if (p.getCard() instanceof UtilityTitleDeedCard)
                    maxNoOfAvailableCardsFromSameGroup--;
            }
        }
        return maxNoOfAvailableCardsFromSameGroup == 0;
    }

    public ArrayList<Property> getAllTitlesFromSameGroup( Property propertyToCheck) {
        ArrayList<Property> tiles = new ArrayList<Property>();
        if (propertyToCheck.getCard() instanceof LandTitleDeedCard) {
            int propertyGroup = ((LandTitleDeedCard) propertyToCheck.getCard()).getPropertyGroup();
            for (Property p : properties) {
                if (p.getCard() instanceof LandTitleDeedCard) {
                    if (((LandTitleDeedCard) p.getCard()).getPropertyGroup() == propertyGroup)
                        tiles.add(p);
                }
            }
        } else if (propertyToCheck.getCard() instanceof UtilityTitleDeedCard) {
            for (Property p : properties) {
                if (p.getCard() instanceof UtilityTitleDeedCard)
                    tiles.add(p);;
            }
        }
        return tiles;
    }

    public int numberOfTitlesFromSameGroup( Property propertyToCheck) {
        int number = 0;
        int propertyGroup = 0;
        if (propertyToCheck.getCard() instanceof LandTitleDeedCard)
            propertyGroup = ((LandTitleDeedCard) propertyToCheck.getCard()).getPropertyGroup();

        for (Property p : properties) {
            if (propertyToCheck.getCard() instanceof LandTitleDeedCard
                    && p.getCard() instanceof LandTitleDeedCard &&
                    ((LandTitleDeedCard) p.getCard()).getPropertyGroup() == propertyGroup )
                    number++;
            else if(propertyToCheck.getCard() instanceof UtilityTitleDeedCard
                    && p.getCard() instanceof UtilityTitleDeedCard)
                number++;
            else if(propertyToCheck.getCard() instanceof TransportTitleDeedCard
                    && p.getCard() instanceof TransportTitleDeedCard)
                number++;
        }
        return number;
    }

    //what should happen if there is not enough money
    public boolean payBank(int amount) {
        money = money - amount;
        return true;
    }

    public boolean addProperty(Property property) {
        if(properties.contains(property))
            return false;
        properties.add(property);
        return true;
    }

    public void reset() {
        money = 200;
        currentSpace = new GoSpace();
        bankrupt = false;
        properties = null;
        getOutOfJailFreeCount = 0;
        postponedCard = null;
        jailed = false;
        token = null;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }
}

