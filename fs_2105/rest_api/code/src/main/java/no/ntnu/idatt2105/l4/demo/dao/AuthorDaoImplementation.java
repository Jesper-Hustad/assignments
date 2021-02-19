package no.ntnu.idatt2105.l4.demo.dao;

import no.ntnu.idatt2105.l4.demo.model.Author;
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
public class AuthorDaoImplementation implements AuthorDao{

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    //  Simulated db
    private ArrayList<Author> db = new ArrayList<>();

    @Override
    public void create(Author author) {
        db.add(author);
    }

    @Override
    public Optional<Author> read(String id) {
        return db.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    @Override
    public void update(Author author) {
        db = toList(db.stream().filter(i -> i.equals(author)).map(i -> author));
    }

    @Override
    public void delete(String id) {
        db = toList(db.stream().filter(i -> !(i.getId().equals(id))));
    }

    public static <T> ArrayList<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}
