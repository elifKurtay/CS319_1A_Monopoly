package bank;

import entities.Player;
import entities.Property;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
@Setter
public class Bank {
    private ArrayList<Property> unownedProperties;

    private Auction onGoingAuction;
    private Trade onGoingTrade;

    public Bank() {
        unownedProperties = new ArrayList<>();
    }

    public Trade startTrade(Player offerer, Player target) {
        onGoingTrade = new Trade(offerer, target);
        return onGoingTrade;
    }

    public void finishTrade() {
        onGoingTrade.closeTrade();
        onGoingTrade = null;
    }

    public Auction startAuction( Property auctionedProperty ) {
        onGoingAuction = new Auction(auctionedProperty);
        return onGoingAuction;
    }

    public void finishAuction() {
        onGoingAuction.closeAuction();
        onGoingAuction = null;
    }

    public boolean removeFromUnownedProperties(Property property) {
        if(! unownedProperties.contains(property))
            return false;
        unownedProperties.remove( property);
        return true;
    }
}
