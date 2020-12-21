package entities;

public class UtilityProperty extends Property {

    /**
     * Initialized the utility property by setting property name, value,
     * mortgage value, rents and property group.
     * @param propertyName
     * @param value
     * @param mortgageValue
     * @param rents
     * @param propertyGroup
     */
    public UtilityProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
    }

    /**
     * Calculates the rent using the token multiplier and returns it.
     * @param playerToPay
     * @return rent
     */
    @Override
    public int getRent(Player playerToPay) {
        int numberOfTitlesFromSameGroup = owner.numberOfPropertiesFromSameGroup(this);
        if (numberOfTitlesFromSameGroup == 0) {
            System.out.println("AB123");
        }
        int rent = rents[numberOfTitlesFromSameGroup-1] * playerToPay.getCurrentDiceSum();
        return (int) (rent * playerToPay.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier());
    }
}
