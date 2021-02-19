package no.ntnu.idatt2105.l4.demo.model;

import java.util.ArrayList;
import java.util.Set;

public class Author {

    private String name;
    private Address address;

    //  Many-to-many
    private Set<String> books;
    private ArrayList<Book> books_list = null;

    public Author(String name, Address address, Set<String> books) {
        this.name = name;
        this.address = address;
        this.books = books;
    }

    //  Create copy constructor
    public Author(Author author){
        this.name = author.getName();
        this.address = author.getAddress();
        this.books = author.getBooks();
    }

    public String getId() { return name; }

    public void setId(String id) { this.name = name; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public Set<String> getBooks() { return books; }

    public void setBooks(Set<String> books) { this.books = books; }

    public ArrayList<Book> getBooks_list() { return books_list; }

    public void setBooks_list(ArrayList<Book> books_list) { this.books_list = books_list; }
}
