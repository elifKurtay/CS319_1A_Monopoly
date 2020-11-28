package board;
import entities.Player;
import entities.Property;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertySpace extends Space{
    public enum PropertyType {
        LAND, TRANSPORT, UTILITY
    }

    private Player owner;
    private int value;
    private Property associatedProperty;
    private PropertyType type;

    public PropertySpace(String name, String propertyType, Property associatedProperty) {

        owner = null;
        // Need to associate PropertySpaces with Properties at instantiation
        this.associatedProperty = associatedProperty;

        setName(name);

        if (propertyType.equals("LAND")) {
            this.type = PropertyType.LAND;
        }
        else if (propertyType.equals("TRANSPORT")) {
            this.type = PropertyType.TRANSPORT;
        }
        else {
            this.type = PropertyType.UTILITY;
        }
    }

    public boolean buySpace() {
        if (owner == null) {
            // Assuming payBank returns a boolean value, true if payment has succeeded
            if (super.getLatestPlayer().payBank(value)) {
                owner = super.getLatestPlayer();
                super.getLatestPlayer().getProperties().add(associatedProperty);
                return true;
            }
        }
        return false;
    }

}
