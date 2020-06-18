package com.spring.demo.controllers;


import com.spring.demo.entities.Author;
import com.spring.demo.entities.Book;
import com.spring.demo.services.AuthorService;
import com.spring.demo.services.BookService;
import com.spring.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;

// CommandLineRunner implementation, used as a base for an application controller
@Controller
public class AppController implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader reader;

    @Autowired
    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader reader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.reader = reader;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        //1.	Books Titles by Age Restriction
        // Filtering books by age registration
        System.out.println("Enter age restriction");
        this.bookService.getAllBooksByAgeRestriction(this.reader.readLine())
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);

        //2.	Golden Books
        System.out.println("Enter edition type:");
        String editionType = reader.readLine();
        System.out.println("Enter copies:");
        int copies = Integer.parseInt(reader.readLine());
        this.bookService.getAllBooksByEditionTypeAndCopies(editionType, copies)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);

        //3.	Books by Price
        // Filtering books by price limits
        System.out.println("Enter maximal price:");
        int maxPrice = Integer.parseInt(reader.readLine());
        System.out.println("Enter minimal price:");
        int minPrice = Integer.parseInt(reader.readLine());
        this.bookService.getAllBooksWithPriceGreaterThanOrLesserThan(maxPrice, minPrice)
                .forEach(b -> {
                    System.out.printf("%s - $%.2f%n", b.getTitle(), b.getPrice());
                });

        //4.	Not Released Books
        // Get all books which are not released in certain year
        System.out.println("Enter desired year");
        this.bookService.getAllBooksNotReleasedInYear(this.reader.readLine())
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);

        //5.	Books Released Before Date
        // Get all books which are not released in certain year
        System.out.println("Enter date:");
        this.bookService.getAllBooksReleasedBefore(this.reader.readLine())
                .forEach(b -> {
                    System.out.printf("%s %s %.2f%n", b.getTitle(), b.getEditionType(), b.getPrice());
                });

        //6.	Authors Search
        // Search authors by ending of name
        System.out.println("Enter name ending:");
        String ending = this.reader.readLine();
        this.authorService.getAuthorsWithNameEndingWith(ending)
                .forEach(a -> {
                    System.out.printf("%s %s%n", a.getFirstName(), a.getLastName());
                });
        if (this.authorService.getAuthorsWithNameEndingWith(ending).isEmpty()){
            System.out.println("No such names");
        }

        //7.	Books Search
        System.out.println("Enter desired string");
        String cont = this.reader.readLine();
        this.bookService.getAllBooksContainingString(cont)
                .forEach(b -> {
                    System.out.printf("%s%n", b.getTitle());
                });
        if (this.bookService.getAllBooksContainingString(cont).isEmpty()) {
            System.out.println("No such books");
        }

        //8.	Book Titles Search
        System.out.println("Enter desired string");
        String letters = this.reader.readLine();
        this.bookService.getAllByAuthorLastNameStartsWith(letters)
                .forEach(book -> {
                    System.out.printf("%s (%s %s)%n", book.getTitle(), book.getAuthor().getFirstName(), book.getAuthor().getLastName());
                });
        if (this.bookService.getAllByAuthorLastNameStartsWith(cont).isEmpty()) {
            System.out.println("No such authors");
        }
        //9.	Count Books
        System.out.println("Enter desired length");
        System.out.println(this.bookService.getAllBooksWithLengthGreaterThan(Integer.parseInt(this.reader.readLine()))
                .size());

        //10.	Total Book Copies
        Author best = null;
        for (Author author : this.authorService.getAllAuthors()) {
            if (author.getId() == 1) {
                best = author;
            }

            if (author.findCopiesCount() > best.findCopiesCount()) {
                best = author;
            }
        }
        System.out.printf("%s %s - %d%n", best.getFirstName(), best.getLastName(), best.findCopiesCount());

        //11.	Reduced Book
        System.out.println("Enter name");
        this.bookService.getBookByTitle(this.reader.readLine().trim())
                .forEach(book -> {
                    System.out.printf("%s %s %s %.2f%n", book.getTitle(), book.getEditionType(), book.getAgeRestriction(),
                            book.getPrice());
                });
    }
}
