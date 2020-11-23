package entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Player {
    private ArrayList<Property> properties;
    private int money;
    private int getOutOfJailFreeCount;

}
