package no.ntnu.idatt2105.l4.demo.model;

public class Address {
    private String street;
    private String place;

    public String getStreet() {
        return street;
    }

    public Address(String street, String place) {
        this.street = street;
        this.place = place;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
