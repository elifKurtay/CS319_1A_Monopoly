package card;

import event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String cardText;
    private CardEvent cardEvent;

    public Card(String cardText, CardEvent cardEvent){
        this.cardText = cardText;
        this.cardEvent = cardEvent;
    }
}
