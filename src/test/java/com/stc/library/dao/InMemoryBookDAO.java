package com.stc.library.dao;

import com.stc.library.model.Book;
import java.util.ArrayList;
import java.util.List;

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
}
