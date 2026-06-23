package com.stc.library.controller;

import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;
import com.stc.library.model.Book;
import com.stc.library.service.BorrowService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Modality;


import java.io.IOException;
import java.util.Optional;

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
    private TableColumn<Book, Integer> borrowCountColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, Double> ratingColumn;

    private final IBookRepository repository = new SQLiteBookDAO();
    private final BorrowService borrowService = new BorrowService(repository);

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        editionColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copyNumber"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        borrowCountColumn.setCellValueFactory(new PropertyValueFactory<>("borrowCount"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));

        refreshTable();
    }

    private void refreshTable() {
        ObservableList<Book> books = FXCollections.observableArrayList(repository.findAll());
        bookTable.setItems(books);
    }

    @FXML
    private void handleBorrowBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to borrow.");
            return;
        }

        try {
            Book updatedBook = borrowService.borrowBook(selectedBook.getIsbn());
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "You borrowed \"" + updatedBook.getTitle() + "\".\n"
                            + "Remaining copies: " + updatedBook.getCopyNumber() + "\n"
                            + "Total times borrowed: " + updatedBook.getBorrowCount());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Borrow Error", e.getMessage());
        }
    }

    @FXML
    private void handleReduceCopies(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to reduce copies.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Reduce Copies");
        dialog.setHeaderText("Decrease copies for \"" + selectedBook.getTitle() + "\"");
        dialog.setContentText("Enter number of copies to remove:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }

        try {
            int removeCount = Integer.parseInt(result.get().trim());
            selectedBook.reduceCopies(removeCount);
            repository.updateBook(selectedBook);
            refreshTable();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Removed " + removeCount + " copies from \"" + selectedBook.getTitle() + "\".\n"
                            + "Remaining copies: " + selectedBook.getCopyNumber());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid integer for copies.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Reduction Error", e.getMessage());
        }
    }

    @FXML
    private void handleRemoveBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to remove.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete selected book?");
        confirmation.setContentText("Are you sure you want to permanently remove \"" + selectedBook.getTitle() + "\" from the library?");

        Optional<ButtonType> confirmationResult = confirmation.showAndWait();
        if (confirmationResult.isEmpty() || confirmationResult.get() != ButtonType.OK) {
            return;
        }

        repository.deleteByIsbn(selectedBook.getIsbn());
        refreshTable();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Book removed successfully.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    @FXML
    private void handleRateBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to rate.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/stc/library/views/RateBook.fxml"));
            Parent root = loader.load();

            RateBookController controller = loader.getController();
            controller.setBook(selectedBook);
            controller.setService(borrowService);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Rate Book");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
