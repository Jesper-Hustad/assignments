package com.example.rest_db.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Integer address_id;

    @OneToOne(mappedBy = "address")
    private Author author;

    @Column(name = "street")
    private String street;

    @Column(name = "place")
    private String place;

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
