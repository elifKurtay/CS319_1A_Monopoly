package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class StingyDecorator implements PlayStrategy, Serializable {
    private final PlayStrategy strategy;
    private final double LOWER = 0.7;
    private final double HIGHER = 1.3;

    public StingyDecorator(PlayStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties) {
        //acts like it has less money
        return strategy.shouldBuy(property, (int) (money * LOWER), properties);
    }

    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        //acts like it has less money and increases poor limit
        return strategy.getBid(property, highestBid, (int) (money * LOWER), (int) (poorLimit * HIGHER), player);
    }

    @Override
    public Property doMortgage(DigitalPlayer player) {
        //sometimes do not want to sell even though it is needed
        if(Math.random() <= LOWER)
            return null;
        return strategy.doMortgage(player);
    }

    @Override
    public Property doRedeem(DigitalPlayer player) {
        //sometimes do not want to redeem to keep money
        if(Math.random() > LOWER)
            return null;
        return strategy.doRedeem(player);
    }

    @Override
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer) {
        int[] proposal = strategy.doTrade(tradeType, tradePlayer, currentPlayer);
        proposal[1] *= LOWER;
        proposal[4] *= HIGHER;
        return proposal;
    }

    @Override
    public int getMortgageLimit() {
        return (int) (strategy.getMortgageLimit() * HIGHER);
    }

    @Override
    public int getRedeemLimit() {
        return (int) (strategy.getRedeemLimit() * HIGHER);
    }

    @Override
    public int getPoorLimit() {
        return (int) (strategy.getPoorLimit() * HIGHER);
    }

    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        return strategy.getTradeAnswer(trade, player, HIGHER);
    }

    public String toString(){
        return " Stingy Decorator with " + strategy;
    }
}
