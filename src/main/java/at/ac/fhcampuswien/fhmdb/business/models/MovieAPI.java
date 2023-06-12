package at.ac.fhcampuswien.fhmdb.business.models;

import at.ac.fhcampuswien.fhmdb.data.MovieAPIRequestBuilder;
import okhttp3.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MovieAPI {
    static String url = "https://prog2.fh-campuswien.ac.at/movies";

    public List<Movie> fetchMovies() {
        return getMovies(url);
    }

    public List<Movie> searchMovies(String search, Genre genre, Double rating, Integer releaseYear) {
        String filteredUrl = new MovieAPIRequestBuilder(url).query(search)
                .genre(genre.name())
                .ratingFrom(rating.toString())
                .releaseYear(releaseYear.toString())
                .build();

        return getMovies(filteredUrl);
    }

    private static List<Movie> getMovies(String url) {
        List<Movie> movies = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "MyApp/1.0")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            movies = objectMapper.readValue(responseBody, new TypeReference<List<Movie>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }
}