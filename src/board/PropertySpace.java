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

}
