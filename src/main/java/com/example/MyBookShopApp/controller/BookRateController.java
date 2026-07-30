package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.RatingEntity;
import com.example.MyBookShopApp.data.RatingRepository;
import com.example.MyBookShopApp.data.ReviewEntity;
import com.example.MyBookShopApp.data.ReviewRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BookRateController {

  private final RatingRepository ratingRepository;
  private final ReviewRepository reviewRepository;

  @Autowired
  public BookRateController(RatingRepository ratingRepository,
      ReviewRepository reviewRepository) {
    this.ratingRepository = ratingRepository;
    this.reviewRepository = reviewRepository;
  }


  public void increaseRating(RatingEntity ratingEntity, int rating) {
    switch (rating) {
      case 1:
        ratingEntity.setOneStar(ratingEntity.getOneStar() + 1);
        break;
      case 2:
        ratingEntity.setTwoStar(ratingEntity.getTwoStar() + 1);
        break;
      case 3:
        ratingEntity.setThreeStar(ratingEntity.getThreeStar() + 1);
        break;
      case 4:
        ratingEntity.setFourStar(ratingEntity.getFourStar() + 1);
        break;
      case 5:
        ratingEntity.setFiveStar(ratingEntity.getFiveStar() + 1);
        break;
    }
  }

  @PostMapping("/rateBookReview")
  public String rateBookReview(@RequestBody Map<String, String> json) {
    int reviewId = Integer.parseInt(json.get("reviewid"));
    int value = Integer.parseInt(json.get("value"));

    ReviewEntity reviewEntity = reviewRepository.findReviewEntityById(reviewId);
    if (value == 1) {
      reviewEntity.setLikes(reviewEntity.getLikes() + 1);
    } else {
      reviewEntity.setDislike(reviewEntity.getDislike() + 1);
    }
    reviewRepository.save(reviewEntity);
    return "books/slug";
  }


  @PostMapping("/bookReview")
  public String reviewBook(@RequestBody Map<String, String> json) {
    int bookId = Integer.parseInt(json.get("bookId"));
    String text = json.get("text");

    ReviewEntity reviewEntity = new ReviewEntity();
    reviewEntity.setBookId(bookId);
    reviewEntity.setText(text);
    reviewEntity.setTime(LocalDateTime.now());
    reviewEntity.setLikes(0);
    reviewEntity.setDislike(0);
    reviewRepository.save(reviewEntity);
    return "books/slug";
  }


  @PostMapping("/rateBook")
  public String rateBook(@RequestBody Map<String, String> json, Model model) {
    int bookId = Integer.parseInt(json.get("bookId"));
    int value = Integer.parseInt(json.get("value"));

    RatingEntity ratingEntity = ratingRepository.findRatingEntityByBookId(bookId);
    if (ratingEntity != null) {

      increaseRating(ratingEntity, value);
      ratingRepository.save(ratingEntity);

    } else {

      ratingEntity = new RatingEntity(0, 0, 0, 0, 0);
      ratingEntity.setBookId(bookId);
      increaseRating(ratingEntity, value);
      ratingRepository.save(ratingEntity);
    }

    return "books/slug";

  }

  @GetMapping("/slugmy")
  public String getSlugMy() {
    return "books/slugmy";
  }
}
