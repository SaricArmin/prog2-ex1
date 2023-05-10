package at.ac.fhcampuswien.fhmdb.backend;

import at.ac.fhcampuswien.fhmdb.business.models.Genre;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class WatchlistMovieEntity {

    private long id;
    private String apiId;
    private String title;
    private String description;
    private String genres;
    private int releaseYear;
    private String imgUrl;
    private int lengthInMinutes;
    private double rating;

    public String genresToString(List<Genre> genres) {
        return genres.stream().map(Enum::toString).collect(Collectors.joining(","));
    }

}
