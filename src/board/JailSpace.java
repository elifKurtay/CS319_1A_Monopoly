package board;

import entities.Player;
import entities.Property;

import java.util.HashMap;

public class JailSpace extends Space{
    private HashMap<Player, Integer> jailRecord;

    //public void imprisonPlayer(Player player) {
        //jailRecord.put(player, player.getToken().getJailTime());
    //}

    public void releasePlayer(Player player) {
        jailRecord.remove(player);
    }

    @Override
    public Property getAssociatedProperty() {
        return null;
    }
}
