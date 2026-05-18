package com.stc.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.stc.library.model.Book;

public class SQLiteBookDAO implements IBookRepository {

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, published_year, edition, publisher, copies, is_available, isbn) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPublicationYear());
            pstmt.setInt(4, book.getEdition());
            pstmt.setString(5, book.getPublisher());
            pstmt.setInt(6, book.getCopyNumber());
            pstmt.setBoolean(7, book.isAvailable());
            pstmt.setString(8, book.getIsbn());

            pstmt.executeUpdate();
            System.out.println("Success: Book saved to database: " + book.getTitle());

        } catch (SQLException e) {
            System.err.println("Database Error: Failed to save the book. " + e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        return null;
    }
}
