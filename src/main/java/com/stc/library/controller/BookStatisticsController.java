package com.stc.library.controller;

import java.util.List;

import com.stc.library.dao.IBookRepository;
import com.stc.library.dao.SQLiteBookDAO;
import com.stc.library.model.Book;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class BookStatisticsController {

    @FXML
    private BarChart<String, Number> borrowChart;
    @FXML
    private BarChart<String, Number> ratingChart;

    private IBookRepository repository = new SQLiteBookDAO();

    @FXML
    public void initialize() {
        loadBorrowStatistics();
        loadRatingStatistics();
    }

    @FXML
    public void refreshStatistics() {
        borrowChart.getData().clear();
        ratingChart.getData().clear();

        loadBorrowStatistics();
        loadRatingStatistics();
    }

    private void loadBorrowStatistics() {
        List<Book> topBorrowed = repository.getTopBorrowedBooks(5);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Borrow Count");

        for (Book book : topBorrowed) {
            series.getData().add(new XYChart.Data<>(book.getTitle(), book.getBorrowCount()));
        }

        borrowChart.getData().add(series);
    }

    private void loadRatingStatistics() {
        List<Book> topRated = repository.getTopRatedBooks(5);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Rating");

        for (Book book : topRated) {
            series.getData().add(new XYChart.Data<>(book.getTitle(), book.getAverageRating()));
        }

        ratingChart.getData().add(series);
    }
}
