package com.example.MyBookShopApp.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.print.Book;
import java.util.List;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTests {

  private final BookRepository bookRepository;

  @Autowired
  public BookRepositoryTests(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Test
  void findBookEntitiesByAuthorNameContaining() {
    String token = "Carter";
    List<BookEntity> bookListByAuthorName = bookRepository.findBookEntitiesByAuthorNameContaining(token);

    assertNotNull(bookListByAuthorName);
    assertFalse(bookListByAuthorName.isEmpty());

    for (BookEntity bookEntity : bookListByAuthorName){
      Logger.getLogger(this.getClass().getSimpleName()).info(bookEntity.getTitle() +
          " - " + bookEntity.authorsFullName());
      assertThat(bookEntity.getAuthor().getName().contains(token));
    }
  }

  @Test
  void findBookEntitiesByTitleContaining() {
    String token = "Love";
    List<BookEntity> bookListByTitleContaining = bookRepository.findBookEntitiesByTitleContaining(token);

    assertNotNull(bookListByTitleContaining);
    assertFalse(bookListByTitleContaining.isEmpty());

    for (BookEntity bookEntity : bookListByTitleContaining){
      Logger.getLogger(this.getClass().getSimpleName()).info(bookEntity.getTitle() +
          " - " + bookEntity.authorsFullName());
      assertThat(bookEntity.getTitle().contains(token));
    }
  }

  @Test
  void getBestsellers() {
    List<BookEntity> bestSellers = bookRepository.getBestsellers();
    assertNotNull(bestSellers);
    assertFalse(bestSellers.isEmpty());
    assertThat(bestSellers.size()).isGreaterThan(1);
  }
}