package card;

import event.CardEvent;

public class Card {
    private String cardText;
    private CardEvent cardEvent;

    public Card(String cardText, CardEvent cardEvent){
        this.cardText = cardText;
        this.cardEvent = cardEvent;
    }

    public void open() {
        //?
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public CardEvent getCardEvent() {
        return cardEvent;
    }

    public void setCardEvent(CardEvent cardEvent) {
        this.cardEvent = cardEvent;
    }
}
