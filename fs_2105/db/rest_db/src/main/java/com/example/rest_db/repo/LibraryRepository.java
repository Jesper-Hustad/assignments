package com.example.rest_db.repo;



import com.example.rest_db.model.Author;
import com.example.rest_db.model.Book;

import java.util.Optional;

public interface LibraryRepository {

//  author

    public void authorCreate(Author author);

    public Optional<Author> authorRead(Integer id);

    public void authorUpdate(Author author);

    public void authorDelete(Integer id);


//  book

    public void bookCreate(Book book);

    public Optional<Book> bookRead(Integer id);

    public void bookUpdate(Book book);

    public void bookDelete(Integer id);

}
