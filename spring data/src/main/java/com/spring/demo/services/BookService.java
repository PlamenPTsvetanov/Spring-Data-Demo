package com.spring.demo.services;


import com.spring.demo.entities.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> getAllBooksByAgeRestriction(String ageRestriction);

    List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies);

    List<Book> getAllBooksNotReleasedInYear(String year);

    List<Book> getAllBooksWithPriceGreaterThanOrLesserThan(double greater, double lesser);

    List<Book> getAllBooksReleasedBefore(String date);

    List<Book> getAllBooksContainingString(String containing);

    List<Book> getAllByAuthorLastNameStartsWith(String containing);

    List<Book> getAllBooksWithLengthGreaterThan(int length);

    List<Book> getBookByTitle(String title);
}
