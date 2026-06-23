package com.stc.library.controller;

import com.stc.library.model.Book;
import com.stc.library.service.BorrowService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RateBookController {

    @FXML
    private Label bookTitleLabel;

    @FXML
    private ChoiceBox<Integer> ratingChoice;

    private BorrowService service;
    private Book book;

    @FXML
    private void initialize() {
        ratingChoice.getItems().addAll(1,2,3,4,5);
        ratingChoice.setValue(5);
    }

    public void setBook(Book book) {
        this.book = book;
        if (bookTitleLabel != null) {
            bookTitleLabel.setText(book.getTitle());
        }
    }

    public void setService(BorrowService service) {
        this.service = service;
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        Integer rating = ratingChoice.getValue();
        if (rating == null) {
            showAlert(Alert.AlertType.WARNING, "No Rating", "Please select a rating between 1 and 5.");
            return;
        }

        try {
            service.rateBook(book.getIsbn(), rating);
            showAlert(Alert.AlertType.INFORMATION, "Thank you", "Your rating has been recorded.");
            // close dialog
            Stage stage = (Stage) bookTitleLabel.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Rating Error", e.getMessage());
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
