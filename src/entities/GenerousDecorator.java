package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class GenerousDecorator implements PlayStrategy, Serializable {
    private PlayStrategy strategy;
    private final double LOWER = 0.7;
    private final double HIGHER = 1.3;

    public GenerousDecorator(PlayStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties) {
        //acts like it has less money
        return strategy.shouldBuy(property, (int) (money * HIGHER), properties);

    }

    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        //acts like it has less money and increases poor limit
        return strategy.getBid(property, highestBid, (int) (money * HIGHER), (int) (poorLimit * LOWER), player);
    }

    @Override
    public Property doMortgage(DigitalPlayer player) {
        if(Math.random() > LOWER)
            return null;
        return strategy.doMortgage(player);
    }

    @Override
    public Property doRedeem(DigitalPlayer player) {
        //more likely to redeem
        if(Math.random() <= LOWER)
            return null;
        return strategy.doRedeem(player);
    }

    @Override
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer) {
        int[] proposal = strategy.doTrade(tradeType, tradePlayer, currentPlayer);
        proposal[1] *= HIGHER;
        proposal[4] *= LOWER;
        return proposal;
    }

    @Override
    public int getMortgageLimit() {
        return (int) (strategy.getMortgageLimit() * LOWER);
    }

    @Override
    public int getRedeemLimit() {
        return (int) (strategy.getRedeemLimit() * LOWER);
    }

    @Override
    public int getPoorLimit() {
        return (int) (strategy.getPoorLimit() * LOWER);
    }

    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        return strategy.getTradeAnswer(trade, player, LOWER);
    }

    public String toString(){
        return " Generous Decorator with " + strategy;
    }
}
