package board;

import java.util.HashMap;
import entities.Player;

public class JailSpace extends Space{
    private HashMap<Player, Integer> jailRecord;

    public void imprisonPlayer(Player player) {
        jailRecord.put(player, player.getToken().getJailTime());
    }

    public void releasePlayer(Player player) {
        jailRecord.remove(player);
    }
}
