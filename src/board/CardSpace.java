package board;

public class CardSpace extends Space {
    private enum CardType {
        CHANCE, COMMUNITY_CHEST
    }

    private CardType type;

    public CardSpace(String cardType) {
        if (cardType.equals("CHANCE")) {
            type = CardType.CHANCE;
        }
        else {
            type = CardType.COMMUNITY_CHEST;
        }

    }

    public void drawCardAndOpen() {
        //Need a reference to board to access the card arraylists
    }

    public void drawCardAndPostpone() {

    }
}
