package card;

import event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Card implements Serializable {
    private String cardText;
    private CardEvent cardEvent;

    public Card(String cardText, CardEvent cardEvent){
        this.cardText = cardText;
        this.cardEvent = cardEvent;
    }
}
