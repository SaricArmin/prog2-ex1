package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository(){
        this.watchlistDao = Database.getWatchlistMovieDao();
    }

    public void removeFromWatchlist(Movie movie) throws SQLException {
        watchlistDao.delete(watchlistDao.queryForEq("apiId",movie.getId()));
    }

    public List<WatchlistMovieEntity> getAll() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public void addToWatchlist(Movie movie) throws SQLException {
        if (watchlistDao.queryForMatching(movieToWatchlist(movie)).isEmpty()){
            watchlistDao.create(movieToWatchlist(movie));
        }
    }

    private WatchlistMovieEntity movieToWatchlist(Movie movie){
        return new WatchlistMovieEntity(movie.getId(),movie.getTitle(), movie.getDescription(), WatchlistMovieEntity.genresToString(movie.getGenres()),movie.getReleaseYear(),movie.getImgUrl(),movie.getLengthInMinutes(),movie.getRating());
    }
}
