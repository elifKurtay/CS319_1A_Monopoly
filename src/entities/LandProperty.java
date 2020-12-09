package entities;

public class LandProperty extends Property {
    private int numOfHouses;
    private boolean hotel;
    private int houseCost;
    private int hotelCost;

    public LandProperty(String propertyName, int value, int mortgageValue, int[] rents, int propertyGroup, int houseCost, int hotelCost) {
        super(propertyName, value, mortgageValue, rents, propertyGroup);
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        hotel = false;
        numOfHouses = 0;
    }

    @Override
    public int getWorth() {
        if (hotel) {
            return super.getWorth() + houseCost * 4 + hotelCost;
        }
        else {
            return super.getWorth() + houseCost * numOfHouses;
        }
    }

    @Override
    public void mortgage() {
        mortgaged = true;
        if (hotel) {
            owner.setMoney(owner.getMoney() + numOfHouses * 4 + hotelCost);
        }
        else {
            owner.setMoney(owner.getMoney() + numOfHouses * houseCost);
        }
        numOfHouses = 0;
        hotel = false;
    }

    public void buildHouse() {
        numOfHouses++;
        if(numOfHouses == 5)
            hotel = true;
    }

    //do we have sell? is this to be used in mortgaging?
    public void sellHouse () {
        if(hotel)
            hotel = false;
        numOfHouses--;
    }

    @Override
    public int getRent(Player playerToPay) {
        int numberOfTitlesFromSameGroup = owner.numberOfTitlesFromSameGroup(this);
        int rent;
        if (numOfHouses == 0 && numberOfTitlesFromSameGroup == numberOfPropertiesInGroups[propertyGroup]) {
            rent = rents[0] * 2;
        }
        else {
            rent = rents[numOfHouses];
        }

        return (int) (rent * playerToPay.getToken().getRentPayMultiplier() * owner.getToken().getRentCollectMultiplier());
    }

}
