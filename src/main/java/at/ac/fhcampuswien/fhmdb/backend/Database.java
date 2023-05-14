package at.ac.fhcampuswien.fhmdb.backend;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {

    private static final String DATABASE_URL  = "jdbc:mysql://localhost:3306/fhmdb";
    private static final String DATABASE_USER  = "root";
    private static final String DATABASE_PASSWORD  = "password";
    private static ConnectionSource connectionSource;
    static Dao<WatchlistMovieEntity, Long> watchlistDao;

    public void createConnectionSource()
    {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL,DATABASE_USER,DATABASE_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        if (connectionSource == null) {
            connectionSource = new JdbcConnectionSource(DATABASE_URL,DATABASE_USER,DATABASE_PASSWORD);
        }
        return connectionSource;
    }
    public void createTables() throws SQLException
    {
        TableUtils.createTable(connectionSource, WatchlistMovieEntity.class);
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistMovieDao() throws SQLException {
        if (watchlistDao == null) {
            watchlistDao = DaoManager.createDao(getConnectionSource(), WatchlistMovieEntity.class);
        }
        return watchlistDao;
    }
}
