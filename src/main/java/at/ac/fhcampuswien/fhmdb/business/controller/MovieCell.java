package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.data.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.data.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label(); //to show genres
    private final Label rating = new Label(); //to show the rating
    private final Button addToWL = createWishlistButton();
    private final VBox layout = new VBox(title, detail, genres, rating, addToWL);
    private static final String ADD_TO_WATCHLIST = "Add to Watchlist";
    private static final String DELETE_FROM_WATCHLIST = "Delete from Watchlist";
    HomeController controller = new HomeController();

    public Button createWishlistButton ()
    {
        Button addToWatchlist = new Button();
        addToWatchlist.getStyleClass().add("background-yellow"); //added
        addToWatchlist.setText(ADD_TO_WATCHLIST);
        return addToWatchlist;
    }

    // Exercise 3 Business Layer
    public MovieCell() {
    }
    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();
        addToWL.setOnMouseClicked(mouseEvent -> {
            addToWatchlistClicked.onClick(getItem());
            if (addToWL.getText().equals(ADD_TO_WATCHLIST)) {
                controller.addMovie(getItem());
                updateItem(getItem(), false);
            } else if (addToWL.getText().equals(DELETE_FROM_WATCHLIST)) {
                controller.removeMovie(getItem());
                updateItem(getItem(), false);
            }
        });
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle() + " ("+ movie.getReleaseYear() + ")");
            genres.setText(movie.getGenres().toString().substring(1, movie.getGenres().toString().length() -1));
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            rating.setText("Rating: " + movie.getRating());
            // needs overhaul
            // color scheme
            title.getStyleClass().add("text-yellow");
            genres.getStyleClass().add("text-white");
            detail.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(Font.font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }

        if (controller.getWatchList().contains(movie)) {
            addToWL.setText(DELETE_FROM_WATCHLIST);
        } else {
            addToWL.setText(ADD_TO_WATCHLIST);
        }
    }

    public static void showExceptionAlert(String origin, Exception ex) {
        // Create a new alert  with the given error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error " + origin);
        alert.setContentText("The following error occurred: " + ex.getMessage());

        Label label = new Label("Exception stacktrace:");
        TextArea textArea = new TextArea(ex.getMessage());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // set the additional info to the Alert dialog
        alert.getDialogPane().setExpandableContent(expContent);

        //stays open until client closes it
        alert.show();
    }
}