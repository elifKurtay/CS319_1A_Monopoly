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

    private Property associatedProperty;
    private PropertyType type;

    /**
     * Initializes the property space by
     * @param name
     * @param index
     * @param propertyType
     * @param associatedProperty
     */
    public PropertySpace(String name, int index, PropertyType propertyType, Property associatedProperty) {
        super(name, index);
        this.associatedProperty = associatedProperty;
        this.type = propertyType;
    }

    public int calculateRent(Player payingPlayer) {
        return associatedProperty.getRent(payingPlayer);
    }
}
