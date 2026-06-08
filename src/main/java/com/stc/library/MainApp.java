package com.stc.library;

import com.stc.library.dao.DatabaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseConnection.initializeDatabase();
        Parent addBookRoot = FXMLLoader.load(getClass().getResource("/com/stc/library/views/AddBook.fxml"));
        Tab addBookTab = new Tab("Register a New Book", addBookRoot);
        addBookTab.setClosable(false);

        Parent returnBookRoot = FXMLLoader.load(getClass().getResource("/com/stc/library/views/ReturnBook.fxml"));
        Tab returnBookTab = new Tab("Return a Book", returnBookRoot);
        returnBookTab.setClosable(false);

        Parent viewBooksRoot = FXMLLoader.load(getClass().getResource("/com/stc/library/views/ListBooks.fxml"));
        Tab viewBooksTab = new Tab("View All Books", viewBooksRoot);
        viewBooksTab.setClosable(false);

        TabPane rootPane = new TabPane();
        rootPane.getTabs().addAll(addBookTab, viewBooksTab, returnBookTab);

        Scene scene = new Scene(rootPane, 700, 600);

        stage.setTitle("STC Library Management System - Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
