package board;

import entities.Player;
import entities.Property;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Space {
    private String name;
    private Player latestPlayer;
    private int index;
}
