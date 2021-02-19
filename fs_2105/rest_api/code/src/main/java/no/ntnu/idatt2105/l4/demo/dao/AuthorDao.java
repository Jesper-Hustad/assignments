package no.ntnu.idatt2105.l4.demo.dao;

import no.ntnu.idatt2105.l4.demo.model.Author;

import java.util.Optional;

public interface AuthorDao {


    //  CRUD operations

    public void create(Author author);

    public Optional<Author> read(String id);

    public void update(Author author);

    public void delete(String id);
}
