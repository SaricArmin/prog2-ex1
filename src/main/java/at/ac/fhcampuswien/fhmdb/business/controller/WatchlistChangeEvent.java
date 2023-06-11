package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Movie;

public class WatchlistChangeEvent {
    private Movie movie;
    private boolean success;

    public WatchlistChangeEvent(Movie movie, boolean success) {
        this.movie = movie;
        this.success = success;
    }

    public Movie getMovie() {
        return movie;
    }

    public boolean isSuccess() {
        return success;
    }
}

