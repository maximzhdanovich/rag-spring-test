package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class PostponedController {

  private final BookRepository bookRepository;

  @ModelAttribute(name = "booksPostponed")
  public List<BookEntity> booksPostponed() {
    return new ArrayList<>();
  }

  @Autowired
  public PostponedController(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @GetMapping("/postponed")
  public String handleCartRequest(
      @CookieValue(value = "postponeContents", required = false) String postponeContents,
      Model model) {
    if (postponeContents == null || postponeContents.equals("")) {
      model.addAttribute("isPostponeEmpty", true);
    } else {
      model.addAttribute("isPostponeEmpty", false);
      postponeContents =
          postponeContents.startsWith("/") ? postponeContents.substring(1) : postponeContents;
      postponeContents = postponeContents.endsWith("/") ? postponeContents
          .substring(0, postponeContents.length() - 1) : postponeContents;
      String[] cookieSlugs = postponeContents.split("/");
      List<BookEntity> booksFromCookieSlugs = bookRepository.findBookEntitiesBySlugIn(cookieSlugs);
      model.addAttribute("booksPostponed", booksFromCookieSlugs);
    }
    return "postponed";
  }

  @PostMapping("/changeBookStatus/postpone/remove/{slug}")
  public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
      @CookieValue(name = "postponeContents", required = false) String postponeContents,
      HttpServletResponse response, Model model) {
    if (postponeContents != null && !postponeContents.equals("")) {
      ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
      cookieBooks.remove(slug);
      Cookie cookie = new Cookie("postponeContents", String.join("/", cookieBooks));
      cookie.setPath("/books");
      response.addCookie(cookie);
      model.addAttribute("isPostponeEmpty", false);

      BookEntity book = bookRepository.findBookEntityBySlug(slug);
      book.setkPopularity(book.getkPopularity() - 1);
      bookRepository.save(book);
    } else {
      model.addAttribute("isPostponeEmpty", true);
    }
    return "redirect:/books/postponed";
  }

  @PostMapping("/changeBookStatus/postpone/{slug}")
  public String handleBookPostpone(@PathVariable("slug") String slug,
      @CookieValue(name = "postponeContents", required = false) String postponeContents,
      HttpServletResponse response, Model model) {

    if (postponeContents == null || postponeContents.equals("")) {
      Cookie cookie = new Cookie("postponeContents", slug);
      cookie.setPath("/books");
      response.addCookie(cookie);
      model.addAttribute("isPostponeEmpty", false);

      BookEntity book = bookRepository.findBookEntityBySlug(slug);
      book.setkPopularity(book.getkPopularity() + 1);
      bookRepository.save(book);
    } else if (!postponeContents.contains(slug)) {
      StringJoiner stringJoiner = new StringJoiner("/");
      stringJoiner.add(postponeContents).add(slug);
      Cookie cookie = new Cookie("postponeContents", stringJoiner.toString());
      cookie.setPath("/books");
      response.addCookie(cookie);
      model.addAttribute("isPostponeEmpty", false);

      BookEntity book = bookRepository.findBookEntityBySlug(slug);
      book.setkPopularity(book.getkPopularity() + 1);
      bookRepository.save(book);
    }
    return "redirect:/books/" + slug;

  }

}
