package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;
    public List<Movie> allMovies = Movie.initializeMovies();
    public List<Genre> allGenres = Arrays.asList(Genre.values());

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(allGenres);

        searchBtn.setOnAction(actionEvent ->{
            String value = searchField.getText().toUpperCase();
            ObservableList<Movie> movies = FXCollections.observableArrayList();

            for (Movie movie : observableMovies) {
                String desc = movie.getDescription().toUpperCase();
                String title = movie.getTitle().toUpperCase();
                String genre = movie.getGenres().toString();

                if ((desc.contains(value) || title.contains(value)) &&
                        (genreComboBox.getSelectionModel().getSelectedIndex() == -1 || genre.contains(genreComboBox.getSelectionModel().getSelectedItem().toString()))) {
                    movies.add(movie);
                }
            }
            movieListView.setItems(movies);
            movieListView.refresh();
        });

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // Sort observableMovies in ascending order by title
                sortBtn.setText("Sort (desc)");
                Comparator<Movie> titleComparator = (movie1, movie2) -> movie1.getTitle().compareTo(movie2.getTitle());
                FXCollections.sort(movieListView.getItems(), titleComparator);
            } else {
                // Sort observableMovies in descending order by title
                sortBtn.setText("Sort (asc)");
                Comparator<Movie> titleComparator = (movie1, movie2) -> movie1.getTitle().compareTo(movie2.getTitle());
                FXCollections.sort(movieListView.getItems(), titleComparator.reversed());
            }
        });

        //filtering with sorted list does not work
    }
}