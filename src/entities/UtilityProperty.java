package entities;

public class UtilityProperty extends Property {

    public UtilityProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
    }

    @Override
    public int getRent(Player playerToPay) {
        int numberOfTitlesFromSameGroup = owner.numberOfPropertiesFromSameGroup(this);
        int rent = rents[numberOfTitlesFromSameGroup-1] * playerToPay.getCurrentDiceSum();
        return (int) (rent * playerToPay.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier());
    }
}
