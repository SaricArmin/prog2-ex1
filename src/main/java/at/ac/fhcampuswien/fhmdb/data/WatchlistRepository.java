package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.controller.WatchlistChangeEvent;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable{
    private static WatchlistRepository _instance;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;
    private List<Observer> observers;

    private WatchlistRepository()
    {
        this.watchlistDao = Database.getWatchlistMovieDao();
        this.observers = new ArrayList<>(); //needs Initialiion else can't start pr
    }
    public static WatchlistRepository getInstance()
    {
        if (_instance == null) {
            _instance = new WatchlistRepository();
        }
        return _instance;
    }

    public void removeFromWatchlist(Movie movie) throws SQLException {
        watchlistDao.delete(watchlistDao.queryForEq("apiId",movie.getId()));
        notifyObserver(new WatchlistChangeEvent(movie, true, false));
    }

    public List<WatchlistMovieEntity> getAll() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public void addToWatchlist(Movie movie) throws SQLException {
        // using parameterized queries to andle the proper escaping of special characters like single quotes (JdbcSQLSyntaxErrorException in Schindler's List)
        QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = watchlistDao.queryBuilder();
        queryBuilder.where().eq("apiId", movie.getId()); // checks if a movie with the same apiId already exists in the Watchlist.
        PreparedQuery<WatchlistMovieEntity> preparedQuery = queryBuilder.prepare();

        List<WatchlistMovieEntity> existingMovies = watchlistDao.query(preparedQuery);

        if (existingMovies.isEmpty()){
            watchlistDao.create(movieToWatchlist(movie));
            notifyObserver(new WatchlistChangeEvent(movie, true, true));
        } else {
            notifyObserver(new WatchlistChangeEvent(movie, false, true));
        }
    }

    private WatchlistMovieEntity movieToWatchlist(Movie movie){
        return new WatchlistMovieEntity(movie.getId(), movie.getTitle(), movie.getDescription(), WatchlistMovieEntity.genresToString(movie.getGenres()),movie.getReleaseYear(),movie.getImgUrl(),movie.getLengthInMinutes(),movie.getRating());
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(WatchlistChangeEvent event) {
        for (Observer observer : observers) {
            observer.update(this, event);
        }
    }
}
