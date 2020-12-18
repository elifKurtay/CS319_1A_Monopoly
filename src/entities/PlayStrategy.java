package entities;

import java.util.ArrayList;

public interface PlayStrategy {

    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties);
    public int getBid(Property property, int highestBid, int money, int poorLimit, DigitalPlayer player);
    public Property doMortgage(DigitalPlayer player);
    public Property doRedeem(DigitalPlayer player);
    public int[] doTrade(int tradeType, Player tradePlayer, DigitalPlayer currentPlayer);
    public int getMortgageLimit();
    public int getRedeemLimit();
    public int getPoorLimit();
}
