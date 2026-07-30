package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.BooksRatingAndPopulatityService;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.TagEntity;
import com.example.MyBookShopApp.data.TagNameDto;
import com.example.MyBookShopApp.data.TagService;
import com.example.MyBookShopApp.errs.EmptySearchException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class MainPageController {

  private final BookService bookService;
  private final TagService tagService;
  private final BooksRatingAndPopulatityService booksRatingAndPopulatityService;
 // private final AuthorService authorService;

  @Autowired
  public MainPageController(BookService bookService,
      TagService tagService,
      BooksRatingAndPopulatityService booksRatingAndPopulatityService) {
    this.bookService = bookService;
    this.tagService = tagService;
    this.booksRatingAndPopulatityService = booksRatingAndPopulatityService;
  }

  @ModelAttribute("recommendedBooks")
  public List<BookEntity> recommendedBooks(){
    return bookService.getPageOfRecommendedBooks(0, 6).getContent();
  }

  @ModelAttribute("tagsList")
  public Map<TagEntity, String> getTagsList(){
    List<TagEntity> tags = tagService.getTags();
    Map<TagEntity, String> tagsMap = new HashMap<>();
    for (TagEntity tag: tags){
      int count = bookService.getBooksByTag(tag.getId()).size();
      String fontSize = "";
      if (count <= 10){
        fontSize = "Tag Tag_xs";
      }
      if (count > 10 && count <= 30){
        fontSize = "Tag Tag_sm";
      }
      if (count > 30 && count<= 100){
        fontSize = "Tag Tag_md";
      }
      if (count > 100){
        fontSize = "Tag Tag_lg";
      }
      tagsMap.put(tag, fontSize);
    }
    return tagsMap;
  }

  @ModelAttribute("newBooks")
  public List<BookEntity> newBooks(){
    return bookService.getPageOfNewBooks(0, 6).getContent();
  }

  @ModelAttribute("popularBooks")
  public List<BookEntity> popularBooks(){
    return booksRatingAndPopulatityService.getPageOfPopularBooks(0, 6).getContent();
  }

  @ModelAttribute("tagNameDto")
  public TagNameDto tagNameDto(){
    return new TagNameDto();
  }

  @ModelAttribute("searchWordDto")
  public SearchWordDto searchWordDto(){
    return new SearchWordDto();
  }

  @ModelAttribute("searchResults")
  public List<BookEntity> searchResults(){
    return new ArrayList<>();
  }

  @GetMapping("/books/recommended")
  @ResponseBody
  public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit){
    return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
  }

  @GetMapping("/books/popular")
  @ResponseBody
  public BooksPageDto getBooksPopularPage(@RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit){
    return new BooksPageDto(booksRatingAndPopulatityService.getPageOfPopularBooks(offset, limit).getContent());
  }

  @GetMapping("/books/recent")
  @ResponseBody
  public BooksPageDto getBooksRecentPage(@RequestParam(value = "from", required = false) String from,
      @RequestParam(value = "to", required = false) String to,
      @RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit){
    if (from==null || to==null) {
      return new BooksPageDto(bookService.getPageOfNewBooks(offset, limit).getContent());
    } else {
      return new BooksPageDto(bookService.getPageOfNewBooksFromTo(from, to, offset, limit).getContent());
    }

  }


  @GetMapping(value = {"/search", "/search/{searchWord}"})
  public String getSearchResults(@PathVariable(value = "searchWord", required = false)
      SearchWordDto searchWordDto, Model model) throws EmptySearchException {
    if (searchWordDto != null) {
      model.addAttribute("searchWordDto", searchWordDto);
      model.addAttribute("searchResults",
          bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
//          bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
      return "search/index";
    } else {
      throw new EmptySearchException("Поиск по null невозможен");
    }
  }

  @GetMapping("/search/page/{searchWord}")
  @ResponseBody
  public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit,
      @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto){
    return new BooksPageDto(bookService
        .getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(),offset,limit));
  }

  @GetMapping("/")
  public String mainPage(){
    return "index";
  }

}
