package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {return genres;}

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Lord of the Rings", "A short mans story to find true love", Arrays.asList(Genre.ACTION, Genre.ADVENTURE)));
        movies.add(new Movie("Schindler's List", "MrBeast's inspiration", Arrays.asList(Genre.BIOGRAPHY, Genre.DRAMA)));
        movies.add(new Movie("Passengers", "Chris Pratt's visualization of a simp", Arrays.asList(Genre.ROMANCE, Genre.FAMILY, Genre.SCIENCE_FICTION)));
        movies.add(new Movie("Tropic Thunder", "A true story based of war heroes", Arrays.asList(Genre.WAR, Genre.FAMILY, Genre.COMEDY)));
        movies.add(new Movie("A Million Ways to Die in the West", "Seth MacFarlane really tries to be an actor", Arrays.asList(Genre.WESTERN, Genre.COMEDY)));
        movies.add(new Movie("Saw 8: Jigsaw", "Fun for the whole family", Arrays.asList(Genre.ROMANCE, Genre.FAMILY, Genre.DRAMA)));
        movies.add(new Movie("(T)Raumschiff Surprise", "Documentation of our future", List.of(Genre.SCIENCE_FICTION)));

        return movies;
    }


}