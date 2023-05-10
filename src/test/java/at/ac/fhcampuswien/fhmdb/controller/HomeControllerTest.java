package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Genre;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import at.ac.fhcampuswien.fhmdb.business.controller.HomeController;
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
        ObservableList<Movie> movies = homeController.filterMovies("", Genre.ACTION, 9.0, 1999);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(moviee -> assertTrue(moviee.getGenres().contains(Genre.ACTION)));
    }

    @Test
    void filterMoviesByStorySearch() {
        ObservableList<Movie> movies = homeController.filterMovies("STORY", null, 8.7, 2004);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 2); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(movie -> assertTrue(movie.getTitle().contains("story") || movie.getDescription().contains("story")));
    }

    @Test
    void filterMoviesByGenreAndSearch() {
        ObservableList<Movie> movies = homeController.filterMovies("STORY", Genre.WAR, 5.4, 2020);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(movie -> assertTrue(movie.getGenres().contains(Genre.WAR)));
        movies.forEach(movie -> assertTrue(movie.getTitle().contains("story") || movie.getDescription().contains("story")));
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
        ObservableList<Movie> movies = homeController.filterMovies("", null, 3.2, 2023);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 7); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
    }

}