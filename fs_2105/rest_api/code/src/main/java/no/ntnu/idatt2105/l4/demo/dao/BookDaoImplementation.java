package no.ntnu.idatt2105.l4.demo.dao;

import no.ntnu.idatt2105.l4.demo.model.Book;
import no.ntnu.idatt2105.l4.demo.web.LibraryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class BookDaoImplementation implements BookDao{

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    //  Simulated db
    private ArrayList<Book> db = new ArrayList<>();

    @Override
    public void create(Book book) {
        db.add(book);
    }

    @Override
    public Optional<Book> read(String id) {
        return db.stream().filter(i -> i.getIsbn().equals(id)).findAny();
    }

    @Override
    public void update(Book book) {
        db = toList(db.stream().filter(i -> i.equals(book)).map(i -> book));
    }

    @Override
    public void delete(String id) {
        db = toList(db.stream().filter(i -> !i.getIsbn().equals(id)));
    }

    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

}

