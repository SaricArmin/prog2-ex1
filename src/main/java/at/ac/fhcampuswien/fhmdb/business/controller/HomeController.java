package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Genre;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import at.ac.fhcampuswien.fhmdb.business.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.data.Observable;
import at.ac.fhcampuswien.fhmdb.data.Observer;
import at.ac.fhcampuswien.fhmdb.data.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.data.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.fhmdb.business.controller.MovieCell.showExceptionAlert;

public class HomeController implements Initializable, Observer {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Genre> genreComboBox;

    @FXML
    public JFXComboBox<Integer> yearComboBox;

    @FXML
    public JFXComboBox<Double> ratingComboBox;

    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton deleteBtn;

    @FXML
    public JFXButton switchSceneBtn;
    public List<Movie> allMovies;
    public Set<Genre> allGenres = new HashSet<>();
    public Set<Double> allRatings = new HashSet<>();
    public Set<Integer> allReleaseYears = new HashSet<>();

    private final MovieAPI movieAPI = new MovieAPI();

    private static final String SORT_ASC = "Sort ↑";
    private static final String SORT_DESC = "Sort ↓";
    private boolean sortAsc = false;
    WatchlistRepository watchlistRepository;

    public List<WatchlistMovieEntity> watchList = new ArrayList<>();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {

    };

    private void initializeData() {
        allMovies = movieAPI.fetchMovies();
        allMovies.forEach(movie -> {
            allGenres.addAll(movie.getGenres());
            allRatings.add(movie.getRating());
            allReleaseYears.add(movie.getReleaseYear());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeData();
        watchlistRepository = watchlistRepository.getInstance();

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        watchlistRepository = WatchlistRepository.getInstance();
        watchlistRepository.addObserver(this);
        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(allGenres);
        FXCollections.sort(genreComboBox.getItems(), Comparator.comparing(Genre::name));

        yearComboBox.setPromptText("Filter by Release Year");
        yearComboBox.getItems().addAll(allReleaseYears);
        FXCollections.sort(yearComboBox.getItems(), Comparator.comparing(Integer::intValue));

        ratingComboBox.setPromptText("Filter by Rating");
        ratingComboBox.getItems().addAll(allRatings);
        FXCollections.sort(ratingComboBox.getItems(), Comparator.comparing(Double::doubleValue));

        searchBtn.setOnAction(actionEvent ->{
            movieListView.setItems(filterMovies(searchField.getText().toUpperCase(),
                    genreComboBox.getSelectionModel().getSelectedItem(),
                    ratingComboBox.getSelectionModel().getSelectedItem(),
                    yearComboBox.getSelectionModel().getSelectedItem()));
            movieListView.refresh();
            deleteBtn.setDisable(false);
        });

        FXCollections.sort(movieListView.getItems(), Comparator.comparing(Movie::getTitle));
        sortBtn.setOnAction(actionEvent -> {
            sortBtn.setText(sortMovies(movieListView.getItems()));
        });

        deleteBtn.setDisable(true);
        deleteBtn.setOnAction(actionEvent -> {
            searchField.clear();
            genreComboBox.getSelectionModel().clearSelection();
            yearComboBox.getSelectionModel().clearSelection();
            ratingComboBox.getSelectionModel().clearSelection();
            deleteBtn.setDisable(true);
            movieListView.setItems(observableMovies);
        });
    }

    public String sortMovies(ObservableList<Movie> movies) {
        if(sortAsc) {
            // Sort observableMovies in ascending order by title
            FXCollections.sort(movies, Comparator.comparing(Movie::getTitle));
            sortAsc = false;
            return SORT_DESC;
        } else {
            // Sort observableMovies in descending order by title
            FXCollections.sort(movies, Comparator.comparing(Movie::getTitle).reversed());
            sortAsc = true;
            return SORT_ASC;
        }
    }

    public ObservableList<Movie> filterMovies(String search, Genre genre, Double rating, Integer releaseYear) {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        Map<String, String> parameters = new HashMap<>();
        if (search != null && !search.isEmpty())
            parameters.put("query", search);
        if (genre != null)
            parameters.put("genre", genre.name());
        if (rating != null)
            parameters.put("ratingFrom", rating.toString());
        if (releaseYear != null)
            parameters.put("releaseYear", releaseYear.toString());

        try {
            movies.addAll(movieAPI.searchMovies(parameters));
        } catch (Exception e) {
            showExceptionAlert("while API search", new MovieApiException(e.getMessage()));
        }
        return movies;
    }

    // Director Count method
    public String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));
        return actorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        Optional<Movie> longestTitleMovie = movies.stream()
            .max(Comparator.comparing(movie -> movie.getTitle().length()));

        return longestTitleMovie.map(movie -> movie.getTitle().length()).orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
            .filter(movie -> movie.getDirectors().contains(director))
            .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .toList();
    }

    public void switchScene() {
        if(switchSceneBtn.getText().equals("Switch to Watchlist"))
        {
            observableMovies.clear();
            List<WatchlistMovieEntity> movieEntities = new ArrayList<>();

            try {
                movieEntities = watchlistRepository.getAll();
            } catch (SQLException e) {
                showExceptionAlert("while fetching Watchlist in DATABASE", new DatabaseException(e));
            }

            ObservableList<Movie> observableMoviesTemp = FXCollections.observableArrayList(
                    movieEntities.stream()
                            .map(WatchlistMovieEntity::createMovie)
                            .toList());
            observableMovies.addAll(observableMoviesTemp); //show all movies from the watchlist
            switchSceneBtn.setText("Switch to Homepage");
        }
        else
        {
            observableMovies.clear();
            observableMovies.addAll(allMovies); //show all movies
            switchSceneBtn.setText("Switch to Watchlist");
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof WatchlistRepository)
            if (arg instanceof Movie)
            {
                Movie movie = (Movie) arg;
                showFailOrSuccessAlert(movie);
            }
    }

    public void showFailOrSuccessAlert(Movie movie)
    {
        String msg;
        if (observableMovies.contains(movie))
        {
            msg = "Movie already in watchlist";
        }
        else  {
            msg = "Movie successfully added to watchlist";
        }
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Watchlist");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}