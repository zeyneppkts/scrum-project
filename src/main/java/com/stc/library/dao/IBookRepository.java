package com.stc.library.dao;

import com.stc.library.model.Book;
import java.util.List;

public interface IBookRepository {

    void save(Book book);

    List<Book> findAll();

    /**
     * Finds a single book by its ISBN.
     *
     * @param isbn the ISBN to look up
     * @return the matching {@link Book}, or {@code null} if none exists
     */
    Book findByIsbn(String isbn);

    /**
     * Persists changes (available copies, availability and borrow statistics)
     * of an already stored book. The book is matched by its ISBN.
     *
     * @param book the book whose current state should be saved
     */
    void update(Book book);

    Book findByISBN(String isbn);
    void updateBook(Book book);
    void deleteByIsbn(String isbn);
}
