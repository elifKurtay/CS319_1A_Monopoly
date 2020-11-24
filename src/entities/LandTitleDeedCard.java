package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LandTitleDeedCard extends TitleDeedCard{

    //properties
    private int propertyGroup;
    private int[] rent;
    private int houseCost;
    private int hotelCost;

    //constructors

    public LandTitleDeedCard(){}

    public LandTitleDeedCard(int propertyGroup, int[] rent, int houseCost, int hotelCost) {
        this.propertyGroup = propertyGroup;
        this.rent = new int[6];
        for(int i = 0; i < 6; i++){
            this.rent[i] = rent[i];
        }
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
    }

    //methods
    public void setRent(int[] rent) {
        for(int i = 0; i < 6; i++)
            this.rent[i] = rent[i];
    }
}