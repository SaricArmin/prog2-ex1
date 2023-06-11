package at.ac.fhcampuswien.fhmdb.data;

public class MovieAPIRequestBuilder {

    private String baseUrl;
    private String query;
    private String id;
    private String genre;
    private String releaseYear;
    private String ratingFrom;

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

    public MovieAPIRequestBuilder genre(String genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public String build() {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (query != null) {
            urlBuilder.append("?query=").append(query);
        }
        if (id != null) {
            urlBuilder.append("&id=").append(id);
        }
        if (genre != null) {
            urlBuilder.append("&genre=").append(genre);
        }
        if (releaseYear != null) {
            urlBuilder.append("&releaseYear=").append(releaseYear);
        }
        if (ratingFrom != null) {
            urlBuilder.append("&ratingFrom=").append(ratingFrom);
        }
        return urlBuilder.toString();
    }
}
