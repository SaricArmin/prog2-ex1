package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.controller.WatchlistChangeEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver(WatchlistChangeEvent event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }
}

