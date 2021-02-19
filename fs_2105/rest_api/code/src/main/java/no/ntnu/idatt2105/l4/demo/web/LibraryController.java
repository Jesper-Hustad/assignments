package no.ntnu.idatt2105.l4.demo.web;

import no.ntnu.idatt2105.l4.demo.model.Author;
import no.ntnu.idatt2105.l4.demo.model.Book;
import no.ntnu.idatt2105.l4.demo.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


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
    public Optional<Author> authorRead(@PathVariable String id){
        return this.service.authorRead(id);
    }

    @PutMapping("/author")
    public void authorUpdate(@RequestBody Author author){
        this.service.authorUpdate(author);
    }

    @DeleteMapping("/author/{id}")
    public void authorDelete(@PathVariable String id){
        this.service.authorDelete(id);
    }


//  book
    @PostMapping("/book")
    public void bookCreate(@RequestBody Book book){
        this.service.bookCreate(book);
    }

    @GetMapping("/book/{id}")
    public Optional<Book> bookRead(@PathVariable String id){
        return this.service.bookRead(id);
    }

    @PutMapping("/book")
    public void bookUpdate(@RequestBody Book book){
        this.service.bookUpdate(book);
    }

    @DeleteMapping("/book/{id}")
    public void bookDelete(@PathVariable String id){
        this.service.bookDelete(id);
    }

    @GetMapping("/booksBy/{name}")
    public ArrayList<Book> booksBy(@PathVariable String name){
        Optional<Author> maybeAuthor = authorRead(name);

        if(maybeAuthor.isEmpty()) return new ArrayList<Book>();

        return maybeAuthor.get().getBooks_list();
    }

}
