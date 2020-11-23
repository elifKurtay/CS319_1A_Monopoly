package entities;

import java.util.ArrayList;

public class Player {
    private ArrayList<Property> properties;
    private int money;
    private int getOutOfJailFreeCount;

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setGetOutOfJailFreeCount(int getOutOfJailFreeCount) {
        this.getOutOfJailFreeCount = getOutOfJailFreeCount;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getGetOutOfJailFreeCount() {
        return getOutOfJailFreeCount;
    }

    public int getMoney() {
        return money;
    }
}
