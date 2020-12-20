package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class GenerousDecorator implements PlayStrategy, Serializable {
    private PlayStrategy strategy;
    private final double LOWER = 0.7;
    private final double HIGHER = 1.3;

    /**
     * Initializes by setting the play strategy
     * @param strategy
     */
    public GenerousDecorator(PlayStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Decides to buy the given property
     * @param property
     * @param money
     * @param properties
     * @return true or false
     */
    @Override
    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties) {
        //acts like it has less money
        return strategy.shouldBuy(property, (int) (money * HIGHER), properties);

    }

    /**
     * Returns the bid amount
     * @param property
     * @param highestBid
     * @param money
     * @param poorLimit
     * @param player
     * @return bid
     */
    @Override
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player) {
        //acts like it has less money and increases poor limit
        return strategy.getBid(property, highestBid, (int) (money * HIGHER), (int) (poorLimit * LOWER), player);
    }

    /**
     * Performs the mortgage and returns the property.
     * @param player
     * @return property
     */
    @Override
    public Property doMortgage(DigitalPlayer player) {
        if(Math.random() > LOWER)
            return null;
        return strategy.doMortgage(player);
    }

    /**
     * Performs the redeem and returns the property.
     * @param player
     * @return property
     */
    @Override
    public Property doRedeem(DigitalPlayer player) {
        //more likely to redeem
        if(Math.random() <= LOWER)
            return null;
        return strategy.doRedeem(player);
    }

    /**
     * Performs the trade and returns trade information
     * @param tradeType
     * @param tradePlayer
     * @param currentPlayer
     * @return proposal
     */
    @Override
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer) {
        int[] proposal = strategy.doTrade(tradeType, tradePlayer, currentPlayer);
        proposal[1] *= HIGHER;
        proposal[4] *= LOWER;
        return proposal;
    }

    /**
     * Get mortgage limit
     * @return mortgage limit
     */
    @Override
    public int getMortgageLimit() {
        return (int) (strategy.getMortgageLimit() * LOWER);
    }

    /**
     * Get redeem limit
     * @return redeem limit
     */
    @Override
    public int getRedeemLimit() {
        return (int) (strategy.getRedeemLimit() * LOWER);
    }

    /**
     * Get poor limit
     * @return poor limit
     */
    @Override
    public int getPoorLimit() {
        return (int) (strategy.getPoorLimit() * LOWER);
    }

    /**
     * Get trade answer
     * @return trade answer
     */
    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        return strategy.getTradeAnswer(trade, player, LOWER);
    }

    /**
     * Overrided the toString method
     */
    public String toString(){
        return " Generous Decorator with " + strategy;
    }
}
