package bank;

import entities.Player;
import entities.Property;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Trade {
    private final Player offerer;
    private final Player target;

    private ArrayList<Property> offeredProperties;
    private int offeredMoney;
    private ArrayList<Property> wantedProperties;
    private int wantedMoney;
    private int offeredGOOJC;
    private int wantedGOOJC;

    private boolean accepted;

    /**
     * Initalizes the trade by setting the players, offerer and target
     * @param offerer
     * @param target
     */
    public Trade( Player offerer, Player target ) {
        this.offerer = offerer;
        this.target = target;

        //default values
        offeredProperties = null;
        offeredMoney = 0;
        offeredGOOJC = 0;
        wantedProperties = null;
        wantedMoney = 0;
        wantedGOOJC = 0;
        accepted = false;
    }

    /**
     * Performs the offer by setting properties, money and goojc
     * @param properties
     * @param money
     * @param goojc
     */
    public void offer( ArrayList<Property> properties, int money, int goojc) {
        this.offeredGOOJC = goojc;
        this.offeredMoney = money;
        this.offeredProperties = properties;
    }

    /**
     * Performs the want by setting properties, money and goojc
     * @param properties
     * @param money
     * @param goojc
     */
    public void want( ArrayList<Property> properties, int money, int goojc) {
        this.wantedGOOJC = goojc;
        this.wantedMoney = money;
        this.wantedProperties = properties;
    }

    //UI functions
    void sendOffer() {} //?

    /**
     * Accepts the offer if the parameter is true
     * @param accepted
     */
    public void acceptOffer(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Closes the trade
     * @return
     */
    public boolean closeTrade() {
        if(!accepted)
            return false;

        //update offerers possesions
        ArrayList<Property> oldProperties = offerer.getProperties();
        oldProperties.addAll(wantedProperties);
        oldProperties.removeAll(offeredProperties);
        offerer.setProperties(oldProperties);

        for (Property p : wantedProperties) {
            p.setOwner(offerer);
        }

        offerer.setMoney(offerer.getMoney() + wantedMoney - offeredMoney);
        offerer.setGetOutOfJailFreeCount(offerer.getGetOutOfJailFreeCount()
               + wantedGOOJC - offeredGOOJC);

        //update target's possessions
        oldProperties = target.getProperties();
        oldProperties.removeAll(wantedProperties);
        oldProperties.addAll(offeredProperties);
        target.setProperties(oldProperties);

        for (Property p : offeredProperties) {
            p.setOwner(target);
        }

        target.setMoney(target.getMoney() - wantedMoney + offeredMoney);
        target.setGetOutOfJailFreeCount(target.getGetOutOfJailFreeCount()
                - wantedGOOJC + offeredGOOJC);

        return true;
    }
}
