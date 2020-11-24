package entities;

import card.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TitleDeedCard extends Card {

    //properties
    private String propertyName;
    private int mortgageValue;

    //constructors
    public TitleDeedCard() {}

    public TitleDeedCard(String propertyName, int mortgageValue) {
        this.propertyName = propertyName;
        this.mortgageValue = mortgageValue;
    }

    //methods
}
