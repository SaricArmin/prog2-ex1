package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MovieAPI {
    // mainURL of api
    static String mainURL = "https://prog2.fh-campuswien.ac.at";

    public static List<Movie> fetchMovies() {
        List<Movie> movies = new ArrayList<>();
        String url = mainURL + "/movies";

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