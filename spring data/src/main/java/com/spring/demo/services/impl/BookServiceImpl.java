package com.spring.demo.services.impl;

import com.spring.demo.entities.*;
import com.spring.demo.repositories.BookRepository;
import com.spring.demo.services.AuthorService;
import com.spring.demo.services.BookService;
import com.spring.demo.services.CategoryService;
import com.spring.demo.utils.FileUtil;
import com.spring.demo.constants.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final FileUtil fileUtil;
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(FileUtil fileUtil, BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.fileUtil = fileUtil;
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }
        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.BOOKS_FILE_PATH);

        Arrays.stream(fileContent).map(r -> r.split("\\s+")).forEach(params -> {
            Author author = this.getRandomAuthor();
            EditionType editionType = EditionType.values()[Integer.parseInt(params[0])];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate releaseDate = LocalDate.parse(params[1], formatter);
            int copies = Integer.parseInt(params[2]);
            BigDecimal price = new BigDecimal(params[3]);
            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(params[4])];
            String title = this.getTitle(params);
            Set<Category> categories = this.getRandomCategories();

            Book book = new Book(title, null, editionType, price, copies, releaseDate, ageRestriction, categories, author);
            this.bookRepository.saveAndFlush(book);
        });
    }

    @Override
    public List<Book> getAllBooksByAgeRestriction(String ageRestriction) {
        return this.bookRepository.
                findAllByAgeRestriction(AgeRestriction.valueOf(ageRestriction.toUpperCase()));
    }

    @Override
    public List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies) {
        return this.bookRepository
                .findAllByEditionTypeAndCopiesLessThan(EditionType.valueOf(editionType.toUpperCase()), copies);
    }

    @Override
    public List<Book> getAllBooksNotReleasedInYear(String year) {
        LocalDate date = LocalDate.of(Integer.parseInt(year), 1, 1);
        LocalDate date_after = LocalDate.of(Integer.parseInt(year), 12, 31);

        return this.bookRepository.findAllByReleaseDateIsAfterOrReleaseDateIsBefore(date_after, date);
    }

    @Override
    public List<Book> getAllBooksWithPriceGreaterThanOrLesserThan(double greater, double lesser) {
        BigDecimal firstPrice = BigDecimal.valueOf(greater);
        BigDecimal secondPrice = BigDecimal.valueOf(lesser);

        return this.bookRepository.findAllByPriceGreaterThanOrPriceLessThan(firstPrice, secondPrice);
    }

    @Override
    public List<Book> getAllBooksReleasedBefore(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate releaseDate = LocalDate.parse(date, formatter);
        return this.bookRepository.findAllByReleaseDateBefore(releaseDate);
    }

    @Override
    public List<Book> getAllBooksContainingString(String containing) {
        return this.bookRepository.findAllByTitleContaining(containing.toLowerCase());
    }

    @Override
    public List<Book> getAllByAuthorLastNameStartsWith(String containing) {
        return this.bookRepository.findAllByAuthorLastNameStartsWith(containing.toLowerCase());
    }

    @Override
    public List<Book> getAllBooksWithLengthGreaterThan(int length) {
        return this.bookRepository.findAllByTitleLengthGreaterThan(length);
    }

    @Override
    public List<Book> getBookByTitle(String title) {
        return this.bookRepository.findAllByTitleEquals(title);
    }

    private Set<Category> getRandomCategories() {
        Set<Category> result = new HashSet<>();
        Random random = new Random();
        int bound = random.nextInt(3) + 1;


        for (int i = 1; i <= bound; i++) {
            int categoryId = random.nextInt(8) + 1;
            result.add(this.categoryService.getCategoryById((long) categoryId));
        }
        return result;
    }

    private String getTitle(String[] params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 5; i < params.length; i++) {
            sb.append(params[i]).append(" ");
        }
        return sb.toString().trim();
    }

    private Author getRandomAuthor() {
        Random random = new Random();
        int randomId = random.nextInt(this.authorService.getAllAuthorsCount()) + 1;

        return this.authorService.findAuthorById((long) randomId);
    }
}
