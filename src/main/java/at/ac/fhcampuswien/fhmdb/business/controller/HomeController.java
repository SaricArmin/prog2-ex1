package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.*;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

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
    public SortState sortState;

    private final MovieAPI movieAPI = new MovieAPI();
    WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private final ClickEventHandler<Movie> onAddToWatchlistClicked = (clickedItem) -> {

    };
    private static final String MOVIE = "Movie '";


    private void initializeData() {
        sortState = new SortAscState(this);
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
        watchlistRepository = WatchlistRepository.getInstance();

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        watchlistRepository = WatchlistRepository.getInstance();
        watchlistRepository.addObserver(this);
        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(listView -> new MovieCell(onAddToWatchlistClicked)); // use custom cell factory to display data

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
            sortState.sortMovies(movieListView.getItems());
            deleteBtn.setDisable(false);
        });

        sortBtn.setOnAction(actionEvent -> {
            sortBtn.setText(sortState.clickSort(movieListView.getItems()));
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
            observableMovies.addAll(getWatchList()); //show all movies from the watchlist
            switchSceneBtn.setText("Switch to Homepage");
        }
        else
        {
            observableMovies.clear();
            observableMovies.addAll(allMovies); //show all movies
            switchSceneBtn.setText("Switch to Watchlist");
        }
    }

    @NotNull
    public ObservableList<Movie> getWatchList() {
        List<WatchlistMovieEntity> watchList = new ArrayList<>();

        try {
            watchList = watchlistRepository.getAll();
        } catch (SQLException e) {
            showExceptionAlert("while fetching Watchlist in DATABASE", new DatabaseException(e));
        }

        return FXCollections.observableArrayList(
                watchList.stream()
                        .map(WatchlistMovieEntity::createMovie)
                        .toList());
    }

    @Override
    public void update(Observable observable, WatchlistChangeEvent event) {
        if (observable instanceof WatchlistRepository) {
            Movie movie = event.getMovie();
            showFailOrSuccessAlert(movie, event.isSuccess(), watchlistRepository.isAddingToWatchlist());
        }
    }
    private void showFailOrSuccessAlert(Movie movie, boolean success, boolean adding) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(success ? "Success" : "Failure");
        a.setHeaderText(null);

        if (success) {
            if (adding) {
                a.setContentText(MOVIE + movie.getTitle() + "' successfully added to watchlist!");
            } else {
                a.setContentText(MOVIE + movie.getTitle() + "' successfully removed from watchlist!");
            }
        } else {
            a.setContentText(MOVIE + movie.getTitle() + "' already exists in watchlist!");
        }

        a.show();
    }

    public void addMovie(Movie movie) {
        try {
            watchlistRepository.addToWatchlist(movie);
        } catch (SQLException e) {
            showExceptionAlert("while adding item from watchlist", new DatabaseException("Error while adding item from watchlist", e));
        }
    }

    public void removeMovie(Movie movie) {
        try {
            watchlistRepository.removeFromWatchlist(movie);
        } catch (SQLException e) {
            showExceptionAlert("while removing item from watchlist", new DatabaseException("Error while removing item from watchlist", e));
        }
    }

    public void changeSortState(SortState sortState) {
        this.sortState = sortState;
    }
}