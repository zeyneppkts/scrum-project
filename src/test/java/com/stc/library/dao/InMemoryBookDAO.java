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
}
