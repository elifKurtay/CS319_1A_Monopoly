package event;

import entities.Player;

import java.util.ArrayList;

public class PayEvent extends CardEvent{
    private boolean toBank;
    private ArrayList<Player> receivers;
    private int amount;
}
