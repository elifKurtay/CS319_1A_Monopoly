package board;

import entities.Player;
import entities.Property;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class Space implements Serializable {
    private String name;
    private Player latestPlayer;
    private int index;

    public Space(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
