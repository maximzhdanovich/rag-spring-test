package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.AuthorEntity;
import com.example.MyBookShopApp.data.AuthorNameDto;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.TagNameDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(description = "authors data")
public class AuthorsController {

  private final AuthorService authorService;
  private final BookService bookService;

  @Autowired
  public AuthorsController(AuthorService authorService,
      BookService bookService) {
    this.authorService = authorService;
    this.bookService = bookService;
  }

  @ModelAttribute("authorsMap")
  public Map<String, List<AuthorEntity>> authorsMap() {
    return authorService.getAuthorsMap();
  }

  @GetMapping(value = {"/authors/{authorId}"})
  public String getAuthorPage(@PathVariable(value = "authorId", required = false)
      Integer authorId, Model model) {

    AuthorEntity authorEntity = authorService.getAuthorById(authorId);
    model.addAttribute("authorEntity", authorEntity);
    model.addAttribute("booksList",
        bookService.getPageOfBooksByAuthorId(authorId, 0, 6).getContent());
    return "authors/slug";
  }

  @GetMapping("/books/author_page/{authorId}")
  public String getBooksPageByAuthorId(@PathVariable(value = "authorId", required = false)
      Integer authorId, Model model) {
    AuthorEntity authorEntity = authorService.getAuthorById(authorId);
    model.addAttribute("authorEntity", authorEntity);
    model.addAttribute("booksList",
        bookService.getPageOfBooksByAuthorId(authorId, 0, 6).getContent());

    return "books/author";
  }

  @GetMapping("/books/author/{authorId}")
  @ResponseBody
  public BooksPageDto getBookPageByAuthor(@PathVariable(value = "authorId", required = false)
      Integer authorId,
      @RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit) {
    return new BooksPageDto(
        bookService.getPageOfBooksByAuthorId(authorId, offset, limit).getContent());
  }

  @GetMapping("/authors")
  public String authorsPage() {
    return "authors/index";
  }

  @ApiOperation("method to get map of authors")
  @GetMapping("/api/authors")
  @ResponseBody // аннотация возвращает ответ напрямую данными, а не через шаблон Thymeleaf
  public Map<String, List<AuthorEntity>> authors() {
    return authorService.getAuthorsMap();
  }

}
