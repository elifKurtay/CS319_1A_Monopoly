package bank;

import entities.Player;
import entities.Property;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class Auction extends Observable{
    private final Property auctionedProperty;

    private int highestBid;
    private Player highestBidder;

    public Auction(@NotNull Property auctionedProperty) {
        this.auctionedProperty = auctionedProperty;

        this.setState(1);
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

        setState(0);
        return true;
    }
}
