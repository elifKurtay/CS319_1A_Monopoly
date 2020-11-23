package bank;

import entities.Player;
import entities.Property;

import java.util.ArrayList;

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

    void offer( ArrayList<Property> properties, int money, int goojc) {
        this.offeredGOOJC = goojc;
        this.offeredMoney = money;
        this.offeredProperties = properties;
    }

    void want( ArrayList<Property> properties, int money, int goojc) {
        this.wantedGOOJC = goojc;
        this.wantedMoney = money;
        this.wantedProperties = properties;
    }

    //UI functions
    void sendOffer() {} //?

    void acceptOffer() {
        accepted = true;
    }

    boolean closeTrade() {
        if(!accepted)
            return false;

        //update offerers possesions
        ArrayList<Property> oldProperties = offerer.getProperties();
        oldProperties.addAll(wantedProperties);
        oldProperties.removeAll(offeredProperties);
        offerer.setProperties(oldProperties);

        offerer.setMoney(offerer.getMoney() + wantedMoney - offeredMoney);
        offerer.setGetOutOfJailFreeCount(offerer.getGetOutOfJailFreeCount()
               + wantedGOOJC - offeredGOOJC);

        //update target's possessions
        oldProperties = target.getProperties();
        oldProperties.removeAll(wantedProperties);
        oldProperties.addAll(offeredProperties);
        target.setProperties(oldProperties);

        target.setMoney(target.getMoney() - wantedMoney + offeredMoney);
        target.setGetOutOfJailFreeCount(target.getGetOutOfJailFreeCount()
                - wantedGOOJC + offeredGOOJC);

        return true;
    }
}
