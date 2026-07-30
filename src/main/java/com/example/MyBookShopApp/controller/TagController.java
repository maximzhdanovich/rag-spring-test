package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.TagNameDto;
import com.example.MyBookShopApp.data.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TagController {

  private final BookService bookService;
  private final TagService tagService;

  public TagController(BookService bookService, TagService tagService) {
    this.bookService = bookService;
    this.tagService = tagService;
  }

  @GetMapping(value = {"/tags", "/tags/{tagName}"})
  public String getBooksByTag(@PathVariable(value = "tagName", required = false)
      TagNameDto tagNameDto, Model model){
    model.addAttribute("tagNameDto", tagNameDto);
    Integer tagId = Integer.parseInt(tagNameDto.getExample());
    String tagTitle = tagService.getTagById(tagId).getName();
    model.addAttribute("tagTitle", tagTitle);
    model.addAttribute("booksList",
        bookService.getPageOfBooksByTagId(tagId, 0,5).getContent());
    return "tags/index";
  }

  @GetMapping("/books/tag/{tagName}")
  @ResponseBody
  public BooksPageDto getNextTagsPage(@RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit,
      @PathVariable(value = "tagName", required = false) TagNameDto tagNameDto){
    return new BooksPageDto(bookService
        .getPageOfBooksByTagId(Integer.parseInt(tagNameDto.getExample()),offset,limit).getContent());
  }

}
