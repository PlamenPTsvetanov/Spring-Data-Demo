package com.spring.demo.services;

import com.spring.demo.entities.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(Long id);

    List<Author> getAuthorsWithNameEndingWith(String ending);

    List<Author> getAllAuthors();
}
