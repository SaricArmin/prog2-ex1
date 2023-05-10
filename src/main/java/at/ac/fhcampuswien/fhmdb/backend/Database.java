package at.ac.fhcampuswien.fhmdb.backend;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public class Database {

    private String DB_URL;
    private String username;
    private String password;
    private ConnectionSource connectionSource;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public void createConnectionSource() {

    }

    public ConnectionSource getConnectionSource() {
        return null;
    }

    public void createTables() {

    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return null;
    }
}
