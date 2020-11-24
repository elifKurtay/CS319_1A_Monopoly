package entities;

import lombok.Getter;

@Getter
public class TransportTitleDeedCard extends TitleDeedCard{

    //properties
    private int[] rent;

    //constructors
    public TransportTitleDeedCard() {
        rent = new int[4];
    }

    public TransportTitleDeedCard(String propertyName, int mortgageValue, int[] rent) {
        super(propertyName, mortgageValue);
        this.rent = new int[4];
        for(int i = 0; i < 4; i++)
            this.rent[i] = rent[i];
    }

    //methods
    public void setRent(int[] rent) {
        for(int i = 0; i < 4; i++)
            this.rent[i] = rent[i];
    }
}
