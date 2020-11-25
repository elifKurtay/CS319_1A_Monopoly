package entities;

import card.TitleDeedCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Property {

    //properties
    private boolean mortgaged;
    private int numOfHouses;
    private boolean hotel;
    private TitleDeedCard card;
    //new variable, it did not exist in the class diagram
    private int value;
    private int[] rents;

    //constructors
    public Property(){}



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

