package com.example.rest_db.dao;


import com.example.rest_db.model.Book;

import java.util.Optional;

public interface BookDao {

    //  CRUD operations

    public void create(Book book);

    public Optional<Book> read(Integer id);

    public void update(Book book);

    public void delete(Integer id);

}
