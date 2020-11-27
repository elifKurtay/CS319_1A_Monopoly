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

    public CardSpace(String cardType) {
        if (cardType.equals("CHANCE")) {
            type = CardType.CHANCE;
            setName("Chance");
        }
        else {
            type = CardType.COMMUNITY_CHEST;
            setName("Community Chest");
        }

    }

    public void drawCardAndOpen() {
        //Need a reference to board to access the card arraylists
    }

    public void drawCardAndPostpone() {

    }
}
