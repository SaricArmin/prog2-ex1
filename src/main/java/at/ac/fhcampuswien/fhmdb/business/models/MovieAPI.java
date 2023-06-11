package at.ac.fhcampuswien.fhmdb.business.models;

import at.ac.fhcampuswien.fhmdb.data.MovieAPIRequestBuilder;
import okhttp3.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MovieAPI {
    // mainURL of api
    static String url = "https://prog2.fh-campuswien.ac.at/movies";

    //String testurl = new MovieAPIRequestBuilder(url + "/" + id).build(); //vl falsche klasse nicht sicher wo sein sollte
    /*String asdf = new MovieAPIRequestBuilder(url).query("suche")
            .genre("ACTION")
            .releaseYear("2012")
            .ratingFrom("8.3")
            .build();*/ //braucht testcases
    public List<Movie> fetchMovies() {

        return getMovies(url);
    }

    public List<Movie> searchMovies(Map<String, String> parameters) {
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (!url.contains("?"))
                    url = url.concat("?").concat(entry.getKey()).concat("=").concat(entry.getValue());
                else
                    url = url.concat("&").concat(entry.getKey()).concat("=").concat(entry.getValue());
            }
        }
        return getMovies(url);
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