package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalPlayer extends Player{
    public DigitalPlayer(String name) {
        super(name);
    }

    public void play() {

    }

    public void decideOnAction(){}

    public void decideOnBid(){

    }

    public void decideOnTrade(){

    }


}
