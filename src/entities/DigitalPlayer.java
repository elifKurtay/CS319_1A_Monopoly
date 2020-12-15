package entities;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import board.PropertySpace;

@Getter
@Setter
public class DigitalPlayer extends Player{

    private PlayStrategy strategy;
    private int mortgageLimit; //lower limit
    private int redeemLimit; //upper limit
    private int poorLimit; //upper limit to be count as poor

    public DigitalPlayer(String name, PlayStrategy strategy) {
        super(name);
        this.strategy = strategy;
        mortgageLimit = strategy.getMortgageLimit();
        redeemLimit = strategy.getRedeemLimit();
        poorLimit = strategy.getPoorLimit();
    }

    //checks mortgage & redeem
    //trade decision on turn actions
    public boolean decideOnTradeAction(){
        //mortgage action
        while(getMoney() < mortgageLimit) {
            setMoney(getMoney() + strategy.doMortgage(getProperties()));
        }
        //redeem action
        while( hasMortgagedProperty() && getMoney() > redeemLimit) {
            strategy.doRedeem(getProperties());
        }

        if( decideOnTrade()) {
            return true;
        }
        return false;
    }

    //for build action
    //build on all
    public boolean decideOnBuildAction() {
        ArrayList<Property> propertyArrayList = getAllPropertiesFromSameGroup(((PropertySpace) getCurrentSpace())
                .getAssociatedProperty() );
        int totalCost = 0;

        for(Property p: propertyArrayList) {
            LandProperty landProperty = (LandProperty) p;
            if(landProperty.getNumOfHouses() == 5 ) //has hotel
                continue;
            if(landProperty.getNumOfHouses() == 4) //4 houses
            {
                if(getMoney() > 3* landProperty.getHotelCost()) {
                    landProperty.buildHouse();
                    totalCost += landProperty.getHotelCost();
                }
            } else { //less than 4 houses
                if(getMoney() > 3* landProperty.getHouseCost()) {
                    landProperty.buildHouse();
                    totalCost += landProperty.getHouseCost();
                }
            }
        }

        if(totalCost > 0) {
            setMoney(getMoney() - totalCost);
            return true;
        }
        return false;
    }

    public boolean decideOnBuy( Property property ) {
        if (strategy.shouldBuy(property, getMoney(), getProperties())) {
            this.addProperty(property);
            setMoney(getMoney() - property.getValue());
            return true;
        }
        return false;
    }

    public String[] getTradeProposal() {
        strategy.doTrade(); //return proposal[property list, money, card, property list, money, card]
        return null;
    }

    public int bidOnAuction(Property property, int highestBid){
        return strategy.getBid(property, highestBid, getMoney(), poorLimit, getProperties() );
    }

    //should trade be done this turn?
    //include strategy maybe?
    private boolean decideOnTrade(){
        return false;
    }

    private boolean hasMortgagedProperty() {
        for (Property p: getProperties() ) {
            if(p.isMortgaged())
                return true;
        }
        return false;
    }
}
