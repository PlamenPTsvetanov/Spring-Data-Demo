package com.spring.demo.repositories;

import com.spring.demo.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByFirstNameEndingWith(String ending);

    @Query
            ("Select a from Author as a")
    List<Author> getAllAuthors();

}
