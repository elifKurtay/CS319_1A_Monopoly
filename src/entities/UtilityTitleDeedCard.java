package entities;

import lombok.Getter;

@Getter
public class UtilityTitleDeedCard extends TitleDeedCard{

    //properties
    private int[] diceMultipliers;

    //constructors
    public UtilityTitleDeedCard() {
        diceMultipliers = new int[2];
        //if one utility owned then diceMultiplier[0] should be chosen,
        //if two utilities are owned then diceMultiplier[1] should be chosen
        diceMultipliers[0] = 4;
        diceMultipliers[1] = 10;
    }

    public UtilityTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        diceMultipliers = new int[2];
        //if one utility owned then diceMultiplier[0] should be chosen,
        //if two utilities are owned then diceMultiplier[1] should be chosen
        diceMultipliers[0] = 4;
        diceMultipliers[1] = 10;
    }

    public int[] getRent() {
        return diceMultipliers;
    }
    //methods
}