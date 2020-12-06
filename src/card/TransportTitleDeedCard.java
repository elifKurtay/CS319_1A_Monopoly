package card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportTitleDeedCard extends TitleDeedCard{
    private int[] rents;
    private final int BASE_RENT = 25;

    public TransportTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        rents = new int[4];
        for (int i = 0; i < 4; i++) {
            rents[i] = BASE_RENT * ((int) Math.pow(2, i));
        }
    }

    public TransportTitleDeedCard(String propertyName, int mortgageValue, int[] rents) {
        super(propertyName, mortgageValue);
        this.rents = rents;
    }

    public int getRent(int index) {
        return rents[index];
    }
}
