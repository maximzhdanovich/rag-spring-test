package com.example.MyBookShopApp.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class BooksRatingAndPopulatityServiceTest {

  private final BooksRatingAndPopulatityService booksRatingAndPopulatityService;

  @Autowired
  public BooksRatingAndPopulatityServiceTest(
      BooksRatingAndPopulatityService booksRatingAndPopulatityService) {
    this.booksRatingAndPopulatityService = booksRatingAndPopulatityService;
  }

  @Test
  void getPageOfPopularBooksTest(){
    ;
    Page<BookEntity> page = booksRatingAndPopulatityService.getPageOfPopularBooks(0, 6);
    List<BookEntity> list = page.getContent();

    double previousPopularity = Double.MAX_VALUE;

    for (BookEntity book : list){

      double currentPopularity = book.getbPopularity() + (0.7 * book.getcPopularity()) +
          (0.4 * book.getkPopularity());

      assertTrue(currentPopularity < previousPopularity);

      previousPopularity = currentPopularity;
    }

  }

}