package event;

import entities.Player;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollectEvent extends CardEvent{
    private boolean fromBank;
    private ArrayList<Player> receivers;
    private int amount;
}
