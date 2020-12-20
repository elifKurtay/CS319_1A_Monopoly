package entities;

import bank.Trade;

import java.io.Serializable;
import java.util.ArrayList;

public class RiskyDecorator implements PlayStrategy, Serializable {

    private final PlayStrategy strategy;

    /**
     * Initializes by setting the play strategy
     * @param strategy
     */
    public RiskyDecorator(PlayStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Changes values randomly like a crazy person
     * @return random multiplier
     */
    private double randomMultiplier() {
        return Math.random() * 2;
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
        return strategy.shouldBuy(property, (int) (money * randomMultiplier()), properties);

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
        return strategy.getBid(property, highestBid, (int) (money * randomMultiplier()), (int) (poorLimit * randomMultiplier()), player);

    }

    /**
     * Performs the mortgage and returns the property.
     * @param player
     * @return property
     */
    @Override
    public Property doMortgage(DigitalPlayer player) {
        //sometimes do not want to sell even though it is needed
        if(Math.random() * 2 <= randomMultiplier())
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
        //sometimes do not want to redeem to keep money
        if(Math.random() * 2 > randomMultiplier())
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
        proposal[1] *= randomMultiplier();
        proposal[4] *= randomMultiplier();
        return proposal;
    }

    /**
     * Get mortgage limit
     * @return mortgage limit
     */
    @Override
    public int getMortgageLimit() {
        return (int) (strategy.getMortgageLimit() * randomMultiplier());
    }

    /**
     * Get redeem limit
     * @return redeem limit
     */
    @Override
    public int getRedeemLimit() {
        return (int) (strategy.getRedeemLimit() * randomMultiplier());
    }

    /**
     * Get poor limit
     * @return poor limit
     */
    @Override
    public int getPoorLimit() {
        return (int) (strategy.getPoorLimit() * randomMultiplier());
    }

    /**
     * Get trade answer
     * @return trade answer
     */
    @Override
    public boolean getTradeAnswer(Trade trade, DigitalPlayer player, double decoratorOffset) {
        return strategy.getTradeAnswer(trade, player, randomMultiplier());
    }

    /**
     * Overrided the toString method
     */
    public String toString(){
        return " Risky Decorator with " + strategy;
    }
}
