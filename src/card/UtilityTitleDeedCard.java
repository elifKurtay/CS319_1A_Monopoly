package card;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UtilityTitleDeedCard extends TitleDeedCard{

    private int[] diceMultipliers;

    public UtilityTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        diceMultipliers = new int[2];
    }

    public UtilityTitleDeedCard(String propertyName, int mortgageValue, int[] diceMultipliers) {
        super(propertyName, mortgageValue);
        this.diceMultipliers = diceMultipliers;
    }

}
