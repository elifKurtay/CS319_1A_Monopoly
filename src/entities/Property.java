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
    public void buildHouse() {
        numOfHouses++;
    }

    public void buildHotel () {
        hotel = true;
    }

    public void sellHouse () {
        numOfHouses--;
    }

    public void sellHotel() {
        hotel = false;
    }

    /*//should not this method have a dice result parameter to calculate rent and the return type should be int or double
    public int calculateRent () {
        if(mortgaged)
            return 0;
        else {
            if(card instanceof LandTitleDeedCard){

            }
            else if(card instanceof UtilityTitleDeedCard){

            }
            else{

            }
        }

    }*/

    public void mortgage () {
        mortgaged = true;
    }

    public void liftMortgage () {
        mortgaged = false;
    }
}

