package at.ac.fhcampuswien.fhmdb.backend;

import com.j256.ormlite.dao.Dao;

import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public void removeFromWatchlist(WatchlistMovieEntity movie) {

    }

    public List<WatchlistMovieEntity> getAll() {
        return null;
    }

    public void addToWatchlist(WatchlistMovieEntity movie) {

    }
}
