package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class HardStrategy implements PlayStrategy , Serializable {
    private final int MULTIPLIER = 3; //should this change according to strategies

    @Override
    public boolean shouldBuy(Property property, int money,
                             ArrayList<Property> properties) {
        if(properties != null) {
            //if player has mortgaged property, do not buy
            for (Property p : properties) {
                if (p.isMortgaged())
                    return false;
            }
        }
        //if you have a property from the same group and enough money, buy
        if(property.getValue() * MULTIPLIER < money && properties != null && properties.size() > 0) {
            return true;
        }
        //if you are rich, just buy it
        return property.getValue() * 5 < money;
    }

    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        if(money < poorLimit)
            return -1;
        if(money > highestBid * 2) {
            //be more eager if you have another property of the same color group
            int number = player.numberOfPropertiesFromSameGroup(property);
            if ( number > 1)
                return highestBid + 20;
            if( number > 0)
                return highestBid + 10;
        }
        if(highestBid < property.getValue() || money > (highestBid + 10) * MULTIPLIER)
            return highestBid + 10; //increment bid a fixed amount
        return -1; //fold
    }

    @Override
    public Property doMortgage(DigitalPlayer player) {
        int highestValue = 0;
        int index = -1, i = 0, limit = 1;
        ArrayList<Property> properties = player.getProperties();

        while(index < 0 && limit < 4) {
            for (Property p : properties) {
                //prioritize utility or transport properties
                if(p instanceof UtilityProperty || (p instanceof TransportProperty
                        && player.numberOfPropertiesFromSameGroup(p) < 3))
                    return p;
                if (player.numberOfPropertiesFromSameGroup(p) > limit)
                    continue;
                if (p.getMortgageValue() > highestValue) {
                    highestValue = p.getMortgageValue();
                    index = i;
                }
                i++;
            }
            limit++;
        }
        if(limit == 4)
            index = properties.size() - 1;
        return properties.get(index);
    }

    @Override
    public Property doRedeem(DigitalPlayer player) {
        int lowestValue = 0;
        int index = -1, i = 0;
        boolean notFound = false;
        ArrayList<Property> properties = player.getProperties();
        while (true) {
            for (Property p : properties) {
                if (p.isMortgaged()) {
                    if (player.numberOfPropertiesFromSameGroup(p) > 1) {
                        index = i;
                        break;
                    }
                    if (p instanceof TransportProperty && !notFound)
                        continue;
                    if (p.getValue() * 1.1 < lowestValue) {
                        lowestValue = p.getMortgageValue();
                        index = i;
                    }
                }
                i++;
            }
            if(index < 0)
                notFound = true;
            else break;
        }
        return properties.get(index);
    }

    @Override
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer) {
        int[] proposal = {-1, 0, 0, -1, 0 , 0};
        // 1 -> needs GOOJF card from other players
        if(tradeType == 1 && tradePlayer.getGetOutOfJailFreeCount() > 0) {
            //requests
            proposal[5] = 1; // 1 goofj card
            //offered
            proposal[1] = 7; // 7k offered
        }
        // 2 -> needs money, has lots of property
        else if (tradeType == 2 ) {
            Property offeredProperty = doMortgage(currentPlayer);
            if(currentPlayer.getGetOutOfJailFreeCount() > 0 ) {
                proposal[2] = 1;
            }
            if(currentPlayer.numberOfPropertiesFromSameGroup(offeredProperty) > 1) {
                for(Property p: currentPlayer.getProperties()){
                    if(p instanceof UtilityProperty || currentPlayer.numberOfPropertiesFromSameGroup(p) == 1) {
                        offeredProperty = p;
                        break;
                    }
                }
            }
            // request money with profit
            proposal[4] = offeredProperty.getWorth() + 30 + proposal[2];
            //offered property
            proposal[0] = currentPlayer.getProperties().indexOf(offeredProperty);


        }
        // 3 -> wants property
        else if(tradeType == 3 ) {
            for(Property p: tradePlayer.getProperties()){
                if(currentPlayer.numberOfPropertiesFromSameGroup(p) > 1
                        && tradePlayer.numberOfPropertiesFromSameGroup(p) == 1)
                    proposal[3] = tradePlayer.getProperties().indexOf(p);
            }
            proposal[1] = tradePlayer.getProperties().get(proposal[3]).getWorth() + 30;
        }
        return proposal;
    }

    @Override
    public int getMortgageLimit() {
        return 100;
    }

    @Override
    public int getRedeemLimit() {
        return 400;
    }

    @Override
    public int getPoorLimit() {
        return 500;
    }

    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        int gain = 0, loss = 0;
        for(Property p: trade.getOfferedProperties()) {
            gain += p.getWorth();
            if(player.numberOfPropertiesFromSameGroup(p) > 1)
                gain += p.getWorth() * 2;
        }
        for(Property p: trade.getWantedProperties()) {
            loss += p.getWorth();
            if(p instanceof UtilityProperty)
                loss += p.getWorth() * 1.5;
            else if(player.numberOfPropertiesFromSameGroup(p) > 1)
                loss += p.getWorth() * 2;
        }

        gain += trade.getOfferedMoney() + trade.getOfferedGOOJC() * 5;
        loss += trade.getWantedMoney() + trade.getWantedGOOJC() * 5;

        return gain >= loss * decoratorOffset;
    }

    public String toString(){
        return " Hard Strategy ";
    }
}
