package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WatchlistChangeEvent {
    private Movie movie;
    private boolean success;
    private boolean addingToWatchlist;
}

