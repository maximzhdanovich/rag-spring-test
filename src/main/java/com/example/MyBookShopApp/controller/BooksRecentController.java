package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BooksRecentController {

  private final BookService bookService;

  public BooksRecentController(BookService bookService) {
    this.bookService = bookService;
  }


  @ModelAttribute("newBooks")
  public List<BookEntity> newBooks() {
    return bookService.getPageOfNewBooks(0, 20).getContent();
  }

  @GetMapping("/books/recent_page")
  public String booksRecentPage() {
    return "books/recent_page";
  }

}
