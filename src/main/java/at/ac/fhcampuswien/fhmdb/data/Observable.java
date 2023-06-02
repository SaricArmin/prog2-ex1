package at.ac.fhcampuswien.fhmdb.data;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(Object arg);
}

