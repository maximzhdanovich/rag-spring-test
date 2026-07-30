package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.GenreNameDto;
import com.example.MyBookShopApp.data.GenreObject;
import com.example.MyBookShopApp.data.GenreService;
import com.example.MyBookShopApp.data.TagNameDto;
import com.example.MyBookShopApp.data.TagService;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenresController {

  private final BookService bookService;
  private final GenreService genreService;

  public GenresController(BookService bookService,
      GenreService genreService) {
    this.bookService = bookService;
    this.genreService = genreService;
  }

  public void findChildren(List<GenreObject> parents, List<GenreEntity> genreEntities) {

    for (int i = 0; i < parents.size(); i++) {
      GenreObject genre = parents.get(i);
      List<GenreObject> children = new ArrayList<>();

      for (int j = 0; j < genreEntities.size(); j++) {
        GenreEntity genreEntity = genreEntities.get(j);
        if (genreEntity.getParentId() == genre.getId()) {
          GenreObject child = new GenreObject(genreEntity.getName(), genreEntity.getId());
          child.setCount(genreEntity.getBookList().size());
          children.add(child);
          genreEntities.remove(j);
          j--;
        }
      }
      if (children != null) {
        findChildren(children, genreEntities);
        genre.setChildren(children);
        parents.remove(i);
        parents.add(i, genre);
      }
    }
  }


  @ModelAttribute("genresList")
  public List<GenreObject> getGenreList() {
    List<GenreEntity> genreEntities = genreService.getGenres();
    List<GenreObject> parents = new ArrayList<>();

    for (int i = 0; i < genreEntities.size(); i++) {
      GenreEntity genreEntity = genreEntities.get(i);
      if (genreEntity.getParentId() == 0) {
        GenreObject genre = new GenreObject(genreEntity.getName(), genreEntity.getId());
        genre.setCount(genreEntity.getBookList().size());
        parents.add(genre);
        genreEntities.remove(i);
        i--;
      }
    }

    findChildren(parents, genreEntities);

    return parents;
  }

  @GetMapping(value = {"/genres/{genreName}"})
  public String getBooksByTag(@PathVariable(value = "genreName", required = false)
      GenreNameDto genreNameDto, Model model) {
    model.addAttribute("genreNameDto", genreNameDto);
    Integer genreId = Integer.parseInt(genreNameDto.getExample());
    String genreTitle = genreService.getGenreById(genreId).getName();
    model.addAttribute("genreTitle", genreTitle);
    model.addAttribute("booksList",
        bookService.getPageOfBooksByGenreId(genreId, 0, 5).getContent());
    return "genres/slug";
  }

  @GetMapping("/books/genre/{genreName}")
  @ResponseBody
  public BooksPageDto getNextGenrePage(@RequestParam("offset") Integer offset,
      @RequestParam("limit") Integer limit,
      @PathVariable(value = "genreName", required = false) GenreNameDto genreNameDto) {
    return new BooksPageDto(bookService
        .getPageOfBooksByGenreId(Integer.parseInt(genreNameDto.getExample()), offset, limit)
        .getContent());
  }

  @GetMapping("/genres")
  public String genresPage() {
    return "genres/index";
  }


}
