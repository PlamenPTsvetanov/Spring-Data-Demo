package com.spring.demo.repositories;


import com.spring.demo.entities.AgeRestriction;
import com.spring.demo.entities.Book;
import com.spring.demo.entities.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, int copies);

    List<Book> findAllByReleaseDateIsAfterOrReleaseDateIsBefore(LocalDate year, LocalDate year1);

    List<Book> findAllByPriceGreaterThanOrPriceLessThan(BigDecimal greater, BigDecimal lesser);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDate);

    List<Book> findAllByTitleContaining(String containing);

    List<Book> findAllByAuthorLastNameStartsWith(String containing);

    @Query
            ("Select b from Book as b where length(b.title) > :length")
    List<Book> findAllByTitleLengthGreaterThan(int length);

    List<Book> findAllByTitleEquals(String title);

}
