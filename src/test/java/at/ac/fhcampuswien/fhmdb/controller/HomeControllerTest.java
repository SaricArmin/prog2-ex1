package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Genre;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import at.ac.fhcampuswien.fhmdb.business.controller.HomeController;
import at.ac.fhcampuswien.fhmdb.business.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.business.models.SortAscState;
import at.ac.fhcampuswien.fhmdb.data.WatchlistRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.ac.fhcampuswien.fhmdb.data.MovieAPIRequestBuilder;
import at.ac.fhcampuswien.fhmdb.presentation.MyFactory;


import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController homeController;
    private WatchlistRepository watchlistRepository;

    @BeforeEach
    void setUp() {

        homeController = new HomeController();
    }

    @Test
    void filterMoviesByActionGenre() {
        ObservableList<Movie> movies = homeController.filterMovies("", Genre.ACTION, 9.0, 2008);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(moviee -> assertTrue(moviee.getGenres().contains(Genre.ACTION)));
    }

    @Test
    void filterMoviesByStorySearch() {
        ObservableList<Movie> movies = homeController.filterMovies("STORY", null, 7.7, 1995);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(movie -> assertTrue(movie.getTitle().toUpperCase().contains("STORY") || movie.getDescription().toUpperCase().contains("STORY")));
    }

    @Test
    void filterMoviesByGenreAndSearch() {
        ObservableList<Movie> movies = homeController.filterMovies("The", Genre.ACTION, 8.5, 2008);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 1); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
        movies.forEach(movie -> assertTrue(movie.getGenres().contains(Genre.ACTION)));
        movies.forEach(movie -> assertTrue(movie.getTitle().contains("The") || movie.getDescription().contains("The")));
    }

    @Test
    void sortMoviesAscDesc() {
        ObservableList<Movie> observableMovies = createMockMovies();
        homeController.sortState = new SortAscState(homeController);
        String sortAsc = homeController.sortState.clickSort(observableMovies);
        assertEquals(sortAsc, "Sort ↑");
        String sortDesc = homeController.sortState.clickSort(observableMovies);
        assertEquals(sortDesc, "Sort ↓");
    }

    @Test
    void filterMoviesByEmptyGenreAndSearch(){
        ObservableList<Movie> movies = homeController.filterMovies("", null, 3.2, 2019);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 2); //Wenn man die Daten erweitert wird das hier eventuell fehlschlagen
    }

    private ObservableList<Movie> createMockMovies() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        Movie movie1 = new Movie();
        movie1.setId("test1");
        movie1.setTitle("A good movie");
        Movie movie2 = new Movie();
        movie2.setId("test2");
        movie2.setTitle("Ze best movie");
        Movie movie3 = new Movie();
        movie3.setId("test3");
        movie3.setTitle("My movie");
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        return movies;
    }

    @Test
    //singleton
    void testWatchListRepositorySingleton()
    {
        assertEquals(WatchlistRepository.getInstance(), WatchlistRepository.getInstance());
    }

    @Test
    void testBuilderPattern()
    {
        String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
        String expectedUrl = baseUrl + "?query=action&genre=SCIENCE_FICTION&releaseYear=2021&ratingFrom=8.0";

        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(baseUrl);
        String actualUrl = builder
                .query("action")
                .genre(Genre.SCIENCE_FICTION)
                .releaseYear(2021)
                .ratingFrom(8.0)
                .build();

        assertEquals(expectedUrl, actualUrl);
    }
/*
    @Test
    void testBuildWithId() {
        String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
        String id = "12345";

        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(baseUrl)
                .id(id);

        String expectedUrl = "https://api.example.com/movies?id=12345";
        String actualUrl = builder.build();

        assertEquals(expectedUrl, actualUrl);
    }*/

    @Test
    void testBuilderPattern2()
    {
        String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
        String expectedUrl = baseUrl + "?query=drama&ratingFrom=7.0";

        MovieAPIRequestBuilder builder = new MovieAPIRequestBuilder(baseUrl);
        String actualUrl = builder
                .query("drama")
                .ratingFrom(7.0)
                .build();

        assertEquals(expectedUrl, actualUrl);
    }
    @Test
    void TestFactoryPattern()
    {
        MyFactory factory = MyFactory.getInstance();

        Class<?> controllerClass = HomeController.class;
        Object controllerInstance = factory.call(controllerClass);

        assertNotNull(controllerInstance);
        assertEquals(controllerClass, controllerInstance.getClass());
    }

}