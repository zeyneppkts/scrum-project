package com.stc.library.dao;

import java.util.ArrayList;
import java.util.List;

import com.stc.library.model.Book;

public class InMemoryBookDAO implements IBookRepository {

    private List<Book> books = new ArrayList<>();

    @Override
    public void save(Book book) {
        books.add(book);
        System.out.println("Book is saved to memory: " + book.getTitle());
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn() != null && book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn() != null && books.get(i).getIsbn().equals(book.getIsbn())) {
                books.set(i, book);
                System.out.println("Book is updated in memory: " + book.getTitle());
                return;
            }
        }
    }

    @Override
    public Book findByISBN(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    @Override
    public void updateBook(Book book) {
        Book existingBook = findByISBN(book.getIsbn());
        if (existingBook != null) {
            existingBook.setCopyNumber(book.getCopyNumber());
        }
    }

    @Override
    public void deleteByIsbn(String isbn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByIsbn'");
    }
}
