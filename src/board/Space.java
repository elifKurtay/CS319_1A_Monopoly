package board;

import entities.Player;
import entities.Property;

public abstract class Space {
    private String name;
    private Player latestPlayer;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLatestPlayerOnSpace(Player latestPlayer) {
        this.latestPlayer = latestPlayer;
    }

    public Player getLatestPlayerOnSpace() {
        return latestPlayer;
    }

    public Property getAssociatedProperty() {return null;};
}
