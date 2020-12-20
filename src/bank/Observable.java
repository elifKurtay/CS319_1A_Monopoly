package bank;

import java.util.ArrayList;

public abstract class Observable {
    private ArrayList<Observer> observers = new ArrayList<Observer>();
    private int state;

    /**
     * Get state method
     * @return state
     */
    public int getState(){
        return state;
    }

    /**
     * Set state method
     * @param state
     */
    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }

    /**
     * Adds the observer to the observers list
     * @param observer
     */
    public void attach(Observer observer){
        observers.add(observer);
    }

    /**
     * Removes the observer to the observers list
     * @param observer
     */
    public void detach(Observer observer){
        observers.remove(observer);
    }

    /**
     * Notifies the observers
     */
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }


}
