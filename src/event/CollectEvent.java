package event;

import entities.Player;

import java.util.ArrayList;

public class CollectEvent extends CardEvent{
    private boolean fromBank;
    private ArrayList<Player> senders;
    private int amount;
}
