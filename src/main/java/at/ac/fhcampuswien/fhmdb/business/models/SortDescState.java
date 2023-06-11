package at.ac.fhcampuswien.fhmdb.business.models;

import at.ac.fhcampuswien.fhmdb.business.controller.HomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class SortDescState extends SortState {

    private static final String SORT_DESC = "Sort ↓";

    public SortDescState(HomeController homeController) {
        super(homeController);
    }

    @Override
    public void sortMovies(ObservableList<Movie> movies) {
        FXCollections.sort(movies, Comparator.comparing(Movie::getTitle).reversed());
    }

    @Override
    public String clickSort(ObservableList<Movie> movies) {
        FXCollections.sort(movies, Comparator.comparing(Movie::getTitle));
        homeController.changeSortState(new SortAscState(homeController));
        return SORT_DESC;
    }
}
