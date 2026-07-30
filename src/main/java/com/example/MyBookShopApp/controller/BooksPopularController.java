package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksRatingAndPopulatityService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BooksPopularController {

  private final BookService bookService;
  private final BooksRatingAndPopulatityService booksRatingAndPopulatityService;

  public BooksPopularController(BookService bookService,
      BooksRatingAndPopulatityService booksRatingAndPopulatityService) {
    this.bookService = bookService;
    this.booksRatingAndPopulatityService = booksRatingAndPopulatityService;
  }

  @ModelAttribute("booksList")
  public List<BookEntity> bookList() {
    return booksRatingAndPopulatityService.getPageOfPopularBooks(0, 5).getContent();
  }

  @GetMapping("/books/popular_page")
  public String booksPopularPage() {
    return "books/popular_page";
  }

}
