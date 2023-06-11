package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.controller.WatchlistChangeEvent;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(WatchlistChangeEvent event);
}

