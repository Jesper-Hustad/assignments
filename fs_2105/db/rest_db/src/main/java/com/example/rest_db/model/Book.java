package com.example.rest_db.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns =  @JoinColumn(name = "book_id")
    )
    Set<Author> authors;

    public Integer getBookId() {
        return this.bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
