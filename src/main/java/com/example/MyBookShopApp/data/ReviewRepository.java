package com.example.MyBookShopApp.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

  List<ReviewEntity> findReviewEntitiesByBookId(Integer bookId);

  ReviewEntity findReviewEntityById(Integer reviewId);

}
