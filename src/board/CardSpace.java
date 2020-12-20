package board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardSpace extends Space {
    public enum CardType {
        CHANCE, COMMUNITY_CHEST
    }

    private CardType type;

    /**
     * Initalizes the card space by setting the card type and index
     * @param cardType
     * @param index
     */
    public CardSpace(String cardType, int index) {
        super(null, index);
        if (cardType.equals("CHANCE")) {
            type = CardType.CHANCE;
            setName("Chance");
        }
        else {
            type = CardType.COMMUNITY_CHEST;
            setName("Community Chest");
        }

    }
}
