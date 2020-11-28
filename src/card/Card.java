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

    public  Card(String cardText, CardEvent cardEvent){
        this.cardText = cardText;
        this.cardEvent = cardEvent;
        if(this.cardEvent instanceof AdvanceEvent){
            advance = true;
            collect = false;
            goToJail = false;
            pay = false;
            getOutOfJailFree = false;
            thief = false;
        } else if (this.cardEvent instanceof CollectEvent){
            advance = false;
            collect = true;
            goToJail = false;
            pay = false;
            getOutOfJailFree = false;
            thief = false;
        } else if (this.cardEvent instanceof GoToJailEvent){
            advance = false;
            collect = false;
            goToJail = true;
            pay = false;
            getOutOfJailFree = false;
            thief = false;
        } else if (this.cardEvent instanceof PayEvent){
            advance = false;
            collect = false;
            goToJail = false;
            pay = true;
            getOutOfJailFree = false;
            thief = false;
        } else if (this.cardEvent instanceof ReceiveGetOutOfJailEvent){
            advance = false;
            collect = false;
            goToJail = false;
            pay = false;
            getOutOfJailFree = true;
            thief = false;
        } else if (this.cardEvent instanceof ThiefEvent){
            advance = false;
            collect = false;
            goToJail = false;
            pay = false;
            getOutOfJailFree = false;
            thief = true;
        }
    }



}
