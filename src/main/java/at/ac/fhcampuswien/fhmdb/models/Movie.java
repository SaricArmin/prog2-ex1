package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {

    //added variables for movies
    //private final String id;
    private String title;
    private String description;
    private List<Genre> genres;

    private int releaseYear;

    private String imgUrl;

    private int lengthMin;

    private List<String> directors;

    private List<String> writers;

    private List<String> mainCast;

    private  double rating;

    public Movie(String title, String description, List<Genre> genres, int releaseYear,
                 String imgUrl, int lengthMin, List<String> directors, List<String> writers,
                 List<String> mainCast, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthMin = lengthMin;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {return genres;}

    public int getReleaseYear() {
        return releaseYear;
    }
    public String getImgUrl ()
    {
        return imgUrl;
    }

    public int getLengthMin() {
        return lengthMin;
    }
    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getWriters() {
        return writers;
    }
    public List<String> getMainCast()
    {
        return mainCast;
    }
    public double getRating() {
        return rating;
    }

    public static List<Movie> initializeMovies(){
        //List<Movie> movies = new ArrayList<>();
        //prob gonna ditch this code
//        movies.add(new Movie("Lord of the Rings", "A short mans story to find true love", Arrays.asList(Genre.ACTION, Genre.ADVENTURE)));
//        movies.add(new Movie("Schindler's List", "MrBeast's inspiration", Arrays.asList(Genre.BIOGRAPHY, Genre.DRAMA)));
//        movies.add(new Movie("Passengers", "Chris Pratt's visualization of a simp", Arrays.asList(Genre.ROMANCE, Genre.FAMILY, Genre.SCIENCE_FICTION)));
//        movies.add(new Movie("Tropic Thunder", "A true story based of war heroes", Arrays.asList(Genre.WAR, Genre.FAMILY, Genre.COMEDY)));
//        movies.add(new Movie("A Million Ways to Die in the West", "Seth MacFarlane really tries to be an actor", Arrays.asList(Genre.WESTERN, Genre.COMEDY)));
//        movies.add(new Movie("Saw 8: Jigsaw", "Fun for the whole family", Arrays.asList(Genre.ROMANCE, Genre.FAMILY, Genre.DRAMA)));
//        movies.add(new Movie("(T)Raumschiff Surprise", "Documentation of our future", List.of(Genre.SCIENCE_FICTION)));
        List <Movie> movies = MovieAPI.fetchMovies();
        return movies;
    }
}