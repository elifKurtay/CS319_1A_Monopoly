package bank;

import lombok.Getter;

public abstract class Observer {
    @Getter protected Observable observable;
    public abstract void update();
}
