package card;

public class LandTitleDeedCard extends TitleDeedCard{
    private int propertyGroup;
    private int[] rent;
    private int houseCost;
    private int hotelCost;

    public LandTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        propertyGroup = -1; //undefined
        rent = new int[6];
        houseCost = -1;
        hotelCost = -1;
    }

    public LandTitleDeedCard(String propertyName, int mortgageValue, int propertyGroup, int[] rent,
                             int houseCost, int hotelCost){
        super(propertyName, mortgageValue);
        this.propertyGroup = propertyGroup;
        this.rent = rent;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
    }

    public int getPropertyGroup() {
        return propertyGroup;
    }

    public void setPropertyGroup(int propertyGroup) {
        this.propertyGroup = propertyGroup;
    }

    public int[] getRent() {
        return rent;
    }

    public void setRent(int[] rent) {
        this.rent = rent;
    }

    public int getHouseCost() {
        return houseCost;
    }

    public void setHouseCost(int houseCost) {
        this.houseCost = houseCost;
    }

    public int getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(int hotelCost) {
        this.hotelCost = hotelCost;
    }
}
