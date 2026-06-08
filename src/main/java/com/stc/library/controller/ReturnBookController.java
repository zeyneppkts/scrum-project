package com.stc.library.controller;

import com.stc.library.model.Book;
import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ReturnBookController {

    @FXML private TextField isbnField;
    @FXML private TextField copiesField;

    private IBookRepository repository = new SQLiteBookDAO();

    @FXML
    private void handleReturnBook(ActionEvent event) {
        String isbn = isbnField.getText();
        String copiesText = copiesField.getText();

        if (isbn == null || isbn.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "ISBN field cannot be empty.");
            return;
        }

        try {
            int copiesToReturn = Integer.parseInt(copiesText);

            Book book = repository.findByISBN(isbn);

            if (book == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No book found with ISBN: " + isbn);
                return;
            }

            book.returnABook(copiesToReturn);

            repository.updateBook(book);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book successfully returned! Stock updated.");

            isbnField.clear();
            copiesField.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number for copies.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Logic Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}