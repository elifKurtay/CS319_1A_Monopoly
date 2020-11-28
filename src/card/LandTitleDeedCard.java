package card;

import entities.Property;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class LandTitleDeedCard extends TitleDeedCard{
    private int propertyGroup;
    private int[] rents;
    private int houseCost;
    private int hotelCost;

    public LandTitleDeedCard(String propertyName, int mortgageValue) {
        super(propertyName, mortgageValue);
        propertyGroup = -1; //undefined
        rents = new int[6];
        houseCost = -1;
        hotelCost = -1;
    }

    public LandTitleDeedCard(String propertyName, int mortgageValue, int propertyGroup, int[] rent,
                             int houseCost, int hotelCost){
        super(propertyName, mortgageValue);
        this.propertyGroup = propertyGroup;
        this.rents = rent;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
    }

    public int getRent( @NotNull Property property) {
        if(property.isHotel())
            return rents[5];
        return rents[property.getNumOfHouses()];
    }


}
