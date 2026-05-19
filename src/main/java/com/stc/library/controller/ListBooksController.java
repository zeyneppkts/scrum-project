package com.stc.library.controller;

import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;
import com.stc.library.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ListBooksController {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> yearColumn;
    @FXML
    private TableColumn<Book, Integer> editionColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, Integer> copiesColumn;
    @FXML
    private TableColumn<Book, Boolean> availableColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;

    private IBookRepository repository = new SQLiteBookDAO();

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copyNumber"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        ObservableList<Book> books = FXCollections.observableArrayList(repository.findAll());
        bookTable.setItems(books);
    }

    @FXML
    private void handleBackToAddBook(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/stc/library/views/AddBook.fxml"));
            Scene scene = new Scene(root, 640, 560);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Library Book Registration");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
