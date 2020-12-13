package entities;

import frontend.Observer;

public interface Observable {
    // can rename to a better named method
    void add(Observer o);
    void notifyObservers();
}
