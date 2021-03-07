package com.example.rest_db.dao;


import com.example.rest_db.model.Author;
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
public class AuthorDaoImplementation implements AuthorDao{

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @PersistenceContext
    EntityManager entityManager;


    @Override
    @Transactional
    public void create(Author author) {
        entityManager.persist(author);
    }

    @Override
    @Transactional
    public Optional<Author> read(Integer id) {
        Author a = entityManager.find(Author.class, id);
        return Optional.ofNullable(a);
    }


    @Override
    @Transactional
    public void update(Author author) {

        Optional<Author>  optionalOldAuthor = read(author.getAuthorId());

        if(optionalOldAuthor.isEmpty()) return;


        Author oldAuthor = optionalOldAuthor.get();

        oldAuthor.setName(author.getName());
        oldAuthor.setAddress (author.getAddress());
        oldAuthor.setBooks(author.getBooks());

        entityManager.merge(oldAuthor);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Author a = entityManager.find(Author.class, id);
        entityManager.remove(a);
    }



    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}
