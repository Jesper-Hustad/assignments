package com.example.rest_db;

import com.example.rest_db.model.Book;
import com.example.rest_db.web.LibraryController;
import org.assertj.core.api.Assert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
//import java.net.http.HttpHeaders;
//import java.net.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private LibraryController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void controllerLoads() {
        assertThat(controller).isNotNull();
    }

//    @Test
//    void createAndReadFunctionality() throws URISyntaxException, JSONException {
////        String baseUrl = "http://localhost:" + "3306" + "/";
////        String createBookUrl = baseUrl + "books/";
////
////        URI uri = new URI(createBookUrl);
////
////        Book testBook = new Book();
////        testBook.setBookId(5213);
////        testBook.setTitle("A fancy test book");
////        testBook.setAuthors(Set.of());
////
////        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
////
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        ResponseEntity<String> result = restTemplate.postForEntity(uri, testBook, String.class);
////
////        //Verify request succeed
////        assertThat(201).isEqualTo(result.getStatusCodeValue());
////
////        Book getBookResult = restTemplate.getForObject(uri, Book.class);
////
////        assertThat(testBook).isEqualTo(getBookResult);
//
//    }


}
