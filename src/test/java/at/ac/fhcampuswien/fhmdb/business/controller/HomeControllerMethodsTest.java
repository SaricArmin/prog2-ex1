package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerMethodsTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void getMostPopularActorTest() {
        String actor = homeController.getMostPopularActor(createMovies());
        assertEquals(actor, "Adam Sandler");
    }

    @Test
    void getLongestMovieTitleTest() {
        int title = homeController.getLongestMovieTitle(createMovies());
        assertEquals(title, 32);
    }

    @Test
    void countMoviesFromTest() {
        long directors = homeController.countMoviesFrom(createMovies(), "Peter Jackson");
        assertEquals(directors, 2);
    }

    @Test
    void getMoviesBetweenYearsTest() {
        List<Movie> movies = homeController.getMoviesBetweenYears(createMovies(), 2018, 2023);
        assertEquals(movies.size(), 1);
        Movie movie = movies.get(0);
        assertEquals(movie.getTitle(), "Title is short");
        assertEquals(movie.getReleaseYear(), 2020);
    }

    private List<Movie> createMovies() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId("id");
        movie1.setDescription("description");
        List<String> mainCast = new ArrayList<>();
        mainCast.add("Adam Sandler");
        mainCast.add("Emma Stone");
        movie1.setMainCast(mainCast);
        movie1.setTitle("Title is short");
        List<String> directors = new ArrayList<>();
        directors.add("Peter Jackson");
        directors.add("Norman");
        movie1.setDirectors(directors);
        movie1.setReleaseYear(2020);

        Movie movie2 = new Movie();
        movie2.setId("id");
        movie2.setDescription("description");
        List<String> mainCast2 = new ArrayList<>();
        mainCast2.add("Adam Sandler");
        mainCast2.add("Ryan Gosling");
        movie2.setMainCast(mainCast2);
        movie2.setTitle("Title is long, oh yes, very long");
        List<String> directors2 = new ArrayList<>();
        directors2.add("Peter Jackson");
        directors2.add("Reed");
        movie2.setDirectors(directors2);
        movie2.setReleaseYear(2005);

        movies.add(movie1);
        movies.add(movie2);
        return movies;
    }
}
