package no.ntnu.idatt2105.l4.demo.repo;

import no.ntnu.idatt2105.l4.demo.dao.AuthorDaoImplementation;
import no.ntnu.idatt2105.l4.demo.dao.BookDaoImplementation;
import no.ntnu.idatt2105.l4.demo.model.Author;
import no.ntnu.idatt2105.l4.demo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class LibraryRepositoryImplementation implements LibraryRepository{

    @Autowired
    private BookDaoImplementation bookDaoImplementation;

    @Autowired
    private AuthorDaoImplementation authorDaoImplementation;

//  Author

    @Override
    public void authorCreate(Author author) {
        this.authorDaoImplementation.create(author);
    }

    @Override
    public Optional<Author> authorRead(String id) {
        Optional<Author> author = this.authorDaoImplementation.read(id);
        if(author.isEmpty()) return author;

//      Fill many to many
        Author realAuthor = author.get();
        ArrayList<Book> books = toList(realAuthor.getBooks().stream().map(i -> bookDaoImplementation.read(i)).filter(Optional::isPresent).map(Optional::get));

        realAuthor.setBooks_list(books);

        return Optional.of(realAuthor);
    }

    @Override
    public void authorUpdate(Author author) {
        this.authorDaoImplementation.update(author);
    }

    @Override
    public void authorDelete(String id) {
        this.authorDaoImplementation.delete(id);
    }

//  Book

    @Override
    public void bookCreate(Book book) {
        this.bookDaoImplementation.create(book);
    }

    @Override
    public Optional<Book> bookRead(String id) {
        Optional<Book> book = this.bookDaoImplementation.read(id);
        if(book.isEmpty()) return book;

//      Fill many to many
        Book realBook = book.get();
        ArrayList<Author> authors = toList(realBook.getAuthors().stream().map(i -> authorDaoImplementation.read(i)).filter(Optional::isPresent).map(Optional::get));

        realBook.setAuthors_list(authors);

        return Optional.of(realBook);
    }

    @Override
    public void bookUpdate(Book book) {
        this.bookDaoImplementation.update(book);
    }

    @Override
    public void bookDelete(String id) {
        this.bookDaoImplementation.delete(id);
    }

    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}
