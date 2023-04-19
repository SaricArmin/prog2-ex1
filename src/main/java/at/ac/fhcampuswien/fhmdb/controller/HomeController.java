package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox<Genre> genreComboBox;

    @FXML
    public JFXComboBox yearComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton deleteBtn;
    public List<Movie> allMovies = Movie.initializeMovies();
    public List<Genre> allGenres = Arrays.asList(Genre.values());

    private static final String SORT_ASC = "Sort ↑";
    private static final String SORT_DESC = "Sort ↓";
    private boolean sortAsc = false;

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(getMostPopularActor(observableMovies));
        alert.showAndWait();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(allGenres);

        yearComboBox.setPromptText("Filter by Release Year");
        List<Integer> allReleaseYears = new ArrayList<>();

        allReleaseYears.add(1234);
        //needs improvements(?)
        yearComboBox.getItems().addAll(allReleaseYears);

        ratingComboBox.setPromptText("Filter by Rating");
        List<Double> allRatings = new ArrayList<>();
        allRatings.add(1234.4214);
        //needs improvements(?)
        ratingComboBox.getItems().addAll(allRatings);

        searchBtn.setOnAction(actionEvent ->{
            movieListView.setItems(filterMovies(searchField.getText().toUpperCase(), genreComboBox.getSelectionModel().getSelectedItem(), allMovies));
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

    public ObservableList<Movie> filterMovies(String search, Genre genre, List<Movie> allMovies) {
        ObservableList<Movie> movies = FXCollections.observableArrayList();

        movies.addAll(allMovies.stream()
                .filter(movie -> movie.getTitle().toUpperCase().contains(search) || movie.getDescription().toUpperCase().contains(search))
                .filter(movie -> (genre == null || movie.getGenres().contains(genre)))
                .toList());
        return movies;
    }

    // Director Count method
    public static String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));
        return actorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }
}