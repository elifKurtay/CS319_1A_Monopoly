package entities;

import java.util.ArrayList;

public interface PlayStrategy {

    public boolean shouldBuy(Property property, int money, ArrayList<Property> properties);
    public int getBid(Property property, int highestBid, int money, int poorLimit, ArrayList<Property> properties );
    public int doMortgage(ArrayList<Property> properties);
    public void doRedeem(ArrayList<Property> properties);
    public void doTrade();
    public int getMortgageLimit();
    public int getRedeemLimit();
    public int getPoorLimit();
}
