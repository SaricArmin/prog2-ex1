module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires okhttp3;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires ormlite.jdbc;
    requires lombok;

    exports at.ac.fhcampuswien.fhmdb.presentation;
    exports at.ac.fhcampuswien.fhmdb.business.models;
    exports at.ac.fhcampuswien.fhmdb.business.controller;
    opens at.ac.fhcampuswien.fhmdb.business.controller to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.business.models to com.fasterxml.jackson.databind;
    opens at.ac.fhcampuswien.fhmdb.presentation to javafx.fxml;

}