package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController {

  private final BookService bookService;

  @Autowired
  public BooksRestApiController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/books/by-author")
  @ApiOperation("get book list of bookshop by passed author name")
  public ResponseEntity<List<BookEntity>> booksByAuthor(@RequestParam("author") String authorName) {
    return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
  }

  @GetMapping("/books/by-title")
  @ApiOperation("get books by book title")
  public ResponseEntity<ApiResponse<BookEntity>> booksByTitle(@RequestParam("title") String title)
      throws BookstoreApiWrongParameterException {
    ApiResponse<BookEntity> response = new ApiResponse<>();
    List<BookEntity> data = bookService.getBooksByTitle(title);
    response.setDebugMessage("successful request");
    response.setMessage("data size: " + data.size() + " elements");
    response.setStatus(HttpStatus.OK);
    response.setTimeStamp(LocalDateTime.now());
    response.setData(data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/books/by-price-range")
  @ApiOperation("get books by price range from minimum to maximum")
  public ResponseEntity<List<BookEntity>> priceRangeBooks(@RequestParam("min") Integer min,
      @RequestParam("max") Integer max) {
    return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
  }

  @GetMapping("/books/with-max-discount")
  @ApiOperation("get list of books with max discount")
  public ResponseEntity<List<BookEntity>> maxDiscountBooks() {
    return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
  }

  @GetMapping("/books/bestsellers")
  @ApiOperation("get bestseller books (which is_bestseller = 1)")
  public ResponseEntity<List<BookEntity>> bestsellerBooks() {
    return ResponseEntity.ok(bookService.getBestsellers());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponse<BookEntity>> handleMissingServletRequestParameterException(
      Exception exception) {
    return new ResponseEntity<>(new ApiResponse<BookEntity>(HttpStatus.BAD_REQUEST,
        "Missing required parameters", exception), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BookstoreApiWrongParameterException.class)
  public ResponseEntity<ApiResponse<BookEntity>> handleBookstoreApiWrongParameterException(
      Exception exception) {
    return new ResponseEntity<>(new ApiResponse<BookEntity>(HttpStatus.BAD_REQUEST,
        "Bad parameter value...", exception), HttpStatus.BAD_REQUEST);
  }

}
