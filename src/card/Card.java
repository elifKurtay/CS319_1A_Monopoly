package card;

import event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Card {
    private String cardText;
    private CardEvent cardEvent;
    private boolean advance;
    private boolean collect;
    private boolean goToJail;
    private boolean pay;
    private boolean getOutOfJailFree;
    private boolean thief;

    public Card() {
        cardText = null;
        cardEvent = null;
        advance = false;
        collect = false;
        goToJail = false;
        pay = false;
        getOutOfJailFree = false;
        thief = false;
    }

    public Card(String cardText, CardEvent cardEvent){
        this.cardText = cardText;
        this.cardEvent = cardEvent;
        advance = false;
        collect = false;
        goToJail = false;
        pay = false;
        getOutOfJailFree = false;
        thief = false;
        if(this.cardEvent instanceof AdvanceEvent){
            advance = true;
        } else if (this.cardEvent instanceof CollectEvent){
            collect = true;
        } else if (this.cardEvent instanceof GoToJailEvent){
            goToJail = true;
        } else if (this.cardEvent instanceof PayEvent){
            pay = true;
        } else if (this.cardEvent instanceof ReceiveGetOutOfJailEvent){
            getOutOfJailFree = true;
        } else if (this.cardEvent instanceof ThiefEvent){
            thief = true;
        }
    }



}
