package com.example.rest_db.dao;


import com.example.rest_db.model.Book;
import com.example.rest_db.web.LibraryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class BookDaoImplementation implements BookDao{

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @PersistenceContext
    EntityManager entityManager;

    //  Simulated db
//    private ArrayList<Book> db = new ArrayList<>();

    @Override
    @Transactional
    public void create(Book book) {
        entityManager.persist(book);
    }

    @Override
    @Transactional
    public Optional<Book> read(Integer id) {
        Book b = entityManager.find(Book.class, id);
//        entityManager.detach(b);
        return Optional.ofNullable(b);
    }


    @Override
    @Transactional
    public void update(Book book) {

        Optional<Book>  optionalOldBook = read(book.getBookId());

        if(optionalOldBook.isEmpty()) return;

        Book oldBook = optionalOldBook.get();

        oldBook.setTitle(book.getTitle());
        oldBook.setAuthors(book.getAuthors());

        entityManager.merge(oldBook);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Book b = entityManager.find(Book.class, id);
        entityManager.remove(b);
    }

    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

}







