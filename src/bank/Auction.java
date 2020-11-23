package bank;

import entities.Player;
import entities.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Auction {
    private final Property auctionedProperty;

    private int highestBid;
    private Player highestBidder;

    public Auction(@NotNull Property auctionedProperty) {
        this.auctionedProperty = auctionedProperty;

        highestBid = 0;
        highestBidder = null;
    }

    void bid(@NotNull Player bidder, int bid) {
        if(bid > highestBid) {
            this.highestBid = bid;
            this.highestBidder = bidder;
        }
    }

    boolean closeAuction() {
        if(highestBid == 0)
            return false;

        //add property to highest bidder
        ArrayList<Property> properties = highestBidder.getProperties();
        properties.add(auctionedProperty);
        highestBidder.setProperties(properties);

        //get payment from highest bidder
        highestBidder.setMoney( highestBidder.getMoney() - highestBid);
        return true;
    }
}
