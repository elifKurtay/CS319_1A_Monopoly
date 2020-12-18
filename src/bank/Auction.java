package bank;

import entities.Player;
import entities.Property;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class Auction extends Observable{
    private final Property auctionedProperty;

    private int[] bids;
    private int highestBid;
    private Player highestBidder;

    public Auction(@NotNull Property auctionedProperty) {
        this.auctionedProperty = auctionedProperty;

        this.setState(1);
        highestBid = 0;
        highestBidder = null;
        bids = new int[4];
        for (int i = 0; i < 4; i++) {
            bids[i] = 0;
        }
    }

    void bid(@NotNull Player bidder, int bid, int playerNum) {
        bids[playerNum] = bid;
        if(bid > highestBid) {
            this.highestBid = bid;
            this.highestBidder = bidder;
        }
    }

    void fold(@NotNull Player bidder, int playerNum){
        bids[playerNum] = -1;
        int activeBidders = 0;
        for (int i = 0; i < 4; i++) {
            if (bids[i] > -1) activeBidders++;
        }

        if (activeBidders == 1){
            closeAuction();
        }
    }

    boolean closeAuction() {
        if(highestBid == 0)
            return false;

        setState(0);
        return true;
    }
}
