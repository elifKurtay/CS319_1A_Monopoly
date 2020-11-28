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
    private int value;

    //constructors
    public Property(TitleDeedCard card, int value){
        this.card = card;
        this.value = value;
        hotel = false;
        numOfHouses = 0;
        mortgaged = false;
    }


    //methods
    public void buildHouse() {
        numOfHouses++;
        if(numOfHouses == 5)
            hotel = true;
    }

    //do we have sell? is this to be used in mortgaging?
    public void sellHouse () {
        if(hotel)
            hotel = false;
        numOfHouses--;
    }

    public void mortgage () {
        mortgaged = true;
    }

    public void liftMortgage () {
        mortgaged = false;
    }
}

