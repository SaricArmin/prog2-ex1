package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.controller.HomeController;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void filterMoviesByActionGenre() {
        ObservableList<Movie> movies = homeController.filterMovies("", Genre.ACTION, homeController.allMovies);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        Movie movie = movies.stream().findFirst().orElseThrow();
        assertEquals(movie.getTitle(), "Lord of the Rings");
        assertTrue(movie.getGenres().contains(Genre.ACTION));
    }

    @Test
    void filterMoviesByStorySearch() {
        ObservableList<Movie> movies = homeController.filterMovies("STORY", null, homeController.allMovies);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 2); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        Movie movie = movies.stream().findFirst().orElseThrow();
        assertTrue(movie.getTitle().contains("story") || movie.getDescription().contains("story"));
    }

    @Test
    void filterMoviesByGenreAndSearch() {
        ObservableList<Movie> movies = homeController.filterMovies("STORY", Genre.WAR, homeController.allMovies);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        Movie movie = movies.stream().findFirst().orElseThrow();
        assertEquals(movie.getTitle(), "Tropic Thunder");
        assertTrue(movie.getGenres().contains(Genre.WAR));
    }

    @Test
    void sortMoviesAscDesc() {
        ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
        observableMovies.addAll(homeController.allMovies);
        String sortAsc = homeController.sortMovies(observableMovies);
        assertEquals(sortAsc, "Sort ↑");
        String sortDesc = homeController.sortMovies(observableMovies);
        assertEquals(sortDesc, "Sort ↓");
    }

    @Test
    void filterMoviesByEmptyGenreAndSearch(){
        ObservableList<Movie> movies = homeController.filterMovies("", null, homeController.allMovies);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 7); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
    }
}