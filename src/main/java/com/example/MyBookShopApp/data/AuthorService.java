package com.example.MyBookShopApp.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  @Autowired
  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Map<String, List<AuthorEntity>> getAuthorsMap() {

    List<AuthorEntity> authors = authorRepository.findAll();


    return authors.stream().collect(Collectors.groupingBy((AuthorEntity a) -> {
      return a.getName().substring(0,1);
    }));
  }

  public AuthorEntity getAuthorById(Integer authorId) {
    return authorRepository.findAuthorEntityById(authorId);
  }
}
