package com.stc.library;

import com.stc.library.dao.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseConnection.initializeDatabase();
        Parent root = FXMLLoader.load(getClass().getResource("/com/stc/library/views/AddBook.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Library Book Registration");
        stage.setScene(scene);
        stage.setWidth(640);
        stage.setHeight(560);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
