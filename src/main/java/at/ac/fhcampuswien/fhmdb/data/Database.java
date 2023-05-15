package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import static at.ac.fhcampuswien.fhmdb.business.controller.MovieCell.showExceptionAlert;

public class Database {

    private static final String DATABASE_URL  = "jdbc:h2:file: ./db/moviesDB";
    private static final String DATABASE_USER  = "root";
    private static final String DATABASE_PASSWORD  = "password";
    private static ConnectionSource connectionSource;
    static Dao<WatchlistMovieEntity, Long> watchlistDao;


    public static void createConnectionSource()
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
    public static void createTables() throws SQLException
    {
        TableUtils.createTable(connectionSource, WatchlistMovieEntity.class);
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistMovieDao() {
        if (watchlistDao == null) {
            try {
                createConnectionSource();
                //createTables();
                watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            } catch (SQLException e) {
                String title = "Error";
                String headerText = "Error while initializing database";
                String contentText = "The following error occurred while initializing the database:";
                showExceptionAlert(title, headerText, contentText, new DatabaseException(e));
            }
        }
        return watchlistDao;
    }
}
