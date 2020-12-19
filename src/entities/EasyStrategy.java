package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class EasyStrategy implements PlayStrategy, Serializable{
    private final int MULTIPLIER = 3;

    @Override
    public boolean shouldBuy(Property property, int money,
                             ArrayList<Property> properties) {
        //just looks at money amount
        return property.getValue() * MULTIPLIER < money;
    }

    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        //only looks at money
        if(highestBid < property.getValue() && money > (highestBid + 20) * MULTIPLIER)
            return highestBid + 20; //increment bid a fixed amount
        return -1; //fold
    }

    @Override
    public Property doMortgage(DigitalPlayer player) {
        int highestValue = 0;
        int index = 0, i = 0;
        ArrayList<Property> properties = player.getProperties();
        for( Property p : properties) {
            if(p.getMortgageValue() > highestValue) {
                highestValue = p.getMortgageValue();
                index = i;
            }
            i++;
        }
        return properties.get(index);
    }

    @Override
    public Property doRedeem(DigitalPlayer player) {
        int lowestValue = 0;
        int index = 0, i = 0;
        ArrayList<Property> properties = player.getProperties();
        for( Property p : properties) {
            if(p.isMortgaged() && p.getValue() * 1.1 < lowestValue) {
                lowestValue = p.getMortgageValue();
                index = i;
            }
            i++;
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
            proposal[1] = 5; // 5k offered
        }
        // 2 -> needs money, has lots of property
        else if (tradeType == 2 ) {
            Property offeredProperty = doMortgage(currentPlayer);
            // request money with profit
            proposal[4] = offeredProperty.getWorth() + 10;
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

    /* how to make these not constant ? */
    @Override
    public int getMortgageLimit() {
        return 50;
    }

    @Override
    public int getRedeemLimit() {
        return 300;
    }

    @Override
    public int getPoorLimit() {
        return 200;
    }

    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        int gain = 0, loss = 0;
        for(Property p: trade.getOfferedProperties())
            gain += p.getWorth();
        for(Property p: trade.getWantedProperties())
            loss += p.getWorth();
        gain += trade.getOfferedMoney() + trade.getOfferedGOOJC() * 10;
        loss += trade.getWantedMoney() + trade.getWantedGOOJC() * 10;
        return gain >= loss * decoratorOffset;
    }

    public String toString(){
        return " Easy Strategy ";
    }
}
