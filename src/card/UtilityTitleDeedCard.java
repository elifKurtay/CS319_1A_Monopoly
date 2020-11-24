package card;

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

    public int[] getDiceMultipliers() {
        return diceMultipliers;
    }

    public void setDiceMultipliers(int[] diceMultipliers) {
        this.diceMultipliers = diceMultipliers;
    }
}
