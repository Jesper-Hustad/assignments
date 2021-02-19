package no.ntnu.idatt2105.l4.demo.repo;

import no.ntnu.idatt2105.l4.demo.dao.AuthorDaoImplementation;
import no.ntnu.idatt2105.l4.demo.model.Author;
import no.ntnu.idatt2105.l4.demo.model.Book;

import java.util.Optional;

public interface LibraryRepository {

//  author

    public void authorCreate(Author author);

    public Optional<Author> authorRead(String id);

    public void authorUpdate(Author author);

    public void authorDelete(String id);


//  book

    public void bookCreate(Book book);

    public Optional<Book> bookRead(String id);

    public void bookUpdate(Book book);

    public void bookDelete(String id);

}
