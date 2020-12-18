package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class RiskyDecorator implements PlayStrategy, Serializable {

    private final PlayStrategy strategy;

    public RiskyDecorator(PlayStrategy strategy) {
        this.strategy = strategy;
    }

    //changes values randomly like a crazy person
    private double randomMultiplier() {
        return Math.random() * 2;
    }

    @Override
    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties) {
        //acts like it has less money
        return strategy.shouldBuy(property, (int) (money * randomMultiplier()), properties);

    }

    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        //acts like it has less money and increases poor limit
        return strategy.getBid(property, highestBid, (int) (money * randomMultiplier()), (int) (poorLimit * randomMultiplier()), player);

    }

    @Override
    public Property doMortgage(DigitalPlayer player) {
        //sometimes do not want to sell even though it is needed
        if(Math.random() * 2 <= randomMultiplier())
            return null;
        return strategy.doMortgage(player);
    }

    @Override
    public Property doRedeem(DigitalPlayer player) {
        //sometimes do not want to redeem to keep money
        if(Math.random() * 2 > randomMultiplier())
            return null;
        return strategy.doRedeem(player);
    }

    @Override
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer) {
        int[] proposal = strategy.doTrade(tradeType, tradePlayer, currentPlayer);
        proposal[1] *= randomMultiplier();
        proposal[4] *= randomMultiplier();
        return proposal;
    }

    @Override
    public int getMortgageLimit() {
        return (int) (strategy.getMortgageLimit() * randomMultiplier());
    }

    @Override
    public int getRedeemLimit() {
        return (int) (strategy.getRedeemLimit() * randomMultiplier());
    }

    @Override
    public int getPoorLimit() {
        return (int) (strategy.getPoorLimit() * randomMultiplier());
    }
}
