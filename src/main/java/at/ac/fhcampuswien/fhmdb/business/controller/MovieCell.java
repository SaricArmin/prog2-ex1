package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.data.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.data.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label(); //to show genres
    private final Label rating = new Label(); //to show the rating

    private final Button addToWL = createWishlistButton();

    private final VBox layout = new VBox(title, detail, genres, rating, addToWL);

    private ClickEventHandler<Movie> clickHandler;

    HomeController controller;
    WatchlistRepository repository = new WatchlistRepository();



//    @FXML
//    public JFXButton watchlistBtn;

    public Button createWishlistButton ()
    {
        Button addToWatchlist = new Button();
        addToWatchlist.getStyleClass().add("background-yellow"); //added
        addToWatchlist.setText("Add to Watchlist");




        return addToWatchlist;
    }

    // Exercise 3 Business Layer
    public MovieCell() {


    }
    public MovieCell(ClickEventHandler<Movie> addToWatchlistClicked) {
        super();
        this.clickHandler = addToWatchlistClicked;
        addToWL.setOnMouseClicked(mouseEvent -> {
            addToWatchlistClicked.onClick(getItem());
            if (addToWL.getText().equals("Add to Watchlist")){
                try {
                    repository.addToWatchlist(getItem());
                    addToWL.setText("Delete from Watchlist");
                } catch (SQLException e) {
                    showExceptionAlert("while adding item to watchlist", new DatabaseException("while adding item to watchlist", e));
                }
            } else if (addToWL.getText().equals("Delete from Watchlist")) {
                try {
                    repository.removeFromWatchlist(getItem());
                    addToWL.setText("Add to Watchlist");
                } catch (SQLException e) {
                    showExceptionAlert("while removing item from watchlist", new DatabaseException("Error while removing item from watchlist", e));
                }
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
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
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
        alert.showAndWait();
    }
}