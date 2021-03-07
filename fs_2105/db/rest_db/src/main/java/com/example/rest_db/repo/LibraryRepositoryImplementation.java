package com.example.rest_db.repo;


import com.example.rest_db.dao.AuthorDaoImplementation;
import com.example.rest_db.dao.BookDaoImplementation;
import com.example.rest_db.model.Author;
import com.example.rest_db.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public Optional<Author> authorRead(Integer id) {
        return this.authorDaoImplementation.read(id);
    }

    @Override
    public void authorUpdate(Author author) {
        this.authorDaoImplementation.update(author);
    }

    @Override
    public void authorDelete(Integer id) {
        this.authorDaoImplementation.delete(id);
    }

//  Book

    @Override
    public void bookCreate(Book book) {
        this.bookDaoImplementation.create(book);
    }

    @Override
    public Optional<Book> bookRead(Integer id) {
        return this.bookDaoImplementation.read(id);
    }

    @Override
    public void bookUpdate(Book book) {
        this.bookDaoImplementation.update(book);
    }

    @Override
    public void bookDelete(Integer id) {
        this.bookDaoImplementation.delete(id);
    }

    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}


