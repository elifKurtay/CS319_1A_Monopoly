package card;

import static java.lang.Math.pow;

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

    public int[] getRent() {
        return rent;
    }

    public void setRent(int[] rent) {
        this.rent = rent;
    }
}
