package at.ac.fhcampuswien.fhmdb.data;

public interface Observer {
    void update(Observable observable, Object arg);
}