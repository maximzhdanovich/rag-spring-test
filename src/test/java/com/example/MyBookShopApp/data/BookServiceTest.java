package com.example.MyBookShopApp.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {

  private final BookService bookService;

  @Autowired
  public BookServiceTest(BookService bookService) {
    this.bookService = bookService;
  }

  @Test
  void getPageOfRecommendedBooks() {
    assertNotNull(bookService.getPageOfRecommendedBooks(0, 6));
  }
}