package no.ntnu.idatt2105.l4.demo.dao;

import no.ntnu.idatt2105.l4.demo.model.Book;

import java.util.Optional;

public interface BookDao {

    //  CRUD operations

    public void create(Book book);

    public Optional<Book> read(String id);

    public void update(Book book);

    public void delete(String id);

}
