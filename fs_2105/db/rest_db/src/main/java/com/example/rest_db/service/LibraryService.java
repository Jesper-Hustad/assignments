package com.example.rest_db.service;

import com.example.rest_db.model.Author;
import com.example.rest_db.model.Book;
import com.example.rest_db.repo.LibraryRepositoryImplementation;
import com.example.rest_db.web.LibraryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);


    @Autowired
    private LibraryRepositoryImplementation repository;

    //  author

    public void authorCreate(Author author){
        logger.info("Creating author " + author.getName());
        this.repository.authorCreate(author);

    }

    public Optional<Author> authorRead(Integer id){
        logger.info("Reading author " + id);
        return this.repository.authorRead(id);
    }

    public void authorUpdate(Author author){
        logger.info("Updating author " + author.getName());
        this.repository.authorUpdate(author);
    }

    public void authorDelete(Integer id){
        logger.info("Deleting author " + id);
        this.repository.authorDelete(id);
    }


//  book

    public void bookCreate(Book book){
        this.repository.bookCreate(book);
    }

    public Optional<Book> bookRead(Integer id){
        return repository.bookRead(id);
    }

    public void bookUpdate(Book book){
        repository.bookUpdate(book);
    }

    public void bookDelete(Integer id){
        repository.bookDelete(id);
    }

}
