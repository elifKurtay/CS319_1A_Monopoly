package card;

import event.CardEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Card {
    private String cardText;
    private CardEvent cardEvent;
    private boolean getOutOfJailFree;
    private boolean thief;

    public Card() {

    }

    public void open() {
        //?
    }

}
