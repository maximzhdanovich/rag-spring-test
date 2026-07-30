package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.BookEntity;
import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.data.RatingEntity;
import com.example.MyBookShopApp.data.RatingRepository;
import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.data.ReviewEntity;
import com.example.MyBookShopApp.data.ReviewRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/books")
public class BooksController {

  private final BookRepository bookRepository;
  private final RatingRepository ratingRepository;
  private final ResourceStorage storage;
  private final ReviewRepository reviewRepository;

  @Autowired
  public BooksController(BookRepository bookRepository,
      RatingRepository ratingRepository, ResourceStorage storage,
      ReviewRepository reviewRepository) {
    this.bookRepository = bookRepository;
    this.ratingRepository = ratingRepository;
    this.storage = storage;
    this.reviewRepository = reviewRepository;
  }

  public boolean isUserAuthenticated() {
    if (SecurityContextHolder.getContext().getAuthentication() != null &&
        SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
        //when Anonymous Authentication is enabled
        !(SecurityContextHolder.getContext().getAuthentication()
            instanceof AnonymousAuthenticationToken)) {

      return true;
    } else {
      return false;
    }
  }


  public int countRating(RatingEntity ratingEntity) {
    int ratingTotal = ((ratingEntity.getOneStar()) + (ratingEntity.getTwoStar() * 2) +
        (ratingEntity.getThreeStar() * 3) + (ratingEntity.getFourStar() * 4) +
        (ratingEntity.getFiveStar() * 5));
    double ratingCount = (ratingEntity.getOneStar() + ratingEntity.getTwoStar() +
        ratingEntity.getThreeStar() + ratingEntity.getFourStar() + ratingEntity.getFiveStar());

    return (int) Math.round(ratingTotal / ratingCount);

  }

  @GetMapping("/{slug}")
  public String bookPage(@PathVariable("slug") String slug, Model model) {
    BookEntity book = bookRepository.findBookEntityBySlug(slug);
    model.addAttribute("slugBook", book);

    RatingEntity ratingEntity = ratingRepository.findRatingEntityByBookId(book.getId());
    if (ratingEntity != null) {
      int rating = countRating(ratingEntity);

      ArrayList<String> starModel = new ArrayList<>();
      switch (rating) {
        case 0:
          starModel.addAll(Arrays.asList("Rating-star", "Rating-star",
              "Rating-star", "Rating-star", "Rating-star"));
          break;
        case 1:
          starModel.addAll(Arrays.asList("Rating-star Rating-star_view", "Rating-star",
              "Rating-star", "Rating-star", "Rating-star"));
          break;
        case 2:
          starModel
              .addAll(Arrays.asList("Rating-star Rating-star_view", "Rating-star Rating-star_view",
                  "Rating-star", "Rating-star", "Rating-star"));
          break;
        case 3:
          starModel
              .addAll(Arrays.asList("Rating-star Rating-star_view", "Rating-star Rating-star_view",
                  "Rating-star Rating-star_view", "Rating-star", "Rating-star"));
          break;
        case 4:
          starModel
              .addAll(Arrays.asList("Rating-star Rating-star_view", "Rating-star Rating-star_view",
                  "Rating-star Rating-star_view", "Rating-star Rating-star_view", "Rating-star"));
          break;
        case 5:
          starModel
              .addAll(Arrays.asList("Rating-star Rating-star_view", "Rating-star Rating-star_view",
                  "Rating-star Rating-star_view", "Rating-star Rating-star_view",
                  "Rating-star Rating-star_view"));
          break;
      }
      int ratingVotes =
          ratingEntity.getOneStar() + ratingEntity.getTwoStar() + ratingEntity.getThreeStar() +
              ratingEntity.getFourStar() + ratingEntity.getFiveStar();

      model.addAttribute("starModel", starModel);
      model.addAttribute("rating", rating);
      model.addAttribute("ratingVotes", ratingVotes);
      model.addAttribute("ratingEntity", ratingEntity);
    } else {

      ArrayList<String> starModel = new ArrayList<>(Arrays.asList("Rating-star", "Rating-star",
          "Rating-star", "Rating-star", "Rating-star"));
      int rating = 0;
      int ratingVotes = 0;
      ratingEntity = new RatingEntity(0, 0, 0, 0, 0);

      model.addAttribute("starModel", starModel);
      model.addAttribute("rating", rating);
      model.addAttribute("ratingVotes", ratingVotes);
      model.addAttribute("ratingEntity", ratingEntity);
    }

    List<ReviewEntity> reviewEntityList = reviewRepository.findReviewEntitiesByBookId(book.getId());
    model.addAttribute("reviewEntityList", reviewEntityList);

    if (isUserAuthenticated()) {
      model.addAttribute("unauthorizedUser", false);
    } else {
      model.addAttribute("unauthorizedUser", true);
    }

    return "books/slug";

  }

  @PostMapping("/{slug}/img/save")
  public String saveNewBookImage(@RequestParam("file") MultipartFile file,
      @PathVariable("slug") String slug)
      throws IOException {
    String savePath = storage.saveNewBookImage(file, slug);
    BookEntity bookToUpdate = bookRepository.findBookEntityBySlug(slug);
    bookToUpdate.setImage(savePath);
    bookRepository.save(bookToUpdate);   //save new path to db here

    return ("redirect:/books/" + slug);
  }

  @GetMapping("/download/{hash}")
  public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash)
      throws IOException {

    Path path = storage.getBookFilePath(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

    MediaType mediaType = storage.getBookFileMime(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

    byte[] data = storage.getBookFileByteArray(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file data lentgh: " + data.length);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=" + path.getFileName().toString())
        .contentType(mediaType)
        .contentLength(data.length)
        .body(new ByteArrayResource(data));
  }


}
