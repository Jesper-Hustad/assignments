package no.ntnu.idatt2105.l4.demo.model;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Book {

    private String isbn;
    private String title;

    //  Many-to-many
    private Set<String> authors;
    private ArrayList<Author> authors_list = null;


    public Book(String isbn, String title, Set<String> authors) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
    }

    //  Create copy constructor
    public Book(Book book){
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.authors = book.getAuthors();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public ArrayList<Author> getAuthors_list() { return authors_list; }

    public void setAuthors_list(ArrayList<Author> authors_list) { this.authors_list = authors_list; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
