package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.business.models.Genre;

public class MovieAPIRequestBuilder {

    private String baseUrl;
    private String query;
    private String id;
    private Genre genre;
    private Integer releaseYear;
    private Double ratingFrom;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public MovieAPIRequestBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(Double ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public String build() {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        boolean hasParameters = false;
        if (query != null) {
            urlBuilder.append("?query=").append(query);
            hasParameters = true;
        }
        if (id != null) {
            urlBuilder.append(hasParameters ? "&id=" : "?id=").append(id);
            hasParameters = true;
        }
        if (genre != null) {
            urlBuilder.append(hasParameters ? "&genre=" : "?genre=").append(genre);
            hasParameters = true;
        }
        if (releaseYear != null) {
            urlBuilder.append(hasParameters ? "&releaseYear=" : "?releaseYear=").append(releaseYear);
            hasParameters = true;
        }
        if (ratingFrom != null) {
            urlBuilder.append(hasParameters ? "&ratingFrom=" : "?ratingFrom=").append(ratingFrom);
        }
        return urlBuilder.toString();
    }
}
