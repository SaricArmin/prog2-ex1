package at.ac.fhcampuswien.fhmdb.business.models;

import at.ac.fhcampuswien.fhmdb.business.controller.HomeController;
import javafx.collections.ObservableList;

abstract public class SortState {

    protected HomeController homeController;

    public SortState(HomeController homeController) {
        this.homeController = homeController;
    }

    abstract public void sortMovies(ObservableList<Movie> movies);
    abstract public String clickSort(ObservableList<Movie> movies);
}
