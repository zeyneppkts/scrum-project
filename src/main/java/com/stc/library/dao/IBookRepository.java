package com.stc.library.dao;

import com.stc.library.model.Book;
import java.util.List;

public interface IBookRepository {

    void save(Book book);

    List<Book> findAll();
}
