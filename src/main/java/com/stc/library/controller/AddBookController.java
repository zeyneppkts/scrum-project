package com.stc.library.controller;

import java.io.IOException;

import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;
import com.stc.library.model.Book;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField editionField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField copiesField;
    @FXML
    private TextField isbnField;

    private IBookRepository repository = new SQLiteBookDAO();

    @FXML
    private void handleSaveBook(ActionEvent event) {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            String isbn = isbnField.getText();

            int year = Integer.parseInt(yearField.getText());
            int edition = Integer.parseInt(editionField.getText());
            int copies = Integer.parseInt(copiesField.getText());

            Book newBook = new Book(title, author, year, edition, publisher, copies, true, isbn);

            repository.save(newBook);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book successfully registered!");

            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Year, Edition, and Copies must be valid numbers.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        yearField.clear();
        editionField.clear();
        publisherField.clear();
        copiesField.clear();
        isbnField.clear();
    }

    @FXML
    private void handleShowBookList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/stc/library/views/ListBooks.fxml"));
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Library Books List");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not open the book list screen.");
        }
    }
}
