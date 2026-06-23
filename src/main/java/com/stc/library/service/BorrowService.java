package com.stc.library.service;

import com.stc.library.dao.IBookRepository;
import com.stc.library.model.Book;

/**
 * Handles the borrowing of books. It verifies availability, decreases the
 * available copies, updates the borrow statistics and persists the changes
 * through the {@link IBookRepository}.
 */
public class BorrowService {

    private final IBookRepository repository;

    public BorrowService(IBookRepository repository) {
        this.repository = repository;
    }

    /**
     * Borrows a single copy of the book identified by the given ISBN.
     *
     * @param isbn the ISBN of the book to borrow
     * @return the updated {@link Book}
     * @throws IllegalArgumentException if the book does not exist or has no
     *                                  copies available
     */
    public Book borrowBook(String isbn) {
        return borrowBook(isbn, 1);
    }

    /**
     * Borrows the given number of copies of the book identified by the ISBN.
     *
     * @param isbn     the ISBN of the book to borrow
     * @param quantity how many copies to borrow (must be positive)
     * @return the updated {@link Book}
     * @throws IllegalArgumentException if the book does not exist, the quantity
     *                                  is not positive, or there are not enough
     *                                  copies available
     */
    public Book borrowBook(String isbn, int quantity) {
        Book book = repository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found for ISBN: " + isbn);
        }

        // Validates availability and updates copies + borrow statistics.
        book.borrowABook(quantity);

        // Persist the new state (decreased copies, availability, statistics).
        repository.update(book);

        return book;
    }

    /**
     * Adds a rating (1-5) to the book identified by ISBN and persists it.
     * @param isbn the book ISBN
     * @param rating integer rating between 1 and 5
     * @return updated Book
     */
    public Book rateBook(String isbn, int rating) {
        Book book = repository.findByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("Book not found for ISBN: " + isbn);
        }

        book.addRating(rating);
        repository.update(book);
        return book;
    }
}
