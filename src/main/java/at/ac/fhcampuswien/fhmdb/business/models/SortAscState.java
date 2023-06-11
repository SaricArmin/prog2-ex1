package at.ac.fhcampuswien.fhmdb.business.models;

import at.ac.fhcampuswien.fhmdb.business.controller.HomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class SortAscState extends SortState {

    private static final String SORT_ASC = "Sort ↑";

    public SortAscState(HomeController homeController) {
        super(homeController);
    }

    @Override
    public void sortMovies(ObservableList<Movie> movies) {
        FXCollections.sort(movies, Comparator.comparing(Movie::getTitle));
    }

    @Override
    public String clickSort(ObservableList<Movie> movies) {
        FXCollections.sort(movies, Comparator.comparing(Movie::getTitle).reversed());
        homeController.changeSortState(new SortDescState(homeController));
        return SORT_ASC;
    }
}
