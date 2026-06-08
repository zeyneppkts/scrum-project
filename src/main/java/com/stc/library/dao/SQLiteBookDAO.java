package com.stc.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.stc.library.model.Book;

public class SQLiteBookDAO implements IBookRepository {

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, published_year, edition, publisher, copies, is_available, isbn, borrow_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPublicationYear());
            pstmt.setInt(4, book.getEdition());
            pstmt.setString(5, book.getPublisher());
            pstmt.setInt(6, book.getCopyNumber());
            pstmt.setBoolean(7, book.isAvailable());
            pstmt.setString(8, book.getIsbn());
            pstmt.setInt(9, book.getBorrowCount());

            pstmt.executeUpdate();
            System.out.println("Success: Book saved to database: " + book.getTitle());

        } catch (SQLException e) {
            System.err.println("Database Error: Failed to save the book. " + e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT title, author, published_year, edition, publisher, copies, is_available, isbn, borrow_count FROM books";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookList.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to load the book list. " + e.getMessage());
        }

        return bookList;
    }

    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT title, author, published_year, edition, publisher, copies, is_available, isbn, borrow_count FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to find the book. " + e.getMessage());
        }

        return null;
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET copies = ?, is_available = ?, borrow_count = ? WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getCopyNumber());
            pstmt.setBoolean(2, book.isAvailable());
            pstmt.setInt(3, book.getBorrowCount());
            pstmt.setString(4, book.getIsbn());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Database Error: No book found to update for ISBN " + book.getIsbn());
            } else {
                System.out.println("Success: Book updated in database: " + book.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to update the book. " + e.getMessage());
        }
    }

    @Override
    public Book findByISBN(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("published_year"),
                    rs.getInt("edition"),
                    rs.getString("publisher"),
                    rs.getInt("copies"),
                    rs.getBoolean("is_available"),
                    rs.getString("isbn")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to find book by ISBN. " + e.getMessage());
        }
        
        return null; 
    }

    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE books SET copies = ?, is_available = ? WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, book.getCopyNumber());
            pstmt.setBoolean(2, book.isAvailable());
            pstmt.setString(3, book.getIsbn());
            
            pstmt.executeUpdate();
            System.out.println("Success: Book stock updated for ISBN: " + book.getIsbn());
            
        } catch (SQLException e) {
            System.err.println("Database Error: Failed to update book. " + e.getMessage());
        }
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book(
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("published_year"),
                rs.getInt("edition"),
                rs.getString("publisher"),
                rs.getInt("copies"),
                rs.getBoolean("is_available"),
                rs.getString("isbn")
        );
        book.setBorrowCount(rs.getInt("borrow_count"));
        return book;
    }
}
