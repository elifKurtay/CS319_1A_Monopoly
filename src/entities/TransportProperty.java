package entities;

public class TransportProperty extends Property {

    public TransportProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
    }

    @Override
    public int getRent(Player playerToPay) {
        int numberOfTitlesFromSameGroup = owner.numberOfTitlesFromSameGroup(this);
        int rent = rents[numberOfTitlesFromSameGroup - 1];
        return (int) (rent * playerToPay.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier());
    }
}
