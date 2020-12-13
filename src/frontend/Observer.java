package frontend;

import entities.Observable;

public interface Observer {
    void update(Observable o);
}
