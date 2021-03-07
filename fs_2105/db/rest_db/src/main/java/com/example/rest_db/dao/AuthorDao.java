package com.example.rest_db.dao;


import com.example.rest_db.model.Author;

import java.util.Optional;

public interface AuthorDao {


    //  CRUD operations

    public void create(Author author);

    public Optional<Author> read(Integer id);

    public void update(Author author);

    public void delete(Integer id);
}
