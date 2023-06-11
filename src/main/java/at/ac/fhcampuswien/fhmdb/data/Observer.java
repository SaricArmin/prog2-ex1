package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.controller.WatchlistChangeEvent;

public interface Observer {
    void update(Observable observable, WatchlistChangeEvent event);
}