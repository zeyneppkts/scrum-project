package com.stc.library.controller;

import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;
import com.stc.library.model.Book; // Veritabanı gelene kadar bunu kullanıyoruz

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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

    // Arayüz (Interface) üzerinden geçici belleğe bağlıyoruz
    private IBookRepository repository = new SQLiteBookDAO();

    @FXML
    private void handleSaveBook(ActionEvent event) {
        try {
            // Metin kutularından verileri al
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            String isbn = isbnField.getText();

            // Sayısal değerleri dönüştür (Eğer harf girilirse NumberFormatException fırlatır)
            int year = Integer.parseInt(yearField.getText());
            int edition = Integer.parseInt(editionField.getText());
            int copies = Integer.parseInt(copiesField.getText());

            // Yeni kitabı oluştur. Senin Book sınıfındaki validasyonlar burada devreye girecek!
            Book newBook = new Book(title, author, year, edition, publisher, copies, true, isbn);

            // Kitabı kaydet
            repository.save(newBook);

            // Başarı mesajı göster
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book successfully registered!");

            // Kayıttan sonra form alanlarını temizle
            clearFields();

        } catch (NumberFormatException e) {
            // Kullanıcı sayı yerine harf girdiyse
            showAlert(Alert.AlertType.ERROR, "Input Error", "Year, Edition, and Copies must be valid numbers.");
        } catch (IllegalArgumentException e) {
            // Senin Book sınıfından gelen özel hata mesajları (örn: "Copy number cannot be negative.")
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
}
