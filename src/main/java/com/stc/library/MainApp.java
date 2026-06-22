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

        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("/com/stc/library/views/ListBooks.fxml"));
        Parent listBooksRoot = listLoader.load();
        Tab listBooksTab = new Tab("View All Books", listBooksRoot);
        listBooksTab.setClosable(false);

        com.stc.library.controller.ListBooksController listController = listLoader.getController();
        listBooksTab.setOnSelectionChanged(event -> {
            if (listBooksTab.isSelected()) {
                listController.refreshTable();
            }
        });

        FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("/com/stc/library/views/BookStatistics.fxml"));
        Parent statsRoot = statsLoader.load();
        Tab statsTab = new Tab("Book Statistics", statsRoot);
        statsTab.setClosable(false);

        com.stc.library.controller.BookStatisticsController statsController = statsLoader.getController();
        statsTab.setOnSelectionChanged(event -> {
            if (statsTab.isSelected()) {
                statsController.refreshStatistics();
            }
        });
        TabPane rootPane = new TabPane();
        rootPane.getTabs().addAll(addBookTab, returnBookTab, listBooksTab, statsTab);

        Scene scene = new Scene(rootPane, 900, 650);

        stage.setTitle("STC Library Management System - Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
