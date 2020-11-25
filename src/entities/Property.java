package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Property {

    //properties
    private boolean mortgaged;
    private int numOfHouses;
    private boolean hotel;
    private TitleDeedCard card;
    //new variable, it did not exist in the class diagram
    private int value;

    //constructors
    public Property(){}

    public Property(boolean mortgaged, int numOfHouses, boolean hotel, TitleDeedCard card, int value) {
        this.mortgaged = mortgaged;
        this.numOfHouses = numOfHouses;
        this.hotel = hotel;
        this.card = card;
        this.value = value;
    }

    //methods
    public boolean buildHouse() {
        if (numOfHouses != 4) {
            if (owner.getMoney() >= ((LandTitleDeedCard) card).getHouseCost() * owner.getToken().getBuildingCostMultiplier()) {
                numOfHouses++;
                owner.setMoney(owner.getMoney() - ((LandTitleDeedCard) card).getHouseCost() * owner.getToken().getBuildingCostMultiplier());
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean buildHotel () {
        if (!hotel && numOfHouses == 4) {
            if (owner.getMoney() >= ((LandTitleDeedCard) card).getHotelCost() * owner.getToken().getBuildingCostMultiplier()) {
                hotel = true;
                owner.setMoney(owner.getMoney() - ((LandTitleDeedCard) card).getHotelCost() * owner.getToken().getBuildingCostMultiplier());
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean hasHotel() {
        return hotel;
    }

    public boolean sellHouse () {
        if (numOfHouses > 0) {
            numOfHouses--;
            owner.setMoney(owner.getMoney() + (((LandTitleDeedCard) card).getHouseCost() * owner.getToken().getBuildingCostMultiplier()) / 2);
            return true;
        }
        return false;
    }

    public boolean sellHotel() {
        if (hotel) {
            hotel = false;
            owner.setMoney(owner.getMoney() + (((LandTitleDeedCard) card).getHotelCost() * owner.getToken().getBuildingCostMultiplier()) / 2);
            return true;
        }
        return false;
    }

    public boolean mortgage () {
        if (!mortgaged) {
            mortgaged = true;
            owner.setMoney(owner.getMoney() + ((LandTitleDeedCard) card).getMortgageValue());
            return true;
        }
        return false;
    }

    public boolean liftMortgage () {
        if (mortgaged) {
            if (owner.getMoney() >= ((LandTitleDeedCard) card).getMortgageValue() * owner.getToken().getMortgageInterest()) {
                mortgaged = false;
                owner.setMoney(owner.getMoney() + ((LandTitleDeedCard) card).getMortgageValue());
                return true;
            }
            return false;
        }
        return false;
    }
}

