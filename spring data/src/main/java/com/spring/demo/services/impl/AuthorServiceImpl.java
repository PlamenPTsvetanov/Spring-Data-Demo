package com.spring.demo.services.impl;


import com.spring.demo.entities.Author;
import com.spring.demo.repositories.AuthorRepository;
import com.spring.demo.services.AuthorService;
import com.spring.demo.utils.FileUtil;
import com.spring.demo.constants.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0) {
            return;
        }
        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.AUTHORS_FILE_PATH);
        Arrays.stream(fileContent)
                .forEach(r -> {
                    String[] params = r.split("\\s+");
                    Author author = new Author(params[0], params[1]);

                    this.authorRepository.saveAndFlush(author);
                });
    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(Long id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public List<Author> getAuthorsWithNameEndingWith(String ending) {
        return this.authorRepository.findAllByFirstNameEndingWith(ending);
    }

    @Override
    public List<Author> getAllAuthors() {
       return this.authorRepository.getAllAuthors();
    }
}
