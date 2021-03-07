package com.example.rest_db.web;


import com.example.rest_db.model.Author;
import com.example.rest_db.model.Book;
import com.example.rest_db.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;


@RestController
public class LibraryController {

    @Autowired
    private LibraryService service;

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @PostMapping("/author")
    public void authorCreate(@RequestBody Author author){
        this.service.authorCreate(author);

    }

    @GetMapping("/author/{id}")
    public Optional<Author> authorRead(@PathVariable Integer id){
        return this.service.authorRead(id);
    }

    @PutMapping("/author")
    public void authorUpdate(@RequestBody Author author){
        this.service.authorUpdate(author);
    }

    @DeleteMapping("/author/{id}")
    public void authorDelete(@PathVariable Integer id){
        this.service.authorDelete(id);
    }


//  book
    @PostMapping("/books")
    public void bookCreate(@RequestBody Book book){
        this.service.bookCreate(book);
    }

    @GetMapping("/books/{id}")
    public Optional<Book> bookRead(@PathVariable Integer id){
        return this.service.bookRead(id);
    }

    @PutMapping("/books")
    public void bookUpdate(@RequestBody Book book){
        this.service.bookUpdate(book);
    }

    @DeleteMapping("/books/{id}")
    public void bookDelete(@PathVariable Integer id){
        this.service.bookDelete(id);
    }

//    @GetMapping("/booksBy/{name}")
//    public Set<Book> booksBy(@PathVariable String name){
//        Optional<Author> maybeAuthor = authorRead(name);
//
//        if(maybeAuthor.isEmpty()) return Set.of();
//
//        return maybeAuthor.get().getBooks();
//    }

}
