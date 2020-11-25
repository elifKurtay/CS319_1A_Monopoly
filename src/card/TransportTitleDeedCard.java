package card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportTitleDeedCard extends TitleDeedCard{
    private int[] rent;
    private final int BASE_RENT = 25;

    public TransportTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        rent = new int[4];
        for (int i = 0; i < 4; i++) {
            rent[i] = BASE_RENT * ((int) Math.pow(2, i));
        }
    }

    public TransportTitleDeedCard(String propertyName, int mortgageValue, int[] rent){
        super(propertyName, mortgageValue);
        this.rent = rent;
    }
}
