package at.ac.fhcampuswien.fhmdb.business.controller;

import at.ac.fhcampuswien.fhmdb.business.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genres = new Label(); //to show genres
    private final Label rating = new Label(); //to show the rating

    private final Button addToWL = createWishlistButton();

    private final VBox layout = new VBox(title, detail, genres, rating, addToWL);

    private ClickEventHandler<Movie> clickHandler;

    HomeController controller;


//    @FXML
//    public JFXButton watchlistBtn;

    public Button createWishlistButton ()
    {
        Button addToWatchlist = new Button();
        addToWatchlist.getStyleClass().add("background-yellow"); //added
        addToWatchlist.setText("Add to Watchlist");


        addToWatchlist.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // Hier Methode aufufen was passieren soll, wenn der Button geklickt wird
                //clickHandler.onClick();
            }
        });

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
            if (!HomeController.watchList.isEmpty()) {
                for (Movie movie: HomeController.watchList) {
                    if (movie.id.equals(getItem().id)) {
                        addToWL.setText("Delete from Watchlist");
                    }
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

            rating.setText("Rating: " + String.valueOf(movie.getRating()));
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
}